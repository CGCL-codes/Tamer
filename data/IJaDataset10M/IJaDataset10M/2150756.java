package net.sf.mpxj.mspdi.schema;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import net.sf.mpxj.WorkGroup;

@SuppressWarnings("all")
public class Adapter12 extends XmlAdapter<String, WorkGroup> {

    public WorkGroup unmarshal(String value) {
        return (net.sf.mpxj.mspdi.DatatypeConverter.parseWorkGroup(value));
    }

    public String marshal(WorkGroup value) {
        return (net.sf.mpxj.mspdi.DatatypeConverter.printWorkGroup(value));
    }
}
