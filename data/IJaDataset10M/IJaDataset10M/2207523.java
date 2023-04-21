package de.schwarzrot.app.support;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Window;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.tree.DefaultMutableTreeNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.MessageSource;
import org.springframework.context.event.ContextRefreshedEvent;
import de.schwarzrot.app.Application;
import de.schwarzrot.app.config.ApplicationConfig;
import de.schwarzrot.data.transaction.TOSave;
import de.schwarzrot.data.transaction.Transaction;
import de.schwarzrot.data.transaction.support.TransactionFactory;
import de.schwarzrot.ui.Desktop;
import de.schwarzrot.ui.MainFrame;
import de.schwarzrot.ui.RootWindowHandler;
import de.schwarzrot.ui.action.ApplicationActionHandler;
import de.schwarzrot.ui.image.ImageFactory;
import de.schwarzrot.ui.support.AbstractStatusBar;

/**
 * base class for a pluggable application. Application means some kind of
 * controller with a main window. The meaning of "main window" depends on the
 * configurated active desktop.
 * 
 * Basic stuff stil to be implemented by any descendants:
 * <dl>
 * <dt><b>createPane()</b></dt>
 * <dd>the most important function to implement, as it determines the look of
 * the application (the content of the toplevel window).</dd>
 * <dt><b>createStatusBar()</b></dt>
 * <dd>create an implementation of <b>StatusBar</b> or just a subclass of
 * <b>AbstractStatusBar</b></dd>
 * <dt><b>ActionHandler</b></dt>
 * <dd>subclass an <b>AbstractActionHandler</b> to define the Actions and link
 * the callbacks to the application-functions.</dd>
 * <dt><b>the application-functions</b></dt>
 * <dd>linked to useractions via ActionHandlers.</dd>
 * </dl>
 * <p>
 * The {@code Application} uses the Applets lifecycle, as it is loaded at
 * startup and stay alive until the applications frame terminates.
 * <dl>
 * <dt>init</dt>
 * <dd>called when the application is loaded, but does not have an visual
 * context yet</dd>
 * <dt>start</dt>
 * <dd>called when the visual context is created and the application should
 * start user interaction</dd>
 * <dt>stop</dt>
 * <dd>called when the user closes the visual context. Depending on the active
 * desktop, there may be other applications active, so the {@code Application}
 * will stay alive, without visual context and of cause withou user interation</dd>
 * <dt>destroy</dt>
 * <dd>called when the application frame terminates. Depending on the active
 * desktop, this may happen on closing the applications window or on closing the
 * desktops window.</dd>
 * </dl>
 * 
 * @author <a href="mailto:rmantey@users.sourceforge.net">Reinhard Mantey</a>
 * @param <C>
 *            - config type for application
 * 
 */
public abstract class AbstractApplication<C extends ApplicationConfig> extends JApplet implements Application<C> {

    protected static RootWindowHandler desktop;

    protected static MessageSource msgSource;

    protected static TransactionFactory taFactory;

    private static final long serialVersionUID = 713L;

    protected AbstractApplication(String name) {
        initialized = false;
        defaultMaximized = false;
        super.setName(name);
    }

    @Override
    public abstract AbstractStatusBar createStatusBar();

    @Override
    public final ApplicationActionHandler getActionHandler() {
        return handler;
    }

    @Override
    public final C getAppConfig() {
        if (appConfig == null) {
            throw new UnsupportedOperationException("must set an config object for class " + getClass().getName() + "!");
        }
        return appConfig;
    }

    @Override
    public final ClassLoader getClassLoader() {
        return classLoader;
    }

    @Override
    public Container getContentPane() {
        if (!initialized) {
            getRootPane().setContentPane(createPane());
            initialized = true;
        }
        return super.getContentPane();
    }

    @Override
    public final JToolBar getExtraToolBar(int orientation) {
        if (extraTB == null) extraTB = getActionHandler().createExtraToolBar();
        if (extraTB != null) extraTB.setOrientation(orientation);
        return extraTB;
    }

    @Override
    public final DefaultMutableTreeNode getHelpRoot() {
        return helpRoot;
    }

    @Override
    public JComponent getInfo() {
        ImageFactory imgFactory = ApplicationServiceProvider.getService(ImageFactory.class);
        if (msgSource == null) msgSource = ApplicationServiceProvider.getService(MessageSource.class);
        ImageIcon icon = imgFactory.createIcon(getName() + ".app.icon");
        String appInfo = msgSource.getMessage(getName() + ".app.info.text", null, null);
        JLabel info = new JLabel(String.format(appInfo, getVersion()), icon, JLabel.LEFT);
        return info;
    }

    public final Log getLogger() {
        return LogFactory.getLog(getClass());
    }

    @Override
    public final Window getMainWindow() {
        Window rv = null;
        if (getTopWindow() != null) rv = getTopWindow().getWindow();
        return rv;
    }

    @Override
    public final JMenuBar getMenuBar() {
        return menuBar;
    }

    @Override
    public final Point getStartupPos(int defX, int defY) {
        Point startupPos = new Point(defX, defY);
        if (this.getAppConfig() != null) startupPos = getAppConfig().getStartupPosition();
        return startupPos;
    }

    @Override
    public Dimension getStartupSize(Dimension defaultSize) {
        Dimension startupSize = defaultSize;
        if (this.getAppConfig() != null) startupSize = getAppConfig().getStartupSize();
        return startupSize;
    }

    @Override
    public final AbstractStatusBar getStatusBar() {
        if (statusBar == null) statusBar = createStatusBar();
        return statusBar;
    }

    @Override
    public final String getTitleKey() {
        return new StringBuilder(getName()).append(".title").toString();
    }

    @Override
    public final JToolBar getToolBar() {
        return toolBar;
    }

    @Override
    public final MainFrame getTopWindow() {
        return topWindow;
    }

    @Override
    public final String getVersion() {
        return appConfig.getVersion();
    }

    @Override
    public final void init() {
        super.init();
        if (handler != null) handler.init();
        if (taFactory == null) taFactory = ApplicationServiceProvider.getService(TransactionFactory.class);
        setup();
    }

    public boolean isCacheable() {
        return false;
    }

    public final boolean isDefaultMaximized() {
        return defaultMaximized;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent arg0) {
        getLogger().error("unimplemented " + getClass().getName() + "::onApplicationEvent() - event: " + arg0);
        if (arg0 instanceof ContextRefreshedEvent) return;
        throw new UnsupportedOperationException("need to implement onApplicationEvent(ApplicationEvent e) for class" + getClass().getName());
    }

    @Override
    public boolean preExit() {
        return true;
    }

    @Override
    public final void refresh() {
        topWindow.validate();
        topWindow.repaint();
    }

    public final void setActionHandler(ApplicationActionHandler handler) {
        this.handler = handler;
    }

    @Override
    public final void setAppConfig(C appConfig) {
        this.appConfig = appConfig;
    }

    @Override
    public final void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public final void setDefaultMaximized(boolean defaultMaximized) {
        this.defaultMaximized = defaultMaximized;
    }

    @Override
    public final void setHelpRoot(DefaultMutableTreeNode helpRoot) {
        this.helpRoot = helpRoot;
    }

    @Override
    public final void setMenuBar(JMenuBar menuBar) {
        this.menuBar = menuBar;
    }

    @Override
    public final void setName(String name) {
        getLogger().error("OUPS - someone tries to change my name? - Nope!!! (" + getName() + " <> " + name);
    }

    @Override
    public void setTitle(String title) {
        StringBuilder sb = new StringBuilder();
        if (msgSource == null) msgSource = ApplicationServiceProvider.getService(MessageSource.class);
        sb.append(msgSource.getMessage(Desktop.ID_TITLE, null, getName(), null));
        sb.append(" - ");
        sb.append(msgSource.getMessage(getTitleKey(), null, getName(), null));
        if (title != null) {
            sb.append(": ");
            sb.append(title);
        }
        getTopWindow().setTitle(sb.toString());
    }

    @Override
    public final void setToolBar(JToolBar toolBar) {
        this.toolBar = toolBar;
    }

    @Override
    public void setTopWindow(MainFrame topWindow) {
        this.topWindow = topWindow;
    }

    @Override
    public void start() {
        if (!initialized) {
            getRootPane().setContentPane(createPane());
            initialized = true;
        }
    }

    @Override
    public void stop() {
        MainFrame c = getTopWindow();
        if (taFactory == null) taFactory = ApplicationServiceProvider.getService(TransactionFactory.class);
        if (c != null && taFactory != null) {
            appConfig.setStartupSize(c.getSize());
            appConfig.setStartupPosition(c.getLocation());
            try {
                Transaction ta = taFactory.createTransaction();
                ta.add(new TOSave<C>(appConfig));
                ta.execute();
            } catch (Throwable t) {
            }
        }
        statusBar = null;
        topWindow = null;
        menuBar = null;
        toolBar = null;
        extraTB = null;
        initialized = false;
    }

    @Override
    public String toString() {
        return getName();
    }

    protected abstract JComponent createPane();

    protected boolean notImplementedYet() {
        if (msgSource == null) msgSource = ApplicationServiceProvider.getService(MessageSource.class);
        boolean rv = true;
        String message = msgSource.getMessage(ID_NOT_IMPLEMENTED, null, ID_NOT_IMPLEMENTED, null);
        String title = msgSource.getMessage(TITLE_NOT_IMPLEMENTED, null, TITLE_NOT_IMPLEMENTED, null);
        getLogger().error(message);
        JOptionPane.showMessageDialog(getMainWindow(), message, title, JOptionPane.INFORMATION_MESSAGE);
        return rv;
    }

    protected abstract void setup();

    private C appConfig;

    private ClassLoader classLoader;

    private boolean defaultMaximized;

    private JToolBar extraTB;

    private ApplicationActionHandler handler;

    private DefaultMutableTreeNode helpRoot;

    private boolean initialized;

    private JMenuBar menuBar;

    private AbstractStatusBar statusBar;

    private JToolBar toolBar;

    private MainFrame topWindow;
}
