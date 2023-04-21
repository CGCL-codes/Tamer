package org.xmlfield.tests.pack2;

import org.xmlfield.annotations.FieldXPath;
import org.xmlfield.annotations.Namespaces;
import org.xmlfield.annotations.ResourceXPath;

/**
 * @author Nicolas Richeton <nicolas.richeton@capgemini.com>
 */
@Namespaces({ "xmlns:a=http://www.w3.org/2005/Atom", "xmlns:x=http://www.w3.org/1999/xhtml" })
@ResourceXPath("/a:entry")
public interface AtomCatalog {

    @FieldXPath("a:title/x:div/x:span[@class='name']")
    String getName();

    @FieldXPath("a:content/x:div/x:div[@class='cd']")
    AtomCd[] getCd();

    AtomCd addToCd();
}
