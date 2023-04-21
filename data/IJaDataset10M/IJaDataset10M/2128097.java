package dtesp.Config;

import org.w3c.dom.Element;

/**
 * <pre>
 * Data structure to save a configuration of a event
 * fields
 *   name- name of the event
 *   field- name of the field to be used in esper
 */
public class EventItem {

    public EventItem(Element e) {
        name = e.getAttribute("name");
        field = e.getAttribute("field");
    }

    public EventItem(String name_, String field_) {
        name = name_;
        field = field_;
    }

    public String name;

    public String field;
}

;
