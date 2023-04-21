package com.jacum.cms.transform.velocity;

import com.jacum.cms.JacumCmsException;
import com.jacum.cms.source.ContentController;
import com.jacum.cms.source.ContentViewHelper;
import com.jacum.cms.source.ContentControllerManager;
import com.jacum.cms.session.ContentRequestContext;
import com.jacum.cms.session.content.Content;
import com.jacum.cms.session.content.ContentItem;
import com.jacum.cms.session.content.TextContent;
import com.jacum.cms.transform.ContentTransformer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.app.Velocity;
import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

/**
 * An implementation of content transformation from JCR content items to text by executing a velocity.
 * <p/>
 */
public class VelocityTransformer implements ContentTransformer {

    private static final Log log = LogFactory.getLog(VelocityTransformer.class);

    private String outputContentType;

    private String encoding;

    private ContentControllerManager controllerManager;

    private Map velocityProperties;

    public void executeTransform(ContentRequestContext ctx) {
        log.debug("Template transformation started");
        Content currentContent = ctx.getCurrentContent();
        ContentItem contentItem = currentContent.getContentItem();
        String templateId = contentItem.getTemplateId();
        log.debug("Template ID '" + templateId + "'");
        String output;
        TemplateContext tc = new TemplateContext(ctx);
        tc.setMainContentItem(contentItem);
        tc.setTemplateId(templateId);
        try {
            tc.put("item", tc.getMainContentItem());
            tc.put("session", ctx.getContentRepositorySession());
            tc.put("render", new RenderViewHelper(this, tc));
            tc.put("controllerManager", controllerManager);
            tc.put("templateContext", tc);
            ContentController ctrl = ctx.getActiveController();
            if (ctrl != null) {
                String controllerName = ctrl.getName() + "Controller";
                log.debug("Adding active controller as '" + controllerName + "' to template context");
                tc.put(controllerName, ctrl);
            }
            Map<String, ContentViewHelper> customViewHelpers = ctx.getViewHelpers();
            if (customViewHelpers != null) {
                for (String key : customViewHelpers.keySet()) {
                    log.debug("Adding view helper '" + key + "'");
                    ContentViewHelper vh = customViewHelpers.get(key);
                    vh.setContentRepositorySession(ctx.getContentRepositorySession());
                    tc.put(key, vh);
                }
            }
            output = renderTemplate(tc);
        } catch (Exception e) {
            log.error("Can't execute template " + templateId, e);
            output = "! Can't execute template " + templateId + ": " + e.getMessage() + "; see logs for more details";
            tc.vetoCaching();
        }
        TextContent outputContent = new TextContent(output, outputContentType, encoding);
        long lastModifiedContent = ctx.getCurrentContent().getLastModified();
        long lastModifiedTemplate = tc.getLastModified();
        outputContent.setLastModified(lastModifiedContent > lastModifiedTemplate ? lastModifiedContent : lastModifiedTemplate);
        ctx.replaceContent(outputContent);
        log.debug("Template transformation done");
        if (tc.isCachingVetoed()) {
            log.debug("Caching vetoed by template transformer");
            ctx.vetoCaching();
        }
    }

    public String renderTemplate(TemplateContext tc) {
        try {
            Template template = Velocity.getTemplate(tc.getTemplateId() + ".vm");
            tc.setLastModified(template.getLastModified());
            template.setEncoding(this.encoding);
            StringWriter writer = new StringWriter();
            template.merge(tc.getVelocityContext(), writer);
            return writer.toString();
        } catch (Exception e) {
            log.error("Error rendering template", e);
            tc.vetoCaching();
            return "Error rendering template '" + tc.getTemplateId() + "', reason: " + e.getMessage();
        }
    }

    public void setVelocityProperties(Map properties) {
        try {
            this.velocityProperties = properties;
            Properties p = new Properties();
            for (Object o : properties.keySet()) {
                String key = (String) o;
                p.setProperty(key, (String) properties.get(key));
            }
            Velocity.init(p);
        } catch (Exception e) {
            throw new JacumCmsException("Can't initialize Velocity engine", e);
        }
    }

    public void setOutputContentType(String outputContentType) {
        this.outputContentType = outputContentType;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public void setControllerManager(ContentControllerManager controllerManager) {
        this.controllerManager = controllerManager;
    }

    public Map getVelocityProperties() {
        return velocityProperties;
    }
}
