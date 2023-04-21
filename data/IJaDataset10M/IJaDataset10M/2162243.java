package uk.ac.imperial.ma.metric.computerese.impl1;

import uk.ac.imperial.ma.metric.computerese.*;

/**
 * An implementation of {@link uk.ac.imperial.ma.metric.computerese.IdentifierNode}.
 * 
 * @author <a href="mailto:mail@daniel.may.name">Daniel J. R. May</a>
 * @version 0.1, 9 Jul 2008
 */
public class IdentifierNodeImpl extends NodeImpl implements IdentifierNode {

    /**
	 * Constructs.
	 * 
	 * @param asciiValue how this node should be entered via the keyboard as a computerese string.
	 * @param unicodeValue how this node should be rendered as a computerese string or in an XML Text node value.
	 * @param entityReferenceValue how this node should be rendered as an XML entity reference.
	 * @param precedence the precedence of this node.
	 * @param minNumberOfArguments the minimum number of arguments which this node can have (as children for instance).
	 * @param maxNumberOfArguments the maximum number of arguments which this node can have (as children for instance).
	 * @param parenthesisLevel the parenthesis level of this node within a node sequence.
	 */
    protected IdentifierNodeImpl(final String asciiValue, final String unicodeValue, final String entityReferenceValue, final int parenthesisLevel) {
        super(ComputereseNodeType.IDENTIFIER, asciiValue, unicodeValue, entityReferenceValue, Precedence.IDENTIFIER, NumberOfArguments.ZERO, NumberOfArguments.ZERO, parenthesisLevel);
    }

    /**
	 * Constructs.
	 * 
	 * @param predefinedIdentifier a predefined identifier.
	 * @param parenthesisLevel the parenthesis level of this node within a node sequence.
	 */
    protected IdentifierNodeImpl(final PredefinedIdentifier predefinedIdentifier, final int parenthesisLevel) {
        super(ComputereseNodeType.IDENTIFIER, predefinedIdentifier.getAsciiValue(), predefinedIdentifier.getUnicodeValue(), predefinedIdentifier.getEntityReferenceValue(), Precedence.IDENTIFIER, NumberOfArguments.ZERO, NumberOfArguments.ZERO, parenthesisLevel);
    }

    /**
	 * Constructs.
	 * 
	 * @param asciiValue how this node should be entered via the keyboard as a computerese string.
	 * @param precedence the precedence of this node.
	 * @param minNumberOfArguments the minimum number of arguments which this node can have (as children for instance).
	 * @param maxNumberOfArguments the maximum number of arguments which this node can have (as children for instance).
	 * @param parenthesisLevel the parenthesis level of this node within a node sequence.
	 */
    protected IdentifierNodeImpl(final String asciiValue, final int parenthesisLevel) {
        super(ComputereseNodeType.IDENTIFIER, asciiValue, asciiValue, null, Precedence.IDENTIFIER, NumberOfArguments.ZERO, NumberOfArguments.ZERO, parenthesisLevel);
    }
}
