package net.sourceforge.nrl.parser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.nrl.parser.ast.IRuleFile;
import net.sourceforge.nrl.parser.ast.action.impl.ActionAstResolver;
import net.sourceforge.nrl.parser.ast.impl.Antlr3NRLTreeAdaptor;
import net.sourceforge.nrl.parser.ast.impl.AntlrModelResolver;
import net.sourceforge.nrl.parser.ast.impl.AntlrOperatorResolverVisitor;
import net.sourceforge.nrl.parser.ast.impl.NRLActionParser;
import net.sourceforge.nrl.parser.ast.impl.NRLJFlexer;
import net.sourceforge.nrl.parser.ast.impl.RuleFileImpl;
import net.sourceforge.nrl.parser.model.IModelCollection;
import net.sourceforge.nrl.parser.operators.IOperators;
import net.sourceforge.nrl.parser.preprocessing.ReferencePreprocessor;
import org.antlr.runtime.CommonTokenStream;

/**
 * The main parser interface for creating an NRL AST. This supports both the
 * action and constraint language, and is based on ANTLR.
 * 
 * @author Christian Nentwich
 * @see net.sourceforge.nrl.parser.INRLParser
 */
public class NRLParser implements INRLParser {

    private List<NRLError> errors = new ArrayList<NRLError>();

    /**
	 * Parse an NRL file using a reader, and return an AST. The AST will already
	 * be fully resolved and checked for basic semantic errors, but will have no
	 * model information associated.
	 * <p>
	 * <b>IMPORTANT:</b> Call {@link #getErrors()} after calling this method to
	 * check if any syntax or semantic errors occurred.
	 * <p>
	 * Use {@link #resolveModelReferences(IRuleFile, IModelCollection)} to
	 * attach model information.
	 * 
	 * @param reader the reader to use
	 * @return the rule file or null
	 * @throws Exception
	 */
    public IRuleFile parse(Reader reader) throws Exception {
        errors = new ArrayList<NRLError>();
        String content = getStreamAsString(reader);
        ReferencePreprocessor processor = new ReferencePreprocessor();
        content = processor.process(content);
        NRLJFlexer lexer = new NRLJFlexer(new StringReader(content));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        NRLActionParser parser = new NRLActionParser(tokenStream);
        parser.setTreeAdaptor(new Antlr3NRLTreeAdaptor());
        RuleFileImpl ruleFile = (RuleFileImpl) parser.fileBody().getTree();
        if (parser.getSyntaxErrors().size() > 0) {
            errors = parser.getSyntaxErrors();
            return null;
        }
        ActionAstResolver resolver = new ActionAstResolver();
        errors = resolver.resolve(ruleFile);
        return ruleFile;
    }

    /**
	 * Parse an NRL file using a stream. See {@link #parse(Reader)} for more
	 * information.
	 * 
	 * @param stream the stream
	 * @return the rule file or null if there are errors
	 */
    public IRuleFile parse(InputStream stream) throws Exception {
        return parse(new InputStreamReader(stream));
    }

    /**
	 * Attach model information to a parsed rule file AST. This traverses the
	 * AST and decorates any model references with actual elements from the
	 * model.
	 * <p>
	 * After calling this, you <b>must</b> call {@link #getErrors()} to check
	 * if any errors occurred. If so, the AST may be unstable.
	 * 
	 * @param ruleFile the rule file to decorate
	 * @param models the models to use
	 */
    public void resolveModelReferences(IRuleFile ruleFile, IModelCollection models) {
        errors = new ArrayList<NRLError>();
        AntlrModelResolver resolver = new AntlrModelResolver(models);
        errors = resolver.resolve(ruleFile);
    }

    /**
	 * Resolve reference to operator invocations given an operator collection.
	 * The operator collection has to be loaded separately from an XML
	 * descriptor file, if one is available.
	 * 
	 * @see net.sourceforge.nrl.parser.operators.XmlOperatorPersistence
	 */
    public void resolveOperatorReferences(IRuleFile ruleFile, IOperators[] operators) {
        AntlrOperatorResolverVisitor visitor = new AntlrOperatorResolverVisitor(operators);
        ruleFile.accept(visitor);
        errors = visitor.getErrors();
    }

    /**
	 * After any call to the parse or resolve methods, this method returns a
	 * list of errors, if any.
	 */
    public List<NRLError> getErrors() {
        return errors;
    }

    /**
	 * Uses a reader to read a stream and returns the content as a string.
	 * 
	 * @param reader the reader
	 * @return the content
	 * @throws Exception
	 */
    protected String getStreamAsString(Reader reader) throws Exception {
        StringBuffer result = new StringBuffer();
        char[] buffer = new char[10000];
        while (reader.ready()) {
            int size = reader.read(buffer);
            result.append(new String(buffer, 0, size));
            if (size != 10000) break;
        }
        return result.toString();
    }
}
