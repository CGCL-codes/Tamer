package net.sf.jwan.jsf.form.tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RadioItemsTag extends AbstractRadioItemTag {

    static Log logger = LogFactory.getLog(RadioItemsTag.class);

    public String getRendererType() {
        return "net.sf.jwan.RadioItem";
    }

    public String getComponentType() {
        return "net.sf.jwan.RadioItems";
    }
}
