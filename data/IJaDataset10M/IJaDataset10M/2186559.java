package org.columba.chat.config.api;

public interface IAccount {

    public boolean isEnableSSL();

    public void setEnableSSL(boolean enableSSL);

    public String getHost();

    public void setHost(String host);

    public String getId();

    public void setId(String id);

    public char[] getPassword();

    public void setPassword(char[] password);

    public int getPort();

    public void setPort(int port);

    public String getResource();

    public void setResource(String resource);
}
