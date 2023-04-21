package com.liferay.portal.service;

/**
 * <a href="ThemeLocalService.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This interface defines the service. The default implementation is
 * <code>com.liferay.portal.service.impl.ThemeLocalServiceImpl</code>.
 * Modify methods in that class and rerun ServiceBuilder to populate this class
 * and all other generated classes.
 * </p>
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.liferay.portal.service.ThemeLocalServiceUtil
 *
 */
public interface ThemeLocalService {

    public com.liferay.portal.model.ColorScheme getColorScheme(long companyId, java.lang.String themeId, java.lang.String colorSchemeId, boolean wapTheme);

    public com.liferay.portal.model.Theme getTheme(long companyId, java.lang.String themeId, boolean wapTheme);

    public java.util.List<com.liferay.portal.model.Theme> getThemes(long companyId);

    public java.util.List<com.liferay.portal.model.Theme> getThemes(long companyId, long groupId, long userId, boolean wapTheme) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException;

    public java.util.List<String> init(javax.servlet.ServletContext servletContext, java.lang.String themesPath, boolean loadFromServletContext, java.lang.String[] xmls, com.liferay.portal.kernel.plugin.PluginPackage pluginPackage);

    public java.util.List<String> init(java.lang.String servletContextName, javax.servlet.ServletContext servletContext, java.lang.String themesPath, boolean loadFromServletContext, java.lang.String[] xmls, com.liferay.portal.kernel.plugin.PluginPackage pluginPackage);

    public void uninstallThemes(java.util.List<String> themeIds);
}
