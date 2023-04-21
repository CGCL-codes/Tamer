package de.haumacher.timecollect.server.ui;

import java.security.Principal;
import net.sf.wtk.ui.stdlib.auth.BasicAuthenticationDevice;

public class DummyAuthenticationDevice implements BasicAuthenticationDevice {

    @Override
    public Principal checkAuthentication(String userName, String password) {
        throw new UnsupportedOperationException();
    }
}
