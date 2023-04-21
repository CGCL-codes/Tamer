package net.sf.parser4j.parser.entity.syntaxnode;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import net.sf.parser4j.parser.entity.EnumNodeType;
import net.sf.parser4j.parser.entity.EnumSource;
import net.sf.parser4j.parser.service.ParsingToStringUtil;
import net.sf.parser4j.parser.service.syntaxnode.ISyntaxNodeVisitor;

/**
 * 
 * @author luc peuvrier
 * 
 */
public class SyntaxNode {

    public static int nextIdentifier = 0;

    private final EnumNodeType nodeType;

    private final EnumSource nodeSource;

    private Set<String> nonTerminalNameSet = new TreeSet<String>();

    private final Set<String> matchIdentifierSet = new TreeSet<String>();

    private final boolean terminal;

    private final boolean whiteSpace;

    private final SyntaxNode[] sonSyntaxNode;

    private final int[] notWhiteIndexes;

    private final String value;

    private final SyntaxNode[] alternative;

    private final String fileName;

    private final int beginLineNumber;

    private final int beginColumnNumber;

    private final int endLineNumber;

    private final int endColumnNumber;

    private final int myIdentifier;

    private final ParsingToStringUtil parsingToStringUtil;

    public SyntaxNode(final EnumNodeType nodeType, final EnumSource nodeSource, final Collection<String> nonTerminalNameCol, final Collection<String> matchIdentifierCol, final boolean terminal, final boolean whiteSpace, final SyntaxNode[] sonSyntaxNode, final int[] notWhiteIndexes, final String value, final SyntaxNode[] alternative, final String fileName, final int beginLineNumber, final int beginColumnNumber, final int endLineNumber, final int endColumnNumber, final ParsingToStringUtil parsingToStringUtil) {
        super();
        this.nodeType = nodeType;
        this.nodeSource = nodeSource;
        nonTerminalNameSet.addAll(nonTerminalNameCol);
        if (matchIdentifierCol != null) {
            matchIdentifierSet.addAll(matchIdentifierCol);
        }
        this.terminal = terminal;
        this.whiteSpace = whiteSpace;
        this.sonSyntaxNode = sonSyntaxNode;
        this.notWhiteIndexes = notWhiteIndexes;
        this.value = value;
        this.alternative = alternative;
        this.fileName = fileName;
        this.beginColumnNumber = beginColumnNumber;
        this.beginLineNumber = beginLineNumber;
        this.endColumnNumber = endColumnNumber;
        this.endLineNumber = endLineNumber;
        this.parsingToStringUtil = parsingToStringUtil;
        myIdentifier = nextIdentifier++;
    }

    public EnumNodeType getNodeType() {
        return nodeType;
    }

    public Set<String> getNonTerminalNameSet() {
        return nonTerminalNameSet;
    }

    public void addNonTerminalName(final String nonTerminalName) {
        nonTerminalNameSet.add(nonTerminalName);
    }

    public void addNonTerminalName(final Collection<String> nonTerminalNameCol) {
        nonTerminalNameSet.addAll(nonTerminalNameCol);
    }

    public void addMatchIdentifier(final String matchIdentifier) {
        matchIdentifierSet.add(matchIdentifier);
    }

    public void addMatchIdentifier(final Collection<String> matchIdentifierCol) {
        matchIdentifierSet.addAll(matchIdentifierCol);
    }

    public Set<String> getMatchIdentifierSet() {
        return matchIdentifierSet;
    }

    public boolean isTerminal() {
        return terminal;
    }

    public boolean isNonTerminal() {
        return true ^ terminal;
    }

    public boolean isWhiteSpace() {
        return whiteSpace;
    }

    public boolean isAlternative() {
        return nodeType.isAlternative();
    }

    public boolean isEmpty() {
        return nodeType.isEmpty();
    }

    public boolean isWhiteSpaceIntermediate() {
        return nodeSource.isWhiteSpaceIntermediate();
    }

    public boolean isGrammar() {
        return nodeSource.isGrammar();
    }

    public SyntaxNode[] getSonSyntaxNode() {
        return sonSyntaxNode;
    }

    public int[] getNotWhiteIndexes() {
        return notWhiteIndexes;
    }

    public String getValue() {
        return value;
    }

    public SyntaxNode[] getAlternative() {
        return alternative;
    }

    public String getFileName() {
        return fileName;
    }

    public int getBeginLineNumber() {
        return beginLineNumber;
    }

    public int getBeginColumnNumber() {
        return beginColumnNumber;
    }

    public int getEndLineNumber() {
        return endLineNumber;
    }

    public int getEndColumnNumber() {
        return endColumnNumber;
    }

    public void accept(final ISyntaxNodeVisitor visitor) {
        if (visitor.beginVisit(this)) {
            if (!terminal) {
                visitor.beginAlternativeVisit(this, 0);
                if (sonSyntaxNode != null) {
                    for (SyntaxNode syntaxNode : sonSyntaxNode) {
                        syntaxNode.accept(visitor);
                    }
                }
                visitor.endAlternativeVisit(this, 0);
                if (alternative != null && !visitor.visitFirstAlternativeOnly()) {
                    int alternativeCount = 1;
                    for (SyntaxNode syntaxNode : alternative) {
                        visitor.beginAlternativeVisit(this, alternativeCount);
                        syntaxNode.accept(visitor);
                        visitor.endAlternativeVisit(this, alternativeCount);
                        alternativeCount++;
                    }
                }
            }
            visitor.endVisit(this);
        }
    }

    public int getMyIdentifier() {
        return myIdentifier;
    }

    public int getNumberOfAlternative() {
        return alternative.length;
    }

    public String toString(final boolean visitAgainEnabled, final boolean logLocationInFile) {
        return parsingToStringUtil.syntaxTreeToString(this, visitAgainEnabled, logLocationInFile);
    }

    public String toString(final boolean visitAgainEnabled) {
        return parsingToStringUtil.syntaxTreeToString(this, visitAgainEnabled, true);
    }

    @Override
    public String toString() {
        return parsingToStringUtil.syntaxTreeToString(this, false, false);
    }
}
