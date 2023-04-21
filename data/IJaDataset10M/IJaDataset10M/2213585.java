package ch.iserver.ace.algorithm;

import java.util.LinkedList;
import java.util.List;
import ch.iserver.ace.DocumentModel;
import ch.iserver.ace.Operation;
import ch.iserver.ace.text.InsertOperation;

/**
 * This is dummy document model implementation for simple text.
 * 
 * @see ch.iserver.ace.DocumentModel
 */
public class DummyDocumentModel implements DocumentModel {

    private List operations = new LinkedList();

    private String text = "";

    /**
	 * {@inheritDoc}
	 */
    public void apply(Operation operation) {
        operations.add(operation);
        if (operation instanceof InsertOperation) {
            text += ((InsertOperation) operation).getText();
        }
    }

    /**
	 * Returns all received operations.
	 * 
	 * @return a list with all received operations
	 */
    public List getOperations() {
        return operations;
    }

    /**
	 * Returns the document content.
	 * 
	 * @return the document content
	 */
    public String getText() {
        return text;
    }
}
