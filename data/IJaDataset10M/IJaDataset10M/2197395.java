package GUIS;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.ProgressMonitor;

/**
 *
 * @author bok
 */
public class ProgMon implements Runnable {

    Thread hilo;

    private ProgressMonitor pm;

    public ProgMon(JFrame frame) {
        pm = new ProgressMonitor(frame, "Cargando", "Avance", 0, 100);
    }

    void startHilo() {
        hilo = new Thread(this);
        hilo.start();
    }

    public void run() {
        for (int i = 0; i < 101; i++) {
            try {
                pm.setProgress(i);
                Thread.sleep(90);
            } catch (InterruptedException ex) {
                Logger.getLogger(ProgressBar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
