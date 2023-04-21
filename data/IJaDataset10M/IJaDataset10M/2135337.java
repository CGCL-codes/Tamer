package org.geonetwork.domain.ebrim.test.utilities.csw202.discovery;

import org.geonetwork.domain.csw202.discovery.DescribeRecord;
import java.util.List;
import java.util.ArrayList;

public class DescribeRecordFactory {

    public static synchronized DescribeRecord create() {
        DescribeRecord dr = new DescribeRecord();
        List<String> namespace = new ArrayList<String>();
        namespace.add("tns=urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0");
        namespace.add("csw=http://www.opengis.net/cat/csw/2.0.2");
        dr.setNamespace(namespace);
        dr.setOutputFormat("application/xml");
        dr.setSchemaLanguage("http://www.w3.org/2001/XMLSchema");
        dr.setService("CSW");
        dr.setVersion("2.0.2");
        List<String> typeNames = new ArrayList<String>();
        typeNames.add("csw:Record");
        dr.setTypeName(typeNames);
        return dr;
    }
}
