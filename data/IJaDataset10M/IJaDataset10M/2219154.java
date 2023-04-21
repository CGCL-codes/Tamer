package org.genxdm.processor.w3c.xs.validation.api;

import java.io.IOException;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import javax.xml.namespace.QName;
import org.genxdm.xs.ComponentProvider;
import org.genxdm.xs.exceptions.AbortException;
import org.genxdm.xs.exceptions.SchemaExceptionHandler;

public interface VxValidator<A> {

    void characters(char ch[], int start, int length) throws IOException, AbortException;

    void endDocument() throws IOException, AbortException;

    VxPSVI endElement() throws IOException, AbortException;

    void reset();

    void setExceptionHandler(final SchemaExceptionHandler handler);

    void setComponentProvider(final ComponentProvider provider);

    void setOutputHandler(VxOutputHandler<A> handler);

    void startDocument(URI documentURI) throws IOException, AbortException;

    void startElement(final QName elementName, final LinkedList<VxMapping<String, String>> namespaces, final LinkedList<VxMapping<QName, String>> attributes) throws IOException, AbortException;

    void text(List<? extends A> value) throws IOException, AbortException;
}
