package blueprint4j.utils;

public class BindFileLocation extends BindString implements BindFileLocationInterface {

    public BindFileLocation(String p_name, String p_description, Bindable p_bindable) {
        super(p_name, p_description, p_bindable);
    }

    public void setObject(Object object) {
        String str = (String) object;
        if (str != null && str.indexOf("\\") != -1) {
            super.setObject(Utils.replaceAll(str, "\\", "/"));
        } else {
            super.setObject(str);
        }
    }
}
