package testifie.ui.common.beans;

/**
 * @author slips
 */
public class Dialog {

    private String _type = null;

    private Enter _enterValue = null;

    private String _title = null;

    public void setType(String type) {
        _type = type;
    }

    public String getType() {
        return _type;
    }

    public void setEnterValue(Enter enter) {
        _enterValue = enter;
    }

    public Enter getEnterValue() {
        return _enterValue;
    }

    public void setTitle(String title) {
        _title = title;
    }

    public String getTitle() {
        return _title;
    }
}
