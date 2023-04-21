package com.legstar.xsdc.test.cases.cultureinfo;

/**
 * The reply data object.
 *
 */
public class CultureInfoReply {

    /** The Locale display language. */
    private String mDisplayLanguage;

    /** The Locale display country. */
    private String mDisplayCountry;

    /** The formatted date. */
    private String mFormattedDate;

    /** The Locale currency symbol . */
    private String mCurrencySymbol;

    /** The formatted decimal number. */
    private String mFormattedDecimalNumber;

    /** The JVM culture info. */
    private ServerCultureInfo mServerCultureInfo;

    /**
     * @return the Currency Symbol
     */
    public String getCurrencySymbol() {
        return mCurrencySymbol;
    }

    /**
     * @param currencySymbol the Currency Symbol to set
     */
    public void setCurrencySymbol(final String currencySymbol) {
        mCurrencySymbol = currencySymbol;
    }

    /**
     * @return the Formatted Date
     */
    public String getFormattedDate() {
        return mFormattedDate;
    }

    /**
     * @param formattedDate the Formatted Date to set
     */
    public void setFormattedDate(final String formattedDate) {
        mFormattedDate = formattedDate;
    }

    /**
     * @return the Display Country
     */
    public String getDisplayCountry() {
        return mDisplayCountry;
    }

    /**
     * @param displayCountry the Display Country to set
     */
    public void setDisplayCountry(final String displayCountry) {
        mDisplayCountry = displayCountry;
    }

    /**
     * @return the Display Language
     */
    public String getDisplayLanguage() {
        return mDisplayLanguage;
    }

    /**
     * @param displayLanguage the Display Language to set
     */
    public void setDisplayLanguage(final String displayLanguage) {
        mDisplayLanguage = displayLanguage;
    }

    /**
     * @return the Formatted Decimal Number
     */
    public String getFormattedDecimalNumber() {
        return mFormattedDecimalNumber;
    }

    /**
     * @param formattedDecimalNumber the Formatted Decimal Number to set
     */
    public void setFormattedDecimalNumber(final String formattedDecimalNumber) {
        mFormattedDecimalNumber = formattedDecimalNumber;
    }

    /**
     * @return the Server Culture Info
     */
    public ServerCultureInfo getServerCultureInfo() {
        return mServerCultureInfo;
    }

    /**
     * @param serverCultureInfo the Server Culture Info to set
     */
    public void setServerCultureInfo(final ServerCultureInfo serverCultureInfo) {
        mServerCultureInfo = serverCultureInfo;
    }
}
