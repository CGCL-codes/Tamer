package gmenu;

import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/** Clase gmOpcionMenu tipo de todos los submenus */
public class gmOpcionMenu extends JMenuItem {

    /** variable que indica los estados en los que esta opcion se habilita */
    private String habilita;

    /** Constructor por defecto de la clase */
    public gmOpcionMenu() {
        habilita = new String();
    }

    /** Funcion establece para durante la ejecucion poder cambiar la habilitacion
	 * @param cad contiene los estados en los que las opciones estan habilitadas
	 * @param estad es el estado actual
	 */
    public void establece(String cad, String estad) {
        int i, j;
        String c;
        setEnabled(false);
        habilita = new String(cad);
        i = 0;
        if (habilita.compareTo(",99,") == 0) setEnabled(true); else if (habilita.compareTo(",18,") == 0) {
            if (estad.lastIndexOf("-6-") != -1 && (estad.lastIndexOf("-9-") != -1 || estad.lastIndexOf("-10-") != -1)) setEnabled(true);
        } else while (i < estad.length()) {
            for (j = i; j < estad.length() && estad.charAt(j) != '-'; j++) ;
            c = new String(estad.substring(i, j));
            i = j + 1;
            if (habilita.lastIndexOf("," + c + ",") != -1) setEnabled(true);
        }
    }

    /** Funcion estableceValores (establece los valores iniciales)
	 * @param texto es el texto que aparecera en el menu
	 * @param tecla son las teclas rapidas para acceder a esa opcion
	 * @param oyente para capturar eventos
	 * @param cad son los estados en los que la opcion esta habilitada
	 * @param estad es el estado actual
	 * @param car es el caracter que se subraya al pulsar la tecla alt
	 */
    public void estableceValores(String texto, KeyStroke tecla, ActionListener oyente, MouseListener oyenteRaton, String cad, String estad, char car) {
        int i, j;
        String c;
        setText(texto);
        setEnabled(false);
        if (car != ' ') setMnemonic(car);
        habilita = new String(cad);
        i = 0;
        if (habilita.compareTo(",99,") == 0) setEnabled(true); else if (habilita.compareTo(",18,") == 0) {
            if (estad.lastIndexOf("-6-") != -1 && (estad.lastIndexOf("-9-") != -1 || estad.lastIndexOf("-10-") != -1)) setEnabled(true);
        } else while (i < estad.length()) {
            for (j = i; j < estad.length() && estad.charAt(j) != '-'; j++) ;
            c = new String(estad.substring(i, j));
            i = j + 1;
            if (habilita.lastIndexOf("," + c + ",") != -1) setEnabled(true);
        }
        setAccelerator(tecla);
        addActionListener(oyente);
        addMouseListener(oyenteRaton);
    }

    /** Funcion ponTexto establece el texto de una opcion
	 * @param texto 
	 */
    public void ponTexto(String texto) {
        setText(texto);
    }
}
