package org.xmlfield.core.types;

import org.xmlfield.annotations.FieldXPath;
import org.xmlfield.annotations.ResourceXPath;

/**
 * 
 * This type should be used instead of 'String' to ensure safety of setXXX()
 * methods.
 * 
 * @author Nicolas Richeton <nicolas.richeton@capgemini.com>
 * 
 */
@ResourceXPath("/")
public interface XmlString {

    @FieldXPath(".")
    String getString();

    void setString(String s);
}
