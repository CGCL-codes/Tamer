package com.simpledata.bc.tools;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import com.simpledata.bc.datamodel.money.Currency;

/**
 * Map to be saved with currencies
 */
public class BCCurrencyMap extends HashMap {

    public BCCurrencyMap(String defaultCurrency) {
        put(defaultCurrency, 1d);
    }

    public void put(String currencyCode, double value) {
        if (value == 0) {
            value = -1d;
        }
        super.put(currencyCode, new Double(value));
        myCurrencies = null;
    }

    /** return -1 if this value is unkown **/
    public double getValue(String currencyCode) {
        Object o = super.get(currencyCode);
        if (o == null || !(o instanceof Double)) return -1;
        return ((Double) o).doubleValue();
    }

    /** return the known currencies has an array **/
    public Currency[] getCurrencies() {
        if (myCurrencies == null) doCurrencies();
        return myCurrencies;
    }

    private transient Currency[] myCurrencies;

    /** refesh the currency list **/
    private synchronized void doCurrencies() {
        myCurrencies = new Currency[size()];
        Vector v = new Vector(keySet());
        Collections.sort(v);
        Iterator i = v.iterator();
        int j = 0;
        String key;
        while (i.hasNext()) {
            key = (String) i.next();
            myCurrencies[j] = new Currency(key);
            j++;
        }
    }

    /** XML ***/
    public BCCurrencyMap() {
    }

    ;
}
