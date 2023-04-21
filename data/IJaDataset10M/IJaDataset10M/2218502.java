package com.objetdirect.gwt.umlapi.client.editors;

import com.objetdirect.gwt.umlapi.client.artifacts.NodePartArtifact;
import com.objetdirect.gwt.umlapi.client.artifacts.object.ObjectPartAttributesArtifact;
import com.objetdirect.gwt.umlapi.client.umlCanvas.UMLCanvas;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLObjectAttribute;

/**
 * @author Florian Mounier (mounier-dot-florian.at.gmail'dot'com)
 */
public class ObjectPartAttributesFieldEditor extends FieldEditor {

    private final UMLObjectAttribute attributeToChange;

    /**
	 * @param canvas
	 * @param objectPartAttributesArtifact
	 * @param attributeToChange
	 */
    public ObjectPartAttributesFieldEditor(final UMLCanvas canvas, final ObjectPartAttributesArtifact objectPartAttributesArtifact, final UMLObjectAttribute attributeToChange) {
        super(canvas, objectPartAttributesArtifact);
        this.attributeToChange = attributeToChange;
    }

    @Override
    protected void next() {
        ((NodePartArtifact) artifact).edit();
    }

    @Override
    protected boolean updateUMLArtifact(final String newContent) {
        if (newContent.trim().equals("")) {
            ((ObjectPartAttributesArtifact) artifact).remove(attributeToChange);
            ((ObjectPartAttributesArtifact) artifact).getNodeArtifact().rebuildGfxObject();
            return false;
        }
        final UMLObjectAttribute newAttribute = UMLObjectAttribute.parseAttribute(newContent);
        if (newAttribute.equals("")) {
            ((ObjectPartAttributesArtifact) artifact).remove(attributeToChange);
            ((ObjectPartAttributesArtifact) artifact).getNodeArtifact().rebuildGfxObject();
            return false;
        }
        attributeToChange.setAttributeName(newAttribute.getAttributeName());
        attributeToChange.setValue(newAttribute.getValue());
        ((ObjectPartAttributesArtifact) artifact).getNodeArtifact().rebuildGfxObject();
        return true;
    }
}
