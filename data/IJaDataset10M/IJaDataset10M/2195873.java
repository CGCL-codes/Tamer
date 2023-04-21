package org.hsqldb;

import java.math.BigDecimal;
import java.util.Locale;
import org.hsqldb.lib.IntValueHashMap;
import org.hsqldb.store.ValuePool;
import org.hsqldb.lib.java.JavaSystem;

/**
 * Provides the ability to tokenize SQL character sequences.
 *
 * Extensively rewritten and extended in successive versions of HSQLDB.
 *
 * @author Thomas Mueller (Hypersonic SQL Group)
 * @version 1.8.0
 * @since Hypersonic SQL
 */
public class Tokenizer {

    private static final int NO_TYPE = 0, NAME = 1, LONG_NAME = 2, SPECIAL = 3, NUMBER = 4, FLOAT = 5, STRING = 6, LONG = 7, DECIMAL = 8, BOOLEAN = 9, DATE = 10, TIME = 11, TIMESTAMP = 12, NULL = 13, NAMED_PARAM = 14;

    private static final int QUOTED_IDENTIFIER = 15, REMARK_LINE = 16, REMARK = 17;

    private String sCommand;

    private int iLength;

    private int iIndex;

    private int tokenIndex;

    private int nextTokenIndex;

    private int beginIndex;

    private int iType;

    private String sToken;

    private int indexLongNameFirst = -1;

    private String sLongNameFirst = null;

    private int typeLongNameFirst;

    private boolean retainFirst = false;

    private boolean bWait;

    private boolean lastTokenQuotedID;

    static IntValueHashMap valueTokens;

    static {
        valueTokens = new IntValueHashMap();
        valueTokens.put(Token.T_NULL, NULL);
        valueTokens.put(Token.T_TRUE, BOOLEAN);
        valueTokens.put(Token.T_FALSE, BOOLEAN);
    }

    public Tokenizer() {
    }

    public Tokenizer(String s) {
        sCommand = s;
        iLength = s.length();
        iIndex = 0;
    }

    public void reset(String s) {
        sCommand = s;
        iLength = s.length();
        iIndex = 0;
        tokenIndex = 0;
        nextTokenIndex = 0;
        beginIndex = 0;
        iType = NO_TYPE;
        typeLongNameFirst = NO_TYPE;
        sToken = null;
        indexLongNameFirst = -1;
        sLongNameFirst = null;
        bWait = false;
        lastTokenQuotedID = false;
        retainFirst = false;
    }

    /**
     *
     * @throws HsqlException
     */
    void back() throws HsqlException {
        if (bWait) {
            Trace.doAssert(false, "Querying state when in Wait mode");
        }
        nextTokenIndex = iIndex;
        iIndex = (indexLongNameFirst != -1) ? indexLongNameFirst : tokenIndex;
        bWait = true;
    }

    /**
     * get the given token or throw
     *
     * for commands and simple unquoted identifiers only
     *
     * @param match
     *
     * @throws HsqlException
     */
    String getThis(String match) throws HsqlException {
        getToken();
        matchThis(match);
        return sToken;
    }

    /**
     * for commands and simple unquoted identifiers only
     */
    void matchThis(String match) throws HsqlException {
        if (bWait) {
            Trace.doAssert(false, "Querying state when in Wait mode");
        }
        if (!sToken.equals(match) || iType == QUOTED_IDENTIFIER || iType == LONG_NAME) {
            String token = iType == LONG_NAME ? sLongNameFirst : sToken;
            throw Trace.error(Trace.UNEXPECTED_TOKEN, Trace.TOKEN_REQUIRED, new Object[] { token, match });
        }
    }

    void throwUnexpected() throws HsqlException {
        String token = iType == LONG_NAME ? sLongNameFirst : sToken;
        throw Trace.error(Trace.UNEXPECTED_TOKEN, token);
    }

    /**
     * Used for commands only
     *
     *
     * @param match
     */
    public boolean isGetThis(String match) throws HsqlException {
        getToken();
        if (iType != QUOTED_IDENTIFIER && iType != LONG_NAME && sToken.equals(match)) {
            return true;
        }
        back();
        return false;
    }

    /**
     * this methode is called before other wasXXX methods and takes
     * precedence
     */
    boolean wasValue() throws HsqlException {
        if (bWait) {
            Trace.doAssert(false, "Querying state when in Wait mode");
        }
        switch(iType) {
            case STRING:
            case NUMBER:
            case LONG:
            case FLOAT:
            case DECIMAL:
            case BOOLEAN:
            case NULL:
                return true;
            default:
                return false;
        }
    }

    boolean wasQuotedIdentifier() throws HsqlException {
        if (bWait) {
            Trace.doAssert(false, "Querying state when in Wait mode");
        }
        return lastTokenQuotedID;
    }

    boolean wasFirstQuotedIdentifier() throws HsqlException {
        if (bWait) {
            Trace.doAssert(false, "Querying state when in Wait mode");
        }
        return (typeLongNameFirst == QUOTED_IDENTIFIER);
    }

    /**
     * Method declaration
     *
     *
     * @return
     */
    boolean wasLongName() throws HsqlException {
        if (bWait) {
            Trace.doAssert(false, "Querying state when in Wait mode");
        }
        return iType == LONG_NAME;
    }

    /**
     * Simple Name means a quoted or unquoted identifier without
     * qualifiers provided it is not in the hKeyword list.
     *
     * @return
     */
    boolean wasSimpleName() throws HsqlException {
        if (bWait) {
            Trace.doAssert(false, "Querying state when in Wait mode");
        }
        if (iType == QUOTED_IDENTIFIER && sToken.length() != 0) {
            return true;
        }
        if (iType != NAME) {
            return false;
        }
        return !Token.isKeyword(sToken);
    }

    /**
     * checks whether the previously obtained token was a (named) parameter
     *
     * @return true if the previously obtained token was a (named) parameter
     */
    boolean wasParameter() throws HsqlException {
        Trace.doAssert(!bWait, "Querying state when in Wait mode");
        return (iType == NAMED_PARAM);
    }

    /**
     * Name means all quoted and unquoted identifiers plus any word not in the
     * hKeyword list.
     *
     * @return true if it's a name
     */
    boolean wasName() throws HsqlException {
        if (bWait) {
            Trace.doAssert(false, "Querying state when in Wait mode");
        }
        if (iType == QUOTED_IDENTIFIER) {
            return true;
        }
        if (iType != NAME && iType != LONG_NAME) {
            return false;
        }
        return !Token.isKeyword(sToken);
    }

    String getLongNamePre() throws HsqlException {
        return null;
    }

    /**
     * Return first part of long name
     *
     *
     * @return
     */
    String getLongNameFirst() throws HsqlException {
        if (bWait) {
            Trace.doAssert(false, "Querying state when in Wait mode");
        }
        return sLongNameFirst;
    }

    boolean wasSimpleToken() throws HsqlException {
        return iType != QUOTED_IDENTIFIER && iType != LONG_NAME && iType != STRING && iType != NAMED_PARAM;
    }

    String getSimpleToken() throws HsqlException {
        getToken();
        if (!wasSimpleToken()) {
            String token = iType == LONG_NAME ? sLongNameFirst : sToken;
            throw Trace.error(Trace.UNEXPECTED_TOKEN, token);
        }
        return sToken;
    }

    public boolean wasThis(String match) throws HsqlException {
        if (sToken.equals(match) && iType != QUOTED_IDENTIFIER && iType != LONG_NAME && iType != STRING) {
            return true;
        }
        return false;
    }

    /**
     * getName() is more broad than getSimpleName() in that it includes
     * 2-part names as well
     *
     * @return popped name
     * @throws HsqlException if next token is not an AName
     */
    public String getName() throws HsqlException {
        getToken();
        if (!wasName()) {
            throw Trace.error(Trace.UNEXPECTED_TOKEN, sToken);
        }
        return sToken;
    }

    /**
     * Returns a single, unqualified name (identifier)
     *
     * @return name
     * @throws HsqlException
     */
    public String getSimpleName() throws HsqlException {
        getToken();
        if (!wasSimpleName()) {
            String token = iType == LONG_NAME ? sLongNameFirst : sToken;
            throw Trace.error(Trace.UNEXPECTED_TOKEN, token);
        }
        return sToken;
    }

    /**
     * Return any token.
     *
     *
     * @return
     *
     * @throws HsqlException
     */
    public String getString() throws HsqlException {
        getToken();
        return sToken;
    }

    int getInt() throws HsqlException {
        long v = getBigint();
        if (v > Integer.MAX_VALUE || v < Integer.MIN_VALUE) {
            throw Trace.error(Trace.WRONG_DATA_TYPE, Types.getTypeString(getType()));
        }
        return (int) v;
    }

    static BigDecimal LONG_MAX_VALUE_INCREMENT = BigDecimal.valueOf(Long.MAX_VALUE).add(BigDecimal.valueOf(1));

    long getBigint() throws HsqlException {
        boolean minus = false;
        getToken();
        if (sToken.equals("-")) {
            minus = true;
            getToken();
        }
        Object o = getAsValue();
        int t = getType();
        switch(t) {
            case Types.INTEGER:
            case Types.BIGINT:
                break;
            case Types.DECIMAL:
                if (minus && LONG_MAX_VALUE_INCREMENT.equals(o)) {
                    return Long.MIN_VALUE;
                }
            default:
                throw Trace.error(Trace.WRONG_DATA_TYPE, Types.getTypeString(t));
        }
        long v = ((Number) o).longValue();
        return minus ? -v : v;
    }

    Object getInType(int type) throws HsqlException {
        getToken();
        Object o = getAsValue();
        int t = getType();
        if (t != type) {
            throw Trace.error(Trace.WRONG_DATA_TYPE, Types.getTypeString(t));
        }
        return o;
    }

    /**
     *
     *
     *
     * @return
     */
    public int getType() throws HsqlException {
        if (bWait) {
            Trace.doAssert(false, "Querying state when in Wait mode");
        }
        switch(iType) {
            case STRING:
                return Types.VARCHAR;
            case NUMBER:
                return Types.INTEGER;
            case LONG:
                return Types.BIGINT;
            case FLOAT:
                return Types.DOUBLE;
            case DECIMAL:
                return Types.DECIMAL;
            case BOOLEAN:
                return Types.BOOLEAN;
            case DATE:
                return Types.DATE;
            case TIME:
                return Types.TIME;
            case TIMESTAMP:
                return Types.TIMESTAMP;
            default:
                return Types.NULL;
        }
    }

    /**
     * Method declaration
     *
     *
     * @return
     *
     * @throws HsqlException
     */
    Object getAsValue() throws HsqlException {
        if (!wasValue()) {
            throw Trace.error(Trace.UNEXPECTED_TOKEN, sToken);
        }
        switch(iType) {
            case NULL:
                return null;
            case STRING:
                return sToken;
            case LONG:
                return ValuePool.getLong(Long.parseLong(sToken));
            case NUMBER:
                if (sToken.length() < 11) {
                    try {
                        return ValuePool.getInt(Integer.parseInt(sToken));
                    } catch (Exception e1) {
                    }
                }
                if (sToken.length() < 20) {
                    try {
                        iType = LONG;
                        return ValuePool.getLong(Long.parseLong(sToken));
                    } catch (Exception e2) {
                    }
                }
                iType = DECIMAL;
                return new BigDecimal(sToken);
            case FLOAT:
                double d = JavaSystem.parseDouble(sToken);
                long l = Double.doubleToLongBits(d);
                return ValuePool.getDouble(l);
            case DECIMAL:
                return new BigDecimal(sToken);
            case BOOLEAN:
                return sToken.equalsIgnoreCase("TRUE") ? Boolean.TRUE : Boolean.FALSE;
            case DATE:
                return HsqlDateTime.dateValue(sToken);
            case TIME:
                return HsqlDateTime.timeValue(sToken);
            case TIMESTAMP:
                return HsqlDateTime.timestampValue(sToken);
            default:
                return sToken;
        }
    }

    /**
     * return the current position to be used for VIEW processing
     *
     * @return
     */
    int getPosition() {
        return iIndex;
    }

    /**
     * mark the current position to be used for future getLastPart() calls
     *
     * @return
     */
    String getPart(int begin, int end) {
        return sCommand.substring(begin, end);
    }

    /**
     * mark the current position to be used for future getLastPart() calls
     *
     * @return
     */
    int getPartMarker() {
        return beginIndex;
    }

    /**
     * mark the current position to be used for future getLastPart() calls
     *
     */
    void setPartMarker() {
        beginIndex = iIndex;
    }

    /**
     * mark the position to be used for future getLastPart() calls
     *
     */
    void setPartMarker(int position) {
        beginIndex = position;
    }

    /**
     * return part of the command string from the last marked position
     *
     * @return
     */
    String getLastPart() {
        return sCommand.substring(beginIndex, iIndex);
    }

    /**
     * Method declaration
     *
     *
     * @throws HsqlException
     */
    private void getToken() throws HsqlException {
        if (bWait) {
            bWait = false;
            iIndex = nextTokenIndex;
            return;
        }
        if (!retainFirst) {
            sLongNameFirst = null;
            indexLongNameFirst = -1;
            typeLongNameFirst = NO_TYPE;
        }
        while (iIndex < iLength && Character.isWhitespace(sCommand.charAt(iIndex))) {
            iIndex++;
        }
        sToken = "";
        tokenIndex = iIndex;
        if (iIndex >= iLength) {
            iType = NO_TYPE;
            return;
        }
        char c = sCommand.charAt(iIndex);
        boolean point = false, digit = false, exp = false, afterexp = false;
        boolean end = false;
        char cfirst = 0;
        lastTokenQuotedID = false;
        if (Character.isJavaIdentifierStart(c)) {
            iType = NAME;
        } else if (Character.isDigit(c)) {
            iType = NUMBER;
            digit = true;
        } else {
            switch(c) {
                case '(':
                    sToken = Token.T_OPENBRACKET;
                    iType = SPECIAL;
                    iIndex++;
                    return;
                case ')':
                    sToken = Token.T_CLOSEBRACKET;
                    iType = SPECIAL;
                    iIndex++;
                    return;
                case ',':
                    sToken = Token.T_COMMA;
                    iType = SPECIAL;
                    iIndex++;
                    return;
                case '*':
                    sToken = Token.T_MULTIPLY;
                    iType = SPECIAL;
                    iIndex++;
                    return;
                case '=':
                    sToken = Token.T_EQUALS;
                    iType = SPECIAL;
                    iIndex++;
                    return;
                case ';':
                    sToken = Token.T_SEMICOLON;
                    iType = SPECIAL;
                    iIndex++;
                    return;
                case '+':
                    sToken = Token.T_PLUS;
                    iType = SPECIAL;
                    iIndex++;
                    return;
                case '%':
                    sToken = Token.T_PERCENT;
                    iType = SPECIAL;
                    iIndex++;
                    return;
                case '?':
                    sToken = Token.T_QUESTION;
                    iType = SPECIAL;
                    iIndex++;
                    return;
                case ':':
                    Trace.check(++iIndex < iLength, Trace.UNEXPECTED_END_OF_COMMAND);
                    c = sCommand.charAt(iIndex);
                    Trace.check(Character.isJavaIdentifierStart(c), Trace.INVALID_IDENTIFIER, ":" + c);
                    iType = NAMED_PARAM;
                    break;
                case '\"':
                    lastTokenQuotedID = true;
                    iType = QUOTED_IDENTIFIER;
                    iIndex++;
                    sToken = getString('"');
                    if (iIndex == sCommand.length()) {
                        return;
                    }
                    c = sCommand.charAt(iIndex);
                    if (c == '.') {
                        sLongNameFirst = sToken;
                        indexLongNameFirst = tokenIndex;
                        typeLongNameFirst = iType;
                        iIndex++;
                        if (retainFirst) {
                            throw Trace.error(Trace.THREE_PART_IDENTIFIER);
                        }
                        retainFirst = true;
                        getToken();
                        retainFirst = false;
                        iType = LONG_NAME;
                    }
                    return;
                case '\'':
                    iType = STRING;
                    iIndex++;
                    sToken = getString('\'');
                    return;
                case '!':
                case '<':
                case '>':
                case '|':
                case '/':
                case '-':
                    cfirst = c;
                    iType = SPECIAL;
                    break;
                case '.':
                    iType = DECIMAL;
                    point = true;
                    break;
                default:
                    throw Trace.error(Trace.UNEXPECTED_TOKEN, String.valueOf(c));
            }
        }
        int start = iIndex++;
        while (true) {
            if (iIndex >= iLength) {
                c = ' ';
                end = true;
                Trace.check(iType != STRING && iType != QUOTED_IDENTIFIER, Trace.UNEXPECTED_END_OF_COMMAND);
            } else {
                c = sCommand.charAt(iIndex);
            }
            switch(iType) {
                case NAMED_PARAM:
                case NAME:
                    if (Character.isJavaIdentifierPart(c)) {
                        break;
                    }
                    sToken = sCommand.substring(start, iIndex).toUpperCase(Locale.ENGLISH);
                    if (iType == NAMED_PARAM) {
                        return;
                    }
                    if (c == '.') {
                        typeLongNameFirst = iType;
                        sLongNameFirst = sToken;
                        indexLongNameFirst = tokenIndex;
                        iIndex++;
                        if (retainFirst) {
                            throw Trace.error(Trace.THREE_PART_IDENTIFIER);
                        }
                        retainFirst = true;
                        getToken();
                        retainFirst = false;
                        iType = LONG_NAME;
                    } else if (c == '(') {
                    } else {
                        int type = valueTokens.get(sToken, -1);
                        if (type != -1) {
                            iType = type;
                        }
                    }
                    return;
                case QUOTED_IDENTIFIER:
                case STRING:
                    break;
                case REMARK:
                    if (end) {
                        iType = NO_TYPE;
                        return;
                    } else if (c == '*') {
                        iIndex++;
                        if (iIndex < iLength && sCommand.charAt(iIndex) == '/') {
                            iIndex++;
                            getToken();
                            return;
                        }
                    }
                    break;
                case REMARK_LINE:
                    if (end) {
                        iType = NO_TYPE;
                        return;
                    } else if (c == '\r' || c == '\n') {
                        getToken();
                        return;
                    }
                    break;
                case SPECIAL:
                    if (c == '/' && cfirst == '/') {
                        iType = REMARK_LINE;
                        break;
                    } else if (c == '-' && cfirst == '-') {
                        iType = REMARK_LINE;
                        break;
                    } else if (c == '*' && cfirst == '/') {
                        iType = REMARK;
                        break;
                    } else if (c == '>' || c == '=' || c == '|') {
                        break;
                    }
                    sToken = sCommand.substring(start, iIndex);
                    return;
                case NUMBER:
                case FLOAT:
                case DECIMAL:
                    if (Character.isDigit(c)) {
                        digit = true;
                    } else if (c == '.') {
                        iType = DECIMAL;
                        if (point) {
                            throw Trace.error(Trace.UNEXPECTED_TOKEN, ".");
                        }
                        point = true;
                    } else if (c == 'E' || c == 'e') {
                        if (exp) {
                            throw Trace.error(Trace.UNEXPECTED_TOKEN, "E");
                        }
                        iType = FLOAT;
                        afterexp = true;
                        point = true;
                        exp = true;
                    } else if (c == '-' && afterexp) {
                        afterexp = false;
                    } else if (c == '+' && afterexp) {
                        afterexp = false;
                    } else {
                        afterexp = false;
                        if (!digit) {
                            if (point && start == iIndex - 1) {
                                sToken = ".";
                                iType = SPECIAL;
                                return;
                            }
                            throw Trace.error(Trace.UNEXPECTED_TOKEN, String.valueOf(c));
                        }
                        sToken = sCommand.substring(start, iIndex);
                        return;
                    }
            }
            iIndex++;
        }
    }

    private String getString(char quoteChar) throws HsqlException {
        try {
            int nextIndex = iIndex;
            boolean quoteInside = false;
            for (; ; ) {
                nextIndex = sCommand.indexOf(quoteChar, nextIndex);
                if (nextIndex < 0) {
                    throw Trace.error(Trace.UNEXPECTED_END_OF_COMMAND);
                }
                if (nextIndex < iLength - 1 && sCommand.charAt(nextIndex + 1) == quoteChar) {
                    quoteInside = true;
                    nextIndex += 2;
                    continue;
                }
                break;
            }
            char[] chBuffer = new char[nextIndex - iIndex];
            sCommand.getChars(iIndex, nextIndex, chBuffer, 0);
            int j = chBuffer.length;
            if (quoteInside) {
                j = 0;
                for (int i = 0; i < chBuffer.length; i++, j++) {
                    if (chBuffer[i] == quoteChar) {
                        i++;
                    }
                    chBuffer[j] = chBuffer[i];
                }
            }
            iIndex = ++nextIndex;
            return new String(chBuffer, 0, j);
        } catch (HsqlException e) {
            throw e;
        } catch (Exception e) {
            e.toString();
        }
        return null;
    }

    /**
     * Method declaration
     *
     *
     * @return
     */
    int getLength() {
        return iLength;
    }
}
