package org.dhcpcluster.config.xml.data;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class Adapter2 extends XmlAdapter<String, Byte> {

    public Byte unmarshal(String value) {
        return ((byte) javax.xml.bind.DatatypeConverter.parseShort(value));
    }

    public String marshal(Byte value) {
        if (value == null) {
            return null;
        }
        return (javax.xml.bind.DatatypeConverter.printShort((short) (byte) value));
    }
}
