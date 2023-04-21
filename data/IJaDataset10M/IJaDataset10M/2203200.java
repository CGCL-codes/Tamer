package org.xmlcml.cml.tools;

import nu.xom.Element;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xmlcml.euclid.EC;

/**
 * properties of a CML object. currently motivated by graphics but could be
 * extended
 * 
 * @author pm286
 * 
 */
public class AbstractDisplay {

    private static final Logger LOG = Logger.getLogger(AbstractDisplay.class);

    static {
        LOG.setLevel(Level.INFO);
    }

    protected static final String FONT_STYLE_NORMAL = "normal";

    protected static final String FONT_STYLE_ITALIC = "italic";

    protected static final String FONT_WEIGHT_NORMAL = "normal";

    protected static final String FONT_WEIGHT_BOLD = "bold";

    protected static final String FONT_SANS_SERIF = "helvetica";

    protected static final String FONT_SERIF = "timesRoman";

    protected static final String FONT_MONOSPACE = "monospace";

    static final AbstractDisplay DEFAULT = new AbstractDisplay();

    static {
        DEFAULT.setDefaults();
    }

    ;

    protected String color;

    protected String fill = "red";

    protected double fontSize = 15;

    protected String fontStyle;

    protected String fontWeight;

    protected String fontFamily = "helvetica";

    protected double opacity;

    protected boolean showChildLabels;

    protected String stroke = "blue";

    protected String backgroundColor;

    protected Element userElement;

    /**
	 * do not use.
	 */
    public AbstractDisplay() {
        init();
    }

    protected void init() {
        setDefaults();
    }

    protected void setDefaults() {
        LOG.debug("SET DEFAULTS");
        color = "black";
        fill = color;
        fontFamily = FONT_SANS_SERIF;
        fontSize = 17;
        fontStyle = FONT_STYLE_NORMAL;
        fontWeight = FONT_WEIGHT_NORMAL;
        opacity = Double.NaN;
        showChildLabels = false;
        stroke = color;
    }

    /**
	 * copy constructor.
	 * 
	 * @param a
	 */
    public AbstractDisplay(AbstractDisplay a) {
        this.color = a.color;
        this.fill = a.fill;
        this.opacity = a.opacity;
        this.fontFamily = a.fontFamily;
        this.fontSize = a.fontSize;
        this.fontStyle = a.fontStyle;
        this.fontWeight = a.fontWeight;
        this.showChildLabels = a.showChildLabels;
        this.stroke = a.stroke;
        this.backgroundColor = a.backgroundColor;
    }

    /**
	 * @return the color
	 */
    public String getColor() {
        return color;
    }

    /**
	 * @param color
	 *            the color to set
	 */
    public void setColor(String color) {
        this.color = color;
    }

    /**
	 * @return the fill
	 */
    public String getFill() {
        return fill;
    }

    /**
	 * @param fill
	 *            the fill to set
	 */
    public void setFillColor(String fill) {
        this.fill = fill;
    }

    /**
	 * @return the opacity
	 */
    public double getOpacity() {
        return opacity;
    }

    /**
	 * @param opacity
	 *            the opacity to set
	 */
    public void setOpacity(double opacity) {
        this.opacity = opacity;
    }

    /**
	 * @return the stroke
	 */
    public String getStroke() {
        return stroke;
    }

    /**
	 * @param stroke
	 *            the stroke to set
	 */
    public void setStroke(String stroke) {
        this.stroke = stroke;
    }

    /**
	 * @return the fontFamily
	 */
    public String getFontFamily() {
        return fontFamily;
    }

    /**
	 * @param fontFamily
	 *            the fontFamilyto set
	 */
    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    /**
	 * @return the fontSize
	 */
    public double getFontSize() {
        return fontSize;
    }

    /**
	 * @param fontSize
	 *            the fontSize to set
	 */
    public void setFontSize(double fontSize) {
        this.fontSize = fontSize;
    }

    /**
	 * @return the fontStyle
	 */
    public String getFontStyle() {
        return fontStyle;
    }

    /**
	 * @param fontStyle
	 *            the fontStyle to set
	 */
    public void setFontStyle(String fontStyle) {
        this.fontStyle = fontStyle;
    }

    /**
	 * @return the fontWeight
	 */
    public String getFontWeight() {
        return fontWeight;
    }

    /**
	 * @param fontWeight
	 *            the fontWeight to set
	 */
    public void setFontWeight(String fontWeight) {
        this.fontWeight = fontWeight;
    }

    /**
	 * @param fill
	 *            the fill to set
	 */
    public void setFill(String fill) {
        this.fill = fill;
    }

    public boolean isShowChildLabels() {
        return showChildLabels;
    }

    public void setShowChildLabels(boolean showChildLabels) {
        this.showChildLabels = showChildLabels;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getDebugString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AbstractDisplay:");
        sb.append("  color:           " + color);
        sb.append(EC.S_NEWLINE);
        sb.append("  fill:            " + fill);
        sb.append(EC.S_NEWLINE);
        sb.append("  fontSize:        " + fontSize);
        sb.append(EC.S_NEWLINE);
        sb.append("  fontStyle:       " + fontStyle);
        sb.append(EC.S_NEWLINE);
        sb.append("  fontWeight:      " + fontWeight);
        sb.append(EC.S_NEWLINE);
        sb.append("fontFamily:      " + fontFamily);
        sb.append(EC.S_NEWLINE);
        sb.append("opacity:         " + opacity);
        sb.append(EC.S_NEWLINE);
        sb.append("showChildLabels: " + showChildLabels);
        sb.append(EC.S_NEWLINE);
        sb.append("stroke:          " + stroke);
        sb.append(EC.S_NEWLINE);
        sb.append("backgroundColor: " + backgroundColor);
        sb.append(EC.S_NEWLINE);
        sb.append(EC.S_NEWLINE);
        return sb.toString();
    }

    public Element getUserElement() {
        return userElement;
    }

    public void setUserElement(Element userElement) {
        this.userElement = userElement;
    }
}
