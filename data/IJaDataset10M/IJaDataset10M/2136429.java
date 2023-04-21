package com.jspx.security;

/**
 * Created by IntelliJ IDEA.
 * User:chenYuan (mail:cayurain@21cn.com)
 * Date: 2005-1-5
 * Time: 14:49:03
 *
 */
public interface Encrypt {

    byte[] fileDecrypt(String fin) throws Exception;

    boolean fileDecrypt(String fin, String fout) throws Exception;

    byte[] getDecrypt(byte classData[]) throws Exception;

    byte[] getEncrypt(byte[] b) throws Exception;

    boolean fileEncrypt(String fin, String fout) throws Exception;

    byte[] fileEncrypt(String fin) throws Exception;
}
