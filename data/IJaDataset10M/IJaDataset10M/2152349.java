package eu.soa4all.execution.executor;

/**
            *  ExtensionMapper class
            */
public class ExtensionMapper {

    public static java.lang.Object getTypeObject(java.lang.String namespaceURI, java.lang.String typeName, javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
        if ("http://soa4all.eu/execution/executor".equals(namespaceURI) && "adaptiveInvokeRequestType".equals(typeName)) {
            return eu.soa4all.execution.executor.AdaptiveInvokeRequestType.Factory.parse(reader);
        }
        if ("http://soa4all.eu/execution/executor".equals(namespaceURI) && "serviceType".equals(typeName)) {
            return eu.soa4all.execution.executor.ServiceType.Factory.parse(reader);
        }
        if ("http://soa4all.eu/execution/executor".equals(namespaceURI) && "alternativeServiceListType".equals(typeName)) {
            return eu.soa4all.execution.executor.AlternativeServiceListType.Factory.parse(reader);
        }
        throw new org.apache.axis2.databinding.ADBException("Unsupported type " + namespaceURI + " " + typeName);
    }
}
