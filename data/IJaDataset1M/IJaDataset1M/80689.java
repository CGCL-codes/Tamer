package net.sf.jsfcomp.chartcreator.component;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * @author Cagatay Civici (latest modification by $Author: rpa_rio $)
 * @version $Revision: 826 $ $Date: 2007-11-10 18:30:15 -0200 (sáb, 10 nov 2007) $
 * 
 * Core component representing a chart 
 */
public class UIChart extends UIComponentBase {

    public static final String COMPONENT_TYPE = "net.sf.jsfcomp.chartcreator.UIChart";

    public static final String COMPONENT_FAMILY = "net.sf.jsfcomp.chartcreator.UIChart";

    public static final String DEFAULT_RENDERER = "net.sf.jsfcomp.chartcreator.ChartRenderer";

    private Object datasource;

    private Integer width;

    private Integer height;

    private Integer alpha;

    private Integer depth;

    private Integer startAngle;

    private String title;

    private String type;

    private String background;

    private String foreground;

    private String xlabel;

    private String ylabel;

    private String orientation;

    private String colors;

    private Boolean is3d;

    private Boolean legend;

    private String legendPosition;

    private Float legendFontSize;

    private Boolean legendBorder;

    private Boolean antialias;

    private Boolean outline;

    private String styleClass;

    private String alt;

    private String imgTitle;

    private String onclick;

    private String ondblclick;

    private String onmousedown;

    private String onmouseup;

    private String onmouseover;

    private String onmousemove;

    private String onmouseout;

    private String onkeypress;

    private String onkeydown;

    private String onkeyup;

    private String output;

    private String usemap;

    private String generateMap;

    private String ongeneratedimagemapclick;

    private Boolean domainGridLines;

    private Boolean rangeGridLines;

    private Float lineStrokeWidth;

    private String dataTooltipFormat;

    private String pieLabelBackground;

    private String pieLabelOutline;

    public UIChart() {
        super();
        setRendererType(DEFAULT_RENDERER);
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public boolean getRendersChildren() {
        return true;
    }

    /**
	 * Alpha attribute for pie charts
	 */
    public int getAlpha() {
        if (alpha != null) return alpha.intValue();
        ValueBinding vb = getValueBinding("alpha");
        Integer v = vb != null ? (Integer) vb.getValue(getFacesContext()) : null;
        return v != null ? v.intValue() : 100;
    }

    public void setAlpha(int alpha) {
        this.alpha = new Integer(alpha);
    }

    /**
	 * Antialias attribute
	 */
    public boolean getAntialias() {
        if (antialias != null) return antialias.booleanValue();
        ValueBinding vb = getValueBinding("antialias");
        Boolean v = vb != null ? (Boolean) vb.getValue(getFacesContext()) : null;
        return v != null ? v.booleanValue() : false;
    }

    public void setAntialias(boolean antialias) {
        this.antialias = Boolean.valueOf(antialias);
    }

    /**
	 * Background attribute
	 */
    public String getBackground() {
        if (background != null) return background;
        ValueBinding vb = getValueBinding("background");
        String v = vb != null ? (String) vb.getValue(getFacesContext()) : null;
        return v != null ? v : "white";
    }

    public void setBackground(String background) {
        this.background = background;
    }

    /**
	 * Foreground attribute
	 */
    public String getForeground() {
        if (foreground != null) return foreground;
        ValueBinding vb = getValueBinding("foreground");
        String v = vb != null ? (String) vb.getValue(getFacesContext()) : null;
        return v != null ? v : "white";
    }

    public void setForeground(String foreground) {
        this.foreground = foreground;
    }

    /**
	 * 3D attribute
	 */
    public boolean getIs3d() {
        if (is3d != null) return is3d.booleanValue();
        ValueBinding vb = getValueBinding("is3d");
        Boolean v = vb != null ? (Boolean) vb.getValue(getFacesContext()) : null;
        return v != null ? v.booleanValue() : true;
    }

    public void setIs3d(boolean is3d) {
        this.is3d = Boolean.valueOf(is3d);
    }

    /**
	 * Colors attributes for bar charts
	 */
    public String getColors() {
        if (colors != null) return colors;
        ValueBinding vb = getValueBinding("colors");
        String v = vb != null ? (String) vb.getValue(getFacesContext()) : null;
        return v != null ? v : null;
    }

    public void setColors(String colors) {
        this.colors = colors;
    }

    /**
	 * DataSource attribute
	 */
    public Object getDatasource() {
        if (datasource != null) return datasource;
        ValueBinding vb = getValueBinding("datasource");
        Object v = vb != null ? vb.getValue(getFacesContext()) : null;
        return v != null ? v : null;
    }

    public void setDatasource(Object datasource) {
        this.datasource = datasource;
    }

    /**
	 * Depth attribute for pie charts
	 */
    public int getDepth() {
        if (depth != null) return depth.intValue();
        ValueBinding vb = getValueBinding("depth");
        Integer v = vb != null ? (Integer) vb.getValue(getFacesContext()) : null;
        return v != null ? v.intValue() : 15;
    }

    public void setDepth(int depth) {
        this.depth = new Integer(depth);
    }

    /**
	 * Width attribute
	 */
    public int getWidth() {
        if (width != null) return width.intValue();
        ValueBinding vb = getValueBinding("width");
        Integer v = vb != null ? (Integer) vb.getValue(getFacesContext()) : null;
        return v != null ? v.intValue() : 400;
    }

    public void setWidth(int width) {
        this.width = new Integer(width);
    }

    /**
	 * Height attribute
	 */
    public int getHeight() {
        if (height != null) return height.intValue();
        ValueBinding vb = getValueBinding("height");
        Integer v = vb != null ? (Integer) vb.getValue(getFacesContext()) : null;
        return v != null ? v.intValue() : 300;
    }

    public void setHeight(int height) {
        this.height = new Integer(height);
    }

    /**
	 * Legend attribute
	 */
    public boolean getLegend() {
        if (legend != null) return legend.booleanValue();
        ValueBinding vb = getValueBinding("legend");
        Boolean v = vb != null ? (Boolean) vb.getValue(getFacesContext()) : null;
        return v != null ? v.booleanValue() : true;
    }

    public void setLegend(boolean legend) {
        this.legend = Boolean.valueOf(legend);
    }

    /**
	 * Legend postion attribute
	 */
    public String getLegendPosition() {
        if (legendPosition != null) return legendPosition;
        ValueBinding vb = getValueBinding("legendPosition");
        String v = vb != null ? (String) vb.getValue(getFacesContext()) : null;
        return v != null ? v : null;
    }

    public void setLegendPosition(String legendPosition) {
        this.legendPosition = legendPosition;
    }

    public boolean getLegendBorder() {
        if (legendBorder != null) return legendBorder.booleanValue();
        ValueBinding vb = getValueBinding("legendBorder");
        Boolean v = vb != null ? (Boolean) vb.getValue(getFacesContext()) : null;
        return v != null ? v.booleanValue() : true;
    }

    public void setLegendBorder(boolean legendBorder) {
        this.legendBorder = Boolean.valueOf(legendBorder);
    }

    public float getLegendFontSize() {
        if (legendFontSize != null) return legendFontSize.floatValue();
        ValueBinding vb = getValueBinding("legendFontSize");
        Float v = vb != null ? (Float) vb.getValue(getFacesContext()) : null;
        return v != null ? v.floatValue() : 0;
    }

    public void setLegendFontSize(float legendFontSize) {
        this.legendFontSize = new Float(legendFontSize);
    }

    /**
	 * Orientation attribute
	 */
    public String getOrientation() {
        if (orientation != null) return orientation;
        ValueBinding vb = getValueBinding("orientation");
        String v = vb != null ? (String) vb.getValue(getFacesContext()) : null;
        return v != null ? v : "vertical";
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    /**
	 * Outline attribute
	 */
    public boolean getOutline() {
        if (outline != null) return outline.booleanValue();
        ValueBinding vb = getValueBinding("outline");
        Boolean v = vb != null ? (Boolean) vb.getValue(getFacesContext()) : null;
        return v != null ? v.booleanValue() : true;
    }

    public void setOutline(boolean outline) {
        this.outline = Boolean.valueOf(outline);
    }

    /**
	 * Start Angle attribute for pie charts
	 */
    public int getStartAngle() {
        if (startAngle != null) return startAngle.intValue();
        ValueBinding vb = getValueBinding("startAngle");
        Integer v = vb != null ? (Integer) vb.getValue(getFacesContext()) : null;
        return v != null ? v.intValue() : 0;
    }

    public void setStartAngle(int startAngle) {
        this.startAngle = new Integer(startAngle);
    }

    /**
	 * Title attribute
	 */
    public String getTitle() {
        if (title != null) return title;
        ValueBinding vb = getValueBinding("title");
        String v = vb != null ? (String) vb.getValue(getFacesContext()) : null;
        return v != null ? v : null;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
	 * Type attribute
	 */
    public String getType() {
        if (type != null) return type;
        ValueBinding vb = getValueBinding("type");
        String v = vb != null ? (String) vb.getValue(getFacesContext()) : null;
        return v != null ? v : null;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
	 * X-axis attribute
	 */
    public String getXlabel() {
        if (xlabel != null) return xlabel;
        ValueBinding vb = getValueBinding("xlabel");
        String v = vb != null ? (String) vb.getValue(getFacesContext()) : null;
        return v != null ? v : null;
    }

    public void setXlabel(String xlabel) {
        this.xlabel = xlabel;
    }

    /**
	 * Y-axis attribute
	 */
    public String getYlabel() {
        if (ylabel != null) return ylabel;
        ValueBinding vb = getValueBinding("ylabel");
        String v = vb != null ? (String) vb.getValue(getFacesContext()) : null;
        return v != null ? v : null;
    }

    public void setYlabel(String ylabel) {
        this.ylabel = ylabel;
    }

    /**
	 * StyleClass attribute
	 */
    public String getStyleClass() {
        if (styleClass != null) return styleClass;
        ValueBinding vb = getValueBinding("styleClass");
        String v = vb != null ? (String) vb.getValue(getFacesContext()) : null;
        return v != null ? v : null;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    /**
	 * Alt attribute
	 */
    public String getAlt() {
        if (alt != null) return alt;
        ValueBinding vb = getValueBinding("alt");
        String v = vb != null ? (String) vb.getValue(getFacesContext()) : null;
        return v != null ? v : null;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    /**
	 * ImgTitle attribute
	 */
    public String getImgTitle() {
        if (imgTitle != null) return imgTitle;
        ValueBinding vb = getValueBinding("imgTitle");
        String v = vb != null ? (String) vb.getValue(getFacesContext()) : null;
        return v != null ? v : null;
    }

    public void setImgTitle(String imgTitle) {
        this.imgTitle = imgTitle;
    }

    /**
	 * Onclick attribute
	 */
    public String getOnclick() {
        if (onclick != null) return onclick;
        ValueBinding vb = getValueBinding("onclick");
        String v = vb != null ? (String) vb.getValue(getFacesContext()) : null;
        return v != null ? v : null;
    }

    public void setOnclick(String onclick) {
        this.onclick = onclick;
    }

    /**
	 * Ondblclick attribute
	 */
    public String getOndblclick() {
        if (ondblclick != null) return ondblclick;
        ValueBinding vb = getValueBinding("ondblclick");
        String v = vb != null ? (String) vb.getValue(getFacesContext()) : null;
        return v != null ? v : null;
    }

    public void setOndblclick(String ondblclick) {
        this.ondblclick = ondblclick;
    }

    /**
	 * Onkeydown attribute
	 */
    public String getOnkeydown() {
        if (onkeydown != null) return onkeydown;
        ValueBinding vb = getValueBinding("onkeydown");
        String v = vb != null ? (String) vb.getValue(getFacesContext()) : null;
        return v != null ? v : null;
    }

    public void setOnkeydown(String onkeydown) {
        this.onkeydown = onkeydown;
    }

    /**
	 * Onkeypress attribute
	 */
    public String getOnkeypress() {
        if (onkeypress != null) return onkeypress;
        ValueBinding vb = getValueBinding("onkeypress");
        String v = vb != null ? (String) vb.getValue(getFacesContext()) : null;
        return v != null ? v : null;
    }

    public void setOnkeypress(String onkeypress) {
        this.onkeypress = onkeypress;
    }

    /**
	 * Onkeyup attribute
	 */
    public String getOnkeyup() {
        if (onkeyup != null) return onkeyup;
        ValueBinding vb = getValueBinding("onkeyup");
        String v = vb != null ? (String) vb.getValue(getFacesContext()) : null;
        return v != null ? v : null;
    }

    public void setOnkeyup(String onkeyup) {
        this.onkeyup = onkeyup;
    }

    /**
	 * Onmousedown attribute
	 */
    public String getOnmousedown() {
        if (onmousedown != null) return onmousedown;
        ValueBinding vb = getValueBinding("onmousedown");
        String v = vb != null ? (String) vb.getValue(getFacesContext()) : null;
        return v != null ? v : null;
    }

    public void setOnmousedown(String onmousedown) {
        this.onmousedown = onmousedown;
    }

    /**
	 * Onmousemove attribute
	 */
    public String getOnmousemove() {
        if (onmousemove != null) return onmousemove;
        ValueBinding vb = getValueBinding("onmousemove");
        String v = vb != null ? (String) vb.getValue(getFacesContext()) : null;
        return v != null ? v : null;
    }

    public void setOnmousemove(String onmousemove) {
        this.onmousemove = onmousemove;
    }

    /**
	 * Onmouseout attribute
	 */
    public String getOnmouseout() {
        if (onmouseout != null) return onmouseout;
        ValueBinding vb = getValueBinding("onmouseout");
        String v = vb != null ? (String) vb.getValue(getFacesContext()) : null;
        return v != null ? v : null;
    }

    public void setOnmouseout(String onmouseout) {
        this.onmouseout = onmouseout;
    }

    /**
	 * Onmouseover attribute
	 */
    public String getOnmouseover() {
        if (onmouseover != null) return onmouseover;
        ValueBinding vb = getValueBinding("onmouseover");
        String v = vb != null ? (String) vb.getValue(getFacesContext()) : null;
        return v != null ? v : null;
    }

    public void setOnmouseover(String onmouseover) {
        this.onmouseover = onmouseover;
    }

    /**
	 * Onmouseup attribute
	 */
    public String getOnmouseup() {
        if (onmouseup != null) return onmouseup;
        ValueBinding vb = getValueBinding("onmouseup");
        String v = vb != null ? (String) vb.getValue(getFacesContext()) : null;
        return v != null ? v : null;
    }

    public void setOnmouseup(String onmouseup) {
        this.onmouseup = onmouseup;
    }

    /**
	 * Output attribute, default value is png
	 */
    public String getOutput() {
        if (output != null) return output;
        ValueBinding vb = getValueBinding("output");
        String v = vb != null ? (String) vb.getValue(getFacesContext()) : null;
        return v != null ? v : "png";
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getUsemap() {
        if (usemap != null) return usemap;
        ValueBinding vb = getValueBinding("usemap");
        String v = vb != null ? (String) vb.getValue(getFacesContext()) : null;
        return v != null ? v : null;
    }

    public void setUsemap(String usemap) {
        this.usemap = usemap;
    }

    public String getGenerateMap() {
        if (generateMap != null) return generateMap;
        ValueBinding vb = getValueBinding("generateMap");
        String v = vb != null ? (String) vb.getValue(getFacesContext()) : null;
        return v != null ? v : null;
    }

    public void setGenerateMap(String generateMap) {
        this.generateMap = generateMap;
    }

    /**
	 * Ongeneratedimagemapclick attribute
	 */
    public String getOngeneratedimagemapclick() {
        if (ongeneratedimagemapclick != null) return ongeneratedimagemapclick;
        ValueBinding vb = getValueBinding("ongeneratedimagemapclick");
        String v = vb != null ? (String) vb.getValue(getFacesContext()) : null;
        return v != null ? v : null;
    }

    public void setOngeneratedimagemapclick(String ongeneratedimagemapclick) {
        this.ongeneratedimagemapclick = ongeneratedimagemapclick;
    }

    public boolean getDomainGridLines() {
        if (domainGridLines != null) return domainGridLines.booleanValue();
        ValueBinding vb = getValueBinding("domainGridLines");
        Boolean v = vb != null ? (Boolean) vb.getValue(getFacesContext()) : null;
        return v != null ? v.booleanValue() : true;
    }

    public void setDomainGridLines(boolean domainGridLines) {
        this.domainGridLines = Boolean.valueOf(domainGridLines);
    }

    public boolean getRangeGridLines() {
        if (rangeGridLines != null) return rangeGridLines.booleanValue();
        ValueBinding vb = getValueBinding("rangeGridLines");
        Boolean v = vb != null ? (Boolean) vb.getValue(getFacesContext()) : null;
        return v != null ? v.booleanValue() : true;
    }

    public void setRangeGridLines(boolean rangeGridLines) {
        this.rangeGridLines = Boolean.valueOf(rangeGridLines);
    }

    public float getLineStrokeWidth() {
        if (lineStrokeWidth != null) return lineStrokeWidth.floatValue();
        ValueBinding vb = getValueBinding("lineStrokeWidth");
        Float v = vb != null ? (Float) vb.getValue(getFacesContext()) : null;
        return v != null ? v.floatValue() : 0;
    }

    public void setLineStrokeWidth(float lineStrokeWidth) {
        this.lineStrokeWidth = new Float(lineStrokeWidth);
    }

    public String getDataTooltipFormat() {
        if (dataTooltipFormat != null) return dataTooltipFormat;
        ValueBinding vb = getValueBinding("dataTooltipFormat");
        String v = vb != null ? (String) vb.getValue(getFacesContext()) : null;
        return v != null ? v : null;
    }

    public void setDataTooltipFormat(String dataTooltipFormat) {
        this.dataTooltipFormat = dataTooltipFormat;
    }

    public String getPieLabelBackground() {
        if (pieLabelBackground != null) return pieLabelBackground;
        ValueBinding vb = getValueBinding("pieLabelBackground");
        String v = vb != null ? (String) vb.getValue(getFacesContext()) : null;
        return v != null ? v : null;
    }

    public void setPieLabelBackground(String pieLabelBackground) {
        this.pieLabelBackground = pieLabelBackground;
    }

    public String getPieLabelOutline() {
        if (pieLabelOutline != null) return pieLabelOutline;
        ValueBinding vb = getValueBinding("pieLabelOutline");
        String v = vb != null ? (String) vb.getValue(getFacesContext()) : null;
        return v != null ? v : null;
    }

    public void setPieLabelOutline(String pieLabelOutline) {
        this.pieLabelOutline = pieLabelOutline;
    }

    public Object saveState(FacesContext context) {
        Object values[] = new Object[45];
        values[0] = super.saveState(context);
        values[1] = datasource;
        values[2] = width;
        values[3] = height;
        values[4] = alpha;
        values[5] = depth;
        values[6] = startAngle;
        values[7] = title;
        values[8] = type;
        values[9] = background;
        values[10] = foreground;
        values[11] = xlabel;
        values[12] = ylabel;
        values[13] = orientation;
        values[14] = colors;
        values[15] = is3d;
        values[16] = legend;
        values[17] = antialias;
        values[18] = outline;
        values[19] = styleClass;
        values[20] = alt;
        values[21] = imgTitle;
        values[22] = onclick;
        values[23] = ondblclick;
        values[24] = onmousedown;
        values[25] = onmouseup;
        values[26] = onmouseover;
        values[27] = onmousemove;
        values[28] = onmouseout;
        values[29] = onkeypress;
        values[30] = onkeydown;
        values[31] = onkeyup;
        values[32] = output;
        values[33] = usemap;
        values[34] = legendFontSize;
        values[35] = generateMap;
        values[36] = ongeneratedimagemapclick;
        values[37] = domainGridLines;
        values[38] = rangeGridLines;
        values[39] = legendBorder;
        values[40] = lineStrokeWidth;
        values[41] = dataTooltipFormat;
        values[42] = pieLabelBackground;
        values[43] = pieLabelOutline;
        values[44] = legendPosition;
        return values;
    }

    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        this.datasource = values[1];
        this.width = (Integer) values[2];
        this.height = (Integer) values[3];
        this.alpha = (Integer) values[4];
        this.depth = (Integer) values[5];
        this.startAngle = (Integer) values[6];
        this.title = (String) values[7];
        this.type = (String) values[8];
        this.background = (String) values[9];
        this.foreground = (String) values[10];
        this.xlabel = (String) values[11];
        this.ylabel = (String) values[12];
        this.orientation = (String) values[13];
        this.colors = (String) values[14];
        this.is3d = (Boolean) values[15];
        this.legend = (Boolean) values[16];
        this.antialias = (Boolean) values[17];
        this.outline = (Boolean) values[18];
        this.styleClass = (String) values[19];
        this.alt = (String) values[20];
        this.imgTitle = (String) values[21];
        this.onclick = (String) values[22];
        this.ondblclick = (String) values[23];
        this.onmousedown = (String) values[24];
        this.onmouseup = (String) values[25];
        this.onmouseover = (String) values[26];
        this.onmousemove = (String) values[27];
        this.onmouseout = (String) values[28];
        this.onkeypress = (String) values[29];
        this.onkeydown = (String) values[30];
        this.onkeyup = (String) values[31];
        this.output = (String) values[32];
        this.usemap = (String) values[33];
        this.legendFontSize = (Float) values[34];
        this.generateMap = (String) values[35];
        this.ongeneratedimagemapclick = (String) values[36];
        this.domainGridLines = (Boolean) values[37];
        this.rangeGridLines = (Boolean) values[38];
        this.legendBorder = (Boolean) values[39];
        this.lineStrokeWidth = (Float) values[40];
        this.dataTooltipFormat = (String) values[41];
        this.pieLabelBackground = (String) values[42];
        this.pieLabelOutline = (String) values[43];
        this.legendPosition = (String) values[44];
    }
}
