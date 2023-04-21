package com.pcmsolutions.device.EMU.E4.events.preset;

import com.pcmsolutions.device.EMU.E4.preset.PresetListener;
import com.pcmsolutions.device.EMU.E4.events.preset.PresetEvent;

/**
 * @author pmeehan
 */
public class PresetNameChangeEvent extends PresetEvent {

    private String name;

    public PresetNameChangeEvent(Object source, Integer preset, String name) {
        super(source, preset);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void fire(PresetListener pl) {
        if (pl != null) pl.presetNameChanged(this);
    }
}
