package org.kablink.teaming.gwt.client.lpe;

import org.kablink.teaming.gwt.client.widgets.UtilityElementWidget;
import org.kablink.teaming.gwt.client.widgets.VibeWidget;
import org.kablink.teaming.gwt.client.widgets.WidgetStyles;

/**
 * This class represents the configuration data for a Utility Element
 * @author jwootton
 *
 */
public class UtilityElementConfig extends ConfigItem {

    private UtilityElementProperties m_properties;

    /**
	 * 
	 */
    public UtilityElementConfig(String configStr, String binderId) {
        String[] results;
        m_properties = new UtilityElementProperties();
        m_properties.setBinderId(binderId);
        results = configStr.split("[,;]");
        if (results != null) {
            int i;
            for (i = 0; i < results.length; ++i) {
                String[] results2;
                results2 = results[i].split("=");
                if (results2 != null && results2.length == 2 && results2[0] != null && results2[1] != null && results2[1].length() > 0) {
                    if (results2[0].equalsIgnoreCase("element")) {
                        String type;
                        type = results2[1];
                        if (type != null) {
                            if (type.equalsIgnoreCase("myWorkspace")) m_properties.setType(UtilityElement.LINK_TO_MYWORKSPACE); else if (type.equalsIgnoreCase("siteAdmin")) m_properties.setType(UtilityElement.LINK_TO_ADMIN_PAGE); else if (type.equalsIgnoreCase("trackThis")) m_properties.setType(UtilityElement.LINK_TO_TRACK_FOLDER_OR_WORKSPACE); else if (type.equalsIgnoreCase("shareThis")) m_properties.setType(UtilityElement.LINK_TO_SHARE_FOLDER_OR_WORKSPACE); else if (type.equalsIgnoreCase("signInForm")) m_properties.setType(UtilityElement.SIGNIN_FORM);
                        }
                    }
                }
            }
        }
    }

    /**
	 * 
	 */
    public void addChild(ConfigItem configItem) {
    }

    /**
	 * Create a composite that can be used on any page.
	 */
    public VibeWidget createWidget(WidgetStyles widgetStyles) {
        return new UtilityElementWidget(this, widgetStyles);
    }

    /**
	 * Create a DropWidget that can be used in the landing page editor.
	 */
    public UtilityElementDropWidget createDropWidget(LandingPageEditor lpe) {
        return new UtilityElementDropWidget(lpe, this);
    }

    /**
	 * 
	 */
    public UtilityElementProperties getProperties() {
        return m_properties;
    }
}
