package com.edxm10;

public class valueSchemeType extends com.altova.xml.TypeBase {

    public static com.altova.xml.meta.ComplexType getStaticInfo() {
        return new com.altova.xml.meta.ComplexType(com.edxm10.edxm10_TypeInfo.binder.getTypes()[com.edxm10.edxm10_TypeInfo._altova_ti_altova_valueSchemeType]);
    }

    public valueSchemeType(org.w3c.dom.Node init) {
        super(init);
        instantiateMembers();
    }

    private void instantiateMembers() {
        explicitAddressScheme = new MemberElement_explicitAddressScheme(this, com.edxm10.edxm10_TypeInfo.binder.getMembers()[com.edxm10.edxm10_TypeInfo._altova_mi_altova_valueSchemeType._explicitAddressScheme]);
        explicitAddressValue = new MemberElement_explicitAddressValue(this, com.edxm10.edxm10_TypeInfo.binder.getMembers()[com.edxm10.edxm10_TypeInfo._altova_mi_altova_valueSchemeType._explicitAddressValue]);
    }

    public MemberElement_explicitAddressScheme explicitAddressScheme;

    public static class MemberElement_explicitAddressScheme {

        public static class MemberElement_explicitAddressScheme_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_explicitAddressScheme member;

            public MemberElement_explicitAddressScheme_Iterator(MemberElement_explicitAddressScheme member) {
                this.member = member;
                nextNode = member.owner.getElementFirst(member.info);
            }

            public boolean hasNext() {
                while (nextNode != null) {
                    if (com.altova.xml.TypeBase.memberEqualsNode(member.info, nextNode)) return true;
                    nextNode = nextNode.getNextSibling();
                }
                return false;
            }

            public Object next() {
                com.edxm10.xs.stringType nx = new com.edxm10.xs.stringType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected com.altova.xml.TypeBase owner;

        protected com.altova.typeinfo.MemberInfo info;

        public MemberElement_explicitAddressScheme(com.altova.xml.TypeBase owner, com.altova.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public com.edxm10.xs.stringType at(int index) {
            return new com.edxm10.xs.stringType(owner.getElementAt(info, index));
        }

        public com.edxm10.xs.stringType first() {
            return new com.edxm10.xs.stringType(owner.getElementFirst(info));
        }

        public com.edxm10.xs.stringType last() {
            return new com.edxm10.xs.stringType(owner.getElementLast(info));
        }

        public com.edxm10.xs.stringType append() {
            return new com.edxm10.xs.stringType(owner.createElement(info));
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
            return new MemberElement_explicitAddressScheme_Iterator(this);
        }

        public com.altova.xml.meta.Element getInfo() {
            return new com.altova.xml.meta.Element(info);
        }
    }

    public MemberElement_explicitAddressValue explicitAddressValue;

    public static class MemberElement_explicitAddressValue {

        public static class MemberElement_explicitAddressValue_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_explicitAddressValue member;

            public MemberElement_explicitAddressValue_Iterator(MemberElement_explicitAddressValue member) {
                this.member = member;
                nextNode = member.owner.getElementFirst(member.info);
            }

            public boolean hasNext() {
                while (nextNode != null) {
                    if (com.altova.xml.TypeBase.memberEqualsNode(member.info, nextNode)) return true;
                    nextNode = nextNode.getNextSibling();
                }
                return false;
            }

            public Object next() {
                com.edxm10.xs.stringType nx = new com.edxm10.xs.stringType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected com.altova.xml.TypeBase owner;

        protected com.altova.typeinfo.MemberInfo info;

        public MemberElement_explicitAddressValue(com.altova.xml.TypeBase owner, com.altova.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public com.edxm10.xs.stringType at(int index) {
            return new com.edxm10.xs.stringType(owner.getElementAt(info, index));
        }

        public com.edxm10.xs.stringType first() {
            return new com.edxm10.xs.stringType(owner.getElementFirst(info));
        }

        public com.edxm10.xs.stringType last() {
            return new com.edxm10.xs.stringType(owner.getElementLast(info));
        }

        public com.edxm10.xs.stringType append() {
            return new com.edxm10.xs.stringType(owner.createElement(info));
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
            return new MemberElement_explicitAddressValue_Iterator(this);
        }

        public com.altova.xml.meta.Element getInfo() {
            return new com.altova.xml.meta.Element(info);
        }
    }

    public void setXsiType() {
        com.altova.xml.XmlTreeOperations.setAttribute(getNode(), "http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "urn:oasis:names:tc:emergency:EDXL:DE:1.0", "valueSchemeType");
    }
}
