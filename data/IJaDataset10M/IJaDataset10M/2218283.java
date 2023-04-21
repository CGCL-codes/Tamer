package br.usp.pcs.lahpc.ogsadai.common.jdbc.security.gsi;

import org.globus.axis.util.Util;
import br.usp.pcs.lahpc.ogsadai.common.jdbc.security.Security;

public class GSISecurity implements Security {

    static {
        Util.registerTransport();
    }

    public void initialize() throws SecurityException {
    }

    public String getSecurityType() {
        return GSI_SECURITY;
    }
}
