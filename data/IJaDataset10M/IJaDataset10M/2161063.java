package org.jostraca.transform;

import org.jostraca.Property;
import org.jostraca.util.Standard;
import org.jostraca.util.PropertySet;

/** <b>Description:</b><br>
 *  Create a programming statement which inserts text into the generated content.
 */
public class TextElementTransform extends TextualTransformSupport {

    private String iParamInsertPrefix = Standard.EMPTY;

    private String iParamInsertSuffix = Standard.EMPTY;

    private String iParamTextQuote = Standard.EMPTY;

    /** @see org.jostraca.transform.TextualTransform */
    public String transform(String pFrom) {
        String result = iParamInsertPrefix + iParamTextQuote + pFrom + iParamTextQuote + iParamInsertSuffix;
        return result;
    }

    /** @see org.jostraca.transform.TextualTransform */
    public void setParameters(PropertySet pPropertySet) {
        iParamInsertPrefix = pPropertySet.get(Property.lang_InsertPrefix);
        iParamInsertSuffix = pPropertySet.get(Property.lang_InsertSuffix);
        iParamTextQuote = pPropertySet.get(Property.lang_TextQuote);
    }
}
