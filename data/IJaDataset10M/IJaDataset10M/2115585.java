package gqtiv2;

import gqtitypes.*;

public class InlineChoiceInteractionHandler {

    private org.w3c.dom.Node theElement;

    private String myNodeName;

    private String parentNode;

    private DOMNodeParser parser;

    private DocumentDataStore thedataStore;

    private String responseIdentifier;

    private String shuffle;

    public InlineChoiceInteractionHandler(org.w3c.dom.Node theNode, String pNode, DocumentDataStore ds) {
        thedataStore = ds;
        theElement = theNode;
        parentNode = pNode;
        myNodeName = theElement.getNodeName();
    }

    public void run(String[] Parameters) throws gqtiexcept.XMLException {
        thedataStore.clearChoiceData();
        thedataStore.appendDocumentOutputText("<input type='hidden' size='20' name='QUESTIONTYPE' value='" + myNodeName + "'/>\n");
        responseIdentifier = qtiv2utils.selectAttribute(theElement, "responseIdentifier", "");
        shuffle = qtiv2utils.selectAttribute(theElement, "shuffle", "false");
        thedataStore.appendDocumentOutputText(thedataStore.getDocumentElementText() + "\n");
        thedataStore.setDocumentElementText("");
        String debug = thedataStore.getProperty("debug");
        if (debug.equals("ON")) System.out.println("***************INLINE CHOICE INTERACTION***********");
        if (thedataStore.getProperty("strictType").equals("ON")) {
            String maxChoices = "1";
            qtiv2utils.interactionTypeCheck(thedataStore, myNodeName, maxChoices, responseIdentifier, debug);
        }
        int childElementCount = qtiv2utils.getChildElementCount(theElement);
        for (int i = 0; i < childElementCount; i++) {
            org.w3c.dom.Node thenode = qtiv2utils.getChildElement(theElement, i);
            DOMNodeParser parser = new DOMNodeParser(thenode, myNodeName, thedataStore);
            parser.parseDocument(thenode, Parameters);
        }
        renderinlinechoice();
    }

    private void renderinlinechoice() {
        String noshufflenumbers = "";
        ChoiceData aChoice;
        int[] choicearray;
        String state = thedataStore.getState();
        int noOfChoices = thedataStore.getListLength("choicedata");
        if (state.equals("FormGeneration")) {
            if (shuffle.equals("true")) {
                for (int i = 0; i < noOfChoices; i++) {
                    aChoice = thedataStore.retrieveChoiceDataByIndex(i);
                    if (aChoice == null) {
                    }
                    if (aChoice.getFixed().equals("true")) noshufflenumbers = noshufflenumbers + Integer.toString(i) + "|";
                }
                choicearray = qtiv2utils.randomarray(noOfChoices, true, noshufflenumbers);
            } else choicearray = qtiv2utils.randomarray(noOfChoices, false, "");
        } else {
            choicearray = qtiv2utils.randomarray(noOfChoices, false, "");
        }
        String orderstring = "";
        for (int i = 0; i < noOfChoices; i++) {
            int choiceindex = choicearray[i];
            orderstring = orderstring + Integer.toString(choiceindex) + "|";
        }
        thedataStore.appendDocumentOutputText("<input type='hidden' size='20' name='" + responseIdentifier + "ORDER' value='" + orderstring + "'/>\n");
        if (!thedataStore.getDocumentPromptText().equals("")) thedataStore.appendDocumentOutputText("<strong>" + thedataStore.getDocumentPromptText() + "</strong>\n");
        thedataStore.setDocumentPromptText("");
        if (!thedataStore.getDocumentRubricText().equals("")) thedataStore.appendDocumentOutputText("<strong>" + thedataStore.getDocumentRubricText() + "</strong>\n");
        thedataStore.setDocumentRubricText("");
        thedataStore.appendDocumentOutputText("<select name = '" + responseIdentifier + "' size = 1'>\n");
        if ((state.equals("FormGeneration")) && (!qtiv2utils.hasAnswered(thedataStore, responseIdentifier))) {
            for (int i = 0; i < noOfChoices; i++) {
                int index = choicearray[i];
                aChoice = aChoice = thedataStore.retrieveChoiceDataByIndex(index);
                String choiceID = aChoice.getIdentifier();
                String choicetext = aChoice.getText();
                thedataStore.appendDocumentOutputText("<option value = '" + choiceID + "'>" + choicetext + "</option>");
            }
            thedataStore.appendDocumentOutputText("</select>");
        } else {
            String[] RequestData = thedataStore.getRequestData();
            String[] responseID = qtiv2utils.getRequestParameter(responseIdentifier, RequestData);
            String selectedtext = "";
            for (int i = 0; i < noOfChoices; i++) {
                aChoice = aChoice = thedataStore.retrieveChoiceDataByIndex(i);
                String choiceID = aChoice.getIdentifier();
                boolean found = false;
                if (responseID == null) {
                    selectedtext = "";
                    break;
                }
                for (int i2 = 0; i2 < responseID.length; i2++) {
                    if (responseID[i2].equals(choiceID)) {
                        found = true;
                        selectedtext = aChoice.getText();
                        break;
                    }
                }
                if (found) break;
            }
            for (int i = 0; i < noOfChoices; i++) thedataStore.appendDocumentOutputText("<option>" + selectedtext + "</option>");
            thedataStore.appendDocumentOutputText("</select>");
        }
    }
}
