package org.itx.equipment.l0;

import java.sql.Timestamp;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class SqlTSAdapter extends XmlAdapter<String, Timestamp> {

    public Timestamp unmarshal(String val) throws Exception {
        return Timestamp.valueOf(val);
    }

    public String marshal(Timestamp val) throws Exception {
        return val.toString();
    }
}
