package org.kablink.teaming.gwt.client.lpe;

import org.kablink.teaming.gwt.client.widgets.LinkToEntryWidget;
import org.kablink.teaming.gwt.client.widgets.VibeWidget;
import org.kablink.teaming.gwt.client.widgets.WidgetStyles;
import com.google.gwt.http.client.URL;

/**
 * This class represents the configuration data for a "Link to Entry" widget
 * @author jwootton
 *
 */
public class LinkToEntryConfig extends ConfigItem {

    private LinkToEntryProperties m_properties;

    /**
	 * 
	 */
    public LinkToEntryConfig(String configStr) {
        String[] results;
        m_properties = new LinkToEntryProperties();
        results = configStr.split("[,;]");
        if (results != null) {
            int i;
            for (i = 0; i < results.length; ++i) {
                String[] results2;
                results2 = ConfigData.splitConfigItem(results[i]);
                if (results2 != null && results2.length == 2 && results2[0] != null && results2[1] != null && results2[1].length() > 0) {
                    try {
                        if (results2[0].equalsIgnoreCase("title")) m_properties.setTitle(URL.decodeComponent(results2[1])); else if (results2[0].equalsIgnoreCase("entryId")) m_properties.setEntryId(results2[1]); else if (results2[0].equalsIgnoreCase("zoneUUID")) m_properties.setZoneUUID(results2[1]); else if (results2[0].equalsIgnoreCase("popup")) m_properties.setOpenInNewWindow(results2[1].equalsIgnoreCase("1"));
                    } catch (Exception ex) {
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
        return new LinkToEntryWidget(this, widgetStyles);
    }

    /**
	 * Create a DropWidget that can be used in the landing page editor.
	 */
    public LinkToEntryDropWidget createDropWidget(LandingPageEditor lpe) {
        return new LinkToEntryDropWidget(lpe, this);
    }

    /**
	 * 
	 */
    public LinkToEntryProperties getProperties() {
        return m_properties;
    }
}
