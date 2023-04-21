package uk.ac.bolton.archimate.editor.ui.services;

/**
 * Listener Interface for UI Requests
 * 
 * @author Phillip Beauvoir
 */
public interface IUIRequestListener {

    /**
     * Request an Action
     * @param request
     */
    void requestAction(UIRequest request);
}
