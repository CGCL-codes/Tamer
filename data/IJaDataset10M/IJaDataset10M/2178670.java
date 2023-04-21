package org.cesar.flip.flipex.ajdt.extractors;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.rewrite.TargetSourceRangeComputer;

/**
 * The class that calculates the target source range
 * 
 * @author Fernando Calheiros (fernando.calheiros@cesar.org.br)
 * 
 */
public class AJDTTargetSourceRangeComputer extends TargetSourceRangeComputer {

    /**
	 * 
	 * @param node
	 *            The node that will be computed.
	 * @return the source range of the given node.
	 */
    public SourceRange computeSourceRange(ASTNode node) {
        return new SourceRange(node.getStartPosition(), node.getLength());
    }
}
