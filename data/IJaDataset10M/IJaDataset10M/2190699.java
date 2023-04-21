package com.tensegrity.palorules.dialogs;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.palo.api.Cube;
import org.palo.api.Rule;
import com.tensegrity.palorules.PaloRulesPlugin;

/**
 * Dialog to configure the deletion of rules.
 * @author Andreas Ebbert
 * @version $Id: DeleteRulesDialog.java,v 1.1 2007/12/06 18:23:41 AndreasEbbert
 *          Exp $
 */
public class DeleteRulesDialog extends TitleAreaDialog {

    private final Rule[] rules;

    private final String title;

    private final String desc, desc2;

    private CheckboxTableViewer viewer;

    private Object checkedItems[];

    private static final Image IMG;

    static {
        IMG = PaloRulesPlugin.getImageDescriptor("icons/rules.gif").createImage();
    }

    public DeleteRulesDialog(Shell shell, Cube cube) {
        super(shell);
        setShellStyle(getShellStyle() | SWT.RESIZE);
        if (cube == null) throw new IllegalArgumentException("Cube must not be null!");
        this.rules = cube.getRules();
        this.checkedItems = new Object[0];
        this.title = DialogMessages.getString("dlg.delete.rules.title");
        this.desc = DialogMessages.getString("dlg.delete.rules.msg", new String[] { cube.getName() });
        this.desc2 = DialogMessages.getString("dlg.delete.rules.msg2");
    }

    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText(title);
    }

    protected Point getInitialSize() {
        Point p = super.getInitialSize();
        return new Point(p.x, p.x * 11 / 10);
    }

    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        setTitle(title);
        setMessage(desc);
        Composite content = new Composite(composite, SWT.NONE);
        content.setLayoutData(new GridData(GridData.FILL_BOTH));
        {
            GridLayout layout = new GridLayout(1, false);
            content.setLayout(layout);
        }
        new Label(content, SWT.NONE).setText(desc2);
        Table table = new Table(content, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.SINGLE | SWT.FULL_SELECTION);
        {
            GridData gd = new GridData(GridData.FILL_BOTH);
            table.setLayoutData(gd);
        }
        viewer = new CheckboxTableViewer(table);
        viewer.addCheckStateListener(new ICheckStateListener() {

            public void checkStateChanged(CheckStateChangedEvent e) {
                checkedItems = viewer.getCheckedElements();
            }
        });
        viewer.setLabelProvider(new ItemLabelProvider());
        viewer.setContentProvider(new DelRuleContentProvider());
        viewer.setInput(rules);
        checkedItems = viewer.getCheckedElements();
        Composite buttons = new Composite(content, SWT.NONE);
        {
            GridData gd = new GridData();
            gd.horizontalAlignment = SWT.END;
            buttons.setLayoutData(gd);
        }
        {
            GridLayout layout = new GridLayout(2, false);
            buttons.setLayout(layout);
        }
        final Button selectAll;
        final Button deselectAll;
        selectAll = new Button(buttons, SWT.PUSH);
        selectAll.setText(DialogMessages.getString("button.select_all"));
        selectAll.setFont(JFaceResources.getDialogFont());
        setButtonLayoutData(selectAll);
        deselectAll = new Button(buttons, SWT.PUSH);
        deselectAll.setText(DialogMessages.getString("button.deselect_all"));
        deselectAll.setFont(JFaceResources.getDialogFont());
        setButtonLayoutData(deselectAll);
        SelectionAdapter selectionListener = new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                if (e.getSource() == selectAll) {
                    viewer.setAllChecked(true);
                    checkedItems = viewer.getCheckedElements();
                } else if (e.getSource() == deselectAll) {
                    viewer.setAllChecked(false);
                    checkedItems = new Object[0];
                }
            }
        };
        selectAll.addSelectionListener(selectionListener);
        deselectAll.addSelectionListener(selectionListener);
        Label sep = new Label(content, SWT.SEPARATOR | SWT.HORIZONTAL);
        sep.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        return composite;
    }

    protected void okPressed() {
        super.okPressed();
    }

    public Object[] getCheckedItems() {
        return checkedItems;
    }

    private static class DelRuleContentProvider implements IStructuredContentProvider {

        private Rule[] elements;

        public Object[] getElements(Object inputElement) {
            return elements;
        }

        public void dispose() {
            elements = null;
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            if (!(newInput instanceof Rule[])) return;
            elements = (Rule[]) newInput;
        }
    }

    private static final class ItemLabelProvider extends LabelProvider {

        public Image getImage(Object element) {
            return IMG;
        }

        public String getText(Object element) {
            if (element instanceof Rule) {
                return ((Rule) element).getDefinition();
            }
            return super.getText(element);
        }
    }
}
