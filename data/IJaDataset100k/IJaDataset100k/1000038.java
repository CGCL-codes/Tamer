package net.cordova.justus.progressparser.loaders;

import java.util.ArrayList;
import net.cordova.justus.progressparser.*;

@StatementLoader
public class CaseStatementLoader extends AbstractCommandLoader {

    public ArrayList<GenericProgressStatement> recognize(String command) {
        CaseStatement stmt = null;
        if (command.trim().toUpperCase().startsWith("CASE")) {
            stmt = new CaseStatement();
            stmt.setCommand(command);
        }
        ArrayList<GenericProgressStatement> vt = null;
        if (stmt != null) {
            vt = new ArrayList<GenericProgressStatement>();
            vt.add(stmt);
        }
        return vt;
    }

    public GenericProgressStatement[] getExamples() {
        CaseStatement example = new CaseStatement();
        example.setCommand("CASE value:");
        GenericProgressStatement[] ex = { example };
        return ex;
    }

    class CaseStatement extends GenericProgressStatement {

        public String getType() {
            return "Case";
        }
    }
}
