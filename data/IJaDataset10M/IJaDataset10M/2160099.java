package javax.help;

/**
 * MainWindow is a class that will create a single main help 
 * window for an application. Although there is generally only one per
 * application there can  be multiple MainWindow.
 * By default it is a tri-paned fully decorated window
 * consisting of a toolbar, navigator pane, and help content view. By default
 * the class is not destroyed when the window exits.
 *
 * @author Roger D.Brinkley
 * @version	1.3	10/30/06
 * @since 2.0
 *
 * @see javax.help.WindowPresentation
 * @see javax.help.Presentation
 */
public class MainWindow extends WindowPresentation {

    private MainWindow(HelpSet hs) {
        super(hs);
    }

    /**
     * Creates a new MainWindow for a given HelpSet and 
     * HelpSet.Presentation "name". If the "name"d HelpSet.Presentation
     * does not exist in HelpSet then the default HelpSet.Presentation
     * is used.
     * 
     * @param hs The HelpSet used in this presentation
     * @param name The name of the Presentation to create - also the name
     *             of the HelpSet.Presentation to use.
     * @returns Presentation A unique MainWindow. 
     */
    public static Presentation getPresentation(HelpSet hs, String name) {
        MainWindow mwp = new MainWindow(hs);
        if (hs != null) {
            HelpSet.Presentation presentation = null;
            if (name != null) {
                presentation = hs.getPresentation(name);
            }
            if (presentation == null) {
                presentation = hs.getDefaultPresentation();
            }
            mwp.setHelpSetPresentation(presentation);
        }
        return mwp;
    }

    /**
     * Debugging code...
     */
    private static final boolean debug = false;

    private static void debug(Object msg) {
        if (debug) {
            System.err.println("MainWindow: " + msg);
        }
    }
}
