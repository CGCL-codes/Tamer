package org.adapit.wctoolkit.propertyeditors.editors.comment;

import org.adapit.wctoolkit.infrastructure.DefaultApplicationFrame;
import org.adapit.wctoolkit.infrastructure.propertyeditors.DefaultElementPropertyEditorPane;
import org.adapit.wctoolkit.propertyeditors.form.element.comment.CommentEditorPanel;
import org.adapit.wctoolkit.uml.ext.core.IElement;

public class CommentPropertyEditor extends DefaultElementPropertyEditorPane {

    private static final long serialVersionUID = 358248246836L;

    protected CommentEditorPanel baseEditorPanel;

    public CommentPropertyEditor(DefaultApplicationFrame defaultSplitPane, IElement element) {
        super(defaultSplitPane, element);
    }

    public CommentPropertyEditor(DefaultApplicationFrame defaultSplitPane) {
        super(defaultSplitPane);
    }

    public CommentPropertyEditor() {
        super();
    }

    public void initialize() {
        baseEditorPanel = new CommentEditorPanel(this);
        add("Comment", baseEditorPanel);
    }

    public void notifyElementDestroyed() {
    }
}
