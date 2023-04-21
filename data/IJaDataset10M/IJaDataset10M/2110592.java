package net.sourceforge.gosp.dictionary.process.term;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import net.sourceforge.gosp.common.MDCSequence;
import net.sourceforge.gosp.dictionary.data.Term;
import net.sourceforge.gosp.dictionary.data.Variation;

public class MDCSequencer implements TermProcessor {

    private static final Logger logger = Logger.getLogger("MdC Sequencer");

    @Override
    public void process(List<Term> terms) throws TermProcessorException {
        logger.info("Sequencing MdC...");
        Iterator<Term> i = terms.iterator();
        Term term = null;
        String phrase = null;
        int removedTerms = 0, removedVariations = 0;
        while (i.hasNext()) {
            try {
                term = i.next();
                phrase = term.getPhrase();
                term.setMdcSequence(new MDCSequence(phrase));
            } catch (IllegalArgumentException e) {
                removedTerms++;
                i.remove();
                continue;
            }
            Iterator<Variation> v = term.getVariations().iterator();
            while (v.hasNext()) {
                try {
                    Variation variation = v.next();
                    phrase = variation.getPhrase();
                    variation.setMdcSequence(new MDCSequence(phrase));
                } catch (IllegalArgumentException e) {
                    removedVariations++;
                    v.remove();
                }
            }
        }
        if (removedTerms > 0) logger.warning("Removed " + removedTerms + " terms.");
        if (removedVariations > 0) logger.warning("Removed " + removedVariations + " variations.");
    }
}
