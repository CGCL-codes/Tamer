package edu.indiana.extreme.www.xgws.msgbox._2004;

/**
 * MsgBoxServiceMessageReceiverInOut message receiver
 */
public class MsgBoxServiceMessageReceiverInOut extends org.apache.axis2.receivers.AbstractInOutMessageReceiver {

    public void invokeBusinessLogic(org.apache.axis2.context.MessageContext msgContext, org.apache.axis2.context.MessageContext newMsgContext) throws org.apache.axis2.AxisFault {
        try {
            Object obj = getTheImplementationObject(msgContext);
            MsgBoxServiceSkeleton skel = (MsgBoxServiceSkeleton) obj;
            org.apache.axiom.soap.SOAPEnvelope envelope = null;
            org.apache.axis2.description.AxisOperation op = msgContext.getOperationContext().getAxisOperation();
            if (op == null) {
                throw new org.apache.axis2.AxisFault("Operation is not located, if this is doclit style the SOAP-ACTION should specified via the SOAP Action to use the RawXMLProvider");
            }
            java.lang.String methodName;
            if ((op.getName() != null) && ((methodName = org.apache.axis2.util.JavaUtils.xmlNameToJava(op.getName().getLocalPart())) != null)) {
                if ("storeMessages".equals(methodName)) {
                    edu.indiana.extreme.www.xgws.msgbox._2004.StoreMessagesResponse storeMessagesResponse1 = null;
                    edu.indiana.extreme.www.xgws.msgbox._2004.StoreMessages wrappedParam = (edu.indiana.extreme.www.xgws.msgbox._2004.StoreMessages) fromOM(msgContext.getEnvelope().getBody().getFirstElement(), edu.indiana.extreme.www.xgws.msgbox._2004.StoreMessages.class, getEnvelopeNamespaces(msgContext.getEnvelope()));
                    storeMessagesResponse1 = skel.storeMessages(wrappedParam);
                    envelope = toEnvelope(getSOAPFactory(msgContext), storeMessagesResponse1, false);
                } else if ("destroyMsgBox".equals(methodName)) {
                    edu.indiana.extreme.www.xgws.msgbox._2004.DestroyMsgBoxResponse destroyMsgBoxResponse3 = null;
                    edu.indiana.extreme.www.xgws.msgbox._2004.DestroyMsgBox wrappedParam = (edu.indiana.extreme.www.xgws.msgbox._2004.DestroyMsgBox) fromOM(msgContext.getEnvelope().getBody().getFirstElement(), edu.indiana.extreme.www.xgws.msgbox._2004.DestroyMsgBox.class, getEnvelopeNamespaces(msgContext.getEnvelope()));
                    destroyMsgBoxResponse3 = skel.destroyMsgBox(wrappedParam);
                    envelope = toEnvelope(getSOAPFactory(msgContext), destroyMsgBoxResponse3, false);
                } else if ("takeMessages".equals(methodName)) {
                    edu.indiana.extreme.www.xgws.msgbox._2004.TakeMessagesResponse takeMessagesResponse5 = null;
                    edu.indiana.extreme.www.xgws.msgbox._2004.TakeMessages wrappedParam = (edu.indiana.extreme.www.xgws.msgbox._2004.TakeMessages) fromOM(msgContext.getEnvelope().getBody().getFirstElement(), edu.indiana.extreme.www.xgws.msgbox._2004.TakeMessages.class, getEnvelopeNamespaces(msgContext.getEnvelope()));
                    takeMessagesResponse5 = skel.takeMessages(wrappedParam);
                    envelope = toEnvelope(getSOAPFactory(msgContext), takeMessagesResponse5, false);
                } else if ("createMsgBox".equals(methodName)) {
                    edu.indiana.extreme.www.xgws.msgbox._2004.CreateMsgBoxResponse createMsgBoxResponse7 = null;
                    edu.indiana.extreme.www.xgws.msgbox._2004.CreateMsgBox wrappedParam = (edu.indiana.extreme.www.xgws.msgbox._2004.CreateMsgBox) fromOM(msgContext.getEnvelope().getBody().getFirstElement(), edu.indiana.extreme.www.xgws.msgbox._2004.CreateMsgBox.class, getEnvelopeNamespaces(msgContext.getEnvelope()));
                    createMsgBoxResponse7 = skel.createMsgBox(wrappedParam);
                    envelope = toEnvelope(getSOAPFactory(msgContext), createMsgBoxResponse7, false);
                } else {
                    throw new java.lang.RuntimeException("method not found");
                }
                newMsgContext.setEnvelope(envelope);
            }
        } catch (java.lang.Exception e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.om.OMElement toOM(edu.indiana.extreme.www.xgws.msgbox._2004.StoreMessages param, boolean optimizeContent) throws org.apache.axis2.AxisFault {
        try {
            return param.getOMElement(edu.indiana.extreme.www.xgws.msgbox._2004.StoreMessages.MY_QNAME, org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.om.OMElement toOM(edu.indiana.extreme.www.xgws.msgbox._2004.StoreMessagesResponse param, boolean optimizeContent) throws org.apache.axis2.AxisFault {
        try {
            return param.getOMElement(edu.indiana.extreme.www.xgws.msgbox._2004.StoreMessagesResponse.MY_QNAME, org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.om.OMElement toOM(edu.indiana.extreme.www.xgws.msgbox._2004.DestroyMsgBox param, boolean optimizeContent) throws org.apache.axis2.AxisFault {
        try {
            return param.getOMElement(edu.indiana.extreme.www.xgws.msgbox._2004.DestroyMsgBox.MY_QNAME, org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.om.OMElement toOM(edu.indiana.extreme.www.xgws.msgbox._2004.DestroyMsgBoxResponse param, boolean optimizeContent) throws org.apache.axis2.AxisFault {
        try {
            return param.getOMElement(edu.indiana.extreme.www.xgws.msgbox._2004.DestroyMsgBoxResponse.MY_QNAME, org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.om.OMElement toOM(edu.indiana.extreme.www.xgws.msgbox._2004.TakeMessages param, boolean optimizeContent) throws org.apache.axis2.AxisFault {
        try {
            return param.getOMElement(edu.indiana.extreme.www.xgws.msgbox._2004.TakeMessages.MY_QNAME, org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.om.OMElement toOM(edu.indiana.extreme.www.xgws.msgbox._2004.TakeMessagesResponse param, boolean optimizeContent) throws org.apache.axis2.AxisFault {
        try {
            return param.getOMElement(edu.indiana.extreme.www.xgws.msgbox._2004.TakeMessagesResponse.MY_QNAME, org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.om.OMElement toOM(edu.indiana.extreme.www.xgws.msgbox._2004.CreateMsgBox param, boolean optimizeContent) throws org.apache.axis2.AxisFault {
        try {
            return param.getOMElement(edu.indiana.extreme.www.xgws.msgbox._2004.CreateMsgBox.MY_QNAME, org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.om.OMElement toOM(edu.indiana.extreme.www.xgws.msgbox._2004.CreateMsgBoxResponse param, boolean optimizeContent) throws org.apache.axis2.AxisFault {
        try {
            return param.getOMElement(edu.indiana.extreme.www.xgws.msgbox._2004.CreateMsgBoxResponse.MY_QNAME, org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, edu.indiana.extreme.www.xgws.msgbox._2004.StoreMessagesResponse param, boolean optimizeContent) throws org.apache.axis2.AxisFault {
        try {
            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(edu.indiana.extreme.www.xgws.msgbox._2004.StoreMessagesResponse.MY_QNAME, factory));
            return emptyEnvelope;
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private edu.indiana.extreme.www.xgws.msgbox._2004.StoreMessagesResponse wrapstoreMessages() {
        edu.indiana.extreme.www.xgws.msgbox._2004.StoreMessagesResponse wrappedElement = new edu.indiana.extreme.www.xgws.msgbox._2004.StoreMessagesResponse();
        return wrappedElement;
    }

    private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, edu.indiana.extreme.www.xgws.msgbox._2004.DestroyMsgBoxResponse param, boolean optimizeContent) throws org.apache.axis2.AxisFault {
        try {
            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(edu.indiana.extreme.www.xgws.msgbox._2004.DestroyMsgBoxResponse.MY_QNAME, factory));
            return emptyEnvelope;
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private edu.indiana.extreme.www.xgws.msgbox._2004.DestroyMsgBoxResponse wrapdestroyMsgBox() {
        edu.indiana.extreme.www.xgws.msgbox._2004.DestroyMsgBoxResponse wrappedElement = new edu.indiana.extreme.www.xgws.msgbox._2004.DestroyMsgBoxResponse();
        return wrappedElement;
    }

    private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, edu.indiana.extreme.www.xgws.msgbox._2004.TakeMessagesResponse param, boolean optimizeContent) throws org.apache.axis2.AxisFault {
        try {
            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(edu.indiana.extreme.www.xgws.msgbox._2004.TakeMessagesResponse.MY_QNAME, factory));
            return emptyEnvelope;
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private edu.indiana.extreme.www.xgws.msgbox._2004.TakeMessagesResponse wraptakeMessages() {
        edu.indiana.extreme.www.xgws.msgbox._2004.TakeMessagesResponse wrappedElement = new edu.indiana.extreme.www.xgws.msgbox._2004.TakeMessagesResponse();
        return wrappedElement;
    }

    private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, edu.indiana.extreme.www.xgws.msgbox._2004.CreateMsgBoxResponse param, boolean optimizeContent) throws org.apache.axis2.AxisFault {
        try {
            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(edu.indiana.extreme.www.xgws.msgbox._2004.CreateMsgBoxResponse.MY_QNAME, factory));
            return emptyEnvelope;
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private edu.indiana.extreme.www.xgws.msgbox._2004.CreateMsgBoxResponse wrapcreateMsgBox() {
        edu.indiana.extreme.www.xgws.msgbox._2004.CreateMsgBoxResponse wrappedElement = new edu.indiana.extreme.www.xgws.msgbox._2004.CreateMsgBoxResponse();
        return wrappedElement;
    }

    /**
     * get the default envelope
     */
    private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory) {
        return factory.getDefaultEnvelope();
    }

    private java.lang.Object fromOM(org.apache.axiom.om.OMElement param, java.lang.Class type, java.util.Map extraNamespaces) throws org.apache.axis2.AxisFault {
        try {
            if (edu.indiana.extreme.www.xgws.msgbox._2004.StoreMessages.class.equals(type)) {
                return edu.indiana.extreme.www.xgws.msgbox._2004.StoreMessages.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }
            if (edu.indiana.extreme.www.xgws.msgbox._2004.StoreMessagesResponse.class.equals(type)) {
                return edu.indiana.extreme.www.xgws.msgbox._2004.StoreMessagesResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }
            if (edu.indiana.extreme.www.xgws.msgbox._2004.DestroyMsgBox.class.equals(type)) {
                return edu.indiana.extreme.www.xgws.msgbox._2004.DestroyMsgBox.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }
            if (edu.indiana.extreme.www.xgws.msgbox._2004.DestroyMsgBoxResponse.class.equals(type)) {
                return edu.indiana.extreme.www.xgws.msgbox._2004.DestroyMsgBoxResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }
            if (edu.indiana.extreme.www.xgws.msgbox._2004.TakeMessages.class.equals(type)) {
                return edu.indiana.extreme.www.xgws.msgbox._2004.TakeMessages.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }
            if (edu.indiana.extreme.www.xgws.msgbox._2004.TakeMessagesResponse.class.equals(type)) {
                return edu.indiana.extreme.www.xgws.msgbox._2004.TakeMessagesResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }
            if (edu.indiana.extreme.www.xgws.msgbox._2004.CreateMsgBox.class.equals(type)) {
                return edu.indiana.extreme.www.xgws.msgbox._2004.CreateMsgBox.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }
            if (edu.indiana.extreme.www.xgws.msgbox._2004.CreateMsgBoxResponse.class.equals(type)) {
                return edu.indiana.extreme.www.xgws.msgbox._2004.CreateMsgBoxResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }
        } catch (java.lang.Exception e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
        return null;
    }

    /**
     * A utility method that copies the namepaces from the SOAPEnvelope
     */
    private java.util.Map getEnvelopeNamespaces(org.apache.axiom.soap.SOAPEnvelope env) {
        java.util.Map returnMap = new java.util.HashMap();
        java.util.Iterator namespaceIterator = env.getAllDeclaredNamespaces();
        while (namespaceIterator.hasNext()) {
            org.apache.axiom.om.OMNamespace ns = (org.apache.axiom.om.OMNamespace) namespaceIterator.next();
            returnMap.put(ns.getPrefix(), ns.getNamespaceURI());
        }
        return returnMap;
    }

    private org.apache.axis2.AxisFault createAxisFault(java.lang.Exception e) {
        org.apache.axis2.AxisFault f;
        Throwable cause = e.getCause();
        if (cause != null) {
            f = new org.apache.axis2.AxisFault(e.getMessage(), cause);
        } else {
            f = new org.apache.axis2.AxisFault(e.getMessage());
        }
        return f;
    }
}
