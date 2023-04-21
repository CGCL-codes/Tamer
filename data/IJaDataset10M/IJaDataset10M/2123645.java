package sui;

import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.event.ListDataListener;

public class CBM extends DefaultComboBoxModel {

    private String key;

    private String value;

    public CBM() {
        super();
    }

    public CBM(String key, String value, Object[] items) {
        super(items);
        this.key = key;
        this.value = value;
    }

    public CBM(Object[] items) {
        super(items);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void addElement(Object obj) {
        Map<String, String> map = (Map<String, String>) obj;
        super.addElement(map.get(key));
    }

    @Override
    public Object getSelectedItem() {
        Map<String, String> map = (Map<String, String>) super.getSelectedItem();
        return map.get(value);
    }
}
