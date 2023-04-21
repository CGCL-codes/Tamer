package patho.textmining.munich.gui;

/**
 *
 * @author Administrator
 */
public class Main {

    /** Creates a new instance of Main */
    public Main() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(final String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new TextMiningMunichGui(args).setVisible(true);
            }
        });
    }
}
