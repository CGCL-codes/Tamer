package org.mitre.caasd.aixm.gmd;

public class DS_Aggregate_PropertyType extends org.mitre.caasd.aixm.xml.TypeBase {

    public static org.mitre.caasd.aixm.xml.meta.ComplexType getStaticInfo() {
        return new org.mitre.caasd.aixm.xml.meta.ComplexType(org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getTypes()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_ti_gmd_altova_DS_Aggregate_PropertyType]);
    }

    public DS_Aggregate_PropertyType(org.w3c.dom.Node init) {
        super(init);
        instantiateMembers();
    }

    private void instantiateMembers() {
        type = new MemberAttribute_type(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gmd_altova_DS_Aggregate_PropertyType._type]);
        href = new MemberAttribute_href(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gmd_altova_DS_Aggregate_PropertyType._href]);
        role = new MemberAttribute_role(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gmd_altova_DS_Aggregate_PropertyType._role]);
        arcrole = new MemberAttribute_arcrole(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gmd_altova_DS_Aggregate_PropertyType._arcrole]);
        title = new MemberAttribute_title(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gmd_altova_DS_Aggregate_PropertyType._title]);
        show = new MemberAttribute_show(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gmd_altova_DS_Aggregate_PropertyType._show]);
        actuate = new MemberAttribute_actuate(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gmd_altova_DS_Aggregate_PropertyType._actuate]);
        uuidref = new MemberAttribute_uuidref(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gmd_altova_DS_Aggregate_PropertyType._uuidref]);
        nilReason = new MemberAttribute_nilReason(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gmd_altova_DS_Aggregate_PropertyType._nilReason]);
        DS_Initiative = new MemberElement_DS_Initiative(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gmd_altova_DS_Aggregate_PropertyType._DS_Initiative]);
        DS_OtherAggregate = new MemberElement_DS_OtherAggregate(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gmd_altova_DS_Aggregate_PropertyType._DS_OtherAggregate]);
        DS_Platform = new MemberElement_DS_Platform(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gmd_altova_DS_Aggregate_PropertyType._DS_Platform]);
        DS_ProductionSeries = new MemberElement_DS_ProductionSeries(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gmd_altova_DS_Aggregate_PropertyType._DS_ProductionSeries]);
        DS_Sensor = new MemberElement_DS_Sensor(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gmd_altova_DS_Aggregate_PropertyType._DS_Sensor]);
        DS_Series = new MemberElement_DS_Series(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gmd_altova_DS_Aggregate_PropertyType._DS_Series]);
        DS_StereoMate = new MemberElement_DS_StereoMate(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gmd_altova_DS_Aggregate_PropertyType._DS_StereoMate]);
    }

    public MemberAttribute_type type;

    public static class MemberAttribute_type {

        private org.mitre.caasd.aixm.xml.TypeBase owner;

        private org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberAttribute_type(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
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

    public MemberAttribute_href href;

    public static class MemberAttribute_href {

        private org.mitre.caasd.aixm.xml.TypeBase owner;

        private org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberAttribute_href(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
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

    public MemberAttribute_role role;

    public static class MemberAttribute_role {

        private org.mitre.caasd.aixm.xml.TypeBase owner;

        private org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberAttribute_role(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
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

    public MemberAttribute_arcrole arcrole;

    public static class MemberAttribute_arcrole {

        private org.mitre.caasd.aixm.xml.TypeBase owner;

        private org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberAttribute_arcrole(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
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

    public MemberAttribute_title title;

    public static class MemberAttribute_title {

        private org.mitre.caasd.aixm.xml.TypeBase owner;

        private org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberAttribute_title(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
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

    public MemberAttribute_show show;

    public static class MemberAttribute_show {

        private org.mitre.caasd.aixm.xml.TypeBase owner;

        private org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberAttribute_show(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
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

    public MemberAttribute_actuate actuate;

    public static class MemberAttribute_actuate {

        private org.mitre.caasd.aixm.xml.TypeBase owner;

        private org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberAttribute_actuate(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
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

    public MemberAttribute_uuidref uuidref;

    public static class MemberAttribute_uuidref {

        private org.mitre.caasd.aixm.xml.TypeBase owner;

        private org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberAttribute_uuidref(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
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

    public MemberAttribute_nilReason nilReason;

    public static class MemberAttribute_nilReason {

        private org.mitre.caasd.aixm.xml.TypeBase owner;

        private org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberAttribute_nilReason(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
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

    public MemberElement_DS_Initiative DS_Initiative;

    public static class MemberElement_DS_Initiative {

        public static class MemberElement_DS_Initiative_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_DS_Initiative member;

            public MemberElement_DS_Initiative_Iterator(MemberElement_DS_Initiative member) {
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
                org.mitre.caasd.aixm.gmd.DS_Initiative_Type nx = new org.mitre.caasd.aixm.gmd.DS_Initiative_Type(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_DS_Initiative(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.gmd.DS_Initiative_Type at(int index) {
            return new org.mitre.caasd.aixm.gmd.DS_Initiative_Type(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.gmd.DS_Initiative_Type first() {
            return new org.mitre.caasd.aixm.gmd.DS_Initiative_Type(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.gmd.DS_Initiative_Type last() {
            return new org.mitre.caasd.aixm.gmd.DS_Initiative_Type(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.gmd.DS_Initiative_Type append() {
            return new org.mitre.caasd.aixm.gmd.DS_Initiative_Type(owner.createElement(info));
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
            return new MemberElement_DS_Initiative_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_DS_OtherAggregate DS_OtherAggregate;

    public static class MemberElement_DS_OtherAggregate {

        public static class MemberElement_DS_OtherAggregate_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_DS_OtherAggregate member;

            public MemberElement_DS_OtherAggregate_Iterator(MemberElement_DS_OtherAggregate member) {
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
                org.mitre.caasd.aixm.gmd.DS_OtherAggregate_Type nx = new org.mitre.caasd.aixm.gmd.DS_OtherAggregate_Type(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_DS_OtherAggregate(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.gmd.DS_OtherAggregate_Type at(int index) {
            return new org.mitre.caasd.aixm.gmd.DS_OtherAggregate_Type(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.gmd.DS_OtherAggregate_Type first() {
            return new org.mitre.caasd.aixm.gmd.DS_OtherAggregate_Type(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.gmd.DS_OtherAggregate_Type last() {
            return new org.mitre.caasd.aixm.gmd.DS_OtherAggregate_Type(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.gmd.DS_OtherAggregate_Type append() {
            return new org.mitre.caasd.aixm.gmd.DS_OtherAggregate_Type(owner.createElement(info));
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
            return new MemberElement_DS_OtherAggregate_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_DS_Platform DS_Platform;

    public static class MemberElement_DS_Platform {

        public static class MemberElement_DS_Platform_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_DS_Platform member;

            public MemberElement_DS_Platform_Iterator(MemberElement_DS_Platform member) {
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
                org.mitre.caasd.aixm.gmd.DS_Platform_Type nx = new org.mitre.caasd.aixm.gmd.DS_Platform_Type(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_DS_Platform(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.gmd.DS_Platform_Type at(int index) {
            return new org.mitre.caasd.aixm.gmd.DS_Platform_Type(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.gmd.DS_Platform_Type first() {
            return new org.mitre.caasd.aixm.gmd.DS_Platform_Type(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.gmd.DS_Platform_Type last() {
            return new org.mitre.caasd.aixm.gmd.DS_Platform_Type(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.gmd.DS_Platform_Type append() {
            return new org.mitre.caasd.aixm.gmd.DS_Platform_Type(owner.createElement(info));
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
            return new MemberElement_DS_Platform_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_DS_ProductionSeries DS_ProductionSeries;

    public static class MemberElement_DS_ProductionSeries {

        public static class MemberElement_DS_ProductionSeries_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_DS_ProductionSeries member;

            public MemberElement_DS_ProductionSeries_Iterator(MemberElement_DS_ProductionSeries member) {
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
                org.mitre.caasd.aixm.gmd.DS_ProductionSeries_Type nx = new org.mitre.caasd.aixm.gmd.DS_ProductionSeries_Type(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_DS_ProductionSeries(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.gmd.DS_ProductionSeries_Type at(int index) {
            return new org.mitre.caasd.aixm.gmd.DS_ProductionSeries_Type(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.gmd.DS_ProductionSeries_Type first() {
            return new org.mitre.caasd.aixm.gmd.DS_ProductionSeries_Type(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.gmd.DS_ProductionSeries_Type last() {
            return new org.mitre.caasd.aixm.gmd.DS_ProductionSeries_Type(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.gmd.DS_ProductionSeries_Type append() {
            return new org.mitre.caasd.aixm.gmd.DS_ProductionSeries_Type(owner.createElement(info));
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
            return new MemberElement_DS_ProductionSeries_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_DS_Sensor DS_Sensor;

    public static class MemberElement_DS_Sensor {

        public static class MemberElement_DS_Sensor_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_DS_Sensor member;

            public MemberElement_DS_Sensor_Iterator(MemberElement_DS_Sensor member) {
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
                org.mitre.caasd.aixm.gmd.DS_Sensor_Type nx = new org.mitre.caasd.aixm.gmd.DS_Sensor_Type(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_DS_Sensor(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.gmd.DS_Sensor_Type at(int index) {
            return new org.mitre.caasd.aixm.gmd.DS_Sensor_Type(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.gmd.DS_Sensor_Type first() {
            return new org.mitre.caasd.aixm.gmd.DS_Sensor_Type(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.gmd.DS_Sensor_Type last() {
            return new org.mitre.caasd.aixm.gmd.DS_Sensor_Type(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.gmd.DS_Sensor_Type append() {
            return new org.mitre.caasd.aixm.gmd.DS_Sensor_Type(owner.createElement(info));
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
            return new MemberElement_DS_Sensor_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_DS_Series DS_Series;

    public static class MemberElement_DS_Series {

        public static class MemberElement_DS_Series_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_DS_Series member;

            public MemberElement_DS_Series_Iterator(MemberElement_DS_Series member) {
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
                org.mitre.caasd.aixm.gmd.DS_Series_Type nx = new org.mitre.caasd.aixm.gmd.DS_Series_Type(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_DS_Series(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.gmd.DS_Series_Type at(int index) {
            return new org.mitre.caasd.aixm.gmd.DS_Series_Type(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.gmd.DS_Series_Type first() {
            return new org.mitre.caasd.aixm.gmd.DS_Series_Type(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.gmd.DS_Series_Type last() {
            return new org.mitre.caasd.aixm.gmd.DS_Series_Type(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.gmd.DS_Series_Type append() {
            return new org.mitre.caasd.aixm.gmd.DS_Series_Type(owner.createElement(info));
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
            return new MemberElement_DS_Series_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_DS_StereoMate DS_StereoMate;

    public static class MemberElement_DS_StereoMate {

        public static class MemberElement_DS_StereoMate_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_DS_StereoMate member;

            public MemberElement_DS_StereoMate_Iterator(MemberElement_DS_StereoMate member) {
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
                org.mitre.caasd.aixm.gmd.DS_StereoMate_Type nx = new org.mitre.caasd.aixm.gmd.DS_StereoMate_Type(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_DS_StereoMate(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.gmd.DS_StereoMate_Type at(int index) {
            return new org.mitre.caasd.aixm.gmd.DS_StereoMate_Type(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.gmd.DS_StereoMate_Type first() {
            return new org.mitre.caasd.aixm.gmd.DS_StereoMate_Type(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.gmd.DS_StereoMate_Type last() {
            return new org.mitre.caasd.aixm.gmd.DS_StereoMate_Type(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.gmd.DS_StereoMate_Type append() {
            return new org.mitre.caasd.aixm.gmd.DS_StereoMate_Type(owner.createElement(info));
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
            return new MemberElement_DS_StereoMate_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public void setXsiType() {
        org.mitre.caasd.aixm.xml.XmlTreeOperations.setAttribute(getNode(), "http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "http://www.isotc211.org/2005/gmd", "DS_Aggregate_PropertyType");
    }
}
