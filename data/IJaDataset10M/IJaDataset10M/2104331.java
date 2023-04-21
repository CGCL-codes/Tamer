package net.kano.partypad.pipeline.ports;

import java.util.Set;

/**
 * An object that has output ports.
 */
public interface OutputHolder {

    /**
     * Returns this object's output ports.
     *
     * @return this object's output ports
     */
    Set<OutputPort<?>> getOutputs();

    /**
     * Returns the output port associated with the given ID (specified by the
     * port's {@linkplain OutputPort#getUniqueID() unique ID}). If no input
     * with that name is present on this output holder, this method will return
     * {@code null}.
     *
     * @param id an object ID
     * @return the output port associated with the given ID
     */
    OutputPort<?> getOutput(String id);
}
