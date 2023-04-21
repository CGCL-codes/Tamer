package generales;

import java.util.HashMap;
import javax.swing.JButton;

public class Terminales {

    private HashMap<Integer, Terminal> terminales = new HashMap<Integer, Terminal>();

    /**
	 * Constructor de la clase
	 */
    public Terminales() {
    }

    public boolean agregarTerminal(int nroTerminal) {
        terminales.put(nroTerminal, new Terminal());
        return true;
    }
}
