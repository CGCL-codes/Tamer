package de.l3s.boilerpipe.filters.english;

import java.util.Iterator;
import de.l3s.boilerpipe.BoilerpipeFilter;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.document.TextBlock;
import de.l3s.boilerpipe.document.TextDocument;
import de.l3s.boilerpipe.labels.DefaultLabels;

/**
 * Marks all blocks as "non-content" that occur after blocks that have been
 * marked {@link DefaultLabels#INDICATES_END_OF_TEXT}. These marks are ignored
 * unless a minimum number of words in content blocks occur before this mark (default: 60).
 * This can be used in conjunction with an upstream {@link TerminatingBlocksFinder}.
 * 
 * @author Christian Kohlschütter
 * @see TerminatingBlocksFinder
 */
public final class IgnoreBlocksAfterContentFilter extends HeuristicFilterBase implements BoilerpipeFilter {

    public static final IgnoreBlocksAfterContentFilter DEFAULT_INSTANCE = new IgnoreBlocksAfterContentFilter(60);

    public static final IgnoreBlocksAfterContentFilter INSTANCE_200 = new IgnoreBlocksAfterContentFilter(200);

    private final int minNumWords;

    /**
     * Returns the singleton instance for DeleteBlocksAfterContentFilter.
     */
    public static IgnoreBlocksAfterContentFilter getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    public IgnoreBlocksAfterContentFilter(final int minNumWords) {
        this.minNumWords = minNumWords;
    }

    public boolean process(TextDocument doc) throws BoilerpipeProcessingException {
        boolean changes = false;
        int numWords = 0;
        boolean foundEndOfText = false;
        for (Iterator<TextBlock> it = doc.getTextBlocks().iterator(); it.hasNext(); ) {
            TextBlock block = it.next();
            final boolean endOfText = block.hasLabel(DefaultLabels.INDICATES_END_OF_TEXT);
            if (block.isContent()) {
                numWords += getNumFullTextWords(block);
            }
            if (endOfText && numWords >= minNumWords) {
                foundEndOfText = true;
            }
            if (foundEndOfText) {
                changes = true;
                block.setIsContent(false);
            }
        }
        return changes;
    }
}
