package com.jiexplorer.rcp.ui.category;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class CategoryDialog extends org.eclipse.swt.widgets.Dialog {

    private Shell dialogShell;

    private Tree tree1;

    private Button appendCBox;

    private Button saveButton;

    private Button closeButton;

    private Composite composite1;

    /**
	 * Auto-generated main method to display this org.eclipse.swt.widgets.Dialog
	 * inside a new Shell.
	 */
    public static void main(final String[] args) {
        try {
            final Display display = Display.getDefault();
            final Shell shell = new Shell(display);
            final CategoryDialog inst = new CategoryDialog(shell, SWT.NULL);
            inst.open();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public CategoryDialog(final Shell parent, final int style) {
        super(parent, style);
    }

    public void open() {
        try {
            final Shell parent = getParent();
            dialogShell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
            dialogShell.setLayout(new GridLayout());
            {
                final GridData tree1LData = new GridData();
                tree1LData.horizontalAlignment = GridData.FILL;
                tree1LData.grabExcessHorizontalSpace = true;
                tree1 = new Tree(dialogShell, SWT.NONE);
                tree1.setLayoutData(tree1LData);
            }
            {
                composite1 = new Composite(dialogShell, SWT.NONE);
                final RowLayout composite1Layout = new RowLayout(org.eclipse.swt.SWT.HORIZONTAL);
                composite1Layout.fill = true;
                composite1Layout.pack = false;
                composite1Layout.wrap = false;
                composite1Layout.marginRight = 5;
                composite1Layout.marginLeft = 5;
                composite1Layout.spacing = 8;
                final GridData composite1LData = new GridData();
                composite1LData.horizontalAlignment = GridData.END;
                composite1LData.grabExcessHorizontalSpace = true;
                composite1.setLayoutData(composite1LData);
                composite1.setLayout(composite1Layout);
                {
                    closeButton = new Button(composite1, SWT.PUSH | SWT.CENTER);
                    closeButton.setText("Close");
                }
                {
                    saveButton = new Button(composite1, SWT.PUSH | SWT.CENTER);
                    saveButton.setText("Save");
                }
                {
                    appendCBox = new Button(composite1, SWT.CHECK | SWT.LEFT);
                    appendCBox.setText("Append");
                }
            }
            dialogShell.layout();
            dialogShell.pack();
            dialogShell.setSize(250, 166);
            dialogShell.setText("Categories");
            dialogShell.open();
            final Display display = dialogShell.getDisplay();
            while (!dialogShell.isDisposed()) {
                if (!display.readAndDispatch()) {
                    display.sleep();
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
