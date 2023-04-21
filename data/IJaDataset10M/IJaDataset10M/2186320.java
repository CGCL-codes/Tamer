package bt747.j2se_view;

/**
 * Main class (application entry) An empty wrapper to maintain compatibility
 * after moving classes into specific package.
 * 
 * @author Mario De Weerd
 */
public class BT747 extends bt747.waba_view.AppBT747 {

    public void onStart() {
        super.onStart();
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
            }
        });
    }

    static final String[] MY_ARGS = { "/w", "320", "/h", "320", "/scale", "1", "/bpp", "8", BT747.class.getName() };

    /**
     * Allow this to be "self executable" in J2SE
     * 
     * @param args
     *            the command line arguments
     */
    public static void main(final String args[]) {
        if ((args.length != 0) && args[0].equals("arch")) {
            System.out.print(java.lang.System.getProperty("os.arch"));
        }
        int i;
        int j;
        String[] newArgs = new String[args.length + MY_ARGS.length];
        for (i = 0; i < args.length; i++) {
            newArgs[i] = args[i];
        }
        for (j = 0; j < MY_ARGS.length; i++, j++) {
            newArgs[i] = MY_ARGS[j];
        }
        waba.applet.Applet.main(newArgs);
    }
}
