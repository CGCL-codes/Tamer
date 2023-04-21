package commonapp.gui;

import common.StringUtils;
import common.Utils;
import common.log.Log;
import commonapp.datadef.BaseDef;
import commonapp.datadef.PanelDef;
import commonapp.datadef.WidgetDef;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import javax.swing.JComponent;
import javax.swing.JMenuBar;
import javax.swing.JSplitPane;

/**
   Builds a GUI frame (LocalFrame) with a menu, toolbars, message area,
   desktop, etc. from a Frame definition.
*/
public class AppFrame {

    private static final int DIVIDER_SIZE = 8;

    private static final String A_DESKTOP = "desktop";

    private static HashMap<Class<?>, String> ourClassToName = new HashMap<Class<?>, String>();

    static {
        ourClassToName.put(ITree.class, BaseDef.E_TREE);
        ourClassToName.put(IDesktop.class, BaseDef.E_DESKTOP);
        ourClassToName.put(IButtonBar.class, BaseDef.E_BUTTON_BAR);
        ourClassToName.put(IMessageArea.class, BaseDef.E_MESSAGE_AREA);
    }

    private BaseHandler myBaseHandler = null;

    /** Name of the Frame definition. */
    private String myFrameDefName = null;

    /** Frame definition. */
    private BaseDef myFrameDef = null;

    /** GUI Frame. */
    private LocalFrame myFrame = null;

    /** Frame property name prefix. */
    private String myFramePropPrefix = null;

    /** Menu bar. */
    private JMenuBar myMenuBar = null;

    private IMenuBar myIMenuBar = null;

    private IButtonBar myButtonBar = null;

    private ITree myITree = null;

    private IMessageArea myMessageArea = null;

    /** Menu disable list. */
    private DisableList myMenuDisableList = new DisableList();

    private TreeMap<String, TreeDataNode> myTreeDataNodes = null;

    private TreeMap<String, BaseHandler> myComponents = new TreeMap<String, BaseHandler>();

    private TreeMap<String, BaseDef> myDefinitions = new TreeMap<String, BaseDef>();

    /**
     Constructor.

     @param theFrameDefName the name of the Frame definition.

     @param theBaseHandler the parent base handler that contains the
     message area.
  */
    public AppFrame(String theFrameDefName, BaseHandler theBaseHandler) {
        myFrameDef = BaseDef.find("Frame", theFrameDefName);
        if (myFrameDef == null) {
            System.exit(10);
        }
        myFrameDefName = theFrameDefName;
        myBaseHandler = theBaseHandler;
    }

    public void setDisableList(DisableList theMenuDisableList) {
        myMenuDisableList = theMenuDisableList;
    }

    /**
     Checks for enables/disables buttons, and menus
  */
    public void checkMenusEnabled() {
        if (myButtonBar != null) {
            myButtonBar.checkButtonsEnabled();
        }
        if (myIMenuBar != null) {
            myIMenuBar.checkMenusEnabled();
        }
    }

    public void addTreeDataNode(String theName, TreeDataNode theNode) {
        if (myTreeDataNodes == null) {
            myTreeDataNodes = new TreeMap<String, TreeDataNode>();
        }
        myTreeDataNodes.put(theName, theNode);
    }

    public void setTreeDataNodes(TreeMap<String, TreeDataNode> theMap) {
        myTreeDataNodes = theMap;
    }

    public void startFrame() {
        getFrame();
        if (myFrame != null) {
            Point frameLoc = GUIProps.main.getComponentProp(locationKey(), GUIProps.main.getScreenRelative(.05, .05)).getPoint();
            myFrame.setLocation(frameLoc);
            myFrame.startDisplay();
            GDialog.pushDefaultFrame(myFrame);
        }
    }

    public void stopFrame() {
        boolean resetGUI = GUIProps.main.getBoolean(GUIProps.A_RESET_GUI_PROPS);
        GUIProps.main.setProperty(GUIProps.A_RESET_GUI_PROPS, BaseDef.V_FALSE);
        for (String key : myComponents.keySet()) {
            BaseHandler component = myComponents.get(key);
            key = sizeKey(key);
            GUIProps.ScreenRelative size = null;
            if (!resetGUI) {
                size = GUIProps.main.getScreenRelative(component.getContainer().getSize());
            }
            GUIProps.main.setComponentProp(key, size);
        }
        GUIProps.ScreenRelative location = null;
        if (!resetGUI) {
            location = GUIProps.main.getScreenRelative(myFrame.getLocation());
        }
        GUIProps.main.setComponentProp(locationKey(), location);
        myFrame.closeFrame();
        myFrame.dispose();
        GUIProps.main.save();
    }

    public LocalFrame getFrame() {
        if (myFrame == null) {
            buildFrame();
        }
        return myFrame;
    }

    public <T> T getComponent(Class<T> theClass, String theName) {
        T component = null;
        String type = ourClassToName.get(theClass);
        if (type == null) {
            Log.main.println(Log.FAULT, "AppFrame.getComponent(): invalid class: " + theClass);
        } else {
            BaseHandler test = myComponents.get(key(type, theName));
            if (theClass.isInstance(test)) {
                component = theClass.cast(test);
            }
        }
        return component;
    }

    private void buildFrame() {
        myFramePropPrefix = myFrameDef.getAttr("framePropName", myFrameDefName);
        String title = myFrameDef.getAttr(BaseDef.A_TITLE, "Frame Title");
        myFrame = new LocalFrame(title, null, myBaseHandler);
        myFrame.setFramePropName(myFramePropPrefix);
        String menuName = myFrameDef.getAttr(BaseDef.A_MENU_NAME);
        if (menuName != null) {
            myMenuBar = new JMenuBar();
            MenuFactory.main.addMenuItems(myMenuBar, menuName, myBaseHandler, null, myMenuDisableList);
            myFrame.setJMenuBar(myMenuBar);
        }
        String toolbarName = myFrameDef.getAttr(BaseDef.A_TOOLBAR_NAME);
        if (toolbarName != null) {
        }
        for (BaseDef def : myFrameDef.getDefs()) {
            JComponent component = getComponent(def);
            if (component != null) {
                myFrame.getContentPane().add(component);
            }
        }
        myFrame.pack();
        String icon = myFrameDef.getAttr(WidgetDef.A_ICON, null);
        if (icon != null) {
            myFrame.setIconImage(IconFactory.main.getIcon(icon).getImage());
        }
        if (myMenuBar != null) {
            myIMenuBar = new IMenuBar(myBaseHandler, myMenuBar, myITree);
        }
        for (String key : myComponents.keySet()) {
            BaseDef def = myDefinitions.get(key);
            BaseHandler component = myComponents.get(key);
            String type = def.getName();
            if (type.equals(BaseDef.E_TREE)) {
                ITree tree = (ITree) component;
                String dkey = key(BaseDef.E_DESKTOP, def.getAttr(A_DESKTOP, null));
                IDesktop desktop = (IDesktop) myComponents.get(dkey);
                if (desktop != null) {
                    tree.setDesktop(desktop);
                }
                String bkey = key(BaseDef.E_BUTTON_BAR, def.getAttr("buttonBar", null));
                IButtonBar buttonBar = (IButtonBar) myComponents.get(bkey);
                if (buttonBar != null) {
                    tree.setButtonBar(buttonBar);
                }
            }
        }
    }

    private JComponent getComponent(BaseDef theDefinition) {
        JComponent component = null;
        String type = theDefinition.getName();
        if (type.equals(PanelDef.E_PANEL)) {
            component = PanelFactory.getPanel((PanelDef) theDefinition);
        } else if (type.equals(PanelDef.E_SPLIT_PANE)) {
            component = getSplitPane(theDefinition);
        } else if (type.equals(BaseDef.E_DESKTOP)) {
            IDesktop desktop = new IDesktop(myBaseHandler);
            setPreferredSize(desktop, theDefinition);
            component = (JComponent) desktop.getContainer();
        } else if (type.equals(BaseDef.E_MESSAGE_AREA)) {
            IMessageArea messageArea = new IMessageArea(myBaseHandler);
            setPreferredSize(messageArea, theDefinition);
            component = (JComponent) messageArea.getContainer();
            if (myMessageArea == null) {
                myMessageArea = messageArea;
            }
        } else if (type.equals(BaseDef.E_BUTTON_BAR)) {
            IButtonBar buttonBar = new IButtonBar(myBaseHandler);
            setPreferredSize(buttonBar, theDefinition);
            component = (JComponent) buttonBar.getContainer();
            if (myButtonBar == null) {
                myButtonBar = buttonBar;
            }
        } else if (type.equals(BaseDef.E_TREE)) {
            TreeDataNode node = null;
            String name = theDefinition.getAttr(BaseDef.A_NAME, null);
            if (myTreeDataNodes != null) {
                if (name != null) {
                    node = myTreeDataNodes.get(name);
                }
            }
            if (node == null) {
                Log.main.println(Log.FAULT, "No tree data node set for Tree named '" + name + "'");
            } else {
                ITree tree = new ITree(myBaseHandler, node);
                Utils.invoke(node, "setTree", tree);
                setPreferredSize(tree, theDefinition);
                component = (JComponent) tree.getContainer();
                if (myITree == null) {
                    myITree = tree;
                }
            }
        }
        return component;
    }

    private JComponent getSplitPane(BaseDef theDefinition) {
        JSplitPane pane = null;
        ArrayList<BaseDef> defs = theDefinition.getDefs();
        if (defs.size() != 2) {
            Log.main.println(Log.FAULT, PanelDef.E_SPLIT_PANE + " element does not have two " + PanelDef.E_PANEL + " subelments.");
        } else {
            String splitProp = theDefinition.getAttr(WidgetDef.A_SPLIT_PANE_PROP, "");
            int splitAxis = JSplitPane.HORIZONTAL_SPLIT;
            if (theDefinition.getAttr(PanelDef.A_SPLIT, PanelDef.V_SPLIT_HORIZONTAL).equals(PanelDef.V_SPLIT_VERTICAL)) {
                splitAxis = JSplitPane.VERTICAL_SPLIT;
            }
            JComponent panel1 = getComponent(defs.get(0));
            JComponent panel2 = getComponent(defs.get(1));
            pane = new JSplitPane(splitAxis, panel1, panel2);
            pane.setOneTouchExpandable(true);
            pane.setDividerSize(DIVIDER_SIZE);
            double resizeWeight = Utils.parseDouble(theDefinition.getAttr(WidgetDef.A_RESIZE_WEIGHT, "0.5"));
            if (Double.isNaN(resizeWeight)) {
                resizeWeight = 0.5;
            }
            resizeWeight = Math.max(0.0, Math.min(1.0, resizeWeight));
            pane.setResizeWeight(resizeWeight);
            String dividerProp = null;
            double dividerPct = Double.NaN;
            if (!splitProp.equals("")) {
                pane.putClientProperty(WidgetDef.A_SPLIT_PANE_PROP, splitProp);
                dividerProp = GUIProps.main.getComponentProp(WidgetDef.SPLIT_PREFIX + splitProp, (String) null);
                if (dividerProp != null) {
                    dividerPct = Utils.parseDouble(dividerProp);
                }
            }
            if (Double.isNaN(dividerPct)) {
                dividerPct = Utils.parseDouble(theDefinition.getAttr(WidgetDef.A_DIVIDER, "0.5"));
            }
            if (Double.isNaN(dividerPct) || (dividerPct <= 0.0) || (dividerPct >= 1.0)) {
                dividerPct = 0.5;
            }
            if ((splitProp != null) && (dividerProp == null)) {
                GUIProps.main.setComponentProp(WidgetDef.SPLIT_PREFIX + splitProp, String.valueOf(dividerPct));
            }
            Dimension p1size = panel1.getPreferredSize();
            Dimension p2size = panel2.getPreferredSize();
            if ((p1size != null) && (p2size != null)) {
                if (splitAxis == JSplitPane.HORIZONTAL_SPLIT) {
                    int total = p1size.width + p2size.width;
                    p1size.width = (int) (dividerPct * total);
                    p2size.width = (int) ((1.0 - dividerPct) * total);
                } else {
                    int total = p1size.height + p2size.height;
                    p1size.height = (int) (dividerPct * total);
                    p2size.height = (int) ((1.0 - dividerPct) * total);
                }
                panel1.setPreferredSize(p1size);
                panel2.setPreferredSize(p2size);
            }
        }
        return pane;
    }

    /**
     Sets the preferred size of a BaseHandler component and save the
     component in a map of components.

     @param theComponent the base handler component.

     @param theDefinition the definition of the component.

     @return the preferred size of the component.
  */
    private Dimension setPreferredSize(BaseHandler theComponent, BaseDef theDefinition) {
        String key = key(theDefinition.getName(), theDefinition.getAttr(BaseDef.A_NAME));
        String base = key;
        int count = 0;
        while (myComponents.containsKey(key)) {
            key = base + String.valueOf(++count);
        }
        myComponents.put(key, theComponent);
        myDefinitions.put(key, theDefinition);
        double relW = 0.2;
        double relH = 0.2;
        String relSize = theDefinition.getAttr("relativeSize", null);
        if (relSize != null) {
            String[] relSizes = StringUtils.parseString(relSize, ' ');
            if (relSizes.length == 2) {
                double w = Utils.parseDouble(relSizes[0]);
                if (!Double.isNaN(w) && (w > 0.01) && (w < 0.99)) {
                    relW = w;
                }
                w = Utils.parseDouble(relSizes[1]);
                if (!Double.isNaN(w) && (w > 0.01) && (w < 0.99)) {
                    relH = w;
                }
            }
        }
        Dimension size = GUIProps.main.getComponentProp(sizeKey(key), GUIProps.main.getScreenRelative(relW, relH)).getSize();
        theComponent.setPreferredSize(size);
        return size;
    }

    private String key(String theBase, String theName) {
        return (theName == null) ? theBase : (theBase + "_" + theName);
    }

    private String locationKey() {
        return myFramePropPrefix + "_loc";
    }

    private String sizeKey(String theKey) {
        return myFramePropPrefix + "_" + theKey + "_size";
    }
}
