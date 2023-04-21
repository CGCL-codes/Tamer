package org.gwtplanning.client.model;

import java.io.Serializable;
import java.util.List;

/**
 * DOC stephane class global comment. Detailled comment <br/>
 */
public interface Category extends Serializable {

    public List<Event> getEvents();
}
