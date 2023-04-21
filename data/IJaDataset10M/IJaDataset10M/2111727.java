package org.openacs.message;

import java.util.ArrayList;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import org.openacs.Message;

/**
 *
 * @author Administrator
 */
public class X_00000C_ShowStatus extends Message {

    public X_00000C_ShowStatus() {
        name = "X_00000C_ShowStatus";
    }

    @Override
    protected void createBody(SOAPBodyElement body, SOAPFactory spf) throws SOAPException {
        SOAPElement elm = body.addChildElement(spf.createName("ExecCommandList"));
        elm.setAttribute(SOAP_ARRAY_TYPE, "xsd:string[" + String.valueOf(ExecCommandList.size()) + "]");
        for (int i = 0; i < ExecCommandList.size(); i++) {
            SOAPElement s = elm.addChildElement("string");
            s.setValue(ExecCommandList.get(i));
        }
    }

    @Override
    protected void parseBody(SOAPBodyElement body, SOAPFactory f) throws SOAPException {
        throw new UnsupportedOperationException("Not implemented.");
    }

    public void addCommand(String cmd) {
        ExecCommandList.add(cmd);
    }

    private ArrayList<String> ExecCommandList = new ArrayList<String>();
}
