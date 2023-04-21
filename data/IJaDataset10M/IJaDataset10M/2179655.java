package org.eclipse.core.databinding.dom.conversion.xerces;

import java.io.StringWriter;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.eclipse.core.databinding.dom.conversion.ElementToStringConverter;
import org.w3c.dom.Element;

/**
 * Converts {@link Element} to String value.
 */
public class XercesElementToStringConverter extends ElementToStringConverter {

    protected String toString(Element element) {
        StringWriter elementContent = new StringWriter();
        OutputFormat outputFormat = new OutputFormat();
        outputFormat.setIndent(1);
        XMLSerializer serializer = new XMLSerializer(elementContent, outputFormat);
        try {
            serializer.serialize(element);
        } catch (Exception e1) {
        }
        return elementContent.toString();
    }
}
