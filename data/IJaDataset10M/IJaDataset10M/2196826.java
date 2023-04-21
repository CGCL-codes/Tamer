package org.android.brasil.projetos.exemplo.base;

import org.android.brasil.projetos.exemplo.R;
import org.android.brasil.projetos.exemplo.R.layout;
import android.app.Activity;
import android.os.Bundle;

public class Exemplo extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
}
