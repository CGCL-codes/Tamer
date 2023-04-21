package org.xmldap.sts.db;

import java.util.Iterator;
import java.util.List;

public class SupportedClaims {

    protected static DbSupportedClaim[] dbSupportedClaims;

    public static SupportedClaims getInstance(String name) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        Class<?> clasz = Class.forName(name);
        if (clasz != null) {
            SupportedClaims supportedClaims = (SupportedClaims) clasz.newInstance();
            return supportedClaims;
        } else {
            return null;
        }
    }

    public DbSupportedClaim getClaimByUri(String uri) {
        return null;
    }

    public List<DbSupportedClaim> dbSupportedClaims() {
        return null;
    }

    public Iterator<DbSupportedClaim> iterator() {
        return null;
    }
}
