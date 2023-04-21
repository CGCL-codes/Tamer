package com.cosmos.acacia.callback.assembling;

import com.cosmos.acacia.crm.assembling.AlgorithmException;

/**
 *
 * @author Miro
 */
public class LessSelectedItemsThanAllowedException extends AlgorithmException {

    int selected;

    int allowed;

    public LessSelectedItemsThanAllowedException(int selected, int allowed) {
        super("The selected (" + selected + ") items are less than the required (" + allowed + ")");
        this.selected = selected;
        this.allowed = allowed;
    }

    public int getAllowed() {
        return allowed;
    }

    public int getSelected() {
        return selected;
    }
}
