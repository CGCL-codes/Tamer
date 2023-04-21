package org.mitre.caasd.aixm.gml;

public class CurvePropertyType extends org.mitre.caasd.aixm.xml.TypeBase {

    public static org.mitre.caasd.aixm.xml.meta.ComplexType getStaticInfo() {
        return new org.mitre.caasd.aixm.xml.meta.ComplexType(org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getTypes()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_ti_gml_altova_CurvePropertyType]);
    }

    public CurvePropertyType(org.w3c.dom.Node init) {
        super(init);
        instantiateMembers();
    }

    private void instantiateMembers() {
        type = new MemberAttribute_type(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gml_altova_CurvePropertyType._type]);
        href = new MemberAttribute_href(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gml_altova_CurvePropertyType._href]);
        role = new MemberAttribute_role(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gml_altova_CurvePropertyType._role]);
        arcrole = new MemberAttribute_arcrole(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gml_altova_CurvePropertyType._arcrole]);
        title = new MemberAttribute_title(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gml_altova_CurvePropertyType._title]);
        show = new MemberAttribute_show(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gml_altova_CurvePropertyType._show]);
        actuate = new MemberAttribute_actuate(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gml_altova_CurvePropertyType._actuate]);
        nilReason = new MemberAttribute_nilReason(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gml_altova_CurvePropertyType._nilReason]);
        remoteSchema = new MemberAttribute_remoteSchema(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gml_altova_CurvePropertyType._remoteSchema]);
        owns = new MemberAttribute_owns(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gml_altova_CurvePropertyType._owns]);
        Curve = new MemberElement_Curve(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gml_altova_CurvePropertyType._Curve]);
        ElevatedCurve = new MemberElement_ElevatedCurve(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gml_altova_CurvePropertyType._ElevatedCurve]);
        CompositeCurve = new MemberElement_CompositeCurve(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gml_altova_CurvePropertyType._CompositeCurve]);
        Curve2 = new MemberElement_Curve2(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gml_altova_CurvePropertyType._Curve2]);
        LineString = new MemberElement_LineString(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gml_altova_CurvePropertyType._LineString]);
        OrientableCurve = new MemberElement_OrientableCurve(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gml_altova_CurvePropertyType._OrientableCurve]);
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

    public MemberAttribute_remoteSchema remoteSchema;

    public static class MemberAttribute_remoteSchema {

        private org.mitre.caasd.aixm.xml.TypeBase owner;

        private org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberAttribute_remoteSchema(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
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

    public MemberAttribute_owns owns;

    public static class MemberAttribute_owns {

        private org.mitre.caasd.aixm.xml.TypeBase owner;

        private org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberAttribute_owns(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public boolean getValue() {
            return (boolean) org.mitre.caasd.aixm.xml.XmlTreeOperations.castToBool(org.mitre.caasd.aixm.xml.XmlTreeOperations.findAttribute(owner.getNode(), info), info);
        }

        public void setValue(boolean value) {
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

    public MemberElement_Curve Curve;

    public static class MemberElement_Curve {

        public static class MemberElement_Curve_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_Curve member;

            public MemberElement_Curve_Iterator(MemberElement_Curve member) {
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
                org.mitre.caasd.aixm.aixm.CurveType nx = new org.mitre.caasd.aixm.aixm.CurveType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_Curve(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.CurveType at(int index) {
            return new org.mitre.caasd.aixm.aixm.CurveType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.CurveType first() {
            return new org.mitre.caasd.aixm.aixm.CurveType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.CurveType last() {
            return new org.mitre.caasd.aixm.aixm.CurveType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.CurveType append() {
            return new org.mitre.caasd.aixm.aixm.CurveType(owner.createElement(info));
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
            return new MemberElement_Curve_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_ElevatedCurve ElevatedCurve;

    public static class MemberElement_ElevatedCurve {

        public static class MemberElement_ElevatedCurve_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_ElevatedCurve member;

            public MemberElement_ElevatedCurve_Iterator(MemberElement_ElevatedCurve member) {
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
                org.mitre.caasd.aixm.aixm.ElevatedCurveType nx = new org.mitre.caasd.aixm.aixm.ElevatedCurveType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_ElevatedCurve(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.aixm.ElevatedCurveType at(int index) {
            return new org.mitre.caasd.aixm.aixm.ElevatedCurveType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.aixm.ElevatedCurveType first() {
            return new org.mitre.caasd.aixm.aixm.ElevatedCurveType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.aixm.ElevatedCurveType last() {
            return new org.mitre.caasd.aixm.aixm.ElevatedCurveType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.aixm.ElevatedCurveType append() {
            return new org.mitre.caasd.aixm.aixm.ElevatedCurveType(owner.createElement(info));
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
            return new MemberElement_ElevatedCurve_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_CompositeCurve CompositeCurve;

    public static class MemberElement_CompositeCurve {

        public static class MemberElement_CompositeCurve_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_CompositeCurve member;

            public MemberElement_CompositeCurve_Iterator(MemberElement_CompositeCurve member) {
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
                org.mitre.caasd.aixm.gml.CompositeCurveType nx = new org.mitre.caasd.aixm.gml.CompositeCurveType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_CompositeCurve(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.gml.CompositeCurveType at(int index) {
            return new org.mitre.caasd.aixm.gml.CompositeCurveType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.gml.CompositeCurveType first() {
            return new org.mitre.caasd.aixm.gml.CompositeCurveType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.gml.CompositeCurveType last() {
            return new org.mitre.caasd.aixm.gml.CompositeCurveType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.gml.CompositeCurveType append() {
            return new org.mitre.caasd.aixm.gml.CompositeCurveType(owner.createElement(info));
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
            return new MemberElement_CompositeCurve_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_Curve2 Curve2;

    public static class MemberElement_Curve2 {

        public static class MemberElement_Curve2_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_Curve2 member;

            public MemberElement_Curve2_Iterator(MemberElement_Curve2 member) {
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
                org.mitre.caasd.aixm.gml.CurveType nx = new org.mitre.caasd.aixm.gml.CurveType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_Curve2(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.gml.CurveType at(int index) {
            return new org.mitre.caasd.aixm.gml.CurveType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.gml.CurveType first() {
            return new org.mitre.caasd.aixm.gml.CurveType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.gml.CurveType last() {
            return new org.mitre.caasd.aixm.gml.CurveType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.gml.CurveType append() {
            return new org.mitre.caasd.aixm.gml.CurveType(owner.createElement(info));
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
            return new MemberElement_Curve2_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_LineString LineString;

    public static class MemberElement_LineString {

        public static class MemberElement_LineString_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_LineString member;

            public MemberElement_LineString_Iterator(MemberElement_LineString member) {
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
                org.mitre.caasd.aixm.gml.LineStringType nx = new org.mitre.caasd.aixm.gml.LineStringType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_LineString(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.gml.LineStringType at(int index) {
            return new org.mitre.caasd.aixm.gml.LineStringType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.gml.LineStringType first() {
            return new org.mitre.caasd.aixm.gml.LineStringType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.gml.LineStringType last() {
            return new org.mitre.caasd.aixm.gml.LineStringType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.gml.LineStringType append() {
            return new org.mitre.caasd.aixm.gml.LineStringType(owner.createElement(info));
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
            return new MemberElement_LineString_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_OrientableCurve OrientableCurve;

    public static class MemberElement_OrientableCurve {

        public static class MemberElement_OrientableCurve_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_OrientableCurve member;

            public MemberElement_OrientableCurve_Iterator(MemberElement_OrientableCurve member) {
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
                org.mitre.caasd.aixm.gml.OrientableCurveType nx = new org.mitre.caasd.aixm.gml.OrientableCurveType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_OrientableCurve(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.gml.OrientableCurveType at(int index) {
            return new org.mitre.caasd.aixm.gml.OrientableCurveType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.gml.OrientableCurveType first() {
            return new org.mitre.caasd.aixm.gml.OrientableCurveType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.gml.OrientableCurveType last() {
            return new org.mitre.caasd.aixm.gml.OrientableCurveType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.gml.OrientableCurveType append() {
            return new org.mitre.caasd.aixm.gml.OrientableCurveType(owner.createElement(info));
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
            return new MemberElement_OrientableCurve_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public void setXsiType() {
        org.mitre.caasd.aixm.xml.XmlTreeOperations.setAttribute(getNode(), "http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "http://www.opengis.net/gml/3.2", "CurvePropertyType");
    }
}
