package org.weblayouttag.tag.panel;

import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.weblayouttag.config.ConfigProperties;

/**
 * @author Andy Marek
 * @version Jun 7, 2005
 * @jsp.tag
 * 	name="panel"
 * 	body-content="JSP"
 * 	description="Creates a container for text, form elements, etc."
 */
public class PanelTagImpl extends AbstractPanelTag {

    public Element createElement() {
        return DocumentHelper.createElement("panel");
    }

    protected String getDefaultLegend() {
        return this.getFormConfig().getProperty(ConfigProperties.FIELDS_PANEL_LEGEND, StringUtils.EMPTY);
    }

    protected String getDefaultMode() {
        return this.getFormConfig().getProperty(ConfigProperties.FIELDS_PANEL_MODE, "E,E,I");
    }
}
