package org.mitre.caasd.aixm.aixm;

public class PointReferenceType extends org.mitre.caasd.aixm.xml.TypeBase {

    public static org.mitre.caasd.aixm.xml.meta.ComplexType getStaticInfo() {
        return new org.mitre.caasd.aixm.xml.meta.ComplexType(org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getTypes()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_ti_aixm_altova_PointReferenceType]);
    }

    public PointReferenceType(org.w3c.dom.Node init) {
        super(init);
        instantiateMembers();
    }

    private void instantiateMembers() {
        role = new MemberElement_role(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_PointReferenceType._role]);
        priorFixTolerance = new MemberElement_priorFixTolerance(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_PointReferenceType._priorFixTolerance]);
        postFixTolerance = new MemberElement_postFixTolerance(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_PointReferenceType._postFixTolerance]);
        point = new MemberElement_point(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_PointReferenceType._point]);
        facilityAngle = new MemberElement_facilityAngle(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_PointReferenceType._facilityAngle]);
        facilityDistance = new MemberElement_facilityDistance(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_PointReferenceType._facilityDistance]);
        fixToleranceArea = new MemberElement_fixToleranceArea(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_PointReferenceType._fixToleranceArea]);
        annotation = new MemberElement_annotation(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_PointReferenceType._annotation]);
        extension = new MemberElement_extension(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_PointReferenceType._extension]);
    }

    public MemberElement_role role;

    public static class MemberElement_role {

        public static class MemberElement_role_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_role member;

            public MemberElement_role_Iterator(MemberElement_role member) {
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
                org.mitre.caasd.aixm.aixm.roleType nx = new org.mitre.caasd.aixm.aixm.roleType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_role(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.roleType at(int index) {
            return new org.mitre.caasd.aixm.aixm.roleType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.roleType first() {
            return new org.mitre.caasd.aixm.aixm.roleType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.roleType last() {
            return new org.mitre.caasd.aixm.aixm.roleType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.roleType append() {
            return new org.mitre.caasd.aixm.aixm.roleType(owner.createElement(info));
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
            return new MemberElement_role_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_priorFixTolerance priorFixTolerance;

    public static class MemberElement_priorFixTolerance {

        public static class MemberElement_priorFixTolerance_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_priorFixTolerance member;

            public MemberElement_priorFixTolerance_Iterator(MemberElement_priorFixTolerance member) {
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
                org.mitre.caasd.aixm.aixm.priorFixToleranceType nx = new org.mitre.caasd.aixm.aixm.priorFixToleranceType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_priorFixTolerance(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.priorFixToleranceType at(int index) {
            return new org.mitre.caasd.aixm.aixm.priorFixToleranceType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.priorFixToleranceType first() {
            return new org.mitre.caasd.aixm.aixm.priorFixToleranceType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.priorFixToleranceType last() {
            return new org.mitre.caasd.aixm.aixm.priorFixToleranceType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.priorFixToleranceType append() {
            return new org.mitre.caasd.aixm.aixm.priorFixToleranceType(owner.createElement(info));
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
            return new MemberElement_priorFixTolerance_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_postFixTolerance postFixTolerance;

    public static class MemberElement_postFixTolerance {

        public static class MemberElement_postFixTolerance_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_postFixTolerance member;

            public MemberElement_postFixTolerance_Iterator(MemberElement_postFixTolerance member) {
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
                org.mitre.caasd.aixm.aixm.postFixToleranceType nx = new org.mitre.caasd.aixm.aixm.postFixToleranceType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_postFixTolerance(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.postFixToleranceType at(int index) {
            return new org.mitre.caasd.aixm.aixm.postFixToleranceType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.postFixToleranceType first() {
            return new org.mitre.caasd.aixm.aixm.postFixToleranceType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.postFixToleranceType last() {
            return new org.mitre.caasd.aixm.aixm.postFixToleranceType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.postFixToleranceType append() {
            return new org.mitre.caasd.aixm.aixm.postFixToleranceType(owner.createElement(info));
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
            return new MemberElement_postFixTolerance_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_point point;

    public static class MemberElement_point {

        public static class MemberElement_point_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_point member;

            public MemberElement_point_Iterator(MemberElement_point member) {
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
                org.mitre.caasd.aixm.aixm.DesignatedPointPropertyType nx = new org.mitre.caasd.aixm.aixm.DesignatedPointPropertyType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_point(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.DesignatedPointPropertyType at(int index) {
            return new org.mitre.caasd.aixm.aixm.DesignatedPointPropertyType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.DesignatedPointPropertyType first() {
            return new org.mitre.caasd.aixm.aixm.DesignatedPointPropertyType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.DesignatedPointPropertyType last() {
            return new org.mitre.caasd.aixm.aixm.DesignatedPointPropertyType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.DesignatedPointPropertyType append() {
            return new org.mitre.caasd.aixm.aixm.DesignatedPointPropertyType(owner.createElement(info));
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
            return new MemberElement_point_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_facilityAngle facilityAngle;

    public static class MemberElement_facilityAngle {

        public static class MemberElement_facilityAngle_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_facilityAngle member;

            public MemberElement_facilityAngle_Iterator(MemberElement_facilityAngle member) {
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
                org.mitre.caasd.aixm.aixm.facilityAngleType nx = new org.mitre.caasd.aixm.aixm.facilityAngleType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_facilityAngle(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.facilityAngleType at(int index) {
            return new org.mitre.caasd.aixm.aixm.facilityAngleType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.facilityAngleType first() {
            return new org.mitre.caasd.aixm.aixm.facilityAngleType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.facilityAngleType last() {
            return new org.mitre.caasd.aixm.aixm.facilityAngleType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.facilityAngleType append() {
            return new org.mitre.caasd.aixm.aixm.facilityAngleType(owner.createElement(info));
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
            return new MemberElement_facilityAngle_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_facilityDistance facilityDistance;

    public static class MemberElement_facilityDistance {

        public static class MemberElement_facilityDistance_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_facilityDistance member;

            public MemberElement_facilityDistance_Iterator(MemberElement_facilityDistance member) {
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
                org.mitre.caasd.aixm.aixm.DistanceIndicationPropertyType nx = new org.mitre.caasd.aixm.aixm.DistanceIndicationPropertyType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_facilityDistance(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.DistanceIndicationPropertyType at(int index) {
            return new org.mitre.caasd.aixm.aixm.DistanceIndicationPropertyType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.DistanceIndicationPropertyType first() {
            return new org.mitre.caasd.aixm.aixm.DistanceIndicationPropertyType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.DistanceIndicationPropertyType last() {
            return new org.mitre.caasd.aixm.aixm.DistanceIndicationPropertyType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.DistanceIndicationPropertyType append() {
            return new org.mitre.caasd.aixm.aixm.DistanceIndicationPropertyType(owner.createElement(info));
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
            return new MemberElement_facilityDistance_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_fixToleranceArea fixToleranceArea;

    public static class MemberElement_fixToleranceArea {

        public static class MemberElement_fixToleranceArea_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_fixToleranceArea member;

            public MemberElement_fixToleranceArea_Iterator(MemberElement_fixToleranceArea member) {
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
                org.mitre.caasd.aixm.aixm.fixToleranceAreaType nx = new org.mitre.caasd.aixm.aixm.fixToleranceAreaType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_fixToleranceArea(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.fixToleranceAreaType at(int index) {
            return new org.mitre.caasd.aixm.aixm.fixToleranceAreaType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.fixToleranceAreaType first() {
            return new org.mitre.caasd.aixm.aixm.fixToleranceAreaType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.fixToleranceAreaType last() {
            return new org.mitre.caasd.aixm.aixm.fixToleranceAreaType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.fixToleranceAreaType append() {
            return new org.mitre.caasd.aixm.aixm.fixToleranceAreaType(owner.createElement(info));
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
            return new MemberElement_fixToleranceArea_Iterator(this);
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
                org.mitre.caasd.aixm.aixm.annotationType107 nx = new org.mitre.caasd.aixm.aixm.annotationType107(nextNode);
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

        public org.mitre.caasd.aixm.aixm.annotationType107 at(int index) {
            return new org.mitre.caasd.aixm.aixm.annotationType107(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.annotationType107 first() {
            return new org.mitre.caasd.aixm.aixm.annotationType107(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.annotationType107 last() {
            return new org.mitre.caasd.aixm.aixm.annotationType107(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.annotationType107 append() {
            return new org.mitre.caasd.aixm.aixm.annotationType107(owner.createElement(info));
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
                org.mitre.caasd.aixm.aixm.extensionType139 nx = new org.mitre.caasd.aixm.aixm.extensionType139(nextNode);
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

        public org.mitre.caasd.aixm.aixm.extensionType139 at(int index) {
            return new org.mitre.caasd.aixm.aixm.extensionType139(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.extensionType139 first() {
            return new org.mitre.caasd.aixm.aixm.extensionType139(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.extensionType139 last() {
            return new org.mitre.caasd.aixm.aixm.extensionType139(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.extensionType139 append() {
            return new org.mitre.caasd.aixm.aixm.extensionType139(owner.createElement(info));
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
        org.mitre.caasd.aixm.xml.XmlTreeOperations.setAttribute(getNode(), "http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "http://www.aixm.aero/schema/5.0", "PointReferenceType");
    }
}
