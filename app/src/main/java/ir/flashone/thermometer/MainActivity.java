package ir.flashone.thermometer;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    AppCompatButton btn_Start;
    AppCompatTextView txt_Temperature_C;


    TcpClient mTcpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Define
        btn_Start = (AppCompatButton) findViewById(R.id.btn_Start);
        txt_Temperature_C = (AppCompatTextView) findViewById(R.id.txt_Temperature_C);

        //Manage Buttons
        btn_Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_Start.getText().equals("روشن")) {
                    btn_Start.setText("خاموش");
//                    disconnect();
                    connect();
                } else {
                    btn_Start.setText("روشن");
                    setDefaultTextOnTextView();
                    connect();
                }
            }
        });


        sendMassage();

    }


    //set Default Texts On TextViews
    private void setDefaultTextOnTextView() {
        txt_Temperature_C.setText("0 *C");
    }


    //manage Destroy Activity
    @Override
    protected void onDestroy() {
        btn_Start.setText("روشن");
        setDefaultTextOnTextView();
        super.onDestroy();
    }

    public void connect() {
        try {
           new ConnectTask(this).execute("");

//            mTcpClient.run();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void disconnect() {
        try {

//            mTcpClient.stopClient();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void sendMassage() {
        if (mTcpClient == null) {
            connect();

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        mTcpClient.sendMessage("");
    }

    public class ConnectTask extends AsyncTask<String, String, TcpClient> {
        Activity activity;
        public ConnectTask(Activity activity){
            this.activity=activity;
        }

        @Override
        protected TcpClient doInBackground(String... message) {

            //we create a TCPClient object
            mTcpClient = new TcpClient(new TcpClient.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    Log.i("TAG","--"+message);
                    publishProgress(message);
                }
            },activity);
            mTcpClient.run();

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            //response received from server
            Log.d("test", "response " + values[0]);
            //process server response here....

        }
    }
}
