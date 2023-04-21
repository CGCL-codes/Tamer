package org.frameworkset.spi.security;

/**
 * <p>Title: SecurityManager.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-2-5 ����04:32:22
 * @author biaoping.yin
 * @version 1.0
 */
public interface SecurityManager {

    public static final String USER_ACCOUNT_KEY = "user";

    public static final String USER_PASSWORD_KEY = "password";

    public boolean checkUser(SecurityContext context) throws Exception;

    /**
     * ���ܷ���
     * @param value
     * @return
     */
    public byte[] encode(byte[] value) throws Exception;

    /**
     * ���ܷ���
     * @param value
     * @return
     */
    public byte[] decode(byte[] value) throws Exception;

    public boolean checkPermission(SecurityContext context) throws Exception;

    public boolean enableEncrypt();

    public boolean enableAuthenticate();

    public boolean enableAuthority();
}
