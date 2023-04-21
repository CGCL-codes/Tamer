package org.sti2.elly.basics;

import org.deri.iris.builtins.EqualBuiltin;
import org.deri.iris.builtins.ExactEqualBuiltin;
import org.deri.iris.builtins.GreaterBuiltin;
import org.deri.iris.builtins.GreaterEqualBuiltin;
import org.deri.iris.builtins.LessBuiltin;
import org.deri.iris.builtins.LessEqualBuiltin;
import org.deri.iris.builtins.NotEqualBuiltin;
import org.deri.iris.builtins.NotExactEqualBuiltin;
import org.deri.iris.builtins.datatype.IsBase64BinaryBuiltin;
import org.deri.iris.builtins.datatype.IsBooleanBuiltin;
import org.deri.iris.builtins.datatype.IsDatatypeBuiltin;
import org.deri.iris.builtins.datatype.IsDateBuiltin;
import org.deri.iris.builtins.datatype.IsDateTimeBuiltin;
import org.deri.iris.builtins.datatype.IsDayTimeDurationBuiltin;
import org.deri.iris.builtins.datatype.IsDecimalBuiltin;
import org.deri.iris.builtins.datatype.IsDoubleBuiltin;
import org.deri.iris.builtins.datatype.IsDurationBuiltin;
import org.deri.iris.builtins.datatype.IsFloatBuiltin;
import org.deri.iris.builtins.datatype.IsGDayBuiltin;
import org.deri.iris.builtins.datatype.IsGMonthBuiltin;
import org.deri.iris.builtins.datatype.IsGYearBuiltin;
import org.deri.iris.builtins.datatype.IsGYearMonthBuiltin;
import org.deri.iris.builtins.datatype.IsHexBinaryBuiltin;
import org.deri.iris.builtins.datatype.IsIntegerBuiltin;
import org.deri.iris.builtins.datatype.IsIriBuiltin;
import org.deri.iris.builtins.datatype.IsNotDatatypeBuiltin;
import org.deri.iris.builtins.datatype.IsStringBuiltin;
import org.deri.iris.builtins.datatype.IsPlainLiteralBuiltin;
import org.deri.iris.builtins.datatype.IsTimeBuiltin;
import org.deri.iris.builtins.datatype.IsXMLLiteralBuiltin;
import org.deri.iris.builtins.datatype.IsYearMonthDurationBuiltin;
import org.deri.iris.builtins.datatype.ToBase64Builtin;
import org.deri.iris.builtins.datatype.ToBooleanBuiltin;
import org.deri.iris.builtins.datatype.ToDateBuiltin;
import org.deri.iris.builtins.datatype.ToDateTimeBuiltin;
import org.deri.iris.builtins.datatype.ToDayTimeDurationBuiltin;
import org.deri.iris.builtins.datatype.ToDecimalBuiltin;
import org.deri.iris.builtins.datatype.ToDoubleBuiltin;
import org.deri.iris.builtins.datatype.ToDurationBuiltin;
import org.deri.iris.builtins.datatype.ToFloatBuiltin;
import org.deri.iris.builtins.datatype.ToGDayBuiltin;
import org.deri.iris.builtins.datatype.ToGMonthBuiltin;
import org.deri.iris.builtins.datatype.ToGMonthDayBuiltin;
import org.deri.iris.builtins.datatype.ToGYearBuiltin;
import org.deri.iris.builtins.datatype.ToGYearMonthBuiltin;
import org.deri.iris.builtins.datatype.ToHexBinaryBuiltin;
import org.deri.iris.builtins.datatype.ToIntegerBuiltin;
import org.deri.iris.builtins.datatype.ToIriBuiltin;
import org.deri.iris.builtins.datatype.ToStringBuiltin;
import org.deri.iris.builtins.datatype.ToPlainLiteralBuiltin;
import org.deri.iris.builtins.datatype.ToTimeBuiltin;
import org.deri.iris.builtins.datatype.ToXMLLiteralBuiltin;
import org.deri.iris.builtins.datatype.ToYearMonthDurationBuiltin;
import org.deri.iris.builtins.date.DayPartBuiltin;
import org.deri.iris.builtins.date.HourPartBuiltin;
import org.deri.iris.builtins.date.MinutePartBuiltin;
import org.deri.iris.builtins.date.MonthPartBuiltin;
import org.deri.iris.builtins.date.SecondPartBuiltin;
import org.deri.iris.builtins.date.TimezonePartBuiltin;
import org.deri.iris.builtins.date.YearPartBuiltin;
import org.deri.iris.builtins.string.LangFromPlainLiteralBuiltin;
import org.deri.iris.builtins.string.StringContainsWithoutCollationBuiltin;
import org.deri.iris.builtins.string.StringEndsWithWithoutCollationBuiltin;
import org.deri.iris.builtins.string.StringEscapeHtmlUriBuiltin;
import org.deri.iris.builtins.string.StringFromPlainLiteralBuiltin;
import org.deri.iris.builtins.string.StringIriToUriBuiltin;
import org.deri.iris.builtins.string.StringLengthBuiltin;
import org.deri.iris.builtins.string.StringMatchesWithoutFlagsBuiltin;
import org.deri.iris.builtins.string.StringStartsWithWithoutCollationBuiltin;
import org.deri.iris.builtins.string.StringToLowerBuiltin;
import org.deri.iris.builtins.string.StringToUpperBuiltin;
import org.deri.iris.builtins.string.StringUriEncodeBuiltin;
import org.deri.iris.builtins.string.PlainLiteralFromStringBuiltin;
import org.sti2.elly.api.basics.IBuiltinAtom;
import org.sti2.elly.api.factory.IBuiltinsFactory;
import org.sti2.elly.api.terms.ITerm;

/**
 * Factory to create IRIS built-ins.
 */
public class BuiltinsFactory implements IBuiltinsFactory {

    private static final IBuiltinsFactory INSTANCE = new BuiltinsFactory();

    private BuiltinsFactory() {
    }

    /**
	 * Returns the singelton instance of this factory.
	 * 
	 * @return a instane of this factory
	 */
    public static IBuiltinsFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public IBuiltinAtom createEqual(ITerm t0, ITerm t1) {
        return BuiltInAtom.fromIRISBuiltinClass(EqualBuiltin.class, new Tuple(t0, t1));
    }

    @Override
    public IBuiltinAtom createGreater(ITerm t0, ITerm t1) {
        return BuiltInAtom.fromIRISBuiltinClass(GreaterBuiltin.class, new Tuple(t0, t1));
    }

    @Override
    public IBuiltinAtom createGreaterEqual(ITerm t0, ITerm t1) {
        return BuiltInAtom.fromIRISBuiltinClass(GreaterEqualBuiltin.class, new Tuple(t0, t1));
    }

    @Override
    public IBuiltinAtom createLess(ITerm t0, ITerm t1) {
        return BuiltInAtom.fromIRISBuiltinClass(LessBuiltin.class, new Tuple(t0, t1));
    }

    @Override
    public IBuiltinAtom createLessEqual(ITerm t0, ITerm t1) {
        return BuiltInAtom.fromIRISBuiltinClass(LessEqualBuiltin.class, new Tuple(t0, t1));
    }

    @Override
    public IBuiltinAtom createUnequal(ITerm t0, ITerm t1) {
        return BuiltInAtom.fromIRISBuiltinClass(NotEqualBuiltin.class, new Tuple(t0, t1));
    }

    @Override
    public IBuiltinAtom createExactEqual(final ITerm t0, final ITerm t1) {
        return BuiltInAtom.fromIRISBuiltinClass(ExactEqualBuiltin.class, new Tuple(t0, t1));
    }

    @Override
    public IBuiltinAtom createNotExactEqual(final ITerm t0, final ITerm t1) {
        return BuiltInAtom.fromIRISBuiltinClass(NotExactEqualBuiltin.class, new Tuple(t0, t1));
    }

    @Override
    public IBuiltinAtom createIsDatatype(final ITerm t0, final ITerm t1) {
        return BuiltInAtom.fromIRISBuiltinClass(IsDatatypeBuiltin.class, new Tuple(t0, t1));
    }

    @Override
    public IBuiltinAtom createIsNotDatatype(final ITerm t0, final ITerm t1) {
        return BuiltInAtom.fromIRISBuiltinClass(IsNotDatatypeBuiltin.class, new Tuple(t0, t1));
    }

    @Override
    public IBuiltinAtom createStringLength(final ITerm t0, final ITerm t1) {
        return BuiltInAtom.fromIRISBuiltinClass(StringLengthBuiltin.class, new Tuple(t0, t1));
    }

    @Override
    public IBuiltinAtom createStringToUpper(final ITerm t0, final ITerm t1) {
        return BuiltInAtom.fromIRISBuiltinClass(StringToUpperBuiltin.class, new Tuple(t0, t1));
    }

    @Override
    public IBuiltinAtom createStringToLower(final ITerm t0, final ITerm t1) {
        return BuiltInAtom.fromIRISBuiltinClass(StringToLowerBuiltin.class, new Tuple(t0, t1));
    }

    @Override
    public IBuiltinAtom createStringUriEncode(final ITerm t0, final ITerm t1) {
        return BuiltInAtom.fromIRISBuiltinClass(StringUriEncodeBuiltin.class, new Tuple(t0, t1));
    }

    @Override
    public IBuiltinAtom createStringIriToUri(final ITerm t0, final ITerm t1) {
        return BuiltInAtom.fromIRISBuiltinClass(StringIriToUriBuiltin.class, new Tuple(t0, t1));
    }

    @Override
    public IBuiltinAtom createStringEscapeHtmlUri(final ITerm t0, final ITerm t1) {
        return BuiltInAtom.fromIRISBuiltinClass(StringEscapeHtmlUriBuiltin.class, new Tuple(t0, t1));
    }

    @Override
    public IBuiltinAtom createStringContains(final ITerm t0, final ITerm t1) {
        return BuiltInAtom.fromIRISBuiltinClass(StringContainsWithoutCollationBuiltin.class, new Tuple(t0, t1));
    }

    @Override
    public IBuiltinAtom createStringStartsWith(final ITerm t0, final ITerm t1) {
        return BuiltInAtom.fromIRISBuiltinClass(StringStartsWithWithoutCollationBuiltin.class, new Tuple(t0, t1));
    }

    @Override
    public IBuiltinAtom createStringEndsWith(final ITerm t0, final ITerm t1) {
        return BuiltInAtom.fromIRISBuiltinClass(StringEndsWithWithoutCollationBuiltin.class, new Tuple(t0, t1));
    }

    @Override
    public IBuiltinAtom createStringMatches(final ITerm t0, final ITerm t1) {
        return BuiltInAtom.fromIRISBuiltinClass(StringMatchesWithoutFlagsBuiltin.class, new Tuple(t0, t1));
    }

    @Override
    public IBuiltinAtom createYearPart(final ITerm t0, final ITerm t1) {
        return BuiltInAtom.fromIRISBuiltinClass(YearPartBuiltin.class, new Tuple(t0, t1));
    }

    @Override
    public IBuiltinAtom createMonthPart(final ITerm t0, final ITerm t1) {
        return BuiltInAtom.fromIRISBuiltinClass(MonthPartBuiltin.class, new Tuple(t0, t1));
    }

    @Override
    public IBuiltinAtom createDayPart(final ITerm t0, final ITerm t1) {
        return BuiltInAtom.fromIRISBuiltinClass(DayPartBuiltin.class, new Tuple(t0, t1));
    }

    @Override
    public IBuiltinAtom createHourPart(final ITerm t0, final ITerm t1) {
        return BuiltInAtom.fromIRISBuiltinClass(HourPartBuiltin.class, new Tuple(t0, t1));
    }

    @Override
    public IBuiltinAtom createMinutePart(final ITerm t0, final ITerm t1) {
        return BuiltInAtom.fromIRISBuiltinClass(MinutePartBuiltin.class, new Tuple(t0, t1));
    }

    @Override
    public IBuiltinAtom createSecondPart(final ITerm t0, final ITerm t1) {
        return BuiltInAtom.fromIRISBuiltinClass(SecondPartBuiltin.class, new Tuple(t0, t1));
    }

    @Override
    public IBuiltinAtom createTimezonePart(final ITerm t0, final ITerm t1) {
        return BuiltInAtom.fromIRISBuiltinClass(TimezonePartBuiltin.class, new Tuple(t0, t1));
    }

    @Override
    public IBuiltinAtom createStringFromText(final ITerm t0, final ITerm t1) {
        return BuiltInAtom.fromIRISBuiltinClass(StringFromPlainLiteralBuiltin.class, new Tuple(t0, t1));
    }

    @Override
    public IBuiltinAtom createLangFromText(final ITerm t0, final ITerm t1) {
        return BuiltInAtom.fromIRISBuiltinClass(LangFromPlainLiteralBuiltin.class, new Tuple(t0, t1));
    }

    @Override
    public IBuiltinAtom createTextFromString(final ITerm t0, final ITerm t1) {
        return BuiltInAtom.fromIRISBuiltinClass(PlainLiteralFromStringBuiltin.class, new Tuple(t0, t1));
    }

    @Override
    public IBuiltinAtom createToBase64Binary(final ITerm t0, final ITerm t1) {
        return BuiltInAtom.fromIRISBuiltinClass(ToBase64Builtin.class, new Tuple(t0, t1));
    }

    @Override
    public IBuiltinAtom createToBoolean(final ITerm t0, final ITerm t1) {
        return BuiltInAtom.fromIRISBuiltinClass(ToBooleanBuiltin.class, new Tuple(t0, t1));
    }

    @Override
    public IBuiltinAtom createToDate(final ITerm t0, final ITerm t1) {
        return BuiltInAtom.fromIRISBuiltinClass(ToDateBuiltin.class, new Tuple(t0, t1));
    }

    @Override
    public IBuiltinAtom createToDateTime(final ITerm t0, final ITerm t1) {
        return BuiltInAtom.fromIRISBuiltinClass(ToDateTimeBuiltin.class, new Tuple(t0, t1));
    }

    @Override
    public IBuiltinAtom createToDayTimeDuration(final ITerm t0, final ITerm t1) {
        return BuiltInAtom.fromIRISBuiltinClass(ToDayTimeDurationBuiltin.class, new Tuple(t0, t1));
    }

    @Override
    public IBuiltinAtom createToDecimal(final ITerm t0, final ITerm t1) {
        return BuiltInAtom.fromIRISBuiltinClass(ToDecimalBuiltin.class, new Tuple(t0, t1));
    }

    @Override
    public IBuiltinAtom createToDouble(final ITerm t0, final ITerm t1) {
        return BuiltInAtom.fromIRISBuiltinClass(ToDoubleBuiltin.class, new Tuple(t0, t1));
    }

    @Override
    public IBuiltinAtom createToDuration(final ITerm t0, final ITerm t1) {
        return BuiltInAtom.fromIRISBuiltinClass(ToDurationBuiltin.class, new Tuple(t0, t1));
    }

    @Override
    public IBuiltinAtom createToFloat(final ITerm t0, final ITerm t1) {
        return BuiltInAtom.fromIRISBuiltinClass(ToFloatBuiltin.class, new Tuple(t0, t1));
    }

    @Override
    public IBuiltinAtom createToGDay(final ITerm t0, final ITerm t1) {
        return BuiltInAtom.fromIRISBuiltinClass(ToGDayBuiltin.class, new Tuple(t0, t1));
    }

    @Override
    public IBuiltinAtom createToGMonth(final ITerm t0, final ITerm t1) {
        return BuiltInAtom.fromIRISBuiltinClass(ToGMonthBuiltin.class, new Tuple(t0, t1));
    }

    @Override
    public IBuiltinAtom createToGMonthDay(final ITerm t0, final ITerm t1) {
        return BuiltInAtom.fromIRISBuiltinClass(ToGMonthDayBuiltin.class, new Tuple(t0, t1));
    }

    @Override
    public IBuiltinAtom createToGYear(final ITerm t0, final ITerm t1) {
        return BuiltInAtom.fromIRISBuiltinClass(ToGYearBuiltin.class, new Tuple(t0, t1));
    }

    @Override
    public IBuiltinAtom createToGYearMonth(final ITerm t0, final ITerm t1) {
        return BuiltInAtom.fromIRISBuiltinClass(ToGYearMonthBuiltin.class, new Tuple(t0, t1));
    }

    @Override
    public IBuiltinAtom createToHexBinary(final ITerm t0, final ITerm t1) {
        return BuiltInAtom.fromIRISBuiltinClass(ToHexBinaryBuiltin.class, new Tuple(t0, t1));
    }

    @Override
    public IBuiltinAtom createToInteger(final ITerm t0, final ITerm t1) {
        return BuiltInAtom.fromIRISBuiltinClass(ToIntegerBuiltin.class, new Tuple(t0, t1));
    }

    @Override
    public IBuiltinAtom createToIRI(final ITerm t0, final ITerm t1) {
        return BuiltInAtom.fromIRISBuiltinClass(ToIriBuiltin.class, new Tuple(t0, t1));
    }

    @Override
    public IBuiltinAtom createToString(final ITerm t0, final ITerm t1) {
        return BuiltInAtom.fromIRISBuiltinClass(ToStringBuiltin.class, new Tuple(t0, t1));
    }

    @Override
    public IBuiltinAtom createToText(final ITerm t0, final ITerm t1) {
        return BuiltInAtom.fromIRISBuiltinClass(ToPlainLiteralBuiltin.class, new Tuple(t0, t1));
    }

    @Override
    public IBuiltinAtom createToTime(final ITerm t0, final ITerm t1) {
        return BuiltInAtom.fromIRISBuiltinClass(ToTimeBuiltin.class, new Tuple(t0, t1));
    }

    @Override
    public IBuiltinAtom createToXMLLiteral(final ITerm t0, final ITerm t1) {
        return BuiltInAtom.fromIRISBuiltinClass(ToXMLLiteralBuiltin.class, new Tuple(t0, t1));
    }

    @Override
    public IBuiltinAtom createToYearMonthDuration(final ITerm t0, final ITerm t1) {
        return BuiltInAtom.fromIRISBuiltinClass(ToYearMonthDurationBuiltin.class, new Tuple(t0, t1));
    }

    @Override
    public IBuiltinAtom createIsBase64Binary(final ITerm t0) {
        return BuiltInAtom.fromIRISBuiltinClass(IsBase64BinaryBuiltin.class, new Tuple(t0));
    }

    @Override
    public IBuiltinAtom createIsBoolean(final ITerm t0) {
        return BuiltInAtom.fromIRISBuiltinClass(IsBooleanBuiltin.class, new Tuple(t0));
    }

    @Override
    public IBuiltinAtom createIsDate(final ITerm t0) {
        return BuiltInAtom.fromIRISBuiltinClass(IsDateBuiltin.class, new Tuple(t0));
    }

    @Override
    public IBuiltinAtom createIsDateTime(final ITerm t0) {
        return BuiltInAtom.fromIRISBuiltinClass(IsDateTimeBuiltin.class, new Tuple(t0));
    }

    @Override
    public IBuiltinAtom createIsDayTimeDuration(final ITerm t0) {
        return BuiltInAtom.fromIRISBuiltinClass(IsDayTimeDurationBuiltin.class, new Tuple(t0));
    }

    @Override
    public IBuiltinAtom createIsDecimal(final ITerm t0) {
        return BuiltInAtom.fromIRISBuiltinClass(IsDecimalBuiltin.class, new Tuple(t0));
    }

    @Override
    public IBuiltinAtom createIsDouble(final ITerm t0) {
        return BuiltInAtom.fromIRISBuiltinClass(IsDoubleBuiltin.class, new Tuple(t0));
    }

    @Override
    public IBuiltinAtom createIsDuration(final ITerm t0) {
        return BuiltInAtom.fromIRISBuiltinClass(IsDurationBuiltin.class, new Tuple(t0));
    }

    @Override
    public IBuiltinAtom createIsFloat(final ITerm t0) {
        return BuiltInAtom.fromIRISBuiltinClass(IsFloatBuiltin.class, new Tuple(t0));
    }

    @Override
    public IBuiltinAtom createIsGDay(final ITerm t0) {
        return BuiltInAtom.fromIRISBuiltinClass(IsGDayBuiltin.class, new Tuple(t0));
    }

    @Override
    public IBuiltinAtom createIsGMonth(final ITerm t0) {
        return BuiltInAtom.fromIRISBuiltinClass(IsGMonthBuiltin.class, new Tuple(t0));
    }

    @Override
    public IBuiltinAtom createIsGMonthDay(final ITerm t0) {
        return BuiltInAtom.fromIRISBuiltinClass(IsGMonthBuiltin.class, new Tuple(t0));
    }

    @Override
    public IBuiltinAtom createIsGYear(final ITerm t0) {
        return BuiltInAtom.fromIRISBuiltinClass(IsGYearBuiltin.class, new Tuple(t0));
    }

    @Override
    public IBuiltinAtom createIsGYearMonth(final ITerm t0) {
        return BuiltInAtom.fromIRISBuiltinClass(IsGYearMonthBuiltin.class, new Tuple(t0));
    }

    @Override
    public IBuiltinAtom createIsHexBinary(final ITerm t0) {
        return BuiltInAtom.fromIRISBuiltinClass(IsHexBinaryBuiltin.class, new Tuple(t0));
    }

    @Override
    public IBuiltinAtom createIsInteger(final ITerm t0) {
        return BuiltInAtom.fromIRISBuiltinClass(IsIntegerBuiltin.class, new Tuple(t0));
    }

    @Override
    public IBuiltinAtom createIsIRI(final ITerm t0) {
        return BuiltInAtom.fromIRISBuiltinClass(IsIriBuiltin.class, new Tuple(t0));
    }

    @Override
    public IBuiltinAtom createIsString(final ITerm t0) {
        return BuiltInAtom.fromIRISBuiltinClass(IsStringBuiltin.class, new Tuple(t0));
    }

    @Override
    public IBuiltinAtom createIsPlainLiteral(final ITerm t0) {
        return BuiltInAtom.fromIRISBuiltinClass(IsPlainLiteralBuiltin.class, new Tuple(t0));
    }

    @Override
    public IBuiltinAtom createIsTime(final ITerm t0) {
        return BuiltInAtom.fromIRISBuiltinClass(IsTimeBuiltin.class, new Tuple(t0));
    }

    @Override
    public IBuiltinAtom createIsXMLLiteral(final ITerm t0) {
        return BuiltInAtom.fromIRISBuiltinClass(IsXMLLiteralBuiltin.class, new Tuple(t0));
    }

    @Override
    public IBuiltinAtom createIsYearMonthDuration(final ITerm t0) {
        return BuiltInAtom.fromIRISBuiltinClass(IsYearMonthDurationBuiltin.class, new Tuple(t0));
    }
}
