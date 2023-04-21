package com.jrefinery.ibd.io;

import antlr.TokenBuffer;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.ANTLRException;
import antlr.LLkParser;
import antlr.Token;
import antlr.TokenStream;
import antlr.RecognitionException;
import antlr.NoViableAltException;
import antlr.MismatchedTokenException;
import antlr.SemanticException;
import antlr.ParserSharedInputState;
import antlr.collections.impl.BitSet;
import antlr.collections.AST;
import antlr.ASTPair;
import antlr.collections.impl.ASTArray;
import java.util.*;
import com.jrefinery.common.date.*;
import com.jrefinery.finance.*;
import com.jrefinery.finance.data.reference.*;

public class CalendarRuleParser extends antlr.LLkParser implements CalendarRuleParserTokenTypes {

    BusinessDayCalendar calendar;

    List holidays;

    SerialDates serialDates = new SerialDates();

    protected CalendarRuleParser(TokenBuffer tokenBuf, int k) {
        super(tokenBuf, k);
        tokenNames = _tokenNames;
    }

    public CalendarRuleParser(TokenBuffer tokenBuf) {
        this(tokenBuf, 3);
    }

    protected CalendarRuleParser(TokenStream lexer, int k) {
        super(lexer, k);
        tokenNames = _tokenNames;
    }

    public CalendarRuleParser(TokenStream lexer) {
        this(lexer, 3);
    }

    public CalendarRuleParser(ParserSharedInputState state) {
        super(state, 3);
        tokenNames = _tokenNames;
    }

    public final void readCalendarsIntoModel(BusinessDayCalendarsToModel model) throws RecognitionException, TokenStreamException {
        BusinessDayCalendar cal = null;
        {
            _loop3: do {
                if ((LA(1) == STRING)) {
                    cal = getCalendar();
                    try {
                        model.addBusinessDayCalendar(cal);
                    } catch (ModelException e) {
                        e.printStackTrace();
                    }
                } else {
                    break _loop3;
                }
            } while (true);
        }
    }

    public final BusinessDayCalendar getCalendar() throws RecognitionException, TokenStreamException {
        BusinessDayCalendar cal = null;
        Token name = null;
        calendar = new BusinessDayCalendar("No name yet");
        Weekend weekend = null;
        name = LT(1);
        match(STRING);
        match(COMMA);
        weekend = getWeekend();
        match(LEFTBRACE);
        holidays = getHolidays();
        match(RIGHTBRACE);
        calendar.setName(name.getText());
        calendar.setWeekend(weekend);
        calendar.setHolidays(holidays);
        cal = calendar;
        return cal;
    }

    public final Weekend getWeekend() throws RecognitionException, TokenStreamException {
        Weekend weekend = null;
        match(LEFTBRACKET);
        weekend = getWeekdays();
        match(RIGHTBRACKET);
        return weekend;
    }

    public final List getHolidays() throws RecognitionException, TokenStreamException {
        List h = null;
        holidays = new ArrayList();
        Holiday holiday = null;
        {
            _loop13: do {
                if ((LA(1) == ONE || LA(1) == ANNUAL)) {
                    holiday = getHoliday();
                    holidays.add(holiday);
                    {
                        switch(LA(1)) {
                            case SEMICOLON:
                                {
                                    match(SEMICOLON);
                                    break;
                                }
                            case RIGHTBRACE:
                            case ONE:
                            case ANNUAL:
                                {
                                    break;
                                }
                            default:
                                {
                                    throw new NoViableAltException(LT(1), getFilename());
                                }
                        }
                    }
                } else {
                    break _loop13;
                }
            } while (true);
        }
        h = holidays;
        return h;
    }

    public final Weekend getWeekdays() throws RecognitionException, TokenStreamException {
        Weekend weekend;
        int d = 0;
        weekend = new Weekend();
        weekend.set(SerialDate.SATURDAY, false);
        weekend.set(SerialDate.SUNDAY, false);
        {
            switch(LA(1)) {
                case IDENTIFIER:
                    {
                        d = getWeekday();
                        weekend.set(d, true);
                        break;
                    }
                case COMMA:
                case RIGHTBRACKET:
                    {
                        break;
                    }
                default:
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        }
        {
            _loop9: do {
                if ((LA(1) == COMMA)) {
                    match(COMMA);
                    d = getWeekday();
                    weekend.set(d, true);
                } else {
                    break _loop9;
                }
            } while (true);
        }
        return weekend;
    }

    public final int getWeekday() throws RecognitionException, TokenStreamException {
        int day = SerialDate.MONDAY;
        Token id = null;
        id = LT(1);
        match(IDENTIFIER);
        day = serialDates.stringToWeekday(id.getText());
        return day;
    }

    public final Holiday getHoliday() throws RecognitionException, TokenStreamException {
        Holiday holiday = null;
        switch(LA(1)) {
            case ONE:
                {
                    match(ONE);
                    match(OFF);
                    match(HOLIDAY);
                    match(LEFTBRACKET);
                    holiday = getOneOffHolidayParameters();
                    match(RIGHTBRACKET);
                    break;
                }
            case ANNUAL:
                {
                    match(ANNUAL);
                    match(HOLIDAY);
                    match(LEFTBRACKET);
                    holiday = getAnnualHolidayParameters();
                    match(RIGHTBRACKET);
                    break;
                }
            default:
                {
                    throw new NoViableAltException(LT(1), getFilename());
                }
        }
        return holiday;
    }

    public final OneOffHoliday getOneOffHolidayParameters() throws RecognitionException, TokenStreamException {
        OneOffHoliday holiday = null;
        Token name = null;
        SerialDate date = null;
        boolean adjusted = false;
        boolean doubleHoliday = false;
        name = LT(1);
        match(STRING);
        match(COMMA);
        date = getDate();
        match(COMMA);
        adjusted = getAdjusted();
        {
            switch(LA(1)) {
                case COMMA:
                    {
                        match(COMMA);
                        doubleHoliday = getDoubleHoliday();
                        break;
                    }
                case RIGHTBRACKET:
                    {
                        break;
                    }
                default:
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        }
        holiday = new OneOffHoliday(name.getText(), date, null, adjusted, doubleHoliday);
        return holiday;
    }

    public final AnnualHoliday getAnnualHolidayParameters() throws RecognitionException, TokenStreamException {
        AnnualHoliday holiday = null;
        Token name = null;
        AnnualDateRule subrule = null;
        boolean adjusted = false;
        boolean doubleHoliday = false;
        name = LT(1);
        match(STRING);
        match(COMMA);
        subrule = getAnnualDateRule();
        match(COMMA);
        adjusted = getAdjusted();
        {
            switch(LA(1)) {
                case COMMA:
                    {
                        match(COMMA);
                        doubleHoliday = getDoubleHoliday();
                        break;
                    }
                case RIGHTBRACKET:
                    {
                        break;
                    }
                default:
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        }
        holiday = new AnnualHoliday(name.getText(), 0, calendar, subrule, adjusted, doubleHoliday);
        return holiday;
    }

    public final SerialDate getDate() throws RecognitionException, TokenStreamException {
        SerialDate date = null;
        Token d = null;
        Token y = null;
        int day = 0;
        int month = 0;
        int yyyy = 0;
        d = LT(1);
        match(INT);
        match(DASH);
        month = getMonth();
        match(DASH);
        y = LT(1);
        match(INT);
        day = Integer.parseInt(d.getText());
        yyyy = Integer.parseInt(y.getText());
        date = SerialDate.createInstance(day, month, yyyy);
        return date;
    }

    public final boolean getAdjusted() throws RecognitionException, TokenStreamException {
        boolean adjusted = false;
        switch(LA(1)) {
            case ADJUSTED:
                {
                    match(ADJUSTED);
                    adjusted = true;
                    break;
                }
            case NOT:
                {
                    match(NOT);
                    match(ADJUSTED);
                    adjusted = false;
                    break;
                }
            default:
                {
                    throw new NoViableAltException(LT(1), getFilename());
                }
        }
        return adjusted;
    }

    public final boolean getDoubleHoliday() throws RecognitionException, TokenStreamException {
        boolean doubleHoliday = false;
        switch(LA(1)) {
            case DOUBLE:
                {
                    match(DOUBLE);
                    match(HOLIDAY);
                    doubleHoliday = true;
                    break;
                }
            case RIGHTBRACKET:
                {
                    break;
                }
            default:
                {
                    throw new NoViableAltException(LT(1), getFilename());
                }
        }
        return doubleHoliday;
    }

    public final int getMonth() throws RecognitionException, TokenStreamException {
        int month = SerialDate.JANUARY;
        switch(LA(1)) {
            case JANUARY:
                {
                    match(JANUARY);
                    month = SerialDate.JANUARY;
                    break;
                }
            case FEBRUARY:
                {
                    match(FEBRUARY);
                    month = SerialDate.FEBRUARY;
                    break;
                }
            case MARCH:
                {
                    match(MARCH);
                    month = SerialDate.MARCH;
                    break;
                }
            case APRIL:
                {
                    match(APRIL);
                    month = SerialDate.APRIL;
                    break;
                }
            case MAY:
                {
                    match(MAY);
                    month = SerialDate.MAY;
                    break;
                }
            case JUNE:
                {
                    match(JUNE);
                    month = SerialDate.JUNE;
                    break;
                }
            case JULY:
                {
                    match(JULY);
                    month = SerialDate.JULY;
                    break;
                }
            case AUGUST:
                {
                    match(AUGUST);
                    month = SerialDate.AUGUST;
                    break;
                }
            case SEPTEMBER:
                {
                    match(SEPTEMBER);
                    month = SerialDate.SEPTEMBER;
                    break;
                }
            case OCTOBER:
                {
                    match(OCTOBER);
                    month = SerialDate.OCTOBER;
                    break;
                }
            case NOVEMBER:
                {
                    match(NOVEMBER);
                    month = SerialDate.NOVEMBER;
                    break;
                }
            case DECEMBER:
                {
                    match(DECEMBER);
                    month = SerialDate.DECEMBER;
                    break;
                }
            default:
                {
                    throw new NoViableAltException(LT(1), getFilename());
                }
        }
        return month;
    }

    public final AnnualDateRule getAnnualDateRule() throws RecognitionException, TokenStreamException {
        AnnualDateRule rule = null;
        Token day = null;
        int m1;
        switch(LA(1)) {
            case WEEKDAY:
                {
                    match(WEEKDAY);
                    match(RULE);
                    match(LEFTBRACKET);
                    rule = getRelativeDayOfWeekRuleParameters();
                    match(RIGHTBRACKET);
                    break;
                }
            case EASTER:
                {
                    match(EASTER);
                    match(RULE);
                    match(LEFTBRACKET);
                    match(RIGHTBRACKET);
                    rule = new EasterSundayRule();
                    break;
                }
            default:
                if ((LA(1) == DAY) && (LA(2) == AND)) {
                    match(DAY);
                    match(AND);
                    match(LITERAL_Month);
                    match(RULE);
                    match(LEFTBRACKET);
                    day = LT(1);
                    match(INT);
                    match(COMMA);
                    m1 = getMonth();
                    match(RIGHTBRACKET);
                    rule = new DayAndMonthRule(Integer.parseInt(day.getText()), m1);
                } else if ((LA(1) == DAY) && (LA(2) == IN)) {
                    match(DAY);
                    match(IN);
                    match(LITERAL_Month);
                    match(RULE);
                    match(LEFTBRACKET);
                    rule = getDayOfWeekInMonthRuleParameters();
                    match(RIGHTBRACKET);
                } else {
                    throw new NoViableAltException(LT(1), getFilename());
                }
        }
        return rule;
    }

    public final AnnualDateRule getRelativeDayOfWeekRuleParameters() throws RecognitionException, TokenStreamException {
        AnnualDateRule rule = null;
        AnnualDateRule subrule;
        int d1 = 0;
        int r1;
        subrule = getAnnualDateRule();
        match(COMMA);
        d1 = getWeekday();
        match(COMMA);
        r1 = getRelative();
        rule = new RelativeDayOfWeekRule(subrule, d1, r1);
        return rule;
    }

    public final DayOfWeekInMonthRule getDayOfWeekInMonthRuleParameters() throws RecognitionException, TokenStreamException {
        DayOfWeekInMonthRule rule = null;
        int c = 0;
        int d = 0;
        int m = 0;
        c = getCount();
        match(COMMA);
        d = getWeekday();
        match(COMMA);
        m = getMonth();
        rule = new DayOfWeekInMonthRule(c, d, m);
        return rule;
    }

    public final int getRelative() throws RecognitionException, TokenStreamException {
        int relative = 0;
        switch(LA(1)) {
            case PRECEDING:
                {
                    match(PRECEDING);
                    relative = SerialDate.PRECEDING;
                    break;
                }
            case NEAREST:
                {
                    match(NEAREST);
                    relative = SerialDate.NEAREST;
                    break;
                }
            case FOLLOWING:
                {
                    match(FOLLOWING);
                    relative = SerialDate.FOLLOWING;
                    break;
                }
            default:
                {
                    throw new NoViableAltException(LT(1), getFilename());
                }
        }
        return relative;
    }

    public final int getCount() throws RecognitionException, TokenStreamException {
        int count = 0;
        switch(LA(1)) {
            case FIRST:
                {
                    match(FIRST);
                    count = SerialDate.FIRST;
                    break;
                }
            case SECOND:
                {
                    match(SECOND);
                    count = SerialDate.SECOND;
                    break;
                }
            case THIRD:
                {
                    match(THIRD);
                    count = SerialDate.THIRD;
                    break;
                }
            case FOURTH:
                {
                    match(FOURTH);
                    count = SerialDate.FOURTH;
                    break;
                }
            case LAST:
                {
                    match(LAST);
                    count = SerialDate.LAST;
                    break;
                }
            default:
                {
                    throw new NoViableAltException(LT(1), getFilename());
                }
        }
        return count;
    }

    public static final String[] _tokenNames = { "<0>", "EOF", "<2>", "NULL_TREE_LOOKAHEAD", "STRING", "COMMA", "LEFTBRACE", "RIGHTBRACE", "LEFTBRACKET", "RIGHTBRACKET", "SEMICOLON", "ONE", "OFF", "HOLIDAY", "ANNUAL", "INT", "DASH", "DAY", "AND", "\"Month\"", "RULE", "WEEKDAY", "IN", "EASTER", "JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER", "IDENTIFIER", "PRECEDING", "NEAREST", "FOLLOWING", "FIRST", "SECOND", "THIRD", "FOURTH", "LAST", "ADJUSTED", "NOT", "DOUBLE", "COMMENT", "DIGIT", "WS" };
}
