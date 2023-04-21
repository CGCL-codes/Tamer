package bioweka.aligners;

import bioweka.core.components.CloneableComponent;

/**
 * Interface to a class that implements an alignment algorithm for aligning 
 * sequences.
 * @author <a href="mailto:Martin.Szugat@GMX.net">Martin Szugat</a>
 * @version $Revision: 1.1 $
 */
public interface Aligner extends CloneableComponent {

    double align(String targetSequence, String templateSequence) throws Exception;
}
