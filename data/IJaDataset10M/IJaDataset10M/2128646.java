package com.hifiremote.jp1;

import java.awt.*;
import javax.swing.*;

/**
 * The Class MacroButtonRenderer.
 */
public class MacroButtonRenderer extends DefaultListCellRenderer {

    /** The remote. */
    private Remote remote = null;

    /**
   * Instantiates a new macro button renderer.
   */
    public MacroButtonRenderer() {
    }

    /**
   * Sets the remote.
   * 
   * @param remote the new remote
   */
    public void setRemote(Remote remote) {
        this.remote = remote;
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        String text = remote.getButtonName(((Number) value).intValue());
        return super.getListCellRendererComponent(list, text, index, isSelected, cellHasFocus);
    }
}
