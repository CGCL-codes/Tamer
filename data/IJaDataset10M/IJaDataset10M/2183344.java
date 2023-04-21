package org.mitre.caasd.aixm.gml;

public class ShellPropertyType extends org.mitre.caasd.aixm.xml.TypeBase {

    public static org.mitre.caasd.aixm.xml.meta.ComplexType getStaticInfo() {
        return new org.mitre.caasd.aixm.xml.meta.ComplexType(org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getTypes()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_ti_gml_altova_ShellPropertyType]);
    }

    public ShellPropertyType(org.w3c.dom.Node init) {
        super(init);
        instantiateMembers();
    }

    private void instantiateMembers() {
        Shell = new MemberElement_Shell(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gml_altova_ShellPropertyType._Shell]);
    }

    public MemberElement_Shell Shell;

    public static class MemberElement_Shell {

        public static class MemberElement_Shell_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_Shell member;

            public MemberElement_Shell_Iterator(MemberElement_Shell member) {
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
                org.mitre.caasd.aixm.gml.ShellType nx = new org.mitre.caasd.aixm.gml.ShellType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_Shell(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.gml.ShellType at(int index) {
            return new org.mitre.caasd.aixm.gml.ShellType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.gml.ShellType first() {
            return new org.mitre.caasd.aixm.gml.ShellType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.gml.ShellType last() {
            return new org.mitre.caasd.aixm.gml.ShellType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.gml.ShellType append() {
            return new org.mitre.caasd.aixm.gml.ShellType(owner.createElement(info));
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
            return new MemberElement_Shell_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public void setXsiType() {
        org.mitre.caasd.aixm.xml.XmlTreeOperations.setAttribute(getNode(), "http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "http://www.opengis.net/gml/3.2", "ShellPropertyType");
    }
}
