package com.google.code.sagetvaddons.sdm.properties;

import sage.SageTVPlugin;
import sagex.plugin.IPropertyValidator;
import com.google.code.sagetvaddons.sdm.Plugin;

/**
 * @author dbattams
 *
 */
public final class PluginPropertyBtSeedRatio extends PluginPropertyServerPersisted {

    private static final String PROP_NAME = Plugin.PROP_PREFIX + "bt_seed_ratio";

    private static final String PROP_DEFAULT = "1.0";

    public PluginPropertyBtSeedRatio() {
        super(SageTVPlugin.CONFIG_TEXT, PROP_NAME, PROP_DEFAULT, "BitTorrent Seed Ratio", "Set the seed ratio for all BitTorrent downloads.  Zero means seed forever.  This is a floating point value.");
        setValidator(new IPropertyValidator() {

            @Override
            public void validate(String prop, String val) throws Exception {
                if (Double.parseDouble(val) < 0) throw new IllegalArgumentException("Seed ratio must be zero or greater.");
            }
        });
    }
}
