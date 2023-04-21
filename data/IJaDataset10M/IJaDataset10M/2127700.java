package net.sf.opensftp.impl;

import java.util.Hashtable;
import net.sf.opensftp.SftpFile;
import net.sf.opensftp.SftpUtilFactory;
import com.jcraft.jsch.ChannelSftp;

/**
 * 
 * @author BurningXFlame@gmail.com
 * 
 */
public class SftpSessionImpl implements net.sf.opensftp.SftpSession {

    private ChannelSftp channelSftp = null;

    private boolean dirChanged = true;

    private String host;

    private String user;

    private String serverDateTimePattern;

    private String currentPath;

    private Hashtable extras = new Hashtable();

    public SftpSessionImpl(ChannelSftp channelSftp) {
        this.channelSftp = channelSftp;
    }

    public ChannelSftp getChannelSftp() {
        return channelSftp;
    }

    public void setChannelSftp(ChannelSftp channelSftp) {
        this.channelSftp = channelSftp;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Deprecated
    public String getServerDateTimePattern() {
        return serverDateTimePattern;
    }

    @Deprecated
    public void setServerDateTimePattern(String serverDateTimePattern) {
        this.serverDateTimePattern = serverDateTimePattern;
    }

    public boolean getDirChanged() {
        return dirChanged;
    }

    public void setDirChanged(boolean dirChanged) {
        this.dirChanged = dirChanged;
    }

    public void setCurrentPath(String currentPath) {
        this.currentPath = currentPath;
    }

    public String getCurrentPath() {
        if (currentPath == null) {
            dirChanged = true;
        }
        if (dirChanged) {
            currentPath = (String) SftpUtilFactory.getSftpUtil().pwd(this).getExtension();
        }
        return currentPath;
    }

    public Hashtable getExtras() {
        return this.extras;
    }

    public void setExtras(Hashtable bundle) {
        this.extras = bundle;
    }
}
