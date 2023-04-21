package ch.blackspirit.graphics;

/**
 * Notifications to the window listener are not necessarily made on the users thread.
 * @author Markus Koller
 */
public interface WindowListener {

    public static WindowListener EXIT_ON_CLOSE = new WindowListener() {

        public void windowActivated() {
        }

        public void windowClosing() {
            System.exit(0);
        }

        public void windowClosed() {
        }

        public void windowDeactivated() {
        }

        public void windowDeiconified() {
        }

        public void windowIconified() {
        }
    };

    /**
     * Invoked when the user attempts to close the window.
     */
    public void windowClosing();

    public void windowClosed();

    public void windowIconified();

    public void windowDeiconified();

    public void windowActivated();

    public void windowDeactivated();
}
