package org.bcholmes.jmicro.util.text;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.bcholmes.jmicro.util.PropertiesLoader;

class DateFormatSymbolFactory {

    enum Month {

        JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER
    }

    enum Weekday {

        SUNDAY(Calendar.SUNDAY), MONDAY(Calendar.MONDAY), TUESDAY(Calendar.TUESDAY), WEDNESDAY(Calendar.WEDNESDAY), THURSDAY(Calendar.THURSDAY), FRIDAY(Calendar.FRIDAY), SATURDAY(Calendar.SATURDAY);

        private final int weekday;

        private Weekday(int weekday) {
            this.weekday = weekday;
        }

        public int getWeekday() {
            return this.weekday;
        }
    }

    private Map<Locale, DateFormatSymbols> map = Collections.synchronizedMap(new HashMap<Locale, DateFormatSymbols>());

    public DateFormatSymbols create(Locale locale) {
        if (!this.map.containsKey(locale)) {
            this.map.put(locale, initializeSymbols(locale));
        }
        return this.map.get(locale);
    }

    private DateFormatSymbols initializeSymbols(Locale locale) {
        Properties properties = PropertiesLoader.loadProperties(getClass(), "dateSymbols", locale);
        if (properties.isEmpty()) {
            return new DateFormatSymbols(locale);
        } else {
            return processProperties(properties, locale);
        }
    }

    private DateFormatSymbols processProperties(Properties properties, Locale locale) {
        DateFormatSymbols symbols = new DateFormatSymbols(locale);
        symbols.setMonths(readMonths(properties));
        symbols.setWeekdays(readWeekdays(properties));
        symbols.setShortMonths(readShortMonths(properties));
        return symbols;
    }

    private String[] readMonths(Properties properties) {
        String[] months = new String[Month.values().length];
        for (Month month : Month.values()) {
            String name = properties.getProperty(month.name());
            months[month.ordinal()] = StringUtils.isEmpty(name) ? WordUtils.capitalizeFully(month.name()) : name;
        }
        return months;
    }

    private String[] readShortMonths(Properties properties) {
        String[] months = new String[Month.values().length];
        for (Month month : Month.values()) {
            String shortName = properties.getProperty(month.name() + "_SHORT");
            String name = properties.getProperty(month.name());
            if (StringUtils.isNotBlank(shortName)) {
                months[month.ordinal()] = shortName;
            } else if (StringUtils.isNotBlank(name)) {
                months[month.ordinal()] = StringUtils.left(name, 3);
            } else {
                months[month.ordinal()] = StringUtils.left(WordUtils.capitalizeFully(month.name()), 3);
            }
        }
        return months;
    }

    private String[] readWeekdays(Properties properties) {
        String[] weekdays = new String[Weekday.values().length + 1];
        weekdays[0] = "";
        for (Weekday weekday : Weekday.values()) {
            String name = properties.getProperty(weekday.name());
            weekdays[weekday.getWeekday()] = StringUtils.isEmpty(name) ? WordUtils.capitalizeFully(weekday.name()) : name;
        }
        return weekdays;
    }
}
