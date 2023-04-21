package org.genxdm.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import org.genxdm.exceptions.XdmMarshalException;
import org.xml.sax.InputSource;

/** Provides an interface for reading and writing XML.
 * 
 * The DocumentHandler interface provides a means to supply XML (as
 * readers or input streams) to be built into a target
 * tree model; it also permits tree models to "serialize" onto parallel output
 * abstractions (writers and output streams). 
 *
 * @param <N> the Node handle.
 */
public interface DocumentHandler<N> {

    /** Parse an input stream (bytes) as a document node.
     * 
     * This method typically delegates to parse(InputSource, systemId).
     * 
     * @param byteStream the input; may not be null
     * @param systemId the URI of the document, if available; may be null
     * @return a document node resulting from the parse
     */
    N parse(final InputStream byteStream, final URI systemId) throws IOException, XdmMarshalException;

    /** Parse a reader (characters) as a document node.
     * 
     * This method typically delegates to parse(InputSource, systemId).
     * 
     * @param characterStream the input; may not be null
     * @param systemId the URI of the document, if available; may be null
     * @return a document node resulting from the parse
     */
    N parse(final Reader characterStream, final URI systemId) throws IOException, XdmMarshalException;

    /** Parse a SAX InputSource to a document node.
     * 
     * The contract of this method is that the SAX InputSource will be read
     * completely, feeding a document builder associated with the corresponding
     * bridge.
     * 
     * @param source the input; may not be null
     * @param systemId the URI of the document, if available; may be null
     * @return a document node resulting from the parse.
     * 
     */
    N parse(final InputSource source, final URI systemId) throws IOException, XdmMarshalException;

    /** Write XML, as bytes in a specified character encoding, to an output stream, unformatted.
     *
     * The output makes no attempt to format or pretty-print the output, but guarantees
     * well-formedness (for core XML and for namespaces), and tries to be compact (that
     * is, no newlines or spaces will be introduced).
     * 
     * @param byteStream the target output stream; may not be null
     * @param source the starting node from which to traverse (usually a document node); may not be null
     * @param encoding the encoding in which to write characters as bytes; if null, or an unsupported encoding
     *   for the JVM, "UTF-8" will be used.
     */
    void write(final OutputStream byteStream, final N source, String encoding) throws IOException, XdmMarshalException;

    /** Write XML, as characters, to a Writer, unformatted.
     * 
     * The output makes no attempt to format or pretty-print the output, but guarantees
     * well-formedness (for core XML and for namespaces), and tries to be compact (that
     * is, no newlines or spaces will be introduced).
     * 
     * @param characterStream the target Writer; may not be null
     * @param source the starting node from which to traverse (usually a document node); may not be null
     */
    void write(final Writer characterStream, final N source) throws IOException, XdmMarshalException;
}
