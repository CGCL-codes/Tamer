package mswing.demos;

/**
 * Applet g�n�rique pour lancer les d�mos.
 *
 * @author Emeric Vernat
 */
public class MDemoApplet extends javax.swing.JApplet {

    private static final long serialVersionUID = 1L;

    /**
	 * Constructeur.
	 */
    public MDemoApplet() {
        super();
    }

    /** {@inheritDoc} */
    public void init() {
        super.init();
        try {
            Class.forName("com.incors.plaf.kunststoff.KunststoffLookAndFeel");
            System.setProperty("mswing.LOOK_AND_FEEL", "com.incors.plaf.kunststoff.KunststoffLookAndFeel");
        } catch (final Exception e) {
        }
        mswing.MSwingTheme.checkInit();
        mswing.MUtilities.setApplet(this);
        final String text = getParameter("text");
        final javax.swing.JButton button = new javax.swing.JButton(text);
        getContentPane().add(button);
        button.addActionListener(new java.awt.event.ActionListener() {

            /** {@inheritDoc} */
            public void actionPerformed(java.awt.event.ActionEvent event) {
                run();
            }
        });
    }

    void run() {
        final String className = getParameter("className");
        try {
            mswing.MUtilities.run((javax.swing.JComponent) Class.forName(className).newInstance());
        } catch (final Exception e) {
            mswing.MUtilities.handleError(e);
        }
    }
}
