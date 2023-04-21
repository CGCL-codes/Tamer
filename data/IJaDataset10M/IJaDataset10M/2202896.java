package jpicedt.format.input.util;

import java.util.*;
import java.io.*;

/**
 * An expression that can encompass a sub-expression it encloses with markers, e.g. "{" + sub-expression + "}"<br>
 * The interpret() methods work as follows :<br>
 * <ul>
 * <li>look up an endMarker matching beginMarker in Context.getRemainingSubstring (that is, skip enclosed blocks with the same markers type)
 * <li>set this endMarker as the new Context's endMarker
 * <li>save enclosed expression as "value", and interpret it
 * <li>restore old Context's endMarker
 * </ul>
 * @since jpicedt 1.3
 * @author Sylvain Reynal
 * @version $Id: EnclosingExpression.java,v 1.5 2011/07/23 05:17:50 vincentb1 Exp $
 */
public class EnclosingExpression extends AbstractRegularExpression {

    private String value, openingDelimiter, closingDelimiter;

    private AbstractRegularExpression child;

    private boolean noLineFeed;

    /**
	 * @param openingDelimiter the opening delimiter
	 * @param child the Expression that must be parsed inside the delimiters (can be null)
	 * @param closingDelimiter the closing delimiter
	 * @param noLineFeed if true, closingDelimiter must be on the same line as openingDelimiter
	 *
	 * A typical use would be : EnclosingExpression("{", new WildChar(ANY_CHAR),"}",false)
	 */
    public EnclosingExpression(String openingDelimiter, AbstractRegularExpression child, String closingDelimiter, boolean noLineFeed) {
        this.child = child;
        this.openingDelimiter = openingDelimiter;
        this.closingDelimiter = closingDelimiter;
        this.noLineFeed = noLineFeed;
    }

    /**
	 * @param openingDelimiter the opening delimiter
	 * @param child the Expression that must be parse inside the delimiter (can be null)
	 * @param closingDelimiter the closing delimiter
	 * Linefeeds are allowed.
	 */
    public EnclosingExpression(String openingDelimiter, AbstractRegularExpression child, String closingDelimiter) {
        this(openingDelimiter, child, closingDelimiter, false);
    }

    /**
	 * Change the Expression that must be parsed inside the delimiters 
	 * to the given expression (can be null)
	 */
    public void setChild(AbstractRegularExpression child) {
        this.child = child;
    }

    /**
	 * Call action() with value=enclosed string 
	 * @return TRUE if delimiters as well as inward expr have been found, 
	 */
    public boolean interpret(Context context) throws REParserException {
        if (!context.matchAndMove(openingDelimiter)) return false;
        context.mark();
        int inwardBlocks = 0;
        while (true) {
            if (context.matchAndMove(openingDelimiter)) {
                inwardBlocks++;
                continue;
            }
            if (context.matchAndMove(closingDelimiter)) {
                if (inwardBlocks == 0) {
                    int pastClosingDelimiter = context.getCaretPosition();
                    int newEndBlockMarker = pastClosingDelimiter - closingDelimiter.length();
                    context.reset();
                    context.enterBlock(newEndBlockMarker);
                    boolean result;
                    if (child != null) result = child.interpret(context); else result = true;
                    value = context.getBlockContent();
                    if (result == true) action(new ParserEvent(this, context, true, value));
                    context.exitBlock();
                    context.moveCaretTo(pastClosingDelimiter);
                    return result;
                } else {
                    inwardBlocks--;
                    continue;
                }
            }
            try {
                Character C = context.read();
                if (C == null) break;
                if (noLineFeed && C.charValue() == '\n') break;
            } catch (REParserException.EOF pe) {
                break;
            }
        }
        throw new REParserException.BlockMismatch(context, this);
    }

    /**
	 * @return the enclosed string (= block content as returned by Context)
	 */
    public String getEnclosedString() {
        return value;
    }

    /**
	 *
	 */
    public String toString() {
        return "[EnclosingExpression:\"" + openingDelimiter + "\", " + child + ", \"" + closingDelimiter + "\"]";
    }
}
