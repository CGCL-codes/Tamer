package aini.nur;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class AksesFileActivity extends Activity {

    /** Called when the activity is first created. */
    int x = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final TextView tulisan = (TextView) findViewById(R.id.isifile);
        final ImageButton btntulis = (ImageButton) findViewById(R.id.ImageButtontulis);
        final ImageButton btnbaca = (ImageButton) findViewById(R.id.ImageButtonbaca);
        btntulis.setOnClickListener(new ImageButton.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(AksesFileActivity.this, "ke :" + x, Toast.LENGTH_SHORT).show();
                writefile(x++);
            }
        });
        btnbaca.setOnClickListener(new ImageButton.OnClickListener() {

            @Override
            public void onClick(View v) {
                tulisan.setText(readfile());
            }
        });
    }

    private void writefile(int x) {
        String FILENAME = "nuraini";
        String string = "menulis di android ke " + x;
        FileOutputStream fos;
        try {
            fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write(string.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readfile() {
        String FILENAME = "nuraini";
        String baca = "";
        char[] buffer = new char[200];
        try {
            FileInputStream inputfile = openFileInput(FILENAME);
            InputStreamReader buf = new InputStreamReader(inputfile);
            int bytesRead = 0;
            while ((bytesRead = buf.read(buffer)) != -1) {
                String chunk = new String(buffer, 0, bytesRead);
                baca = baca + chunk;
            }
            buf.close();
            inputfile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baca;
    }
}
