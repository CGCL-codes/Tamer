package org.formaria.awt;

import java.awt.AWTEvent;
import java.awt.*;
import java.awt.peer.*;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.MenuComponent;

public class DialogEventDispatchThread extends Thread {

    private EventQueue theQueue;

    private boolean doDispatch = true;

    DialogEventDispatchThread(String name, EventQueue queue) {
        super(name);
        theQueue = queue;
    }

    public void stopDispatching(boolean bFireEmptyEvent) {
        doDispatch = false;
        if (bFireEmptyEvent) theQueue.postEvent(new EmptyEvent());
        if (Thread.currentThread() != this) {
            try {
                join();
            } catch (InterruptedException e) {
            }
        }
    }

    class EmptyEvent extends AWTEvent implements ActiveEvent {

        public EmptyEvent() {
            super(DialogEventDispatchThread.this, 0);
        }

        public void dispatch() {
        }
    }

    public void run() {
        while (doDispatch) {
            try {
                AWTEvent event = theQueue.getNextEvent();
                if (false) {
                } else {
                    Object src = event.getSource();
                    if (event instanceof ActiveEvent) {
                        ((ActiveEvent) event).dispatch();
                    } else if (src instanceof Component) {
                        ((Component) src).dispatchEvent(event);
                    } else if (src instanceof MenuComponent) {
                        ((MenuComponent) src).dispatchEvent(event);
                    }
                }
            } catch (ThreadDeath death) {
                return;
            } catch (Throwable e) {
                System.err.println("Exception occurred during event dispatching:");
                e.printStackTrace();
            }
        }
    }
}
