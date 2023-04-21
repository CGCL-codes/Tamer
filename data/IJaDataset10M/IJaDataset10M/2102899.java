package cn.myapps.ui;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Image;

public class Option {

    public Option(String name, String value) {
        this(name, value, null);
    }

    public Option(String name, String value, Image imgPort) {
        this(name, value, imgPort, false);
    }

    public Option(String name, String value, Image imgPort, boolean selected) {
        this.name = name;
        this.selected = selected;
        this.value = value;
        this.imgPort = imgPort;
        font = Font.getDefaultFont();
    }

    public Option(String text) {
        this(text, "");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Image getImgPort() {
        return imgPort;
    }

    public void setImgPort(Image imgPort) {
        this.imgPort = imgPort;
    }

    public void setGroup(OptionGroup group) {
        this.group = group;
    }

    public OptionGroup getGroup() {
        return group;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    private Font font;

    private Image imgPort;

    private String type;

    String name = "";

    boolean selected = false;

    String value = "";

    private OptionGroup group;
}
