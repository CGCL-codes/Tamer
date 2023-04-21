package gqtitests;

public class BranchRuleHandler {

    private org.w3c.dom.Node theElement;

    private String myNodeName;

    private String parentNode;

    private gqtiv2.DOMNodeParser parser;

    private gqtiv2.DocumentDataStore thedataStore;

    private String[] returnedValue;

    public BranchRuleHandler(org.w3c.dom.Node theNode, String pNode, gqtiv2.DocumentDataStore ds) {
        thedataStore = ds;
        theElement = theNode;
        parentNode = pNode;
        myNodeName = theElement.getNodeName();
    }

    public String[] run(String[] Parameters) throws gqtiexcept.XMLException {
        String typeinfo = "";
        String debug = "";
        String target = gqtiv2.qtiv2utils.selectAttribute(theElement, "target", "", false, thedataStore);
        org.w3c.dom.Node thenode = gqtiv2.qtiv2utils.getChildElement(theElement, 0);
        gqtiv2.DOMNodeParser parser = new gqtiv2.DOMNodeParser(thenode, myNodeName, thedataStore);
        returnedValue = parser.parseDocument(thenode, Parameters);
        if (returnedValue == null) {
            String[] outputValue = new String[2];
            outputValue[0] = "boolean:single";
            outputValue[1] = "UNDETERMINED";
            return outputValue;
        }
        typeinfo = returnedValue[0];
        debug = thedataStore.getProperty("debug");
        if (thedataStore.getProperty("strictType").equals("ON")) {
            gqtiv2.qtiv2utils.typeCheck(thedataStore, "boolean", "single", typeinfo, myNodeName, debug);
        }
        if (returnedValue[1].equals("FALSE")) {
            returnedValue[1] = "UNDETERMINED";
            if (debug.equals("ON")) System.out.println("BRANCH RULE OUTPUT NOT NULL. BUT IS UNDETERMINED");
            return returnedValue;
        } else {
            if (debug.equals("ON")) System.out.println("BRANCH RULE OUTPUT NOT NULL. TARGET IS " + target);
            String[] outputValue = new String[2];
            outputValue[0] = "identifier:single";
            outputValue[1] = target;
            return outputValue;
        }
    }
}
