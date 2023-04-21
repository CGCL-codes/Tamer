package org.xmlfield.core.test;

import org.xmlfield.annotations.FieldXPath;
import org.xmlfield.annotations.Namespaces;
import org.xmlfield.annotations.ResourceXPath;

/**
 * @author Nicolas Richeton <nicolas.richeton@capgemini.com>
 */
@Namespaces({ "xmlns:a=http://www.w3.org/2005/Atom", "xmlns:x=http://www.w3.org/1999/xhtml" })
@ResourceXPath("x:div[@class='cd']")
public interface AtomCd {

    @FieldXPath("x:span[@class='title']")
    String getTitle();

    void setTitle(String t);

    @FieldXPath("x:span[@class='price']")
    float getPrice();

    void setPrice(float price);
}
