package net.sf.jwan.servlet.gui.form;

public class WanFormSelectItem extends AbstractWanFormInput implements WanFormInput {

    private String display, value;

    private boolean selected;

    public WanFormSelectItem(String display, String value) {
        this.display = display;
        this.value = value;
    }

    public WanFormSelectItem(String display, int value) {
        this(display, value + "");
    }

    public WanFormSelectItem(int display, int value) {
        this(display + "", value + "");
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public StringBuffer render() {
        StringBuffer sb = new StringBuffer();
        sb.append("<option ");
        if (value != null) {
            sb.append("value=\"" + value + "\"");
        }
        if (selected) {
            sb.append(" selected");
        }
        sb.append(">");
        sb.append(display);
        sb.append("</option>");
        return sb;
    }
}
