package com.google.gwt.event.shared;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.junit.client.GWTTestCase;
import java.util.HashSet;

/**
 * Support code for handler tests.
 */
public abstract class HandlerTestBase extends GWTTestCase {

    @Override
    public String getModuleName() {
        return "com.google.gwt.event.Event";
    }

    class Adaptor implements ClickHandler, MouseDownHandler {

        public void onClick(ClickEvent event) {
            add(this);
        }

        public void onMouseDown(MouseDownEvent event) {
            add(this);
        }

        @Override
        public String toString() {
            return "adaptor 1";
        }
    }

    Adaptor adaptor1 = new Adaptor();

    private HashSet<EventHandler> active = new HashSet<EventHandler>();

    MouseDownHandler mouse1 = new MouseDownHandler() {

        public void onMouseDown(MouseDownEvent event) {
            add(mouse1);
        }

        @Override
        public String toString() {
            return "mouse 1";
        }
    };

    MouseDownHandler mouse2 = new MouseDownHandler() {

        public void onMouseDown(MouseDownEvent event) {
            add(mouse2);
        }

        @Override
        public String toString() {
            return "mouse 2";
        }
    };

    MouseDownHandler mouse3 = new MouseDownHandler() {

        public void onMouseDown(MouseDownEvent event) {
            add(mouse3);
        }

        @Override
        public String toString() {
            return "mouse 3";
        }
    };

    ClickHandler click1 = new ClickHandler() {

        public void onClick(ClickEvent event) {
            add(click1);
        }

        @Override
        public String toString() {
            return "click 1";
        }
    };

    ClickHandler click2 = new ClickHandler() {

        public void onClick(ClickEvent event) {
            add(click2);
        }

        @Override
        public String toString() {
            return "click 2";
        }
    };

    ClickHandler click3 = new ClickHandler() {

        public void onClick(ClickEvent event) {
            add(click3);
        }

        @Override
        public String toString() {
            return "click 3";
        }
    };

    void add(EventHandler handler) {
        active.add(handler);
    }

    void assertFired(EventHandler... handler) {
        for (int i = 0; i < handler.length; i++) {
            assertTrue(handler[i] + " should have fired", active.contains(handler[i]));
        }
    }

    void assertNotFired(EventHandler... handler) {
        for (int i = 0; i < handler.length; i++) {
            assertFalse(handler[i] + " should not have fired", active.contains(handler[i]));
        }
    }

    void reset() {
        active.clear();
    }
}
