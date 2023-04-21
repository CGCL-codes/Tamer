package org.gvt.gui;

import java.util.ArrayList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.gvt.ChisioMain;
import org.gvt.util.NeighborhoodOptionsPack;
import org.gvt.util.EntityHolder;

/**
 * This class maintains Neighborhood Query Dialog for TopMenuBar
 *
 * @author Ozgun Babur
 * @author Merve Cakir
 * @author Shatlyk Ashyralyev
 *
 * Copyright: I-Vis Research Group, Bilkent University, 2007
 */
public class NeighborhoodQueryParamWithEntitiesDialog extends AbstractQueryParamWithStreamDialog {

    /**
	 * All entities of graph
	 */
    ArrayList<EntityHolder> allEntities;

    /**
	 * Entities which are added
	 */
    ArrayList<EntityHolder> addedEntities;

    /**
	 * Getter
	 */
    public ArrayList<EntityHolder> getAddedEntities() {
        return this.addedEntities;
    }

    /**
	 * Create the dialog
	 */
    public NeighborhoodQueryParamWithEntitiesDialog(ChisioMain main) {
        super(main);
        this.allEntities = main.getAllEntities();
        this.addedEntities = new ArrayList<EntityHolder>();
    }

    /**
	 * Open the dialog
	 */
    public NeighborhoodOptionsPack open(NeighborhoodOptionsPack opt) {
        createContents(opt);
        shell.setLocation(getParent().getLocation().x + (getParent().getSize().x / 2) - (shell.getSize().x / 2), getParent().getLocation().y + (getParent().getSize().y / 2) - (shell.getSize().y / 2));
        shell.open();
        shell.layout();
        Display display = getParent().getDisplay();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        return opt;
    }

    /**
	 * Create contents of the dialog.
	 * Buttons, List, Text Field, Radio Buttons, etc
	 */
    protected void createContents(final NeighborhoodOptionsPack opt) {
        super.createContents(opt);
        shell.setText("Neighborhood Query Properties");
        ImageDescriptor id = ImageDescriptor.createFromFile(NeighborhoodQueryParamWithEntitiesDialog.class, "/org/gvt/icon/cbe-icon.png");
        shell.setImage(id.createImage());
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 6;
        shell.setLayout(gridLayout);
        GridData gridData;
        createList(2, 2, 300, 5);
        createResultViewGroup(2, 2);
        createStreamDirectionGroup(2, 3, true);
        addButton = new Button(shell, SWT.NONE);
        addButton.setText("Add...");
        gridData = new GridData(GridData.END, GridData.BEGINNING, true, false);
        gridData.minimumWidth = 100;
        gridData.horizontalIndent = 5;
        addButton.setLayoutData(gridData);
        addButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent arg0) {
                AddEntityDialog addEntityDialog = new AddEntityDialog(new Shell(), allEntities);
                boolean addPressed = addEntityDialog.open();
                if (addPressed) {
                    for (EntityHolder entity : addEntityDialog.getSelectedEntities()) {
                        if (!previouslyAdded(entity)) {
                            entityList.add(entity.getName());
                            addedEntities.add(entity);
                        }
                    }
                }
            }
        });
        removeButton = new Button(shell, SWT.NONE);
        removeButton.setText("Remove");
        gridData = new GridData(GridData.BEGINNING, GridData.BEGINNING, true, false);
        gridData.horizontalIndent = 5;
        gridData.minimumWidth = 100;
        removeButton.setLayoutData(gridData);
        removeButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent arg0) {
                String[] selectionResult = entityList.getSelection();
                for (String selected : selectionResult) {
                    for (int j = 0; j < addedEntities.size(); j++) {
                        EntityHolder entity = addedEntities.get(j);
                        if (selected != null && selected.equals(entity.getName())) {
                            addedEntities.remove(j);
                            entityList.remove(selected);
                        }
                    }
                }
            }
        });
        createLengthLimit(1, 1, 1, 1, 50);
        exeCancelDefaultGroup = new Group(shell, SWT.NONE);
        gridData = new GridData(GridData.FILL, GridData.CENTER, false, false);
        gridData.horizontalSpan = 6;
        exeCancelDefaultGroup.setLayoutData(gridData);
        exeCancelDefaultGroup.setLayout(new GridLayout(3, true));
        executeButton = new Button(exeCancelDefaultGroup, SWT.NONE);
        executeButton.setText("Execute");
        gridData = new GridData(GridData.END, GridData.CENTER, true, false);
        executeButton.setLayoutData(gridData);
        executeButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent arg0) {
                if (getAddedEntities().isEmpty()) {
                    MessageDialog.openError(main.getShell(), "Error!", "Add Entity!");
                    return;
                }
                storeValuesToOptionsPack(opt);
                opt.setCancel(false);
                shell.close();
            }
        });
        cancelButton = new Button(exeCancelDefaultGroup, SWT.NONE);
        gridData = new GridData(GridData.CENTER, GridData.CENTER, true, false);
        createCancelButton(gridData);
        defaultButton = new Button(exeCancelDefaultGroup, SWT.NONE);
        defaultButton.setText("Default");
        gridData = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
        defaultButton.setLayoutData(gridData);
        defaultButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent arg0) {
                setDefaultQueryDialogOptions();
            }
        });
        shell.pack();
        setInitialValues(opt);
    }

    /**
	 * This method checks whether physicalEntity is added before.
	 */
    private boolean previouslyAdded(EntityHolder pe) {
        for (EntityHolder addedBefore : this.addedEntities) {
            if (pe == addedBefore) {
                return true;
            }
        }
        return false;
    }

    /**
	 * After clicking OK button,
	 * all data in dialog is saved to NeighborhoodOptionsPack
	 */
    public void storeValuesToOptionsPack(NeighborhoodOptionsPack opt) {
        opt.setLengthLimit(Integer.parseInt(lengthLimit.getText()));
        if (downstreamButton.getSelection()) {
            opt.setDownstream(true);
            opt.setUpstream(false);
        } else if (upstreamButton.getSelection()) {
            opt.setDownstream(false);
            opt.setUpstream(true);
        } else {
            opt.setDownstream(true);
            opt.setUpstream(true);
        }
        if (currentViewButton.getSelection()) {
            opt.setCurrentView(true);
        } else {
            opt.setCurrentView(false);
        }
    }

    /**
	 * After creating the dialog box, 
	 * fields are completed with data in opt OptionsPack
	 */
    public void setInitialValues(NeighborhoodOptionsPack opt) {
        super.setInitialValues(opt);
        if (opt.isDownstream() && opt.isUpstream()) {
            bothBotton.setSelection(true);
        } else if (opt.isDownstream()) {
            downstreamButton.setSelection(true);
        } else if (opt.isUpstream()) {
            upstreamButton.setSelection(true);
        }
    }

    /**
	 * Set default values into dialog
	 */
    public void setDefaultQueryDialogOptions() {
        super.setDefaultQueryDialogOptions();
        bothBotton.setSelection(DOWNSTREAM && UPSTREAM);
        downstreamButton.setSelection(DOWNSTREAM && !UPSTREAM);
        upstreamButton.setSelection(!DOWNSTREAM && UPSTREAM);
    }
}
