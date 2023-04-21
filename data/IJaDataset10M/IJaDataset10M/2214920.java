package net.community.chest.svnkit;

import java.io.File;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

/**
 * <P>Copyright 2009 as per GPLv2</P>
 *
 * @author Lyor G.
 * @since Aug 5, 2009 12:14:47 PM
 */
public class SVNAccessor {

    public SVNAccessor() {
        super();
    }

    private boolean _readOnlyAccess;

    public boolean isReadOnlyAccess() {
        return _readOnlyAccess;
    }

    public void setReadOnlyAccess(boolean readOnlyAccess) {
        _readOnlyAccess = readOnlyAccess;
    }

    private ISVNOptions _opts;

    public ISVNOptions getSVNOptions(boolean createIfNotExist) {
        if ((null == _opts) && createIfNotExist) _opts = SVNWCUtil.createDefaultOptions(getConfigDirectory(), isReadOnlyAccess());
        return _opts;
    }

    public ISVNOptions getSVNOptions() {
        return getSVNOptions(false);
    }

    public void setSVNOptions(ISVNOptions o) {
        _opts = o;
    }

    private String _username;

    public String getUsername() {
        return _username;
    }

    public void setUsername(String v) {
        _username = v;
    }

    private String _password;

    public String getPassword() {
        return _password;
    }

    public void setPassword(String v) {
        _password = v;
    }

    private File _configDir;

    public File getConfigDirectory() {
        return _configDir;
    }

    public void setConfigDirectory(File d) {
        _configDir = d;
    }

    private boolean _storeAuth;

    public boolean isStoreAuth() {
        return _storeAuth;
    }

    public void setStoreAuth(boolean storeAuth) {
        _storeAuth = storeAuth;
    }

    private ISVNAuthenticationManager _authMgr;

    public void setSVNAuthManager(ISVNAuthenticationManager authMgr) {
        _authMgr = authMgr;
    }

    public ISVNAuthenticationManager getSVNAuthManager(boolean createIfNotExist) {
        if ((null == _authMgr) && createIfNotExist) _authMgr = SVNWCUtil.createDefaultAuthenticationManager(getConfigDirectory(), getUsername(), getPassword(), isStoreAuth());
        return _authMgr;
    }

    public ISVNAuthenticationManager getSVNAuthManager() {
        return getSVNAuthManager(false);
    }

    private SVNClientManager _mgr;

    public SVNClientManager getSVNClientManager(ISVNOptions opts, ISVNAuthenticationManager mgr, boolean createIfNotExist) {
        if ((null == _mgr) && createIfNotExist) _mgr = SVNClientManager.newInstance(opts, mgr);
        return _mgr;
    }

    public SVNClientManager getSVNClientManager(boolean createIfNotExist) {
        return getSVNClientManager(getSVNOptions(createIfNotExist), getSVNAuthManager(createIfNotExist), createIfNotExist);
    }

    public SVNClientManager getSVNClientManager() {
        return getSVNClientManager(false);
    }

    public void setSVNClientManager(SVNClientManager mgr) {
        _mgr = mgr;
    }
}
