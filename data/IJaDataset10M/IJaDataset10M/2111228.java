package edu.berkeley.cs.db.yfilterplus.queryparser.xpathparser;

import java.util.*;

public class PathPredicate {

    ArrayList m_stepExprs;

    public PathPredicate(StepExpression expr) {
        m_stepExprs = new ArrayList();
        m_stepExprs.add(expr);
    }

    public void addStepExpr(StepExpression expr) {
        m_stepExprs.add(expr);
    }

    public ArrayList getStepExpressions() {
        return m_stepExprs;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[.");
        int size = m_stepExprs.size();
        for (int i = 0; i < size; i++) sb.append(m_stepExprs.get(i));
        sb.append("]");
        return sb.toString();
    }
}
