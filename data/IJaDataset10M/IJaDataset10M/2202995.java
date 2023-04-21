package gqtiv2;

import gqtitypes.*;

public class MappingHandler {

    private org.w3c.dom.Node theElement;

    private String myNodeName;

    private String parentNode;

    private DOMNodeParser parser;

    private DocumentDataStore thedataStore;

    private String identifier;

    private String value;

    private Boolean isnull;

    private ItemVariable responseVariable;

    public MappingHandler(org.w3c.dom.Node theNode, String pNode, DocumentDataStore ds) {
        thedataStore = ds;
        theElement = theNode;
        parentNode = pNode;
        myNodeName = theElement.getNodeName();
    }

    public void run(String[] Parameters) {
        String variableClass;
        String identifier;
        MapEntry theEntry;
        AreaMapEntry theAreaEntry;
        org.w3c.dom.Node thenode;
        int childCount = theElement.getChildNodes().getLength();
        int childElementCount = qtiv2utils.getChildElementCount(theElement);
        identifier = Parameters[0];
        variableClass = parentNode;
        String lowerBound = qtiv2utils.selectAttribute(theElement, "lowerBound", "");
        String upperBound = qtiv2utils.selectAttribute(theElement, "upperBound", "");
        String defaultValue = qtiv2utils.selectAttribute(theElement, "defaultValue", "");
        responseVariable = thedataStore.retrieveVariable(identifier);
        if (myNodeName.equals("mapping")) responseVariable.setMapping(lowerBound, upperBound, defaultValue, childElementCount, false);
        if (myNodeName.equals("areaMapping")) responseVariable.setAreaMapping(defaultValue, childElementCount, false);
        int count = 0;
        for (int i = 0; i < childCount; i++) {
            thenode = theElement.getChildNodes().item(i);
            if (thenode.getNodeType() == 1) {
                if (myNodeName.equals("mapping")) {
                    theEntry = collectMapEntry(thenode);
                    responseVariable.setMapEntry(theEntry, count);
                    count++;
                }
                if (myNodeName.equals("areaMapping")) {
                    theAreaEntry = collectAreaMapEntry(thenode);
                    responseVariable.setAreaMapEntry(theAreaEntry, count);
                    count++;
                }
            }
        }
    }

    private MapEntry collectMapEntry(org.w3c.dom.Node thenode) {
        MapEntry returnedEntry;
        String mapKey = qtiv2utils.selectAttribute(thenode, "mapKey", "");
        String mappedValue = qtiv2utils.selectAttribute(thenode, "mappedValue", "");
        returnedEntry = new MapEntry(mapKey, mappedValue, false);
        return returnedEntry;
    }

    private AreaMapEntry collectAreaMapEntry(org.w3c.dom.Node thenode) {
        AreaMapEntry returnedEntry;
        String shape = qtiv2utils.selectAttribute(thenode, "shape", "");
        String coords = qtiv2utils.selectAttribute(thenode, "coords", "");
        String mappedValue = qtiv2utils.selectAttribute(thenode, "mappedValue", "");
        returnedEntry = new AreaMapEntry(mappedValue, shape, coords, false);
        return returnedEntry;
    }
}
