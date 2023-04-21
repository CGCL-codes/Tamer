package org.jlense.zone.j2ee;

import org.jlense.zone.*;
import java.io.*;
import java.util.*;

/**
 * An implementation of the org.jlense.ZoneQuery interface.
 * This implementation merely servers as a container for the query string and 
 * bind parameters.  The query is not parsed or processed until it is 
 * sent to a ZoneService.
 * 
 * @author	ted stockwell [emorning@yahoo.com]
 */
public class ZoneServiceQuery implements ZoneQuery, Serializable {

    private String _queryString = null;

    private List _parameters = new ArrayList();

    public void create(String query) {
        _queryString = query;
    }

    public void bind(Object parameter) {
        _parameters.add(parameter);
    }

    public String getQueryString() {
        return _queryString;
    }

    public List getParameters() {
        return _parameters;
    }
}
