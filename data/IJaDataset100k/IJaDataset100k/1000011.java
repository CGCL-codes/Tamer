package uk.ac.shef.wit.aleph.validation;

import uk.ac.shef.wit.aleph.dataset.Dataset;
import uk.ac.shef.wit.aleph.AlephException;
import java.util.List;
import java.util.Collections;

/**
 * Convenience utility class that enables specifying that no split should be used.
 *
 * @author Jose' Iria, NLP Group, University of Sheffield
 *         (<a  href="mailto:J.Iria@dcs.shef.ac.uk" >email</a>)
 */
public class SplitNone implements Split {

    public List<Dataset> split(final Dataset dataset) throws AlephException {
        return Collections.singletonList(dataset);
    }

    public SplitObject getObject() {
        return new SplitObjectIdentity();
    }

    public SplitMeasure getMeasure() {
        return new SplitMeasureUnit();
    }
}
