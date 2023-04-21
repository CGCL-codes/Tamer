package nuts.exts.struts2.views.java.simple;

import java.io.IOException;
import java.util.Iterator;
import nuts.core.lang.StringEscapeUtils;
import nuts.exts.struts2.views.java.AbstractTemplateRenderer;
import nuts.exts.struts2.views.java.Attributes;
import org.apache.struts2.components.template.TemplateRenderingContext;
import org.apache.struts2.util.MakeIterator;

/**
 * Base class for ActionError and ActionMessage
 */
public abstract class NutsMessageListRenderer extends AbstractTemplateRenderer {

    public NutsMessageListRenderer(TemplateRenderingContext context) {
        super(context);
    }

    public void render() throws IOException {
        Object errorsObj = findValue(getListExpression());
        if (errorsObj != null) {
            Iterator itt = MakeIterator.convert(errorsObj);
            if (itt.hasNext()) {
                Attributes attrs = new Attributes();
                attrs.id(params).cssStyle(params).add("class", defs((String) params.get("cssClass"), getDefaultULClass()));
                stag("ul", attrs);
                Attributes attrsLI = new Attributes();
                attrsLI.add("class", getDefaultLIClass());
                while (itt.hasNext()) {
                    String error = (String) itt.next();
                    stag("li", attrsLI);
                    write(icon(getDefaultIMGClass()));
                    if (Attributes.isTrue(params.get("escape"), true)) {
                        write(phtml(error));
                    } else {
                        write(error);
                    }
                    etag("li");
                }
                etag("ul");
            }
        }
    }

    protected abstract String getListExpression();

    protected abstract String getDefaultULClass();

    protected abstract String getDefaultLIClass();

    protected abstract String getDefaultIMGClass();
}
