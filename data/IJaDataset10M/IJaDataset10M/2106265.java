package com.sun.spot.spotworld.gui;

import com.sun.spot.client.SPOTWorldCommands;
import com.sun.spot.client.command.HelloResultList;
import com.sun.spot.spotworld.SpotWorld;
import com.sun.spot.spotworld.common.LocaleUtil;
import com.sun.spot.spotworld.emulator.EmulatorConfig;
import com.sun.spot.spotworld.participants.Application;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import java.util.Vector;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.MenuElement;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.JSplitPane;
import com.sun.spot.util.IEEEAddress;

/**
 * Top level viewer into spotworld, its job is to hold the actual views.
 * This version allows for two simultaneous views to be loaded. These views are loaded as jar files,
 * and this class knows nothing of the details of the views. (Though they are assumed to be Components)
 * @author arshan, robert, randy
 */
public class SpotWorldPortal extends JFrame implements ActionListener {

    protected JSplitPane topPane;

    private SpotWorld world;

    protected DesktopAppManager desktopAppManager;

    protected Vector<String> voList = null;

    public static final String SPOTWORLD_HEIGHT = "SPOTWORLD-HEIGHT";

    public static final String SPOTWORLD_WIDTH = "SPOTWORLD-WIDTH";

    public static final String LEFTPANE_HEIGHT = "LEFTPANE-HEIGHT";

    public static final String LEFTPANE_WIDTH = "LEFTPANE-WIDTH";

    public static final String LEFTPANE_VIEW = "LEFTPANE-VIEW";

    public static final String RIGHTPANE_HEIGHT = "RIGHTPANE-HEIGHT";

    public static final String RIGHTPANE_WIDTH = "RIGHTPANE-WIDTH";

    public static final String RIGHTPANE_VIEW = "RIGHTPANE-VIEW";

    public static final String NO_COMPONENT = "NOTHING";

    protected String SPOTWORLD_PROPERTY_COMMENT = "SPOT World Properties File";

    protected static final String spotWorldPropsFileName = ".oh2008.properties";

    protected static Properties SPOTWorldProperties;

    protected static boolean verbose = false;

    /** Creates a new instance of SpotWorldPortal */
    public SpotWorldPortal() {
        init();
    }

    public void init() {
        setWorld(new SpotWorld());
        desktopAppManager = DesktopAppManager.getInstance();
        desktopAppManager.setSPOTWorldPortal(this);
        setTitle("SPOTWorld");
        readPropertiesFromFile();
        ToolTipManager.sharedInstance().setDismissDelay(7000);
        if (System.getProperty("os.name").toLowerCase().startsWith("mac os x")) {
            macQuitListener();
        }
        this.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                writePropertiesToFile();
                System.exit(0);
            }
        });
        this.getContentPane().setLayout(new BorderLayout());
        topPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        topPane.setOneTouchExpandable(true);
        topPane.setDividerSize(7);
        Component comp = null;
        String leftPaneView = getSPOTWorldProperty(LEFTPANE_VIEW);
        String leftPaneWidth = getSPOTWorldProperty(LEFTPANE_WIDTH);
        String leftPaneHeight = getSPOTWorldProperty(LEFTPANE_HEIGHT);
        String rightPaneView = getSPOTWorldProperty(RIGHTPANE_VIEW);
        String rightPaneWidth = getSPOTWorldProperty(RIGHTPANE_WIDTH);
        String rightPaneHeight = getSPOTWorldProperty(RIGHTPANE_HEIGHT);
        String verboseFlag = getSPOTWorldProperty("verbose");
        if (verboseFlag != null) {
            if (verboseFlag.equalsIgnoreCase("true")) verbose = true; else if (verboseFlag.equalsIgnoreCase("false")) verbose = false;
        }
        ;
        if (leftPaneView == null && rightPaneView == null) {
            leftPaneView = "Tree View";
            leftPaneWidth = "200";
            leftPaneHeight = "1310";
            rightPaneView = "Grid View";
            rightPaneWidth = "800";
            rightPaneHeight = "1310";
        }
        if (!leftPaneView.equals(NO_COMPONENT)) {
            comp = getWorld().getView(leftPaneView);
            comp.setName(leftPaneView);
            topPane.setLeftComponent(comp);
            topPane.getLeftComponent().setPreferredSize(new Dimension(new Integer(leftPaneWidth), new Integer(leftPaneHeight)));
        }
        if (!rightPaneView.equals(NO_COMPONENT)) {
            comp = getWorld().getView(rightPaneView);
            comp.setName(rightPaneView);
            topPane.setRightComponent(comp);
            topPane.getRightComponent().setPreferredSize(new Dimension(new Integer(rightPaneWidth), new Integer(rightPaneHeight)));
        }
        this.setJMenuBar(createMenuBar());
        this.getContentPane().add(topPane);
        StatusBar sb = new StatusBar();
        sb.setWorld(getWorld());
        this.getContentPane().add(sb, BorderLayout.SOUTH);
        getWorld().setBasestation(sb.getRadioIndicator());
        pack();
        setVisible(true);
        testrun();
        getWorld().setReadyToAddVirtualObjects(true);
    }

    public static String getSPOTWorldProperty(String key) {
        return SPOTWorldProperties.getProperty(key);
    }

    public static void setSPOTWorldProperty(String key, String value) {
        SPOTWorldProperties.setProperty(key, value);
    }

    protected void macQuitListener() {
        try {
            Class applicationClass = Class.forName("com.apple.eawt.Application");
            Object applicationInstance = applicationClass.newInstance();
            Class applicationListener = Class.forName("com.apple.eawt.ApplicationListener");
            Object proxy = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[] { applicationListener }, new InvocationHandler() {

                public Object invoke(Object proxy, Method method, Object[] args) {
                    if (method.getName().equals("handleQuit")) {
                        exit();
                    }
                    return null;
                }
            });
            Method m = applicationClass.getMethod("addApplicationListener", applicationListener);
            m.invoke(applicationInstance, proxy);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        } catch (SecurityException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
        }
    }

    public void exit() {
        writePropertiesToFile();
        System.exit(0);
    }

    public void readPropertiesFromFile() {
        File file = new File(spotWorldPropsFileName);
        try {
            if (file.exists()) {
                FileInputStream is = new FileInputStream(file);
                SPOTWorldProperties = new Properties();
                SPOTWorldProperties.loadFromXML(is);
                is.close();
            } else {
                FileOutputStream os = new FileOutputStream(file);
                SPOTWorldProperties = new Properties();
                setSPOTWorldProperty(SpotWorldPortal.SPOTWORLD_HEIGHT, "");
                setSPOTWorldProperty(SpotWorldPortal.SPOTWORLD_WIDTH, "");
                setSPOTWorldProperty(SpotWorldPortal.LEFTPANE_HEIGHT, "640");
                setSPOTWorldProperty(SpotWorldPortal.LEFTPANE_WIDTH, "200");
                setSPOTWorldProperty(SpotWorldPortal.LEFTPANE_VIEW, "Tree View");
                setSPOTWorldProperty(SpotWorldPortal.RIGHTPANE_HEIGHT, "640");
                setSPOTWorldProperty(SpotWorldPortal.RIGHTPANE_WIDTH, "800");
                setSPOTWorldProperty(SpotWorldPortal.RIGHTPANE_VIEW, "Grid View");
                SPOTWorldProperties.storeToXML(os, SPOTWORLD_PROPERTY_COMMENT);
                os.close();
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (InvalidPropertiesFormatException ex) {
            boolean success = file.delete();
            if (success) {
                readPropertiesFromFile();
                return;
            } else {
                System.err.println("Your SPOTWorld properties file is corrupt and couldn't be fixed. Please close SPOT World and delete it.");
                ex.printStackTrace();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void writePropertiesToFile() {
        setSPOTWorldProperty(SPOTWORLD_HEIGHT, Integer.toString(SpotWorldPortal.this.getHeight()));
        setSPOTWorldProperty(SPOTWORLD_WIDTH, Integer.toString(SpotWorldPortal.this.getWidth()));
        if (topPane.getLeftComponent() != null) {
            setSPOTWorldProperty(LEFTPANE_HEIGHT, Integer.toString(topPane.getLeftComponent().getHeight()));
            setSPOTWorldProperty(LEFTPANE_WIDTH, Integer.toString(topPane.getLeftComponent().getWidth()));
            setSPOTWorldProperty(LEFTPANE_VIEW, topPane.getLeftComponent().getName());
        } else {
            setSPOTWorldProperty(LEFTPANE_VIEW, NO_COMPONENT);
        }
        if (topPane.getRightComponent() != null) {
            setSPOTWorldProperty(RIGHTPANE_HEIGHT, Integer.toString(topPane.getRightComponent().getHeight()));
            setSPOTWorldProperty(RIGHTPANE_WIDTH, Integer.toString(topPane.getRightComponent().getWidth()));
            setSPOTWorldProperty(RIGHTPANE_VIEW, topPane.getRightComponent().getName());
        } else {
            setSPOTWorldProperty(RIGHTPANE_VIEW, NO_COMPONENT);
        }
        try {
            File file = new File(spotWorldPropsFileName);
            FileOutputStream os = new FileOutputStream(file);
            SPOTWorldProperties.storeToXML(os, SPOTWORLD_PROPERTY_COMMENT);
            os.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void testrun() {
        getWorld().dummy();
    }

    public JMenuBar createMenuBar() {
        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu(LocaleUtil.getString("File"));
        JMenuItem item = new JMenuItem(LocaleUtil.getString("about"));
        item.addActionListener(new ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openAboutPanel();
            }
        });
        menu.add(item);
        menu.addSeparator();
        item = new JMenuItem(LocaleUtil.getString("exit"));
        item.addActionListener(this);
        menu.add(item);
        bar.add(menu);
        menu = new JMenu(LocaleUtil.getString("View"));
        for (String view : getWorld().getAvailableViews()) {
            JCheckBoxMenuItem cbMenuItem = new JCheckBoxMenuItem(view);
            if (topPane.getLeftComponent() != null && topPane.getLeftComponent().getName().equals(view)) cbMenuItem.setSelected(true);
            if (topPane.getRightComponent() != null && topPane.getRightComponent().getName().equals(view)) cbMenuItem.setSelected(true);
            cbMenuItem.setActionCommand(view);
            cbMenuItem.addActionListener(this);
            menu.add(cbMenuItem);
        }
        bar.add(menu);
        menu = new JMenu(LocaleUtil.getString("Discovery"));
        item = new JMenuItem(LocaleUtil.getString("Discover Sun SPOTs"));
        item.addActionListener(new ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HelloResultList hrl = getWorld().discover();
                if (hrl == null) warnUser("Problem during discovery: null response.\n" + "Make sure a basestation is connected,\n" + "remote SPOTs have over-the-air services\n" + "enabled and are listening on the same\n" + "channel/panId as the basestation."); else {
                    int count = hrl.size();
                    String text = (count == 1) ? " Sun SPOT reply." : " Sun SPOT replies.";
                    if (count == 1) tellUser("Collected " + hrl.size() + text + "\n(Waited " + getWorld().getDiscoveryTimeout() / 1000.0 + " seconds)");
                }
            }
        });
        menu.add(item);
        item = new JMenuItem(LocaleUtil.getString("Discover one..."));
        item.addActionListener(new ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String initial = getWorld().getSPOTAddress();
                String newVal = getAddressFromUser("IEEE Address:", initial);
                if ((newVal != null) && !newVal.equals("")) {
                    getWorld().setSPOTAddress(newVal);
                    getWorld().discover(newVal);
                }
            }
        });
        menu.add(item);
        item = new JMenuItem(LocaleUtil.getString("Set discovery timeout..."));
        item.addActionListener(new ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                int initial = getWorld().getDiscoveryTimeout();
                int newVal = getIntFromUser("Seconds for discovery timeout:", initial / 1000);
                getWorld().setDiscoveryTimeout(newVal * 1000);
            }
        });
        menu.add(item);
        item = new JMenuItem(LocaleUtil.getString("Set discovery hop count..."));
        item.addActionListener(new ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                int initial = getWorld().getDiscoveryHopCount();
                int newVal = getIntFromUser("Hops in the discovery broadcast:", initial, 1, Integer.MAX_VALUE);
                getWorld().setDiscoveryHopCount(newVal);
            }
        });
        menu.add(item);
        item = new JMenuItem(LocaleUtil.getString("Set radio channel ..."));
        item.addActionListener(new ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                int initial = getWorld().getDiscoveryChannel();
                int newVal = getIntFromUser("Radio channel:", initial, 1, 26);
                getWorld().setDiscoveryChannel(newVal);
                SPOTWorldCommands.setChannel(newVal);
            }
        });
        menu.add(item);
        item = new JMenuItem(LocaleUtil.getString("Set radio PAN Id ..."));
        item.addActionListener(new ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                int initial = getWorld().getDiscoveryPanId();
                int newVal = getIntFromUser("Radio PAN Id:", initial, 0, 0xFFFE);
                getWorld().setDiscoveryPanId(newVal);
                SPOTWorldCommands.setPanId(newVal);
            }
        });
        menu.add(item);
        bar.add(menu);
        menu = new JMenu(LocaleUtil.getString("emulator"));
        item = new JMenuItem(LocaleUtil.getString("new_virtual_spot"));
        item.addActionListener(this);
        menu.add(item);
        menu.addSeparator();
        item = new JMenuItem(LocaleUtil.getString("open_virtual_config"));
        item.addActionListener(this);
        menu.add(item);
        item = new JMenuItem(LocaleUtil.getString("save_virtual_config"));
        item.addActionListener(this);
        menu.add(item);
        item = new JMenuItem(LocaleUtil.getString("describe_virtual_config"));
        item.addActionListener(this);
        menu.add(item);
        menu.addSeparator();
        item = new JMenuItem(LocaleUtil.getString("delete_all_virtual_spots"));
        item.addActionListener(this);
        menu.add(item);
        bar.add(menu);
        menu = new JMenu(LocaleUtil.getString("DesktopApps"));
        item = new JMenuItem(LocaleUtil.getString("Run ..."));
        item.addActionListener(this);
        menu.add(item);
        menu.addSeparator();
        for (String fn : desktopAppManager.getRecentDesktopApps()) {
            String[] pathParts = fn.split(File.separator);
            item = desktopAppManager.createMenuItem(pathParts[pathParts.length - 1], fn);
            item.setToolTipText(fn);
            item.addActionListener(this);
            menu.add(item);
        }
        bar.add(menu);
        return bar;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        if (args.length > 0 && args[0].equalsIgnoreCase("-verbose")) {
            verbose = true;
        }
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        SpotWorldPortal swp = new SpotWorldPortal();
    }

    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getActionCommand().equals(LocaleUtil.getString("exit"))) {
            exit();
        }
        if (actionEvent.getActionCommand().equals(LocaleUtil.getString("new_virtual_spot"))) {
            EmulatorConfig.addVirtualSpot(this, getWorld());
            return;
        }
        if (actionEvent.getActionCommand().equals(LocaleUtil.getString("delete_all_virtual_spots"))) {
            EmulatorConfig.deleteAllVirtualSpots(this, getWorld());
            return;
        }
        if (actionEvent.getActionCommand().equals(LocaleUtil.getString("open_virtual_config"))) {
            EmulatorConfig.openVirtualConfig(this, getWorld());
            return;
        }
        if (actionEvent.getActionCommand().equals(LocaleUtil.getString("save_virtual_config"))) {
            EmulatorConfig.saveVirtualConfig(this, getWorld());
            return;
        }
        if (actionEvent.getActionCommand().equals(LocaleUtil.getString("describe_virtual_config"))) {
            EmulatorConfig.displayDescription(this, getWorld());
            return;
        }
        if (actionEvent.getActionCommand().equals("Allow Running App Drag and Drop")) {
            JCheckBoxMenuItem cbItem = (JCheckBoxMenuItem) actionEvent.getSource();
            Application.ALLOW_APP_MIGRATION = cbItem.getState();
            if (Application.ALLOW_APP_MIGRATION) {
                String msg = "Live app migration is not yet fully supported.";
                msg = msg + "\nIt currently only works for the WhiteBlinker app, a very simple case.";
                msg = msg + "\nDrag and drop only works in GridView (not TreeView).";
                JOptionPane.showMessageDialog(this, msg, "", JOptionPane.WARNING_MESSAGE);
            }
        }
        if (actionEvent.getActionCommand().equals("Run ...")) {
            desktopAppManager.runAppFromUser();
            return;
        }
        if (desktopAppManager.doMenuItem((JMenuItem) actionEvent.getSource())) {
            return;
        }
        for (String view : getWorld().getAvailableViews()) {
            if (actionEvent.getActionCommand().equals(view)) {
                if (((JCheckBoxMenuItem) actionEvent.getSource()).isSelected()) {
                    addView(view);
                } else {
                    removeView(view);
                }
                Vector<String> names = new Vector<String>();
                if (topPane.getLeftComponent() != null) names.addElement(topPane.getLeftComponent().getName());
                if (topPane.getRightComponent() != null) names.addElement(topPane.getRightComponent().getName());
                JPopupMenu menu = (JPopupMenu) ((JCheckBoxMenuItem) actionEvent.getSource()).getParent();
                for (MenuElement element : menu.getSubElements()) {
                    if (element instanceof JCheckBoxMenuItem) {
                        if (names.contains(((JCheckBoxMenuItem) element).getActionCommand())) ((JCheckBoxMenuItem) element).setSelected(true); else ((JCheckBoxMenuItem) element).setSelected(false);
                    }
                }
            }
        }
    }

    public void exitSpotWorld(ActionEvent evt) {
        System.exit(0);
    }

    public void searchForSPOTS(ActionEvent evt) {
    }

    public void discoverSPOTS(ActionEvent evt) {
    }

    public void setAutoDiscover(ActionEvent evt) {
    }

    public void addView(String view) {
        if (topPane.getLeftComponent() == null) {
            Component comp = getWorld().getView(view);
            topPane.setLeftComponent(comp);
        } else if (topPane.getRightComponent() == null) {
            Component comp = getWorld().getView(view);
            topPane.setRightComponent(comp);
        } else {
            Component mvComp = topPane.getLeftComponent();
            int oldLocation = topPane.getDividerLocation();
            Component newComp = getWorld().getView(view);
            topPane.setLeftComponent(newComp);
            topPane.setRightComponent(mvComp);
            topPane.setDividerLocation(oldLocation);
            topPane.validate();
            topPane.repaint();
        }
    }

    public void removeView(String view) {
        if (topPane.getLeftComponent() != null && topPane.getLeftComponent().getName().equals(view)) {
            topPane.setLeftComponent(null);
            topPane.validate();
            topPane.repaint();
        }
        if (topPane.getRightComponent() != null && topPane.getRightComponent().getName().equals(view)) {
            topPane.setRightComponent(null);
            topPane.validate();
            topPane.repaint();
        }
    }

    public static void msg(String msg) {
        if (verbose) {
            System.out.println(msg);
        }
    }

    public void warnUser(String msg) {
        JOptionPane.showMessageDialog(this, msg, "", JOptionPane.WARNING_MESSAGE);
    }

    public void tellUser(String msg) {
        JOptionPane.showMessageDialog(this, msg, "", JOptionPane.PLAIN_MESSAGE);
    }

    public void openAboutPanel() {
        String msg = "SPOTWorld is an environment for managing your Sun SPOTs\n" + "You should discover Sun SPOTs within range of your basestation\n" + "If not, make sure the OTA command server is enbaled on your SPOTs\n" + "and that a basestation is attached, then do a discovery again.";
        tellUser(msg);
    }

    public int getIntFromUser(String message) {
        return getIntFromUser(message, 0);
    }

    public int getIntFromUser(String message, int initial) {
        return getIntFromUser(message, initial, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public int getIntFromUser(String message, int initial, int minValue, int maxValue) {
        boolean setToGo = false;
        int val = initial;
        while (!setToGo) {
            String input = JOptionPane.showInputDialog(this, message, "" + initial);
            if (input == null) return initial;
            try {
                val = Integer.decode(input);
                setToGo = true;
            } catch (NumberFormatException e) {
                warnUser(input + "?\nIt's supposed to be an integer.");
                setToGo = false;
            }
            if (setToGo && ((val < minValue) || (val > maxValue))) {
                warnUser("Needs to be a value between " + minValue + " and " + maxValue + " inclusive.");
                setToGo = false;
            }
        }
        return val;
    }

    public String getAddressFromUser(String message, String initial) {
        boolean setToGo = false;
        String input = "";
        IEEEAddress addr = null;
        while (!setToGo) {
            input = JOptionPane.showInputDialog(this, message, initial);
            if (input == null) return null;
            try {
                addr = new IEEEAddress(input);
                if (input.equalsIgnoreCase(addr.toString())) setToGo = true;
            } catch (Exception ex) {
                warnUser("Not a valid IEEE Address, e.g. 0014.4F01.0000.0B0B");
            }
        }
        return input;
    }

    public SpotWorld getWorld() {
        return world;
    }

    public void setWorld(SpotWorld world) {
        this.world = world;
    }
}
