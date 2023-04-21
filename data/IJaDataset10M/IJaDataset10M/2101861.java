package hu.schmidtsoft.parser.expression;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class ExpLang {

    public static ExpLang parse(String expressions) throws IOException {
        ExpLang ret = new ExpLang();
        BufferedReader br = new BufferedReader(new StringReader(expressions));
        String line = br.readLine();
        int lineNumber = 0;
        while (line != null) {
            if (line.startsWith("@")) {
                ret.expId = line.substring(1);
            } else if (line.startsWith("$")) {
                ret.lowerId = line.substring(1);
            } else if (line.startsWith("%")) {
                ret.insertHere = line.substring(1);
            } else if (line.startsWith("#")) {
                ret.thisId = line.substring(1);
            } else if (line.startsWith("!")) {
                line = line.substring(1);
                int idx = line.indexOf(":");
                String num = line.substring(0, idx);
                Integer i = Integer.parseInt(num);
                line = line.substring(idx + 1);
                idx = line.indexOf(":");
                String id = line.substring(0, idx);
                line = line.substring(idx + 1);
                ret.addExp(i, new Exp(line, id));
            } else if (line.startsWith("//")) {
            } else if (line.length() > 0) {
                throw new IOException("Line is not empty and is not a valid command: " + lineNumber);
            }
            line = br.readLine();
            lineNumber++;
        }
        return ret;
    }

    public static void main(String[] args) {
        System.out.println(new ExpLang().renderExpLang());
    }

    String expId = "expression";

    String lowerId = "lower";

    String thisId = "this";

    public String insertHere = "INSERT_EXPRESSION_LANGUAGE_HERE";

    SortedMap<Integer, List<Exp>> map = new TreeMap<Integer, List<Exp>>();

    void addExp(Integer level, Exp exp) {
        List<Exp> l = map.get(level);
        if (l == null) {
            l = new ArrayList<Exp>();
            map.put(level, l);
        }
        l.add(exp);
    }

    public ExpLang() {
    }

    public ExpLang initDummy() {
        addExp(1, new Exp("(lower+plus)+lower", "expAdd"));
        addExp(1, new Exp("(lower+minus)+lower", "expMinus"));
        addExp(2, new Exp("(lower+multiple)+lower", "expMul"));
        addExp(3, new Exp("id", "constId"));
        addExp(3, new Exp("cString", "constString"));
        addExp(3, new Exp("(bracketOpen+expression)+bracketClose", "bracketed"));
        addExp(3, new Exp("id+((bracketOpen+(!|(expression+*1(comma+expression))))+bracketClose)", "fnCall"));
        return this;
    }

    String getLevelString(int level) {
        return expId + level + "lower";
    }

    String getLevelStringThis(int level) {
        return expId + level + "this";
    }

    public String renderExpLang() {
        StringBuffer ret = new StringBuffer();
        String prev = expId;
        int count = map.size();
        int ctr = 0;
        for (Map.Entry<Integer, List<Exp>> entry : map.entrySet()) {
            int level = entry.getKey();
            String levelString = getLevelString(level);
            String levelStringThis = getLevelStringThis(level);
            List<String> levelIds = new ArrayList<String>();
            for (Exp e : entry.getValue()) {
                String rule = e.rule;
                String id = e.id;
                rule = rule.replaceAll(lowerId, levelString);
                List<String> ors = new ArrayList<String>();
                String rule1 = rule.replaceAll(thisId, levelStringThis);
                String rule2 = rule.replaceAll(thisId, levelString);
                ors.add(rule1);
                if (!rule1.equals(rule2)) {
                    ors.add(rule2);
                }
                ret.append("// term: " + id + " of level: " + level + "\n");
                ret.append(id + ":=" + buildOrString(ors, true) + ";" + "\n");
                levelIds.add(id);
            }
            ctr++;
            boolean last = ctr == count;
            ret.append(levelStringThis + ":=" + buildOrString(levelIds, false) + ";\n");
            ret.append(prev + ":=" + levelStringThis);
            if (!last) {
                ret.append("|" + levelString);
            }
            ret.append(";\n");
            prev = levelString;
        }
        return ret.toString();
    }

    /**
	 * 
	 * @param li
	 * @return
	 */
    String buildOrString(List<String> li, boolean addBracket) {
        StringBuffer ret = new StringBuffer();
        StringBuffer pre = new StringBuffer();
        if (li.size() == 0) {
            addBracket = false;
        }
        appendOne(ret, li.get(0), addBracket);
        buildOrString(ret, li.subList(1, li.size()), pre, addBracket);
        pre.append(ret);
        return pre.toString();
    }

    void appendOne(StringBuffer ret, String s, boolean addBracket) {
        if (addBracket) {
            ret.append("(");
        }
        ret.append(s);
        if (addBracket) {
            ret.append(")");
        }
    }

    void buildOrString(StringBuffer ret, List<String> li, StringBuffer pre, boolean addBracket) {
        if (li.size() > 0) {
            pre.append("(");
            ret.append("|");
            appendOne(ret, li.get(0), addBracket);
            ret.append(")");
            buildOrString(ret, li.subList(1, li.size()), pre, addBracket);
        }
    }
}
