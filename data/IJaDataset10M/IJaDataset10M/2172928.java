package nl.mikproject.MERRYts.Support;

import nl.mikproject.MERRYts.External.SnAPI.*;
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

    public requestHandler() {
    }

    public void buildResponse(ArrayList<String[]> Information) {
        RESULT_TEMPLATE.setResponseType(requiredResponsType);
        switch(requiredResponsType) {
            case 0:
                response = RESULT_TEMPLATE.buildPlainResponse(Information, isTextSearch);
                break;
            case 1:
                response = RESULT_TEMPLATE.buildColorTableResponse(Information, isTextSearch);
                break;
            default:
                response = RESULT_TEMPLATE.buildPlainResponse(Information, isTextSearch);
                break;
        }
    }

    public String getDragDropSite(int type, String searchText) {
        ArrayList labelInfo = new ArrayList<String[]>();
        DragDrop dropSite = new DragDrop();
        SubSetHandling EenSubset = new SubSetHandling();
        int locationOfTitle = 1;
        int labelposition = 0;
        int locationOfID = 2;
        boolean multipleDropboxes = true;
        switch(type) {
            case 1:
                multipleDropboxes = false;
                labelInfo = EenSubset.getInfo();
                break;
            case 2:
                locationOfID = 0;
                locationOfTitle = 2;
                labelposition = 3;
                SearchByText textBasedSearch = new SearchByText();
                textBasedSearch.setSearchText(searchText);
                labelInfo = textBasedSearch.getInfo();
                break;
            default:
                labelInfo = EenSubset.getInfo();
                break;
        }
        return dropSite.showSite(labelInfo, locationOfTitle, labelposition, locationOfID, multipleDropboxes);
    }

    public String getListOfSubsets() {
        isTextSearch = false;
        setIDLocation(0);
        SubSetHandling EenSubset = new SubSetHandling();
        buildResponse(EenSubset.getInfo());
        return response;
    }

    public String getListOfSubsets(int ResponseType) {
        setResponseType(ResponseType);
        return getListOfSubsets();
    }

    public String getConceptByText() {
        isTextSearch = true;
        SearchByText textBasedSearch = new SearchByText();
        textBasedSearch.setSearchText(searchKey);
        buildResponse(textBasedSearch.getInfo());
        return response;
    }

    public void setSearchKey(String text) {
        searchKey = text;
    }

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

    public String getConceptByText(String text) {
        setSearchKey(text);
        return getConceptByText();
    }

    public String getConceptByText(String text, int desiredResponse) {
        setSearchKey(text);
        setResponseType(desiredResponse);
        return getConceptByText();
    }
}
