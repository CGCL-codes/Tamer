package com.cap12.cap;

public class resourceType extends com.altova.xml.TypeBase {

    public static com.altova.xml.meta.ComplexType getStaticInfo() {
        return new com.altova.xml.meta.ComplexType(com.cap12.cap12_TypeInfo.binder.getTypes()[com.cap12.cap12_TypeInfo._altova_ti_cap_altova_resourceType]);
    }

    public resourceType(org.w3c.dom.Node init) {
        super(init);
        instantiateMembers();
    }

    private void instantiateMembers() {
        resourceDesc = new MemberElement_resourceDesc(this, com.cap12.cap12_TypeInfo.binder.getMembers()[com.cap12.cap12_TypeInfo._altova_mi_cap_altova_resourceType._resourceDesc]);
        mimeType = new MemberElement_mimeType(this, com.cap12.cap12_TypeInfo.binder.getMembers()[com.cap12.cap12_TypeInfo._altova_mi_cap_altova_resourceType._mimeType]);
        size = new MemberElement_size(this, com.cap12.cap12_TypeInfo.binder.getMembers()[com.cap12.cap12_TypeInfo._altova_mi_cap_altova_resourceType._size]);
        uri = new MemberElement_uri(this, com.cap12.cap12_TypeInfo.binder.getMembers()[com.cap12.cap12_TypeInfo._altova_mi_cap_altova_resourceType._uri]);
        derefUri = new MemberElement_derefUri(this, com.cap12.cap12_TypeInfo.binder.getMembers()[com.cap12.cap12_TypeInfo._altova_mi_cap_altova_resourceType._derefUri]);
        digest = new MemberElement_digest(this, com.cap12.cap12_TypeInfo.binder.getMembers()[com.cap12.cap12_TypeInfo._altova_mi_cap_altova_resourceType._digest]);
    }

    public MemberElement_resourceDesc resourceDesc;

    public static class MemberElement_resourceDesc {

        public static class MemberElement_resourceDesc_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_resourceDesc member;

            public MemberElement_resourceDesc_Iterator(MemberElement_resourceDesc member) {
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
                com.cap12.xs.stringType nx = new com.cap12.xs.stringType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected com.altova.xml.TypeBase owner;

        protected com.altova.typeinfo.MemberInfo info;

        public MemberElement_resourceDesc(com.altova.xml.TypeBase owner, com.altova.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public com.cap12.xs.stringType at(int index) {
            return new com.cap12.xs.stringType(owner.getElementAt(info, index));
        }

        public com.cap12.xs.stringType first() {
            return new com.cap12.xs.stringType(owner.getElementFirst(info));
        }

        public com.cap12.xs.stringType last() {
            return new com.cap12.xs.stringType(owner.getElementLast(info));
        }

        public com.cap12.xs.stringType append() {
            return new com.cap12.xs.stringType(owner.createElement(info));
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
            return new MemberElement_resourceDesc_Iterator(this);
        }

        public com.altova.xml.meta.Element getInfo() {
            return new com.altova.xml.meta.Element(info);
        }
    }

    public MemberElement_mimeType mimeType;

    public static class MemberElement_mimeType {

        public static class MemberElement_mimeType_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_mimeType member;

            public MemberElement_mimeType_Iterator(MemberElement_mimeType member) {
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
                com.cap12.xs.stringType nx = new com.cap12.xs.stringType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected com.altova.xml.TypeBase owner;

        protected com.altova.typeinfo.MemberInfo info;

        public MemberElement_mimeType(com.altova.xml.TypeBase owner, com.altova.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public com.cap12.xs.stringType at(int index) {
            return new com.cap12.xs.stringType(owner.getElementAt(info, index));
        }

        public com.cap12.xs.stringType first() {
            return new com.cap12.xs.stringType(owner.getElementFirst(info));
        }

        public com.cap12.xs.stringType last() {
            return new com.cap12.xs.stringType(owner.getElementLast(info));
        }

        public com.cap12.xs.stringType append() {
            return new com.cap12.xs.stringType(owner.createElement(info));
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
            return new MemberElement_mimeType_Iterator(this);
        }

        public com.altova.xml.meta.Element getInfo() {
            return new com.altova.xml.meta.Element(info);
        }
    }

    public MemberElement_size size;

    public static class MemberElement_size {

        public static class MemberElement_size_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_size member;

            public MemberElement_size_Iterator(MemberElement_size member) {
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
                com.cap12.xs.integerType nx = new com.cap12.xs.integerType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected com.altova.xml.TypeBase owner;

        protected com.altova.typeinfo.MemberInfo info;

        public MemberElement_size(com.altova.xml.TypeBase owner, com.altova.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public com.cap12.xs.integerType at(int index) {
            return new com.cap12.xs.integerType(owner.getElementAt(info, index));
        }

        public com.cap12.xs.integerType first() {
            return new com.cap12.xs.integerType(owner.getElementFirst(info));
        }

        public com.cap12.xs.integerType last() {
            return new com.cap12.xs.integerType(owner.getElementLast(info));
        }

        public com.cap12.xs.integerType append() {
            return new com.cap12.xs.integerType(owner.createElement(info));
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
            return new MemberElement_size_Iterator(this);
        }

        public com.altova.xml.meta.Element getInfo() {
            return new com.altova.xml.meta.Element(info);
        }
    }

    public MemberElement_uri uri;

    public static class MemberElement_uri {

        public static class MemberElement_uri_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_uri member;

            public MemberElement_uri_Iterator(MemberElement_uri member) {
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
                com.cap12.xs.anyURIType nx = new com.cap12.xs.anyURIType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected com.altova.xml.TypeBase owner;

        protected com.altova.typeinfo.MemberInfo info;

        public MemberElement_uri(com.altova.xml.TypeBase owner, com.altova.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public com.cap12.xs.anyURIType at(int index) {
            return new com.cap12.xs.anyURIType(owner.getElementAt(info, index));
        }

        public com.cap12.xs.anyURIType first() {
            return new com.cap12.xs.anyURIType(owner.getElementFirst(info));
        }

        public com.cap12.xs.anyURIType last() {
            return new com.cap12.xs.anyURIType(owner.getElementLast(info));
        }

        public com.cap12.xs.anyURIType append() {
            return new com.cap12.xs.anyURIType(owner.createElement(info));
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
            return new MemberElement_uri_Iterator(this);
        }

        public com.altova.xml.meta.Element getInfo() {
            return new com.altova.xml.meta.Element(info);
        }
    }

    public MemberElement_derefUri derefUri;

    public static class MemberElement_derefUri {

        public static class MemberElement_derefUri_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_derefUri member;

            public MemberElement_derefUri_Iterator(MemberElement_derefUri member) {
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
                com.cap12.xs.stringType nx = new com.cap12.xs.stringType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected com.altova.xml.TypeBase owner;

        protected com.altova.typeinfo.MemberInfo info;

        public MemberElement_derefUri(com.altova.xml.TypeBase owner, com.altova.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public com.cap12.xs.stringType at(int index) {
            return new com.cap12.xs.stringType(owner.getElementAt(info, index));
        }

        public com.cap12.xs.stringType first() {
            return new com.cap12.xs.stringType(owner.getElementFirst(info));
        }

        public com.cap12.xs.stringType last() {
            return new com.cap12.xs.stringType(owner.getElementLast(info));
        }

        public com.cap12.xs.stringType append() {
            return new com.cap12.xs.stringType(owner.createElement(info));
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
            return new MemberElement_derefUri_Iterator(this);
        }

        public com.altova.xml.meta.Element getInfo() {
            return new com.altova.xml.meta.Element(info);
        }
    }

    public MemberElement_digest digest;

    public static class MemberElement_digest {

        public static class MemberElement_digest_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_digest member;

            public MemberElement_digest_Iterator(MemberElement_digest member) {
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
                com.cap12.xs.stringType nx = new com.cap12.xs.stringType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected com.altova.xml.TypeBase owner;

        protected com.altova.typeinfo.MemberInfo info;

        public MemberElement_digest(com.altova.xml.TypeBase owner, com.altova.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public com.cap12.xs.stringType at(int index) {
            return new com.cap12.xs.stringType(owner.getElementAt(info, index));
        }

        public com.cap12.xs.stringType first() {
            return new com.cap12.xs.stringType(owner.getElementFirst(info));
        }

        public com.cap12.xs.stringType last() {
            return new com.cap12.xs.stringType(owner.getElementLast(info));
        }

        public com.cap12.xs.stringType append() {
            return new com.cap12.xs.stringType(owner.createElement(info));
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
            return new MemberElement_digest_Iterator(this);
        }

        public com.altova.xml.meta.Element getInfo() {
            return new com.altova.xml.meta.Element(info);
        }
    }
}
