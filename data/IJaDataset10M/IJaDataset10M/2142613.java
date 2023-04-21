package de.xirp.ui.widgets.panels;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import de.xirp.io.ConnectionObject;
import de.xirp.io.comm.CommunicationManager;
import de.xirp.io.event.ConnectionEvent;
import de.xirp.io.event.ConnectionListener;
import de.xirp.plugin.IPlugable;
import de.xirp.plugin.IPluginFilter;
import de.xirp.plugin.PluginI18NHandler;
import de.xirp.plugin.PluginManager;
import de.xirp.plugin.PluginType;
import de.xirp.plugin.VisualizationType;
import de.xirp.profile.Robot;
import de.xirp.ui.Application;
import de.xirp.ui.event.LocaleChangedEvent;
import de.xirp.ui.event.LocaleChangedListener;
import de.xirp.ui.util.ApplicationManager;
import de.xirp.ui.util.SWTUtil;
import de.xirp.ui.util.ressource.ImageManager;
import de.xirp.ui.util.ressource.ImageManager.SystemImage;
import de.xirp.ui.widgets.StopWatch;
import de.xirp.ui.widgets.custom.XCombo;
import de.xirp.ui.widgets.custom.XCoolBar;
import de.xirp.ui.widgets.custom.XCoolItem;
import de.xirp.ui.widgets.custom.XToolBar;
import de.xirp.ui.widgets.custom.XToolItem;
import de.xirp.ui.widgets.custom.XButton.XButtonType;
import de.xirp.ui.widgets.dialogs.XMessageBox;
import de.xirp.ui.widgets.dialogs.XMessageBox.HMessageBoxType;
import de.xirp.util.Constants;

/**
 * This user interface class represents the tool bar of the content
 * area.
 * 
 * @author Matthias Gernand
 * @author Rabea Gransberger
 */
public final class RobotToolbar {

    /**
	 * A string constant.
	 */
    private static final String MAP = "map";

    /**
	 * The logger for this class.
	 */
    private static final Logger logClass = Logger.getLogger(RobotToolbar.class);

    /**
	 * The parent composite.
	 */
    private Composite parent;

    /**
	 * The active robot.
	 */
    private Robot robot;

    /**
	 * Vector with the names of the classes.
	 */
    private final Vector<KeyClassHandlerTriple> protocolTriples = new Vector<KeyClassHandlerTriple>();

    /**
	 * Vector with the names of the communication classes.
	 */
    private final Vector<KeyClassHandlerTriple> interfaceTriples = new Vector<KeyClassHandlerTriple>();

    /**
	 * A combo box.
	 */
    private XCombo comboInterface;

    /**
	 * A tool bar separator.
	 */
    @SuppressWarnings("unused")
    private XToolItem separator;

    /**
	 * The stop watch of the content area shown in the tool bar.
	 */
    private StopWatch watch;

    /**
	 * A combo box.
	 */
    private XCombo comboProtocol;

    /**
	 * A cool bar.
	 */
    private XCoolBar coolBar;

    /**
	 * A tool bar.
	 */
    private XToolBar toolBar;

    /**
	 * A tool item.
	 */
    private XToolItem toolItemConnect;

    /**
	 * An image.
	 */
    private Image open;

    /**
	 * An image.
	 */
    private Image close;

    /**
	 * A tool bar separator.
	 */
    private XToolItem separatorInterface;

    /**
	 * A tool bar separator.
	 */
    private XToolItem separatorProtocol;

    /**
	 * Constructs a new tool bar used in the content area of the
	 * application.
	 * 
	 * @param parent
	 *            The parent composite.
	 * @param robot
	 *            The robot.
	 */
    public RobotToolbar(Composite parent, Robot robot) {
        this.parent = parent;
        this.robot = robot;
        for (IPlugable plugin : PluginManager.getPluginsForType(robot.getName(), PluginType.COMMUNICATION)) {
            interfaceTriples.add(new KeyClassHandlerTriple(plugin.getNameKey(), plugin.getInfo().getMainClass(), plugin.getHandler()));
        }
        for (IPlugable plugin : PluginManager.getPluginsForType(robot.getName(), PluginType.PROTOCOL)) {
            protocolTriples.add(new KeyClassHandlerTriple(plugin.getNameKey(), plugin.getInfo().getMainClass(), plugin.getHandler()));
        }
        init();
        correctLayout();
    }

    /**
	 * Corrects the layout.
	 */
    public void correctLayout() {
        ((GridData) coolBar.getLayoutData()).heightHint = coolBar.getSize().y;
        parent.layout();
        Application.getApplication().getShell().layout();
    }

    /**
	 * Initializes the tool bar and adds the content of the tool bar.
	 */
    private void init() {
        coolBar = new XCoolBar(parent, SWT.FLAT);
        coolBar.addControlListener(new ControlAdapter() {

            @Override
            public void controlResized(ControlEvent e) {
                correctLayout();
            }
        });
        GridData gd = SWTUtil.setGridData(coolBar, true, false, SWT.FILL, SWT.FILL, 1, 1);
        gd.heightHint = 30;
        XCoolItem coolItem = new XCoolItem(coolBar, SWT.DROP_DOWN);
        toolBar = new XToolBar(coolBar, SWT.FLAT);
        SWTUtil.setGridData(toolBar, true, false, SWT.FILL, SWT.BEGINNING, 1, 1);
        gd = (GridData) toolBar.getLayoutData();
        gd.heightHint = 30;
        open = ImageManager.getSystemImage(SystemImage.NOT_CONNECTED);
        close = ImageManager.getSystemImage(SystemImage.CONNECTED);
        toolItemConnect = new XToolItem(toolBar, SWT.PUSH);
        setConnected(false);
        toolItemConnect.addSelectionListener(new SelectionAdapter() {

            @SuppressWarnings("unchecked")
            @Override
            public void widgetSelected(@SuppressWarnings("unused") SelectionEvent e) {
                if (!CommunicationManager.isConnected(robot.getName())) {
                    try {
                        @SuppressWarnings("unused") String interfaceName = comboInterface.getText();
                        @SuppressWarnings("unused") String protocolName = comboProtocol.getText();
                        Map<String, String> interfaceMap = (Map<String, String>) comboInterface.getData(MAP);
                        Map<String, String> protocolMap = (Map<String, String>) comboProtocol.getData(MAP);
                        String interfaceClass = interfaceMap.get(interfaceName);
                        String protocolClass = protocolMap.get(protocolName);
                        ConnectionObject obj = new ConnectionObject(robot.getName(), interfaceClass, protocolClass);
                        CommunicationManager.connect(obj);
                    } catch (RuntimeException ex) {
                        logClass.error("Error: " + ex.getMessage() + Constants.LINE_SEPARATOR, ex);
                        XMessageBox box = new XMessageBox(parent.getShell(), HMessageBoxType.INFO, XButtonType.CLOSE);
                        box.setTextForLocaleKey("RobotToolbar.messagebox.title");
                        box.setMessageForLocaleKey("RobotToolbar.messagebox.message");
                        box.open();
                    }
                } else {
                    CommunicationManager.disconnect(robot.getName());
                    setConnected(false);
                }
            }
        });
        final ConnectionListener listener = new ConnectionListener() {

            public void connectionEstablished(ConnectionEvent event) {
                if (event.getRobotName().equals(robot.getName())) {
                    setConnected(true);
                }
            }

            public void disconnected(ConnectionEvent event) {
                if (event.getRobotName().equals(robot.getName())) {
                    setConnected(false);
                }
            }
        };
        CommunicationManager.addConnectionListener(listener);
        toolItemConnect.addDisposeListener(new DisposeListener() {

            public void widgetDisposed(DisposeEvent event) {
                CommunicationManager.removeConnectionListener(listener);
            }
        });
        final LocaleChangedListener localeListener = new LocaleChangedListener() {

            public void localeChanged(LocaleChangedEvent event) {
                comboProtocol.removeAll();
                comboProtocol.setData(MAP, null);
                comboInterface.removeAll();
                comboInterface.setData(MAP, null);
                initCombos();
            }
        };
        ApplicationManager.addLocaleChangedListener(localeListener);
        coolBar.addDisposeListener(new DisposeListener() {

            public void widgetDisposed(DisposeEvent event) {
                ApplicationManager.removeLocaleChangedListener(localeListener);
            }
        });
        comboInterface = new XCombo(toolBar, SWT.BORDER | SWT.READ_ONLY);
        comboProtocol = new XCombo(toolBar, SWT.BORDER | SWT.READ_ONLY);
        initCombos();
        coolBar.layout();
        comboInterface.layout();
        comboProtocol.layout();
        separatorInterface = new XToolItem(toolBar, SWT.SEPARATOR | SWT.FLAT);
        separatorInterface.setControl(comboInterface);
        separatorInterface.setWidth(150);
        separatorProtocol = new XToolItem(toolBar, SWT.SEPARATOR | SWT.FLAT);
        separatorProtocol.setControl(comboProtocol);
        separatorProtocol.setWidth(150);
        separator = new XToolItem(toolBar, SWT.SEPARATOR | SWT.FLAT);
        watch = new StopWatch(toolBar);
        coolItem.setControl(toolBar);
        List<IPlugable> plugins = PluginManager.getPlugins(robot.getName(), new IPluginFilter() {

            public boolean filterPlugin(IPlugable plugin) {
                return PluginType.containsType(plugin, PluginType.TOOLBAR) && VisualizationType.containsType(plugin, VisualizationType.ROBOT_TOOLBAR);
            }
        });
        Vector<XCoolItem> coolItems = new Vector<XCoolItem>();
        for (IPlugable plugin : plugins) {
            XCoolItem parentItem = new XCoolItem(coolBar, SWT.DROP_DOWN);
            ToolBar bar = plugin.getToolBar(parentItem);
            parentItem.setControl(bar);
            coolItems.add(parentItem);
        }
        Point size = toolBar.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        Point coolSize = coolItem.computeSize(size.x, size.y);
        coolItem.setSize(coolSize);
        for (XCoolItem itm : coolItems) {
            Point locSize = itm.getControl().computeSize(SWT.DEFAULT, SWT.DEFAULT);
            Point locCoolSize = coolItem.computeSize(locSize.x, locSize.y);
            itm.setSize(locCoolSize);
        }
        coolItems.clear();
    }

    /**
	 * 
	 */
    private void initCombos() {
        final Map<String, String> interfaceNameClassMap = new HashMap<String, String>();
        final Vector<String> interfaceNames = new Vector<String>();
        for (KeyClassHandlerTriple t : interfaceTriples) {
            String name = t.getTranslationForKey();
            interfaceNames.add(name);
            interfaceNameClassMap.put(name, t.getClazz());
        }
        comboInterface.setData(MAP, interfaceNameClassMap);
        for (String name : interfaceNames) {
            comboInterface.add(name);
        }
        comboInterface.select(0);
        if (interfaceNames.size() == 0) {
            comboInterface.setEnabled(false);
        }
        final Map<String, String> protocolNameClassMap = new HashMap<String, String>();
        final Vector<String> protocolNames = new Vector<String>();
        for (KeyClassHandlerTriple t : protocolTriples) {
            String name = t.getTranslationForKey();
            protocolNames.add(name);
            protocolNameClassMap.put(name, t.getClazz());
        }
        comboProtocol.setData(MAP, protocolNameClassMap);
        for (String name : protocolNames) {
            comboProtocol.add(name);
        }
        comboProtocol.select(0);
        if (protocolNames.size() == 0) {
            comboProtocol.setEnabled(false);
        }
        if (protocolNames.size() == 0 && protocolNames.size() == 0) {
            toolItemConnect.setEnabled(false);
        }
    }

    /**
	 * Sets the connection status.
	 * 
	 * @param connected
	 *            <code>true</code>: connected.
	 */
    private void setConnected(final boolean connected) {
        SWTUtil.asyncExec(new Runnable() {

            public void run() {
                if (toolItemConnect == null || toolItemConnect.isDisposed()) {
                    return;
                }
                if (connected) {
                    toolItemConnect.setToolTipTextForLocaleKey("MainRobotPanelToolbar.gui.disconnectFrom", robot.getName());
                    toolItemConnect.setImage(close);
                } else {
                    toolItemConnect.setToolTipTextForLocaleKey("MainRobotPanelToolbar.gui.connectTo", robot.getName());
                    toolItemConnect.setImage(open);
                }
            }
        });
    }

    /**
	 * Starts the timer.
	 */
    protected void startWatch() {
        watch.start();
    }

    /**
	 * Pauses the timer.
	 */
    protected void pauseTime() {
        watch.pause();
    }

    /**
	 * Stops the timer.
	 */
    protected void stopTime() {
        watch.stop();
    }

    class KeyClassHandlerTriple {

        private String key;

        private String clazz;

        private PluginI18NHandler handler;

        public KeyClassHandlerTriple(String key, String clazz, PluginI18NHandler handler) {
            this.key = key;
            this.clazz = clazz;
            this.handler = handler;
        }

        /**
		 * @return
		 */
        protected String getTranslationForKey() {
            return handler.getString(key);
        }

        /**
		 * @return the clazz
		 */
        protected String getClazz() {
            return clazz;
        }

        /**
		 * @return the handler
		 */
        protected PluginI18NHandler getHandler() {
            return handler;
        }

        /**
		 * @return the key
		 */
        protected String getKey() {
            return key;
        }
    }
}
