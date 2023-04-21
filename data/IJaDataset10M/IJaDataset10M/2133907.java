package net.sourceforge.jtee.gui;

import java.lang.*;
import java.util.*;
import java.awt.*;

public abstract class DisplayElement {

    private String id;

    protected int x, y, width, height;

    protected Manager manager;

    protected boolean visible;

    protected ArrayList eventPosterDescriptors;

    protected ArrayList eventObserverDescriptors;

    public static DisplayElement CreateDisplayElement(Manager manager, String type, String id, int x, int y, int width, int height) {
        if (type.equals("Title")) {
            return new TitleElement(manager, id, x, y, width, height);
        } else if (type.equals("Text")) {
            return new TextElement(manager, id, x, y, width, height);
        } else if (type.equals("TextField")) {
            return new TextFieldElement(manager, id, x, y, width, height);
        } else if (type.equals("TextArea")) {
            return new TextAreaElement(manager, id, x, y, width, height);
        } else if (type.equals("RadioButtonGroup")) {
            return new RadioButtonGroupElement(manager, id, x, y, width, height);
        } else if (type.equals("PrisonersDilemma")) {
            return new PrisonersDilemmaElement(manager, id, x, y, width, height);
        } else if (type.equals("SpinningBar")) {
            return new SpinningBarElement(manager, id, x, y, width, height);
        }
        return null;
    }

    public DisplayElement(Manager manager, String id, int x, int y, int width, int height) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.manager = manager;
        this.visible = true;
        this.eventPosterDescriptors = new ArrayList();
        this.eventObserverDescriptors = new ArrayList();
    }

    public String getId() {
        return id;
    }

    public boolean setContent(String content) {
        return false;
    }

    public boolean setLabel(String label) {
        return false;
    }

    public boolean setProperty(String key, String value) {
        if (key.equals("visible")) {
            try {
                this.visible = Boolean.parseBoolean(value);
                this.handleVisibilityUpdate();
                return true;
            } catch (Exception e) {
                this.visible = true;
            }
        }
        return false;
    }

    public void setEventPoster(String eventName, String property, String operator, String value) {
        EventDescriptor eventDescriptor = new EventDescriptor(eventName, property, operator, value);
        this.eventPosterDescriptors.add(eventDescriptor);
    }

    public void setEventObserver(String eventName, String property, String value) {
        EventDescriptor eventDescriptor = new EventDescriptor(eventName, property, "", value);
        this.eventObserverDescriptors.add(eventDescriptor);
    }

    public void handleEvent(String eventName) {
        Iterator iterator = this.eventObserverDescriptors.iterator();
        while (iterator.hasNext()) {
            EventDescriptor eventDescriptor = (EventDescriptor) iterator.next();
            if (eventDescriptor.getEventName().equals(eventName)) {
                String property = eventDescriptor.getProperty();
                String value = eventDescriptor.getValue();
                this.setProperty(property, value);
            }
        }
    }

    /**
	 * Abstract methods to be implemented by
	 * subclasses
	 **/
    public abstract void setup(Container container);

    public abstract String validateResult();

    public abstract String getResult();

    /**
	 * Callback that is called when the visibility
	 * of the display element changes
	 **/
    public void handleVisibilityUpdate() {
    }

    protected class EventDescriptor {

        public static final int NO = 0;

        public static final int EQ = 1;

        public static final int LT = 2;

        public static final int GT = 3;

        private String eventName;

        private String property;

        private int operator;

        private String value;

        public EventDescriptor(String eventName, String property, String operator, String value) {
            this.eventName = eventName;
            this.property = property;
            this.value = value;
            if (operator.equals("EQ")) {
                this.operator = EQ;
            } else if (operator.equals("LT")) {
                this.operator = LT;
            } else if (operator.equals("GT")) {
                this.operator = GT;
            } else {
                this.operator = NO;
            }
        }

        public String getEventName() {
            return this.eventName;
        }

        public String getProperty() {
            return this.property;
        }

        public int getOperator() {
            return this.operator;
        }

        public String getValue() {
            return this.value;
        }
    }
}
