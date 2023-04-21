package org.mxeclipse.dialogs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import matrix.db.BusinessType;
import matrix.db.BusinessTypeItr;
import matrix.db.BusinessTypeList;
import matrix.db.Context;
import matrix.db.RelationshipType;
import matrix.db.RelationshipTypeItr;
import matrix.db.RelationshipTypeList;
import matrix.util.MatrixException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.mxeclipse.MxEclipsePlugin;
import org.mxeclipse.configure.table.MxTableColumnList;
import org.mxeclipse.model.MxTreeDomainObject;
import org.mxeclipse.utils.MxEclipseConstants;
import org.mxeclipse.utils.MxEclipseUtils;
import org.mxeclipse.views.MxEclipseObjectView;

/**
 * <p>Title: ConnectExistingDialog</p>
 * <p>Description: </p>
 * <p>Company: ABB Switzerland</p>
 * @author Tihomir Ilic
 * @version 1.0
 *
 */
public class CreateNewDialog extends Dialog {

    CreateNewComposite inner;

    public static String DIRECTION_FROM = "" + MxEclipseConstants.RIGHT_ARROW;

    public static String DIRECTION_TO = "" + MxEclipseConstants.LEFT_ARROW;

    /**
	 *
	 */
    public CreateNewDialog(Shell parent) {
        super(parent);
        setShellStyle(getShellStyle() | SWT.RESIZE);
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Display display = Display.getDefault();
        CreateNewDialog thisClass = new CreateNewDialog(null);
        thisClass.createDialogArea(null);
        thisClass.open();
        while (!thisClass.getShell().isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();
    }

    @Override
    protected void cancelPressed() {
        super.cancelPressed();
    }

    @Override
    protected void okPressed() {
        if (inner.okPressed()) {
            super.okPressed();
        }
    }

    public MxTreeDomainObject getNewObject() {
        return inner.getNewObject();
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite comp = (Composite) super.createDialogArea(parent);
        GridLayout layout = (GridLayout) comp.getLayout();
        layout.numColumns = 1;
        Composite comRel = new Composite(comp, SWT.NONE);
        GridLayout layRel = new GridLayout();
        layRel.numColumns = 3;
        comRel.setLayout(layRel);
        GridData gridData = new GridData();
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        inner = new CreateNewComposite(comp, SWT.NONE);
        inner.setLayoutData(gridData);
        return comp;
    }

    /**
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("Create New Object");
        newShell.setMinimumSize(400, 270);
    }
}
