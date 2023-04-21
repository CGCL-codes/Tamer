package be.lassi.lanbox.cuesteps;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import be.lassi.util.Hex;

/**
 * Load a cue list at a cue step in a layer.  If the layer was running a
 * cue list, it will continue running with given cue list and step.  If the
 * layer number is 0, then the layer executing this cue step is the target.
 */
public class GoLayerCueList extends LayerCueStep {

    /**
     * The cue list to load.
     */
    private int cueListNumber;

    /**
     * The cue step within the cue list to execute.
     */
    private int cueStepNumber;

    /**
     * Cue step type identifier.
     */
    public static final int ID = 10;

    /**
     * Constructs a new cue step.
     */
    public GoLayerCueList() {
        this(0, 0, 0);
    }

    /**
     * Constructs a new cue step.
     *
     * @param layerNumber the layer to load the cue list on
     * @param cueListNumber the cue list to load
     * @param cueStepNumber the cue step to execute
     */
    public GoLayerCueList(final int layerNumber, final int cueListNumber, final int cueStepNumber) {
        super(layerNumber);
        this.cueListNumber = cueListNumber;
        this.cueStepNumber = cueStepNumber;
    }

    public GoLayerCueList(final byte[] bytes) {
        super(bytes);
        cueListNumber = Hex.getInt4(bytes, 4);
        cueStepNumber = Hex.getInt(bytes, 8);
    }

    @Override
    public byte[] getBytes() {
        byte[] bytes = super.getBytes();
        Hex.set4(bytes, 4, cueListNumber);
        Hex.set2(bytes, 8, cueStepNumber);
        return bytes;
    }

    /**
     * Gets the cue list to load.
     *
     * @return the cue list to load
     */
    public int getCueListNumber() {
        return cueListNumber;
    }

    /**
     * Sets the cue list to load.
     *
     * @param cueListNumber the cue list to load
     */
    public void setCueListNumber(final int cueListNumber) {
        this.cueListNumber = cueListNumber;
    }

    /**
     * Gets the cue step to execute.
     *
     * @return the cue step to execute
     */
    public int getCueStepNumber() {
        return cueStepNumber;
    }

    /**
     * Sets the cue step to execute.
     *
     * @param cueStepNumber the cue step to execute
     */
    public void setCueStepNumber(final int cueStepNumber) {
        this.cueStepNumber = cueStepNumber;
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
        b.append("go ");
        b.append(cueListNumber);
        b.append(".");
        b.append(cueStepNumber);
        if (getLayerNumber() > 0) {
            b.append(" in layer ");
            b.append(getLayerName());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(getClass().getName());
        b.append("(layerNumber=");
        b.append(getLayerNumber());
        b.append(", cueListNumber=");
        b.append(getCueListNumber());
        b.append(", cueStepNumber=");
        b.append(getCueStepNumber());
        b.append(")");
        return b.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object object) {
        boolean result = object == this;
        if (!result && object instanceof GoLayerCueList) {
            GoLayerCueList other = (GoLayerCueList) object;
            EqualsBuilder b = new EqualsBuilder();
            b.appendSuper(super.equals(object));
            b.append(cueListNumber, other.cueListNumber);
            b.append(cueStepNumber, other.cueStepNumber);
            result = b.isEquals();
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        HashCodeBuilder b = new HashCodeBuilder(1017851449, 477719939);
        b.append(super.hashCode());
        b.append(cueListNumber);
        b.append(cueStepNumber);
        return b.toHashCode();
    }
}
