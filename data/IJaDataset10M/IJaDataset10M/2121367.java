package com.pcmsolutions.device.EMU.E4.zcommands.preset;

import com.pcmsolutions.device.EMU.E4.preset.ReadablePreset;
import java.awt.event.KeyEvent;

/**
 * Created by IntelliJ IDEA.
 * User: pmeehan
 * Date: 22-Mar-2003
 * Time: 14:40:54
 * To change this template use Options | File Templates.
 */
public class ClosePresetZMTC extends AbstractReadablePresetZMTCommand {

    public int getMnemonic() {
        return KeyEvent.VK_C;
    }

    public boolean isSuitableAsButton() {
        return true;
    }

    public String getPresentationString() {
        return "Close";
    }

    public String getDescriptiveString() {
        return "Close preset if opened in the workspace";
    }

    public boolean handleTarget(ReadablePreset p, int total, int curr) throws Exception {
        p.getPresetContext().getDeviceContext().getViewManager().closePreset(p).post();
        return true;
    }
}
