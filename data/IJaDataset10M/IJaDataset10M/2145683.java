package org.jcvi.vics.model.tasks.search;

import org.jcvi.vics.model.tasks.Event;
import org.jcvi.vics.model.tasks.Task;
import org.jcvi.vics.model.user_data.Node;
import org.jcvi.vics.model.user_data.search.SearchResultNode;
import org.jcvi.vics.model.vo.MultiSelectVO;
import org.jcvi.vics.model.vo.ParameterException;
import org.jcvi.vics.model.vo.ParameterVO;
import org.jcvi.vics.model.vo.TextParameterVO;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: cgoina
 * Date: Aug 24, 2006
 * Time: 4:47:12 PM
 *
 * @version $Id: SearchTask.java 1 2011-02-16 21:07:19Z tprindle $
 */
public class SearchTask extends Task {

    public static final transient int MATCH_ANY = 0;

    public static final transient int MATCH_ALL = 1;

    public static final transient int MATCH_PHRASE = 3;

    public static final transient int ALL_SEARCH_TOPICS_COMPLETED_SUCCESSFULLY = 0;

    public static final transient int SEARCH_COMPLETED_WITH_ERRORS = -1;

    public static final transient int SEARCH_TIMEDOUT = 2;

    public static final transient int SEARCH_STILL_RUNNING = 1;

    public static final transient String TOPIC_ALL = "all";

    public static final transient String TOPIC_ACCESSION = "accession";

    public static final transient String TOPIC_PROTEIN = "protein";

    public static final transient String TOPIC_CLUSTER = "final_cluster";

    public static final transient String TOPIC_PUBLICATION = "publication";

    public static final transient String TOPIC_PROJECT = "project";

    public static final transient String TOPIC_SAMPLE = "sample";

    public static final transient String TOPIC_WEBSITE = "website";

    public static final transient String TOPIC_GENOME = "genome";

    public static final transient String TOPIC_NCGENE = "ncgene";

    public static final transient String PARAM_searchString = "searchString";

    public static final transient String PARAM_searchTopic = "searchTopic";

    public static final transient String PARAM_matchFlags = "matchFlags";

    public static final transient String SEARCH_STATUS_RUNNING = "running";

    public static final transient String SEARCH_STATUS_ERROR = "error";

    public static final transient String SEARCH_STATUS_COMPLETED = "completed";

    private static final transient String[] SUPPORTED_TOPICS = { TOPIC_ACCESSION, TOPIC_PROTEIN, TOPIC_CLUSTER, TOPIC_PUBLICATION, TOPIC_PROJECT, TOPIC_SAMPLE, TOPIC_WEBSITE };

    public static String matchFlagsToString(int matchFlags) {
        String stringifiedMatchFlags = "any";
        if (matchFlags == MATCH_ALL) {
            stringifiedMatchFlags = "all";
        } else if (matchFlags == MATCH_PHRASE) {
            stringifiedMatchFlags = "phrase";
        }
        return stringifiedMatchFlags;
    }

    public SearchTask() {
        super();
        setTaskName("String Search");
    }

    public String getDisplayName() {
        return "Search Task";
    }

    public ParameterVO getParameterVO(String key) throws ParameterException {
        if (key == null) return null;
        String value = getParameter(key);
        if (value == null) return null;
        if (key.equals(PARAM_searchString)) {
            return new TextParameterVO(value);
        }
        if (key.equals(PARAM_searchTopic)) {
            return new MultiSelectVO(Task.listOfStringsFromCsvString(value), Task.listOfStringsFromCsvString(value));
        }
        if (key.equals(PARAM_matchFlags)) {
            return new TextParameterVO(value);
        }
        return null;
    }

    public boolean isParameterRequired(String parameterKeyName) {
        if (!super.isParameterRequired(parameterKeyName)) {
            return false;
        }
        return PARAM_searchString.equalsIgnoreCase(parameterKeyName);
    }

    public SearchResultNode getSearchResultNode() {
        SearchResultNode searchResultNode = null;
        Set outputNodes = getOutputNodes();
        for (Object outputNode : outputNodes) {
            Node node = (Node) outputNode;
            if (node instanceof SearchResultNode) {
                searchResultNode = (SearchResultNode) node;
                break;
            }
        }
        return searchResultNode;
    }

    public String getSearchString() {
        String searchStringParam = null;
        try {
            searchStringParam = ((TextParameterVO) getParameterVO(PARAM_searchString)).getTextValue();
        } catch (Exception ignore) {
        }
        return searchStringParam;
    }

    public void setSearchString(String searchString) {
        setParameter(PARAM_searchString, searchString);
    }

    public List<String> getSearchTopics() {
        MultiSelectVO searchTopicsParam;
        List<String> searchTopics = null;
        try {
            searchTopicsParam = ((MultiSelectVO) getParameterVO(PARAM_searchTopic));
            if (searchTopicsParam != null) {
                searchTopics = searchTopicsParam.getActualUserChoices();
            }
        } catch (Exception ignore) {
        }
        return searchTopics;
    }

    public void setSearchTopics(List<String> searchTopics) {
        if (searchTopics != null && searchTopics.contains(TOPIC_ALL)) {
            setParameter(PARAM_searchTopic, csvStringFromList(getAllSupportedTopics()));
        } else {
            setParameter(PARAM_searchTopic, csvStringFromList(searchTopics));
        }
    }

    public String getSearchTopicsAsCSV() {
        MultiSelectVO searchTopicsParam;
        List searchTopics = null;
        try {
            searchTopicsParam = ((MultiSelectVO) getParameterVO(PARAM_searchTopic));
            if (searchTopicsParam != null) {
                searchTopics = searchTopicsParam.getActualUserChoices();
            }
        } catch (Exception ignore) {
        }
        String csvTopics = "all";
        if (searchTopics != null && searchTopics.size() > 0) {
            StringBuffer topicsBuffer = new StringBuffer();
            for (Object searchTopic : searchTopics) {
                if (topicsBuffer.length() > 0) {
                    topicsBuffer.append(',');
                }
                topicsBuffer.append((String) searchTopic);
            }
            csvTopics = topicsBuffer.toString();
        }
        return csvTopics;
    }

    public int getMatchFlags() {
        TextParameterVO matchFlagsParam = null;
        try {
            matchFlagsParam = (TextParameterVO) getParameterVO(PARAM_matchFlags);
        } catch (Exception ignore) {
        }
        int matchFlags = MATCH_ANY;
        if (matchFlagsParam != null && matchFlagsParam.getStringValue() != null) {
            if (matchFlagsParam.getStringValue().equalsIgnoreCase("all")) {
                matchFlags = MATCH_ALL;
            } else if (matchFlagsParam.getStringValue().equalsIgnoreCase("phrase")) {
                matchFlags = MATCH_PHRASE;
            }
        }
        return matchFlags;
    }

    public void setMatchFlags(int matchFlags) {
        setParameter(PARAM_matchFlags, matchFlagsToString(matchFlags));
    }

    public String getMatchFlagsAsString() {
        return matchFlagsToString(getMatchFlags());
    }

    public List<String> getAllSupportedTopics() {
        ArrayList<String> allSupportedTopicList = new ArrayList<String>();
        allSupportedTopicList.addAll(Arrays.asList(SUPPORTED_TOPICS));
        return allSupportedTopicList;
    }

    public boolean isTopicSupported(String topic) {
        for (String SUPPORTED_TOPIC : SUPPORTED_TOPICS) {
            if (SUPPORTED_TOPIC.equals(topic)) {
                return true;
            }
        }
        return false;
    }

    /**
     * checks whether the task has completed
     *
     * @return 0 if the task has completed successfully
     *         -1 if the task has completed with errors
     *         1 if the task is still running
     */
    public int hasCompleted() {
        Event lastEvent = getLastEvent();
        if (lastEvent.getEventType().equals(Event.COMPLETED_EVENT)) {
            return ALL_SEARCH_TOPICS_COMPLETED_SUCCESSFULLY;
        } else if (lastEvent.getEventType().equals(Event.ERROR_EVENT)) {
            return SEARCH_COMPLETED_WITH_ERRORS;
        }
        List<String> searchTopics = getSearchTopics();
        if (searchTopics != null && searchTopics.contains(SearchTask.TOPIC_ALL)) {
            searchTopics.addAll(getAllSupportedTopics());
        }
        boolean subTaskErrorFound = false;
        for (Object o : getEvents()) {
            Event searchTaskEvent = (Event) o;
            boolean subTaskCompleted = false;
            if (searchTaskEvent.getEventType().equals(Event.SUBTASKCOMPLETED_EVENT)) {
                subTaskCompleted = true;
            } else if (searchTaskEvent.getEventType().equals(Event.SUBTASKERROR_EVENT)) {
                subTaskCompleted = true;
                subTaskErrorFound = true;
            }
            if (subTaskCompleted) {
                String searchCategory = searchTaskEvent.getDescription();
                if (null != searchTopics) {
                    searchTopics.remove(searchCategory);
                }
            }
        }
        if (null != searchTopics && searchTopics.isEmpty()) {
            if (subTaskErrorFound) {
                return SEARCH_COMPLETED_WITH_ERRORS;
            } else {
                return ALL_SEARCH_TOPICS_COMPLETED_SUCCESSFULLY;
            }
        } else {
            return SEARCH_STILL_RUNNING;
        }
    }
}
