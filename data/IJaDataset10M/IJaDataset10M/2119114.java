package euclides.app;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

public class NoteActivity extends Activity {

    private EditText ed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notities);
        ed = (EditText) findViewById(R.id.edit_notitie);
    }

    String current = "notities.txt";

    @Override
    protected void onPause() {
        getFilesDir().mkdirs();
        try {
            OutputStream os = openFileOutput(current, MODE_PRIVATE);
            DataOutputStream dos = new DataOutputStream(os);
            String text = ed.getText().toString();
            dos.writeUTF(text);
            dos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            InputStream in = openFileInput(current);
            DataInputStream din = new DataInputStream(in);
            String text = din.readUTF();
            ed.setText(text);
            din.close();
        } catch (IOException e) {
        }
    }
}
