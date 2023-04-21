package at.vartmp.jschnellen.gui;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Properties;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.apache.log4j.Level;
import at.vartmp.jschnellen.core.ReleaseInfo;
import at.vartmp.jschnellen.core.util.confManager.ConfigurationData;
import at.vartmp.jschnellen.core.util.logger.SchnellnLogFactory;
import at.vartmp.jschnellen.core.util.logger.SchnellnLogger;
import at.vartmp.jschnellen.gui.dialogs.AbstractInformationDialog;
import at.vartmp.jschnellen.gui.helper.DialogUtil;
import at.vartmp.jschnellen.gui.helper.GuiBeanManager;
import at.vartmp.jschnellen.gui.helper.handler.GameHandlerUtil;
import at.vartmp.jschnellen.gui.parts.MenuBar;
import pagelayout.Row;

/**
 * The MainGui is composed of different components:
 * 
 * This class implements the Interface
 * <code>at.vartmp.jschnellen.core.modules.ISpieler</code> and may used as RemoteObject.
 * 
 * @author Lukas Tischler [tischler.lukas_AT_gmail.com]
 * @author Stephan Winkler
 * 
 * @see javax.swing.JFrame
 */
public final class MainGui extends JFrame {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 2726982503901368458L;

    /** The logger. */
    private static SchnellnLogger logger = SchnellnLogFactory.getLog(MainGui.class);

    /** The main panel. */
    private JPanel mainPanel;

    private SplashScreen splashScreen;

    private static final String NAME = ReleaseInfo.getProject() + " v" + ReleaseInfo.getVersion() + ", build " + ReleaseInfo.getBuildNumber();

    private static Properties lookAndFeels = new Properties();

    static {
        final String defaultFallbackLnF = "Metal";
        lookAndFeels.setProperty("Metal", "javax.swing.plaf.metal.MetalLookAndFeel");
        lookAndFeels.setProperty("Motif", "com.sun.java.swing.plaf.motif.MotifLookAndFeel");
        lookAndFeels.setProperty("GTK", "com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
        lookAndFeels.setProperty("NimbusLookAndFeel", "net.sourceforge.napkinlaf.NapkinLookAndFeel");
        lookAndFeels.setProperty("lipstik", "com.lipstikLF.LipstikLookAndFeel");
        String lnf = System.getProperty("lnf");
        if (lnf == null) {
            try {
                Class.forName(lookAndFeels.getProperty("lipstik"));
                lnf = "lipstik";
            } catch (ClassNotFoundException e) {
                lnf = defaultFallbackLnF;
            }
        } else {
            try {
                String className = lookAndFeels.getProperty(lnf, defaultFallbackLnF);
                Class.forName(className);
            } catch (ClassNotFoundException e) {
                lnf = defaultFallbackLnF;
            }
        }
        System.setProperty("lnf", lnf);
    }

    /** The window listener. */
    private WindowListener windowListener = new WindowAdapter() {

        public void windowClosing(WindowEvent e) {
            logger.debug("Got a windowClosing operation!");
            askForClosing();
        }
    };

    /**
	 * Instantiates a new main gui.<br>
	 * <br>
	 * <b>Note</b>: Invoking the constructors of this class directly makes no
	 * sense, as there should be only one instance of MainGui in a JVM.
	 */
    public MainGui() {
        this(NAME);
    }

    /**
	 * The Constructor. After calling this one, execute the method
	 * {@link #createAndShowGui()}<br>
	 * <br>
	 * <b>Note</b>: Invoking the constructors of this class directly makes no
	 * sense, as there should be only one instance of MainGui in a JVM.
	 * 
	 * @param title
	 *            the title
	 * 
	 * @throws HeadlessException
	 *             the headless exception
	 */
    public MainGui(String title) throws HeadlessException {
        super(title);
        this.setContentPane(new TableImpl());
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    /**
	 * Creates the and show the GUI.
	 */
    public void createAndShowGui() {
        this.splashScreen.updateState(20);
        this.setSize(new java.awt.Dimension(1024, 768));
        this.splashScreen.updateState(40);
        this.setJMenuBar((MenuBar) GuiBeanManager.getInstance().getJComponent("menuBar"));
        this.addWindowListener(windowListener);
        this.splashScreen.updateState(60);
        mainPanel = (JPanel) GuiBeanManager.getInstance().getJComponent("mainPanel");
        this.splashScreen.updateState(80);
        Row rtopLevel = new Row();
        rtopLevel.newColumn().add(mainPanel);
        rtopLevel.createLayout(this);
        this.validate();
        this.splashScreen.updateState(100);
        this.setVisible(true);
    }

    /**
	 * Ask the user for closing the application.<br>
	 * If the user wants this to, there is invoked {@link System#exit(int)}.
	 * 
	 * @see DialogUtil#showDialog(String, String, String, Object...)
	 * @see System#exit(int)
	 */
    public void askForClosing() {
        boolean ok = DialogUtil.showDialog("yesNoDialog", AbstractInformationDialog.TYPE_EXIT, "information.information.exit");
        logger.debug("User chosed: " + ok);
        if (ok) {
            GameHandlerUtil.interrupt();
            System.exit(0);
        }
    }

    /**
	 * Prints out the installed fonts of this system. For debugging only!
	 */
    static void printOutFonts() {
        logger.debug("+++++++++++++ FONT +++++++++++++");
        Font[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
        for (int i = 0; i < fonts.length; i++) {
            String str = "";
            str += (fonts[i].getFontName() + " : ");
            str += (fonts[i].getFamily() + " : ");
            str += (fonts[i].getName());
            logger.debug(str);
        }
        logger.debug("+++++++++++++++++++++++++++++++++++++++++++");
    }

    /**
	 * 
	 * 
	 * TODO : try to find the perfect lookAndFeel TODO : make this dynamic
	 * 
	 * 
	 * <code>http://javabyexample.wisdomplug.com/component/content/article/37-core-java/65-20-free-look-and-feel-libraries-for-java-swings.html</code>
	 */
    private static void initLookAndFeel() {
        logger.debug("Initializing LookAndFeel...");
        String lookAndFeel = System.getProperty("lnf");
        try {
            logger.info("Using following lookAndFeel: " + lookAndFeels.getProperty(lookAndFeel));
            UIManager.setLookAndFeel(lookAndFeels.getProperty(lookAndFeel));
        } catch (UnsupportedLookAndFeelException e) {
            logger.error("Can't use the specified look and feel (" + lookAndFeel + ") on this platform.");
            logger.error("Falling back to default...");
        } catch (Exception e) {
            logger.error("Couldn't get specified look and feel (" + lookAndFeel + "), for some reason.");
            logger.error("Falling back to default...");
        }
    }

    /**
	 * Prints the available look and feels.
	 */
    static void printAvailableLookAndFeels() {
        for (Object o : lookAndFeels.keySet()) {
            System.out.println(o);
        }
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        System.out.println(NAME);
        System.out.println("Create on: " + ReleaseInfo.getBuildDate());
        System.out.println();
        System.out.println("This program comes with ABSOLUTELY NO WARRANTY;");
        System.out.println("This is free software, and you are welcome to redistribute it");
        System.out.println("under certain conditions");
        System.out.println();
        if (System.getProperty("guiloglevel") != null) {
            SchnellnLogger l = SchnellnLogFactory.getLog("at.vartmp.jschnellen.gui");
            l.setLevel(Level.toLevel(System.getProperty("guiloglevel"), Level.INFO));
        }
        java.io.File path = null;
        if (!(path = new java.io.File(ConfigurationData.getDefaultPath())).exists()) {
            path.mkdirs();
        }
        SplashScreen ss = new SplashScreen();
        javax.swing.SwingUtilities.invokeLater(ss);
        initLookAndFeel();
        GuiBeanManager beanManager = GuiBeanManager.getInstance();
        beanManager.getMainGui().splashScreen = ss;
        GuiBeanManager.getInstance().getMainGui().createAndShowGui();
    }
}
