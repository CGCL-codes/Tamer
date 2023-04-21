package org.openrtk.idl.epp02;

public interface epp_ActionOperations {

    String toXML() throws org.openrtk.idl.epp02.epp_Exception, org.openrtk.idl.epp02.epp_XMLException;

    void fromXML(String xml) throws org.openrtk.idl.epp02.epp_Exception, org.openrtk.idl.epp02.epp_XMLException;
}
