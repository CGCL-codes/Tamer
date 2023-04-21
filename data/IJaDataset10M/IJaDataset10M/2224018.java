package com.tangledcode.mtm_game.event;

import java.util.ArrayList;

public class EventDispatcher {

    private static ArrayList<EventHandler> eventHandlers = new ArrayList<EventHandler>();

    private EventDispatcher() {
    }

    public static void addEventHandler(EventHandler eventHandler) {
        EventDispatcher.eventHandlers.add(eventHandler);
    }

    public static void removeEventHandler(EventHandler eventHandler) {
        EventDispatcher.eventHandlers.remove(eventHandler);
    }

    public static void dispatch(Event event) {
        for (int i = 0; i < EventDispatcher.eventHandlers.size(); i++) {
            EventDispatcher.eventHandlers.get(i).handleEvent(event);
        }
    }
}
