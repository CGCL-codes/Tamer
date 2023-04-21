package edu.berkeley.cs.db.yfilterplus.queryparser.xpathparser;

import java.util.*;

public class StepExpression {

    public static char UNKNOWN = 'u';

    public static char CHILD = 'c';

    public static char DESCENDANT = 'd';

    char m_axis = UNKNOWN;

    String m_nameTest = null;

    ArrayList m_predicates = null;

    public StepExpression(String nameTest) {
        m_nameTest = nameTest;
    }

    public StepExpression(String nameTest, ArrayList predicates) {
        m_nameTest = nameTest;
        m_predicates = predicates;
    }

    public void setAxis(char axis) {
        m_axis = axis;
    }

    public char getAxis() {
        return m_axis;
    }

    public String getNameTest() {
        return m_nameTest;
    }

    public ArrayList getPredicates() {
        return m_predicates;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (m_axis == CHILD) sb.append("/"); else if (m_axis == DESCENDANT) sb.append("//"); else if (m_axis == UNKNOWN) sb.append("?");
        sb.append(m_nameTest);
        int size = 0;
        if (m_predicates != null) size = m_predicates.size();
        for (int i = 0; i < size; i++) sb.append(m_predicates.get(i));
        return sb.toString();
    }
}
