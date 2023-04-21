package org.tripcom.metadata.handler;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import net.jini.core.entry.Entry;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.model.vocabulary.RDF;
import org.tripcom.integration.entry.ClientInfo;
import org.tripcom.integration.entry.Error;
import org.tripcom.integration.entry.OutMetaMEntry;
import org.tripcom.integration.entry.RdMetaMEntry;
import org.tripcom.integration.entry.SpaceURI;
import org.tripcom.integration.entry.TSAdapterEntry;
import org.tripcom.integration.entry.TripleEntry;
import org.tripcom.metadata.TSOntologyConstants;

/**
 *
 * this class will be used to inject published triples and
 * executed operations for annotation into the metadata manager.
 *
 * @author Joachim Adi Schuetz (adi.schuetz@sti2.at)
 * @version $Id: MetadataCreator.java 2370 2009-03-09 16:11:40Z retokru $
 *
 */
public class MetadataCreator extends Handler implements TSOntologyConstants {

    Set<TripleEntry> data;

    Set<URI> uriset;

    SpaceURI space;

    Long operationId;

    URI transactionId;

    ClientInfo client;

    long timestamp;

    String creator;

    Boolean WRITE_MGMT_DATA_RESULT = false;

    /**
	 * handle request for meta-data
	 *
	 * @param entry - entry to be handled by this creator/handler
	 * @return Set<TripleEntry> - set of meta-data created, for testing purposes
	 */
    public Set<TripleEntry> handleRequest(Entry entry) {
        Set<TripleEntry> metaset = new HashSet<TripleEntry>();
        Set<URI> tripleset = new HashSet<URI>();
        URIImpl aleURI = null;
        if (entry instanceof RdMetaMEntry) {
            log.info("got a metadata-creation request for a read operation.");
            RdMetaMEntry rdEntry = (RdMetaMEntry) entry;
            data = rdEntry.getData();
            space = rdEntry.getSpace();
            uriset = rdEntry.getTripleset();
            client = rdEntry.getClientInfo();
            timestamp = rdEntry.getTimestamp();
            operationId = rdEntry.getOperationID();
            transactionId = rdEntry.getTransactionID();
            creator = null;
            if (client != null) {
                if (client.getClientID() != null) {
                    creator = client.getClientID();
                }
            }
            try {
                String encodedcreator = "";
                if (creator != null) {
                    encodedcreator = URLEncoder.encode(creator, "UTF-8") + "/";
                }
                String url = ALE_URIBase + encodedcreator + "" + timestamp;
                aleURI = new URIImpl(url);
                tripleset.add(new URI(aleURI.toString()));
            } catch (URISyntaxException e) {
                log.error(e.getStackTrace());
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                log.error(e.getStackTrace());
                e.printStackTrace();
            }
            metaset.addAll(generateAccesslogEntry(aleURI, ALEread));
            TSAdapterEntry tsAdapterEntry = new TSAdapterEntry(new HashSet<TripleEntry>(), space, tripleset, client, operationId, transactionId, metaset, false, WRITE_MGMT_DATA_RESULT, null);
            writer.write(tsAdapterEntry);
            log.info("wrote access-log entry to the ts apapter.");
            return metaset;
        } else {
            if (entry instanceof OutMetaMEntry) {
                log.info("got a metadata-creation request for a write operation.");
                OutMetaMEntry outEntry = (OutMetaMEntry) entry;
                data = outEntry.getData();
                space = outEntry.getSpace();
                operationId = outEntry.getOperationID();
                transactionId = outEntry.getTransactionID();
                client = outEntry.getClientInfo();
                try {
                    timestamp = outEntry.getTimestamp();
                } catch (NullPointerException ne) {
                    timestamp = 0L;
                }
                if (space == null) {
                    return error(operationId, Error.MM_create_noSpace, "this is a metadata creation request with an empty space.");
                }
                if (data == null) {
                    return error(operationId, Error.MM_create_noData, "this is a metadata creation request with a null or an empty data set.");
                }
                if (data.size() == 0) {
                    return error(operationId, Error.MM_create_noData, "this is a metadata creation request with a null or an empty data set.");
                }
                creator = null;
                if (client != null) {
                    if (client.getClientID() != null) {
                        creator = client.getClientID();
                    }
                }
                try {
                    String encodedcreator = "";
                    if (creator != null) {
                        encodedcreator = URLEncoder.encode(creator, "UTF-8") + "/";
                    }
                    String url = ALE_URIBase + encodedcreator + "" + timestamp;
                    aleURI = new URIImpl(url);
                    tripleset.add(new URI(aleURI.toString()));
                } catch (URISyntaxException e) {
                    log.error(e.getStackTrace());
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    log.error(e.getStackTrace());
                    e.printStackTrace();
                }
                metaset.addAll(generateAccesslogEntry(aleURI, ALEcreate));
                TSAdapterEntry tsAdapterEntry = new TSAdapterEntry(data, space, tripleset, client, operationId, transactionId, metaset, true, WRITE_MGMT_DATA_RESULT, null);
                writer.write(tsAdapterEntry);
                log.info("wrote data + annotation to the ts apapter for operations: " + operationId);
                tripleset.clear();
                metaset.clear();
                Iterator<TripleEntry> iterator = data.iterator();
                URIImpl ontology;
                TripleEntry tmpTrip;
                Set<TripleEntry> s = new HashSet<TripleEntry>();
                int index = 0;
                while (iterator.hasNext()) {
                    tmpTrip = iterator.next();
                    if (tmpTrip.getPredicate().equals(RDF.TYPE)) {
                        index = tmpTrip.getObject().stringValue().lastIndexOf('#');
                        if (index < 0) {
                            index = tmpTrip.getObject().stringValue().lastIndexOf('/');
                        }
                        if (index < 0) {
                            continue;
                        } else {
                            String ontologyStr = tmpTrip.getObject().stringValue().substring(0, index);
                            ontology = new URIImpl(ontologyStr);
                            tmpTrip = new TripleEntry(new URIImpl(space.toString()), isDefinedByOntology, ontology);
                            s.add(tmpTrip);
                        }
                    }
                }
                if (s.size() > 0) {
                    tsAdapterEntry = new TSAdapterEntry(s, structuralMetadataSpace, tripleset, client, operationId, transactionId, metaset, false, WRITE_MGMT_DATA_RESULT, null);
                    writer.write(tsAdapterEntry);
                    log.info("wrote ontology binding to metadataspace.");
                }
                return metaset;
            }
        }
        if (entry != null) {
            log.info("got metadata-creation for " + entry.getClass().getName() + " operation - do nothing at the moment.");
        } else {
            log.info("got metadata-creation for null entry - do not know what to do.");
        }
        return null;
    }

    /**
	 * generate the necessary triples for an access-log entry and return them in
	 * a set<TripleEntry>
	 *
	 * @param aleURI - URI of the access-log entry
	 * @param accessType - type of access to the data
	 * @return - set of TripleEntry containing the triples of the access-log entry
	 */
    private Set<TripleEntry> generateAccesslogEntry(URIImpl aleURI, URIImpl accessType) {
        Set<TripleEntry> resultset = new HashSet<TripleEntry>();
        TripleEntry tmpTrip;
        tmpTrip = new TripleEntry(aleURI, isALE, valFactory.createLiteral(timestamp));
        resultset.add(tmpTrip);
        if (creator != null) {
            tmpTrip = new TripleEntry(aleURI, ALE_CLIENT, valFactory.createLiteral(creator));
            resultset.add(tmpTrip);
        }
        tmpTrip = new TripleEntry(aleURI, ALE_DATE, valFactory.createLiteral(timestamp));
        resultset.add(tmpTrip);
        tmpTrip = new TripleEntry(aleURI, ALE_TYPE, accessType);
        resultset.add(tmpTrip);
        return resultset;
    }
}
