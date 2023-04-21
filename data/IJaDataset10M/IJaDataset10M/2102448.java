package net.sourceforge.contactmanager;

/**
 * The main application.  This class simply loads the main window.
 * @author Stephen Phillips
 *
 */
public class Application {

    /**
	 * Run the application.  It simply loads the main window.
	 * @param args   The command-line arguments; none are expected.
	 */
    public static void main(String[] args) {
        MainWindow win = new MainWindow();
        win.setVisible(true);
    }
}
