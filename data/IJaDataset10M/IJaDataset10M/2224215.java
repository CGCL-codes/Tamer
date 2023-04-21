package org.mitre.caasd.aixm.gml;

public class ArrayType extends org.mitre.caasd.aixm.gml.AbstractGMLType {

    public static org.mitre.caasd.aixm.xml.meta.ComplexType getStaticInfo() {
        return new org.mitre.caasd.aixm.xml.meta.ComplexType(org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getTypes()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_ti_gml_altova_ArrayType]);
    }

    public ArrayType(org.w3c.dom.Node init) {
        super(init);
        instantiateMembers();
    }

    private void instantiateMembers() {
        members = new MemberElement_members(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gml_altova_ArrayType._members]);
    }

    public MemberElement_members members;

    public static class MemberElement_members {

        public static class MemberElement_members_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_members member;

            public MemberElement_members_Iterator(MemberElement_members member) {
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
                org.mitre.caasd.aixm.gml.ArrayAssociationType nx = new org.mitre.caasd.aixm.gml.ArrayAssociationType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_members(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.gml.ArrayAssociationType at(int index) {
            return new org.mitre.caasd.aixm.gml.ArrayAssociationType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.gml.ArrayAssociationType first() {
            return new org.mitre.caasd.aixm.gml.ArrayAssociationType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.gml.ArrayAssociationType last() {
            return new org.mitre.caasd.aixm.gml.ArrayAssociationType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.gml.ArrayAssociationType append() {
            return new org.mitre.caasd.aixm.gml.ArrayAssociationType(owner.createElement(info));
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
            return new MemberElement_members_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public void setXsiType() {
        org.mitre.caasd.aixm.xml.XmlTreeOperations.setAttribute(getNode(), "http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "http://www.opengis.net/gml/3.2", "ArrayType");
    }
}
