package org.ssa4j.mock.schema;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlType(name = "scriptType", namespace = "http://schemas.mobuser.com/ssa4jmock")
public class MockScript {

    @XmlAttribute
    public String lang;

    @XmlValue
    public String script;
}
