package org.xaware.ide.xadev.richui.editor.service;

import org.xaware.ide.xadev.gui.ChangeListener;

public class InputXmlChangeUserData {

    private ChangeListener producer;

    public InputXmlChangeUserData(ChangeListener producer) {
        this.producer = producer;
    }

    /**
     * @return the editor
     */
    public ChangeListener getProducer() {
        return producer;
    }
}
