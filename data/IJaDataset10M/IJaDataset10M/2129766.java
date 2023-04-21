package wrapper.strategies;

import java.util.Iterator;
import org.deft.repository.ast.TokenNode;
import debug.AbstractDebuggable;
import wrapper.binding.TokenBindingContainer;
import wrapper.binding.TokenBindingKey;
import wrapper.options.LineWrapperOptions;
import wrapper.util.Caret;
import wrapper.util.CodeLine;

/**
 * Abstract base class for all line wrapping strategies. A line wrapping strategy is responsible for
 * fitting a single physical line into a specified character limit while obeying user preferences
 * specified in the line wrapper options. The scope is limited to a single line so that global layouts
 * (taking the entire code file into account) are impossible. This behavior is intended and is meant to
 * reduce complexity.
 *  
 * @author Christoph Seidl
 */
public abstract class LineWrappingStrategy extends AbstractDebuggable {

    private LineWrapperOptions lineWrapperOptions;

    private TokenBindingContainer tbc;

    private Caret caret;

    private int characterLimit;

    private TokenBindingKey[] cachedKeys;

    private int numberOfWraps;

    /**
	 * If necessary, wraps a single physical line to fit the specified character limit. The concrete
	 * behavior for wrapping is delegated to subclasses via <code>doWrapSinglePhysicalLine(..)</code>.
	 * 
	 * @param lineWrapperOptions The structure holding user preferences for the wrapping.
	 * @param tbc The container holding pre-calculated binding strengths and alignment columns for token combinations.
	 * @param physicalLine The line of code that is to be wrapped.
	 * @param caret The caret determining the position of tokens in the physical line after the wrapping.
	 * @param characterLimit The maximum number of allowed characters per line.
	 * 
	 * @return The number of wraps that were introduced into the given physical line.
	 */
    public int wrapSinglePhysicalLine(LineWrapperOptions lineWrapperOptions, TokenBindingContainer tbc, CodeLine physicalLine, Caret caret, int characterLimit) {
        initialize(lineWrapperOptions, tbc, caret, characterLimit);
        int lowerBoundForLineWraps = calculateLowerBoundForLineWraps(physicalLine);
        if (lowerBoundForLineWraps == 0) {
            physicalLine.placeTokenNodes(caret, 0, physicalLine.numberOfTokenNodes() - 1);
            return 0;
        }
        cacheTokenBindingKeys(physicalLine);
        doWrapSinglePhysicalLine(physicalLine);
        return numberOfWraps;
    }

    /**
	 * Initializes one run of the line wrapping strategy.
	 * 
	 * @param lineWrapperOptions The structure holding user preferences for the wrapping.
	 * @param tbc The container holding pre-calculated binding strengths and alignment columns for token combinations.
	 * @param caret The caret determining the position of tokens in the physical line after the wrapping.
	 * @param characterLimit The maximum number of allowed characters per line.
	 */
    private void initialize(LineWrapperOptions lineWrapperOptions, TokenBindingContainer tbc, Caret caret, int characterLimit) {
        this.lineWrapperOptions = lineWrapperOptions;
        this.tbc = tbc;
        this.caret = caret;
        this.characterLimit = characterLimit;
        numberOfWraps = 0;
    }

    /**
	 * Calculates the lower bound for the number of line wraps with the given character limit.
	 * Even though this lower bound is theoretically correct, there can be cases involving
	 * over-sized tokens, which are longer than the character limit permits. Imagine a
	 * character limit of 30 and a line consisting of three token nodes, each of which has
	 * a length of 40. Despite the calculated lower bound for the line wraps of 4, the
	 * token nodes have to be placed on 3 lines due to the inability of breaking up tokens.
	 * In this case, a solution exists that has less line breaks than the lower bound estimates.
	 * However, the important part is that this calculation is correct when no wraps have to
	 * be introduced.
	 * 
	 * @param physicalLine The line of code that is to be wrapped. 
	 * 
	 * @return The lower bound for the number of line wraps that have to be introduced.
	 */
    private int calculateLowerBoundForLineWraps(CodeLine physicalLine) {
        TokenNode lastTokenNode = physicalLine.get(physicalLine.numberOfTokenNodes() - 1);
        int numberOfCharacters = lastTokenNode.getEndCol();
        int lowerBoundForLineWraps = (int) Math.ceil(numberOfCharacters / (double) characterLimit) - 1;
        return lowerBoundForLineWraps;
    }

    /**
	 * Determines and saves the keys for token combinations in this physical line for later usage.
	 * 
	 * @param physicalLine The line of code that is to be wrapped.
	 */
    private void cacheTokenBindingKeys(CodeLine physicalLine) {
        cachedKeys = new TokenBindingKey[physicalLine.numberOfTokenNodes() - 1];
        Iterator<TokenNode> iterator = physicalLine.iterator();
        TokenNode oldTokenNode = null;
        int i = 0;
        while (iterator.hasNext()) {
            TokenNode currentTokenNode = iterator.next();
            if (oldTokenNode != null) {
                TokenBindingKey key = new TokenBindingKey(oldTokenNode, currentTokenNode);
                cachedKeys[i - 1] = key;
            }
            oldTokenNode = currentTokenNode;
            ++i;
        }
    }

    /**
	 * Perform the actual wrapping of the physical line. At this point it was determined
	 * that at least one wrap has to occur within this line of code.
	 * 
	 * @param physicalLine The line of code that is to be wrapped.
	 */
    protected abstract void doWrapSinglePhysicalLine(CodeLine physicalLine);

    /**
	 * Places the caret at the beginning of the wrapped line and logs that a wrap occurred.
	 * 
	 * @param firstWrappedNode The first token node on the (newly) wrapped partial line.
	 * @param alignmentCol The column the token node is aligned to on the new line.
	 */
    protected void updateCaretForLineWrap(TokenNode firstWrappedNode, int alignmentCol) {
        caret.advanceForLineWrap(firstWrappedNode, alignmentCol);
        ++numberOfWraps;
    }

    /**
	 * Tests whether a given node exceeds the set character limit.
	 * 
	 * @param tokenNode The token node that is to be tested.
	 * 
	 * @return <code>true</code> if the last character of the specified token node is beyond the character limit.
	 */
    protected boolean nodeExceedsCharacterLimit(TokenNode tokenNode) {
        int numberOfCharactersUpToTokenNode = caret.convertCol(tokenNode.getEndCol()) - 1;
        return (numberOfCharactersUpToTokenNode > characterLimit);
    }

    /**
	 * Retrieves the cached binding strength of the token combination of the tokens with (index - 1) and (index).
	 * 
	 * @param index The index of the token key.
	 * 
	 * @return The cached binding strength of the token combination of the tokens with (index - 1) and (index).
	 */
    protected int getBindingStrength(int index) {
        TokenBindingKey key = cachedKeys[index - 1];
        return tbc.getBindingStrength(key);
    }

    /**
	 * Retrieves the cached alignment column of the token combination of the tokens with (index - 1) and (index).
	 * 
	 * @param index The index of the token key.
	 * 
	 * @return The cached alignment column of the token combination of the tokens with (index - 1) and (index).
	 */
    protected int getAlignmentCol(int index) {
        TokenBindingKey key = cachedKeys[index - 1];
        return tbc.getAlignmentCol(key);
    }

    /**
	 * Returns the line wrapper options used for wrapping.
	 * 
	 * @return The line wrapper options used for wrapping.
	 */
    protected LineWrapperOptions getLineWrapperOptions() {
        return lineWrapperOptions;
    }

    /**
	 * Returns the caret used for wrapping.
	 * 
	 * @return The caret used for wrapping.
	 */
    protected Caret getCaret() {
        return caret;
    }

    /**
	 * Returns the character limit used for wrapping.
	 * 
	 * @return The character limit used for wrapping.
	 */
    protected int getCharacterLimit() {
        return characterLimit;
    }
}
