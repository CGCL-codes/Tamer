package de.knowwe.core.kdom.taghandler;

import java.util.Map;
import de.knowwe.core.Environment;
import de.knowwe.core.report.Messages;
import de.knowwe.core.taghandler.AbstractHTMLTagHandler;
import de.knowwe.core.user.UserContext;
import de.knowwe.kdom.visitor.RenderKDOMVisitor;

public class KDOMRenderer extends AbstractHTMLTagHandler {

    public KDOMRenderer() {
        super("renderKDOM");
        this.setMaskJSPWikiSyntax(true);
    }

    @Override
    public String getDescription(UserContext user) {
        return Messages.getMessageBundle(user).getString("KnowWE.KDOMRenderer.description");
    }

    @Override
    public String renderHTML(String topic, UserContext user, Map<String, String> values, String web) {
        RenderKDOMVisitor v = new RenderKDOMVisitor();
        v.visit(Environment.getInstance().getArticle(web, topic).getRootSection());
        String data = "<div><h3>KDOM:</h3><tt>" + v.getRenderedKDOM() + "</tt></div>";
        return data;
    }
}
