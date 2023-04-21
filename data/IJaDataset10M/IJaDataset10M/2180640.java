package eu.popeye.middleware.dataSearch;

import java.io.IOException;
import java.net.SocketException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import javax.swing.JTextArea;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import eu.popeye.networkabstraction.communication.ApplicationMessageListener;
import eu.popeye.networkabstraction.communication.CommunicationChannel;
import eu.popeye.networkabstraction.communication.message.PopeyeMessage;

/**
 * This class represents the Query Server. It will responsible for answering the quries comming from other peers. 
 * It must be as many instances of RemoteQueryBroker in each peer as the number of Shared Spaces it participates
 *  
 * @author Francisco Antonio Bas Esparza
 *
 */
public class RemoteQueryBroker implements ApplicationMessageListener {

    CommunicationChannel requestChannel;

    CommunicationChannel answerChannel;

    IndexSearcher searcher = null;

    boolean logActive;

    JTextArea logEditor;

    public RemoteQueryBroker(CommunicationChannel requestChannel, CommunicationChannel answerChannel) {
        this.requestChannel = requestChannel;
        this.answerChannel = answerChannel;
        this.start();
        this.logActive = false;
    }

    /**
	 * Start receiving remote queries
	 *
	 */
    public void start() {
        this.requestChannel.addApplicationMessageListener(this);
        this.logMessage("LOCAL SEARCH ENGINE RUNNING");
    }

    /**
	 * Stop receiving remote queries
	 *
	 */
    public void stop() {
        this.requestChannel.removeApplicationMessageListener(this);
        this.logMessage("LOCAL SEARCH ENGINE STOPPED");
    }

    /**
	 * Sets the searcher. This method should be called every time the index associated to the searcher changes 
	 * @param searcher new searcher to be used during the search process
	 */
    public void setSearcher(IndexSearcher searcher) {
        this.searcher = searcher;
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

    /**
	 * Receive a Message from the query channel, performs it and send the results back to the origin
	 */
    public void onMessage(PopeyeMessage msg) {
        try {
            if (!(msg instanceof RemoteQuery)) {
                this.logMessage("Received Message is not instance of RemoteQuery!!!");
                return;
            }
            RemoteQuery remoteQuery = (RemoteQuery) msg;
            this.logMessage("Received Query: " + remoteQuery.toString());
            QueryParser queryParser = new QueryParser("description", new WhitespaceAnalyzer());
            Query query;
            query = queryParser.parse(remoteQuery.humanQuery);
            Hits hits = searcher.search(query);
            HashMap<String, String> hitsMap = new HashMap();
            for (int i = 0; i < hits.length(); i++) {
                String dataPath = hits.doc(i).get("path");
                String attributeValue = hits.doc(i).get(remoteQuery.expectedAttribute);
                hitsMap.put(dataPath, attributeValue);
            }
            HitsCollection remoteHits = new HitsCollection(hitsMap, remoteQuery.expectedAttribute, remoteQuery.id);
            this.answerChannel.send(remoteQuery.member, remoteHits);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (CorruptIndexException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void logMessage(String message) {
        if (this.logActive) this.logEditor.append(message + "\n"); else System.out.println(message);
    }
}
