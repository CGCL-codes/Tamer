package com.peterhi.player.actions;

import java.awt.event.ActionEvent;
import com.peterhi.player.Whiteboard;

public class InPlaceEditCancelAction extends BaseAction {

    private static final InPlaceEditCancelAction instance = new InPlaceEditCancelAction();

    public static InPlaceEditCancelAction getInstance() {
        return instance;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Whiteboard.getWhiteboard().cancelInPlaceEdit();
    }
}
