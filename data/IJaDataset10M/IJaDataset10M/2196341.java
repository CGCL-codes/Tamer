package eu.irreality.age.windowing;

import java.awt.Component;
import javax.swing.SwingUtilities;

/**
 * Thread to update a component.
 * @author Administrador
 *
 */
public class UpdatingRun implements Runnable {

    Component comp;

    Runnable r = new Runnable() {

        public void run() {
            justUpdate();
        }
    };

    public UpdatingRun(Component jc) {
        comp = jc;
    }

    public void run() {
        try {
            SwingUtilities.invokeAndWait(r);
        } catch (Exception exc) {
            System.out.println("Excepci�n.");
            exc.printStackTrace();
        }
    }

    public void justUpdate() {
        comp.repaint();
    }
}
