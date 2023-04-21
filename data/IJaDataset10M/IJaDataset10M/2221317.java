package codesheet.parser;

import codesheet.classes.Function;
import codesheet.classes.ScopeNode;
import java.util.Vector;
import luaparser.LUA_PT.Token;

public class ScopeChecker {

    public static boolean isInScope(Function f, Token t) {
        ScopeNode start = t.getNode();
        ScopeNode max = f.getStartNode();
        Vector nodesToProcess = new Vector();
        if (f.hasArgument(t.toString())) return true;
        while (start.getParent() != null && start.equals(max) == false) {
            if (!nodesToProcess.contains(start)) nodesToProcess.add(start);
            start = start.getParent();
        }
        for (int i = 0; i < nodesToProcess.size(); i++) {
            ScopeNode toProcess = (ScopeNode) nodesToProcess.get(i);
            Object declarator = toProcess.getTokenDeclaratorInScope(t);
            if (declarator != null) {
                if (toProcess.isTokenInProperPlaceInScope(t, declarator)) return true;
            }
        }
        return false;
    }

    private static void printScopeTree(ScopeNode root) {
        if (root.hasChildren()) {
            for (int i = 0; i < root.getChildren().size(); i++) {
                ScopeNode child = (ScopeNode) root.getChildren().get(i);
                StringBuffer buf = new StringBuffer();
                for (int x = 0; x < child.getLevel(); x++) buf.append(" ");
                System.err.println(child.getLevel() + buf.toString() + " " + child.toString() + " --> " + child.getParent());
                printScopeTree(child);
            }
        } else {
        }
    }
}
