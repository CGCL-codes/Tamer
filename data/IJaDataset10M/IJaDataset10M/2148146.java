package nl.mikproject.MERRYts;

import java.util.ArrayList;

/**
 *
 * @author WPME Hofland
 */
public class requestHandler {

    String searchKey = "Search";

    private static final ResultTemplates RESULT_TEMPLATE = new ResultTemplates();

    int requiredResponsType = 1;

    String response = "";

    boolean isTextSearch = false;

    private int typeOfSearch = 1;

    private static final SearchByText TEXTBASEDSEARCH = new SearchByText();

    /**
     * Basic Class for calling the right template, starting the right request function, etc.
     */
    public requestHandler() {
    }

    /**
     * Method to fit found information into a template
     * @param Information List with data to prestent in a template
     */
    public void buildResponse(ArrayList<String[]> Information) {
        RESULT_TEMPLATE.setResponseType(requiredResponsType);
        switch(requiredResponsType) {
            case 0:
                response = RESULT_TEMPLATE.buildPlainResponse(Information, isTextSearch);
                break;
            case 1:
                response = RESULT_TEMPLATE.buildColorTableResponse(Information, isTextSearch);
                break;
            case 2:
                response = RESULT_TEMPLATE.buildSnobLikeResponse(Information, isTextSearch);
                break;
            case 3:
                response = RESULT_TEMPLATE.buildDragDropSite(Information, typeOfSearch, searchKey);
                break;
            case 4:
                response = RESULT_TEMPLATE.buildCliniClueLikeResponse(Information, isTextSearch);
                break;
            default:
                response = RESULT_TEMPLATE.buildPlainResponse(Information, isTextSearch);
                break;
        }
    }

    /**
     * Set the search method used in the resulttemplate template
     * @param type type of search used
     */
    public void setSearchMode(int type) {
        TEXTBASEDSEARCH.setMatchtype(type);
    }

    /**
     * Set the maximum number of results to be fetched
     * @param results maximum number to be fetched
     */
    public void setNumberOfResults(int results) {
        TEXTBASEDSEARCH.setMaxResults(results);
    }

    /**
     * Get a list of subsets implemented in the SnAPI
     * @return Strung containging a list of available subsets
     */
    public String getListOfSubsets() {
        isTextSearch = false;
        setIDLocation(0);
        SubSetHandling EenSubset = new SubSetHandling();
        buildResponse(EenSubset.getInfo());
        return response;
    }

    /**
     * Return a list of subsets based presented in various ways
     * @param ResponseType type of presentation
     * @return HTML-string containing list of subsets available in the SnAPI
     */
    public String getListOfSubsets(int ResponseType) {
        setResponseType(ResponseType);
        return getListOfSubsets();
    }

    /**
     * Get concepts based on text set in the setSearchKey method
     * @return HTML-formated respons
     */
    public String getConceptByText() {
        TEXTBASEDSEARCH.setResponsType(requiredResponsType);
        isTextSearch = true;
        TEXTBASEDSEARCH.setSearchText(searchKey);
        buildResponse(TEXTBASEDSEARCH.getInfo());
        return response;
    }

    /**
     * set the term to search for
     * @param text text to search for
     */
    public void setSearchKey(String text) {
        searchKey = text;
    }

    /**
     * Set the type of response (template) you want to get
     * @param desiredResponse type of response you want. 0 = Plain text. 1 = Colored Table. 2 = Snob like. 3 = DragDrop. 4 = CliniClue like. Default = 1. On out of bound, default = 0.
     */
    public void setResponseType(int desiredResponse) {
        requiredResponsType = desiredResponse;
    }

    /**
     * Sets the location of the ID in an Array. If this option is set, the table column with the ID will turn green
     * @param locationOfIDInArray Location of ID in array. -1 if no ID available. Default: -1.
     */
    public void setIDLocation(int locationOfIDInArray) {
        RESULT_TEMPLATE.setIDLocation(locationOfIDInArray);
    }

    /**
     * Get a formated response based on searched text
     * @param text text to search for
     * @return HTM-formated respons
     */
    public String getConceptByText(String text) {
        setSearchKey(text);
        return getConceptByText();
    }

    /**
     * Get concept based on serached text,formated as desired
     * @param text text to search for
     * @param desiredResponse response desired
     * @return HTML-string presenting concepts searched by text
     */
    public String getConceptByText(String text, int desiredResponse) {
        setSearchKey(text);
        setResponseType(desiredResponse);
        return getConceptByText();
    }

    /**
     * Set teh type to use for searching.
     * @param searchType type of search. 0 = whole word, 1 = pharse search
     */
    public void setSearchType(int searchType) {
        typeOfSearch = searchType;
    }

    /**
     * Get a HTML-formated string containing concepts.
     * @param text text to search
     * @param desiredResponse type of response (template)
     * @param searchType Type of search. 0 = whole word, 1 = pharse search
     * @return HTML-formated string containing concepts fetched by searching on text by searchType. Formated as set in desiredResponse.
     */
    public String getConceptByText(String text, int desiredResponse, int searchType) {
        setSearchType(searchType);
        setSearchKey(text);
        setResponseType(desiredResponse);
        return getConceptByText();
    }
}
