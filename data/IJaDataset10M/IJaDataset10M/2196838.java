package org.drftpd.event;

import org.drftpd.usermanager.User;
import org.drftpd.vfs.DirectoryHandle;

/**
 * @author mog
 * 
 * @version $Id: DirectoryFtpEvent.java 1621 2007-02-13 20:41:31Z djb61 $
 */
public class DirectoryFtpEvent extends ConnectionEvent {

    private DirectoryHandle directory;

    public DirectoryFtpEvent(User user, String command, DirectoryHandle directory) {
        this(user, command, directory, System.currentTimeMillis());
    }

    public DirectoryFtpEvent(User user, String command, DirectoryHandle directory, long time) {
        super(user, command, time);
        this.directory = directory;
    }

    public DirectoryHandle getDirectory() {
        return directory;
    }

    public String toString() {
        return getClass().getName() + "[user=" + getUser() + ",cmd=" + getCommand() + ",directory=" + directory.getPath() + "]";
    }
}
