package com.fortytwo.deepthought;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ProfileActivity extends Activity {

    private String mName;

    private String mSurname;

    private String mUsername;

    private String mUserCode;

    private String mLastGame;

    private String mUserImage;

    @Override
    public void onCreate(Bundle savedINstanceState) {
        super.onCreate(savedINstanceState);
        setContentView(R.layout.profile_layout);
        mUsername = this.getIntent().getStringExtra("Username");
        mUserCode = this.getIntent().getStringExtra("dt_code");
        this.setTitle("Your Profile");
    }
}
