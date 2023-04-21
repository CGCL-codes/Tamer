package org.ostion.util.webservice;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import org.apache.axis.client.Call;

/**
 * Web Service invoker. Uses Apache Axis 1.4.<br>
 * Parameters should be provided based on the Web Service Description document (WSDL).<br>
 * <br>
 * Needed JAR files on JBoss 4.2.2.GA:<br>
 * - From Axis: axis.jar, commons-discovery-0.2.jar<br>
 * - The other JAR files are part of JBoss.<br>
 * <br>
 * Needed JAR files for standalone execution:<br>
 * - From Axis: axis.jar, commons-discovery-0.2.jar, jaxrpc.jar, commons-logging-1.0.4.jar, saaj.jar, wsdl4j-1.5.1.jar<br>
 * - Java Activation Framework: activation.jar<br>
 * - JavaMail API: mail.jar<br>
 * <br>  
 * Based on: http://snipplr.com/view.php?codeview&id=31140
 * 
 * @author Luis Antonio Tama Wong
 * 
 * @since 2010-07-31
 */
public class WebServiceInvoker {

    /**
	 * Web Service parameter
	 * @author Luiggi
	 *
	 */
    public static final class Parameter {

        public final String name;

        public final QName type;

        public final Object value;

        /**
		 * Web Service parameter
		 * @param name String
		 * @param type QName
		 * @param value Object
		 */
        public Parameter(String name, QName type, Object value) {
            this.name = name;
            this.type = type;
            this.value = value;
        }
    }

    /**
	 * Web Service invoker. Uses Apache Axis 1.4.<br>
	 * Parameters should be provided based on the Web Service Description document (WSDL).
	 * 
	 * @param targetWebServiceURL String
	 * @param soapActionURI String
	 * @param namespaceURI String
	 * @param operationName String
	 * @param returnType QName
	 * @param parameters Parameter[]
	 * @return Object
	 */
    public static Object invoke(String targetWebServiceURL, String soapActionURI, String namespaceURI, String operationName, QName returnType, Parameter... parameters) {
        try {
            Call call = new Call(targetWebServiceURL);
            call.setSOAPActionURI(soapActionURI);
            call.setOperationName(new QName(namespaceURI, operationName));
            List<Object> values = new ArrayList<Object>();
            for (Parameter parameter : parameters) {
                call.addParameter(new QName(namespaceURI, parameter.name), parameter.type, ParameterMode.IN);
                values.add(parameter.value);
            }
            call.setReturnType(returnType);
            return call.invoke(values.toArray());
        } catch (MalformedURLException mue) {
            mue.getMessage();
        } catch (RemoteException re) {
            re.getMessage();
        }
        return null;
    }
}
