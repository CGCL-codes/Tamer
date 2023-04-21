package bioweka.core.mappers;

import org.biojava.bio.seq.Sequence;
import org.biojava.bio.seq.io.SequenceBuilderFactory;
import org.biojava.bio.symbol.Alphabet;
import bioweka.core.components.CloneableComponent;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Interface to a component that maps sequence annotations and features into 
 * instance attributes.
 * @author <a href="mailto:Martin.Szugat@GMX.net">Martin Szugat</a>
 * @version $Revision: 1.1 $
 */
public interface SequenceMapper extends CloneableComponent {

    Sequence asSequence(Instance instance, Alphabet defaultAlphabet) throws Exception;

    Instance asInstance(Sequence sequence, Instances structure) throws Exception;

    /**
     * Lists the attributes which are expected by the 
     * {@link #asSequence(Instance, Alphabet)} method and set for an instance
     * by the {@link #asInstance(Sequence, Instances)} method.
     * @param attributes a list of {@link weka.core.Attribute} objects.
     * @throws NullPointerException if <code>attributes</code> is
     * <code>null</code>.
     */
    void listAttributes(FastVector attributes) throws NullPointerException;

    SequenceBuilderFactory sequenceBuilderFactory();
}
