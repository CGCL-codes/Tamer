package org.ulpgc.ws.sessioninternal;

import com.sun.xml.rpc.server.http.MessageContextProperties;
import com.sun.xml.rpc.streaming.*;
import com.sun.xml.rpc.encoding.*;
import com.sun.xml.rpc.encoding.soap.SOAPConstants;
import com.sun.xml.rpc.encoding.soap.SOAP12Constants;
import com.sun.xml.rpc.encoding.literal.*;
import com.sun.xml.rpc.soap.streaming.*;
import com.sun.xml.rpc.soap.message.*;
import com.sun.xml.rpc.soap.SOAPVersion;
import com.sun.xml.rpc.soap.SOAPEncodingConstants;
import com.sun.xml.rpc.wsdl.document.schema.SchemaConstants;
import javax.xml.namespace.QName;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.lang.reflect.*;
import java.lang.Class;
import com.sun.xml.rpc.client.SenderException;
import com.sun.xml.rpc.client.*;
import com.sun.xml.rpc.client.http.*;
import javax.xml.rpc.handler.*;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.soap.SOAPFaultException;

public class Sesion_WebServicePortType_Stub extends com.sun.xml.rpc.client.StubBase implements org.ulpgc.ws.sessioninternal.Sesion_WebServicePortType {

    public Sesion_WebServicePortType_Stub(HandlerChain handlerChain) {
        super(handlerChain);
        _setProperty(ENDPOINT_ADDRESS_PROPERTY, "http://www.ulpgc.es/miulpgc/autorizacion/acceso.php");
    }

    public java.lang.String comprobar_validez_sesion(java.lang.String identificador_sesion, java.lang.String dni_usuario) throws java.rmi.RemoteException {
        try {
            StreamingSenderState _state = _start(_handlerChain);
            InternalSOAPMessage _request = _state.getRequest();
            _request.setOperationCode(comprobar_validez_sesion_OPCODE);
            org.ulpgc.ws.sessioninternal.Sesion_WebServicePortType_comprobar_validez_sesion_RequestStruct _mySesion_WebServicePortType_comprobar_validez_sesion_RequestStruct = new org.ulpgc.ws.sessioninternal.Sesion_WebServicePortType_comprobar_validez_sesion_RequestStruct();
            _mySesion_WebServicePortType_comprobar_validez_sesion_RequestStruct.setIdentificador_sesion(identificador_sesion);
            _mySesion_WebServicePortType_comprobar_validez_sesion_RequestStruct.setDni_usuario(dni_usuario);
            SOAPBlockInfo _bodyBlock = new SOAPBlockInfo(ns1_comprobar_validez_sesion_comprobar_validez_sesion_QNAME);
            _bodyBlock.setValue(_mySesion_WebServicePortType_comprobar_validez_sesion_RequestStruct);
            _bodyBlock.setSerializer(ns1_mySesion_WebServicePortType_comprobar_validez_sesion_RequestStruct_SOAPSerializer);
            _request.setBody(_bodyBlock);
            _state.getMessageContext().setProperty(HttpClientTransport.HTTP_SOAPACTION_PROPERTY, "urn:Sesion_WebService#comprobar_validez_sesion");
            _send((java.lang.String) _getProperty(ENDPOINT_ADDRESS_PROPERTY), _state);
            org.ulpgc.ws.sessioninternal.Sesion_WebServicePortType_comprobar_validez_sesion_ResponseStruct _mySesion_WebServicePortType_comprobar_validez_sesion_ResponseStruct = null;
            Object _responseObj = _state.getResponse().getBody().getValue();
            if (_responseObj instanceof SOAPDeserializationState) {
                _mySesion_WebServicePortType_comprobar_validez_sesion_ResponseStruct = (org.ulpgc.ws.sessioninternal.Sesion_WebServicePortType_comprobar_validez_sesion_ResponseStruct) ((SOAPDeserializationState) _responseObj).getInstance();
            } else {
                _mySesion_WebServicePortType_comprobar_validez_sesion_ResponseStruct = (org.ulpgc.ws.sessioninternal.Sesion_WebServicePortType_comprobar_validez_sesion_ResponseStruct) _responseObj;
            }
            return _mySesion_WebServicePortType_comprobar_validez_sesion_ResponseStruct.get_return();
        } catch (RemoteException e) {
            throw e;
        } catch (JAXRPCException e) {
            throw new RemoteException(e.getMessage(), e);
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            } else {
                throw new RemoteException(e.getMessage(), e);
            }
        }
    }

    protected void _readFirstBodyElement(XMLReader bodyReader, SOAPDeserializationContext deserializationContext, StreamingSenderState state) throws Exception {
        int opcode = state.getRequest().getOperationCode();
        switch(opcode) {
            case comprobar_validez_sesion_OPCODE:
                _deserialize_comprobar_validez_sesion(bodyReader, deserializationContext, state);
                break;
            default:
                throw new SenderException("sender.response.unrecognizedOperation", java.lang.Integer.toString(opcode));
        }
    }

    private void _deserialize_comprobar_validez_sesion(XMLReader bodyReader, SOAPDeserializationContext deserializationContext, StreamingSenderState state) throws Exception {
        java.lang.Object mySesion_WebServicePortType_comprobar_validez_sesion_ResponseStructObj = ns1_mySesion_WebServicePortType_comprobar_validez_sesion_ResponseStruct_SOAPSerializer.deserialize(ns1_comprobar_validez_sesion_comprobar_validez_sesionResponse_QNAME, bodyReader, deserializationContext);
        SOAPBlockInfo bodyBlock = new SOAPBlockInfo(ns1_comprobar_validez_sesion_comprobar_validez_sesionResponse_QNAME);
        bodyBlock.setValue(mySesion_WebServicePortType_comprobar_validez_sesion_ResponseStructObj);
        state.getResponse().setBody(bodyBlock);
    }

    public java.lang.String _getDefaultEnvelopeEncodingStyle() {
        return SOAPNamespaceConstants.ENCODING;
    }

    public java.lang.String _getImplicitEnvelopeEncodingStyle() {
        return "";
    }

    public java.lang.String _getEncodingStyle() {
        return SOAPNamespaceConstants.ENCODING;
    }

    public void _setEncodingStyle(java.lang.String encodingStyle) {
        throw new UnsupportedOperationException("cannot set encoding style");
    }

    protected java.lang.String[] _getNamespaceDeclarations() {
        return myNamespace_declarations;
    }

    public javax.xml.namespace.QName[] _getUnderstoodHeaders() {
        return understoodHeaderNames;
    }

    public void _initialize(InternalTypeMappingRegistry registry) throws Exception {
        super._initialize(registry);
        ns1_mySesion_WebServicePortType_comprobar_validez_sesion_ResponseStruct_SOAPSerializer = (CombinedSerializer) registry.getSerializer(SOAPConstants.NS_SOAP_ENCODING, org.ulpgc.ws.sessioninternal.Sesion_WebServicePortType_comprobar_validez_sesion_ResponseStruct.class, ns1_comprobar_validez_sesionResponse_TYPE_QNAME);
        ns1_mySesion_WebServicePortType_comprobar_validez_sesion_RequestStruct_SOAPSerializer = (CombinedSerializer) registry.getSerializer(SOAPConstants.NS_SOAP_ENCODING, org.ulpgc.ws.sessioninternal.Sesion_WebServicePortType_comprobar_validez_sesion_RequestStruct.class, ns1_comprobar_validez_sesion_TYPE_QNAME);
    }

    private static final javax.xml.namespace.QName _portName = new QName("urn:Sesion_WebService", "Sesion_WebServicePort");

    private static final int comprobar_validez_sesion_OPCODE = 0;

    private static final javax.xml.namespace.QName ns1_comprobar_validez_sesion_comprobar_validez_sesion_QNAME = new QName("urn:Sesion_WebService", "comprobar_validez_sesion");

    private static final javax.xml.namespace.QName ns1_comprobar_validez_sesion_TYPE_QNAME = new QName("urn:Sesion_WebService", "comprobar_validez_sesion");

    private CombinedSerializer ns1_mySesion_WebServicePortType_comprobar_validez_sesion_RequestStruct_SOAPSerializer;

    private static final javax.xml.namespace.QName ns1_comprobar_validez_sesion_comprobar_validez_sesionResponse_QNAME = new QName("urn:Sesion_WebService", "comprobar_validez_sesionResponse");

    private static final javax.xml.namespace.QName ns1_comprobar_validez_sesionResponse_TYPE_QNAME = new QName("urn:Sesion_WebService", "comprobar_validez_sesionResponse");

    private CombinedSerializer ns1_mySesion_WebServicePortType_comprobar_validez_sesion_ResponseStruct_SOAPSerializer;

    private static final java.lang.String[] myNamespace_declarations = new java.lang.String[] { "ns0", "urn:Sesion_WebService" };

    private static final QName[] understoodHeaderNames = new QName[] {};
}
