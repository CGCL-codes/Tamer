package net.liveseeds.eye;

import net.liveseeds.base.*;
import net.liveseeds.base.world.DefaultWorld;
import net.liveseeds.base.world.World;
import net.liveseeds.errorutil.ErrorUtil;
import net.liveseeds.eye.actions.*;
import net.liveseeds.eye.detailsview.DetailsView;
import net.liveseeds.eye.detailsview.WorldTreeRenderer;
import net.liveseeds.runner.DefaultRunner;
import net.liveseeds.runner.Runner;
import net.liveseeds.eye.selection.CellSelectionManager;
import net.liveseeds.eye.selection.DefaultCellSelectionManager;
import net.liveseeds.eye.worldview.WorldView;
import net.liveseeds.statistics.StatisticsView;
import net.liveseeds.eye.view.View;
import net.liveseeds.eye.domination.DominationView;
import net.liveseeds.eye.imageutil.ImageUtil;
import org.apache.log4j.Logger;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ResourceBundle;

/**
 * <a href="mailto:misha@ispras.ru>Mikhail Ksenzov</a>
 */
public class Eye {

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(Eye.class.getName());

    private static final Logger logger = Logger.getLogger(Eye.class);

    private static final double DIVIDER_LOCATION = 0.3d;

    private static final int WIDTH = 800;

    private static final int HEIGHT = 600;

    public static JFrame frame;

    private final CellSelectionManager cellSelectionManager = new DefaultCellSelectionManager();

    private final Driver driver;

    private final Runner runner;

    private World world;

    private final NewAction newAction;

    private final OpenAction openAction;

    private final SaveAction saveAction;

    private final CloseAction closeAction;

    private final StartAction startAction;

    private final StepAction stepAction;

    private final PauseAction pauseAction;

    private final AboutAction aboutAction;

    private JSplitPane splitPane;

    private StatusBar statusBar;

    private final JTabbedPane graphicsTabs = new JTabbedPane();

    private final JTabbedPane navigationTabs = new JTabbedPane();

    private final View[] views = new View[] { new WorldView(cellSelectionManager), new StatisticsView(), new DetailsView(cellSelectionManager), new DominationView() };

    private final BackupCreator backupCreator;

    static {
        try {
            if (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0) {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }

    private Eye(final Driver driver) {
        if (logger.isDebugEnabled()) {
            logger.debug("Eye(" + driver + ')');
        }
        this.driver = driver;
        driver.addDriverListener(new DriverListenerImpl());
        frame = new JFrame(RESOURCE_BUNDLE.getString("frame.caption"));
        ErrorUtil.setOwner(frame);
        try {
            frame.setIconImage(ImageUtil.loadImage(frame, WorldTreeRenderer.class.getResource("active_instruction.jpg")));
        } catch (InterruptedException e) {
            ErrorUtil.showError(e);
        }
        runner = new DefaultRunner(driver);
        backupCreator = new BackupCreator(this, runner);
        newAction = new NewAction(this, runner);
        saveAction = new SaveAction(this, runner);
        openAction = new OpenAction(this, runner);
        closeAction = new CloseAction(this);
        startAction = new StartAction(runner);
        pauseAction = new PauseAction(runner);
        stepAction = new StepAction(driver, runner);
        aboutAction = new AboutAction();
        setUpFrame();
        initMenuBar();
        initToolBar();
        try {
            setWorld(new DefaultWorld());
        } catch (LiveSeedsException e) {
            ErrorUtil.showError(e);
        }
        frame.setVisible(true);
    }

    public World getWorld() {
        return world;
    }

    private void setUpFrame() {
        frame.setLocation(0, 0);
        frame.setSize(WIDTH, HEIGHT);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.getContentPane().setLayout(new BorderLayout());
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setOneTouchExpandable(true);
        splitPane.setLeftComponent(navigationTabs);
        splitPane.setRightComponent(graphicsTabs);
        splitPane.setDividerLocation(DIVIDER_LOCATION);
        splitPane.setResizeWeight(DIVIDER_LOCATION);
        frame.getContentPane().add(splitPane, BorderLayout.CENTER);
        statusBar = new StatusBar(driver);
        frame.getContentPane().add(statusBar, BorderLayout.SOUTH);
        frame.addWindowListener(new EyeWindowListener(this));
        for (int i = 0; i < views.length; i++) {
            final View view = views[i];
            addView(view);
        }
    }

    public void setWorld(final World world) {
        if (this.world != null) {
            driver.removeDriverListener(this.world);
        }
        cellSelectionManager.clearSelection();
        this.world = world;
        statusBar.setWorld(world);
        backupCreator.init(world, driver);
        driver.addDriverListener(world);
        for (int i = 0; i < views.length; i++) {
            final View view = views[i];
            view.init(runner, driver, world);
        }
        frame.invalidate();
        frame.validate();
        frame.repaint();
    }

    public void addView(final View view) {
        if (view.getLocation() == View.GRAPHICS_LOCATION) {
            graphicsTabs.add(view.getName(), view.getView());
        } else if (view.getLocation() == View.NAVIGATION_LOCATION) {
            navigationTabs.add(view.getName(), view.getView());
        }
    }

    public void close() {
        runner.pause();
        System.exit(0);
    }

    private void initMenuBar() {
        final JMenuBar menuBar = new JMenuBar();
        final JMenu mainMenu = new JMenu(RESOURCE_BUNDLE.getString("menu.main"));
        mainMenu.add(newAction).setIcon(null);
        mainMenu.add(saveAction).setIcon(null);
        mainMenu.add(openAction).setIcon(null);
        mainMenu.addSeparator();
        mainMenu.add(closeAction);
        menuBar.add(mainMenu);
        final JMenu timeMenu = new JMenu(RESOURCE_BUNDLE.getString("menu.time"));
        timeMenu.add(startAction).setIcon(null);
        timeMenu.add(pauseAction).setIcon(null);
        timeMenu.add(stepAction).setIcon(null);
        menuBar.add(timeMenu);
        final JMenu helpMenu = new JMenu(RESOURCE_BUNDLE.getString("menu.help"));
        helpMenu.add(aboutAction);
        menuBar.add(helpMenu);
        frame.setJMenuBar(menuBar);
    }

    private void initToolBar() {
        final JToolBar toolBar = new JToolBar();
        toolBar.add(newAction);
        toolBar.add(saveAction);
        toolBar.add(openAction);
        toolBar.addSeparator();
        toolBar.add(stepAction);
        toolBar.addSeparator();
        toolBar.add(startAction);
        toolBar.add(pauseAction);
        frame.getContentPane().add(toolBar, BorderLayout.NORTH);
    }

    public Driver getDriver() {
        return driver;
    }

    public void setMessage(final String message) {
        statusBar.setMessage(message);
    }

    private final class EyeWindowListener extends WindowAdapter {

        private Eye eye;

        EyeWindowListener(final Eye eye) {
            this.eye = eye;
        }

        public void windowClosing(final WindowEvent e) {
            super.windowClosing(e);
            eye.close();
        }
    }

    private final class DriverListenerImpl implements DriverListener {

        public void moveTime() {
            if (world.getLiveSeedsAmount() == 0) {
                runner.pause();
                JOptionPane.showMessageDialog(frame, RESOURCE_BUNDLE.getString("message.world.empty"), "", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    public static void main(final String[] args) throws Exception {
        LoggerUtil.configureLogger();
        new SplashScreen(frame, true);
        final DefaultDriver driver = new DefaultDriver();
        new Eye(driver);
    }
}
