package net.sf.clexw.cml.trans;

import java.io.*;
import java.util.*;
import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;
import org.antlr.stringtemplate.*;
import net.sf.clexw.cml.proc.*;
import net.sf.clexw.cml.parse.*;
import net.sf.clexw.cml.util.*;

/**
 * This class represents a transformer to convert source code in the Code
 * Modifier Language to source code in the target language. This class should be
 * instantced only with an implementation of the CMLTransformerFactory
 * interface.
 *
 * @author J. Pikl
 */
public class CMLTransformer {

    /** Type of source code used to define members of classes. */
    public static final int MEMBER_DEFS = CMLParser.CT_MEMBER_DEFS;

    /** Type of source code used within the body of methods and constructors. */
    public static final int CODE_FRAGMENT = CMLParser.CT_CODE_FRAGMENT;

    /** Type of source code used for expression. */
    public static final int EXPRESSION = CMLParser.CT_EXPRESSION;

    /** The directory containing language templates. */
    private String templateDir;

    /** The string used for indentation of the output source code. */
    private String indentation;

    /** List of algorithms to process the abstract syntax tree. */
    private List<ASTProcessing> processList;

    /**
     * Constructor. This constructor should be called only within an
     * implementation of the CMLTransformerFactory interface.
     *
     * @param templateDir the directory containing language templates
     * @param indentation the string used for indentation of the output source
     *                    code
     */
    CMLTransformer(String templateDir, String indentation) {
        this.templateDir = templateDir;
        this.indentation = indentation;
        this.processList = new ArrayList<ASTProcessing>();
    }

    /**
     * Sets the directory containing language templates.
     *
     * @param templateDir the directory containing language templates
     */
    public void setTemplateDir(String templateDir) {
        this.templateDir = templateDir;
    }

    /**
     * Sets the string used for indentation of the output source code.
     *
     * @param indentation the string used for indentation of the output source
     *                    code
     */
    public void setIndentation(String indentation) {
        this.indentation = indentation;
    }

    /**
     * Adds a new algorithm for processing of abstract syntax tree.
     *
     * @param processing the new AST processing
     */
    public void addASTProcessing(ASTProcessing processing) {
        processList.add(processing);
    }

    /**
     * Generates a new instance of the CMLTransformException which is based on
     * the error message from the parser.
     *
     * @param br the base recognizer instance (parser or tree parser)
     * @param ex an exception thrown by the parser
     * @return a new instance of the CMLTransformException
     */
    private CMLTransformException buildTransformException(BaseRecognizer br, RecognitionException ex) {
        int line = ex.line;
        int column = ex.charPositionInLine;
        String message = br.getErrorMessage(ex, br.getTokenNames());
        return new CMLTransformException(message, line, column);
    }

    /**
     * Performs the transformation of the input source code in the CML to the
     * source code in the target language.
     *
     * @param input     the input source code
     * @param firstLine number of the first line of the input source code
     * @param template  the name of template used for printing the output source
     *                  code
     * @param codeType  the type of the parsed input source code
     * @return the output source code in the target language
     * @throws CMLTransformException when an error occurs during the
     *                               transformation
     */
    public String transform(String input, int firstLine, String template, int codeType) throws CMLTransformException {
        if (input == null || input.isEmpty()) {
            return null;
        }
        CharStream stream = new CMLStringStream(input, firstLine);
        return doTransformation(stream, template, codeType);
    }

    /**
     * Performs the transformation of the input source code in the CML to the
     * source code in the target language.
     *
     * @param input     the file containing input source code
     * @param template  the name of template used for printing the output source
     *                  code
     * @param codeType  the type of the parsed input source code
     * @return the output source code in the target language
     * @throws CMLTransformException when an error occurs during the
     *                               transformation
     */
    public String transform(File input, String template, int codeType) throws CMLTransformException {
        try {
            CharStream stream = new ANTLRFileStream(input.getAbsolutePath());
            return doTransformation(stream, template, codeType);
        } catch (IOException ex) {
            String msg = "Unable to read input file: " + ex.getMessage();
            throw new CMLTransformException(msg);
        }
    }

    /**
     * Checks the number of the specified type of source code. An exception is
     * thrown if the type has wrong value.
     *
     * @throws CMLTransformException if the code type has wrong value
     */
    private void checkCodeType(int codeType) throws CMLTransformException {
        if (codeType != MEMBER_DEFS && codeType != CODE_FRAGMENT && codeType != EXPRESSION) {
            String msg = "Wrong value of the code type parameter: " + codeType;
            throw new CMLTransformException(msg);
        }
    }

    /**
     * Performs the transformation of the input source code in the CML to the
     * source code in the target language.
     *
     * @param stream    a stream to read the input source code
     * @param template  the name of template used for printing the output source
     *                  code
     * @param codeType  the type of the parsed input source code
     * @return the output source code in the target language
     * @throws CMLTransformException when an error occurs during the
     *                               transformation
     */
    private String doTransformation(CharStream stream, String template, int codeType) throws CMLTransformException {
        checkCodeType(codeType);
        CMLLexer lexer = new CMLLexer(stream);
        TokenStream tokens = new ModifiableTokenStream(lexer);
        CMLParser parser = new CMLParser(tokens);
        PathGroupLoader loader = new PathGroupLoader(templateDir, null);
        StringTemplateGroup.registerGroupLoader(loader);
        StringTemplateGroup templates = loader.loadGroup(template);
        templates.setStringTemplateWriter(GlobalIndentWriter.class);
        GlobalIndentWriter.setIndentString(indentation);
        Tree tree = null;
        CommonTreeNodeStream nodes = null;
        try {
            tree = (Tree) parser.codeModifier(codeType).getTree();
            nodes = new CommonTreeNodeStream(tree);
            nodes.setTokenStream(tokens);
        } catch (RecognitionException ex) {
            throw buildTransformException(parser, ex);
        } catch (RuntimeException ex) {
            if (ex.getCause() instanceof RecognitionException) {
                RecognitionException rex = (RecognitionException) ex.getCause();
                throw buildTransformException(lexer, rex);
            }
            throw ex;
        }
        for (ASTProcessing processing : processList) {
            try {
                processing.setTokenStream(tokens);
                processing.processAST(tree);
            } catch (ASTProcessException ex) {
                System.err.println("Recoverable error: AST processing failed");
                System.err.println("  " + ex.getMessage());
            }
        }
        CMLEval eval = new CMLEval(nodes);
        eval.setTemplateLib(templates);
        try {
            StringTemplate output = null;
            output = (StringTemplate) eval.codeModifier().getTemplate();
            return output.toString();
        } catch (RecognitionException ex) {
            throw buildTransformException(eval, ex);
        }
    }
}
