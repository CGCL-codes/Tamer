package ms.jacrim.framework;

import ms.utils.XmlFile;
import org.w3c.dom.Element;

public class XmlConfiguration implements Configuration {

    private XmlFile source;

    public XmlConfiguration(XmlFile source) {
        this.source = source;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(String item) throws Exception {
        Element node = source.selectSingleNode("/configuration/settings/add[@item='" + item + "']");
        Object result = null;
        if (node != null) {
            String text = node.getTextContent();
            String dataType = node.getAttribute("type");
            if ("int".equalsIgnoreCase(dataType)) result = Integer.parseInt(text); else result = text;
        } else throw new Exception(String.format("There is no configuration item named %s exists.", item));
        return (T) result;
    }

    @Override
    public <T> T get(String item, T defaultValue) {
        try {
            return get(item);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public XmlFile getSource() {
        return source;
    }
}
