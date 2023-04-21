package actualizacion;

import java.io.FileOutputStream;
import java.io.IOException;

public class Cargador implements IMostrarMensaje {

    String path;

    private IMostrarMensaje mMsg;

    public Cargador(String jarFile, IMostrarMensaje mMsg) {
        this.mMsg = mMsg;
        path = System.getProperty("java.class.path");
        path = path.substring(0, path.lastIndexOf(System.getProperty("file.separator")) + 1);
        try {
            descargar(jarFile);
            ejecutar(System.getProperty("java.class.path"));
            System.exit(0);
        } catch (IOException e) {
            mostrar(e + "");
            e.printStackTrace();
        }
    }

    public void mostrar(String msg) {
        if (mMsg != null) mMsg.mostrar(msg);
    }

    private void descargar(String jar) throws IOException {
        HttpReader reader = new HttpReader(this);
        mostrar("Descargando http://prdownloads.sourceforge.net/apeiron/" + jar + "?download");
        byte buffer[] = reader.getFile("http://prdownloads.sourceforge.net/apeiron/" + jar + "?download");
        FileOutputStream jarFile = new FileOutputStream(System.getProperty("java.class.path"));
        jarFile.write(buffer);
        jarFile.close();
        mostrar("Terminada la carga de http://prdownloads.sourceforge.net/apeiron/" + jar + "?download");
    }

    private void ejecutar(String jar) {
        try {
            String exec = "javaw.exe -jar \"" + jar + "\"";
            mostrar("Ejecutando: " + exec);
            Runtime.getRuntime().exec(exec, null);
        } catch (IOException e) {
            mostrar(e + "");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
    }
}
