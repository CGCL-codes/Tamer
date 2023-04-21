package org.vfsutils.ftpserver.usermanager;

public class UserProps {

    protected boolean writePermission = true;

    protected int maxIdleTime = 0;

    protected int maxLogin = 1;

    protected int maxLoginPerIp = 1;

    protected int downloadRate = 0;

    protected int uploadRate = 0;

    public boolean isWritePermission() {
        return writePermission;
    }

    public void setWritePermission(boolean writePermission) {
        this.writePermission = writePermission;
    }

    public int getDownloadRate() {
        return downloadRate;
    }

    public void setDownloadRate(int downloadRate) {
        this.downloadRate = downloadRate;
    }

    public int getMaxIdleTime() {
        return maxIdleTime;
    }

    public void setMaxIdleTime(int maxIdleTime) {
        this.maxIdleTime = maxIdleTime;
    }

    public int getMaxLogin() {
        return maxLogin;
    }

    public void setMaxLogin(int maxLogin) {
        this.maxLogin = maxLogin;
    }

    public int getMaxLoginPerIp() {
        return maxLoginPerIp;
    }

    public void setMaxLoginPerIp(int maxLoginPerIp) {
        this.maxLoginPerIp = maxLoginPerIp;
    }

    public int getUploadRate() {
        return uploadRate;
    }

    public void setUploadRate(int uploadRate) {
        this.uploadRate = uploadRate;
    }
}
