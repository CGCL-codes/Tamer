package jimo.osgi.xui;

import java.net.URL;
import java.util.Dictionary;

public interface ComponentFactory {

    String CF_TYPE = "xui.componentFactory.type";

    String CF_ABSTRACT = "xui.componentFactory.abstract";

    public Component createComponent(Dictionary properties);

    public Component[] importComponents(URL location);
}
