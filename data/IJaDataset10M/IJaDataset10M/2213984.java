package de.d3web.we.kdom.questionTree.dialog;

import java.util.List;
import de.d3web.we.kdom.KnowWEArticle;
import de.d3web.we.kdom.KnowWEObjectType;
import de.d3web.we.kdom.Section;
import de.d3web.we.kdom.basic.PlainText;
import de.d3web.we.kdom.rendering.CustomRenderer;
import de.d3web.we.kdom.rendering.RenderingMode;
import de.d3web.we.utils.KnowWEUtils;
import de.d3web.we.wikiConnector.KnowWEUserContext;
import de.knowwe.core.renderer.FontColorRenderer;

/**
 * QuestionDefRenderer. Renders the {@link QuestionDefRenderer } in the
 * collapsible question tree view. Used to remove ...
 * 
 * @author smark
 * @since 2010/03/28
 */
public class QuestionDefRenderer extends CustomRenderer {

    @Override
    public boolean doesApply(String user, String topic, RenderingMode type) {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void render(KnowWEArticle article, Section sec, KnowWEUserContext user, StringBuilder string) {
        string.append(KnowWEUtils.maskHTML("<span"));
        string.append(" style='").append(FontColorRenderer.COLOR1).append("'");
        string.append(KnowWEUtils.maskHTML(">"));
        List<Section<? extends KnowWEObjectType>> children = sec.getChildren();
        for (Section<? extends KnowWEObjectType> section : children) {
            if (section.getObjectType() instanceof PlainText) {
                String cleaned = section.getOriginalText();
                if (cleaned.startsWith("\"")) {
                    cleaned = cleaned.replaceAll("\"", "");
                }
                string.append(cleaned);
            }
        }
        string.append(KnowWEUtils.maskHTML("</span>"));
    }
}
