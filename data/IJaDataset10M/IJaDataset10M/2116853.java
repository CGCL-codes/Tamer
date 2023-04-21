package hauraro.kunkunshi.gui.dialogboxes;

import hauraro.kunkunshi.core.*;
import hauraro.kunkunshi.gui.listeners.*;
import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class RenameKunkunshiBox {

    private Text text;

    public RenameKunkunshiBox(Shell shell, MessageRouter router, String oldName) {
        Shell newShell = new Shell(shell);
        newShell.setText("Rename the song:");
        newShell.setLayout(new GridLayout());
        GridData gridData = new GridData(SWT.FILL, SWT.NONE, true, true);
        gridData.minimumWidth = 200;
        gridData.widthHint = 400;
        text = new Text(newShell, SWT.BORDER);
        text.setLayoutData(gridData);
        text.setText(oldName);
        text.setSelection(0, text.getText().length());
        RenameKunkunshiBoxListener listener = new RenameKunkunshiBoxListener(router, newShell, text, oldName);
        text.addKeyListener(listener);
        Button button = new Button(newShell, SWT.NONE);
        button.setText("OK");
        button.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, true));
        button.addSelectionListener(listener);
        newShell.pack();
        newShell.open();
    }
}
