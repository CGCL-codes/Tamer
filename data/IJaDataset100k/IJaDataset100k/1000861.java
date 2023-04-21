package com.foursoft.foureveredit.view.impl;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import com.foursoft.fourever.objectmodel.ComplexInstance;
import com.foursoft.fourever.objectmodel.Instance;
import com.foursoft.fourever.objectmodel.ObjectModel;
import com.foursoft.foureveredit.controller.GenericController;
import com.foursoft.foureveredit.controller.command.CheckCommand;
import com.foursoft.foureveredit.controller.command.CloseAllObjectModelsCommand;
import com.foursoft.foureveredit.controller.command.CloseCurrentObjectModelCommand;
import com.foursoft.foureveredit.controller.command.CreateNewEmptyModelFromSchemaCommand;
import com.foursoft.foureveredit.controller.command.CreateNewModelFromTemplateCommand;
import com.foursoft.foureveredit.controller.command.ExitCommand;
import com.foursoft.foureveredit.controller.command.ExportCommand;
import com.foursoft.foureveredit.controller.command.LinkSubDocToCurrentFromFileCommand;
import com.foursoft.foureveredit.controller.command.LinkSubDocToCurrentFromTemplateCommand;
import com.foursoft.foureveredit.controller.command.MoveDownCommand;
import com.foursoft.foureveredit.controller.command.MoveUpCommand;
import com.foursoft.foureveredit.controller.command.OpenObjectModelCommand;
import com.foursoft.foureveredit.controller.command.RedoCommand;
import com.foursoft.foureveredit.controller.command.SaveCurrentObjectModelCommand;
import com.foursoft.foureveredit.controller.command.SetCurrentDocumentRootLocationCommand;
import com.foursoft.foureveredit.controller.command.SetCurrentFragmentLocationCommand;
import com.foursoft.foureveredit.controller.command.SetSelectedInstanceCommand;
import com.foursoft.foureveredit.controller.command.SortCurrentComplexInstanceCommand;
import com.foursoft.foureveredit.controller.command.UndoCommand;
import com.foursoft.foureveredit.view.ComplexInstanceView;
import com.foursoft.foureveredit.view.FoureverEditView;
import com.foursoft.foureveredit.view.ObjectModelView;
import com.foursoft.foureveredit.view.ViewManager;
import com.foursoft.mvc.view.AbstractView;

/**
 * Provides a base implementation to be reused by the concrete implementations.
 * 
 * @author bergner
 * @version $Revision: 1.13 $
 * @see com.fourever.foureveredit.view.GenericSimpleInstanceView
 */
public abstract class FoureverEditViewBase extends AbstractView implements FoureverEditView, PropertyChangeListener {

    /**
	 * the viewed object model
	 */
    protected ObjectModel objectModel;

    /**
	 * the currently selected instance
	 */
    protected Instance selectedInstance;

    /**
	 * the object model subview
	 */
    protected ObjectModelView objectModelView;

    /**
	 * the complex instance subview
	 */
    protected ComplexInstanceView complexInstanceView;

    /**
	 * the CreateNewEmptyModelFromSchemaCommand to be used in the implementation
	 */
    protected CreateNewEmptyModelFromSchemaCommand createFromSchemaCommand = null;

    /**
	 * the CreateNewModelFromTemplateCommand to be used in the implementation
	 */
    protected CreateNewModelFromTemplateCommand createNewModelFromTemplateCommand = null;

    /**
	 * the OpenObjectModelCommand to be used in the implementation
	 */
    protected OpenObjectModelCommand openCommand = null;

    /**
	 * the CloseObjectModelCommand to be used in the implementation
	 */
    protected CloseCurrentObjectModelCommand closeCommand = null;

    /**
	 * the CloseAllObjectModelsCommand to be used in the implementation
	 */
    protected CloseAllObjectModelsCommand closeAllCommand = null;

    /**
	 * the LinkSubDocToCurrentFromTemplateCommand to be used in the
	 * implementation
	 */
    protected LinkSubDocToCurrentFromTemplateCommand readSubDocFromTemplateCommand = null;

    /**
	 * the LinkSubDocToCurrentFromFileCommand to be used in the implementation
	 */
    protected LinkSubDocToCurrentFromFileCommand readSubDocFromFileCommand = null;

    /**
	 * the SetCurrentFragmentLocationCommand to be used in the implementation
	 */
    protected SetCurrentFragmentLocationCommand setCurrentFragmentLocationCommand = null;

    /**
	 * the SetCurrentFragmentLocationCommand to be used in the implementation
	 */
    protected SetCurrentDocumentRootLocationCommand setLocationOfCurrentDocRootCommand = null;

    /**
	 * the ExportCommand to be used in the implementation
	 */
    protected ExportCommand exportCommand = null;

    /**
	 * the CheckCommand to be used in the implementation
	 */
    protected CheckCommand checkCommand = null;

    /**
	 * the UndoCommand to be used in the implementation
	 */
    protected UndoCommand undoCommand = null;

    /**
	 * the RedoCommand to be used in the implementation
	 */
    protected RedoCommand redoCommand = null;

    /**
	 * the SortCurrentComplexInstanceCommand to be used in the implementation
	 */
    protected SortCurrentComplexInstanceCommand sortCommand = null;

    /**
	 * the MoveUpCommand to be used in the implementation
	 */
    protected MoveUpCommand moveUpCommand = null;

    /**
	 * the MoveDownCommand to be used in the implementation
	 */
    protected MoveDownCommand moveDownCommand = null;

    /**
	 * the ExitCommand to be used in the implementation
	 */
    protected ExitCommand exitCommand = null;

    /**
	 * the SaveCurrentObjectModelCommand to be used in the implementation
	 */
    protected SaveCurrentObjectModelCommand saveCommand = null;

    /**
	 * the SetSelectedInstanceCommand to be used in the implementation
	 */
    protected SetSelectedInstanceCommand setSelectedInstanceCommand = null;

    /**
	 * Construct the base implementation part of this view. To be enriched by
	 * the implementation class.
	 * 
	 * @param c
	 *            the controller to be set
	 */
    protected FoureverEditViewBase(GenericController ctrl) {
        super(ctrl);
        undoCommand = (UndoCommand) ctrl.getCommand("Undo");
        redoCommand = (RedoCommand) ctrl.getCommand("Redo");
        createFromSchemaCommand = (CreateNewEmptyModelFromSchemaCommand) ctrl.getCommand("CreateNewEmptyModelFromSchema");
        createNewModelFromTemplateCommand = (CreateNewModelFromTemplateCommand) ctrl.getCommand("CreateNewModelFromTemplate");
        openCommand = (OpenObjectModelCommand) ctrl.getCommand("OpenObjectModel");
        saveCommand = (SaveCurrentObjectModelCommand) ctrl.getCommand("SaveCurrentObjectModel");
        closeCommand = (CloseCurrentObjectModelCommand) ctrl.getCommand("CloseCurrentObjectModel");
        closeAllCommand = (CloseAllObjectModelsCommand) ctrl.getCommand("CloseAllObjectModels");
        exitCommand = (ExitCommand) ctrl.getCommand("Exit");
        sortCommand = (SortCurrentComplexInstanceCommand) ctrl.getCommand("SortCurrentComplexInstance");
        moveUpCommand = (MoveUpCommand) ctrl.getCommand("MoveUp");
        moveDownCommand = (MoveDownCommand) ctrl.getCommand("MoveDown");
        setSelectedInstanceCommand = (SetSelectedInstanceCommand) ctrl.getCommand("SetSelectedInstance");
        setCurrentFragmentLocationCommand = (SetCurrentFragmentLocationCommand) ctrl.getCommand("SetCurrentFragmentLocation");
        setLocationOfCurrentDocRootCommand = (SetCurrentDocumentRootLocationCommand) ctrl.getCommand("SetCurrentDocumentRootLocation");
        readSubDocFromFileCommand = (LinkSubDocToCurrentFromFileCommand) ctrl.getCommand("LinkSubDocToCurrentFromFile");
        readSubDocFromTemplateCommand = (LinkSubDocToCurrentFromTemplateCommand) ctrl.getCommand("LinkSubDocToCurrentFromTemplate");
        exportCommand = (ExportCommand) ctrl.getCommand("Export");
        checkCommand = (CheckCommand) ctrl.getCommand("Check");
        objectModelView = getViewManager().createObjectModelView(ctrl);
        complexInstanceView = getViewManager().createComplexInstanceView(ctrl);
        getController().addControllerPropertyListener("selectedInstance", this);
    }

    /**
	 * Convenience operation to be able to use the ViewManager
	 * 
	 * @return the view manager of the view component
	 */
    public abstract ViewManager getViewManager();

    /**
	 * Base implementation for getting the object model to be viewed.
	 * 
	 * @return the object model to be viewed
	 */
    public ObjectModel getObjectModel() {
        return objectModel;
    }

    /**
	 * Base implementation for setting the object model to be viewed. To be
	 * enriched by the implementation class.
	 * 
	 * @param om
	 *            the object model to be viewed
	 */
    public void setObjectModel(ObjectModel om) {
        if (om == objectModel) {
            return;
        }
        objectModel = om;
        objectModelView.setObjectModel(om);
    }

    /**
	 * Base implementation for getting the selected instance.
	 * 
	 * @return the selected instance
	 */
    public Instance getSelectedInstance() {
        return selectedInstance;
    }

    /**
	 * Base implementation for setting the selected instance. To be enriched by
	 * the implementation class.
	 * 
	 * @param selected
	 *            the selected instance to be set
	 */
    public void setSelectedInstance(Instance selected) {
        if (selected instanceof ComplexInstance) {
            selectedInstance = selected;
        } else {
            selectedInstance = null;
        }
    }

    /**
	 * Base implementation for reacting on notifications from the controller
	 * about controller state changes. Currently only reacts on changes of the
	 * <em>selectedInstance</em> controller property.
	 * 
	 * @param evt
	 *            the controller property change event
	 */
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("selectedInstance")) {
            Instance selected = (Instance) evt.getNewValue();
            setSelectedInstance(selected);
        } else if (evt.getPropertyName().equals("currentObjectModel")) {
            objectModelView.setObjectModel((ObjectModel) evt.getNewValue());
        } else {
            assert (false);
        }
    }

    /**
	 * Base implementation for destroying this view. To be enriched by the
	 * implementation class.
	 */
    public void destroy() {
        openCommand = null;
        closeCommand = null;
        readSubDocFromTemplateCommand = null;
        readSubDocFromFileCommand = null;
        setCurrentFragmentLocationCommand = null;
        setLocationOfCurrentDocRootCommand = null;
        exportCommand = null;
        checkCommand = null;
        undoCommand = null;
        redoCommand = null;
        sortCommand = null;
        moveUpCommand = null;
        moveDownCommand = null;
        exitCommand = null;
        saveCommand = null;
        setSelectedInstanceCommand = null;
        complexInstanceView.destroy();
        complexInstanceView = null;
        objectModelView.destroy();
        objectModelView = null;
    }
}
