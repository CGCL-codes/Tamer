package org.makagiga.laf.tango;

import org.makagiga.commons.*;
import org.makagiga.laf.LookAndFeelPlugin;
import org.makagiga.plugins.PluginException;
import org.makagiga.plugins.PluginInfo;

public final class Plugin extends LookAndFeelPlugin {

    public Plugin() {
        setIconThemeName("Tango");
    }

    @Override
    public void onInit(final Config config, final PluginInfo info) throws PluginException {
        super.onInit(config, info);
        setIconLoader(new TangoIconLoader());
        FS.addUserIconLoader(getIconLoader());
    }
}
