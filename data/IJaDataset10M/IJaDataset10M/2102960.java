package org.aubit4gl.remote_client.connection.command;

import java.util.ArrayList;
import org.aubit4gl.remote_client.connection.ClientUICommand;
import org.aubit4gl.remote_client.connection.FGLUIException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * 4gl Prompt command information.
 * 
 * TODO : Implement the attributes
 * TODO : Implement the missing methods
 * TODO : Implement the events
 * 
 * @author S�rgio Ferreira
 */
public class DisplayArray implements ClientUICommand {

    private int context;

    private ArrayList<Attribute> attributes = new ArrayList<Attribute>();

    private int arrCount;

    private ArrayList<String> fieldList = new ArrayList<String>();

    private ArrayList<Event> events = new ArrayList<Event>();

    public DisplayArray() {
        super();
    }

    public void execute() {
    }

    public String getXml() {
        return null;
    }

    /**
	 * TODO : Implement it
	 */
    public String toString() {
        return "";
    }

    /**
	 * Initialize the UI command from the corresponding element on the
	 * DOM.
	 * 
	 * TODO : Implement the loading of the XML
	 * 
	 * @param dom The Document Object Model that contains the information
	 * @throws FGLUIException 
	 */
    public void initFromDom(Element dom) throws FGLUIException {
        context = Integer.parseInt(dom.getAttribute("CONTEXT"));
        arrCount = Integer.parseInt(dom.getAttribute("ARRCOUNT"));
        NodeList commandTags = dom.getElementsByTagName("FIELD");
        int commandCount = commandTags.getLength();
        for (int i = 0; i < commandCount; i++) {
            Element fieldTag = (Element) commandTags.item(i);
            fieldList.add(fieldTag.getAttribute("NAME"));
        }
    }

    public int getContext() {
        return context;
    }

    public void setContext(int context) {
        this.context = context;
    }

    public ArrayList<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(ArrayList<Attribute> attributes) {
        this.attributes = attributes;
    }

    public int getArrCount() {
        return arrCount;
    }

    public void setArrCount(int arrCount) {
        this.arrCount = arrCount;
    }

    public ArrayList<String> getFieldList() {
        return fieldList;
    }

    public void setFieldList(ArrayList<String> fieldList) {
        this.fieldList = fieldList;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }
}
