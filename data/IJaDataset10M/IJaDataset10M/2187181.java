package jp.ac.osaka_u.ist.sel.metricstool.main.plugin.loader;

/**
 * ���̗�O�̓v���O�C���̍\�������L�^����XML�t�@�C���̌`�����AXML�̍\���㐳�����Ȃ��ꍇ��
 * ���̃t�H�[�}�b�g�ɏ]���Ă��Ȃ��ꍇ�C�K�v�ȏ�񂪌����Ă���ꍇ�ɓ�������D
 * 
 * @author kou-tngt
 */
public class IllegalPluginXmlFormatException extends PluginLoadException {

    /**
     * 
     */
    private static final long serialVersionUID = 4168828183801661313L;

    public IllegalPluginXmlFormatException() {
        super();
    }

    public IllegalPluginXmlFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalPluginXmlFormatException(String message) {
        super(message);
    }

    public IllegalPluginXmlFormatException(Throwable cause) {
        super(cause);
    }
}
