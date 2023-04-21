package org.sss.common.impl;

import java.sql.Connection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.sss.common.model.ILoginContext;
import org.sss.common.model.IMenuItem;
import org.sss.exception.ContainerException;

/**
 * 哑登录类
 * @author Jason.Hoo (latest modification by $Author: hujianxin78728 $)
 * @version $Revision: 632 $ $Date: 2010-07-06 22:21:04 -0400 (Tue, 06 Jul 2010) $
 */
public class DefaultLoginContext implements ILoginContext {

    protected String userName;

    protected boolean disposed;

    protected boolean authed;

    public DefaultLoginContext(String userName) {
        this.userName = userName;
    }

    public synchronized void dispose() {
        userName = null;
        disposed = true;
    }

    public boolean isDisposed() {
        return disposed;
    }

    public void clear() {
    }

    public boolean auth(Connection conn, Map parameterMap) throws ContainerException {
        authed = true;
        return true;
    }

    public boolean isAuthed() {
        return authed;
    }

    public boolean callback() {
        return true;
    }

    public String getErrorText() {
        return null;
    }

    public String getHomePageName() {
        return null;
    }

    public String getHomeTransaction() {
        return null;
    }

    public Locale getLocale() {
        return null;
    }

    public String getEntity() {
        return null;
    }

    public String getLoginUser() {
        return userName;
    }

    public String getCustomNumber() {
        return null;
    }

    public IMenuItem getMenu() {
        return null;
    }

    public List<IMenuItem> getHeader() {
        return null;
    }

    public Object getValue(String key) {
        return null;
    }

    public boolean isLogon() {
        return true;
    }

    public boolean login(Connection conn, Map parameterMap) throws ContainerException {
        return true;
    }

    public void logout(Connection conn) throws ContainerException {
    }
}
