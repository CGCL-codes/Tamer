package org.mitre.caasd.aixm.xml;

import org.mitre.caasd.aixm.CoreTypes;
import org.mitre.caasd.aixm.types.*;
import org.mitre.caasd.aixm.typeinfo.ValueFormatter;

public class XmlTimeFormatter extends XmlFormatter {

    public String format(DateTime dt) {
        return dt.toTimeString("0.0######");
    }
}
