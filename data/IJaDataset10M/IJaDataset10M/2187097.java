package org.tripcom.query.client.conn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import org.tripcom.query.client.model.Property;
import org.tripcom.query.client.model.Space;
import org.tripcom.query.client.model.Vocabulary;
import org.tripcom.query.client.view.ResultListener;
import org.tripcom.query.client.view.SpaceListener;
import org.tripcom.query.client.view.VocabularyListener;
import org.tripcom.query.client.view.dialogs.CostDialog;

/**
 * Class for retrieving information from Tripcom Kernels. Communication done with GWT-RPC.
 * @author Pasi Tiitinen
 * 
 */
public class TripcomQueryConn {

    /**
	 * Needed for GWT-RPC
	 */
    private SpaceServiceAsync spaceService = (SpaceServiceAsync) GWT.create(SpaceService.class);

    private ServiceDefTarget endpoint;

    /**
	 * Vocabularies retrieved from Triple Space are saved for later use
	 */
    private HashMap<String, Vocabulary> vocMap;

    /**
	 * Spaces retrieved from Triple Space are saved for later use
	 */
    private HashMap<String, Space> spaceMap;

    private SpaceListener spaceListener;

    private VocabularyListener vocabularyListener;

    private ResultListener resultListener;

    private String rootSpace;

    public String result;

    public TripcomQueryConn() {
        endpoint = (ServiceDefTarget) spaceService;
        endpoint.setServiceEntryPoint("http://localhost:8080/sdqt-0.2-SNAPSHOT/spaceData");
        rootSpace = "tsc://localhost:8080/profiumroot";
    }

    /**
	 * Retrieve subspaces of some space from kernel
	 * @param rootSpace
	 * @param listener
	 */
    public void getSpaceList(String rootSpace, SpaceListener listener) {
        this.rootSpace = rootSpace;
        spaceListener = listener;
        spaceService.getSubSpaces(rootSpace, new GetSubSpacesCallback());
    }

    /**
	 * Retrieves vocabularies that have been used in selected spaces
	 * @param spaces Spaces that were chosen to be queried
	 * @param listener 
	 */
    public void getVocabularies(Set<String> spaces, VocabularyListener listener) {
        vocabularyListener = listener;
        spaceService.getVocabularies(spaces, new GetVocabulariesCallback());
    }

    /**
	 * Retrieves query costs
	 * @param query SPARQL 
	 */
    public void getCosts(String query) {
        spaceService.getCost(query, new GetCostCallback());
    }

    /**
	 * @param namespace of the vocabulary
	 * @return vocabulary
	 */
    public Vocabulary getVocabulary(String namespace) {
        if (vocMap.containsKey(namespace)) return vocMap.get(namespace); else return null;
    }

    /**
	 * Returns all properties of the vocabularies on the list
	 * @param vocabularies
	 * @return properties
	 */
    public List<Property> getProperties(List<String> vocabularies) {
        List<Property> properties = new ArrayList<Property>();
        while (vocabularies.listIterator().hasNext()) properties.addAll(vocMap.get(vocabularies.listIterator().next()).getProperties());
        return properties;
    }

    /**
	 * Method for querying all spaces
	 * @param query SPARQL
	 * @param rootSpace rootaddress
	 * @param listener 
	 */
    public void queryAllSpaces(String query, final ResultListener listener) {
        resultListener = listener;
        spaceService.queryAllSpaces(query, rootSpace, new QueryCallback());
    }

    /**
	 * Method for querying selected spaces
	 * @param query SPARQL
	 * @param spaces that are queried
	 * @param listener 
	 */
    public void querySomeSpaces(String query, Set<String> spaces, final ResultListener listener) {
        resultListener = listener;
        spaceService.querySomeSpaces(spaces, query, new QueryCallback());
    }

    /**
	 * Vocabularies are saved for later use
	 * @param vocabularies <String namespace, Vocabulary>
	 */
    private void setVocabularies(HashMap<String, Vocabulary> vocabularies) {
        vocMap = vocabularies;
    }

    /**
	 * Callback class for retrieval of vocabularies
	 *
	 */
    private class GetVocabulariesCallback implements AsyncCallback {

        public void onFailure(Throwable throwable) {
            vocabularyListener.onRetrievalError("Error retrieving vocabularies:" + throwable.getMessage());
        }

        public void onSuccess(Object obj) {
            List<Vocabulary> vocabularies = new ArrayList<Vocabulary>();
            HashMap<String, Vocabulary> vocSet = (HashMap<String, Vocabulary>) obj;
            setVocabularies(vocSet);
            for (Iterator<String> iterator = vocSet.keySet().iterator(); iterator.hasNext(); ) {
                vocabularies.add(vocSet.get(iterator.next()));
            }
            vocabularyListener.onVocabulariesRetrieved(vocabularies);
        }
    }

    /**
	 * Callback class for retrieval of subspaces
	 *
	 */
    private class GetSubSpacesCallback implements AsyncCallback {

        public void onFailure(Throwable throwable) {
            spaceListener.onRetrievalError("Error retrieving subspaces:" + throwable.getMessage());
        }

        public void onSuccess(Object obj) {
            spaceMap = (HashMap<String, Space>) obj;
            spaceListener.onSpacesRetrieved(spaceMap);
        }
    }

    /**
	 * Callback class for retrieval of cost
	 *
	 */
    private class GetCostCallback implements AsyncCallback {

        public void onFailure(Throwable throwable) {
            Window.alert("Retrieval of cost failed. Maybe no connection to Tripcom server.");
        }

        public void onSuccess(Object obj) {
            final CostDialog costWindow = new CostDialog();
            costWindow.show();
            costWindow.setResult((String) obj);
        }
    }

    /**
	 * Callback class for retrieval of query results
	 *
	 */
    private class QueryCallback implements AsyncCallback {

        public void onFailure(Throwable throwable) {
            resultListener.onRetrievalError("Retrieval of results failed. Maybe no connection to Tripcom server.");
        }

        public void onSuccess(Object obj) {
            resultListener.onResultRetrieved((String) obj);
            result = (String) obj;
        }
    }

    /**
	 * For testing connection
	 *
	 */
    private class HelloWorldCallback implements AsyncCallback {

        public void onFailure(Throwable throwable) {
            Window.alert("Hello world error:" + throwable + " Viesti: " + throwable.getMessage());
            GWT.log("Hello error", throwable.getCause());
        }

        public void onSuccess(Object obj) {
            String result = (String) obj;
            Window.alert(result);
        }
    }

    /**
	 * @return rootSpace
	 */
    public String getRootSpace() {
        return rootSpace;
    }

    public String getResult() {
        return result;
    }
}
