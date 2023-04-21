package org.drftpd.master.config;

import java.util.List;
import java.util.Properties;
import org.drftpd.dynamicdata.Key;
import org.drftpd.dynamicdata.KeyedMap;
import org.drftpd.permissions.PathPermission;
import org.drftpd.permissions.Permission;
import org.drftpd.usermanager.User;
import org.drftpd.util.PortRange;
import org.drftpd.vfs.DirectoryHandle;
import org.drftpd.vfs.perms.VFSPermissions;

/**
 * @author mog
 * @author fr0w
 * @version $Id: ConfigInterface.java 1819 2007-10-21 15:07:30Z fr0w $
 */
public interface ConfigInterface {

    public KeyedMap<Key, Object> getKeyedMap();

    public Properties getMainProperties();

    public VFSPermissions getVFSPermissions();

    public void reload();

    public boolean checkPathPermission(String directive, User fromUser, DirectoryHandle path);

    public boolean checkPathPermission(String directive, User fromUser, DirectoryHandle path, boolean defaults);

    public boolean checkPermission(String directive, User user);

    public void addPathPermission(String directive, PathPermission permission);

    public void addPermission(String directive, Permission permission);

    public List getBouncerIps();

    public boolean getHideIps();

    public String getLoginPrompt();

    public int getMaxUsersExempt();

    public int getMaxUsersTotal();

    public boolean isLoginAllowed(User user);

    public PortRange getPortRange();

    public String getPasvAddress() throws NullPointerException;

    public String[] getCipherSuites();
}
