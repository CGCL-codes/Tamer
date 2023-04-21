package net.yapbam.gui.tools;

import java.text.MessageFormat;
import java.util.Arrays;
import javax.swing.table.AbstractTableModel;
import net.yapbam.currency.CurrencyConverter;

@SuppressWarnings("serial")
public class CurrencyTableModel extends AbstractTableModel {

    private int currentCurrency;

    private CurrencyConverter converter;

    private String[] codes;

    public CurrencyTableModel(CurrencyConverter converter) {
        this.converter = converter;
        if (this.converter != null) {
            this.codes = this.converter.getCurrencies();
            Arrays.sort(this.codes);
            this.currentCurrency = 0;
        } else {
            this.currentCurrency = -1;
        }
    }

    public void setCurrency(String currencyCode) {
        if (this.converter != null) {
            this.currentCurrency = Arrays.binarySearch(this.codes, currencyCode);
            this.fireTableStructureChanged();
        }
    }

    @Override
    public int getColumnCount() {
        if ((this.converter == null) || (this.codes.length == 0)) return 0; else return 3;
    }

    @Override
    public int getRowCount() {
        return this.currentCurrency < 0 ? 0 : this.codes.length - 1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        String currencyCode = this.codes[(rowIndex < this.currentCurrency) ? rowIndex : rowIndex + 1];
        if (columnIndex == 0) return CurrencyNames.getString(currencyCode);
        if (columnIndex == 1) return this.converter.convert(1.0, this.codes[currentCurrency], currencyCode);
        if (columnIndex == 2) return this.converter.convert(1.0, currencyCode, this.codes[currentCurrency]);
        throw new IllegalArgumentException();
    }

    @Override
    public String getColumnName(int column) {
        if (column == 0) return Messages.getString("CurrencyConverterPanel.CurrencyColumnName");
        String currencyName = CurrencyNames.getString(this.codes[currentCurrency]);
        if (column == 1) return MessageFormat.format(Messages.getString("CurrencyConverterPanel.ToCurrencyColumnName"), currencyName);
        if (column == 2) return MessageFormat.format(Messages.getString("CurrencyConverterPanel.CurrencyToColumnName"), currencyName);
        return super.getColumnName(column);
    }
}
