package gnu.java.locale;

import java.util.ListResourceBundle;

public class LocaleInformation_fo_FO extends ListResourceBundle {

    static final String decimalSeparator = LocaleInformation_da_DK.decimalSeparator;

    static final String groupingSeparator = LocaleInformation_da_DK.groupingSeparator;

    static final String numberFormat = LocaleInformation_da_DK.numberFormat;

    static final String percentFormat = LocaleInformation_da_DK.percentFormat;

    static final String[] weekdays = { null, "sunnudagur", "mánadagur", "týsdagur", "mikudagur", "hósdagur", "fríggjadagur", "leygardagur" };

    static final String[] shortWeekdays = { null, "sun", "mán", "týs", "mik", "hós", "frí", "ley" };

    static final String[] shortMonths = { "jan", "feb", "mar", "apr", "mai", "jun", "jul", "aug", "sep", "okt", "nov", "des", null };

    static final String[] months = { "januar", "februar", "mars", "apríl", "mai", "juni", "juli", "august", "september", "oktober", "november", "desember", null };

    static final String[] ampms = { "", "" };

    static final String shortDateFormat = "dd/MM-yyyy";

    static final String defaultTimeFormat = "";

    static final String currencySymbol = LocaleInformation_da_DK.currencySymbol;

    static final String intlCurrencySymbol = LocaleInformation_da_DK.intlCurrencySymbol;

    static final String currencyFormat = LocaleInformation_da_DK.currencyFormat;

    private static final Object[][] contents = { { "weekdays", weekdays }, { "shortWeekdays", shortWeekdays }, { "shortMonths", shortMonths }, { "months", months }, { "ampms", ampms }, { "shortDateFormat", shortDateFormat }, { "defaultTimeFormat", defaultTimeFormat }, { "currencySymbol", currencySymbol }, { "intlCurrencySymbol", intlCurrencySymbol }, { "currencyFormat", currencyFormat }, { "decimalSeparator", decimalSeparator }, { "groupingSeparator", groupingSeparator }, { "numberFormat", numberFormat }, { "percentFormat", percentFormat } };

    public Object[][] getContents() {
        return contents;
    }
}
