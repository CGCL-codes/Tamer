package net.sf.elbe.core.model;

public interface ICredentials {

    public ConnectionParameter getConnectionParameter();

    public String getBindDN();

    public String getBindPassword();
}
