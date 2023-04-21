package org.mitre.caasd.aixm.aixm;

public class CodeNDBUsageTypeType extends org.mitre.caasd.aixm.xml.TypeBase {

    public static org.mitre.caasd.aixm.xml.meta.ComplexType getStaticInfo() {
        return new org.mitre.caasd.aixm.xml.meta.ComplexType(org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getTypes()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_ti_aixm_altova_CodeNDBUsageTypeType]);
    }

    public CodeNDBUsageTypeType(org.w3c.dom.Node init) {
        super(init);
        instantiateMembers();
    }

    private void instantiateMembers() {
    }

    public String getValue() {
        org.mitre.caasd.aixm.typeinfo.MemberInfo member = org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_CodeNDBUsageTypeType._unnamed];
        return (String) org.mitre.caasd.aixm.xml.XmlTreeOperations.castToString(getNode(), member);
    }

    public void setValue(String value) {
        org.mitre.caasd.aixm.typeinfo.MemberInfo member = org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_CodeNDBUsageTypeType._unnamed];
        org.mitre.caasd.aixm.xml.XmlTreeOperations.setValue(getNode(), member, value);
    }

    public void setXsiType() {
        org.mitre.caasd.aixm.xml.XmlTreeOperations.setAttribute(getNode(), "http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "http://www.aixm.aero/schema/5.0", "CodeNDBUsageType");
    }
}
