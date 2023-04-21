package org.mitre.caasd.aixm.gml;

public class VerticalCRSType extends org.mitre.caasd.aixm.gml.AbstractCRSType {

    public static org.mitre.caasd.aixm.xml.meta.ComplexType getStaticInfo() {
        return new org.mitre.caasd.aixm.xml.meta.ComplexType(org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getTypes()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_ti_gml_altova_VerticalCRSType]);
    }

    public VerticalCRSType(org.w3c.dom.Node init) {
        super(init);
        instantiateMembers();
    }

    private void instantiateMembers() {
        verticalCS = new MemberElement_verticalCS(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gml_altova_VerticalCRSType._verticalCS]);
        usesVerticalCS = new MemberElement_usesVerticalCS(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gml_altova_VerticalCRSType._usesVerticalCS]);
        verticalDatum = new MemberElement_verticalDatum(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gml_altova_VerticalCRSType._verticalDatum]);
        usesVerticalDatum = new MemberElement_usesVerticalDatum(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gml_altova_VerticalCRSType._usesVerticalDatum]);
    }

    public MemberElement_verticalCS verticalCS;

    public static class MemberElement_verticalCS {

        public static class MemberElement_verticalCS_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_verticalCS member;

            public MemberElement_verticalCS_Iterator(MemberElement_verticalCS member) {
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
                org.mitre.caasd.aixm.gml.VerticalCSPropertyType nx = new org.mitre.caasd.aixm.gml.VerticalCSPropertyType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_verticalCS(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.gml.VerticalCSPropertyType at(int index) {
            return new org.mitre.caasd.aixm.gml.VerticalCSPropertyType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.gml.VerticalCSPropertyType first() {
            return new org.mitre.caasd.aixm.gml.VerticalCSPropertyType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.gml.VerticalCSPropertyType last() {
            return new org.mitre.caasd.aixm.gml.VerticalCSPropertyType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.gml.VerticalCSPropertyType append() {
            return new org.mitre.caasd.aixm.gml.VerticalCSPropertyType(owner.createElement(info));
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
            return new MemberElement_verticalCS_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_usesVerticalCS usesVerticalCS;

    public static class MemberElement_usesVerticalCS {

        public static class MemberElement_usesVerticalCS_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_usesVerticalCS member;

            public MemberElement_usesVerticalCS_Iterator(MemberElement_usesVerticalCS member) {
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
                org.mitre.caasd.aixm.gml.VerticalCSPropertyType nx = new org.mitre.caasd.aixm.gml.VerticalCSPropertyType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_usesVerticalCS(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.gml.VerticalCSPropertyType at(int index) {
            return new org.mitre.caasd.aixm.gml.VerticalCSPropertyType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.gml.VerticalCSPropertyType first() {
            return new org.mitre.caasd.aixm.gml.VerticalCSPropertyType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.gml.VerticalCSPropertyType last() {
            return new org.mitre.caasd.aixm.gml.VerticalCSPropertyType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.gml.VerticalCSPropertyType append() {
            return new org.mitre.caasd.aixm.gml.VerticalCSPropertyType(owner.createElement(info));
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
            return new MemberElement_usesVerticalCS_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_verticalDatum verticalDatum;

    public static class MemberElement_verticalDatum {

        public static class MemberElement_verticalDatum_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_verticalDatum member;

            public MemberElement_verticalDatum_Iterator(MemberElement_verticalDatum member) {
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
                org.mitre.caasd.aixm.gml.VerticalDatumPropertyType nx = new org.mitre.caasd.aixm.gml.VerticalDatumPropertyType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_verticalDatum(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.gml.VerticalDatumPropertyType at(int index) {
            return new org.mitre.caasd.aixm.gml.VerticalDatumPropertyType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.gml.VerticalDatumPropertyType first() {
            return new org.mitre.caasd.aixm.gml.VerticalDatumPropertyType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.gml.VerticalDatumPropertyType last() {
            return new org.mitre.caasd.aixm.gml.VerticalDatumPropertyType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.gml.VerticalDatumPropertyType append() {
            return new org.mitre.caasd.aixm.gml.VerticalDatumPropertyType(owner.createElement(info));
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
            return new MemberElement_verticalDatum_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_usesVerticalDatum usesVerticalDatum;

    public static class MemberElement_usesVerticalDatum {

        public static class MemberElement_usesVerticalDatum_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_usesVerticalDatum member;

            public MemberElement_usesVerticalDatum_Iterator(MemberElement_usesVerticalDatum member) {
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
                org.mitre.caasd.aixm.gml.VerticalDatumPropertyType nx = new org.mitre.caasd.aixm.gml.VerticalDatumPropertyType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_usesVerticalDatum(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.gml.VerticalDatumPropertyType at(int index) {
            return new org.mitre.caasd.aixm.gml.VerticalDatumPropertyType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.gml.VerticalDatumPropertyType first() {
            return new org.mitre.caasd.aixm.gml.VerticalDatumPropertyType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.gml.VerticalDatumPropertyType last() {
            return new org.mitre.caasd.aixm.gml.VerticalDatumPropertyType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.gml.VerticalDatumPropertyType append() {
            return new org.mitre.caasd.aixm.gml.VerticalDatumPropertyType(owner.createElement(info));
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
            return new MemberElement_usesVerticalDatum_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public void setXsiType() {
        org.mitre.caasd.aixm.xml.XmlTreeOperations.setAttribute(getNode(), "http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "http://www.opengis.net/gml/3.2", "VerticalCRSType");
    }
}
