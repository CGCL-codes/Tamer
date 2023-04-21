package com.sebulli.fakturama.export.accounts;

import static com.sebulli.fakturama.Translate._;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import com.sebulli.fakturama.data.Data;
import com.sebulli.fakturama.data.UniData;
import com.sebulli.fakturama.data.UniDataType;
import com.sebulli.fakturama.editors.CurrencyText;
import com.sebulli.fakturama.export.ExportWizardPageStartEndDate;
import com.sebulli.fakturama.misc.DataUtils;

/**
 * Create the 3rd page of the account export wizard. This page is
 * used to select start value and date of the selected account.
 * 
 * @author Gerd Bartelt
 */
public class AccountSettingsPage extends WizardPage {

    private DateTime dtDate;

    private CurrencyText txtValue;

    private UniData value = new UniData(UniDataType.PRICE, 0.0);

    private Label warning;

    /**
	 * Constructor Create the page and set title and message.
	 */
    public AccountSettingsPage(String title, String label) {
        super("ExportOptionPage");
        setTitle(title);
        setMessage(label);
    }

    /**
	 * Creates the top level control for this dialog page under the given parent
	 * composite.
	 * 
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
    @Override
    public void createControl(Composite parent) {
        Composite top = new Composite(parent, SWT.NONE);
        GridLayoutFactory.swtDefaults().numColumns(1).applyTo(top);
        GridDataFactory.swtDefaults().align(SWT.BEGINNING, SWT.CENTER).applyTo(top);
        setControl(top);
        Label labelDescription = new Label(top, SWT.NONE);
        labelDescription.setText(_("Set the account start date and value") + ":");
        GridDataFactory.swtDefaults().align(SWT.BEGINNING, SWT.CENTER).indent(0, 10).applyTo(labelDescription);
        Composite dateAndValue = new Composite(top, SWT.NONE);
        GridLayoutFactory.swtDefaults().numColumns(2).applyTo(dateAndValue);
        GridDataFactory.swtDefaults().align(SWT.BEGINNING, SWT.CENTER).applyTo(dateAndValue);
        dtDate = new DateTime(dateAndValue, SWT.DROP_DOWN);
        GridDataFactory.swtDefaults().align(SWT.BEGINNING, SWT.CENTER).applyTo(dtDate);
        dtDate.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                setPageComplete(isPageComplete());
            }
        });
        txtValue = new CurrencyText(null, dateAndValue, SWT.BORDER | SWT.RIGHT, value);
        txtValue.setToolTipText(_("Start value of this account"));
        GridDataFactory.swtDefaults().hint(100, SWT.DEFAULT).applyTo(txtValue.getText());
        warning = new Label(top, SWT.NONE);
        warning.setText(_("The date must not be after the start date!"));
        GridDataFactory.swtDefaults().align(SWT.BEGINNING, SWT.CENTER).applyTo(warning);
        Color red = new Color(null, 255, 0, 0);
        warning.setForeground(red);
        red.dispose();
        isPageComplete();
    }

    /**
	 * Returns the date as a GregorianCalendar object
	 * 
	 * @return date as a GregorianCalendar object
	 */
    public GregorianCalendar getDate() {
        return new GregorianCalendar(dtDate.getYear(), dtDate.getMonth(), dtDate.getDay());
    }

    /**
	 * Returns the value as unidata
	 * 
	 * @return
	 * 		The value as unidata
	 */
    public UniData getValue() {
        return value;
    }

    /**
	 * Sets the start date and value. Use the stored values from the data base
	 */
    public void setAccountStartValues(String account) {
        String datePropertyKey = "export_account_date_" + account.toLowerCase();
        String date = Data.INSTANCE.getProperty(datePropertyKey, "2000-01-01");
        GregorianCalendar calendar = new GregorianCalendar();
        calendar = DataUtils.getCalendarFromDateString(date);
        if (dtDate != null) {
            dtDate.setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        }
        String valuePropertyKey = "export_account_value_" + account.toLowerCase();
        String valueString = Data.INSTANCE.getProperty(valuePropertyKey, "0.0");
        if (value != null) {
            value.setValue(valueString);
            txtValue.update();
        }
    }

    /**
	 * Return the  date as a GregorianCalendar object
	 * 
	 * @return date as a GregorianCalendar object
	 */
    public GregorianCalendar getDateAsCalendar() {
        return new GregorianCalendar(dtDate.getYear(), dtDate.getMonth(), dtDate.getDay());
    }

    /**
	 * Test, whether the page is complete
	 */
    @Override
    public boolean isPageComplete() {
        if (!this.getPreviousPage().canFlipToNextPage()) return false;
        ExportWizardPageStartEndDate startPage = (ExportWizardPageStartEndDate) (this.getPreviousPage().getPreviousPage());
        if (startPage.getDoNotUseTimePeriod()) return true;
        if (dtDate == null) return false;
        boolean isAfterStartDate = getDateAsCalendar().after(startPage.getStartDate());
        if (warning != null) warning.setVisible(isAfterStartDate);
        return !isAfterStartDate;
    }
}
