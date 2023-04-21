package org.mitre.caasd.aixm.gml;

public class GenericMetaDataType extends org.mitre.caasd.aixm.gml.AbstractMetaDataType {

    public static org.mitre.caasd.aixm.xml.meta.ComplexType getStaticInfo() {
        return new org.mitre.caasd.aixm.xml.meta.ComplexType(org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getTypes()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_ti_gml_altova_GenericMetaDataType]);
    }

    public GenericMetaDataType(org.w3c.dom.Node init) {
        super(init);
        instantiateMembers();
    }

    private void instantiateMembers() {
    }

    public void setXsiType() {
        org.mitre.caasd.aixm.xml.XmlTreeOperations.setAttribute(getNode(), "http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "http://www.opengis.net/gml/3.2", "GenericMetaDataType");
    }
}
