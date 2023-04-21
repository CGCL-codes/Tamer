package eu.soa4all.execution.adapter.parser.mismatches;

import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.jdom.Element;
import eu.soa4all.execution.adapter.parser.types.MessageName;
import eu.soa4all.execution.adapter.parser.types.OperationName;

/**
 * Class MessageNameMismatch
 */
public class MessageNameMismatch implements Mismatch {

    static Logger log = Logger.getLogger(MessageNameMismatch.class);

    private MessageName input;

    private MessageName mapping;

    public static final String[] messageRenameString = {};

    public MessageNameMismatch(Element elem) {
        parse(elem);
    }

    private void parse(Element elem) {
        Element input = null, mapping = null;
        List list = elem.getChildren();
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            Element temp = (Element) iter.next();
            if (temp.getName().equalsIgnoreCase("Input")) input = temp;
            if (temp.getName().equalsIgnoreCase("Mapping")) mapping = temp;
        }
        String name = "", side = "";
        String opName = "", opSide = "";
        OperationName operationName = new OperationName();
        list = input.getChildren();
        iter = list.iterator();
        while (iter.hasNext()) {
            Element temp = (Element) iter.next();
            if (temp.getName().equalsIgnoreCase("Name")) name = temp.getTextTrim();
            if (temp.getName().equalsIgnoreCase("ReferenceOperation")) {
                List refOpList = temp.getChildren();
                Iterator refOpIter = refOpList.iterator();
                while (refOpIter.hasNext()) {
                    Element refOp = (Element) refOpIter.next();
                    if (refOp.getName().equalsIgnoreCase("Name")) opName = refOp.getTextTrim();
                    if (refOp.getName().equalsIgnoreCase("Side")) opSide = refOp.getTextTrim();
                }
                operationName = new OperationName(opName, opSide);
            }
            if (temp.getName().equalsIgnoreCase("Side")) side = temp.getTextTrim();
        }
        this.input = new MessageName(name, side, operationName);
        name = "";
        side = "";
        opName = "";
        opSide = "";
        operationName = new OperationName();
        list = mapping.getChildren();
        iter = list.iterator();
        while (iter.hasNext()) {
            Element temp = (Element) iter.next();
            if (temp.getName().equalsIgnoreCase("Name")) name = temp.getTextTrim();
            if (temp.getName().equalsIgnoreCase("ReferenceOperation")) {
                List refOpList = temp.getChildren();
                Iterator refOpIter = refOpList.iterator();
                while (refOpIter.hasNext()) {
                    Element refOp = (Element) refOpIter.next();
                    if (refOp.getName().equalsIgnoreCase("Name")) opName = refOp.getTextTrim();
                    if (refOp.getName().equalsIgnoreCase("Side")) opSide = refOp.getTextTrim();
                }
                operationName = new OperationName(opName, opSide);
            }
            if (temp.getName().equalsIgnoreCase("Side")) side = temp.getTextTrim();
        }
        this.mapping = new MessageName(name, side, operationName);
        log.debug("New MessageNameMismatch built successfully: " + this.input.getName() + " -> " + this.mapping.getName());
    }

    /**
	 * Discover if this mismatch is involved in the adaption process
	 */
    public boolean discoverMismatch(String name, String side) {
        int i;
        for (i = 0; i < messageRenameString.length; i++) {
            if (name.equalsIgnoreCase(messageRenameString[i])) {
                String inputSide = input.getSide();
                if (inputSide.equalsIgnoreCase(side)) return true;
            }
        }
        return false;
    }

    /**
	 * @return
	 * @uml.property  name="input"
	 */
    public MessageName getInput() {
        return input;
    }

    /**
	 * @return
	 * @uml.property  name="mapping"
	 */
    public MessageName getMapping() {
        return mapping;
    }
}
