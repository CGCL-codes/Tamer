package sisc.data;

import sisc.reader.CharUtil;
import java.io.*;
import sisc.io.ValueWriter;
import sisc.ser.Serializer;
import sisc.ser.Deserializer;

public class SchemeCharacter extends Value {

    public char c;

    public SchemeCharacter(char c) {
        this.c = c;
    }

    public void display(ValueWriter w) throws IOException {
        w.append(c);
    }

    public void write(ValueWriter w) throws IOException {
        String hr = CharUtil.charToNamedConst(this);
        w.append("#\\");
        if (hr != null) {
            w.append(hr);
        } else {
            if (c <= '~' && c >= ' ') w.append(c); else w.append(CharUtil.charToOct(c));
        }
    }

    public boolean eqv(Object v) {
        return (v instanceof SchemeCharacter && ((SchemeCharacter) v).c == c);
    }

    public int hashCode() {
        return (int) c;
    }

    public void serialize(Serializer s) throws IOException {
        s.writeChar(c);
    }

    public SchemeCharacter() {
    }

    public void deserialize(Deserializer s) throws IOException {
        c = s.readChar();
    }
}
