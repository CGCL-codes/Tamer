package org.gridbus.broker.services.application.ws;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import javax.wsdl.Binding;
import javax.wsdl.Definition;
import javax.wsdl.Operation;
import javax.wsdl.Port;
import javax.wsdl.PortType;
import javax.wsdl.Service;
import javax.wsdl.WSDLException;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.namespace.QName;
import com.ibm.wsdl.ImportImpl;
import com.ibm.wsdl.factory.WSDLFactoryImpl;

/**
 * @author Xingchen Chu (xchu@csse.unimelb.edu.au)
 */
public abstract class WSExecutor {

    private Map calls = new HashMap();

    private String serviceURI;

    private String operationName;

    private String portName;

    private String targetNamespace;

    private String serviceName;

    private Object[] params;

    public WSExecutor(String serviceURI, String targetNamespace, String serviceName, String portName, String operationName, Object[] params) {
        this.serviceURI = serviceURI;
        this.targetNamespace = targetNamespace;
        this.serviceName = serviceName;
        this.portName = portName;
        this.operationName = operationName;
        this.params = params;
    }

    public WSExecutor(String serviceURI, String targetNamespace, String serviceName, String portName) {
        this.serviceURI = serviceURI;
        this.targetNamespace = targetNamespace;
        this.serviceName = serviceName;
        this.portName = portName;
    }

    public void addCall(String operationName, Object[] params) {
        calls.put(generateKey(operationName, params), new Object[] { operationName, params });
    }

    private String generateKey(String operationName, Object[] params) {
        StringBuffer sb = new StringBuffer();
        sb.append(operationName);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                sb.append(params[i].getClass());
            }
        }
        return sb.toString();
    }

    public String getTargetNamespace() {
        return targetNamespace;
    }

    public void setTargetNamespace(String targetNamespace) {
        this.targetNamespace = targetNamespace;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    private void parseWSDL(String wsdlURI) throws WSDLException {
        Definition wsdlDef = getWSDLDefinition(wsdlURI);
        Definition interfaceDef = getInterfaceDefinition(wsdlDef);
        String targetNamespace = wsdlDef.getTargetNamespace();
        PortType port = interfaceDef.getPortType(new QName(targetNamespace, portName));
        QName bindingQName = getBindingQName(interfaceDef, port);
        String serviceName = findService(wsdlDef, bindingQName);
        Operation op = findOperation(port.getOperations(), operationName);
        printInformation(targetNamespace, serviceName);
        try {
            Object ret = invoke();
            System.err.println("Call Service At " + serviceName + " and get Result " + ret);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * @param inputParams
	 */
    private void printInformation(String targetNamespace, String serviceName) {
        System.out.print("\nAxis parameters gathered:\nTargetNamespace = " + targetNamespace + "\n" + "Service Name = " + serviceName + "\n" + "Port Name = " + portName + "\n" + "Operation Name = " + operationName + "\n" + "Input Parameters = ");
        Object[] inputParams = getParams();
        for (int i = 0; i < inputParams.length; i++) {
            System.out.print(inputParams[i]);
            if (inputParams.length != 0 && inputParams.length - 1 > i) {
                System.out.print(", ");
            }
        }
        System.out.println("\n");
    }

    /**
	 * @param operations
	 * @param operationName
	 * @return
	 */
    private Operation findOperation(List operations, String operationName) {
        Operation op = null;
        for (Iterator opIt = operations.iterator(); opIt.hasNext(); ) {
            op = (Operation) opIt.next();
            if (op.getName().equals(operationName)) {
                break;
            }
        }
        return op;
    }

    /**
	 * @param wsdlDef
	 * @param bindingQName
	 * @return
	 */
    private String findService(Definition wsdlDef, QName bindingQName) {
        String serviceName = null;
        String portName = null;
        Map implServices = wsdlDef.getServices();
        Set s = implServices.keySet();
        Iterator it = s.iterator();
        while (it.hasNext()) {
            Service serv = (Service) implServices.get(it.next());
            Map m = serv.getPorts();
            Set set = m.keySet();
            Iterator iter = set.iterator();
            while (iter.hasNext()) {
                Port p = (Port) m.get(iter.next());
                if (p.getBinding().getQName().toString().equals(bindingQName.toString())) {
                    serviceName = serv.getQName().toString();
                    portName = p.getName();
                    break;
                }
            }
            if (portName != null) break;
        }
        return serviceName;
    }

    /**
	 * @param interfaceDef
	 * @param port
	 * @return
	 * @throws WSDLException
	 */
    private QName getBindingQName(Definition interfaceDef, PortType port) throws WSDLException {
        QName bindingQName = null;
        Map bindings = interfaceDef.getBindings();
        Set s = bindings.keySet();
        Iterator it = s.iterator();
        while (it.hasNext()) {
            Binding binding = (Binding) bindings.get(it.next());
            if (binding.getPortType() == port) {
                bindingQName = binding.getQName();
            }
        }
        if (bindingQName == null) {
            throw new WSDLException(WSDLException.OTHER_ERROR, "No binding found for chosen port type.");
        }
        return bindingQName;
    }

    /**
	 * @param wsdlDef
	 * @return
	 */
    private Definition getInterfaceDefinition(Definition wsdlDef) {
        Definition interfaceDef = parseImports(wsdlDef);
        if (interfaceDef == null) {
            System.out.println("No WSDL interface definition found from imports, use the wsdl definition directly");
            interfaceDef = wsdlDef;
        }
        return interfaceDef;
    }

    /**
	 * @param wsdlDef
	 * @return
	 */
    private Definition parseImports(Definition wsdlDef) {
        Definition interfaceDef = null;
        Map imports = wsdlDef.getImports();
        Set s = imports.keySet();
        Iterator it = s.iterator();
        while (it.hasNext()) {
            Object o = it.next();
            Vector intDoc = (Vector) imports.get(o);
            for (int i = 0; i < intDoc.size(); i++) {
                Object obj = intDoc.elementAt(i);
                if (obj instanceof ImportImpl) {
                    interfaceDef = ((ImportImpl) obj).getDefinition();
                }
            }
        }
        return interfaceDef;
    }

    /**
	 * @param wsdlURI
	 * @return
	 * @throws WSDLException
	 */
    private Definition getWSDLDefinition(String wsdlURI) throws WSDLException {
        Definition implDef = null;
        try {
            WSDLFactory factory = new WSDLFactoryImpl();
            WSDLReader reader = factory.newWSDLReader();
            implDef = reader.readWSDL(wsdlURI);
        } catch (WSDLException e) {
            e.printStackTrace();
        }
        if (implDef == null) {
            throw new WSDLException(WSDLException.OTHER_ERROR, "No WSDL impl definition found.");
        }
        return implDef;
    }

    public Class wrapPrimitive(Class cl) throws WSDLException {
        String type = cl.getName();
        try {
            if (type.equals("byte")) {
                return Class.forName("java.lang.Byte");
            } else if (type.equals("char")) {
                return Class.forName("java.lang.Character");
            } else if (type.equals("short")) {
                return Class.forName("java.lang.Short");
            } else if (type.equals("int")) {
                return Class.forName("java.lang.Integer");
            } else if (type.equals("double")) {
                return Class.forName("java.lang.Double");
            } else if (type.equals("float")) {
                return Class.forName("java.lang.Float");
            } else if (type.equals("long")) {
                return Class.forName("java.lang.Long");
            } else {
                throw new WSDLException(WSDLException.OTHER_ERROR, "Unrecognized primitive type");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Object invoke() throws Exception {
        return doInvoke(getServiceURI(), getTargetNamespace(), getServiceName(), getPortName(), getOperationName(), getParams());
    }

    public Object[] bulkInvoke() {
        List result = new ArrayList();
        Object[] values = calls.values().toArray();
        for (int i = 0; i < values.length; i++) {
            Object[] value = (Object[]) values[i];
            if (value != null) {
                String operationName = (String) value[0];
                Object[] params = (Object[]) value[1];
                try {
                    Object obj = doInvoke(getServiceURI(), getTargetNamespace(), getServiceName(), getPortName(), operationName, params);
                    if (obj != null) result.add(obj);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return result.toArray();
    }

    public abstract Object doInvoke(String serviceURI, String targetNamespace, String serviceName, String portName, String operationName, Object[] inputParams) throws Exception;

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public String getServiceURI() {
        return serviceURI;
    }

    public void setServiceURI(String serviceURI) {
        this.serviceURI = serviceURI;
    }
}
