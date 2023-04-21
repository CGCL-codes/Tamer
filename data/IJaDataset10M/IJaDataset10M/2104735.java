package nl.mikproject.MERRYts;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 *
 * @author WPME Hofland
 */
public class ResultTemplates {

    static int responseType;

    private int locationOfID = -1;

    /**
     * Prepare a template containing a Table
     * @param infoImported An arraylist containing information fetched from a TS. See the SearchByText Class for information about the order of strings
     * @param isTextSearch True if infoImported is an array of retrieved terms and various categories. False if it is a list of subsets.
     * @return Html-string containing send information. Formatted in a table highlighted in various colors.
     */
    public String buildColorTableResponse(ArrayList<String[]> infoImported, boolean isTextSearch) {
        if (isTextSearch) {
            return buildTextColorTableResponse(infoImported);
        } else {
            return buildDefaultColorTableResponse(infoImported);
        }
    }

    /**
     * Sets the location of the ID in an Array. If this option is set, the table column with the ID will turn green
     * @param locationOfIDInArray Location of ID in array. -1 if no ID available. Default: -1.
     */
    public void setIDLocation(int locationOfIDInArray) {
        locationOfID = locationOfIDInArray;
    }

    /**
     * Create a table response using colors
     * @param infoImported an arraylist of terminological information. See the SearchByText Class for information about the order of strings
     * @return HTML-string containing send information. Formatted in a table highlighted in various colors.
     */
    public String buildDefaultColorTableResponse(ArrayList<String[]> infoImported) {
        return buildDefaultTableResponse(infoImported, true);
    }

    /**
     * Build a table response with or without colors. Used to create tables for subsets
     * @param infoImported An arraylist containing subsets.
     * @param useColor True if the table needs to use an alternate color to present the ID.
     * @return HTML-string presenting a table with information set by infoImported
     */
    public String buildDefaultTableResponse(ArrayList<String[]> infoImported, boolean useColor) {
        String partOfResponse = "<table style=\"border: 1px solid\">";
        for (int i = 0; i < infoImported.size(); i++) {
            partOfResponse += "<tr>";
            for (int j = 0; j < infoImported.get(i).length; j++) {
                partOfResponse += "<td style=\"border: 1px solid;";
                if (j == locationOfID && useColor) {
                    partOfResponse += " color: #090";
                }
                partOfResponse += "\">" + infoImported.get(i)[j];
                partOfResponse += "</td>";
            }
            partOfResponse += "</tr>";
        }
        partOfResponse += "</table>";
        return partOfResponse;
    }

    /**
     * Build a table response with colors. Used to create tables with IDs, concepts, categories and weight
     * @param infoImported An arraylist containing subsets.
     * @return HTML-string presenting a table with information set by infoImported
     */
    public String buildTextColorTableResponse(ArrayList<String[]> infoImported) {
        return buildTextColorTableResponse(infoImported, true);
    }

    /**
     * Build a table response with colors. Used to create tables with IDs, concepts, categories and weight
     * @param infoImported An arraylist containing subsets.
     * @param useColor True if the table needs to be presented using colors for various types of information
     * @return HTML-string presenting a table with information set by infoImported
     */
    public String buildTextColorTableResponse(ArrayList<String[]> infoImported, boolean useColor) {
        String partOfResponse = "<table style=\"border: 1px solid\"><tr><th style=\"color: #090\">ID</th><th>Fully Specified Name</th><th width=\"100px\"style=\"color: #00F\">Type</th><th>Weight</th></tr>";
        for (int i = 0; i < infoImported.size(); i++) {
            partOfResponse += "<tr>";
            partOfResponse += "<td style=\"border: 1px solid;";
            if (useColor) {
                partOfResponse += "color: #090";
            }
            partOfResponse += "\">" + infoImported.get(i)[0];
            partOfResponse += "</td>";
            partOfResponse += "<td style=\"border: 1px solid\">";
            partOfResponse += infoImported.get(i)[2];
            partOfResponse += "</td><td style=\"border: 1px solid\">";
            if (useColor) {
                partOfResponse += "<font color=\"Blue\">";
            }
            partOfResponse += infoImported.get(i)[3];
            if (useColor) {
                partOfResponse += "</font>";
            }
            partOfResponse += "</td>";
            partOfResponse += "<td style=\"border: 1px solid; ";
            Double weight = 0.0;
            String altWeight = "";
            Boolean useWeight = false;
            if (useColor) {
                try {
                    weight = Double.parseDouble(infoImported.get(i)[4]);
                    useWeight = true;
                } catch (NumberFormatException ex) {
                    altWeight = infoImported.get(i)[4];
                    useWeight = false;
                }
                if (useWeight) {
                    if (weight > 99.9) {
                        partOfResponse += "color: green; font-weight: bold";
                    } else if (weight < 50.0) {
                        partOfResponse += "color: orange";
                    }
                }
            }
            partOfResponse += "\">";
            if (useWeight) {
                partOfResponse += weight;
            }
            partOfResponse += altWeight + "</td>";
            partOfResponse += "</tr>";
        }
        partOfResponse += "</table>";
        return partOfResponse;
    }

    /**
     * Returns a string of all information set by information
     * @param information Arraylist presenting data.
     * @param isTextSearch True if information contains concepts and categories, False if information contains subsets
     * @return A string presenting al data. Information on one item is separated by a tab \\t. Items are separated by a return \\n
     */
    public String buildPlainResponse(ArrayList<String[]> information, boolean isTextSearch) {
        String partOfResponse = "";
        for (int i = 0; i < information.size(); i++) {
            partOfResponse += information.get(i)[0];
            partOfResponse += "\t";
            partOfResponse += information.get(i)[1];
            partOfResponse += "\t";
            if (isTextSearch) {
                partOfResponse += information.get(i)[4];
            } else {
                partOfResponse += information.get(i)[2];
            }
            partOfResponse += "\n";
        }
        return partOfResponse;
    }

    /**
     * Create a template presenting information in a SNOB-like environment
     * @param infoImported An arraylist with data to present. See the SearchByText Class for information about the order of strings
     * @param isTextSearch True if infoImported contains concepts and categories. False if it contains subsets
     * @return HTML-string presenting the SNOB-like environment
     */
    public String buildSnobLikeResponse(ArrayList<String[]> infoImported, boolean isTextSearch) {
        int labelposition;
        String usedTypes = "";
        locationOfID = 0;
        if (isTextSearch) {
            labelposition = 3;
        } else {
            labelposition = 1;
        }
        Hashtable<String, Integer> theTypes = new Hashtable<String, Integer>();
        for (int i = 0; i < infoImported.size(); i++) {
            if (isTextSearch) {
                if (theTypes.containsKey(infoImported.get(i)[labelposition])) {
                    theTypes.put(infoImported.get(i)[labelposition], theTypes.get(infoImported.get(i)[labelposition]) + 1);
                } else {
                    theTypes.put(infoImported.get(i)[labelposition], 1);
                }
            } else {
                theTypes.put(infoImported.get(i)[labelposition], 1);
            }
        }
        usedTypes = theTypes.toString().substring(1, theTypes.toString().length() - 1);
        String[][] groups = new String[theTypes.size() + 1][2];
        int currentValue = 0;
        while (usedTypes.length() > 0) {
            int startValue = usedTypes.lastIndexOf("=");
            if (startValue > -1) {
                groups[currentValue][1] = usedTypes.substring(startValue + 1);
                usedTypes = usedTypes.substring(0, startValue);
            }
            int startKey = usedTypes.lastIndexOf(",");
            if (startKey > -1) {
                groups[currentValue][0] = usedTypes.substring(startKey + 2);
                usedTypes = usedTypes.substring(0, startKey);
            } else {
                groups[currentValue][0] = usedTypes;
                usedTypes = "";
            }
            currentValue++;
        }
        usedTypes = "<ul type='square'>";
        for (int i = 0; i < theTypes.size(); i++) {
            usedTypes += "<li>" + groups[i][0];
            if (isTextSearch) {
                usedTypes += " (" + groups[i][1] + ")";
            }
        }
        usedTypes += "</ul>";
        String partOfResponse = "<table border = 1px><tr><td>";
        partOfResponse += "<table><tr><td><input type=\"textbox\"><input type=\"submit\" value=\"Search\">" + "</td></tr>" + "<tr><td>" + usedTypes + "</td></tr>";
        partOfResponse += "</table></td>";
        partOfResponse += "<td>";
        partOfResponse += "<table border=\"0\"><tr height=\"100px\"><td width=\"500px\">" + "</td></tr><tr height=\"200px\"><td valign=\"top\" width=\"500px\">" + "<hr>" + "</td></tr></table>";
        partOfResponse += "</td>";
        partOfResponse += "</tr></table>";
        return partOfResponse;
    }

    /**
     * Build a skin presenting the information in a CliniClue-like environment
     * @param infoImported ArrayList containing subsets or categories. See the SearchByText Class for information about the order of strings
     * @param isTextSearch True if infoImported contains labels and categories. False if it contains subsets
     * @return HTML-string Presenting infoImported in a CliniClue-like environment
     */
    public String buildCliniClueLikeResponse(ArrayList<String[]> infoImported, boolean isTextSearch) {
        int labelposition;
        SearchByID IDBasedInfo = new SearchByID();
        String usedTypes = "<select size=10 width=420 style='width:420px;'>";
        locationOfID = 0;
        org.datacontract.schemas._2004._07.snapiwcfservice.ArrayOfDescription[] listOfDescriptions;
        Long conceptID = (long) 0;
        String descriptionID = "";
        String readCode = "";
        String langCode = "";
        if (isTextSearch) {
            readCode = infoImported.get(0)[5];
            listOfDescriptions = SearchByText.getListOfDescriptions();
            conceptID = listOfDescriptions[0].getDescription().get(0).getConceptId();
            descriptionID = listOfDescriptions[0].getDescription().get(0).getDescriptionId().toString();
            langCode = listOfDescriptions[0].getDescription().get(0).getLanguageCode().getValue();
            labelposition = 2;
        } else {
            labelposition = 1;
        }
        for (int i = 0; i < infoImported.size(); i++) {
            usedTypes += "<option value=\"" + infoImported.get(i)[labelposition] + "\">F " + infoImported.get(i)[labelposition] + "</option>";
        }
        usedTypes += "</select>";
        String partOfResponse = "<table border = 0px>";
        partOfResponse += "<tr valign='top'><td align=\"right\">" + "Concept Id <input size=15 type=\"textbox\" disabled=\"disabled\" value=\"" + conceptID + "\"><br>" + "DescriptionID <input size=15 type=\"textbox\" disabled=\"disabled\" value=\"" + descriptionID + "\">" + "</td><td colspan=\"2\"><textarea rows=4 cols=80 disabled='disabled'>";
        if (isTextSearch) {
            partOfResponse += infoImported.get(0)[3];
        }
        partOfResponse += "</textarea></td></tr>" + "<tr><td colspan=\"2\" valign='top'>" + "<select><option value=\"0\">Words - any order</option>" + "<option value=\"1\">Phrase Search</option></select>" + "<input type=\"textbox\" disabled=\"disabled\" size=6 value=\"" + infoImported.size() + "\">" + "<br><input type=\"textbox\"><input type=\"submit\" value=\"Search\"></td>" + "<td rowspan=3 width=\"500px\">";
        if (isTextSearch) {
            partOfResponse += "Concept Status: ...<br>" + infoImported.get(0)[2] + "(" + infoImported.get(0)[3] + ")" + "<BR>Lang: *:LANG" + "<ul><li class='clinicule-list'><font color='#888'>Concept not translated</font></ul>" + "<br>Lang: " + langCode + "<ul><li class='clinicule-list'>F " + infoImported.get(0)[2] + "(" + infoImported.get(0)[3] + ")" + "</ul><br><font color='#00A'>Codes</font>" + "<font color='#080'><ul><li class='clinicule-list'>Read Code (Ctv3Id): " + readCode + "</ul></font>";
            partOfResponse += "<hr>" + IDBasedInfo.getConceptDescription(conceptID, "<br>") + "<hr>";
        }
        partOfResponse += "</td></tr>" + "<tr><td colspan=\"2\">" + usedTypes + "</td></tr>" + "<tr><td colspan=\"2\">" + "<textarea disabled = \"disabled\" rows=10 cols=50>" + IDBasedInfo.getNeighboursOfConceptByID(conceptID, "\n") + "</textarea>" + "</td></tr>" + "</table>";
        return partOfResponse;
    }

    /**
     * Store type of desired Response
     * @param desiredResponse value representing the skin to be stored
     */
    protected void setResponseType(int desiredResponse) {
        responseType = desiredResponse;
    }

    /**
     * Builds a template presenting data in a drag and drop environment used to create functions like post-coordination
     * @param listOfDragItems Arraylist of items. Positions needs to be formatted in this way: (ArrayList[i] = content(type=1)/content(type=2)) 0 = label/conceptID, 1 = title of target box/-, 2 = conceptID/title of target box, 3 = -/label
     * @param type type of presentation of target-boxes. 1 = use 1 target-box. 2 = use multiple target boxes. Default is 1.
     * @param searchText Text used to fetch items found in listOfDragItems
     * @return HTML-string Presenting listOfDragItems in a DragDrop environment
     */
    public String buildDragDropSite(ArrayList<String[]> listOfDragItems, int type, String searchText) {
        ArrayList<String[]> labelInfo = listOfDragItems;
        DragDrop dropSite = new DragDrop();
        int locationOfTitle = 1;
        int labelposition = 0;
        locationOfID = 2;
        boolean multipleDropboxes;
        switch(type) {
            case 1:
                multipleDropboxes = false;
                break;
            case 2:
                locationOfID = 0;
                locationOfTitle = 2;
                labelposition = 3;
                multipleDropboxes = true;
                break;
            default:
                multipleDropboxes = false;
                break;
        }
        return dropSite.showSite(labelInfo, locationOfTitle, labelposition, locationOfID, multipleDropboxes);
    }
}
