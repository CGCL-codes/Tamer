package com.google.code.sagetvaddons.sdm.properties;

import sage.SageTVPlugin;
import sagex.plugin.IPropertyValidator;
import com.google.code.sagetvaddons.sdm.Plugin;

/**
 * @author dbattams
 *
 */
public final class PluginPropertyMaxUploadSpeed extends PluginPropertyServerPersisted {

    private static final String PROP_NAME = Plugin.PROP_PREFIX + "max_upload_speed";

    private static final String PROP_DEFAULT = "0";

    public PluginPropertyMaxUploadSpeed() {
        super(SageTVPlugin.CONFIG_INTEGER, PROP_NAME, PROP_DEFAULT, "Max Upload Speed (KB)", "Maximum upload speed for all active uploads combined, in KBytes.  Zero means no maximum.");
        setValidator(new IPropertyValidator() {

            @Override
            public void validate(String prop, String val) throws Exception {
                if (Integer.parseInt(val) < 0) throw new IllegalArgumentException("Max upload speed must be greater than zero!");
            }
        });
    }
}
