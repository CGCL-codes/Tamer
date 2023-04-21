package persdocmanager.gui.base;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import persdocmanager.Persdocmanager;
import persdocmanager.gui.use.UseMainFrame;

public class BasicMainFrame extends JFrame {

    /**
	 *
	 */
    private static final long serialVersionUID = 3961901130786671032L;

    /**
	 * Logger for this class
	 */
    private static final Logger log = LoggerFactory.getLogger(BasicMainFrame.class);

    /**
	 * the Desktop
	 */
    private JToolBar mToolBar = null;

    private JMenuBar mMenuBar = null;

    private JLabel mStatusTextLabel;

    private JProgressBar mProgressBar;

    private JMenu jMenu = null;

    private JDesktopPane mDesktop = null;

    private JPanel mStatusPanel = null;

    private static Splash mSplash = null;

    private static boolean mSplashshow = true;

    public BasicMainFrame() {
        super();
        initialize();
        setCenter(this);
    }

    /**
	 * This method initializes this
	 * 
	 */
    private void initialize() {
        setTitle("PersDocManager");
        setLayout(new BorderLayout());
        this.setSize(new Dimension(800, 600));
        this.setJMenuBar(getJMenuBar());
        this.add(getToolBar(), BorderLayout.NORTH);
        this.add(getDesktop(), BorderLayout.CENTER);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                Persdocmanager.tearDownDB();
                System.exit(0);
            }
        });
    }

    /**
	 * Centers a given component on the display.
	 */
    public static void setCenter(Component pComponent) {
        log.info("setCenter(Component pComponent=" + pComponent + ") - start");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        pComponent.setLocation((int) ((screenSize.width / 2) - (pComponent.getSize().getWidth() / 2)), (int) ((screenSize.height / 2) - (pComponent.getSize().getHeight() / 2)));
        pComponent.repaint();
        log.debug("setCenter(Component) - end");
    }

    /**
	 * This method initializes jJToolBarBar
	 * 
	 * @return javax.swing.JToolBar
	 */
    public JToolBar getToolBar() {
        if (mToolBar == null) {
            mToolBar = new JToolBar();
            mToolBar.setPreferredSize(new java.awt.Dimension(790, 28));
        }
        return mToolBar;
    }

    /**
	 * This method initializes jJMenuBar
	 * 
	 * @return javax.swing.JMenuBar
	 */
    public JMenuBar getJMenuBar() {
        if (mMenuBar == null) {
            mMenuBar = new JMenuBar();
            mMenuBar.add(getJMenu());
        }
        return mMenuBar;
    }

    /**
	 * This method initializes jMenu
	 * 
	 * @return javax.swing.JMenu
	 */
    public JMenu getJMenu() {
        if (jMenu == null) {
            jMenu = new JMenu();
        }
        return jMenu;
    }

    /**
	 * This method initializes jDesktopPane
	 * 
	 * @return javax.swing.JDesktopPane
	 */
    public JDesktopPane getDesktop() {
        if (mDesktop == null) {
            mDesktop = new JDesktopPane();
        }
        return mDesktop;
    }

    public JPanel getStatusPanel() {
        if (mStatusPanel == null) {
            mStatusPanel = new JPanel();
            mStatusPanel.add(getStatusTextLabel());
            mStatusPanel.add(getProgressBar());
            mStatusPanel.setVisible(true);
            getProgressBar().setVisible(true);
            getStatusTextLabel().setVisible(true);
        }
        return mStatusPanel;
    }

    public void setStatusPanel(JPanel pStatusPanel) {
        mStatusPanel = pStatusPanel;
    }

    public JProgressBar getProgressBar() {
        if (mProgressBar == null) {
            mProgressBar = new JProgressBar();
        }
        return mProgressBar;
    }

    public JLabel getStatusTextLabel() {
        if (mStatusTextLabel == null) {
            mStatusTextLabel = new JLabel();
            mStatusTextLabel.setText("...");
        }
        return mStatusTextLabel;
    }

    public static void startProgressIndeterminate(String pString) {
        ImageIcon icon = Persdocmanager.getImageLoader().getImage("working.png");
        showSplash(pString, icon);
    }

    private static void showSplash(String pString, ImageIcon pIcon) {
        mSplashshow = true;
        mSplash = new Splash(pString, pIcon);
        mSplash.setTitle(pString);
        UseMainFrame.setCenter(mSplash);
        mSplash.setUndecorated(true);
        mSplash.setVisible(true);
        mSplash.getProgressBar().setIndeterminate(true);
        if (Persdocmanager.main != null) {
            Persdocmanager.main.setEnabled(false);
        }
    }

    public void stopProgress() {
        if (mSplash != null) {
            mSplash.dispose();
        }
        if (Persdocmanager.main != null) {
            Persdocmanager.main.setEnabled(true);
        }
        mSplashshow = false;
    }

    public void setStatusLabelText(String pText) {
        getStatusTextLabel().setText(pText);
        getStatusTextLabel().repaint();
    }
}
