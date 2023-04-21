package org.eclipse.core.databinding.dom.conversion.xerces;

import java.io.StringWriter;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.eclipse.core.databinding.dom.conversion.DocumentToStringConverter;
import org.w3c.dom.Document;

/**
 * Converts {@link Document} to String value.
 */
public class XercesDocumentToStringConverter extends DocumentToStringConverter {

    protected String toString(Document document) {
        StringWriter documentContent = new StringWriter();
        OutputFormat outputFormat = new OutputFormat();
        outputFormat.setIndent(1);
        XMLSerializer serializer = new XMLSerializer(documentContent, outputFormat);
        try {
            serializer.serialize(document);
        } catch (Exception e1) {
        }
        return documentContent.toString();
    }
}
