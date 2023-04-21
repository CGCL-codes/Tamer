package filter;

import gui.BringerOfPerdition;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;

/**
 * @author Roy
 * @author Roman Rudenko
 */
public abstract class Favorites extends Observable implements Observer {

    protected Composite self;

    protected ArrayList filters;

    protected ListViewer listViewer;

    protected String settingsFilename;

    public Composite getSelf() {
        return self;
    }

    public Favorites(Composite parent, String name) {
        BringerOfPerdition.getInstance().addObserver(this);
        filters = new ArrayList();
        self = new Composite(parent, SWT.NONE);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 4;
        gridLayout.marginHeight = 0;
        gridLayout.marginWidth = 0;
        self.setLayout(gridLayout);
        Label label = new Label(self, SWT.LEFT);
        label.setText(name);
        GridData gridData = new GridData();
        gridData.horizontalSpan = 4;
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        label.setLayoutData(gridData);
        listViewer = new ListViewer(self, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.FILL_VERTICAL);
        gridData.horizontalSpan = 4;
        listViewer.getList().setLayoutData(gridData);
        Button add = new Button(self, SWT.NONE);
        add.setText("New...");
        add.setLayoutData(new GridData(GridData.CENTER));
        Button modify = new Button(self, SWT.NONE);
        modify.setText("Modify...");
        modify.setLayoutData(new GridData(GridData.CENTER));
        Button rename = new Button(self, SWT.NONE);
        rename.setText("Rename...");
        rename.setLayoutData(new GridData(GridData.CENTER));
        Button delete = new Button(self, SWT.NONE);
        delete.setText("Delete");
        delete.setLayoutData(new GridData(GridData.CENTER));
        add.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                add();
            }
        });
        delete.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                int index = listViewer.getList().getSelectionIndex();
                if (index < 0) {
                    return;
                } else {
                    Filter temp = (Filter) listViewer.getElementAt(index);
                    delete(temp);
                }
            }
        });
        modify.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                int index = listViewer.getList().getSelectionIndex();
                if (index < 0) {
                    return;
                } else {
                    Filter temp = (Filter) listViewer.getElementAt(index);
                    temp.copyFrom(modify(temp));
                }
            }
        });
        rename.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                int index = listViewer.getList().getSelectionIndex();
                if (index < 0) {
                    return;
                } else {
                    Filter temp = (Filter) listViewer.getElementAt(index);
                    rename(temp);
                }
            }
        });
    }

    public void add() {
        InputDialog addDialog = new InputDialog(self.getShell(), "Add Filter", "Enter new name", "", null);
        addDialog.setBlockOnOpen(true);
        addDialog.open();
        if (addDialog.getReturnCode() == Window.OK) {
            Filter newFilter = new Filter(addDialog.getValue());
            filters.add(newFilter);
            listViewer.add(newFilter);
        }
    }

    public Filter modify(Filter filter) {
        return filter;
    }

    public void delete(Filter filter) {
        if (filter.isDefault()) {
            MessageDialog.openError(self.getShell(), "Error", "Cannot delete the default filter.");
            return;
        }
        MessageDialog confirmDialog = new MessageDialog(self.getShell(), "Delete filter?", null, "Are you sure you want to delete this filter?", MessageDialog.QUESTION, new String[] { "OK", "Cancel" }, 1);
        confirmDialog.setBlockOnOpen(true);
        confirmDialog.open();
        if (confirmDialog.getReturnCode() == Window.OK) {
            listViewer.remove(filter);
            filters.remove(filter);
        }
    }

    public void rename(Filter filter) {
        InputDialog renameDialog = new InputDialog(self.getShell(), "Rename Filter", "Enter new name", filter.getName(), null);
        renameDialog.setBlockOnOpen(true);
        renameDialog.open();
        if (renameDialog.getReturnCode() == Window.OK) {
            filter.setName(renameDialog.getValue());
            listViewer.update(filter, null);
        }
    }

    public void update(Observable source, Object message) {
        if (source == BringerOfPerdition.getInstance()) {
            FileOutputStream fileOutput = null;
            ObjectOutput objectOutput = null;
            try {
                fileOutput = new FileOutputStream(settingsFilename);
                objectOutput = new ObjectOutputStream(fileOutput);
                objectOutput.writeObject(filters);
                fileOutput.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadFilters() {
        FileInputStream fileInput = null;
        ObjectInput objectInput = null;
        try {
            fileInput = new FileInputStream(settingsFilename);
            objectInput = new ObjectInputStream(fileInput);
            Object obj = objectInput.readObject();
            if (obj instanceof ArrayList) {
                filters = (ArrayList) obj;
                listViewer.add(filters.toArray());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
