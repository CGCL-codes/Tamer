package org.mitre.caasd.aixm.aixm;

public class RadioCommunicationChannelTimeSliceType extends org.mitre.caasd.aixm.aixm.AbstractAIXMTimeSliceType {

    public static org.mitre.caasd.aixm.xml.meta.ComplexType getStaticInfo() {
        return new org.mitre.caasd.aixm.xml.meta.ComplexType(org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getTypes()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_ti_aixm_altova_RadioCommunicationChannelTimeSliceType]);
    }

    public RadioCommunicationChannelTimeSliceType(org.w3c.dom.Node init) {
        super(init);
        instantiateMembers();
    }

    private void instantiateMembers() {
        mode = new MemberElement_mode(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_RadioCommunicationChannelTimeSliceType._mode]);
        rank = new MemberElement_rank(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_RadioCommunicationChannelTimeSliceType._rank]);
        frequencyTransmission = new MemberElement_frequencyTransmission(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_RadioCommunicationChannelTimeSliceType._frequencyTransmission]);
        frequencyReception = new MemberElement_frequencyReception(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_RadioCommunicationChannelTimeSliceType._frequencyReception]);
        channel = new MemberElement_channel(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_RadioCommunicationChannelTimeSliceType._channel]);
        logon = new MemberElement_logon(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_RadioCommunicationChannelTimeSliceType._logon]);
        emmissionType = new MemberElement_emmissionType(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_RadioCommunicationChannelTimeSliceType._emmissionType]);
        selectiveCall = new MemberElement_selectiveCall(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_RadioCommunicationChannelTimeSliceType._selectiveCall]);
        flightChecked = new MemberElement_flightChecked(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_RadioCommunicationChannelTimeSliceType._flightChecked]);
        operationalStatus = new MemberElement_operationalStatus(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_RadioCommunicationChannelTimeSliceType._operationalStatus]);
        trafficDirection = new MemberElement_trafficDirection(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_RadioCommunicationChannelTimeSliceType._trafficDirection]);
        workingHours = new MemberElement_workingHours(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_RadioCommunicationChannelTimeSliceType._workingHours]);
        location = new MemberElement_location(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_RadioCommunicationChannelTimeSliceType._location]);
        annotation = new MemberElement_annotation(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_RadioCommunicationChannelTimeSliceType._annotation]);
        extension = new MemberElement_extension(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_RadioCommunicationChannelTimeSliceType._extension]);
    }

    public MemberElement_mode mode;

    public static class MemberElement_mode {

        public static class MemberElement_mode_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_mode member;

            public MemberElement_mode_Iterator(MemberElement_mode member) {
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
                org.mitre.caasd.aixm.aixm.modeType nx = new org.mitre.caasd.aixm.aixm.modeType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_mode(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.modeType at(int index) {
            return new org.mitre.caasd.aixm.aixm.modeType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.modeType first() {
            return new org.mitre.caasd.aixm.aixm.modeType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.modeType last() {
            return new org.mitre.caasd.aixm.aixm.modeType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.modeType append() {
            return new org.mitre.caasd.aixm.aixm.modeType(owner.createElement(info));
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
            return new MemberElement_mode_Iterator(this);
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
                org.mitre.caasd.aixm.aixm.rankType2 nx = new org.mitre.caasd.aixm.aixm.rankType2(nextNode);
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

        public org.mitre.caasd.aixm.aixm.rankType2 at(int index) {
            return new org.mitre.caasd.aixm.aixm.rankType2(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.rankType2 first() {
            return new org.mitre.caasd.aixm.aixm.rankType2(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.rankType2 last() {
            return new org.mitre.caasd.aixm.aixm.rankType2(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.rankType2 append() {
            return new org.mitre.caasd.aixm.aixm.rankType2(owner.createElement(info));
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

    public MemberElement_frequencyTransmission frequencyTransmission;

    public static class MemberElement_frequencyTransmission {

        public static class MemberElement_frequencyTransmission_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_frequencyTransmission member;

            public MemberElement_frequencyTransmission_Iterator(MemberElement_frequencyTransmission member) {
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
                org.mitre.caasd.aixm.aixm.frequencyTransmissionType nx = new org.mitre.caasd.aixm.aixm.frequencyTransmissionType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_frequencyTransmission(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.frequencyTransmissionType at(int index) {
            return new org.mitre.caasd.aixm.aixm.frequencyTransmissionType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.frequencyTransmissionType first() {
            return new org.mitre.caasd.aixm.aixm.frequencyTransmissionType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.frequencyTransmissionType last() {
            return new org.mitre.caasd.aixm.aixm.frequencyTransmissionType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.frequencyTransmissionType append() {
            return new org.mitre.caasd.aixm.aixm.frequencyTransmissionType(owner.createElement(info));
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
            return new MemberElement_frequencyTransmission_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_frequencyReception frequencyReception;

    public static class MemberElement_frequencyReception {

        public static class MemberElement_frequencyReception_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_frequencyReception member;

            public MemberElement_frequencyReception_Iterator(MemberElement_frequencyReception member) {
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
                org.mitre.caasd.aixm.aixm.frequencyReceptionType nx = new org.mitre.caasd.aixm.aixm.frequencyReceptionType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_frequencyReception(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.frequencyReceptionType at(int index) {
            return new org.mitre.caasd.aixm.aixm.frequencyReceptionType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.frequencyReceptionType first() {
            return new org.mitre.caasd.aixm.aixm.frequencyReceptionType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.frequencyReceptionType last() {
            return new org.mitre.caasd.aixm.aixm.frequencyReceptionType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.frequencyReceptionType append() {
            return new org.mitre.caasd.aixm.aixm.frequencyReceptionType(owner.createElement(info));
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
            return new MemberElement_frequencyReception_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_channel channel;

    public static class MemberElement_channel {

        public static class MemberElement_channel_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_channel member;

            public MemberElement_channel_Iterator(MemberElement_channel member) {
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
                org.mitre.caasd.aixm.aixm.channelType3 nx = new org.mitre.caasd.aixm.aixm.channelType3(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_channel(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.channelType3 at(int index) {
            return new org.mitre.caasd.aixm.aixm.channelType3(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.channelType3 first() {
            return new org.mitre.caasd.aixm.aixm.channelType3(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.channelType3 last() {
            return new org.mitre.caasd.aixm.aixm.channelType3(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.channelType3 append() {
            return new org.mitre.caasd.aixm.aixm.channelType3(owner.createElement(info));
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
            return new MemberElement_channel_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_logon logon;

    public static class MemberElement_logon {

        public static class MemberElement_logon_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_logon member;

            public MemberElement_logon_Iterator(MemberElement_logon member) {
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
                org.mitre.caasd.aixm.aixm.logonType nx = new org.mitre.caasd.aixm.aixm.logonType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_logon(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.logonType at(int index) {
            return new org.mitre.caasd.aixm.aixm.logonType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.logonType first() {
            return new org.mitre.caasd.aixm.aixm.logonType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.logonType last() {
            return new org.mitre.caasd.aixm.aixm.logonType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.logonType append() {
            return new org.mitre.caasd.aixm.aixm.logonType(owner.createElement(info));
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
            return new MemberElement_logon_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_emmissionType emmissionType;

    public static class MemberElement_emmissionType {

        public static class MemberElement_emmissionType_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_emmissionType member;

            public MemberElement_emmissionType_Iterator(MemberElement_emmissionType member) {
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
                org.mitre.caasd.aixm.aixm.emmissionTypeType nx = new org.mitre.caasd.aixm.aixm.emmissionTypeType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_emmissionType(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.emmissionTypeType at(int index) {
            return new org.mitre.caasd.aixm.aixm.emmissionTypeType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.emmissionTypeType first() {
            return new org.mitre.caasd.aixm.aixm.emmissionTypeType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.emmissionTypeType last() {
            return new org.mitre.caasd.aixm.aixm.emmissionTypeType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.emmissionTypeType append() {
            return new org.mitre.caasd.aixm.aixm.emmissionTypeType(owner.createElement(info));
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
            return new MemberElement_emmissionType_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_selectiveCall selectiveCall;

    public static class MemberElement_selectiveCall {

        public static class MemberElement_selectiveCall_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_selectiveCall member;

            public MemberElement_selectiveCall_Iterator(MemberElement_selectiveCall member) {
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
                org.mitre.caasd.aixm.aixm.selectiveCallType nx = new org.mitre.caasd.aixm.aixm.selectiveCallType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_selectiveCall(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.selectiveCallType at(int index) {
            return new org.mitre.caasd.aixm.aixm.selectiveCallType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.selectiveCallType first() {
            return new org.mitre.caasd.aixm.aixm.selectiveCallType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.selectiveCallType last() {
            return new org.mitre.caasd.aixm.aixm.selectiveCallType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.selectiveCallType append() {
            return new org.mitre.caasd.aixm.aixm.selectiveCallType(owner.createElement(info));
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
            return new MemberElement_selectiveCall_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_flightChecked flightChecked;

    public static class MemberElement_flightChecked {

        public static class MemberElement_flightChecked_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_flightChecked member;

            public MemberElement_flightChecked_Iterator(MemberElement_flightChecked member) {
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
                org.mitre.caasd.aixm.aixm.flightCheckedType4 nx = new org.mitre.caasd.aixm.aixm.flightCheckedType4(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_flightChecked(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.flightCheckedType4 at(int index) {
            return new org.mitre.caasd.aixm.aixm.flightCheckedType4(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.flightCheckedType4 first() {
            return new org.mitre.caasd.aixm.aixm.flightCheckedType4(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.flightCheckedType4 last() {
            return new org.mitre.caasd.aixm.aixm.flightCheckedType4(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.flightCheckedType4 append() {
            return new org.mitre.caasd.aixm.aixm.flightCheckedType4(owner.createElement(info));
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
            return new MemberElement_flightChecked_Iterator(this);
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
                org.mitre.caasd.aixm.aixm.operationalStatusType6 nx = new org.mitre.caasd.aixm.aixm.operationalStatusType6(nextNode);
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

        public org.mitre.caasd.aixm.aixm.operationalStatusType6 at(int index) {
            return new org.mitre.caasd.aixm.aixm.operationalStatusType6(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.operationalStatusType6 first() {
            return new org.mitre.caasd.aixm.aixm.operationalStatusType6(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.operationalStatusType6 last() {
            return new org.mitre.caasd.aixm.aixm.operationalStatusType6(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.operationalStatusType6 append() {
            return new org.mitre.caasd.aixm.aixm.operationalStatusType6(owner.createElement(info));
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

    public MemberElement_trafficDirection trafficDirection;

    public static class MemberElement_trafficDirection {

        public static class MemberElement_trafficDirection_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_trafficDirection member;

            public MemberElement_trafficDirection_Iterator(MemberElement_trafficDirection member) {
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
                org.mitre.caasd.aixm.aixm.trafficDirectionType nx = new org.mitre.caasd.aixm.aixm.trafficDirectionType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_trafficDirection(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.trafficDirectionType at(int index) {
            return new org.mitre.caasd.aixm.aixm.trafficDirectionType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.trafficDirectionType first() {
            return new org.mitre.caasd.aixm.aixm.trafficDirectionType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.trafficDirectionType last() {
            return new org.mitre.caasd.aixm.aixm.trafficDirectionType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.trafficDirectionType append() {
            return new org.mitre.caasd.aixm.aixm.trafficDirectionType(owner.createElement(info));
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
            return new MemberElement_trafficDirection_Iterator(this);
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
                org.mitre.caasd.aixm.aixm.workingHoursType2 nx = new org.mitre.caasd.aixm.aixm.workingHoursType2(nextNode);
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

        public org.mitre.caasd.aixm.aixm.workingHoursType2 at(int index) {
            return new org.mitre.caasd.aixm.aixm.workingHoursType2(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.workingHoursType2 first() {
            return new org.mitre.caasd.aixm.aixm.workingHoursType2(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.workingHoursType2 last() {
            return new org.mitre.caasd.aixm.aixm.workingHoursType2(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.workingHoursType2 append() {
            return new org.mitre.caasd.aixm.aixm.workingHoursType2(owner.createElement(info));
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
                org.mitre.caasd.aixm.aixm.locationType11 nx = new org.mitre.caasd.aixm.aixm.locationType11(nextNode);
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

        public org.mitre.caasd.aixm.aixm.locationType11 at(int index) {
            return new org.mitre.caasd.aixm.aixm.locationType11(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.locationType11 first() {
            return new org.mitre.caasd.aixm.aixm.locationType11(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.locationType11 last() {
            return new org.mitre.caasd.aixm.aixm.locationType11(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.locationType11 append() {
            return new org.mitre.caasd.aixm.aixm.locationType11(owner.createElement(info));
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
                org.mitre.caasd.aixm.aixm.annotationType115 nx = new org.mitre.caasd.aixm.aixm.annotationType115(nextNode);
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

        public org.mitre.caasd.aixm.aixm.annotationType115 at(int index) {
            return new org.mitre.caasd.aixm.aixm.annotationType115(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.annotationType115 first() {
            return new org.mitre.caasd.aixm.aixm.annotationType115(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.annotationType115 last() {
            return new org.mitre.caasd.aixm.aixm.annotationType115(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.annotationType115 append() {
            return new org.mitre.caasd.aixm.aixm.annotationType115(owner.createElement(info));
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
                org.mitre.caasd.aixm.aixm.extensionType149 nx = new org.mitre.caasd.aixm.aixm.extensionType149(nextNode);
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

        public org.mitre.caasd.aixm.aixm.extensionType149 at(int index) {
            return new org.mitre.caasd.aixm.aixm.extensionType149(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.extensionType149 first() {
            return new org.mitre.caasd.aixm.aixm.extensionType149(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.extensionType149 last() {
            return new org.mitre.caasd.aixm.aixm.extensionType149(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.extensionType149 append() {
            return new org.mitre.caasd.aixm.aixm.extensionType149(owner.createElement(info));
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
        org.mitre.caasd.aixm.xml.XmlTreeOperations.setAttribute(getNode(), "http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "http://www.aixm.aero/schema/5.0", "RadioCommunicationChannelTimeSliceType");
    }
}
