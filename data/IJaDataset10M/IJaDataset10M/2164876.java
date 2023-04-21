package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;

import java.util.SortedSet;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;

/**
 * else �u���b�N��\���N���X
 * 
 * @author higo
 */
@SuppressWarnings("serial")
public final class ElseBlockInfo extends BlockInfo implements SubsequentialBlockInfo<IfBlockInfo> {

    /**
     * �Ή����� if �u���b�N��^���āCelse �u���b�N��������
     * 
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public ElseBlockInfo(final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        super(fromLine, fromColumn, toLine, toColumn);
    }

    /**
     * ����else���̃e�L�X�g�\���iString�^�j��Ԃ�
     * 
     * @return ����else���̃e�L�X�g�\���iString�^�j
     */
    @Override
    public String getText() {
        final StringBuilder sb = new StringBuilder();
        sb.append("else {");
        sb.append(System.getProperty("line.separator"));
        final SortedSet<StatementInfo> statements = this.getStatements();
        for (final StatementInfo statement : statements) {
            sb.append(statement.getText());
            sb.append(System.getProperty("line.separator"));
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * ���� else �u���b�N�ƑΉ����� if �u���b�N��Ԃ�
     * ���̃��\�b�h�͏����p�~�\��ł���C�g�p�͐�������Ȃ�
     * {@link ElseBlockInfo#getOwnerBlock()} ���g�p���ׂ��ł���D
     * 
     * @return ���� else �u���b�N�ƑΉ����� if �u���b�N
     * @deprecated
     */
    public IfBlockInfo getOwnerIfBlock() {
        return this.ownerIfBlock;
    }

    /**
     * ���� else �u���b�N�ƑΉ����� if �u���b�N��Ԃ�
     * 
     * @return ���� else �u���b�N�ƑΉ����� if �u���b�N
     */
    @Override
    public IfBlockInfo getOwnerBlock() {
        assert null != this.ownerIfBlock : "this.ownerIfBlock must not be null!";
        return this.ownerIfBlock;
    }

    @Override
    public void setOwnerBlock(final IfBlockInfo ownerIfBlock) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == ownerIfBlock) {
            throw new NullPointerException();
        }
        if (null != this.ownerIfBlock) {
            throw new IllegalStateException();
        }
        this.ownerIfBlock = ownerIfBlock;
    }

    @Override
    public ExecutableElementInfo copy() {
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();
        final ElseBlockInfo newElseBlock = new ElseBlockInfo(fromLine, fromColumn, toLine, toColumn);
        final IfBlockInfo ownerIfBlock = this.getOwnerBlock();
        newElseBlock.setOwnerBlock(ownerIfBlock);
        final UnitInfo outerUnit = this.getOuterUnit();
        newElseBlock.setOuterUnit(outerUnit);
        for (final StatementInfo statement : this.getStatementsWithoutSubsequencialBlocks()) {
            newElseBlock.addStatement((StatementInfo) statement.copy());
        }
        return newElseBlock;
    }

    /**
     * ���� else �u���b�N�ƑΉ����� if �u���b�N��ۑ����邽�߂̕ϐ�
     */
    private IfBlockInfo ownerIfBlock;
}
