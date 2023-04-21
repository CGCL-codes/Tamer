package com.c2b2.ipoint.presentation;

import com.c2b2.ipoint.model.Portlet;
import java.io.Serializable;

/**
 * This class represents a Portal Event
 * <p>
 * iPoint Portal
 * Copyright 2007 C2B2 Consulting Limited. All rights reserved.
 * </p>
 */
public class PortletEvent {

    /**
   * Constructor
   * @param name The Name of the Event
   * @param payload The payload of the event
   * @param raisedBy The Portlet which raised the event
   */
    public PortletEvent(String name, Serializable payload, Portlet raisedBy) {
        myName = name;
        myPayload = payload;
        myPortlet = raisedBy;
    }

    private String myName;

    private Serializable myPayload;

    private Portlet myPortlet;

    public void setName(String name) {
        this.myName = name;
    }

    public String getName() {
        return myName;
    }

    public void setPayload(Serializable payload) {
        this.myPayload = payload;
    }

    public Serializable getPayload() {
        return myPayload;
    }
}
