package org.jma.lib.soap.test;

import java.util.Date;
import java.util.HashMap;
import org.jma.lib.soap.utils.XMLSerializable;

public class TimeService implements XMLSerializable {

    Date t;

    public XMLSerializable getTime(HashMap params) {
        t = new Date();
        return this;
    }

    @Override
    public String serializeToXML() {
        return ("<dateTime><time>" + t.toString() + "</time>" + "<time_in_ms>" + t.getTime() + "</time_in_ms></dateTime>");
    }
}
