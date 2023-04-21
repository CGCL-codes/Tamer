package org.jzonic.yawiki.views;

import org.apache.velocity.VelocityContext;
import org.jconfig.Configuration;
import org.jconfig.ConfigurationManager;
import org.jzonic.core.ViewException;
import org.jzonic.core.WebContext;
import org.jzonic.yawiki.util.FileUtils;
import org.jzonic.yawiki.util.VelocityTransformer;

/**
 *
 * @author  Andreas Mecky andreasmecky@yahoo.de
 */
public class AddPage extends AbstractView {

    private static final Configuration cm = ConfigurationManager.getConfiguration("jZonic");

    public AddPage() {
    }

    public String generate(WebContext webContext) throws ViewException {
        try {
            init(webContext);
            VelocityContext ctx = getBaseContext(webContext);
            String myPath = cm.getProperty("web_resources") + "/templates";
            ctx.put("files", FileUtils.listFiles(myPath, null));
            String theme = webContext.getVariable("theme");
            return VelocityTransformer.transform(theme + "/add_page.vm", ctx);
        } catch (Exception e) {
            throw new ViewException("Error while generating view:" + e.getMessage());
        }
    }
}
