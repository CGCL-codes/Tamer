package com.vin.scriptutils.utils.ssh.jsch;

import com.vin.scriptutils.utils.ssh.SshConnection;
import java.io.IOException;

/**
 *
 * @author igor
 */
public class JschSshConnection extends SshConnection {

    @Override
    public boolean authenticate() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
