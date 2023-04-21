package data;

import data.ooimpl.CatalogImpl;
import data.ooimpl.CurrencyItemImpl;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * Abstract Java implementation of the {@link Currency} interface.
 *
 * @author  Thomas Medack
 * @version 3.0
 */
public abstract class AbstractCurrency extends CatalogImpl implements Currency {

    /**
   * Data container for names and values of CurrencyItems.
   */
    protected class CurrencyItemData {

        private String name;

        private int value;

        /**
     * Constructor
     *
     * @param name the name of the CurrencyItem.
     * @param value the appropriate value of the CurrencyItem.
     */
        public CurrencyItemData(String name, int value) {
            this.value = value;
            this.name = name;
        }

        /**
     * Returns the name of a CurrencyItem in this data container.
     *
     * @return the name of the CurrencyItem.
     */
        public String getName() {
            return name;
        }

        /**
     * Returns the value of a CurrencyItem in this data container.
     *
     * @return the value of the CurrencyItem.
     */
        public int getValue() {
            return value;
        }
    }

    /**
    * Tool used to format and parse currency values.
    *
    * @serial
    */
    private NumberFormat m_nfFormatter;

    /**
    * Create a new, initially empty AbstractCurrency for the given locale.
    *
    * @param sName the name of the currency to create.
    * @param l the locale that determines how currency values will be formatted.
    */
    public AbstractCurrency(String sName, Locale l) {
        super(sName);
        m_nfFormatter = NumberFormat.getCurrencyInstance(l);
        initCurrencyItems();
    }

    /**
    * Create a new AbstractCurrency with a default locale of {@link Locale#GERMANY} and fill it.
    *
    * @param sName the name of the new currency.
    */
    public AbstractCurrency(String sName) {
        this(sName, Locale.GERMANY);
    }

    /**
   * Initializes all CurrencyItems in this container with names and values.
   */
    private void initCurrencyItems() {
        CurrencyItemData[] data = getCurrencyItemData();
        for (int i = 0; i < data.length; i++) {
            add(new CurrencyItemImpl(data[i].getName(), data[i].getValue()), null);
        }
    }

    /**
   * Try to interpret the given {@link String} according to the currency format of the specific currency.
   *
   * @override Always
   * @param s the text to be parsed
   * @return the interpreted value in the smallest unit of the currency.
   * @exception ParseException if the input could not be parsed.
   */
    public NumberValue parse(String s) throws ParseException {
        int j = 1;
        for (int i = 1; i <= m_nfFormatter.getMinimumFractionDigits(); i++) {
            j *= 10;
        }
        return new IntegerValue((int) java.lang.Math.round(m_nfFormatter.parse(s).doubleValue() * j));
    }

    /**
   * Convert the given value into a {@link String} representation according to the currency format of the
   * specific currency. <code>nv</code> must be given in the smallest unit of the currency, i.e. if you want
   * to specify 5,05 DM <code>nv</code> should be 505.
   *
   * @override Always
   * @param nv the value to be converted
   * @return formatted String.
   */
    public String toString(NumberValue nv) {
        int j = 1;
        for (int i = 1; i <= m_nfFormatter.getMinimumFractionDigits(); i++) {
            j *= 10;
        }
        return m_nfFormatter.format(nv.getValue().doubleValue() / j);
    }

    /**
   * This abstract method allows the programmer to choose the names and values of the
   * CurrencyItems (EURO, DM, ...).
   *
   * A complete list of data containers, which always contain pairs of name and value, has to be returned.
   * The single CurrencyItems can be found in the Catalog (Currency) with those names.
   *
   * @return an Array of data containers that contain names and values of CurrencyItems to be added.
   */
    protected abstract CurrencyItemData[] getCurrencyItemData();
}
