package de.d3web.we.kdom.questionTree.dialog;

import de.d3web.we.kdom.KnowWEArticle;
import de.d3web.we.kdom.Section;
import de.d3web.we.kdom.questionTree.AnswerLine;
import de.d3web.we.kdom.rendering.CustomRenderer;
import de.d3web.we.kdom.rendering.RenderingMode;
import de.d3web.we.wikiConnector.KnowWEUserContext;

/**
 * QuestionTypeDeclarationRenderer Removes the icons from the default question
 * tree renderer. used within the collapsible view of the question tree in order
 * to simplify the view and clean the {@link AnswerLine} from unnecessary icons.
 * 
 * @author smark
 * @since 2010/03/09
 */
public class QuestionTypeDeclarationRenderer extends CustomRenderer {

    @Override
    public boolean doesApply(String user, String topic, RenderingMode type) {
        return true;
    }

    @Override
    public void render(KnowWEArticle article, Section sec, KnowWEUserContext user, StringBuilder string) {
    }
}
