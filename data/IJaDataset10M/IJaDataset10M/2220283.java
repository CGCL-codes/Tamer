package org.xaware.ide.xadev.conversion;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.GDate;
import org.apache.xmlbeans.GDateBuilder;
import org.apache.xmlbeans.GDuration;
import org.apache.xmlbeans.GDurationBuilder;
import org.apache.xmlbeans.SchemaLocalElement;
import org.apache.xmlbeans.SchemaParticle;
import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlAnySimpleType;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlDate;
import org.apache.xmlbeans.XmlDateTime;
import org.apache.xmlbeans.XmlDecimal;
import org.apache.xmlbeans.XmlDuration;
import org.apache.xmlbeans.XmlGDay;
import org.apache.xmlbeans.XmlGMonth;
import org.apache.xmlbeans.XmlGMonthDay;
import org.apache.xmlbeans.XmlGYear;
import org.apache.xmlbeans.XmlGYearMonth;
import org.apache.xmlbeans.XmlInteger;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlTime;
import org.apache.xmlbeans.impl.util.Base64;
import org.apache.xmlbeans.impl.util.HexBin;
import org.apache.xmlbeans.soap.SOAPArrayType;
import org.apache.xmlbeans.soap.SchemaWSDLArrayType;
import org.xaware.ide.xadev.datamodel.InputParameterData;

public class SchemaUtilHelper {

    private static final int MAX_ELEMENTS = 1000;

    private int _nElements;

    private static final QName XSD_TYPE = new QName("http://www.w3.org/2001/XMLSchema", "", "xsd");

    private static final QName HREF = new QName("href");

    private static final QName ID = new QName("id");

    private static final QName XSI_TYPE = new QName("http://www.w3.org/2001/XMLSchema-instance", "type", "xsi");

    private static final QName ENC_ARRAYTYPE = new QName("http://schemas.xmlsoap.org/soap/encoding/", "arrayType");

    private static final QName ENC_OFFSET = new QName("http://schemas.xmlsoap.org/soap/encoding/", "offset");

    private static final Set SKIPPED_SOAP_ATTRS = new HashSet(Arrays.asList(new QName[] { HREF, ID, ENC_OFFSET }));

    private HashMap uriPrefix;

    private SchemaUtilHelper(final boolean soapEnc) {
        uriPrefix = new HashMap();
        uriPrefix.put(XSD_TYPE.getNamespaceURI(), XSD_TYPE.getPrefix());
        uriPrefix.put(XSI_TYPE.getNamespaceURI(), XSD_TYPE.getPrefix());
    }

    public static String createSampleForType(final QName qName, final SchemaType sType, final List params, final boolean isRpcRequest) {
        final XmlObject object = XmlObject.Factory.newInstance();
        final XmlCursor cursor = object.newCursor();
        cursor.toNextToken();
        new SchemaUtilHelper(false).createSampleForType(qName, sType, cursor, params, isRpcRequest);
        final XmlOptions options = new XmlOptions();
        options.setLoadStripWhitespace();
        final String result = object.xmlText(options);
        return result;
    }

    Random _picker = new Random(1);

    /**
     * Cursor position
     * Before:
     * <theElement>^</theElement>
     * After:
     * <theElement><lots of stuff/>^</theElement>
     */
    private void createSampleForType(final QName qName, final SchemaType stype, final XmlCursor xmlc, final List params, final boolean isRpcRequest) {
        if (_typeStack.contains(stype)) {
            return;
        }
        _typeStack.add(stype);
        try {
            if (stype.isSimpleType() || stype.isURType()) {
                processSimpleType(qName, stype, xmlc, params, isRpcRequest);
                return;
            }
            processAttributes(stype, xmlc, isRpcRequest);
            switch(stype.getContentType()) {
                case SchemaType.NOT_COMPLEX_TYPE:
                case SchemaType.EMPTY_CONTENT:
                    break;
                case SchemaType.SIMPLE_CONTENT:
                    {
                        processSimpleType(qName, stype, xmlc, params, isRpcRequest);
                    }
                    break;
                case SchemaType.MIXED_CONTENT:
                    if (stype.getContentModel() != null) {
                        processParticle(stype.getContentModel(), xmlc, true, params, isRpcRequest);
                    }
                    break;
                case SchemaType.ELEMENT_CONTENT:
                    if (stype.getContentModel() != null) {
                        processParticle(stype.getContentModel(), xmlc, false, params, isRpcRequest);
                    }
                    break;
            }
        } finally {
            _typeStack.remove(_typeStack.size() - 1);
        }
    }

    private void processSimpleType(final QName qName, final SchemaType stype, final XmlCursor xmlc, final List inputParamList, final boolean isRpcRequest) {
        if (isRpcRequest) {
            final QName qname = stype.getName();
            final String uri = qname.getNamespaceURI();
            String prefix = XSD_TYPE.getPrefix();
            if (uri != null && uri.length() > 0) {
                prefix = xmlc.prefixForNamespace(uri);
            }
            xmlc.insertAttributeWithValue(XSI_TYPE, prefix + ":" + stype.getName().getLocalPart());
        }
        final String sample = getTypeNameForSimpleType(stype);
        if (inputParamList != null) {
            final InputParameterData data = new InputParameterData();
            data.setName(qName.getLocalPart());
            data.setType(sample);
            inputParamList.add(data);
            final StringBuffer buff = new StringBuffer();
            buff.append('%').append(qName.getLocalPart()).append('%');
            xmlc.insertChars(buff.toString());
        } else {
            xmlc.insertChars(sample);
        }
    }

    private String getTypeNameForSimpleType(final SchemaType sType) {
        if (XmlObject.type.equals(sType)) {
            return "anyType";
        }
        if (XmlAnySimpleType.type.equals(sType)) {
            return "anySimpleType";
        }
        if (sType.getSimpleVariety() == SchemaType.LIST) {
            final SchemaType itemType = sType.getListItemType();
            final StringBuffer sb = new StringBuffer();
            final int length = pickLength(sType);
            if (length > 0) {
                sb.append(getTypeNameForSimpleType(itemType));
            }
            for (int i = 1; i < length; i += 1) {
                sb.append(' ');
                sb.append(getTypeNameForSimpleType(itemType));
            }
            return sb.toString();
        }
        if (sType.getSimpleVariety() == SchemaType.UNION) {
            final SchemaType[] possibleTypes = sType.getUnionConstituentTypes();
            if (possibleTypes.length == 0) {
                return "";
            }
            return getTypeNameForSimpleType(possibleTypes[pick(possibleTypes.length)]);
        }
        switch(sType.getPrimitiveType().getBuiltinTypeCode()) {
            default:
            case SchemaType.BTC_NOT_BUILTIN:
                return "";
            case SchemaType.BTC_ANY_TYPE:
            case SchemaType.BTC_ANY_SIMPLE:
                return "anything";
            case SchemaType.BTC_BOOLEAN:
                return "boolean";
            case SchemaType.BTC_BASE_64_BINARY:
                return "base64binary";
            case SchemaType.BTC_HEX_BINARY:
                return "binary";
            case SchemaType.BTC_ANY_URI:
                return "string";
            case SchemaType.BTC_QNAME:
                return formatToLength("qname", sType);
            case SchemaType.BTC_NOTATION:
                return formatToLength("notation", sType);
            case SchemaType.BTC_FLOAT:
                return "float";
            case SchemaType.BTC_DOUBLE:
                return "double";
            case SchemaType.BTC_DECIMAL:
                switch(closestBuiltin(sType).getBuiltinTypeCode()) {
                    case SchemaType.BTC_SHORT:
                        return "short";
                    case SchemaType.BTC_UNSIGNED_SHORT:
                        return "short";
                    case SchemaType.BTC_BYTE:
                        return "byte";
                    case SchemaType.BTC_UNSIGNED_BYTE:
                        return "byte";
                    case SchemaType.BTC_INT:
                        return "int";
                    case SchemaType.BTC_UNSIGNED_INT:
                        return "int";
                    case SchemaType.BTC_LONG:
                        return "long";
                    case SchemaType.BTC_UNSIGNED_LONG:
                        return "long";
                    case SchemaType.BTC_INTEGER:
                        return "int";
                    case SchemaType.BTC_NON_POSITIVE_INTEGER:
                        return "int";
                    case SchemaType.BTC_NEGATIVE_INTEGER:
                        return "int";
                    case SchemaType.BTC_NON_NEGATIVE_INTEGER:
                        return "int";
                    case SchemaType.BTC_POSITIVE_INTEGER:
                        return "int";
                    default:
                    case SchemaType.BTC_DECIMAL:
                        return "decimal";
                }
            case SchemaType.BTC_STRING:
                {
                    String result;
                    switch(closestBuiltin(sType).getBuiltinTypeCode()) {
                        case SchemaType.BTC_STRING:
                        case SchemaType.BTC_NORMALIZED_STRING:
                            result = "string";
                            break;
                        case SchemaType.BTC_TOKEN:
                            result = "token";
                            break;
                        default:
                            result = "string";
                            break;
                    }
                    return formatToLength(result, sType);
                }
            case SchemaType.BTC_DURATION:
                return "duration";
            case SchemaType.BTC_DATE_TIME:
            case SchemaType.BTC_DATE:
                return "date";
            case SchemaType.BTC_TIME:
                return "time";
            case SchemaType.BTC_G_YEAR_MONTH:
                return "year_month";
            case SchemaType.BTC_G_YEAR:
                return "year";
            case SchemaType.BTC_G_MONTH_DAY:
                return "month_day";
            case SchemaType.BTC_G_DAY:
                return "day";
            case SchemaType.BTC_G_MONTH:
                return "month";
        }
    }

    private String sampleDataForSimpleType(final SchemaType sType) {
        if (XmlObject.type.equals(sType)) {
            return "anyType";
        }
        if (XmlAnySimpleType.type.equals(sType)) {
            return "anySimpleType";
        }
        if (sType.getSimpleVariety() == SchemaType.LIST) {
            final SchemaType itemType = sType.getListItemType();
            final StringBuffer sb = new StringBuffer();
            final int length = pickLength(sType);
            if (length > 0) {
                sb.append(sampleDataForSimpleType(itemType));
            }
            for (int i = 1; i < length; i += 1) {
                sb.append(' ');
                sb.append(sampleDataForSimpleType(itemType));
            }
            return sb.toString();
        }
        if (sType.getSimpleVariety() == SchemaType.UNION) {
            final SchemaType[] possibleTypes = sType.getUnionConstituentTypes();
            if (possibleTypes.length == 0) {
                return "";
            }
            return sampleDataForSimpleType(possibleTypes[pick(possibleTypes.length)]);
        }
        final XmlAnySimpleType[] enumValues = sType.getEnumerationValues();
        if (enumValues != null && enumValues.length > 0) {
            return enumValues[pick(enumValues.length)].getStringValue();
        }
        switch(sType.getPrimitiveType().getBuiltinTypeCode()) {
            default:
            case SchemaType.BTC_NOT_BUILTIN:
                return "";
            case SchemaType.BTC_ANY_TYPE:
            case SchemaType.BTC_ANY_SIMPLE:
                return "anything";
            case SchemaType.BTC_BOOLEAN:
                return pick(2) == 0 ? "true" : "false";
            case SchemaType.BTC_BASE_64_BINARY:
                {
                    String result = null;
                    try {
                        result = new String(Base64.encode(formatToLength(pick(WORDS), sType).getBytes("utf-8")));
                    } catch (final java.io.UnsupportedEncodingException e) {
                    }
                    return result;
                }
            case SchemaType.BTC_HEX_BINARY:
                return HexBin.encode(formatToLength(pick(WORDS), sType));
            case SchemaType.BTC_ANY_URI:
            case SchemaType.BTC_QNAME:
                return formatToLength("qname", sType);
            case SchemaType.BTC_NOTATION:
                return formatToLength("notation", sType);
            case SchemaType.BTC_FLOAT:
                return "1.5E2";
            case SchemaType.BTC_DOUBLE:
                return "1.051732E7";
            case SchemaType.BTC_DECIMAL:
                switch(closestBuiltin(sType).getBuiltinTypeCode()) {
                    case SchemaType.BTC_SHORT:
                        return formatDecimal("1", sType);
                    case SchemaType.BTC_UNSIGNED_SHORT:
                        return formatDecimal("5", sType);
                    case SchemaType.BTC_BYTE:
                        return formatDecimal("2", sType);
                    case SchemaType.BTC_UNSIGNED_BYTE:
                        return formatDecimal("6", sType);
                    case SchemaType.BTC_INT:
                        return formatDecimal("3", sType);
                    case SchemaType.BTC_UNSIGNED_INT:
                        return formatDecimal("7", sType);
                    case SchemaType.BTC_LONG:
                        return formatDecimal("10", sType);
                    case SchemaType.BTC_UNSIGNED_LONG:
                        return formatDecimal("11", sType);
                    case SchemaType.BTC_INTEGER:
                        return formatDecimal("100", sType);
                    case SchemaType.BTC_NON_POSITIVE_INTEGER:
                        return formatDecimal("-200", sType);
                    case SchemaType.BTC_NEGATIVE_INTEGER:
                        return formatDecimal("-201", sType);
                    case SchemaType.BTC_NON_NEGATIVE_INTEGER:
                        return formatDecimal("200", sType);
                    case SchemaType.BTC_POSITIVE_INTEGER:
                        return formatDecimal("201", sType);
                    default:
                    case SchemaType.BTC_DECIMAL:
                        return formatDecimal("1000.00", sType);
                }
            case SchemaType.BTC_STRING:
                {
                    String result;
                    switch(closestBuiltin(sType).getBuiltinTypeCode()) {
                        case SchemaType.BTC_STRING:
                        case SchemaType.BTC_NORMALIZED_STRING:
                            result = "string";
                            break;
                        case SchemaType.BTC_TOKEN:
                            result = "token";
                            break;
                        default:
                            result = "string";
                            break;
                    }
                    return formatToLength(result, sType);
                }
            case SchemaType.BTC_DURATION:
                return formatDuration(sType);
            case SchemaType.BTC_DATE_TIME:
            case SchemaType.BTC_TIME:
            case SchemaType.BTC_DATE:
            case SchemaType.BTC_G_YEAR_MONTH:
            case SchemaType.BTC_G_YEAR:
            case SchemaType.BTC_G_MONTH_DAY:
            case SchemaType.BTC_G_DAY:
            case SchemaType.BTC_G_MONTH:
                return formatDate(sType);
        }
    }

    public static final String[] WORDS = new String[] { "ipsa", "iovis", "rapidum", "iaculata", "e", "nubibus", "ignem", "disiecitque", "rates", "evertitque", "aequora", "ventis", "illum", "exspirantem", "transfixo", "pectore", "flammas", "turbine", "corripuit", "scopuloque", "infixit", "acuto", "ast", "ego", "quae", "divum", "incedo", "regina", "iovisque", "et", "soror", "et", "coniunx", "una", "cum", "gente", "tot", "annos", "bella", "gero", "et", "quisquam", "numen", "iunonis", "adorat", "praeterea", "aut", "supplex", "aris", "imponet", "honorem", "talia", "flammato", "secum", "dea", "corde", "volutans", "nimborum", "in", "patriam", "loca", "feta", "furentibus", "austris", "aeoliam", "venit", "hic", "vasto", "rex", "aeolus", "antro", "luctantis", "ventos", "tempestatesque", "sonoras", "imperio", "premit", "ac", "vinclis", "et", "carcere", "frenat", "illi", "indignantes", "magno", "cum", "murmure", "montis", "circum", "claustra", "fremunt", "celsa", "sedet", "aeolus", "arce", "sceptra", "tenens", "mollitque", "animos", "et", "temperat", "iras", "ni", "faciat", "maria", "ac", "terras", "caelumque", "profundum", "quippe", "ferant", "rapidi", "secum", "verrantque", "per", "auras", "sed", "pater", "omnipotens", "speluncis", "abdidit", "atris", "hoc", "metuens", "molemque", "et", "montis", "insuper", "altos", "imposuit", "regemque", "dedit", "qui", "foedere", "certo", "et", "premere", "et", "laxas", "sciret", "dare", "iussus", "habenas" };

    private static final String[] DNS1 = new String[] { "corp", "your", "my", "sample", "company", "test", "any" };

    private static final String[] DNS2 = new String[] { "com", "org", "com", "gov", "org", "com", "org", "com", "edu" };

    private int pick(final int n) {
        return _picker.nextInt(n);
    }

    private String pick(final String[] a) {
        return a[pick(a.length)];
    }

    private String pick(final String[] a, int count) {
        if (count <= 0) {
            return "";
        }
        int i = pick(a.length);
        final StringBuffer sb = new StringBuffer(a[i]);
        while (count-- > 0) {
            i += 1;
            if (i >= a.length) {
                i = 0;
            }
            sb.append(' ');
            sb.append(a[i]);
        }
        return sb.toString();
    }

    private String pickDigits(int digits) {
        final StringBuffer sb = new StringBuffer();
        while (digits-- > 0) {
            sb.append(Integer.toString(pick(10)));
        }
        return sb.toString();
    }

    private int pickLength(final SchemaType sType) {
        final XmlInteger length = (XmlInteger) sType.getFacet(SchemaType.FACET_LENGTH);
        if (length != null) {
            return length.getBigIntegerValue().intValue();
        }
        final XmlInteger min = (XmlInteger) sType.getFacet(SchemaType.FACET_MIN_LENGTH);
        final XmlInteger max = (XmlInteger) sType.getFacet(SchemaType.FACET_MAX_LENGTH);
        int minInt, maxInt;
        if (min == null) {
            minInt = 0;
        } else {
            minInt = min.getBigIntegerValue().intValue();
        }
        if (max == null) {
            maxInt = Integer.MAX_VALUE;
        } else {
            maxInt = max.getBigIntegerValue().intValue();
        }
        if (minInt == 0 && maxInt >= 1) {
            minInt = 1;
        }
        if (maxInt > minInt + 2) {
            maxInt = minInt + 2;
        }
        if (maxInt < minInt) {
            maxInt = minInt;
        }
        return minInt + pick(maxInt - minInt);
    }

    /**
     * Formats a given string to the required length, using the following operations:
     * - append the source string to itself as necessary to pass the minLength;
     * - truncate the result of previous step, if necessary, to keep it within minLength.
     */
    private String formatToLength(final String s, final SchemaType sType) {
        String result = s;
        try {
            SimpleValue min = (SimpleValue) sType.getFacet(SchemaType.FACET_LENGTH);
            if (min == null) {
                min = (SimpleValue) sType.getFacet(SchemaType.FACET_MIN_LENGTH);
            }
            if (min != null) {
                final int len = min.getIntValue();
                while (result.length() < len) {
                    result = result + result;
                }
            }
            SimpleValue max = (SimpleValue) sType.getFacet(SchemaType.FACET_LENGTH);
            if (max == null) {
                max = (SimpleValue) sType.getFacet(SchemaType.FACET_MAX_LENGTH);
            }
            if (max != null) {
                final int len = max.getIntValue();
                if (result.length() > len) {
                    result = result.substring(0, len);
                }
            }
        } catch (final Exception e) {
        }
        return result;
    }

    private String formatDecimal(final String start, final SchemaType sType) {
        BigDecimal result = new BigDecimal(start);
        XmlDecimal xmlD;
        xmlD = (XmlDecimal) sType.getFacet(SchemaType.FACET_MIN_INCLUSIVE);
        BigDecimal min = xmlD != null ? xmlD.getBigDecimalValue() : null;
        xmlD = (XmlDecimal) sType.getFacet(SchemaType.FACET_MAX_INCLUSIVE);
        BigDecimal max = xmlD != null ? xmlD.getBigDecimalValue() : null;
        boolean minInclusive = true, maxInclusive = true;
        xmlD = (XmlDecimal) sType.getFacet(SchemaType.FACET_MIN_EXCLUSIVE);
        if (xmlD != null) {
            final BigDecimal minExcl = xmlD.getBigDecimalValue();
            if (min == null || min.compareTo(minExcl) < 0) {
                min = minExcl;
                minInclusive = false;
            }
        }
        xmlD = (XmlDecimal) sType.getFacet(SchemaType.FACET_MAX_EXCLUSIVE);
        if (xmlD != null) {
            final BigDecimal maxExcl = xmlD.getBigDecimalValue();
            if (max == null || max.compareTo(maxExcl) > 0) {
                max = maxExcl;
                maxInclusive = false;
            }
        }
        xmlD = (XmlDecimal) sType.getFacet(SchemaType.FACET_TOTAL_DIGITS);
        int totalDigits = -1;
        if (xmlD != null) {
            totalDigits = xmlD.getBigDecimalValue().intValue();
            final StringBuffer sb = new StringBuffer(totalDigits);
            for (int i = 0; i < totalDigits; i++) {
                sb.append('9');
            }
            BigDecimal digitsLimit = new BigDecimal(sb.toString());
            if (max != null && max.compareTo(digitsLimit) > 0) {
                max = digitsLimit;
                maxInclusive = true;
            }
            digitsLimit = digitsLimit.negate();
            if (min != null && min.compareTo(digitsLimit) < 0) {
                min = digitsLimit;
                minInclusive = true;
            }
        }
        final int sigMin = min == null ? 1 : result.compareTo(min);
        final int sigMax = max == null ? -1 : result.compareTo(max);
        boolean minOk = sigMin > 0 || sigMin == 0 && minInclusive;
        boolean maxOk = sigMax < 0 || sigMax == 0 && maxInclusive;
        xmlD = (XmlDecimal) sType.getFacet(SchemaType.FACET_FRACTION_DIGITS);
        int fractionDigits = -1;
        BigDecimal increment;
        if (xmlD == null) {
            increment = new BigDecimal(1);
        } else {
            fractionDigits = xmlD.getBigDecimalValue().intValue();
            if (fractionDigits > 0) {
                final StringBuffer sb = new StringBuffer("0.");
                for (int i = 1; i < fractionDigits; i++) {
                    sb.append('0');
                }
                sb.append('1');
                increment = new BigDecimal(sb.toString());
            } else {
                increment = new BigDecimal(1.0);
            }
        }
        if (minOk && maxOk) {
        } else if (minOk && !maxOk) {
            if (maxInclusive) {
                result = max;
            } else {
                result = max.subtract(increment);
            }
        } else if (!minOk && maxOk) {
            if (minInclusive) {
                result = min;
            } else {
                result = min.add(increment);
            }
        } else {
        }
        int digits = 0;
        final BigDecimal ONE = new BigDecimal(BigInteger.ONE);
        for (BigDecimal n = result; n.abs().compareTo(ONE) >= 0; digits++) {
            n = n.movePointLeft(1);
        }
        if (fractionDigits > 0) {
            if (totalDigits >= 0) {
                result.setScale(Math.max(fractionDigits, totalDigits - digits));
            } else {
                result.setScale(fractionDigits);
            }
        } else if (fractionDigits == 0) {
            result.setScale(0);
        }
        return result.toString();
    }

    private String formatDuration(final SchemaType sType) {
        XmlDuration d = (XmlDuration) sType.getFacet(SchemaType.FACET_MIN_INCLUSIVE);
        GDuration minInclusive = null;
        if (d != null) {
            minInclusive = d.getGDurationValue();
        }
        d = (XmlDuration) sType.getFacet(SchemaType.FACET_MAX_INCLUSIVE);
        GDuration maxInclusive = null;
        if (d != null) {
            maxInclusive = d.getGDurationValue();
        }
        d = (XmlDuration) sType.getFacet(SchemaType.FACET_MIN_EXCLUSIVE);
        GDuration minExclusive = null;
        if (d != null) {
            minExclusive = d.getGDurationValue();
        }
        d = (XmlDuration) sType.getFacet(SchemaType.FACET_MAX_EXCLUSIVE);
        GDuration maxExclusive = null;
        if (d != null) {
            maxExclusive = d.getGDurationValue();
        }
        final GDurationBuilder gdurb = new GDurationBuilder();
        final BigInteger min, max;
        gdurb.setSecond(pick(800000));
        gdurb.setMonth(pick(20));
        if (minInclusive != null) {
            if (gdurb.getYear() < minInclusive.getYear()) {
                gdurb.setYear(minInclusive.getYear());
            }
            if (gdurb.getMonth() < minInclusive.getMonth()) {
                gdurb.setMonth(minInclusive.getMonth());
            }
            if (gdurb.getDay() < minInclusive.getDay()) {
                gdurb.setDay(minInclusive.getDay());
            }
            if (gdurb.getHour() < minInclusive.getHour()) {
                gdurb.setHour(minInclusive.getHour());
            }
            if (gdurb.getMinute() < minInclusive.getMinute()) {
                gdurb.setMinute(minInclusive.getMinute());
            }
            if (gdurb.getSecond() < minInclusive.getSecond()) {
                gdurb.setSecond(minInclusive.getSecond());
            }
            if (gdurb.getFraction().compareTo(minInclusive.getFraction()) < 0) {
                gdurb.setFraction(minInclusive.getFraction());
            }
        }
        if (maxInclusive != null) {
            if (gdurb.getYear() > maxInclusive.getYear()) {
                gdurb.setYear(maxInclusive.getYear());
            }
            if (gdurb.getMonth() > maxInclusive.getMonth()) {
                gdurb.setMonth(maxInclusive.getMonth());
            }
            if (gdurb.getDay() > maxInclusive.getDay()) {
                gdurb.setDay(maxInclusive.getDay());
            }
            if (gdurb.getHour() > maxInclusive.getHour()) {
                gdurb.setHour(maxInclusive.getHour());
            }
            if (gdurb.getMinute() > maxInclusive.getMinute()) {
                gdurb.setMinute(maxInclusive.getMinute());
            }
            if (gdurb.getSecond() > maxInclusive.getSecond()) {
                gdurb.setSecond(maxInclusive.getSecond());
            }
            if (gdurb.getFraction().compareTo(maxInclusive.getFraction()) > 0) {
                gdurb.setFraction(maxInclusive.getFraction());
            }
        }
        if (minExclusive != null) {
            if (gdurb.getYear() <= minExclusive.getYear()) {
                gdurb.setYear(minExclusive.getYear() + 1);
            }
            if (gdurb.getMonth() <= minExclusive.getMonth()) {
                gdurb.setMonth(minExclusive.getMonth() + 1);
            }
            if (gdurb.getDay() <= minExclusive.getDay()) {
                gdurb.setDay(minExclusive.getDay() + 1);
            }
            if (gdurb.getHour() <= minExclusive.getHour()) {
                gdurb.setHour(minExclusive.getHour() + 1);
            }
            if (gdurb.getMinute() <= minExclusive.getMinute()) {
                gdurb.setMinute(minExclusive.getMinute() + 1);
            }
            if (gdurb.getSecond() <= minExclusive.getSecond()) {
                gdurb.setSecond(minExclusive.getSecond() + 1);
            }
            if (gdurb.getFraction().compareTo(minExclusive.getFraction()) <= 0) {
                gdurb.setFraction(minExclusive.getFraction().add(new BigDecimal(0.001)));
            }
        }
        if (maxExclusive != null) {
            if (gdurb.getYear() > maxExclusive.getYear()) {
                gdurb.setYear(maxExclusive.getYear());
            }
            if (gdurb.getMonth() > maxExclusive.getMonth()) {
                gdurb.setMonth(maxExclusive.getMonth());
            }
            if (gdurb.getDay() > maxExclusive.getDay()) {
                gdurb.setDay(maxExclusive.getDay());
            }
            if (gdurb.getHour() > maxExclusive.getHour()) {
                gdurb.setHour(maxExclusive.getHour());
            }
            if (gdurb.getMinute() > maxExclusive.getMinute()) {
                gdurb.setMinute(maxExclusive.getMinute());
            }
            if (gdurb.getSecond() > maxExclusive.getSecond()) {
                gdurb.setSecond(maxExclusive.getSecond());
            }
            if (gdurb.getFraction().compareTo(maxExclusive.getFraction()) > 0) {
                gdurb.setFraction(maxExclusive.getFraction());
            }
        }
        gdurb.normalize();
        return gdurb.toString();
    }

    private String formatDate(final SchemaType sType) {
        GDateBuilder gdateb = new GDateBuilder(new Date(1000L * pick(365 * 24 * 60 * 60) + (30L + pick(20)) * 365 * 24 * 60 * 60 * 1000));
        GDate min = null, max = null;
        final GDate temp;
        switch(sType.getPrimitiveType().getBuiltinTypeCode()) {
            case SchemaType.BTC_DATE_TIME:
                {
                    XmlDateTime x = (XmlDateTime) sType.getFacet(SchemaType.FACET_MIN_INCLUSIVE);
                    if (x != null) {
                        min = x.getGDateValue();
                    }
                    x = (XmlDateTime) sType.getFacet(SchemaType.FACET_MIN_EXCLUSIVE);
                    if (x != null) {
                        if (min == null || min.compareToGDate(x.getGDateValue()) <= 0) {
                            min = x.getGDateValue();
                        }
                    }
                    x = (XmlDateTime) sType.getFacet(SchemaType.FACET_MAX_INCLUSIVE);
                    if (x != null) {
                        max = x.getGDateValue();
                    }
                    x = (XmlDateTime) sType.getFacet(SchemaType.FACET_MAX_EXCLUSIVE);
                    if (x != null) {
                        if (max == null || max.compareToGDate(x.getGDateValue()) >= 0) {
                            max = x.getGDateValue();
                        }
                    }
                    break;
                }
            case SchemaType.BTC_TIME:
                {
                    XmlTime x = (XmlTime) sType.getFacet(SchemaType.FACET_MIN_INCLUSIVE);
                    if (x != null) {
                        min = x.getGDateValue();
                    }
                    x = (XmlTime) sType.getFacet(SchemaType.FACET_MIN_EXCLUSIVE);
                    if (x != null) {
                        if (min == null || min.compareToGDate(x.getGDateValue()) <= 0) {
                            min = x.getGDateValue();
                        }
                    }
                    x = (XmlTime) sType.getFacet(SchemaType.FACET_MAX_INCLUSIVE);
                    if (x != null) {
                        max = x.getGDateValue();
                    }
                    x = (XmlTime) sType.getFacet(SchemaType.FACET_MAX_EXCLUSIVE);
                    if (x != null) {
                        if (max == null || max.compareToGDate(x.getGDateValue()) >= 0) {
                            max = x.getGDateValue();
                        }
                    }
                    break;
                }
            case SchemaType.BTC_DATE:
                {
                    XmlDate x = (XmlDate) sType.getFacet(SchemaType.FACET_MIN_INCLUSIVE);
                    if (x != null) {
                        min = x.getGDateValue();
                    }
                    x = (XmlDate) sType.getFacet(SchemaType.FACET_MIN_EXCLUSIVE);
                    if (x != null) {
                        if (min == null || min.compareToGDate(x.getGDateValue()) <= 0) {
                            min = x.getGDateValue();
                        }
                    }
                    x = (XmlDate) sType.getFacet(SchemaType.FACET_MAX_INCLUSIVE);
                    if (x != null) {
                        max = x.getGDateValue();
                    }
                    x = (XmlDate) sType.getFacet(SchemaType.FACET_MAX_EXCLUSIVE);
                    if (x != null) {
                        if (max == null || max.compareToGDate(x.getGDateValue()) >= 0) {
                            max = x.getGDateValue();
                        }
                    }
                    break;
                }
            case SchemaType.BTC_G_YEAR_MONTH:
                {
                    XmlGYearMonth x = (XmlGYearMonth) sType.getFacet(SchemaType.FACET_MIN_INCLUSIVE);
                    if (x != null) {
                        min = x.getGDateValue();
                    }
                    x = (XmlGYearMonth) sType.getFacet(SchemaType.FACET_MIN_EXCLUSIVE);
                    if (x != null) {
                        if (min == null || min.compareToGDate(x.getGDateValue()) <= 0) {
                            min = x.getGDateValue();
                        }
                    }
                    x = (XmlGYearMonth) sType.getFacet(SchemaType.FACET_MAX_INCLUSIVE);
                    if (x != null) {
                        max = x.getGDateValue();
                    }
                    x = (XmlGYearMonth) sType.getFacet(SchemaType.FACET_MAX_EXCLUSIVE);
                    if (x != null) {
                        if (max == null || max.compareToGDate(x.getGDateValue()) >= 0) {
                            max = x.getGDateValue();
                        }
                    }
                    break;
                }
            case SchemaType.BTC_G_YEAR:
                {
                    XmlGYear x = (XmlGYear) sType.getFacet(SchemaType.FACET_MIN_INCLUSIVE);
                    if (x != null) {
                        min = x.getGDateValue();
                    }
                    x = (XmlGYear) sType.getFacet(SchemaType.FACET_MIN_EXCLUSIVE);
                    if (x != null) {
                        if (min == null || min.compareToGDate(x.getGDateValue()) <= 0) {
                            min = x.getGDateValue();
                        }
                    }
                    x = (XmlGYear) sType.getFacet(SchemaType.FACET_MAX_INCLUSIVE);
                    if (x != null) {
                        max = x.getGDateValue();
                    }
                    x = (XmlGYear) sType.getFacet(SchemaType.FACET_MAX_EXCLUSIVE);
                    if (x != null) {
                        if (max == null || max.compareToGDate(x.getGDateValue()) >= 0) {
                            max = x.getGDateValue();
                        }
                    }
                    break;
                }
            case SchemaType.BTC_G_MONTH_DAY:
                {
                    XmlGMonthDay x = (XmlGMonthDay) sType.getFacet(SchemaType.FACET_MIN_INCLUSIVE);
                    if (x != null) {
                        min = x.getGDateValue();
                    }
                    x = (XmlGMonthDay) sType.getFacet(SchemaType.FACET_MIN_EXCLUSIVE);
                    if (x != null) {
                        if (min == null || min.compareToGDate(x.getGDateValue()) <= 0) {
                            min = x.getGDateValue();
                        }
                    }
                    x = (XmlGMonthDay) sType.getFacet(SchemaType.FACET_MAX_INCLUSIVE);
                    if (x != null) {
                        max = x.getGDateValue();
                    }
                    x = (XmlGMonthDay) sType.getFacet(SchemaType.FACET_MAX_EXCLUSIVE);
                    if (x != null) {
                        if (max == null || max.compareToGDate(x.getGDateValue()) >= 0) {
                            max = x.getGDateValue();
                        }
                    }
                    break;
                }
            case SchemaType.BTC_G_DAY:
                {
                    XmlGDay x = (XmlGDay) sType.getFacet(SchemaType.FACET_MIN_INCLUSIVE);
                    if (x != null) {
                        min = x.getGDateValue();
                    }
                    x = (XmlGDay) sType.getFacet(SchemaType.FACET_MIN_EXCLUSIVE);
                    if (x != null) {
                        if (min == null || min.compareToGDate(x.getGDateValue()) <= 0) {
                            min = x.getGDateValue();
                        }
                    }
                    x = (XmlGDay) sType.getFacet(SchemaType.FACET_MAX_INCLUSIVE);
                    if (x != null) {
                        max = x.getGDateValue();
                    }
                    x = (XmlGDay) sType.getFacet(SchemaType.FACET_MAX_EXCLUSIVE);
                    if (x != null) {
                        if (max == null || max.compareToGDate(x.getGDateValue()) >= 0) {
                            max = x.getGDateValue();
                        }
                    }
                    break;
                }
            case SchemaType.BTC_G_MONTH:
                {
                    XmlGMonth x = (XmlGMonth) sType.getFacet(SchemaType.FACET_MIN_INCLUSIVE);
                    if (x != null) {
                        min = x.getGDateValue();
                    }
                    x = (XmlGMonth) sType.getFacet(SchemaType.FACET_MIN_EXCLUSIVE);
                    if (x != null) {
                        if (min == null || min.compareToGDate(x.getGDateValue()) <= 0) {
                            min = x.getGDateValue();
                        }
                    }
                    x = (XmlGMonth) sType.getFacet(SchemaType.FACET_MAX_INCLUSIVE);
                    if (x != null) {
                        max = x.getGDateValue();
                    }
                    x = (XmlGMonth) sType.getFacet(SchemaType.FACET_MAX_EXCLUSIVE);
                    if (x != null) {
                        if (max == null || max.compareToGDate(x.getGDateValue()) >= 0) {
                            max = x.getGDateValue();
                        }
                    }
                    break;
                }
        }
        if (min != null && max == null) {
            if (min.compareToGDate(gdateb) >= 0) {
                final Calendar c = gdateb.getCalendar();
                c.add(Calendar.HOUR_OF_DAY, pick(8));
                gdateb = new GDateBuilder(c);
            }
        } else if (min == null && max != null) {
            if (max.compareToGDate(gdateb) <= 0) {
                final Calendar c = gdateb.getCalendar();
                c.add(Calendar.HOUR_OF_DAY, 0 - pick(8));
                gdateb = new GDateBuilder(c);
            }
        } else if (min != null && max != null) {
            if (min.compareToGDate(gdateb) >= 0 || max.compareToGDate(gdateb) <= 0) {
                final Calendar c = min.getCalendar();
                final Calendar cmax = max.getCalendar();
                c.add(Calendar.HOUR_OF_DAY, 1);
                if (c.after(cmax)) {
                    c.add(Calendar.HOUR_OF_DAY, -1);
                    c.add(Calendar.MINUTE, 1);
                    if (c.after(cmax)) {
                        c.add(Calendar.MINUTE, -1);
                        c.add(Calendar.SECOND, 1);
                        if (c.after(cmax)) {
                            c.add(Calendar.SECOND, -1);
                            c.add(Calendar.MILLISECOND, 1);
                            if (c.after(cmax)) {
                                c.add(Calendar.MILLISECOND, -1);
                            }
                        }
                    }
                }
                gdateb = new GDateBuilder(c);
            }
        }
        gdateb.setBuiltinTypeCode(sType.getPrimitiveType().getBuiltinTypeCode());
        if (pick(2) == 0) {
            gdateb.clearTimeZone();
        }
        return gdateb.toString();
    }

    private SchemaType closestBuiltin(SchemaType sType) {
        while (!sType.isBuiltinType()) {
            sType = sType.getBaseType();
        }
        return sType;
    }

    /**
     * Cracks a combined QName of the form URL:localname
     */
    public static QName crackQName(final String qName) {
        String ns;
        String name;
        final int index = qName.lastIndexOf(':');
        if (index >= 0) {
            ns = qName.substring(0, index);
            name = qName.substring(index + 1);
        } else {
            ns = "";
            name = qName;
        }
        return new QName(ns, name);
    }

    /**
     * Cursor position:
     * Before this call:
     * <outer><foo/>^</outer>  (cursor at the ^)
     * After this call:
     * <<outer><foo/><bar/>som text<etc/>^</outer>
     */
    private void processParticle(final SchemaParticle sp, final XmlCursor xmlc, final boolean mixed, final List params, final boolean isRpcRequest) {
        int loop = determineMinMaxForSample(sp, xmlc);
        while (loop-- > 0) {
            switch(sp.getParticleType()) {
                case (SchemaParticle.ELEMENT):
                    processElement(sp, xmlc, mixed, params, isRpcRequest);
                    break;
                case (SchemaParticle.SEQUENCE):
                    processSequence(sp, xmlc, mixed, params, isRpcRequest);
                    break;
                case (SchemaParticle.CHOICE):
                    processChoice(sp, xmlc, mixed, params, isRpcRequest);
                    break;
                case (SchemaParticle.ALL):
                    processAll(sp, xmlc, mixed, params, isRpcRequest);
                    break;
                case (SchemaParticle.WILDCARD):
                    processWildCard(sp, xmlc, mixed);
                    break;
                default:
            }
        }
    }

    private int determineMinMaxForSample(final SchemaParticle sp, final XmlCursor xmlc) {
        final int minOccurs = sp.getIntMinOccurs();
        final int maxOccurs = sp.getIntMaxOccurs();
        if (minOccurs == maxOccurs) {
            return minOccurs;
        }
        int result = minOccurs;
        if (result == 0 && _nElements < MAX_ELEMENTS) {
            result = 1;
        }
        if (sp.getParticleType() != SchemaParticle.ELEMENT) {
            return result;
        }
        if (sp.getMaxOccurs() == null) {
            if (minOccurs == 0) {
                xmlc.insertComment("Zero or more repetitions:");
            } else {
                xmlc.insertComment(minOccurs + " or more repetitions:");
            }
        } else if (sp.getIntMaxOccurs() > 1) {
            xmlc.insertComment(minOccurs + " to " + String.valueOf(sp.getMaxOccurs()) + " repetitions:");
        }
        return result;
    }

    private String getItemNameOrType(final SchemaParticle sp, final XmlCursor xmlc) {
        String elementOrTypeName = null;
        if (sp.getParticleType() == SchemaParticle.ELEMENT) {
            elementOrTypeName = "Element (" + sp.getName().getLocalPart() + ")";
        } else {
            elementOrTypeName = printParticleType(sp.getParticleType());
        }
        return elementOrTypeName;
    }

    private void processElement(final SchemaParticle sp, final XmlCursor xmlc, final boolean mixed, final List params, final boolean isRpcRequest) {
        final SchemaLocalElement element = (SchemaLocalElement) sp;
        xmlc.insertElement(element.getName().getLocalPart(), element.getName().getNamespaceURI());
        _nElements++;
        xmlc.toPrevToken();
        createSampleForType(element.getName(), element.getType(), xmlc, params, isRpcRequest);
        xmlc.toNextToken();
    }

    private void moveToken(final int numToMove, final XmlCursor xmlc) {
        for (int i = 0; i < Math.abs(numToMove); i++) {
            if (numToMove < 0) {
                xmlc.toPrevToken();
            } else {
                xmlc.toNextToken();
            }
        }
    }

    private static final String formatQName(final XmlCursor xmlc, final QName qName) {
        final XmlCursor parent = xmlc.newCursor();
        parent.toParent();
        final String prefix = parent.prefixForNamespace(qName.getNamespaceURI());
        parent.dispose();
        String name;
        if (prefix == null || prefix.length() == 0) {
            name = qName.getLocalPart();
        } else {
            name = prefix + ":" + qName.getLocalPart();
        }
        return name;
    }

    private void processAttributes(final SchemaType stype, final XmlCursor xmlc, final boolean isRpcEncReq) {
        if (isRpcEncReq) {
            {
                final QName typeName = stype.getName();
                if (typeName != null) {
                    if (!typeName.getLocalPart().startsWith("XAWARE_XAWARE")) {
                        xmlc.insertAttributeWithValue(XSI_TYPE, formatQName(xmlc, typeName));
                    }
                }
            }
        }
        final SchemaProperty[] attrProps = stype.getAttributeProperties();
        for (int i = 0; i < attrProps.length; i++) {
            final SchemaProperty attr = attrProps[i];
            {
                if (SKIPPED_SOAP_ATTRS.contains(attr.getName())) {
                    continue;
                }
                if (isRpcEncReq) {
                    if (ENC_ARRAYTYPE.equals(attr.getName())) {
                        final SOAPArrayType arrayType = ((SchemaWSDLArrayType) stype.getAttributeModel().getAttribute(attr.getName())).getWSDLArrayType();
                        if (arrayType != null) {
                            xmlc.insertAttributeWithValue(attr.getName(), formatQName(xmlc, arrayType.getQName()) + arrayType.soap11DimensionString());
                        }
                        continue;
                    }
                }
            }
            final String defaultValue = attr.getDefaultText();
            xmlc.insertAttributeWithValue(attr.getName(), defaultValue == null ? sampleDataForSimpleType(attr.getType()) : defaultValue);
        }
    }

    private void processSequence(final SchemaParticle sp, final XmlCursor xmlc, final boolean mixed, final List params, final boolean isRpcRequest) {
        final SchemaParticle[] spc = sp.getParticleChildren();
        for (int i = 0; i < spc.length; i++) {
            processParticle(spc[i], xmlc, mixed, params, isRpcRequest);
            if (mixed && i < spc.length - 1) {
                xmlc.insertChars(pick(WORDS));
            }
        }
    }

    private void processChoice(final SchemaParticle sp, final XmlCursor xmlc, final boolean mixed, final List params, final boolean isRpcRequest) {
        final SchemaParticle[] spc = sp.getParticleChildren();
        xmlc.insertComment("You have a CHOICE of the next " + String.valueOf(spc.length) + " items at this level");
        for (int i = 0; i < spc.length; i++) {
            processParticle(spc[i], xmlc, mixed, params, isRpcRequest);
        }
    }

    private void processAll(final SchemaParticle sp, final XmlCursor xmlc, final boolean mixed, final List params, final boolean isRpcRequest) {
        final SchemaParticle[] spc = sp.getParticleChildren();
        for (int i = 0; i < spc.length; i++) {
            processParticle(spc[i], xmlc, mixed, params, isRpcRequest);
            if (mixed && i < spc.length - 1) {
                xmlc.insertChars(pick(WORDS));
            }
        }
    }

    private void processWildCard(final SchemaParticle sp, final XmlCursor xmlc, final boolean mixed) {
    }

    /**
     * This method will get the base type for the schema type
     */
    private static QName getClosestName(SchemaType sType) {
        while (sType.getName() == null) {
            sType = sType.getBaseType();
        }
        return sType.getName();
    }

    private String printParticleType(final int particleType) {
        final StringBuffer returnParticleType = new StringBuffer();
        returnParticleType.append("Schema Particle Type: ");
        switch(particleType) {
            case SchemaParticle.ALL:
                returnParticleType.append("ALL\n");
                break;
            case SchemaParticle.CHOICE:
                returnParticleType.append("CHOICE\n");
                break;
            case SchemaParticle.ELEMENT:
                returnParticleType.append("ELEMENT\n");
                break;
            case SchemaParticle.SEQUENCE:
                returnParticleType.append("SEQUENCE\n");
                break;
            case SchemaParticle.WILDCARD:
                returnParticleType.append("WILDCARD\n");
                break;
            default:
                returnParticleType.append("Schema Particle Type Unknown");
                break;
        }
        return returnParticleType.toString();
    }

    private final ArrayList _typeStack = new ArrayList();
}
