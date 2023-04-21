package org.yjchun.hanghe.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.beans.PropertyVetoException;
import java.io.File;
import java.util.HashMap;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import org.jdesktop.application.SingleFrameApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yjchun.hanghe.ConfigController;
import org.yjchun.hanghe.Global;
import org.yjchun.hanghe.chart.ChartManager;
import org.yjchun.hanghe.device.DeviceManager;
import org.yjchun.hanghe.device.DeviceMonitorPane;
import org.yjchun.hanghe.util.Misc;
import org.yjchun.hanghe.util.event.DirectEventDispatcher;
import ch.qos.logback.classic.LoggerContext;
import bsh.EvalError;
import bsh.Interpreter;
import bsh.util.JConsole;

/**
 * @author yjchun
 *
 */
public class MainFrame extends SingleFrameApplication {

    protected static final Logger log = LoggerFactory.getLogger(MainFrame.class);

    public static DirectEventDispatcher eventManager;

    protected JDesktopPane desktop;

    protected JToolBar toolPalette;

    protected CursorDataWindow cursorDataWindow;

    protected MenuBuilder menuBuilder;

    @Override
    protected void initialize(java.lang.String[] args) {
        Misc.setLogLevelFor(LoggerContext.ROOT_NAME, "DEBUG");
        if (!ConfigController.getInstance().initConfig()) throw new RuntimeException("Configuration failed");
        Misc.initLogger();
        if (!ChartManager.getInstance().initialize()) {
            Global.config.setProperty("layer.raster-chart.enabled", false);
        }
        eventManager = new DirectEventDispatcher();
    }

    @Override
    protected void startup() {
        menuBuilder = new MenuBuilder(this);
        getMainFrame().setJMenuBar(menuBuilder.menuBar);
        toolPalette = menuBuilder.toolBar;
        getMainFrame().add(toolPalette, BorderLayout.WEST);
        desktop = new JDesktopPane();
        desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
        desktop.setBackground(Color.gray);
        show(desktop);
        cursorDataWindow = new CursorDataWindow();
        desktop.add(cursorDataWindow, JLayeredPane.PALETTE_LAYER);
        cursorDataWindow.setLocation(desktop.getWidth() - 100, 100);
        cursorDataWindow.setVisible(true);
        JInternalFrame f = newChartWindow();
        try {
            f.setMaximum(true);
            f.setSelected(true);
        } catch (PropertyVetoException e) {
        }
    }

    @Override
    protected void ready() {
        DeviceManager.getInstance().setGPSEnabled(true);
    }

    @Override
    protected void shutdown() {
        log.info("Application is terminated");
        DeviceManager.getInstance().closeAll();
        ConfigController.getInstance().saveConfig();
        super.shutdown();
    }

    protected void processAction(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("open")) {
            JOptionPane.showMessageDialog(getMainFrame(), "Open a map file");
        } else if (cmd.equals("quit")) {
            exit();
        } else if (cmd.equals("deviceOption")) {
            deviceOption();
        } else if (cmd.equals("bshconsole")) {
            bshconsole();
        } else if (cmd.equals("gpsMonitor")) {
            gpsMonitor();
        } else if (cmd.equals("zoomin")) {
            ChartView view = getSelectedChartView();
            if (view != null) view.zoom(1);
        } else if (cmd.equals("zoomout")) {
            ChartView view = getSelectedChartView();
            if (view != null) view.zoom(-1);
        } else if (cmd.equals("zoom")) {
        } else if (cmd.equals("pan")) {
        } else if (cmd.equals("about")) {
            about();
        } else if (cmd.equals("newWindow")) {
            newChartWindow();
        } else if (cmd.equals("GC")) {
            System.gc();
        } else if (cmd.equals("viewLog")) {
            String logfile = new File(Global.config.getString("log.file.path"), Global.appName + ".log").getPath();
            UIUtils.openEditorFor(logfile);
        } else if (cmd.equals("LNF")) {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception e1) {
            }
            SwingUtilities.updateComponentTreeUI(getMainFrame());
        } else if (cmd.equals("bilinear")) {
            Global.config.setProperty("layer.raster-chart.bilinear", !Global.config.getBoolean("layer.raster-chart.bilinear"));
            ChartView view = getSelectedChartView();
            if (view != null) view.repaint();
        } else if (cmd.equals("test")) {
            PaletteWindow w = new PaletteWindow("Cursor");
            desktop.add(w, JLayeredPane.PALETTE_LAYER);
            w.setLocation(100, 100);
            w.setVisible(true);
        } else {
            log.debug("Unhandled command: {}", e.getActionCommand());
        }
    }

    public void deviceOption() {
        int ret = JOptionPane.showConfirmDialog(getMainFrame(), "Option dialog is not implemented yet.\nEdit following file manually and start application again:\n" + ConfigController.DEFAULT_USER_CONFIG, "Device Settings", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
        if (ret == JOptionPane.OK_OPTION) {
            ConfigController.getInstance().saveConfig();
            UIUtils.openEditorFor(ConfigController.DEFAULT_USER_CONFIG);
        }
    }

    public void bshconsole() {
        final JConsole console = new JConsole();
        console.setPreferredSize(new Dimension(430, 240));
        Interpreter interpreter = new Interpreter(console);
        try {
            interpreter.eval("setAccessibility(true)");
            interpreter.eval("import org.yjchun.hanghe.geographic.*");
        } catch (EvalError e) {
        }
        new Thread(interpreter).start();
        JDialog jdlg = new JDialog(getMainFrame(), "BeanShell Console");
        jdlg.setLayout(new BorderLayout());
        jdlg.add(console, BorderLayout.CENTER);
        jdlg.setLocation(getMainFrame().getLocation());
        jdlg.pack();
        jdlg.setVisible(true);
    }

    public void gpsMonitor() {
        String device = Global.config.getString("device.gps.port");
        if (device == null || device.length() == 0) {
            UIUtils.showErrorDialog("GPS Device is configured");
            return;
        }
        JDialog dlg = DeviceMonitorPane.createDialog(null, device);
        dlg.setLocation(getMainFrame().getLocation());
    }

    public void about() {
        JOptionPane.showMessageDialog(getMainFrame(), Global.appDesc);
    }

    public JInternalFrame newChartWindow() {
        ChartFrame frame = new ChartFrame();
        desktop.add(frame);
        frame.setVisible(true);
        return frame;
    }

    public ChartView getSelectedChartView() {
        JInternalFrame frame = desktop.getSelectedFrame();
        if (frame instanceof ChartFrame) {
            return ((ChartFrame) frame).getChartView();
        }
        return null;
    }
}
