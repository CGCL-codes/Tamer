package org.mitre.caasd.aixm.gco;

public class Length_PropertyType extends org.mitre.caasd.aixm.xml.TypeBase {

    public static org.mitre.caasd.aixm.xml.meta.ComplexType getStaticInfo() {
        return new org.mitre.caasd.aixm.xml.meta.ComplexType(org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getTypes()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_ti_gco_altova_Length_PropertyType]);
    }

    public Length_PropertyType(org.w3c.dom.Node init) {
        super(init);
        instantiateMembers();
    }

    private void instantiateMembers() {
        nilReason = new MemberAttribute_nilReason(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gco_altova_Length_PropertyType._nilReason]);
        Length = new MemberElement_Length(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gco_altova_Length_PropertyType._Length]);
        Distance = new MemberElement_Distance(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gco_altova_Length_PropertyType._Distance]);
    }

    public MemberAttribute_nilReason nilReason;

    public static class MemberAttribute_nilReason {

        private org.mitre.caasd.aixm.xml.TypeBase owner;

        private org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberAttribute_nilReason(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public String getValue() {
            return (String) org.mitre.caasd.aixm.xml.XmlTreeOperations.castToString(org.mitre.caasd.aixm.xml.XmlTreeOperations.findAttribute(owner.getNode(), info), info);
        }

        public void setValue(String value) {
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

    public MemberElement_Length Length;

    public static class MemberElement_Length {

        public static class MemberElement_Length_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_Length member;

            public MemberElement_Length_Iterator(MemberElement_Length member) {
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
                org.mitre.caasd.aixm.gml.LengthType nx = new org.mitre.caasd.aixm.gml.LengthType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_Length(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.gml.LengthType at(int index) {
            return new org.mitre.caasd.aixm.gml.LengthType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.gml.LengthType first() {
            return new org.mitre.caasd.aixm.gml.LengthType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.gml.LengthType last() {
            return new org.mitre.caasd.aixm.gml.LengthType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.gml.LengthType append() {
            return new org.mitre.caasd.aixm.gml.LengthType(owner.createElement(info));
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
            return new MemberElement_Length_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_Distance Distance;

    public static class MemberElement_Distance {

        public static class MemberElement_Distance_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_Distance member;

            public MemberElement_Distance_Iterator(MemberElement_Distance member) {
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
                org.mitre.caasd.aixm.gml.LengthType nx = new org.mitre.caasd.aixm.gml.LengthType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_Distance(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.gml.LengthType at(int index) {
            return new org.mitre.caasd.aixm.gml.LengthType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.gml.LengthType first() {
            return new org.mitre.caasd.aixm.gml.LengthType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.gml.LengthType last() {
            return new org.mitre.caasd.aixm.gml.LengthType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.gml.LengthType append() {
            return new org.mitre.caasd.aixm.gml.LengthType(owner.createElement(info));
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
            return new MemberElement_Distance_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public void setXsiType() {
        org.mitre.caasd.aixm.xml.XmlTreeOperations.setAttribute(getNode(), "http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "http://www.isotc211.org/2005/gco", "Length_PropertyType");
    }
}
