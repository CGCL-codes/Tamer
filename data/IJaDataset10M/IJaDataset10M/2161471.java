package org.thechiselgroup.choosel.core.client.ui;

import java.util.HashMap;
import java.util.Map;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class keeps track of a list of Flow Panels which are used for
 * configuring Slot Mappings.
 */
public class ConfigurationPanel extends VerticalPanel {

    private static final String CSS_CONFIGURATION_PANEL_SETTING_LABEL = "choosel-ConfigurationPanel-SettingLabel";

    private static final String CSS_CONFIGURATION_PANEL = "choosel-ConfigurationPanel";

    private Map<String, SlotMappingFlowPanel> flowPanels = new HashMap<String, SlotMappingFlowPanel>();

    public ConfigurationPanel() {
        setStyleName(CSS_CONFIGURATION_PANEL);
    }

    private void addConfigurationSetting(String name, Widget widget) {
        SlotMappingFlowPanel settingPanel = createSlotMappingUI(name, widget);
        flowPanels.put(name, settingPanel);
        add(settingPanel);
    }

    private Label createLabelFromString(String name) {
        Label settingLabel = new Label(name);
        settingLabel.setStyleName(CSS_CONFIGURATION_PANEL_SETTING_LABEL);
        return settingLabel;
    }

    private SlotMappingFlowPanel createSlotMappingUI(String name, Widget widget) {
        Label settingLabel = createLabelFromString(name);
        SlotMappingFlowPanel settingPanel = new SlotMappingFlowPanel(settingLabel, widget);
        return settingPanel;
    }

    /**
     * this method will add the correct slot mapping widgets to this class. If
     * they are not already created, then it will create them, and if they are
     * already created it will update them
     */
    public void setConfigurationSetting(String name, Widget widget) {
        assert name != null;
        assert widget != null;
        if (flowPanels.containsKey(name)) {
            updateConfigurationSetting(name, widget);
        } else {
            addConfigurationSetting(name, widget);
        }
    }

    /**
     * This method is the same as setConfigurationSetting except that it will
     * fail if the configuration has not already been set
     */
    public void updateConfigurationSetting(String name, Widget widget) {
        assert name != null;
        assert widget != null;
        assert flowPanels.containsKey(name);
        flowPanels.get(name).setSlotMappingWidget(widget);
    }
}
