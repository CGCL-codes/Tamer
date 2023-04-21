package com.fh.auge.internal;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.Currency;
import com.domainlanguage.money.Money;
import com.domainlanguage.time.CalendarDate;
import com.domainlanguage.time.TimePoint;

public class FormatHolder {

    private static FormatHolder instance = null;

    private DecimalFormat moneyInputFormat = new DecimalFormat("###,##0.00");

    private Format moneyFormat = new Format() {

        private static final long serialVersionUID = 1L;

        public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
            Money m = (Money) obj;
            return toAppendTo.append(toString(m));
        }

        @Override
        public Object parseObject(String source, ParsePosition pos) {
            return null;
        }

        private String toString(Money m) {
            if (m == null) return "";
            String symbol = getCurrencySymbol(m.breachEncapsulationOfCurrency());
            return moneyInputFormat.format(m.breachEncapsulationOfAmount()) + " " + symbol;
        }
    };

    private DateFormat dateFormat = DateFormat.getDateInstance();

    private DecimalFormat percentFormat = new DecimalFormat("####0.00%");

    public Format getMoneyFormat() {
        return moneyFormat;
    }

    public DateFormat getDateFormat() {
        return dateFormat;
    }

    public DecimalFormat getPercentFormat() {
        return percentFormat;
    }

    public static FormatHolder getInstance() {
        if (instance == null) {
            instance = new FormatHolder();
        }
        return instance;
    }

    public void setMoneyFormat(Format moneyFormat) {
        this.moneyFormat = moneyFormat;
    }

    public void setDateFormat(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public void setPercentFormat(DecimalFormat percentFormat) {
        this.percentFormat = percentFormat;
    }

    public DecimalFormat getMoneyInputFormat() {
        return moneyInputFormat;
    }

    public void setMoneyInputFormat(DecimalFormat moneyInputFormat) {
        this.moneyInputFormat = moneyInputFormat;
    }

    public String getCurrencySymbol(Currency currency) {
        return (currency.getSymbol().equals("USD")) ? "$" : currency.getSymbol();
    }

    public String format(CalendarDate start) {
        return getDateFormat().format(TimePoint.atMidnightGMT(start.breachEncapsulationOf_year(), start.breachEncapsulationOf_month(), start.breachEncapsulationOf_day()).asJavaUtilDate());
    }
}
