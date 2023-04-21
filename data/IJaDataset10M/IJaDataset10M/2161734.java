package cyk;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import cyk.view.CYKMainFrame;

/**
 * Diese Klasse startet das Hauptprogramm. Die <code>main</code>-Funktion stellt
 * den Prozedureinstiegspunkt des Programms dar.
 * 
 * @author Stephan
 */
public class CYK {

    public static boolean DEBUG = false;

    /**
	 * Main Funktion des Programms
	 * 
	 * @param args Startargumente
	 */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                try {
                    JFrame.setDefaultLookAndFeelDecorated(true);
                    JDialog.setDefaultLookAndFeelDecorated(true);
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
                CYKMainFrame frame = new CYKMainFrame();
                frame.setVisible(true);
            }
        });
    }
}
