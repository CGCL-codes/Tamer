package org.sd_network.db;

/**
 * �f�[�^�x�[�X�R�l�N�V�����������ł��Ȃ������ꍇ�ɃX���[������s����O�ł��B
 *
 * <p> $Id$
 *
 * @author Masatoshi Sato
 */
public class ConnectTimeoutException extends RuntimeException {

    /**
     * �f�t�H���g�R���X�g���N�^�ł��B
     */
    public ConnectTimeoutException() {
        super();
    }

    /**
     * �w�肳�ꂽ���b�Z�[�W�ŃC���X�^���X�𐶐����܂��B
     *
     * @param message   ��O���b�Z�[�W�B
     */
    public ConnectTimeoutException(String message) {
        super(message);
    }

    /**
     * ���̗�O���������������ƂȂ�����O�ŃC���X�^���X�𐶐����܂��B
     *
     * @param exception �����ƂȂ�����O�̃C���X�^���X�B
     */
    public ConnectTimeoutException(Exception exception) {
        super(exception);
    }

    /**
     * �w�肳�ꂽ���b�Z�[�W�ƌ����ƂȂ�����O�ŃC���X�^���X�𐶐����܂��B
     *
     * @param message   ��O���b�Z�[�W�B
     * @param exception �����ƂȂ�����O�̃C���X�^���X�B
     */
    public ConnectTimeoutException(String message, Exception exception) {
        super(message, exception);
    }
}
