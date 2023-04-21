package alice.tuprologx.eclipse.properties;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;

public class LoadEngine extends MessageDialog {

    private Text name;

    private Button theories;

    private String[] list;

    private Label l;

    private static String ret = "";

    private static boolean all = false;

    public LoadEngine(Shell parentShell, String dialogTitle, IProject project, String[] list) {
        super(parentShell, dialogTitle, null, null, NONE, new String[] { IDialogConstants.YES_LABEL, IDialogConstants.NO_LABEL }, 0);
        this.list = list;
    }

    public static String show(Shell parent, String title, IProject project, String[] list) {
        ret = "";
        all = false;
        LoadEngine q = new LoadEngine(parent, title, project, list);
        if (q.open() == 0) {
            PropertyManager.addEngineInProperty(project, ret);
            if (!ret.equals("")) {
                if (all) PropertyManager.setTheoriesInProperty(project, ret, null, true); else PropertyManager.setTheoriesInProperty(project, ret, null, false);
                return ret;
            }
            return null;
        }
        return null;
    }

    protected Control createMessageArea(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        container.setLayout(new RowLayout(SWT.VERTICAL));
        final Group engineName = new Group(container, SWT.NONE);
        final RowData rowData2 = new RowData();
        rowData2.width = 412;
        engineName.setLayoutData(rowData2);
        engineName.setText("Engine Name");
        engineName.setLayout(new RowLayout());
        name = new Text(engineName, SWT.BORDER);
        final RowData rowData = new RowData();
        rowData.width = 391;
        name.setLayoutData(rowData);
        name.setToolTipText("Insert the name of the engine to create");
        name.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                dialogChanged();
            }
        });
        name.addFocusListener(new FocusListener() {

            public void focusGained(FocusEvent e) {
                Button yes = getButton(0);
                if (name.getText().equals("")) yes.setEnabled(false); else yes.setEnabled(false);
            }

            public void focusLost(FocusEvent e) {
            }
        });
        final Group scopeGroup = new Group(container, SWT.NONE);
        scopeGroup.setText("Default scope");
        final RowData rowData8 = new RowData();
        rowData8.height = 30;
        rowData8.width = 412;
        scopeGroup.setLayoutData(rowData8);
        scopeGroup.setLayout(new RowLayout(SWT.VERTICAL));
        final Composite composite = new Composite(scopeGroup, SWT.NONE);
        composite.setLayout(new GridLayout());
        final RowData rowData3 = new RowData();
        rowData3.width = 410;
        rowData3.height = 20;
        composite.setLayoutData(rowData3);
        l = new Label(container, SWT.NONE);
        final RowData rowData4 = new RowData();
        rowData4.width = 410;
        l.setLayoutData(rowData4);
        l.setText("");
        return super.createMessageArea(composite);
    }

    private void dialogChanged() {
        Button yes = getButton(0);
        l.setText("");
        yes.setEnabled(true);
        ret = name.getText();
        if (name.getText().equals("")) {
            yes.setEnabled(false);
        }
        if (name.getText().indexOf(";") >= 0) {
            yes.setEnabled(false);
            l.setText("Engine name cannot contain the character ';'");
        }
        for (int i = 0; i < list.length; i++) if (list[i].equals(name.getText())) {
            yes.setEnabled(false);
            l.setText("Engine name already used");
        }
    }
}
