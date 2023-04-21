package net.sf.brightside.mobilestock.metamodel.api;

public interface Shareholder extends Identifiable {

    String getUserName();

    void setUserName(String userName);

    String getPassword();

    void setPassword(String password);

    String getName();

    void setName(String name);
}
