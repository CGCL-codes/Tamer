package radar.config.webapp;

import org.jdom.*;
import radar.util.*;

public final class InitParam {

    private Element root;

    public InitParam() {
        root = new Element("init-param");
    }

    public InitParam(Element root) {
        this.root = root;
    }

    public String getParamName() {
        return JDOMUtil.getChildContent(root, "param-name");
    }

    public void setParamName(String paramName) {
        JDOMUtil.setChildContent(root, "param-name", paramName);
    }

    public String getParamValue() {
        return JDOMUtil.getChildContent(root, "param-value");
    }

    public void setParamValue(String paramValue) {
        JDOMUtil.setChildContent(root, "param-value", paramValue);
    }

    public String getDescription() {
        return JDOMUtil.getChildContent(root, "description");
    }

    public void setDescription(String description) {
        JDOMUtil.setChildContent(root, "description", description);
    }

    public Element getRootElement() {
        return root;
    }

    public void setRootElement(Element root) {
        this.root = root;
    }
}
