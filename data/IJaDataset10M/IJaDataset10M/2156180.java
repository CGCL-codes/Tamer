package periman;

/**
 * @author kroiz
 *
 */
public class DetExp {

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        Wizard.instance = new Wizard();
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
