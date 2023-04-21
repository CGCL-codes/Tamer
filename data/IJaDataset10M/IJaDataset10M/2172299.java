package org.mitre.caasd.aixm.aixm;

public class AircraftCharacteristicType extends org.mitre.caasd.aixm.xml.TypeBase {

    public static org.mitre.caasd.aixm.xml.meta.ComplexType getStaticInfo() {
        return new org.mitre.caasd.aixm.xml.meta.ComplexType(org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getTypes()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_ti_aixm_altova_AircraftCharacteristicType]);
    }

    public AircraftCharacteristicType(org.w3c.dom.Node init) {
        super(init);
        instantiateMembers();
    }

    private void instantiateMembers() {
        type = new MemberElement_type(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_AircraftCharacteristicType._type]);
        engine = new MemberElement_engine(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_AircraftCharacteristicType._engine]);
        numberEngine = new MemberElement_numberEngine(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_AircraftCharacteristicType._numberEngine]);
        typeAircraftICAO = new MemberElement_typeAircraftICAO(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_AircraftCharacteristicType._typeAircraftICAO]);
        aircraftLandingCategory = new MemberElement_aircraftLandingCategory(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_AircraftCharacteristicType._aircraftLandingCategory]);
        wingSpan = new MemberElement_wingSpan(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_AircraftCharacteristicType._wingSpan]);
        wingSpanInterpretation = new MemberElement_wingSpanInterpretation(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_AircraftCharacteristicType._wingSpanInterpretation]);
        classWingSpan = new MemberElement_classWingSpan(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_AircraftCharacteristicType._classWingSpan]);
        weight = new MemberElement_weight(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_AircraftCharacteristicType._weight]);
        weightInterpretation = new MemberElement_weightInterpretation(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_AircraftCharacteristicType._weightInterpretation]);
        passengers = new MemberElement_passengers(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_AircraftCharacteristicType._passengers]);
        passengersInterpretation = new MemberElement_passengersInterpretation(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_AircraftCharacteristicType._passengersInterpretation]);
        speed = new MemberElement_speed(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_AircraftCharacteristicType._speed]);
        speedInterpretation = new MemberElement_speedInterpretation(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_AircraftCharacteristicType._speedInterpretation]);
        wakeTurbulence = new MemberElement_wakeTurbulence(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_AircraftCharacteristicType._wakeTurbulence]);
        aircraftCapability = new MemberElement_aircraftCapability(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_AircraftCharacteristicType._aircraftCapability]);
        annotation = new MemberElement_annotation(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_AircraftCharacteristicType._annotation]);
        extension = new MemberElement_extension(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_AircraftCharacteristicType._extension]);
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
                org.mitre.caasd.aixm.aixm.typeType4 nx = new org.mitre.caasd.aixm.aixm.typeType4(nextNode);
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

        public org.mitre.caasd.aixm.aixm.typeType4 at(int index) {
            return new org.mitre.caasd.aixm.aixm.typeType4(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.typeType4 first() {
            return new org.mitre.caasd.aixm.aixm.typeType4(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.typeType4 last() {
            return new org.mitre.caasd.aixm.aixm.typeType4(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.typeType4 append() {
            return new org.mitre.caasd.aixm.aixm.typeType4(owner.createElement(info));
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

    public MemberElement_engine engine;

    public static class MemberElement_engine {

        public static class MemberElement_engine_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_engine member;

            public MemberElement_engine_Iterator(MemberElement_engine member) {
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
                org.mitre.caasd.aixm.aixm.engineType nx = new org.mitre.caasd.aixm.aixm.engineType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_engine(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.engineType at(int index) {
            return new org.mitre.caasd.aixm.aixm.engineType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.engineType first() {
            return new org.mitre.caasd.aixm.aixm.engineType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.engineType last() {
            return new org.mitre.caasd.aixm.aixm.engineType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.engineType append() {
            return new org.mitre.caasd.aixm.aixm.engineType(owner.createElement(info));
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
            return new MemberElement_engine_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_numberEngine numberEngine;

    public static class MemberElement_numberEngine {

        public static class MemberElement_numberEngine_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_numberEngine member;

            public MemberElement_numberEngine_Iterator(MemberElement_numberEngine member) {
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
                org.mitre.caasd.aixm.aixm.numberEngineType nx = new org.mitre.caasd.aixm.aixm.numberEngineType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_numberEngine(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.numberEngineType at(int index) {
            return new org.mitre.caasd.aixm.aixm.numberEngineType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.numberEngineType first() {
            return new org.mitre.caasd.aixm.aixm.numberEngineType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.numberEngineType last() {
            return new org.mitre.caasd.aixm.aixm.numberEngineType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.numberEngineType append() {
            return new org.mitre.caasd.aixm.aixm.numberEngineType(owner.createElement(info));
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
            return new MemberElement_numberEngine_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_typeAircraftICAO typeAircraftICAO;

    public static class MemberElement_typeAircraftICAO {

        public static class MemberElement_typeAircraftICAO_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_typeAircraftICAO member;

            public MemberElement_typeAircraftICAO_Iterator(MemberElement_typeAircraftICAO member) {
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
                org.mitre.caasd.aixm.aixm.typeAircraftICAOType nx = new org.mitre.caasd.aixm.aixm.typeAircraftICAOType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_typeAircraftICAO(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.typeAircraftICAOType at(int index) {
            return new org.mitre.caasd.aixm.aixm.typeAircraftICAOType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.typeAircraftICAOType first() {
            return new org.mitre.caasd.aixm.aixm.typeAircraftICAOType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.typeAircraftICAOType last() {
            return new org.mitre.caasd.aixm.aixm.typeAircraftICAOType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.typeAircraftICAOType append() {
            return new org.mitre.caasd.aixm.aixm.typeAircraftICAOType(owner.createElement(info));
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
            return new MemberElement_typeAircraftICAO_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_aircraftLandingCategory aircraftLandingCategory;

    public static class MemberElement_aircraftLandingCategory {

        public static class MemberElement_aircraftLandingCategory_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_aircraftLandingCategory member;

            public MemberElement_aircraftLandingCategory_Iterator(MemberElement_aircraftLandingCategory member) {
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
                org.mitre.caasd.aixm.aixm.aircraftLandingCategoryType nx = new org.mitre.caasd.aixm.aixm.aircraftLandingCategoryType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_aircraftLandingCategory(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.aircraftLandingCategoryType at(int index) {
            return new org.mitre.caasd.aixm.aixm.aircraftLandingCategoryType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.aircraftLandingCategoryType first() {
            return new org.mitre.caasd.aixm.aixm.aircraftLandingCategoryType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.aircraftLandingCategoryType last() {
            return new org.mitre.caasd.aixm.aixm.aircraftLandingCategoryType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.aircraftLandingCategoryType append() {
            return new org.mitre.caasd.aixm.aixm.aircraftLandingCategoryType(owner.createElement(info));
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
            return new MemberElement_aircraftLandingCategory_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_wingSpan wingSpan;

    public static class MemberElement_wingSpan {

        public static class MemberElement_wingSpan_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_wingSpan member;

            public MemberElement_wingSpan_Iterator(MemberElement_wingSpan member) {
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
                org.mitre.caasd.aixm.aixm.wingSpanType nx = new org.mitre.caasd.aixm.aixm.wingSpanType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_wingSpan(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.wingSpanType at(int index) {
            return new org.mitre.caasd.aixm.aixm.wingSpanType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.wingSpanType first() {
            return new org.mitre.caasd.aixm.aixm.wingSpanType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.wingSpanType last() {
            return new org.mitre.caasd.aixm.aixm.wingSpanType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.wingSpanType append() {
            return new org.mitre.caasd.aixm.aixm.wingSpanType(owner.createElement(info));
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
            return new MemberElement_wingSpan_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_wingSpanInterpretation wingSpanInterpretation;

    public static class MemberElement_wingSpanInterpretation {

        public static class MemberElement_wingSpanInterpretation_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_wingSpanInterpretation member;

            public MemberElement_wingSpanInterpretation_Iterator(MemberElement_wingSpanInterpretation member) {
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
                org.mitre.caasd.aixm.aixm.wingSpanInterpretationType nx = new org.mitre.caasd.aixm.aixm.wingSpanInterpretationType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_wingSpanInterpretation(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.wingSpanInterpretationType at(int index) {
            return new org.mitre.caasd.aixm.aixm.wingSpanInterpretationType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.wingSpanInterpretationType first() {
            return new org.mitre.caasd.aixm.aixm.wingSpanInterpretationType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.wingSpanInterpretationType last() {
            return new org.mitre.caasd.aixm.aixm.wingSpanInterpretationType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.wingSpanInterpretationType append() {
            return new org.mitre.caasd.aixm.aixm.wingSpanInterpretationType(owner.createElement(info));
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
            return new MemberElement_wingSpanInterpretation_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_classWingSpan classWingSpan;

    public static class MemberElement_classWingSpan {

        public static class MemberElement_classWingSpan_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_classWingSpan member;

            public MemberElement_classWingSpan_Iterator(MemberElement_classWingSpan member) {
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
                org.mitre.caasd.aixm.aixm.classWingSpanType nx = new org.mitre.caasd.aixm.aixm.classWingSpanType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_classWingSpan(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.classWingSpanType at(int index) {
            return new org.mitre.caasd.aixm.aixm.classWingSpanType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.classWingSpanType first() {
            return new org.mitre.caasd.aixm.aixm.classWingSpanType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.classWingSpanType last() {
            return new org.mitre.caasd.aixm.aixm.classWingSpanType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.classWingSpanType append() {
            return new org.mitre.caasd.aixm.aixm.classWingSpanType(owner.createElement(info));
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
            return new MemberElement_classWingSpan_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_weight weight;

    public static class MemberElement_weight {

        public static class MemberElement_weight_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_weight member;

            public MemberElement_weight_Iterator(MemberElement_weight member) {
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
                org.mitre.caasd.aixm.aixm.weightType nx = new org.mitre.caasd.aixm.aixm.weightType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_weight(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.weightType at(int index) {
            return new org.mitre.caasd.aixm.aixm.weightType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.weightType first() {
            return new org.mitre.caasd.aixm.aixm.weightType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.weightType last() {
            return new org.mitre.caasd.aixm.aixm.weightType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.weightType append() {
            return new org.mitre.caasd.aixm.aixm.weightType(owner.createElement(info));
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
            return new MemberElement_weight_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_weightInterpretation weightInterpretation;

    public static class MemberElement_weightInterpretation {

        public static class MemberElement_weightInterpretation_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_weightInterpretation member;

            public MemberElement_weightInterpretation_Iterator(MemberElement_weightInterpretation member) {
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
                org.mitre.caasd.aixm.aixm.weightInterpretationType nx = new org.mitre.caasd.aixm.aixm.weightInterpretationType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_weightInterpretation(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.weightInterpretationType at(int index) {
            return new org.mitre.caasd.aixm.aixm.weightInterpretationType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.weightInterpretationType first() {
            return new org.mitre.caasd.aixm.aixm.weightInterpretationType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.weightInterpretationType last() {
            return new org.mitre.caasd.aixm.aixm.weightInterpretationType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.weightInterpretationType append() {
            return new org.mitre.caasd.aixm.aixm.weightInterpretationType(owner.createElement(info));
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
            return new MemberElement_weightInterpretation_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_passengers passengers;

    public static class MemberElement_passengers {

        public static class MemberElement_passengers_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_passengers member;

            public MemberElement_passengers_Iterator(MemberElement_passengers member) {
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
                org.mitre.caasd.aixm.aixm.passengersType nx = new org.mitre.caasd.aixm.aixm.passengersType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_passengers(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.passengersType at(int index) {
            return new org.mitre.caasd.aixm.aixm.passengersType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.passengersType first() {
            return new org.mitre.caasd.aixm.aixm.passengersType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.passengersType last() {
            return new org.mitre.caasd.aixm.aixm.passengersType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.passengersType append() {
            return new org.mitre.caasd.aixm.aixm.passengersType(owner.createElement(info));
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
            return new MemberElement_passengers_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_passengersInterpretation passengersInterpretation;

    public static class MemberElement_passengersInterpretation {

        public static class MemberElement_passengersInterpretation_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_passengersInterpretation member;

            public MemberElement_passengersInterpretation_Iterator(MemberElement_passengersInterpretation member) {
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
                org.mitre.caasd.aixm.aixm.passengersInterpretationType nx = new org.mitre.caasd.aixm.aixm.passengersInterpretationType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_passengersInterpretation(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.passengersInterpretationType at(int index) {
            return new org.mitre.caasd.aixm.aixm.passengersInterpretationType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.passengersInterpretationType first() {
            return new org.mitre.caasd.aixm.aixm.passengersInterpretationType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.passengersInterpretationType last() {
            return new org.mitre.caasd.aixm.aixm.passengersInterpretationType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.passengersInterpretationType append() {
            return new org.mitre.caasd.aixm.aixm.passengersInterpretationType(owner.createElement(info));
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
            return new MemberElement_passengersInterpretation_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_speed speed;

    public static class MemberElement_speed {

        public static class MemberElement_speed_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_speed member;

            public MemberElement_speed_Iterator(MemberElement_speed member) {
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
                org.mitre.caasd.aixm.aixm.speedType nx = new org.mitre.caasd.aixm.aixm.speedType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_speed(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.speedType at(int index) {
            return new org.mitre.caasd.aixm.aixm.speedType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.speedType first() {
            return new org.mitre.caasd.aixm.aixm.speedType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.speedType last() {
            return new org.mitre.caasd.aixm.aixm.speedType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.speedType append() {
            return new org.mitre.caasd.aixm.aixm.speedType(owner.createElement(info));
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
            return new MemberElement_speed_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_speedInterpretation speedInterpretation;

    public static class MemberElement_speedInterpretation {

        public static class MemberElement_speedInterpretation_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_speedInterpretation member;

            public MemberElement_speedInterpretation_Iterator(MemberElement_speedInterpretation member) {
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
                org.mitre.caasd.aixm.aixm.speedInterpretationType nx = new org.mitre.caasd.aixm.aixm.speedInterpretationType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_speedInterpretation(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.speedInterpretationType at(int index) {
            return new org.mitre.caasd.aixm.aixm.speedInterpretationType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.speedInterpretationType first() {
            return new org.mitre.caasd.aixm.aixm.speedInterpretationType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.speedInterpretationType last() {
            return new org.mitre.caasd.aixm.aixm.speedInterpretationType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.speedInterpretationType append() {
            return new org.mitre.caasd.aixm.aixm.speedInterpretationType(owner.createElement(info));
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
            return new MemberElement_speedInterpretation_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_wakeTurbulence wakeTurbulence;

    public static class MemberElement_wakeTurbulence {

        public static class MemberElement_wakeTurbulence_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_wakeTurbulence member;

            public MemberElement_wakeTurbulence_Iterator(MemberElement_wakeTurbulence member) {
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
                org.mitre.caasd.aixm.aixm.wakeTurbulenceType nx = new org.mitre.caasd.aixm.aixm.wakeTurbulenceType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_wakeTurbulence(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.wakeTurbulenceType at(int index) {
            return new org.mitre.caasd.aixm.aixm.wakeTurbulenceType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.wakeTurbulenceType first() {
            return new org.mitre.caasd.aixm.aixm.wakeTurbulenceType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.wakeTurbulenceType last() {
            return new org.mitre.caasd.aixm.aixm.wakeTurbulenceType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.wakeTurbulenceType append() {
            return new org.mitre.caasd.aixm.aixm.wakeTurbulenceType(owner.createElement(info));
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
            return new MemberElement_wakeTurbulence_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_aircraftCapability aircraftCapability;

    public static class MemberElement_aircraftCapability {

        public static class MemberElement_aircraftCapability_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_aircraftCapability member;

            public MemberElement_aircraftCapability_Iterator(MemberElement_aircraftCapability member) {
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
                org.mitre.caasd.aixm.aixm.aircraftCapabilityType2 nx = new org.mitre.caasd.aixm.aixm.aircraftCapabilityType2(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_aircraftCapability(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.aircraftCapabilityType2 at(int index) {
            return new org.mitre.caasd.aixm.aixm.aircraftCapabilityType2(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.aircraftCapabilityType2 first() {
            return new org.mitre.caasd.aixm.aixm.aircraftCapabilityType2(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.aircraftCapabilityType2 last() {
            return new org.mitre.caasd.aixm.aixm.aircraftCapabilityType2(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.aircraftCapabilityType2 append() {
            return new org.mitre.caasd.aixm.aixm.aircraftCapabilityType2(owner.createElement(info));
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
            return new MemberElement_aircraftCapability_Iterator(this);
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
                org.mitre.caasd.aixm.aixm.annotationType8 nx = new org.mitre.caasd.aixm.aixm.annotationType8(nextNode);
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

        public org.mitre.caasd.aixm.aixm.annotationType8 at(int index) {
            return new org.mitre.caasd.aixm.aixm.annotationType8(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.annotationType8 first() {
            return new org.mitre.caasd.aixm.aixm.annotationType8(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.annotationType8 last() {
            return new org.mitre.caasd.aixm.aixm.annotationType8(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.annotationType8 append() {
            return new org.mitre.caasd.aixm.aixm.annotationType8(owner.createElement(info));
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
                org.mitre.caasd.aixm.aixm.extensionType9 nx = new org.mitre.caasd.aixm.aixm.extensionType9(nextNode);
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

        public org.mitre.caasd.aixm.aixm.extensionType9 at(int index) {
            return new org.mitre.caasd.aixm.aixm.extensionType9(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.extensionType9 first() {
            return new org.mitre.caasd.aixm.aixm.extensionType9(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.extensionType9 last() {
            return new org.mitre.caasd.aixm.aixm.extensionType9(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.extensionType9 append() {
            return new org.mitre.caasd.aixm.aixm.extensionType9(owner.createElement(info));
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
        org.mitre.caasd.aixm.xml.XmlTreeOperations.setAttribute(getNode(), "http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "http://www.aixm.aero/schema/5.0", "AircraftCharacteristicType");
    }
}
