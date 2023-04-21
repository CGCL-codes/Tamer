package netgest.bo.xwc.framework.jsf;

import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.PhaseId;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.Renderer;
import javax.faces.render.ResponseStateManager;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import netgest.bo.def.boDefHandler;
import netgest.bo.localizations.MessageLocalizer;
import netgest.bo.system.boApplication;
import netgest.bo.transaction.XTransaction;
import netgest.bo.xwc.components.classic.Layouts;
import netgest.bo.xwc.components.security.ViewerAccessPolicyBuilder;
import netgest.bo.xwc.framework.PackageIAcessor;
import netgest.bo.xwc.framework.XUIApplicationContext;
import netgest.bo.xwc.framework.XUIRendererServlet;
import netgest.bo.xwc.framework.XUIRequestContext;
import netgest.bo.xwc.framework.XUIResponseWriter;
import netgest.bo.xwc.framework.XUIScriptContext;
import netgest.bo.xwc.framework.XUIViewBean;
import netgest.bo.xwc.framework.components.XUIComponentBase;
import netgest.bo.xwc.framework.components.XUIViewRoot;
import netgest.bo.xwc.framework.def.XUIViewerDefinition;
import netgest.bo.xwc.framework.http.XUIAjaxRequestWrapper;
import netgest.bo.xwc.framework.localization.XUICoreMessages;
import netgest.bo.xwc.xeo.beans.XEOSecurityBaseBean;
import netgest.utils.ngtXMLUtils;
import oracle.xml.parser.v2.XMLDocument;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.sun.faces.RIConstants;
import com.sun.faces.application.ViewHandlerResponseWrapper;
import com.sun.faces.config.WebConfiguration;
import com.sun.faces.config.WebConfiguration.WebContextInitParameter;
import com.sun.faces.io.FastStringWriter;
import com.sun.faces.util.RequestStateManager;
import com.sun.faces.util.Util;

/**
 * <B>ViewHandlerImpl</B> is the default implementation class for ViewHandler.
 *
 * @version $Id: ViewHandlerImpl.java,v 1.45.12.2.2.1 2006/04/12 19:32:04 ofung Exp $
 * @see javax.faces.application.ViewHandler
 */
public class XUIViewHandler extends XUIViewHandlerImpl {

    private static final Log log = LogFactory.getLog(netgest.bo.xwc.framework.jsf.XUIViewHandler.class);

    javax.xml.transform.TransformerFactory tFactory = javax.xml.transform.TransformerFactory.newInstance();

    javax.xml.transform.Transformer transformer;

    public XUIViewHandler() {
        if (log.isDebugEnabled()) {
            log.debug(MessageLocalizer.getMessage("CREATE_VIEWHANDLER_INSTANCE"));
        }
    }

    public void renderView(FacesContext context, UIViewRoot viewToRender) throws IOException, FacesException {
        long ini = System.currentTimeMillis();
        XUIRequestContext.getCurrentContext().setRenderedViewer(viewToRender);
        ExternalContext extContext = context.getExternalContext();
        ServletRequest request = (ServletRequest) extContext.getRequest();
        ServletResponse response = (ServletResponse) extContext.getResponse();
        RenderKitFactory renderFactory = (RenderKitFactory) FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit renderKit = renderFactory.getRenderKit(context, viewToRender.getRenderKitId());
        ResponseWriter oldWriter = context.getResponseWriter();
        if (bufSize == -1) {
            WebConfiguration webConfig = WebConfiguration.getInstance(context.getExternalContext());
            try {
                bufSize = Integer.parseInt(webConfig.getOptionValue(WebContextInitParameter.ResponseBufferSize));
            } catch (NumberFormatException nfe) {
                bufSize = Integer.parseInt(WebContextInitParameter.ResponseBufferSize.getDefaultValue());
            }
        }
        String[] oCompId = (String[]) request.getParameterMap().get("xvw.servlet");
        if (oCompId != null) {
            renderServlet(context, request, response, renderKit, viewToRender, oCompId[0]);
        } else if (request instanceof XUIAjaxRequestWrapper) {
            renderAjax(context, request, response, renderKit, viewToRender);
        } else {
            if ("XEOXML".equals(viewToRender.getRenderKitId())) {
                renderToBuffer(context, request, response, renderKit, viewToRender);
            } else {
                renderNormal(context, request, response, renderKit, viewToRender);
            }
        }
        if (null != oldWriter) {
            context.setResponseWriter(oldWriter);
        }
        ViewHandlerResponseWrapper wrapper = (ViewHandlerResponseWrapper) RequestStateManager.remove(context, RequestStateManager.AFTER_VIEW_CONTENT);
        if (null != wrapper) {
            wrapper.flushToWriter(response.getWriter(), response.getCharacterEncoding());
        }
        response.flushBuffer();
    }

    public void renderToBuffer(FacesContext context, ServletRequest request, ServletResponse response, RenderKit renderKit, UIViewRoot viewToRender) throws IOException {
        if (!viewToRender.isRendered()) {
            return;
        }
        response.setContentType("text/html;charset=utf-8");
        CharArrayWriter w = new CharArrayWriter();
        XUIWriteBehindStateWriter headWriter = new XUIWriteBehindStateWriter(w, context, bufSize);
        XUIWriteBehindStateWriter bodyWriter = new XUIWriteBehindStateWriter(w, context, bufSize);
        XUIWriteBehindStateWriter footerWriter = new XUIWriteBehindStateWriter(w, context, bufSize);
        ResponseWriter newWriter;
        newWriter = renderKit.createResponseWriter(bodyWriter, null, request.getCharacterEncoding());
        context.setResponseWriter(newWriter);
        if (newWriter instanceof XUIResponseWriter) PackageIAcessor.setHeaderAndFooterToWriter((XUIResponseWriter) newWriter, headWriter, footerWriter);
        newWriter.startDocument();
        viewToRender.encodeAll(context);
        newWriter.endDocument();
        headWriter.flushToWriter(false);
        headWriter.release();
        bodyWriter.flushToWriter(false);
        bodyWriter.release();
        footerWriter.flushToWriter(false);
        footerWriter.release();
        String temp = w.toString();
        XMLDocument doc = ngtXMLUtils.loadXML(temp);
        String xmlContent = ngtXMLUtils.getXML(doc);
        final String HTML_TEMPLATES = "html_templates.xsl";
        final String PROJECT_HTML_TEMPLATES = "projectHtmlTemplates.xsl";
        if ("true".equals(request.getAttribute("xsltransform"))) {
            InputStream finalTransformer = Thread.currentThread().getContextClassLoader().getResourceAsStream(PROJECT_HTML_TEMPLATES);
            if (finalTransformer == null) {
                finalTransformer = Thread.currentThread().getContextClassLoader().getResourceAsStream(HTML_TEMPLATES);
            }
            Source xmlSource = new StreamSource(new StringReader(xmlContent));
            Source xsltSource = new StreamSource(finalTransformer);
            CharArrayWriter out = new CharArrayWriter();
            try {
                TransformerFactory transFact = TransformerFactory.newInstance();
                Transformer trans = transFact.newTransformer(xsltSource);
                trans.transform(xmlSource, new StreamResult(out));
                xmlContent = out.toString();
            } catch (Exception e) {
                throw new RuntimeException("XEOEditBean - XSLT Transformation Error", e);
            } finally {
                response.getWriter().write(out.toString());
            }
        } else {
            response.getWriter().write(xmlContent);
        }
    }

    public UIViewRoot restoreView(FacesContext context, String viewId) {
        return restoreView(context, viewId, null);
    }

    public UIViewRoot restoreView(FacesContext context, String viewId, Object savedId) {
        ExternalContext extContext = context.getExternalContext();
        if (viewId.startsWith("/")) {
            viewId = viewId.substring(1);
        }
        Map<String, String> headerMap = (Map<String, String>) extContext.getRequestHeaderMap();
        String contentType = null, charEnc = null;
        if (null != (contentType = (String) headerMap.get("Content-Type"))) {
            String charsetStr = "charset=";
            int len = charsetStr.length(), i = 0;
            if (-1 != (i = contentType.indexOf(charsetStr)) && (i + len < contentType.length())) {
                charEnc = contentType.substring(i + len);
            }
        }
        if (null == charEnc) {
            if (null != extContext.getSession(false)) {
                charEnc = (String) extContext.getSessionMap().get(CHARACTER_ENCODING_KEY);
            }
        }
        if (null != charEnc) {
            try {
                Object request = extContext.getRequest();
                if (request instanceof ServletRequest) {
                    ((ServletRequest) request).setCharacterEncoding(charEnc);
                }
            } catch (java.io.UnsupportedEncodingException uee) {
                if (log.isErrorEnabled()) {
                    log.error(uee.getMessage(), uee);
                }
                throw new FacesException(uee);
            }
        }
        XUIViewRoot viewRoot = null;
        ViewHandler outerViewHandler = context.getApplication().getViewHandler();
        String renderKitId = outerViewHandler.calculateRenderKitId(context);
        XUIStateManagerImpl oStateManagerImpl = (XUIStateManagerImpl) Util.getStateManager(context);
        viewRoot = (XUIViewRoot) oStateManagerImpl.restoreView(context, viewId, renderKitId, savedId);
        if (viewRoot != null) {
            if (viewRoot.getBean("viewBean") instanceof XUIViewBean) {
                if (savedId != null) ((XUIViewBean) viewRoot.getBean("viewBean")).setViewRoot((String) savedId); else ((XUIViewBean) viewRoot.getBean("viewBean")).setViewRoot(viewRoot.getViewState());
            }
            String sTransactionId = viewRoot.getTransactionId();
            if (sTransactionId != null) {
                XTransaction oTransaction = XUIRequestContext.getCurrentContext().getTransactionManager().getTransaction(sTransactionId);
                if (oTransaction != null) {
                    oTransaction.activate();
                }
            }
            UIViewRoot savedView = context.getViewRoot();
            context.setViewRoot(viewRoot);
            viewRoot.notifyPhaseListeners(context, PhaseId.RESTORE_VIEW, false);
            if (viewRoot == context.getViewRoot() && savedView != null) context.setViewRoot(savedView);
        }
        return viewRoot;
    }

    public UIViewRoot createView(FacesContext context, String viewId) {
        return createView(context, viewId, null, null);
    }

    public UIViewRoot createView(FacesContext context, String viewId, String sTransactionId) {
        return createView(context, viewId, null, sTransactionId);
    }

    public UIViewRoot createView(FacesContext context, String viewId, InputStream viewerInputStream, String sTransactionId) {
        XUIViewerBuilder oViewerBuilder;
        XUIRequestContext oContext;
        XUIApplicationContext oApp;
        Locale locale = null;
        String renderKitId = null;
        oContext = XUIRequestContext.getCurrentContext();
        oApp = oContext.getApplicationContext();
        oViewerBuilder = oApp.getViewerBuilder();
        if (viewId.startsWith("/")) {
            viewId = viewId.substring(1);
        }
        if (context.getViewRoot() != null) {
            locale = context.getViewRoot().getLocale();
            renderKitId = context.getViewRoot().getRenderKitId();
        }
        XUIViewRoot result = new XUIViewRoot();
        UIViewRoot previousViewRoot = context.getViewRoot();
        try {
            context.setViewRoot(result);
            result.setViewId(viewId);
            XTransaction oTransaction;
            if (sTransactionId == null) {
                oTransaction = XUIRequestContext.getCurrentContext().getSessionContext().getTransactionManager().createTransaction();
                result.setOwnsTransaction(true);
            } else {
                oTransaction = XUIRequestContext.getCurrentContext().getSessionContext().getTransactionManager().getTransaction(sTransactionId);
                result.setOwnsTransaction(false);
            }
            if (oTransaction != null) {
                result.setTransactionId(oTransaction.getId());
                oTransaction.activate();
            }
            if (context.getViewRoot() != null) {
                String pValue = XUIRequestContext.getCurrentContext().getRequestParameterMap().get(ResponseStateManager.VIEW_STATE_PARAM);
                result.setParentViewState(pValue);
            }
            if (log.isDebugEnabled()) {
                log.debug(MessageLocalizer.getMessage("CREATE_NEW_VIEW_FOR") + " " + viewId);
            }
            if (locale == null) {
                locale = context.getApplication().getViewHandler().calculateLocale(context);
                if (log.isDebugEnabled()) {
                    log.debug(MessageLocalizer.getMessage("LOCALE_FOR_THIS_VIEW_AS_DETERMINED_BY_CALCULATELOCALE") + " " + locale.toString());
                }
            } else {
                if (log.isDebugEnabled()) {
                    log.debug(MessageLocalizer.getMessage("USING_LOCALE_FROM_PREVIOUS_VIEW") + " " + locale.toString());
                }
            }
            if (renderKitId == null) {
                renderKitId = context.getApplication().getViewHandler().calculateRenderKitId(context);
                if (log.isDebugEnabled()) {
                    log.debug(MessageLocalizer.getMessage("RENDERKITID_FOR_THIS_VIEW_AS_DETERMINATED_BY_CALCULTERENDERKIT") + " " + renderKitId);
                }
            } else {
                if (log.isDebugEnabled()) {
                    log.debug(MessageLocalizer.getMessage("USING_RENDERKITID_FROM_PREVIOUS_VIEW") + " " + renderKitId);
                }
            }
            result.setLocale(locale);
            result.setRenderKitId(renderKitId);
            XUIViewerDefinition oViewerDef;
            if (viewerInputStream != null) oViewerDef = oApp.getViewerDef(viewerInputStream); else oViewerDef = oApp.getViewerDef(viewId);
            result.setTransient(oViewerDef.isTransient());
            String sBeanName = oViewerDef.getViewerBeanId();
            String sBeanClassName = oViewerDef.getViewerBean();
            try {
                if (log.isDebugEnabled()) {
                    log.debug(MessageLocalizer.getMessage("INITIALIZING_BEANS_FOR_VIEW") + " " + viewId);
                }
                if (sBeanClassName != null && sBeanClassName.length() > 0) {
                    Class<?> oBeanClass = Thread.currentThread().getContextClassLoader().loadClass(sBeanClassName);
                    Object oViewBean = oBeanClass.newInstance();
                    result.addBean(sBeanName, oViewBean);
                    if (oViewBean instanceof XUIViewBean) {
                        ((XUIViewBean) oViewBean).setViewRoot(result.getViewState());
                    }
                }
            } catch (Exception ex) {
                throw new FacesException(XUICoreMessages.VIEWER_CLASS_NOT_FOUND.toString(sBeanClassName, viewId));
            }
            if (log.isDebugEnabled()) {
                log.debug(MessageLocalizer.getMessage("START_BUILDING_COMPONENT_VIEW") + " " + viewId);
            }
            oViewerBuilder.buildView(oContext, oViewerDef, result);
            if (log.isDebugEnabled()) {
                log.debug(MessageLocalizer.getMessage("END_BUILDING_COMPONENT_VIEW") + " " + viewId);
            }
            try {
                Object bean = result.getBean("viewBean");
                if (ViewerAccessPolicyBuilder.applyViewerSecurity) {
                    ViewerAccessPolicyBuilder.applyViewerSecurity = boDefHandler.getBoDefinition("XVWAccessPolicy") != null;
                    if (bean != null && bean instanceof XEOSecurityBaseBean) {
                        ViewerAccessPolicyBuilder viewerAccessPolicyBuilder = new ViewerAccessPolicyBuilder();
                        viewerAccessPolicyBuilder.processViewer(result, boApplication.currentContext().getEboContext(), false);
                        ((XEOSecurityBaseBean) bean).initializeSecurityMap(viewerAccessPolicyBuilder, result.getViewId());
                    }
                }
                UIViewRoot savedView = context.getViewRoot();
                context.setViewRoot(result);
                result.notifyPhaseListeners(context, PhaseId.RESTORE_VIEW, true);
                if (result == context.getViewRoot() && savedView != null) context.setViewRoot(savedView);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } finally {
            if (previousViewRoot != null) context.setViewRoot(previousViewRoot);
        }
        return result;
    }

    public void processInitComponent(XUIViewRoot oViewRoot) {
        Iterator<UIComponent> kids = oViewRoot.getFacetsAndChildren();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            if (kid instanceof UIComponent) {
                ((XUIComponentBase) kid).processInitComponents();
            }
        }
    }

    public String getAjaxActionURL(FacesContext context, String viewId) {
        String contextPath = context.getExternalContext().getRequestContextPath();
        String mapping = Util.getFacesMapping(context);
        viewId = context.getViewRoot().getViewId();
        if (mapping == null) {
            return contextPath + viewId;
        }
        if (!contextPath.endsWith("/") && !viewId.startsWith("/")) {
            contextPath += "/";
        }
        return contextPath + viewId;
    }

    public String getActionURL(FacesContext context, String viewId) {
        String sActionUrl;
        sActionUrl = (String) ((ServletRequest) context.getExternalContext().getRequest()).getAttribute("xvw.actionUrl");
        if (sActionUrl != null) {
            return sActionUrl;
        }
        sActionUrl = (String) ((ServletRequest) context.getExternalContext().getRequest()).getParameter("xvw.actionUrl");
        if (sActionUrl != null) {
            return sActionUrl;
        }
        String contextPath = context.getExternalContext().getRequestContextPath();
        String mapping = Util.getFacesMapping(context);
        if (viewId == null) viewId = context.getViewRoot().getViewId();
        if (mapping == null) {
            return contextPath + viewId;
        }
        if (!contextPath.endsWith("/") && !viewId.startsWith("/")) {
            contextPath += "/";
        }
        return contextPath + viewId;
    }

    public void renderNormal(FacesContext context, ServletRequest request, ServletResponse response, RenderKit renderKit, UIViewRoot viewToRender) throws IOException {
        if (!viewToRender.isRendered()) {
            return;
        }
        response.setContentType("text/html;charset=utf-8");
        Writer w = response.getWriter();
        XUIWriteBehindStateWriter headWriter = new XUIWriteBehindStateWriter(w, context, bufSize);
        XUIWriteBehindStateWriter bodyWriter = new XUIWriteBehindStateWriter(w, context, bufSize);
        XUIWriteBehindStateWriter footerWriter = new XUIWriteBehindStateWriter(response.getWriter(), context, bufSize);
        ResponseWriter newWriter;
        newWriter = renderKit.createResponseWriter(bodyWriter, null, request.getCharacterEncoding());
        context.setResponseWriter(newWriter);
        if (newWriter instanceof XUIResponseWriter) PackageIAcessor.setHeaderAndFooterToWriter((XUIResponseWriter) newWriter, headWriter, footerWriter);
        newWriter.startDocument();
        viewToRender.encodeAll(context);
        newWriter.endDocument();
        headWriter.flushToWriter(false);
        headWriter.release();
        bodyWriter.flushToWriter();
        bodyWriter.release();
        footerWriter.flushToWriter(false);
        footerWriter.release();
    }

    public void renderServlet(FacesContext context, ServletRequest request, ServletResponse response, RenderKit renderKit, UIViewRoot viewToRender, String oCompId) throws IOException {
        String pValue = context.getExternalContext().getRequestParameterMap().get(ResponseStateManager.VIEW_STATE_PARAM);
        if (pValue != null && pValue.length() == 0) {
            pValue = null;
        }
        if (pValue != null) {
            try {
                Field f = ((XUIViewRoot) viewToRender).getClass().getDeclaredField("sStateId");
                f.setAccessible(true);
                f.set(viewToRender, pValue);
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        XUIComponentBase oComp = (XUIComponentBase) viewToRender.findComponent(oCompId);
        String rendererType = oComp.getRendererType();
        if (rendererType != null) {
            Renderer renderer = null;
            if (rendererType != null) {
                renderer = context.getRenderKit().getRenderer(oComp.getFamily(), rendererType);
            }
            if (renderer != null) {
                ((XUIRendererServlet) renderer).service(request, response, oComp);
            } else {
                log.warn(MessageLocalizer.getMessage("CANT_GET_RENDERER_FOR_TYPE") + " " + rendererType + " " + MessageLocalizer.getMessage("COMPONENT_SERVLET_REQUEST_ABORTED"));
            }
        }
        context.responseComplete();
        XUIWriteBehindStateWriter responseWriter = new XUIWriteBehindStateWriter(new FastStringWriter(), context, bufSize);
        XUIResponseWriter newWriter = (XUIResponseWriter) renderKit.createResponseWriter(responseWriter, null, request.getCharacterEncoding());
        context.setResponseWriter(newWriter);
        responseWriter.flushToWriter(true);
        responseWriter.release();
    }

    public void renderAjax(FacesContext context, ServletRequest request, ServletResponse response, RenderKit renderKit, UIViewRoot viewToRender) throws IOException {
        XUIWriteBehindStateWriter responseWriter;
        Document oAjaxXmlResp;
        response.setContentType("text/xml;charset=utf-8");
        responseWriter = new XUIWriteBehindStateWriter(response.getWriter(), context, bufSize);
        XUIResponseWriter newWriter = (XUIResponseWriter) renderKit.createResponseWriter(responseWriter, null, request.getCharacterEncoding());
        context.setResponseWriter(newWriter);
        ((XUIViewRoot) viewToRender).notifyPhaseListeners(context, PhaseId.RENDER_RESPONSE, true);
        oAjaxXmlResp = getAjaxXML(context, request, renderKit, (XUIViewRoot) viewToRender);
        ((XMLDocument) oAjaxXmlResp).setEncoding("utf-8");
        String sXml = ngtXMLUtils.getXML((XMLDocument) oAjaxXmlResp);
        responseWriter.write(sXml);
        responseWriter.flushToWriter();
        responseWriter.release();
        ((XUIViewRoot) viewToRender).notifyPhaseListeners(context, PhaseId.RENDER_RESPONSE, false);
    }

    public Document getAjaxXML(FacesContext context, ServletRequest request, RenderKit renderKit, XUIViewRoot oViewToRender) throws IOException {
        XUIResponseWriter newWriter;
        newWriter = null;
        ArrayList<XUIComponentBase> oToRenderList = new ArrayList<XUIComponentBase>();
        if (oViewToRender.isPostBack() && oViewToRender.isRendered()) {
            String[] oRenderCompId = (String[]) request.getParameterMap().get("xvw.render");
            if (oRenderCompId != null && oRenderCompId.length > 0) {
                for (int i = 0; i < oRenderCompId.length; i++) {
                    XUIComponentBase comp = (XUIComponentBase) oViewToRender.findComponent(oRenderCompId[i]);
                    if (comp != null) {
                        oToRenderList.add(comp);
                    }
                }
            } else {
                processStateWasChanged(oToRenderList, oViewToRender);
            }
        }
        Writer oHeadWriter = new FastStringWriter(bufSize / 8);
        Writer oFooterWriter = new FastStringWriter(bufSize / 8);
        XUIWriteBehindStateWriter headWriter = new XUIWriteBehindStateWriter(oHeadWriter, context, bufSize / 8);
        XUIWriteBehindStateWriter footerWriter = new XUIWriteBehindStateWriter(oFooterWriter, context, bufSize / 8);
        Document oAjaxXmlResp;
        Element oXvwAjaxResp;
        Element oRenderElement;
        Element oCompElement;
        XUIScriptContext oSavedScriptContext = null;
        oAjaxXmlResp = new XMLDocument();
        oXvwAjaxResp = oAjaxXmlResp.createElement("xvwAjaxResp");
        oAjaxXmlResp.appendChild(oXvwAjaxResp);
        oXvwAjaxResp.setAttribute("viewId", oViewToRender.getClientId());
        oXvwAjaxResp.setAttribute("isPostBack", String.valueOf(((XUIViewRoot) oViewToRender).isPostBack()));
        oRenderElement = oAjaxXmlResp.createElement("render");
        oXvwAjaxResp.appendChild(oRenderElement);
        ((HttpServletRequest) request).setAttribute("__xwcAjaxDomDoc", oAjaxXmlResp);
        ((HttpServletRequest) request).setAttribute("__xwcRenderElement", oRenderElement);
        ((HttpServletRequest) request).setAttribute("__xwcRenderKit", renderKit);
        if (oViewToRender.isPostBack()) {
            for (UIComponent comp : oToRenderList) {
                if (comp instanceof XUIComponentBase) {
                    ((XUIComponentBase) comp).resetRenderedOnClient();
                }
            }
            Writer oComponentWriter = new FastStringWriter(bufSize / 4);
            XUIWriteBehindStateWriter oCompBodyWriter = new XUIWriteBehindStateWriter(oComponentWriter, context, bufSize / 4);
            newWriter = (XUIResponseWriter) renderKit.createResponseWriter(oCompBodyWriter, null, request.getCharacterEncoding());
            if (oSavedScriptContext != null) {
                PackageIAcessor.setScriptContextToWriter(newWriter, oSavedScriptContext);
            }
            context.setResponseWriter(newWriter);
            PackageIAcessor.setHeaderAndFooterToWriter(newWriter, headWriter, footerWriter);
            newWriter.startDocument();
            oSavedScriptContext = newWriter.getScriptContext();
            newWriter.getStyleContext();
            for (int i = 0; i < oToRenderList.size(); i++) {
                XUIComponentBase oComp = oToRenderList.get(i);
                oComp.encodeAll();
            }
            newWriter.endDocument();
            oCompBodyWriter.flushToWriter(false);
            oCompBodyWriter.release();
            if (newWriter != null && request.getAttribute("__skip.Layouts.doLayout") == null) {
                Layouts.doLayout(newWriter);
            }
        } else {
            Writer oComponentWriter = new FastStringWriter(bufSize / 4);
            XUIWriteBehindStateWriter oCompBodyWriter = new XUIWriteBehindStateWriter(oComponentWriter, context, bufSize / 4);
            newWriter = (XUIResponseWriter) renderKit.createResponseWriter(oCompBodyWriter, null, request.getCharacterEncoding());
            if (oSavedScriptContext != null) {
                PackageIAcessor.setScriptContextToWriter(newWriter, oSavedScriptContext);
            }
            context.setResponseWriter(newWriter);
            PackageIAcessor.setHeaderAndFooterToWriter(newWriter, headWriter, footerWriter);
            newWriter.startDocument();
            request.setAttribute("__xwcAjaxTagOpened", Boolean.TRUE);
            oViewToRender.encodeAll(context);
            newWriter.endDocument();
            oCompBodyWriter.flushToWriter(false);
            oCompBodyWriter.release();
            String sResult = oComponentWriter.toString();
            if ("XEOXML".equals(oViewToRender.getRenderKitId())) {
                long init = System.currentTimeMillis();
                try {
                    InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("html_templates.xsl");
                    if (transformer == null) {
                        transformer = tFactory.newTransformer(new javax.xml.transform.stream.StreamSource(in));
                    }
                    CharArrayWriter out = new CharArrayWriter();
                    transformer.transform(new javax.xml.transform.stream.StreamSource(new CharArrayReader(sResult.toCharArray())), new javax.xml.transform.stream.StreamResult(out));
                    sResult = out.toString();
                } catch (TransformerConfigurationException e) {
                    e.printStackTrace();
                } catch (TransformerFactoryConfigurationError e) {
                    e.printStackTrace();
                } catch (TransformerException e) {
                    e.printStackTrace();
                }
                System.out.println(MessageLocalizer.getMessage("XSL_TIME") + (System.currentTimeMillis() - init));
            }
            if (!oViewToRender.getViewId().equals("netgest/bo/xwc/components/viewers/Dummy.xvw")) {
                oCompElement = oAjaxXmlResp.createElement("component");
                oCompElement.setAttribute("id", oViewToRender.getClientId());
                oCompElement.appendChild(oAjaxXmlResp.createCDATASection(sResult));
                oRenderElement.appendChild(oCompElement);
            }
            if (newWriter != null) {
                oSavedScriptContext = newWriter.getScriptContext();
            }
        }
        if (oToRenderList.size() > 0 || !oViewToRender.isPostBack()) {
            headWriter.flushToWriter(false);
            headWriter.release();
            footerWriter.flushToWriter(false);
            footerWriter.release();
            if (oSavedScriptContext != null) {
                oSavedScriptContext.renderForAjaxDom(oXvwAjaxResp);
            }
        }
        XUIRequestContext.getCurrentContext().getScriptContext().renderForAjaxDom(oXvwAjaxResp);
        Element oStateNode = oAjaxXmlResp.createElement("viewState");
        oStateNode.appendChild(oAjaxXmlResp.createTextNode(RIConstants.SAVESTATE_FIELD_MARKER));
        oXvwAjaxResp.appendChild(oStateNode);
        ((XMLDocument) oAjaxXmlResp).setEncoding("windows-1252");
        ((XMLDocument) oAjaxXmlResp).setVersion("1.0");
        return oAjaxXmlResp;
    }

    public void processStateWasChanged(ArrayList<XUIComponentBase> oRenderList, XUIViewRoot oRootView) {
        UIComponent oKid;
        List<UIComponent> oKids = oRootView.getChildren();
        for (int i = 0; i < oKids.size(); i++) {
            oKid = oKids.get(i);
            if (oKid instanceof XUIComponentBase) {
                ((XUIComponentBase) oKid).processStateChanged(oRenderList);
            }
        }
    }
}
