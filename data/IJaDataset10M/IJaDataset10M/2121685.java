package org.apache.mina.filter.codec.statemachine;

/**
 * {@link DecodingState} which skips space (0x20) and tab (0x09) characters.
 * 
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public abstract class LinearWhitespaceSkippingState extends SkippingState {

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean canSkip(byte b) {
        return b == 32 || b == 9;
    }
}
