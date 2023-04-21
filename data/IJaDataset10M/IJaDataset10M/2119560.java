package edu.mit.lcs.haystack.server.extensions.wrapperinduction;

import org.w3c.dom.NodeList;
import edu.mit.lcs.haystack.Constants;
import edu.mit.lcs.haystack.rdf.IRDFContainer;
import edu.mit.lcs.haystack.rdf.Literal;
import edu.mit.lcs.haystack.rdf.RDFException;
import edu.mit.lcs.haystack.rdf.Resource;
import edu.mit.lcs.haystack.rdf.Statement;
import edu.mit.lcs.haystack.rdf.Utilities;
import edu.mit.lcs.haystack.server.extensions.wrapperinduction.dom.INode;

/**
 *  The standard, non-range pattern matcher
 */
public class StandardMatcher extends Matcher {

    protected PatternNode patternNode;

    public StandardMatcher(PatternNode patternNode) {
        this.patternNode = patternNode;
    }

    /**
     *  Returns all matches in children of the given element.
     */
    public PatternResult match(INode parent, Resource semanticClassRes) {
        if (parent == null || !this.patternNode.equals(parent)) {
            return new PatternResult(false);
        }
        if (this.patternNode.hasSemanticResource(WrapperManager.PATTERN_SEMANTIC_ROOT_PROP)) {
            semanticClassRes = Utilities.generateUniqueResource();
        }
        PatternResult childrenResult = matchHelper(0, parent.getChildNodes(), 0, (PatternNode[]) this.patternNode.children.toArray(new PatternNode[0]), semanticClassRes);
        if (childrenResult.isMatch()) {
            if (this.patternNode.isSemantic()) {
                childrenResult.add(semanticClassRes, this.patternNode.getSemanticResources(), parent);
            }
        }
        return childrenResult;
    }

    protected PatternResult matchHelper(int toMatchIndex, NodeList toMatch, int patternIndex, PatternNode[] patternNodes, Resource semanticClassRes) {
        if (patternNodes.length == 0) {
            return new PatternResult(true);
        }
        if (toMatch.getLength() == 0 && patternNodes.length == 1 && patternNodes[0].isWildcard()) {
            return new PatternResult(true);
        }
        PatternResult allResults = new PatternResult(false);
        for (int i = toMatchIndex; i < toMatch.getLength(); i++) {
            PatternResult thisResult = patternNodes[patternIndex].match((INode) toMatch.item(i), semanticClassRes);
            if (thisResult.isMatch()) {
                if (patternIndex == patternNodes.length - 1) {
                    allResults.merge(thisResult);
                } else {
                    PatternResult rest = matchHelper(i + 1, toMatch, patternIndex + 1, patternNodes, semanticClassRes);
                    if (rest.isMatch()) {
                        allResults.merge(thisResult.merge(rest));
                    }
                }
            }
            if (patternIndex < patternNodes.length - 1 && patternNodes[patternIndex].isWildcard()) {
                PatternResult rest = matchHelper(i, toMatch, patternIndex + 1, patternNodes, semanticClassRes);
                if (rest.isMatch()) {
                    allResults.merge(rest);
                }
            }
        }
        return allResults;
    }

    /**
     *  Creates an RDF resource representing this matcher.
     */
    public Resource makeResource(IRDFContainer rdfc) throws RDFException {
        Resource matcherRes = Utilities.generateUniqueResource();
        rdfc.add(new Statement(matcherRes, Constants.s_rdf_type, WrapperManager.MATCHER_CLASS));
        rdfc.add(new Statement(matcherRes, Constants.s_rdf_type, WrapperManager.STANDARD_MATCHER_CLASS));
        rdfc.add(new Statement(matcherRes, WrapperManager.MATCHER_JAVA_CLASS_PROP, new Literal(this.getClass().getName())));
        return matcherRes;
    }

    public static Matcher fromResource(PatternNode patternNode, Resource matcherRes, IRDFContainer rdfc) throws RDFException {
        return new StandardMatcher(patternNode);
    }
}
