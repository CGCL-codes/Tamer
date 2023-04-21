package org.ddth.txbb.base;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.ddth.daf.AuthorityAgent;
import org.ddth.daf.Group;
import org.ddth.daf.Passport;
import org.ddth.daf.Permission;
import org.ddth.daf.Resource;
import org.ddth.daf.User;
import org.ddth.panda.BaseActionMapping;
import org.ddth.panda.BaseApp;
import org.ddth.panda.BaseBootstrap;
import org.ddth.panda.BaseLanguage;
import org.ddth.panda.util.API_System;

public abstract class TXBBApp extends BaseApp {

    public TXBBApp(BaseBootstrap bootstrap, HttpServletRequest request, HttpServletResponse response, BaseActionMapping actionMapping) throws ServletException {
        super(bootstrap, request, response, actionMapping);
        System.out.println("Constructing..." + this);
    }

    protected void finalize() throws Throwable {
        System.out.println("Finalizing..." + this);
        System.out.println();
        super.finalize();
    }

    @Override
    public void doRequest() throws ServletException {
        String tempPath = _REQUEST.getRequestURI();
        String tempCalcPath = _REQUEST.getContextPath() + _REQUEST.getServletPath();
        if (tempPath.equalsIgnoreCase(tempCalcPath)) try {
            String queryString = _REQUEST.getQueryString();
            if (queryString != null && queryString.length() != 0) queryString = "?" + queryString; else queryString = "";
            forwardURL(tempPath + "/" + queryString);
        } catch (IOException e) {
            throw new ServletException(e.getMessage(), e);
        } else super.doRequest();
    }

    @Override
    public TXBBBootstrap getBootstrap() {
        return (TXBBBootstrap) super.getBootstrap();
    }

    @Override
    public TXBBConfig getConfig() {
        return (TXBBConfig) super.getConfig();
    }

    @Override
    public TXBBDBLayer getDBLayer() {
        return (TXBBDBLayer) super.getDBLayer();
    }

    public String getVersionInformation() {
        return _CONFIG.getAppVersionInformation();
    }

    public boolean isGod(User user) throws ServletException {
        if (user == null) return false;
        try {
            return getBootstrap().getAuthorityAgent().isInGodGroup(user);
        } catch (Exception e) {
            throw new ServletException(e.getMessage(), e);
        }
    }

    public boolean isGod() throws ServletException {
        return isGod(getCurrentUser());
    }

    public boolean hasPassport(Permission permission, Resource resource, Group group) throws ServletException {
        if (permission == null || resource == null || group == null) return false;
        try {
            return getBootstrap().getAuthorityAgent().getAuthority(group, permission, resource) != null;
        } catch (Exception e) {
            throw new ServletException(e.getMessage(), e);
        }
    }

    public boolean hasPassport(Permission permission, Resource resource, User member) throws ServletException {
        if (permission == null || resource == null || member == null) return false;
        List<Passport> passports = null;
        try {
            passports = getBootstrap().getAuthorityAgent().getAuthority(member, permission, resource, AuthorityAgent.RETURN_ALL_PASSPORT);
        } catch (Exception e) {
            throw new ServletException(e.getMessage(), e);
        }
        return passports != null && passports.size() > 0;
    }

    public boolean hasPassport(Permission permission, Resource resource) throws ServletException {
        Group group;
        try {
            group = _DBLAYER.getGroup(getConfig().getGroupIdForGuest());
        } catch (Exception e) {
            throw new ServletException(e.getMessage(), e);
        }
        return hasPassport(permission, resource, getCurrentUser()) || hasPassport(permission, resource, group);
    }

    public boolean hasPassport(Permission permission, Group group) throws ServletException {
        if (group == null || group == null) return false;
        try {
            return getBootstrap().getAuthorityAgent().getAuthority(group, permission) != null;
        } catch (Exception e) {
            throw new ServletException(e.getMessage(), e);
        }
    }

    public boolean hasPassport(Permission permission, User member) throws ServletException {
        if (member == null || permission == null) return false;
        List<Passport> passports;
        try {
            passports = getBootstrap().getAuthorityAgent().getAuthority(member, permission, AuthorityAgent.RETURN_ALL_PASSPORT);
        } catch (Exception e) {
            throw new ServletException(e.getMessage(), e);
        }
        return passports != null && passports.size() > 0;
    }

    public boolean hasPassport(Permission permission) throws ServletException {
        Group group;
        try {
            group = _DBLAYER.getGroup(getConfig().getGroupIdForGuest());
        } catch (Exception e) {
            throw new ServletException(e.getMessage(), e);
        }
        return hasPassport(permission, getCurrentUser()) || hasPassport(permission, group);
    }

    public String timestampToString(int timestamp) {
        int currentTimestamp = API_System.getCurrentTimestamp();
        int delta = currentTimestamp - timestamp;
        if (0 <= delta && delta < 60) {
            return timestampToStringSecond(delta);
        } else if (60 <= delta && delta < 60 * 60) {
            return timestampToStringMinute(delta);
        } else if (60 * 60 <= delta && delta < 24 * 60 * 60) {
            return timestampToStringHour(delta);
        } else {
            return timestampToStringDate(timestamp);
        }
    }

    private String timestampToStringDate(int timestamp) {
        Date date = new Date(1000 * (long) timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd-MM-yyyy");
        return sdf.format(date);
    }

    private String timestampToStringHour(int seconds) {
        BaseLanguage language = getLanguage();
        int hours = seconds / 3600;
        int minutes = (seconds - hours * 3600) / 60;
        String s;
        if (hours < 2) s = hours + " " + language.getText("COMMON", "hour") + " "; else s = hours + " " + language.getText("COMMON", "hours") + " ";
        if (minutes < 2) s += minutes + " " + language.getText("COMMON", "minute") + " "; else s += minutes + " " + language.getText("COMMON", "minutes") + " ";
        return s + language.getText("COMMON", "ago");
    }

    private String timestampToStringMinute(int seconds) {
        BaseLanguage language = getLanguage();
        int minutes = seconds / 60;
        String s;
        if (minutes < 2) s = minutes + " " + language.getText("COMMON", "minute") + " "; else s = minutes + " " + language.getText("COMMON", "minutes") + " ";
        return s + language.getText("COMMON", "ago");
    }

    private String timestampToStringSecond(int seconds) {
        BaseLanguage language = getLanguage();
        String s;
        if (seconds < 2) s = seconds + " " + language.getText("COMMON", "second") + " "; else s = seconds + " " + language.getText("COMMON", "seconds") + " ";
        return s + language.getText("COMMON", "ago");
    }
}
