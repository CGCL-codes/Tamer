package net.sf.crsx.util;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import net.sf.crsx.CRS;
import net.sf.crsx.CRSException;
import net.sf.crsx.Constructor;
import net.sf.crsx.Factory;
import net.sf.crsx.Parser;
import net.sf.crsx.Sink;
import net.sf.crsx.Term;
import net.sf.crsx.Variable;

/**
 * Parser that generates a list of constant constructors, one per line, named as the contents of the line.
 * @author <a href="http://www.research.ibm.com/people/k/krisrose">Kristoffer Rose</a>.
 * @version $Id: LineParser.java,v 1.11 2010/09/29 04:10:26 krisrose Exp $
 */
public class LineParser implements Parser {

    /** Whether we report progress. */
    private boolean noisy;

    /** Whether we report the line number of each constructor. */
    private boolean enumerate;

    /**
	 * Create one.
	 */
    public LineParser() {
    }

    public Parser parser(Factory<? extends Term> factory) {
        return this;
    }

    public Sink parse(Sink sink, String category, Reader reader, String identifier, int firstLine, int firstColumn, ExtensibleMap<String, Variable> bound, Map<String, Variable> free) throws CRSException, IOException {
        if (category != null) throw new UnsupportedOperationException("Line parser does not support categories.");
        boolean more = true;
        int character;
        StringBuilder line = new StringBuilder();
        int count = 0;
        Constructor cons = sink.makeConstructor(CRS.CONS_SYMBOL);
        Constructor nil = sink.makeConstructor(CRS.NIL_SYMBOL);
        while (more) {
            switch(character = reader.read()) {
                case -1:
                    more = false;
                case '\n':
                    {
                        sink = sink.start(cons);
                        String text = line.toString();
                        Constructor c = sink.makeConstructor(text);
                        if (enumerate) Util.wrapWithProperty(sink, c, LOCATION_PROPERTY, Buffer.materialize(sink, sink.makeConstructor(Util.constructLocation(count + firstLine, 1, count + firstLine, text.length()))));
                        sink = sink.start(c).end();
                        ++count;
                        if (noisy) System.out.println("Line " + count + ": " + text);
                        line = new StringBuilder();
                        break;
                    }
                default:
                    line.append((char) character);
                    break;
            }
        }
        sink = sink.start(nil).end();
        while (count-- > 0) sink = sink.end();
        return sink;
    }

    public Iterable<String> categories() {
        return new EmptyIterable<String>();
    }

    public void setEmbeddedParser(Parser subparser) throws CRSException {
        throw new CRSException("Line parser does not support embedded parser.");
    }

    public void setParserVerbose(boolean verbose) {
        noisy = verbose;
    }

    public void setParserLocation(boolean capture) {
        enumerate = capture;
    }
}
