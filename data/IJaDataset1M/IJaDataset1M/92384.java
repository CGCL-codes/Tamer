package freemind.modes;

import java.awt.Color;
import java.awt.Font;
import java.util.List;
import java.util.LinkedList;
import java.util.Enumeration;
import java.util.Vector;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import freemind.main.XMLElement;
import freemind.main.Tools;

/**
 * This class represents a Pattern than can be applied
 * to a node or a whole branch. The properties of the
 * nodes are replaced with the properties saved in the
 * pattern. If a property "text" is given, this pattern
 * is automatically applied to all nodes that contain the
 * String saved in "text".
 */
public class Pattern {

    private String name;

    private boolean recursive;

    private String text;

    private Color nodeColor;

    private String nodeStyle;

    private Font nodeFont;

    private boolean folded;

    private Color edgeColor;

    private String edgeStyle;

    private int edgeWidth;

    public Pattern() {
    }

    public Pattern(String name) {
        setName(name);
    }

    public Pattern(File file) {
        loadPatterns(file);
    }

    public Pattern(XMLElement elm) {
        loadPattern(elm);
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
    public void setNodeStyle(String v) {
        this.nodeStyle = v;
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
    public void setNodeFont(Font v) {
        this.nodeFont = v;
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
    public void setEdgeColor(Color v) {
        this.edgeColor = v;
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
    public void setEdgeStyle(String v) {
        this.edgeStyle = v;
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
    public void setEdgeWidth(int v) {
        this.edgeWidth = v;
    }

    public static List loadPatterns(File file) {
        List list = new LinkedList();
        XMLElement parser = new XMLElement();
        try {
            parser.parseFromReader(new InputStreamReader(new FileInputStream(file)));
        } catch (Exception ex) {
            System.err.println("Error while parsing file.");
            return null;
        }
        for (Enumeration e = parser.enumerateChildren(); e.hasMoreElements(); ) {
            list.add(new Pattern((XMLElement) e.nextElement()));
        }
        return list;
    }

    public void loadPattern(XMLElement pattern) {
        if (pattern.getProperty("name") != null) {
            setName(pattern.getProperty("name"));
        }
        if (pattern.getProperty("recursive") != null && pattern.getProperty("recursive").equals("true")) {
            setRecursive(true);
        }
        Vector patternsChildren = pattern.getChildren();
        for (int i = 0; i < patternsChildren.size(); i++) {
            if (((XMLElement) patternsChildren.elementAt(i)).getTagName().equals("node")) {
                XMLElement node = (XMLElement) patternsChildren.elementAt(i);
                if (node.getProperty("folded") != null && node.getProperty("folded").equals("true")) {
                    setFolded(true);
                }
                if (node.getProperty("folded") != null && node.getProperty("folded").equals("true")) {
                    setFolded(true);
                }
                if (node.getProperty("color") != null && node.getProperty("color").length() == 7) {
                    setNodeColor(Tools.xmlToColor(node.getProperty("color")));
                }
                if (node.getProperty("style") != null) {
                    setNodeStyle(node.getProperty("style"));
                }
                Vector nodesChildren = node.getChildren();
                for (int j = 0; j < nodesChildren.size(); j++) {
                    if (((XMLElement) nodesChildren.elementAt(j)).getTagName().equals("nodeFont")) {
                        XMLElement font = ((XMLElement) nodesChildren.elementAt(j));
                        String name = font.getProperty("name");
                        int style = 0;
                        int size = 0;
                        if (!Tools.isValidFont(name)) {
                            name = "Sans Serif";
                        }
                        if (font.getProperty("bold") != null && font.getProperty("bold").equals("true")) style += Font.BOLD;
                        ;
                        if (font.getProperty("italic") != null && font.getProperty("italic").equals("true")) style += Font.ITALIC;
                        if (font.getProperty("size") != null) {
                            size = Integer.parseInt(font.getProperty("size"));
                        }
                        setNodeFont(new Font(name, style, size));
                    }
                }
                if (((XMLElement) patternsChildren.elementAt(i)).getTagName().equals("edge")) {
                    XMLElement edge = (XMLElement) patternsChildren.elementAt(i);
                    if (edge.getProperty("style") != null) {
                        setEdgeStyle(edge.getProperty("style"));
                    }
                    if (edge.getProperty("color") != null) {
                        setEdgeColor(Tools.xmlToColor(edge.getProperty("color")));
                    }
                    if (edge.getProperty("width") != null) {
                        if (edge.getProperty("width").equals("thin")) setEdgeWidth(freemind.modes.EdgeAdapter.WIDTH_THIN); else setEdgeWidth(Integer.parseInt(edge.getProperty("width")));
                    }
                }
            }
        }
    }
}
