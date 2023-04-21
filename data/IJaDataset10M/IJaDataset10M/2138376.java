package net.sf.parser4j.kernelgenerator.entity.grammarnode.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sf.parser4j.kernelgenerator.entity.grammarnode.AbstractMularyGrammarNode;
import net.sf.parser4j.kernelgenerator.entity.grammarnode.IGrammarNode;
import net.sf.parser4j.parser.entity.EnumNodeType;
import net.sf.parser4j.parser.entity.EnumSource;

/**
 * common to all non terminal node based on concatenation<br>
 * 
 * @author luc peuvrier
 * 
 */
public abstract class AbstractGrammarNodeConcat extends AbstractMularyGrammarNode {

    public AbstractGrammarNodeConcat(final EnumNodeType grammarNodeType, final EnumSource nodeSource) {
        super(grammarNodeType, nodeSource);
    }

    @Override
    public boolean isEmptyNonTerminal() {
        if (emptyNonTerminal == null) {
            final Iterator<OperandGrammarNode> iterator = operandList.iterator();
            emptyNonTerminal = true;
            while (emptyNonTerminal && iterator.hasNext()) {
                final OperandGrammarNode operandNode = iterator.next();
                final IGrammarNode grammarNode = operandNode.getGrammarNode();
                if (!grammarNode.isEmptyNonTerminal()) {
                    emptyNonTerminal = false;
                }
            }
        }
        return emptyNonTerminal;
    }

    public boolean match(final int recognitionPointIndex) {
        assertOperandListSetted();
        final boolean match;
        if (recognitionPointIndex < 0 || recognitionPointIndex > operandList.size()) {
            badRecognitionPointIndex(recognitionPointIndex);
            match = false;
        } else if (recognitionPointIndex == operandList.size()) {
            match = true;
        } else {
            match = false;
        }
        return match;
    }

    public List<OperandGrammarNode> expectedNonTerminal(final int recognitionPointIndex) {
        assertOperandListSetted();
        final List<OperandGrammarNode> expectedNonTerminal;
        if (recognitionPointIndex < 0 || recognitionPointIndex > operandList.size()) {
            badRecognitionPointIndex(recognitionPointIndex);
            expectedNonTerminal = null;
        } else if (recognitionPointIndex == operandList.size()) {
            expectedNonTerminal = EMPTY_LIST;
        } else {
            expectedNonTerminal = new ArrayList<OperandGrammarNode>(1);
            expectedNonTerminal.add(operandList.get(recognitionPointIndex));
        }
        return expectedNonTerminal;
    }

    @Override
    public String toDefinitionString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(nonTerminalToString());
        builder.append(" :(concat)");
        int index = 0;
        if (operandList == null) {
            builder.append(" <no sons: bad state error>");
        } else {
            for (OperandGrammarNode grammarNode : operandList) {
                final String name = grammarNode.getNonTerminalName();
                builder.append(' ');
                if (name == null) {
                    builder.append("<null name: bad state error>");
                } else {
                    builder.append(name);
                    if (grammarNode.isWhiteSpace()) {
                        builder.append(" (as white space)");
                    }
                }
                if (index < operandList.size() - 1) {
                    builder.append(" &");
                }
                index++;
            }
        }
        builder.append(matchClassToString());
        return builder.toString();
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(super.toString());
        builder.append(" :");
        int index = 0;
        if (operandList == null) {
            builder.append(" <no sons: bad state error>");
        } else {
            for (OperandGrammarNode grammarNode : operandList) {
                builder.append(" NT #");
                builder.append(grammarNode.getNonTerminalIdentifier());
                final String name = grammarNode.getNonTerminalName();
                builder.append(' ');
                if (name == null) {
                    builder.append("<null name: bad state error>");
                } else {
                    builder.append(name);
                    if (grammarNode.isWhiteSpace()) {
                        builder.append(" (as white space)");
                    }
                }
                if (index < operandList.size() - 1) {
                    builder.append(" &");
                }
                index++;
            }
        }
        builder.append(matchClassToString());
        return builder.toString();
    }
}
