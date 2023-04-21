package edu.mit.lcs.haystack.rdf.converters;

import edu.mit.lcs.haystack.rdf.IRDFContainer;
import edu.mit.lcs.haystack.rdf.IURIGenerator;
import edu.mit.lcs.haystack.rdf.LocalRDFContainer;
import edu.mit.lcs.haystack.rdf.RDFException;
import edu.mit.lcs.haystack.rdf.Resource;
import edu.mit.lcs.haystack.rdf.Utilities;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

/**
 * <p>A Parser is an abstract class implementing the IParser
 * interface.  TextParser takes care of managing the details of
 * buffering, input streams, and character sets for its subclasses.
 * If the constructor is provided with a Charset, it will be used to
 * process the input.  Otherwise, the Charset provided by the
 * URLConnection (for Resources), will be used.  If neither of these
 * is available, the system default will be used.
 *
 * @author Nick Matsakis */
public abstract class TextParser implements IParser {

    URL parseURL;

    private Charset charset;

    protected static void parseFile(String args[], TextParser parser) throws Exception {
        File file = new File(args[0]);
        if (!file.exists()) {
            System.out.println("Usage: ...Parser filename");
            System.exit(-1);
        }
        System.out.print("Parsing...");
        java.net.URL url = file.toURL();
        IRDFContainer rdf = new LocalRDFContainer();
        Resource result = parser.parse(url, rdf);
        System.out.println("Finished. Parser returned URI: " + result);
        System.out.print("Writing...");
        file = new File(args[0].substring(0, args[0].lastIndexOf(".")) + ".rdf");
        Writer out = new BufferedWriter(new FileWriter(file));
        Utilities.generateRDF(rdf, out);
        out.close();
        System.out.println("finished writing file: " + file);
    }

    public TextParser() {
    }

    ;

    /** Constructs a Parser which will use <code>charset</code> as its
   	   charset for text parsing. This constructor should not be used
   	   unless it is known that the default Characterset will be
   	   incorrect. */
    public TextParser(Charset charset) {
        this.charset = charset;
    }

    public Resource parse(Resource res, IRDFContainer target) throws RDFException, IOException {
        return parse(new URL(res.getURI()), target);
    }

    ;

    public Resource parse(URL url, IRDFContainer target) throws RDFException, IOException {
        parseURL = url;
        URLConnection connection = url.openConnection();
        if (charset == null) {
            charset = Charset.forName("UTF-8");
        }
        Reader reader = new InputStreamReader(connection.getInputStream(), charset);
        return internalParse(reader, target);
    }

    ;

    /** This version of <code>parse()</code> assumes the InputStream
        is sufficiently buffered. */
    public Resource parse(InputStream is, IRDFContainer target) throws RDFException, IOException {
        return internalParse(new InputStreamReader(is), target);
    }

    ;

    /** This version of <code>parse()</code> assumes the reader is
        sufficiently buffered. */
    public Resource parse(Reader r, IRDFContainer target) throws RDFException, IOException {
        return internalParse(r, target);
    }

    public Resource parse(String s, IRDFContainer target) throws RDFException, IOException {
        return internalParse(new StringReader(s), target);
    }

    /** @deprecated */
    public void parse(Resource res, InputStream is, IRDFContainer target, IURIGenerator uirg) throws RDFException, IOException {
        internalParse(new InputStreamReader(is), target);
    }

    /** This version doesn't do anything! */
    protected Resource internalParse(Reader reader, IRDFContainer target) throws RDFException, IOException {
        return null;
    }
}
