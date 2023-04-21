package org.plazmaforge.bsolution.goods.client.swing.forms;

import javax.swing.*;
import org.plazmaforge.bsolution.inventory.common.beans.InventoryRest;
import org.plazmaforge.bsolution.inventory.common.beans.InventoryRestItem;
import org.plazmaforge.bsolution.product.client.swing.forms.common.ProductUnitComboBox;
import org.plazmaforge.bsolution.product.common.beans.Product;
import org.plazmaforge.framework.client.swing.controls.XComboEdit;
import org.plazmaforge.framework.client.swing.controls.XCurrencyField;
import org.plazmaforge.framework.client.swing.controls.XNumberField;
import org.plazmaforge.framework.client.swing.gui.GridBagPanel;
import org.plazmaforge.framework.core.exception.ApplicationException;
import java.awt.*;

/**
 * @author Oleh Hapon
 * Date: 29.05.2004
 * Time: 13:36:48
 * $Id: GoodsRestItemEdit.java,v 1.4 2010/12/05 07:56:43 ohapon Exp $
 */
public class GoodsRestItemEdit extends AbstractGoodsDocumentItemEdit {

    public GoodsRestItemEdit() {
        super();
        initialize();
    }

    private void initialize() {
        this.setEntityClass(InventoryRestItem.class);
    }

    protected void initComponents() throws ApplicationException {
        setTitle(getString("title"));
        GridBagPanel editPanel = new GridBagPanel();
        goodsLabel = new JLabel(getString("panel.label-goods.text"));
        quantityLabel = new JLabel(getString("panel.label-quantity.text"));
        unitLabel = new JLabel(getString("panel.label-unit.text"));
        unitRateLabel = new JLabel(getString("panel.label-unit-rate.text"));
        currencyPriceLabel = new JLabel(getString("panel.label-currency-price.text"));
        currencyAmountLabel = new JLabel(getString("panel.label-currency-amount.text"));
        goodsComboEdit = new XComboEdit();
        goodsComboEdit.setColumns(30);
        quantityField = new XNumberField(12);
        unitComboBox = new ProductUnitComboBox();
        unitRateField = new XNumberField(12);
        unitRateField.setEditable(false);
        currencyPriceField = new XCurrencyField();
        currencyAmountField = new XCurrencyField();
        GridBagConstraints gbc = editPanel.getGridBagConstraints();
        editPanel.add(goodsLabel);
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        editPanel.addByY(goodsComboEdit);
        gbc.gridx = 0;
        editPanel.addByY(Box.createVerticalStrut(3));
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        editPanel.addByY(unitLabel);
        editPanel.addByX(unitComboBox);
        gbc.gridx = 0;
        editPanel.addByY(unitRateLabel);
        editPanel.addByX(unitRateField);
        gbc.gridx = 0;
        editPanel.addByY(quantityLabel);
        editPanel.addByX(quantityField);
        gbc.gridx = 0;
        editPanel.addByY(currencyPriceLabel);
        editPanel.addByX(currencyPriceField);
        gbc.gridx = 0;
        editPanel.addByY(currencyAmountLabel);
        editPanel.addByX(currencyAmountField);
        initControl();
        add(editPanel);
    }

    public InventoryRest getGoodsRest() {
        return (InventoryRest) this.getProductDocument();
    }

    private InventoryRestItem getGoodsRestItem() {
        return (InventoryRestItem) this.getEntity();
    }

    protected void updateView() throws ApplicationException {
        appendTitle(getGoodsRestItem().getProductName());
        goodsComboEdit.setValue(getGoodsRestItem().getProduct());
        unitComboBox.initProduct(getGoodsRestItem().getProduct());
        unitComboBox.setSelectedUnit(getGoodsRestItem().getUnit());
        unitRateField.setValue(getGoodsRestItem().getUnitRate());
        quantityField.setValue(getGoodsRestItem().getQuantity());
        currencyPriceField.setValue(getGoodsRestItem().getCurrencyPrice());
        currencyAmountField.setValue(getGoodsRestItem().getCurrencyAmount());
    }

    protected void populateData() throws ApplicationException {
        getGoodsRestItem().setProduct((Product) goodsComboEdit.getValue());
        getGoodsRestItem().setUnit(unitComboBox.getSelectedUnit());
        getGoodsRestItem().setUnitRate(unitRateField.getValue());
        getGoodsRestItem().setQuantity(quantityField.getValue());
        getGoodsRestItem().setCurrencyPrice(currencyPriceField.getValue());
        getGoodsRestItem().setCurrencyAmount(currencyAmountField.getValue());
        getGoodsRestItem().calculateGeneral(getGoodsRest().getCurrencyRate());
    }

    protected void calculateTaxAmount() {
    }
}
