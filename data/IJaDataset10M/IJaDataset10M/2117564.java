package org.gvt.inspector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.jface.resource.ImageDescriptor;
import org.gvt.ChisioMain;
import org.gvt.util.CommonStreamOptionsPack;

/**
 * This class maintains Common Stream Query Dialog for PopupMenu
 *
 * @author Shatlyk Ashyralyev
 *
 * Copyright: I-Vis Research Group, Bilkent University, 2007
 */
public class CommonStreamQueryParamDialog extends AbstractQueryParamWithStreamDialog {

    /**
	 * Create the dialog
	 */
    public CommonStreamQueryParamDialog(ChisioMain main) {
        super(main);
    }

    /**
	 * Open the dialog
	 */
    public CommonStreamOptionsPack open(CommonStreamOptionsPack opt) {
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
	 * Create contents of the dialog
	 * Buttons, Text Field, Radio Buttons, etc
	 */
    protected void createContents(final CommonStreamOptionsPack opt) {
        super.createContents(opt);
        shell.setText("Common Stream Query Properties");
        ImageDescriptor id = ImageDescriptor.createFromFile(NeighborhoodQueryParamWithEntitiesDialog.class, "/org/gvt/icon/cbe-icon.png");
        shell.setImage(id.createImage());
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 4;
        shell.setLayout(gridLayout);
        GridData gridData;
        createResultViewGroup(2, 2);
        createStreamDirectionGroup(2, 2, false);
        createLengthLimit(1, 1, 3, 1, 50);
        executeButton = new Button(shell, SWT.NONE);
        executeButton.setText("Execute");
        gridData = new GridData(GridData.END, GridData.CENTER, true, false);
        executeButton.setLayoutData(gridData);
        executeButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent arg0) {
                storeValuesToOptionsPack(opt);
                opt.setCancel(false);
                shell.close();
            }
        });
        gridData = new GridData(GridData.CENTER, GridData.CENTER, true, false);
        createCancelButton(gridData);
        defaultButton = new Button(shell, SWT.NONE);
        defaultButton.setText("Default");
        gridData = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
        gridData.horizontalSpan = 2;
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
	 * After clicking OK button,
	 * all data in dialog is saved to CommonStreamOptionsPack
	 */
    public void storeValuesToOptionsPack(CommonStreamOptionsPack opt) {
        opt.setLengthLimit(Integer.parseInt(lengthLimit.getText()));
        if (downstreamButton.getSelection()) {
            opt.setDownstream(true);
        } else {
            opt.setDownstream(false);
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
    public void setInitialValues(CommonStreamOptionsPack opt) {
        if (opt.isCurrentView()) {
            currentViewButton.setSelection(true);
        } else {
            newViewButton.setSelection(true);
        }
        if (opt.isDownstream()) {
            downstreamButton.setSelection(true);
        } else {
            upstreamButton.setSelection(true);
        }
        lengthLimit.setText(String.valueOf(opt.getLengthLimit()));
    }

    /**
	 * Set default values into dialog
	 */
    public void setDefaultQueryDialogOptions() {
        super.setDefaultQueryDialogOptions();
        downstreamButton.setSelection(DOWNSTREAM);
        upstreamButton.setSelection(!DOWNSTREAM);
    }
}
