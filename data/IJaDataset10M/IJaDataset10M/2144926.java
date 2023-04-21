package org.mustardseed.editors;

import org.eclipse.ui.editors.text.TextEditor;

public class DiagramEditor extends TextEditor {

    private ColorManager colorManager;

    public DiagramEditor() {
        super();
        colorManager = new ColorManager();
        setSourceViewerConfiguration(new XMLConfiguration(colorManager));
        setDocumentProvider(new XMLDocumentProvider());
    }

    public void dispose() {
        colorManager.dispose();
        super.dispose();
    }
}
