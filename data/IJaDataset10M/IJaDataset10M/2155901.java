package net.sf.jmoney.model2;

import java.text.NumberFormat;
import java.text.ParseException;

/**
 * This class was created because the currency support wich comes with the Java
 * SDK is to complicated. Therefore we provide a simpler model which is
 * not based upon locales but upon the ISO 4217 currencies.
 */
public class Currency extends Commodity {

    private static final int MAX_DECIMALS = 4;

    private static final short[] SCALE_FACTOR = { 1, 10, 100, 1000, 10000 };

    private static NumberFormat[] numberFormat = null;

    private String code;

    private int decimals;

    private static void initNumberFormat() {
        numberFormat = new NumberFormat[MAX_DECIMALS + 1];
        for (int i = 0; i < numberFormat.length; i++) {
            numberFormat[i] = NumberFormat.getNumberInstance();
            numberFormat[i].setMaximumFractionDigits(i);
            numberFormat[i].setMinimumFractionDigits(i);
        }
    }

    /**
     * Constructor used by datastore plug-ins to create
     * a currency object.
     */
    public Currency(IObjectKey objectKey, ListKey parentKey, String name, String code, int decimals, IValues extensionValues) {
        super(objectKey, parentKey, name, extensionValues);
        if (decimals < 0 || decimals > MAX_DECIMALS) throw new IllegalArgumentException("Number of decimals not supported");
        this.code = code;
        this.decimals = decimals;
    }

    /**
     * Constructor used by datastore plug-ins to create
     * a currency object.
     */
    public Currency(IObjectKey objectKey, ListKey parentKey) {
        super(objectKey, parentKey);
        this.code = null;
        this.decimals = 2;
    }

    @Override
    protected String getExtendablePropertySetId() {
        return "net.sf.jmoney.currency";
    }

    /**
	 * @return the currency code.
	 */
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        String oldCode = this.code;
        this.code = code;
        if (oldCode != null) {
            getSession().currencies.remove(oldCode);
        }
        if (code != null) {
            getSession().currencies.put(code, this);
        }
        processPropertyChange(CurrencyInfo.getCodeAccessor(), oldCode, code);
    }

    /**
	 * @return the number of decimals that this currency has.
	 */
    public int getDecimals() {
        return decimals;
    }

    /**
	 * set the number of decimals that this currency has.
	 */
    public void setDecimals(int decimals) {
        int oldDecimals = this.decimals;
        this.decimals = decimals;
        processPropertyChange(CurrencyInfo.getDecimalsAccessor(), new Integer(oldDecimals), new Integer(decimals));
    }

    @Override
    public String toString() {
        return getName() + " (" + getCode() + ")";
    }

    /**
	 * @return a number format instance for this currency.
	 */
    private NumberFormat getNumberFormat() {
        if (numberFormat == null) initNumberFormat();
        return numberFormat[getDecimals()];
    }

    @Override
    public long parse(String amountString) {
        Number amount = new Double(0);
        try {
            amount = getNumberFormat().parse(amountString);
        } catch (ParseException pex) {
        }
        return Math.round(amount.doubleValue() * getScaleFactor());
    }

    @Override
    public String format(long amount) {
        double a = ((double) amount) / getScaleFactor();
        return getNumberFormat().format(a);
    }

    /**
	 * @return the scale factor for this currency (10 to the number of decimals)
	 */
    @Override
    public short getScaleFactor() {
        return SCALE_FACTOR[decimals];
    }
}
