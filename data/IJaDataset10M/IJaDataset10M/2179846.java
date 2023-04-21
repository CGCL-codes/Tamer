package com.pcmsolutions.device.EMU.E4;

import com.pcmsolutions.device.EMU.E4.preset.PresetException;

/**
 *
 * @author  pmeehan
 */
class NoSuchVoiceException extends PresetException {

    public NoSuchVoiceException() {
        super("no such voice");
    }
}
