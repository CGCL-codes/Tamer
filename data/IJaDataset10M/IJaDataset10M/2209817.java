package codebarre.com.google.zxing.client.result;

import codebarre.com.google.zxing.Result;
import java.util.Hashtable;
import java.util.Vector;

/**
 * <p>Abstract class representing the result of decoding a barcode, as more than
 * a String -- as some type of structured data. This might be a subclass which represents
 * a URL, or an e-mail address. {@link #parseResult(codebarre.com.google.zxing.Result)} will turn a raw
 * decoded string into the most appropriate type of structured representation.</p>
 *
 * <p>Thanks to Jeff Griffin for proposing rewrite of these classes that relies less
 * on exception-based mechanisms during parsing.</p>
 *
 * @author Sean Owen
 */
public abstract class ResultParser {

    public static ParsedResult parseResult(Result theResult) {
        ParsedResult result;
        if ((result = BookmarkDoCoMoResultParser.parse(theResult)) != null) {
            return result;
        } else if ((result = AddressBookDoCoMoResultParser.parse(theResult)) != null) {
            return result;
        } else if ((result = EmailDoCoMoResultParser.parse(theResult)) != null) {
            return result;
        } else if ((result = AddressBookAUResultParser.parse(theResult)) != null) {
            return result;
        } else if ((result = VCardResultParser.parse(theResult)) != null) {
            return result;
        } else if ((result = BizcardResultParser.parse(theResult)) != null) {
            return result;
        } else if ((result = VEventResultParser.parse(theResult)) != null) {
            return result;
        } else if ((result = EmailAddressResultParser.parse(theResult)) != null) {
            return result;
        } else if ((result = SMTPResultParser.parse(theResult)) != null) {
            return result;
        } else if ((result = TelResultParser.parse(theResult)) != null) {
            return result;
        } else if ((result = SMSMMSResultParser.parse(theResult)) != null) {
            return result;
        } else if ((result = SMSTOMMSTOResultParser.parse(theResult)) != null) {
            return result;
        } else if ((result = GeoResultParser.parse(theResult)) != null) {
            return result;
        } else if ((result = WifiResultParser.parse(theResult)) != null) {
            return result;
        } else if ((result = URLTOResultParser.parse(theResult)) != null) {
            return result;
        } else if ((result = URIResultParser.parse(theResult)) != null) {
            return result;
        } else if ((result = ISBNResultParser.parse(theResult)) != null) {
            return result;
        } else if ((result = ProductResultParser.parse(theResult)) != null) {
            return result;
        } else if ((result = ExpandedProductResultParser.parse(theResult)) != null) {
            return result;
        }
        return new TextParsedResult(theResult.getText(), null);
    }

    protected static void maybeAppend(String value, StringBuffer result) {
        if (value != null) {
            result.append('\n');
            result.append(value);
        }
    }

    protected static void maybeAppend(String[] value, StringBuffer result) {
        if (value != null) {
            for (int i = 0; i < value.length; i++) {
                result.append('\n');
                result.append(value[i]);
            }
        }
    }

    protected static String[] maybeWrap(String value) {
        return value == null ? null : new String[] { value };
    }

    protected static String unescapeBackslash(String escaped) {
        if (escaped != null) {
            int backslash = escaped.indexOf((int) '\\');
            if (backslash >= 0) {
                int max = escaped.length();
                StringBuffer unescaped = new StringBuffer(max - 1);
                unescaped.append(escaped.toCharArray(), 0, backslash);
                boolean nextIsEscaped = false;
                for (int i = backslash; i < max; i++) {
                    char c = escaped.charAt(i);
                    if (nextIsEscaped || c != '\\') {
                        unescaped.append(c);
                        nextIsEscaped = false;
                    } else {
                        nextIsEscaped = true;
                    }
                }
                return unescaped.toString();
            }
        }
        return escaped;
    }

    private static String urlDecode(String escaped) {
        if (escaped == null) {
            return null;
        }
        char[] escapedArray = escaped.toCharArray();
        int first = findFirstEscape(escapedArray);
        if (first < 0) {
            return escaped;
        }
        int max = escapedArray.length;
        StringBuffer unescaped = new StringBuffer(max - 2);
        unescaped.append(escapedArray, 0, first);
        for (int i = first; i < max; i++) {
            char c = escapedArray[i];
            if (c == '+') {
                unescaped.append(' ');
            } else if (c == '%') {
                if (i >= max - 2) {
                    unescaped.append('%');
                } else {
                    int firstDigitValue = parseHexDigit(escapedArray[++i]);
                    int secondDigitValue = parseHexDigit(escapedArray[++i]);
                    if (firstDigitValue < 0 || secondDigitValue < 0) {
                        unescaped.append('%');
                        unescaped.append(escapedArray[i - 1]);
                        unescaped.append(escapedArray[i]);
                    }
                    unescaped.append((char) ((firstDigitValue << 4) + secondDigitValue));
                }
            } else {
                unescaped.append(c);
            }
        }
        return unescaped.toString();
    }

    private static int findFirstEscape(char[] escapedArray) {
        int max = escapedArray.length;
        for (int i = 0; i < max; i++) {
            char c = escapedArray[i];
            if (c == '+' || c == '%') {
                return i;
            }
        }
        return -1;
    }

    private static int parseHexDigit(char c) {
        if (c >= 'a') {
            if (c <= 'f') {
                return 10 + (c - 'a');
            }
        } else if (c >= 'A') {
            if (c <= 'F') {
                return 10 + (c - 'A');
            }
        } else if (c >= '0') {
            if (c <= '9') {
                return c - '0';
            }
        }
        return -1;
    }

    protected static boolean isStringOfDigits(String value, int length) {
        if (value == null) {
            return false;
        }
        int stringLength = value.length();
        if (length != stringLength) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            char c = value.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    protected static boolean isSubstringOfDigits(String value, int offset, int length) {
        if (value == null) {
            return false;
        }
        int stringLength = value.length();
        int max = offset + length;
        if (stringLength < max) {
            return false;
        }
        for (int i = offset; i < max; i++) {
            char c = value.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    static Hashtable parseNameValuePairs(String uri) {
        int paramStart = uri.indexOf('?');
        if (paramStart < 0) {
            return null;
        }
        Hashtable result = new Hashtable(3);
        paramStart++;
        int paramEnd;
        while ((paramEnd = uri.indexOf('&', paramStart)) >= 0) {
            appendKeyValue(uri, paramStart, paramEnd, result);
            paramStart = paramEnd + 1;
        }
        appendKeyValue(uri, paramStart, uri.length(), result);
        return result;
    }

    private static void appendKeyValue(String uri, int paramStart, int paramEnd, Hashtable result) {
        int separator = uri.indexOf('=', paramStart);
        if (separator >= 0) {
            String key = uri.substring(paramStart, separator);
            String value = uri.substring(separator + 1, paramEnd);
            value = urlDecode(value);
            result.put(key, value);
        }
    }

    static String[] matchPrefixedField(String prefix, String rawText, char endChar, boolean trim) {
        Vector matches = null;
        int i = 0;
        int max = rawText.length();
        while (i < max) {
            i = rawText.indexOf(prefix, i);
            if (i < 0) {
                break;
            }
            i += prefix.length();
            int start = i;
            boolean done = false;
            while (!done) {
                i = rawText.indexOf((int) endChar, i);
                if (i < 0) {
                    i = rawText.length();
                    done = true;
                } else if (rawText.charAt(i - 1) == '\\') {
                    i++;
                } else {
                    if (matches == null) {
                        matches = new Vector(3);
                    }
                    String element = unescapeBackslash(rawText.substring(start, i));
                    if (trim) {
                        element = element.trim();
                    }
                    matches.addElement(element);
                    i++;
                    done = true;
                }
            }
        }
        if (matches == null || matches.isEmpty()) {
            return null;
        }
        return toStringArray(matches);
    }

    static String matchSinglePrefixedField(String prefix, String rawText, char endChar, boolean trim) {
        String[] matches = matchPrefixedField(prefix, rawText, endChar, trim);
        return matches == null ? null : matches[0];
    }

    static String[] toStringArray(Vector strings) {
        int size = strings.size();
        String[] result = new String[size];
        for (int j = 0; j < size; j++) {
            result[j] = (String) strings.elementAt(j);
        }
        return result;
    }
}
