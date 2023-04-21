package org.formaria.aria.helper;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.lang.reflect.Constructor;
import java.util.Enumeration;
import java.util.Hashtable;
import org.formaria.debug.DebugLogger;
import org.formaria.aria.ILayoutHelper;
import org.formaria.aria.WidgetAdapter;
import org.formaria.aria.PageSupport;
import org.formaria.aria.build.BuildProperties;

/**
 * A helper class for working with layout managers. This class provides mappings
 * between the names of layout styles and constraints and the corresponding
 * Java constants.
 * <p>Copyright (c) Formaria Ltd., 2008</p>
 * <p>License: see license.txt</p>
 * $Revision: 2.21 $
 */
public class LayoutHelper implements ILayoutHelper {

    public static final int LEFT = 0;

    public static final int RIGHT = 1;

    public static final int TOP = 2;

    public static final int BOTTOM = 3;

    /**
   * Sets a LayoutManager for the panel
   * @param cont the container whose layout manager is being set or null to set the parent panel's layout manager
   * @param type the layout manager as defined in the LayoutHelper class
   */
    public Object addLayout(Object cont, int type) {
        try {
            LayoutManager lm = null;
            switch(type) {
                case PageSupport.BORDER_LAYOUT:
                    lm = new BorderLayout();
                    break;
                case PageSupport.FLOW_LAYOUT:
                    lm = new FlowLayout();
                    break;
                case PageSupport.CARD_LAYOUT:
                    lm = new CardLayout();
                    break;
                case PageSupport.GRID_LAYOUT:
                    lm = new GridLayout();
                    break;
                case PageSupport.GRIDBAG_LAYOUT:
                    lm = new GridBagLayout();
                    break;
                case PageSupport.BOX_LAYOUT:
                    {
                        Object[] args = { cont, "0" };
                        lm = (LayoutManager) ReflectionHelper.constructViaReflection("javax.swing.BoxLayout", args);
                    }
                    break;
                case PageSupport.SPRING_LAYOUT:
                    lm = (LayoutManager) Class.forName("javax.swing.SpringLayout").newInstance();
                    break;
                case PageSupport.SCALE_LAYOUT:
                    lm = (LayoutManager) Class.forName("org.formaria.swing.layout.ScaleLayout").newInstance();
                    break;
                case PageSupport.GUIDE_LAYOUT:
                    lm = (LayoutManager) Class.forName("org.formaria.swing.layout.GuideLayoutEx").newInstance();
                    break;
                case PageSupport.LAYER_LAYOUT:
                    lm = (LayoutManager) Class.forName("org.formaria.swing.layout.LayerLayout").newInstance();
                    break;
                case PageSupport.COLUMN_LAYOUT:
                    lm = (LayoutManager) Class.forName("org.formaria.swing.layout.ColumnLayout").newInstance();
                    break;
                case PageSupport.NULL_LAYOUT:
                default:
                    break;
            }
            WidgetAdapter.getInstance().setLayout(cont, lm);
            return lm;
        } catch (Exception e) {
            return null;
        }
    }

    /**
   * Sets a LayoutManager for the panel
   * @param cont the container whose layout manager is being set or null to set the parent panel's layout manager
   * @param type the layout manager as defined in the LayoutHelper class
   */
    public Object addLayout(Object cont, int type, Hashtable attribs) {
        LayoutManager lm = null;
        if (type != PageSupport.BOX_LAYOUT) lm = (LayoutManager) addLayout(cont, type);
        if (attribs != null) {
            switch(type) {
                case PageSupport.BORDER_LAYOUT:
                    ((BorderLayout) lm).setHgap(getIntAttrib(attribs, "hgap"));
                    ((BorderLayout) lm).setVgap(getIntAttrib(attribs, "vgap"));
                    break;
                case PageSupport.FLOW_LAYOUT:
                    ((FlowLayout) lm).setHgap(getIntAttrib(attribs, "hgap"));
                    ((FlowLayout) lm).setVgap(getIntAttrib(attribs, "vgap"));
                    String alignment = (String) attribs.get("alignment");
                    if (alignment != null) ((FlowLayout) lm).setAlignment(getAlignment(alignment));
                    break;
                case PageSupport.CARD_LAYOUT:
                    ((CardLayout) lm).setHgap(getIntAttrib(attribs, "hgap"));
                    ((CardLayout) lm).setVgap(getIntAttrib(attribs, "vgap"));
                    break;
                case PageSupport.GRID_LAYOUT:
                    ((GridLayout) lm).setHgap(getIntAttrib(attribs, "hgap"));
                    ((GridLayout) lm).setVgap(getIntAttrib(attribs, "vgap"));
                    int rows = getIntAttrib(attribs, "rows");
                    int cols = getIntAttrib(attribs, "cols");
                    if ((rows > 0) || (cols > 0)) {
                        if (rows == 0) {
                            ((GridLayout) lm).setColumns(cols);
                            ((GridLayout) lm).setRows(rows);
                        } else {
                            ((GridLayout) lm).setRows(rows);
                            ((GridLayout) lm).setColumns(cols);
                        }
                    }
                    break;
                case PageSupport.BOX_LAYOUT:
                    {
                        String layoutStyle = (String) attribs.get("layoutStyle");
                        Object[] args = new Object[2];
                        args[0] = cont;
                        args[1] = getBoxAlignment(layoutStyle);
                        try {
                            Class[] clazzes = { Container.class, int.class };
                            Constructor ctor = Class.forName("javax.swing.BoxLayout").getConstructor(clazzes);
                            lm = (LayoutManager) ctor.newInstance(args);
                            WidgetAdapter.getInstance().setLayout(cont, lm);
                        } catch (Exception e) {
                            if (BuildProperties.DEBUG) DebugLogger.logError("LAYOUT", "Failed to create BoxLayout");
                        }
                    }
                    break;
                case PageSupport.SPRING_LAYOUT:
                case PageSupport.SCALE_LAYOUT:
                case PageSupport.GUIDE_LAYOUT:
                case PageSupport.GRIDBAG_LAYOUT:
                case PageSupport.LAYER_LAYOUT:
                case PageSupport.COLUMN_LAYOUT:
                    {
                        ReflectionHelper.setErrorMode(ReflectionHelper.LOG_ERROR_MESSAGE);
                        Enumeration enumeration = attribs.keys();
                        while (enumeration.hasMoreElements()) {
                            String key = (String) enumeration.nextElement();
                            setAttrib(cont, lm, key, attribs.get(key));
                        }
                        ReflectionHelper.setErrorMode(ReflectionHelper.PRINT_STACK_TRACE);
                    }
                    break;
                default:
                case PageSupport.NULL_LAYOUT:
                    break;
            }
        }
        return lm;
    }

    /**
   * Gets a constraint object corresponding to a constraint name
   * @param name the constraint name
   * @return the constraint object
   */
    public int getAlignment(String name) {
        int alignment = FlowLayout.CENTER;
        name = name.toUpperCase();
        if (name == null) return alignment; else if (name.compareTo("LEADING") == 0) return 3; else if (name.compareTo("CENTER") == 0) return FlowLayout.CENTER; else if (name.compareTo("LEFT") == 0) return FlowLayout.LEFT; else if (name.compareTo("RIGHT") == 0) return FlowLayout.RIGHT; else if (name.compareTo("TRAILING") == 0) return 4; else {
            try {
                alignment = Integer.parseInt(name);
                alignment = Math.max(FlowLayout.LEFT, Math.min(alignment, 4));
            } catch (Exception e) {
            }
        }
        return alignment;
    }

    /**
   * Get the layout type enumerated in Page
   * @param ls the layout style
   * @return the type id
   */
    public int getLayoutType(String ls) {
        ls = ls.toUpperCase();
        if ((ls == null) || (ls.length() == 0) || ls.equals("NULL")) return PageSupport.NULL_LAYOUT; else if (ls.equals("FLOW")) return PageSupport.FLOW_LAYOUT; else if (ls.equals("BORDER")) return PageSupport.BORDER_LAYOUT; else if (ls.equals("GRID")) return PageSupport.GRID_LAYOUT; else if (ls.equals("GRIDBAG")) return PageSupport.GRIDBAG_LAYOUT; else if (ls.equals("CARD")) return PageSupport.CARD_LAYOUT; else if (ls.equals("BOX")) return PageSupport.BOX_LAYOUT; else if (ls.equals("GUIDE")) return PageSupport.GUIDE_LAYOUT; else if (ls.equals("SCALE")) return PageSupport.SCALE_LAYOUT; else if (ls.equals("SPRING")) return PageSupport.SPRING_LAYOUT; else if (ls.equals("LAYER")) return PageSupport.LAYER_LAYOUT; else if (ls.equals("COLUMN")) return PageSupport.COLUMN_LAYOUT;
        return PageSupport.NULL_LAYOUT;
    }

    /**
   * Get the layout type
   * @param layout the layout manager instance
   * @return the type name e.g. FLOW or BORDER
   */
    public String getLayoutClass(Object layout) {
        if (layout != null) {
            String className = layout.getClass().getName();
            if (layout instanceof FlowLayout) return "Flow"; else if (layout instanceof BorderLayout) return "Border"; else if (layout instanceof GridLayout) return "Grid"; else if (layout instanceof GridBagLayout) return "GridBag"; else if (layout instanceof CardLayout) return "Card"; else if (layout instanceof GridLayout) return "Box"; else if (className.indexOf("BoxLayout") > 0) return "Box"; else if (className.indexOf("GuideLayout") > 0) return "Guide"; else if (className.indexOf("ScaleLayout") > 0) return "Scale"; else if (className.indexOf("LayerLayout") > 0) return "Layer"; else if (className.indexOf("SpringLayout") > 0) return "Spring";
        }
        return null;
    }

    /**
   * Does the layout manager use constraints for its children?
   * @param layout the layout manager instance
   * @return true if the layout uses constraints
   */
    public boolean getUsesConstraints(Object layout) {
        if (layout != null) {
            String className = layout.getClass().getName();
            if (layout instanceof FlowLayout) return false; else if (layout instanceof BorderLayout) return true; else if (layout instanceof GridLayout) return true; else if (layout instanceof GridBagLayout) return true; else if (layout instanceof CardLayout) return true; else if (layout instanceof GridLayout) return false; else if (className.indexOf("BoxLayout") > 0) return false; else if (className.indexOf("SpringLayout") > 0) return true; else if (className.indexOf("ScaleLayout") > 0) return false; else if (className.indexOf("LayerLayout") > 0) return false; else if (className.indexOf("GuideLayout") > 0) return true;
        }
        return false;
    }

    /**
   * Does this layout manager used the absoulte dimensions?
   * @param layout the layout manager instance
   * @return true if the X,Y,W,H dimensions are used
   */
    public boolean getUsesDimensions(Object layout) {
        if (layout != null) {
            String className = layout.getClass().getName();
            if (className.indexOf("SpringLayout") > 0) return true; else if (className.indexOf("GuideLayout") > 0) return true; else return false;
        }
        return true;
    }

    /**
   * Gets a constraint object corresponding to a constraint name
   * @param name the quoted constraint name e.g. "WEST"
   * <ul>
   * <li>WEST=BorderLayout.WEST</li>
   * <li>EAST=BorderLayout.EAST</li>
   * <li>NORTH=BorderLayout.NORTH</li>
   * <li>SOUTH=BorderLayout.SOUTH</li>
   * <li>CENTER=BorderLayout.CENTER</li>
   * <li>AFTER_LAST_LINE=BorderLayout.AFTER_LAST_LINE</li>
   * <li>AFTER_LINE_ENDS=BorderLayout.AFTER_LINE_ENDS</li>
   * <li>BEFORE_FIRST_LINE=BorderLayout.BEFORE_FIRST_LINE</li>
   * <li>BEFORE_LINE_BEGINS=BorderLayout.BEFORE_LINE_BEGINS</li>
   * <li>GridBagConstraints=14 GridBagConstraint parameters separated by commas
   *   <OL>
   *     <li> gridx - The initial gridx value</li>
   *     <li> gridy - The initial gridy value.</li>
   *     <li> gridwidth - The initial gridwidth value.</li>
   *     <li> gridheight - The initial gridheight value.</li>
   *     <li> weightx - The initial weightx value.</li>
   *     <li> weighty - The initial weighty value.</li>
   *     <li> anchorstr - The anchor string value (EAST|WEST|NORTH|SOUTH|NORTHEAST|NORTHWEST|SOUTHEAST|SOUTHWEST)</li>
   *     <li> fillstr - The fill string value (HORIZONTAL|VERTICAL)</li>
   *     <li> insets top - The initial inset</li>
   *     <li> insets left - The initial inset</li>
   *     <li> insets right - The initial inset</li>
   *     <li> insets bottom - The initial inset</li>
   *     <li> ipadx - The initial ipadx value</li>
   *     <li> ipady - The initial ipady value</li>
   *   </OL>
   * </li>
   * </ul>
   * @return the constraint object
   */
    public Object getConstraint(String name) {
        if (name == null) return null;
        String uname = name.toUpperCase();
        if (uname.equals("WEST")) return BorderLayout.WEST; else if (uname.equals("EAST")) return BorderLayout.EAST; else if (uname.equals("NORTH")) return BorderLayout.NORTH; else if (uname.equals("SOUTH")) return BorderLayout.SOUTH; else if (uname.equals("CENTER")) return BorderLayout.CENTER; else if (uname.equals("AFTER_LAST_LINE")) return "Last"; else if (uname.equals("AFTER_LINE_ENDS")) return "After"; else if (uname.equals("BEFORE_FIRST_LINE")) return "First"; else if (uname.equals("BEFORE_LINE_BEGINS")) return "Before"; else {
            String[] params = uname.split(",");
            if (params.length == 14) {
                String anchorstr = params[6].toUpperCase();
                String fillstr = params[7].toUpperCase();
                int anchor = GridBagConstraints.CENTER;
                int fill = GridBagConstraints.NONE;
                if (anchorstr.equals("EAST")) anchor = GridBagConstraints.EAST; else if (anchorstr.equals("WEST")) anchor = GridBagConstraints.WEST; else if (anchorstr.equals("NORTH")) anchor = GridBagConstraints.NORTH; else if (anchorstr.equals("SOUTH")) anchor = GridBagConstraints.SOUTH; else if (anchorstr.equals("NORTHEAST")) anchor = GridBagConstraints.NORTHEAST; else if (anchorstr.equals("NORTHWEST")) anchor = GridBagConstraints.NORTHWEST; else if (anchorstr.equals("SOUTHEAST")) anchor = GridBagConstraints.SOUTHEAST; else if (anchorstr.equals("SOUTHWEST")) anchor = GridBagConstraints.SOUTHWEST;
                if (fillstr.equals("HORIZONTAL")) fill = GridBagConstraints.HORIZONTAL; else if (fillstr.equals("VERTICAL")) fill = GridBagConstraints.VERTICAL;
                return new GridBagConstraints(new Integer(params[0]).intValue(), new Integer(params[1]).intValue(), new Integer(params[2]).intValue(), new Integer(params[3]).intValue(), new Double(params[4]).doubleValue(), new Double(params[5]).doubleValue(), anchor, fill, new Insets(new Integer(params[8]).intValue(), new Integer(params[9]).intValue(), new Integer(params[10]).intValue(), new Integer(params[11]).intValue()), new Integer(params[12]).intValue(), new Integer(params[13]).intValue());
            }
        }
        return name;
    }

    /**
   * Get an attribute as an int value
   * @param attribs the attribs hashtable
   * @param attrib the attribute key
   * @return the integer value
   */
    protected int getIntAttrib(Hashtable attribs, String attrib) {
        try {
            Object value = attribs.get(attrib);
            if (value != null) return new Integer((String) value).intValue();
        } catch (NumberFormatException ex) {
        }
        return 0;
    }

    /**
   * Set the attributes for a layout manager
   * @param cont the container
   * @param lm the layout manager
   * @param attrib the attribute name
   * @param value the value of the attribute
   */
    public void setAttrib(Object cont, Object lm, String attrib, Object value) {
        String methodName = attrib.substring(0, 1).toUpperCase() + attrib.substring(1);
        ReflectionHelper.setViaReflection(methodName, lm, value, value.getClass());
    }

    /**
   * Convert an attribute value to the equivalent BoxLayout constant, 
   * defaults to x-axis if not found.
   * @param value the attribute value specifying the alignment
   * @return the equivalent BoxLayout integer constant
   */
    protected Integer getBoxAlignment(String value) {
        if (value != null) {
            String uvalue = value.toUpperCase();
            if (uvalue.equals("X") || uvalue.equals("0")) return new Integer(0); else if (uvalue.equals("Y") || uvalue.equals("1")) return new Integer(1); else if (uvalue.equals("LINE")) return new Integer(2); else if (uvalue.equals("PAGE")) return new Integer(3);
        }
        return new Integer(0);
    }
}
