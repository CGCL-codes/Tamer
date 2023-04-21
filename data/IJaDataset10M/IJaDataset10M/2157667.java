package org.mitre.caasd.aixm.gml;

public class ArcByBulgeType extends org.mitre.caasd.aixm.xml.TypeBase {

    public static org.mitre.caasd.aixm.xml.meta.ComplexType getStaticInfo() {
        return new org.mitre.caasd.aixm.xml.meta.ComplexType(org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getTypes()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_ti_gml_altova_ArcByBulgeType]);
    }

    public ArcByBulgeType(org.w3c.dom.Node init) {
        super(init);
        instantiateMembers();
    }

    private void instantiateMembers() {
        numDerivativesAtStart = new MemberAttribute_numDerivativesAtStart(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gml_altova_ArcByBulgeType._numDerivativesAtStart]);
        numDerivativesAtEnd = new MemberAttribute_numDerivativesAtEnd(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gml_altova_ArcByBulgeType._numDerivativesAtEnd]);
        numDerivativeInterior = new MemberAttribute_numDerivativeInterior(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gml_altova_ArcByBulgeType._numDerivativeInterior]);
        interpolation = new MemberAttribute_interpolation(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gml_altova_ArcByBulgeType._interpolation]);
        numArc = new MemberAttribute_numArc(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gml_altova_ArcByBulgeType._numArc]);
        pos = new MemberElement_pos(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gml_altova_ArcByBulgeType._pos]);
        pointProperty = new MemberElement_pointProperty(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gml_altova_ArcByBulgeType._pointProperty]);
        pointRep = new MemberElement_pointRep(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gml_altova_ArcByBulgeType._pointRep]);
        posList = new MemberElement_posList(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gml_altova_ArcByBulgeType._posList]);
        coordinates = new MemberElement_coordinates(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gml_altova_ArcByBulgeType._coordinates]);
        bulge = new MemberElement_bulge(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gml_altova_ArcByBulgeType._bulge]);
        normal = new MemberElement_normal(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gml_altova_ArcByBulgeType._normal]);
    }

    public MemberAttribute_numDerivativesAtStart numDerivativesAtStart;

    public static class MemberAttribute_numDerivativesAtStart {

        private org.mitre.caasd.aixm.xml.TypeBase owner;

        private org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberAttribute_numDerivativesAtStart(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public java.math.BigInteger getValue() {
            return (java.math.BigInteger) org.mitre.caasd.aixm.xml.XmlTreeOperations.castToBigInteger(org.mitre.caasd.aixm.xml.XmlTreeOperations.findAttribute(owner.getNode(), info), info);
        }

        public void setValue(java.math.BigInteger value) {
            org.mitre.caasd.aixm.xml.XmlTreeOperations.setValue(owner.getNode(), info, value);
        }

        public boolean exists() {
            return owner.getAttribute(info) != null;
        }

        public void remove() {
            owner.removeAttribute(info);
        }

        public org.mitre.caasd.aixm.xml.meta.Attribute getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Attribute(info);
        }
    }

    public MemberAttribute_numDerivativesAtEnd numDerivativesAtEnd;

    public static class MemberAttribute_numDerivativesAtEnd {

        private org.mitre.caasd.aixm.xml.TypeBase owner;

        private org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberAttribute_numDerivativesAtEnd(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public java.math.BigInteger getValue() {
            return (java.math.BigInteger) org.mitre.caasd.aixm.xml.XmlTreeOperations.castToBigInteger(org.mitre.caasd.aixm.xml.XmlTreeOperations.findAttribute(owner.getNode(), info), info);
        }

        public void setValue(java.math.BigInteger value) {
            org.mitre.caasd.aixm.xml.XmlTreeOperations.setValue(owner.getNode(), info, value);
        }

        public boolean exists() {
            return owner.getAttribute(info) != null;
        }

        public void remove() {
            owner.removeAttribute(info);
        }

        public org.mitre.caasd.aixm.xml.meta.Attribute getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Attribute(info);
        }
    }

    public MemberAttribute_numDerivativeInterior numDerivativeInterior;

    public static class MemberAttribute_numDerivativeInterior {

        private org.mitre.caasd.aixm.xml.TypeBase owner;

        private org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberAttribute_numDerivativeInterior(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public java.math.BigInteger getValue() {
            return (java.math.BigInteger) org.mitre.caasd.aixm.xml.XmlTreeOperations.castToBigInteger(org.mitre.caasd.aixm.xml.XmlTreeOperations.findAttribute(owner.getNode(), info), info);
        }

        public void setValue(java.math.BigInteger value) {
            org.mitre.caasd.aixm.xml.XmlTreeOperations.setValue(owner.getNode(), info, value);
        }

        public boolean exists() {
            return owner.getAttribute(info) != null;
        }

        public void remove() {
            owner.removeAttribute(info);
        }

        public org.mitre.caasd.aixm.xml.meta.Attribute getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Attribute(info);
        }
    }

    public MemberAttribute_interpolation interpolation;

    public static class MemberAttribute_interpolation {

        private org.mitre.caasd.aixm.xml.TypeBase owner;

        private org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberAttribute_interpolation(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public String getValue() {
            return (String) org.mitre.caasd.aixm.xml.XmlTreeOperations.castToString(org.mitre.caasd.aixm.xml.XmlTreeOperations.findAttribute(owner.getNode(), info), info);
        }

        public void setValue(String value) {
            org.mitre.caasd.aixm.xml.XmlTreeOperations.setValue(owner.getNode(), info, value);
        }

        public boolean exists() {
            return owner.getAttribute(info) != null;
        }

        public void remove() {
            owner.removeAttribute(info);
        }

        public org.mitre.caasd.aixm.xml.meta.Attribute getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Attribute(info);
        }
    }

    public MemberAttribute_numArc numArc;

    public static class MemberAttribute_numArc {

        private org.mitre.caasd.aixm.xml.TypeBase owner;

        private org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberAttribute_numArc(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public java.math.BigInteger getValue() {
            return (java.math.BigInteger) org.mitre.caasd.aixm.xml.XmlTreeOperations.castToBigInteger(org.mitre.caasd.aixm.xml.XmlTreeOperations.findAttribute(owner.getNode(), info), info);
        }

        public void setValue(java.math.BigInteger value) {
            org.mitre.caasd.aixm.xml.XmlTreeOperations.setValue(owner.getNode(), info, value);
        }

        public boolean exists() {
            return owner.getAttribute(info) != null;
        }

        public void remove() {
            owner.removeAttribute(info);
        }

        public org.mitre.caasd.aixm.xml.meta.Attribute getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Attribute(info);
        }
    }

    public MemberElement_pos pos;

    public static class MemberElement_pos {

        public static class MemberElement_pos_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_pos member;

            public MemberElement_pos_Iterator(MemberElement_pos member) {
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
                org.mitre.caasd.aixm.gml.DirectPositionType nx = new org.mitre.caasd.aixm.gml.DirectPositionType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_pos(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.gml.DirectPositionType at(int index) {
            return new org.mitre.caasd.aixm.gml.DirectPositionType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.gml.DirectPositionType first() {
            return new org.mitre.caasd.aixm.gml.DirectPositionType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.gml.DirectPositionType last() {
            return new org.mitre.caasd.aixm.gml.DirectPositionType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.gml.DirectPositionType append() {
            return new org.mitre.caasd.aixm.gml.DirectPositionType(owner.createElement(info));
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
            return new MemberElement_pos_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_pointProperty pointProperty;

    public static class MemberElement_pointProperty {

        public static class MemberElement_pointProperty_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_pointProperty member;

            public MemberElement_pointProperty_Iterator(MemberElement_pointProperty member) {
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
                org.mitre.caasd.aixm.gml.PointPropertyType nx = new org.mitre.caasd.aixm.gml.PointPropertyType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_pointProperty(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.gml.PointPropertyType at(int index) {
            return new org.mitre.caasd.aixm.gml.PointPropertyType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.gml.PointPropertyType first() {
            return new org.mitre.caasd.aixm.gml.PointPropertyType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.gml.PointPropertyType last() {
            return new org.mitre.caasd.aixm.gml.PointPropertyType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.gml.PointPropertyType append() {
            return new org.mitre.caasd.aixm.gml.PointPropertyType(owner.createElement(info));
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
            return new MemberElement_pointProperty_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_pointRep pointRep;

    public static class MemberElement_pointRep {

        public static class MemberElement_pointRep_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_pointRep member;

            public MemberElement_pointRep_Iterator(MemberElement_pointRep member) {
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
                org.mitre.caasd.aixm.gml.PointPropertyType nx = new org.mitre.caasd.aixm.gml.PointPropertyType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_pointRep(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.gml.PointPropertyType at(int index) {
            return new org.mitre.caasd.aixm.gml.PointPropertyType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.gml.PointPropertyType first() {
            return new org.mitre.caasd.aixm.gml.PointPropertyType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.gml.PointPropertyType last() {
            return new org.mitre.caasd.aixm.gml.PointPropertyType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.gml.PointPropertyType append() {
            return new org.mitre.caasd.aixm.gml.PointPropertyType(owner.createElement(info));
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
            return new MemberElement_pointRep_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_posList posList;

    public static class MemberElement_posList {

        public static class MemberElement_posList_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_posList member;

            public MemberElement_posList_Iterator(MemberElement_posList member) {
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
                org.mitre.caasd.aixm.gml.DirectPositionListType nx = new org.mitre.caasd.aixm.gml.DirectPositionListType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_posList(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.gml.DirectPositionListType at(int index) {
            return new org.mitre.caasd.aixm.gml.DirectPositionListType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.gml.DirectPositionListType first() {
            return new org.mitre.caasd.aixm.gml.DirectPositionListType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.gml.DirectPositionListType last() {
            return new org.mitre.caasd.aixm.gml.DirectPositionListType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.gml.DirectPositionListType append() {
            return new org.mitre.caasd.aixm.gml.DirectPositionListType(owner.createElement(info));
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
            return new MemberElement_posList_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_coordinates coordinates;

    public static class MemberElement_coordinates {

        public static class MemberElement_coordinates_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_coordinates member;

            public MemberElement_coordinates_Iterator(MemberElement_coordinates member) {
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
                org.mitre.caasd.aixm.gml.CoordinatesType nx = new org.mitre.caasd.aixm.gml.CoordinatesType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_coordinates(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.gml.CoordinatesType at(int index) {
            return new org.mitre.caasd.aixm.gml.CoordinatesType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.gml.CoordinatesType first() {
            return new org.mitre.caasd.aixm.gml.CoordinatesType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.gml.CoordinatesType last() {
            return new org.mitre.caasd.aixm.gml.CoordinatesType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.gml.CoordinatesType append() {
            return new org.mitre.caasd.aixm.gml.CoordinatesType(owner.createElement(info));
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
            return new MemberElement_coordinates_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_bulge bulge;

    public static class MemberElement_bulge {

        public static class MemberElement_bulge_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_bulge member;

            public MemberElement_bulge_Iterator(MemberElement_bulge member) {
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
                org.mitre.caasd.aixm.xs.doubleType nx = new org.mitre.caasd.aixm.xs.doubleType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_bulge(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.xs.doubleType at(int index) {
            return new org.mitre.caasd.aixm.xs.doubleType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.xs.doubleType first() {
            return new org.mitre.caasd.aixm.xs.doubleType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.xs.doubleType last() {
            return new org.mitre.caasd.aixm.xs.doubleType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.xs.doubleType append() {
            return new org.mitre.caasd.aixm.xs.doubleType(owner.createElement(info));
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
            return new MemberElement_bulge_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_normal normal;

    public static class MemberElement_normal {

        public static class MemberElement_normal_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_normal member;

            public MemberElement_normal_Iterator(MemberElement_normal member) {
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
                org.mitre.caasd.aixm.gml.VectorType nx = new org.mitre.caasd.aixm.gml.VectorType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_normal(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.gml.VectorType at(int index) {
            return new org.mitre.caasd.aixm.gml.VectorType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.gml.VectorType first() {
            return new org.mitre.caasd.aixm.gml.VectorType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.gml.VectorType last() {
            return new org.mitre.caasd.aixm.gml.VectorType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.gml.VectorType append() {
            return new org.mitre.caasd.aixm.gml.VectorType(owner.createElement(info));
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
            return new MemberElement_normal_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public void setXsiType() {
        org.mitre.caasd.aixm.xml.XmlTreeOperations.setAttribute(getNode(), "http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "http://www.opengis.net/gml/3.2", "ArcByBulgeType");
    }
}
