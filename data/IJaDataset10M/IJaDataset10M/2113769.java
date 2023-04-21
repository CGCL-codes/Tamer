package org.mitre.caasd.aixm.gmd;

public class DQ_ConceptualConsistency_Type extends org.mitre.caasd.aixm.gmd.AbstractDQ_LogicalConsistency_Type {

    public static org.mitre.caasd.aixm.xml.meta.ComplexType getStaticInfo() {
        return new org.mitre.caasd.aixm.xml.meta.ComplexType(org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getTypes()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_ti_gmd_altova_DQ_ConceptualConsistency_Type]);
    }

    public DQ_ConceptualConsistency_Type(org.w3c.dom.Node init) {
        super(init);
        instantiateMembers();
    }

    private void instantiateMembers() {
    }

    public void setXsiType() {
        org.mitre.caasd.aixm.xml.XmlTreeOperations.setAttribute(getNode(), "http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "http://www.isotc211.org/2005/gmd", "DQ_ConceptualConsistency_Type");
    }
}
