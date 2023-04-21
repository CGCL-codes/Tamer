package org.dyno.visual.swing.types.editor;

import java.util.HashMap;
import javax.swing.border.Border;
import org.dyno.visual.swing.types.editor.borders.BorderContentProvider;
import org.dyno.visual.swing.types.editor.borders.BorderType;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.views.properties.PropertySheetPage;

public class BorderDialog extends Dialog {

    private ComboViewer viewer;

    private PropertySheetPage propertyPage;

    public BorderDialog(Shell parentShell) {
        super(parentShell);
        borders = new HashMap<BorderType, Border>();
    }

    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        getShell().setText(Messages.BorderDialog_Title);
        Composite innerComposite = new Composite(composite, SWT.NONE);
        innerComposite.setLayoutData(new GridData());
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        innerComposite.setLayout(layout);
        Label lbl = new Label(innerComposite, SWT.NONE);
        lbl.setText(Messages.BorderDialog_Type);
        GridData data = new GridData();
        lbl.setLayoutData(data);
        Combo cmbType = new Combo(innerComposite, SWT.DROP_DOWN | SWT.READ_ONLY);
        viewer = new ComboViewer(cmbType);
        viewer.setContentProvider(new BorderContentProvider());
        viewer.setInput(BorderType.getBorderTypes());
        BorderType type = BorderType.getBorderType(border);
        ISelection selection = null;
        if (type != null) {
            borders.put(type, border);
            selection = createSelection(type);
            viewer.setSelection(selection);
        }
        viewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                viewer_selectionChanged(event.getSelection());
            }
        });
        data = new GridData();
        data.widthHint = 280;
        cmbType.setLayoutData(data);
        propertyPage = new PropertySheetPage();
        Composite container = new Composite(innerComposite, SWT.BORDER);
        data = new GridData(GridData.FILL_BOTH);
        data.horizontalSpan = 2;
        data.heightHint = 170;
        data.horizontalIndent = 36;
        container.setLayoutData(data);
        FillLayout fLayout = new FillLayout();
        propertyPage.createControl(container);
        container.setLayout(fLayout);
        if (selection != null) viewer_selectionChanged(selection);
        applyDialogFont(composite);
        return composite;
    }

    private void viewer_selectionChanged(ISelection selection) {
        if (selection instanceof StructuredSelection && !selection.isEmpty()) {
            BorderType type = (BorderType) ((StructuredSelection) selection).getFirstElement();
            if (borders.containsKey(type)) {
                border = borders.get(type);
            } else {
                border = type.createBorder();
                borders.put(type, border);
            }
            propertyPage.setPropertySourceProvider(null);
            if (border != null) {
                propertyPage.selectionChanged(null, createSelection(border));
                propertyPage.setPropertySourceProvider(type.getPropertySourceProvider(border));
            }
        }
    }

    protected void okPressed() {
        ISelection selection = viewer.getSelection();
        if (selection.isEmpty()) {
            MessageDialog.openError(getShell(), Messages.BorderDialog_Error, Messages.BorderDialog_Prompt);
            return;
        }
        super.okPressed();
    }

    private ISelection createSelection(Object value) {
        return new StructuredSelection(value);
    }

    public void setBorder(Border border) {
        this.border = border;
    }

    public Border getBorder() {
        return border;
    }

    private Border border;

    private HashMap<BorderType, Border> borders;
}
