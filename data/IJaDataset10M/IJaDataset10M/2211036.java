package com.erinors.tapestry.tapdoc.xml;

import org.apache.tapestry.IMarkupWriter;

/**
 * @author Norbert Sándor
 */
public interface XmlPart {

    public void toXml(IMarkupWriter out);
}
