package org.xteam.cs.model.lex.test;

import org.xteam.cs.runtime.ILexerTables;
import org.xteam.cs.runtime.IStateAttributes;

public class LexerTableDump {

    public static void dumpTable(ILexerTables tables) {
        short[] charmap = tables.charMapTable();
        short[] rowmap = tables.rowMapTable();
        short[] transition = tables.transitionTable();
        short[] attributes = tables.attributeTable();
        for (int state = 0; state < rowmap.length; ++state) {
            System.out.print("=== state " + state + ":");
            int attr = attributes[state];
            if (attr != 0) {
                System.out.print(" [");
                if ((attr & IStateAttributes.FINAL) != 0) {
                    System.out.print("FINAL(" + tables.getAction(state) + ") ");
                }
                if ((attr & IStateAttributes.LOOKEND) != 0) {
                    System.out.print("LOOKEND ");
                }
                if ((attr & IStateAttributes.NOLOOK) != 0) {
                    System.out.print("NOLOOK ");
                }
                if ((attr & IStateAttributes.PUSHBACK) != 0) {
                    System.out.print("PUSHBACK ");
                }
                System.out.print("]");
            }
            System.out.println();
            for (int c = 0; c < charmap.length; ++c) {
                short next = transition[rowmap[state] + charmap[c]];
                if (next != -1) {
                    String x = String.valueOf((char) c);
                    if (c < 32) {
                        if (c == '\n') x = "\\n"; else if (c == '\t') x = "\\t"; else if (c == '\r') x = "\\r"; else if (c == '\b') x = "\\b"; else if (c == '\f') x = "\\f"; else {
                            x = "[" + c + "]";
                        }
                    }
                    System.out.println(x + " => " + next);
                }
            }
            System.out.println();
        }
    }
}
