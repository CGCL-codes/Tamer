package com.iver.cit.gvsig;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.gui.panels.FPanelAbout;

/**
 * About extension.
 *
 * Add the about coments to the gvSIG About panel
 */
public class AboutExpressionFieldExtension extends Extension {

    public void initialize() {
    }

    public void postInitialize() {
        About about = (About) PluginServices.getExtension(About.class);
        FPanelAbout panelAbout = about.getAboutPanel();
        java.net.URL aboutURL = this.getClass().getResource("/about.htm");
        panelAbout.addAboutUrl(PluginServices.getText(this, "calculate_expression"), aboutURL);
    }

    public void execute(String actionCommand) {
    }

    public boolean isEnabled() {
        return false;
    }

    public boolean isVisible() {
        return false;
    }
}
