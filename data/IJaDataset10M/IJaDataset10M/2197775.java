package gnu.java.locale;

import java.util.ListResourceBundle;

public class LocaleInformation_sq_AL extends ListResourceBundle {

    static final String decimalSeparator = ",";

    static final String groupingSeparator = ".";

    static final String numberFormat = "#,##0.###";

    static final String percentFormat = "#,##0%";

    static final String[] weekdays = { null, "e diel ", "e hënë ", "e martë ", "e mërkurë ", "e enjte ", "e premte ", "e shtunë " };

    static final String[] shortWeekdays = { null, "Die ", "Hën ", "Mar ", "Mër ", "Enj ", "Pre ", "Sht " };

    static final String[] shortMonths = { "Jan", "Shk", "Mar", "Pri", "Maj", "Qer", "Kor", "Gsh", "Sht", "Tet", "Nën", "Dhj", null };

    static final String[] months = { "janar", "shkurt", "mars", "prill", "maj", "qershor", "korrik", "gusht", "shtator", "tetor", "nëntor", "dhjetor", null };

    static final String[] ampms = { "PD", "MD" };

    static final String shortDateFormat = "yyyy-MMM-dd";

    static final String defaultTimeFormat = "hh.m.s.a z";

    static final String currencySymbol = "Lek";

    static final String intlCurrencySymbol = "ALL";

    static final String currencyFormat = "$#,##0.000;-$#,##0.000";

    private static final Object[][] contents = { { "weekdays", weekdays }, { "shortWeekdays", shortWeekdays }, { "shortMonths", shortMonths }, { "months", months }, { "ampms", ampms }, { "shortDateFormat", shortDateFormat }, { "defaultTimeFormat", defaultTimeFormat }, { "currencySymbol", currencySymbol }, { "intlCurrencySymbol", intlCurrencySymbol }, { "currencyFormat", currencyFormat }, { "decimalSeparator", decimalSeparator }, { "groupingSeparator", groupingSeparator }, { "numberFormat", numberFormat }, { "percentFormat", percentFormat } };

    public Object[][] getContents() {
        return contents;
    }
}
