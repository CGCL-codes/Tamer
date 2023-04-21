package smartHexMandalzoom.mandlzoom;

import java.awt.Component;
import javax.swing.JFrame;

/**
 * @author Nathan
 *
 */
class DemoUi {

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Mandalbrot Display");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Component pane = new DemoUiCanvas();
        frame.getContentPane().add(pane);
        frame.pack();
        frame.setVisible(true);
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                createAndShowGUI();
            }
        });
    }
}
