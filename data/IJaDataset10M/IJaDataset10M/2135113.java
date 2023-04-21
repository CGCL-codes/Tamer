package freemind.modes;

import java.awt.Color;
import java.awt.Font;
import java.util.*;
import java.io.*;
import freemind.main.XMLElement;
import freemind.main.Tools;
import freemind.modes.MindIcon;

/**
 * This class represents a StylePattern than can be applied
 * to a node or a whole branch. The properties of the
 * nodes are replaced with the properties saved in the
 * pattern. If a property "text" is given, this pattern
 * is automatically applied to all nodes that contain the
 * String saved in "text".
 */
public class StylePattern {

    private String name;

    private boolean recursive;

    private String text;

    private boolean folded;

    private boolean appliesToNode = false;

    private Color nodeColor;

    private String nodeStyle;

    private Font nodeFont;

    private MindIcon nodeIcon;

    private boolean appliesToEdge = false;

    private Color edgeColor;

    private String edgeStyle;

    private int edgeWidth;

    private boolean appliesToNodeFont = false;

    private boolean appliesToNodeIcon = false;

    /** Inhertitable patterns, fc, 3.12.2003.*/
    private boolean appliesToChildren = false;

    private StylePattern ChildrenStylePattern;

    public StylePattern(XMLElement elm, List justConstructedPatterns) {
        loadPattern(elm, justConstructedPatterns);
    }

    public String toString() {
        return "node: " + nodeColor + ", " + nodeStyle + ", " + nodeFont + ", " + nodeIcon + ", " + "\nedge: " + edgeColor + ", " + edgeStyle + ", " + edgeWidth;
    }

    public boolean getAppliesToEdge() {
        return appliesToEdge;
    }

    public boolean getAppliesToNode() {
        return appliesToNode;
    }

    public boolean getAppliesToNodeFont() {
        return appliesToNodeFont;
    }

    public boolean getAppliesToNodeIcon() {
        return appliesToNodeIcon;
    }

    public boolean getAppliesToChildren() {
        return appliesToChildren;
    }

    /**
       * Get the value of name.
       * @return Value of name.
       */
    public String getName() {
        return name;
    }

    /**
       * Set the value of name.
       * @param v  Value to assign to name.
       */
    public void setName(String v) {
        this.name = v;
    }

    /**
     * Determine if the properies of this pattern, of course
     * except the "text" attribute, apply to all the child nodes
     * of this node.
     * @return Value of recursive.
     */
    public boolean getRecursive() {
        return recursive;
    }

    /**
       * Set the value of recursive.
       * @param v  Value to assign to recursive.
       */
    public void setRecursive(boolean v) {
        this.recursive = v;
    }

    /**
       * Get the value of folded.
       * @return Value of folded.
       */
    public boolean getFolded() {
        return folded;
    }

    /**
       * Set the value of folded.
       * @param v  Value to assign to folded.
       */
    public void setFolded(boolean v) {
        this.folded = v;
    }

    /**
       * Get the value of text.
       * @return Value of text.
       */
    public String getText() {
        return text;
    }

    /**
       * Set the value of text.
       * @param v  Value to assign to text.
       */
    public void setText(String v) {
        this.text = v;
    }

    /**
       * Get the value of nodeColor.
       * @return Value of nodeColor.
       */
    public Color getNodeColor() {
        return nodeColor;
    }

    /**
       * Set the value of nodeColor.
       * @param v  Value to assign to nodeColor.
       */
    public void setNodeColor(Color v) {
        this.nodeColor = v;
    }

    /**
       * Get the value of nodeStyle.
       * @return Value of nodeStyle.
       */
    public String getNodeStyle() {
        return nodeStyle;
    }

    /**
       * Set the value of nodeStyle.
       * @param v  Value to assign to nodeStyle.
       */
    public void setNodeStyle(String nodeStyle) {
        this.nodeStyle = nodeStyle;
    }

    /**
       * Get the value of font.
       * @return Value of font.
       */
    public Font getNodeFont() {
        return nodeFont;
    }

    /**
       * Set the value of font.
       * @param v  Value to assign to font.
       */
    public void setNodeFont(Font nodeFont) {
        this.nodeFont = nodeFont;
    }

    /**
       * Get the value of icon.
       * @return Value of icon.
       */
    public MindIcon getNodeIcon() {
        return nodeIcon;
    }

    /**
       * Set the value of icon.
       * @param v  Value to assign to icon.
       */
    public void setNodeIcon(MindIcon nodeIcon) {
        this.nodeIcon = nodeIcon;
    }

    /**
       * Get the value of edgeColor.
       * @return Value of edgeColor.
       */
    public Color getEdgeColor() {
        return edgeColor;
    }

    /**
       * Set the value of edgeColor.
       * @param v  Value to assign to edgeColor.
       */
    public void setEdgeColor(Color edgeColor) {
        this.edgeColor = edgeColor;
    }

    /**
      * Get the value of edgeStyle.
      * @return Value of edgeStyle.
      */
    public String getEdgeStyle() {
        return edgeStyle;
    }

    /**
     * Set the value of edgeStyle.
       * @param v  Value to assign to edgeStyle.
       */
    public void setEdgeStyle(String edgeStyle) {
        this.edgeStyle = edgeStyle;
    }

    /**
       * Get the value of edgeWidth.
       * @return Value of edgeWidth.
       */
    public int getEdgeWidth() {
        return edgeWidth;
    }

    /**
       * Set the value of edgeWidth.
       * @param v  Value to assign to edgeWidth.
       */
    public void setEdgeWidth(int edgeWidth) {
        this.edgeWidth = edgeWidth;
    }

    /**
       * Get the value of ChildrenStylePattern.
       * @return Value of ChildrenStylePattern.
       */
    public StylePattern getChildrenStylePattern() {
        return ChildrenStylePattern;
    }

    /**
       * Set the value of ChildrenStylePattern.
       * @param v  Value to assign to ChildrenStylePattern.
       */
    public void setChildrenStylePattern(StylePattern ChildrenStylePattern) {
        this.ChildrenStylePattern = ChildrenStylePattern;
    }

    public static List loadPatterns(File file) throws Exception {
        return loadPatterns(new BufferedReader(new FileReader(file)));
    }

    public static List loadPatterns(Reader reader) throws Exception {
        List list = new LinkedList();
        XMLElement parser = new XMLElement();
        parser.parseFromReader(reader);
        for (Enumeration e = parser.enumerateChildren(); e.hasMoreElements(); ) {
            list.add(new StylePattern((XMLElement) e.nextElement(), list));
        }
        return list;
    }

    protected void loadPattern(XMLElement pattern, List justConstructedPatterns) {
        if (pattern.getStringAttribute("name") != null) {
            setName(pattern.getStringAttribute("name"));
        }
        if (Tools.safeEquals(pattern.getStringAttribute("recursive"), "true")) {
            setRecursive(true);
        }
        for (Iterator i = pattern.getChildren().iterator(); i.hasNext(); ) {
            XMLElement child = (XMLElement) i.next();
            if (child.getName().equals("node")) {
                appliesToNode = true;
                if (child.getStringAttribute("color") != null && child.getStringAttribute("color").length() == 7) {
                    setNodeColor(Tools.xmlToColor(child.getStringAttribute("color")));
                }
                if (child.getStringAttribute("style") != null) {
                    setNodeStyle(child.getStringAttribute("style"));
                }
                if (child.getStringAttribute("icon") != null) {
                    appliesToNodeIcon = true;
                    if (child.getStringAttribute("icon").equals("none")) {
                        appliesToNodeIcon = true;
                        setNodeIcon(child.getStringAttribute("icon").equals("none") ? null : new MindIcon(child.getStringAttribute("icon")));
                    }
                }
                setText(child.getStringAttribute("text"));
                for (Iterator j = child.getChildren().iterator(); j.hasNext(); ) {
                    XMLElement nodeChild = (XMLElement) j.next();
                    if (nodeChild.getName().equals("font")) {
                        appliesToNodeFont = true;
                        String name = nodeChild.getStringAttribute("name");
                        int style = 0;
                        int size = 0;
                        if (Tools.safeEquals(nodeChild.getStringAttribute("bold"), "true")) {
                            style += Font.BOLD;
                        }
                        if (Tools.safeEquals(nodeChild.getStringAttribute("italic"), "true")) {
                            style += Font.ITALIC;
                        }
                        if (nodeChild.getStringAttribute("size") != null) {
                            size = Integer.parseInt(nodeChild.getStringAttribute("size"));
                        }
                        setNodeFont(new Font(name, style, size));
                        if (size == 0) {
                            setNodeFont(null);
                        }
                    }
                }
            }
            if (child.getName().equals("edge")) {
                appliesToEdge = true;
                if (child.getStringAttribute("style") != null) {
                    setEdgeStyle(child.getStringAttribute("style"));
                }
                if (child.getStringAttribute("color") != null) {
                    setEdgeColor(Tools.xmlToColor(child.getStringAttribute("color")));
                }
                if (child.getStringAttribute("width") != null) {
                    if (child.getStringAttribute("width").equals("thin")) {
                        setEdgeWidth(freemind.modes.EdgeAdapter.WIDTH_THIN);
                    } else {
                        setEdgeWidth(Integer.parseInt(child.getStringAttribute("width")));
                    }
                }
            }
            if (child.getName().equals("child")) {
                appliesToChildren = true;
                if (child.getStringAttribute("pattern") != null) {
                    String searchName = child.getStringAttribute("pattern");
                    boolean anythingFound = false;
                    for (ListIterator e = justConstructedPatterns.listIterator(); e.hasNext(); ) {
                        StylePattern patternFound = (StylePattern) e.next();
                        if (patternFound.getName().equals(searchName)) {
                            setChildrenStylePattern(patternFound);
                            anythingFound = true;
                            break;
                        }
                    }
                    if (getName().equals(searchName)) {
                        setChildrenStylePattern(this);
                        anythingFound = true;
                    }
                    if (anythingFound == false) System.err.println("Cannot find the children " + searchName + " to the pattern " + getName());
                }
            }
        }
    }
}
