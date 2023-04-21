package bioweka.filters.sequence;

import bioweka.core.sequence.SequenceHandler;
import bioweka.core.sequence.SequenceProperty;
import bioweka.filters.meta.AbstractMultipleFilter;

/**
 * Abstract base class for filter that operate on sequences and use delegation
 * to process the input instances.
 * @author <a href="mailto:Martin.Szugat@GMX.net">Martin Szugat</a>
 * @version $Revision: 1.1 $
 */
public abstract class AbstractDelegationSequenceFilter extends AbstractMultipleFilter implements SequenceHandler {

    /**
     * The sequence property.
     */
    private SequenceProperty sequenceProperty = null;

    /**
     * Initializes the sequence filter. 
     */
    public AbstractDelegationSequenceFilter() {
        super();
        sequenceProperty = new SequenceProperty();
        manager().addProperty(sequenceProperty);
    }

    /**
     * Returns the sequence property.
     * @return the sequence property.
     */
    protected final SequenceProperty sequenceProperty() {
        return sequenceProperty;
    }

    /**
     * {@inheritDoc}
     */
    public Object clone() {
        AbstractDelegationSequenceFilter clone = (AbstractDelegationSequenceFilter) super.clone();
        clone.sequenceProperty = (SequenceProperty) sequenceProperty.clone();
        clone.manager().addProperty(clone.sequenceProperty);
        return clone;
    }

    /**
     * {@inheritDoc}
     */
    public final String getSequenceAttribute() {
        return sequenceProperty.getSequenceAttribute();
    }

    /**
     * {@inheritDoc}
     */
    public final String sequenceAttributeTipText() {
        return sequenceProperty.sequenceAttributeTipText();
    }

    /**
     * {@inheritDoc}
     */
    public final void setSequenceAttribute(String sequenceAttributeIndex) throws Exception {
        sequenceProperty.setSequenceAttribute(sequenceAttributeIndex);
    }
}
