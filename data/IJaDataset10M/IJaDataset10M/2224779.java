package de.d3web.we.flow;

import java.util.Map;
import de.d3web.we.core.KnowWEArticleManager;
import de.d3web.we.core.KnowWEEnvironment;
import de.d3web.we.kdom.KnowWEArticle;
import de.d3web.we.kdom.KnowWEObjectType;
import de.d3web.we.kdom.Section;
import de.d3web.we.kdom.rendering.KnowWEDomRenderer;
import de.d3web.we.taghandler.AbstractHTMLTagHandler;
import de.d3web.we.wikiConnector.KnowWEUserContext;
import de.knowwe.core.renderer.ReRenderSectionMarkerRenderer;

/**
 * [{KnowWEPlugin Flowchart}]
 *
 * @author Florian Ziegler
 */
public class FlowchartStateTagHandler extends AbstractHTMLTagHandler {

    private final KnowWEDomRenderer<KnowWEObjectType> renderer;

    public FlowchartStateTagHandler() {
        super("diaflux");
        renderer = new ReRenderSectionMarkerRenderer<KnowWEObjectType>(new FlowchartStateRender());
    }

    @Override
    public String renderHTML(String topic, KnowWEUserContext user, Map<String, String> values, String web) {
        KnowWEArticleManager artManager = KnowWEEnvironment.getInstance().getArticleManager(web);
        KnowWEArticle article = artManager.getArticle(topic);
        StringBuilder string = new StringBuilder();
        String id = values.get("kdomid");
        Section<KnowWEObjectType> section = (Section<KnowWEObjectType>) article.findSection(id);
        renderer.render(article, section, user, string);
        return string.toString();
    }
}
