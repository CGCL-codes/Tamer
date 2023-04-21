package csiebug.domain.hibernateImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import csiebug.domain.Cookie;
import csiebug.domain.Dashboard;
import csiebug.domain.DashboardPortlet;
import csiebug.domain.Resource;
import csiebug.domain.Role;
import csiebug.domain.User;
import csiebug.domain.UserEmail;
import csiebug.domain.UserProfile;
import csiebug.domain.WebservicesChannel;
import csiebug.util.AssertUtility;

/**
 * 
 * @author George_Tsai
 * @version 2009/6/22
 *
 */
public class UserImpl extends BasicObjectImpl implements User {

    private static final long serialVersionUID = 1L;

    private String username;

    private String password;

    private Boolean enabled;

    private UserProfile userProfile;

    private Set<UserEmail> userEmails = new HashSet<UserEmail>();

    private Set<Dashboard> dashboards = new HashSet<Dashboard>();

    private Set<Role> roles = new HashSet<Role>();

    private Set<Cookie> cookies = new HashSet<Cookie>();

    private Set<WebservicesChannel> webservicesChannels = new HashSet<WebservicesChannel>();

    private Set<Resource> resources = new HashSet<Resource>();

    public void setId(String username) {
        this.username = username;
    }

    public String getId() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setCookies(Set<Cookie> cookies) {
        this.cookies = cookies;
    }

    public Set<Cookie> getCookies() {
        return cookies;
    }

    public void addCookie(Cookie cookie) {
        cookies.add(cookie);
    }

    public void removeCookie(Cookie cookie) {
        cookies.remove(cookie);
    }

    public Cookie getCookie(String series) {
        Cookie cookie = null;
        AssertUtility.notNull(series);
        Iterator<Cookie> iterator = cookies.iterator();
        while (iterator.hasNext()) {
            Cookie temp = iterator.next();
            if (temp.getSeries().equalsIgnoreCase(series)) {
                cookie = temp;
            }
        }
        return cookie;
    }

    public String getAvailableCookieSeries() {
        String series = "";
        for (int i = 0; i < cookies.size(); i++) {
            if (getCookie("" + i) == null) {
                series = "" + i;
                break;
            }
        }
        if (series.equals("")) {
            series = "" + cookies.size();
        }
        return series;
    }

    public List<Cookie> getExpiredCookie(int lifecycle) {
        List<Cookie> list = new ArrayList<Cookie>();
        Iterator<Cookie> iterator = cookies.iterator();
        while (iterator.hasNext()) {
            Cookie cookie = iterator.next();
            Calendar expiredDate = Calendar.getInstance();
            expiredDate.add(Calendar.DAY_OF_YEAR, (0 - lifecycle));
            Calendar cookieLastUsedDate = cookie.getLastUsed();
            if (expiredDate.after(cookieLastUsedDate)) {
                list.add(cookie);
            }
        }
        return list;
    }

    public void setWebservicesChannels(Set<WebservicesChannel> webservicesChannels) {
        this.webservicesChannels = webservicesChannels;
    }

    public Set<WebservicesChannel> getWebservicesChannels() {
        return webservicesChannels;
    }

    public void addWebservicesChannel(WebservicesChannel webservicesChannel) {
        webservicesChannels.add(webservicesChannel);
    }

    public void removeWebservicesChannel(WebservicesChannel webservicesChannel) {
        webservicesChannels.remove(webservicesChannel);
    }

    public WebservicesChannel getWebservicesChannel(String channelId) {
        WebservicesChannel webservicesChannel = null;
        AssertUtility.notNull(channelId);
        Iterator<WebservicesChannel> iterator = webservicesChannels.iterator();
        while (iterator.hasNext()) {
            WebservicesChannel temp = iterator.next();
            if (temp.getChannelId().equalsIgnoreCase(channelId)) {
                webservicesChannel = temp;
            }
        }
        return webservicesChannel;
    }

    public String getAvailableWebservicesChannelId() {
        String series = "";
        for (int i = 0; i < webservicesChannels.size(); i++) {
            if (getWebservicesChannel("" + i) == null) {
                series = "" + i;
                break;
            }
        }
        if (series.equals("")) {
            series = "" + webservicesChannels.size();
        }
        return series;
    }

    public List<WebservicesChannel> getExpiredWebservicesChannel(int lifecycle) {
        List<WebservicesChannel> list = new ArrayList<WebservicesChannel>();
        Iterator<WebservicesChannel> iterator = webservicesChannels.iterator();
        while (iterator.hasNext()) {
            WebservicesChannel webservicesChannel = iterator.next();
            Calendar expiredDate = Calendar.getInstance();
            expiredDate.add(Calendar.DAY_OF_YEAR, (0 - lifecycle));
            Calendar webservicesChannelLastUsedDate = webservicesChannel.getLastUsed();
            if (expiredDate.after(webservicesChannelLastUsedDate)) {
                list.add(webservicesChannel);
            }
        }
        return list;
    }

    public void setAuthorities(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Role> getAuthorities() {
        return roles;
    }

    public void addAuthority(Role role) {
        roles.add(role);
    }

    public void removeAuthority(Role role) {
        roles.remove(role);
    }

    public void setUserEmails(Set<UserEmail> userEmails) {
        this.userEmails = userEmails;
    }

    public Set<UserEmail> getUserEmails() {
        return userEmails;
    }

    public void addUserEmail(UserEmail userEmail) {
        userEmails.add(userEmail);
    }

    public void removeUserEmail(UserEmail userEmail) {
        userEmails.remove(userEmail);
    }

    public void setDashboards(Set<Dashboard> dashboards) {
        this.dashboards = dashboards;
    }

    public Set<Dashboard> getDashboards() {
        return dashboards;
    }

    public void addDashboard(Dashboard dashboard) {
        dashboards.add(dashboard);
    }

    public void removeDashboard(Dashboard dashboard) {
        dashboards.remove(dashboard);
    }

    public List<Resource> getResources() {
        List<Resource> list = new ArrayList<Resource>();
        Iterator<Role> iterator = getAuthorities().iterator();
        while (iterator.hasNext()) {
            Role role = iterator.next();
            list.addAll(role.getAuthorityResources());
        }
        return list;
    }

    public Dashboard getDashboard(String dashboardId) {
        Dashboard dashboard = null;
        AssertUtility.notNull(dashboardId);
        Iterator<Dashboard> iterator = dashboards.iterator();
        while (iterator.hasNext()) {
            Dashboard temp = iterator.next();
            if (temp.getDashboardId().equalsIgnoreCase(dashboardId)) {
                dashboard = temp;
            }
        }
        return dashboard;
    }

    public DashboardPortlet getDashboardPortlet(String portletId) {
        DashboardPortlet portlet = null;
        AssertUtility.notNull(portletId);
        Iterator<Dashboard> iterator = getDashboards().iterator();
        while (iterator.hasNext()) {
            Dashboard dashboard = iterator.next();
            boolean flag = false;
            Iterator<DashboardPortlet> iterator2 = dashboard.getPortlets().iterator();
            while (iterator2.hasNext()) {
                DashboardPortlet targetPortlet = iterator2.next();
                if (targetPortlet.getPortletId().equalsIgnoreCase(portletId)) {
                    portlet = targetPortlet;
                    flag = true;
                }
                if (flag) {
                    break;
                }
            }
            if (flag) {
                break;
            }
        }
        return portlet;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setNickname(String nickname) {
        if (userProfile == null) {
            userProfile = new UserProfileImpl();
            userProfile.setId(this.username);
        }
        this.userProfile.setNickname(nickname);
    }

    public String getNickname() {
        if (userProfile == null) {
            userProfile = new UserProfileImpl();
            userProfile.setId(this.username);
        }
        return this.userProfile.getNickname();
    }

    public void setLocale(String locale) {
        if (userProfile == null) {
            userProfile = new UserProfileImpl();
            userProfile.setId(this.username);
        }
        this.userProfile.setLocale(locale);
    }

    public String getLocale() {
        if (userProfile == null) {
            userProfile = new UserProfileImpl();
            userProfile.setId(this.username);
        }
        return this.userProfile.getLocale();
    }

    public void setBirthday(Calendar birthday) {
        if (userProfile == null) {
            userProfile = new UserProfileImpl();
            userProfile.setId(this.username);
        }
        this.userProfile.setBirthday(birthday);
    }

    public Calendar getBirthday() {
        if (userProfile == null) {
            userProfile = new UserProfileImpl();
            userProfile.setId(this.username);
        }
        return this.userProfile.getBirthday();
    }

    public String getMajorEmail() {
        String majorEmail = "";
        if (userEmails != null && userEmails.size() > 0) {
            Iterator<UserEmail> iterator = userEmails.iterator();
            while (iterator.hasNext()) {
                UserEmail email = iterator.next();
                if (majorEmail.trim().equals("")) {
                    majorEmail = email.toString();
                } else if (email.getMajorFlag()) {
                    majorEmail = email.toString();
                    break;
                }
            }
        }
        return majorEmail;
    }

    public void setUserResources(Set<Resource> resource) {
        this.resources = resource;
    }

    public Set<Resource> getUserResources() {
        return resources;
    }

    public void addUserResource(Resource resource) {
        resources.add(resource);
    }

    public void removeUserResource(Resource resource) {
        resources.remove(resource);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof UserImpl)) {
            return false;
        }
        UserImpl user = (UserImpl) obj;
        return new EqualsBuilder().append(this.username, user.getId()).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(this.username).toHashCode();
    }
}
