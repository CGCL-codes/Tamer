package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.Position;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;

/**
 * �������v���O�������j�b�g��\���N���X
 * 
 * @author higo
 * 
 * @param <T> �����ς݃��j�b�g�̌^
 */
public abstract class UnresolvedUnitInfo<T extends UnitInfo> implements Resolvable<T>, PositionSetting {

    /**
     * �J�n�s���Z�b�g����
     * 
     * @param fromLine �J�n�s
     */
    public final void setFromLine(final int fromLine) {
        if (fromLine < 0) {
            throw new IllegalArgumentException();
        }
        this.fromLine = fromLine;
    }

    /**
     * �J�n����Z�b�g����
     * 
     * @param fromColumn �J�n��
     */
    public final void setFromColumn(final int fromColumn) {
        if (fromColumn < 0) {
            throw new IllegalArgumentException();
        }
        this.fromColumn = fromColumn;
    }

    /**
     * �I���s���Z�b�g����
     * 
     * @param toLine �I���s
     */
    public final void setToLine(final int toLine) {
        if (toLine < 0) {
            throw new IllegalArgumentException();
        }
        this.toLine = toLine;
    }

    /**
     * �I������Z�b�g����
     * 
     * @param toColumn �I����
     */
    public final void setToColumn(final int toColumn) {
        if (toColumn < 0) {
            throw new IllegalArgumentException();
        }
        this.toColumn = toColumn;
    }

    /**
     * �J�n�s��Ԃ�
     * 
     * @return �J�n�s
     */
    public final int getFromLine() {
        return this.fromLine;
    }

    /**
     * �J�n���Ԃ�
     * 
     * @return �J�n��
     */
    public final int getFromColumn() {
        return this.fromColumn;
    }

    /**
     * �I���s��Ԃ�
     * 
     * @return �I���s
     */
    public final int getToLine() {
        return this.toLine;
    }

    /**
     * �I�����Ԃ�
     * 
     * @return �I����
     */
    public final int getToColumn() {
        return this.toColumn;
    }

    /**
     * ���̃��j�b�g�̍s����Ԃ�
     * 
     * @return ���j�b�g�̍s��
     */
    public final int getLOC() {
        return this.getToLine() - this.getFromLine() + 1;
    }

    @Override
    public final boolean alreadyResolved() {
        return null != this.resolvedInfo;
    }

    @Override
    public final T getResolved() {
        if (!this.alreadyResolved()) {
            throw new NotResolvedException();
        }
        return this.resolvedInfo;
    }

    @Override
    public int compareTo(Position o) {
        if (this.getFromLine() < o.getFromLine()) {
            return -1;
        } else if (this.getFromLine() > o.getFromLine()) {
            return 1;
        } else if (this.getFromColumn() < o.getFromColumn()) {
            return -1;
        } else if (this.getFromColumn() > o.getFromColumn()) {
            return 1;
        } else if (this.getToLine() < o.getToLine()) {
            return -1;
        } else if (this.getToLine() > o.getToLine()) {
            return 1;
        } else if (this.getToColumn() < o.getToColumn()) {
            return -1;
        } else if (this.getToColumn() > o.getToColumn()) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * �J�n�s��ۑ����邽�߂̕ϐ�
     */
    private int fromLine;

    /**
     * �J�n���ۑ����邽�߂̕ϐ�
     */
    private int fromColumn;

    /**
     * �I���s��ۑ����邽�߂̕ϐ�
     */
    private int toLine;

    /**
     * �J�n���ۑ����邽�߂̕ϐ�
     */
    private int toColumn;

    /**
     * ���O�������ꂽ�����i�[���邽�߂̕ϐ�
     */
    protected T resolvedInfo;
}
