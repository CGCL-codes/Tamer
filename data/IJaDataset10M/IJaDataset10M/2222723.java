package net.sf.cardic.games.mastermind;

import java.awt.Color;
import java.util.logging.Logger;
import javax.swing.JPanel;
import net.sf.cardic.core.GameTopComponent;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.openide.util.ImageUtilities;
import org.netbeans.api.settings.ConvertAsProperties;

/**
 * @author Patrik Karlsson <patrik@trixon.se>
 */
@ConvertAsProperties(dtd = "-//net.sf.cardic.games.mastermind//Mastermind//EN", autostore = false)
public final class MastermindTopComponent extends GameTopComponent {

    private static MastermindTopComponent instance;

    /** path to the icon used by the component and its open action */
    static final String ICON_PATH = "net/sf/cardic/games/mastermind/res/icon.png";

    private static final String PREFERRED_ID = "MastermindTopComponent";

    private Mastermind gameController;

    public MastermindTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(MastermindTopComponent.class, "CTL_MastermindTopComponent"));
        setIcon(ImageUtilities.loadImage(ICON_PATH, true));
        gameController = new Mastermind(this);
        topPanel.setBackground(Color.DARK_GRAY);
    }

    private void initComponents() {
        topPanel = new javax.swing.JPanel();
        setLayout(new java.awt.BorderLayout());
        topPanel.setLayout(new java.awt.BorderLayout());
        add(topPanel, java.awt.BorderLayout.CENTER);
    }

    private javax.swing.JPanel topPanel;

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     */
    public static synchronized MastermindTopComponent getDefault() {
        if (instance == null) {
            instance = new MastermindTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the MastermindTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized MastermindTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(MastermindTopComponent.class.getName()).warning("Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof MastermindTopComponent) {
            return (MastermindTopComponent) win;
        }
        Logger.getLogger(MastermindTopComponent.class.getName()).warning("There seem to be multiple components with the '" + PREFERRED_ID + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    @Override
    public void componentOpened() {
        gameController.startNewGame();
    }

    @Override
    public void componentClosed() {
    }

    void writeProperties(java.util.Properties p) {
        p.setProperty("version", "1.0");
    }

    Object readProperties(java.util.Properties p) {
        if (instance == null) {
            instance = this;
        }
        instance.readPropertiesImpl(p);
        return instance;
    }

    private void readPropertiesImpl(java.util.Properties p) {
        String version = p.getProperty("version");
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    public Mastermind getGameController() {
        return gameController;
    }

    @Override
    public JPanel getTopPanel() {
        return topPanel;
    }
}
