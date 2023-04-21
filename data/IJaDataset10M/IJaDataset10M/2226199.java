package com.liferay.portal.servlet.filters.compression;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.servlet.BrowserSnifferUtil;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.ServerDetector;
import com.liferay.portal.servlet.filters.BasePortalFilter;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <a href="CompressionFilter.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 * @author Raymond Augé
 *
 */
public class CompressionFilter extends BasePortalFilter {

    public static final String SKIP_FILTER = CompressionFilter.class.getName() + "SKIP_FILTER";

    public CompressionFilter() {
        if (super.isFilterEnabled()) {
            if (ServerDetector.isJBoss() || ServerDetector.isJetty() || ServerDetector.isJOnAS() || ServerDetector.isOC4J() || ServerDetector.isOrion() || ServerDetector.isTomcat()) {
                _filterEnabled = true;
            } else {
                _filterEnabled = false;
            }
        }
    }

    protected boolean isAlreadyFiltered(HttpServletRequest request) {
        if (request.getAttribute(SKIP_FILTER) != null) {
            return true;
        } else {
            return false;
        }
    }

    protected boolean isCompress(HttpServletRequest request) {
        if (!ParamUtil.get(request, _COMPRESS, true)) {
            return false;
        } else {
            String lifecycle = ParamUtil.getString(request, "p_p_lifecycle");
            if ((lifecycle.equals("1") && LiferayWindowState.isExclusive(request)) || lifecycle.equals("2")) {
                return false;
            } else {
                return true;
            }
        }
    }

    protected boolean isFilterEnabled() {
        return _filterEnabled;
    }

    protected boolean isInclude(HttpServletRequest request) {
        String uri = (String) request.getAttribute(JavaConstants.JAVAX_SERVLET_INCLUDE_REQUEST_URI);
        if (uri == null) {
            return false;
        } else {
            return true;
        }
    }

    protected void processFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String completeURL = HttpUtil.getCompleteURL(request);
        if (isCompress(request) && !isInclude(request) && BrowserSnifferUtil.acceptsGzip(request) && !isAlreadyFiltered(request)) {
            if (_log.isDebugEnabled()) {
                _log.debug("Compressing " + completeURL);
            }
            request.setAttribute(SKIP_FILTER, Boolean.TRUE);
            CompressionResponse compressionResponse = new CompressionResponse(response);
            processFilter(CompressionFilter.class, request, compressionResponse, filterChain);
            compressionResponse.finishResponse();
        } else {
            if (_log.isDebugEnabled()) {
                _log.debug("Not compressing " + completeURL);
            }
            processFilter(CompressionFilter.class, request, response, filterChain);
        }
    }

    private static final String _COMPRESS = "compress";

    private static Log _log = LogFactoryUtil.getLog(CompressionFilter.class);

    private boolean _filterEnabled;
}
