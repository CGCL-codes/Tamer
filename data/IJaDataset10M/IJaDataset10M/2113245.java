package org.mitre.caasd.aixm.proc;

public class InstrumentApproachProcedureExtensionType extends org.mitre.caasd.aixm.aixm.AbstractExtensionType {

    public static org.mitre.caasd.aixm.xml.meta.ComplexType getStaticInfo() {
        return new org.mitre.caasd.aixm.xml.meta.ComplexType(org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getTypes()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_ti_proc_altova_InstrumentApproachProcedureExtensionType]);
    }

    public InstrumentApproachProcedureExtensionType(org.w3c.dom.Node init) {
        super(init);
        instantiateMembers();
    }

    private void instantiateMembers() {
        channelNumberType = new MemberElement_channelNumberType(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_proc_altova_InstrumentApproachProcedureExtensionType._channelNumberType]);
        ownerCode = new MemberElement_ownerCode(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_proc_altova_InstrumentApproachProcedureExtensionType._ownerCode]);
        alignmentType = new MemberElement_alignmentType(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_proc_altova_InstrumentApproachProcedureExtensionType._alignmentType]);
        featureExtension = new MemberElement_featureExtension(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_proc_altova_InstrumentApproachProcedureExtensionType._featureExtension]);
        attachments = new MemberElement_attachments(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_proc_altova_InstrumentApproachProcedureExtensionType._attachments]);
    }

    public MemberElement_channelNumberType channelNumberType;

    public static class MemberElement_channelNumberType {

        public static class MemberElement_channelNumberType_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_channelNumberType member;

            public MemberElement_channelNumberType_Iterator(MemberElement_channelNumberType member) {
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
                org.mitre.caasd.aixm.proc.channelNumberTypeType nx = new org.mitre.caasd.aixm.proc.channelNumberTypeType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_channelNumberType(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.proc.channelNumberTypeType at(int index) {
            return new org.mitre.caasd.aixm.proc.channelNumberTypeType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.proc.channelNumberTypeType first() {
            return new org.mitre.caasd.aixm.proc.channelNumberTypeType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.proc.channelNumberTypeType last() {
            return new org.mitre.caasd.aixm.proc.channelNumberTypeType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.proc.channelNumberTypeType append() {
            return new org.mitre.caasd.aixm.proc.channelNumberTypeType(owner.createElement(info));
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
            return new MemberElement_channelNumberType_Iterator(this);
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
                org.mitre.caasd.aixm.proc.ownerCodeType nx = new org.mitre.caasd.aixm.proc.ownerCodeType(nextNode);
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

        public org.mitre.caasd.aixm.proc.ownerCodeType at(int index) {
            return new org.mitre.caasd.aixm.proc.ownerCodeType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.proc.ownerCodeType first() {
            return new org.mitre.caasd.aixm.proc.ownerCodeType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.proc.ownerCodeType last() {
            return new org.mitre.caasd.aixm.proc.ownerCodeType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.proc.ownerCodeType append() {
            return new org.mitre.caasd.aixm.proc.ownerCodeType(owner.createElement(info));
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

    public MemberElement_alignmentType alignmentType;

    public static class MemberElement_alignmentType {

        public static class MemberElement_alignmentType_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_alignmentType member;

            public MemberElement_alignmentType_Iterator(MemberElement_alignmentType member) {
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
                org.mitre.caasd.aixm.proc.alignmentTypeType nx = new org.mitre.caasd.aixm.proc.alignmentTypeType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_alignmentType(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.proc.alignmentTypeType at(int index) {
            return new org.mitre.caasd.aixm.proc.alignmentTypeType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.proc.alignmentTypeType first() {
            return new org.mitre.caasd.aixm.proc.alignmentTypeType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.proc.alignmentTypeType last() {
            return new org.mitre.caasd.aixm.proc.alignmentTypeType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.proc.alignmentTypeType append() {
            return new org.mitre.caasd.aixm.proc.alignmentTypeType(owner.createElement(info));
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
            return new MemberElement_alignmentType_Iterator(this);
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
                org.mitre.caasd.aixm.proc.featureExtensionType2 nx = new org.mitre.caasd.aixm.proc.featureExtensionType2(nextNode);
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

        public org.mitre.caasd.aixm.proc.featureExtensionType2 at(int index) {
            return new org.mitre.caasd.aixm.proc.featureExtensionType2(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.proc.featureExtensionType2 first() {
            return new org.mitre.caasd.aixm.proc.featureExtensionType2(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.proc.featureExtensionType2 last() {
            return new org.mitre.caasd.aixm.proc.featureExtensionType2(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.proc.featureExtensionType2 append() {
            return new org.mitre.caasd.aixm.proc.featureExtensionType2(owner.createElement(info));
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

    public MemberElement_attachments attachments;

    public static class MemberElement_attachments {

        public static class MemberElement_attachments_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_attachments member;

            public MemberElement_attachments_Iterator(MemberElement_attachments member) {
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
                org.mitre.caasd.aixm.proc.attachmentsType nx = new org.mitre.caasd.aixm.proc.attachmentsType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_attachments(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.proc.attachmentsType at(int index) {
            return new org.mitre.caasd.aixm.proc.attachmentsType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.proc.attachmentsType first() {
            return new org.mitre.caasd.aixm.proc.attachmentsType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.proc.attachmentsType last() {
            return new org.mitre.caasd.aixm.proc.attachmentsType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.proc.attachmentsType append() {
            return new org.mitre.caasd.aixm.proc.attachmentsType(owner.createElement(info));
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
            return new MemberElement_attachments_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public void setXsiType() {
        org.mitre.caasd.aixm.xml.XmlTreeOperations.setAttribute(getNode(), "http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "http://www.faa.gov/FPP/procedures", "InstrumentApproachProcedureExtensionType");
    }
}
