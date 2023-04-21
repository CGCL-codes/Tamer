package ru.adv.util;

import org.w3c.dom.Element;
import org.w3c.dom.Document;

/**
 * User: roma
 * Date: 26.09.2003
 * Time: 12:36:12
 */
public interface XMLObject {

    Element toXML(Document doc);
}
