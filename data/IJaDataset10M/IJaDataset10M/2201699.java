package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;

import java.util.Set;

/**
 * �������̏����߂̏���\���N���X
 * 
 * @author t-miyake
 *
 */
@SuppressWarnings("serial")
public final class ConditionalClauseInfo extends UnitInfo {

    /**
     * �����߂�ێ�����u���b�N���ƈʒu���
     * @param ownerConditionalBlock �������̏����߂�ێ�����u���b�N
     * @param condition �����߂ɋL�q����Ă������
     * @param fromLine �J�n�s
     * @param fromColumn �J�n�ʒu
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public ConditionalClauseInfo(final ConditionalBlockInfo ownerConditionalBlock, final ConditionInfo condition, final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        super(fromLine, fromColumn, toLine, toColumn);
        if (null == ownerConditionalBlock) {
            throw new IllegalArgumentException();
        }
        this.ownerConditionalBlock = ownerConditionalBlock;
        if (null != condition) {
            this.condition = condition;
            if (this.condition instanceof ExpressionInfo) {
                ((ExpressionInfo) this.condition).setOwnerExecutableElement(this.ownerConditionalBlock);
            }
        } else {
            final CallableUnitInfo ownerMethod = ownerConditionalBlock.getOwnerMethod();
            this.condition = new EmptyExpressionInfo(ownerMethod, toLine, toColumn - 1, toLine, toColumn - 1);
            ((ExpressionInfo) this.condition).setOwnerExecutableElement(this.ownerConditionalBlock);
        }
    }

    /**
     * �����߂�ێ�����u���b�N��Ԃ�
     * @return �����߂�ێ�����u���b�N
     */
    public final ConditionalBlockInfo getOwnerConditionalBlock() {
        return this.ownerConditionalBlock;
    }

    /**
     * �����߂ɋL�q����Ă��������Ԃ�
     * @return �����߂ɋL�q����Ă������
     */
    public final ConditionInfo getCondition() {
        return this.condition;
    }

    /**
     * �����߂̃e�L�X�g�\����Ԃ�
     * 
     * @return �����߂̃e�L�X�g�\��
     */
    public final String getText() {
        return this.getCondition().getText();
    }

    /**
     * ���̏����߂̃n�b�V���R�[�h��Ԃ�
     */
    @Override
    public final int hashCode() {
        return this.getCondition().hashCode();
    }

    /**
     * �����ߓ��ɂ�����ϐ��g�p��Set��Ԃ�
     * 
     * @return �����ߓ��ɂ�����ϐ��g�p��Set 
     */
    @Override
    public final Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages() {
        return this.getCondition().getVariableUsages();
    }

    /**
     * �����߂Œ�`���ꂽ�ϐ���Set��Ԃ�
     * 
     * @return �����߂Œ�`���ꂽ�ϐ���Set
     */
    @Override
    public Set<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
        return this.getCondition().getDefinedVariables();
    }

    /**
     * �����߂ɂ�����Ăяo����Set��Ԃ�
     * 
     * @return ����s�ɂ�����Ăяo����Set
     */
    @Override
    public Set<CallInfo<? extends CallableUnitInfo>> getCalls() {
        return this.getCondition().getCalls();
    }

    public ConditionalClauseInfo copy() {
        final ConditionalBlockInfo ownerConditionalBlock = this.getOwnerConditionalBlock();
        final ConditionInfo condition = (ConditionInfo) this.getCondition().copy();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();
        final ConditionalClauseInfo newConditionalClause = new ConditionalClauseInfo(ownerConditionalBlock, condition, fromLine, fromColumn, toLine, toColumn);
        return newConditionalClause;
    }

    /**
     * �����߂�ێ�����u���b�N��\���ϐ�
     */
    private final ConditionalBlockInfo ownerConditionalBlock;

    /**
     * �����߂ɋL�q����Ă��������\���ϐ�
     */
    private final ConditionInfo condition;
}
