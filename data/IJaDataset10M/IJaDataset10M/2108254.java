package org.thechiselgroup.choosel.workbench.client.command.ui;

import org.thechiselgroup.choosel.core.client.util.Initializable;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public class CommandPresenter<T extends HasClickHandlers & IsWidget> implements IsWidget, Initializable {

    private final Command command;

    private final T display;

    public CommandPresenter(T display, Command command) {
        assert display != null;
        assert command != null;
        this.display = display;
        this.command = command;
    }

    @Override
    public Widget asWidget() {
        return display.asWidget();
    }

    @Override
    public void init() {
        display.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                command.execute();
            }
        });
    }
}
