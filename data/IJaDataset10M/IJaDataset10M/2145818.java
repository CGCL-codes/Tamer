package unbbayes.io.xmlbif.version4.xmlclasses.impl;

public class BIFTypeDriver implements org.apache.ws.jaxme.impl.JMSAXDriver {

    public static class HEADERTypeDriver implements org.apache.ws.jaxme.impl.JMSAXDriver {

        public org.xml.sax.helpers.AttributesImpl getAttributes(org.apache.ws.jaxme.impl.JMSAXDriverController pController, java.lang.Object pObject) throws org.xml.sax.SAXException {
            org.xml.sax.helpers.AttributesImpl _1 = new org.xml.sax.helpers.AttributesImpl();
            return _1;
        }

        public java.lang.String getPreferredPrefix(java.lang.String pURI) {
            if (pURI == null) {
                pURI = "";
            }
            if (pURI.equals("http://localhost/xml/model.xsd")) {
                return "mstns";
            }
            return null;
        }

        public void marshalChilds(org.apache.ws.jaxme.impl.JMSAXDriverController pController, org.xml.sax.ContentHandler pHandler, java.lang.Object pObject) throws org.xml.sax.SAXException {
            unbbayes.io.xmlbif.version4.xmlclasses.BIFType.HEADERType _1 = (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.HEADERType) pObject;
            java.lang.String _2 = _1.getNAME();
            if (_2 != null) {
                pController.marshalSimpleChild(this, "http://localhost/xml/model.xsd", "NAME", _1.getNAME());
            }
            pController.marshalSimpleChild(this, "http://localhost/xml/model.xsd", "VERSION", pController.getDatatypeConverter().printInt(_1.getVERSION()));
            java.lang.String _3 = _1.getCREATOR();
            if (_3 != null) {
                pController.marshalSimpleChild(this, "http://localhost/xml/model.xsd", "CREATOR", _1.getCREATOR());
            }
        }
    }

    public static class STATICPROPERTYTypeDriver implements org.apache.ws.jaxme.impl.JMSAXDriver {

        public org.xml.sax.helpers.AttributesImpl getAttributes(org.apache.ws.jaxme.impl.JMSAXDriverController pController, java.lang.Object pObject) throws org.xml.sax.SAXException {
            org.xml.sax.helpers.AttributesImpl _1 = new org.xml.sax.helpers.AttributesImpl();
            return _1;
        }

        public java.lang.String getPreferredPrefix(java.lang.String pURI) {
            if (pURI == null) {
                pURI = "";
            }
            if (pURI.equals("http://localhost/xml/model.xsd")) {
                return "mstns";
            }
            return null;
        }

        public void marshalChilds(org.apache.ws.jaxme.impl.JMSAXDriverController pController, org.xml.sax.ContentHandler pHandler, java.lang.Object pObject) throws org.xml.sax.SAXException {
            unbbayes.io.xmlbif.version4.xmlclasses.BIFType.STATICPROPERTYType _1 = (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.STATICPROPERTYType) pObject;
            java.lang.String _2 = _1.getNODESIZE();
            if (_2 != null) {
                pController.marshalSimpleChild(this, "http://localhost/xml/model.xsd", "NODESIZE", _1.getNODESIZE());
            }
            java.lang.String _3 = _1.getNODEFONTNAME();
            if (_3 != null) {
                pController.marshalSimpleChild(this, "http://localhost/xml/model.xsd", "NODEFONTNAME", _1.getNODEFONTNAME());
            }
            pController.marshalSimpleChild(this, "http://localhost/xml/model.xsd", "NODEFONTSIZE", pController.getDatatypeConverter().printInt(_1.getNODEFONTSIZE()));
            pController.marshalSimpleChild(this, "http://localhost/xml/model.xsd", "COLORUTILITY", pController.getDatatypeConverter().printInt(_1.getCOLORUTILITY()));
            pController.marshalSimpleChild(this, "http://localhost/xml/model.xsd", "COLORDECISION", pController.getDatatypeConverter().printInt(_1.getCOLORDECISION()));
            pController.marshalSimpleChild(this, "http://localhost/xml/model.xsd", "COLORPROBDESCRIPTION", pController.getDatatypeConverter().printInt(_1.getCOLORPROBDESCRIPTION()));
            pController.marshalSimpleChild(this, "http://localhost/xml/model.xsd", "COLORPROBEXPLANATION", pController.getDatatypeConverter().printInt(_1.getCOLORPROBEXPLANATION()));
        }
    }

    public static class HIERARCHYTypeDriver implements org.apache.ws.jaxme.impl.JMSAXDriver {

        public static class ROOTTypeDriver implements org.apache.ws.jaxme.impl.JMSAXDriver {

            public static class LEVELTypeDriver implements org.apache.ws.jaxme.impl.JMSAXDriver {

                public org.xml.sax.helpers.AttributesImpl getAttributes(org.apache.ws.jaxme.impl.JMSAXDriverController pController, java.lang.Object pObject) throws org.xml.sax.SAXException {
                    org.xml.sax.helpers.AttributesImpl _1 = new org.xml.sax.helpers.AttributesImpl();
                    unbbayes.io.xmlbif.version4.xmlclasses.BIFType.HIERARCHYType.ROOTType.LEVELType _2 = (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.HIERARCHYType.ROOTType.LEVELType) pObject;
                    java.lang.String _3 = _2.getNAME();
                    if (_3 != null) {
                        _1.addAttribute("", "NAME", pController.getAttrQName(this, "", "NAME"), "CDATA", _2.getNAME());
                    }
                    return _1;
                }

                public java.lang.String getPreferredPrefix(java.lang.String pURI) {
                    return null;
                }

                public void marshalChilds(org.apache.ws.jaxme.impl.JMSAXDriverController pController, org.xml.sax.ContentHandler pHandler, java.lang.Object pObject) throws org.xml.sax.SAXException {
                }
            }

            public org.xml.sax.helpers.AttributesImpl getAttributes(org.apache.ws.jaxme.impl.JMSAXDriverController pController, java.lang.Object pObject) throws org.xml.sax.SAXException {
                org.xml.sax.helpers.AttributesImpl _1 = new org.xml.sax.helpers.AttributesImpl();
                unbbayes.io.xmlbif.version4.xmlclasses.BIFType.HIERARCHYType.ROOTType _2 = (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.HIERARCHYType.ROOTType) pObject;
                java.lang.String _3 = _2.getNAME();
                if (_3 != null) {
                    _1.addAttribute("", "NAME", pController.getAttrQName(this, "", "NAME"), "CDATA", _2.getNAME());
                }
                return _1;
            }

            public java.lang.String getPreferredPrefix(java.lang.String pURI) {
                if (pURI == null) {
                    pURI = "";
                }
                if (pURI.equals("http://localhost/xml/model.xsd")) {
                    return "mstns";
                }
                return null;
            }

            public void marshalChilds(org.apache.ws.jaxme.impl.JMSAXDriverController pController, org.xml.sax.ContentHandler pHandler, java.lang.Object pObject) throws org.xml.sax.SAXException {
                unbbayes.io.xmlbif.version4.xmlclasses.BIFType.HIERARCHYType.ROOTType _1 = (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.HIERARCHYType.ROOTType) pObject;
                java.util.List _2 = _1.getLEVEL();
                for (int _3 = 0; _3 < (_2).size(); _3++) {
                    org.apache.ws.jaxme.impl.JMSAXDriver _4 = new unbbayes.io.xmlbif.version4.xmlclasses.impl.BIFTypeDriver.HIERARCHYTypeDriver.ROOTTypeDriver.LEVELTypeDriver();
                    pController.marshal(_4, "http://localhost/xml/model.xsd", "LEVEL", (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.HIERARCHYType.ROOTType.LEVELType) _2.get(_3));
                }
            }
        }

        public org.xml.sax.helpers.AttributesImpl getAttributes(org.apache.ws.jaxme.impl.JMSAXDriverController pController, java.lang.Object pObject) throws org.xml.sax.SAXException {
            org.xml.sax.helpers.AttributesImpl _1 = new org.xml.sax.helpers.AttributesImpl();
            return _1;
        }

        public java.lang.String getPreferredPrefix(java.lang.String pURI) {
            if (pURI == null) {
                pURI = "";
            }
            if (pURI.equals("http://localhost/xml/model.xsd")) {
                return "mstns";
            }
            return null;
        }

        public void marshalChilds(org.apache.ws.jaxme.impl.JMSAXDriverController pController, org.xml.sax.ContentHandler pHandler, java.lang.Object pObject) throws org.xml.sax.SAXException {
            unbbayes.io.xmlbif.version4.xmlclasses.BIFType.HIERARCHYType _1 = (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.HIERARCHYType) pObject;
            java.util.List _2 = _1.getROOT();
            for (int _3 = 0; _3 < (_2).size(); _3++) {
                org.apache.ws.jaxme.impl.JMSAXDriver _4 = new unbbayes.io.xmlbif.version4.xmlclasses.impl.BIFTypeDriver.HIERARCHYTypeDriver.ROOTTypeDriver();
                pController.marshal(_4, "http://localhost/xml/model.xsd", "ROOT", (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.HIERARCHYType.ROOTType) _2.get(_3));
            }
        }
    }

    public static class NETWORKTypeDriver implements org.apache.ws.jaxme.impl.JMSAXDriver {

        public static class VARIABLESTypeDriver implements org.apache.ws.jaxme.impl.JMSAXDriver {

            public static class VARTypeDriver implements org.apache.ws.jaxme.impl.JMSAXDriver {

                public static class STATENAMETypeDriver implements org.apache.ws.jaxme.impl.JMSAXDriver {

                    public org.xml.sax.helpers.AttributesImpl getAttributes(org.apache.ws.jaxme.impl.JMSAXDriverController pController, java.lang.Object pObject) throws org.xml.sax.SAXException {
                        org.xml.sax.helpers.AttributesImpl _1 = new org.xml.sax.helpers.AttributesImpl();
                        return _1;
                    }

                    public java.lang.String getPreferredPrefix(java.lang.String pURI) {
                        return null;
                    }

                    public void marshalChilds(org.apache.ws.jaxme.impl.JMSAXDriverController pController, org.xml.sax.ContentHandler pHandler, java.lang.Object pObject) throws org.xml.sax.SAXException {
                        unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.VARIABLESType.VARType.STATENAMEType _1 = (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.VARIABLESType.VARType.STATENAMEType) pObject;
                        java.lang.String _2 = _1.getValue();
                        if (_2 != null && _2.length() > 0) {
                            char[] _3 = _2.toCharArray();
                            pHandler.characters(_3, 0, _3.length);
                        }
                    }
                }

                public static class METAPHORETypeDriver implements org.apache.ws.jaxme.impl.JMSAXDriver {

                    public static class TRIGGERTypeDriver implements org.apache.ws.jaxme.impl.JMSAXDriver {

                        public org.xml.sax.helpers.AttributesImpl getAttributes(org.apache.ws.jaxme.impl.JMSAXDriverController pController, java.lang.Object pObject) throws org.xml.sax.SAXException {
                            org.xml.sax.helpers.AttributesImpl _1 = new org.xml.sax.helpers.AttributesImpl();
                            return _1;
                        }

                        public java.lang.String getPreferredPrefix(java.lang.String pURI) {
                            if (pURI == null) {
                                pURI = "";
                            }
                            if (pURI.equals("http://localhost/xml/model.xsd")) {
                                return "mstns";
                            }
                            return null;
                        }

                        public void marshalChilds(org.apache.ws.jaxme.impl.JMSAXDriverController pController, org.xml.sax.ContentHandler pHandler, java.lang.Object pObject) throws org.xml.sax.SAXException {
                            unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.VARIABLESType.VARType.METAPHOREType.TRIGGERType _1 = (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.VARIABLESType.VARType.METAPHOREType.TRIGGERType) pObject;
                            java.lang.String _2 = _1.getNAME();
                            if (_2 != null) {
                                pController.marshalSimpleChild(this, "http://localhost/xml/model.xsd", "NAME", _1.getNAME());
                            }
                            java.lang.String _3 = _1.getCOMMENTS();
                            if (_3 != null) {
                                pController.marshalSimpleChild(this, "http://localhost/xml/model.xsd", "COMMENTS", _1.getCOMMENTS());
                            }
                        }
                    }

                    public static class EXCLUDENTTypeDriver implements org.apache.ws.jaxme.impl.JMSAXDriver {

                        public org.xml.sax.helpers.AttributesImpl getAttributes(org.apache.ws.jaxme.impl.JMSAXDriverController pController, java.lang.Object pObject) throws org.xml.sax.SAXException {
                            org.xml.sax.helpers.AttributesImpl _1 = new org.xml.sax.helpers.AttributesImpl();
                            return _1;
                        }

                        public java.lang.String getPreferredPrefix(java.lang.String pURI) {
                            if (pURI == null) {
                                pURI = "";
                            }
                            if (pURI.equals("http://localhost/xml/model.xsd")) {
                                return "mstns";
                            }
                            return null;
                        }

                        public void marshalChilds(org.apache.ws.jaxme.impl.JMSAXDriverController pController, org.xml.sax.ContentHandler pHandler, java.lang.Object pObject) throws org.xml.sax.SAXException {
                            unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.VARIABLESType.VARType.METAPHOREType.EXCLUDENTType _1 = (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.VARIABLESType.VARType.METAPHOREType.EXCLUDENTType) pObject;
                            java.lang.String _2 = _1.getNAME();
                            if (_2 != null) {
                                pController.marshalSimpleChild(this, "http://localhost/xml/model.xsd", "NAME", _1.getNAME());
                            }
                            java.lang.String _3 = _1.getCOMMENTS();
                            if (_3 != null) {
                                pController.marshalSimpleChild(this, "http://localhost/xml/model.xsd", "COMMENTS", _1.getCOMMENTS());
                            }
                        }
                    }

                    public static class ESSENCIALTypeDriver implements org.apache.ws.jaxme.impl.JMSAXDriver {

                        public org.xml.sax.helpers.AttributesImpl getAttributes(org.apache.ws.jaxme.impl.JMSAXDriverController pController, java.lang.Object pObject) throws org.xml.sax.SAXException {
                            org.xml.sax.helpers.AttributesImpl _1 = new org.xml.sax.helpers.AttributesImpl();
                            return _1;
                        }

                        public java.lang.String getPreferredPrefix(java.lang.String pURI) {
                            if (pURI == null) {
                                pURI = "";
                            }
                            if (pURI.equals("http://localhost/xml/model.xsd")) {
                                return "mstns";
                            }
                            return null;
                        }

                        public void marshalChilds(org.apache.ws.jaxme.impl.JMSAXDriverController pController, org.xml.sax.ContentHandler pHandler, java.lang.Object pObject) throws org.xml.sax.SAXException {
                            unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.VARIABLESType.VARType.METAPHOREType.ESSENCIALType _1 = (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.VARIABLESType.VARType.METAPHOREType.ESSENCIALType) pObject;
                            java.lang.String _2 = _1.getNAME();
                            if (_2 != null) {
                                pController.marshalSimpleChild(this, "http://localhost/xml/model.xsd", "NAME", _1.getNAME());
                            }
                            java.lang.String _3 = _1.getCOMMENTS();
                            if (_3 != null) {
                                pController.marshalSimpleChild(this, "http://localhost/xml/model.xsd", "COMMENTS", _1.getCOMMENTS());
                            }
                        }
                    }

                    public static class COMPLEMENTARYTypeDriver implements org.apache.ws.jaxme.impl.JMSAXDriver {

                        public org.xml.sax.helpers.AttributesImpl getAttributes(org.apache.ws.jaxme.impl.JMSAXDriverController pController, java.lang.Object pObject) throws org.xml.sax.SAXException {
                            org.xml.sax.helpers.AttributesImpl _1 = new org.xml.sax.helpers.AttributesImpl();
                            return _1;
                        }

                        public java.lang.String getPreferredPrefix(java.lang.String pURI) {
                            if (pURI == null) {
                                pURI = "";
                            }
                            if (pURI.equals("http://localhost/xml/model.xsd")) {
                                return "mstns";
                            }
                            return null;
                        }

                        public void marshalChilds(org.apache.ws.jaxme.impl.JMSAXDriverController pController, org.xml.sax.ContentHandler pHandler, java.lang.Object pObject) throws org.xml.sax.SAXException {
                            unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.VARIABLESType.VARType.METAPHOREType.COMPLEMENTARYType _1 = (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.VARIABLESType.VARType.METAPHOREType.COMPLEMENTARYType) pObject;
                            java.lang.String _2 = _1.getNAME();
                            if (_2 != null) {
                                pController.marshalSimpleChild(this, "http://localhost/xml/model.xsd", "NAME", _1.getNAME());
                            }
                            java.lang.String _3 = _1.getCOMMENTS();
                            if (_3 != null) {
                                pController.marshalSimpleChild(this, "http://localhost/xml/model.xsd", "COMMENTS", _1.getCOMMENTS());
                            }
                        }
                    }

                    public static class NATypeDriver implements org.apache.ws.jaxme.impl.JMSAXDriver {

                        public org.xml.sax.helpers.AttributesImpl getAttributes(org.apache.ws.jaxme.impl.JMSAXDriverController pController, java.lang.Object pObject) throws org.xml.sax.SAXException {
                            org.xml.sax.helpers.AttributesImpl _1 = new org.xml.sax.helpers.AttributesImpl();
                            return _1;
                        }

                        public java.lang.String getPreferredPrefix(java.lang.String pURI) {
                            if (pURI == null) {
                                pURI = "";
                            }
                            if (pURI.equals("http://localhost/xml/model.xsd")) {
                                return "mstns";
                            }
                            return null;
                        }

                        public void marshalChilds(org.apache.ws.jaxme.impl.JMSAXDriverController pController, org.xml.sax.ContentHandler pHandler, java.lang.Object pObject) throws org.xml.sax.SAXException {
                            unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.VARIABLESType.VARType.METAPHOREType.NAType _1 = (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.VARIABLESType.VARType.METAPHOREType.NAType) pObject;
                            java.lang.String _2 = _1.getNAME();
                            if (_2 != null) {
                                pController.marshalSimpleChild(this, "http://localhost/xml/model.xsd", "NAME", _1.getNAME());
                            }
                            java.lang.String _3 = _1.getCOMMENTS();
                            if (_3 != null) {
                                pController.marshalSimpleChild(this, "http://localhost/xml/model.xsd", "COMMENTS", _1.getCOMMENTS());
                            }
                        }
                    }

                    public org.xml.sax.helpers.AttributesImpl getAttributes(org.apache.ws.jaxme.impl.JMSAXDriverController pController, java.lang.Object pObject) throws org.xml.sax.SAXException {
                        org.xml.sax.helpers.AttributesImpl _1 = new org.xml.sax.helpers.AttributesImpl();
                        return _1;
                    }

                    public java.lang.String getPreferredPrefix(java.lang.String pURI) {
                        if (pURI == null) {
                            pURI = "";
                        }
                        if (pURI.equals("http://localhost/xml/model.xsd")) {
                            return "mstns";
                        }
                        return null;
                    }

                    public void marshalChilds(org.apache.ws.jaxme.impl.JMSAXDriverController pController, org.xml.sax.ContentHandler pHandler, java.lang.Object pObject) throws org.xml.sax.SAXException {
                        unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.VARIABLESType.VARType.METAPHOREType _1 = (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.VARIABLESType.VARType.METAPHOREType) pObject;
                        java.util.List _2 = _1.getDESCRIPTION();
                        for (int _3 = 0; _3 < (_2).size(); _3++) {
                            java.lang.String _4 = (java.lang.String) _2.get(_3);
                            if (_4 != null) {
                                pController.marshalSimpleChild(this, "http://localhost/xml/model.xsd", "DESCRIPTION", (java.lang.String) _2.get(_3));
                            }
                        }
                        java.util.List _5 = _1.getTRIGGER();
                        for (int _6 = 0; _6 < (_5).size(); _6++) {
                            org.apache.ws.jaxme.impl.JMSAXDriver _7 = new unbbayes.io.xmlbif.version4.xmlclasses.impl.BIFTypeDriver.NETWORKTypeDriver.VARIABLESTypeDriver.VARTypeDriver.METAPHORETypeDriver.TRIGGERTypeDriver();
                            pController.marshal(_7, "http://localhost/xml/model.xsd", "TRIGGER", (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.VARIABLESType.VARType.METAPHOREType.TRIGGERType) _5.get(_6));
                        }
                        java.util.List _8 = _1.getEXCLUDENT();
                        for (int _9 = 0; _9 < (_8).size(); _9++) {
                            org.apache.ws.jaxme.impl.JMSAXDriver _10 = new unbbayes.io.xmlbif.version4.xmlclasses.impl.BIFTypeDriver.NETWORKTypeDriver.VARIABLESTypeDriver.VARTypeDriver.METAPHORETypeDriver.EXCLUDENTTypeDriver();
                            pController.marshal(_10, "http://localhost/xml/model.xsd", "EXCLUDENT", (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.VARIABLESType.VARType.METAPHOREType.EXCLUDENTType) _8.get(_9));
                        }
                        java.util.List _11 = _1.getESSENCIAL();
                        for (int _12 = 0; _12 < (_11).size(); _12++) {
                            org.apache.ws.jaxme.impl.JMSAXDriver _13 = new unbbayes.io.xmlbif.version4.xmlclasses.impl.BIFTypeDriver.NETWORKTypeDriver.VARIABLESTypeDriver.VARTypeDriver.METAPHORETypeDriver.ESSENCIALTypeDriver();
                            pController.marshal(_13, "http://localhost/xml/model.xsd", "ESSENCIAL", (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.VARIABLESType.VARType.METAPHOREType.ESSENCIALType) _11.get(_12));
                        }
                        java.util.List _14 = _1.getCOMPLEMENTARY();
                        for (int _15 = 0; _15 < (_14).size(); _15++) {
                            org.apache.ws.jaxme.impl.JMSAXDriver _16 = new unbbayes.io.xmlbif.version4.xmlclasses.impl.BIFTypeDriver.NETWORKTypeDriver.VARIABLESTypeDriver.VARTypeDriver.METAPHORETypeDriver.COMPLEMENTARYTypeDriver();
                            pController.marshal(_16, "http://localhost/xml/model.xsd", "COMPLEMENTARY", (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.VARIABLESType.VARType.METAPHOREType.COMPLEMENTARYType) _14.get(_15));
                        }
                        java.util.List _17 = _1.getNA();
                        for (int _18 = 0; _18 < (_17).size(); _18++) {
                            org.apache.ws.jaxme.impl.JMSAXDriver _19 = new unbbayes.io.xmlbif.version4.xmlclasses.impl.BIFTypeDriver.NETWORKTypeDriver.VARIABLESTypeDriver.VARTypeDriver.METAPHORETypeDriver.NATypeDriver();
                            pController.marshal(_19, "http://localhost/xml/model.xsd", "NA", (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.VARIABLESType.VARType.METAPHOREType.NAType) _17.get(_18));
                        }
                    }
                }

                public org.xml.sax.helpers.AttributesImpl getAttributes(org.apache.ws.jaxme.impl.JMSAXDriverController pController, java.lang.Object pObject) throws org.xml.sax.SAXException {
                    org.xml.sax.helpers.AttributesImpl _1 = new org.xml.sax.helpers.AttributesImpl();
                    unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.VARIABLESType.VARType _2 = (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.VARIABLESType.VARType) pObject;
                    java.lang.String _3 = _2.getNAME();
                    if (_3 != null) {
                        _1.addAttribute("", "NAME", pController.getAttrQName(this, "", "NAME"), "CDATA", _2.getNAME());
                    }
                    java.lang.String _4 = _2.getTYPE();
                    if (_4 != null) {
                        _1.addAttribute("", "TYPE", pController.getAttrQName(this, "", "TYPE"), "CDATA", _2.getTYPE());
                    }
                    _1.addAttribute("", "XPOS", pController.getAttrQName(this, "", "XPOS"), "CDATA", pController.getDatatypeConverter().printInt(_2.getXPOS()));
                    _1.addAttribute("", "YPOS", pController.getAttrQName(this, "", "YPOS"), "CDATA", pController.getDatatypeConverter().printInt(_2.getYPOS()));
                    return _1;
                }

                public java.lang.String getPreferredPrefix(java.lang.String pURI) {
                    if (pURI == null) {
                        pURI = "";
                    }
                    if (pURI.equals("http://localhost/xml/model.xsd")) {
                        return "mstns";
                    }
                    return null;
                }

                public void marshalChilds(org.apache.ws.jaxme.impl.JMSAXDriverController pController, org.xml.sax.ContentHandler pHandler, java.lang.Object pObject) throws org.xml.sax.SAXException {
                    unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.VARIABLESType.VARType _1 = (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.VARIABLESType.VARType) pObject;
                    java.lang.String _2 = _1.getLABEL();
                    if (_2 != null) {
                        pController.marshalSimpleChild(this, "http://localhost/xml/model.xsd", "LABEL", _1.getLABEL());
                    }
                    byte[] _3 = _1.getMMIDIA();
                    if (_3 != null) {
                        pController.marshalSimpleChild(this, "http://localhost/xml/model.xsd", "MMIDIA", pController.getDatatypeConverter().printBase64Binary(_1.getMMIDIA()));
                    }
                    java.util.List _4 = _1.getSTATENAME();
                    for (int _5 = 0; _5 < (_4).size(); _5++) {
                        org.apache.ws.jaxme.impl.JMSAXDriver _6 = new unbbayes.io.xmlbif.version4.xmlclasses.impl.BIFTypeDriver.NETWORKTypeDriver.VARIABLESTypeDriver.VARTypeDriver.STATENAMETypeDriver();
                        pController.marshal(_6, "http://localhost/xml/model.xsd", "STATENAME", (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.VARIABLESType.VARType.STATENAMEType) _4.get(_5));
                    }
                    java.util.List _7 = _1.getMETAPHORE();
                    for (int _8 = 0; _8 < (_7).size(); _8++) {
                        org.apache.ws.jaxme.impl.JMSAXDriver _9 = new unbbayes.io.xmlbif.version4.xmlclasses.impl.BIFTypeDriver.NETWORKTypeDriver.VARIABLESTypeDriver.VARTypeDriver.METAPHORETypeDriver();
                        pController.marshal(_9, "http://localhost/xml/model.xsd", "METAPHORE", (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.VARIABLESType.VARType.METAPHOREType) _7.get(_8));
                    }
                }
            }

            public static class DECISIONTypeDriver implements org.apache.ws.jaxme.impl.JMSAXDriver {

                public static class STATENAMETypeDriver implements org.apache.ws.jaxme.impl.JMSAXDriver {

                    public org.xml.sax.helpers.AttributesImpl getAttributes(org.apache.ws.jaxme.impl.JMSAXDriverController pController, java.lang.Object pObject) throws org.xml.sax.SAXException {
                        org.xml.sax.helpers.AttributesImpl _1 = new org.xml.sax.helpers.AttributesImpl();
                        return _1;
                    }

                    public java.lang.String getPreferredPrefix(java.lang.String pURI) {
                        return null;
                    }

                    public void marshalChilds(org.apache.ws.jaxme.impl.JMSAXDriverController pController, org.xml.sax.ContentHandler pHandler, java.lang.Object pObject) throws org.xml.sax.SAXException {
                        unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.VARIABLESType.DECISIONType.STATENAMEType _1 = (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.VARIABLESType.DECISIONType.STATENAMEType) pObject;
                        java.lang.String _2 = _1.getValue();
                        if (_2 != null && _2.length() > 0) {
                            char[] _3 = _2.toCharArray();
                            pHandler.characters(_3, 0, _3.length);
                        }
                    }
                }

                public org.xml.sax.helpers.AttributesImpl getAttributes(org.apache.ws.jaxme.impl.JMSAXDriverController pController, java.lang.Object pObject) throws org.xml.sax.SAXException {
                    org.xml.sax.helpers.AttributesImpl _1 = new org.xml.sax.helpers.AttributesImpl();
                    unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.VARIABLESType.DECISIONType _2 = (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.VARIABLESType.DECISIONType) pObject;
                    java.lang.String _3 = _2.getNAME();
                    if (_3 != null) {
                        _1.addAttribute("", "NAME", pController.getAttrQName(this, "", "NAME"), "CDATA", _2.getNAME());
                    }
                    java.lang.String _4 = _2.getTYPE();
                    if (_4 != null) {
                        _1.addAttribute("", "TYPE", pController.getAttrQName(this, "", "TYPE"), "CDATA", _2.getTYPE());
                    }
                    _1.addAttribute("", "XPOS", pController.getAttrQName(this, "", "XPOS"), "CDATA", pController.getDatatypeConverter().printInt(_2.getXPOS()));
                    _1.addAttribute("", "YPOS", pController.getAttrQName(this, "", "YPOS"), "CDATA", pController.getDatatypeConverter().printInt(_2.getYPOS()));
                    return _1;
                }

                public java.lang.String getPreferredPrefix(java.lang.String pURI) {
                    if (pURI == null) {
                        pURI = "";
                    }
                    if (pURI.equals("http://localhost/xml/model.xsd")) {
                        return "mstns";
                    }
                    return null;
                }

                public void marshalChilds(org.apache.ws.jaxme.impl.JMSAXDriverController pController, org.xml.sax.ContentHandler pHandler, java.lang.Object pObject) throws org.xml.sax.SAXException {
                    unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.VARIABLESType.DECISIONType _1 = (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.VARIABLESType.DECISIONType) pObject;
                    java.lang.String _2 = _1.getLABEL();
                    if (_2 != null) {
                        pController.marshalSimpleChild(this, "http://localhost/xml/model.xsd", "LABEL", _1.getLABEL());
                    }
                    byte[] _3 = _1.getMMIDIA();
                    if (_3 != null) {
                        pController.marshalSimpleChild(this, "http://localhost/xml/model.xsd", "MMIDIA", pController.getDatatypeConverter().printBase64Binary(_1.getMMIDIA()));
                    }
                    java.util.List _4 = _1.getSTATENAME();
                    for (int _5 = 0; _5 < (_4).size(); _5++) {
                        org.apache.ws.jaxme.impl.JMSAXDriver _6 = new unbbayes.io.xmlbif.version4.xmlclasses.impl.BIFTypeDriver.NETWORKTypeDriver.VARIABLESTypeDriver.DECISIONTypeDriver.STATENAMETypeDriver();
                        pController.marshal(_6, "http://localhost/xml/model.xsd", "STATENAME", (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.VARIABLESType.DECISIONType.STATENAMEType) _4.get(_5));
                    }
                }
            }

            public static class UTILITYTypeDriver implements org.apache.ws.jaxme.impl.JMSAXDriver {

                public static class STATENAMETypeDriver implements org.apache.ws.jaxme.impl.JMSAXDriver {

                    public org.xml.sax.helpers.AttributesImpl getAttributes(org.apache.ws.jaxme.impl.JMSAXDriverController pController, java.lang.Object pObject) throws org.xml.sax.SAXException {
                        org.xml.sax.helpers.AttributesImpl _1 = new org.xml.sax.helpers.AttributesImpl();
                        return _1;
                    }

                    public java.lang.String getPreferredPrefix(java.lang.String pURI) {
                        return null;
                    }

                    public void marshalChilds(org.apache.ws.jaxme.impl.JMSAXDriverController pController, org.xml.sax.ContentHandler pHandler, java.lang.Object pObject) throws org.xml.sax.SAXException {
                        unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.VARIABLESType.UTILITYType.STATENAMEType _1 = (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.VARIABLESType.UTILITYType.STATENAMEType) pObject;
                        java.lang.String _2 = _1.getValue();
                        if (_2 != null && _2.length() > 0) {
                            char[] _3 = _2.toCharArray();
                            pHandler.characters(_3, 0, _3.length);
                        }
                    }
                }

                public org.xml.sax.helpers.AttributesImpl getAttributes(org.apache.ws.jaxme.impl.JMSAXDriverController pController, java.lang.Object pObject) throws org.xml.sax.SAXException {
                    org.xml.sax.helpers.AttributesImpl _1 = new org.xml.sax.helpers.AttributesImpl();
                    unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.VARIABLESType.UTILITYType _2 = (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.VARIABLESType.UTILITYType) pObject;
                    java.lang.String _3 = _2.getNAME();
                    if (_3 != null) {
                        _1.addAttribute("", "NAME", pController.getAttrQName(this, "", "NAME"), "CDATA", _2.getNAME());
                    }
                    java.lang.String _4 = _2.getTYPE();
                    if (_4 != null) {
                        _1.addAttribute("", "TYPE", pController.getAttrQName(this, "", "TYPE"), "CDATA", _2.getTYPE());
                    }
                    _1.addAttribute("", "XPOS", pController.getAttrQName(this, "", "XPOS"), "CDATA", pController.getDatatypeConverter().printInt(_2.getXPOS()));
                    _1.addAttribute("", "YPOS", pController.getAttrQName(this, "", "YPOS"), "CDATA", pController.getDatatypeConverter().printInt(_2.getYPOS()));
                    return _1;
                }

                public java.lang.String getPreferredPrefix(java.lang.String pURI) {
                    if (pURI == null) {
                        pURI = "";
                    }
                    if (pURI.equals("http://localhost/xml/model.xsd")) {
                        return "mstns";
                    }
                    return null;
                }

                public void marshalChilds(org.apache.ws.jaxme.impl.JMSAXDriverController pController, org.xml.sax.ContentHandler pHandler, java.lang.Object pObject) throws org.xml.sax.SAXException {
                    unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.VARIABLESType.UTILITYType _1 = (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.VARIABLESType.UTILITYType) pObject;
                    java.lang.String _2 = _1.getLABEL();
                    if (_2 != null) {
                        pController.marshalSimpleChild(this, "http://localhost/xml/model.xsd", "LABEL", _1.getLABEL());
                    }
                    byte[] _3 = _1.getMMIDIA();
                    if (_3 != null) {
                        pController.marshalSimpleChild(this, "http://localhost/xml/model.xsd", "MMIDIA", pController.getDatatypeConverter().printBase64Binary(_1.getMMIDIA()));
                    }
                    java.util.List _4 = _1.getSTATENAME();
                    for (int _5 = 0; _5 < (_4).size(); _5++) {
                        org.apache.ws.jaxme.impl.JMSAXDriver _6 = new unbbayes.io.xmlbif.version4.xmlclasses.impl.BIFTypeDriver.NETWORKTypeDriver.VARIABLESTypeDriver.UTILITYTypeDriver.STATENAMETypeDriver();
                        pController.marshal(_6, "http://localhost/xml/model.xsd", "STATENAME", (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.VARIABLESType.UTILITYType.STATENAMEType) _4.get(_5));
                    }
                }
            }

            public org.xml.sax.helpers.AttributesImpl getAttributes(org.apache.ws.jaxme.impl.JMSAXDriverController pController, java.lang.Object pObject) throws org.xml.sax.SAXException {
                org.xml.sax.helpers.AttributesImpl _1 = new org.xml.sax.helpers.AttributesImpl();
                return _1;
            }

            public java.lang.String getPreferredPrefix(java.lang.String pURI) {
                if (pURI == null) {
                    pURI = "";
                }
                if (pURI.equals("http://localhost/xml/model.xsd")) {
                    return "mstns";
                }
                return null;
            }

            public void marshalChilds(org.apache.ws.jaxme.impl.JMSAXDriverController pController, org.xml.sax.ContentHandler pHandler, java.lang.Object pObject) throws org.xml.sax.SAXException {
                unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.VARIABLESType _1 = (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.VARIABLESType) pObject;
                java.util.List _2 = _1.getVAR();
                for (int _3 = 0; _3 < (_2).size(); _3++) {
                    org.apache.ws.jaxme.impl.JMSAXDriver _4 = new unbbayes.io.xmlbif.version4.xmlclasses.impl.BIFTypeDriver.NETWORKTypeDriver.VARIABLESTypeDriver.VARTypeDriver();
                    pController.marshal(_4, "http://localhost/xml/model.xsd", "VAR", (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.VARIABLESType.VARType) _2.get(_3));
                }
                java.util.List _5 = _1.getDECISION();
                for (int _6 = 0; _6 < (_5).size(); _6++) {
                    org.apache.ws.jaxme.impl.JMSAXDriver _7 = new unbbayes.io.xmlbif.version4.xmlclasses.impl.BIFTypeDriver.NETWORKTypeDriver.VARIABLESTypeDriver.DECISIONTypeDriver();
                    pController.marshal(_7, "http://localhost/xml/model.xsd", "DECISION", (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.VARIABLESType.DECISIONType) _5.get(_6));
                }
                java.util.List _8 = _1.getUTILITY();
                for (int _9 = 0; _9 < (_8).size(); _9++) {
                    org.apache.ws.jaxme.impl.JMSAXDriver _10 = new unbbayes.io.xmlbif.version4.xmlclasses.impl.BIFTypeDriver.NETWORKTypeDriver.VARIABLESTypeDriver.UTILITYTypeDriver();
                    pController.marshal(_10, "http://localhost/xml/model.xsd", "UTILITY", (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.VARIABLESType.UTILITYType) _8.get(_9));
                }
            }
        }

        public static class STRUCTURETypeDriver implements org.apache.ws.jaxme.impl.JMSAXDriver {

            public static class ARCTypeDriver implements org.apache.ws.jaxme.impl.JMSAXDriver {

                public org.xml.sax.helpers.AttributesImpl getAttributes(org.apache.ws.jaxme.impl.JMSAXDriverController pController, java.lang.Object pObject) throws org.xml.sax.SAXException {
                    org.xml.sax.helpers.AttributesImpl _1 = new org.xml.sax.helpers.AttributesImpl();
                    unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.STRUCTUREType.ARCType _2 = (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.STRUCTUREType.ARCType) pObject;
                    java.lang.String _3 = _2.getPARENT();
                    if (_3 != null) {
                        _1.addAttribute("", "PARENT", pController.getAttrQName(this, "", "PARENT"), "CDATA", _2.getPARENT());
                    }
                    java.lang.String _4 = _2.getCHILD();
                    if (_4 != null) {
                        _1.addAttribute("", "CHILD", pController.getAttrQName(this, "", "CHILD"), "CDATA", _2.getCHILD());
                    }
                    return _1;
                }

                public java.lang.String getPreferredPrefix(java.lang.String pURI) {
                    return null;
                }

                public void marshalChilds(org.apache.ws.jaxme.impl.JMSAXDriverController pController, org.xml.sax.ContentHandler pHandler, java.lang.Object pObject) throws org.xml.sax.SAXException {
                }
            }

            public org.xml.sax.helpers.AttributesImpl getAttributes(org.apache.ws.jaxme.impl.JMSAXDriverController pController, java.lang.Object pObject) throws org.xml.sax.SAXException {
                org.xml.sax.helpers.AttributesImpl _1 = new org.xml.sax.helpers.AttributesImpl();
                return _1;
            }

            public java.lang.String getPreferredPrefix(java.lang.String pURI) {
                if (pURI == null) {
                    pURI = "";
                }
                if (pURI.equals("http://localhost/xml/model.xsd")) {
                    return "mstns";
                }
                return null;
            }

            public void marshalChilds(org.apache.ws.jaxme.impl.JMSAXDriverController pController, org.xml.sax.ContentHandler pHandler, java.lang.Object pObject) throws org.xml.sax.SAXException {
                unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.STRUCTUREType _1 = (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.STRUCTUREType) pObject;
                java.util.List _2 = _1.getARC();
                for (int _3 = 0; _3 < (_2).size(); _3++) {
                    org.apache.ws.jaxme.impl.JMSAXDriver _4 = new unbbayes.io.xmlbif.version4.xmlclasses.impl.BIFTypeDriver.NETWORKTypeDriver.STRUCTURETypeDriver.ARCTypeDriver();
                    pController.marshal(_4, "http://localhost/xml/model.xsd", "ARC", (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.STRUCTUREType.ARCType) _2.get(_3));
                }
            }
        }

        public static class POTENTIALTypeDriver implements org.apache.ws.jaxme.impl.JMSAXDriver {

            public static class POTTypeDriver implements org.apache.ws.jaxme.impl.JMSAXDriver {

                public static class PRIVATETypeDriver implements org.apache.ws.jaxme.impl.JMSAXDriver {

                    public org.xml.sax.helpers.AttributesImpl getAttributes(org.apache.ws.jaxme.impl.JMSAXDriverController pController, java.lang.Object pObject) throws org.xml.sax.SAXException {
                        org.xml.sax.helpers.AttributesImpl _1 = new org.xml.sax.helpers.AttributesImpl();
                        unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.POTENTIALType.POTType.PRIVATEType _2 = (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.POTENTIALType.POTType.PRIVATEType) pObject;
                        java.lang.String _3 = _2.getNAME();
                        if (_3 != null) {
                            _1.addAttribute("", "NAME", pController.getAttrQName(this, "", "NAME"), "CDATA", _2.getNAME());
                        }
                        return _1;
                    }

                    public java.lang.String getPreferredPrefix(java.lang.String pURI) {
                        return null;
                    }

                    public void marshalChilds(org.apache.ws.jaxme.impl.JMSAXDriverController pController, org.xml.sax.ContentHandler pHandler, java.lang.Object pObject) throws org.xml.sax.SAXException {
                    }
                }

                public static class CONDSETTypeDriver implements org.apache.ws.jaxme.impl.JMSAXDriver {

                    public static class CONDLEMTypeDriver implements org.apache.ws.jaxme.impl.JMSAXDriver {

                        public org.xml.sax.helpers.AttributesImpl getAttributes(org.apache.ws.jaxme.impl.JMSAXDriverController pController, java.lang.Object pObject) throws org.xml.sax.SAXException {
                            org.xml.sax.helpers.AttributesImpl _1 = new org.xml.sax.helpers.AttributesImpl();
                            unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.POTENTIALType.POTType.CONDSETType.CONDLEMType _2 = (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.POTENTIALType.POTType.CONDSETType.CONDLEMType) pObject;
                            java.lang.String _3 = _2.getNAME();
                            if (_3 != null) {
                                _1.addAttribute("", "NAME", pController.getAttrQName(this, "", "NAME"), "CDATA", _2.getNAME());
                            }
                            return _1;
                        }

                        public java.lang.String getPreferredPrefix(java.lang.String pURI) {
                            return null;
                        }

                        public void marshalChilds(org.apache.ws.jaxme.impl.JMSAXDriverController pController, org.xml.sax.ContentHandler pHandler, java.lang.Object pObject) throws org.xml.sax.SAXException {
                        }
                    }

                    public org.xml.sax.helpers.AttributesImpl getAttributes(org.apache.ws.jaxme.impl.JMSAXDriverController pController, java.lang.Object pObject) throws org.xml.sax.SAXException {
                        org.xml.sax.helpers.AttributesImpl _1 = new org.xml.sax.helpers.AttributesImpl();
                        return _1;
                    }

                    public java.lang.String getPreferredPrefix(java.lang.String pURI) {
                        if (pURI == null) {
                            pURI = "";
                        }
                        if (pURI.equals("http://localhost/xml/model.xsd")) {
                            return "mstns";
                        }
                        return null;
                    }

                    public void marshalChilds(org.apache.ws.jaxme.impl.JMSAXDriverController pController, org.xml.sax.ContentHandler pHandler, java.lang.Object pObject) throws org.xml.sax.SAXException {
                        unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.POTENTIALType.POTType.CONDSETType _1 = (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.POTENTIALType.POTType.CONDSETType) pObject;
                        java.util.List _2 = _1.getCONDLEM();
                        for (int _3 = 0; _3 < (_2).size(); _3++) {
                            org.apache.ws.jaxme.impl.JMSAXDriver _4 = new unbbayes.io.xmlbif.version4.xmlclasses.impl.BIFTypeDriver.NETWORKTypeDriver.POTENTIALTypeDriver.POTTypeDriver.CONDSETTypeDriver.CONDLEMTypeDriver();
                            pController.marshal(_4, "http://localhost/xml/model.xsd", "CONDLEM", (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.POTENTIALType.POTType.CONDSETType.CONDLEMType) _2.get(_3));
                        }
                    }
                }

                public static class DPISTypeDriver implements org.apache.ws.jaxme.impl.JMSAXDriver {

                    public static class DPITypeDriver implements org.apache.ws.jaxme.impl.JMSAXDriver {

                        public org.xml.sax.helpers.AttributesImpl getAttributes(org.apache.ws.jaxme.impl.JMSAXDriverController pController, java.lang.Object pObject) throws org.xml.sax.SAXException {
                            org.xml.sax.helpers.AttributesImpl _1 = new org.xml.sax.helpers.AttributesImpl();
                            unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.POTENTIALType.POTType.DPISType.DPIType _2 = (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.POTENTIALType.POTType.DPISType.DPIType) pObject;
                            _1.addAttribute("", "INDEXES", pController.getAttrQName(this, "", "INDEXES"), "CDATA", pController.getDatatypeConverter().printInt(_2.getINDEXES()));
                            return _1;
                        }

                        public java.lang.String getPreferredPrefix(java.lang.String pURI) {
                            return null;
                        }

                        public void marshalChilds(org.apache.ws.jaxme.impl.JMSAXDriverController pController, org.xml.sax.ContentHandler pHandler, java.lang.Object pObject) throws org.xml.sax.SAXException {
                            unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.POTENTIALType.POTType.DPISType.DPIType _1 = (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.POTENTIALType.POTType.DPISType.DPIType) pObject;
                            java.lang.String _2 = _1.getValue();
                            if (_2 != null && _2.length() > 0) {
                                char[] _3 = _2.toCharArray();
                                pHandler.characters(_3, 0, _3.length);
                            }
                        }
                    }

                    public org.xml.sax.helpers.AttributesImpl getAttributes(org.apache.ws.jaxme.impl.JMSAXDriverController pController, java.lang.Object pObject) throws org.xml.sax.SAXException {
                        org.xml.sax.helpers.AttributesImpl _1 = new org.xml.sax.helpers.AttributesImpl();
                        return _1;
                    }

                    public java.lang.String getPreferredPrefix(java.lang.String pURI) {
                        if (pURI == null) {
                            pURI = "";
                        }
                        if (pURI.equals("http://localhost/xml/model.xsd")) {
                            return "mstns";
                        }
                        return null;
                    }

                    public void marshalChilds(org.apache.ws.jaxme.impl.JMSAXDriverController pController, org.xml.sax.ContentHandler pHandler, java.lang.Object pObject) throws org.xml.sax.SAXException {
                        unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.POTENTIALType.POTType.DPISType _1 = (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.POTENTIALType.POTType.DPISType) pObject;
                        java.util.List _2 = _1.getDPI();
                        for (int _3 = 0; _3 < (_2).size(); _3++) {
                            org.apache.ws.jaxme.impl.JMSAXDriver _4 = new unbbayes.io.xmlbif.version4.xmlclasses.impl.BIFTypeDriver.NETWORKTypeDriver.POTENTIALTypeDriver.POTTypeDriver.DPISTypeDriver.DPITypeDriver();
                            pController.marshal(_4, "http://localhost/xml/model.xsd", "DPI", (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.POTENTIALType.POTType.DPISType.DPIType) _2.get(_3));
                        }
                    }
                }

                public org.xml.sax.helpers.AttributesImpl getAttributes(org.apache.ws.jaxme.impl.JMSAXDriverController pController, java.lang.Object pObject) throws org.xml.sax.SAXException {
                    org.xml.sax.helpers.AttributesImpl _1 = new org.xml.sax.helpers.AttributesImpl();
                    unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.POTENTIALType.POTType _2 = (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.POTENTIALType.POTType) pObject;
                    java.lang.String _3 = _2.getTYPE();
                    if (_3 != null) {
                        _1.addAttribute("", "TYPE", pController.getAttrQName(this, "", "TYPE"), "CDATA", _2.getTYPE());
                    }
                    return _1;
                }

                public java.lang.String getPreferredPrefix(java.lang.String pURI) {
                    if (pURI == null) {
                        pURI = "";
                    }
                    if (pURI.equals("http://localhost/xml/model.xsd")) {
                        return "mstns";
                    }
                    return null;
                }

                public void marshalChilds(org.apache.ws.jaxme.impl.JMSAXDriverController pController, org.xml.sax.ContentHandler pHandler, java.lang.Object pObject) throws org.xml.sax.SAXException {
                    unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.POTENTIALType.POTType _1 = (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.POTENTIALType.POTType) pObject;
                    java.util.List _2 = _1.getPRIVATE();
                    for (int _3 = 0; _3 < (_2).size(); _3++) {
                        org.apache.ws.jaxme.impl.JMSAXDriver _4 = new unbbayes.io.xmlbif.version4.xmlclasses.impl.BIFTypeDriver.NETWORKTypeDriver.POTENTIALTypeDriver.POTTypeDriver.PRIVATETypeDriver();
                        pController.marshal(_4, "http://localhost/xml/model.xsd", "PRIVATE", (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.POTENTIALType.POTType.PRIVATEType) _2.get(_3));
                    }
                    java.util.List _5 = _1.getCONDSET();
                    for (int _6 = 0; _6 < (_5).size(); _6++) {
                        org.apache.ws.jaxme.impl.JMSAXDriver _7 = new unbbayes.io.xmlbif.version4.xmlclasses.impl.BIFTypeDriver.NETWORKTypeDriver.POTENTIALTypeDriver.POTTypeDriver.CONDSETTypeDriver();
                        pController.marshal(_7, "http://localhost/xml/model.xsd", "CONDSET", (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.POTENTIALType.POTType.CONDSETType) _5.get(_6));
                    }
                    java.util.List _8 = _1.getDPIS();
                    for (int _9 = 0; _9 < (_8).size(); _9++) {
                        org.apache.ws.jaxme.impl.JMSAXDriver _10 = new unbbayes.io.xmlbif.version4.xmlclasses.impl.BIFTypeDriver.NETWORKTypeDriver.POTENTIALTypeDriver.POTTypeDriver.DPISTypeDriver();
                        pController.marshal(_10, "http://localhost/xml/model.xsd", "DPIS", (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.POTENTIALType.POTType.DPISType) _8.get(_9));
                    }
                }
            }

            public org.xml.sax.helpers.AttributesImpl getAttributes(org.apache.ws.jaxme.impl.JMSAXDriverController pController, java.lang.Object pObject) throws org.xml.sax.SAXException {
                org.xml.sax.helpers.AttributesImpl _1 = new org.xml.sax.helpers.AttributesImpl();
                return _1;
            }

            public java.lang.String getPreferredPrefix(java.lang.String pURI) {
                if (pURI == null) {
                    pURI = "";
                }
                if (pURI.equals("http://localhost/xml/model.xsd")) {
                    return "mstns";
                }
                return null;
            }

            public void marshalChilds(org.apache.ws.jaxme.impl.JMSAXDriverController pController, org.xml.sax.ContentHandler pHandler, java.lang.Object pObject) throws org.xml.sax.SAXException {
                unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.POTENTIALType _1 = (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.POTENTIALType) pObject;
                java.util.List _2 = _1.getPOT();
                for (int _3 = 0; _3 < (_2).size(); _3++) {
                    org.apache.ws.jaxme.impl.JMSAXDriver _4 = new unbbayes.io.xmlbif.version4.xmlclasses.impl.BIFTypeDriver.NETWORKTypeDriver.POTENTIALTypeDriver.POTTypeDriver();
                    pController.marshal(_4, "http://localhost/xml/model.xsd", "POT", (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.POTENTIALType.POTType) _2.get(_3));
                }
            }
        }

        public org.xml.sax.helpers.AttributesImpl getAttributes(org.apache.ws.jaxme.impl.JMSAXDriverController pController, java.lang.Object pObject) throws org.xml.sax.SAXException {
            org.xml.sax.helpers.AttributesImpl _1 = new org.xml.sax.helpers.AttributesImpl();
            return _1;
        }

        public java.lang.String getPreferredPrefix(java.lang.String pURI) {
            if (pURI == null) {
                pURI = "";
            }
            if (pURI.equals("http://localhost/xml/model.xsd")) {
                return "mstns";
            }
            return null;
        }

        public void marshalChilds(org.apache.ws.jaxme.impl.JMSAXDriverController pController, org.xml.sax.ContentHandler pHandler, java.lang.Object pObject) throws org.xml.sax.SAXException {
            unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType _1 = (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType) pObject;
            java.util.List _2 = _1.getVARIABLES();
            for (int _3 = 0; _3 < (_2).size(); _3++) {
                org.apache.ws.jaxme.impl.JMSAXDriver _4 = new unbbayes.io.xmlbif.version4.xmlclasses.impl.BIFTypeDriver.NETWORKTypeDriver.VARIABLESTypeDriver();
                pController.marshal(_4, "http://localhost/xml/model.xsd", "VARIABLES", (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.VARIABLESType) _2.get(_3));
            }
            java.util.List _5 = _1.getSTRUCTURE();
            for (int _6 = 0; _6 < (_5).size(); _6++) {
                org.apache.ws.jaxme.impl.JMSAXDriver _7 = new unbbayes.io.xmlbif.version4.xmlclasses.impl.BIFTypeDriver.NETWORKTypeDriver.STRUCTURETypeDriver();
                pController.marshal(_7, "http://localhost/xml/model.xsd", "STRUCTURE", (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.STRUCTUREType) _5.get(_6));
            }
            java.util.List _8 = _1.getPOTENTIAL();
            for (int _9 = 0; _9 < (_8).size(); _9++) {
                org.apache.ws.jaxme.impl.JMSAXDriver _10 = new unbbayes.io.xmlbif.version4.xmlclasses.impl.BIFTypeDriver.NETWORKTypeDriver.POTENTIALTypeDriver();
                pController.marshal(_10, "http://localhost/xml/model.xsd", "POTENTIAL", (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType.POTENTIALType) _8.get(_9));
            }
        }
    }

    public org.xml.sax.helpers.AttributesImpl getAttributes(org.apache.ws.jaxme.impl.JMSAXDriverController pController, java.lang.Object pObject) throws org.xml.sax.SAXException {
        org.xml.sax.helpers.AttributesImpl _1 = new org.xml.sax.helpers.AttributesImpl();
        unbbayes.io.xmlbif.version4.xmlclasses.BIFType _2 = (unbbayes.io.xmlbif.version4.xmlclasses.BIFType) pObject;
        _1.addAttribute("", "VERSION", pController.getAttrQName(this, "", "VERSION"), "CDATA", pController.getDatatypeConverter().printInt(_2.getVERSION()));
        return _1;
    }

    public java.lang.String getPreferredPrefix(java.lang.String pURI) {
        if (pURI == null) {
            pURI = "";
        }
        if (pURI.equals("http://localhost/xml/model.xsd")) {
            return "mstns";
        }
        return null;
    }

    public void marshalChilds(org.apache.ws.jaxme.impl.JMSAXDriverController pController, org.xml.sax.ContentHandler pHandler, java.lang.Object pObject) throws org.xml.sax.SAXException {
        unbbayes.io.xmlbif.version4.xmlclasses.BIFType _1 = (unbbayes.io.xmlbif.version4.xmlclasses.BIFType) pObject;
        java.util.List _2 = _1.getHEADER();
        for (int _3 = 0; _3 < (_2).size(); _3++) {
            org.apache.ws.jaxme.impl.JMSAXDriver _4 = new unbbayes.io.xmlbif.version4.xmlclasses.impl.BIFTypeDriver.HEADERTypeDriver();
            pController.marshal(_4, "http://localhost/xml/model.xsd", "HEADER", (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.HEADERType) _2.get(_3));
        }
        java.util.List _5 = _1.getSTATICPROPERTY();
        for (int _6 = 0; _6 < (_5).size(); _6++) {
            org.apache.ws.jaxme.impl.JMSAXDriver _7 = new unbbayes.io.xmlbif.version4.xmlclasses.impl.BIFTypeDriver.STATICPROPERTYTypeDriver();
            pController.marshal(_7, "http://localhost/xml/model.xsd", "STATICPROPERTY", (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.STATICPROPERTYType) _5.get(_6));
        }
        java.util.List _8 = _1.getHIERARCHY();
        for (int _9 = 0; _9 < (_8).size(); _9++) {
            org.apache.ws.jaxme.impl.JMSAXDriver _10 = new unbbayes.io.xmlbif.version4.xmlclasses.impl.BIFTypeDriver.HIERARCHYTypeDriver();
            pController.marshal(_10, "http://localhost/xml/model.xsd", "HIERARCHY", (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.HIERARCHYType) _8.get(_9));
        }
        java.util.List _11 = _1.getNETWORK();
        for (int _12 = 0; _12 < (_11).size(); _12++) {
            org.apache.ws.jaxme.impl.JMSAXDriver _13 = new unbbayes.io.xmlbif.version4.xmlclasses.impl.BIFTypeDriver.NETWORKTypeDriver();
            pController.marshal(_13, "http://localhost/xml/model.xsd", "NETWORK", (unbbayes.io.xmlbif.version4.xmlclasses.BIFType.NETWORKType) _11.get(_12));
        }
    }
}
