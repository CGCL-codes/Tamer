package gov.esporing.ost.presentation.web.model;

import java.util.ArrayList;
import gov.esporing.ost.presentation.web.controller.IController;

/**
 * @uml.dependency   supplier="gov.esporing.ost.presentation.web.controller.IController"
 */
public class ViewModel {

    private String viewContent;

    private ArrayList<IViewModelListener> listeners;

    public ViewModel() {
        viewContent = "";
        listeners = new ArrayList<IViewModelListener>();
    }

    public void addListener(IViewModelListener listener) {
        listeners.add(listener);
    }

    public void removeListener(IViewModelListener listener) {
        listeners.remove(listener);
    }

    private void fireViewContentChange(String oldContent, String newContent) {
        System.out.println("Firing to " + listeners.size() + " listeneres");
        for (IViewModelListener listener : listeners) listener.viewContentChange(oldContent, newContent);
    }

    public void setViewContent(String viewContent) {
        System.out.println("Firing view content change");
        String oldContent = this.viewContent;
        this.viewContent = viewContent;
        fireViewContentChange(oldContent, viewContent);
    }

    public String getViewContent() {
        return viewContent;
    }

    /**
	 * @uml.property  name="iController"
	 * @uml.associationEnd  multiplicity="(0 -1)" dimension="1" ordering="true" inverse="viewModel:gov.esporing.ost.presentation.web.controller.IController"
	 */
    private IController[] controllers;

    /**
	 * Getter of the property <tt>iController</tt>
	 * @return  Returns the controllers.
	 * @uml.property  name="iController"
	 */
    public IController[] getIController() {
        return controllers;
    }

    /**
	 * Setter of the property <tt>iController</tt>
	 * @param iController  The controllers to set.
	 * @uml.property  name="iController"
	 */
    public void setIController(IController[] controller) {
        controllers = controller;
    }
}
