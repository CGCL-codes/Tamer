package gnu.java.locale;

import java.util.ListResourceBundle;

public class LocaleInformation_ar_BH extends ListResourceBundle {

    static final String decimalSeparator = ".";

    static final String groupingSeparator = ",";

    static final String numberFormat = "#,##0.###";

    static final String percentFormat = "#,##0%";

    static final String[] weekdays = { null, "الأحد", "الاثنين", "الثلاثاء", "الأربعاء", "الخميس", "الجمعة", "السبت" };

    static final String[] shortWeekdays = { null, "ح", "ن", "ث", "ر", "خ", "ج", "س" };

    static final String[] shortMonths = { "ينا", "فبر", "مار", "أبر", "ماي", "يون", "يول", "أغس", "سبت", "أكت", "نوف", "ديس", null };

    static final String[] months = { "يناير", "فبراير", "مارس", "أبريل", "مايو", "يونيو", "يوليو", "أغسطس", "سبتمبر", "أكتوبر", "نوفمبر", "ديسمبر", null };

    static final String[] ampms = { "ص", "م" };

    static final String shortDateFormat = "dd MMM, yyyy";

    static final String defaultTimeFormat = "z hh:m:s a";

    static final String currencySymbol = "د.ب.";

    static final String intlCurrencySymbol = "BHD";

    static final String currencyFormat = "$ #,##0.000;$ #,##0.000-";

    private static final Object[][] contents = { { "weekdays", weekdays }, { "shortWeekdays", shortWeekdays }, { "shortMonths", shortMonths }, { "months", months }, { "ampms", ampms }, { "shortDateFormat", shortDateFormat }, { "defaultTimeFormat", defaultTimeFormat }, { "currencySymbol", currencySymbol }, { "intlCurrencySymbol", intlCurrencySymbol }, { "currencyFormat", currencyFormat }, { "decimalSeparator", decimalSeparator }, { "groupingSeparator", groupingSeparator }, { "numberFormat", numberFormat }, { "percentFormat", percentFormat } };

    public Object[][] getContents() {
        return contents;
    }
}
