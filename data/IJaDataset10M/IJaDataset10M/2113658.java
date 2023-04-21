package org.mitre.caasd.aixm.aixm;

public class AirspaceLayerClassType extends org.mitre.caasd.aixm.xml.TypeBase {

    public static org.mitre.caasd.aixm.xml.meta.ComplexType getStaticInfo() {
        return new org.mitre.caasd.aixm.xml.meta.ComplexType(org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getTypes()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_ti_aixm_altova_AirspaceLayerClassType]);
    }

    public AirspaceLayerClassType(org.w3c.dom.Node init) {
        super(init);
        instantiateMembers();
    }

    private void instantiateMembers() {
        class2 = new MemberElement_class2(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_AirspaceLayerClassType._class2]);
        associatedLevels = new MemberElement_associatedLevels(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_AirspaceLayerClassType._associatedLevels]);
        annotation = new MemberElement_annotation(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_AirspaceLayerClassType._annotation]);
        extension = new MemberElement_extension(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_AirspaceLayerClassType._extension]);
    }

    public MemberElement_class2 class2;

    public static class MemberElement_class2 {

        public static class MemberElement_class2_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_class2 member;

            public MemberElement_class2_Iterator(MemberElement_class2 member) {
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
                org.mitre.caasd.aixm.aixm.classType nx = new org.mitre.caasd.aixm.aixm.classType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_class2(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.classType at(int index) {
            return new org.mitre.caasd.aixm.aixm.classType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.classType first() {
            return new org.mitre.caasd.aixm.aixm.classType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.classType last() {
            return new org.mitre.caasd.aixm.aixm.classType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.classType append() {
            return new org.mitre.caasd.aixm.aixm.classType(owner.createElement(info));
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
            return new MemberElement_class2_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_associatedLevels associatedLevels;

    public static class MemberElement_associatedLevels {

        public static class MemberElement_associatedLevels_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_associatedLevels member;

            public MemberElement_associatedLevels_Iterator(MemberElement_associatedLevels member) {
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
                org.mitre.caasd.aixm.aixm.associatedLevelsType nx = new org.mitre.caasd.aixm.aixm.associatedLevelsType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_associatedLevels(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.associatedLevelsType at(int index) {
            return new org.mitre.caasd.aixm.aixm.associatedLevelsType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.associatedLevelsType first() {
            return new org.mitre.caasd.aixm.aixm.associatedLevelsType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.associatedLevelsType last() {
            return new org.mitre.caasd.aixm.aixm.associatedLevelsType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.associatedLevelsType append() {
            return new org.mitre.caasd.aixm.aixm.associatedLevelsType(owner.createElement(info));
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
            return new MemberElement_associatedLevels_Iterator(this);
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
                org.mitre.caasd.aixm.aixm.annotationType19 nx = new org.mitre.caasd.aixm.aixm.annotationType19(nextNode);
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

        public org.mitre.caasd.aixm.aixm.annotationType19 at(int index) {
            return new org.mitre.caasd.aixm.aixm.annotationType19(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.annotationType19 first() {
            return new org.mitre.caasd.aixm.aixm.annotationType19(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.annotationType19 last() {
            return new org.mitre.caasd.aixm.aixm.annotationType19(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.annotationType19 append() {
            return new org.mitre.caasd.aixm.aixm.annotationType19(owner.createElement(info));
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
                org.mitre.caasd.aixm.aixm.extensionType22 nx = new org.mitre.caasd.aixm.aixm.extensionType22(nextNode);
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

        public org.mitre.caasd.aixm.aixm.extensionType22 at(int index) {
            return new org.mitre.caasd.aixm.aixm.extensionType22(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.extensionType22 first() {
            return new org.mitre.caasd.aixm.aixm.extensionType22(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.extensionType22 last() {
            return new org.mitre.caasd.aixm.aixm.extensionType22(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.extensionType22 append() {
            return new org.mitre.caasd.aixm.aixm.extensionType22(owner.createElement(info));
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
        org.mitre.caasd.aixm.xml.XmlTreeOperations.setAttribute(getNode(), "http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "http://www.aixm.aero/schema/5.0", "AirspaceLayerClassType");
    }
}
