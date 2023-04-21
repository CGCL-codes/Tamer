package org.mitre.caasd.aixm.gml;

public class MultiGeometryType extends org.mitre.caasd.aixm.gml.AbstractGeometricAggregateType {

    public static org.mitre.caasd.aixm.xml.meta.ComplexType getStaticInfo() {
        return new org.mitre.caasd.aixm.xml.meta.ComplexType(org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getTypes()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_ti_gml_altova_MultiGeometryType]);
    }

    public MultiGeometryType(org.w3c.dom.Node init) {
        super(init);
        instantiateMembers();
    }

    private void instantiateMembers() {
        geometryMember = new MemberElement_geometryMember(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gml_altova_MultiGeometryType._geometryMember]);
        geometryMembers = new MemberElement_geometryMembers(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gml_altova_MultiGeometryType._geometryMembers]);
    }

    public MemberElement_geometryMember geometryMember;

    public static class MemberElement_geometryMember {

        public static class MemberElement_geometryMember_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_geometryMember member;

            public MemberElement_geometryMember_Iterator(MemberElement_geometryMember member) {
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
                org.mitre.caasd.aixm.gml.GeometryPropertyType nx = new org.mitre.caasd.aixm.gml.GeometryPropertyType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_geometryMember(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.gml.GeometryPropertyType at(int index) {
            return new org.mitre.caasd.aixm.gml.GeometryPropertyType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.gml.GeometryPropertyType first() {
            return new org.mitre.caasd.aixm.gml.GeometryPropertyType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.gml.GeometryPropertyType last() {
            return new org.mitre.caasd.aixm.gml.GeometryPropertyType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.gml.GeometryPropertyType append() {
            return new org.mitre.caasd.aixm.gml.GeometryPropertyType(owner.createElement(info));
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
            return new MemberElement_geometryMember_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_geometryMembers geometryMembers;

    public static class MemberElement_geometryMembers {

        public static class MemberElement_geometryMembers_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_geometryMembers member;

            public MemberElement_geometryMembers_Iterator(MemberElement_geometryMembers member) {
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
                org.mitre.caasd.aixm.gml.GeometryArrayPropertyType nx = new org.mitre.caasd.aixm.gml.GeometryArrayPropertyType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_geometryMembers(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.gml.GeometryArrayPropertyType at(int index) {
            return new org.mitre.caasd.aixm.gml.GeometryArrayPropertyType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.gml.GeometryArrayPropertyType first() {
            return new org.mitre.caasd.aixm.gml.GeometryArrayPropertyType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.gml.GeometryArrayPropertyType last() {
            return new org.mitre.caasd.aixm.gml.GeometryArrayPropertyType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.gml.GeometryArrayPropertyType append() {
            return new org.mitre.caasd.aixm.gml.GeometryArrayPropertyType(owner.createElement(info));
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
            return new MemberElement_geometryMembers_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public void setXsiType() {
        org.mitre.caasd.aixm.xml.XmlTreeOperations.setAttribute(getNode(), "http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "http://www.opengis.net/gml/3.2", "MultiGeometryType");
    }
}
