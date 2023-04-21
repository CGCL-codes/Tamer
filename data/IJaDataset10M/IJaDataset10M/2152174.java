package org.mitre.caasd.aixm.gml;

public class TopoCurveType extends org.mitre.caasd.aixm.gml.AbstractTopologyType {

    public static org.mitre.caasd.aixm.xml.meta.ComplexType getStaticInfo() {
        return new org.mitre.caasd.aixm.xml.meta.ComplexType(org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getTypes()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_ti_gml_altova_TopoCurveType]);
    }

    public TopoCurveType(org.w3c.dom.Node init) {
        super(init);
        instantiateMembers();
    }

    private void instantiateMembers() {
        aggregationType2 = new MemberAttribute_aggregationType2(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gml_altova_TopoCurveType._aggregationType2]);
        directedEdge = new MemberElement_directedEdge(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gml_altova_TopoCurveType._directedEdge]);
    }

    public MemberAttribute_aggregationType2 aggregationType2;

    public static class MemberAttribute_aggregationType2 {

        private org.mitre.caasd.aixm.xml.TypeBase owner;

        private org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberAttribute_aggregationType2(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
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

    public MemberElement_directedEdge directedEdge;

    public static class MemberElement_directedEdge {

        public static class MemberElement_directedEdge_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_directedEdge member;

            public MemberElement_directedEdge_Iterator(MemberElement_directedEdge member) {
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
                org.mitre.caasd.aixm.gml.DirectedEdgePropertyType nx = new org.mitre.caasd.aixm.gml.DirectedEdgePropertyType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_directedEdge(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.gml.DirectedEdgePropertyType at(int index) {
            return new org.mitre.caasd.aixm.gml.DirectedEdgePropertyType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.gml.DirectedEdgePropertyType first() {
            return new org.mitre.caasd.aixm.gml.DirectedEdgePropertyType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.gml.DirectedEdgePropertyType last() {
            return new org.mitre.caasd.aixm.gml.DirectedEdgePropertyType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.gml.DirectedEdgePropertyType append() {
            return new org.mitre.caasd.aixm.gml.DirectedEdgePropertyType(owner.createElement(info));
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
            return new MemberElement_directedEdge_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public void setXsiType() {
        org.mitre.caasd.aixm.xml.XmlTreeOperations.setAttribute(getNode(), "http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "http://www.opengis.net/gml/3.2", "TopoCurveType");
    }
}
