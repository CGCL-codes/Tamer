package sisc.data;

import java.io.*;
import sisc.io.ValueWriter;
import sisc.reader.*;
import sisc.ser.Serializer;
import sisc.ser.Deserializer;

public class Symbol extends Value {

    public static Symbol getUnique(String str) {
        return new Symbol(str);
    }

    /**
     * Interns the symbol with the given name.
     * @return the value of the symbol, or null if it is not defined
     */
    public static Symbol intern(String str) {
        return MemoizedSymbol.intern(str);
    }

    /**
     * Retrieves the value of the symbol with the given name.
     * Equivalent to <code>get(str,false)</code>.
     * @return the value of the symbol, or null if it is not defined
     */
    public static Symbol get(String str) {
        return get(str, false);
    }

    /**
     * Retrieves the value of the symbol with the given name.
     * @param str the name of the symbol
     * @param caseSensitive true if the case of the symbol name
     * is to be respected
     * @return the value of the symbol, or null if it is not defined
     */
    public static Symbol get(String str, boolean caseSensitive) {
        return intern(caseSensitive ? str : str.toLowerCase());
    }

    public String symval;

    public Symbol(String symval) {
        this.symval = symval;
    }

    public Symbol normalize() {
        return Symbol.get(symval.toLowerCase());
    }

    public void display(ValueWriter w) throws IOException {
        w.append(symval);
    }

    private void slashify(ValueWriter w, boolean protectedLiteral) throws IOException {
        for (int i = 0; i < symval.length(); i++) {
            char c = symval.charAt(i);
            if (protectedLiteral) {
                if (c == '|' || !Lexer.isPrintable(c)) w.append('\\').append(CharUtil.charToEscaped(c)); else w.append(c);
            } else {
                if (!Lexer.isIdentifierSubsequent(c)) {
                    w.append('\\').append(CharUtil.charToEscaped(c));
                } else w.append(c);
            }
        }
    }

    public int valueHashCode() {
        return symval.hashCode();
    }

    public boolean valueEqual(Value v) {
        return super.valueEqual(v) || ((v instanceof Symbol) && ((Symbol) v).symval.equals(symval));
    }

    public void write(ValueWriter w) throws IOException {
        if ((w.caseSensitive() || symval.toLowerCase().equals(symval)) && !Lexer.contains(symval, Lexer.special_and_reserved) && (Parser.isPeculiarIdentifier(symval) || (symval.length() > 0 && Lexer.isIdentifierStart(symval.charAt(0))))) slashify(w, false); else {
            w.append('|');
            slashify(w, true);
            w.append('|');
        }
    }

    public Symbol() {
    }

    public void serialize(Serializer s) throws IOException {
        s.writeUTF(symval);
    }

    public void deserialize(Deserializer s) throws IOException {
        symval = s.readUTF();
    }
}
