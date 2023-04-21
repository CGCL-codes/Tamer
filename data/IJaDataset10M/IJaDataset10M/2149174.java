package freja.di;

/**
 * Context �X�R�[�v�̃R���|�[�l���g���p����R���e�L�X�g����\���܂��B
 * @author SiroKuro
 */
public interface Context {

    /**
	 * ���̃R���e�L�X�g����A�w��̃R���|�[�l���g�������I�u�W�F�N�g���擾���܂��B
	 * �R���|�[�l���g��������Ȃ��ꍇ�ȂǁA�K�v�ɉ����ăR���|�[�l���g�̐������s���܂��B
	 * @param componentName �R���|�[�l���g��
	 * @param factory �R���|�[�l���g�̐����ɗp���� ComponentFactory �I�u�W�F�N�g
	 * @return �R���|�[�l���g�I�u�W�F�N�g
	 */
    public Object getComponent(String componentName, ComponentFactory factory);

    /**
	 * ���̃R���e�L�X�g�̖��O��Ԃ��܂��B
	 * @return �R���e�L�X�g��
	 */
    public String getName();
}
