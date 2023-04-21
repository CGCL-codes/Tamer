package be.lassi.lanbox.cuesteps;

import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Continues execution of the current cue list in the target layer
 * at the next cue step.
 */
public class GoNext extends LayerCueStep {

    /**
     * Cue step type identifier.
     */
    public static final int ID = 21;

    /**
     * Constructs a new cue step.
     */
    public GoNext() {
        this(0);
    }

    /**
     * Constructs a new cue step.
     *
     * @param layerNumber the layer number
     */
    public GoNext(final int layerNumber) {
        super(layerNumber);
    }

    /**
     * Constructs a new cue step.
     *
     * @param bytes the raw cue step buffer
     */
    public GoNext(final byte[] bytes) {
        super(bytes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getId() {
        return ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void append(final StringBuilder b) {
        super.append(b);
        b.append("go next");
        if (getLayerNumber() > 0) {
            b.append(" in layer");
            b.append(" ");
            b.append(getLayerName());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object object) {
        boolean result = object == this;
        if (!result && object instanceof GoNext) {
            result = super.equals(object);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        HashCodeBuilder b = new HashCodeBuilder(1919540271, 613549841);
        b.append(super.hashCode());
        return b.toHashCode();
    }
}
