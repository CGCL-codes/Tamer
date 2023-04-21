package entradasalida;

import java.awt.Frame;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class EntradaFicheroID3 {

    /**
	 * Fichero del que se obtendrán los datos de entrada
	 */
    File fichero;

    /**
	 * Constructor por defecto
	 */
    public EntradaFicheroID3() {
    }

    /**
	 * Constructor de la clase
	 * @param arg0 Frame llamante
	 */
    public EntradaFicheroID3(Frame arg0) {
        JFileChooser f = new JFileChooser(System.getProperty("user.dir"));
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Ficheros de datos", "txt");
        f.setFileFilter(filtro);
        int returnVal = f.showOpenDialog(arg0);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            fichero = f.getSelectedFile();
        } else {
            fichero = null;
        }
        if (fichero != null) {
            LeeDatosFichero();
        }
    }

    /**
	 * Lee el fichero y adapta las estructuras utilizadas en memoria
	 */
    private void LeeDatosFichero() {
    }
}
