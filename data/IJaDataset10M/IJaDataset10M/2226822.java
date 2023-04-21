package alt.djudge.frontend.client.ui.editors;

public interface CustomEditor {

    public String getValueForKey(String key);

    public String getKey();

    public String getValue();

    public void setKey(String key);

    public void setValue(String value);

    public void setEnabled(boolean flag);
}
