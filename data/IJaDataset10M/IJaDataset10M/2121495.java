package com.cbsgmbh.xi.af.edifact.jca;

import javax.resource.ResourceException;
import javax.resource.cci.ConnectionMetaData;
import javax.resource.spi.security.PasswordCredential;
import com.cbsgmbh.xi.af.trace.helpers.BaseTracer;
import com.cbsgmbh.xi.af.trace.helpers.BaseTracerSapImpl;
import com.cbsgmbh.xi.af.trace.helpers.Tracer;
import com.cbsgmbh.xi.af.trace.helpers.TracerCategories;

public class CCIConnectionMetaData implements ConnectionMetaData {

    private static final String VERSION_ID = "$Id://OPI2_EDIFACT_Adapter_Http/com/cbsgmbh/opi2/xi/af/edifact/jca/CCIConnectionMetaData.java#1 $";

    private static final BaseTracer baseTracer = new BaseTracerSapImpl(VERSION_ID, TracerCategories.APP_ADAPTER_HTTP);

    private SPIManagedConnection spiManagedConnection;

    public CCIConnectionMetaData(SPIManagedConnection spiManagedConnection) {
        this.spiManagedConnection = spiManagedConnection;
    }

    public String getEISProductName() throws ResourceException {
        return "OPI2 EDIFACT HTTP Adapter";
    }

    public String getEISProductVersion() throws ResourceException {
        return "1.0";
    }

    public String getUserName() throws ResourceException {
        final Tracer tracer = baseTracer.entering("getUserName()");
        String user = null;
        if (!spiManagedConnection.isDestroyed()) {
            PasswordCredential passwordCredential = spiManagedConnection.getPasswordCredential();
            if (passwordCredential == null) user = passwordCredential.getUserName();
        } else {
            throw new ResourceException("User could not be determined. Reason: Connection not available.");
        }
        tracer.leaving();
        return user;
    }
}
