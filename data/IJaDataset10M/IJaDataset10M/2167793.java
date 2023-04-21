package fr.ens.transcriptome.teolenn.sequence.filter;

import fr.ens.transcriptome.teolenn.Module;
import fr.ens.transcriptome.teolenn.sequence.Sequence;

/**
 * This interface define a sequence filter.
 * @author Laurent Jourdren
 */
public interface SequenceFilter extends Module {

    /**
   * Tests whether or not the specified sequence should be accepted.
   * @param sequence Sequence to test
   * @return true if and only if the specified sequence should be accepted
   */
    boolean accept(Sequence sequence);
}
