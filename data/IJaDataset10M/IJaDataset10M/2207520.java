package researchers_night.presenter;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import jaca.android.dev.JaCaActivity;

/**
 * 
 * @author mguidi
 *
 */
public class EventActivity extends JaCaActivity {

    protected TextView mTxtPartecipants;

    protected TextView mTxtTitle;

    protected ImageView mImgStatus;

    protected Button mBtnInc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_view);
        mTxtTitle = (TextView) findViewById(R.id.txtTitle);
        mImgStatus = (ImageView) findViewById(R.id.imgStatus);
        mTxtPartecipants = (TextView) findViewById(R.id.txtParticipants);
        mTxtTitle.setText(getIntent().getStringExtra("title"));
        mBtnInc = (Button) findViewById(R.id.btnStart);
        createArtifact("EventActivity", EventActivityArtifact.class.getCanonicalName());
    }

    public void setParticipants(final String text) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                mTxtPartecipants.setText(text);
            }
        });
    }

    public void setStatus(final String status) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (status.equals("status_waiting")) {
                    mImgStatus.setImageResource(R.drawable.status_waiting);
                } else if (status.equals("status_ready")) {
                    mImgStatus.setImageResource(R.drawable.status_ready);
                } else if (status.equals("status_running")) {
                    mImgStatus.setImageResource(R.drawable.status_running);
                } else if (status.equals("status_ended")) {
                    mImgStatus.setImageResource(R.drawable.status_ended);
                }
            }
        });
    }
}
