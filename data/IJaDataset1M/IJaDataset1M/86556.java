package net.yzwlab.gwtmmd.server;

import java.io.UnsupportedEncodingException;

/**
 * �����񉻂̂��߂̃��[�e�B���e�B�N���X�ł��B
 */
public final class StringUtils {

    /**
	 * �����񉻂��܂��B
	 * 
	 * @param data
	 *            �f�[�^�Bnull�͕s�B
	 * @return ������B
	 * @throws UnsupportedEncodingException
	 *             ���߂ł��Ȃ��G���R�[�h�Ɋւ���G���[�B
	 */
    public static String getString(byte[] data) throws UnsupportedEncodingException {
        if (data == null) {
            throw new IllegalArgumentException();
        }
        int len = 0;
        for (byte elem : data) {
            if (elem == 0) {
                break;
            }
            len++;
        }
        return (new String(data, 0, len, "Shift_JIS"));
    }

    /**
	 * �C���X�^���X���������܂���B
	 */
    private StringUtils() {
        ;
    }
}
