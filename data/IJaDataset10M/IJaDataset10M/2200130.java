package gnu.java.locale;

import java.util.ListResourceBundle;

public class LocaleInformation_se_NO extends ListResourceBundle {

    static final String decimalSeparator = ",";

    static final String groupingSeparator = ".";

    static final String numberFormat = "#,###,##0.###";

    static final String percentFormat = "#,###,##0%";

    static final String[] weekdays = { null, "sotnabeaivi", "vuossárga", "maŋŋebarga", "gaskavahkku", "duorasdat", "bearjadat", "lávvardat" };

    static final String[] shortWeekdays = { null, "sotn", "vuos", "maŋ", "gask", "duor", "bear", "láv" };

    static final String[] shortMonths = { "ođđj", "guov", "njuk", "cuoŋ", "mies", "geas", "suoi", "borg", "čakč", "golg", "skáb", "juov", null };

    static final String[] months = { "ođđajagemánu", "guovvamánu", "njukčamánu", "cuoŋománu", "miessemánu", "geassemánu", "suoidnemánu", "borgemánu", "čakčamánu", "golggotmánu", "skábmamánu", "juovlamánu", null };

    static final String[] ampms = { "", "" };

    static final String shortDateFormat = "yyyy-MM-dd";

    static final String defaultTimeFormat = "";

    static final String currencySymbol = " ru";

    static final String intlCurrencySymbol = "NOK";

    static final String currencyFormat = "$#,###,##0.00;$-#,###,##0.00";

    private static final Object[][] contents = { { "weekdays", weekdays }, { "shortWeekdays", shortWeekdays }, { "shortMonths", shortMonths }, { "months", months }, { "ampms", ampms }, { "shortDateFormat", shortDateFormat }, { "defaultTimeFormat", defaultTimeFormat }, { "currencySymbol", currencySymbol }, { "intlCurrencySymbol", intlCurrencySymbol }, { "currencyFormat", currencyFormat }, { "decimalSeparator", decimalSeparator }, { "groupingSeparator", groupingSeparator }, { "numberFormat", numberFormat }, { "percentFormat", percentFormat } };

    public Object[][] getContents() {
        return contents;
    }
}
