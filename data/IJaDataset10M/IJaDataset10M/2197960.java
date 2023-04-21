package org.mitre.caasd.aixm.aixm;

public class InformationServiceTimeSliceType extends org.mitre.caasd.aixm.aixm.AbstractAIXMTimeSliceType {

    public static org.mitre.caasd.aixm.xml.meta.ComplexType getStaticInfo() {
        return new org.mitre.caasd.aixm.xml.meta.ComplexType(org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getTypes()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_ti_aixm_altova_InformationServiceTimeSliceType]);
    }

    public InformationServiceTimeSliceType(org.w3c.dom.Node init) {
        super(init);
        instantiateMembers();
    }

    private void instantiateMembers() {
        flightOperations = new MemberElement_flightOperations(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_InformationServiceTimeSliceType._flightOperations]);
        rank = new MemberElement_rank(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_InformationServiceTimeSliceType._rank]);
        compliantICAO = new MemberElement_compliantICAO(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_InformationServiceTimeSliceType._compliantICAO]);
        name2 = new MemberElement_name2(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_InformationServiceTimeSliceType._name2]);
        operationalStatus = new MemberElement_operationalStatus(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_InformationServiceTimeSliceType._operationalStatus]);
        location = new MemberElement_location(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_InformationServiceTimeSliceType._location]);
        serviceProvider = new MemberElement_serviceProvider(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_InformationServiceTimeSliceType._serviceProvider]);
        workingHours = new MemberElement_workingHours(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_InformationServiceTimeSliceType._workingHours]);
        callsign = new MemberElement_callsign(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_InformationServiceTimeSliceType._callsign]);
        radioCommunication = new MemberElement_radioCommunication(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_InformationServiceTimeSliceType._radioCommunication]);
        groundCommunication = new MemberElement_groundCommunication(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_InformationServiceTimeSliceType._groundCommunication]);
        annotation = new MemberElement_annotation(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_InformationServiceTimeSliceType._annotation]);
        type = new MemberElement_type(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_InformationServiceTimeSliceType._type]);
        voice = new MemberElement_voice(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_InformationServiceTimeSliceType._voice]);
        dataLink = new MemberElement_dataLink(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_InformationServiceTimeSliceType._dataLink]);
        navaidBroadcast = new MemberElement_navaidBroadcast(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_InformationServiceTimeSliceType._navaidBroadcast]);
        clientAirspace = new MemberElement_clientAirspace(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_InformationServiceTimeSliceType._clientAirspace]);
        clientAirport = new MemberElement_clientAirport(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_InformationServiceTimeSliceType._clientAirport]);
        clientRoute = new MemberElement_clientRoute(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_InformationServiceTimeSliceType._clientRoute]);
        clientProcedure = new MemberElement_clientProcedure(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_InformationServiceTimeSliceType._clientProcedure]);
        clientHolding = new MemberElement_clientHolding(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_InformationServiceTimeSliceType._clientHolding]);
        clientAerialRefueling = new MemberElement_clientAerialRefueling(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_InformationServiceTimeSliceType._clientAerialRefueling]);
        extension = new MemberElement_extension(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_InformationServiceTimeSliceType._extension]);
    }

    public MemberElement_flightOperations flightOperations;

    public static class MemberElement_flightOperations {

        public static class MemberElement_flightOperations_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_flightOperations member;

            public MemberElement_flightOperations_Iterator(MemberElement_flightOperations member) {
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
                org.mitre.caasd.aixm.aixm.flightOperationsType nx = new org.mitre.caasd.aixm.aixm.flightOperationsType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_flightOperations(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.flightOperationsType at(int index) {
            return new org.mitre.caasd.aixm.aixm.flightOperationsType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.flightOperationsType first() {
            return new org.mitre.caasd.aixm.aixm.flightOperationsType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.flightOperationsType last() {
            return new org.mitre.caasd.aixm.aixm.flightOperationsType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.flightOperationsType append() {
            return new org.mitre.caasd.aixm.aixm.flightOperationsType(owner.createElement(info));
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
            return new MemberElement_flightOperations_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_rank rank;

    public static class MemberElement_rank {

        public static class MemberElement_rank_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_rank member;

            public MemberElement_rank_Iterator(MemberElement_rank member) {
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
                org.mitre.caasd.aixm.aixm.rankType nx = new org.mitre.caasd.aixm.aixm.rankType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_rank(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.rankType at(int index) {
            return new org.mitre.caasd.aixm.aixm.rankType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.rankType first() {
            return new org.mitre.caasd.aixm.aixm.rankType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.rankType last() {
            return new org.mitre.caasd.aixm.aixm.rankType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.rankType append() {
            return new org.mitre.caasd.aixm.aixm.rankType(owner.createElement(info));
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
            return new MemberElement_rank_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_compliantICAO compliantICAO;

    public static class MemberElement_compliantICAO {

        public static class MemberElement_compliantICAO_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_compliantICAO member;

            public MemberElement_compliantICAO_Iterator(MemberElement_compliantICAO member) {
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
                org.mitre.caasd.aixm.aixm.compliantICAOType nx = new org.mitre.caasd.aixm.aixm.compliantICAOType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_compliantICAO(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.compliantICAOType at(int index) {
            return new org.mitre.caasd.aixm.aixm.compliantICAOType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.compliantICAOType first() {
            return new org.mitre.caasd.aixm.aixm.compliantICAOType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.compliantICAOType last() {
            return new org.mitre.caasd.aixm.aixm.compliantICAOType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.compliantICAOType append() {
            return new org.mitre.caasd.aixm.aixm.compliantICAOType(owner.createElement(info));
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
            return new MemberElement_compliantICAO_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
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
                org.mitre.caasd.aixm.aixm.nameType2 nx = new org.mitre.caasd.aixm.aixm.nameType2(nextNode);
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

        public org.mitre.caasd.aixm.aixm.nameType2 at(int index) {
            return new org.mitre.caasd.aixm.aixm.nameType2(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.nameType2 first() {
            return new org.mitre.caasd.aixm.aixm.nameType2(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.nameType2 last() {
            return new org.mitre.caasd.aixm.aixm.nameType2(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.nameType2 append() {
            return new org.mitre.caasd.aixm.aixm.nameType2(owner.createElement(info));
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

    public MemberElement_operationalStatus operationalStatus;

    public static class MemberElement_operationalStatus {

        public static class MemberElement_operationalStatus_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_operationalStatus member;

            public MemberElement_operationalStatus_Iterator(MemberElement_operationalStatus member) {
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
                org.mitre.caasd.aixm.aixm.operationalStatusType nx = new org.mitre.caasd.aixm.aixm.operationalStatusType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_operationalStatus(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.operationalStatusType at(int index) {
            return new org.mitre.caasd.aixm.aixm.operationalStatusType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.operationalStatusType first() {
            return new org.mitre.caasd.aixm.aixm.operationalStatusType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.operationalStatusType last() {
            return new org.mitre.caasd.aixm.aixm.operationalStatusType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.operationalStatusType append() {
            return new org.mitre.caasd.aixm.aixm.operationalStatusType(owner.createElement(info));
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
            return new MemberElement_operationalStatus_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_location location;

    public static class MemberElement_location {

        public static class MemberElement_location_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_location member;

            public MemberElement_location_Iterator(MemberElement_location member) {
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
                org.mitre.caasd.aixm.aixm.locationType nx = new org.mitre.caasd.aixm.aixm.locationType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_location(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.locationType at(int index) {
            return new org.mitre.caasd.aixm.aixm.locationType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.locationType first() {
            return new org.mitre.caasd.aixm.aixm.locationType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.locationType last() {
            return new org.mitre.caasd.aixm.aixm.locationType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.locationType append() {
            return new org.mitre.caasd.aixm.aixm.locationType(owner.createElement(info));
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
            return new MemberElement_location_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_serviceProvider serviceProvider;

    public static class MemberElement_serviceProvider {

        public static class MemberElement_serviceProvider_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_serviceProvider member;

            public MemberElement_serviceProvider_Iterator(MemberElement_serviceProvider member) {
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
                org.mitre.caasd.aixm.aixm.UnitPropertyType nx = new org.mitre.caasd.aixm.aixm.UnitPropertyType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_serviceProvider(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.UnitPropertyType at(int index) {
            return new org.mitre.caasd.aixm.aixm.UnitPropertyType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.UnitPropertyType first() {
            return new org.mitre.caasd.aixm.aixm.UnitPropertyType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.UnitPropertyType last() {
            return new org.mitre.caasd.aixm.aixm.UnitPropertyType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.UnitPropertyType append() {
            return new org.mitre.caasd.aixm.aixm.UnitPropertyType(owner.createElement(info));
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
            return new MemberElement_serviceProvider_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_workingHours workingHours;

    public static class MemberElement_workingHours {

        public static class MemberElement_workingHours_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_workingHours member;

            public MemberElement_workingHours_Iterator(MemberElement_workingHours member) {
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
                org.mitre.caasd.aixm.aixm.workingHoursType nx = new org.mitre.caasd.aixm.aixm.workingHoursType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_workingHours(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.workingHoursType at(int index) {
            return new org.mitre.caasd.aixm.aixm.workingHoursType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.workingHoursType first() {
            return new org.mitre.caasd.aixm.aixm.workingHoursType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.workingHoursType last() {
            return new org.mitre.caasd.aixm.aixm.workingHoursType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.workingHoursType append() {
            return new org.mitre.caasd.aixm.aixm.workingHoursType(owner.createElement(info));
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
            return new MemberElement_workingHours_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_callsign callsign;

    public static class MemberElement_callsign {

        public static class MemberElement_callsign_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_callsign member;

            public MemberElement_callsign_Iterator(MemberElement_callsign member) {
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
                org.mitre.caasd.aixm.aixm.callsignType nx = new org.mitre.caasd.aixm.aixm.callsignType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_callsign(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.callsignType at(int index) {
            return new org.mitre.caasd.aixm.aixm.callsignType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.callsignType first() {
            return new org.mitre.caasd.aixm.aixm.callsignType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.callsignType last() {
            return new org.mitre.caasd.aixm.aixm.callsignType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.callsignType append() {
            return new org.mitre.caasd.aixm.aixm.callsignType(owner.createElement(info));
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
            return new MemberElement_callsign_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_radioCommunication radioCommunication;

    public static class MemberElement_radioCommunication {

        public static class MemberElement_radioCommunication_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_radioCommunication member;

            public MemberElement_radioCommunication_Iterator(MemberElement_radioCommunication member) {
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
                org.mitre.caasd.aixm.aixm.RadioCommunicationChannelPropertyType nx = new org.mitre.caasd.aixm.aixm.RadioCommunicationChannelPropertyType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_radioCommunication(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.RadioCommunicationChannelPropertyType at(int index) {
            return new org.mitre.caasd.aixm.aixm.RadioCommunicationChannelPropertyType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.RadioCommunicationChannelPropertyType first() {
            return new org.mitre.caasd.aixm.aixm.RadioCommunicationChannelPropertyType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.RadioCommunicationChannelPropertyType last() {
            return new org.mitre.caasd.aixm.aixm.RadioCommunicationChannelPropertyType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.RadioCommunicationChannelPropertyType append() {
            return new org.mitre.caasd.aixm.aixm.RadioCommunicationChannelPropertyType(owner.createElement(info));
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
            return new MemberElement_radioCommunication_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_groundCommunication groundCommunication;

    public static class MemberElement_groundCommunication {

        public static class MemberElement_groundCommunication_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_groundCommunication member;

            public MemberElement_groundCommunication_Iterator(MemberElement_groundCommunication member) {
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
                org.mitre.caasd.aixm.aixm.groundCommunicationType nx = new org.mitre.caasd.aixm.aixm.groundCommunicationType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_groundCommunication(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.groundCommunicationType at(int index) {
            return new org.mitre.caasd.aixm.aixm.groundCommunicationType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.groundCommunicationType first() {
            return new org.mitre.caasd.aixm.aixm.groundCommunicationType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.groundCommunicationType last() {
            return new org.mitre.caasd.aixm.aixm.groundCommunicationType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.groundCommunicationType append() {
            return new org.mitre.caasd.aixm.aixm.groundCommunicationType(owner.createElement(info));
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
            return new MemberElement_groundCommunication_Iterator(this);
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
                org.mitre.caasd.aixm.aixm.annotationType6 nx = new org.mitre.caasd.aixm.aixm.annotationType6(nextNode);
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

        public org.mitre.caasd.aixm.aixm.annotationType6 at(int index) {
            return new org.mitre.caasd.aixm.aixm.annotationType6(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.annotationType6 first() {
            return new org.mitre.caasd.aixm.aixm.annotationType6(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.annotationType6 last() {
            return new org.mitre.caasd.aixm.aixm.annotationType6(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.annotationType6 append() {
            return new org.mitre.caasd.aixm.aixm.annotationType6(owner.createElement(info));
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
                org.mitre.caasd.aixm.aixm.typeType30 nx = new org.mitre.caasd.aixm.aixm.typeType30(nextNode);
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

        public org.mitre.caasd.aixm.aixm.typeType30 at(int index) {
            return new org.mitre.caasd.aixm.aixm.typeType30(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.typeType30 first() {
            return new org.mitre.caasd.aixm.aixm.typeType30(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.typeType30 last() {
            return new org.mitre.caasd.aixm.aixm.typeType30(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.typeType30 append() {
            return new org.mitre.caasd.aixm.aixm.typeType30(owner.createElement(info));
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

    public MemberElement_voice voice;

    public static class MemberElement_voice {

        public static class MemberElement_voice_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_voice member;

            public MemberElement_voice_Iterator(MemberElement_voice member) {
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
                org.mitre.caasd.aixm.aixm.voiceType nx = new org.mitre.caasd.aixm.aixm.voiceType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_voice(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.voiceType at(int index) {
            return new org.mitre.caasd.aixm.aixm.voiceType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.voiceType first() {
            return new org.mitre.caasd.aixm.aixm.voiceType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.voiceType last() {
            return new org.mitre.caasd.aixm.aixm.voiceType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.voiceType append() {
            return new org.mitre.caasd.aixm.aixm.voiceType(owner.createElement(info));
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
            return new MemberElement_voice_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_dataLink dataLink;

    public static class MemberElement_dataLink {

        public static class MemberElement_dataLink_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_dataLink member;

            public MemberElement_dataLink_Iterator(MemberElement_dataLink member) {
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
                org.mitre.caasd.aixm.aixm.dataLinkType nx = new org.mitre.caasd.aixm.aixm.dataLinkType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_dataLink(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.dataLinkType at(int index) {
            return new org.mitre.caasd.aixm.aixm.dataLinkType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.dataLinkType first() {
            return new org.mitre.caasd.aixm.aixm.dataLinkType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.dataLinkType last() {
            return new org.mitre.caasd.aixm.aixm.dataLinkType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.dataLinkType append() {
            return new org.mitre.caasd.aixm.aixm.dataLinkType(owner.createElement(info));
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
            return new MemberElement_dataLink_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_navaidBroadcast navaidBroadcast;

    public static class MemberElement_navaidBroadcast {

        public static class MemberElement_navaidBroadcast_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_navaidBroadcast member;

            public MemberElement_navaidBroadcast_Iterator(MemberElement_navaidBroadcast member) {
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
                org.mitre.caasd.aixm.aixm.VORPropertyType nx = new org.mitre.caasd.aixm.aixm.VORPropertyType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_navaidBroadcast(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.VORPropertyType at(int index) {
            return new org.mitre.caasd.aixm.aixm.VORPropertyType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.VORPropertyType first() {
            return new org.mitre.caasd.aixm.aixm.VORPropertyType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.VORPropertyType last() {
            return new org.mitre.caasd.aixm.aixm.VORPropertyType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.VORPropertyType append() {
            return new org.mitre.caasd.aixm.aixm.VORPropertyType(owner.createElement(info));
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
            return new MemberElement_navaidBroadcast_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_clientAirspace clientAirspace;

    public static class MemberElement_clientAirspace {

        public static class MemberElement_clientAirspace_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_clientAirspace member;

            public MemberElement_clientAirspace_Iterator(MemberElement_clientAirspace member) {
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

        public MemberElement_clientAirspace(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
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
            return new MemberElement_clientAirspace_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_clientAirport clientAirport;

    public static class MemberElement_clientAirport {

        public static class MemberElement_clientAirport_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_clientAirport member;

            public MemberElement_clientAirport_Iterator(MemberElement_clientAirport member) {
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
                org.mitre.caasd.aixm.aixm.AirportHeliportPropertyType nx = new org.mitre.caasd.aixm.aixm.AirportHeliportPropertyType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_clientAirport(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.AirportHeliportPropertyType at(int index) {
            return new org.mitre.caasd.aixm.aixm.AirportHeliportPropertyType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.AirportHeliportPropertyType first() {
            return new org.mitre.caasd.aixm.aixm.AirportHeliportPropertyType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.AirportHeliportPropertyType last() {
            return new org.mitre.caasd.aixm.aixm.AirportHeliportPropertyType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.AirportHeliportPropertyType append() {
            return new org.mitre.caasd.aixm.aixm.AirportHeliportPropertyType(owner.createElement(info));
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
            return new MemberElement_clientAirport_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_clientRoute clientRoute;

    public static class MemberElement_clientRoute {

        public static class MemberElement_clientRoute_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_clientRoute member;

            public MemberElement_clientRoute_Iterator(MemberElement_clientRoute member) {
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
                org.mitre.caasd.aixm.aixm.clientRouteType3 nx = new org.mitre.caasd.aixm.aixm.clientRouteType3(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_clientRoute(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.clientRouteType3 at(int index) {
            return new org.mitre.caasd.aixm.aixm.clientRouteType3(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.clientRouteType3 first() {
            return new org.mitre.caasd.aixm.aixm.clientRouteType3(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.clientRouteType3 last() {
            return new org.mitre.caasd.aixm.aixm.clientRouteType3(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.clientRouteType3 append() {
            return new org.mitre.caasd.aixm.aixm.clientRouteType3(owner.createElement(info));
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
            return new MemberElement_clientRoute_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_clientProcedure clientProcedure;

    public static class MemberElement_clientProcedure {

        public static class MemberElement_clientProcedure_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_clientProcedure member;

            public MemberElement_clientProcedure_Iterator(MemberElement_clientProcedure member) {
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
                org.mitre.caasd.aixm.aixm.ProcedurePropertyType nx = new org.mitre.caasd.aixm.aixm.ProcedurePropertyType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_clientProcedure(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.ProcedurePropertyType at(int index) {
            return new org.mitre.caasd.aixm.aixm.ProcedurePropertyType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.ProcedurePropertyType first() {
            return new org.mitre.caasd.aixm.aixm.ProcedurePropertyType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.ProcedurePropertyType last() {
            return new org.mitre.caasd.aixm.aixm.ProcedurePropertyType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.ProcedurePropertyType append() {
            return new org.mitre.caasd.aixm.aixm.ProcedurePropertyType(owner.createElement(info));
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
            return new MemberElement_clientProcedure_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_clientHolding clientHolding;

    public static class MemberElement_clientHolding {

        public static class MemberElement_clientHolding_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_clientHolding member;

            public MemberElement_clientHolding_Iterator(MemberElement_clientHolding member) {
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
                org.mitre.caasd.aixm.aixm.HoldingPatternPropertyType nx = new org.mitre.caasd.aixm.aixm.HoldingPatternPropertyType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_clientHolding(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.HoldingPatternPropertyType at(int index) {
            return new org.mitre.caasd.aixm.aixm.HoldingPatternPropertyType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.HoldingPatternPropertyType first() {
            return new org.mitre.caasd.aixm.aixm.HoldingPatternPropertyType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.HoldingPatternPropertyType last() {
            return new org.mitre.caasd.aixm.aixm.HoldingPatternPropertyType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.HoldingPatternPropertyType append() {
            return new org.mitre.caasd.aixm.aixm.HoldingPatternPropertyType(owner.createElement(info));
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
            return new MemberElement_clientHolding_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_clientAerialRefueling clientAerialRefueling;

    public static class MemberElement_clientAerialRefueling {

        public static class MemberElement_clientAerialRefueling_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_clientAerialRefueling member;

            public MemberElement_clientAerialRefueling_Iterator(MemberElement_clientAerialRefueling member) {
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
                org.mitre.caasd.aixm.aixm.AerialRefuelingPropertyType nx = new org.mitre.caasd.aixm.aixm.AerialRefuelingPropertyType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_clientAerialRefueling(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.AerialRefuelingPropertyType at(int index) {
            return new org.mitre.caasd.aixm.aixm.AerialRefuelingPropertyType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.AerialRefuelingPropertyType first() {
            return new org.mitre.caasd.aixm.aixm.AerialRefuelingPropertyType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.AerialRefuelingPropertyType last() {
            return new org.mitre.caasd.aixm.aixm.AerialRefuelingPropertyType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.AerialRefuelingPropertyType append() {
            return new org.mitre.caasd.aixm.aixm.AerialRefuelingPropertyType(owner.createElement(info));
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
            return new MemberElement_clientAerialRefueling_Iterator(this);
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
                org.mitre.caasd.aixm.aixm.extensionType101 nx = new org.mitre.caasd.aixm.aixm.extensionType101(nextNode);
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

        public org.mitre.caasd.aixm.aixm.extensionType101 at(int index) {
            return new org.mitre.caasd.aixm.aixm.extensionType101(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.extensionType101 first() {
            return new org.mitre.caasd.aixm.aixm.extensionType101(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.extensionType101 last() {
            return new org.mitre.caasd.aixm.aixm.extensionType101(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.extensionType101 append() {
            return new org.mitre.caasd.aixm.aixm.extensionType101(owner.createElement(info));
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
        org.mitre.caasd.aixm.xml.XmlTreeOperations.setAttribute(getNode(), "http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "http://www.aixm.aero/schema/5.0", "InformationServiceTimeSliceType");
    }
}
