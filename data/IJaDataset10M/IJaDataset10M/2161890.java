package com.coladoro.core.handlers;

import com.coladoro.core.events.CoreCommandEvent;
import com.coladoro.core.events.Event;
import com.coladoro.core.events.EventResult;
import com.coladoro.core.shell.CoreCommand;

/**
 * The handler for the Core Command Events.
 * 
 * @author tanis
 */
public class CoreCommandEventHandler implements EventHandler {

    public void handleEvent(Event eventToHandle) throws InterruptedException {
        CoreCommandEvent netCommandEvent = (CoreCommandEvent) eventToHandle;
        EventResult commandResult = ((CoreCommand) netCommandEvent.getCommandToTreat()).execute();
        netCommandEvent.getEventListener().eventFinished(commandResult);
    }
}
