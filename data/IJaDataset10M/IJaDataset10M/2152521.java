package org.mitre.caasd.aixm.aixm;

public class PrimarySurveillanceRadarType extends org.mitre.caasd.aixm.aixm.AbstractSurveillanceRadarType {

    public static org.mitre.caasd.aixm.xml.meta.ComplexType getStaticInfo() {
        return new org.mitre.caasd.aixm.xml.meta.ComplexType(org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getTypes()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_ti_aixm_altova_PrimarySurveillanceRadarType]);
    }

    public PrimarySurveillanceRadarType(org.w3c.dom.Node init) {
        super(init);
        instantiateMembers();
    }

    private void instantiateMembers() {
        timeSlice = new MemberElement_timeSlice(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_PrimarySurveillanceRadarType._timeSlice]);
    }

    public MemberElement_timeSlice timeSlice;

    public static class MemberElement_timeSlice {

        public static class MemberElement_timeSlice_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_timeSlice member;

            public MemberElement_timeSlice_Iterator(MemberElement_timeSlice member) {
                this.member = member;
                nextNode = member.owner.getElementFirst(member.info);
            }

            public boolean hasNext() {
                while (nextNode != null) {
                    if (org.mitre.caasd.aixm.xml.TypeBase.memberEqualsNode(member.info, nextNode)) return true;
                    nextNode = nextNode.getNextSibling();
                }
                return false;
            }

            public Object next() {
                org.mitre.caasd.aixm.aixm.PrimarySurveillanceRadarTimeSlicePropertyType nx = new org.mitre.caasd.aixm.aixm.PrimarySurveillanceRadarTimeSlicePropertyType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_timeSlice(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.PrimarySurveillanceRadarTimeSlicePropertyType at(int index) {
            return new org.mitre.caasd.aixm.aixm.PrimarySurveillanceRadarTimeSlicePropertyType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.PrimarySurveillanceRadarTimeSlicePropertyType first() {
            return new org.mitre.caasd.aixm.aixm.PrimarySurveillanceRadarTimeSlicePropertyType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.PrimarySurveillanceRadarTimeSlicePropertyType last() {
            return new org.mitre.caasd.aixm.aixm.PrimarySurveillanceRadarTimeSlicePropertyType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.PrimarySurveillanceRadarTimeSlicePropertyType append() {
            return new org.mitre.caasd.aixm.aixm.PrimarySurveillanceRadarTimeSlicePropertyType(owner.createElement(info));
        }

        public boolean exists() {
            return count() > 0;
        }

        public int count() {
            return owner.countElement(info);
        }

        public void remove() {
            owner.removeElement(info);
        }

        public void removeAt(int index) {
            owner.removeElementAt(info, index);
        }

        public java.util.Iterator iterator() {
            return new MemberElement_timeSlice_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public void setXsiType() {
        org.mitre.caasd.aixm.xml.XmlTreeOperations.setAttribute(getNode(), "http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "http://www.aixm.aero/schema/5.0", "PrimarySurveillanceRadarType");
    }
}
