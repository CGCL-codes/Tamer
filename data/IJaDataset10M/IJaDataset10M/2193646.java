package org.mitre.caasd.aixm.aixm;

public class MarkingElementPropertyType extends org.mitre.caasd.aixm.xml.TypeBase {

    public static org.mitre.caasd.aixm.xml.meta.ComplexType getStaticInfo() {
        return new org.mitre.caasd.aixm.xml.meta.ComplexType(org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getTypes()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_ti_aixm_altova_MarkingElementPropertyType]);
    }

    public MarkingElementPropertyType(org.w3c.dom.Node init) {
        super(init);
        instantiateMembers();
    }

    private void instantiateMembers() {
        owns = new MemberAttribute_owns(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_MarkingElementPropertyType._owns]);
        MarkingElement = new MemberElement_MarkingElement(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_MarkingElementPropertyType._MarkingElement]);
    }

    public MemberAttribute_owns owns;

    public static class MemberAttribute_owns {

        private org.mitre.caasd.aixm.xml.TypeBase owner;

        private org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberAttribute_owns(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public boolean getValue() {
            return (boolean) org.mitre.caasd.aixm.xml.XmlTreeOperations.castToBool(org.mitre.caasd.aixm.xml.XmlTreeOperations.findAttribute(owner.getNode(), info), info);
        }

        public void setValue(boolean value) {
            org.mitre.caasd.aixm.xml.XmlTreeOperations.setValue(owner.getNode(), info, value);
        }

        public boolean exists() {
            return owner.getAttribute(info) != null;
        }

        public void remove() {
            owner.removeAttribute(info);
        }

        public org.mitre.caasd.aixm.xml.meta.Attribute getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Attribute(info);
        }
    }

    public MemberElement_MarkingElement MarkingElement;

    public static class MemberElement_MarkingElement {

        public static class MemberElement_MarkingElement_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_MarkingElement member;

            public MemberElement_MarkingElement_Iterator(MemberElement_MarkingElement member) {
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
                org.mitre.caasd.aixm.aixm.MarkingElementType nx = new org.mitre.caasd.aixm.aixm.MarkingElementType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_MarkingElement(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.MarkingElementType at(int index) {
            return new org.mitre.caasd.aixm.aixm.MarkingElementType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.MarkingElementType first() {
            return new org.mitre.caasd.aixm.aixm.MarkingElementType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.MarkingElementType last() {
            return new org.mitre.caasd.aixm.aixm.MarkingElementType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.MarkingElementType append() {
            return new org.mitre.caasd.aixm.aixm.MarkingElementType(owner.createElement(info));
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
            return new MemberElement_MarkingElement_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public void setXsiType() {
        org.mitre.caasd.aixm.xml.XmlTreeOperations.setAttribute(getNode(), "http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "http://www.aixm.aero/schema/5.0", "MarkingElementPropertyType");
    }
}
