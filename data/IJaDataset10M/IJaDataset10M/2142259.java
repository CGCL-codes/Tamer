package com.novse.segmentation.core.io;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author Mac Kwan �ֽ�������Դ
 */
public class ByteArrayResource implements Resource {

    /**
     * �ֽ�����
     */
    private byte[] byteArray = null;

    /**
     * ���ֽ�����Ϊ����Ĺ��캯��
     * 
     * @param byteArray
     *            �ֽ�����
     */
    public ByteArrayResource(byte[] byteArray) {
        this.byteArray = byteArray;
    }

    /**
     * ����ָ���ֽ������������
     */
    public InputStream getInputStream() throws Exception {
        return new ByteArrayInputStream(this.byteArray);
    }
}
