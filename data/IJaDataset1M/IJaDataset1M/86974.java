package org.plazmaforge.bsolution.sale.client.swt.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.plazmaforge.bsolution.finance.client.swt.forms.common.XTaxCombo;
import org.plazmaforge.bsolution.goods.client.swt.forms.AbstractGoodsDocumentItemEditForm;
import org.plazmaforge.bsolution.product.client.swt.forms.common.XProductUnitCombo;
import org.plazmaforge.bsolution.product.common.beans.PriceType;
import org.plazmaforge.framework.client.PWT;
import org.plazmaforge.framework.client.swt.controls.XComboEdit;
import org.plazmaforge.framework.client.swt.controls.XCurrencyField;
import org.plazmaforge.framework.client.swt.controls.XTextField;

/** 
 * @author Oleh Hapon
 * $Id: SaleServItemEditForm.java,v 1.3 2010/04/28 06:31:06 ohapon Exp $
 */
public class SaleServItemEditForm extends AbstractGoodsDocumentItemEditForm {

    private Label goodsLabel;

    private Label productFeatureLabel;

    private Label unitLabel;

    private Label unitRateLabel;

    private Label quantityLabel;

    private Label currencyPriceLabel;

    private Label currencyAmountLabel;

    private Label taxLabel;

    private Label currencyTaxAmountLabel;

    private XComboEdit goodsField;

    private XTextField productFeatureField;

    private XProductUnitCombo unitField;

    private XCurrencyField unitRateField;

    private XCurrencyField quantityField;

    private XCurrencyField currencyPriceField;

    private XCurrencyField currencyAmountField;

    private XTaxCombo taxField;

    private XCurrencyField currencyTaxAmountField;

    public SaleServItemEditForm(Composite parent, int style) {
        super(parent, style);
        initialize();
    }

    private void initialize() {
        Label label;
        GridData gridData;
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 4;
        setLayout(gridLayout);
        goodsLabel = new Label(this, SWT.NONE);
        goodsLabel.setText(Messages.getString("SaleServItemEditForm.goodsLabel.text"));
        goodsField = new XComboEdit(this, SWT.BORDER, PWT.VIEW_BUTTON | PWT.EDIT_BUTTON | PWT.DELETE_BUTTON);
        gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
        gridData.widthHint = 300;
        goodsField.setLayoutData(gridData);
        productFeatureLabel = new Label(this, SWT.NONE);
        productFeatureLabel.setText(Messages.getString("SaleServItemEditForm.productFeatureLabel.text"));
        productFeatureField = new XTextField(this, SWT.BORDER);
        gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
        gridData.widthHint = 300;
        productFeatureField.setLayoutData(gridData);
        label = new Label(this, SWT.HORIZONTAL | SWT.SEPARATOR);
        label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));
        unitLabel = new Label(this, SWT.NONE);
        unitLabel.setText(Messages.getString("SaleServItemEditForm.unitLabel.text"));
        unitField = new XProductUnitCombo(this, SWT.BORDER);
        gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
        gridData.widthHint = 100;
        unitField.setLayoutData(gridData);
        unitRateLabel = new Label(this, SWT.NONE);
        gridData = new GridData();
        gridData.horizontalIndent = 40;
        unitRateLabel.setLayoutData(gridData);
        unitRateLabel.setText(Messages.getString("SaleServItemEditForm.unitRateLabel.text"));
        unitRateField = new XCurrencyField(this, SWT.BORDER);
        gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
        gridData.widthHint = 100;
        unitRateField.setLayoutData(gridData);
        quantityLabel = new Label(this, SWT.NONE);
        quantityLabel.setText(Messages.getString("SaleServItemEditForm.quantityLabel.text"));
        quantityField = new XCurrencyField(this, SWT.BORDER);
        gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
        gridData.widthHint = 100;
        quantityField.setLayoutData(gridData);
        currencyPriceLabel = new Label(this, SWT.NONE);
        gridData = new GridData();
        gridData.horizontalIndent = 40;
        currencyPriceLabel.setLayoutData(gridData);
        currencyPriceLabel.setText(Messages.getString("SaleServItemEditForm.currencyPriceLabel.text"));
        currencyPriceField = new XCurrencyField(this, SWT.BORDER);
        gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
        gridData.widthHint = 100;
        currencyPriceField.setLayoutData(gridData);
        label = new Label(this, SWT.NONE);
        label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
        currencyAmountLabel = new Label(this, SWT.NONE);
        gridData = new GridData();
        gridData.horizontalIndent = 40;
        currencyAmountLabel.setLayoutData(gridData);
        currencyAmountLabel.setText(Messages.getString("SaleServItemEditForm.currencyAmountLabel.text"));
        currencyAmountField = new XCurrencyField(this, SWT.BORDER);
        gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
        gridData.widthHint = 100;
        currencyAmountField.setLayoutData(gridData);
        label = new Label(this, SWT.HORIZONTAL | SWT.SEPARATOR);
        label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));
        taxLabel = new Label(this, SWT.NONE);
        taxLabel.setText(Messages.getString("SaleServItemEditForm.taxLabel.text"));
        taxField = new XTaxCombo(this, SWT.BORDER);
        gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
        gridData.widthHint = 100;
        taxField.setLayoutData(gridData);
        currencyTaxAmountLabel = new Label(this, SWT.NONE);
        gridData = new GridData();
        gridData.horizontalIndent = 40;
        currencyTaxAmountLabel.setLayoutData(gridData);
        currencyTaxAmountLabel.setText(Messages.getString("SaleServItemEditForm.currencyTaxAmountLabel.text"));
        currencyTaxAmountField = new XCurrencyField(this, SWT.BORDER);
        gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
        gridData.widthHint = 100;
        currencyTaxAmountField.setLayoutData(gridData);
        this.setSize(new Point(470, 108));
        initGoodsField(goodsField);
        initUnitField(unitField);
        initUnitRateField(unitRateField);
        initTaxField(taxField);
        initQuantityField(quantityField);
        initCurrencyPriceField(currencyPriceField);
        initCurrencyAmountField(currencyAmountField);
        initCurrencyTaxAmountField(currencyTaxAmountField);
        initNoEditableField(unitRateField);
    }

    protected void bindControls() {
        bindControl(goodsField, "product");
        bindControl(productFeatureField, "productFeatureName");
        bindControl(unitField, "unit", unitLabel, REQUIRED);
        bindControl(unitRateField, "unitRate");
        bindControl(quantityField, "quantity");
        bindControl(currencyPriceField, "enterCurrencyPrice");
        bindControl(currencyAmountField, "enterCurrencyAmount");
        bindControl(taxField, "taxId");
        bindControl(currencyTaxAmountField, "currencyTaxAmount");
    }

    protected int getDefaultPriceType() {
        return PriceType.LIST_PRICE;
    }
}
