package org.mitre.caasd.aixm.aixm;

public class AirportHeliportUsageConditionType extends org.mitre.caasd.aixm.xml.TypeBase {

    public static org.mitre.caasd.aixm.xml.meta.ComplexType getStaticInfo() {
        return new org.mitre.caasd.aixm.xml.meta.ComplexType(org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getTypes()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_ti_aixm_altova_AirportHeliportUsageConditionType]);
    }

    public AirportHeliportUsageConditionType(org.w3c.dom.Node init) {
        super(init);
        instantiateMembers();
    }

    private void instantiateMembers() {
        meteoConditions = new MemberElement_meteoConditions(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_AirportHeliportUsageConditionType._meteoConditions]);
        visibility = new MemberElement_visibility(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_AirportHeliportUsageConditionType._visibility]);
        visibilityInterpretation = new MemberElement_visibilityInterpretation(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_AirportHeliportUsageConditionType._visibilityInterpretation]);
        runwayVisualRange = new MemberElement_runwayVisualRange(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_AirportHeliportUsageConditionType._runwayVisualRange]);
        runwayVisualRangeInterpretation = new MemberElement_runwayVisualRangeInterpretation(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_AirportHeliportUsageConditionType._runwayVisualRangeInterpretation]);
        flight = new MemberElement_flight(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_AirportHeliportUsageConditionType._flight]);
        aircraft = new MemberElement_aircraft(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_AirportHeliportUsageConditionType._aircraft]);
        schedule = new MemberElement_schedule(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_AirportHeliportUsageConditionType._schedule]);
        operation = new MemberElement_operation(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_AirportHeliportUsageConditionType._operation]);
        annotation = new MemberElement_annotation(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_AirportHeliportUsageConditionType._annotation]);
        extension = new MemberElement_extension(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_aixm_altova_AirportHeliportUsageConditionType._extension]);
    }

    public MemberElement_meteoConditions meteoConditions;

    public static class MemberElement_meteoConditions {

        public static class MemberElement_meteoConditions_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_meteoConditions member;

            public MemberElement_meteoConditions_Iterator(MemberElement_meteoConditions member) {
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
                org.mitre.caasd.aixm.aixm.meteoConditionsType nx = new org.mitre.caasd.aixm.aixm.meteoConditionsType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_meteoConditions(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.meteoConditionsType at(int index) {
            return new org.mitre.caasd.aixm.aixm.meteoConditionsType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.meteoConditionsType first() {
            return new org.mitre.caasd.aixm.aixm.meteoConditionsType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.meteoConditionsType last() {
            return new org.mitre.caasd.aixm.aixm.meteoConditionsType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.meteoConditionsType append() {
            return new org.mitre.caasd.aixm.aixm.meteoConditionsType(owner.createElement(info));
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
            return new MemberElement_meteoConditions_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_visibility visibility;

    public static class MemberElement_visibility {

        public static class MemberElement_visibility_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_visibility member;

            public MemberElement_visibility_Iterator(MemberElement_visibility member) {
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
                org.mitre.caasd.aixm.aixm.visibilityType nx = new org.mitre.caasd.aixm.aixm.visibilityType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_visibility(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.visibilityType at(int index) {
            return new org.mitre.caasd.aixm.aixm.visibilityType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.visibilityType first() {
            return new org.mitre.caasd.aixm.aixm.visibilityType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.visibilityType last() {
            return new org.mitre.caasd.aixm.aixm.visibilityType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.visibilityType append() {
            return new org.mitre.caasd.aixm.aixm.visibilityType(owner.createElement(info));
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
            return new MemberElement_visibility_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_visibilityInterpretation visibilityInterpretation;

    public static class MemberElement_visibilityInterpretation {

        public static class MemberElement_visibilityInterpretation_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_visibilityInterpretation member;

            public MemberElement_visibilityInterpretation_Iterator(MemberElement_visibilityInterpretation member) {
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
                org.mitre.caasd.aixm.aixm.visibilityInterpretationType nx = new org.mitre.caasd.aixm.aixm.visibilityInterpretationType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_visibilityInterpretation(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.visibilityInterpretationType at(int index) {
            return new org.mitre.caasd.aixm.aixm.visibilityInterpretationType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.visibilityInterpretationType first() {
            return new org.mitre.caasd.aixm.aixm.visibilityInterpretationType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.visibilityInterpretationType last() {
            return new org.mitre.caasd.aixm.aixm.visibilityInterpretationType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.visibilityInterpretationType append() {
            return new org.mitre.caasd.aixm.aixm.visibilityInterpretationType(owner.createElement(info));
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
            return new MemberElement_visibilityInterpretation_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_runwayVisualRange runwayVisualRange;

    public static class MemberElement_runwayVisualRange {

        public static class MemberElement_runwayVisualRange_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_runwayVisualRange member;

            public MemberElement_runwayVisualRange_Iterator(MemberElement_runwayVisualRange member) {
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
                org.mitre.caasd.aixm.aixm.runwayVisualRangeType2 nx = new org.mitre.caasd.aixm.aixm.runwayVisualRangeType2(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_runwayVisualRange(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.runwayVisualRangeType2 at(int index) {
            return new org.mitre.caasd.aixm.aixm.runwayVisualRangeType2(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.runwayVisualRangeType2 first() {
            return new org.mitre.caasd.aixm.aixm.runwayVisualRangeType2(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.runwayVisualRangeType2 last() {
            return new org.mitre.caasd.aixm.aixm.runwayVisualRangeType2(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.runwayVisualRangeType2 append() {
            return new org.mitre.caasd.aixm.aixm.runwayVisualRangeType2(owner.createElement(info));
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
            return new MemberElement_runwayVisualRange_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_runwayVisualRangeInterpretation runwayVisualRangeInterpretation;

    public static class MemberElement_runwayVisualRangeInterpretation {

        public static class MemberElement_runwayVisualRangeInterpretation_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_runwayVisualRangeInterpretation member;

            public MemberElement_runwayVisualRangeInterpretation_Iterator(MemberElement_runwayVisualRangeInterpretation member) {
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
                org.mitre.caasd.aixm.aixm.runwayVisualRangeInterpretationType nx = new org.mitre.caasd.aixm.aixm.runwayVisualRangeInterpretationType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_runwayVisualRangeInterpretation(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.runwayVisualRangeInterpretationType at(int index) {
            return new org.mitre.caasd.aixm.aixm.runwayVisualRangeInterpretationType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.runwayVisualRangeInterpretationType first() {
            return new org.mitre.caasd.aixm.aixm.runwayVisualRangeInterpretationType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.runwayVisualRangeInterpretationType last() {
            return new org.mitre.caasd.aixm.aixm.runwayVisualRangeInterpretationType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.runwayVisualRangeInterpretationType append() {
            return new org.mitre.caasd.aixm.aixm.runwayVisualRangeInterpretationType(owner.createElement(info));
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
            return new MemberElement_runwayVisualRangeInterpretation_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_flight flight;

    public static class MemberElement_flight {

        public static class MemberElement_flight_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_flight member;

            public MemberElement_flight_Iterator(MemberElement_flight member) {
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
                org.mitre.caasd.aixm.aixm.flightType nx = new org.mitre.caasd.aixm.aixm.flightType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_flight(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.flightType at(int index) {
            return new org.mitre.caasd.aixm.aixm.flightType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.flightType first() {
            return new org.mitre.caasd.aixm.aixm.flightType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.flightType last() {
            return new org.mitre.caasd.aixm.aixm.flightType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.flightType append() {
            return new org.mitre.caasd.aixm.aixm.flightType(owner.createElement(info));
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
            return new MemberElement_flight_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_aircraft aircraft;

    public static class MemberElement_aircraft {

        public static class MemberElement_aircraft_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_aircraft member;

            public MemberElement_aircraft_Iterator(MemberElement_aircraft member) {
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
                org.mitre.caasd.aixm.aixm.aircraftType2 nx = new org.mitre.caasd.aixm.aixm.aircraftType2(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_aircraft(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.aircraftType2 at(int index) {
            return new org.mitre.caasd.aixm.aixm.aircraftType2(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.aircraftType2 first() {
            return new org.mitre.caasd.aixm.aixm.aircraftType2(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.aircraftType2 last() {
            return new org.mitre.caasd.aixm.aixm.aircraftType2(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.aircraftType2 append() {
            return new org.mitre.caasd.aixm.aixm.aircraftType2(owner.createElement(info));
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
            return new MemberElement_aircraft_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_schedule schedule;

    public static class MemberElement_schedule {

        public static class MemberElement_schedule_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_schedule member;

            public MemberElement_schedule_Iterator(MemberElement_schedule member) {
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
                org.mitre.caasd.aixm.aixm.scheduleType nx = new org.mitre.caasd.aixm.aixm.scheduleType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_schedule(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.scheduleType at(int index) {
            return new org.mitre.caasd.aixm.aixm.scheduleType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.scheduleType first() {
            return new org.mitre.caasd.aixm.aixm.scheduleType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.scheduleType last() {
            return new org.mitre.caasd.aixm.aixm.scheduleType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.scheduleType append() {
            return new org.mitre.caasd.aixm.aixm.scheduleType(owner.createElement(info));
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
            return new MemberElement_schedule_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_operation operation;

    public static class MemberElement_operation {

        public static class MemberElement_operation_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_operation member;

            public MemberElement_operation_Iterator(MemberElement_operation member) {
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
                org.mitre.caasd.aixm.aixm.operationType nx = new org.mitre.caasd.aixm.aixm.operationType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_operation(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.operationType at(int index) {
            return new org.mitre.caasd.aixm.aixm.operationType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.operationType first() {
            return new org.mitre.caasd.aixm.aixm.operationType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.operationType last() {
            return new org.mitre.caasd.aixm.aixm.operationType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.operationType append() {
            return new org.mitre.caasd.aixm.aixm.operationType(owner.createElement(info));
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
            return new MemberElement_operation_Iterator(this);
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
                org.mitre.caasd.aixm.aixm.annotationType12 nx = new org.mitre.caasd.aixm.aixm.annotationType12(nextNode);
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

        public org.mitre.caasd.aixm.aixm.annotationType12 at(int index) {
            return new org.mitre.caasd.aixm.aixm.annotationType12(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.annotationType12 first() {
            return new org.mitre.caasd.aixm.aixm.annotationType12(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.annotationType12 last() {
            return new org.mitre.caasd.aixm.aixm.annotationType12(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.annotationType12 append() {
            return new org.mitre.caasd.aixm.aixm.annotationType12(owner.createElement(info));
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
                org.mitre.caasd.aixm.aixm.extensionType14 nx = new org.mitre.caasd.aixm.aixm.extensionType14(nextNode);
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

        public org.mitre.caasd.aixm.aixm.extensionType14 at(int index) {
            return new org.mitre.caasd.aixm.aixm.extensionType14(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.extensionType14 first() {
            return new org.mitre.caasd.aixm.aixm.extensionType14(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.extensionType14 last() {
            return new org.mitre.caasd.aixm.aixm.extensionType14(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.extensionType14 append() {
            return new org.mitre.caasd.aixm.aixm.extensionType14(owner.createElement(info));
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
        org.mitre.caasd.aixm.xml.XmlTreeOperations.setAttribute(getNode(), "http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "http://www.aixm.aero/schema/5.0", "AirportHeliportUsageConditionType");
    }
}
