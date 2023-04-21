package demo.hello;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    public static String PUB_ID = "ttt";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        PackageManager manager = getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo("demo.hello", PackageManager.GET_ACTIVITIES);
            Toast.makeText(this, "version:" + info.versionName + "\nversionCode:" + info.versionCode, Toast.LENGTH_LONG).show();
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        TextView textView = (TextView) findViewById(R.id.text);
        textView.setText(PUB_ID);
    }
}
