package org.mitre.caasd.aixm.gmd;

public class MD_Band_Type extends org.mitre.caasd.aixm.gmd.MD_RangeDimension_Type {

    public static org.mitre.caasd.aixm.xml.meta.ComplexType getStaticInfo() {
        return new org.mitre.caasd.aixm.xml.meta.ComplexType(org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getTypes()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_ti_gmd_altova_MD_Band_Type]);
    }

    public MD_Band_Type(org.w3c.dom.Node init) {
        super(init);
        instantiateMembers();
    }

    private void instantiateMembers() {
        maxValue = new MemberElement_maxValue(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gmd_altova_MD_Band_Type._maxValue]);
        minValue = new MemberElement_minValue(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gmd_altova_MD_Band_Type._minValue]);
        units = new MemberElement_units(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gmd_altova_MD_Band_Type._units]);
        peakResponse = new MemberElement_peakResponse(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gmd_altova_MD_Band_Type._peakResponse]);
        bitsPerValue = new MemberElement_bitsPerValue(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gmd_altova_MD_Band_Type._bitsPerValue]);
        toneGradation = new MemberElement_toneGradation(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gmd_altova_MD_Band_Type._toneGradation]);
        scaleFactor = new MemberElement_scaleFactor(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gmd_altova_MD_Band_Type._scaleFactor]);
        offset = new MemberElement_offset(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gmd_altova_MD_Band_Type._offset]);
    }

    public MemberElement_maxValue maxValue;

    public static class MemberElement_maxValue {

        public static class MemberElement_maxValue_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_maxValue member;

            public MemberElement_maxValue_Iterator(MemberElement_maxValue member) {
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
                org.mitre.caasd.aixm.gco.Real_PropertyType nx = new org.mitre.caasd.aixm.gco.Real_PropertyType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_maxValue(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.gco.Real_PropertyType at(int index) {
            return new org.mitre.caasd.aixm.gco.Real_PropertyType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.gco.Real_PropertyType first() {
            return new org.mitre.caasd.aixm.gco.Real_PropertyType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.gco.Real_PropertyType last() {
            return new org.mitre.caasd.aixm.gco.Real_PropertyType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.gco.Real_PropertyType append() {
            return new org.mitre.caasd.aixm.gco.Real_PropertyType(owner.createElement(info));
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
            return new MemberElement_maxValue_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_minValue minValue;

    public static class MemberElement_minValue {

        public static class MemberElement_minValue_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_minValue member;

            public MemberElement_minValue_Iterator(MemberElement_minValue member) {
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
                org.mitre.caasd.aixm.gco.Real_PropertyType nx = new org.mitre.caasd.aixm.gco.Real_PropertyType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_minValue(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.gco.Real_PropertyType at(int index) {
            return new org.mitre.caasd.aixm.gco.Real_PropertyType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.gco.Real_PropertyType first() {
            return new org.mitre.caasd.aixm.gco.Real_PropertyType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.gco.Real_PropertyType last() {
            return new org.mitre.caasd.aixm.gco.Real_PropertyType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.gco.Real_PropertyType append() {
            return new org.mitre.caasd.aixm.gco.Real_PropertyType(owner.createElement(info));
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
            return new MemberElement_minValue_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_units units;

    public static class MemberElement_units {

        public static class MemberElement_units_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_units member;

            public MemberElement_units_Iterator(MemberElement_units member) {
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
                org.mitre.caasd.aixm.gco.UomLength_PropertyType nx = new org.mitre.caasd.aixm.gco.UomLength_PropertyType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_units(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.gco.UomLength_PropertyType at(int index) {
            return new org.mitre.caasd.aixm.gco.UomLength_PropertyType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.gco.UomLength_PropertyType first() {
            return new org.mitre.caasd.aixm.gco.UomLength_PropertyType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.gco.UomLength_PropertyType last() {
            return new org.mitre.caasd.aixm.gco.UomLength_PropertyType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.gco.UomLength_PropertyType append() {
            return new org.mitre.caasd.aixm.gco.UomLength_PropertyType(owner.createElement(info));
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
            return new MemberElement_units_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_peakResponse peakResponse;

    public static class MemberElement_peakResponse {

        public static class MemberElement_peakResponse_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_peakResponse member;

            public MemberElement_peakResponse_Iterator(MemberElement_peakResponse member) {
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
                org.mitre.caasd.aixm.gco.Real_PropertyType nx = new org.mitre.caasd.aixm.gco.Real_PropertyType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_peakResponse(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.gco.Real_PropertyType at(int index) {
            return new org.mitre.caasd.aixm.gco.Real_PropertyType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.gco.Real_PropertyType first() {
            return new org.mitre.caasd.aixm.gco.Real_PropertyType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.gco.Real_PropertyType last() {
            return new org.mitre.caasd.aixm.gco.Real_PropertyType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.gco.Real_PropertyType append() {
            return new org.mitre.caasd.aixm.gco.Real_PropertyType(owner.createElement(info));
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
            return new MemberElement_peakResponse_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_bitsPerValue bitsPerValue;

    public static class MemberElement_bitsPerValue {

        public static class MemberElement_bitsPerValue_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_bitsPerValue member;

            public MemberElement_bitsPerValue_Iterator(MemberElement_bitsPerValue member) {
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
                org.mitre.caasd.aixm.gco.Integer_PropertyType nx = new org.mitre.caasd.aixm.gco.Integer_PropertyType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_bitsPerValue(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.gco.Integer_PropertyType at(int index) {
            return new org.mitre.caasd.aixm.gco.Integer_PropertyType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.gco.Integer_PropertyType first() {
            return new org.mitre.caasd.aixm.gco.Integer_PropertyType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.gco.Integer_PropertyType last() {
            return new org.mitre.caasd.aixm.gco.Integer_PropertyType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.gco.Integer_PropertyType append() {
            return new org.mitre.caasd.aixm.gco.Integer_PropertyType(owner.createElement(info));
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
            return new MemberElement_bitsPerValue_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_toneGradation toneGradation;

    public static class MemberElement_toneGradation {

        public static class MemberElement_toneGradation_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_toneGradation member;

            public MemberElement_toneGradation_Iterator(MemberElement_toneGradation member) {
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
                org.mitre.caasd.aixm.gco.Integer_PropertyType nx = new org.mitre.caasd.aixm.gco.Integer_PropertyType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_toneGradation(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.gco.Integer_PropertyType at(int index) {
            return new org.mitre.caasd.aixm.gco.Integer_PropertyType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.gco.Integer_PropertyType first() {
            return new org.mitre.caasd.aixm.gco.Integer_PropertyType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.gco.Integer_PropertyType last() {
            return new org.mitre.caasd.aixm.gco.Integer_PropertyType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.gco.Integer_PropertyType append() {
            return new org.mitre.caasd.aixm.gco.Integer_PropertyType(owner.createElement(info));
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
            return new MemberElement_toneGradation_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_scaleFactor scaleFactor;

    public static class MemberElement_scaleFactor {

        public static class MemberElement_scaleFactor_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_scaleFactor member;

            public MemberElement_scaleFactor_Iterator(MemberElement_scaleFactor member) {
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
                org.mitre.caasd.aixm.gco.Real_PropertyType nx = new org.mitre.caasd.aixm.gco.Real_PropertyType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_scaleFactor(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.gco.Real_PropertyType at(int index) {
            return new org.mitre.caasd.aixm.gco.Real_PropertyType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.gco.Real_PropertyType first() {
            return new org.mitre.caasd.aixm.gco.Real_PropertyType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.gco.Real_PropertyType last() {
            return new org.mitre.caasd.aixm.gco.Real_PropertyType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.gco.Real_PropertyType append() {
            return new org.mitre.caasd.aixm.gco.Real_PropertyType(owner.createElement(info));
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
            return new MemberElement_scaleFactor_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_offset offset;

    public static class MemberElement_offset {

        public static class MemberElement_offset_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_offset member;

            public MemberElement_offset_Iterator(MemberElement_offset member) {
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
                org.mitre.caasd.aixm.gco.Real_PropertyType nx = new org.mitre.caasd.aixm.gco.Real_PropertyType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_offset(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.gco.Real_PropertyType at(int index) {
            return new org.mitre.caasd.aixm.gco.Real_PropertyType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.gco.Real_PropertyType first() {
            return new org.mitre.caasd.aixm.gco.Real_PropertyType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.gco.Real_PropertyType last() {
            return new org.mitre.caasd.aixm.gco.Real_PropertyType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.gco.Real_PropertyType append() {
            return new org.mitre.caasd.aixm.gco.Real_PropertyType(owner.createElement(info));
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
            return new MemberElement_offset_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public void setXsiType() {
        org.mitre.caasd.aixm.xml.XmlTreeOperations.setAttribute(getNode(), "http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "http://www.isotc211.org/2005/gmd", "MD_Band_Type");
    }
}
