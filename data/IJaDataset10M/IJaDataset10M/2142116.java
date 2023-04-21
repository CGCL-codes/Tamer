package calendar;

class Symbols implements CalOpSymbols {

    static final int UNKOWN = -1;

    static final int CREATE = 11;

    static final int DROP = 12;

    static final int CALENDAR = 13;

    static final int ADD = 14;

    static final int GRAN = 15;

    static final int WITH = 16;

    static final int INTO = 17;

    static final int LAB_SCHEME = 18;

    static final int FOR = 19;

    static final int CONVERT = 20;

    static final int FROM = 21;

    static final int TO = 22;

    static final int OVERLAP = 23;

    static final int COVERING = 24;

    static final int COVERED_BY = 25;

    static final int IN = 26;

    static final int IDENT = 31;

    static final int INTEGER = 32;

    static final int LPAREN = 33;

    static final int RPAREN = 34;

    static final int COMMA = 35;

    static final int SEMICOLON = 36;

    static final int PLUS = 37;

    static final int MINUS = 38;

    static final int TIMES = 39;

    static final int DIVIDES = 40;

    static final int QUIT = 51;

    static final int AT = 52;

    static final int FILE_NAME = 53;

    static final int END_OF_INPUT = 100;

    static final int[] kwSymbol = { LAB_SCHEME, FOR, CONVERT, FROM, TO, OVERLAP, COVERING, COVERED_BY, IN, CREATE, DROP, CALENDAR, ADD, GRAN, WITH, INTO, GROUP, ALTER, SHIFT, SUBSET, SELECT_DOWN, SELECT_UP, SELECT_BY_OVERLAP, UNION, INTERSECT, DIFFERENCE, COMBINE, ANCHORED_GROUP, QUIT };

    static final String[] kwString = { "LAB_SCHEME", "FOR", "CONVERT", "FROM", "TO", "OVERLAP", "COVERING", "COVERED_BY", "IN", "CREATE", "DROP", "CALENDAR", "ADD", "GRAN", "WITH", "INTO", "GROUP", "ALTER", "SHIFT", "SUBSET", "SELECT_DOWN", "SELECT_UP", "SELECT_BY_OVERLAP", "UNION", "INTERSECT", "DIFFERENCE", "COMBINE", "ANCHORED_GROUP", "QUIT" };
}

class SymbolWithAttr extends Symbols {

    int type;

    String sValue;

    long iValue;
}

class CalParser extends Symbols {

    static final int maxDimen = 32;

    private String buffer = null;

    private SymbolWithAttr symbol = new SymbolWithAttr();

    private int curInput;

    private int index;

    private boolean divideZero;

    private ExecToken tokenStream;

    private ExecToken lastToken;

    private String granId;

    private String calId;

    private int granNum;

    boolean parse(String input) {
        buffer = input;
        curInput = 0;
        index = 0;
        divideZero = false;
        tokenStream = null;
        lastToken = null;
        granNum = 0;
        getSymbol();
        do {
            if (convert()) break; else if (createCalendar()) break; else if (dropCalendar()) break; else if (addGran()) break; else if (labScheme()) break; else if (quit()) break; else if (readFile()) break;
        } while (false);
        if (symbol.type == END_OF_INPUT) return (true);
        tokenStream = null;
        return (false);
    }

    ExecToken getTokenStream() {
        return (tokenStream);
    }

    private boolean quit() {
        if (symbol.type != QUIT) return (false);
        getSymbol();
        ExecToken t = new ExecToken();
        t.token = ExecToken.QUIT;
        appendToken(t);
        if (symbol.type == SEMICOLON) getSymbol();
        return (true);
    }

    private boolean readFile() {
        if (symbol.type != AT) return (false);
        getSymbol();
        if (symbol.type != IDENT && symbol.type != FILE_NAME) return (false);
        ExecToken t = new ExecToken();
        t.token = ExecToken.READ_FILE;
        t.fileName = symbol.sValue;
        getSymbol();
        if (symbol.type != SEMICOLON) return (false);
        appendToken(t);
        getSymbol();
        return (true);
    }

    private boolean createCalendar() {
        if (symbol.type != CREATE) return (false);
        getSymbol();
        if (symbol.type != CALENDAR) return (false);
        getSymbol();
        ExecToken t = new ExecToken();
        t.token = ExecToken.CREATE_CALENDAR;
        if (symbol.type != IDENT) return (false);
        t.calId = symbol.sValue;
        getSymbol();
        if (symbol.type != WITH) return (false);
        getSymbol();
        if (symbol.type != IDENT) return (false);
        t.gran1 = symbol.sValue;
        getSymbol();
        if (symbol.type != SEMICOLON) return (false);
        getSymbol();
        appendToken(t);
        return (true);
    }

    private boolean dropCalendar() {
        if (symbol.type != DROP) return (false);
        getSymbol();
        if (symbol.type != CALENDAR) return (false);
        getSymbol();
        ExecToken t = new ExecToken();
        t.token = ExecToken.DROP_CALENDAR;
        if (symbol.type != IDENT) return (false);
        t.calId = symbol.sValue;
        getSymbol();
        if (symbol.type != SEMICOLON) return (false);
        getSymbol();
        appendToken(t);
        return (true);
    }

    private boolean addGran() {
        if (symbol.type != ADD) return (false);
        getSymbol();
        if (symbol.type != GRAN) return (false);
        getSymbol();
        if (symbol.type != IDENT) return (false);
        granId = symbol.sValue;
        getSymbol();
        if (symbol.type != INTO) return (false);
        getSymbol();
        if (symbol.type != IDENT) return (false);
        calId = symbol.sValue;
        getSymbol();
        if (symbol.type != WITH) return (false);
        getSymbol();
        String[] newGran = new String[1];
        if (calExpr(newGran) == false) return (false);
        if (symbol.type != SEMICOLON) return (false);
        getSymbol();
        lastToken.newGran = granId;
        return (true);
    }

    private boolean calExpr(String[] thisId) {
        do {
            if (symbol.type == LPAREN) {
                getSymbol();
                if (calExpr(thisId) == false) return (false);
                if (symbol.type != RPAREN) return (false);
                getSymbol();
                break;
            } else if (groupSubsetShift(GROUP, thisId)) break; else if (alter(thisId)) break; else if (groupSubsetShift(SHIFT, thisId)) break; else if (groupSubsetShift(SUBSET, thisId)) break; else if (setOrSelect(SELECT_DOWN, thisId)) break; else if (setOrSelect(SELECT_UP, thisId)) break; else if (setOrSelect(SELECT_BY_OVERLAP, thisId)) break; else if (setOrSelect(UNION, thisId)) break; else if (setOrSelect(INTERSECT, thisId)) break; else if (setOrSelect(DIFFERENCE, thisId)) break;
            return (false);
        } while (false);
        return (true);
    }

    private boolean groupSubsetShift(int which, String[] thisId) {
        if (symbol.type != which) return (false);
        getSymbol();
        ExecToken t = new ExecToken();
        t.token = ExecToken.ADD_GRAN;
        t.calId = calId;
        t.operation = which;
        String[] opndId = new String[1];
        if (symbol.type != LPAREN) return (false);
        getSymbol();
        if (gran(opndId) == false) return (false);
        t.gran1 = opndId[0];
        if (symbol.type != COMMA) return (false);
        getSymbol();
        long[] p1 = new long[1], p2 = new long[1];
        if (intExpr(p1) == false) return (false);
        if (which == SUBSET) {
            if (symbol.type != COMMA) return (false);
            getSymbol();
            if (intExpr(p2) == false) return (false);
        }
        if (symbol.type != RPAREN) return (false);
        getSymbol();
        switch(which) {
            case GROUP:
            case SHIFT:
                t.m = p1[0];
                break;
            case SUBSET:
                t.m = p1[0];
                t.l_n = p2[0];
                break;
        }
        thisId[0] = "~$$" + granId + (++granNum);
        t.newGran = thisId[0];
        appendToken(t);
        return (true);
    }

    private boolean alter(String[] thisId) {
        if (symbol.type != ALTER) return (false);
        getSymbol();
        ExecToken t = new ExecToken();
        t.token = ExecToken.ADD_GRAN;
        t.calId = calId;
        t.operation = ALTER;
        String[] opndId = new String[1];
        if (symbol.type != LPAREN) return (false);
        getSymbol();
        if (gran(opndId) == false) return (false);
        t.gran1 = opndId[0];
        if (symbol.type != COMMA) return (false);
        getSymbol();
        if (gran(opndId) == false) return (false);
        t.gran2 = opndId[0];
        if (symbol.type != COMMA) return (false);
        getSymbol();
        long[] l = new long[1], k = new long[1], m = new long[1];
        if (intExpr(l) == false) return (false);
        if (symbol.type != COMMA) return (false);
        getSymbol();
        if (intExpr(k) == false) return (false);
        if (symbol.type != COMMA) return (false);
        getSymbol();
        if (intExpr(m) == false) return (false);
        if (symbol.type != RPAREN) return (false);
        getSymbol();
        t.l_n = l[0];
        t.k = k[0];
        t.m = m[0];
        thisId[0] = "~$$" + granId + (++granNum);
        t.newGran = thisId[0];
        appendToken(t);
        return (true);
    }

    private boolean setOrSelect(int opType, String[] thisId) {
        if (symbol.type != opType) return (false);
        getSymbol();
        ExecToken t = new ExecToken();
        t.token = ExecToken.ADD_GRAN;
        t.calId = calId;
        t.operation = opType;
        String[] opndId = new String[1];
        if (symbol.type != LPAREN) return (false);
        getSymbol();
        if (gran(opndId) == false) return (false);
        t.gran1 = opndId[0];
        if (symbol.type != COMMA) return (false);
        getSymbol();
        if (gran(opndId) == false) return (false);
        t.gran2 = opndId[0];
        if (opType == SELECT_DOWN || opType == SELECT_BY_OVERLAP) {
            long[] l = new long[1], k = new long[1];
            if (symbol.type != COMMA) return (false);
            getSymbol();
            if (intExpr(k) == false) return (false);
            if (symbol.type != COMMA) return (false);
            getSymbol();
            if (intExpr(l) == false) return (false);
            t.l_n = l[0];
            t.k = k[0];
        }
        if (symbol.type != RPAREN) return (false);
        getSymbol();
        thisId[0] = "~$$" + granId + (++granNum);
        t.newGran = thisId[0];
        appendToken(t);
        return (true);
    }

    private boolean labScheme() {
        if (symbol.type != LAB_SCHEME) return (false);
        getSymbol();
        ExecToken t = new ExecToken();
        t.token = ExecToken.LAB_SCHEME;
        if (symbol.type != IDENT) return (false);
        t.labSchemeId = symbol.sValue;
        getSymbol();
        if (symbol.type != IN) return (false);
        getSymbol();
        if (symbol.type != IDENT) return (false);
        t.calId = symbol.sValue;
        getSymbol();
        if (symbol.type != FOR) return (false);
        getSymbol();
        if (symbol.type != IDENT) return (false);
        t.newGran = symbol.sValue;
        getSymbol();
        if (symbol.type != WITH) return (false);
        getSymbol();
        String[] theScheme = new String[maxDimen];
        int[] dim = new int[1];
        if (!scheme(theScheme, dim)) return (false);
        t.labScheme = new String[dim[0]];
        for (int i = 0; i < dim[0]; ++i) t.labScheme[i] = theScheme[dim[0] - i - 1];
        if (symbol.type != SEMICOLON) return (false);
        getSymbol();
        appendToken(t);
        return (true);
    }

    private boolean scheme(String[] grans, int[] dim) {
        int i = 0;
        if (symbol.type != IDENT) return (false);
        grans[i++] = symbol.sValue;
        getSymbol();
        while (symbol.type == COMMA) {
            getSymbol();
            if (symbol.type != IDENT) return (false);
            grans[i++] = symbol.sValue;
            getSymbol();
        }
        dim[0] = i;
        return (true);
    }

    private boolean convert() {
        if (symbol.type != CONVERT) return (false);
        getSymbol();
        ExecToken t = new ExecToken();
        t.token = ExecToken.CONVERT;
        long[] theLabel = new long[maxDimen];
        int[] dim = new int[1];
        if (!label(theLabel, dim)) return (false);
        t.label = new Label(dim[0]);
        for (int i = 0; i < dim[0]; ++i) t.label.dim[i] = theLabel[dim[0] - i - 1];
        if (symbol.type != IN) return (false);
        getSymbol();
        if (symbol.type != IDENT) return (false);
        t.calId = symbol.sValue;
        getSymbol();
        if (symbol.type != FROM) return (false);
        getSymbol();
        if (symbol.type != IDENT) return (false);
        t.gran1 = symbol.sValue;
        getSymbol();
        if (symbol.type != TO) return (false);
        getSymbol();
        if (symbol.type != IDENT) return (false);
        t.gran2 = symbol.sValue;
        getSymbol();
        if (symbol.type != FOR) return (false);
        getSymbol();
        if (symbol.type == OVERLAP || symbol.type == COVERING || symbol.type == COVERED_BY) t.operation = symbol.type; else return (false);
        getSymbol();
        if (symbol.type != SEMICOLON) return (false);
        getSymbol();
        appendToken(t);
        return (true);
    }

    private boolean label(long[] theLabel, int[] dim) {
        int i = 0;
        if (symbol.type != LPAREN) return (false);
        getSymbol();
        if (symbol.type != INTEGER) return (false);
        theLabel[i++] = symbol.iValue;
        getSymbol();
        while (symbol.type != RPAREN) {
            if (symbol.type != COMMA) return (false);
            getSymbol();
            if (symbol.type != INTEGER) return (false);
            theLabel[i++] = symbol.iValue;
            getSymbol();
        }
        getSymbol();
        dim[0] = i;
        return (true);
    }

    private boolean gran(String[] thisId) {
        if (symbol.type == IDENT) {
            thisId[0] = symbol.sValue;
            getSymbol();
        } else if (symbol.type == LPAREN) {
            getSymbol();
            if (calExpr(thisId) == false) return (false);
            if (symbol.type != RPAREN) return (false);
            getSymbol();
        } else {
            if (calExpr(thisId) == false) return (false);
        }
        return (true);
    }

    private boolean intExpr(long[] value) {
        boolean negative = false;
        if (symbol.type == PLUS) getSymbol(); else if (symbol.type == MINUS) {
            getSymbol();
            negative = true;
        }
        if (intTerm(value) == false) return (false);
        if (negative) value[0] = -value[0];
        while (symbol.type == PLUS || symbol.type == MINUS) {
            int td = symbol.type;
            getSymbol();
            long[] newValue = new long[1];
            if (intTerm(newValue) == false) return (false);
            if (td == PLUS) value[0] += newValue[0]; else value[0] -= newValue[0];
        }
        return (true);
    }

    private boolean intTerm(long[] value) {
        if (intFactor(value) == false) return (false);
        while (symbol.type == TIMES || symbol.type == DIVIDES) {
            int td = symbol.type;
            getSymbol();
            long[] newValue = new long[1];
            if (intFactor(newValue) == false) return (false);
            if (td == TIMES) value[0] *= newValue[0]; else if (newValue[0] == 0) {
                divideZero = true;
                return (false);
            } else value[0] /= newValue[0];
        }
        return (true);
    }

    private boolean intFactor(long[] value) {
        if (symbol.type == INTEGER) {
            value[0] = symbol.iValue;
            getSymbol();
        } else if (symbol.type == LPAREN) {
            getSymbol();
            if (intExpr(value) == false) return (false);
            if (symbol.type != RPAREN) return (false);
            getSymbol();
        }
        return (true);
    }

    private void getSymbol() {
        char c;
        while (index < buffer.length()) {
            c = buffer.charAt(index);
            if (c == ' ' || c == '\t' || c == '\n' || c == '\r' || c == '\b') {
                ++index;
                continue;
            } else break;
        }
        if (index >= buffer.length()) {
            symbol.type = END_OF_INPUT;
            return;
        }
        curInput = index;
        c = buffer.charAt(index);
        if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
            while (++index < buffer.length()) {
                c = buffer.charAt(index);
                if (!((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || (c == '_'))) {
                    if (c == '.' || c == ':' || c == '\\' || c == '/') {
                        while (++index < buffer.length()) {
                            c = buffer.charAt(index);
                            if (!((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || (c == '_') || (c == '.') || (c == ':') || (c == '\\') || (c == '/'))) break;
                        }
                        StringBuffer t = new StringBuffer(index - curInput);
                        for (int i = curInput; i < index; ++i) t.append(buffer.charAt(i));
                        symbol.type = FILE_NAME;
                        symbol.sValue = t.toString();
                        return;
                    }
                    break;
                }
            }
            StringBuffer t = new StringBuffer(index - curInput);
            for (int i = curInput; i < index; ++i) t.append(Character.toUpperCase(buffer.charAt(i)));
            symbol.type = IDENT;
            symbol.sValue = t.toString();
            for (int i = 0; i < kwSymbol.length; ++i) if (symbol.sValue.compareTo(kwString[i]) == 0) {
                symbol.type = kwSymbol[i];
                symbol.sValue = null;
                break;
            }
        } else if (c >= '0' && c <= '9') {
            long t = (int) (c - '0');
            while (++index < buffer.length()) {
                c = buffer.charAt(index);
                if (c >= '0' && c <= '9') t = t * 10 + (int) (c - '0'); else break;
            }
            symbol.type = INTEGER;
            symbol.iValue = t;
        } else if (c == '(') {
            ++index;
            symbol.type = LPAREN;
        } else if (c == ')') {
            ++index;
            symbol.type = RPAREN;
        } else if (c == ';') {
            ++index;
            symbol.type = SEMICOLON;
        } else if (c == ',') {
            ++index;
            symbol.type = COMMA;
        } else if (c == '+') {
            ++index;
            symbol.type = PLUS;
        } else if (c == '-') {
            ++index;
            symbol.type = MINUS;
        } else if (c == '*') {
            ++index;
            symbol.type = TIMES;
        } else if (c == '/') {
            ++index;
            symbol.type = DIVIDES;
        } else if (c == '@') {
            ++index;
            symbol.type = AT;
        } else if (c == '#') {
            ++index;
            while ((c = buffer.charAt(index)) != '\n') ++index;
        } else {
            ++index;
            symbol.type = UNKOWN;
        }
    }

    private void appendToken(ExecToken t) {
        if (tokenStream == null) {
            tokenStream = t;
            lastToken = t;
        } else {
            lastToken.next = t;
            lastToken = t;
        }
    }
}
