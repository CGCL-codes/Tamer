package cinema.helpers;

import java.util.*;

public class DSHelper {

    /**
	 * Format "{(Rambo,{(1900,{1,2,3,4,5})})},{(Rambo,{(1900,{1,2,3,4,5})})}"
	 * @param seatsString
	 * @return
	 */
    public static java.util.Hashtable<String, Hashtable<String, Vector<String>>> createSeats(String seatsString) {
        Tree parent = new Tree();
        createTree(0, seatsString, parent);
        java.util.Hashtable<String, Hashtable<String, Vector<String>>> table = createTable(parent.children);
        return table;
    }

    public static java.util.Hashtable<String, Vector<String>> createSessions(String sessions) {
        Tree parent = new Tree();
        createTree(0, sessions, parent);
        Hashtable<String, Vector<String>> table = createSubtable(parent.children);
        return table;
    }

    public static Vector<String> createVS(String string) {
        Tree parent = new Tree();
        createTree(0, string, parent);
        Vector<String> vector = new Vector<String>();
        for (Tree tree : parent.children.elementAt(0).children) {
            vector.add(tree.root.toString());
        }
        return vector;
    }

    private static Hashtable<String, Hashtable<String, Vector<String>>> createTable(Vector<Tree> children) {
        Hashtable<String, Hashtable<String, Vector<String>>> result = new Hashtable<String, Hashtable<String, Vector<String>>>();
        for (Tree entry : children) {
            String key = entry.children.elementAt(0).root.toString();
            Hashtable<String, Vector<String>> sub = createSubtable(entry.children.elementAt(1).children);
            result.put(key, sub);
        }
        return result;
    }

    private static Hashtable<String, Vector<String>> createSubtable(Vector<Tree> children) {
        Hashtable<String, Vector<String>> result = new Hashtable<String, Vector<String>>();
        for (Tree child : children) {
            String key = child.children.elementAt(0).root.toString();
            Vector<String> valuesV = new Vector<String>();
            for (Tree session : child.children.elementAt(1).children) {
                valuesV.add(session.root.toString());
            }
            result.put(key, valuesV);
        }
        return result;
    }

    private static int createTree(int lastIndex, String string, Tree parent) {
        Tree nt = new Tree();
        nt.root = null;
        parent.children.add(nt);
        while (lastIndex < string.length() && (string.charAt(lastIndex) != '}' && string.charAt(lastIndex) != ')')) {
            switch(string.charAt(lastIndex)) {
                case '{':
                    {
                        lastIndex = createTree(lastIndex + 1, string, nt);
                        break;
                    }
                case '(':
                    {
                        lastIndex = createTree(lastIndex + 1, string, nt);
                        break;
                    }
                case ',':
                    {
                        nt = new Tree();
                        nt.root = null;
                        parent.children.add(nt);
                        lastIndex++;
                        break;
                    }
                default:
                    {
                        int initial = lastIndex;
                        for (; lastIndex < string.length() && (Character.isDigit(string.charAt(lastIndex)) || Character.isLetter(string.charAt(lastIndex))); lastIndex++) ;
                        if (lastIndex < string.length()) nt.root = string.substring(initial, lastIndex);
                    }
            }
        }
        return lastIndex + 1;
    }

    ;

    public static void main(String args[]) {
        String seatsPerSession = "(Rambo,((1900,{1,2,3,4,5}))),(Rocky,((1900,{1,2,3,4,5}),(2000,{1,2,3,4,5,6})))";
        Hashtable<String, Hashtable<String, Vector<String>>> table = createSeats(seatsPerSession);
        System.err.println(table);
        String sessionsPerMovie = "(Rambo,{1900,2000}),(Rocky,{1900,2100})";
        Tree t = new Tree();
        createTree(0, sessionsPerMovie, t);
        Hashtable<String, Vector<String>> table1 = createSessions(sessionsPerMovie);
        System.err.println(table1);
        System.err.println(createVS("(1,2,3,4,5)"));
    }
}
