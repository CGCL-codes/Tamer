package com.pcmsolutions.device.EMU.E4.zcommands.preset;

import com.pcmsolutions.device.EMU.E4.preset.ReadablePreset;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: pmeehan
 * Date: 22-Mar-2003
 * Time: 14:40:54
 * To change this template use Options | File Templates.
 */
public class ViewVoiceZMTC extends AbstractReadableVoiceZMTCommand {

    public String getPresentationCategory() {
        return "Visibility";
    }

    public String getPresentationString() {
        return "OpenV";
    }

    public String getDescriptiveString() {
        return "Open for viewing";
    }

    public boolean handleTarget(ReadablePreset.ReadableVoice readableVoice, int total, int curr) throws Exception {
        ReadablePreset.ReadableVoice[] voices = getTargets().toArray(new ReadablePreset.ReadableVoice[numTargets()]);
        Arrays.sort(voices);
        boolean tabbed = voices[0].getPreset().getDeviceContext().getDevicePreferences().ZPREF_usePartitionedVoiceEditing.getValue();
        boolean grouped = voices[0].getPreset().getDeviceContext().getDevicePreferences().ZPREF_groupEnvelopesWhenVoiceTabbed.getValue();
        for (int i = 0; i < voices.length; i++) if (tabbed) {
            voices[i].getPreset().getDeviceContext().getViewManager().openTabbedVoice(voices[i], grouped, (i == 0 ? true : false)).post();
        } else voices[i].getPreset().getDeviceContext().getViewManager().openVoice(voices[i], (i == 0 ? true : false)).post();
        return false;
    }
}
