package org.kablink.teaming.liferay.servlet;

import com.liferay.portal.deploy.hot.PluginPackageHotDeployListener;
import com.liferay.portal.events.EventsProcessor;
import com.liferay.portal.events.StartupAction;
import com.liferay.portal.kernel.plugin.PluginPackage;
import com.liferay.portal.kernel.servlet.PortletSessionTracker;
import com.liferay.portal.lastmodified.LastModifiedAction;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.CompanyThreadLocal;
import com.liferay.portal.security.auth.PrincipalThreadLocal;
import com.liferay.portal.service.CompanyLocalServiceUtil;
import com.liferay.portal.service.PortletLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.service.impl.LayoutTemplateLocalUtil;
import com.liferay.portal.service.impl.ThemeLocalUtil;
import com.liferay.portal.struts.PortletRequestProcessor;
import com.liferay.portal.struts.StrutsUtil;
import com.liferay.portal.util.Constants;
import com.liferay.portal.util.ContentUtil;
import com.liferay.portal.util.InitUtil;
import com.liferay.portal.util.PortalInstances;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PropsUtil;
import com.liferay.portal.util.ReleaseInfo;
import com.liferay.portal.util.SAXReaderFactory;
import com.liferay.portal.util.ShutdownUtil;
import com.liferay.portal.util.WebKeys;
import com.liferay.portal.velocity.VelocityContextPool;
import com.liferay.portlet.PortletInstanceFactory;
import com.liferay.util.CollectionFactory;
import com.liferay.util.GetterUtil;
import com.liferay.util.Http;
import com.liferay.util.HttpHeaders;
import com.liferay.util.InstancePool;
import com.liferay.util.ParamUtil;
import com.liferay.util.Validator;
import com.liferay.util.servlet.EncryptedServletRequest;
import com.liferay.util.servlet.ProtectedServletRequest;
import com.liferay.portal.kernel.util.PortalInitableUtil;
import com.liferay.portal.kernel.deploy.hot.HotDeployUtil;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.Set;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.tiles.TilesUtilImpl;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * <a href="MainServlet.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 * @author Jorge Ferrer
 * @author Brian Myunghun Kim
 *
 */
public class MainServlet extends ActionServlet {

    static {
        InitUtil.init();
    }

    public static ServletContext servletContext;

    public void init() throws ServletException {
        if (_log.isDebugEnabled()) {
            _log.debug("Initialize");
        }
        super.init();
        if (_log.isDebugEnabled()) {
            _log.debug("Process startup events");
        }
        try {
            EventsProcessor.process(new String[] { StartupAction.class.getName() }, true);
        } catch (Exception e) {
            _log.error(e, e);
        }
        String contextPath = PortalUtil.getPathContext();
        ServletContext ctx = getServletContext();
        VelocityContextPool.put(contextPath, ctx);
        if (_log.isDebugEnabled()) {
            _log.debug("Initialize plugin package");
        }
        PluginPackage pluginPackage = null;
        try {
            pluginPackage = PluginPackageHotDeployListener.readPluginPackage(ctx);
        } catch (Exception e) {
            _log.error(e, e);
        }
        if (_log.isDebugEnabled()) {
            _log.debug("Initialize portlets");
        }
        try {
            String[] xmls = new String[] { Http.URLtoString(ctx.getResource("/WEB-INF/" + PortalUtil.PORTLET_XML_FILE_NAME_CUSTOM)), Http.URLtoString(ctx.getResource("/WEB-INF/portlet-ext.xml")), Http.URLtoString(ctx.getResource("/WEB-INF/liferay-portlet.xml")), Http.URLtoString(ctx.getResource("/WEB-INF/liferay-portlet-ext.xml")), Http.URLtoString(ctx.getResource("/WEB-INF/web.xml")) };
            PortletLocalServiceUtil.initEAR(xmls, pluginPackage);
        } catch (Exception e) {
            _log.error(e, e);
        }
        if (_log.isDebugEnabled()) {
            _log.debug("Initialize layout templates");
        }
        try {
            String[] xmls = new String[] { Http.URLtoString(ctx.getResource("/WEB-INF/liferay-layout-templates.xml")), Http.URLtoString(ctx.getResource("/WEB-INF/liferay-layout-templates-ext.xml")) };
            LayoutTemplateLocalUtil.init(ctx, xmls, pluginPackage);
        } catch (Exception e) {
            _log.error(e, e);
        }
        if (_log.isDebugEnabled()) {
            _log.debug("Initialize look and feel");
        }
        try {
            String[] xmls = new String[] { Http.URLtoString(ctx.getResource("/WEB-INF/liferay-look-and-feel.xml")), Http.URLtoString(ctx.getResource("/WEB-INF/liferay-look-and-feel-ext.xml")) };
            ThemeLocalUtil.init(ctx, null, true, xmls, pluginPackage);
        } catch (Exception e) {
            _log.error(e, e);
        }
        if (_log.isDebugEnabled()) {
            _log.debug("Check web settings");
        }
        try {
            String xml = Http.URLtoString(ctx.getResource("/WEB-INF/web.xml"));
            checkWebSettings(xml);
        } catch (Exception e) {
            _log.error(e, e);
        }
        if (_log.isDebugEnabled()) {
            _log.debug("Last modified paths");
        }
        if (_lastModifiedPaths == null) {
            _lastModifiedPaths = CollectionFactory.getHashSet();
            String[] pathsArray = PropsUtil.getArray(PropsUtil.LAST_MODIFIED_PATHS);
            for (int i = 0; i < pathsArray.length; i++) {
                _lastModifiedPaths.add(pathsArray[i]);
            }
        }
        if (_log.isDebugEnabled()) {
            _log.debug("Process global startup events");
        }
        try {
            EventsProcessor.process(PropsUtil.getArray(PropsUtil.GLOBAL_STARTUP_EVENTS), true);
        } catch (Exception e) {
            _log.error(e, e);
        }
        String[] webIds = PortalInstances.getWebIds();
        for (int i = 0; i < webIds.length; i++) {
            PortalInstances.initCompany(ctx, webIds[i]);
        }
        PortalInitableUtil.flushInitables();
        HotDeployUtil.flushEvents();
        servletContext = getServletContext();
    }

    public void callParentService(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        super.service(req, res);
    }

    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        if (ShutdownUtil.isShutdown()) {
            res.setContentType(Constants.TEXT_HTML);
            String html = ContentUtil.get("com/liferay/portal/dependencies/shutdown.html");
            res.getOutputStream().print(html);
            return;
        }
        HttpSession ses = req.getSession();
        long companyId = PortalInstances.getCompanyId(req);
        CompanyThreadLocal.setCompanyId(companyId);
        ServletContext ctx = getServletContext();
        req.setAttribute(WebKeys.CTX, ctx);
        ModuleConfig moduleConfig = getModuleConfig(req);
        if (GetterUtil.getBoolean(PropsUtil.get(PropsUtil.LAST_MODIFIED_CHECK))) {
            String path = req.getPathInfo();
            if ((path != null) && _lastModifiedPaths.contains(path)) {
                ActionMapping mapping = (ActionMapping) moduleConfig.findActionConfig(path);
                LastModifiedAction lastModifiedAction = (LastModifiedAction) InstancePool.get(mapping.getType());
                String lmKey = lastModifiedAction.getLastModifiedKey(req);
                if (lmKey != null) {
                    long ifModifiedSince = req.getDateHeader(HttpHeaders.IF_MODIFIED_SINCE);
                    if (ifModifiedSince <= 0) {
                        lastModifiedAction.setLastModifiedValue(lmKey, lmKey);
                    } else {
                        String lmValue = lastModifiedAction.getLastModifiedValue(lmKey);
                        if (lmValue != null) {
                            res.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                            return;
                        } else {
                            lastModifiedAction.setLastModifiedValue(lmKey, lmKey);
                        }
                    }
                }
            }
        }
        if (ses.getAttribute(WebKeys.PORTLET_SESSION_TRACKER) == null) {
            ses.setAttribute(WebKeys.PORTLET_SESSION_TRACKER, PortletSessionTracker.getInstance());
        }
        PortletRequestProcessor portletReqProcessor = (PortletRequestProcessor) ctx.getAttribute(WebKeys.PORTLET_STRUTS_PROCESSOR);
        if (portletReqProcessor == null) {
            portletReqProcessor = PortletRequestProcessor.getInstance(this, moduleConfig);
            ctx.setAttribute(WebKeys.PORTLET_STRUTS_PROCESSOR, portletReqProcessor);
        }
        if (ctx.getAttribute(TilesUtilImpl.DEFINITIONS_FACTORY) == null) {
            ctx.setAttribute(TilesUtilImpl.DEFINITIONS_FACTORY, ctx.getAttribute(TilesUtilImpl.DEFINITIONS_FACTORY));
        }
        Object applicationAssociate = ctx.getAttribute(WebKeys.ASSOCIATE_KEY);
        if (ctx.getAttribute(WebKeys.ASSOCIATE_KEY) == null) {
            ctx.setAttribute(WebKeys.ASSOCIATE_KEY, applicationAssociate);
        }
        String strutsCharEncoding = PropsUtil.get(PropsUtil.STRUTS_CHAR_ENCODING);
        req.setCharacterEncoding(strutsCharEncoding);
        if (ParamUtil.get(req, WebKeys.ENCRYPT, false)) {
            try {
                Company company = CompanyLocalServiceUtil.getCompanyById(companyId);
                req = new EncryptedServletRequest(req, company.getKeyObj());
            } catch (Exception e) {
            }
        }
        String completeURL = Http.getCompleteURL(req);
        if ((Validator.isNotNull(completeURL)) && (completeURL.indexOf("j_security_check") == -1)) {
            completeURL = completeURL.substring(completeURL.indexOf("://") + 3, completeURL.length());
            completeURL = completeURL.substring(completeURL.indexOf("/"), completeURL.length());
        }
        if (Validator.isNull(completeURL)) {
            completeURL = PortalUtil.getPathMain();
        }
        req.setAttribute(WebKeys.CURRENT_URL, completeURL);
        long userId = PortalUtil.getUserId(req);
        String remoteUser = req.getRemoteUser();
        if (!GetterUtil.getBoolean(PropsUtil.get(PropsUtil.PORTAL_JAAS_ENABLE))) {
            String jRemoteUser = (String) ses.getAttribute("j_remoteuser");
            if (jRemoteUser != null) {
                remoteUser = jRemoteUser;
                ses.removeAttribute("j_remoteuser");
            }
        }
        if ((userId > 0) && (remoteUser == null)) {
            remoteUser = String.valueOf(userId);
        }
        req = new ProtectedServletRequest(req, remoteUser);
        if ((userId > 0) || (remoteUser != null)) {
            String name = String.valueOf(userId);
            if (remoteUser != null) {
                name = remoteUser;
            }
            PrincipalThreadLocal.setName(name);
        }
        if ((userId <= 0) && (remoteUser != null)) {
            try {
                userId = GetterUtil.getLong(remoteUser);
                EventsProcessor.process(PropsUtil.getArray(PropsUtil.LOGIN_EVENTS_PRE), req, res);
                User user = UserLocalServiceUtil.getUserById(userId);
                UserLocalServiceUtil.updateLastLogin(userId, req.getRemoteAddr());
                ses.setAttribute(WebKeys.USER_ID, new Long(userId));
                ses.setAttribute(Globals.LOCALE_KEY, user.getLocale());
                EventsProcessor.process(PropsUtil.getArray(PropsUtil.LOGIN_EVENTS_POST), req, res);
            } catch (Exception e) {
                _log.error(e, e);
            }
        }
        try {
            EventsProcessor.process(PropsUtil.getArray(PropsUtil.SERVLET_SERVICE_EVENTS_PRE), req, res);
        } catch (Exception e) {
            _log.error(e, e);
            req.setAttribute(PageContext.EXCEPTION, e);
            StrutsUtil.forward(PropsUtil.get(PropsUtil.SERVLET_SERVICE_EVENTS_PRE_ERROR_PAGE), ctx, req, res);
        }
        try {
            callParentService(req, res);
        } finally {
            try {
                EventsProcessor.process(PropsUtil.getArray(PropsUtil.SERVLET_SERVICE_EVENTS_POST), req, res);
            } catch (Exception e) {
                _log.error(e, e);
            }
            res.addHeader(_LIFERAY_PORTAL_REQUEST_HEADER, ReleaseInfo.getReleaseInfo());
            CompanyThreadLocal.setCompanyId(0);
            PrincipalThreadLocal.setName(null);
        }
    }

    public void destroy() {
        long[] companyIds = PortalInstances.getCompanyIds();
        for (int i = 0; i < companyIds.length; i++) {
            destroyCompany(companyIds[i]);
        }
        try {
            EventsProcessor.process(PropsUtil.getArray(PropsUtil.GLOBAL_SHUTDOWN_EVENTS), true);
        } catch (Exception e) {
            _log.error(e, e);
        }
        super.destroy();
    }

    protected void checkWebSettings(String xml) throws DocumentException {
        SAXReader reader = SAXReaderFactory.getInstance(false);
        Document doc = reader.read(new StringReader(xml));
        Element root = doc.getRootElement();
        int timeout = GetterUtil.getInteger(PropsUtil.get(PropsUtil.SESSION_TIMEOUT));
        Element sessionConfig = root.element("session-config");
        if (sessionConfig != null) {
            String sessionTimeout = sessionConfig.elementText("session-timeout");
            timeout = GetterUtil.getInteger(sessionTimeout, timeout);
        }
        PropsUtil.set(PropsUtil.SESSION_TIMEOUT, Integer.toString(timeout));
    }

    protected void destroyCompany(long companyId) {
        try {
            Iterator itr = PortletLocalServiceUtil.getPortlets(companyId).iterator();
            while (itr.hasNext()) {
                Portlet portlet = (Portlet) itr.next();
                PortletInstanceFactory.destroy(portlet);
            }
        } catch (Exception e) {
            _log.error(e, e);
        }
        if (_log.isDebugEnabled()) {
            _log.debug("Process shutdown events");
        }
        try {
            EventsProcessor.process(PropsUtil.getArray(PropsUtil.APPLICATION_SHUTDOWN_EVENTS), new String[] { String.valueOf(companyId) });
        } catch (Exception e) {
            _log.error(e, e);
        }
    }

    private static final String _LIFERAY_PORTAL_REQUEST_HEADER = "Liferay-Portal";

    private static Log _log = LogFactory.getLog(MainServlet.class);

    private Set _lastModifiedPaths;
}
