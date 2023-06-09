package org.apache.mina.filter.codec.statemachine;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.filter.codec.ProtocolDecoderException;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

/**
 * {@link DecodingState} which decodes <code>short</code> values in big-endian 
 * order (high bytes come first).
 * 
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public abstract class ShortIntegerDecodingState implements DecodingState {

    private int highByte;

    private int counter;

    /**
     * {@inheritDoc}
     */
    public DecodingState decode(IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        while (in.hasRemaining()) {
            switch(counter) {
                case 0:
                    highByte = in.getUnsigned();
                    break;
                case 1:
                    counter = 0;
                    return finishDecode((short) ((highByte << 8) | in.getUnsigned()), out);
                default:
                    throw new InternalError();
            }
            counter++;
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public DecodingState finishDecode(ProtocolDecoderOutput out) throws Exception {
        throw new ProtocolDecoderException("Unexpected end of session while waiting for a short integer.");
    }

    /**
     * Invoked when this state has consumed a complete <code>short</code>.
     * 
     * @param value the short.
     * @param out the current {@link ProtocolDecoderOutput} used to write 
     *        decoded messages.
     * @return the next state if a state transition was triggered (use 
     *         <code>this</code> for loop transitions) or <code>null</code> if 
     *         the state machine has reached its end.
     * @throws Exception if the read data violated protocol specification.
     */
    protected abstract DecodingState finishDecode(short value, ProtocolDecoderOutput out) throws Exception;
}
