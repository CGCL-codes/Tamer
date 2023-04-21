package com.yerihyo.yeritools.xml;

import org.w3c.dom.Element;

public interface XMLable {

    String toXML();

    /**
	 * optional; May not be implemented
	 * @param xmlString
	 * @throws Exception
	 */
    void fromXML(Element element) throws Exception;
}
