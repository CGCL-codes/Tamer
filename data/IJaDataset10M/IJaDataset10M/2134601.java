package net.sourceforge.NetProcessor.Core;

import java.*;
import java.lang.*;
import javax.xml.bind.annotation.*;

@XmlType(name = "Property")
public class PropertyInfo {

    public PropertyInfo() {
    }

    @XmlAttribute(name = "Name")
    public String getName() {
        return mName;
    }

    public void setName(String value) {
        mName = (value != null) ? value : "";
    }

    @XmlValue
    @XmlInlineBinaryData
    public byte[] getValue() {
        return mValue;
    }

    public void setValue(byte[] value) {
        mValue = (value != null) ? value : new byte[0];
    }

    private String mName = "";

    private byte[] mValue = new byte[0];
}
