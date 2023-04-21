package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.DefaultMessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageSource;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter.MESSAGE_TYPE;

/**
 * �������ϐ��g�p��ۑ����邽�߂̃N���X
 * 
 * @author t-miyake, higo
 * @param <T> �����ς݂̌^
 */
public abstract class UnresolvedVariableUsageInfo<T extends VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> extends UnresolvedExpressionInfo<T> {

    /**
     * �K�v�ȏ���^���āC�I�u�W�F�N�g������
     * 
     * @param usedVariableName �ϐ���
     * @param reference �Q�Ƃł��邩�ǂ���
     * @param assignment ���ł��邩�ǂ���
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public UnresolvedVariableUsageInfo(final String usedVariableName, final boolean reference, final boolean assignment, final UnresolvedUnitInfo<? extends UnitInfo> outerUnit, final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        if (null == usedVariableName) {
            throw new IllegalArgumentException("usedVarialbeName is null");
        }
        this.usedVariableName = usedVariableName;
        this.reference = reference;
        this.assignment = assignment;
        this.setOuterUnit(outerUnit);
        this.setFromLine(fromLine);
        this.setFromColumn(fromColumn);
        this.setToLine(toLine);
        this.setToColumn(toColumn);
    }

    /**
     * ���̕ϐ��g�p���Q�Ƃł��邩�ǂ�����Ԃ�
     * 
     * @return �Q�Ƃł���ꍇ�� true�C���ł���ꍇ�� false
     */
    public final boolean isReference() {
        return this.reference;
    }

    /**
     * ���̕ϐ��g�p�����ł��邩�ǂ�����Ԃ�
     * 
     * @return ���ł���ꍇ�� true�C�Q�Ƃł���ꍇ�� false
     */
    public final boolean isAssignment() {
        return this.assignment;
    }

    /**
     * �g�p����Ă���ϐ��̖��O��Ԃ�
     * @return �g�p����Ă���ϐ��̖��O
     */
    public String getUsedVariableName() {
        return this.usedVariableName;
    }

    /**
     * �g�p����Ă���ϐ��̖��O��ۑ�����ϐ�
     */
    protected final String usedVariableName;

    private boolean reference;

    private boolean assignment;

    /**
     * �G���[���b�Z�[�W�o�͗p�̃v�����^
     */
    protected static final MessagePrinter err = new DefaultMessagePrinter(new MessageSource() {

        public String getMessageSourceName() {
            return "UnresolvedVariableUsage";
        }
    }, MESSAGE_TYPE.ERROR);
}
