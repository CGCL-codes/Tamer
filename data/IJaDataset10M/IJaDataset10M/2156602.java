package net.sf.tcpswitch.parser;

import java.util.ArrayList;
import net.sf.tcpswitch.ConnInfo;

public class PStatementList extends PStatement {

    private ArrayList statements;

    public PStatementList() {
        statements = new ArrayList();
    }

    public String show(String indent) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < statements.size(); i++) {
            PStatement statement = (PStatement) statements.get(i);
            result.append(statement.show(indent));
        }
        return result.toString();
    }

    public boolean add(PStatement statement) {
        return statements.add(statement);
    }

    public int forward(ConnInfo conn) {
        int result = EX_NEXT;
        for (int i = 0; i < statements.size(); i++) {
            PStatement statement = (PStatement) statements.get(i);
            result = statement.forward(conn);
            if (result != EX_NEXT) break;
        }
        return result;
    }
}
