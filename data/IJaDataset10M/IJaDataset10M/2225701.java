package br.ufsc.gsigma.wsdl.schema;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

public class SingleValue implements Value {

    private String value;

    public void readFromParam(String paramName, ParamGetter parameters) {
        String value = parameters.get("param/" + paramName);
        this.value = value;
    }

    public void putIntoElement(String name, SOAPElement element) throws SOAPException {
        SOAPElement child = element.addChildElement(name);
        child.addTextNode(value);
    }
}
