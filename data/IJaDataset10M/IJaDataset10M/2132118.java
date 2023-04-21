package com.android.nb.activity;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import com.android.nb.activity.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class NumberBoxTutorial extends Activity implements OnClickListener {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Button ButtonTutorialToMainMenu;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.numberbox_tutorial);
        InputStream iFile = getResources().openRawResource(R.raw.tutorial);
        try {
            TextView helpText = (TextView) findViewById(R.id.TextView_TutorialText);
            String strFile = inputStreamToString(iFile);
            helpText.setText(strFile);
        } catch (IOException e) {
        }
        ButtonTutorialToMainMenu = (Button) findViewById(R.id.tutorialButtonMainMenuID);
        ButtonTutorialToMainMenu.setOnClickListener(this);
    }

    public String inputStreamToString(InputStream is) throws IOException {
        StringBuffer sBuffer = new StringBuffer();
        DataInputStream dataIO = new DataInputStream(is);
        String strLine = null;
        while ((strLine = dataIO.readLine()) != null) {
            sBuffer.append(strLine + "\n");
        }
        dataIO.close();
        is.close();
        return sBuffer.toString();
    }

    @Override
    public void onClick(View src) {
        switch(src.getId()) {
            case R.id.tutorialButtonMainMenuID:
                Intent myIntent = new Intent(src.getContext(), NumberBoxMenu.class);
                startActivityForResult(myIntent, 0);
                break;
        }
    }
}
