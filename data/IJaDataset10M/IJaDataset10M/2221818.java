package org.ofbiz.content.webapp.ftl;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.GeneralException;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.base.util.template.FreeMarkerWorker;
import org.ofbiz.content.content.ContentWorker;
import org.ofbiz.entity.GenericDelegator;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.service.LocalDispatcher;
import freemarker.core.Environment;
import freemarker.template.TemplateTransformModel;

/**
 * WrapSubContentCacheTransform - Freemarker Transform for URLs (links)
 * 
 * This is an interactive FreeMarker tranform that allows the user to modify the contents that are placed within it.
 */
public class WrapSubContentCacheTransform implements TemplateTransformModel {

    public static final String module = WrapSubContentCacheTransform.class.getName();

    public static final String[] upSaveKeyNames = { "globalNodeTrail" };

    public static final String[] saveKeyNames = { "contentId", "subContentId", "subDataResourceTypeId", "mimeTypeId", "whenMap", "locale", "wrapTemplateId", "encloseWrapText", "nullThruDatesOnly" };

    /**
     * A wrapper for the FreeMarkerWorker version.
     */
    public static Object getWrappedObject(String varName, Environment env) {
        return FreeMarkerWorker.getWrappedObject(varName, env);
    }

    public static String getArg(Map args, String key, Environment env) {
        return FreeMarkerWorker.getArg(args, key, env);
    }

    public static String getArg(Map args, String key, Map ctx) {
        return FreeMarkerWorker.getArg(args, key, ctx);
    }

    public Writer getWriter(final Writer out, Map args) {
        final StringBuffer buf = new StringBuffer();
        final Environment env = Environment.getCurrentEnvironment();
        Map envContext = (Map) FreeMarkerWorker.getWrappedObject("context", env);
        final Map templateCtx;
        if (envContext == null) {
            templateCtx = FreeMarkerWorker.createEnvironmentMap(env);
        } else {
            templateCtx = envContext;
        }
        final LocalDispatcher dispatcher = (LocalDispatcher) FreeMarkerWorker.getWrappedObject("dispatcher", env);
        final GenericDelegator delegator = (GenericDelegator) FreeMarkerWorker.getWrappedObject("delegator", env);
        final HttpServletRequest request = (HttpServletRequest) FreeMarkerWorker.getWrappedObject("request", env);
        FreeMarkerWorker.getSiteParameters(request, templateCtx);
        final Map savedValuesUp = new HashMap();
        FreeMarkerWorker.saveContextValues(templateCtx, upSaveKeyNames, savedValuesUp);
        FreeMarkerWorker.overrideWithArgs(templateCtx, args);
        final String wrapTemplateId = (String) templateCtx.get("wrapTemplateId");
        final GenericValue userLogin = (GenericValue) FreeMarkerWorker.getWrappedObject("userLogin", env);
        List trail = (List) templateCtx.get("globalNodeTrail");
        String contentAssocPredicateId = (String) templateCtx.get("contentAssocPredicateId");
        String strNullThruDatesOnly = (String) templateCtx.get("nullThruDatesOnly");
        Boolean nullThruDatesOnly = (strNullThruDatesOnly != null && strNullThruDatesOnly.equalsIgnoreCase("true")) ? Boolean.TRUE : Boolean.FALSE;
        GenericValue val = null;
        try {
            val = ContentWorker.getCurrentContent(delegator, trail, userLogin, templateCtx, nullThruDatesOnly, contentAssocPredicateId);
        } catch (GeneralException e) {
            throw new RuntimeException("Error getting current content. " + e.toString());
        }
        final GenericValue view = val;
        String dataResourceId = null;
        try {
            dataResourceId = (String) view.get("drDataResourceId");
        } catch (Exception e) {
            dataResourceId = (String) view.get("dataResourceId");
        }
        String subContentIdSub = (String) view.get("contentId");
        String subDataResourceTypeId = (String) templateCtx.get("subDataResourceTypeId");
        if (UtilValidate.isEmpty(subDataResourceTypeId)) {
            try {
                subDataResourceTypeId = (String) view.get("drDataResourceTypeId");
            } catch (Exception e) {
            }
        }
        final Map savedValues = new HashMap();
        FreeMarkerWorker.saveContextValues(templateCtx, saveKeyNames, savedValues);
        String mimeTypeId = ContentWorker.getMimeTypeId(delegator, view, templateCtx);
        templateCtx.put("drDataResourceId", dataResourceId);
        templateCtx.put("mimeTypeId", mimeTypeId);
        templateCtx.put("dataResourceId", dataResourceId);
        templateCtx.put("subContentIdSub", subContentIdSub);
        templateCtx.put("subDataResourceTypeId", subDataResourceTypeId);
        templateCtx.put("wrapTemplateId", null);
        return new Writer(out) {

            public void write(char cbuf[], int off, int len) {
                buf.append(cbuf, off, len);
            }

            public void flush() throws IOException {
                out.flush();
            }

            public void close() throws IOException {
                FreeMarkerWorker.reloadValues(templateCtx, savedValues, env);
                String wrappedContent = buf.toString();
                if (UtilValidate.isNotEmpty(wrapTemplateId)) {
                    templateCtx.put("wrappedContent", wrappedContent);
                    Map templateRoot = null;
                    Map templateRootTemplate = (Map) templateCtx.get("templateRootTemplate");
                    if (templateRootTemplate == null) {
                        Map templateRootTmp = FreeMarkerWorker.createEnvironmentMap(env);
                        templateRoot = new HashMap(templateRootTmp);
                        templateCtx.put("templateRootTemplate", templateRootTmp);
                    } else {
                        templateRoot = new HashMap(templateRootTemplate);
                    }
                    templateRoot.put("context", templateCtx);
                    String mimeTypeId = (String) templateCtx.get("mimeTypeId");
                    Locale locale = null;
                    try {
                        ContentWorker.renderContentAsText(dispatcher, delegator, wrapTemplateId, out, templateRoot, locale, mimeTypeId, true);
                    } catch (IOException e) {
                        Debug.logError(e, "Error rendering content" + e.getMessage(), module);
                        throw new IOException("Error rendering content" + e.toString());
                    } catch (GeneralException e2) {
                        Debug.logError(e2, "Error rendering content" + e2.getMessage(), module);
                        throw new IOException("Error rendering content" + e2.toString());
                    }
                    FreeMarkerWorker.reloadValues(templateCtx, savedValuesUp, env);
                }
            }
        };
    }
}
