package com.pcmsolutions.device.EMU.E4.zcommands.preset;

import com.pcmsolutions.device.EMU.E4.preset.ContextEditablePreset;
import com.pcmsolutions.device.EMU.E4.preset.PresetContextMacros;

/**
 * Created by IntelliJ IDEA.
 * User: pmeehan
 * Date: 22-Mar-2003
 * Time: 14:40:54
 * To change this template use Options | File Templates.
 */
public class AutoMapVoicesZMTC extends AbstractEditableVoiceZMTCommand {

    public int getMinNumTargets() {
        return 2;
    }

    public String getPresentationString() {
        return "AutoMapV";
    }

    public String getDescriptiveString() {
        return "Auto-map voices based on original key";
    }

    public String getMenuPathString() {
        return ";Key Mapping";
    }

    public boolean handleTarget(ContextEditablePreset.EditableVoice editableVoice, int total, int curr) throws Exception {
        ContextEditablePreset.EditableVoice[] voices = getTargets().toArray(new ContextEditablePreset.EditableVoice[numTargets()]);
        PresetContextMacros.autoMapVoiceKeyWin(voices);
        return false;
    }
}
