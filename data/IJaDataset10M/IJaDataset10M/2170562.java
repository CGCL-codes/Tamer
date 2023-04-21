package jp.sourceforge.medaka;

/**
 * �G���[
 * @author G.Sukigara
 */
public class MessageException extends Exception {

    /**
     * <code>serialVersionUID</code> �̃R�����g
     */
    private static final long serialVersionUID = 1L;

    /**
     * �R���X�g���N�^
     * @param msg ���b�Z�[�W
     */
    public MessageException(String msg) {
        super(msg);
    }

    /**
     * �R���X�g���N�^
     * @param msg ���b�Z�[�W
     * @param th �����ƂȂ����G���[
     */
    public MessageException(String msg, Throwable th) {
        super(msg, th);
    }
}
