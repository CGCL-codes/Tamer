package com.ibm.tuningfork.core.player;

import org.eclipse.jface.action.Action;

public final class SkipForwardAction extends Action {

    /**
     * 
     */
    private final PlayerController controller;

    private static final String DEFINITION_ID = "com.ibm.tuningfork.core.action.SkipForwardAction";

    public SkipForwardAction(PlayerController controller) {
        this.controller = controller;
        setId(DEFINITION_ID);
        setActionDefinitionId(DEFINITION_ID);
        setText("Skip Forward");
        setToolTipText("Skip to the next bookmark");
        setImageDescriptor(PlayerController.SKIP_FORWARD_IMAGE_DESCRIPTOR);
        setEnabled(true);
    }

    public final void run() {
        controller.playerSkip(Player.FORWARD);
    }
}
