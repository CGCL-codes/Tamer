package net.sf.jmoney.stocks.model;

import java.text.MessageFormat;
import net.sf.jmoney.model2.ExtendableObject;
import net.sf.jmoney.model2.IPropertyControl;
import net.sf.jmoney.model2.ScalarPropertyAccessor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * Editor class for a table of progressive rates.  For example,
 * the commission rates or the stamp duty chargable on stock
 * purchase and sales.  (Actually, commission rates are regressive,
 * and go down in percentage terms as the amount goes up.  This
 * editor allows rates to be entered that go up or down as the
 * amount goes up.
 * 
 * @author  Nigel Westbury
 */
public class RatesEditor implements IPropertyControl<ExtendableObject> {

    private StockAccount account = null;

    private ScalarPropertyAccessor<RatesTable> ratesPropertyAccessor;

    private Button propertyControl;

    public RatesEditor(Composite parent, ScalarPropertyAccessor<RatesTable> propertyAccessor) {
        this.ratesPropertyAccessor = propertyAccessor;
        propertyControl = new Button(parent, SWT.PUSH);
        propertyControl.setText("Setup...");
        propertyControl.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                Object[] messageArgs = new Object[] { "commission/tax", account.getName() };
                String title = new MessageFormat("Setup {0} rates for {1}", java.util.Locale.US).format(messageArgs);
                RatesDialog dialog = new RatesDialog(propertyControl.getShell(), title, account.getPropertyValue(ratesPropertyAccessor), account.getCurrency());
                if (dialog.open() == Window.OK) {
                    account.setPropertyValue(ratesPropertyAccessor, dialog.getRates());
                }
            }
        });
    }

    /**
     * Load the control with the value from the given account.
     */
    public void load(ExtendableObject object) {
        account = (StockAccount) object;
        propertyControl.setEnabled(true);
    }

    /**
     * Save the value from the control back into the account object.
     *
     * Editors may update the property on a regular basis, not just when
     * the framework calls the <code>save</code> method.  However, the only time
     * that editors must update the property is when the framework calls this method.
     *
     * The framework should never call this method when no account is selected
     * so we can assume that <code>account</code> is not null.
     */
    public void save() {
    }

    /**
	 * @return The underlying SWT widget.
	 */
    public Control getControl() {
        return propertyControl;
    }
}
