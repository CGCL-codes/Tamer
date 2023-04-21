package es.usc.citius.servando.android.medim.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.medimandroid.R;
import es.usc.citius.servando.android.communications.ObjectTransporter;
import es.usc.citius.servando.android.helper.ContextManager;

public class ServiceTest extends Activity implements OnClickListener {

    /**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
    Button runButton;

    ObjectTransporter transporter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContextManager.getInstance().setApplicationContext(getApplicationContext());
        setContentView(R.layout.servicetest);
        runButton = (Button) findViewById(R.id.sendMessageBUtton);
        runButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.sendMessageBUtton:
                sendMessage();
                break;
            default:
                break;
        }
    }

    private void sendMessage() {
    }
}
