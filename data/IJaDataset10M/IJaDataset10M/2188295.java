package org.mitre.caasd.aixm.arpt;

public class VisualGlideSlopeIndicatorExtensionType extends org.mitre.caasd.aixm.aixm.AbstractExtensionType {

    public static org.mitre.caasd.aixm.xml.meta.ComplexType getStaticInfo() {
        return new org.mitre.caasd.aixm.xml.meta.ComplexType(org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getTypes()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_ti_arpt_altova_VisualGlideSlopeIndicatorExtensionType]);
    }

    public VisualGlideSlopeIndicatorExtensionType(org.w3c.dom.Node init) {
        super(init);
        instantiateMembers();
    }

    private void instantiateMembers() {
        thresholdCrossingHeight = new MemberElement_thresholdCrossingHeight(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_arpt_altova_VisualGlideSlopeIndicatorExtensionType._thresholdCrossingHeight]);
        nonStandard = new MemberElement_nonStandard(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_arpt_altova_VisualGlideSlopeIndicatorExtensionType._nonStandard]);
        commissionDate = new MemberElement_commissionDate(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_arpt_altova_VisualGlideSlopeIndicatorExtensionType._commissionDate]);
        ownerCode = new MemberElement_ownerCode(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_arpt_altova_VisualGlideSlopeIndicatorExtensionType._ownerCode]);
        highAngleMsmt = new MemberElement_highAngleMsmt(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_arpt_altova_VisualGlideSlopeIndicatorExtensionType._highAngleMsmt]);
        thldDistance = new MemberElement_thldDistance(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_arpt_altova_VisualGlideSlopeIndicatorExtensionType._thldDistance]);
        length = new MemberElement_length(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_arpt_altova_VisualGlideSlopeIndicatorExtensionType._length]);
        pilotControlFrequency = new MemberElement_pilotControlFrequency(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_arpt_altova_VisualGlideSlopeIndicatorExtensionType._pilotControlFrequency]);
        dwbElevation = new MemberElement_dwbElevation(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_arpt_altova_VisualGlideSlopeIndicatorExtensionType._dwbElevation]);
        dwbThldDistance = new MemberElement_dwbThldDistance(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_arpt_altova_VisualGlideSlopeIndicatorExtensionType._dwbThldDistance]);
        faaType = new MemberElement_faaType(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_arpt_altova_VisualGlideSlopeIndicatorExtensionType._faaType]);
        elevatedPoint = new MemberElement_elevatedPoint(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_arpt_altova_VisualGlideSlopeIndicatorExtensionType._elevatedPoint]);
        featureExtension = new MemberElement_featureExtension(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_arpt_altova_VisualGlideSlopeIndicatorExtensionType._featureExtension]);
    }

    public MemberElement_thresholdCrossingHeight thresholdCrossingHeight;

    public static class MemberElement_thresholdCrossingHeight {

        public static class MemberElement_thresholdCrossingHeight_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_thresholdCrossingHeight member;

            public MemberElement_thresholdCrossingHeight_Iterator(MemberElement_thresholdCrossingHeight member) {
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
                org.mitre.caasd.aixm.arpt.thresholdCrossingHeightType nx = new org.mitre.caasd.aixm.arpt.thresholdCrossingHeightType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_thresholdCrossingHeight(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.arpt.thresholdCrossingHeightType at(int index) {
            return new org.mitre.caasd.aixm.arpt.thresholdCrossingHeightType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.arpt.thresholdCrossingHeightType first() {
            return new org.mitre.caasd.aixm.arpt.thresholdCrossingHeightType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.arpt.thresholdCrossingHeightType last() {
            return new org.mitre.caasd.aixm.arpt.thresholdCrossingHeightType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.arpt.thresholdCrossingHeightType append() {
            return new org.mitre.caasd.aixm.arpt.thresholdCrossingHeightType(owner.createElement(info));
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
            return new MemberElement_thresholdCrossingHeight_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_nonStandard nonStandard;

    public static class MemberElement_nonStandard {

        public static class MemberElement_nonStandard_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_nonStandard member;

            public MemberElement_nonStandard_Iterator(MemberElement_nonStandard member) {
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
                org.mitre.caasd.aixm.arpt.nonStandardType3 nx = new org.mitre.caasd.aixm.arpt.nonStandardType3(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_nonStandard(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.arpt.nonStandardType3 at(int index) {
            return new org.mitre.caasd.aixm.arpt.nonStandardType3(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.arpt.nonStandardType3 first() {
            return new org.mitre.caasd.aixm.arpt.nonStandardType3(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.arpt.nonStandardType3 last() {
            return new org.mitre.caasd.aixm.arpt.nonStandardType3(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.arpt.nonStandardType3 append() {
            return new org.mitre.caasd.aixm.arpt.nonStandardType3(owner.createElement(info));
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
            return new MemberElement_nonStandard_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_commissionDate commissionDate;

    public static class MemberElement_commissionDate {

        public static class MemberElement_commissionDate_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_commissionDate member;

            public MemberElement_commissionDate_Iterator(MemberElement_commissionDate member) {
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
                org.mitre.caasd.aixm.arpt.commissionDateType3 nx = new org.mitre.caasd.aixm.arpt.commissionDateType3(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_commissionDate(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.arpt.commissionDateType3 at(int index) {
            return new org.mitre.caasd.aixm.arpt.commissionDateType3(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.arpt.commissionDateType3 first() {
            return new org.mitre.caasd.aixm.arpt.commissionDateType3(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.arpt.commissionDateType3 last() {
            return new org.mitre.caasd.aixm.arpt.commissionDateType3(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.arpt.commissionDateType3 append() {
            return new org.mitre.caasd.aixm.arpt.commissionDateType3(owner.createElement(info));
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
            return new MemberElement_commissionDate_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_ownerCode ownerCode;

    public static class MemberElement_ownerCode {

        public static class MemberElement_ownerCode_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_ownerCode member;

            public MemberElement_ownerCode_Iterator(MemberElement_ownerCode member) {
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
                org.mitre.caasd.aixm.arpt.ownerCodeType3 nx = new org.mitre.caasd.aixm.arpt.ownerCodeType3(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_ownerCode(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.arpt.ownerCodeType3 at(int index) {
            return new org.mitre.caasd.aixm.arpt.ownerCodeType3(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.arpt.ownerCodeType3 first() {
            return new org.mitre.caasd.aixm.arpt.ownerCodeType3(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.arpt.ownerCodeType3 last() {
            return new org.mitre.caasd.aixm.arpt.ownerCodeType3(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.arpt.ownerCodeType3 append() {
            return new org.mitre.caasd.aixm.arpt.ownerCodeType3(owner.createElement(info));
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
            return new MemberElement_ownerCode_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_highAngleMsmt highAngleMsmt;

    public static class MemberElement_highAngleMsmt {

        public static class MemberElement_highAngleMsmt_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_highAngleMsmt member;

            public MemberElement_highAngleMsmt_Iterator(MemberElement_highAngleMsmt member) {
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
                org.mitre.caasd.aixm.arpt.highAngleMsmtType nx = new org.mitre.caasd.aixm.arpt.highAngleMsmtType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_highAngleMsmt(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.arpt.highAngleMsmtType at(int index) {
            return new org.mitre.caasd.aixm.arpt.highAngleMsmtType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.arpt.highAngleMsmtType first() {
            return new org.mitre.caasd.aixm.arpt.highAngleMsmtType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.arpt.highAngleMsmtType last() {
            return new org.mitre.caasd.aixm.arpt.highAngleMsmtType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.arpt.highAngleMsmtType append() {
            return new org.mitre.caasd.aixm.arpt.highAngleMsmtType(owner.createElement(info));
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
            return new MemberElement_highAngleMsmt_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_thldDistance thldDistance;

    public static class MemberElement_thldDistance {

        public static class MemberElement_thldDistance_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_thldDistance member;

            public MemberElement_thldDistance_Iterator(MemberElement_thldDistance member) {
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
                org.mitre.caasd.aixm.arpt.thldDistanceType2 nx = new org.mitre.caasd.aixm.arpt.thldDistanceType2(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_thldDistance(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.arpt.thldDistanceType2 at(int index) {
            return new org.mitre.caasd.aixm.arpt.thldDistanceType2(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.arpt.thldDistanceType2 first() {
            return new org.mitre.caasd.aixm.arpt.thldDistanceType2(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.arpt.thldDistanceType2 last() {
            return new org.mitre.caasd.aixm.arpt.thldDistanceType2(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.arpt.thldDistanceType2 append() {
            return new org.mitre.caasd.aixm.arpt.thldDistanceType2(owner.createElement(info));
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
            return new MemberElement_thldDistance_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_length length;

    public static class MemberElement_length {

        public static class MemberElement_length_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_length member;

            public MemberElement_length_Iterator(MemberElement_length member) {
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
                org.mitre.caasd.aixm.arpt.lengthType2 nx = new org.mitre.caasd.aixm.arpt.lengthType2(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_length(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.arpt.lengthType2 at(int index) {
            return new org.mitre.caasd.aixm.arpt.lengthType2(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.arpt.lengthType2 first() {
            return new org.mitre.caasd.aixm.arpt.lengthType2(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.arpt.lengthType2 last() {
            return new org.mitre.caasd.aixm.arpt.lengthType2(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.arpt.lengthType2 append() {
            return new org.mitre.caasd.aixm.arpt.lengthType2(owner.createElement(info));
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
            return new MemberElement_length_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_pilotControlFrequency pilotControlFrequency;

    public static class MemberElement_pilotControlFrequency {

        public static class MemberElement_pilotControlFrequency_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_pilotControlFrequency member;

            public MemberElement_pilotControlFrequency_Iterator(MemberElement_pilotControlFrequency member) {
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
                org.mitre.caasd.aixm.arpt.pilotControlFrequencyType2 nx = new org.mitre.caasd.aixm.arpt.pilotControlFrequencyType2(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_pilotControlFrequency(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.arpt.pilotControlFrequencyType2 at(int index) {
            return new org.mitre.caasd.aixm.arpt.pilotControlFrequencyType2(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.arpt.pilotControlFrequencyType2 first() {
            return new org.mitre.caasd.aixm.arpt.pilotControlFrequencyType2(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.arpt.pilotControlFrequencyType2 last() {
            return new org.mitre.caasd.aixm.arpt.pilotControlFrequencyType2(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.arpt.pilotControlFrequencyType2 append() {
            return new org.mitre.caasd.aixm.arpt.pilotControlFrequencyType2(owner.createElement(info));
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
            return new MemberElement_pilotControlFrequency_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_dwbElevation dwbElevation;

    public static class MemberElement_dwbElevation {

        public static class MemberElement_dwbElevation_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_dwbElevation member;

            public MemberElement_dwbElevation_Iterator(MemberElement_dwbElevation member) {
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
                org.mitre.caasd.aixm.arpt.dwbElevationType nx = new org.mitre.caasd.aixm.arpt.dwbElevationType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_dwbElevation(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.arpt.dwbElevationType at(int index) {
            return new org.mitre.caasd.aixm.arpt.dwbElevationType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.arpt.dwbElevationType first() {
            return new org.mitre.caasd.aixm.arpt.dwbElevationType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.arpt.dwbElevationType last() {
            return new org.mitre.caasd.aixm.arpt.dwbElevationType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.arpt.dwbElevationType append() {
            return new org.mitre.caasd.aixm.arpt.dwbElevationType(owner.createElement(info));
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
            return new MemberElement_dwbElevation_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_dwbThldDistance dwbThldDistance;

    public static class MemberElement_dwbThldDistance {

        public static class MemberElement_dwbThldDistance_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_dwbThldDistance member;

            public MemberElement_dwbThldDistance_Iterator(MemberElement_dwbThldDistance member) {
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
                org.mitre.caasd.aixm.arpt.dwbThldDistanceType nx = new org.mitre.caasd.aixm.arpt.dwbThldDistanceType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_dwbThldDistance(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.arpt.dwbThldDistanceType at(int index) {
            return new org.mitre.caasd.aixm.arpt.dwbThldDistanceType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.arpt.dwbThldDistanceType first() {
            return new org.mitre.caasd.aixm.arpt.dwbThldDistanceType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.arpt.dwbThldDistanceType last() {
            return new org.mitre.caasd.aixm.arpt.dwbThldDistanceType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.arpt.dwbThldDistanceType append() {
            return new org.mitre.caasd.aixm.arpt.dwbThldDistanceType(owner.createElement(info));
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
            return new MemberElement_dwbThldDistance_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_faaType faaType;

    public static class MemberElement_faaType {

        public static class MemberElement_faaType_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_faaType member;

            public MemberElement_faaType_Iterator(MemberElement_faaType member) {
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
                org.mitre.caasd.aixm.arpt.faaTypeType3 nx = new org.mitre.caasd.aixm.arpt.faaTypeType3(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_faaType(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.arpt.faaTypeType3 at(int index) {
            return new org.mitre.caasd.aixm.arpt.faaTypeType3(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.arpt.faaTypeType3 first() {
            return new org.mitre.caasd.aixm.arpt.faaTypeType3(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.arpt.faaTypeType3 last() {
            return new org.mitre.caasd.aixm.arpt.faaTypeType3(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.arpt.faaTypeType3 append() {
            return new org.mitre.caasd.aixm.arpt.faaTypeType3(owner.createElement(info));
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
            return new MemberElement_faaType_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_elevatedPoint elevatedPoint;

    public static class MemberElement_elevatedPoint {

        public static class MemberElement_elevatedPoint_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_elevatedPoint member;

            public MemberElement_elevatedPoint_Iterator(MemberElement_elevatedPoint member) {
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
                org.mitre.caasd.aixm.arpt.elevatedPointType nx = new org.mitre.caasd.aixm.arpt.elevatedPointType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_elevatedPoint(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.arpt.elevatedPointType at(int index) {
            return new org.mitre.caasd.aixm.arpt.elevatedPointType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.arpt.elevatedPointType first() {
            return new org.mitre.caasd.aixm.arpt.elevatedPointType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.arpt.elevatedPointType last() {
            return new org.mitre.caasd.aixm.arpt.elevatedPointType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.arpt.elevatedPointType append() {
            return new org.mitre.caasd.aixm.arpt.elevatedPointType(owner.createElement(info));
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
            return new MemberElement_elevatedPoint_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_featureExtension featureExtension;

    public static class MemberElement_featureExtension {

        public static class MemberElement_featureExtension_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_featureExtension member;

            public MemberElement_featureExtension_Iterator(MemberElement_featureExtension member) {
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
                org.mitre.caasd.aixm.arpt.featureExtensionType17 nx = new org.mitre.caasd.aixm.arpt.featureExtensionType17(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_featureExtension(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.arpt.featureExtensionType17 at(int index) {
            return new org.mitre.caasd.aixm.arpt.featureExtensionType17(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.arpt.featureExtensionType17 first() {
            return new org.mitre.caasd.aixm.arpt.featureExtensionType17(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.arpt.featureExtensionType17 last() {
            return new org.mitre.caasd.aixm.arpt.featureExtensionType17(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.arpt.featureExtensionType17 append() {
            return new org.mitre.caasd.aixm.arpt.featureExtensionType17(owner.createElement(info));
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
            return new MemberElement_featureExtension_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public void setXsiType() {
        org.mitre.caasd.aixm.xml.XmlTreeOperations.setAttribute(getNode(), "http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "http://www.faa.gov/FPP/airports", "VisualGlideSlopeIndicatorExtensionType");
    }
}
