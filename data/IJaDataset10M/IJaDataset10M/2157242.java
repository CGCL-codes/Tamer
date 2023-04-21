package com.vin.scriptutils.utils.ssh.jsch;

import com.vin.scriptutils.utils.ssh.SshTask;
import com.vin.scriptutils.utils.ssh.SshTaskExec;
import com.vin.scriptutils.utils.ssh.SshTaskResult;
import com.vin.scriptutils.utils.ssh.exception.SshTaskExecException;
import java.io.IOException;

/**
 *
 * @author igor
 */
public class JschSshExec implements SshTaskExec {

    public SshTaskResult execute(SshTask task) throws SshTaskExecException, InterruptedException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
