package gen.desarrolladores.android.com.explorador.archivos;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class ExploradorArchivos extends ListActivity {

    private List<String> elementos = null;

    private void rellenar(File[] archivos) {
        elementos = new ArrayList<String>();
        for (File archivo : archivos) elementos.add(archivo.getPath());
        ArrayAdapter<String> listaArchivos = new ArrayAdapter<String>(this, R.layout.fila, elementos);
        setListAdapter(listaArchivos);
    }

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.listado);
        rellenar(new File("/").listFiles());
    }
}
