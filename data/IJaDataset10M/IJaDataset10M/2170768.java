package be.lassi.lanbox.commands.layer;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Lanbox command to enable or disable solo on a given layer.
 *
 */
public class LayerSetSolo extends LayerCommand {

    /**
     * Lanbox command identifier.
     */
    public static final String ID = "4A";

    private final boolean enabled;

    /**
     * Constructs command from request buffer.
     *
     * @param request the lanbox request buffer
     */
    public LayerSetSolo(final byte[] request) {
        super(request);
        enabled = getBoolean(5);
    }

    /**
     * Constructs the command.
     *
     * @param layerNumber the layer number
     * @param enabled true if solo should be enabled
     */
    public LayerSetSolo(final int layerNumber, final boolean enabled) {
        super("*4Aeevv#", layerNumber);
        this.enabled = enabled;
        set2(5, enabled);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void appendCommand(final StringBuilder b) {
        b.append("LayerSetSolo(layerNumber=");
        b.append(getLayerNumber());
        b.append(", enabled=");
        b.append(enabled);
        b.append(")");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object object) {
        boolean result = object instanceof LayerSetSolo;
        if (result) {
            LayerSetSolo other = (LayerSetSolo) object;
            EqualsBuilder b = new EqualsBuilder();
            b.appendSuper(super.equals(object));
            b.append(enabled, other.enabled);
            result = b.isEquals();
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        HashCodeBuilder b = new HashCodeBuilder(337569909, -1898782397);
        b.appendSuper(super.hashCode());
        b.append(enabled);
        return b.toHashCode();
    }
}
