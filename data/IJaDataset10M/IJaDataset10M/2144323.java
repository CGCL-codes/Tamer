package de.iritgo.aktera.configuration;

import de.iritgo.aktera.model.ModelException;
import de.iritgo.aktera.model.ModelRequest;
import de.iritgo.aktera.tools.ModelTools;
import de.iritgo.simplelife.string.StringTools;

/**
 *
 */
public class SystemConfigTools {

    /**
	 * Get the URL to the web application accessed by the given request.
	 *
	 * @param request A model request
	 * @return The web application URL
	 */
    public static String getWebAppUrl(ModelRequest request) throws ModelException {
        boolean localRequest = false;
        StringBuilder sb = new StringBuilder();
        try {
            if (request == null) {
                request = ModelTools.createModelRequest();
                localRequest = true;
            }
            SystemConfigManager systemConfigManager = (SystemConfigManager) request.getSpringBean(SystemConfigManager.ID);
            String url = systemConfigManager.getString("tb2", "webAppUrl");
            if (!StringTools.isTrimEmpty(url)) {
                return url;
            }
            sb.append(request.getScheme());
            sb.append("://");
            sb.append(request.getServerName());
            if (request.getServerPort() != ("https".equals(request.getScheme()) ? 443 : 80)) {
                sb.append(":");
                sb.append(request.getServerPort());
            }
            sb.append(request.getContextPath());
            sb.append("/");
        } catch (Exception x) {
            throw new ModelException(x);
        } finally {
            if (localRequest) {
                ModelTools.releaseModelRequest(request);
            }
        }
        return sb.toString();
    }

    /**
	 * Get the URL to the jnlp-service accessed by the given request.
	 *
	 * @param request A model request
	 * @return The web start URL
	 */
    public static String getWebStartUrl(ModelRequest request) throws ModelException {
        SystemConfigManager systemConfigManager = (SystemConfigManager) request.getSpringBean(SystemConfigManager.ID);
        String url = systemConfigManager.getString("tb2", "webStartUrl");
        if (!StringTools.isTrimEmpty(url)) {
            return url;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(request.getScheme());
        sb.append("://");
        sb.append(request.getServerName());
        if (request.getServerPort() != ("https".equals(request.getScheme()) ? 443 : 80)) {
            sb.append(":");
            sb.append(request.getServerPort());
        }
        sb.append(request.getContextPath());
        sb.append("/");
        return sb.toString();
    }
}
