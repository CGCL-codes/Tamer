package com.idna.wsconsumer.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import javax.xml.namespace.QName;
import org.apache.axis2.databinding.utils.BeanUtil;
import org.apache.axiom.om.*;

/**
 * @author kan.sun
 *
 */
public class ClientUtilImpl implements ClientUtil {

    private static ApplicationUtilImpl utils;

    private static Properties prop;

    private static String xmlRequest;

    private void init(String preffix) {
        try {
            utils = new ApplicationUtilImpl();
            prop = utils.loadProperty(ApplicationUtilImpl.PROP_PATH);
            xmlRequest = utils.readFile(prop.getProperty(preffix.trim().toLowerCase() + "_xmlrequest"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public OMElement getOperationOMElement(String operationName, String rootElement) {
        init(rootElement);
        OMElement elem = BeanUtil.getOMElement(new QName(operationName), new String[] { xmlRequest, "1" }, null, false, null);
        return elem;
    }

    public OMElement[] getPartOMElmenent(String[] localNames, String rootElement) {
        init(rootElement);
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMElement[] parts = new OMElement[localNames.length];
        for (int i = 0; i < localNames.length; i++) {
            OMElement part = fac.createOMElement(localNames[i], null);
            if (part.getQName().getLocalPart().equalsIgnoreCase("searchPacket")) {
                part.addChild(fac.createOMText(xmlRequest));
            } else if (part.getQName().getLocalPart().equalsIgnoreCase("apiVersion")) {
                part.addChild(fac.createOMText("1"));
            }
            parts[i] = part;
        }
        return parts;
    }
}
