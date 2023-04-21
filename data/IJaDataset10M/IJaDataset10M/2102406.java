package org.apache.harmony.auth.internal.kerberos.v5;

public class KerberosException extends RuntimeException {

    public KerberosException() {
    }

    public KerberosException(String message) {
        super(message);
    }
}
