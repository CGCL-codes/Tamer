package pl.edu.agh.ssm.monitor.visualization.test;

import javax.swing.SwingUtilities;
import pl.edu.agh.ssm.monitor.visualization.MonitorFrame;

/**
 * 
 * @author Monika Nawrot
 * 
 * Start Monitor GUI
 */
public class GUIStarter {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new MonitorFrame();
            }
        });
    }
}
