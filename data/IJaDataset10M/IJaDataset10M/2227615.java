package gui;

import entities.Event;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author IoanaC
 */
public class CellValue {

    private String value;

    private boolean selected = false;

    private Set<Event> events = new HashSet<Event>();

    public Set<Event> getEvent() {
        return events;
    }

    public void setEvent(Set<Event> events) {
        this.events = events;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
