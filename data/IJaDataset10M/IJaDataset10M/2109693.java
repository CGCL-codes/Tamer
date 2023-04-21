package com.xenoage.zong.viewer.config.keyboard;

import com.xenoage.pdlib.PVector;
import com.xenoage.zong.gui.event.VScoreDocKeyEvent;

/**
 * One entry of a keyboard configuration file.
 * 
 * Each entry has a command ID, the corresponding
 * key code and optional parameters.
 * 
 * @author Andreas Wenger
 */
public final class KeyboardConfigEntry {

    public final String commandID;

    public final PVector<String> params;

    public final int keyCode;

    /** A combination of {@link VScoreDocKeyEvent}.SHIFT, .CTRL and .ALT */
    public final int modifiers;

    public KeyboardConfigEntry(String commandID, PVector<String> params, int keyCode, int modifiers) {
        this.commandID = commandID;
        this.params = params;
        this.keyCode = keyCode;
        this.modifiers = modifiers;
    }
}
