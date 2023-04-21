package org.mariella.rcp.resources;

public abstract class AbstractVResourceEditorCustomizationCallback implements VResourceSingleEditorCustomizationCallback {

    private VResourceEditorPart resourceEditorPart;

    public void aboutToCloseEditor() {
    }

    public void aboutToSave() {
    }

    public void activated() {
    }

    public void deactivated() {
    }

    public void implementSetFocus() {
    }

    public void implementInit() {
    }

    public void implementDispose() {
    }

    public void setResourceEditorPart(VResourceEditorPart editorPart) {
        this.resourceEditorPart = editorPart;
    }

    public VResourceEditorPart getResourceEditorPart() {
        return resourceEditorPart;
    }

    public Object createCustomEditingContext() {
        return null;
    }

    public boolean handleVResourceSaveException(VResourceSaveException ex) {
        return false;
    }
}
