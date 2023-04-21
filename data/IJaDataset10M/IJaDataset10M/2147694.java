package com.zara.store.client.view.swing.table;

public class InvoiceTableModel extends StoreTableModel {

    private static final int INVOICE_ID = 0;

    private static final int INVOICE_TOTAL = 1;

    private static final int INVOICE_SUBTOTAL = 2;

    private static final int INVOICE_TYPE = 3;

    private static final int INVOICE_TAX_IVA = 4;

    private static final int CLIENT_CUIT = 5;

    private static final int CLIENT_NAME = 6;

    private static final int CLIENT_ADDRESS = 7;

    private static final int CLIENT_STATUS_IVA = 8;

    private static final long serialVersionUID = 1L;

    private boolean modifiable = true;

    public InvoiceTableModel() {
        super();
        setColumnIdentifiers(new Object[] { "Detalle", "Tipo" });
        addRow(new Object[] { "Factura", "" });
        addRow(new Object[] { "Total a facturar en pesos", "" });
        addRow(new Object[] { "Subtotal", "" });
        addRow(new Object[] { "Factura tipo", "B" });
        addRow(new Object[] { "IVA", "21%" });
        addRow(new Object[] { "CUIT Cliente", "Consumidor Final" });
        addRow(new Object[] { "Nombre Cliente", " " });
        addRow(new Object[] { "Direcci�n Cliente", " " });
        addRow(new Object[] { "Situaci�n IVA Cliente", "Inscripto" });
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex == 1) {
            if (rowIndex == INVOICE_TYPE || rowIndex == CLIENT_ADDRESS || rowIndex == CLIENT_CUIT || rowIndex == CLIENT_NAME || rowIndex == CLIENT_STATUS_IVA) {
                return modifiable;
            }
        }
        return false;
    }

    public void setClientAddressAction(TableOnChangeAction clientAddressAction) {
        this.clientAddressAction = clientAddressAction;
    }

    public void setClientCUITAction(TableOnChangeAction clientCUITAction) {
        this.clientCUITAction = clientCUITAction;
    }

    public void setClientNameAction(TableOnChangeAction clientNameAction) {
        this.clientNameAction = clientNameAction;
    }

    public void setClientStatusIVAAction(TableOnChangeAction clientStatusIVAAction) {
        this.clientStatusIVAAction = clientStatusIVAAction;
    }

    public void setInvoiceTaxIVAAction(TableOnChangeAction invoiceTaxIVAAction) {
        this.invoiceTaxIVAAction = invoiceTaxIVAAction;
    }

    public void setInvoiceTypeAction(TableOnChangeAction invoiceTypeAction) {
        this.invoiceTypeAction = invoiceTypeAction;
    }

    public void setModifiable(boolean modifiable) {
        this.modifiable = modifiable;
    }

    private TableOnChangeAction invoiceTypeAction;

    private TableOnChangeAction invoiceTaxIVAAction;

    private TableOnChangeAction clientCUITAction;

    private TableOnChangeAction clientNameAction;

    private TableOnChangeAction clientAddressAction;

    private TableOnChangeAction clientStatusIVAAction;

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == INVOICE_TYPE) {
            invoiceTypeAction.onChange(rowIndex, aValue.toString());
            if (!aValue.toString().equals("A") || !aValue.toString().equals("B") || !aValue.toString().equals("C")) {
                return;
            }
        }
        super.setValueAt(aValue, rowIndex, columnIndex);
        if (columnIndex == INVOICE_TAX_IVA) invoiceTaxIVAAction.onChange(rowIndex, aValue.toString());
        if (columnIndex == CLIENT_CUIT) clientCUITAction.onChange(rowIndex, aValue.toString());
        if (columnIndex == CLIENT_NAME) clientNameAction.onChange(rowIndex, aValue.toString());
        if (columnIndex == CLIENT_ADDRESS) clientAddressAction.onChange(rowIndex, aValue.toString());
        if (columnIndex == CLIENT_STATUS_IVA) clientStatusIVAAction.onChange(rowIndex, aValue.toString());
    }

    public void updateInvoiceId(Object aValue) {
        super.setValueAt(aValue, INVOICE_ID, 1);
    }

    public void updateInvoiceTotal(Object aValue) {
        super.setValueAt(aValue, INVOICE_TOTAL, 1);
    }

    public void updateInvoiceSubTotal(String aValue) {
        super.setValueAt(aValue, INVOICE_SUBTOTAL, 1);
    }
}

;
