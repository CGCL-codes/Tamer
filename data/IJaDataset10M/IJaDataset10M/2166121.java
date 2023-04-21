package org.binstitute.client.command;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;

public class CommandClickHandler implements ClickHandler {

    private Command command;

    public CommandClickHandler(Command command) {
        this.command = command;
    }

    @Override
    public void onClick(ClickEvent event) {
        command.execute();
    }
}
