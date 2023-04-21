package com.sebulli.fakturama.views.datasettable;

import static com.sebulli.fakturama.Translate._;
import org.eclipse.swt.widgets.Composite;
import com.sebulli.fakturama.ContextHelpConstants;
import com.sebulli.fakturama.actions.NewExpenditureVoucherAction;
import com.sebulli.fakturama.data.Data;
import com.sebulli.fakturama.data.DataSetExpenditureVoucher;

/**
 * View with the table of all expenditures
 * 
 * @author Gerd Bartelt
 * 
 */
public class ViewExpenditureVoucherTable extends ViewVoucherTable {

    public static final String ID = "com.sebulli.fakturama.views.datasettable.viewExpenditureVoucherTable";

    /**
	 * Creates the SWT controls for this workbench part.
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
    @Override
    public void createPartControl(Composite parent) {
        addNewAction = new NewExpenditureVoucherAction();
        editor = "ExpenditureVoucher";
        customerSupplier = DataSetExpenditureVoucher.CUSTOMERSUPPLIER;
        super.createPartControl(parent, ContextHelpConstants.VOUCHER_TABLE_VIEW);
        this.setPartName(_("Expenditure vouchers"));
        tableViewer.setInput(Data.INSTANCE.getExpenditureVouchers());
        topicTreeViewer.setInput(Data.INSTANCE.getExpenditureVouchers());
    }
}
