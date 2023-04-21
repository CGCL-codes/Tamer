package org.mitre.caasd.aixm.aixm;

public class AuthorityForAirspaceTimeSliceType extends org.mitre.caasd.aixm.aixm.AbstractAIXMTimeSliceType {

    public static org.mitre.caasd.aixm.xml.meta.ComplexType getStaticInfo() {
        return new org.mitre.caasd.aixm.xml.meta.ComplexType(org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getTypes()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_ti_aixm_altova_AuthorityForAirspaceTimeSliceType]);
    }

    public AuthorityForAirspaceTimeSliceType(org.w3c.dom.Node init) {
        super(init);
        instantiateMembers();
    }

    private void instantiateMembers() {
        type = new MemberElement_type(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_AuthorityForAirspaceTimeSliceType._type]);
        responsibleOrganisation = new MemberElement_responsibleOrganisation(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_AuthorityForAirspaceTimeSliceType._responsibleOrganisation]);
        assignedAirspace = new MemberElement_assignedAirspace(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_AuthorityForAirspaceTimeSliceType._assignedAirspace]);
        annotation = new MemberElement_annotation(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_AuthorityForAirspaceTimeSliceType._annotation]);
        extension = new MemberElement_extension(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_AuthorityForAirspaceTimeSliceType._extension]);
    }

    public MemberElement_type type;

    public static class MemberElement_type {

        public static class MemberElement_type_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_type member;

            public MemberElement_type_Iterator(MemberElement_type member) {
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
                org.mitre.caasd.aixm.aixm.typeType14 nx = new org.mitre.caasd.aixm.aixm.typeType14(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_type(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.typeType14 at(int index) {
            return new org.mitre.caasd.aixm.aixm.typeType14(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.typeType14 first() {
            return new org.mitre.caasd.aixm.aixm.typeType14(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.typeType14 last() {
            return new org.mitre.caasd.aixm.aixm.typeType14(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.typeType14 append() {
            return new org.mitre.caasd.aixm.aixm.typeType14(owner.createElement(info));
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
            return new MemberElement_type_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_responsibleOrganisation responsibleOrganisation;

    public static class MemberElement_responsibleOrganisation {

        public static class MemberElement_responsibleOrganisation_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_responsibleOrganisation member;

            public MemberElement_responsibleOrganisation_Iterator(MemberElement_responsibleOrganisation member) {
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
                org.mitre.caasd.aixm.aixm.OrganisationAuthorityPropertyType nx = new org.mitre.caasd.aixm.aixm.OrganisationAuthorityPropertyType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_responsibleOrganisation(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.OrganisationAuthorityPropertyType at(int index) {
            return new org.mitre.caasd.aixm.aixm.OrganisationAuthorityPropertyType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.OrganisationAuthorityPropertyType first() {
            return new org.mitre.caasd.aixm.aixm.OrganisationAuthorityPropertyType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.OrganisationAuthorityPropertyType last() {
            return new org.mitre.caasd.aixm.aixm.OrganisationAuthorityPropertyType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.OrganisationAuthorityPropertyType append() {
            return new org.mitre.caasd.aixm.aixm.OrganisationAuthorityPropertyType(owner.createElement(info));
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
            return new MemberElement_responsibleOrganisation_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_assignedAirspace assignedAirspace;

    public static class MemberElement_assignedAirspace {

        public static class MemberElement_assignedAirspace_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_assignedAirspace member;

            public MemberElement_assignedAirspace_Iterator(MemberElement_assignedAirspace member) {
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
                org.mitre.caasd.aixm.aixm.AirspacePropertyType nx = new org.mitre.caasd.aixm.aixm.AirspacePropertyType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_assignedAirspace(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.AirspacePropertyType at(int index) {
            return new org.mitre.caasd.aixm.aixm.AirspacePropertyType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.AirspacePropertyType first() {
            return new org.mitre.caasd.aixm.aixm.AirspacePropertyType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.AirspacePropertyType last() {
            return new org.mitre.caasd.aixm.aixm.AirspacePropertyType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.AirspacePropertyType append() {
            return new org.mitre.caasd.aixm.aixm.AirspacePropertyType(owner.createElement(info));
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
            return new MemberElement_assignedAirspace_Iterator(this);
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
                org.mitre.caasd.aixm.aixm.annotationType40 nx = new org.mitre.caasd.aixm.aixm.annotationType40(nextNode);
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

        public org.mitre.caasd.aixm.aixm.annotationType40 at(int index) {
            return new org.mitre.caasd.aixm.aixm.annotationType40(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.annotationType40 first() {
            return new org.mitre.caasd.aixm.aixm.annotationType40(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.annotationType40 last() {
            return new org.mitre.caasd.aixm.aixm.annotationType40(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.annotationType40 append() {
            return new org.mitre.caasd.aixm.aixm.annotationType40(owner.createElement(info));
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
                org.mitre.caasd.aixm.aixm.extensionType45 nx = new org.mitre.caasd.aixm.aixm.extensionType45(nextNode);
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

        public org.mitre.caasd.aixm.aixm.extensionType45 at(int index) {
            return new org.mitre.caasd.aixm.aixm.extensionType45(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.extensionType45 first() {
            return new org.mitre.caasd.aixm.aixm.extensionType45(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.extensionType45 last() {
            return new org.mitre.caasd.aixm.aixm.extensionType45(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.extensionType45 append() {
            return new org.mitre.caasd.aixm.aixm.extensionType45(owner.createElement(info));
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
        org.mitre.caasd.aixm.xml.XmlTreeOperations.setAttribute(getNode(), "http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "http://www.aixm.aero/schema/5.0", "AuthorityForAirspaceTimeSliceType");
    }
}
