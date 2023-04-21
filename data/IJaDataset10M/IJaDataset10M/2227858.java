package org.personalsmartspace.cm.source.api.pss3p;

import java.io.Serializable;
import org.personalsmartspace.cm.model.api.pss3p.ICtxEntity;
import org.personalsmartspace.cm.source.api.pss3p.callback.ICtxSource;

/**
 * @author <a href="mailto:Korbinian.Frank@dlr.de">Korbinian Frank</a> (DLR)
 * @since 0.1.0
 */
public interface ICtxSourceManager {

    /**
     * Registers a context source with the CSM. A String ID will be returned via the callback methods of the caller.
     * 
     * @param name
     *            the name of the context source
     * @param contextType
     *            the type of data the source will store into the contextDB
     * @param caller
     *            the ICtxSource calling the method and receiving the identifier as result
     */
    public void register(String name, String contextType, ICtxSource caller);

    /**
     * With this method a context source provides updates of the registered context type.
     * 
     * @param identifier
     *            received by the register callback
     * @param data
     *            content for the registered context type
     * @param owner
     *            the entity, the data is referring to
     *
     * @see org.personalsmartspace.cm.model.api.pss3p.ICtxEntity
     */
    public void sendUpdate(String identifier, Serializable data, ICtxEntity owner);

    /**
     * Sending updates without specifying the data owner.
     * Used if the context source has no information about the ICtxEntity it is providing data for. The device owner will be used. instead of an explicitly stated owner
     * @see org.personalsmartspace.cm.source.api.pss3p.ICtxSourceManager#sendUpdate(String identifier, Serializable data, ICtxEntity owner)
     */
    public void sendUpdate(String identifier, Serializable data);

    /**
     * This method sends updates like {@link ICtxSourceManager#sendUpdate(String, Serializable, ICtxEntity)}, but provides additional Quality of Context (see {@link org.personalsmartspace.cm.model.api.pss3p.ICtxQuality}).
     * @param identifier
     * 		received by the register callback
     * @param data
     *            content for the registered context type
     * @param owner
     *            the entity, the data is referring to
     * @param inferred
     * 				boolean value referring to {@link org.personalsmartspace.cm.model.api.pss3p.ICtxQuality#setOrigin(org.personalsmartspace.cm.model.api.pss3p.CtxOriginType)}. 
     * @param precision
     * 				double value for the precision or probability of correctness of the data value
     * @param updateFrequency
     * 				double value in Hz.
     */
    public void sendUpdate(String identifier, Serializable data, ICtxEntity owner, boolean inferred, double precision, double updateFrequency);

    /**
     * Unregisters a context source when it stops its service.
     * 
     * @param identifier
     *            received by the register call
     * @return true, if unregistering was successful
     */
    public boolean unregister(String identifier);
}
