package jhomenet.ui.window;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import jhomenet.ui.DesktopView;
import org.apache.log4j.Logger;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * The <code>AbstractInternalFrame</code> is the base class for all jHomenet
 * internal frame GUI windows. The window is an internal frame with main panel which
 * in turn holds a status bar (Borderlayout.SOUTH) and the built GUI
 * elements (Borderlayout.CENTER). 
 * <br>
 * By default, the status bar is set to null and if required the concrete class
 * should override the <code>buildStatusBar</code> method.
 * <br>
 * Id: $Id: AbstractWindow.java 1279 2006-02-08 23:00:24Z dhirwinjr $
 * 
 * @author dhirwinjr
 */
public abstract class AbstractInternalFrame {

    /**
	 * Define a logging mechanism.
	 */
    private static Logger logger = Logger.getLogger(AbstractInternalFrame.class.getName());

    /**
	 * Internal frame reference
	 */
    private JInternalFrame internalFrame;

    /**
	 * Whether or not the interal frame window is resizable.
	 */
    private final boolean resizable;

    /**
	 * Buttons
	 */
    private JButton ok_b, cancel_b;

    /**
	 * Reference to the window's status bar
	 */
    private JStatusBar statusBar;

    /**
	 * Default constructor.
	 */
    public AbstractInternalFrame() {
        this(false);
    }

    /**
	 * Constructor.
	 * 
	 * @param resizable
	 */
    public AbstractInternalFrame(boolean resizable) {
        super();
        this.resizable = resizable;
    }

    /**
	 * Create an internal frame.
	 *
	 * @return A reference to a newly created internal frame
	 */
    public JInternalFrame createInternalFrame() {
        if (internalFrame == null) internalFrame = createInternalFrame(resizable);
        return internalFrame;
    }

    /**
	 * Load an object into the internal frame. By default, this method does nothing.
	 * This method is intended to be overridden if desired.
	 * 
	 * @param <T>
	 * @param obj
	 */
    public <T> void loadObject(T obj) {
    }

    /**
	 * Get the window title.
	 *
	 * @return The window title
	 */
    protected abstract String getWindowTitle();

    /**
	 * Build the GUI. The GUI layout defaults to a Border layout, however if other
	 * layout managers are required, simply add another JPanel to this panel 
	 * (ie add(somePanel, BorderLayout.CENTER)).
	 * 
	 * This method is called when the window's <code>createAsInternalFrame</code>
	 * method is called. Simply instantiating the window does not build it.
	 * 
	 * @return A reference to the newly created internal <code>JPanel</code>
	 */
    protected abstract JPanel buildInternalPanel();

    /**
	 * Notify implementations that the "OK" button has been clicked. By default, this
	 * method does nothing. This method is intended to be overridden if desired.
	 */
    protected void okButtonClicked() {
    }

    /**
	 * Notify implementations that the internal window is closing. By default, this method
	 * does nothing. This method is intended to be overridden if desired. 
	 */
    protected void internalWindowIsClosing() {
    }

    /**
	 * Called by a parent object to request that the implementation reset its GUI. By default,
	 * this method does nothing. This method is intended to be overridden if desired.
	 */
    protected void resetInternalFrame() {
    }

    /**
	 * Build the status bar. 
	 * <br>
	 * By default, the operation doesn't build anything.
	 * Classes that extend this class and want a status bar should override
	 * this method and create the desired status bar.
	 *
	 * @return A status bar panel
	 */
    protected JStatusBar buildStatusBar() {
        return null;
    }

    /**
	 * Get the window identifier. This identifier is used as the control in not
	 * allowing multiple windows of the same type to be opened. For example, two
	 * services status window should not allowed to be opened. In general, the
	 * system will not create another window if the identifier is already in the
	 * current list.
	 *
	 * @return The window's unique identifier
	 */
    public abstract String getWindowId();

    /**
	 * Defines a hook for adding a menu bar to windows. By default, the abstract
	 * window class does not provide a complete implementation of this method. If
	 * a subclass wishes to create and install a menu bar, it should override this
	 * method and provide the necessary <code>JMenuBar</code> object. 
	 * 
	 * @return Reference to a menu bar
	 */
    protected JMenuBar getMenuBar() {
        return null;
    }

    /**
	 * Try to close the internal frame. The method is available to
	 * classes that extend this base class, but it may not be overriden.
	 */
    protected final void closeInternalFrame() {
        internalWindowIsClosing();
        internalFrame.dispose();
        internalFrame = null;
        DesktopView.windowClosed(getWindowId());
    }

    /**
	 * Provide access to the actual internal frame object.
	 * 
	 * @return A reference to the internal frame object
	 */
    protected JComponent getFrameReference() {
        return internalFrame;
    }

    /**
	 * Create an internal frame.
	 * 
	 * @param resizable
	 * @return A reference to a newly created intenral frame
	 */
    private final JInternalFrame createInternalFrame(boolean resizable) {
        logger.debug("Creating internal frame");
        JInternalFrame iFrame = new JInternalFrame(getWindowTitle(), resizable, true, false, true);
        iFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        iFrame.addInternalFrameListener(new InternalFrameAdapter() {

            public void internalFrameClosing(InternalFrameEvent e) {
                closeInternalFrame();
            }
        });
        iFrame.setContentPane(buildMainPanel());
        JMenuBar menuBar = getMenuBar();
        if (menuBar != null) iFrame.setJMenuBar(menuBar);
        Dimension size = getWindowDimension();
        iFrame.setSize(size);
        iFrame.setLocation(getDesiredLocation());
        iFrame.pack();
        return iFrame;
    }

    /**
	 * Build the internal frame's main panel.
	 * 
	 * @return A reference to the internal frame's main panel
	 */
    private final JPanel buildMainPanel() {
        FormLayout panelLayout = new FormLayout("4dlu, c:p, 4dlu", "4dlu, p, 4dlu, p, 4dlu, p, 4dlu");
        CellConstraints cc = new CellConstraints();
        PanelBuilder builder = new PanelBuilder(panelLayout);
        builder.add(buildInternalPanel(), cc.xy(2, 2));
        builder.add(buildButtonBar(), cc.xy(2, 4));
        JStatusBar statusBar = buildStatusBar();
        if (statusBar != null) builder.add(statusBar, cc.xy(2, 6));
        return builder.getPanel();
    }

    /**
	 * Build the button bar.
	 * 
	 * @return A reference to the newly created button <code>JPanel</code>
	 */
    private final JPanel buildButtonBar() {
        ok_b = new JButton("OK");
        ok_b.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                okButtonClicked();
                closeInternalFrame();
            }
        });
        cancel_b = new JButton("Cancel");
        cancel_b.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                closeInternalFrame();
            }
        });
        return ButtonBarFactory.buildRightAlignedBar(ok_b, cancel_b);
    }

    /**
	 * Add a child frame.
	 *
	 * @param childWindow
	 */
    protected void addChildWindow(AbstractInternalFrame childWindow) {
        DesktopView.addWindowToDesktop(childWindow);
    }

    /**
	 * Get the desired window dimension.
	 *
	 * @return The desired window dimension
	 */
    protected Dimension getWindowDimension() {
        return new Dimension(375, 385);
    }

    /**
	 * Get the desired internal frame position.
	 *
	 * @return Desired frame position
	 */
    protected Point getDesiredLocation() {
        return new Point(0, 0);
    }

    /**
	 * Indicate that work has started.
	 */
    public final void startWork() {
        if (statusBar != null) {
            statusBar.startProgress();
        }
    }

    /**
	 * Indicate that work has started with the given text description.
	 *
	 * @param text Description of work being completed
	 */
    public final void startWork(String text) {
        if (statusBar != null) {
            statusBar.startProgress(text);
        }
    }

    /**
	 * Indicate that work has stopped.
	 */
    public final void stopWork() {
        if (statusBar != null) {
            statusBar.stopProgress();
        }
    }

    /**
	 * Indicate that work has stopped with the given text description.
	 *
	 * @param text
	 */
    public final void stopWork(String text) {
        if (statusBar != null) {
            statusBar.stopProgress(text);
        }
    }
}
