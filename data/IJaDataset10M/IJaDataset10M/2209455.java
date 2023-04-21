package org.mitre.caasd.aixm.aixm;

public class ApproachAltitudeTableType extends org.mitre.caasd.aixm.xml.TypeBase {

    public static org.mitre.caasd.aixm.xml.meta.ComplexType getStaticInfo() {
        return new org.mitre.caasd.aixm.xml.meta.ComplexType(org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getTypes()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_ti_aixm_altova_ApproachAltitudeTableType]);
    }

    public ApproachAltitudeTableType(org.w3c.dom.Node init) {
        super(init);
        instantiateMembers();
    }

    private void instantiateMembers() {
        measurementPoint = new MemberElement_measurementPoint(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_ApproachAltitudeTableType._measurementPoint]);
        altitude = new MemberElement_altitude(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_ApproachAltitudeTableType._altitude]);
        altitudeReference = new MemberElement_altitudeReference(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_ApproachAltitudeTableType._altitudeReference]);
        annotation = new MemberElement_annotation(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_ApproachAltitudeTableType._annotation]);
        extension = new MemberElement_extension(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_ApproachAltitudeTableType._extension]);
    }

    public MemberElement_measurementPoint measurementPoint;

    public static class MemberElement_measurementPoint {

        public static class MemberElement_measurementPoint_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_measurementPoint member;

            public MemberElement_measurementPoint_Iterator(MemberElement_measurementPoint member) {
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
                org.mitre.caasd.aixm.aixm.measurementPointType nx = new org.mitre.caasd.aixm.aixm.measurementPointType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_measurementPoint(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.measurementPointType at(int index) {
            return new org.mitre.caasd.aixm.aixm.measurementPointType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.measurementPointType first() {
            return new org.mitre.caasd.aixm.aixm.measurementPointType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.measurementPointType last() {
            return new org.mitre.caasd.aixm.aixm.measurementPointType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.measurementPointType append() {
            return new org.mitre.caasd.aixm.aixm.measurementPointType(owner.createElement(info));
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
            return new MemberElement_measurementPoint_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_altitude altitude;

    public static class MemberElement_altitude {

        public static class MemberElement_altitude_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_altitude member;

            public MemberElement_altitude_Iterator(MemberElement_altitude member) {
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
                org.mitre.caasd.aixm.aixm.altitudeType nx = new org.mitre.caasd.aixm.aixm.altitudeType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_altitude(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.altitudeType at(int index) {
            return new org.mitre.caasd.aixm.aixm.altitudeType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.altitudeType first() {
            return new org.mitre.caasd.aixm.aixm.altitudeType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.altitudeType last() {
            return new org.mitre.caasd.aixm.aixm.altitudeType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.altitudeType append() {
            return new org.mitre.caasd.aixm.aixm.altitudeType(owner.createElement(info));
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
            return new MemberElement_altitude_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_altitudeReference altitudeReference;

    public static class MemberElement_altitudeReference {

        public static class MemberElement_altitudeReference_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_altitudeReference member;

            public MemberElement_altitudeReference_Iterator(MemberElement_altitudeReference member) {
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
                org.mitre.caasd.aixm.aixm.altitudeReferenceType nx = new org.mitre.caasd.aixm.aixm.altitudeReferenceType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_altitudeReference(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.altitudeReferenceType at(int index) {
            return new org.mitre.caasd.aixm.aixm.altitudeReferenceType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.altitudeReferenceType first() {
            return new org.mitre.caasd.aixm.aixm.altitudeReferenceType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.altitudeReferenceType last() {
            return new org.mitre.caasd.aixm.aixm.altitudeReferenceType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.altitudeReferenceType append() {
            return new org.mitre.caasd.aixm.aixm.altitudeReferenceType(owner.createElement(info));
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
            return new MemberElement_altitudeReference_Iterator(this);
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
                org.mitre.caasd.aixm.aixm.annotationType29 nx = new org.mitre.caasd.aixm.aixm.annotationType29(nextNode);
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

        public org.mitre.caasd.aixm.aixm.annotationType29 at(int index) {
            return new org.mitre.caasd.aixm.aixm.annotationType29(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.annotationType29 first() {
            return new org.mitre.caasd.aixm.aixm.annotationType29(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.annotationType29 last() {
            return new org.mitre.caasd.aixm.aixm.annotationType29(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.annotationType29 append() {
            return new org.mitre.caasd.aixm.aixm.annotationType29(owner.createElement(info));
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
                org.mitre.caasd.aixm.aixm.extensionType32 nx = new org.mitre.caasd.aixm.aixm.extensionType32(nextNode);
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

        public org.mitre.caasd.aixm.aixm.extensionType32 at(int index) {
            return new org.mitre.caasd.aixm.aixm.extensionType32(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.extensionType32 first() {
            return new org.mitre.caasd.aixm.aixm.extensionType32(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.extensionType32 last() {
            return new org.mitre.caasd.aixm.aixm.extensionType32(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.extensionType32 append() {
            return new org.mitre.caasd.aixm.aixm.extensionType32(owner.createElement(info));
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
        org.mitre.caasd.aixm.xml.XmlTreeOperations.setAttribute(getNode(), "http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "http://www.aixm.aero/schema/5.0", "ApproachAltitudeTableType");
    }
}
