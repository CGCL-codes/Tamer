package org.tripcom.security.metadata;

import static org.tripcom.security.util.Checks.checkNotNull;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tripcom.integration.entry.Component;
import org.tripcom.integration.entry.KernelAddress;
import org.tripcom.integration.entry.MetaDataKind;
import org.tripcom.integration.entry.MetaMQueryEntry;
import org.tripcom.integration.entry.MetaMResultEntry;
import org.tripcom.integration.entry.SpaceURI;
import org.tripcom.integration.entry.Timeout;
import org.tripcom.security.bus.Bus;
import org.tripcom.security.exceptions.ExternalFailureException;
import org.tripcom.security.exceptions.InternalFailureException;
import org.tripcom.security.util.Util;

/**
 * Metadata strategy that delegates to the Metadata Manager kernel component.
 * <p>
 * This class access the Metadata Manager component in order to retrieve the
 * metadata required by the Security Manager.
 * </p>
 * 
 * @author Francesco Corcoglioniti &lt;francesco.corcoglioniti@cefriel.it&gt;
 */
public class MetadataManagerMetadataStrategy implements MetadataStrategy {

    /** The shared log object. */
    private static Log log = LogFactory.getLog(MetadataManagerMetadataStrategy.class);

    /** The space corresponding to the internal area. */
    private Bus bus;

    /** The timeout to be used when contacting the Metadata Manager. */
    private long queryTimeout;

    /**
     * Create a new instance given the dependency provided. *
     * 
     * @param bus the space corresponding to the internal area (not null).
     * @param queryTimeout the timeout to be used when contacting the Metadata
     *            Manager.
     */
    public MetadataManagerMetadataStrategy(Bus bus, long queryTimeout) {
        checkNotNull(bus, "Null bus");
        this.bus = bus;
        this.queryTimeout = queryTimeout;
    }

    /**
     * {@inheritDoc} This method queries the Metadata Manager and returns the
     * spaces matching the request. This method sends a {@link MetaMQueryEntry}
     * to the Metadata Manager, requesting the spaces that match the specified
     * {@link MetaDataKind} for an optional reference space. Then the method
     * waits for a {@link MetaMResultEntry} response, carrying the requested
     * subspaces.
     */
    @SuppressWarnings("unchecked")
    public Map<SpaceURI, KernelAddress> getMetadata(SpaceURI referenceSpace, MetaDataKind metadataKind) {
        checkNotNull(metadataKind, "Null metadata kind");
        if (metadataKind != MetaDataKind.LIST_ROOTSPACES) {
            checkNotNull(referenceSpace, "Null reference space");
        }
        if ((metadataKind == MetaDataKind.GET_MANAGING_KERNEL_IF_KNOWN) && referenceSpace.isRootSpace()) {
            return Util.makeMap(referenceSpace, referenceSpace.getKernelAddressOfRootSpace());
        }
        long timestamp = System.currentTimeMillis();
        long operationId = System.nanoTime();
        if (log.isInfoEnabled()) {
            log.info("Querying Metadata Manager for " + metadataKind + (referenceSpace == null ? "" : " of " + referenceSpace));
        }
        MetaMQueryEntry query = new MetaMQueryEntry();
        query.kind = metadataKind;
        query.operationID = operationId;
        query.space = referenceSpace;
        query.timeout = new Timeout(queryTimeout, timestamp);
        bus.write(query, Bus.NO_TIMEOUT);
        MetaMResultEntry response = null;
        try {
            MetaMResultEntry template = new MetaMResultEntry();
            template.operationID = operationId;
            response = bus.take(template, queryTimeout);
        } catch (InterruptedException ex) {
            throw new InternalFailureException("Operation aborted due to Security Manager shutdown");
        }
        if (response == null) {
            throw new ExternalFailureException(Component.METADATAMANAGER, "Timeout occured while querying Metadata Manager");
        }
        Map<SpaceURI, KernelAddress> result = new HashMap<SpaceURI, KernelAddress>();
        if (metadataKind == MetaDataKind.GET_MANAGING_KERNEL_IF_KNOWN) {
            result.put(referenceSpace, response.managingKernel);
        } else if ((metadataKind == MetaDataKind.LIST_KNOWN_SUBSPACES) || (metadataKind == MetaDataKind.LIST_DEEPEST_KNOWN_SUBSPACES)) {
            if (response.spacesAndTheirManagingKernels != null) {
                result.putAll(response.spacesAndTheirManagingKernels);
            } else {
                log.warn("Incorrect Metadata Manager response: field " + "response.spacesAndTheirManagingKernels is null");
            }
        } else if ((metadataKind == MetaDataKind.LIST_ROOTSPACES) || (metadataKind == MetaDataKind.LIST_SUPERSPACES) || (metadataKind == MetaDataKind.SIMILAR) || (metadataKind == MetaDataKind.SEEALSO) || (metadataKind == MetaDataKind.RELATED)) {
            Map<SpaceURI, KernelAddress> spaceToKernel;
            spaceToKernel = response.spacesAndTheirManagingKernels;
            if (spaceToKernel == null) {
                spaceToKernel = Collections.EMPTY_MAP;
            }
            if (response.result != null) {
                for (SpaceURI space : response.result) {
                    KernelAddress kernel = spaceToKernel.get(space);
                    if ((kernel == null) && space.isRootSpace()) {
                        kernel = space.getKernelAddressOfRootSpace();
                    }
                    result.put(space, kernel);
                }
            } else {
                log.warn("Incorrect Metadata Manager response: " + "field response.result is null");
            }
        } else {
            throw new InternalFailureException("Don't know how to interpret " + metadataKind + " response");
        }
        return Collections.unmodifiableMap(result);
    }
}
