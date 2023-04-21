package up2p.peer.generic.message;

import java.util.ArrayList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import up2p.core.LocationEntry;
import up2p.search.SearchResponse;
import up2p.xml.TransformerHelper;

/**
 * Message sent from server to client in response to a search request.
 * 
 * @author Neal Arthorne
 * @version 1.0
 */
public class SearchResponseMessage extends GenericPeerMessage {

    /** Holds a list of SearchResponse objects */
    private ArrayList results;

    private int resultSetSize;

    private String communityId;

    /** Error message returned when a parsing error occurs */
    public static final String ERROR_MSG = "Error parsing a Search Request";

    /** XML tags for response messages */
    public static final String X_COMMUNITY = "communityId";

    public static final String X_SEARCH_RESPONSE = "searchResponse";

    public static final String X_SEARCH_RESULT = "searchResult";

    public static final String X_SEARCH_ID = "id";

    public static final String X_RESULT_SIZE = "resultSetSize";

    public static final String X_TITLE = "title";

    public static final String X_RESOURCE_ID = "id";

    public static final String X_FILENAME = "filename";

    public static final String X_LOCATION = "location";

    public static final String X_LOCATION_ENTRY = "resource";

    /**
     * Constructs an empty response message.
     * 
     * @param id the id of the search response message
     */
    public SearchResponseMessage(String id) {
        super(SEARCH_RESPONSE);
        results = new ArrayList();
        this.id = id;
    }

    /**
     * Constructs an empty response message.
     */
    public SearchResponseMessage() {
        super(SEARCH_RESPONSE);
        results = new ArrayList();
    }

    /**
     * Adds a search result to this search response.
     * 
     * @param communityId id of the community where the resource is shared
     * @param resourceId id of the resource to add to the result
     * @param resourceTitle title of the resource
     * @param fileName name of the file where the resource should be saved
     * @param locations location entries for the given resource
     */
    public void addResult(String communityId, String resourceId, String resourceTitle, String fileName, LocationEntry[] locations) {
        results.add(new SearchResponse(resourceId, resourceTitle, communityId, fileName, locations, false));
    }

    /**
     * Returns a list of <code>SearchResponse</code> objects that are
     * contained in this Search Response Message.
     * 
     * @return a list of responses
     */
    public SearchResponse[] getResponses() {
        return (SearchResponse[]) results.toArray(new SearchResponse[results.size()]);
    }

    /**
     * Parses a search response from the given XML fragment.
     * 
     * @param xmlNode the XML containing the search response
     * @return the search response
     * @throws MalformedPeerMessageException on a badly formed or invalid
     * message
     */
    public static SearchResponseMessage parse(Node xmlNode) throws MalformedPeerMessageException {
        SearchResponseMessage response = new SearchResponseMessage();
        try {
            if (xmlNode.getNodeName().equals(GenericPeerMessage.X_UP2P_MESSAGE)) {
                Node currentNode = xmlNode.getFirstChild();
                while (currentNode != null && currentNode.getNodeType() != Node.ELEMENT_NODE) currentNode = currentNode.getNextSibling();
                Element current = (Element) currentNode;
                String communityId = current.getAttribute(X_COMMUNITY);
                response.setCommunityId(communityId);
                response.setResultSetSize(Integer.parseInt(current.getAttribute(X_RESULT_SIZE)));
                String searchId = current.getAttribute(X_SEARCH_ID);
                response.setId(searchId);
                LocationEntry[] entries = null;
                NodeList resultList = current.getChildNodes();
                for (int i = 0; i < resultList.getLength(); i++) {
                    String resourceTitle = null;
                    String resourceId = null;
                    String fileName = null;
                    if (resultList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                        Element result = (Element) resultList.item(i);
                        resourceTitle = result.getAttribute(X_TITLE);
                        resourceId = result.getAttribute(X_RESOURCE_ID);
                        fileName = result.getAttribute(X_FILENAME);
                        ArrayList locations = new ArrayList();
                        NodeList entryList = result.getChildNodes();
                        for (int j = 0; j < entryList.getLength(); j++) {
                            if (entryList.item(j).getNodeType() == Node.ELEMENT_NODE) {
                                Element locationNode = (Element) entryList.item(j);
                                locations.add(new LocationEntry(locationNode.getAttribute(X_LOCATION)));
                            }
                        }
                        entries = (LocationEntry[]) locations.toArray(new LocationEntry[locations.size()]);
                    }
                    response.addResult(communityId, resourceId, resourceTitle, fileName, entries);
                }
                return response;
            }
            throw new MalformedPeerMessageException(ERROR_MSG + " Root node name: " + xmlNode.getNodeName());
        } catch (Exception e) {
            e.printStackTrace();
            throw new MalformedPeerMessageException(ERROR_MSG);
        }
    }

    public Node serialize() {
        Document document = TransformerHelper.newDocument();
        Element root = document.createElement(GenericPeerMessage.X_UP2P_MESSAGE);
        document.appendChild(root);
        Element current = document.createElement(X_SEARCH_RESPONSE);
        current.setAttribute(X_SEARCH_ID, id);
        current.setAttribute(X_RESULT_SIZE, String.valueOf(resultSetSize));
        current.setAttribute(X_COMMUNITY, communityId);
        root.appendChild(current);
        SearchResponse[] responses = getResponses();
        for (int i = 0; i < responses.length; i++) {
            SearchResponse response = responses[i];
            Element searchResult = document.createElement(X_SEARCH_RESULT);
            searchResult.setAttribute(X_TITLE, response.getTitle());
            searchResult.setAttribute(X_RESOURCE_ID, response.getId());
            searchResult.setAttribute(X_FILENAME, response.getFileName());
            LocationEntry[] entries = response.getLocations();
            for (int j = 0; j < entries.length; j++) {
                Element resourceElement = document.createElement(X_LOCATION_ENTRY);
                resourceElement.setAttribute(X_LOCATION, entries[j].location);
                searchResult.appendChild(resourceElement);
            }
            current.appendChild(searchResult);
        }
        return document.getDocumentElement();
    }

    /**
     * Returns the result set size.
     * 
     * @return the size of the result set
     */
    public int getResultSetSize() {
        return resultSetSize;
    }

    /**
     * Sets the size of the result set.
     * 
     * @param resultSetSize size of the result set
     */
    public void setResultSetSize(int resultSetSize) {
        this.resultSetSize = resultSetSize;
    }

    /**
     * Sets the resource id.
     * 
     * @param messageId resource id to set
     */
    public void setId(String messageId) {
        id = messageId;
    }

    /**
     * Returns the community id.
     * 
     * @return community id
     */
    public String getCommunityId() {
        return communityId;
    }

    /**
     * Sets the community id.
     * 
     * @param communityId id to set
     */
    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
}
