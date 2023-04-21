package gnu.java.locale;

import java.util.ListResourceBundle;

/**
 * This class contains locale data for English.
 */
public class LocaleInformation_en extends ListResourceBundle {

    private static final String collation_rules = "<0<1<2<3<4<5<6<7<8<9<A,a<b,B<c,C<d,D<e,E<f,F<g,G<h,H<i,I<j,J<k,K" + "<l,L<m,M<n,N<o,O<p,P<q,Q<r,R<s,S<t,T<u,U<v,V<w,W<x,X<y,Y<z,Z";

    /**
   * This is the list of months, fully spelled out
   */
    private static final String[] months = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December", null };

    /**
   * This is the list of abbreviated month names
   */
    private static final String[] shortMonths = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", null };

    /**
   * This is the list of weekdays, fully spelled out
   */
    private static final String[] weekdays = { null, "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };

    /**
   * This is the list of abbreviated weekdays
   */
    private static final String[] shortWeekdays = { null, "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };

    /**
   * This is the list of AM/PM strings
   */
    private static final String[] ampms = { "AM", "PM" };

    /**
   * This is the list of era identifiers
   */
    private static final String[] eras = { "BC", "AD" };

    private static final String[][] zoneStrings = { { "GMT", "Greenwich Mean Time", "GMT", "Greenwich Mean Time", "GMT", "GMT" }, { "PST", "Pacific Standard Time", "PST", "Pacific Daylight Time", "PDT", "San Francisco" }, { "MST", "Mountain Standard Time", "MST", "Mountain Daylight Time", "MDT", "Denver" }, { "PNT", "Mountain Standard Time", "MST", "Mountain Standard Time", "MST", "Phoenix" }, { "CST", "Central Standard Time", "CST", "Central Daylight Time", "CDT", "Chicago" }, { "EST", "Eastern Standard Time", "EST", "Eastern Daylight Time", "EDT", "Boston" }, { "IET", "Eastern Standard Time", "EST", "Eastern Standard Time", "EST", "Indianapolis" }, { "PRT", "Atlantic Standard Time", "AST", "Atlantic Daylight Time", "ADT", "Halifax" }, { "CNT", "Newfoundland Standard Time", "NST", "Newfoundland Daylight Time", "NDT", "St. Johns" }, { "ECT", "Central European Standard Time", "CET", "Central European Daylight Time", "CEST", "Paris" }, { "CTT", "China Standard Time", "CST", "China Standard Time", "CST", "Shanghai" }, { "JST", "Japan Standard Time", "JST", "Japan Standard Time", "JST", "Tokyo" }, { "HST", "Hawaii Standard Time", "HST", "Hawaii Standard Time", "HST", "Honolulu" }, { "AST", "Alaska Standard Time", "AKST", "Alaska Daylight Time", "AKDT", "Anchorage" } };

    /**
   * This is the object array used to hold the keys and values
   * for this bundle
   */
    private static final Object[][] contents = { { "collation_rules", collation_rules }, { "months", months }, { "shortMonths", shortMonths }, { "weekdays", weekdays }, { "shortWeekdays", shortWeekdays }, { "ampms", ampms }, { "eras", eras }, { "localPatternChars", "GyMdkHmsSEDFwWahKzYeugAZ" }, { "zoneStrings", zoneStrings }, { "shortDateFormat", "M/d/yy" }, { "mediumDateFormat", "d-MMM-yy" }, { "longDateFormat", "MMMM d, yyyy" }, { "fullDateFormat", "EEEE MMMM d, yyyy G" }, { "defaultDateFormat", "d-MMMM-yy" }, { "shortTimeFormat", "h:mm a" }, { "mediumTimeFormat", "h:mm:ss a" }, { "longTimeFormat", "h:mm:ss a z" }, { "fullTimeFormat", "h:mm:ss;S 'o''clock' a z" }, { "defaultTimeFormat", "h:mm:ss a" }, { "decimalSeparator", "." }, { "digit", "#" }, { "exponential", "E" }, { "groupingSeparator", "," }, { "infinity", "∞" }, { "NaN", "�" }, { "minusSign", "-" }, { "monetarySeparator", "." }, { "patternSeparator", ";" }, { "percent", "%" }, { "perMill", "‰" }, { "zeroDigit", "0" }, { "numberFormat", "#,##0.###" }, { "percentFormat", "#,##0%" } };

    /**
   * This method returns the object array of key, value pairs containing
   * the data for this bundle.
   *
   * @return The key, value information.
   */
    public Object[][] getContents() {
        return contents;
    }
}
