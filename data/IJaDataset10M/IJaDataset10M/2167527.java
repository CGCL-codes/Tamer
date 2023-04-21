package uk.ac.ed.ph.snuggletex.dombuilding;

import uk.ac.ed.ph.snuggletex.ErrorCode;
import uk.ac.ed.ph.snuggletex.SnuggleLogicException;
import uk.ac.ed.ph.snuggletex.conversion.DOMBuilder;
import uk.ac.ed.ph.snuggletex.conversion.SnuggleParseException;
import uk.ac.ed.ph.snuggletex.definitions.BuiltinEnvironment;
import uk.ac.ed.ph.snuggletex.definitions.GlobalBuiltins;
import uk.ac.ed.ph.snuggletex.definitions.Globals;
import uk.ac.ed.ph.snuggletex.tokens.CommandToken;
import uk.ac.ed.ph.snuggletex.tokens.EnvironmentToken;
import uk.ac.ed.ph.snuggletex.tokens.FlowToken;
import uk.ac.ed.ph.snuggletex.tokens.TokenType;
import org.w3c.dom.Element;

/**
 * This handles LaTeX list environments (i.e. <tt>itemize</tt> and <tt>enumerate</tt>).
 * 
 * @author  David McKain
 * @version $Revision: 179 $
 */
public final class ListEnvironmentBuilder implements EnvironmentHandler, CommandHandler {

    /**
     * Builds the actual List environment
     */
    public void handleEnvironment(DOMBuilder builder, Element parentElement, EnvironmentToken token) throws SnuggleParseException {
        String listElementName = null;
        BuiltinEnvironment environment = token.getEnvironment();
        if (environment == GlobalBuiltins.ENV_ITEMIZE) {
            listElementName = "ul";
        } else if (environment == GlobalBuiltins.ENV_ENUMERATE) {
            listElementName = "ol";
        } else {
            throw new SnuggleLogicException("No logic to handle list environment " + environment.getTeXName());
        }
        Element listElement = builder.appendXHTMLElement(parentElement, listElementName);
        for (FlowToken contentToken : token.getContent()) {
            if (contentToken.isCommand(GlobalBuiltins.CMD_LIST_ITEM)) {
                builder.handleToken(listElement, contentToken);
            } else if (contentToken.getType() == TokenType.ERROR) {
                builder.handleToken(parentElement, contentToken);
            } else if (contentToken.getType() == TokenType.COMMENT) {
                builder.handleToken(listElement, contentToken);
            } else {
                throw new SnuggleLogicException("List environments can only contain list items - this should have been handled earlier");
            }
        }
    }

    /**
     * Builds list items.
     */
    public void handleCommand(DOMBuilder builder, Element parentElement, CommandToken itemToken) throws SnuggleParseException {
        if (itemToken.isCommand(GlobalBuiltins.CMD_LIST_ITEM)) {
            if (builder.isParentElement(parentElement, Globals.XHTML_NAMESPACE, "ul", "ol")) {
                Element listItem = builder.appendXHTMLElement(parentElement, "li");
                builder.handleTokens(listItem, itemToken.getArguments()[0], true);
            } else {
                throw new SnuggleLogicException("List item outside environment - this should not have occurred");
            }
        } else if (itemToken.isCommand(GlobalBuiltins.CMD_ITEM)) {
            builder.appendOrThrowError(parentElement, itemToken, ErrorCode.TDEL00);
        }
    }
}
