package com.hp.hpl.guess.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EventListener;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;
import java.util.prefs.Preferences;
import com.hp.hpl.guess.util.PrefWrapper;
import java.awt.GridBagConstraints;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import com.jidesoft.swing.JideTabbedPane;
import com.jidesoft.swing.JideTabbedPane.ColorProvider;
import java.awt.*;

public class MainUIWindow extends JFrame {

    private static final long serialVersionUID = 1L;

    public static final int HORIZONTAL_DOCK = 1;

    public static final int VERTICAL_DOCK = 2;

    private Component canvas;

    private GraphicsDevice graphicsDevice;

    private EventListener escapeFullScreenModeListener;

    private JideTabbedPane tabbedPaneH = null;

    private JideTabbedPane tabbedPaneV = null;

    private JSplitPane splitPaneH = null;

    private JSplitPane splitPaneV = null;

    private GMenuBar myMenu = null;

    private final JPopupMenu jpop = new JPopupMenu("Dock Menu");

    private JideTabbedPane selected = null;

    /**
     * Object to save user preferences
     */
    private Preferences userPrefs = PrefWrapper.userNodeForPackage(getClass());

    public JideTabbedPane getHorizontalTabbedPane() {
        return (tabbedPaneH);
    }

    public JideTabbedPane getVerticalTabbedPane() {
        return (tabbedPaneV);
    }

    public JSplitPane getHorizontalSplitPane() {
        return (splitPaneH);
    }

    public JSplitPane getVerticalSplitPane() {
        return (splitPaneV);
    }

    public Set<Component> getVerticalDockedComponents() {
        HashSet<Component> s = new HashSet<Component>();
        for (int i = 0; i < tabbedPaneV.getTabCount(); i++) {
            s.add(tabbedPaneV.getComponentAt(i));
        }
        return (s);
    }

    public Set<Component> getHorizontalDockedComponents() {
        HashSet<Component> s = new HashSet<Component>();
        for (int i = 0; i < tabbedPaneH.getTabCount(); i++) {
            s.add(tabbedPaneH.getComponentAt(i));
        }
        return (s);
    }

    public MainUIWindow(Component aCanvas) {
        this(false, aCanvas);
    }

    public GMenuBar getGMenuBar() {
        return (myMenu);
    }

    public MainUIWindow(boolean fullScreenMode, Component aCanvas) {
        this(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice(), fullScreenMode, aCanvas);
    }

    public MainUIWindow(GraphicsDevice aDevice, final boolean fullScreenMode, final Component aCanvas) {
        super(aDevice.getDefaultConfiguration());
        getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        graphicsDevice = aDevice;
        try {
            ImageIcon imageIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(Thread.currentThread().getContextClassLoader().getResource("images/guess-icon.png")));
            setIconImage(imageIcon.getImage());
        } catch (Exception ex) {
        }
        setBounds(getDefaultFrameBounds());
        setBackground(null);
        try {
            addWindowListener(new WindowAdapter() {

                public void windowClosing(WindowEvent e) {
                    userPrefs.putInt("HDividerLocation", getHDividerLocation());
                    userPrefs.putInt("VDividerLocation", getVDividerLocation());
                    userPrefs.putInt("MainWindowWidth", getWidth());
                    userPrefs.putInt("MainWindowHeight", getHeight());
                    userPrefs.putInt("MainWindowX", getX());
                    userPrefs.putInt("MainWindowY", getY());
                    com.hp.hpl.guess.Guess.shutdown();
                }
            });
        } catch (SecurityException e) {
        }
        canvas = aCanvas;
        if (canvas == null) {
            System.err.println("null canvas");
        }
        myMenu = new GMenuBar();
        myMenu.setPreferredSize(new Dimension(Integer.MAX_VALUE, 35));
        myMenu.setMinimumSize(new Dimension(0, 35));
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 0;
        c.weightx = 1;
        c.gridy = 0;
        c.gridx = 0;
        c.anchor = GridBagConstraints.NORTH;
        c.ipady = 0;
        getContentPane().add(myMenu, c);
        createPanes();
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1;
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 1;
        getContentPane().add(splitPaneV, c);
        StatusBar jp = new StatusBar();
        c.fill = GridBagConstraints.NONE;
        c.weighty = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.SOUTHWEST;
        getContentPane().add(jp, c);
        getContentPane().setComponentZOrder(jp, 0);
        getContentPane().setComponentZOrder(splitPaneV, 1);
        validate();
        setFullScreenMode(fullScreenMode);
        canvas.requestFocus();
        setTitle("Visualization - GUESS");
        JMenuItem jmi1 = jpop.add("Undock");
        JMenuItem jmi2 = jpop.add("Close");
        ActionListener al = new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                if (event.getActionCommand().equals("Undock")) {
                    if (selected == tabbedPaneH) {
                        Component c = tabbedPaneH.getSelectedComponent();
                        if ((c != null) && (c instanceof Dockable)) {
                            undock((Dockable) c);
                        }
                    } else {
                        Component c = tabbedPaneV.getSelectedComponent();
                        if ((c != null) && (c instanceof Dockable)) {
                            undock((Dockable) c);
                        }
                    }
                } else if (event.getActionCommand().equals("Close")) {
                    if (selected == tabbedPaneH) {
                        Component c = tabbedPaneH.getSelectedComponent();
                        if ((c != null) && (c instanceof Dockable)) {
                            selected.remove((Component) c);
                            ((Dockable) c).opening(false);
                        }
                    } else {
                        Component c = tabbedPaneV.getSelectedComponent();
                        if ((c != null) && (c instanceof Dockable)) {
                            selected.remove((Component) c);
                            ((Dockable) c).opening(false);
                        }
                    }
                    if (selected.getTabCount() == 0) {
                        if (selected == tabbedPaneV) {
                            hideDividers(splitPaneV);
                        } else {
                            hideDividers(splitPaneH);
                        }
                    }
                }
            }
        };
        jmi1.addActionListener(al);
        jmi2.addActionListener(al);
        MouseAdapter ma = new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                if ((e.getButton() == MouseEvent.BUTTON2) || (e.getButton() == MouseEvent.BUTTON3)) {
                    selected = (JideTabbedPane) e.getComponent();
                    jpop.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        };
        tabbedPaneH.addMouseListener(ma);
        tabbedPaneV.addMouseListener(ma);
    }

    public final Color getBgColor() {
        return SystemColor.text;
    }

    private void createPanes() {
        final Color lineColor1 = new Color(224, 232, 242);
        final Color lineColor2 = new Color(255, 255, 255);
        final Color lineColor3 = new Color(180, 188, 197);
        final Color lineColor4 = new Color(238, 243, 250);
        tabbedPaneH = new JideTabbedPane();
        tabbedPaneH.setTabPlacement(JTabbedPane.BOTTOM);
        tabbedPaneH.setHideOneTab(true);
        tabbedPaneH.setBorder(BorderFactory.createEmptyBorder());
        tabbedPaneH.setContentBorderInsets(new Insets(0, 0, 0, 0));
        tabbedPaneV = new JideTabbedPane();
        tabbedPaneV.setTabPlacement(JTabbedPane.LEFT);
        tabbedPaneV.setHideOneTab(true);
        tabbedPaneV.setBorder(BorderFactory.createEmptyBorder());
        tabbedPaneV.setContentBorderInsets(new Insets(0, 0, 0, 0));
        splitPaneH = new JSplitPane(JSplitPane.VERTICAL_SPLIT, canvas, tabbedPaneH);
        splitPaneV = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tabbedPaneV, splitPaneH);
        splitPaneH.setBorder(BorderFactory.createEmptyBorder());
        splitPaneV.setBorder(BorderFactory.createEmptyBorder());
        splitPaneH.setOneTouchExpandable(false);
        splitPaneV.setOneTouchExpandable(false);
        splitPaneH.setDividerSize(4);
        splitPaneV.setDividerSize(4);
        splitPaneH.setResizeWeight(1);
        tabbedPaneH.setBackground(getBgColor());
        tabbedPaneV.setBackground(getBgColor());
        tabbedPaneH.setTabColorProvider(new ColorProvider() {

            public Color getBackgroundAt(int arg0) {
                if (tabbedPaneH.getSelectedIndex() == arg0) {
                    return getBgColor();
                }
                return getBgColor();
            }

            public Color getForegroudAt(int arg0) {
                return Color.BLACK;
            }

            public float getGradientRatio(int arg0) {
                if (tabbedPaneH.getSelectedIndex() == arg0) {
                    return 0.9f;
                }
                return 0.5f;
            }
        });
        tabbedPaneV.setTabColorProvider(new ColorProvider() {

            public Color getBackgroundAt(int arg0) {
                if (tabbedPaneV.getSelectedIndex() == arg0) {
                    return getBgColor();
                }
                return getBgColor();
            }

            public Color getForegroudAt(int arg0) {
                return Color.BLACK;
            }

            public float getGradientRatio(int arg0) {
                if (tabbedPaneV.getSelectedIndex() == arg0) {
                    return 0.9f;
                }
                return 0.5f;
            }
        });
        splitPaneH.setBackground(getBgColor());
        splitPaneV.setBackground(getBgColor());
        splitPaneH.setUI(new BasicSplitPaneUI() {

            public BasicSplitPaneDivider createDefaultDivider() {
                return new BasicSplitPaneDivider(this) {

                    private static final long serialVersionUID = 1L;

                    public void setBorder(Border b) {
                        Border b1 = BorderFactory.createMatteBorder(1, 0, 0, 0, lineColor1);
                        Border b2 = BorderFactory.createMatteBorder(1, 0, 0, 0, lineColor2);
                        Border b3 = BorderFactory.createMatteBorder(1, 0, 0, 0, lineColor3);
                        Border b4 = BorderFactory.createMatteBorder(1, 0, 0, 0, lineColor4);
                        Border b1b2 = BorderFactory.createCompoundBorder(b1, b2);
                        Border b1b2b3 = BorderFactory.createCompoundBorder(b1b2, b3);
                        Border b1b2b3b4 = BorderFactory.createCompoundBorder(b1b2b3, b4);
                        super.setBorder(b1b2b3b4);
                    }
                };
            }
        });
        splitPaneV.setUI(new BasicSplitPaneUI() {

            public BasicSplitPaneDivider createDefaultDivider() {
                return new BasicSplitPaneDivider(this) {

                    private static final long serialVersionUID = 1L;

                    public void setBorder(Border b) {
                        Border b1 = BorderFactory.createMatteBorder(0, 0, 0, 1, lineColor4);
                        Border b2 = BorderFactory.createMatteBorder(0, 0, 0, 1, lineColor3);
                        Border b3 = BorderFactory.createMatteBorder(0, 0, 0, 1, lineColor2);
                        Border b4 = BorderFactory.createMatteBorder(0, 0, 0, 1, lineColor1);
                        Border b1b2 = BorderFactory.createCompoundBorder(b1, b2);
                        Border b1b2b3 = BorderFactory.createCompoundBorder(b1b2, b3);
                        Border b1b2b3b4 = BorderFactory.createCompoundBorder(b1b2b3, b4);
                        super.setBorder(b1b2b3b4);
                    }
                };
            }
        });
    }

    /**
     * set the location of the horizontal panel
     * @param loc the integer location, distance from top
     */
    public void setHDividerLocation(int loc) {
        splitPaneH.setDividerLocation(loc);
        if (loc >= getHeight()) {
            hideDividers(splitPaneH);
        }
    }

    /**
     * set the location of the vertical panel
     * @param loc the integer location, distance from left
     */
    public void setVDividerLocation(int loc) {
        splitPaneV.setDividerLocation(loc);
        if (loc == 0) {
            hideDividers(splitPaneV);
        }
    }

    /**
     * get the location of the horizontal panel
     * @return loc the integer location, distance from top
     */
    public int getHDividerLocation() {
        BasicSplitPaneUI bspui = (BasicSplitPaneUI) splitPaneH.getUI();
        if (bspui.getDivider().isVisible()) {
            return splitPaneH.getDividerLocation();
        } else {
            return splitPaneH.getMaximumDividerLocation();
        }
    }

    /**
     * get the location of the vertical panel
     * @param loc the integer location, distance from left
     */
    public int getVDividerLocation() {
        BasicSplitPaneUI bspui = (BasicSplitPaneUI) splitPaneV.getUI();
        if (bspui.getDivider().isVisible()) {
            return splitPaneV.getDividerLocation();
        } else {
            return 0;
        }
    }

    public void dock(Dockable d) {
        try {
            if (d.getDirectionPreference() == VERTICAL_DOCK) {
                double m1 = getVDividerLocation();
                double m2 = ((Component) d).getPreferredSize().getWidth();
                int m3 = (int) Math.max(m1, m2);
                showDividers(splitPaneV, m3);
                tabbedPaneV.addTab(d.getTitle(), null, (Component) d);
                tabbedPaneV.setBackgroundAt(0, Color.darkGray);
                tabbedPaneV.setSelectedComponent((Component) d);
            } else {
                double m1 = splitPaneH.getDividerLocation();
                double m2 = ((Component) d).getPreferredSize().getHeight();
                int m3 = (int) Math.max(m1, m2);
                showDividers(splitPaneH, m3);
                tabbedPaneH.addTab(d.getTitle(), (Component) d);
                tabbedPaneH.setSelectedComponent((Component) d);
            }
            d.opening(true);
            d.attaching(true);
        } catch (Exception e) {
            ExceptionWindow.getExceptionWindow(e);
        }
    }

    public void undock(Dockable d) {
        GuessJFrame gjf = d.getWindow();
        if (gjf == null) {
            gjf = new GuessJFrame(d);
        } else {
            gjf.getContentPane().removeAll();
            gjf.getContentPane().add((Component) d);
        }
        d.opening(true);
        d.attaching(false);
        gjf.setBounds(20, 20, (int) ((Component) d).getPreferredSize().getWidth(), (int) ((Component) d).getPreferredSize().getHeight() + 50);
        gjf.setVisible(true);
        if (selected == null) {
            if (d.getDirectionPreference() == HORIZONTAL_DOCK) {
                selected = tabbedPaneH;
            } else {
                selected = tabbedPaneV;
            }
        }
        if (selected != null) {
            selected.remove((Component) d);
            if (selected.getTabCount() == 0) {
                if (selected == tabbedPaneV) {
                    hideDividers(splitPaneV);
                } else {
                    hideDividers(splitPaneH);
                }
            }
        }
    }

    /**
     * Hides the Dividers from the JSplitpane
     * @param splitpane
     */
    private void hideDividers(JSplitPane splitpane) {
        BasicSplitPaneUI bspui = (BasicSplitPaneUI) splitpane.getUI();
        bspui.getDivider().setVisible(false);
        splitpane.setEnabled(false);
        if (splitpane.getOrientation() == JSplitPane.HORIZONTAL_SPLIT) {
            tabbedPaneV.setVisible(false);
            splitpane.setDividerLocation(0);
            splitpane.setLastDividerLocation(0);
            userPrefs.putInt("VDividerLocation", 0);
        } else {
            tabbedPaneH.setVisible(false);
            splitpane.setDividerLocation(splitPaneH.getMaximumDividerLocation());
            splitpane.setLastDividerLocation(splitPaneH.getMaximumDividerLocation());
            userPrefs.putInt("HDividerLocation", splitPaneH.getMaximumDividerLocation());
        }
    }

    /**
     * Shows the Dividers from the JSplitpane
     * @param splitpane
     */
    private void showDividers(JSplitPane splitpane, int dividerLoction) {
        BasicSplitPaneUI bspui = (BasicSplitPaneUI) splitpane.getUI();
        bspui.getDivider().setVisible(true);
        splitpane.setEnabled(true);
        if (splitpane.getOrientation() == JSplitPane.HORIZONTAL_SPLIT) {
            tabbedPaneV.setVisible(true);
            setVDividerLocation(dividerLoction);
        } else {
            tabbedPaneH.setVisible(true);
            setHDividerLocation(dividerLoction);
        }
    }

    public void close(Dockable d) {
        try {
            JTabbedPane selected = null;
            if (d.getDirectionPreference() == VERTICAL_DOCK) {
                selected = tabbedPaneV;
            } else {
                selected = tabbedPaneH;
            }
            d.opening(false);
            selected.remove((Component) d);
            if (selected.getTabCount() == 0) {
                if (selected == tabbedPaneV) {
                    hideDividers(splitPaneV);
                } else {
                    hideDividers(splitPaneH);
                }
            }
        } catch (Exception e) {
            ExceptionWindow.getExceptionWindow(e);
        }
    }

    public Component getCanvas() {
        return canvas;
    }

    public Rectangle getDefaultFrameBounds() {
        return new Rectangle(100, 100, 800, 600);
    }

    public void setFullScreenMode(boolean fullScreenMode) {
        if (fullScreenMode) {
            addEscapeFullScreenModeListener();
            if (isDisplayable()) {
                dispose();
            }
            setUndecorated(true);
            setResizable(false);
            graphicsDevice.setFullScreenWindow(this);
            if (graphicsDevice.isDisplayChangeSupported()) {
                chooseBestDisplayMode(graphicsDevice);
            }
            validate();
        } else {
            removeEscapeFullScreenModeListener();
            if (isDisplayable()) {
                dispose();
            }
            setUndecorated(false);
            setResizable(true);
            graphicsDevice.setFullScreenWindow(null);
            setSize(userPrefs.getInt("MainWindowWidth", getWidth()), userPrefs.getInt("MainWindowHeight", getHeight()));
            setLocation(userPrefs.getInt("MainWindowX", getX()), userPrefs.getInt("MainWindowY", getY()));
            validate();
            setVisible(true);
        }
        setHDividerLocation(userPrefs.getInt("HDividerLocation", 2 * getHeight() / 4));
        setVDividerLocation(userPrefs.getInt("VDividerLocation", 0));
    }

    protected void chooseBestDisplayMode(GraphicsDevice device) {
        DisplayMode best = getBestDisplayMode(device);
        if (best != null) {
            device.setDisplayMode(best);
        }
    }

    protected DisplayMode getBestDisplayMode(GraphicsDevice device) {
        try {
            Iterator<DisplayMode> itr = getPreferredDisplayModes(device).iterator();
            while (itr.hasNext()) {
                DisplayMode each = (DisplayMode) itr.next();
                DisplayMode[] modes = device.getDisplayModes();
                for (int i = 0; i < modes.length; i++) {
                    if (modes[i].getWidth() == each.getWidth() && modes[i].getHeight() == each.getHeight() && modes[i].getBitDepth() == each.getBitDepth()) {
                        return each;
                    }
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

    protected Collection<DisplayMode> getPreferredDisplayModes(GraphicsDevice device) {
        ArrayList<DisplayMode> result = new ArrayList<DisplayMode>();
        result.add(device.getDisplayMode());
        return result;
    }

    ;

    public void addEscapeFullScreenModeListener() {
        removeEscapeFullScreenModeListener();
        escapeFullScreenModeListener = new KeyAdapter() {

            public void keyPressed(KeyEvent aEvent) {
                if (aEvent.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    setFullScreenMode(false);
                }
            }
        };
        canvas.addKeyListener((KeyListener) escapeFullScreenModeListener);
    }

    /**
     * This method removes the escape full screen mode key listener. It will be
     * called for you automatically when full screen mode exits, but the method
     * has been made public for applications that wish to use other methods for
     * exiting full screen mode.
     */
    public void removeEscapeFullScreenModeListener() {
        if (escapeFullScreenModeListener != null) {
            canvas.removeKeyListener((KeyListener) escapeFullScreenModeListener);
            escapeFullScreenModeListener = null;
        }
    }
}
