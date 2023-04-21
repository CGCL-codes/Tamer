package com.roku.soundbridge;

import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;
import java.util.prefs.*;

/**
 * Multi-function remote control for the Roku SoundBridge. 
 * Unlike other remotes, this one supports complex searches.
 * 
 * Design notes:
 * The basic concept is that the simple "remote" search functions
 * are replaced by a dialog box that allows you to select the type 
 * of search and to build a list of songs to play.
 * 
 * Alternately, instead of building a list, offer to either 
 * immediately play a selected song or to add it to the end of
 * the current queue.
 */
public class SoundBridgeCommander extends JFrame {

    private static MDNSBrowser mdnsBrowser;

    private static Preferences preferences;

    private Preferences hostprefs;

    private DisplayConnection displayconnection;

    private SoundBridgeDisplay display;

    private JPanel mainpanel;

    private JPanel lowerchoices;

    private JPanel lowercards;

    private JPanel remotepanel;

    private SearchPanel searchpanel;

    private JComponent prefspanel;

    private JMenuBar menubar;

    private JMenu filemenu;

    private int widthcode = -1;

    private int panelready = 0;

    protected String host;

    protected String address;

    private java.util.List presets;

    /** How often we will check for display updates, in milliseconds. */
    private static final int DEFAULT_REFRESH_INTERVAL = 200;

    private static final int SLOW_REFRESH_INTERVAL = 333;

    /** Used to detect when the last window has closed. */
    private static int activewindows = 0;

    /**
	 * True if we're running on a Mac (changes how the menus work)
	 */
    private static boolean isOSX;

    static {
        String lcOSName = System.getProperty("os.name").toLowerCase();
        isOSX = lcOSName.startsWith("mac os x");
    }

    /** Magic cookie. The # of hypothetical buttons on the remote. */
    static final int REMOTE_NUM_BUTTONS = 27;

    static final int DEFAULT_PANEL_COLOR = 0x003333;

    static final int DEFAULT_DISPLAY_COLOR = 0x00FFFF;

    /**
	 * These strings serve several purposes. 1. They are the "names" of the
	 * buttons on the Roku remote. 2. With a suffix ".gif" they are the names of
	 * the icons used on the buttons. 3. With a prefix "CK_" they are the
	 * simulated Roku remote comands.
	 * 
	 * These are in the same order the buttons occur in on the virtual remote.
	 */
    private static final String[] rokucommand = { "POWER", null, "MENU", "NORTH", null, null, "PREVIOUS", "PLAY", "NEXT", "VOLUME_UP", null, "WEST", "SELECT", "EAST", null, "PAUSE", "SHUFFLE", "REPEAT", "VOLUME_DOWN", null, "EXIT", "SOUTH", null, null, "SNOOZE", null, "ALARM" };

    /**
	 * Keyboard Equivalents. They must be in the same order as the commands, above.
	 * 0's are no-ops.
	 */
    private static int[] rokukeys = { KeyEvent.VK_O, 0, KeyEvent.VK_M, KeyEvent.VK_UP, 0, 0, KeyEvent.VK_COMMA, KeyEvent.VK_PERIOD, KeyEvent.VK_SLASH, KeyEvent.VK_CLOSE_BRACKET, 0, KeyEvent.VK_LEFT, KeyEvent.VK_ENTER, KeyEvent.VK_RIGHT, 0, KeyEvent.VK_P, KeyEvent.VK_S, KeyEvent.VK_R, KeyEvent.VK_OPEN_BRACKET, 0, KeyEvent.VK_ESCAPE, KeyEvent.VK_DOWN, 0, 0, KeyEvent.VK_Z, 0, KeyEvent.VK_A };

    private static final AboutBox aboutBox = new AboutBox();

    private static final HelpBox helpBox = new HelpBox();

    /**
	 * If we're running on a Mac, attach ourselves to the OS X system menu. This goes through
	 * a funky reflection method so that attempts to run on a Windows box won't fail.
	 * 
	 * Note that you still have to compile the jar on a Mac.
	 */
    private void macOSXRegistration() {
        if (isOSX) {
            try {
                Class osxAdapter = ClassLoader.getSystemClassLoader().loadClass("com.roku.soundbridge.OSXAdapter");
                Class[] defArgs = { SoundBridgeCommander.class };
                Method registerMethod = osxAdapter.getDeclaredMethod("registerMacOSXApplication", defArgs);
                if (registerMethod != null) {
                    Object[] args = { this };
                    registerMethod.invoke(osxAdapter, args);
                }
            } catch (Exception e) {
                System.err.println("Exception while loading the OSXAdapter:");
                e.printStackTrace();
            }
        }
    }

    /**
	 * The callback for keyboard equivalents and buttons.
	 */
    private final Action remoteaction = new AbstractAction() {

        public final void actionPerformed(final ActionEvent e) {
            if (!searchpanel.isVisible()) {
                final String command = "IrDispatchCommand CK_" + e.getActionCommand();
                displayconnection.send(command);
            }
        }
    };

    public void updateDisplay(int count, byte hex[]) {
        display.setData(count, hex);
    }

    public void updatePresets(java.util.List newpresets) {
        presets = newpresets;
        while (panelready == 0) {
            java.lang.Thread.yield();
        }
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                widthcode = -1;
                pack();
            }
        });
    }

    /**
	 * OS independent about box.
	 */
    public void about() {
        int x = getX();
        int y = getY();
        aboutBox.setLocation(x + 32, y + 16);
        aboutBox.setVisible(true);
    }

    /**
	 * OS independent help function.
	 */
    public void help() {
        int x = getX();
        int y = getY();
        helpBox.setLocation(x + 32, y + 16);
        helpBox.setVisible(true);
    }

    /**
	 * OS independent quit function.
	 */
    public void quit() {
        System.exit(0);
    }

    public SoundBridgeCommander(String hostName, String hostAddr, int x, int y) {
        activewindows++;
        host = hostName;
        address = (hostAddr == null) ? hostName : hostAddr;
        hostprefs = preferences.node(host);
        hostprefs.put("Address", address);
        System.out.println("New SoundBridgeCommander(" + hostName + "," + hostAddr + "," + x + "," + y + ")");
        menubar = new JMenuBar();
        ;
        filemenu = new JMenu("File");
        JMenuItem mi;
        menubar.add(filemenu);
        if (isOSX) {
            macOSXRegistration();
        } else {
            menubar.setBorder(null);
            filemenu.setForeground(null);
            filemenu.add(mi = new JMenuItem("About"));
            mi.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent ae) {
                    about();
                }
            });
        }
        filemenu.add(mi = new JMenuItem("Add Another SoundBridge"));
        mi.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                mdnsBrowser.open();
            }
        });
        filemenu.add(mi = new JMenuItem("Forget " + host));
        mi.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                try {
                    activewindows--;
                    hostprefs.removeNode();
                    dispose();
                    if (activewindows <= 0) System.exit(0);
                } catch (Exception e) {
                }
            }
        });
        filemenu.add(mi = new JMenuItem("Reconnect"));
        mi.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                try {
                    address = mdnsBrowser.resolveName(host);
                    hostprefs.put("Address", address);
                    displayconnection.reconnect(address);
                    searchpanel.connection.reconnect(address);
                } catch (Exception e) {
                }
            }
        });
        filemenu.add(mi = new JMenuItem("Help"));
        mi.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                help();
            }
        });
        if (!isOSX) {
            filemenu.addSeparator();
            filemenu.add(mi = new JMenuItem("Quit", KeyEvent.VK_Q));
            mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
            mi.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent ae) {
                    quit();
                }
            });
        }
        this.setJMenuBar(menubar);
        displayconnection = new DisplayConnection(this);
        mainpanel = new JPanel(new BorderLayout());
        this.setContentPane(mainpanel);
        display = new SoundBridgeDisplay(hostprefs);
        display.setBorder(new EmptyBorder(isOSX ? 8 : 0, 8, 4, 8));
        mainpanel.add(display, BorderLayout.NORTH);
        remotepanel = new JPanel(new BorderLayout());
        remotepanel.setBackground(null);
        remotepanel.setForeground(null);
        searchpanel = new SearchPanel(address);
        prefspanel = new PreferencesPanel(hostprefs);
        hostprefs.addPreferenceChangeListener(new PreferenceChangeListener() {

            public void preferenceChange(PreferenceChangeEvent pce) {
                updateFromPreferences();
            }
        });
        JPanel lowerpanel = new JPanel(new BorderLayout());
        lowerpanel.setBackground(null);
        lowerpanel.setForeground(null);
        final CardLayout lowerCLO = new CardLayout();
        lowerchoices = new JPanel(new GridLayout(1, 0) {

            public void layoutContainer(Container parent) {
                Insets in = parent.getInsets();
                int space = parent.getWidth() - (in.left + in.right);
                Component[] c = parent.getComponents();
                Dimension[] d = new Dimension[c.length];
                for (int i = 0; i < c.length; i++) {
                    space -= (d[i] = c[i].getPreferredSize()).width;
                }
                Point p = new Point(in.left, in.top);
                for (int i = 0; i < c.length; i++) {
                    c[i].setBounds(new Rectangle(p, d[i]));
                    if (i < c.length - 1) {
                        int pad = space / (c.length - 1 - i);
                        p.x += d[i].width + pad;
                        space -= pad;
                    }
                }
            }
        });
        lowerchoices.setBackground(null);
        lowerchoices.setForeground(null);
        lowercards = new JPanel(lowerCLO) {

            public Dimension getPreferredSize() {
                Component c;
                for (int i = 0; i < getComponentCount(); i++) if ((c = getComponent(i)).isVisible()) {
                    return new Dimension(1, c.getPreferredSize().height);
                }
                return new Dimension(0, 0);
            }
        };
        lowercards.setBackground(null);
        lowercards.setForeground(null);
        lowerpanel.add(lowerchoices, BorderLayout.NORTH);
        lowerpanel.add(lowercards, BorderLayout.CENTER);
        ActionListener lowerAL = new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                lowerCLO.show(lowercards, ((CardSelector) ae.getSource()).getText());
                pack();
            }
        };
        ButtonGroup lowerBG = new ButtonGroup();
        AbstractButton remoteTB = new CardSelector("Remote", lowerAL, lowerBG, lowerchoices);
        remoteTB.setSelected(true);
        lowercards.add(remotepanel, remoteTB.getText());
        AbstractButton searchTB = new CardSelector("Search", lowerAL, lowerBG, lowerchoices);
        lowercards.add(searchpanel, searchTB.getText());
        AbstractButton preferencesTB = new CardSelector("Preferences", lowerAL, lowerBG, lowerchoices);
        lowercards.add(prefspanel, preferencesTB.getText());
        AbstractButton hideTB = new CardSelector("Hide", lowerAL, lowerBG, lowerchoices);
        JPanel hider = new JPanel();
        hider.setPreferredSize(new Dimension(0, 0));
        lowercards.add(hider, hideTB.getText());
        mainpanel.add(lowerpanel, BorderLayout.CENTER);
        for (int i = 0; i < rokucommand.length; i++) {
            if (rokukeys[i] != 0 && rokucommand[i] != null) {
                KeyStroke keystroke = KeyStroke.getKeyStroke(rokukeys[i], 0, false);
                this.getRootPane().registerKeyboardAction(remoteaction, rokucommand[i], keystroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
            }
        }
        final ComponentListener windowlistener = new ComponentListener() {

            public final void componentHidden(final ComponentEvent e) {
                activewindows--;
                if (activewindows <= 0) {
                    quit();
                }
            }

            public final void componentShown(final ComponentEvent e) {
            }

            public final void componentResized(final ComponentEvent e) {
            }

            public final void componentMoved(final ComponentEvent e) {
                hostprefs.putInt("X", getX());
                hostprefs.putInt("Y", getY());
            }
        };
        addComponentListener(windowlistener);
        setTitle("Roku SoundBridge @ " + host);
        setResizable(false);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setUndecorated(!hostprefs.getBoolean("ShowFrame", true));
        updateFromPreferences();
        pack();
        setLocation(x, y);
        setVisible(true);
    }

    /**
	 * Update UI colors, etc from preferences.
	 */
    private void updateFromPreferences() {
        try {
            Color c = new Color(hostprefs.getInt("Panel", DEFAULT_PANEL_COLOR));
            lowercards.setBackground(c);
            lowercards.setForeground(Images.getContrastingForeground(c));
        } catch (Exception e) {
        }
        try {
        } catch (Exception e) {
        }
        try {
            Color c = new Color(hostprefs.getInt("Display", DEFAULT_DISPLAY_COLOR));
            display.setBackground(Color.black);
            display.setColor(c);
            menubar.setBackground(Color.black);
            menubar.setForeground(Color.gray);
            lowerchoices.setBackground(Color.black);
            lowerchoices.setForeground(Color.gray);
        } catch (Exception e) {
        }
        pack();
        repaint();
    }

    /**
	 * Add the selected SoundBridge.
	 */
    public static void addSB(String hostName) {
        if (hostName != null) {
            String hostAddr = mdnsBrowser.resolveName(hostName);
            new SoundBridgeCommander(hostName, hostAddr, 0, 0);
        }
    }

    /**
	 * 
	 */
    public int getRefreshRate() {
        if (hostprefs.getBoolean("SlowUpdate", false)) {
            return SLOW_REFRESH_INTERVAL;
        } else {
            return DEFAULT_REFRESH_INTERVAL;
        }
    }

    /**
	 * 
	 */
    public boolean conserveBandwidth() {
        return hostprefs.getBoolean("SlowUpdate", false);
    }

    /**
	 *
	 */
    public void pack() {
        int wc = (display.getPreferredSize().width < 1000 ? 0 : 1) + (hostprefs.getBoolean("ShowPresets", true) ? 0 : 2);
        if (getContentPane().getWidth() != display.getPreferredSize().width || wc != widthcode) {
            widthcode = wc;
            remotepanel.removeAll();
            remotepanel.add(new RemotePanel(address, displayconnection, presets, widthcode));
        }
        panelready = 1;
        super.pack();
    }

    /**
	 * Main program. Accepts one or more hostnames and/or ip addresses as
	 * arguments, and loads previously invoked SBCs from preferences.
	 * 
	 * Preferences are stored as "hostname", "hostname"/X and "hostname"/Y
	 */
    public static void main(String[] argv) {
        mdnsBrowser = MDNSBrowser.getBrowser(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                addSB(ae.getActionCommand());
            }
        });
        preferences = Preferences.userNodeForPackage(SoundBridgeCommander.class);
        Set hosts = new TreeSet(Arrays.asList(argv));
        try {
            hosts.addAll(Arrays.asList(preferences.childrenNames()));
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        if (hosts.isEmpty()) {
            mdnsBrowser.open();
        } else {
            for (Iterator i = hosts.iterator(); i.hasNext(); ) {
                String host = (String) i.next();
                Preferences hostprefs = preferences.node(host);
                String hostAddr = hostprefs.get("Address", null);
                if (hostAddr == null) {
                    hostAddr = host;
                }
                int x = hostprefs.getInt("X", 0);
                int y = hostprefs.getInt("Y", 0);
                new SoundBridgeCommander(host, hostAddr, x, y);
            }
        }
    }

    /**
 * Bottom panel selector button.
 */
    private class CardSelector extends JToggleButton {

        /**
	 * Set appearance properties and add to panel and button group
	 */
        public CardSelector(String text, ActionListener actionListener, ButtonGroup buttonGroup, JPanel controlPanel) {
            setText(text);
            setMargin(new Insets(0, 0, 0, 0));
            setBorder(new EmptyBorder(0, 10, 0, 10));
            addActionListener(actionListener);
            buttonGroup.add(this);
            controlPanel.add(this);
            setBackground(null);
            setForeground(null);
            setRolloverEnabled(true);
        }

        /**
	 * Custom rendering. to do: adjust appearance for low gamma monitors of high ambient light
	 */
        public void paintComponent(Graphics g) {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
            if (isSelected() && "Hide".equals(getText())) return;
            Icon icon = Images.getImageIcon(this, "panelTab", new Dimension(getWidth(), 1), false, true);
            icon.paintIcon(this, g, 0, 0);
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            FontMetrics fm = getFontMetrics(getFont());
            Rectangle viewR = new Rectangle(getSize());
            Rectangle iconR = new Rectangle();
            Rectangle textR = new Rectangle();
            String text = SwingUtilities.layoutCompoundLabel(this, fm, getText(), null, CENTER, CENTER, CENTER, CENTER, viewR, iconR, textR, 0);
            g.setFont(getFont());
            g.setColor(getModel().isRollover() ? Color.white : isSelected() ? getParent().getForeground() : getBackground());
            g.drawString(text, textR.x, textR.y + fm.getAscent());
        }
    }
}
