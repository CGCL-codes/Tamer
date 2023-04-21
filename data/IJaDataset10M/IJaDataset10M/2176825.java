package hu.gbalage.owl.editor.internal;

import hu.gbalage.owl.editor.IOWLEditorItem;
import hu.gbalage.owl.editor.OWLEditorManager;
import hu.gbalage.owl.editor.TabManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

/**
 * @author balage
 *
 */
public class OWLEditorItemContainer extends Composite {

    Label label;

    Composite composite;

    IOWLEditorItem item;

    public OWLEditorItemContainer(Composite parent, int style, IOWLEditorItem item, OWLEditorManager manager, TabManager tabManager) {
        super(parent, style);
        this.setLayout(new FormLayout());
        this.item = item;
        label = new Label(this, SWT.NONE);
        label.setText(item.getTitle());
        label.setBackground(new Color(Display.getCurrent(), item.getColor()));
        composite = new Composite(this, SWT.NONE);
        item.init(composite, manager, tabManager);
        FormData data = new FormData();
        data.top = new FormAttachment(0, 3);
        data.left = new FormAttachment(0, 3);
        data.right = new FormAttachment(100, -3);
        label.setLayoutData(data);
        data = new FormData();
        data.top = new FormAttachment(label, 3);
        data.left = new FormAttachment(0);
        data.right = new FormAttachment(100);
        data.bottom = new FormAttachment(100);
        composite.setLayoutData(data);
        this.layout();
    }
}
