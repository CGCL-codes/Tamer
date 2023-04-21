package net.claribole.zgrviewer.dot;

/**
 * Specifies a node or edge style. It can be one of the predefined style or
 * a new one, which can have attributes
 * @author Eric Mounhem
 */
public class Style {

    /**
     * Save (maybe parameterized) styles that are not predefined
     * @author Eric Mounhem
     */
    class CustomStyle {

        /**
         * Style's name
         */
        String name;

        /**
         * Style's arguments
         */
        Object[] arguments = null;

        /**
         * @see java.lang.Object#toString()
         */
        public String toString() {
            String s = this.name;
            if (this.arguments != null) {
                s += "(";
                for (int i = 0; i < this.arguments.length; i++) {
                    if (i > 0) s += ",";
                    s += this.arguments[i];
                }
                s += ")";
            }
            return s;
        }
    }

    /**
     * Styles not predefined
     */
    CustomStyle[] otherStyles = null;

    /**
     * Dashed style modifier
     */
    static final int DASHED = 0;

    /**
     * Dotted style modifier
     */
    static final int DOTTED = 1;

    /**
     * Solid style modifier
     */
    static final int SOLID = 2;

    /**
     * Invisible style modifier
     */
    static final int INVIS = 3;

    /**
     * Bold style modifier
     */
    static final int BOLD = 4;

    /**
     * Filled style modifier
     */
    static final int FILLED = 5;

    /**
     * Diagonals style modifier
     */
    static final int DIAGONALS = 6;

    /**
     * Rounded style modifier
     */
    static final int ROUNDED = 7;

    /**
     * Values of node styles
     */
    private boolean[] styles = { false, false, false, false, false, false, false, false };

    /**
     * Name of style attributes
     */
    static final String[] styleAttributeName = { "dashed", "dotted", "solid", "invis", "bold", "filled", "bold", "filled", "diagonals", "rounded" };

    /**
     * Set the style of a node
     * @param style one of the following:
     * <ul>
     * <li>DASHED</li>
     * <li>DOTTED</li>
     * <li>SOLID</li>
     * <li>INVIS</li>
     * <li>BOLD</li>
     * <li>FILLED</li>
     * <li>DIAGONALS</li>
     * <li>ROUNDED</li>
     * </ul>
     * @param value value you want to apply to a style
     */
    public void setStyle(int style, boolean value) {
        this.styles[style] = value;
    }

    /**
     * Get the style of a node
     * @param s One of the following style:
     * <ul>
     * <li>DASHED</li>
     * <li>DOTTED</li>
     * <li>SOLID</li>
     * <li>INVIS</li>
     * <li>BOLD</li>
     * <li>FILLED</li>
     * <li>DIAGONALS</li>
     * <li>ROUNDED</li>
     * </ul>
     * @return State of a style (activated or not)
     */
    public boolean getStyle(int s) {
        return this.styles[s];
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String s = "";
        for (int i = 0; i < this.styles.length; i++) if (this.styles[i]) {
            if (s.length() > 0) s += ",";
            s += styleAttributeName[i];
        }
        if (this.otherStyles != null) for (int i = 0; i < this.otherStyles.length; i++) {
            if (i > 0) s += ",";
            s += this.otherStyles[i];
        }
        if (s.equals("")) return "";
        return "\"" + s + "\"";
    }
}
