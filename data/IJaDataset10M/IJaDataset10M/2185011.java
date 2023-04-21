package com.PUMa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class groupbm extends Activity implements OnClickListener {

    Button FamilyButton, FriendsButton, WorkButton, OthersButton;

    String group_ID;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groupsbm);
        FamilyButton = (Button) findViewById(R.id.FamilyButtonBM);
        FriendsButton = (Button) findViewById(R.id.FriendsButtonBM);
        WorkButton = (Button) findViewById(R.id.WorkButtonBM);
        OthersButton = (Button) findViewById(R.id.OthersButtonBM);
        FamilyButton.setOnClickListener(this);
        FriendsButton.setOnClickListener(this);
        WorkButton.setOnClickListener(this);
        OthersButton.setOnClickListener(this);
    }

    public void onClick(View gp) {
        int[] selects1 = bookmarklist.selects;
        switch(gp.getId()) {
            case R.id.FamilyButtonBM:
                group_ID = "family";
                for (int i = 1; i < selects1.length; i++) {
                    if (selects1[i] != -1) {
                        databaseControl.updateBookmarks(selects1.length - i, group_ID);
                    }
                }
                Intent i1 = new Intent(this, PUMa.class);
                startActivity(i1);
                break;
            case R.id.FriendsButtonBM:
                group_ID = "friends";
                for (int i = 1; i < selects1.length; i++) {
                    if (selects1[i] != -1) {
                        databaseControl.updateBookmarks(selects1.length - i, group_ID);
                    }
                }
                Intent i2 = new Intent(this, PUMa.class);
                startActivity(i2);
                break;
            case R.id.WorkButtonBM:
                group_ID = "Work";
                for (int i = 1; i < selects1.length; i++) {
                    if (selects1[i] != -1) {
                        databaseControl.updateBookmarks(selects1.length - i, group_ID);
                    }
                }
                Intent i3 = new Intent(this, PUMa.class);
                startActivity(i3);
                break;
            case R.id.OthersButtonBM:
                group_ID = "Others";
                for (int i = 1; i < selects1.length; i++) {
                    if (selects1[i] != -1) {
                        databaseControl.updateBookmarks(selects1.length - i, group_ID);
                    }
                }
                Intent i4 = new Intent(this, PUMa.class);
                startActivity(i4);
                break;
        }
    }
}
