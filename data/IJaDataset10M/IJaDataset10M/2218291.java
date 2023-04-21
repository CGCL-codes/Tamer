package de.beas.explicanto.client.rcp.workspace.dialogs;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import de.bea.services.vidya.client.datasource.VidyaDataTree;
import de.bea.services.vidya.client.datasource.types.WSTaskAudience;
import de.bea.services.vidya.client.datasource.types.WSUser;
import de.beas.explicanto.client.I18N;
import de.beas.explicanto.client.rcp.dialogs.BaseDialog;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Combo;

public class TaskAudienceDialog extends Dialog {

    private Button okButton = null;

    private List audienceList = null;

    private Button addButton = null;

    private Button removeButton = null;

    private Combo userCombo = null;

    private java.util.List audience = new LinkedList();

    private WSUser[] wsUserList;

    private Composite composite;

    public TaskAudienceDialog(Shell parent) {
        super(parent);
    }

    public String getTitleId() {
        return I18N.translate("task.dialog.audience.title");
    }

    /**
     * This method initializes shell	
     *
     */
    protected Control createDialogArea(Composite parent) {
        setBlockOnOpen(true);
        Composite base = (Composite) super.createDialogArea(parent);
        composite = new Composite(base, SWT.NONE);
        composite.setSize(new org.eclipse.swt.graphics.Point(360, 287));
        composite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        getShell().setText(getTitleId());
        audienceList = new List(composite, SWT.BORDER);
        audienceList.setBounds(new org.eclipse.swt.graphics.Rectangle(13, 46, 324, 166));
        Iterator it = audience.iterator();
        while (it.hasNext()) {
            WSTaskAudience user = (WSTaskAudience) it.next();
            audienceList.add(user.getUser().getUsername());
        }
        addButton = new Button(composite, SWT.NONE);
        addButton.setBounds(new org.eclipse.swt.graphics.Rectangle(13, 8, 66, 23));
        addButton.setText(I18N.translate("task.dialog.audience.button.add"));
        addButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                addAudience();
            }
        });
        removeButton = new Button(composite, SWT.NONE);
        removeButton.setBounds(new org.eclipse.swt.graphics.Rectangle(252, 8, 85, 23));
        removeButton.setText(I18N.translate("task.dialog.audience.button.remove"));
        removeButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                removeAudience();
            }
        });
        createUserCombo();
        return composite;
    }

    /**
     * This method initializes userCombo	
     *
     */
    private void createUserCombo() {
        userCombo = new Combo(composite, SWT.READ_ONLY);
        userCombo.setBounds(new org.eclipse.swt.graphics.Rectangle(99, 8, 141, 23));
        try {
            java.util.List statuses = VidyaDataTree.getDefault().loadLoginUserList();
            wsUserList = (WSUser[]) statuses.toArray(new WSUser[0]);
            for (int i = 0; i < wsUserList.length; i++) userCombo.add(wsUserList[i].getUsername());
            userCombo.select(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public java.util.List getAudience() {
        return Collections.unmodifiableList(audience);
    }

    public void setAudience(java.util.List newAudience) {
        audience.clear();
        Iterator it = newAudience.iterator();
        while (it.hasNext()) {
            WSTaskAudience user = (WSTaskAudience) it.next();
            audience.add(user);
        }
        if (audienceList != null) {
            audienceList.removeAll();
            it = newAudience.iterator();
            while (it.hasNext()) {
                WSTaskAudience user = (WSTaskAudience) it.next();
                audienceList.add(user.getUser().getUsername());
            }
        }
    }

    protected void addAudience() {
        int index = userCombo.getSelectionIndex();
        if (index == -1) return;
        if (Arrays.asList(audienceList.getItems()).contains(wsUserList[index].getUsername())) return;
        audience.add(new WSTaskAudience(0, null, null, wsUserList[index]));
        audienceList.add(wsUserList[index].getUsername());
    }

    protected void removeAudience() {
        int index = audienceList.getSelectionIndex();
        if (index == -1) return;
        audience.remove(index);
        audienceList.remove(index);
    }

    protected void createButtonsForButtonBar(Composite parent) {
        okButton = new Button(composite, SWT.NONE);
        okButton.setBounds(new org.eclipse.swt.graphics.Rectangle(258, 225, 79, 23));
        okButton.setText(I18N.translate("task.dialog.audience.button.close"));
        okButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                TaskAudienceDialog.this.okPressed();
            }
        });
    }
}
