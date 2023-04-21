package com.global360.sketchpadbpmn.documents;

import java.util.ArrayList;
import com.global360.sketchpadbpmn.Utility;
import com.global360.sketchpadbpmn.documents.xpdl.elements.MessageElement;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class BPMNMessage implements Cloneable {

    private String name = null;

    private ArrayList<BPMNProperty> properties = null;

    String fromName = null;

    String toName = null;

    public static BPMNMessage make(MessageElement messageElement) {
        BPMNMessage result = null;
        if ((messageElement != null) && (messageElement.getElement().getName().equalsIgnoreCase(XPDLConstants.S_MESSAGE))) {
            result = new BPMNMessage();
            result.setName(messageElement.getName());
            result.setFromName(messageElement.getFrom());
            result.setToName(messageElement.getTo());
            ArrayList<BPMNProperty> bpmnProperties = BPMNProperty.makeList(messageElement.getProperties());
            result.setProperties(bpmnProperties);
        }
        return result;
    }

    public BPMNMessage() {
    }

    public BPMNMessage(BPMNMessage source) {
        this();
        if (source != null) {
            this.setFromName(source.getFromName());
            this.setName(source.getName());
            this.setToName(source.getToName());
            this.setProperties(source.getProperties());
        }
    }

    protected BPMNMessage clone() throws CloneNotSupportedException {
        BPMNMessage clone = new BPMNMessage(this);
        return clone;
    }

    public boolean equals(Object other) {
        if (other != null) {
            if (other instanceof BPMNMessage) {
                return equals((BPMNMessage) other);
            }
        }
        return false;
    }

    public boolean equals(BPMNMessage other) {
        boolean result = true;
        if (other == null) {
            return false;
        }
        result = result && Utility.areEqual(this.fromName, other.fromName);
        result = result && Utility.areEqual(this.name, other.name);
        result = result && Utility.areEqual(this.toName, other.toName);
        result = result && Utility.areEqual(this.properties, other.properties);
        return result;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setFromName(String from) {
        this.fromName = from;
    }

    public String getFromName() {
        return this.fromName;
    }

    public void setToName(String to) {
        this.toName = to;
    }

    public String getToName() {
        return this.toName;
    }

    public void setProperties(ArrayList<BPMNProperty> properties) {
        this.properties = properties;
    }

    public void addProperty(BPMNProperty property) {
        this.properties.add(property);
    }

    public void removeProperty(BPMNProperty property) {
        this.properties.remove(property);
    }

    /**
   * returns an array list of BPMNProperty object.
   *
   * @return ArrayList
   */
    public ArrayList<BPMNProperty> getProperties() {
        return this.properties;
    }
}
