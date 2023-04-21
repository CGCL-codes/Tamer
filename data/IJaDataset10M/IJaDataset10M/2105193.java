package com.mindbright.application;

import com.mindbright.sshcommon.SSHFileTransferDialog;
import com.mindbright.ssh2.SSH2SFTPFactory;
import com.mindbright.ssh2.SSH2SFTPFileBrowser;
import com.isnetworks.ssh.LocalFileBrowser;

public class ModuleSFTP implements MindTermModule {

    public void init(MindTermApp mindterm) {
    }

    public void activate(MindTermApp mindterm) {
        String title = mindterm.getAppName() + " - SFTP (" + mindterm.getHost() + ")";
        SSHFileTransferDialog dialog = new SSHFileTransferDialog(title, mindterm, new SSH2SFTPFactory());
        String lcwd = mindterm.getProperty("module.sftp.cwd-local");
        String rcwd = mindterm.getProperty("module.sftp.cwd-remote");
        if (rcwd == null) {
            rcwd = ".";
        }
        try {
            if (lcwd == null) {
                lcwd = System.getProperty("user.home");
                if (lcwd == null) lcwd = System.getProperty("user.dir");
                if (lcwd == null) lcwd = System.getProperty("java.home");
            }
        } catch (Throwable t) {
        }
        dialog.setLocalFileBrowser(new LocalFileBrowser(dialog.getLocalFileDisplay(), lcwd));
        dialog.setRemoteFileBrowser(new SSH2SFTPFileBrowser(mindterm.getConnection(), dialog.getRemoteFileDisplay(), rcwd));
        dialog.show();
    }

    public boolean isAvailable(MindTermApp mindterm) {
        return (mindterm.isConnected() && (mindterm.getConnection() != null));
    }

    public void connected(MindTermApp mindterm) {
    }

    public void disconnected(MindTermApp mindterm) {
    }

    public String description(MindTermApp mindterm) {
        return null;
    }
}
