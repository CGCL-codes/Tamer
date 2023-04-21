package org.orbeon.oxf.processor.converter;

import org.orbeon.oxf.processor.serializer.legacy.TextSerializer;
import org.orbeon.oxf.processor.ProcessorInputOutputInfo;

/**
 * Converts XML into text according to the XSLT Text output method.
 *
 * See http://www.w3.org/TR/xslt#section-Text-Output-Method
 */
public class TextConverter extends TextSerializer {

    public TextConverter() {
        addOutputInfo(new ProcessorInputOutputInfo(OUTPUT_DATA));
    }

    protected String getConfigSchemaNamespaceURI() {
        return XMLConverter.STANDARD_TEXT_CONVERTER_CONFIG_NAMESPACE_URI;
    }
}
