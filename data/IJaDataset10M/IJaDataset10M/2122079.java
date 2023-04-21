package edu.berkeley.cs.db.yfilterplus.queryparser;

import edu.berkeley.cs.db.yfilterplus.queryparser.xpathparser.SimplePredicate;
import java.io.PrintWriter;

public abstract class Predicate {

    protected int m_queryId;

    protected int m_pathId;

    protected int m_level;

    protected char m_predicateType;

    protected String m_attrName;

    protected char m_operator;

    protected int m_value = -1;

    protected String m_stringValue;

    public void setLevel(int level) {
        m_level = level;
    }

    public void updateLevel(int offSet) {
        m_level += offSet;
    }

    public String toString() {
        StringBuffer s = new StringBuffer();
        s.append(m_queryId);
        s.append("\t");
        s.append(m_pathId);
        s.append("\t");
        s.append(m_level);
        s.append("\t");
        s.append(m_predicateType);
        s.append("\t");
        if (m_predicateType == 'a') s.append(m_attrName); else if (m_predicateType == 'd') s.append("text()"); else if (m_predicateType == 'p') s.append("position()");
        s.append("\t");
        String op = SimplePredicate.getOperatorString(m_operator);
        if (op != null) {
            s.append(op);
            s.append("\t");
        }
        if (m_value != -1) s.append(m_value); else if (m_stringValue != null) {
            s.append("\"");
            s.append(m_stringValue);
            s.append("\"");
        }
        return s.toString();
    }

    public void print() {
        System.out.println(toString());
    }

    public void printToFile(PrintWriter out) {
        out.println(toString());
    }

    public int getLevel() {
        return m_level;
    }

    public int getQueryId() {
        return m_queryId;
    }

    public int getPathId() {
        return m_pathId;
    }

    public char getType() {
        return m_predicateType;
    }

    public String getAttrName() {
        if (m_predicateType == 'a') return m_attrName;
        return null;
    }

    public String getTargetName() {
        return m_attrName;
    }

    public char getOperator() {
        return m_operator;
    }

    public String getOperatorString() {
        return SimplePredicate.getOperatorString(m_operator);
    }

    public int getValue() {
        return m_value;
    }

    public String getStringValue() {
        if (m_stringValue != null) return m_stringValue; else if (m_value != -1) return String.valueOf(m_value);
        return null;
    }
}
