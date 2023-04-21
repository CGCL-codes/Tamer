package org.deri.iris.api.factory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import org.deri.iris.api.terms.concrete.IAnyURI;
import org.deri.iris.api.terms.concrete.IBase64Binary;
import org.deri.iris.api.terms.concrete.IBooleanTerm;
import org.deri.iris.api.terms.concrete.IByteTerm;
import org.deri.iris.api.terms.concrete.IDateTerm;
import org.deri.iris.api.terms.concrete.IDateTime;
import org.deri.iris.api.terms.concrete.IDayTimeDuration;
import org.deri.iris.api.terms.concrete.IDecimalTerm;
import org.deri.iris.api.terms.concrete.IDoubleTerm;
import org.deri.iris.api.terms.concrete.IDuration;
import org.deri.iris.api.terms.concrete.IENTITY;
import org.deri.iris.api.terms.concrete.IFloatTerm;
import org.deri.iris.api.terms.concrete.IGDay;
import org.deri.iris.api.terms.concrete.IGMonth;
import org.deri.iris.api.terms.concrete.IGMonthDay;
import org.deri.iris.api.terms.concrete.IGYear;
import org.deri.iris.api.terms.concrete.IGYearMonth;
import org.deri.iris.api.terms.concrete.IHexBinary;
import org.deri.iris.api.terms.concrete.IID;
import org.deri.iris.api.terms.concrete.IIDREF;
import org.deri.iris.api.terms.concrete.IIntTerm;
import org.deri.iris.api.terms.concrete.IIntegerTerm;
import org.deri.iris.api.terms.concrete.IIri;
import org.deri.iris.api.terms.concrete.ILanguage;
import org.deri.iris.api.terms.concrete.ILongTerm;
import org.deri.iris.api.terms.concrete.INCName;
import org.deri.iris.api.terms.concrete.INMTOKEN;
import org.deri.iris.api.terms.concrete.INOTATION;
import org.deri.iris.api.terms.concrete.IName;
import org.deri.iris.api.terms.concrete.INegativeInteger;
import org.deri.iris.api.terms.concrete.INonNegativeInteger;
import org.deri.iris.api.terms.concrete.INonPositiveInteger;
import org.deri.iris.api.terms.concrete.INormalizedString;
import org.deri.iris.api.terms.concrete.IPlainLiteral;
import org.deri.iris.api.terms.concrete.IPositiveInteger;
import org.deri.iris.api.terms.concrete.IQName;
import org.deri.iris.api.terms.concrete.IShortTerm;
import org.deri.iris.api.terms.concrete.ISqName;
import org.deri.iris.api.terms.concrete.ITime;
import org.deri.iris.api.terms.concrete.IToken;
import org.deri.iris.api.terms.concrete.IUnsignedByte;
import org.deri.iris.api.terms.concrete.IUnsignedInt;
import org.deri.iris.api.terms.concrete.IUnsignedLong;
import org.deri.iris.api.terms.concrete.IUnsignedShort;
import org.deri.iris.api.terms.concrete.IXMLLiteral;
import org.deri.iris.api.terms.concrete.IYearMonthDuration;

/**
 * <p>
 * An interface that can be used to create set of data types supported by this
 * engine.
 * </p>
 */
public interface IConcreteFactory {

    /**
	 * Create a Base64Binary term from a String representing a Base64Binary data
	 * type.
	 * 
	 * @param s The String representing a Base64Binary data type.
	 * @return The Base64Binary term.
	 */
    public IBase64Binary createBase64Binary(String s);

    /**
	 * Create a boolean term from a boolean value.
	 * 
	 * @param b The value of the term
	 * @return The boolean term.
	 */
    public IBooleanTerm createBoolean(boolean b);

    /**
	 * Create a boolean term with a string value.
	 * 
	 * @param value The string value, which must be either 'true' or '1' for
	 *            true, or 'false' or '0' for false.
	 * @return The boolean term.
	 */
    public IBooleanTerm createBoolean(String value);

    /**
	 * Creates a new date object. The timezone will be set to GMT.
	 * 
	 * @param year the year
	 * @param month the mont (1-12)
	 * @param day the day
	 */
    public IDateTerm createDate(int year, int month, int day);

    /**
	 * Creates a new date object within the given timezone.
	 * 
	 * @param year the year
	 * @param month the mont (1-12)
	 * @param day the day
	 * @param tzHour the timezone hours (relative to GMT)
	 * @param tzMinute the timezone minutes (relative to GMT)
	 * @throws IllegalArgumentException if, the tzHour and tzMinute wheren't
	 *             both positive, or negative
	 */
    public IDateTerm createDate(int year, int month, int day, int tzHour, int tzMinute);

    /**
	 * Creates a datetime object with a given timezone.
	 * 
	 * @param year the years
	 * @param month the months (1-12)
	 * @param day day of the month
	 * @param hour the hours
	 * @param minute the minutes
	 * @param second the decimal seconds
	 * @param tzHour the timezone hours (relative to GMT)
	 * @param tzMinute the timezone minutes (relative to GMT)
	 * @throws IllegalArgumentException if, the tzHour and tzMinute wheren't
	 *             both positive, or negative
	 */
    public IDateTime createDateTime(int year, int month, int day, int hour, int minute, double second, int tzHour, int tzMinute);

    /**
	 * Creates a datetime object with a given timezone.
	 * 
	 * @param year the years
	 * @param month the months (1-12)
	 * @param day day of the month
	 * @param hour the hours
	 * @param minute the minutes
	 * @param second the seconds
	 * @param millisecond the milliseconds
	 * @param tzHour the timezone hours (relative to GMT)
	 * @param tzMinute the timezone minutes (relative to GMT)
	 * @throws IllegalArgumentException if, the tzHour and tzMinute wheren't
	 *             both positive, or negative
	 */
    public IDateTime createDateTime(int year, int month, int day, int hour, int minute, int second, int millisecond, int tzHour, int tzMinute);

    /**
	 * Creates a dateTimeStamp term for the specified values.
	 * 
	 * @param year The year fragment.
	 * @param month The month fragment.
	 * @param day The Day of month fragment.
	 * @param hour The hour fragment.
	 * @param minute The minute fragment.
	 * @param second The second fragment.
	 * @param tzHour The timezone hour (relative to GMT) fragment.
	 * @param tzMinute The timezone minute (relative to GMT) fragment.
	 * @throws IllegalArgumentException If not both the tzHour and tzMinute are
	 *             positive or negative.
	 */
    public IDateTime createDateTimeStamp(int year, int month, int day, int hour, int minute, double second, int tzHour, int tzMinute);

    /**
	 * Creates a new term representing a xs:dayTimeDuration.
	 * 
	 * @param positive True if this term represents a positive duration, false
	 *            otherwise.
	 * @param day The day.
	 * @param hour The hour.
	 * @param minute The minute.
	 * @param second The second.
	 * @return The new term representing a xs:dayTimeDuration.
	 */
    public IDayTimeDuration createDayTimeDuration(boolean positive, int day, int hour, int minute, double second);

    /**
	 * Creates a new term representing a xs:dayTimeDuration.
	 * 
	 * @param positive True if this term represents a positive duration, false
	 *            otherwise.
	 * @param day The day.
	 * @param hour The hour.
	 * @param minute The minute.
	 * @param second The second.
	 * @param millisecond The millisecond.
	 * @return The new term representing a xs:dayTimeDuration.
	 */
    public IDayTimeDuration createDayTimeDuration(boolean positive, int day, int hour, int minute, int second, int millisecond);

    /**
	 * Create a new decimal term.
	 * 
	 * @param d The decimal value
	 * @return The new decimal term
	 */
    public IDecimalTerm createDecimal(double d);

    /**
	 * Create a new decimal term.
	 * 
	 * @param value The decimal value.
	 * @return The new decimal term.
	 */
    public IDecimalTerm createDecimal(BigDecimal value);

    /**
	 * Create a double term.
	 * 
	 * @param d The double values
	 * @return The new term
	 */
    public IDoubleTerm createDouble(double d);

    /**
	 * Create a new Duration term.
	 * 
	 * @param positive true is a positive duration
	 * @param year
	 * @param month
	 * @param day
	 * @param hour
	 * @param minute
	 * @param second
	 * @return
	 */
    public IDuration createDuration(boolean positive, int year, int month, int day, int hour, int minute, double second);

    /**
	 * Constructs a new duration.
	 * 
	 * @param positive <code>true</code>if the duration is positive, otherwise
	 *            <code>false</code>
	 * @param year the yearspan
	 * @param month the monthspa (1-12)
	 * @param day the dayspan
	 * @param hour the hourspan
	 * @param minute the minutespan
	 * @param second the secondspan
	 * @param millisecond the millisecondspan
	 */
    public IDuration createDuration(boolean positive, int year, int month, int day, int hour, int minute, int second, int millisecond);

    /**
	 * Constructs a new duration out of a given amount of milliseconds. The
	 * milliseconds will be round down to the next second.
	 * 
	 * @param millis the millisecond span
	 */
    public IDuration createDuration(long millis);

    /**
	 * Create a new float term
	 * 
	 * @param f The float value
	 * @return The new float term
	 */
    public IFloatTerm createFloat(float f);

    /**
	 * Create a new day term
	 * 
	 * @param day The day value
	 * @return The new term
	 */
    public IGDay createGDay(int day);

    /**
	 * Create a new month term
	 * 
	 * @param month The month value
	 * @return The new term
	 */
    public IGMonth createGMonth(int month);

    /**
	 * Create a new month/day term
	 * 
	 * @param month The month value
	 * @param day The day value
	 * @return The new term
	 */
    public IGMonthDay createGMonthDay(int month, int day);

    /**
	 * Create a new year term
	 * 
	 * @param year The year value
	 * @return The new term
	 */
    public IGYear createGYear(int year);

    /**
	 * Create a new year/month term
	 * 
	 * @param year The year value
	 * @param month The month value
	 * @return The new term
	 */
    public IGYearMonth createGYearMonth(int year, int month);

    /**
	 * Create a new HexBinary term
	 * 
	 * @param s The hex binary value
	 * @return The new term
	 */
    public IHexBinary createHexBinary(String s);

    /**
	 * <p>
	 * Create a new term representing a xs:integer.
	 * </p>
	 * 
	 * @param i The integer value.
	 * @return The new term.
	 */
    public IIntegerTerm createInteger(int i);

    /**
	 * <p>
	 * Create a new term representing a xs:integer.
	 * </p>
	 * 
	 * @param i The BigInteger value.
	 * @return The new term.
	 */
    public IIntegerTerm createInteger(BigInteger i);

    /**
	 * Create a new IRI term
	 * 
	 * @param s The IRI value
	 * @return The new term
	 */
    public IIri createIri(String s);

    /**
	 * Create a new SQName term
	 * 
	 * @param iri The IRI value
	 * @param s The SQName value
	 * @return The new term
	 */
    public ISqName createSqName(IIri iri, String name);

    /**
	 * Create a new SQName term
	 * 
	 * @param s The SQName value
	 * @return The new term
	 */
    public ISqName createSqName(String s);

    /**
	 * Creates a new PlainLiteral term. The string passed to this method is of
	 * the form "text@lang" and contains at least one "@".
	 * 
	 * @param string A string of the form "text@lang", where "lang" is the
	 *            language of the text. Must contain at least one "@" character.
	 * @return The PlainLiteral for the specified string.
	 */
    public IPlainLiteral createPlainLiteral(String string);

    /**
	 * Creates a new PlainLiteral term with the specified string and language
	 * tag.
	 * 
	 * @param string The string.
	 * @param language The language tag.
	 * @return The PlainLiteral term for the specified string and language tag.
	 */
    public IPlainLiteral createPlainLiteral(String string, String language);

    /**
	 * Creates a time object with a given timezone.
	 * 
	 * @param hour the hours
	 * @param minute the minutes
	 * @param second the decimal seconds
	 * @param tzHour the timezone hours (relative to GMT)
	 * @param tzMinute the timezone minutes (relative to GMT)
	 * @throws IllegalArgumentException if, the tzHour and tzMinute wheren't
	 *             both positive, or negative
	 */
    public ITime createTime(int hour, int minute, double second, int tzHour, int tzMinute);

    /**
	 * Creates a time object with a given timezone.
	 * 
	 * @param hour the hours
	 * @param minute the minutes
	 * @param second the seconds
	 * @param millisecond the milliseconds
	 * @param tzHour the timezone hours (relative to GMT)
	 * @param tzMinute the timezone minutes (relative to GMT)
	 * @throws IllegalArgumentException if, the tzHour and tzMinute wheren't
	 *             both positive, or negative
	 */
    public ITime createTime(int hour, int minute, int second, int millisecond, int tzHour, int tzMinute);

    /**
	 * Creates a new term representing a rdf:XMLLiteral.
	 * 
	 * @param string A string representing a XML element.
	 * @return The new term representing the rdf:XMLLiteral.
	 */
    public IXMLLiteral createXMLLiteral(String string);

    /**
	 * Creates a new term representing a rdf:XMLLiteral.
	 * 
	 * @param string A string representing a XML element.
	 * @param lang The language of the XML element.
	 * @return The new term representing the rdf:XMLLiteral.
	 */
    public IXMLLiteral createXMLLiteral(String string, String lang);

    /**
	 * Creates a new term representing a xs:yearMonthDuration.
	 * 
	 * @param positive True if this term represents a positive duration, false
	 *            otherwise.
	 * @param year The year.
	 * @param month The month.
	 * @return The new term representing a xs:yearMonthDuration.
	 */
    public IYearMonthDuration createYearMonthDuration(boolean positive, int year, int month);

    /**
	 * Creates a new term representing a xs:anyURI.
	 * 
	 * @param uri The URI representing the value of the xs:anyURI.
	 * @return The term representing the xs:anyURI for the specified URI.
	 */
    public IAnyURI createAnyURI(URI uri);

    /**
	 * Creates a new term representing a xs:QName.
	 * 
	 * @param namespaceName The namespace name of the xs:QName. May be
	 *            <code>null</code>.
	 * @param localPart The local part of the xs:QName.
	 * @return A term representing the xs:QName for the specified namespace name
	 *         and local part.
	 */
    public IQName createQName(String namespaceName, String localPart);

    /**
	 * Creates a new term representing a xs:NOTATION.
	 * 
	 * @param namespaceName The namespace name of the xs:NOTATION.
	 * @param localPart The local part of the xs:NOTATION. May be
	 *            <code>null</code>.
	 * @return A term representing the xs:NOTATION for the specified namespace
	 *         name and local part.
	 */
    public INOTATION createNOTATION(String namespaceName, String localPart);

    /**
	 * Creates a new term representing a xs:byte.
	 * 
	 * @param value The byte value.
	 * @return The term representing the xs:byte for the specified byte.
	 */
    public IByteTerm createByte(byte value);

    /**
	 * Create a new term representing a xs:ENTITY.
	 * 
	 * @param entity The string representing the value of the xs:ENTITY.
	 * @return The term representing the xs:ENTITY for the specified string.
	 */
    public IENTITY createEntity(String entity);

    /**
	 * Create a new term representing a xs:ID.
	 * 
	 * @param id The string representing the value of the xs:ID.
	 * @return The term representing the xs:ID for the specified string.
	 */
    public IID createID(String id);

    /**
	 * Create a new term representing a xs:IDREF.
	 * 
	 * @param idRef The string representing the value of the xs:IDREF.
	 * @return The term representing the xs:IDREF for the specified string.
	 */
    public IIDREF createIDREF(String idRef);

    /**
	 * Create a new term representing a xs:language.
	 * 
	 * @param language The string representing the value of the xs:language.
	 * @return The term representing the xs:language for the specified string.
	 */
    public ILanguage createLanguage(String language);

    /**
	 * <p>
	 * Create a new term representing a xs:long.
	 * </p>
	 * 
	 * @param value The integer value representing the value of the xs:long.
	 * @return The term representing the xs:long for the specified integer
	 *         value.
	 */
    public ILongTerm createLong(long value);

    /**
	 * Create a new term representing a xs:Name.
	 * 
	 * @param name The string value representing the value of the xs:Name.
	 * @return The term representing the xs:Name for the specified string.
	 */
    public IName createName(String name);

    /**
	 * Create a new term representing a xs:NCName.
	 * 
	 * @param name The string value representing the value of the xs:NCName.
	 * @return The term representing the xs:NCName for the specified string.
	 */
    public INCName createNCName(String name);

    /**
	 * <p>
	 * Create a new term representing a xs:negativeInteger.
	 * </p>
	 * 
	 * @param value The BigInteger value representing the value of the
	 *            xs:negativeInteger.
	 * @return The term representing the xs:negativeInteger for the specified
	 *         BigInteger value.
	 */
    public INegativeInteger createNegativeInteger(BigInteger value);

    /**
	 * Create a new term representing a xs:NMTOKEN.
	 * 
	 * @param token The string value representing the value of the xs:NMTOKEN.
	 * @return The term representing the xs:NMTOKEN for the specified string.
	 */
    public INMTOKEN createNMTOKEN(String token);

    /**
	 * <p>
	 * Create a new term representing a xs:nonNegativeInteger.
	 * </p>
	 * 
	 * @param value The BigInteger value representing the value of the
	 *            xs:nonNegativeInteger.
	 * @return The term representing the xs:nonNegativeInteger for the specified
	 *         BigInteger value.
	 */
    public INonNegativeInteger createNonNegativeInteger(BigInteger value);

    /**
	 * <p>
	 * Create a new term representing a xs:nonPositiveInteger.
	 * </p>
	 * 
	 * @param value The BigInteger value representing the value of the
	 *            xs:nonPositiveInteger.
	 * @return The term representing the xs:nonPositiveInteger for the specified
	 *         BigInteger value.
	 */
    public INonPositiveInteger createNonPositiveInteger(BigInteger value);

    /**
	 * Create a new term representing a xs:normalizedString.
	 * 
	 * @param string The string value representing the value of the
	 *            xs:normalizedString.
	 * @return The term representing the xs:normalizedString for the specified
	 *         string.
	 */
    public INormalizedString createNormalizedString(String string);

    /**
	 * <p>
	 * Create a new term representing a xs:positiveInteger.
	 * </p>
	 * 
	 * @param value The BigInteger value representing the value of the
	 *            xs:positiveInteger.
	 * @return The term representing the xs:positiveInteger for the specified
	 *         BigInteger value.
	 */
    public IPositiveInteger createPositiveInteger(BigInteger value);

    /**
	 * Create a new term representing a xs:short.
	 * 
	 * @param value The integer value representing the value of the xs:short.
	 * @return The term representing the xs:short for the specified integer
	 *         value.
	 */
    public IShortTerm createShort(short value);

    /**
	 * Create a new term representing a xs:token.
	 * 
	 * @param token The string value representing the value of the xs:token.
	 * @return The term representing the xs:token for the specified string.
	 */
    public IToken createToken(String token);

    /**
	 * <p>
	 * Create a new term representing a xs:unsignedLong.
	 * </p>
	 * 
	 * @param value The BigInteger value representing the value of the
	 *            xs:unsignedLong.
	 * @return The term representing the xs:unsignedLong for the specified
	 *         BigInteger value.
	 */
    public IUnsignedLong createUnsignedLong(BigInteger value);

    /**
	 * <p>
	 * Create a new term representing a xs:unsignedInt.
	 * </p>
	 * 
	 * @param value The long value representing the value of the xs:unsignedInt.
	 * @return The term representing the xs:unsignedInt for the specified long
	 *         value.
	 */
    public IUnsignedInt createUnsignedInt(long value);

    /**
	 * <p>
	 * Create a new term representing a xs:unsignedShort.
	 * </p>
	 * 
	 * @param value The integer value representing the value of the
	 *            xs:unsignedShort.
	 * @return The term representing the xs:unsignedShort for the specified
	 *         integer value.
	 */
    public IUnsignedShort createUnsignedShort(int value);

    /**
	 * <p>
	 * Create a new term representing a xs:unsignedByte.
	 * </p>
	 * 
	 * @param value The short value representing the value of the
	 *            xs:unsignedByte.
	 * @return The term representing the xs:unsignedByte for the specified short
	 *         value.
	 */
    public IUnsignedByte createUnsignedByte(short value);

    /**
	 * <p>
	 * Create a new term representing a xs:int.
	 * </p>
	 * 
	 * @param value The integer value representing the value of the xs:int.
	 * @return The term representing the xs:int for the specified integer value.
	 */
    public IIntTerm createInt(int value);
}
