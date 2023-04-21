package org.mitre.caasd.aixm.aixm;

public class ProcedureTransitionType extends org.mitre.caasd.aixm.xml.TypeBase {

    public static org.mitre.caasd.aixm.xml.meta.ComplexType getStaticInfo() {
        return new org.mitre.caasd.aixm.xml.meta.ComplexType(org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getTypes()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_ti_aixm_altova_ProcedureTransitionType]);
    }

    public ProcedureTransitionType(org.w3c.dom.Node init) {
        super(init);
        instantiateMembers();
    }

    private void instantiateMembers() {
        transitionId = new MemberElement_transitionId(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_ProcedureTransitionType._transitionId]);
        type = new MemberElement_type(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_ProcedureTransitionType._type]);
        description = new MemberElement_description(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_ProcedureTransitionType._description]);
        vectorHeading = new MemberElement_vectorHeading(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_ProcedureTransitionType._vectorHeading]);
        departureRunwayTransition = new MemberElement_departureRunwayTransition(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_ProcedureTransitionType._departureRunwayTransition]);
        trajectory = new MemberElement_trajectory(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_ProcedureTransitionType._trajectory]);
        transitionLeg = new MemberElement_transitionLeg(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_ProcedureTransitionType._transitionLeg]);
        annotation = new MemberElement_annotation(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_ProcedureTransitionType._annotation]);
        extension = new MemberElement_extension(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_ProcedureTransitionType._extension]);
    }

    public MemberElement_transitionId transitionId;

    public static class MemberElement_transitionId {

        public static class MemberElement_transitionId_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_transitionId member;

            public MemberElement_transitionId_Iterator(MemberElement_transitionId member) {
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
                org.mitre.caasd.aixm.aixm.transitionIdType nx = new org.mitre.caasd.aixm.aixm.transitionIdType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_transitionId(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.transitionIdType at(int index) {
            return new org.mitre.caasd.aixm.aixm.transitionIdType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.transitionIdType first() {
            return new org.mitre.caasd.aixm.aixm.transitionIdType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.transitionIdType last() {
            return new org.mitre.caasd.aixm.aixm.transitionIdType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.transitionIdType append() {
            return new org.mitre.caasd.aixm.aixm.transitionIdType(owner.createElement(info));
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
            return new MemberElement_transitionId_Iterator(this);
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
                org.mitre.caasd.aixm.aixm.typeType46 nx = new org.mitre.caasd.aixm.aixm.typeType46(nextNode);
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

        public org.mitre.caasd.aixm.aixm.typeType46 at(int index) {
            return new org.mitre.caasd.aixm.aixm.typeType46(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.typeType46 first() {
            return new org.mitre.caasd.aixm.aixm.typeType46(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.typeType46 last() {
            return new org.mitre.caasd.aixm.aixm.typeType46(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.typeType46 append() {
            return new org.mitre.caasd.aixm.aixm.typeType46(owner.createElement(info));
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

    public MemberElement_description description;

    public static class MemberElement_description {

        public static class MemberElement_description_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_description member;

            public MemberElement_description_Iterator(MemberElement_description member) {
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
                org.mitre.caasd.aixm.aixm.descriptionType13 nx = new org.mitre.caasd.aixm.aixm.descriptionType13(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_description(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.descriptionType13 at(int index) {
            return new org.mitre.caasd.aixm.aixm.descriptionType13(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.descriptionType13 first() {
            return new org.mitre.caasd.aixm.aixm.descriptionType13(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.descriptionType13 last() {
            return new org.mitre.caasd.aixm.aixm.descriptionType13(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.descriptionType13 append() {
            return new org.mitre.caasd.aixm.aixm.descriptionType13(owner.createElement(info));
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
            return new MemberElement_description_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_vectorHeading vectorHeading;

    public static class MemberElement_vectorHeading {

        public static class MemberElement_vectorHeading_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_vectorHeading member;

            public MemberElement_vectorHeading_Iterator(MemberElement_vectorHeading member) {
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
                org.mitre.caasd.aixm.aixm.vectorHeadingType nx = new org.mitre.caasd.aixm.aixm.vectorHeadingType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_vectorHeading(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.vectorHeadingType at(int index) {
            return new org.mitre.caasd.aixm.aixm.vectorHeadingType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.vectorHeadingType first() {
            return new org.mitre.caasd.aixm.aixm.vectorHeadingType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.vectorHeadingType last() {
            return new org.mitre.caasd.aixm.aixm.vectorHeadingType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.vectorHeadingType append() {
            return new org.mitre.caasd.aixm.aixm.vectorHeadingType(owner.createElement(info));
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
            return new MemberElement_vectorHeading_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_departureRunwayTransition departureRunwayTransition;

    public static class MemberElement_departureRunwayTransition {

        public static class MemberElement_departureRunwayTransition_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_departureRunwayTransition member;

            public MemberElement_departureRunwayTransition_Iterator(MemberElement_departureRunwayTransition member) {
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
                org.mitre.caasd.aixm.aixm.departureRunwayTransitionType nx = new org.mitre.caasd.aixm.aixm.departureRunwayTransitionType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_departureRunwayTransition(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.departureRunwayTransitionType at(int index) {
            return new org.mitre.caasd.aixm.aixm.departureRunwayTransitionType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.departureRunwayTransitionType first() {
            return new org.mitre.caasd.aixm.aixm.departureRunwayTransitionType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.departureRunwayTransitionType last() {
            return new org.mitre.caasd.aixm.aixm.departureRunwayTransitionType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.departureRunwayTransitionType append() {
            return new org.mitre.caasd.aixm.aixm.departureRunwayTransitionType(owner.createElement(info));
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
            return new MemberElement_departureRunwayTransition_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_trajectory trajectory;

    public static class MemberElement_trajectory {

        public static class MemberElement_trajectory_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_trajectory member;

            public MemberElement_trajectory_Iterator(MemberElement_trajectory member) {
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
                org.mitre.caasd.aixm.aixm.trajectoryType2 nx = new org.mitre.caasd.aixm.aixm.trajectoryType2(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_trajectory(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.trajectoryType2 at(int index) {
            return new org.mitre.caasd.aixm.aixm.trajectoryType2(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.trajectoryType2 first() {
            return new org.mitre.caasd.aixm.aixm.trajectoryType2(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.trajectoryType2 last() {
            return new org.mitre.caasd.aixm.aixm.trajectoryType2(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.trajectoryType2 append() {
            return new org.mitre.caasd.aixm.aixm.trajectoryType2(owner.createElement(info));
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
            return new MemberElement_trajectory_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_transitionLeg transitionLeg;

    public static class MemberElement_transitionLeg {

        public static class MemberElement_transitionLeg_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_transitionLeg member;

            public MemberElement_transitionLeg_Iterator(MemberElement_transitionLeg member) {
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
                org.mitre.caasd.aixm.aixm.transitionLegType nx = new org.mitre.caasd.aixm.aixm.transitionLegType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_transitionLeg(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.transitionLegType at(int index) {
            return new org.mitre.caasd.aixm.aixm.transitionLegType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.transitionLegType first() {
            return new org.mitre.caasd.aixm.aixm.transitionLegType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.transitionLegType last() {
            return new org.mitre.caasd.aixm.aixm.transitionLegType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.transitionLegType append() {
            return new org.mitre.caasd.aixm.aixm.transitionLegType(owner.createElement(info));
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
            return new MemberElement_transitionLeg_Iterator(this);
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
                org.mitre.caasd.aixm.aixm.annotationType111 nx = new org.mitre.caasd.aixm.aixm.annotationType111(nextNode);
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

        public org.mitre.caasd.aixm.aixm.annotationType111 at(int index) {
            return new org.mitre.caasd.aixm.aixm.annotationType111(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.annotationType111 first() {
            return new org.mitre.caasd.aixm.aixm.annotationType111(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.annotationType111 last() {
            return new org.mitre.caasd.aixm.aixm.annotationType111(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.annotationType111 append() {
            return new org.mitre.caasd.aixm.aixm.annotationType111(owner.createElement(info));
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
                org.mitre.caasd.aixm.aixm.extensionType145 nx = new org.mitre.caasd.aixm.aixm.extensionType145(nextNode);
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

        public org.mitre.caasd.aixm.aixm.extensionType145 at(int index) {
            return new org.mitre.caasd.aixm.aixm.extensionType145(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.extensionType145 first() {
            return new org.mitre.caasd.aixm.aixm.extensionType145(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.extensionType145 last() {
            return new org.mitre.caasd.aixm.aixm.extensionType145(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.extensionType145 append() {
            return new org.mitre.caasd.aixm.aixm.extensionType145(owner.createElement(info));
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
        org.mitre.caasd.aixm.xml.XmlTreeOperations.setAttribute(getNode(), "http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "http://www.aixm.aero/schema/5.0", "ProcedureTransitionType");
    }
}
