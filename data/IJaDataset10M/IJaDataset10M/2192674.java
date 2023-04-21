package org.mitre.caasd.aixm.gmd;

public class CI_Citation_Type extends org.mitre.caasd.aixm.gco.AbstractObject_Type {

    public static org.mitre.caasd.aixm.xml.meta.ComplexType getStaticInfo() {
        return new org.mitre.caasd.aixm.xml.meta.ComplexType(org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getTypes()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_ti_gmd_altova_CI_Citation_Type]);
    }

    public CI_Citation_Type(org.w3c.dom.Node init) {
        super(init);
        instantiateMembers();
    }

    private void instantiateMembers() {
        title = new MemberElement_title(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gmd_altova_CI_Citation_Type._title]);
        alternateTitle = new MemberElement_alternateTitle(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gmd_altova_CI_Citation_Type._alternateTitle]);
        date = new MemberElement_date(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gmd_altova_CI_Citation_Type._date]);
        edition = new MemberElement_edition(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gmd_altova_CI_Citation_Type._edition]);
        editionDate = new MemberElement_editionDate(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gmd_altova_CI_Citation_Type._editionDate]);
        identifier = new MemberElement_identifier(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gmd_altova_CI_Citation_Type._identifier]);
        citedResponsibleParty = new MemberElement_citedResponsibleParty(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gmd_altova_CI_Citation_Type._citedResponsibleParty]);
        presentationForm = new MemberElement_presentationForm(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gmd_altova_CI_Citation_Type._presentationForm]);
        series = new MemberElement_series(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gmd_altova_CI_Citation_Type._series]);
        otherCitationDetails = new MemberElement_otherCitationDetails(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gmd_altova_CI_Citation_Type._otherCitationDetails]);
        collectiveTitle = new MemberElement_collectiveTitle(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gmd_altova_CI_Citation_Type._collectiveTitle]);
        ISBN = new MemberElement_ISBN(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gmd_altova_CI_Citation_Type._ISBN]);
        ISSN = new MemberElement_ISSN(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_gmd_altova_CI_Citation_Type._ISSN]);
    }

    public MemberElement_title title;

    public static class MemberElement_title {

        public static class MemberElement_title_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_title member;

            public MemberElement_title_Iterator(MemberElement_title member) {
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
                org.mitre.caasd.aixm.gco.CharacterString_PropertyType nx = new org.mitre.caasd.aixm.gco.CharacterString_PropertyType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_title(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.gco.CharacterString_PropertyType at(int index) {
            return new org.mitre.caasd.aixm.gco.CharacterString_PropertyType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.gco.CharacterString_PropertyType first() {
            return new org.mitre.caasd.aixm.gco.CharacterString_PropertyType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.gco.CharacterString_PropertyType last() {
            return new org.mitre.caasd.aixm.gco.CharacterString_PropertyType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.gco.CharacterString_PropertyType append() {
            return new org.mitre.caasd.aixm.gco.CharacterString_PropertyType(owner.createElement(info));
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
            return new MemberElement_title_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_alternateTitle alternateTitle;

    public static class MemberElement_alternateTitle {

        public static class MemberElement_alternateTitle_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_alternateTitle member;

            public MemberElement_alternateTitle_Iterator(MemberElement_alternateTitle member) {
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
                org.mitre.caasd.aixm.gco.CharacterString_PropertyType nx = new org.mitre.caasd.aixm.gco.CharacterString_PropertyType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_alternateTitle(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.gco.CharacterString_PropertyType at(int index) {
            return new org.mitre.caasd.aixm.gco.CharacterString_PropertyType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.gco.CharacterString_PropertyType first() {
            return new org.mitre.caasd.aixm.gco.CharacterString_PropertyType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.gco.CharacterString_PropertyType last() {
            return new org.mitre.caasd.aixm.gco.CharacterString_PropertyType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.gco.CharacterString_PropertyType append() {
            return new org.mitre.caasd.aixm.gco.CharacterString_PropertyType(owner.createElement(info));
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
            return new MemberElement_alternateTitle_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_date date;

    public static class MemberElement_date {

        public static class MemberElement_date_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_date member;

            public MemberElement_date_Iterator(MemberElement_date member) {
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
                org.mitre.caasd.aixm.gmd.CI_Date_PropertyType nx = new org.mitre.caasd.aixm.gmd.CI_Date_PropertyType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_date(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.gmd.CI_Date_PropertyType at(int index) {
            return new org.mitre.caasd.aixm.gmd.CI_Date_PropertyType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.gmd.CI_Date_PropertyType first() {
            return new org.mitre.caasd.aixm.gmd.CI_Date_PropertyType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.gmd.CI_Date_PropertyType last() {
            return new org.mitre.caasd.aixm.gmd.CI_Date_PropertyType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.gmd.CI_Date_PropertyType append() {
            return new org.mitre.caasd.aixm.gmd.CI_Date_PropertyType(owner.createElement(info));
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
            return new MemberElement_date_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_edition edition;

    public static class MemberElement_edition {

        public static class MemberElement_edition_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_edition member;

            public MemberElement_edition_Iterator(MemberElement_edition member) {
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
                org.mitre.caasd.aixm.gco.CharacterString_PropertyType nx = new org.mitre.caasd.aixm.gco.CharacterString_PropertyType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_edition(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.gco.CharacterString_PropertyType at(int index) {
            return new org.mitre.caasd.aixm.gco.CharacterString_PropertyType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.gco.CharacterString_PropertyType first() {
            return new org.mitre.caasd.aixm.gco.CharacterString_PropertyType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.gco.CharacterString_PropertyType last() {
            return new org.mitre.caasd.aixm.gco.CharacterString_PropertyType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.gco.CharacterString_PropertyType append() {
            return new org.mitre.caasd.aixm.gco.CharacterString_PropertyType(owner.createElement(info));
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
            return new MemberElement_edition_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_editionDate editionDate;

    public static class MemberElement_editionDate {

        public static class MemberElement_editionDate_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_editionDate member;

            public MemberElement_editionDate_Iterator(MemberElement_editionDate member) {
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
                org.mitre.caasd.aixm.gco.Date_PropertyType nx = new org.mitre.caasd.aixm.gco.Date_PropertyType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_editionDate(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.gco.Date_PropertyType at(int index) {
            return new org.mitre.caasd.aixm.gco.Date_PropertyType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.gco.Date_PropertyType first() {
            return new org.mitre.caasd.aixm.gco.Date_PropertyType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.gco.Date_PropertyType last() {
            return new org.mitre.caasd.aixm.gco.Date_PropertyType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.gco.Date_PropertyType append() {
            return new org.mitre.caasd.aixm.gco.Date_PropertyType(owner.createElement(info));
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
            return new MemberElement_editionDate_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_identifier identifier;

    public static class MemberElement_identifier {

        public static class MemberElement_identifier_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_identifier member;

            public MemberElement_identifier_Iterator(MemberElement_identifier member) {
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
                org.mitre.caasd.aixm.gmd.MD_Identifier_PropertyType nx = new org.mitre.caasd.aixm.gmd.MD_Identifier_PropertyType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_identifier(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.gmd.MD_Identifier_PropertyType at(int index) {
            return new org.mitre.caasd.aixm.gmd.MD_Identifier_PropertyType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.gmd.MD_Identifier_PropertyType first() {
            return new org.mitre.caasd.aixm.gmd.MD_Identifier_PropertyType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.gmd.MD_Identifier_PropertyType last() {
            return new org.mitre.caasd.aixm.gmd.MD_Identifier_PropertyType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.gmd.MD_Identifier_PropertyType append() {
            return new org.mitre.caasd.aixm.gmd.MD_Identifier_PropertyType(owner.createElement(info));
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
            return new MemberElement_identifier_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_citedResponsibleParty citedResponsibleParty;

    public static class MemberElement_citedResponsibleParty {

        public static class MemberElement_citedResponsibleParty_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_citedResponsibleParty member;

            public MemberElement_citedResponsibleParty_Iterator(MemberElement_citedResponsibleParty member) {
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
                org.mitre.caasd.aixm.gmd.CI_ResponsibleParty_PropertyType nx = new org.mitre.caasd.aixm.gmd.CI_ResponsibleParty_PropertyType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_citedResponsibleParty(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.gmd.CI_ResponsibleParty_PropertyType at(int index) {
            return new org.mitre.caasd.aixm.gmd.CI_ResponsibleParty_PropertyType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.gmd.CI_ResponsibleParty_PropertyType first() {
            return new org.mitre.caasd.aixm.gmd.CI_ResponsibleParty_PropertyType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.gmd.CI_ResponsibleParty_PropertyType last() {
            return new org.mitre.caasd.aixm.gmd.CI_ResponsibleParty_PropertyType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.gmd.CI_ResponsibleParty_PropertyType append() {
            return new org.mitre.caasd.aixm.gmd.CI_ResponsibleParty_PropertyType(owner.createElement(info));
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
            return new MemberElement_citedResponsibleParty_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_presentationForm presentationForm;

    public static class MemberElement_presentationForm {

        public static class MemberElement_presentationForm_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_presentationForm member;

            public MemberElement_presentationForm_Iterator(MemberElement_presentationForm member) {
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
                org.mitre.caasd.aixm.gmd.CI_PresentationFormCode_PropertyType nx = new org.mitre.caasd.aixm.gmd.CI_PresentationFormCode_PropertyType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_presentationForm(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.gmd.CI_PresentationFormCode_PropertyType at(int index) {
            return new org.mitre.caasd.aixm.gmd.CI_PresentationFormCode_PropertyType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.gmd.CI_PresentationFormCode_PropertyType first() {
            return new org.mitre.caasd.aixm.gmd.CI_PresentationFormCode_PropertyType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.gmd.CI_PresentationFormCode_PropertyType last() {
            return new org.mitre.caasd.aixm.gmd.CI_PresentationFormCode_PropertyType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.gmd.CI_PresentationFormCode_PropertyType append() {
            return new org.mitre.caasd.aixm.gmd.CI_PresentationFormCode_PropertyType(owner.createElement(info));
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
            return new MemberElement_presentationForm_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_series series;

    public static class MemberElement_series {

        public static class MemberElement_series_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_series member;

            public MemberElement_series_Iterator(MemberElement_series member) {
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
                org.mitre.caasd.aixm.gmd.CI_Series_PropertyType nx = new org.mitre.caasd.aixm.gmd.CI_Series_PropertyType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_series(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.gmd.CI_Series_PropertyType at(int index) {
            return new org.mitre.caasd.aixm.gmd.CI_Series_PropertyType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.gmd.CI_Series_PropertyType first() {
            return new org.mitre.caasd.aixm.gmd.CI_Series_PropertyType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.gmd.CI_Series_PropertyType last() {
            return new org.mitre.caasd.aixm.gmd.CI_Series_PropertyType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.gmd.CI_Series_PropertyType append() {
            return new org.mitre.caasd.aixm.gmd.CI_Series_PropertyType(owner.createElement(info));
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
            return new MemberElement_series_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_otherCitationDetails otherCitationDetails;

    public static class MemberElement_otherCitationDetails {

        public static class MemberElement_otherCitationDetails_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_otherCitationDetails member;

            public MemberElement_otherCitationDetails_Iterator(MemberElement_otherCitationDetails member) {
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
                org.mitre.caasd.aixm.gco.CharacterString_PropertyType nx = new org.mitre.caasd.aixm.gco.CharacterString_PropertyType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_otherCitationDetails(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.gco.CharacterString_PropertyType at(int index) {
            return new org.mitre.caasd.aixm.gco.CharacterString_PropertyType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.gco.CharacterString_PropertyType first() {
            return new org.mitre.caasd.aixm.gco.CharacterString_PropertyType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.gco.CharacterString_PropertyType last() {
            return new org.mitre.caasd.aixm.gco.CharacterString_PropertyType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.gco.CharacterString_PropertyType append() {
            return new org.mitre.caasd.aixm.gco.CharacterString_PropertyType(owner.createElement(info));
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
            return new MemberElement_otherCitationDetails_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_collectiveTitle collectiveTitle;

    public static class MemberElement_collectiveTitle {

        public static class MemberElement_collectiveTitle_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_collectiveTitle member;

            public MemberElement_collectiveTitle_Iterator(MemberElement_collectiveTitle member) {
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
                org.mitre.caasd.aixm.gco.CharacterString_PropertyType nx = new org.mitre.caasd.aixm.gco.CharacterString_PropertyType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_collectiveTitle(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.gco.CharacterString_PropertyType at(int index) {
            return new org.mitre.caasd.aixm.gco.CharacterString_PropertyType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.gco.CharacterString_PropertyType first() {
            return new org.mitre.caasd.aixm.gco.CharacterString_PropertyType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.gco.CharacterString_PropertyType last() {
            return new org.mitre.caasd.aixm.gco.CharacterString_PropertyType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.gco.CharacterString_PropertyType append() {
            return new org.mitre.caasd.aixm.gco.CharacterString_PropertyType(owner.createElement(info));
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
            return new MemberElement_collectiveTitle_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_ISBN ISBN;

    public static class MemberElement_ISBN {

        public static class MemberElement_ISBN_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_ISBN member;

            public MemberElement_ISBN_Iterator(MemberElement_ISBN member) {
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
                org.mitre.caasd.aixm.gco.CharacterString_PropertyType nx = new org.mitre.caasd.aixm.gco.CharacterString_PropertyType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_ISBN(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.gco.CharacterString_PropertyType at(int index) {
            return new org.mitre.caasd.aixm.gco.CharacterString_PropertyType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.gco.CharacterString_PropertyType first() {
            return new org.mitre.caasd.aixm.gco.CharacterString_PropertyType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.gco.CharacterString_PropertyType last() {
            return new org.mitre.caasd.aixm.gco.CharacterString_PropertyType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.gco.CharacterString_PropertyType append() {
            return new org.mitre.caasd.aixm.gco.CharacterString_PropertyType(owner.createElement(info));
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
            return new MemberElement_ISBN_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public MemberElement_ISSN ISSN;

    public static class MemberElement_ISSN {

        public static class MemberElement_ISSN_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_ISSN member;

            public MemberElement_ISSN_Iterator(MemberElement_ISSN member) {
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
                org.mitre.caasd.aixm.gco.CharacterString_PropertyType nx = new org.mitre.caasd.aixm.gco.CharacterString_PropertyType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_ISSN(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.gco.CharacterString_PropertyType at(int index) {
            return new org.mitre.caasd.aixm.gco.CharacterString_PropertyType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.gco.CharacterString_PropertyType first() {
            return new org.mitre.caasd.aixm.gco.CharacterString_PropertyType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.gco.CharacterString_PropertyType last() {
            return new org.mitre.caasd.aixm.gco.CharacterString_PropertyType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.gco.CharacterString_PropertyType append() {
            return new org.mitre.caasd.aixm.gco.CharacterString_PropertyType(owner.createElement(info));
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
            return new MemberElement_ISSN_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public void setXsiType() {
        org.mitre.caasd.aixm.xml.XmlTreeOperations.setAttribute(getNode(), "http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "http://www.isotc211.org/2005/gmd", "CI_Citation_Type");
    }
}
