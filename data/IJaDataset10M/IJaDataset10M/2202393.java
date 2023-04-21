package util;

import java.util.Currency;
import java.util.Locale;
import java.text.DecimalFormatSymbols;

/**
 *  Test a currency symbol
 *  @keyword
 */
public class DoPriveleged {

    public static void main(String[] s) {
        Currency c = Currency.getInstance(Locale.US);
        new DecimalFormatSymbols();
        System.out.println(c.getSymbol());
        System.out.println("PASSED");
    }
}
