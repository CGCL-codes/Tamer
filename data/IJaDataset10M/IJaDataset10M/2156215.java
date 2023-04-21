package org.formaria.swing;

import java.awt.Graphics;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import org.formaria.aria.ErrorDisplay;
import org.formaria.aria.TextHolder;
import org.formaria.aria.helper.AriaUtilities;
import org.formaria.aria.validation.AbstractValidator;

/**
 * Handles input of monetary values, stripping out the thousand separators as needed.
 * THIS CLASS IS INCOMPLETE
 * 
 * <p> Copyright (c) Formaria Ltd., 2008, This software is licensed under
 * the GNU Public License (GPL), please see license.txt for more details. If
 * you make commercial use of this software you must purchase a commercial
 * license from formaria.</p>
 */
public class MoneyEdit extends Edit implements TextHolder, ErrorDisplay {

    protected char thousandSeparator = ',';

    protected NumberFormat format;

    protected Currency currency;

    protected Locale locale;

    private boolean prefixed;

    private boolean suffixed;

    protected int errorStatus;

    protected String errorMessage;

    /**
   * Create a new MoneyEdit
   */
    public MoneyEdit() {
        super();
        locale = Locale.getDefault();
        currency = Currency.getInstance(locale);
        NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
        setNumberFormat(nf);
        errorStatus = AbstractValidator.USE_NORMAL_STYLE;
    }

    /**
   * Gets the value of the control stripping out and thousand separators and
   * spaces in the process
   * @return the stripped value
   */
    public String getText() {
        String text = super.getText().trim();
        int pos = text.indexOf(thousandSeparator);
        while (pos > 0) {
            text = text.substring(0, pos) + text.substring(pos + 1);
            pos = text.indexOf(thousandSeparator);
        }
        pos = text.indexOf(' ');
        while (pos > 0) {
            text = text.substring(0, pos) + text.substring(pos + 1);
            pos = text.indexOf(' ');
        }
        return text;
    }

    /**
   * Set the format of the currency field
   * @param format the new currency format - the group separator
   */
    public void setFormat(String format) {
        thousandSeparator = format.charAt(0);
    }

    /**
   * Set the format of the currency field
   * @return - the group separator
   */
    public String getFormat() {
        return ((thousandSeparator == ',') ? "," : "");
    }

    /**
   * Set the DecimalFormat of the currency field
   * @param df the new DecimalFormat 
   */
    public void setNumberFormat(NumberFormat df) {
        format = df;
        format.setCurrency(currency);
        if (df instanceof DecimalFormat) thousandSeparator = ((DecimalFormat) df).getDecimalFormatSymbols().getGroupingSeparator();
        setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(df)));
    }

    /**
   * Set the DecimalFormat of the currency field
   * @return - the format
   */
    public NumberFormat getNumberFormat() {
        return format;
    }

    /**
   * Set the currency field
   * @param currency the new currency 
   */
    public void setCurrency(String currencyCode) {
        DecimalFormat df = (DecimalFormat) DecimalFormat.getCurrencyInstance(locale);
        currency = Currency.getInstance(currencyCode);
        df.setCurrency(currency);
        if (!prefixed) {
            df.setPositivePrefix("");
            df.setNegativePrefix("-");
        }
        format = df;
        setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(df)));
    }

    /**
   * Set the country code for the currency e.g 'EN', 'DA', 'FR'
   * @param localeCode the language code of the new currency 
   */
    public void setLocaleLanguage(String localeCode) {
        if (localeCode.length() > 0) {
            locale = new Locale(localeCode, getLocaleCountry());
            setCurrency(getCurrency());
        }
    }

    /**
   * Set the locale country code e.g 'IE', 'US', 'FR'
   * @param countryCode country code of the new currency 
   */
    public void setLocaleCountry(String countryCode) {
        if (countryCode.length() > 0) {
            locale = new Locale(getLocaleLanguage(), countryCode);
            setCurrency(getCurrency());
        }
    }

    /**
   * Get the locale language code 
   * @return the language code
   */
    public String getLocaleLanguage() {
        return locale.getLanguage();
    }

    /**
   * Get the locale country code
   * @return the language code
   */
    public String getLocaleCountry() {
        return locale.getCountry();
    }

    /**
   * Set the format of the currency field
   * @return - the group separator
   */
    public String getCurrency() {
        return currency.getCurrencyCode();
    }

    /**
   * Does the format to use a currency prefix
   * @return true if a prefix is used
   */
    public boolean isPrefixed() {
        return prefixed;
    }

    /**
   * Set the currency prefix flag
   * @param state true if a prefix is to be used
   */
    public void setPrefixed(boolean state) {
        prefixed = state;
    }

    /**
   * Does the format to use a currency suffix
   * @return trueif a prefix is used
   */
    public boolean isSuffixed() {
        return suffixed;
    }

    /**
   * Set the currency suffix flag
   * @param state true if a suffix is to be used
   */
    public void setSuffixed(boolean state) {
        suffixed = state;
    }

    /**
   * Get the thousand separator
   * @return
   */
    public String getThousandSeparator() {
        char[] ts = new char[1];
        ts[0] = thousandSeparator;
        return new String(ts);
    }

    /**
   * Set the thousand separator
   * @param thousandSeparator
   */
    public void setThousandSeparator(String thousandSeparator) {
        this.thousandSeparator = thousandSeparator.charAt(0);
    }

    /**
   * Paint the component, rendering the error badge if necessary
   * @param g
   */
    public void paint(Graphics g) {
        super.paint(g);
        if (errorStatus > AbstractValidator.USE_NORMAL_STYLE) g.drawImage(AriaUtilities.ERROR_BADGE, getWidth() - 8, 0, null);
    }

    /**
   * Set the error status
   * @param status
   */
    public void setErrorStatus(int status) {
        errorStatus = status;
    }

    /**
   * Set the error message
   * @param errorMsg the message text
   */
    public void setErrorMessage(String errorMsg) {
        errorMessage = errorMsg;
        setToolTipText(errorMessage);
    }
}
