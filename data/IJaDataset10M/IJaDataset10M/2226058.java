package org.spbu.pldoctoolkit.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.spbu.pldoctoolkit.actions.SelectIntoInfElementAction;
import org.spbu.pldoctoolkit.refactor.Util;

public class ReplaceWithInfElemRefDialog extends Dialog {

    private Text textFrom;

    private Text textTo;

    public ReplaceWithInfElemRefDialog(Shell parentShell) {
        super(parentShell);
        setBlockOnOpen(true);
    }

    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        GridLayout layout = new GridLayout(2, true);
        composite.setLayout(layout);
        textFrom = new Text(composite, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        GridData gd = new GridData();
        gd.horizontalAlignment = SWT.FILL;
        gd.verticalAlignment = SWT.FILL;
        gd.grabExcessHorizontalSpace = true;
        gd.grabExcessVerticalSpace = true;
        textFrom.setLayoutData(gd);
        textTo = new Text(composite, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        gd = new GridData();
        gd.horizontalAlignment = SWT.FILL;
        gd.verticalAlignment = SWT.FILL;
        gd.grabExcessHorizontalSpace = true;
        gd.grabExcessVerticalSpace = true;
        textTo.setLayoutData(gd);
        Button button = new Button(composite, SWT.PUSH);
        button.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                textTo.setText((Util.getTextWithOutSpaces((textFrom.getText()))).fst);
            }
        });
        return composite;
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        Point size = new Point(420, 210);
        newShell.setSize(size);
        Shell p = getParentShell();
        newShell.setLocation(p.getLocation().x + p.getSize().x / 2 - size.x / 2, p.getLocation().y + p.getSize().y / 2 - size.y / 2);
        newShell.setText(SelectIntoInfElementAction.refactName);
    }
}
