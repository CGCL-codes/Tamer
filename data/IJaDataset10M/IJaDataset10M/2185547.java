package gnu.javax.rmi.CORBA;

import java.util.HashSet;
import javax.rmi.CORBA.Tie;

/**
 * Represents a Tie, connected to possibly multiple invocation targets.
 * 
 * @author Audrius Meskauskas (AudriusA@Bioinformatics.org)
 */
public class TieTargetRecord {

    /**
   * The associated Tie.
   */
    public final Tie tie;

    /**
   * The objects, exposing the tie.
   */
    public HashSet targets = new HashSet();

    /**
   * Create a new record.
   */
    public TieTargetRecord(Tie a_tie) {
        tie = a_tie;
    }

    /**
   * Add a target.
   */
    public void add(Object target) {
        targets.add(target);
    }

    /**
   * Remove target.
   */
    public void remove(Object target) {
        targets.remove(target);
    }

    /**
   * Return true if the tie has no associated invocation targets.
   */
    public boolean unused() {
        return targets.size() == 0;
    }
}
