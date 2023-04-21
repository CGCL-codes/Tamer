package org.notify4b.util;

/**
 * IdentifierUtil
 * 
 * TODO ע�⣺�򵥵������һ�㷨�ڶ�ε��ú����ջ�������ֵ����������
 *      �ɿ����޸��޸�ΪUUID
 * 
 * @author Sui Dapeng
 *
 */
public class IdentifierUtil {

    private static long sn = 0;

    public static String generateSn() {
        sn++;
        return String.valueOf(sn);
    }
}
