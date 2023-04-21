package gqtiv2;

import gqtitypes.*;

public class SimpleChoiceHandler {

    private org.w3c.dom.Node theElement;

    private String myNodeName;

    private String parentNode;

    private DOMNodeParser parser;

    private DocumentDataStore thedataStore;

    private String identifier;

    private String fixed;

    private String theText = "";

    private String feedbackInlineText = "";

    private String[] returnedValue;

    private String matchMax = "";

    private String setName = "";

    public SimpleChoiceHandler(org.w3c.dom.Node theNode, String pNode, DocumentDataStore ds) {
        thedataStore = ds;
        theElement = theNode;
        parentNode = pNode;
        myNodeName = theElement.getNodeName();
    }

    public void run(String[] Parameters) throws gqtiexcept.XMLException {
        identifier = qtiv2utils.selectAttribute(theElement, "identifier", "");
        fixed = qtiv2utils.selectAttribute(theElement, "fixed", "false");
        if (myNodeName.equals("simpleAssociableChoice")) matchMax = qtiv2utils.selectAttribute(theElement, "matchMax", "");
        if (Parameters != null) setName = Parameters[0];
        int childCount = theElement.getChildNodes().getLength();
        for (int i = 0; i < childCount; i++) {
            org.w3c.dom.Node thenode = theElement.getChildNodes().item(i);
            DOMNodeParser parser = new DOMNodeParser(thenode, myNodeName, thedataStore);
            returnedValue = parser.parseDocument(thenode, Parameters);
            if (thenode.getNodeName().equals("feedbackInline")) if (returnedValue != null) feedbackInlineText = returnedValue[1];
        }
        if (!thedataStore.getDocumentElementText().equals("")) {
            theText = thedataStore.getDocumentElementText();
            thedataStore.setDocumentElementText("");
        }
        if (!feedbackInlineText.equals("")) theText = feedbackInlineText;
        ChoiceData thedata = new ChoiceData(setName, identifier, matchMax, fixed, theText, "");
        thedataStore.addChoiceData(thedata);
    }
}
