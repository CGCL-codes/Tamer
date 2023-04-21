package net.sf.portletunit;

public interface PortletUser {

    public String getUserId();

    public String getFirstName();

    public String getLastName();

    public String[] getRoles();

    public void addRole(String role);
}
