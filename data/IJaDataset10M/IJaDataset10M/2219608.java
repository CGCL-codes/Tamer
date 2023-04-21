package org.desimeter.xmlJava.xmlReader;

import org.w3c.dom.Node;

/**
 * @auther: sandeep dixit.<a href="mailto:sandeep.dixit@ugs.com">sandeep.dixit@ugs.com</a>
 * @Date: 01-Apr-2009
 * @Time: 20:17:24
 */
public class FloatReader implements Reader {

    public Float read(Node xmlNode) {
        if (xmlNode.getNodeValue() == null || !"".equals(xmlNode.getNodeValue().trim())) {
            return null;
        }
        return Float.parseFloat(xmlNode.getNodeValue());
    }
}
