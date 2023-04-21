package org.nakedobjects.remoting.client.facets;

import org.apache.log4j.Logger;
import org.nakedobjects.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.facets.DecoratingFacet;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionAddToFacet;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionAddToFacetAbstract;
import org.nakedobjects.remoting.data.common.IdentityData;
import org.nakedobjects.remoting.data.common.ObjectData;
import org.nakedobjects.remoting.exchange.SetAssociationRequest;
import org.nakedobjects.remoting.exchange.SetAssociationResponse;
import org.nakedobjects.remoting.facade.ServerFacade;
import org.nakedobjects.remoting.protocol.encoding.internal.ObjectEncoderDecoder;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.ConcurrencyException;

/**
 * A reflection peer for changing one-to-many fields remotely, instead of on the local machine. Any requests
 * to add or remove elements from the field will be passed over the network to the server for completion. Only
 * requests on persistent objects are passed to the server; on a transient object the request will always be
 * dealt with locally.
 * 
 * <p>
 * If any of the objects involved have been changed on the server by another process then a
 * ConcurrencyException will be passed back to the client and re-thrown.
 * </p>
 */
public final class CollectionAddToFacetWrapProxy extends CollectionAddToFacetAbstract implements DecoratingFacet<CollectionAddToFacet> {

    private static final Logger LOG = Logger.getLogger(CollectionAddToFacetWrapProxy.class);

    private final ServerFacade serverFacade;

    private final ObjectEncoderDecoder encoderDecoder;

    private final CollectionAddToFacet underlyingFacet;

    private final String name;

    public CollectionAddToFacetWrapProxy(final CollectionAddToFacet underlyingFacet, final ServerFacade connection, final ObjectEncoderDecoder encoderDecoder, final String name) {
        super(underlyingFacet.getFacetHolder());
        this.underlyingFacet = underlyingFacet;
        this.serverFacade = connection;
        this.encoderDecoder = encoderDecoder;
        this.name = name;
    }

    public CollectionAddToFacet getDecoratedFacet() {
        return underlyingFacet;
    }

    public void add(final NakedObject inObject, final NakedObject referencedAdapter) {
        if (inObject.isPersistent()) {
            try {
                final IdentityData targetReference = encoderDecoder.encodeIdentityData(inObject);
                final IdentityData associateReference = encoderDecoder.encodeIdentityData(referencedAdapter);
                SetAssociationRequest request = new SetAssociationRequest(getAuthenticationSession(), name, targetReference, associateReference);
                SetAssociationResponse response = serverFacade.setAssociation(request);
                ObjectData[] updates = response.getUpdates();
                encoderDecoder.decode(updates);
            } catch (final ConcurrencyException e) {
                throw ProxyUtil.concurrencyException(e);
            } catch (final NakedObjectException e) {
                LOG.error("remote exception: " + e.getMessage(), e);
                throw e;
            }
        } else {
            underlyingFacet.add(inObject, referencedAdapter);
        }
    }

    protected static AuthenticationSession getAuthenticationSession() {
        return NakedObjectsContext.getAuthenticationSession();
    }
}
