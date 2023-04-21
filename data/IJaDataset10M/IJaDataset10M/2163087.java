package org.odiem.api;

public interface OdmStackListener {

    public void onConnectionClose(String ldapUrl, String username, String cause);

    public void onConnectionOpen(String ldapUrl, String username);
}
