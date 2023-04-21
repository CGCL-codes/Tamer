package uk.ac.bolton.archimate.editor.views.tree;

import uk.ac.bolton.archimate.editor.ui.services.UIRequest;

/**
 * Tree Edit Name in-place command
 * 
 * @author Phillip Beauvoir
 */
public class TreeEditElementRequest extends UIRequest {

    public static final String REQUEST_NAME = "request.editElement";

    /**
     * @param source The source of the request
     * @param selection The selection
     * @param doReveal Whether to reveal the selection
     */
    public TreeEditElementRequest(Object source, Object element) {
        super(source, REQUEST_NAME, element);
    }
}
