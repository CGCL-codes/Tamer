package Absyn;

import java.io.Serializable;
import Symbol.Symbol;

/**
 * @author MaYunlei
 *
 */
public class ColName implements Serializable {

    public Symbol table;

    public Symbol col;

    public ColName(Symbol t, Symbol c) {
        table = t;
        col = c;
    }

    public ColName(String s, String s2) {
        table = new Symbol(s);
        col = new Symbol(s2);
    }

    public String toString() {
        String str = "";
        if (table != null && !table.toString().equals("")) str += table.toString() + ".";
        return str + col.toString();
    }
}
