package edu.mit.lcs.haystack.rdf.converters;

import edu.mit.lcs.haystack.rdf.IRDFContainer;
import edu.mit.lcs.haystack.rdf.IURIGenerator;
import edu.mit.lcs.haystack.rdf.RDFException;
import edu.mit.lcs.haystack.rdf.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;

/**
 * <p>An IParser object parses an InputStream, Reader, or other stream
 * of bits or text into RDF statements added to an RDF container.
 * Calls to the <code>parse</code> methods block until the parse is
 * complete, and the caller is responsible for threading this
 * operation if desired.  Only a single call to a parse method is
 * supported; multiple attempts to parse using the same object will
 * result in undefined behavior.
 *
 * <p>The parse methods return a single resource which in some sense
 * represents the parsed content.  Typically, this will be a Haystack
 * collection of resources but may be a single object. There is no
 * requirement that any statements containing the returned resource
 * will have been added to the store. Additionally, the returned
 * resource is allowed to be <code>null</code> in the case where
 * summarizing the imported stream as a single resource doesn't make
 * sense.  For example, when importing a stream of RDF-XML, one would
 * expect that only statements in the stream would be added to the
 * target.  In such a case it would be possible that there is no
 * resource in the stream that represents the stream itself.
 *
 * <p>The caller is encouraged to use the most general form of the
 * parse methods possible.  For example, if the caller would like a
 * file parsed, the file should be passed to the parser as a
 * <code>URL</code> and the parser will be responsible for retrieving
 * the file using the standard java APIs.  The reason for this is that
 * the IParser may need to create URIs for new resources discovered in
 * the file, and by providing the IParser with the file URL may allow
 * it to generate "better" URIs than would be possible with less
 * information.
 *
 * @author Nick Matsakis */
public interface IParser {

    /** Parses a Java-retrievable Resource. The Resource must be
        retrievable by Java, thus an <code>http:</code> or
        <code>file:</code> URL. */
    public Resource parse(Resource res, IRDFContainer target) throws RDFException, IOException;

    /** Parses a Java-retrievable URL. The URL must be retrievable by
        Java, thus an <code>http:</code> or <code>file:</code> URL. */
    public Resource parse(URL url, IRDFContainer target) throws RDFException, IOException;

    /** Parses a binary input stream as content. Text parsers assume
        the default character encoding unless another encoding has
        been specified through some other means.  */
    public Resource parse(InputStream is, IRDFContainer target) throws RDFException, IOException;

    /** Parses a text string as content. The method should only be
	 *  called if the parser is a text parser. */
    public Resource parse(String s, IRDFContainer target) throws RDFException, IOException;

    /** Parses a Reader as content. The method should only be called
	 *  if the parser is a text parser. */
    public Resource parse(Reader r, IRDFContainer target) throws RDFException, IOException;

    /** @deprecated */
    public void parse(Resource res, InputStream is, IRDFContainer target, IURIGenerator uirg) throws RDFException, IOException;
}
