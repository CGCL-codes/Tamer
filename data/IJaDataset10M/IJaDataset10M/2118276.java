package uk.ac.ed.ph.snuggletex.dombuilding;

import uk.ac.ed.ph.snuggletex.conversion.DOMBuilder;
import uk.ac.ed.ph.snuggletex.conversion.SnuggleParseException;
import uk.ac.ed.ph.snuggletex.definitions.GlobalBuiltins;
import uk.ac.ed.ph.snuggletex.tokens.CommandToken;
import uk.ac.ed.ph.snuggletex.tokens.FlowToken;
import java.util.List;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;

/**
 * This builds the content of a (fixed) {@link GlobalBuiltins#CMD_PARAGRAPH} token.
 * 
 * @author  David McKain
 * @version $Revision: 179 $
 */
public final class ParagraphBuilder implements CommandHandler {

    public void handleCommand(DOMBuilder builder, Element parentElement, CommandToken token) throws SnuggleParseException {
        buildParagraph(builder, parentElement, token.getArguments()[0].getContents());
    }

    /**
     * Builds a single paragraph, being careful to consider how best to represent it in the output
     * tree at the existing position.
     * 
     * @param builder
     * @param parentElement
     * @param inlineContent
     * @throws DOMException
     * @throws SnuggleParseException
     */
    private void buildParagraph(DOMBuilder builder, Element parentElement, List<FlowToken> inlineContent) throws SnuggleParseException {
        Element resultElement;
        boolean isInline;
        if (builder.isBuildingMathMLIsland()) {
            resultElement = parentElement;
            isInline = true;
        } else {
            resultElement = builder.appendXHTMLElement(parentElement, "p");
            isInline = false;
        }
        builder.handleTokens(resultElement, inlineContent, !isInline);
    }
}
