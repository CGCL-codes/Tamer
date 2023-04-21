package net.sourceforge.thinfeeder.vo;

import java.awt.Rectangle;
import net.sourceforge.thinfeeder.model.dao.DAOI18N;
import net.sourceforge.thinfeeder.model.dao.DAOSkin;

/**
 * @author fabianofranz@users.sourceforge.net
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class System implements SystemIF {

    private long id;

    private String name;

    private String version;

    private long skin;

    private long i18n;

    private int defaultMaxItems;

    private boolean enableIcons;

    private boolean allowDuplicatedChannels;

    private Rectangle bounds;

    private int dividerXPosition;

    private int dividerYPosition;

    private boolean useDefaultBrowser;

    private String browser;

    private boolean useProxy;

    private String proxyHost;

    private String proxyPort;

    private String proxyUser;

    private String proxyPassword;

    private int refreshTime;

    /**
	 * @return Returns the browser.
	 */
    public String getBrowser() {
        return browser;
    }

    /**
	 * @param browser
	 *            The browser to set.
	 */
    public void setBrowser(String browser) {
        this.browser = browser;
    }

    /**
	 * @return Returns the proxyHost.
	 */
    public String getProxyHost() {
        return proxyHost;
    }

    /**
	 * @param proxyHost
	 *            The proxyHost to set.
	 */
    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    /**
	 * @return Returns the proxyPassword.
	 */
    public String getProxyPassword() {
        return proxyPassword;
    }

    /**
	 * @param proxyPassword
	 *            The proxyPassword to set.
	 */
    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }

    /**
	 * @return Returns the proxyPort.
	 */
    public String getProxyPort() {
        return proxyPort;
    }

    /**
	 * @param proxyPort
	 *            The proxyPort to set.
	 */
    public void setProxyPort(String proxyPort) {
        this.proxyPort = proxyPort;
    }

    /**
	 * @return Returns the proxyUser.
	 */
    public String getProxyUser() {
        return proxyUser;
    }

    /**
	 * @param proxyUser
	 *            The proxyUser to set.
	 */
    public void setProxyUser(String proxyUser) {
        this.proxyUser = proxyUser;
    }

    /**
	 * @return Returns the useDefaultBrowser.
	 */
    public boolean isUseDefaultBrowser() {
        return useDefaultBrowser;
    }

    /**
	 * @param useDefaultBrowser
	 *            The useDefaultBrowser to set.
	 */
    public void setUseDefaultBrowser(boolean useDefaultBrowser) {
        this.useDefaultBrowser = useDefaultBrowser;
    }

    /**
	 * @return Returns the useProxy.
	 */
    public boolean isUseProxy() {
        return useProxy;
    }

    /**
	 * @param useProxy
	 *            The useProxy to set.
	 */
    public void setUseProxy(boolean useProxy) {
        this.useProxy = useProxy;
    }

    /**
	 * @return Returns the dividerXPosition.
	 */
    public int getDividerXPosition() {
        return dividerXPosition;
    }

    /**
	 * @param dividerXPosition
	 *            The dividerXPosition to set.
	 */
    public void setDividerXPosition(int dividerXPosition) {
        this.dividerXPosition = dividerXPosition;
    }

    /**
	 * @return Returns the dividerYPosition.
	 */
    public int getDividerYPosition() {
        return dividerYPosition;
    }

    /**
	 * @param dividerYPosition
	 *            The dividerYPosition to set.
	 */
    public void setDividerYPosition(int dividerYPosition) {
        this.dividerYPosition = dividerYPosition;
    }

    /**
	 * @return Returns the bounds.
	 */
    public Rectangle getBounds() {
        return bounds;
    }

    /**
	 * @param bounds
	 *            The bounds to set.
	 */
    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    /**
	 * @return Returns the i18n.
	 */
    public long getI18N() {
        return i18n;
    }

    /**
	 * @param i18n
	 *            The i18n to set.
	 */
    public void setI18N(long i18n) {
        this.i18n = i18n;
    }

    /**
	 * @return Returns the defaultMaxItems.
	 */
    public int getDefaultMaxItems() {
        return defaultMaxItems;
    }

    /**
	 * @param defaultMaxItems
	 *            The defaultMaxItems to set.
	 */
    public void setDefaultMaxItems(int defaultMaxItems) {
        this.defaultMaxItems = defaultMaxItems;
    }

    /**
	 * @return Returns the id.
	 */
    public long getId() {
        return id;
    }

    /**
	 * @param id
	 *            The id to set.
	 */
    public void setId(long id) {
        this.id = id;
    }

    /**
	 * @return Returns the name.
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name
	 *            The name to set.
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @return Returns the skin.
	 */
    public long getSkin() {
        return skin;
    }

    /**
	 * @return Returns the skin.
	 */
    public SkinIF getSkinObject() throws Exception {
        return DAOSkin.getSkin(skin);
    }

    /**
	 * @return Returns the i18n.
	 */
    public I18NIF getI18NObject() throws Exception {
        return DAOI18N.getI18N(i18n);
    }

    /**
	 * @param skin
	 *            The skin to set.
	 */
    public void setSkin(long skin) {
        this.skin = skin;
    }

    /**
	 * @param skin
	 *            The skin to set.
	 */
    public void setSkinObject(SkinIF skin) {
        this.skin = skin.getId();
    }

    /**
	 * @param i18n
	 *            The i18n to set.
	 */
    public void setI18NObject(I18NIF i18n) {
        this.i18n = i18n.getId();
    }

    /**
	 * @return Returns the version.
	 */
    public String getVersion() {
        return version;
    }

    /**
	 * @param version
	 *            The version to set.
	 */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
	 * @return Returns the enableIcons.
	 */
    public boolean isEnableIcons() {
        return enableIcons;
    }

    /**
	 * @param enableIcons
	 *            The enableIcons to set.
	 */
    public void setEnableIcons(boolean enableIcons) {
        this.enableIcons = enableIcons;
    }

    /**
	 * @return Returns the allowDuplicatedChannels.
	 */
    public boolean isAllowDuplicatedChannels() {
        return allowDuplicatedChannels;
    }

    /**
	 * @param allowDuplicatedChannels
	 *            The allowDuplicatedChannels to set.
	 */
    public void setAllowDuplicatedChannels(boolean allowDuplicatedChannels) {
        this.allowDuplicatedChannels = allowDuplicatedChannels;
    }

    /**
	 * @return The time in minutes to refresh all the channels.
	 */
    public int getRefreshTime() {
        return refreshTime;
    }

    /**
	 * @param The
	 *            time in minutes to refresh all the channels.
	 */
    public void setRefreshTime(int refreshTime) {
        this.refreshTime = refreshTime;
    }
}
