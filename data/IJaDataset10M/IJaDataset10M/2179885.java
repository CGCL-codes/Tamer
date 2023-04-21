package com.dukesoftware.utils.jsch;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.UserInfo;

public class ScpToolUserAuthPassword implements IScpTool {

    private final String user;

    private final String host;

    private final String password;

    public ScpToolUserAuthPassword(String userName, String password, String remoteHost) {
        this.user = userName;
        this.host = remoteHost;
        this.password = password;
    }

    public boolean scpString(String contents, String destFilePath) {
        return ScpToolHelper.scpStringTo(new JSch(), new MyUserInfo(), user, host, contents, destFilePath);
    }

    private final class MyUserInfo implements UserInfo {

        private int count;

        public MyUserInfo() {
            this.count = 0;
        }

        public String getPassword() {
            count++;
            if (count > 1) {
                return null;
            }
            return password;
        }

        public boolean promptYesNo(String str) {
            return true;
        }

        public String getPassphrase() {
            return null;
        }

        public boolean promptPassphrase(String message) {
            return true;
        }

        public boolean promptPassword(String message) {
            return true;
        }

        public void showMessage(String message) {
        }
    }

    @Override
    public boolean scpFile(String sourceFilePath, String destFilePath) {
        return ScpToolHelper.scpStringTo(new JSch(), new MyUserInfo(), user, host, sourceFilePath, destFilePath);
    }
}
