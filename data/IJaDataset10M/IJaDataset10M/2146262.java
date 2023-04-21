package be.lassi.lanbox.cuesteps;

import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Resets given layer.
 */
public class ResetLayer extends LayerCueStep {

    /**
     * Cue step type identifier.
     */
    public static final int ID = 17;

    /**
     * Constructs a new cue step.
     */
    public ResetLayer() {
        this(0);
    }

    /**
     * Constructs a new cue step.
     *
     * @param layerNumber the layer to be resetted
     */
    public ResetLayer(final int layerNumber) {
        super(layerNumber);
    }

    /**
     * Constructs a new cue step.
     *
     * @param bytes the raw cue step buffer
     */
    public ResetLayer(final byte[] bytes) {
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
        b.append("reset layer");
        if (getLayerNumber() > 0) {
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
        if (!result && object instanceof ResetLayer) {
            result = super.equals(object);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        HashCodeBuilder b = new HashCodeBuilder(1284476731, 786338585);
        b.append(super.hashCode());
        return b.toHashCode();
    }
}
