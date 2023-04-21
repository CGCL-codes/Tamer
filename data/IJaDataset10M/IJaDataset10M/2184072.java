package be.lassi.lanbox.commands.layer;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import be.lassi.lanbox.domain.FadeType;

/**
 * Lanbox command the fade type of an layer.
 *
 */
public class LayerSetFadeType extends LayerCommand {

    /**
     * Lanbox command identifier.
     */
    public static final String ID = "4D";

    private final FadeType fadeType;

    /**
     * Constructs command from request buffer.
     *
     * @param request the lanbox request buffer
     */
    public LayerSetFadeType(final byte[] request) {
        super(request);
        int fadeTypeValue = getInt(5);
        fadeType = FadeType.get(fadeTypeValue);
    }

    /**
     * Constructs the command.
     *
     * @param layerNumber the layer number
     * @param fadeType the fade type to set
     */
    public LayerSetFadeType(final int layerNumber, final FadeType fadeType) {
        super("*4Deett#", layerNumber);
        set2(5, fadeType.getId());
        this.fadeType = fadeType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void appendCommand(final StringBuilder b) {
        b.append("LayerSetFadeType(layerNumber=");
        b.append(getLayerNumber());
        b.append(", fadeType=");
        b.append(fadeType.getString());
        b.append(")");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object object) {
        boolean result = object instanceof LayerSetFadeType;
        if (result) {
            LayerSetFadeType other = (LayerSetFadeType) object;
            EqualsBuilder b = new EqualsBuilder();
            b.appendSuper(super.equals(object));
            b.append(fadeType, other.fadeType);
            result = b.isEquals();
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        HashCodeBuilder b = new HashCodeBuilder(1301203611, 1880983483);
        b.appendSuper(super.hashCode());
        b.append(fadeType);
        return b.toHashCode();
    }
}
