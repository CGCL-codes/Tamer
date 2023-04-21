package edu.indiana.extreme.xbaya.test.service.adder;

import edu.indiana.extreme.xbaya.test.service.Service;
import org.xmlpull.v1.builder.XmlElement;
import xsul.xwsif_runtime.XmlElementBasedStub;
import java.io.File;

/**
 */
public interface Adder extends XmlElementBasedStub {

    /**
     * SERVICE_NAME
     */
    public static final String SERVICE_NAME = "AdderService";

    /**
     * WSDL_NAME
     */
    public static final String WSDL_NAME = "adder.wsdl";

    /**
     * WSDL_PATH
     */
    public static final String WSDL_PATH = Service.MATH_DIRECTORY_NAME + File.separator + WSDL_NAME;

    /**
     * @param input
     *            the input message
     * @return the output message
     */
    public XmlElement add(XmlElement input);
}
