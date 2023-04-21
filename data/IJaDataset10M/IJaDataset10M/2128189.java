package org.mitre.caasd.aixm.proc;

public class TerminalSegmentPointExtensionType extends org.mitre.caasd.aixm.aixm.AbstractExtensionType {

    public static org.mitre.caasd.aixm.xml.meta.ComplexType getStaticInfo() {
        return new org.mitre.caasd.aixm.xml.meta.ComplexType(org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getTypes()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_ti_proc_altova_TerminalSegmentPointExtensionType]);
    }

    public TerminalSegmentPointExtensionType(org.w3c.dom.Node init) {
        super(init);
        instantiateMembers();
    }

    private void instantiateMembers() {
        profileYesNo = new MemberElement_profileYesNo(this, org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo.binder.getMembers()[org.mitre.caasd.aixm.FPP_SIAP_Data_TypeInfo._altova_mi_proc_altova_TerminalSegmentPointExtensionType._profileYesNo]);
    }

    public MemberElement_profileYesNo profileYesNo;

    public static class MemberElement_profileYesNo {

        public static class MemberElement_profileYesNo_Iterator implements java.util.Iterator {

            private org.w3c.dom.Node nextNode;

            private MemberElement_profileYesNo member;

            public MemberElement_profileYesNo_Iterator(MemberElement_profileYesNo member) {
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
                org.mitre.caasd.aixm.proc.profileYesNoType nx = new org.mitre.caasd.aixm.proc.profileYesNoType(nextNode);
                nextNode = nextNode.getNextSibling();
                return nx;
            }

            public void remove() {
            }
        }

        protected org.mitre.caasd.aixm.xml.TypeBase owner;

        protected org.mitre.caasd.aixm.typeinfo.MemberInfo info;

        public MemberElement_profileYesNo(org.mitre.caasd.aixm.xml.TypeBase owner, org.mitre.caasd.aixm.typeinfo.MemberInfo info) {
            this.owner = owner;
            this.info = info;
        }

        public org.mitre.caasd.aixm.proc.profileYesNoType at(int index) {
            return new org.mitre.caasd.aixm.proc.profileYesNoType(owner.getElementAt(info, index));
        }

        public org.mitre.caasd.aixm.proc.profileYesNoType first() {
            return new org.mitre.caasd.aixm.proc.profileYesNoType(owner.getElementFirst(info));
        }

        public org.mitre.caasd.aixm.proc.profileYesNoType last() {
            return new org.mitre.caasd.aixm.proc.profileYesNoType(owner.getElementLast(info));
        }

        public org.mitre.caasd.aixm.proc.profileYesNoType append() {
            return new org.mitre.caasd.aixm.proc.profileYesNoType(owner.createElement(info));
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
            return new MemberElement_profileYesNo_Iterator(this);
        }

        public org.mitre.caasd.aixm.xml.meta.Element getInfo() {
            return new org.mitre.caasd.aixm.xml.meta.Element(info);
        }
    }

    public void setXsiType() {
        org.mitre.caasd.aixm.xml.XmlTreeOperations.setAttribute(getNode(), "http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "http://www.faa.gov/FPP/procedures", "TerminalSegmentPointExtensionType");
    }
}
