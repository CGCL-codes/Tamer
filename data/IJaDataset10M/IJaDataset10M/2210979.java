package calculadoraws;

import javax.xml.rpc.JAXRPCException;
import javax.xml.namespace.QName;
import javax.microedition.xml.rpc.Operation;
import javax.microedition.xml.rpc.Type;
import javax.microedition.xml.rpc.ComplexType;
import javax.microedition.xml.rpc.Element;

public class CalculadoraWSService_Stub implements CalculadoraWSService, javax.xml.rpc.Stub {

    private String[] _propertyNames;

    private Object[] _propertyValues;

    public CalculadoraWSService_Stub() {
        _propertyNames = new String[] { ENDPOINT_ADDRESS_PROPERTY };
        _propertyValues = new Object[] { "http://localhost:8084/J2ME_WebService/CalculadoraWS" };
    }

    public void _setProperty(String name, Object value) {
        int size = _propertyNames.length;
        for (int i = 0; i < size; ++i) {
            if (_propertyNames[i].equals(name)) {
                _propertyValues[i] = value;
                return;
            }
        }
        String[] newPropNames = new String[size + 1];
        System.arraycopy(_propertyNames, 0, newPropNames, 0, size);
        _propertyNames = newPropNames;
        Object[] newPropValues = new Object[size + 1];
        System.arraycopy(_propertyValues, 0, newPropValues, 0, size);
        _propertyValues = newPropValues;
        _propertyNames[size] = name;
        _propertyValues[size] = value;
    }

    public Object _getProperty(String name) {
        for (int i = 0; i < _propertyNames.length; ++i) {
            if (_propertyNames[i].equals(name)) {
                return _propertyValues[i];
            }
        }
        if (ENDPOINT_ADDRESS_PROPERTY.equals(name) || USERNAME_PROPERTY.equals(name) || PASSWORD_PROPERTY.equals(name)) {
            return null;
        }
        if (SESSION_MAINTAIN_PROPERTY.equals(name)) {
            return new Boolean(false);
        }
        throw new JAXRPCException("Stub does not recognize property: " + name);
    }

    protected void _prepOperation(Operation op) {
        for (int i = 0; i < _propertyNames.length; ++i) {
            op.setProperty(_propertyNames[i], _propertyValues[i].toString());
        }
    }

    public String Soma(String num1, String num2) throws java.rmi.RemoteException {
        Object inputObject[] = new Object[] { num1, num2 };
        Operation op = Operation.newInstance(_qname_operation_Soma, _type_Soma, _type_SomaResponse);
        _prepOperation(op);
        op.setProperty(Operation.SOAPACTION_URI_PROPERTY, "");
        Object resultObj;
        try {
            resultObj = op.invoke(inputObject);
        } catch (JAXRPCException e) {
            Throwable cause = e.getLinkedCause();
            if (cause instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) cause;
            }
            throw e;
        }
        return (String) ((Object[]) resultObj)[0];
    }

    protected static final QName _qname_operation_Soma = new QName("http://calculadora.webservice.ceuclar/", "Soma");

    protected static final QName _qname_SomaResponse = new QName("http://calculadora.webservice.ceuclar/", "SomaResponse");

    protected static final QName _qname_Soma = new QName("http://calculadora.webservice.ceuclar/", "Soma");

    protected static final Element _type_Soma;

    protected static final Element _type_SomaResponse;

    static {
        _type_SomaResponse = new Element(_qname_SomaResponse, _complexType(new Element[] { new Element(new QName("", "return"), Type.STRING, 0, 1, false) }), 1, 1, false);
        _type_Soma = new Element(_qname_Soma, _complexType(new Element[] { new Element(new QName("", "num1"), Type.STRING, 0, 1, false), new Element(new QName("", "num2"), Type.STRING, 0, 1, false) }), 1, 1, false);
    }

    private static ComplexType _complexType(Element[] elements) {
        ComplexType result = new ComplexType();
        result.elements = elements;
        return result;
    }
}
