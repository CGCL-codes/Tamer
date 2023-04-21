package org.tripcom.metadata.test.exampleentry;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import org.openrdf.model.impl.URIImpl;
import org.tripcom.integration.entry.ClientInfo;
import org.tripcom.integration.entry.TSAdapterEntry;
import org.tripcom.integration.entry.TripleEntry;

/**
 * 
 * this class generates a ts-adapter entry
 * as it will be written by the meta-data manager in case of a 
 * management api space create event
 * http://www.tripcom.org/internal/document/814
 *  
 * @author Joachim Adi Schuetz (adi.schuetz@sti2.at)
 * @version $Id: out_metadatamanager_814_tsadapter.java 451 2008-03-05 08:57:50Z atoz $
 * 
 */
public class out_metadatamanager_814_tsadapter extends EntryExamples {

    public static void main(String argv[]) {
        System.out.println("OutMetaMEntry generates TSAdapterEntry");
        TripleEntry trip = new TripleEntry(new URIImpl("ts://dummy/subject"), new URIImpl("http://triple/predicate"), new URIImpl("http://some/object"));
        data = new HashSet<TripleEntry>();
        data.add(trip);
        client = new ClientInfo();
        client.setClientID("testClient");
        timestamp = 123456789;
        opid = 87987L;
        URIImpl spaceURI = null;
        try {
            space = new URI("ts://spaceuri.structtest/");
            spaceURI = new URIImpl("ts://spaceuri.structtest/example/space");
            transid = new URI("http://342324.transaction.metacreator/id/7");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        URIImpl aleURI = new URIImpl(ALE_URIBase + client.getClientID() + "/" + timestamp);
        System.out.println(aleURI.toString());
        try {
            tripleset.add(new URI(aleURI.toString()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        structset = new HashSet<TripleEntry>();
        trip = new TripleEntry(spaceURI, isSubSPACEof, new URIImpl(space.toString()));
        structset.add(trip);
        trip = new TripleEntry(new URIImpl(space.toString()), hasSubSPACE, spaceURI);
        structset.add(trip);
        ent = new TSAdapterEntry(null, space, tripleset, client, opid, transid, structset);
    }

    public static TSAdapterEntry getEnt() {
        return (TSAdapterEntry) ent;
    }
}
