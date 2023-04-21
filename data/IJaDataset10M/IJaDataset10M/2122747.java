package net.sourceforge.symba.web.client.gui.handlers;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Widget;
import net.sourceforge.symba.web.client.gui.panel.SymbaController;

public class CancelAllClickHandler implements ClickHandler {

    private final SymbaController controller;

    private final Widget replacement;

    public CancelAllClickHandler(SymbaController controller, Widget replacement) {
        this.controller = controller;
        this.replacement = replacement;
    }

    public void onClick(ClickEvent event) {
        controller.cancelAll();
    }
}
