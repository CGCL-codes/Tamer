package org.ztest.output.graphml.style;

public class ZEdgeStyle {

    public static final String ARROW_TYPE_STANDARD = "standard";

    public static final String ARROW_TYPE_DELTA = "delta";

    public static final String ARROW_TYPE_WHITE_DELTA = "white_delta";

    public static final String ARROW_TYPE_NONE = "none";

    private String text = "";

    private ZLineStyle lineStyle = new ZLineStyle();

    private String arrow1 = "none";

    private String arrow2 = ARROW_TYPE_STANDARD;

    private ZBorderStyle borderStyle = new ZBorderStyle();

    public ZBorderStyle getBorderStyle() {
        return borderStyle;
    }

    public void setBorderStyle(ZBorderStyle borderStyle) {
        this.borderStyle = borderStyle;
    }

    public String getArrow1() {
        return arrow1;
    }

    public void setArrow1(String arrow1) {
        this.arrow1 = arrow1;
    }

    public String getArrow2() {
        return arrow2;
    }

    public void setArrow2(String arrow2) {
        this.arrow2 = arrow2;
    }

    public ZLineStyle getLineStyle() {
        return lineStyle;
    }

    public void setLineStyle(ZLineStyle line) {
        this.lineStyle = line;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
