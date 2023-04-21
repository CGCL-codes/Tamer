package gnu.testlet.java2.util.Currency;

import gnu.testlet.Testlet;
import gnu.testlet.TestHarness;
import java.util.Calendar;
import java.util.Currency;
import java.util.Locale;

/**
 * Class to test the Italian currency.
 *
 * @author Andrew John Hughes <gnu_andrew@member.fsf.org>
 */
public class Italy implements Testlet {

    private static final Locale TEST_LOCALE = Locale.ITALY;

    private static final String ISO4217_CODE = "ITL";

    private static final String CURRENCY_SYMBOL = "L.";

    private static final int FRACTION_DIGITS = 0;

    private static final String EURO_ISO4217_CODE = "EUR";

    private static final String EURO_CURRENCY_SYMBOL = "€";

    private static final int EURO_FRACTION_DIGITS = 2;

    private static final int EURO_CHANGE_YEAR = 2002;

    private static final int EURO_CHANGE_MONTH = 0;

    private static final int EURO_CHANGE_DATE = 1;

    public void test(TestHarness harness) {
        Currency currency;
        Calendar calendar;
        Calendar euroCalendar;
        Locale.setDefault(TEST_LOCALE);
        currency = Currency.getInstance(TEST_LOCALE);
        calendar = Calendar.getInstance(TEST_LOCALE);
        euroCalendar = Calendar.getInstance(TEST_LOCALE);
        euroCalendar.set(EURO_CHANGE_YEAR, EURO_CHANGE_MONTH, EURO_CHANGE_DATE);
        if (calendar.after(euroCalendar)) {
            harness.check(currency.getCurrencyCode(), EURO_ISO4217_CODE, "Euro ISO 4217 currency code retrieval check (" + currency.getCurrencyCode() + ").");
            harness.check(currency.getSymbol(), EURO_CURRENCY_SYMBOL, "Euro currency symbol retrieval check (" + currency.getSymbol() + ").");
            harness.check(currency.getDefaultFractionDigits(), EURO_FRACTION_DIGITS, "Euro currency fraction digits retrieval check (" + currency.getDefaultFractionDigits() + ").");
            harness.check(currency.toString(), EURO_ISO4217_CODE, "Euro ISO 4217 currency code retrieval check (" + currency.toString() + ").");
        } else {
            harness.check(currency.getCurrencyCode(), ISO4217_CODE, "ISO 4217 currency code retrieval check (" + currency.getCurrencyCode() + ").");
            harness.check(currency.getSymbol(), CURRENCY_SYMBOL, "Currency symbol retrieval check (" + currency.getSymbol() + ").");
            harness.check(currency.getDefaultFractionDigits(), FRACTION_DIGITS, "Currency fraction digits retrieval check (" + currency.getDefaultFractionDigits() + ").");
            harness.check(currency.toString(), ISO4217_CODE, "ISO 4217 currency code retrieval check (" + currency.toString() + ").");
        }
    }
}
