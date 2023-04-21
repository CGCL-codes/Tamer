package com.liferay.portlet;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.portlet.ActionResponse;
import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;
import javax.portlet.PortletURL;
import javax.portlet.WindowState;
import javax.portlet.WindowStateException;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.model.User;
import com.liferay.portal.util.PortalUtil;

/**
 * <a href="ActionResponseImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1.18 $
 *
 */
public class ActionResponseImpl implements ActionResponse {

    public ActionResponseImpl(ActionRequestImpl req, HttpServletResponse res, String portletName, User user, Layout layout, WindowState windowState, PortletMode portletMode) throws PortletModeException, WindowStateException {
        _req = req;
        _res = res;
        _portletName = portletName;
        _user = user;
        _layout = layout;
        setWindowState(windowState);
        setPortletMode(portletMode);
        _params = new LinkedHashMap();
        _calledSetRenderParameter = false;
    }

    public void addProperty(String key, String value) {
    }

    public void setProperty(String key, String value) {
    }

    public PortletURL createActionURL() {
        PortletURL portletURL = createPortletURL(true);
        try {
            portletURL.setWindowState(_req.getWindowState());
        } catch (WindowStateException wse) {
        }
        try {
            portletURL.setPortletMode(_req.getPortletMode());
        } catch (PortletModeException pme) {
        }
        return portletURL;
    }

    public PortletURL createRenderURL() {
        PortletURL portletURL = createPortletURL(false);
        try {
            portletURL.setWindowState(_req.getWindowState());
        } catch (WindowStateException wse) {
        }
        try {
            portletURL.setPortletMode(_req.getPortletMode());
        } catch (PortletModeException pme) {
        }
        return portletURL;
    }

    public String getNamespace() {
        return PortalUtil.getPortletNamespace(_portletName);
    }

    public String encodeURL(String path) {
        return path;
    }

    public void setWindowState(WindowState windowState) throws WindowStateException {
        if (_redirectLocation != null) {
            throw new IllegalStateException();
        }
        if (!_req.isWindowStateAllowed(windowState)) {
            throw new WindowStateException(windowState.toString(), windowState);
        }
        try {
            _windowState = PortalUtil.updateWindowState(_portletName, _user, _layout, windowState);
            _req.setWindowState(_windowState);
        } catch (Exception e) {
            throw new WindowStateException(e, windowState);
        }
        _calledSetRenderParameter = true;
    }

    public void setPortletMode(PortletMode portletMode) throws PortletModeException {
        if (_redirectLocation != null) {
            throw new IllegalStateException();
        }
        if (!_req.isPortletModeAllowed(portletMode)) {
            throw new PortletModeException(portletMode.toString(), portletMode);
        }
        try {
            _portletMode = PortalUtil.updatePortletMode(_portletName, _user, _layout, portletMode);
            _req.setPortletMode(_portletMode);
        } catch (Exception e) {
            throw new PortletModeException(e, portletMode);
        }
        _calledSetRenderParameter = true;
    }

    public Map getRenderParameters() {
        return _params;
    }

    public void setRenderParameter(String name, String value) {
        if (_redirectLocation != null) {
            throw new IllegalStateException();
        }
        if ((name == null) || (value == null)) {
            throw new IllegalArgumentException();
        }
        setRenderParameter(name, new String[] { value });
    }

    public void setRenderParameter(String name, String[] values) {
        if (_redirectLocation != null) {
            throw new IllegalStateException();
        }
        if ((name == null) || (values == null)) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < values.length; i++) {
            if (values[i] == null) {
                throw new IllegalArgumentException();
            }
        }
        _params.put(PortalUtil.getPortletNamespace(_portletName) + name, values);
        _calledSetRenderParameter = true;
    }

    public void setRenderParameters(Map params) {
        if (_redirectLocation != null) {
            throw new IllegalStateException();
        }
        if (params == null) {
            throw new IllegalArgumentException();
        } else {
            Map newParams = new LinkedHashMap();
            Iterator itr = params.entrySet().iterator();
            while (itr.hasNext()) {
                Map.Entry entry = (Map.Entry) itr.next();
                Object key = entry.getKey();
                Object value = entry.getValue();
                if (key == null) {
                    throw new IllegalArgumentException();
                } else if (value == null) {
                    throw new IllegalArgumentException();
                }
                if (value instanceof String[]) {
                    newParams.put(PortalUtil.getPortletNamespace(_portletName) + key, value);
                } else {
                    throw new IllegalArgumentException();
                }
            }
            _params = newParams;
        }
        _calledSetRenderParameter = true;
    }

    public String getRedirectLocation() {
        return _redirectLocation;
    }

    public void sendRedirect(String location) throws IOException {
        if ((location == null) || (!location.startsWith("/") && (location.indexOf("://") == -1))) {
            _log.error(location);
            throw new IllegalArgumentException();
        }
        if (_calledSetRenderParameter) {
            throw new IllegalStateException();
        }
        _redirectLocation = location;
    }

    public HttpServletResponse getHttpServletResponse() {
        return _res;
    }

    protected PortletURL createPortletURL(boolean action) {
        return new PortletURLImpl(_req, _portletName, _layout.getLayoutId(), action);
    }

    protected Layout getLayout() {
        return _layout;
    }

    protected Map getParams() {
        return _params;
    }

    protected Portlet getPortlet() {
        return _portlet;
    }

    protected PortletMode getPortletMode() {
        return _portletMode;
    }

    protected String getPortletName() {
        return _portletName;
    }

    protected ActionRequestImpl getReq() {
        return _req;
    }

    protected User getUser() {
        return _user;
    }

    protected WindowState getWindowState() {
        return _windowState;
    }

    protected boolean isCalledSetRenderParameter() {
        return _calledSetRenderParameter;
    }

    private static final Log _log = LogFactory.getLog(ActionResponseImpl.class);

    private ActionRequestImpl _req;

    private HttpServletResponse _res;

    private String _portletName;

    private Portlet _portlet;

    private User _user;

    private Layout _layout;

    private WindowState _windowState;

    private PortletMode _portletMode;

    private Map _params;

    private String _redirectLocation;

    private boolean _calledSetRenderParameter;
}
