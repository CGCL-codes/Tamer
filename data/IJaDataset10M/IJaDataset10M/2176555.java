package eu.popeye.middleware.dataSearch;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import javax.naming.event.EventContext;
import javax.swing.JTextArea;
import org.apache.lucene.index.Term;
import eu.popeye.middleware.dataSharing.Metadata;
import eu.popeye.middleware.groupmanagement.management.Workgroup;
import eu.popeye.middleware.groupmanagement.membership.Member;
import eu.popeye.networkabstraction.communication.ApplicationMessageListener;
import eu.popeye.networkabstraction.communication.CommunicationChannel;
import eu.popeye.networkabstraction.communication.TimeoutException;
import eu.popeye.networkabstraction.communication.message.PopeyeMessage;

/**
 * This class reperesents a distributed Search Engine. It must be associated to a Shared Space
 * @author Francisco Antonio Bas Esparza
 *
 */
public class SearchEngine implements ApplicationMessageListener {

    private static final int timeout = 5000;

    private static final String CHANNEL_00 = "DataSearch 00";

    private static final String CHANNEL_01 = "DataSearch 01";

    private Workgroup group;

    private CommunicationChannel requestChannel;

    private CommunicationChannel answerChannel;

    private EventContext namingService;

    private RemoteQueryBroker remoteQueryBroker;

    private Index index;

    private QueryProvider queryProvider;

    private Member mySelf;

    private int expectedAnswers;

    private double expectedQueryID;

    private Collection<HitsCollection> answersCollection;

    boolean logActive;

    JTextArea logEditor;

    /**
	 * Create a Search engine
	 * @param mySelf my own identity 
	 * @param group communication group
	 * @param indexDirectory local path where the index is located
	 */
    public SearchEngine(Member mySelf, Workgroup group, String indexDirectory) {
        this.mySelf = mySelf;
        this.group = group;
        group.createNamedCommunicationChannel(CHANNEL_00);
        this.requestChannel = group.getNamedCommunicationChannel(CHANNEL_00);
        group.createNamedCommunicationChannel(CHANNEL_01);
        this.answerChannel = group.getNamedCommunicationChannel(CHANNEL_01);
        this.namingService = group.getContext();
        this.remoteQueryBroker = new RemoteQueryBroker(this.requestChannel, this.answerChannel);
        this.index = new Index(indexDirectory, this.remoteQueryBroker);
        this.answerChannel.addApplicationMessageListener(this);
    }

    /**
	 * Removes all the indexed metadata
	 *
	 */
    public void reset() {
        HitsCollection hits = this.search(Index.INTERNAL_PATH + ":/*", Metadata.PATH, true);
        Iterator it = hits.hits.keySet().iterator();
        while (it.hasNext()) {
            String path = (String) it.next();
            this.index.delete(new Term(index.INTERNAL_PATH, path));
        }
    }

    /**
	 * returns the Inverted inde asociated to this Search Engine
	 * @return
	 */
    public Index getIndex() {
        return this.index;
    }

    /**
	 * Indexes a metadata
	 * @param md
	 */
    public void addMetadata(Metadata md) {
        this.index.add(md);
    }

    /**
	 * Removes a metadata with certain path from the index 
	 * @param path
	 */
    public void removeMetadata(String path) {
        this.index.delete(new Term(Index.INTERNAL_PATH, path));
    }

    public void updateMetadata(Metadata md) {
        this.index.update(md);
    }

    /**
	 * returns the RemoteQueryBroker associated to this Search Engine
	 * @return
	 */
    public RemoteQueryBroker getRemoteQueryBroker() {
        return this.remoteQueryBroker;
    }

    /**
	 * Performs a distributed search
	 * @param humanQuery
	 * @return
	 */
    public HitsCollection search(String humanQuery, String expectedAttribute, boolean local) {
        try {
            RemoteQuery remoteQuery = new RemoteQuery(mySelf, humanQuery, expectedAttribute);
            this.expectedQueryID = remoteQuery.id;
            answersCollection = new HashSet();
            if (local) {
                this.expectedAnswers = 1;
                this.logMessage("Sending local query");
                this.requestChannel.send(this.mySelf, remoteQuery);
            } else {
                this.expectedAnswers = group.getMembers().size();
                this.logMessage("Sending query to whole group (" + this.expectedAnswers + " members)");
                this.requestChannel.sendGroup(remoteQuery);
            }
            boolean stop = false;
            int timeoutSteps = 50;
            int currentStep = 0;
            while (!stop) {
                if ((this.expectedAnswers == 0) || (currentStep == timeoutSteps)) {
                    stop = true;
                } else {
                    Thread.sleep(this.timeout / timeoutSteps);
                    currentStep++;
                }
            }
            if (this.expectedAnswers > 0) this.logMessage("TIMEOUT: " + this.expectedAnswers + " weren't received"); else this.logMessage("All expected answer have been received");
            return HitsCollection.merge(answersCollection);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void onMessage(PopeyeMessage msg) {
        if (!(msg instanceof HitsCollection)) {
            this.logMessage("Received Message is not instance of HitsCollection:  received \"" + msg.getClass() + "\" instead");
            return;
        }
        HitsCollection hitsCollection = (HitsCollection) msg;
        if (hitsCollection.queryID == this.expectedQueryID) {
            this.answersCollection.add((HitsCollection) msg);
            this.expectedAnswers--;
            this.logMessage("Query answer received (" + this.expectedAnswers + " to go)");
        } else {
            this.logMessage("Old Query answer received: received " + hitsCollection.queryID + ". Expected " + this.expectedQueryID);
        }
    }

    /**
	 * Activate RemoteQueryBroker log
	 * @param logEditor JTextArea where log messages will be written
	 */
    public void activateLog(JTextArea logEditor) {
        this.logActive = true;
        this.logEditor = logEditor;
    }

    /**
	 * Deactivate RemoteQueryBroker log 
	 *
	 */
    public void deactivateLog() {
        this.logActive = false;
    }

    public void logMessage(String message) {
        if (this.logActive) this.logEditor.append(message + "\n"); else System.out.println(message);
    }
}
