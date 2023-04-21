package aplic.v1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageButton;

public class Aplicacion1 extends Activity {

    /** Called when the activity is first created. */
    static final int MENU_GOOGLE = 0;

    static final int MENU_QUIT = 1;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final ImageButton button = (ImageButton) findViewById(R.id.android_button);
        button.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Intent startActivity = new Intent();
                startActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity.setAction("android.intent.action.SUBMAIN");
                startActivity(startActivity);
            }
        });
        final ImageButton button2 = (ImageButton) findViewById(R.id.android_button2);
        button2.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Intent startActivity = new Intent();
                startActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity.setAction("android.intent.action.SUBMAIN2");
                startActivity(startActivity);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_GOOGLE, 0, "Google").setIcon(R.drawable.google);
        menu.add(0, MENU_QUIT, 0, "Finalizar").setIcon(R.drawable.exit);
        ;
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case MENU_GOOGLE:
                llamadaGoogle();
                return true;
            case MENU_QUIT:
                finish();
                return true;
        }
        return false;
    }

    public void llamadaGoogle() {
        WebView webview;
        webview = (WebView) findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl("http://images.google.es/");
    }
}
