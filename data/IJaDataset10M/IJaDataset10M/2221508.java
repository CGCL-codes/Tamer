package org.mitre.caasd.aixm.gml;

public class EllipsoidType extends org.mitre.caasd.aixm.gml.IdentifiedObjectType {

    public static org.mitre.caasd.aixm.xml.meta.ComplexType getStaticInfo() {
        return new org.mitre.caasd.aixm.xml.meta.ComplexType(org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getTypes()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_ti_gml_altova_EllipsoidType]);
    }

    public EllipsoidType(org.w3c.dom.Node init) {
        super(init);
        instantiateMembers();
    }

    private void instantiateMembers() {
        semiMajorAxis = new MemberElement_semiMajorAxis(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gml_altova_EllipsoidType._semiMajorAxis]);
        secondDefiningParameter = new MemberElement_secondDefiningParameter(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gml_altova_EllipsoidType._secondDefiningParameter]);
    }

    public MemberElement_semiMajorAxis semiMajorAxis;

    public static class MemberElement_semiMajorAxis {

        public static class MemberElement_semiMajorAxis_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_semiMajorAxis member;

            public MemberElement_semiMajorAxis_Iterator(MemberElement_semiMajorAxis member) {
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
                org.mitre.caasd.aixm.gml.MeasureType nx = new org.mitre.caasd.aixm.gml.MeasureType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_semiMajorAxis(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.gml.MeasureType at(int index) {
            return new org.mitre.caasd.aixm.gml.MeasureType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.gml.MeasureType first() {
            return new org.mitre.caasd.aixm.gml.MeasureType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.gml.MeasureType last() {
            return new org.mitre.caasd.aixm.gml.MeasureType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.gml.MeasureType append() {
            return new org.mitre.caasd.aixm.gml.MeasureType(owner.createElement(info));
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
            return new MemberElement_semiMajorAxis_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_secondDefiningParameter secondDefiningParameter;

    public static class MemberElement_secondDefiningParameter {

        public static class MemberElement_secondDefiningParameter_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_secondDefiningParameter member;

            public MemberElement_secondDefiningParameter_Iterator(MemberElement_secondDefiningParameter member) {
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
                org.mitre.caasd.aixm.proc.secondDefiningParameterType2 nx = new org.mitre.caasd.aixm.proc.secondDefiningParameterType2(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_secondDefiningParameter(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.proc.secondDefiningParameterType2 at(int index) {
            return new org.mitre.caasd.aixm.proc.secondDefiningParameterType2(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.proc.secondDefiningParameterType2 first() {
            return new org.mitre.caasd.aixm.proc.secondDefiningParameterType2(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.proc.secondDefiningParameterType2 last() {
            return new org.mitre.caasd.aixm.proc.secondDefiningParameterType2(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.proc.secondDefiningParameterType2 append() {
            return new org.mitre.caasd.aixm.proc.secondDefiningParameterType2(owner.createElement(info));
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
            return new MemberElement_secondDefiningParameter_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public void setXsiType() {
        org.mitre.caasd.aixm.xml.XmlTreeOperations.setAttribute(getNode(), "http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "http://www.opengis.net/gml/3.2", "EllipsoidType");
    }
}
