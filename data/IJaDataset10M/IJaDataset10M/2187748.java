package org.mitre.caasd.aixm.aixm;

public class StandardLevelTableTimeSliceType extends org.mitre.caasd.aixm.aixm.AbstractAIXMTimeSliceType {

    public static org.mitre.caasd.aixm.xml.meta.ComplexType getStaticInfo() {
        return new org.mitre.caasd.aixm.xml.meta.ComplexType(org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getTypes()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_ti_aixm_altova_StandardLevelTableTimeSliceType]);
    }

    public StandardLevelTableTimeSliceType(org.w3c.dom.Node init) {
        super(init);
        instantiateMembers();
    }

    private void instantiateMembers() {
        name2 = new MemberElement_name2(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_StandardLevelTableTimeSliceType._name2]);
        description2 = new MemberElement_description2(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_StandardLevelTableTimeSliceType._description2]);
        standardICAO = new MemberElement_standardICAO(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_StandardLevelTableTimeSliceType._standardICAO]);
        annotation = new MemberElement_annotation(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_StandardLevelTableTimeSliceType._annotation]);
        extension = new MemberElement_extension(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_StandardLevelTableTimeSliceType._extension]);
    }

    public MemberElement_name2 name2;

    public static class MemberElement_name2 {

        public static class MemberElement_name2_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_name2 member;

            public MemberElement_name2_Iterator(MemberElement_name2 member) {
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
                org.mitre.caasd.aixm.aixm.nameType19 nx = new org.mitre.caasd.aixm.aixm.nameType19(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_name2(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.nameType19 at(int index) {
            return new org.mitre.caasd.aixm.aixm.nameType19(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.nameType19 first() {
            return new org.mitre.caasd.aixm.aixm.nameType19(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.nameType19 last() {
            return new org.mitre.caasd.aixm.aixm.nameType19(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.nameType19 append() {
            return new org.mitre.caasd.aixm.aixm.nameType19(owner.createElement(info));
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
            return new MemberElement_name2_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_description2 description2;

    public static class MemberElement_description2 {

        public static class MemberElement_description2_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_description2 member;

            public MemberElement_description2_Iterator(MemberElement_description2 member) {
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
                org.mitre.caasd.aixm.aixm.descriptionType14 nx = new org.mitre.caasd.aixm.aixm.descriptionType14(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_description2(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.descriptionType14 at(int index) {
            return new org.mitre.caasd.aixm.aixm.descriptionType14(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.descriptionType14 first() {
            return new org.mitre.caasd.aixm.aixm.descriptionType14(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.descriptionType14 last() {
            return new org.mitre.caasd.aixm.aixm.descriptionType14(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.descriptionType14 append() {
            return new org.mitre.caasd.aixm.aixm.descriptionType14(owner.createElement(info));
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
            return new MemberElement_description2_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_standardICAO standardICAO;

    public static class MemberElement_standardICAO {

        public static class MemberElement_standardICAO_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_standardICAO member;

            public MemberElement_standardICAO_Iterator(MemberElement_standardICAO member) {
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
                org.mitre.caasd.aixm.aixm.standardICAOType nx = new org.mitre.caasd.aixm.aixm.standardICAOType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_standardICAO(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.standardICAOType at(int index) {
            return new org.mitre.caasd.aixm.aixm.standardICAOType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.standardICAOType first() {
            return new org.mitre.caasd.aixm.aixm.standardICAOType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.standardICAOType last() {
            return new org.mitre.caasd.aixm.aixm.standardICAOType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.standardICAOType append() {
            return new org.mitre.caasd.aixm.aixm.standardICAOType(owner.createElement(info));
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
            return new MemberElement_standardICAO_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_annotation annotation;

    public static class MemberElement_annotation {

        public static class MemberElement_annotation_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_annotation member;

            public MemberElement_annotation_Iterator(MemberElement_annotation member) {
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
                org.mitre.caasd.aixm.aixm.annotationType146 nx = new org.mitre.caasd.aixm.aixm.annotationType146(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_annotation(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.annotationType146 at(int index) {
            return new org.mitre.caasd.aixm.aixm.annotationType146(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.annotationType146 first() {
            return new org.mitre.caasd.aixm.aixm.annotationType146(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.annotationType146 last() {
            return new org.mitre.caasd.aixm.aixm.annotationType146(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.annotationType146 append() {
            return new org.mitre.caasd.aixm.aixm.annotationType146(owner.createElement(info));
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
            return new MemberElement_annotation_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_extension extension;

    public static class MemberElement_extension {

        public static class MemberElement_extension_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_extension member;

            public MemberElement_extension_Iterator(MemberElement_extension member) {
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
                org.mitre.caasd.aixm.aixm.extensionType189 nx = new org.mitre.caasd.aixm.aixm.extensionType189(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_extension(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.extensionType189 at(int index) {
            return new org.mitre.caasd.aixm.aixm.extensionType189(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.extensionType189 first() {
            return new org.mitre.caasd.aixm.aixm.extensionType189(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.extensionType189 last() {
            return new org.mitre.caasd.aixm.aixm.extensionType189(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.extensionType189 append() {
            return new org.mitre.caasd.aixm.aixm.extensionType189(owner.createElement(info));
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
            return new MemberElement_extension_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public void setXsiType() {
        org.mitre.caasd.aixm.xml.XmlTreeOperations.setAttribute(getNode(), "http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "http://www.aixm.aero/schema/5.0", "StandardLevelTableTimeSliceType");
    }
}
