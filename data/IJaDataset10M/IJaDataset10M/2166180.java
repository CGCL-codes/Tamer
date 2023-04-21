package annone.html;

public class BUTTON extends BlockTag {

    public BUTTON(Element... elements) {
        super("BUTTON", elements);
    }

    public String getType() {
        return getAttribute("type");
    }

    public void setType(String value) {
        setAttribute("type", value);
    }

    public String getName() {
        return getAttribute("name");
    }

    public void setName(String value) {
        setAttribute("name", value);
    }

    public String getValue() {
        return getAttribute("value");
    }

    public void setValue(String value) {
        setAttribute("value", value);
    }

    public String getDisabled() {
        return getAttribute("disabled");
    }

    public void setDisabled(String value) {
        setAttribute("disabled", value);
    }
}
