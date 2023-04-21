package com.pcmsolutions.device.EMU.E4.zcommands.preset;

import com.pcmsolutions.system.AbstractZMTCommand;
import com.pcmsolutions.device.EMU.E4.preset.ReadablePreset;

/**
 * User: paulmeehan
 * Date: 04-Mar-2004
 * Time: 13:10:41
 */
public abstract class AbstractPresetZMTCommand<T extends ReadablePreset> extends AbstractZMTCommand<T> {

    public String getPresentationCategory() {
        return "Preset";
    }
}
