package com.btmatthews.ldapunit.impl;

import java.util.Enumeration;
import netscape.ldap.LDAPAttributeSet;
import netscape.ldap.LDAPConnection;
import netscape.ldap.LDAPEntry;
import netscape.ldap.LDAPException;
import netscape.ldap.LDAPSearchResults;
import com.btmatthews.ldapunit.ILDAPDataSet;
import com.btmatthews.ldapunit.ILDAPDataSource;

public class LDAPQueryDataSource implements ILDAPDataSource {

    private int version;

    private String host;

    private int port;

    private String authDn;

    private String passwd;

    private String baseDn;

    private String filter;

    private int scope;

    public ILDAPDataSet load() {
        final LDAPConnection connection = new LDAPConnection();
        try {
            connection.connect(this.version, this.host, this.port, this.authDn, this.passwd);
            try {
                final LDAPSearchResults results = connection.search(this.baseDn, this.scope, this.filter, null, false);
                while (results.hasMoreElements()) {
                    final LDAPEntry entry = results.next();
                    final LDAPAttributeSet attributeSet = entry.getAttributeSet();
                    final Enumeration attributeEnum = attributeSet.getAttributes();
                }
            } finally {
                connection.disconnect();
            }
        } catch (LDAPException e) {
        }
        return null;
    }
}
