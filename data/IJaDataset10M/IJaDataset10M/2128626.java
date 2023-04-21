package uk.ac.ed.ph.snuggletex.dombuilding;

import uk.ac.ed.ph.snuggletex.ErrorCode;
import uk.ac.ed.ph.snuggletex.SnuggleLogicException;
import uk.ac.ed.ph.snuggletex.conversion.DOMBuilder;
import uk.ac.ed.ph.snuggletex.conversion.SnuggleParseException;
import uk.ac.ed.ph.snuggletex.definitions.AccentMap;
import uk.ac.ed.ph.snuggletex.definitions.LaTeXMode;
import uk.ac.ed.ph.snuggletex.semantics.InterpretationType;
import uk.ac.ed.ph.snuggletex.semantics.MathIdentifierInterpretation;
import uk.ac.ed.ph.snuggletex.tokens.CommandToken;
import uk.ac.ed.ph.snuggletex.tokens.FlowToken;
import uk.ac.ed.ph.snuggletex.tokens.TokenType;
import java.util.List;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;

/**
 * This builder handles applying accents to characters. It has been written to work in both
 * math and text modes - do note that some accents only apply in one of these modes.
 *
 * @author  David McKain
 * @version $Revision: 179 $
 */
public final class AccentBuilder implements CommandHandler {

    /** AccentMap to use to look up single characters (may be null) */
    private final AccentMap accentMap;

    /** Combining character to use when forming MathML accents (0 if this builder doesn't support MATH mode) */
    private final char combiningCharacter;

    /** Name of resulting MathML element (null if this builder doesn't support MATH mode) */
    private final String mathMLElementName;

    public AccentBuilder(final AccentMap accentMap, final char combiningCharacter, final String mathMLElementName) {
        this.accentMap = accentMap;
        this.combiningCharacter = combiningCharacter;
        this.mathMLElementName = mathMLElementName;
    }

    public AccentBuilder(AccentMap accentMap) {
        this(accentMap, (char) 0, null);
    }

    public void handleCommand(DOMBuilder builder, Element parentElement, CommandToken token) throws SnuggleParseException {
        if (token.getLatexMode() == LaTeXMode.MATH) {
            if (mathMLElementName == null) {
                throw new SnuggleLogicException("Unexpected logic branch - unexpected accent found in MATH mode");
            }
            handleCommandMathMode(builder, parentElement, token);
        } else {
            handleCommandTextMode(builder, parentElement, token);
        }
    }

    /**
     * Handles accents in MATH mode.
     * 
     * @param builder
     * @param parentElement
     * @param token
     * @throws DOMException
     * @throws SnuggleParseException
     */
    public void handleCommandMathMode(DOMBuilder builder, Element parentElement, CommandToken token) throws SnuggleParseException {
        List<FlowToken> content = token.getArguments()[0].getContents();
        char mathAccent = 0;
        if (accentMap != null && content.size() == 1 && content.get(0).isInterpretationType(InterpretationType.MATH_IDENTIFIER)) {
            MathIdentifierInterpretation mathIdentifier = (MathIdentifierInterpretation) content.get(0).getInterpretation();
            String identifier = mathIdentifier.getName();
            if (identifier.length() == 1) {
                mathAccent = accentMap.getAccentedMathChar(identifier.charAt(0));
            }
        }
        if (mathAccent != 0) {
            builder.appendMathMLIdentifierElement(parentElement, Character.toString(mathAccent));
        } else {
            Element result = builder.appendMathMLElement(parentElement, mathMLElementName);
            result.setAttribute("accent", "true");
            builder.handleMathTokensAsSingleElement(result, content);
            builder.appendMathMLOperatorElement(result, Character.toString(combiningCharacter));
        }
    }

    /**
     * LaTeX only adds an accent to the first character of the first token, and only if it is
     * text. If not, it outputs a lone accent character before the first token.
     * <p>
     * For simplicity, we will only allow a single simple text token as the argument of this command
     * since anything else is really a nasty hack and does nothing for the output in LaTeX.
     */
    public void handleCommandTextMode(DOMBuilder builder, Element parentElement, CommandToken token) throws SnuggleParseException {
        List<FlowToken> contents = token.getArguments()[0].getContents();
        if (contents.size() == 1 && contents.get(0).getType() != TokenType.TEXT_MODE_TEXT) {
            builder.appendOrThrowError(parentElement, token, ErrorCode.TDETA0);
            return;
        }
        CharSequence textContent = contents.isEmpty() ? null : contents.get(0).getSlice().extract();
        if (textContent == null || textContent.length() == 0) {
            builder.appendOrThrowError(parentElement, token, ErrorCode.TDETA1);
            return;
        }
        char firstChar = textContent.charAt(0);
        char accentedFirstChar = accentMap.getAccentedTextChar(firstChar);
        if (accentedFirstChar != 0) {
            String replacedContent = accentedFirstChar + textContent.subSequence(1, textContent.length()).toString();
            builder.appendTextNode(parentElement, replacedContent, false);
        } else {
            builder.appendOrThrowError(parentElement, token, ErrorCode.TDETA2, Character.toString(firstChar));
        }
    }
}
