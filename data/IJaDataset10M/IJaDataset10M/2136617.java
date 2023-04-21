package org.ssa4j.mock.schema;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlType(name = "fieldType", namespace = "http://schemas.mobuser.com/ssa4jmock")
public class MockDataRecordField {

    @XmlAttribute
    public String name;

    @XmlValue
    public String value;
}
