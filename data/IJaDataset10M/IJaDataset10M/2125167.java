package org.cesta.trans.java;

import org.cesta.parsers.dot.DotLexer;
import org.cesta.parsers.dot.DotParser;
import org.cesta.util.antlr.ANTLRHelper;
import org.cesta.util.antlr.java.ANTLRJavaHelper;
import org.cesta.trans.TransformationException;
import org.cesta.types.MappedFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;
import org.cesta.parsers.dot.DotTree;
import org.cesta.parsers.java.ValidateStateTransitionsParser;

/**
 * Transformation, which can check state transitions in class
 * according to rules defined in GraphViz's DOT language.
 *
 * <p>Parameters:</p>
 * <table>
 *  <tr><th>Name</th><th>Type</th><th>Default</th><th>Description</th></tr>
 *  <tr>
 *      <td>stateSetter</td>
 *      <td>String</td>
 *      <td>"setState"</td>
 *      <td>
 *          name of function to set state
 *      </td>
 *  </tr>
 *  <tr>
 *      <td>stateSetterArg</td>
 *      <td>String</td>
 *      <td>"newState"</td>
 *      <td>
 *          name of argument in state setter
 *      </td>
 *  </tr>
 *  <tr>
 *      <td>stateGetter</td>
 *      <td>String</td>
 *      <td>"getState"</td>
 *      <td>
 *          name of function to get state
 *      </td>
 *  </tr>
 *  <tr>
 *      <td>stateConstants</td>
 *      <td>String (regexp)</td>
 *      <td>"STATE_.*"</td>
 *      <td>
 *          name of state constants
 *      </td>
 *  </tr>
 *  <tr>
 *      <td>allowedFunctions</td>
 *      <td>String (regexp)</td>
 *      <td>""</td>
 *      <td>
 *          name of functions, that should be always allowed
 *      </td>
 *  </tr>
 *  <tr>
 *      <td>ignoredFunctions</td>
 *      <td>String (regexp)</td>
 *      <td>""</td>
 *      <td>
 *          name of functions, that should be completely ignored
 *      </td>
 *  </tr>
 * </table>
 * <p>
 *  Basic parameters are inherited from {@link AbstractRewriteTransformation}.
 * </p>
 * @author Tobias Smolka
 */
public class ValidateStateTransitions extends AbstractRewriteTransformation {

    protected DotTree.Graph graph = null;

    public void setGraph(DotTree.Graph graph) {
        this.graph = graph;
    }

    public DotTree.Graph getGraph() {
        return graph;
    }

    /**
     * Transforms single file
     *
     * @param filePair File pair (input/output) to transform
     * @throws TransformationException In case transformation failed
     */
    @Override
    public void transform(MappedFile filePair) throws TransformationException {
        ANTLRInputStream inputStream = null;
        try {
            inputStream = new ANTLRInputStream(new FileInputStream(filePair.getFrom()));
        } catch (IOException ex) {
            throw new TransformationException("Could not open input file.", ex);
        }
        TreeNodeStream nodes = prepareTreeNodeStream(inputStream);
        logger.fine("Calling ValidateState tree parser");
        try {
            ValidateStateTransitionsParser parser = new ValidateStateTransitionsParser(nodes);
            parser.setParams(getParams());
            parser.setDefaultTemplateLib();
            parser.setGraph(getGraph());
            parser.javaSource();
            if (parser.hasErrors()) {
                throw new TransformationException("Parser has failed.");
            }
        } catch (RecognitionException ex) {
            throw new TransformationException("Parser could not process file.", ex);
        }
        try {
            ANTLRHelper.writeTokens(tokens, filePair.getTo());
        } catch (IOException ex) {
            throw new TransformationException("Could not save transformed file.", ex);
        }
        try {
            ANTLRJavaHelper.checkSyntax(tokens);
        } catch (TransformationException ex) {
            throw new TransformationException("Transformation resulted in broken code and contains syntax errors.", ex);
        }
    }

    /**
     * Loads given DOT graph.
     *
     * @throws TransformationException in case that graph could not be loaded and parsed
     */
    public void loadDotGraph() throws TransformationException {
        String dotGrammarFile = getParam("dotGrammar", "").toString();
        if (dotGrammarFile.isEmpty()) {
            throw new TransformationException("Parameter dotGrammar has to be specified");
        }
        logger.info("Loading DOT grammar file from " + dotGrammarFile);
        File f = new File(getParam("baseDir", "").toString(), dotGrammarFile);
        if (f.exists()) {
            dotGrammarFile = f.getPath();
        }
        ANTLRFileStream fs = null;
        try {
            fs = new ANTLRFileStream(dotGrammarFile);
        } catch (IOException ex) {
            throw new TransformationException("DOT grammar file could not be loaded");
        }
        DotLexer dotLexer = new DotLexer(fs);
        CommonTokenStream dotStream = new CommonTokenStream(dotLexer);
        DotParser dotParser = new DotParser(dotStream);
        CommonTree dotTree = null;
        try {
            DotParser.graph_return r = dotParser.graph();
            dotTree = (CommonTree) r.getTree();
        } catch (RecognitionException ex) {
            throw new TransformationException("DOT grammar file could not be parsed");
        }
        if (dotParser.hasErrors()) {
            throw new TransformationException("DOT grammar file contains errors");
        }
        CommonTreeNodeStream dotNodes = new CommonTreeNodeStream(dotTree);
        dotNodes.setTokenStream(dotStream);
        logger.info("Analyzing DOT grammar");
        DotTree dotTreeParser = new DotTree(dotNodes);
        try {
            DotTree.graph_return r = dotTreeParser.graph();
            setGraph(r.graphObj);
        } catch (RecognitionException ex) {
            throw new TransformationException("DOT grammar file couldn't be analysed");
        }
    }

    @Override
    public void execute() throws TransformationException {
        loadDotGraph();
        if (getGraph() == null || getGraph().edges.isEmpty()) {
            throw new TransformationException("Graph from DOT file doesn't have any edges");
        }
        logger.finer("Successfully loaded " + getGraph());
        super.execute();
    }

    /**
     * Returns default parameters
     * @return map of parameters
     */
    @Override
    public Map<String, Object> getDefaultParams() {
        Map<String, Object> params = super.getDefaultParams();
        params.put("stateSetter", "setState");
        params.put("stateSetterArg", "newState");
        params.put("stateGetter", "getState");
        params.put("stateConstants", "STATE_.*");
        params.put("allowedFunctions", "");
        params.put("ignoredFunctions", "");
        return params;
    }
}
