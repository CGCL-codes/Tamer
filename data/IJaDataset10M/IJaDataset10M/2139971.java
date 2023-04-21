package org.apache.myfaces.trinidadinternal.style.xml.parse;

import org.apache.myfaces.trinidadinternal.style.PropertyParseException;
import org.apache.myfaces.trinidadinternal.style.util.CSSUtils;

/**
 * PropertyValidater for font-size values
 * @version $Name:  $ ($Revision: adfrt/faces/adf-faces-impl/src/main/java/oracle/adfinternal/view/faces/style/xml/parse/FontSizeValidater.java#0 $) $Date: 10-nov-2005.18:58:06 $
 */
class FontSizeValidater implements PropertyValidater {

    /**
   * Tests whether the specified value is valid for the given property 
   * name.  If the property is not valid, an error message is returned.
   * Otherwise, null is returned to indicate that the value is valid.
   */
    public String validateValue(String name, String value) {
        try {
            CSSUtils.parseFontSize(value);
        } catch (PropertyParseException e) {
            return e.getMessage();
        }
        return null;
    }
}
