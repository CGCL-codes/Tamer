package csiebug.web.html.chart.jqPlotBean;

/**
 * @author George_Tsai
 * @version 2010/6/4
 */
public class JQPlotCanvasAxisLabelRenderer implements JQPlotOptions {

    private Integer angle;

    private Boolean show;

    private Boolean showLabel;

    private String label;

    private String fontFamily;

    private String fontSize;

    private String fontWeight;

    private Float fontStretch;

    private String textColor;

    private Boolean enableFontSupport;

    private Float pt2px;

    public void setAngle(Integer angle) {
        this.angle = angle;
    }

    public Integer getAngle() {
        return angle;
    }

    public void setShow(Boolean show) {
        this.show = show;
    }

    public Boolean getShow() {
        return show;
    }

    public void setShowLabel(Boolean showLabel) {
        this.showLabel = showLabel;
    }

    public Boolean getShowLabel() {
        return showLabel;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    public String getFontSize() {
        return fontSize;
    }

    public void setFontWeight(String fontWeight) {
        this.fontWeight = fontWeight;
    }

    public String getFontWeight() {
        return fontWeight;
    }

    public void setFontStretch(Float fontStretch) {
        this.fontStretch = fontStretch;
    }

    public Float getFontStretch() {
        return fontStretch;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setEnableFontSupport(Boolean enableFontSupport) {
        this.enableFontSupport = enableFontSupport;
    }

    public Boolean getEnableFontSupport() {
        return enableFontSupport;
    }

    public void setPt2px(Float pt2px) {
        this.pt2px = pt2px;
    }

    public Float getPt2px() {
        return pt2px;
    }
}
