package org.hackathon.ashiato;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Ashiato extends Activity implements OnClickListener {

    Button getButton;

    Button postButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        getButton = (Button) findViewById(R.id.get_button);
        getButton.setOnClickListener(this);
        getButton = (Button) findViewById(R.id.post_button);
        getButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.get_button:
                Intent intent1 = new Intent(this, GetActivity.class);
                startActivity(intent1);
                break;
            case R.id.post_button:
                Intent intent2 = new Intent(this, PostActivity.class);
                startActivity(intent2);
                break;
            default:
                break;
        }
    }
}
