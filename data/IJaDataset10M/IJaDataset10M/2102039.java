package net.sourceforge.zmanim.hebrewcalendar;

import java.text.SimpleDateFormat;

/**
 * The HebrewDateFormatter class formats a {@link JewishDate}.
 * 
 * The class formats Jewish dates in Hebrew or Latin chars, and has various settings. Sample full date output includes
 * (using various options):
 * <ul>
 * <li>21 Shevat, 5729</li>
 * <li>&#x5DB;&#x5D0; &#x5E9;&#x5D1;&#x5D8; &#x5EA;&#x5E9;&#x5DB;&#x5D8;</li>
 * <li>&#x5D4;&#x5F3; &#x5DB;&#x5F4;&#x5D0; &#x5E9;&#x5D1;&#x5D8; &#x5EA;&#x5E9;&#x5DB;&#x5F4;&#x5D8;</li>
 * <li>&#x5DB;&#x5F4;&#x5D0; &#x5E9;&#x5D1;&#x5D8; &#x5EA;&#x5E9;&#x5DA;&#x5F3;</li>
 * </ul>
 * 
 * @see net.sourceforge.zmanim.hebrewcalendar.JewishDate
 * @see net.sourceforge.zmanim.hebrewcalendar.JewishCalendar
 * 
 * @author &copy; Eliyahu Hershfeld 2011
 * @version 0.3
 */
public class HebrewDateFormatter {

    private boolean hebrewFormat = false;

    private boolean useLonghebrewYears = false;

    private boolean useGershGershayim = true;

    private boolean longWeekFormat = true;

    /**
	 * returns if the {@link #formatDayOfWeek(JewishDate)} will use the long format such as
	 * &#x05E8;&#x05D0;&#x05E9;&#x05D5;&#x05DF; or short such as &#x05D0; when formatting the day of week in
	 * {@link #isHebrewFormat() Hebrew}.
	 * 
	 * @return the longWeekFormat
	 * @see #setLongWeekFormat(boolean)
	 * @see #formatDayOfWeek(JewishDate)
	 */
    public boolean isLongWeekFormat() {
        return longWeekFormat;
    }

    /**
	 * Setting to control if the {@link #formatDayOfWeek(JewishDate)} will use the long format such as
	 * &#x05E8;&#x05D0;&#x05E9;&#x05D5;&#x05DF; or short such as &#x05D0; when formatting the day of week in
	 * {@link #isHebrewFormat() Hebrew}.
	 * 
	 * @param longWeekFormat
	 *            the longWeekFormat to set
	 */
    public void setLongWeekFormat(boolean longWeekFormat) {
        this.longWeekFormat = longWeekFormat;
    }

    private static final String GERESH = "׳";

    private static final String GERSHAYIM = "״";

    private String[] transliteratedMonths = { "Nissan", "Iyar", "Sivan", "Tammuz", "Av", "Elul", "Tishrei", "Cheshvan", "Kislev", "Teves", "Shevat", "Adar", "Adar II", "Adar I" };

    private String hebrewOmerPrefix = "ב";

    private String transliteratedShabbosDayOfweek = "Shabbos";

    /**
	 * Returns the day of Shabbos transliterated into Latin chars. The default uses Ashkenazi pronounciation "Shabbos".
	 * This can be overwritten using the {@link #setTransliteratedShabbosDayOfWeek(String)}
	 * 
	 * @return the transliteratedShabbos. The default list of months uses Ashkenazi pronounciation "Shabbos".
	 * @see #setTransliteratedShabbosDayOfWeek(String)
	 * @see #formatDayOfWeek(JewishDate)
	 */
    public String getTransliteratedShabbosDayOfWeek() {
        return transliteratedShabbosDayOfweek;
    }

    /**
	 * Setter to override the default transliterated name of "Shabbos" to alternate spelling such as "Shabbat" used by
	 * the {@link #formatDayOfWeek(JewishDate)}
	 * 
	 * @param transliteratedShabbos
	 *            the transliteratedShabbos to set
	 * 
	 * @see #getTransliteratedShabbosDayOfWeek()
	 * @see #formatDayOfWeek(JewishDate)
	 */
    public void setTransliteratedShabbosDayOfWeek(String transliteratedShabbos) {
        this.transliteratedShabbosDayOfweek = transliteratedShabbos;
    }

    private String[] transliteratedHolidays = { "Erev Pesach", "Pesach", "Chol Hamoed Pesach", "Pesach Sheni", "Erev Shavuos", "Shavuos", "Seventeenth of Tammuz", "Tishah B'Av", "Tu B'Av", "Erev Rosh Hashana", "Rosh Hashana", "Fast of Gedalyah", "Erev Yom Kippur", "Yom Kippur", "Erev Succos", "Succos", "Chol Hamoed Succos", "Hoshana Rabbah", "Shemini Atzeres", "Simchas Torah", "Erev Chanukah", "Chanukah", "Tenth of Teves", "Tu B'Shvat", "Fast of Esther", "Purim", "Shushan Purim", "Purim Katan", "Rosh Chodesh", "Yom HaShoah", "Yom Hazikaron", "Yom Ha'atzmaut", "Yom Yerushalayim" };

    /**
	 * Returns the list of holidays transliterated into Latin chars. This is used by the
	 * {@link #formatYomTov(JewishCalendar)} when formatting the Yom Tov String. The default list of months uses
	 * Ashkenazi pronunciation in typical American English spelling.
	 * 
	 * @return the list of holidays "Adar", "Adar II", "Adar I". The default list is currently "Erev Pesach", "Pesach",
	 *         "Chol Hamoed Pesach", "Pesach Sheni", "Erev Shavuos", "Shavuos", "Seventeenth of Tammuz", "Tishah B'Av",
	 *         "Tu B'Av", "Erev Rosh Hashana", "Rosh Hashana", "Fast of Gedalyah", "Erev Yom Kippur", "Yom Kippur",
	 *         "Erev Succos", "Succos", "Chol Hamoed Succos", "Hoshana Rabbah", "Shemini Atzeres", "Simchas Torah",
	 *         "Erev Chanukah", "Chanukah", "Tenth of Teves", "Tu B'Shvat", "Fast of Esther", "Purim", "Shushan Purim",
	 *         "Purim Katan", "Rosh Chodesh", "Yom HaShoah", "Yom Hazikaron", "Yom Ha'atzmaut", "Yom Yerushalayim"
	 * 
	 * @see #setTransliteratedMonthList(String[])
	 * @see #formatYomTov(JewishCalendar)
	 * @see #isHebrewFormat()
	 */
    public String[] getTransliteratedHolidayList() {
        return transliteratedHolidays;
    }

    /**
	 * Sets the list of holidays transliterated into Latin chars. This is used by the
	 * {@link #formatYomTov(JewishCalendar)} when formatting the Yom Tov String.
	 * 
	 * @param transliteratedHolidays
	 *            the transliteratedHolidays to set. Ensure that the sequence exactly matches the list returned by the
	 *            defaulyt
	 */
    public void setTransliteratedHolidayList(String[] transliteratedHolidays) {
        this.transliteratedHolidays = transliteratedHolidays;
    }

    /**
	 * Hebrew holiday list
	 */
    private String[] hebrewHolidays = { "ערב פסח", "פסח", "חול המועד פסח", "פסח שני", "ערב שבועות", "שבועות", "שבעה עשר בתמוז", "תשעה באב", "ט״ו באב", "ערב ראש השנה", "ראש השנה", "צום גדליה", "ערב יום כיפור", "יום כיפור", "ערב סוכות", "סוכות", "חול המועד סוכות", "הושענא רבה", "שמיני עצרת", "שמחת תורה", "ערב חנוכה", "חנוכה", "עשרה בטבת", "ט״ו בשבט", "תענית אסתר", "פורים", "פורים שושן", "פורים קטן", "ראש חודש", "יום השואה", "יום הזיכרון", "יום העצמאות", "יום ירושלים" };

    /**
	 * Formats the Yom Tov (holiday) in Hebrew or transliterated Latin characters.
	 * 
	 * @param jewishCalendar
	 * @return the formatted holiday or an empty String if the day is not a holiday.
	 * @see #isHebrewFormat()
	 */
    public String formatYomTov(JewishCalendar jewishCalendar) {
        int index = jewishCalendar.getYomTovIndex();
        if (index == JewishCalendar.CHANUKAH) {
            int dayOfChanukah = jewishCalendar.getDayOfChanukah();
            return hebrewFormat ? (formatHebrewNumber(dayOfChanukah) + " " + hebrewHolidays[index]) : (transliteratedHolidays[index] + dayOfChanukah);
        }
        return index == -1 ? "" : hebrewFormat ? hebrewHolidays[index] : transliteratedHolidays[index];
    }

    public String formatRoshChodesh(JewishCalendar jewishCalendar) {
        if (!jewishCalendar.isRoshChodesh()) {
            return "";
        }
        String formattedRoshChodesh = "";
        int month = jewishCalendar.getJewishMonth();
        if (jewishCalendar.getJewishDayOfMonth() == 30) {
            if (month < JewishCalendar.ADAR || (month == JewishCalendar.ADAR && jewishCalendar.isJewishLeapYear())) {
                month++;
            } else {
                month = JewishCalendar.NISSAN;
            }
        }
        jewishCalendar = (JewishCalendar) jewishCalendar.clone();
        jewishCalendar.setJewishMonth(month);
        formattedRoshChodesh = hebrewFormat ? hebrewHolidays[JewishCalendar.ROSH_CHODESH] : transliteratedHolidays[JewishCalendar.ROSH_CHODESH];
        formattedRoshChodesh += " " + formatMonth(jewishCalendar);
        return formattedRoshChodesh;
    }

    /**
	 * Returns if the formatter is set to use Hebrew formatting in the various formatting methods.
	 * 
	 * @return the hebrewFormat
	 * @see #setHebrewFormat(boolean)
	 * @see #format(JewishDate)
	 * @see #formatDayOfWeek(JewishDate)
	 * @see #formatMonth(JewishDate)
	 * @see #formatOmer(JewishCalendar)
	 * @see #formatParsha(JewishCalendar)
	 * @see #formatYomTov(JewishCalendar)
	 */
    public boolean isHebrewFormat() {
        return hebrewFormat;
    }

    /**
	 * Sets the formatter to format in Hebrew in the various formatting methods.
	 * 
	 * @param hebrewFormat
	 *            the hebrewFormat to set
	 * @see #isHebrewFormat()
	 * @see #format(JewishDate)
	 * @see #formatDayOfWeek(JewishDate)
	 * @see #formatMonth(JewishDate)
	 * @see #formatOmer(JewishCalendar)
	 * @see #formatParsha(JewishCalendar)
	 * @see #formatYomTov(JewishCalendar)
	 */
    public void setHebrewFormat(boolean hebrewFormat) {
        this.hebrewFormat = hebrewFormat;
    }

    /**
	 * Returns the Hebrew Omer prefix. By default it is the letter &#x5D1;, but can be set to &#x5DC; (or any other
	 * prefix) using the {@link #setHebrewOmerPrefix(String)}.
	 * 
	 * @return the hebrewOmerPrefix
	 * 
	 * @see #setHebrewOmerPrefix(String)
	 * @see #formatOmer(JewishCalendar)
	 */
    public String getHebrewOmerPrefix() {
        return hebrewOmerPrefix;
    }

    /**
	 * Method to set the Hebrew Omer prefix. By default it is the letter &#x5D1;, but this allows setting it to a
	 * &#x5DC; (or any other prefix).
	 * 
	 * @param hebrewOmerPrefix
	 *            the hebrewOmerPrefix to set. You can use the Unicode &#92;u05DC to set it to &#x5DC;.
	 * @see #getHebrewOmerPrefix()
	 * @see #formatOmer(JewishCalendar)
	 */
    public void setHebrewOmerPrefix(String hebrewOmerPrefix) {
        this.hebrewOmerPrefix = hebrewOmerPrefix;
    }

    /**
	 * Returns the list of months transliterated into Latin chars. The default list of months uses Ashkenazi
	 * pronunciation in typical American English spelling. This list has a length of 14 with 3 variations for Adar -
	 * "Adar", "Adar II", "Adar I"
	 * 
	 * @return the list of months beginning in Nissan and ending in in "Adar", "Adar II", "Adar I". The default list is
	 *         currently "Nissan", "Iyar", "Sivan", "Tammuz", "Av", "Elul", "Tishrei", "Cheshvan", "Kislev", "Teves",
	 *         "Shevat", "Adar", "Adar II", "Adar I"
	 * @see #setTransliteratedMonthList(String[])
	 */
    public String[] getTransliteratedMonthList() {
        return transliteratedMonths;
    }

    /**
	 * Setter method to allow overriding of the default list of months transliterated into into Latin chars. The default
	 * uses Ashkenazi American English transliteration.
	 * 
	 * @param transliteratedMonths
	 *            an array of 14 month names such as { "Nissan", "Iyar", "Sivan", "Tamuz", "Av", "Elul", "Tishrei",
	 *            "Heshvan", "Kislev", "Tevet", "Shevat", "Adar", "Adar II", "Adar I" }
	 * @see #getTransliteratedMonthList()
	 */
    public void setTransliteratedMonthList(String[] transliteratedMonths) {
        this.transliteratedMonths = transliteratedMonths;
    }

    /**
	 * Unicode list of Hebrew months.
	 * 
	 * @see #formatMonth(JewishDate)
	 */
    private static final String[] hebrewMonths = { "ניסן", "אייר", "סיוון", "תמוז", "אב", "אלול", "תשרי", "חשוון", "כסלו", "טבת", "שבט", "אדר", "אדר ב", "אדר א" };

    /**
	 * list of transliterated parshiyos using the default Ashkenazi pronounciation. The formatParsha method uses this
	 * for transliterated parsha display. This list can be overridden (for Sephardi English transliteration for example)
	 * by setting the {@link #setTransliteratedParshiosList(String[])}.
	 * 
	 * @see #formatParsha(JewishCalendar)
	 */
    private String[] transliteratedParshios = { "Bereshis", "Noach", "Lech Lecha", "Vayera", "Chayei Sara", "Toldos", "Vayetzei", "Vayishlach", "Vayeshev", "Miketz", "Vayigash", "Vayechi", "Shemos", "Vaera", "Bo", "Beshalach", "Yisro", "Mishpatim", "Terumah", "Tetzaveh", "Ki Sisa", "Vayakhel", "Pekudei", "Vayikra", "Tzav", "Shmini", "Tazria", "Metzora", "Achrei Mos", "Kedoshim", "Emor", "Behar", "Bechukosai", "Bamidbar", "Nasso", "Beha'aloscha", "Sh'lach", "Korach", "Chukas", "Balak", "Pinchas", "Matos", "Masei", "Devarim", "Vaeschanan", "Eikev", "Re'eh", "Shoftim", "Ki Seitzei", "Ki Savo", "Nitzavim", "Vayeilech", "Ha'Azinu", "Vayakhel Pekudei", "Tazria Metzora", "Achrei Mos Kedoshim", "Behar Bechukosai", "Chukas Balak", "Matos Masei", "Nitzavim Vayeilech" };

    /**
	 * Retruns the list of transliterated parshiyos used by this formatter.
	 * 
	 * @return the list of transliterated Parshios
	 */
    public String[] getTransliteratedParshiosList() {
        return transliteratedParshios;
    }

    /**
	 * Setter method to allow overriding of the default list of parshiyos transliterated into into Latin chars. The
	 * default uses Ashkenazi American English transliteration.
	 * 
	 * @param transliteratedParshios
	 *            the transliterated Parshios to set
	 * @see #getTransliteratedParshiosList()
	 */
    public void setTransliteratedParshiosList(String[] transliteratedParshios) {
        this.transliteratedParshios = transliteratedParshios;
    }

    /**
	 * Unicode list of Hebrew parshiyos.
	 */
    private static final String[] hebrewParshiyos = { "בראשית", "נח", "לך לך", "וירא", "חיי שרה", "תולדות", "ויצא", "וישלח", "וישב", "מקץ", "ויגש", "ויחי", "שמות", "וארא", "בא", "בשלח", "יתרו", "משפטים", "תרומה", "תצוה", "כי תשא", "ויקהל", "פקודי", "ויקרא", "צו", "שמיני", "תזריע", "מצרע", "אחרי מות", "קדושים", "אמור", "בהר", "בחקתי", "במדבר", "נשא", "בהעלתך", "שלח לך", "קרח", "חוקת", "בלק", "פינחס", "מטות", "מסעי", "דברים", "ואתחנן", "עקב", "ראה", "שופטים", "כי תצא", "כי תבוא", "ניצבים", "וילך", "האזינו", "ויקהל פקודי", "תזריע מצרע", "אחרי מות קדושים", "בהר בחקתי", "חוקת בלק", "מטות מסעי", "ניצבים וילך" };

    /**
	 * Unicode list of Hebrew days of week.
	 */
    private static final String[] hebrewDaysOfWeek = { "ראשון", "שני", "שלישי", "רביעי", "חמישי", "ששי", "שבת" };

    /**
	 * Formats the day of week. If {@link #isHebrewFormat() Hebrew formatting} is set, it will display in the format
	 * &#x05E8;&#x05D0;&#x05E9;&#x05D5;&#x05DF; etc. If Hebrew formatting is not in use it will return it in the format
	 * of Sunday etc. There are various formatting options that will affect the output.
	 * 
	 * @param jewishDate
	 * @return the formatted day of week
	 * @see #isHebrewFormat()
	 * @see #isLongWeekFormat()
	 */
    public String formatDayOfWeek(JewishDate jewishDate) {
        if (hebrewFormat) {
            StringBuffer sb = new StringBuffer();
            sb.append(longWeekFormat ? hebrewDaysOfWeek[jewishDate.getDayOfWeek() - 1] : formatHebrewNumber(jewishDate.getDayOfWeek()));
            return sb.toString();
        } else {
            return jewishDate.getDayOfWeek() == 7 ? getTransliteratedShabbosDayOfWeek() : new SimpleDateFormat("EEEE").format(jewishDate.getTime());
        }
    }

    /**
	 * If the formatter is set to format in Hebrew, returns a string of the current parsha(ios) in Hebrew for example
	 * &#x05D1;&#x05E8;&#x05D0;&#x05E9;&#x05D9;&#x05EA; or &#x05E0;&#x05D9;&#x05E6;&#x05D1;&#x05D9;&#x05DD;
	 * &#x05D5;&#x05D9;&#x05DC;&#x05DA; or an empty string if there are none. If not set to Hebrew, it returns a string
	 * of the parsha(ios) transliterated into Latin chars. The default uses Ashkenazi pronunciation in typical American
	 * English spelling, for example Bereshis or Nitzavim Vayeilech or an empty string if there are none.
	 * 
	 * @param jewishCalendar
	 * @return today's parsha(ios) in Hebrew for example, if the formatter is set to format in Hebrew, returns a string
	 *         of the current parsha(ios) in Hebrew for example &#x05D1;&#x05E8;&#x05D0;&#x05E9;&#x05D9;&#x05EA; or
	 *         &#x05E0;&#x05D9;&#x05E6;&#x05D1;&#x05D9;&#x05DD; &#x05D5;&#x05D9;&#x05DC;&#x05DA; or an empty string if
	 *         there are none. If not set to Hebrew, it returns a string of the parsha(ios) transliterated into Latin
	 *         chars. The default uses Ashkenazi pronunciation in typical American English spelling, for example
	 *         Bereshis or Nitzavim Vayeilech or an empty string if there are none.
	 */
    public String formatParsha(JewishCalendar jewishCalendar) {
        int index = jewishCalendar.getParshaIndex();
        return index == -1 ? "" : hebrewFormat ? hebrewParshiyos[index] : transliteratedParshios[index];
    }

    /**
	 * Returns whether the class is set to use the Geresh &#x5F3; and Gershayim &#x5F4; in formatting Hebrew dates and
	 * numbers. When true and output would look like &#x5DB;&#x5F4;&#x5D0; &#x5E9;&#x5D1;&#x5D8;
	 * &#x5EA;&#x5E9;&#x5DA;&#x5F3;. When set to false, this output would display as &#x5DB&#x5D0; &#x5E9;&#x5D1;&#x5D8;
	 * &#x5EA;&#x5E9;&#x5DA;.
	 * 
	 * @return true if set to use the Geresh &#x5F3; and Gershayim &#x5F4; in formatting Hebrew dates and numbers.
	 */
    public boolean isUseGershGershayim() {
        return useGershGershayim;
    }

    /**
	 * Sets whether to use the Geresh &#x5F3; and Gershayim &#x5F4; in formatting Hebrew dates and numbers. The default
	 * value is true and output would look like &#x5DB;&#x5F4;&#x5D0; &#x5E9;&#x5D1;&#x5D8;
	 * &#x5EA;&#x5E9;&#x5DA;&#x5F3;. When set to false, this output would display as &#x5DB&#x5D0; &#x5E9;&#x5D1;&#x5D8;
	 * &#x5EA;&#x5E9;&#x5DA;.
	 * 
	 * @param useGershGershayim
	 *            set to false to omit the Geresh &#x5F3; and Gershayim &#x5F4; in formatting
	 */
    public void setUseGershGershayim(boolean useGershGershayim) {
        this.useGershGershayim = useGershGershayim;
    }

    /**
	 * Returns whether the class is set to use the thousands digit when formatting. When formatting a Hebrew Year,
	 * traditionally the thousands digit is omitted and output for a year such as 5729 (1969 Gregorian) would be
	 * calculated for 729 and format as &#x5EA;&#x5E9;&#x5DB;&#x5F4;&#x5D8;. When set to true the long format year such
	 * as &#x5D4;&#x5F3; &#x5EA;&#x5E9;&#x5DB;&#x5F4;&#x5D8; for 5729/1969 is returned.
	 * 
	 * @return true if set to use the the thousands digit when formatting Hebrew dates and numbers.
	 */
    public boolean isUseLongHebrewYears() {
        return useLonghebrewYears;
    }

    /**
	 * When formatting a Hebrew Year, traditionally the thousands digit is omitted and output for a year such as 5729
	 * (1969 Gregorian) would be calculated for 729 and format as &#x5EA;&#x5E9;&#x5DB;&#x5F4;&#x5D8;. This method
	 * allows setting this to true to return the long format year such as &#x5D4;&#x5F3;
	 * &#x5EA;&#x5E9;&#x5DB;&#x5F4;&#x5D8; for 5729/1969.
	 * 
	 * @param useLongHebrewYears
	 *            Set this to true to use the long formatting
	 */
    public void setUseLongHebrewYears(boolean useLongHebrewYears) {
        this.useLonghebrewYears = useLongHebrewYears;
    }

    /**
	 * Formats the Jewish date. If the formatter is set to Hebrew, it will format in the form, "day Month year" for
	 * example &#x5DB;&#x5F4;&#x5D0; &#x5E9;&#x5D1;&#x5D8; &#x5EA;&#x5E9;&#x5DB;&#x5F4;&#x5D8;, and the format
	 * "21 Shevat, 5729" if not.
	 * 
	 * @param jewishDate
	 *            the JewishDate to be formatted
	 * @return the formatted date. If the formatter is set to Hebrew, it will format in the form, "day Month year" for
	 *         example &#x5DB;&#x5F4;&#x5D0; &#x5E9;&#x5D1;&#x5D8; &#x5EA;&#x5E9;&#x5DB;&#x5F4;&#x5D8;, and the format
	 *         "21 Shevat, 5729" if not.
	 */
    public String format(JewishDate jewishDate) {
        if (isHebrewFormat()) {
            return formatHebrewNumber(jewishDate.getJewishDayOfMonth()) + " " + formatMonth(jewishDate) + " " + formatHebrewNumber(jewishDate.getJewishYear());
        } else {
            return jewishDate.getJewishDayOfMonth() + " " + formatMonth(jewishDate) + ", " + jewishDate.getJewishYear();
        }
    }

    /**
	 * Returns a string of the current Hebrew month such as "Tishrei". Returns a string of the current Hebrew month such
	 * as "&#x5D0;&#x5D3;&#x5E8; &#x5D1;&#x5F3;".
	 * 
	 * @param jewishDate
	 *            the JewishDate to format
	 * @return the formatted month name
	 * @see #isHebrewFormat()
	 * @see #setHebrewFormat(boolean)
	 * @see #getTransliteratedMonthList()
	 * @see #setTransliteratedMonthList(String[])
	 */
    public String formatMonth(JewishDate jewishDate) {
        final int month = jewishDate.getJewishMonth();
        if (isHebrewFormat()) {
            if (jewishDate.isJewishLeapYear() && month == JewishDate.ADAR) {
                return hebrewMonths[13] + (useGershGershayim ? GERESH : "");
            } else if (jewishDate.isJewishLeapYear() && month == JewishDate.ADAR_II) {
                return hebrewMonths[12] + (useGershGershayim ? GERESH : "");
            } else {
                return hebrewMonths[month - 1];
            }
        } else {
            if (jewishDate.isJewishLeapYear() && month == JewishDate.ADAR) {
                return transliteratedMonths[13];
            } else {
                return transliteratedMonths[month - 1];
            }
        }
    }

    /**
	 * Returns a String of the Omer day in the form &#x5DC;&#x5F4;&#x5D2; &#x5D1;&#x05E2;&#x05D5;&#x05DE;&#x5E8; if
	 * Hebrew Format is set, or "Omer X" or "Lag BaOmer" if not. An empty string if there is no Omer this day.
	 * 
	 * @return a String of the Omer day in the form or an empty string if there is no Omer this day. The default
	 *         formatting has a &#x5D1;&#x5F3; prefix that would output &#x5D1;&#x05E2;&#x05D5;&#x05DE;&#x5E8;, but this
	 *         can be set via the {@link #setHebrewOmerPrefix(String)} method to use a &#x5DC; and output
	 *         &#x5DC;&#x5F4;&#x5D2; &#x5DC;&#x05E2;&#x05D5;&#x05DE;&#x5E8;.
	 * @see #isHebrewFormat()
	 * @see #getHebrewOmerPrefix()
	 * @see #setHebrewOmerPrefix(String)
	 */
    public String formatOmer(JewishCalendar jewishCalendar) {
        int omer = jewishCalendar.getDayOfOmer();
        if (omer == -1) {
            return "";
        }
        if (hebrewFormat) {
            return formatHebrewNumber(omer) + " " + hebrewOmerPrefix + "עומר";
        } else {
            if (omer == 33) {
                return "Lag BaOmer";
            } else {
                return "Omer " + omer;
            }
        }
    }

    /**
	 * Experimental and incomplete
	 * 
	 * @param moladChalakim
	 * @return the formatted molad. FIXME: define proper format in English and Hebrew.
	 */
    private String formatMolad(long moladChalakim) {
        long adjustedChalakim = moladChalakim;
        int MINUTE_CHALAKIM = 18;
        int HOUR_CHALAKIM = 1080;
        int DAY_CHALAKIM = 24 * HOUR_CHALAKIM;
        long days = adjustedChalakim / DAY_CHALAKIM;
        adjustedChalakim = adjustedChalakim - (days * DAY_CHALAKIM);
        int hours = (int) ((adjustedChalakim / HOUR_CHALAKIM));
        if (hours >= 6) {
            days += 1;
        }
        adjustedChalakim = adjustedChalakim - (hours * HOUR_CHALAKIM);
        int minutes = (int) (adjustedChalakim / MINUTE_CHALAKIM);
        adjustedChalakim = adjustedChalakim - minutes * MINUTE_CHALAKIM;
        return "Day: " + days % 7 + " hours: " + hours + ", minutes " + minutes + ", chalakim: " + adjustedChalakim;
    }

    /**
	 * Returns the kviah in the traditional 3 letter Hebrew format where the first letter represents the day of week of
	 * Rosh Hashana, the second letter represents the lengths of Cheshvan and Kislev ({@link JewishDate#SHELAIMIM
	 * Shelaimim} , {@link JewishDate#KESIDRAN Kesidran} or {@link JewishDate#CHASERIM Chaserim}) and the 3rd letter
	 * represents the day of week of Pesach. For example 5729 (1969) would return &#x5D1;&#x5E9;&#x5D4; (Rosh Hashana on
	 * Monday, Shelaimim, and Pesach on Thursday), while 5771 (2011) would return &#x5D4;&#x5E9;&#x5D2; (Rosh Hashana on
	 * Thursday, Shelaimim, and Pesach on Tuesday).
	 * 
	 * @param jewishYear
	 *            the Jewish year
	 * @return the Hebrew String such as &#x5D1;&#x5E9;&#x5D4; for 5729 (1969) and &#x5D4;&#x5E9;&#x5D2; for 5771
	 *         (2011).
	 */
    public String getFormattedKviah(int jewishYear) {
        JewishDate jewishDate = new JewishDate(jewishYear, JewishDate.TISHREI, 1);
        int kviah = jewishDate.getCheshvanKislevKviah();
        int roshHashanaDayOfweek = jewishDate.getDayOfWeek();
        String returnValue = formatHebrewNumber(roshHashanaDayOfweek);
        returnValue += (kviah == JewishDate.CHASERIM ? "ח" : kviah == JewishDate.SHELAIMIM ? "ש" : "כ");
        jewishDate.setJewishDate(jewishYear, JewishDate.NISSAN, 15);
        int pesachDayOfweek = jewishDate.getDayOfWeek();
        returnValue += formatHebrewNumber(pesachDayOfweek);
        returnValue = returnValue.replaceAll(GERESH, "");
        return returnValue;
    }

    public String formatDafYomiBavli(Daf daf) {
        if (hebrewFormat) {
            return daf.getMasechta() + " " + formatHebrewNumber(daf.getDaf());
        } else {
            return daf.getMasechtaTransliterated() + " " + daf.getDaf();
        }
    }

    /**
	 * Returns a Hebrew formatted string of a number. The method can calculate from 0 - 9999.
	 * <ul>
	 * <li>Single digit numbers such as 3, 30 and 100 will be returned with a &#x5F3; (<a
	 * href="http://en.wikipedia.org/wiki/Geresh">Geresh</a>) appended as at the end. For example &#x5D2;&#x5F3;,
	 * &#x5DC;&#x5F3; and &#x5E7;&#x5F3;</li>
	 * <li>multi digit numbers such as 21 and 769 will be returned with a &#x5F4; (<a
	 * href="http://en.wikipedia.org/wiki/Gershayim">Gershayim</a>) between the second to last and last letters. For
	 * example &#x5DB;&#x5F4;&#x5D0;, &#x5EA;&#x5E9;&#x5DB;&#x5F4;&#x5D8;</li>
	 * <li>15 and 16 will be returned as &#x5D8;&#x5F4;&#x5D5; and &#x5D8;&#x5F4;&#x5D6;</li>
	 * <li>Single digit numbers (years assumed) such as 6000 (%1000=0) will be returned as &#x5D5;&#x5F3;
	 * &#x5D0;&#x5DC;&#x5E4;&#x5D9;&#x5DD;</li>
	 * <li>0 will return &#x5D0;&#x5E4;&#x05E1;</li>
	 * </ul>
	 * 
	 * @param number
	 *            the number to be formatted. It will trow an IllegalArgumentException if the number is < 0 or > 9999.
	 * @return the Hebrew formatted number such as &#x5EA;&#x5E9;&#x5DB;&#x5F4;&#x5D8;
	 * 
	 */
    public String formatHebrewNumber(int number) {
        if (number < 0) {
            throw new IllegalArgumentException("negative numbers can't be formatted");
        } else if (number > 9999) {
            throw new IllegalArgumentException("numbers > 9999 can't be formatted");
        }
        String ALAFIM = "אלפים";
        String EFES = "אפס";
        String[] jHundreds = new String[] { "", "ק", "ר", "ש", "ת", "תק", "תר", "תש", "תת", "תתק" };
        String[] jTens = new String[] { "", "י", "כ", "ל", "מ", "נ", "ס", "ע", "פ", "צ" };
        String[] jTenEnds = new String[] { "", "י", "ך", "ל", "ם", "ן", "ס", "ע", "ף", "ץ" };
        String[] tavTaz = new String[] { "טו", "טז" };
        String[] jOnes = new String[] { "", "א", "ב", "ג", "ד", "ה", "ו", "ז", "ח", "ט" };
        if (number == 0) {
            return EFES;
        }
        int shortNumber = number % 1000;
        boolean singleDigitNumber = (shortNumber < 11 || (shortNumber < 100 && shortNumber % 10 == 0) || (shortNumber <= 400 && shortNumber % 100 == 0));
        int thousands = number / 1000;
        StringBuffer sb = new StringBuffer();
        if (number % 1000 == 0) {
            sb.append(jOnes[thousands]);
            if (isUseGershGershayim()) {
                sb.append(GERESH);
            }
            sb.append(" ");
            sb.append(ALAFIM);
            return sb.toString();
        } else if (useLonghebrewYears && number >= 1000) {
            sb.append(jOnes[thousands]);
            if (isUseGershGershayim()) {
                sb.append(GERESH);
            }
            sb.append(" ");
        }
        number = number % 1000;
        int hundreds = number / 100;
        sb.append(jHundreds[hundreds]);
        number = number % 100;
        if (number == 15) {
            sb.append(tavTaz[0]);
        } else if (number == 16) {
            sb.append(tavTaz[1]);
        } else {
            int tens = number / 10;
            if (number % 10 == 0) {
                if (singleDigitNumber == false) {
                    sb.append(jTenEnds[tens]);
                } else {
                    sb.append(jTens[tens]);
                }
            } else {
                sb.append(jTens[tens]);
                number = number % 10;
                sb.append(jOnes[number]);
            }
        }
        if (isUseGershGershayim()) {
            if (singleDigitNumber == true) {
                sb.append(GERESH);
            } else {
                sb.insert(sb.length() - 1, GERSHAYIM);
            }
        }
        return sb.toString();
    }
}
