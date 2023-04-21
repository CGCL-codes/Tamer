package org.destecs.script.ast.expressions;

import org.destecs.script.ast.expressions.SSingleExp;
import java.util.Map;
import org.destecs.script.ast.expressions.ESingleExp;
import org.destecs.script.ast.node.INode;
import java.lang.String;
import org.destecs.script.ast.expressions.PExpBase;
import org.destecs.script.ast.expressions.EExp;

/**
* Generated file by AST Creator
* @author Kenneth Lausdahl
*
*/
public abstract class SSingleExpBase extends PExpBase implements SSingleExp {

    private static final long serialVersionUID = 1L;

    /**
	 * Creates a new {@link SSingleExpBase} node with no children.
	 */
    public SSingleExpBase() {
    }

    /**
	 * Essentially this.toString().equals(o.toString()).
	**/
    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof SSingleExpBase) return toString().equals(o.toString());
        return false;
    }

    /**
	 * Removes the {@link INode} {@code child} as a child of this {@link SSingleExpBase} node.
	 * Do not call this method with any graph fields of this node. This will cause any child's
	 * with the same reference to be removed unintentionally or {@link RuntimeException}will be thrown.
	 * @param child the child node to be removed from this {@link SSingleExpBase} node
	 * @throws RuntimeException if {@code child} is not a child of this {@link SSingleExpBase} node
	 */
    public void removeChild(INode child) {
        throw new RuntimeException("Not a child.");
    }

    /**
	 * Creates a deep clone of this {@link SSingleExpBase} node while putting all
	 * old node-new node relations in the map {@code oldToNewMap}.
	 * @param oldToNewMap the map filled with the old node-new node relation
	 * @return a deep clone of this {@link SSingleExpBase} node
	 */
    @Override
    public abstract SSingleExp clone(Map<INode, INode> oldToNewMap);

    /**
	 * Returns a deep clone of this {@link SSingleExpBase} node.
	 * @return a deep clone of this {@link SSingleExpBase} node
	 */
    @Override
    public abstract SSingleExp clone();

    public String toString() {
        return super.toString();
    }

    /**
	 * Returns the {@link ESingleExp} corresponding to the
	 * type of this {@link ESingleExp} node.
	 * @return the {@link ESingleExp} for this node
	 */
    public abstract ESingleExp kindSSingleExp();

    /**
	 * Returns the {@link EExp} corresponding to the
	 * type of this {@link EExp} node.
	 * @return the {@link EExp} for this node
	 */
    @Override
    public EExp kindPExp() {
        return EExp.SINGLE;
    }
}
