package com.habitsoft.kiyaa.util;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.Widget;

public class HoverStyleHandler implements MouseListener, MouseOutHandler, MouseOverHandler, MouseDownHandler, MouseUpHandler {

    final Widget target;

    final Group group;

    public static class Group {

        HoverStyleHandler active;

        public Group() {
        }

        public HoverStyleHandler getActive() {
            return active;
        }

        public void setActive(HoverStyleHandler active) {
            if (this.active != null && this.active != active) {
                try {
                    this.active.onMouseLeave(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            this.active = active;
        }

        public void setInActive(HoverStyleHandler active) {
            if (this.active == active) {
                this.active = null;
            }
        }

        public void clear() {
            setActive(null);
        }
    }

    public HoverStyleHandler(Widget target, Group group) {
        super();
        this.target = target;
        this.group = group;
    }

    public void onMouseDown(Widget widget, int arg1, int arg2) {
        onMouseDown(null);
    }

    public void onMouseDown(MouseDownEvent event) {
        try {
            target.addStyleDependentName("active");
        } catch (Throwable t) {
            t.printStackTrace();
        }
        if (group != null) group.setActive(this);
    }

    public void onMouseOver(MouseOverEvent event) {
        try {
            target.addStyleDependentName("hover");
        } catch (Throwable t) {
            t.printStackTrace();
        }
        if (group != null) group.setActive(this);
    }

    public void onMouseEnter(Widget widget) {
        onMouseOver(null);
    }

    public void onMouseOut(MouseOutEvent event) {
        try {
            target.removeStyleDependentName("hover");
            target.removeStyleDependentName("active");
        } catch (Throwable t) {
            t.printStackTrace();
        }
        if (group != null) group.setInActive(this);
    }

    public void onMouseLeave(Widget widget) {
        onMouseOut(null);
    }

    public void onMouseMove(Widget widget, int arg1, int arg2) {
    }

    public void onMouseUp(Widget widget, int arg1, int arg2) {
        onMouseUp(null);
    }

    public void onMouseUp(MouseUpEvent event) {
        target.removeStyleDependentName("active");
    }
}
