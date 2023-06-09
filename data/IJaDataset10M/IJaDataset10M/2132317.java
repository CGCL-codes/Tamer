package org.armedbear.lisp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.io.StringReader;
import java.math.BigInteger;

public class CharacterInputStream extends LispInputStream {

    private final PushbackReader reader;

    protected int offset;

    protected int lineNumber;

    protected CharacterInputStream() {
        reader = null;
    }

    public CharacterInputStream(InputStream in) {
        reader = new PushbackReader(new BufferedReader(new InputStreamReader(in)), 2);
    }

    public int getOffset() {
        return offset;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public LispObject read(boolean eofError, LispObject eofValue, boolean recursive) throws ConditionThrowable {
        try {
            LispObject result = readPreservingWhitespace(eofError, eofValue, recursive);
            if (result != eofValue && !recursive) {
                if (ready()) {
                    int n = read();
                    if (n >= 0) {
                        char c = (char) n;
                        if (!Character.isWhitespace(c)) unread(c);
                    }
                }
            }
            return _READ_SUPPRESS_.symbolValueNoThrow() != NIL ? NIL : result;
        } catch (IOException e) {
            throw new ConditionThrowable(new StreamError(e));
        }
    }

    public LispObject readPreservingWhitespace(boolean eofError, LispObject eofValue, boolean recursive) throws ConditionThrowable {
        while (true) {
            int n;
            try {
                n = read();
            } catch (IOException e) {
                throw new ConditionThrowable(new StreamError(e));
            }
            if (n < 0) {
                if (eofError) throw new ConditionThrowable(new EndOfFileException()); else return eofValue;
            }
            char c = (char) n;
            if (Character.isWhitespace(c)) continue;
            LispObject result = processChar(c);
            if (result != null) return result;
        }
    }

    private LispObject processChar(char c) throws ConditionThrowable {
        switch(c) {
            case '"':
                return readString();
            case '\'':
                return readQuote();
            case '(':
                return readList();
            case ')':
                return readRightParen();
            case ';':
                return readComment();
            case ',':
                return readComma();
            case '`':
                return readBackquote();
            case '#':
                return readSharp();
            case '|':
                return getCurrentPackage().intern(readMultipleEscape());
            case ':':
                return readKeyword();
            default:
                return readToken(c);
        }
    }

    private String _readString() throws ConditionThrowable {
        try {
            StringBuffer sb = new StringBuffer();
            while (true) {
                int n = read();
                if (n < 0) throw new ConditionThrowable(new EndOfFileException());
                char c = (char) n;
                if (c == '\\') {
                    n = read();
                    if (n < 0) throw new ConditionThrowable(new EndOfFileException());
                    sb.append((char) n);
                    continue;
                }
                if (c == '"') break;
                sb.append(c);
            }
            return sb.toString();
        } catch (IOException e) {
            throw new ConditionThrowable(new StreamError(e));
        }
    }

    private LispString readString() throws ConditionThrowable {
        return new LispString(_readString());
    }

    private LispObject readPathname() throws ConditionThrowable {
        int n;
        try {
            n = read();
        } catch (IOException e) {
            throw new ConditionThrowable(new StreamError(e));
        }
        if (n < 0) throw new ConditionThrowable(new EndOfFileException());
        char nextChar = (char) n;
        if (nextChar == '"') return Pathname.parseNamestring(_readString());
        throw new ConditionThrowable(new TypeError("#p requires a string argument"));
    }

    private LispObject readQuote() throws ConditionThrowable {
        return new Cons(Symbol.QUOTE, new Cons(read(true, NIL, true)));
    }

    private LispObject readList() throws ConditionThrowable {
        try {
            Cons first = null;
            Cons last = null;
            while (true) {
                char c = flushWhitespace();
                if (c == ')') {
                    return first == null ? NIL : first;
                }
                if (c == '.') {
                    int n = read();
                    if (n < 0) throw new ConditionThrowable(new EndOfFileException());
                    char nextChar = (char) n;
                    if (isTokenDelimiter(nextChar)) {
                        if (last == null) throw new ConditionThrowable(new LispError("nothing appears before . in list"));
                        LispObject obj = read(true, NIL, true);
                        last.setCdr(obj);
                        continue;
                    } else {
                        unread(nextChar);
                    }
                }
                LispObject obj = processChar(c);
                if (obj == null) {
                    continue;
                }
                if (first == null) {
                    first = new Cons(obj);
                    last = first;
                } else {
                    Cons newCons = new Cons(obj);
                    last.setCdr(newCons);
                    last = newCons;
                }
            }
        } catch (IOException e) {
            throw new ConditionThrowable(new StreamError(e));
        }
    }

    private boolean isTokenDelimiter(char c) {
        switch(c) {
            case '"':
            case '\'':
            case '(':
            case ')':
            case ',':
            case ';':
            case '`':
                return true;
            default:
                return Character.isWhitespace(c);
        }
    }

    private LispObject readRightParen() throws ConditionThrowable {
        throw new ConditionThrowable(new LispError("unmatched right parenthesis"));
    }

    private LispObject readComment() throws ConditionThrowable {
        try {
            while (true) {
                int n = read();
                if (n < 0) return null;
                if (n == '\n') return null;
            }
        } catch (IOException e) {
            throw new ConditionThrowable(new StreamError(e));
        }
    }

    private LispObject readComma() throws ConditionThrowable {
        try {
            int n = read();
            if (n < 0) throw new ConditionThrowable(new EndOfFileException());
            char c = (char) n;
            switch(c) {
                case '@':
                    return new Cons(Symbol.COMMA_ATSIGN, new Cons(read(true, NIL, true), NIL));
                case '.':
                    return new Cons(Symbol.COMMA_DOT, new Cons(read(true, NIL, true), NIL));
                default:
                    unread(c);
                    return new Cons(Symbol.COMMA, new Cons(read(true, NIL, true), NIL));
            }
        } catch (IOException e) {
            throw new ConditionThrowable(new StreamError(e));
        }
    }

    private LispObject readBackquote() throws ConditionThrowable {
        return new Cons(Symbol.BACKQUOTE, new Cons(read(true, NIL, true)));
    }

    private LispObject readSharp() throws ConditionThrowable {
        try {
            int numArg = 0;
            char c;
            while (true) {
                int n = read();
                if (n < 0) throw new ConditionThrowable(new EndOfFileException());
                c = (char) n;
                if (c < '0' || c > '9') break;
                numArg = numArg * 10 + c - '0';
            }
            LispObject fun = getCurrentReadtable().getDispatchMacroCharacter('#', c);
            if (fun != NIL) {
                LispObject[] args = new LispObject[3];
                final LispThread thread = LispThread.currentThread();
                LispObject result = funcall3(fun, this, LispCharacter.getInstance(c), new Fixnum(numArg), thread);
                LispObject[] values = thread.getValues();
                if (values != null && values.length == 0) return null;
                return result;
            }
            switch(c) {
                case '\'':
                    return new Cons(Symbol.FUNCTION, new Cons(read(true, NIL, true)));
                case '(':
                    return new Vector(readList());
                case '\\':
                    return readCharacterLiteral();
                case '+':
                case '-':
                    return handleFeature(c);
                case ':':
                    return readUninternedSymbol();
                case '|':
                    skipBalancedComment();
                    return null;
                case '.':
                    return eval(read(true, NIL, true), new Environment(), LispThread.currentThread());
                case '*':
                    return readBitVector();
                case 'a':
                case 'A':
                    return readArray(numArg);
                case 'b':
                case 'B':
                    return readBinary();
                case 'c':
                case 'C':
                    return readComplex();
                case 'p':
                case 'P':
                    return readPathname();
                case 'x':
                case 'X':
                    return readHex();
                default:
                    return null;
            }
        } catch (IOException e) {
            throw new ConditionThrowable(new StreamError(e));
        }
    }

    private LispObject readCharacterLiteral() throws ConditionThrowable {
        try {
            int n = read();
            if (n < 0) throw new ConditionThrowable(new EndOfFileException());
            char c = (char) n;
            StringBuffer sb = new StringBuffer();
            sb.append(c);
            while (true) {
                n = read();
                if (n < 0) break;
                c = (char) n;
                if (Character.isWhitespace(c)) break;
                if (c == '(' || c == ')') {
                    unread(c);
                    break;
                }
                sb.append(c);
            }
            String token = sb.toString();
            if (token.length() == 1) return LispCharacter.getInstance(token.charAt(0));
            n = nameToChar(token);
            if (n >= 0) return LispCharacter.getInstance((char) n);
            throw new ConditionThrowable(new LispError("unrecognized character name: " + token));
        } catch (IOException e) {
            throw new ConditionThrowable(new StreamError(e));
        }
    }

    private LispObject handleFeature(char c) throws ConditionThrowable {
        LispObject feature = read(true, NIL, true);
        LispObject form = read(true, NIL, true);
        if (feature instanceof Symbol) {
            if (((Symbol) feature).getName().equalsIgnoreCase("armedbear")) {
                if (c == '+') return form; else return null;
            } else {
                if (c == '+') return null; else return form;
            }
        }
        return null;
    }

    private Symbol readUninternedSymbol() throws ConditionThrowable {
        try {
            int n = read();
            if (n < 0) throw new ConditionThrowable(new EndOfFileException());
            char c = (char) n;
            StringBuffer sb = new StringBuffer();
            if (c == '|') {
                while (true) {
                    n = read();
                    if (n < 0) throw new ConditionThrowable(new EndOfFileException());
                    c = (char) n;
                    if (c == '\\') {
                        n = read();
                        if (n < 0) throw new ConditionThrowable(new EndOfFileException());
                        sb.append((char) n);
                        continue;
                    }
                    if (c == '|') break;
                    sb.append(c);
                }
            } else {
                sb.append(Utilities.toUpperCase(c));
                while (true) {
                    n = read();
                    if (n < 0) break;
                    c = (char) n;
                    if (c == '\\') {
                        n = read();
                        if (n < 0) throw new ConditionThrowable(new EndOfFileException());
                        sb.append((char) n);
                        continue;
                    }
                    if (Character.isWhitespace(c)) break;
                    if (c == '(' || c == ')') {
                        unread(c);
                        break;
                    }
                    sb.append(Utilities.toUpperCase(c));
                }
            }
            return new Symbol(sb.toString());
        } catch (IOException e) {
            throw new ConditionThrowable(new StreamError(e));
        }
    }

    private void skipBalancedComment() throws ConditionThrowable {
        try {
            while (true) {
                int n = read();
                if (n < 0) return;
                if (n == '|') {
                    n = read();
                    if (n == '#') return; else unread(n);
                } else if (n == '#') {
                    n = read();
                    if (n == '|') skipBalancedComment(); else unread(n);
                }
            }
        } catch (IOException e) {
            throw new ConditionThrowable(new StreamError(e));
        }
    }

    private LispObject readBitVector() throws ConditionThrowable {
        try {
            StringBuffer sb = new StringBuffer();
            while (true) {
                int n = read();
                if (n < 0) break;
                char c = (char) n;
                if (c == '0' || c == '1') sb.append(c); else {
                    unread(c);
                    break;
                }
            }
            return new BitVector(sb.toString());
        } catch (IOException e) {
            throw new ConditionThrowable(new StreamError(e));
        }
    }

    private LispObject readArray(int rank) throws ConditionThrowable {
        LispObject obj = read(true, NIL, true);
        if (rank == 1) return new Vector(obj);
        return new Array(rank, obj);
    }

    private LispObject readComplex() throws ConditionThrowable {
        LispObject obj = read(true, NIL, true);
        if (obj instanceof Cons && obj.length() == 2) return Complex.getInstance(obj.car(), obj.cadr());
        throw new ConditionThrowable(new LispError("invalid complex number format #C" + obj));
    }

    private String readMultipleEscape() throws ConditionThrowable {
        try {
            StringBuffer sb = new StringBuffer();
            while (true) {
                int n = read();
                if (n < 0) break;
                char c = (char) n;
                if (c == '|') break;
                sb.append(c);
            }
            return sb.toString();
        } catch (IOException e) {
            throw new ConditionThrowable(new StreamError(e));
        }
    }

    private LispObject readKeyword() throws ConditionThrowable {
        try {
            StringBuffer sb = new StringBuffer();
            while (true) {
                int n = read();
                if (n < 0) break;
                char c = (char) n;
                if (Character.isWhitespace(c)) break;
                if (c == '(' || c == ')') {
                    unread(c);
                    break;
                }
                if (c == '|') {
                    sb.append(readMultipleEscape());
                    continue;
                }
                sb.append(Utilities.toUpperCase(c));
            }
            return PACKAGE_KEYWORD.intern(sb.toString());
        } catch (IOException e) {
            throw new ConditionThrowable(new StreamError(e));
        }
    }

    private LispObject readToken(char firstChar) throws ConditionThrowable {
        try {
            StringBuffer sb = new StringBuffer();
            sb.append(Utilities.toUpperCase(firstChar));
            while (true) {
                int n = read();
                if (n < 0) return makeObject(sb.toString());
                char c = (char) n;
                if (Character.isWhitespace(c)) return makeObject(sb.toString());
                switch(c) {
                    case '(':
                    case ')':
                        unread(c);
                        return makeObject(sb.toString());
                    default:
                        sb.append(Utilities.toUpperCase(c));
                }
            }
        } catch (IOException e) {
            throw new ConditionThrowable(new StreamError(e));
        }
    }

    private LispObject makeObject(String token) throws ConditionThrowable {
        final LispThread thread = LispThread.currentThread();
        if (_READ_SUPPRESS_.symbolValueNoThrow(thread) != NIL) return NIL;
        char c = token.charAt(0);
        if ("-+0123456789".indexOf(c) >= 0) {
            LispObject number = makeNumber(token);
            if (number != null) return number;
        }
        if (token.equals("T")) return T;
        if (token.equals("NIL")) return NIL;
        if (c == ':') return PACKAGE_KEYWORD.intern(token.substring(1));
        int index = token.indexOf("::");
        if (index > 0) {
            String packageName = token.substring(0, index);
            String symbolName = token.substring(index + 2);
            Package pkg = Packages.findPackage(packageName);
            if (pkg == null) throw new ConditionThrowable(new LispError("package \"" + packageName + "\" not found"));
            return pkg.intern(symbolName);
        }
        index = token.indexOf(':');
        if (index > 0) {
            String packageName = token.substring(0, index);
            String symbolName = token.substring(index + 1);
            Package pkg = Packages.findPackage(packageName);
            if (pkg == null) throw new ConditionThrowable(new PackageError("package \"" + packageName + "\" not found"));
            Symbol symbol = pkg.findExternalSymbol(symbolName);
            if (symbol != null) return symbol;
            if (pkg.findInternalSymbol(symbolName) != null) throw new ConditionThrowable(new LispError("symbol \"" + symbolName + "\" is not external in package " + packageName)); else throw new ConditionThrowable(new LispError("symbol \"" + symbolName + "\" not found in package " + packageName));
        }
        return ((Package) _PACKAGE_.symbolValueNoThrow(thread)).intern(token);
    }

    private LispObject makeNumber(String token) throws ConditionThrowable {
        if (token.indexOf('/') >= 0) return makeRatio(token);
        if (token.endsWith(".")) token = token.substring(0, token.length() - 1);
        LispObject number = makeFloat(token);
        if (number != null) return number;
        for (int i = token.length(); i-- > 1; ) {
            char c = token.charAt(i);
            if (c < '0' || c > '9') return null;
        }
        try {
            return new Fixnum(Integer.parseInt(token));
        } catch (NumberFormatException e) {
        }
        try {
            return new Bignum(new BigInteger(token));
        } catch (NumberFormatException e) {
        }
        return null;
    }

    private LispObject makeRatio(String token) throws ConditionThrowable {
        final int index = token.indexOf('/');
        if (index < 0) return null;
        try {
            BigInteger numerator = new BigInteger(token.substring(0, index));
            BigInteger denominator = new BigInteger(token.substring(index + 1));
            return number(numerator, denominator);
        } catch (NumberFormatException e) {
        }
        return null;
    }

    private LispObject makeFloat(String token) throws ConditionThrowable {
        final int length = token.length();
        if (length == 0) return null;
        StringBuffer sb = new StringBuffer();
        int i = 0;
        boolean maybe = false;
        char c = token.charAt(i);
        if (c == '-' || c == '+') {
            sb.append(c);
            ++i;
        }
        while (i < length) {
            c = token.charAt(i);
            if (c == '.' || (c >= '0' && c <= '9')) {
                if (c == '.') maybe = true;
                sb.append(c);
                ++i;
            } else break;
        }
        if (i < length) {
            if ("esfdlESFDL".indexOf(token.charAt(i)) >= 0) {
                maybe = true;
                sb.append('E');
                ++i;
            }
        }
        if (!maybe) return null;
        sb.append(token.substring(i));
        try {
            return new LispFloat(Double.parseDouble(sb.toString()));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private LispObject readBinary() throws ConditionThrowable {
        try {
            StringBuffer sb = new StringBuffer();
            while (true) {
                int n = read();
                if (n < 0) break;
                char c = (char) n;
                if (c == '0' || c == '1') sb.append(c); else {
                    unread(c);
                    break;
                }
            }
            String s = sb.toString();
            try {
                return new Fixnum(Integer.parseInt(s, 2));
            } catch (NumberFormatException e) {
            }
            try {
                return new Bignum(new BigInteger(s, 2));
            } catch (NumberFormatException e) {
            }
            throw new ConditionThrowable(new LispError());
        } catch (IOException e) {
            throw new ConditionThrowable(new StreamError(e));
        }
    }

    private LispObject readHex() throws ConditionThrowable {
        try {
            StringBuffer sb = new StringBuffer();
            while (true) {
                int n = read();
                if (n < 0) break;
                char c = (char) n;
                if (c >= '0' && c <= '9') sb.append(c); else if (c >= 'A' && c <= 'F') sb.append(c); else if (c >= 'a' && c <= 'f') sb.append(c); else {
                    unread(c);
                    break;
                }
            }
            String s = sb.toString();
            try {
                return new Fixnum(Integer.parseInt(s, 16));
            } catch (NumberFormatException e) {
            }
            try {
                return new Bignum(new BigInteger(s, 16));
            } catch (NumberFormatException e) {
            }
            throw new ConditionThrowable(new LispError());
        } catch (IOException e) {
            throw new ConditionThrowable(new StreamError(e));
        }
    }

    private char flushWhitespace() throws ConditionThrowable {
        try {
            while (true) {
                int n = read();
                if (n < 0) throw new ConditionThrowable(new EndOfFileException());
                char c = (char) n;
                if (!Character.isWhitespace(c)) return c;
            }
        } catch (IOException e) {
            throw new ConditionThrowable(new StreamError(e));
        }
    }

    public LispObject readLine(boolean eofError, LispObject eofValue) throws ConditionThrowable {
        StringBuffer sb = new StringBuffer();
        while (true) {
            try {
                int n = read();
                if (n < 0) {
                    if (sb.length() == 0) {
                        if (eofError) throw new ConditionThrowable(new EndOfFileException());
                        return eofValue;
                    }
                    LispObject[] values = new LispObject[2];
                    values[0] = new LispString(sb.toString());
                    values[1] = T;
                    LispThread.currentThread().setValues(values);
                    return values[0];
                }
                switch(n) {
                    case '\n':
                        {
                            LispObject[] values = new LispObject[2];
                            values[0] = new LispString(sb.toString());
                            values[1] = NIL;
                            LispThread.currentThread().setValues(values);
                            return values[0];
                        }
                    default:
                        sb.append((char) n);
                }
            } catch (IOException e) {
                throw new ConditionThrowable(new StreamError(e));
            }
        }
    }

    public LispObject readChar(boolean eofError, LispObject eofValue) throws ConditionThrowable {
        int n;
        try {
            n = read();
        } catch (IOException e) {
            throw new ConditionThrowable(new StreamError(e));
        }
        if (n < 0) {
            if (eofError) throw new ConditionThrowable(new EndOfFileException()); else return eofValue;
        }
        return LispCharacter.getInstance((char) n);
    }

    public LispObject unreadChar(LispCharacter c) throws ConditionThrowable {
        try {
            unread(c.getValue());
        } catch (IOException e) {
            throw new ConditionThrowable(new StreamError(e));
        }
        return NIL;
    }

    public LispObject clearInput() throws ConditionThrowable {
        try {
            while (ready()) read();
        } catch (IOException e) {
            throw new ConditionThrowable(new StreamError(e));
        }
        return NIL;
    }

    public LispObject close(LispObject abort) throws ConditionThrowable {
        try {
            reader.close();
            return T;
        } catch (IOException e) {
            throw new ConditionThrowable(new StreamError(e));
        }
    }

    protected int read() throws IOException {
        int n = reader.read();
        ++offset;
        if (n == '\n') ++lineNumber;
        return n;
    }

    protected void unread(int n) throws IOException {
        reader.unread(n);
        --offset;
        if (n == '\n') --lineNumber;
    }

    protected boolean ready() throws IOException {
        return reader.ready();
    }

    public String toString() {
        return unreadableString("STREAM [character input]");
    }
}
