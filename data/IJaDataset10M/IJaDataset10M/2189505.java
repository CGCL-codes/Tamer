package incial;

import control.Control;
import control.Controlador;
import vista.MainWindows;

/**
 * La clase principal, la que se va a llamar al ejecutar la aplicación.
 */
public class Main {

    /**
	 * @param args No usados todavía.
	 */
    public static void main(String[] args) {
        Control control = new Controlador();
        MainWindows mainWindows = new MainWindows(control);
        control.setVentanaPrincipal(mainWindows);
        mainWindows.setVisible(true);
    }
}
