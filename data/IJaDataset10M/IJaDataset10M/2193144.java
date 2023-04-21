package time.burrito.menus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AcercaDe extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acerca_de);
        ((Button) findViewById(R.id.btnRegresarAcerca)).setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                finish();
                Intent intencion = new Intent(AcercaDe.this, MenuPrincipal.class);
                startActivity(intencion);
            }
        });
    }
}
