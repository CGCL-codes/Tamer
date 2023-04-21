package nuts.exts.struts2.views.java.simple;

import java.io.IOException;
import org.apache.struts2.components.template.TemplateRenderingContext;
import nuts.exts.struts2.views.java.AbstractTemplateRenderer;
import nuts.exts.struts2.views.java.Attributes;

public class HeadRenderer extends AbstractTemplateRenderer {

    public HeadRenderer(TemplateRenderingContext context) {
        super(context);
    }

    public void render() throws IOException {
        Attributes attrs = new Attributes();
        attrs.put("type", "text/javascript");
        attrs.put("src", uri("/struts/utils.js"));
        stag("script", attrs);
        etag("script");
    }
}
