package com.GNG.Activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import com.GNG.User;
import com.GNG.Tools.DealWithHTML;
import com.GNG.http.Constant;
import com.GNG.http.Http;
import com.GNG.Activity.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PersonalInfoActivity extends Activity {

    User.PersonalInfo info;

    String nick;

    String realname;

    String address;

    String email;

    String year;

    String month;

    String day;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personalinfo);
        TextView mTextViewUserId = (TextView) findViewById(R.id.TextViewUserId1);
        TextView mTextViewPostCount = (TextView) findViewById(R.id.TextViewPostCount1);
        TextView mTextViewMailCount = (TextView) findViewById(R.id.TextViewMailCount1);
        TextView mTextViewLoginCount = (TextView) findViewById(R.id.TextViewLoginCount1);
        TextView mTextViewLoginTime = (TextView) findViewById(R.id.TextViewLoginTime1);
        TextView mTextViewEstablishTime = (TextView) findViewById(R.id.TextViewEstablishTime1);
        TextView mTextViewLastLoginTime = (TextView) findViewById(R.id.TextViewLastLoginTime1);
        TextView mTextViewIp = (TextView) findViewById(R.id.TextViewIP1);
        EditText mEditTextNickName = (EditText) findViewById(R.id.TextViewNickName1);
        EditText mEditTextTrueName = (EditText) findViewById(R.id.TextViewTrueName1);
        EditText mEditTextAddress = (EditText) findViewById(R.id.TextViewAddress1);
        EditText mEditTextEmail = (EditText) findViewById(R.id.TextViewEmail1);
        EditText mEditTextBirthday = (EditText) findViewById(R.id.TextViewBirthday1);
        String strResult = new Http().getWithCookie(Constant._domainURL + "cgi-bin/bbsinfo");
        info = DealWithHTML.getPersonalInfo(strResult);
        mTextViewUserId.setText(info.username);
        mTextViewPostCount.setText(info.postCount);
        mTextViewMailCount.setText(info.mailCount);
        mTextViewLoginCount.setText(info.loginCount);
        mTextViewLoginTime.setText(info.loginMins);
        mTextViewEstablishTime.setText(info.regTime);
        mTextViewLastLoginTime.setText(info.lastLogin);
        mTextViewIp.setText(info.ip);
        mEditTextNickName.setText(info.nick);
        mEditTextTrueName.setText(info.realName);
        mEditTextAddress.setText(info.address);
        mEditTextEmail.setText(info.email);
        mEditTextBirthday.setText(info.birthday);
        nick = mEditTextNickName.getText().toString();
        realname = mEditTextTrueName.getText().toString();
        address = mEditTextAddress.getText().toString();
        email = mEditTextEmail.getText().toString();
        String[] data = info.birthday.split("/");
        year = data[0];
        month = data[1];
        day = data[2];
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        Log.i("create menu", "menu haole");
        menu.add(0, 12, 0, "����");
        menu.findItem(12);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case 12:
                {
                    String str = post();
                    Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
                    break;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private String post() {
        String res;
        String encodeNick = "";
        String encodeRealName = "";
        String encodeAddress = "";
        try {
            encodeNick = URLEncoder.encode(nick, "gb2312");
            encodeRealName = URLEncoder.encode(realname, "gb2312");
            encodeAddress = URLEncoder.encode(address, "gb2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = Constant._domainURL + "cgi-bin/bbsinfo?type=1" + "&nick=" + encodeNick + "&realname=" + encodeRealName + "&address" + encodeAddress + "&email=" + email + "&year=" + year + "&month" + month + "&day" + day;
        String[] errinfo = DealWithHTML.GetErrorInfo(Constant.mHttp.getWithCookie(url));
        if (errinfo == null) res = "�޸ĳɹ���"; else res = "ERROR:" + errinfo[1];
        return res;
    }
}
