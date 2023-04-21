package org.destecs.script.ast.expressions.binop;

import org.destecs.script.ast.analysis.intf.IAnalysis;
import java.util.Map;
import org.destecs.script.ast.analysis.intf.IQuestion;
import org.destecs.script.ast.node.INode;
import java.lang.String;
import org.destecs.script.ast.expressions.binop.AOrBinop;
import org.destecs.script.ast.analysis.intf.IAnswer;
import org.destecs.script.ast.expressions.binop.EBinop;
import org.destecs.script.ast.analysis.intf.IQuestionAnswer;
import org.destecs.script.ast.expressions.binop.PBinopBase;

/**
* Generated file by AST Creator
* @author Kenneth Lausdahl
*
*/
public class AOrBinop extends PBinopBase {

    private static final long serialVersionUID = 1L;

    /**
	 * Creates a new {@link AOrBinop} node with no children.
	 */
    public AOrBinop() {
    }

    /**
	 * Essentially this.toString().equals(o.toString()).
	**/
    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof AOrBinop) return toString().equals(o.toString());
        return false;
    }

    /**
	 * Removes the {@link INode} {@code child} as a child of this {@link AOrBinop} node.
	 * Do not call this method with any graph fields of this node. This will cause any child's
	 * with the same reference to be removed unintentionally or {@link RuntimeException}will be thrown.
	 * @param child the child node to be removed from this {@link AOrBinop} node
	 * @throws RuntimeException if {@code child} is not a child of this {@link AOrBinop} node
	 */
    public void removeChild(INode child) {
        throw new RuntimeException("Not a child.");
    }

    /**
	 * Creates a deep clone of this {@link AOrBinop} node while putting all
	 * old node-new node relations in the map {@code oldToNewMap}.
	 * @param oldToNewMap the map filled with the old node-new node relation
	 * @return a deep clone of this {@link AOrBinop} node
	 */
    public AOrBinop clone(Map<INode, INode> oldToNewMap) {
        AOrBinop node = new AOrBinop();
        oldToNewMap.put(this, node);
        return node;
    }

    /**
	 * Returns the {@link EBinop} corresponding to the
	 * type of this {@link EBinop} node.
	 * @return the {@link EBinop} for this node
	 */
    @Override
    public EBinop kindPBinop() {
        return EBinop.OR;
    }

    public String toString() {
        return super.toString();
    }

    /**
	 * Returns a deep clone of this {@link AOrBinop} node.
	 * @return a deep clone of this {@link AOrBinop} node
	 */
    public AOrBinop clone() {
        return new AOrBinop();
    }

    /**
	* Calls the {@link IAnalysis#caseAOrBinop(AOrBinop)} of the {@link IAnalysis} {@code analysis}.
	* @param analysis the {@link IAnalysis} to which this {@link AOrBinop} node is applied
	*/
    @Override
    public void apply(IAnalysis analysis) {
        analysis.caseAOrBinop(this);
    }

    /**
	* Calls the {@link IAnswer#caseAOrBinop(AOrBinop)} of the {@link IAnswer} {@code caller}.
	* @param caller the {@link IAnswer} to which this {@link AOrBinop} node is applied
	*/
    @Override
    public <A> A apply(IAnswer<A> caller) {
        return caller.caseAOrBinop(this);
    }

    /**
	* Calls the {@link IQuestion#caseAOrBinop(AOrBinop, Object)} of the {@link IQuestion} {@code caller}.
	* @param caller the {@link IQuestion} to which this {@link AOrBinop} node is applied
	* @param question the question provided to {@code caller}
	*/
    @Override
    public <Q> void apply(IQuestion<Q> caller, Q question) {
        caller.caseAOrBinop(this, question);
    }

    /**
	* Calls the {@link IQuestionAnswer#caseAOrBinop(AOrBinop, Object)} of the {@link IQuestionAnswer} {@code caller}.
	* @param caller the {@link IQuestionAnswer} to which this {@link AOrBinop} node is applied
	* @param question the question provided to {@code caller}
	*/
    @Override
    public <Q, A> A apply(IQuestionAnswer<Q, A> caller, Q question) {
        return caller.caseAOrBinop(this, question);
    }
}
