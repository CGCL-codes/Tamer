package com.liferay.portal.model;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * <a href="PortletApp.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public interface PortletApp extends Serializable {

    public String getServletContextName();

    public Set<String> getServletURLPatterns();

    public Set<String> getUserAttributes();

    public Map<String, String> getCustomUserAttributes();

    public void addPortletFilter(PortletFilter portletFilter);

    public PortletFilter getPortletFilter(String filterName);

    public Set<PortletFilter> getPortletFilters();

    public String getDefaultNamespace();

    public void setDefaultNamespace(String defaultNamespace);

    public void addEventDefinition(EventDefinition eventDefinition);

    public void addPublicRenderParameter(PublicRenderParameter publicRenderParameter);

    public PublicRenderParameter getPublicRenderParameter(String identifier);

    public void addPortletURLListener(PortletURLListener portletURLListener);

    public PortletURLListener getPortletURLListener(String listenerClass);

    public Set<PortletURLListener> getPortletURLListeners();

    public Map<String, String[]> getContainerRuntimeOptions();

    public boolean isWARFile();
}
