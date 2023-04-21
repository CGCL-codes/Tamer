package com.objetdirect.gwt.umlapi.client.editors;

import java.util.List;
import com.objetdirect.gwt.umlapi.client.artifacts.sequence.LifeLineArtifact;
import com.objetdirect.gwt.umlapi.client.umlCanvas.UMLCanvas;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLLifeLine;

/**
 * This field editor is a specialized editor for LifeLine editing
 * 
 * @author Florian Mounier (mounier-dot-florian.at.gmail'dot'com)
 * 
 */
public class LifeLineFieldEditor extends FieldEditor {

    /**
	 * Constructor of the {@link LifeLineFieldEditor}
	 * 
	 * @param canvas
	 *            The canvas on which is the artifact
	 * @param artifact
	 *            The artifact being edited
	 */
    public LifeLineFieldEditor(final UMLCanvas canvas, final LifeLineArtifact artifact) {
        super(canvas, artifact);
    }

    @Override
    protected void next() {
    }

    @Override
    protected boolean updateUMLArtifact(final String newContent) {
        final List<String> newNameInstance = UMLLifeLine.parseName(newContent);
        if (newNameInstance.get(1).equals("")) {
            ((LifeLineArtifact) artifact).setName("LifeLine");
        } else {
            ((LifeLineArtifact) artifact).setName(newNameInstance.get(1));
        }
        ((LifeLineArtifact) artifact).setInstance(newNameInstance.get(0));
        artifact.rebuildGfxObject();
        return false;
    }
}
