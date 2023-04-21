package net.future118.smallbusiness.rcp.dialog;

import net.future118.smallbusiness.model.product.ProductGroup;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ProductGroupDialog extends Dialog {

    private ProductGroup productGroup;

    private Text textName;

    public ProductGroup getProductGroup() {
        return productGroup;
    }

    public ProductGroupDialog(Shell parentShell) {
        super(parentShell);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        gridLayout.makeColumnsEqualWidth = false;
        GridData gridDataLName = new GridData(GridData.FILL_HORIZONTAL);
        gridDataLName.grabExcessHorizontalSpace = false;
        gridDataLName.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
        gridDataLName.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridDataLName.widthHint = 100;
        GridData gridDataTName = new org.eclipse.swt.layout.GridData(GridData.FILL_HORIZONTAL);
        gridDataTName.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL_HORIZONTAL;
        gridDataTName.grabExcessHorizontalSpace = true;
        gridDataTName.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        gridDataTName.widthHint = 200;
        Composite top = new Composite(parent, SWT.NULL);
        top.setLayout(gridLayout);
        Label labelName = new Label(top, SWT.NONE);
        labelName.setLayoutData(gridDataLName);
        labelName.setText("nazwa:");
        textName = new Text(top, SWT.BORDER);
        textName.setLayoutData(gridDataTName);
        return top;
    }

    @Override
    protected void okPressed() {
        productGroup = new ProductGroup();
        productGroup.setName(textName.getText());
        super.okPressed();
    }
}
