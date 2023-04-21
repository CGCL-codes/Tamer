package org.ghutchis.reglib;

import java.util.ArrayList;

public class RCombBlock extends RComponent {

    ArrayList<String> statements;

    public RCombBlock() {
        statements = new ArrayList<String>();
    }

    void AddStatement(String statement) {
        statements.add(statement);
    }

    void InsertStatement(String statement) {
        statements.add(0, statement);
    }

    @Override
    public String toVerilog() {
        String result;
        result = "always @*\n" + "  begin\n";
        for (int s = 0; s < statements.size(); s++) {
            result += "    " + statements.get(s) + '\n';
        }
        result += "  end\n";
        return result;
    }
}
