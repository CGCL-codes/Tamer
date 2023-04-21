package com.timenes.clips.platform.controller;

import com.timenes.clips.platform.Platform;
import com.timenes.clips.platform.model.ModelObject;
import com.timenes.clips.platform.model.ReferenceableModelObject;
import com.timenes.clips.platform.utils.List;
import com.timenes.clips.platform.view.View;

/**
 * @author helge@timenes.com
 * 
 */
public abstract class Controller {

    private View view;

    private ModelObject modelObject;

    private ContainerController container;

    private List<ReferenceableController> refrerences;

    public Controller(ContainerController container, ModelObject modelObject) {
        this.container = container;
        this.modelObject = modelObject;
        this.refrerences = Platform.getUtilsFactory().createList(ReferenceableController.class);
        this.view = Platform.getViewFactory().createView(container != null ? container.getView() : null, modelObject);
    }

    public void deleteReference(ReferenceableController ctrl) {
        if (refrerences.contains(ctrl)) {
            refrerences.remove(ctrl);
            modelObject.getReferences().remove(ctrl.getModelObject());
            view.getChildren().remove(ctrl.getView());
        }
        if (ctrl.getReferencingControllers().contains(this)) {
            ctrl.deleteReferencee(this);
        }
    }

    public void insertReference(ReferenceableController ctrl, int index) {
        refrerences.add(index, ctrl);
        view.insertChild(ctrl.getView(), index);
        if (!ctrl.getReferencingControllers().contains(this)) {
            ctrl.getReferencingControllers().add(this);
        }
    }

    public List<ReferenceableController> getReferences() {
        return refrerences;
    }

    public void clearAllReferences() {
        if (modelObject instanceof ReferenceableModelObject) {
            ((ReferenceableModelObject) modelObject).getReferences().clear();
        }
    }

    public Controller copy() {
        try {
            Controller copy = getClass().newInstance();
            copy.modelObject = modelObject.copy();
            copy.container = container;
            copy.view = Platform.getViewFactory().createView(view.getParent(), copy.modelObject);
            return copy;
        } catch (InstantiationException e) {
            throw new RuntimeException("Failed to creat copy of " + getClass().getSimpleName());
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to creat copy of " + getClass().getSimpleName());
        }
    }

    /**
	 * Sets the container of this controller
	 * @param container The container
	 */
    public void setContainer(ContainerController container) {
        this.container = container;
        if (view.getParent() != null) {
            view.getParent().getChildren().remove(view);
        }
        if (container != null) {
            view.setParent(container.getView());
            container.getView().getChildren().add(view);
        }
    }

    /**
	 * @return the container of this controller, or null if root
	 */
    public ContainerController getContainer() {
        return container;
    }

    /**
	 * @return the model object that this controller controls
	 */
    public ModelObject getModelObject() {
        return modelObject;
    }

    public void setModelObject(ModelObject modelObject) {
        this.modelObject = modelObject;
    }

    public View getView() {
        return view;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + getModelObject().getClass().getSimpleName() + "]";
    }
}
