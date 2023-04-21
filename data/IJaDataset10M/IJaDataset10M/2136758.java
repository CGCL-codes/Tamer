package jp.ac.osaka_u.ist.sel.metricstool.main.io;

/**
 * ���b�Z�[�W�v�����^�̃f�t�H���g����
 * 
 * @author kou-tngt
 *
 */
public class DefaultMessagePrinter implements MessagePrinter {

    /**
     * �R���X�g���N�^
     * @param source ���b�Z�[�W���M��
     * @param type ���b�Z�[�W�^�C�v
     */
    public DefaultMessagePrinter(final MessageSource source, final MESSAGE_TYPE type) {
        this.pool = MessagePool.getInstance(type);
        this.source = source;
    }

    /**
     * ���b�Z�[�W�����̂܂܏o�͂���
     * @param o �o�͂��郁�b�Z�[�W
     */
    public void print(final Object o) {
        this.pool.sendMessage(this.source, String.valueOf(o));
    }

    /**
     * ��s����
     */
    public void println() {
        this.print(LINE_SEPARATOR);
    }

    /**
     * ���b�Z�[�W���o�͂��ĉ�s����
     * @param o �o�͂��郁�b�Z�[�W
     */
    public void println(final Object o) {
        this.print(String.valueOf(o) + LINE_SEPARATOR);
    }

    /**
     * �����s�̃��b�Z�[�W�̊ԂɁC���̃��b�Z�[�W�̊��荞�݂��Ȃ��悤�ɏo�͂���.
     * @param objects �o�͂��郁�b�Z�[�W�̔z��
     */
    public void println(final Object[] objects) {
        final StringBuilder builder = new StringBuilder();
        for (final Object o : objects) {
            builder.append(String.valueOf(o));
            builder.append(LINE_SEPARATOR);
        }
        this.print(builder.toString());
    }

    /**
     * ���b�Z�[�W���M��
     */
    private final MessageSource source;

    /**
     * �Ή����郁�b�Z�[�W�v�[��
     */
    private final MessagePool pool;

    /**
     * �V�X�e���ˑ��̉�s�L��
     */
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
}
