package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;

import java.util.LinkedList;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFG;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFGUtility;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReturnStatementInfo;

/**
 * return����\���m�[�h
 * 
 * @author higo
 * 
 */
public class CFGReturnStatementNode extends CFGStatementNode<ReturnStatementInfo> {

    /**
	 * ��������m�[�h�ɑΉ�����return����^���ď���
	 * 
	 * @param returnStatement
	 */
    CFGReturnStatementNode(final ReturnStatementInfo returnStatement) {
        super(returnStatement);
    }

    @Override
    public CFG dissolve(final ICFGNodeFactory nodeFactory) {
        final ReturnStatementInfo statement = this.getCore();
        final ExpressionInfo target = (ExpressionInfo) this.getDissolvingTarget().copy();
        if (!CFGUtility.isDissolved(target)) {
            return null;
        }
        nodeFactory.removeNode(statement);
        this.remove();
        final LocalSpaceInfo ownerSpace = statement.getOwnerSpace();
        final LinkedList<CFGNode<?>> dissolvedNodeList = new LinkedList<CFGNode<?>>();
        final LinkedList<LocalVariableUsageInfo> dissolvedVariableUsageList = new LinkedList<LocalVariableUsageInfo>();
        this.makeDissolvedNode(target, nodeFactory, dissolvedNodeList, dissolvedVariableUsageList);
        final ReturnStatementInfo newStatement = this.makeNewElement(ownerSpace, dissolvedVariableUsageList.getFirst());
        final CFGNode<?> newNode = nodeFactory.makeNormalNode(newStatement);
        dissolvedNodeList.add(newNode);
        this.makeEdges(dissolvedNodeList);
        final CFG newCFG = this.makeCFG(nodeFactory, dissolvedNodeList);
        nodeFactory.addDissolvedNodes(statement, newCFG.getAllNodes());
        return newCFG;
    }

    @Override
    ExpressionInfo getDissolvingTarget() {
        final ReturnStatementInfo statement = this.getCore();
        return statement.getReturnedExpression();
    }

    @Override
    ReturnStatementInfo makeNewElement(final LocalSpaceInfo ownerSpace, final int fromLine, final int fromColumn, final int toLine, final int toColumn, final ExpressionInfo... requiredExpression) {
        if ((null == ownerSpace) || (1 != requiredExpression.length)) {
            throw new IllegalArgumentException();
        }
        final ReturnStatementInfo newStatement = new ReturnStatementInfo(ownerSpace, requiredExpression[0], fromLine, fromColumn, toLine, toColumn);
        return newStatement;
    }

    @Override
    ReturnStatementInfo makeNewElement(final LocalSpaceInfo ownerSpace, final ExpressionInfo... requiredExpression) {
        if ((null == ownerSpace) || (1 != requiredExpression.length)) {
            throw new IllegalArgumentException();
        }
        final ReturnStatementInfo statement = this.getCore();
        final int fromLine = statement.getFromLine();
        final int fromColumn = statement.getFromColumn();
        final int toLine = statement.getToLine();
        final int toColumn = statement.getToColumn();
        return this.makeNewElement(ownerSpace, fromLine, fromColumn, toLine, toColumn, requiredExpression);
    }
}
