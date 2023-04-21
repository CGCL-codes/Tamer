package org.adapit.wctoolkit.propertyeditors.editors.model;

import org.adapit.wctoolkit.infrastructure.DefaultApplicationFrame;
import org.adapit.wctoolkit.infrastructure.propertyeditors.DefaultElementPropertyEditorPane;
import org.adapit.wctoolkit.uml.ext.core.IElement;

public class ModelPropertyEditorPane extends DefaultElementPropertyEditorPane {

    private static final long serialVersionUID = 345914589844576L;

    private ModelElementPanel modelElementPanel;

    public ModelPropertyEditorPane(DefaultApplicationFrame defaultSplitPane, IElement element) {
        super(defaultSplitPane, element);
    }

    public ModelPropertyEditorPane(DefaultApplicationFrame defaultSplitPane) {
        super(defaultSplitPane);
    }

    public ModelPropertyEditorPane() {
        super();
    }

    public void initialize() {
        modelElementPanel = new ModelElementPanel(this);
        this.add(modelElementPanel, "Project Name", 0);
    }

    public void notifyElementDestroyed() {
    }
}
