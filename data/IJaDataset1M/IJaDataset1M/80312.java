package org.mitre.caasd.aixm.aixm;

public class SpecialNavigationStationTimeSliceType extends org.mitre.caasd.aixm.aixm.AbstractAIXMTimeSliceType {

    public static org.mitre.caasd.aixm.xml.meta.ComplexType getStaticInfo() {
        return new org.mitre.caasd.aixm.xml.meta.ComplexType(org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getTypes()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_ti_aixm_altova_SpecialNavigationStationTimeSliceType]);
    }

    public SpecialNavigationStationTimeSliceType(org.w3c.dom.Node init) {
        super(init);
        instantiateMembers();
    }

    private void instantiateMembers() {
        name2 = new MemberElement_name2(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_SpecialNavigationStationTimeSliceType._name2]);
        codeTypeService = new MemberElement_codeTypeService(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_SpecialNavigationStationTimeSliceType._codeTypeService]);
        frequency = new MemberElement_frequency(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_SpecialNavigationStationTimeSliceType._frequency]);
        codeEmmission = new MemberElement_codeEmmission(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_SpecialNavigationStationTimeSliceType._codeEmmission]);
        systemChain = new MemberElement_systemChain(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_SpecialNavigationStationTimeSliceType._systemChain]);
        operatingHours = new MemberElement_operatingHours(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_SpecialNavigationStationTimeSliceType._operatingHours]);
        responsibleOrganisation = new MemberElement_responsibleOrganisation(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_SpecialNavigationStationTimeSliceType._responsibleOrganisation]);
        position = new MemberElement_position(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_SpecialNavigationStationTimeSliceType._position]);
        annotation = new MemberElement_annotation(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_SpecialNavigationStationTimeSliceType._annotation]);
        extension = new MemberElement_extension(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_SpecialNavigationStationTimeSliceType._extension]);
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
                org.mitre.caasd.aixm.aixm.nameType17 nx = new org.mitre.caasd.aixm.aixm.nameType17(nextNode);
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

        public org.mitre.caasd.aixm.aixm.nameType17 at(int index) {
            return new org.mitre.caasd.aixm.aixm.nameType17(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.nameType17 first() {
            return new org.mitre.caasd.aixm.aixm.nameType17(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.nameType17 last() {
            return new org.mitre.caasd.aixm.aixm.nameType17(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.nameType17 append() {
            return new org.mitre.caasd.aixm.aixm.nameType17(owner.createElement(info));
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

    public MemberElement_codeTypeService codeTypeService;

    public static class MemberElement_codeTypeService {

        public static class MemberElement_codeTypeService_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_codeTypeService member;

            public MemberElement_codeTypeService_Iterator(MemberElement_codeTypeService member) {
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
                org.mitre.caasd.aixm.aixm.codeTypeServiceType nx = new org.mitre.caasd.aixm.aixm.codeTypeServiceType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_codeTypeService(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.codeTypeServiceType at(int index) {
            return new org.mitre.caasd.aixm.aixm.codeTypeServiceType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.codeTypeServiceType first() {
            return new org.mitre.caasd.aixm.aixm.codeTypeServiceType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.codeTypeServiceType last() {
            return new org.mitre.caasd.aixm.aixm.codeTypeServiceType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.codeTypeServiceType append() {
            return new org.mitre.caasd.aixm.aixm.codeTypeServiceType(owner.createElement(info));
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
            return new MemberElement_codeTypeService_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_frequency frequency;

    public static class MemberElement_frequency {

        public static class MemberElement_frequency_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_frequency member;

            public MemberElement_frequency_Iterator(MemberElement_frequency member) {
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
                org.mitre.caasd.aixm.aixm.frequencyType6 nx = new org.mitre.caasd.aixm.aixm.frequencyType6(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_frequency(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.frequencyType6 at(int index) {
            return new org.mitre.caasd.aixm.aixm.frequencyType6(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.frequencyType6 first() {
            return new org.mitre.caasd.aixm.aixm.frequencyType6(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.frequencyType6 last() {
            return new org.mitre.caasd.aixm.aixm.frequencyType6(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.frequencyType6 append() {
            return new org.mitre.caasd.aixm.aixm.frequencyType6(owner.createElement(info));
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
            return new MemberElement_frequency_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_codeEmmission codeEmmission;

    public static class MemberElement_codeEmmission {

        public static class MemberElement_codeEmmission_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_codeEmmission member;

            public MemberElement_codeEmmission_Iterator(MemberElement_codeEmmission member) {
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
                org.mitre.caasd.aixm.aixm.codeEmmissionType nx = new org.mitre.caasd.aixm.aixm.codeEmmissionType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_codeEmmission(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.codeEmmissionType at(int index) {
            return new org.mitre.caasd.aixm.aixm.codeEmmissionType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.codeEmmissionType first() {
            return new org.mitre.caasd.aixm.aixm.codeEmmissionType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.codeEmmissionType last() {
            return new org.mitre.caasd.aixm.aixm.codeEmmissionType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.codeEmmissionType append() {
            return new org.mitre.caasd.aixm.aixm.codeEmmissionType(owner.createElement(info));
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
            return new MemberElement_codeEmmission_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_systemChain systemChain;

    public static class MemberElement_systemChain {

        public static class MemberElement_systemChain_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_systemChain member;

            public MemberElement_systemChain_Iterator(MemberElement_systemChain member) {
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
                org.mitre.caasd.aixm.aixm.SpecialNavigationSystemPropertyType nx = new org.mitre.caasd.aixm.aixm.SpecialNavigationSystemPropertyType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_systemChain(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.SpecialNavigationSystemPropertyType at(int index) {
            return new org.mitre.caasd.aixm.aixm.SpecialNavigationSystemPropertyType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.SpecialNavigationSystemPropertyType first() {
            return new org.mitre.caasd.aixm.aixm.SpecialNavigationSystemPropertyType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.SpecialNavigationSystemPropertyType last() {
            return new org.mitre.caasd.aixm.aixm.SpecialNavigationSystemPropertyType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.SpecialNavigationSystemPropertyType append() {
            return new org.mitre.caasd.aixm.aixm.SpecialNavigationSystemPropertyType(owner.createElement(info));
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
            return new MemberElement_systemChain_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_operatingHours operatingHours;

    public static class MemberElement_operatingHours {

        public static class MemberElement_operatingHours_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_operatingHours member;

            public MemberElement_operatingHours_Iterator(MemberElement_operatingHours member) {
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
                org.mitre.caasd.aixm.aixm.operatingHoursType2 nx = new org.mitre.caasd.aixm.aixm.operatingHoursType2(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_operatingHours(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.operatingHoursType2 at(int index) {
            return new org.mitre.caasd.aixm.aixm.operatingHoursType2(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.operatingHoursType2 first() {
            return new org.mitre.caasd.aixm.aixm.operatingHoursType2(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.operatingHoursType2 last() {
            return new org.mitre.caasd.aixm.aixm.operatingHoursType2(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.operatingHoursType2 append() {
            return new org.mitre.caasd.aixm.aixm.operatingHoursType2(owner.createElement(info));
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
            return new MemberElement_operatingHours_Iterator(this);
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
                org.mitre.caasd.aixm.aixm.responsibleOrganisationType nx = new org.mitre.caasd.aixm.aixm.responsibleOrganisationType(nextNode);
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

        public org.mitre.caasd.aixm.aixm.responsibleOrganisationType at(int index) {
            return new org.mitre.caasd.aixm.aixm.responsibleOrganisationType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.responsibleOrganisationType first() {
            return new org.mitre.caasd.aixm.aixm.responsibleOrganisationType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.responsibleOrganisationType last() {
            return new org.mitre.caasd.aixm.aixm.responsibleOrganisationType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.responsibleOrganisationType append() {
            return new org.mitre.caasd.aixm.aixm.responsibleOrganisationType(owner.createElement(info));
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

    public MemberElement_position position;

    public static class MemberElement_position {

        public static class MemberElement_position_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_position member;

            public MemberElement_position_Iterator(MemberElement_position member) {
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
                org.mitre.caasd.aixm.aixm.positionType5 nx = new org.mitre.caasd.aixm.aixm.positionType5(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_position(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.positionType5 at(int index) {
            return new org.mitre.caasd.aixm.aixm.positionType5(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.positionType5 first() {
            return new org.mitre.caasd.aixm.aixm.positionType5(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.positionType5 last() {
            return new org.mitre.caasd.aixm.aixm.positionType5(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.positionType5 append() {
            return new org.mitre.caasd.aixm.aixm.positionType5(owner.createElement(info));
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
            return new MemberElement_position_Iterator(this);
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
                org.mitre.caasd.aixm.aixm.annotationType142 nx = new org.mitre.caasd.aixm.aixm.annotationType142(nextNode);
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

        public org.mitre.caasd.aixm.aixm.annotationType142 at(int index) {
            return new org.mitre.caasd.aixm.aixm.annotationType142(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.annotationType142 first() {
            return new org.mitre.caasd.aixm.aixm.annotationType142(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.annotationType142 last() {
            return new org.mitre.caasd.aixm.aixm.annotationType142(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.annotationType142 append() {
            return new org.mitre.caasd.aixm.aixm.annotationType142(owner.createElement(info));
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
                org.mitre.caasd.aixm.aixm.extensionType183 nx = new org.mitre.caasd.aixm.aixm.extensionType183(nextNode);
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

        public org.mitre.caasd.aixm.aixm.extensionType183 at(int index) {
            return new org.mitre.caasd.aixm.aixm.extensionType183(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.extensionType183 first() {
            return new org.mitre.caasd.aixm.aixm.extensionType183(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.extensionType183 last() {
            return new org.mitre.caasd.aixm.aixm.extensionType183(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.extensionType183 append() {
            return new org.mitre.caasd.aixm.aixm.extensionType183(owner.createElement(info));
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
        org.mitre.caasd.aixm.xml.XmlTreeOperations.setAttribute(getNode(), "http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "http://www.aixm.aero/schema/5.0", "SpecialNavigationStationTimeSliceType");
    }
}
