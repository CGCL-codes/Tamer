package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;

/**
 * switch ���� default �G���g����\���N���X
 * 
 * @author higo
 */
@SuppressWarnings("serial")
public final class DefaultEntryInfo extends CaseEntryInfo {

    /**
     * �Ή����� switch �u���b�N����^���� default �G���g��������
     * 
     * @param ownerSwitchBlock �Ή����� switch�u���b�N
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public DefaultEntryInfo(final SwitchBlockInfo ownerSwitchBlock, int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        super(ownerSwitchBlock, fromLine, fromColumn, toLine, toColumn);
    }

    /**
     * ����default�G���g���̃e�L�X�g�\���iString�^�j��Ԃ�
     * 
     * @return ����default�G���g���̃e�L�X�g�\���iString�^�j
     */
    @Override
    public String getText() {
        return "default:";
    }
}
