package bioweka.translators.protein;

import org.biojava.bio.symbol.SymbolList;
import org.biojava.bio.symbol.SymbolListViews;
import bioweka.core.sequence.GeneticCodeProperty;
import bioweka.translators.AbstractTranslationTableListSequenceTranslator;

/**
 * Sequence translator which translates RNA sequences into its amino acid
 * sequences. 
 * @author <a href="mailto:Martin.Szugat@GMX.net">Martin Szugat</a>
 * @version $Revision: 1.1 $
 */
public final class Translator extends AbstractTranslationTableListSequenceTranslator {

    /**
     * The unique class identifier. 
     */
    private static final long serialVersionUID = 3256440300560724530L;

    /**
     * The description of the protein translator component.
     */
    public static final String TRANSLATOR_GLOBAL_INFO = "Sequence translator which translates RNA sequences into its amino " + "acid sequences.";

    /**
     * Initializes the protein translator.
     * @throws Exception if no genetic codes are available.
     */
    public Translator() throws Exception {
        super(new GeneticCodeProperty());
    }

    /**
     * {@inheritDoc}
     */
    public String globalInfo() {
        return TRANSLATOR_GLOBAL_INFO;
    }

    /**
     * {@inheritDoc}
     */
    public SymbolList translate(SymbolList sequence) throws Exception {
        sequence = sequence.subList(1, sequence.length() - sequence.length() % 3);
        return super.translate(SymbolListViews.windowedSymbolList(sequence, 3));
    }
}
