package vademecum.ui.visualizer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.tree.TreePath;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.freehep.util.export.ExportDialog;
import org.java.plugin.Plugin;
import org.java.plugin.PluginClassLoader;
import org.java.plugin.PluginLifecycleException;
import org.java.plugin.registry.Extension;
import org.java.plugin.registry.ExtensionPoint;
import org.java.plugin.registry.PluginDescriptor;
import vademecum.Core;
import vademecum.core.experiment.ExperimentNode;
import vademecum.core.model.ExtensionComparator;
import vademecum.extensionPoint.ExtensionFactory;
import vademecum.extensionPoint.IVisualizer;
import vademecum.protocol.Protocol;
import vademecum.protocol.resultitem.ResItem;
import vademecum.protocol.resultitem.ResLabel;
import vademecum.ui.VademecumWindow;
import vademecum.ui.VademecumWindowClickAction;
import vademecum.ui.project.DataNavigation;
import vademecum.ui.project.Expertice;
import vademecum.ui.visualizer.panel.FigurePanel;
import vademecum.ui.visualizer.utils.UniqueIDSet;
import vademecum.ui.visualizer.vgraphics.AbstractInteraction;
import vademecum.ui.visualizer.vgraphics.IPlotable;
import vademecum.ui.visualizer.vgraphics.VGraphics;
import vademecum.ui.VademecumViewer;

/**
 * the VisualizerFrame (which could be used as Singleton in further applications)
 * 
 * containing Menu, one or multiple FigurePanels and a Toolbar.
 * The Toolbar becomes visible when the ActivationPanel (aka slit) 
 * has been accessed.
 * 
 */
public class VisualizerFrame extends JFrame implements GraphicsViewer, WindowListener, ComponentListener {

    /** Logger */
    private static Log log = LogFactory.getLog(VisualizerFrame.class);

    static final boolean DEBUG = false;

    private static volatile VisualizerFrame vFrame;

    private String vmcWindowName;

    /**	The ContentPane of this SwingFrame 	*/
    Container content;

    /**	The main FigurePanel containing the VGraphics	*/
    public FigurePanel xp;

    Vector<FigurePanel> xpList = new Vector<FigurePanel>();

    /**	Toolbox which is holding the Activators for the VGraphics(Plots) Interactions	*/
    ToolboxPanel toolboxPanel;

    /**	Toolbox flag */
    boolean toolboxVisible = false;

    /**	Toolbox flag */
    boolean toolboxAutoHide = true;

    /**	Editmode flag: whether toolbox can be accessed */
    boolean editMode = true;

    /** Glass Pane */
    public JPanel glass;

    /** Content of the Glass Pane */
    JComponent sheet;

    /** Progress Panel on glass */
    WaitPanel waitPanel;

    /**	Holder Panel will be used if multiple Figures exist */
    JPanel holder;

    /** Toolbox activator */
    ActivationPanel slit;

    /**	Mouse Control	*/
    GlassPaneMouseAdapter gma;

    /**	Alternative concept : all Graphs are sharing one window	*/
    static final boolean SINGLETON = false;

    /**	the windowMenu : connected with the core Frame, see documentation */
    JMenu windowMenu;

    /** ID set for the Figure(-panels) */
    UniqueIDSet idSet;

    JMenu interactionsMenu = new JMenu("Tools");

    public VisualizerFrame() {
        super("Viewer");
        log.debug("Constructing VisualizerFrame");
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(this);
        this.addComponentListener(this);
        content = this.getContentPane();
        content.setLayout(new BorderLayout());
        glass = (JPanel) getGlassPane();
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        this.setWindowName("Viewer_" + sdf.format(date));
        waitPanel = new WaitPanel();
        idSet = new UniqueIDSet();
    }

    public static VisualizerFrame getInstance() {
        if (SINGLETON == true) {
            if (vFrame == null) {
                log.debug("vframe is null");
                synchronized (VisualizerFrame.class) {
                    if (vFrame == null) {
                        vFrame = new VisualizerFrame();
                    }
                }
            }
            return vFrame;
        } else {
            vFrame = new VisualizerFrame();
            return vFrame;
        }
    }

    /**
	 * inits the main plot to the FigurePanel
	 * @param p
	 */
    public void initFigurePanel(FigurePanel p) {
        int newid = idSet.getNextID();
        p.setID(newid);
        log.debug("add figurepanel ");
        log.debug("giv em id :" + newid);
        log.debug("figure to str: " + p);
        log.debug("---");
        xpList.add(p);
        this.xp = p;
        xp.setGraphicalViewer(this);
        xp.addMouseMotionListener(this);
        content.removeAll();
        content.add(xp, BorderLayout.CENTER);
        slit = new ActivationPanel(this);
        content.add(slit, BorderLayout.PAGE_END);
        pack();
        p.setPreferredSize(new Dimension(500, 500));
        p.setSize(new Dimension(500, 500));
        setSize(700, 500);
        initMenu();
        repaint();
    }

    /**
	 * Adds a Figure(-panel) to the Viewerframe
	 * @param xp2
	 */
    public void addFigurePanel(FigurePanel xp2) {
        if (xpList.size() >= 1) {
            xpList.add(xp2);
            xp2.setGraphicalViewer(this);
            xp2.addMouseMotionListener(this);
            content.removeAll();
            holder = new JPanel();
            int gcols = 0;
            int grows = 0;
            int xpnum = xpList.size();
            if (xpnum == 2) {
                gcols = 2;
                grows = 1;
            } else if (xpnum == 3 || xpnum == 4) {
                gcols = 2;
                grows = 2;
            } else if (xpnum == 5 || xpnum == 6) {
                gcols = 3;
                grows = 2;
            } else {
                int quadnum = 0;
                quadnum = (int) Math.sqrt(xpnum);
                gcols = quadnum;
                grows = quadnum;
            }
            int newid = idSet.getNextID();
            xp2.setID(newid);
            log.debug("add figurepanel ");
            log.debug("giv em id :" + newid);
            log.debug("figure to str: " + xp2);
            log.debug("---");
            holder.setLayout(new GridLayout(grows, gcols));
            holder.setBackground(Color.white);
            holder.addMouseMotionListener(this);
            for (FigurePanel p : xpList) {
                holder.add(p);
            }
            content.add(holder, BorderLayout.CENTER);
            ActivationPanel slit = new ActivationPanel(this);
            content.add(slit, BorderLayout.PAGE_END);
            toolboxPanel = new ToolboxPanel(this);
            pack();
            xpList.get(0).setPreferredSize(new Dimension(450, 500));
            xpList.get(0).setSize(new Dimension(450, 500));
            xpList.get(1).setPreferredSize(new Dimension(450, 500));
            xpList.get(1).setSize(new Dimension(450, 500));
            setSize(700, 500);
            repaint();
        } else {
            initFigurePanel(xp2);
        }
    }

    /**
	 * Adds an interaction area to the left side of the viewer
	 * The Panels can be exchanged with an interaction during runtime.
	 * @param a panel
	 */
    public void setSidePanel(JPanel panel) {
        Component c = content.getComponentAt(new Point(1, getHeight() / 2));
        if (!(c instanceof FigurePanel)) {
            content.remove(c);
        }
        content.add(panel, BorderLayout.WEST);
    }

    public void removeFigurePanel(FigurePanel figure) {
        xpList.remove(figure);
        content.removeAll();
        holder = new JPanel();
        int gcols = 0;
        int grows = 0;
        int xpnum = xpList.size();
        if (xpnum == 1) {
            gcols = 1;
            grows = 1;
        } else if (xpnum == 2) {
            gcols = 2;
            grows = 1;
        } else if (xpnum == 3 || xpnum == 4) {
            gcols = 2;
            grows = 2;
        } else if (xpnum == 5 || xpnum == 6) {
            gcols = 3;
            grows = 2;
        } else {
            int quadnum = 0;
            quadnum = (int) Math.sqrt(xpnum);
            gcols = quadnum;
            grows = quadnum;
        }
        holder.setLayout(new GridLayout(grows, gcols));
        holder.setBackground(Color.white);
        holder.addMouseMotionListener(this);
        for (FigurePanel p : xpList) {
            holder.add(p);
        }
        content.add(holder, BorderLayout.CENTER);
        ActivationPanel slit = new ActivationPanel(this);
        content.add(slit, BorderLayout.PAGE_END);
        toolboxPanel = new ToolboxPanel(this);
        pack();
        setSize(700, 500);
        repaint();
    }

    /**
	 * Builds the Toolbox and the Menu "Tools" by calling the interactions of the figures
	 */
    public void initToolbox() {
        if (toolboxPanel == null) {
            toolboxPanel = new ToolboxPanel(this);
        } else {
            toolboxPanel.removeAll();
            interactionsMenu.removeAll();
        }
        Vector<AbstractInteraction> v = new Vector<AbstractInteraction>();
        for (FigurePanel fig : xpList) {
            for (AbstractInteraction iaction : fig.getUniqueFeatures()) {
                if (!v.contains(iaction)) {
                    v.add(iaction);
                }
            }
        }
        JButton b;
        for (AbstractInteraction feat : v) {
            JLabel label = feat.getInteractionLabel();
            final int mode = feat.getTriggerID();
            b = new JButton();
            b.add(label);
            b.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    log.debug("Interaction Mode Update");
                    for (FigurePanel fig : xpList) {
                        fig.setMode(mode);
                    }
                }
            });
            b.setAlignmentX(JComponent.CENTER_ALIGNMENT);
            b.setAlignmentY(JComponent.CENTER_ALIGNMENT);
            b.setContentAreaFilled(false);
            b.addMouseListener(new MouseAdapter() {

                public void mouseEntered(MouseEvent me) {
                    toolboxPanel.dispatchEvent(me);
                }

                public void mouseExited(MouseEvent me) {
                    toolboxPanel.dispatchEvent(me);
                }
            });
            toolboxPanel.add(b);
            JMenuItem item = new JMenuItem(label.getText());
            item.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    log.debug("Interaction Mode Update");
                    for (FigurePanel fig : xpList) {
                        fig.setMode(mode);
                    }
                }
            });
            interactionsMenu.add(item);
            interactionsMenu.updateUI();
        }
        toolboxPanel.repaint();
        slit.repaint();
        repaint();
    }

    /**
	 * Gets a Unique ID within the Viewer
	 * @return
	 */
    public int getUID() {
        return idSet.getNextID();
    }

    public void initMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu m = new JMenu("Viewer");
        JMenuItem mi = new JMenuItem("Add to Protocol");
        mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK));
        mi.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                addToProtocol();
            }
        });
        m.add(mi);
        mi = new JMenuItem("Export Figure...");
        mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK));
        mi.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                ExportDialog export = new ExportDialog();
                if (holder != null) {
                    export.showExportDialog(holder, "Export view as ...", holder, "plot");
                } else {
                    export.showExportDialog(xp, "Export view as ...", xp, "plot");
                }
            }
        });
        m.add(mi);
        JMenu viewerSettings = new JMenu("Settings");
        mi = new JMenuItem("Background");
        mi.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                Color c = JColorChooser.showDialog(null, "Select Background Color", Color.WHITE);
                xp.setBackground(c);
            }
        });
        viewerSettings.add(mi);
        m.add(viewerSettings);
        m.addSeparator();
        mi = new JMenuItem("Close Viewer");
        mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK));
        mi.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                windowClosing(null);
            }
        });
        m.add(mi);
        menuBar.add(m);
        m = new JMenu("Edit");
        if (xp != null) {
            JMenu addmenu = new JMenu("Add");
            createViewMenu(xp.getSourceNode(), addmenu);
            m.add(addmenu);
            menuBar.add(m);
        }
        if (xpList.size() > 1) {
            JMenu remmenu = new JMenu("Remove");
            for (int i = 1; i < xpList.size(); i++) {
                final FigurePanel tmp = xpList.get(i);
                JMenuItem item = new JMenuItem(tmp.getRILabelString());
                item.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent arg0) {
                        removeFigurePanel(tmp);
                    }
                });
                remmenu.add(item);
            }
            m.add(remmenu);
        }
        ArrayList<JMenu> plotMenus = new ArrayList<JMenu>();
        for (int k = 0; k < xpList.size(); k++) {
            ArrayList<IPlotable> plots = xpList.get(k).getPlotables();
            int numLabels = 0;
            for (int i = 0; i < plots.size(); i++) {
                final IPlotable plot = plots.get(i);
                String label = plot.getPlotMenuLabel();
                JMenu plotMenu = new JMenu("to be defined");
                if (label == null || label.equals("")) {
                } else {
                    plotMenu.setText(label);
                    numLabels++;
                }
                int num = plot.getNumberOfDialogs();
                if (num > 0) {
                    for (int j = 0; j < num; j++) {
                        JMenuItem dmi = new JMenuItem(plot.getDialogLabel(j));
                        final int numDialog = j;
                        dmi.addActionListener(new ActionListener() {

                            public void actionPerformed(ActionEvent arg0) {
                                Object ldialog = plot.getDialog(numDialog);
                                if (ldialog instanceof JDialog) {
                                    ((JDialog) ldialog).setVisible(true);
                                } else if (ldialog instanceof IExecutor) {
                                    ((IExecutor) ldialog).execute();
                                }
                            }
                        });
                        plotMenu.add(dmi);
                    }
                    plotMenus.add(plotMenu);
                }
            }
        }
        if (xpList.size() == 1 && plotMenus.size() > 0) {
            if (plotMenus != null) {
                log.debug(plotMenus.size());
                for (JMenu me : plotMenus) {
                    log.debug("menu : " + me);
                }
                JMenu menu = plotMenus.get(0);
                if (menu != null) {
                    menuBar.add(menu);
                }
            }
        } else if (xpList.size() > 1) {
            JMenu plotsMenu = new JMenu("Plots");
            for (JMenu pmenu : plotMenus) {
                plotsMenu.add(pmenu);
            }
            menuBar.add(plotsMenu);
        }
        menuBar.add(interactionsMenu);
        windowMenu = new JMenu("Window");
        JMenuItem coreItem = new JMenuItem("Main");
        coreItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK));
        coreItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                Core.frame.setVisible(false);
                Core.frame.setVisible(true);
            }
        });
        windowMenu.add(coreItem);
        menuBar.add(windowMenu);
        JMenu helpMenu = new JMenu("Help");
        int cntHelp = 0;
        if (cntHelp > 0) {
            menuBar.add(helpMenu);
        }
        if (DEBUG == true) {
            m = new JMenu("Debug");
            mi = new JMenuItem("Freeze");
            mi.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    xp.freeze();
                }
            });
            m.add(mi);
            mi = new JMenuItem("Melt");
            mi.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    xp.melt();
                }
            });
            m.add(mi);
            m.addSeparator();
            mi = new JMenuItem("Toolbox open");
            mi.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    showToolBox();
                }
            });
            m.add(mi);
            mi = new JMenuItem("Toolbox close");
            mi.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    hideToolBox();
                }
            });
            m.add(mi);
            m.addSeparator();
            m.addSeparator();
            mi = new JMenuItem("Progress start");
            mi.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    showWaitPanel("Debug: Just a moment!");
                }
            });
            m.add(mi);
            mi = new JMenuItem("Progress stop");
            mi.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    hideWaitPanel();
                }
            });
            m.add(mi);
            m.addSeparator();
            mi = new JMenuItem("Add to Protocol");
            mi.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    addToProtocol();
                }
            });
            m.add(mi);
            m.addSeparator();
            mi = new JMenuItem("Print Properties...");
            mi.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    Properties p = getProperties();
                    Enumeration keys = p.keys();
                    while (keys.hasMoreElements()) {
                        String key = (String) keys.nextElement();
                        log.debug(key + " : " + p.getProperty(key));
                    }
                    log.debug("to XML");
                    try {
                        p.storeToXML(System.out, "test");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            });
            m.add(mi);
            mi = new JMenuItem("RESTORE with Properties...");
            mi.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    getInstance().restore(xp.getSourceNode(), getProperties());
                }
            });
            m.add(mi);
            menuBar.add(m);
        }
        this.getRootPane().setJMenuBar(menuBar);
        menuBar.updateUI();
    }

    /**
	 * create the view menu for the given Experiment Node.
	 * This will add all <code>IVisualizer</code> Plugins that accept the nodes
	 * output type as input
	 *
	 * @param node
	 * @param view
	 * @todo check node's data and choose visualizers appropiately
	 */
    protected void createViewMenu(final ExperimentNode node, JMenu view) {
        ExtensionPoint extPoint = Core.manager.getRegistry().getExtensionPoint("vademecum.core", "Visualizer");
        ArrayList<Extension> v = new ArrayList<Extension>(extPoint.getAvailableExtensions());
        Collections.sort(v, new ExtensionComparator());
        Iterator iter = v.iterator();
        while (iter.hasNext()) {
            Extension ext = (Extension) iter.next();
            String name = ext.getParameter("name").valueAsString();
            String description = ext.getParameter("description").valueAsString();
            String category = ext.getParameter("category").valueAsString();
            String iconUrl = ext.getParameter("icon").valueAsString();
            final IVisualizer vis = ExtensionFactory.createVisualizer(ext.getUniqueId(), node);
            log.debug("vis" + vis);
            if (vis instanceof VademecumViewer) {
                log.debug("vadmecum viewer found");
                if (vis != null) {
                    Plugin plugin = Core.manager.getPluginFor(node.getMethod());
                    PluginClassLoader pcl = Core.manager.getPluginClassLoader(plugin.getDescriptor());
                    URL url = pcl.getResource(iconUrl);
                    JMenu menu = this.createDotHierarchyMenu(view, category, true);
                    JMenuItem item = new JMenuItem(name);
                    if (url != null) {
                        ImageIcon icon = new ImageIcon(url);
                        item.setIcon(icon);
                    }
                    item.setToolTipText(description);
                    item.addActionListener(new ActionListener() {

                        public void actionPerformed(ActionEvent e) {
                            log.debug("add figure ..." + e);
                            addFigurePanel(((VademecumViewer) vis).getFigure(node));
                            initMenu();
                            initToolbox();
                        }
                    });
                    menu.add(item);
                }
            }
        }
    }

    private JMenu createDotHierarchyMenu(JMenu parent, String dotSeparated, boolean activePath) {
        if (dotSeparated.length() == 0) return parent;
        dotSeparated = dotSeparated.trim();
        int fdot = dotSeparated.indexOf('.');
        if (fdot <= 0) fdot = dotSeparated.length();
        String cat = dotSeparated.substring(0, fdot);
        String rest = "";
        if (fdot < dotSeparated.length()) {
            rest = dotSeparated.substring(fdot + 1, dotSeparated.length());
        }
        boolean found = false;
        for (int i = 0; i < parent.getItemCount(); i++) {
            JMenuItem comp = parent.getItem(i);
            if (!(comp instanceof JMenu)) continue;
            if (cat.equals(comp.getText())) {
                found = true;
                return createDotHierarchyMenu((JMenu) comp, rest, activePath);
            }
        }
        if (!found) {
            JMenu menu = new JMenu(cat);
            menu.setEnabled(activePath);
            parent.add(menu);
            return createDotHierarchyMenu(menu, rest, activePath);
        }
        return parent;
    }

    public void showToolBox() {
        if (editMode == true) {
            toolboxPanel.validate();
            sheet = toolboxPanel;
            toolboxPanel.repaint();
            toolboxVisible = true;
            sheet.setBounds(-1, this.getHeight() - ToolboxPanel.TBHEIGHT, this.getWidth(), ToolboxPanel.TBHEIGHT);
            glass.setLayout(null);
            sheet.setBorder(new LineBorder(Color.black, 1));
            glass.removeAll();
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.anchor = GridBagConstraints.NORTH;
            glass.add(sheet, gbc);
            gbc.gridy = 1;
            gbc.weighty = Integer.MAX_VALUE;
            glass.add(Box.createGlue(), gbc);
            glass.setVisible(true);
            if (gma == null) {
                gma = new GlassPaneMouseAdapter(this);
            }
            glass.addMouseMotionListener(gma);
            glass.addMouseListener(gma);
        }
    }

    public void hideToolBox() {
        this.toolboxVisible = false;
        glass.setVisible(false);
    }

    public void showWaitPanel(String message) {
        waitPanel.width = getWidth() - 10;
        waitPanel.height = getHeight() - 30;
        waitPanel.setText(message);
        waitPanel.setBounds(getBounds().x, 0, getBounds().width, getBounds().height);
        setGlassPane(waitPanel);
        waitPanel.start();
    }

    public void textFlushToWaitPanel(String s) {
        waitPanel.setText(s);
    }

    public void hideWaitPanel() {
        waitPanel.stop();
        this.setGlassPane(glass);
        glass.setVisible(true);
    }

    public boolean toolboxVisible() {
        return this.toolboxVisible;
    }

    /**
	 * Sets the Edit Status. If true, the toolbox can be activated,
	 * else not :-!
	 * Could be interesting for static graphs without interactions.
	 * @param b boolean value
	 */
    public void setEditStatus(boolean b) {
        this.editMode = b;
    }

    /**
	 * Returns whether the toolbox can be accessed, and contents can be 
	 * edited by this way.
	 * 
	 * @return boolean value
	 */
    public boolean isEditable() {
        return this.editMode;
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
    }

    public void windowActivated(WindowEvent e) {
        if (xp != null) {
            xp.melt();
        }
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        log.debug("Viewer is Closing....");
        try {
            System.out.println("signing off");
            Core.signOffWindow(this);
        } catch (Exception ex) {
            System.err.println(ex);
        }
        this.xp.freeze();
        this.dispose();
        System.gc();
    }

    public void windowDeactivated(WindowEvent e) {
        log.debug("win is now deactivated");
        xp.freeze();
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowOpened(WindowEvent e) {
        log.debug("Window Opened");
        try {
            Core.registerWindow(this);
        } catch (Exception ex) {
        }
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
        log.debug("MouseMoved : " + e.getPoint());
    }

    public void updateViewer() {
        this.initMenu();
        editMode = true;
        this.repaint();
    }

    public void componentHidden(ComponentEvent arg0) {
    }

    public void componentMoved(ComponentEvent arg0) {
        try {
            log.debug("Win Moved");
            xp.freeze();
            try {
                Thread.currentThread().sleep(2);
            } catch (Exception ex) {
            }
            xp.melt();
        } catch (Exception e) {
        }
    }

    public void componentResized(ComponentEvent arg0) {
        if (this.toolboxVisible()) {
            this.hideToolBox();
        }
    }

    public void componentShown(ComponentEvent arg0) {
        log.debug("Win Shown");
    }

    public void stateChanged(ChangeEvent ce) {
    }

    public void showWindow() {
        log.debug("getting focused");
        this.setVisible(false);
        this.setVisible(true);
        this.requestFocus();
    }

    public String getWindowName() {
        return vmcWindowName;
    }

    public void setWindowName(String s) {
        vmcWindowName = s;
    }

    public void propertyChange(PropertyChangeEvent pce) {
        if (pce.getPropertyName().equals("menuRefresh")) {
            JMenuItem mainItem = windowMenu.getItem(0);
            windowMenu.removeAll();
            windowMenu.add(mainItem);
            JMenu coreMenu = Core.windowsMenu;
            int numItems = coreMenu.getItemCount();
            if (numItems >= 2) {
                windowMenu.addSeparator();
            }
            for (int i = 0; i < numItems; i++) {
                JMenuItem mi = coreMenu.getItem(i);
                if (!mi.getText().equals(getWindowName())) {
                    ArrayList<VademecumWindow> wa = Core.getOpenWindowList();
                    final VademecumWindow vmcWin = wa.get(i);
                    JMenuItem nmi = new JMenuItem(mi.getText());
                    nmi.addActionListener(new ActionListener() {

                        public void actionPerformed(ActionEvent arg0) {
                            vmcWin.showWindow();
                        }
                    });
                    windowMenu.add(nmi);
                }
            }
            windowMenu.updateUI();
        }
    }

    /**	Takes a snapshot of the Viewer */
    public BufferedImage getThumbnail() {
        if (xpList.size() == 1) {
            return xp.getThumbnail();
        } else {
            int width = holder.getWidth();
            int height = holder.getHeight();
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2D = image.createGraphics();
            holder.paint(g2D);
            return image;
        }
    }

    public void addToProtocol() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String oldWin = this.getWindowName();
        this.setWindowName("Viewer_" + sdf.format(date));
        Protocol p = xp.getExpertice().getProtocol();
        ImageIcon ico = new ImageIcon(this.getThumbnail());
        ResLabel rl = new ResLabel(ico, xp.getRILabelString());
        rl.setToolTipText(xp.getRITooltipString());
        ResItem ri = new ResItem(rl);
        final Properties properties = this.getProperties();
        ri.setProperties(properties);
        ri.addAction("Show Viewer", new VademecumWindowClickAction(properties));
        log.debug("sourceNode : " + xp.getSourceNode());
        p.addResultItem(ri, xp.getSourceNode());
        this.setWindowName(oldWin);
    }

    public Properties getProperties() {
        Properties p = new Properties();
        p.setProperty("RESTORECLASS", this.getClass().getCanonicalName());
        p.setProperty("WINDOW_ID", getWindowName());
        p.setProperty("WinPluginID", "Core");
        String prefix = "Frame_";
        p.setProperty(prefix + "x", Integer.toString(getBounds().x));
        p.setProperty(prefix + "y", Integer.toString(getBounds().y));
        p.setProperty(prefix + "width", Integer.toString(getBounds().width));
        p.setProperty(prefix + "height", Integer.toString(getBounds().height));
        p.setProperty(prefix + "numFigures", Integer.toString(xpList.size()));
        for (int i = 0; i < xpList.size(); i++) {
            FigurePanel fig = xpList.get(i);
            String fid = String.valueOf(fig.getID());
            p.setProperty(prefix + "fig_" + String.valueOf(i), fid);
        }
        for (int i = 0; i < xpList.size(); i++) {
            Properties p2 = xpList.get(i).getProperties();
            Enumeration keyEn = p2.keys();
            while (keyEn.hasMoreElements()) {
                String key = (String) keyEn.nextElement();
                p.setProperty(key, p2.getProperty(key));
            }
        }
        log.debug("GOT Properties : ");
        Enumeration keyEn = p.keys();
        while (keyEn.hasMoreElements()) {
            String key = (String) keyEn.nextElement();
            String val = p.getProperty(key);
            log.debug(key + ": " + val);
        }
        log.debug("XP List Size : " + xpList.size());
        return p;
    }

    public void setProperties(Properties p) {
        this.setWindowName(p.getProperty("WINDOW_ID"));
        p.setProperty("WINDOW_ID", getWindowName());
        String prefix = "Frame_";
        int x = Integer.parseInt(p.getProperty(prefix + "x"));
        int y = Integer.parseInt(p.getProperty(prefix + "y"));
        int width = Integer.parseInt(p.getProperty(prefix + "width"));
        int height = Integer.parseInt(p.getProperty(prefix + "height"));
        this.setBounds(x, y, width, height);
    }

    /**
	 * Restore Window (and containing panels) with a Properties-Object
	 */
    public void restore(ExperimentNode en, Properties p) {
        String framePrefix = "Frame_";
        VisualizerFrame vf = VisualizerFrame.getInstance();
        int numFigures = Integer.parseInt(p.getProperty(framePrefix + "numFigures"));
        for (int k = 0; k < numFigures; k++) {
            FigurePanel xp = new FigurePanel(en);
            int fid = Integer.parseInt(p.getProperty(framePrefix + "fig_" + String.valueOf(k) + ""));
            log.debug("fid  : " + fid);
            if (xp.expertice == null) {
                try {
                    xp.expertice = (Expertice) Core.projectPanel.getSelectedComponent();
                } catch (Exception e) {
                    System.err.println("no expertice found");
                    e.printStackTrace();
                }
            }
            xp.setProperties(p);
            vf.addFigurePanel(xp);
            log.debug("Restoring VGraphics");
            String panprefix = "Panel_" + String.valueOf(fid);
            int numVG = Integer.parseInt(p.getProperty(panprefix + "NUMVGraphics"));
            Properties vp = new Properties();
            Enumeration fkeyEn = p.keys();
            while (fkeyEn.hasMoreElements()) {
                String key = (String) fkeyEn.nextElement();
                String vkey = key.replaceFirst(panprefix, "");
                vp.setProperty(vkey, p.getProperty(key));
            }
            log.debug("VG Properties : " + vp);
            for (int i = 0; i < numVG; i++) {
                String vgKey = panprefix + "VG" + (i + 1);
                log.debug("prefix : " + panprefix);
                int vgID = Integer.parseInt(p.getProperty(vgKey));
                String vgPrefix = panprefix + "VGraphics" + vgID + "_";
                String pluginid = p.getProperty(vgPrefix + "PluginID");
                log.debug("plugin-id : >" + pluginid + "<");
                ClassLoader classLoader;
                if (!pluginid.equals("Core")) {
                    classLoader = null;
                    try {
                        Plugin plugin = Core.manager.getPlugin(pluginid);
                        PluginDescriptor desc = plugin.getDescriptor();
                        ClassLoader pluginclassloader = Core.manager.getPluginClassLoader(desc);
                        log.debug("Plugin ClassLoader : " + pluginclassloader);
                        classLoader = pluginclassloader;
                    } catch (PluginLifecycleException e) {
                        e.printStackTrace();
                    }
                } else {
                    log.debug("Using Core Classloader");
                    Plugin plugin = Core.corePlugin;
                    PluginDescriptor desc = plugin.getDescriptor();
                    ClassLoader pluginclassloader = Core.manager.getPluginClassLoader(desc);
                    log.debug("Plugin ClassLoader : " + pluginclassloader);
                    classLoader = pluginclassloader;
                }
                String className = p.getProperty(vgPrefix + "Name");
                log.info("Restoring VGraphics Object : " + className);
                Class c;
                try {
                    c = Class.forName(className, true, classLoader);
                    VGraphics vgraphics = (VGraphics) c.newInstance();
                    vgraphics.initInteractions();
                    log.debug(vgraphics);
                    int type = Integer.parseInt(p.getProperty(panprefix + "VGraphics" + vgID + "_" + "type"));
                    int orientation = Integer.parseInt(p.getProperty(panprefix + "VGraphics" + vgID + "_" + "orientation"));
                    log.debug("orient : " + orientation);
                    vgraphics.setType(type);
                    vgraphics.setOrientation(orientation);
                    System.out.println("restore Check : widget ? " + vgraphics.getType());
                    if (vgraphics.isWidget()) {
                        xp.addWidget(vgraphics);
                    } else {
                        xp.addPlot(vgraphics);
                    }
                    int nonValidID = vgraphics.getID();
                    xp.releaseViewableID(nonValidID);
                    vgraphics.setID(vgID);
                    xp.addViewableID(vgID);
                    vgraphics.setProperties(vp);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            log.debug("Reconstruct Interlinkings");
            ArrayList<VGraphics> vglist = xp.getViewables();
            for (VGraphics vg : vglist) {
                vg.restoreLinking(p);
            }
        }
        vf.initToolbox();
        vf.setProperties(p);
        vf.initMenu();
        vf.setVisible(true);
    }

    /**
	 * Adds main Plot to the FigurePanel
	 * @deprecated use addFigurePanel instead
	 */
    public void initXplorePanel(FigurePanel p) {
        initFigurePanel(p);
    }

    /**
	 * @deprecated use addFigurePanel instead
	 * @param xp2
	 */
    public void addXplorePanel(FigurePanel xp2) {
        addFigurePanel(xp2);
    }

    public JMenuBar getWindowMenuBar() {
        return null;
    }
}
