package edu.mit.lcs.haystack.adenine.compilers.javaByteCode;

import edu.mit.lcs.haystack.HaystackException;
import edu.mit.lcs.haystack.adenine.compilers.ICompiler;
import edu.mit.lcs.haystack.adenine.parser2.IParserVisitor;
import edu.mit.lcs.haystack.adenine.parser2.Parser;
import edu.mit.lcs.haystack.adenine.parser2.SyntaxException;
import edu.mit.lcs.haystack.adenine.tokenizer.ErrorToken;
import edu.mit.lcs.haystack.adenine.tokenizer.IScannerVisitor;
import edu.mit.lcs.haystack.adenine.tokenizer.Location;
import edu.mit.lcs.haystack.adenine.tokenizer.Token;
import edu.mit.lcs.haystack.content.ContentClient;
import edu.mit.lcs.haystack.proxy.IServiceAccessor;
import edu.mit.lcs.haystack.rdf.IRDFContainer;
import edu.mit.lcs.haystack.rdf.Resource;
import java.io.Reader;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

/**
 * @author David Huynh
 */
public class JavaByteCodeCompiler implements ICompiler {

    protected IRDFContainer m_target;

    protected String m_sourceFile;

    protected IParserVisitor m_visitor = new IParserVisitor() {

        public void start(Location startLocation) {
        }

        public void end(Location endLocation) {
        }

        public void onException(Exception exception) {
            m_errors.add(exception);
        }
    };

    protected List m_errors;

    static final org.apache.log4j.Logger s_logger = org.apache.log4j.Logger.getLogger(JavaByteCodeCompiler.class);

    public JavaByteCodeCompiler(IRDFContainer target) {
        m_target = target;
    }

    public String getSourceFile() {
        return m_sourceFile;
    }

    public java.util.List compile(Resource pakkage, Reader input, String sourceFile, IRDFContainer source, IServiceAccessor sa) {
        m_sourceFile = sourceFile;
        if (input == null) {
            String str;
            try {
                str = ContentClient.getContentClient(pakkage, source, sa).getContentAsString();
            } catch (Exception e) {
                List errors = new LinkedList();
                errors.add(e);
                return errors;
            }
            input = new StringReader(str);
        }
        return doCompile(input, new TopLevelVisitor(new IParserVisitor() {

            public void start(Location startLocation) {
            }

            public void end(Location endLocation) {
            }

            public void onException(Exception exception) {
                addException(exception);
            }
        }, pakkage, m_target, this));
    }

    protected List doCompile(Reader input, TopLevelVisitor visitor) {
        m_errors = new LinkedList();
        Parser.parse(new IScannerVisitor() {

            public void onToken(Token token) {
                if (token instanceof ErrorToken) {
                    addException(new SyntaxException("Error token of type " + token.getType(), token.getSpan()));
                }
            }
        }, visitor, input);
        return m_errors;
    }

    public IRDFContainer getTarget() {
        return m_target;
    }

    public org.apache.log4j.Logger getLogger() {
        return s_logger;
    }

    public void addException(Exception e) {
        if (m_errors.isEmpty()) HaystackException.uncaught(e);
        m_errors.add(e);
        s_logger.error(e);
    }
}
