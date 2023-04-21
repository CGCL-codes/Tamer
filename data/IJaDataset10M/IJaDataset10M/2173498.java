package com.memoire.vainstall.builder.action;

import com.memoire.vainstall.builder.VAIBuilderController;
import com.memoire.vainstall.builder.VAIBuilderModel;
import javax.swing.AbstractAction;

/**
 * This is an abstract class that extends the AbstractAction class
 * with the knowledge of the VAIBuilderController and VAIBuilderModel.
 *
 * This class is single threaded and subclasses only have to implement
 * the runnit method.
 *
 * @see javax.swing.AbstractAction
 * @see com.memoire.vainstall.builder.VAIBuilderController
 * @see com.memoire.vainstall.builder.VAIBuilderModel
 *
 * @author Henrik Falk
 * @version $Id: AbstractVAIBuilderAction.java,v 1.1 2001/09/28 19:31:19 hfalk Exp $
 */
public abstract class AbstractVAIBuilderAction extends AbstractAction {

    /**
     * The controller of the builder
     */
    VAIBuilderController controller;

    /**
     * The data model of the builder
     */
    VAIBuilderModel model;

    /**
     * Default constructor
     */
    public AbstractVAIBuilderAction() {
        super();
    }

    /**
     * actionPerformed
     * @param evt java.awt.event.ActionEvent
     */
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        runnit();
    }

    /**
     * This method must be implemented by subclasses
     */
    public abstract void runnit();

    /**
     * Initializes this action
     * @param controller VAIBuilderController
     */
    public void initialize(VAIBuilderController controller) {
        this.controller = controller;
        this.model = controller.getModel();
    }

    /**
     * Returns the controller
     * @return a VAIBuilderController
     */
    protected VAIBuilderController getController() {
        return controller;
    }

    /**
     * Returns the model
     * @return a VAIBuilderModel
     */
    protected VAIBuilderModel getModel() {
        return model;
    }
}
