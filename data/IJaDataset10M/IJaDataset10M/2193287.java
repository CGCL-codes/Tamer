package org.formaria.swing.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Hashtable;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import org.formaria.swing.SwingWidgetAdapter;
import org.formaria.swing.docking.CardPanel;
import org.formaria.swing.docking.Dockable;
import org.formaria.swing.docking.DockingPanel;
import org.formaria.swing.docking.DockingSideBar;
import org.formaria.xml.XmlElement;
import org.formaria.aria.PageSupport;
import org.formaria.aria.ApplicationContext;
import org.formaria.aria.Project;
import org.formaria.aria.ProjectManager;
import org.formaria.aria.StartupObject;
import org.formaria.aria.helper.Translator;
import org.formaria.aria.style.Style;
import org.formaria.aria.style.StyleEx;
import org.formaria.aria.style.StyleManager;
import org.jdesktop.swingx.JXMultiSplitPane;
import org.jdesktop.swingx.MultiSplitLayout;
import org.formaria.swing.painter.Painter;
import org.formaria.swing.painter.PainterAsSwingXPainter;

/**
 * <p>A startup class for a desktop/MDI style of application.</p>
 * <p>For information on the initial docking layout please see the article
 * <a ref="http://today.java.net/pub/a/today/2006/03/23/multi-split-pane.html">
 * MultiSplitPane: Splitting Without Nesting</a></p>
 * <p>Copyright: Copyright (c) Formaria Ltd., 2008<br>
 * License:      see license.txt
 * @version $Revision: 1.1 $
 */
public class DockingApp extends JFrame implements StartupObject {

    protected ApplicationContext applicationContext;

    protected CardPanel cardPanel;

    protected JPanel dockingPanel;

    protected JXMultiSplitPane multiSplitPane;

    protected DockingSideBar leftSidebar;

    protected DockingSideBar rightSidebar;

    protected DockingSideBar bottomSidebar;

    protected PageSupport northDecoration;

    protected JMenuBar menuBar;

    protected Project currentProject;

    protected String defaultLayoutDef = "(COLUMN " + " (ROW name=top weight=0.7 " + "   (LEAF name=left weight=0.1) " + "   (LEAF weight=0.8 name=content) " + "   (LEAF name=right weight=0.1)" + " ) " + " (LEAF name=bottom weight=0.3)" + ")";

    private boolean exclusiveView;

    /**
   * main method to be invoked as an application. This method is invoked as the
   * entry point to the 'Application', it is not used if an Applet is being
   * launched. This method establishes the frame within which the application
   * runs. If overloading this method remeber to call the setup method.
   * @param args the command line arguments
   */
    public static void main(String args[]) {
        final String[] theArgs = args;
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                createAndShowGUI(theArgs);
            }
        });
    }

    /**
   * Creates a new instance of DesktopApp
   * @param args the startup arguments
   */
    public DockingApp(String[] args) {
        super("Aria");
        currentProject = ProjectManager.getCurrentProject(this);
        dockingPanel = new JPanel();
        dockingPanel.setLayout(new BorderLayout());
        Container glassPane = (Container) getRootPane().getGlassPane();
        leftSidebar = new DockingSideBar(glassPane, "west");
        rightSidebar = new DockingSideBar(glassPane, "east");
        bottomSidebar = new DockingSideBar(glassPane, "south");
        MultiSplitLayout.Node modelRoot = MultiSplitLayout.parseModel(defaultLayoutDef);
        multiSplitPane = new JXMultiSplitPane();
        multiSplitPane.setDividerSize(5);
        multiSplitPane.getMultiSplitLayout().setModel(modelRoot);
        MultiSplitLayout multiSplitLayout = multiSplitPane.getMultiSplitLayout();
        multiSplitLayout.setLayoutMode(MultiSplitLayout.USER_MIN_SIZE_LAYOUT);
        multiSplitLayout.layoutByWeight(multiSplitPane);
        cardPanel = new CardPanel(multiSplitPane);
        dockingPanel.add(leftSidebar, BorderLayout.WEST);
        dockingPanel.add(rightSidebar, BorderLayout.EAST);
        dockingPanel.add(bottomSidebar, BorderLayout.SOUTH);
        dockingPanel.add(cardPanel, BorderLayout.CENTER);
        setContentPane(dockingPanel);
        SwingWidgetAdapter.getInstance();
        applicationContext = new ApplicationContext(this, "org.formaria.swing.app.DockableFrame", args);
        StyleManager sm = currentProject.getStyleManager();
        if (sm.hasStyle("dockingApp")) {
            Style dockingStyle = sm.getStyle("dockingApp");
            multiSplitPane.setBackground(dockingStyle.getStyleAsColor(Style.COLOR_BACK));
            StyleEx exStyle = (StyleEx) dockingStyle;
            int idx = exStyle.getStyleIndex("padding");
            if (idx > 0) {
                int padding = new Integer(exStyle.getStyleValue(idx).toString()).intValue();
                multiSplitPane.setDividerSize(padding);
            }
        }
        applyStyles(leftSidebar);
        applyStyles(rightSidebar);
        applyStyles(bottomSidebar);
    }

    /**
   * Carry out any post creation styling. This method is called one the
   * application frameowrk has been setup. By default the method sets the 
   * colours of the component as the style manager is now available.
   */
    public void applyStyles(DockingSideBar target) {
        Color sidebarBkColor = SystemColor.control;
        Color sidebarTextColor = SystemColor.controlText;
        Color sidebarRolloverTextColor = SystemColor.controlText;
        StyleManager sm = currentProject.getStyleManager();
        if (sm.hasStyle("dockingSidebar")) {
            Style xstyle = sm.getStyle("dockingSidebar");
            sidebarBkColor = xstyle.getStyleAsColor(Style.COLOR_BACK);
            sidebarTextColor = xstyle.getStyleAsColor(Style.COLOR_FORE);
            xstyle = sm.getStyle("dockingSidebar/active");
            sidebarRolloverTextColor = xstyle.getStyleAsColor(Style.COLOR_FORE);
        }
        target.applyStyles(sidebarBkColor, sidebarTextColor, sidebarRolloverTextColor);
    }

    /**
   * Set a background painter for the empty docking panel
   * @param painter a background painter
   */
    public void setBackgroundPainter(Painter p) {
        multiSplitPane.setBackgroundPainter(new PainterAsSwingXPainter(p));
    }

    /**
   * Get the side bar for a particular object
   * @param key the key for looking up the sidebar
   * @return the sidebar
   */
    public DockingSideBar getSidebar(String key) {
        String keyLower = key.toLowerCase();
        if (keyLower.equals("south") || keyLower.equals("bottom")) return bottomSidebar; else if (keyLower.equals("east") || keyLower.equals("right")) return rightSidebar; else if (keyLower.equals("west") || keyLower.equals("left")) return leftSidebar; else return null;
    }

    /**
   * Set the visibility of the sidebars and toolbars
   * @param state true to make the decorations visible
   */
    public void setDecorationsVisible(boolean state) {
        leftSidebar.setVisible(state);
        rightSidebar.setVisible(state);
        bottomSidebar.setVisible(state);
        if (northDecoration != null) ((Component) northDecoration).setVisible(state);
    }

    /**
   * <p>Display a window decoration, for example a toolbar.</p>
   * <p>This method only sets
   * the decoration for the NORTH constraint. It is intended for setting a 
   * toolbar.</p>
   * @param page the new page
   * @param constraint a value controlling how and where the decoration is 
   * displayed, this value is application type specific
   * @return the page being displayed or null if the constraint is not handled
   * @deprecated use addDecoration
   */
    public Object displayDecoration(PageSupport page, String constraint) {
        return addDecoration(page, constraint);
    }

    /**
   * <p>Display a window decoration, for example a toolbar.</p>
   * <p>This method only sets
   * the decoration for the NORTH constraint. It is intended for setting a 
   * toolbar.</p>
   * @param page the new page
   * @param constraint a value controlling how and where the decoration is 
   * displayed, this value is application type specific
   * @return the page being displayed or null if the constraint is not handled
   */
    public Object addDecoration(PageSupport page, String constraint) {
        String key = constraint.toLowerCase();
        if (key.equals("north") || key.equals("top")) {
            northDecoration = page;
            dockingPanel.add((Component) page, BorderLayout.NORTH);
            return page;
        }
        return null;
    }

    /**
   * Set the visibility of a border layout's component or so called decoration
   * @param constraint must be "NORTH" to have any effect
   * @param visible the visibility state
   */
    public void setDecorationVisibility(String constraint, boolean visible) {
        Component decoration = null;
        String key = constraint.toLowerCase();
        if (key.equals("north") || key.equals("top")) decoration = (Component) northDecoration; else if (key.equals("south") || key.equals("bottom")) decoration = bottomSidebar; else if (key.equals("east") || key.equals("right")) decoration = leftSidebar; else if (key.equals("west") || key.equals("left")) decoration = rightSidebar;
        if (decoration != null) {
            decoration.setVisible(visible);
            doLayout();
        }
    }

    /**
   * Show one of the content panels as the exclusive content, hiding the 
   * sidebars, headers and other decorations
   * @param comp the component to display exclusively in the content area
   * @param state true for an exclusive display, false to restore the normal display
   */
    public void showExclusive(Object comp, boolean state) {
        exclusiveView = state;
        setDecorationsVisible(!state);
        Dockable dockable = findDockable(comp);
        if (dockable != null) {
            dockable.header.zoomPanel();
            dockable.header.getParent().setVisible(!state);
            Object mo = currentProject.getObject("MenuBar");
            StartupObject startupObject = currentProject.getStartupObject();
            if (state) startupObject.setApplicationMenuBar(null); else startupObject.setApplicationMenuBar(mo);
        }
        cardPanel.doLayout();
        multiSplitPane.doLayout();
        repaint();
    }

    /**
   * Get the package name for the default widget set
   */
    public String getWidgetClassPackage() {
        return PageSupport.ARIA_SWING_PACKAGE;
    }

    /**
   * Get a startup parameter
   * @param param the name of the parameter
   */
    public String getParameter(String param) {
        return "";
    }

    /**
   * Get the content pane used by Aria - the container in which pages are 
   * displayed
   * @return the page container
   */
    public Object getContentPaneEx() {
        return multiSplitPane;
    }

    /**
   * Get the parent container/object
   * @return the parent
   */
    public Object getParentObject() {
        return super.getParent();
    }

    /**
   * Gets the URL of the document in which this applet is embedded. 
   * For example, suppose an applet is contained
   * within the document:
   * <blockquote><pre>
   *    http://java.sun.com/products/jdk/1.2/index.html
   * </pre></blockquote>
   * The document base is:
   * <blockquote><pre>
   *    http://java.sun.com/products/jdk/1.2/index.html
   * </pre></blockquote>
   *
   * @return  the {@link java.net.URL} of the document that contains this
   *          applet.
   * @see     java.applet.Applet#getCodeBase()
   */
    public URL getDocumentBase() {
        return null;
    }

    /**
   * Setup frameset. This method is called prior to the addition of any target 
   * areas in the framset and prior to the display of any pages. Since this
   * applet does not support configurable framesets, this method ignores the
   * parameter values passed.
   * @param params the framset parameters if any
   * <ul>
   * <li>config - the layout configuration, for example <code>(COLUMN (ROW weight=1.0 left (COLUMN middleTop content middleBottom) right) bottom)</code>
   * </ul>
   */
    public void setupFrameset(Hashtable params) {
        String layout = (String) params.get("config");
        if ((layout == null) || (layout.length() == 0)) layout = defaultLayoutDef;
        MultiSplitLayout.Node modelRoot = MultiSplitLayout.parseModel(layout);
        MultiSplitLayout multiSplitLayout = multiSplitPane.getMultiSplitLayout();
        multiSplitLayout.setModel(modelRoot);
        multiSplitLayout.layoutByWeight(multiSplitPane);
    }

    /**
   * Validate and repaint the display
   */
    public void refresh() {
        invalidate();
        validate();
        repaint();
    }

    public void setAppTitle(String title) {
        Translator translator = currentProject.getTranslator();
        setTitle(translator.translate(title));
    }

    /**
   * Set the application icon
   * @param img the image name
   */
    public void setIcon(Image img) {
        setIconImage(img);
    }

    /**
   * Setup the windowing.
   * @param context the owner application context
   * @param currentProject the owner project
   * @param clientWidth the desired width of the application
   * @param clientHeight the desired height of the application
   */
    public void setupWindow(ApplicationContext context, Project currentProject, int clientWidth, int clientHeight) {
        currentProject.setStartupParam("MainClass", "org.formaria.swing.app.DockingApp");
        setSize(clientWidth, clientHeight);
        addWindowListener(context);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = getSize();
        String center = null;
        try {
            center = currentProject.getStartupParam("CenterWin");
        } catch (Exception ex) {
        }
        if ((center != null) && (center.compareTo("true") == 0)) setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        setVisible(true);
        currentProject.setApplet(null);
        currentProject.setStartupObject(this);
        currentProject.setAppFrame(this);
        currentProject.setAppWindow(this);
    }

    /**
   * Get the menubar, setting it up if it is not already added to the 
   * application frame
   * @return the menu bar
   */
    public Object getApplicationMenuBar() {
        if (menuBar == null) menuBar = getJMenuBar();
        return menuBar;
    }

    /**
    * Set the menubar
    * @param mb the menubar
    */
    public void setApplicationMenuBar(Object mb) {
        setJMenuBar((JMenuBar) mb);
    }

    /**
   * Create the GUI and show it.  For thread safety,
   * this method should be invoked from the
   * event-dispatching thread.
   */
    private static void createAndShowGUI(String[] args) {
        DockingApp frame = new DockingApp(args);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
   * Save the layout
   */
    public void saveLayout(OutputStream os) {
        try {
            XMLEncoder e = new XMLEncoder(new BufferedOutputStream(os));
            MultiSplitLayout.Node model = multiSplitPane.getMultiSplitLayout().getModel();
            e.writeObject(model);
            e.close();
        } catch (Exception e) {
        }
    }

    /**
   * Attempt to restore the docking layout
   */
    public void loadLayout(InputStream is) {
        MultiSplitLayout multiSplitLayout = multiSplitPane.getMultiSplitLayout();
        try {
            XMLDecoder d = new XMLDecoder(new BufferedInputStream(is));
            MultiSplitLayout.Node model = (MultiSplitLayout.Node) (d.readObject());
            multiSplitLayout.setModel(model);
            multiSplitLayout.setFloatingDividers(false);
            d.close();
            multiSplitPane.setPreferredSize(model.getBounds().getSize());
            dockHiddenChildren();
        } catch (Exception exc) {
            MultiSplitLayout.Node model = MultiSplitLayout.parseModel(defaultLayoutDef);
            multiSplitLayout.setModel(model);
        }
    }

    /**
   * Restore the normal page views, as in the case of the docking layout where 
   * panels may be zoomed or minimized. This method is called prior to the 
   * display of a new page.
   */
    public void restoreViews() {
        if (!exclusiveView) cardPanel.restoreViews();
    }

    /**
   * Restore the application state
   * @return the elements containing the page state
   */
    public void restoreState(XmlElement stateElement) {
        String[] b = stateElement.getAttribute("b").split(",");
        Rectangle bounds = new Rectangle(Integer.parseInt(b[0]), Integer.parseInt(b[1]), Integer.parseInt(b[2]), Integer.parseInt(b[3]));
        setBounds(bounds);
    }

    /**
   * Save the application state
   * @param the elements to hold the page state
   */
    public void saveState(XmlElement stateElement) {
        Rectangle bounds = getBounds();
        stateElement.setAttribute("b", "" + bounds.x + "," + bounds.y + "," + bounds.width + "," + bounds.height + ",");
    }

    /**
   * Find the dockable object that wraps a particular component
   * @param content the content that is wrapped by the Dockable
   * @return the Dockable instance or null if the component was not found
   */
    public Dockable findDockable(Object content) {
        int numChildren = multiSplitPane.getComponentCount();
        for (int i = 0; i < numChildren; i++) {
            Component comp = multiSplitPane.getComponent(i);
            if (comp instanceof DockableFrame) {
                Dockable dockable = ((DockableFrame) comp).findDockable(content);
                if (dockable != null) return dockable;
            }
        }
        Dockable dockable = cardPanel.getDockable();
        if (dockable != null) {
            if ((dockable.content == content) || (dockable.dockedContainer == content) || (dockable.header == content)) return dockable;
        }
        return null;
    }

    /**
   * Dock any children that have been restored.
   */
    private void dockHiddenChildren() {
        MultiSplitLayout multiSplitLayout = multiSplitPane.getMultiSplitLayout();
        int numChildren = multiSplitPane.getComponentCount();
        for (int i = 0; i < numChildren; i++) {
            Component comp = multiSplitPane.getComponent(i);
            if (comp instanceof DockingPanel) {
                MultiSplitLayout.Node node = multiSplitLayout.getNodeForComponent(comp);
                if (!node.isVisible()) ((DockingPanel) comp).dock();
            }
        }
    }
}
