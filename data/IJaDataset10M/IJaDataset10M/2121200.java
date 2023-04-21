package net.sourceforge.xconf.toolbox;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

/**
 * Formatter for Money instances. There are just too many ways to format a Money
 * instance for display to have them as part of the Money class itself. This
 * class provides the most common (so far) formatting options.
 * 
 * @author Tom Czarniecki
 */
public class MoneyFormat {

    private Locale locale;

    private boolean showSymbol;

    private boolean showIsoCode;

    private boolean useGrouping;

    private boolean useBracketsForNegative;

    /**
     * Create an instance using the platform default locale.
     */
    public MoneyFormat() {
        this(Locale.getDefault());
    }

    /**
     * Create an instance using the given locale.
     */
    public MoneyFormat(Locale locale) {
        this.locale = locale;
        this.showIsoCode = true;
    }

    /**
     * Set the locale for this instance that will be used to provide
     * the rules to format currencies and decimal amounts.
     * <p>
     * NOTE: Setting a <code>null</code> locale simply resets this
     * instance to use the platform default locale.
     */
    public void setLocale(Locale inLocale) {
        if (inLocale == null) {
            this.locale = Locale.getDefault();
        } else {
            this.locale = inLocale;
        }
    }

    public Locale getLocale() {
        return locale;
    }

    /**
     * Sets whether this instance should prefix the decimal amount with
     * the ISO 4217 currency code of the Money instance.
     * <p>
     * Default is <code>true</code>.
     * 
     * @see #setShowSymbol(boolean)
     */
    public void setShowIsoCode(boolean showIsoCode) {
        this.showIsoCode = showIsoCode;
    }

    public boolean isShowIsoCode() {
        return showIsoCode;
    }

    /**
     * Returns whether this instance prefix the decimal amount with the
     * currency symbol of the Money instance.
     * <p>
     * Default is <code>false</code>.
     * <p>
     * NOTE: Some locales do not display currency symbols, and some currencies
     * do not actually have a symbol, so what you may see in the formatted
     * string is actually the ISO 4217 currency code in place of a currency
     * symbol.
     * 
     * @see #setShowIsoCode(boolean)
     */
    public void setShowSymbol(boolean showSymbol) {
        this.showSymbol = showSymbol;
    }

    public boolean isShowSymbol() {
        return showSymbol;
    }

    /**
     * By default, negative amounts are prefixed with a minus sign. For some
     * financial applications it is preferred to simply display negative
     * amounts between '(' and ')' brackets. Pass <code>true</code> to this
     * method to enable this behaviour.
     * <p>
     * Default is <code>false</code>.
     */
    public void setUseBracketsForNegative(boolean useBracketsForNegative) {
        this.useBracketsForNegative = useBracketsForNegative;
    }

    public boolean isUseBracketsForNegative() {
        return useBracketsForNegative;
    }

    /**
     * Set whether this instance should format decimal amounts using
     * a locale-specific grouping separator.
     * <p>
     * Default is <code>false</code>.
     */
    public void setUseGrouping(boolean useGrouping) {
        this.useGrouping = useGrouping;
    }

    public boolean isUseGrouping() {
        return useGrouping;
    }

    /**
     * Returns a formatted representation of the given Money instance.
     * <code>null</code> instances are formatted as empty strings.
     */
    public String format(Money money) {
        if (money == null) {
            return "";
        }
        NumberFormat nf = createFormat(money);
        nf.setGroupingUsed(useGrouping);
        Currency currency = money.getCurrency();
        nf.setMinimumFractionDigits(currency.getDefaultFractionDigits());
        nf.setMaximumFractionDigits(currency.getDefaultFractionDigits());
        String value = formatAmount(nf, money);
        if (showIsoCode) {
            value = currency.getCurrencyCode() + " " + value;
        }
        if (needsBrackets(money)) {
            value = "(" + value + ")";
        }
        return value;
    }

    private NumberFormat createFormat(Money money) {
        if (showSymbol) {
            NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
            nf.setCurrency(money.getCurrency());
            return nf;
        }
        return NumberFormat.getNumberInstance(locale);
    }

    private String formatAmount(NumberFormat nf, Money money) {
        if (needsBrackets(money)) {
            return nf.format(money.negate().getDecimalAmount());
        }
        return nf.format(money.getDecimalAmount());
    }

    private boolean needsBrackets(Money money) {
        return money.isNegative() && useBracketsForNegative;
    }
}
