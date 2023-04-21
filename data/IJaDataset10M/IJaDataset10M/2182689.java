package uk.ac.shef.wit.trex.dataset;

import uk.ac.shef.wit.trex.util.ObjectIndex;

/**
 * Extends <i>Locator</i> by additionaly storing the URI of the resource.
 *
 * @author Jose' Iria, NLP Group, University of Sheffield
 *         (<a  href="mailto:J.Iria@dcs.shef.ac.uk" >email</a>)
 */
public class LocatorWithURI extends Locator {

    private static ObjectIndex _index = new ObjectIndex();

    protected Object _sourceDescriptor;

    public LocatorWithURI(final Object sourceURI, final int offset) {
        super(_index.add(sourceURI), offset);
        _sourceDescriptor = sourceURI;
    }

    public Object getSourceURI() {
        return _sourceDescriptor;
    }
}
