package jp.hackathon.voctrl.android.output;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class output extends Activity implements OnClickListener {

    private Button vStartBtn;

    private Button vEndBtn;

    private Button vShowBtn;

    private TextView vMessage;

    private HttpGet request;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);
        vShowBtn = (Button) findViewById(R.id.show_button);
        vShowBtn.setOnClickListener(this);
        vMessage = (TextView) findViewById(R.id.Message);
    }

    @Override
    public void onClick(View v) {
        if (v == vStartBtn) {
        } else if (v == vEndBtn) {
        } else if (v == vShowBtn) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse response1 = null;
            try {
                String url = "http://voctrl.appspot.com/output";
                StringBuilder uriBuilder = new StringBuilder(url);
                request = new HttpGet(uriBuilder.toString());
                response1 = httpClient.execute(request);
                int status = response1.getStatusLine().getStatusCode();
                if (status == HttpStatus.SC_OK) {
                    ByteArrayOutputStream ostream = new ByteArrayOutputStream();
                    response1.getEntity().writeTo(ostream);
                    String message = ostream.toString();
                    vMessage.setText(message);
                }
            } catch (Exception ex) {
                String verror = ex.getMessage();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("�^�C�g��");
                alertDialogBuilder.setMessage(verror);
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        }
    }
}
