package solarex;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import solarex.ui.ApplicationFrame;

/**
 * Solarex main application class.
 * 
 * @author Hj. Malthaner
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                final ApplicationFrame frame = new ApplicationFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }
}
