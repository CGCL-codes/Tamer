package com.google.devtools.depan.eclipse.views.tools;

import com.google.devtools.depan.eclipse.utils.DoubleElementEditorChooser;
import com.google.devtools.depan.eclipse.utils.Resources;
import com.google.devtools.depan.eclipse.views.ViewSelectionListenerTool;
import com.google.devtools.depan.model.GraphNode;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import java.util.Collection;

/**
 * A refactoring tool. For now, only shows a {@link DoubleElementEditorChooser}
 * associated with the first selected Element.
 *
 * @author ycoppel@google.com (Yohann Coppel)
 *
 */
public class RefactorTool extends ViewSelectionListenerTool {

    private DoubleElementEditorChooser chooser;

    @Override
    public void emptySelection() {
        chooser.setNoEditor();
    }

    protected void setNode(GraphNode node) {
        chooser.setEditorFor(node);
    }

    @Override
    public Image getIcon() {
        return Resources.IMAGE_REFACTORING;
    }

    @Override
    public String getName() {
        return Resources.NAME_REFACTORING;
    }

    @Override
    public Control setupComposite(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        chooser = new DoubleElementEditorChooser(composite, SWT.NONE);
        GridLayout layout = new GridLayout(2, false);
        composite.setLayout(layout);
        chooser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        return composite;
    }

    @Override
    public void updateSelectedExtend(Collection<GraphNode> extension) {
        if (extension.size() == 0) {
            emptySelection();
            return;
        }
        setNode(extension.iterator().next());
    }

    @Override
    public void updateSelectedReduce(Collection<GraphNode> reduction) {
        emptySelection();
    }

    @Override
    public void updateSelectionTo(Collection<GraphNode> selection) {
        emptySelection();
        updateSelectedExtend(selection);
    }
}
