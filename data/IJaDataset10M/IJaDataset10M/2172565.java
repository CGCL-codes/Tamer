package com.dotmarketing.viewtools;

import java.io.StringWriter;
import javax.servlet.http.HttpServletRequest;
import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.view.context.ViewContext;
import org.apache.velocity.tools.view.tools.ViewTool;
import com.dotmarketing.util.Logger;
import com.dotmarketing.util.VelocityUtil;

/**
 * @author Jason Tesser
 * @since 1.6.5
 *
 */
public class VelocityWebUtil implements ViewTool {

    private HttpServletRequest request;

    private Context ctx;

    public void init(Object obj) {
        ViewContext context = (ViewContext) obj;
        this.request = context.getRequest();
        this.ctx = context.getVelocityContext();
    }

    public String mergeTemplate(String templatePath) {
        VelocityEngine ve = VelocityUtil.getEngine();
        Template template = null;
        StringWriter sw = new StringWriter();
        try {
            template = ve.getTemplate(templatePath);
            template.merge(ctx, sw);
        } catch (Exception e) {
            Logger.error(this, e.getMessage(), e);
        }
        return sw.toString();
    }
}
