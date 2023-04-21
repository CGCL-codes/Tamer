package jp.eisbahn.eclipse.plugins.lingrclipse;

import java.util.List;

/**
 * Lingr���[�����Ď��������ʁC�T�[�o���瓾��ꂽ���b�Z�[�W�Ɋւ��鏈�����K�肵���C���^�t�F�[�X�ł��B
 * @author Yoichiro Tanaka
 * @since 1.0.0
 */
public interface LingrObservedMessage extends LingrObject {

    /**
	 * �J�E���^�̒l��Ԃ��܂��B
	 * �T�[�o����̃��X�|���X��counter�������܂܂�Ȃ������ꍇ�̒l��-1��Ԃ��܂��B
	 * @return �J�E���^�l
	 */
    public int getCounter();

    public List<LingrOccupant> getLingrOccupants();

    public List<LingrMessage> getLingrMessages();
}
