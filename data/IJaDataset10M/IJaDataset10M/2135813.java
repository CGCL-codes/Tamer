package org.ikasan.spec.wiretap;

/**
 * Wiretap Serialiser service.
 * 
 * @author Ikasan Development Team
 *
 */
public interface WiretapSerialiser<S> {

    public byte[] serialise(S source);
}
