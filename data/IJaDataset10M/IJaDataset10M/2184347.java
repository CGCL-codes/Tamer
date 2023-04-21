package dbixx.sql;

import dbixx.Criteria;
import dbixx.CriteriaRelation;
import dbixx.AttributeCriteria;
import dbixx.Search;

/**
 * Parent for sql-queries containing a where-clause.
 *
 * @author Nikolaj Andresen
 * @version 1.0
 */
public abstract class WhereClause extends SQLQuery {

    protected Search search;

    protected java.text.SimpleDateFormat dateFormat;

    protected WhereClause(java.text.SimpleDateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    /**
     * Constructs this select query and returns it as a <code>String</code>.
     */
    public String toString() {
        String sql = "";
        if (search != null) {
            sql += " WHERE ";
            sql += this.inOrderTraverse(search);
        }
        return sql;
    }

    /**
     *
     */
    public void where(Search search) {
        this.search = search;
    }

    /** 
     * Generates infix sql query from search terms 
     *
     * This method recursively traverses a search tree in order. Each node is then visited.
     * Node visitation consists of escaping illegal sql names and creating a search term from
     * the node.
     *
     * @param s		the search tree to be traversed
     * @return		an sql search term build from the search tree
     * @author		Anders Borch
     */
    private String inOrderTraverse(Search s) {
        String sqlTerm = new String();
        if (s.getCriteria() instanceof AttributeCriteria) {
            AttributeCriteria ac = (AttributeCriteria) s.getCriteria();
            sqlTerm += ac.getAttribute();
            switch(ac.getOperator()) {
                case Search.EQUAL:
                    {
                        sqlTerm += " = ";
                        break;
                    }
                case Search.GREATEQ:
                    {
                        sqlTerm += " >= ";
                        break;
                    }
                case Search.GREATER:
                    {
                        sqlTerm += " > ";
                        break;
                    }
                case Search.LESS:
                    {
                        sqlTerm += " < ";
                        break;
                    }
                case Search.LESSEQ:
                    {
                        sqlTerm += " <= ";
                        break;
                    }
                case Search.NOTEQ:
                    {
                        sqlTerm += " != ";
                        break;
                    }
                default:
                    {
                        throw new IllegalArgumentException("inorderTraverse(): unknown operator");
                    }
            }
            try {
                int notused = Integer.parseInt(ac.getValue());
                sqlTerm += ac.getValue();
            } catch (NumberFormatException e) {
                sqlTerm += "'" + ac.getValue() + "'";
            }
        } else {
            CriteriaRelation cr = (CriteriaRelation) s.getCriteria();
            sqlTerm += " ( " + inOrderTraverse(s.getCriteria().getLeftChild()) + " ) ";
            switch(cr.getRelation()) {
                case Search.AND:
                    {
                        sqlTerm += " and ";
                        break;
                    }
                case Search.NAND:
                    {
                        sqlTerm = " not and ";
                        break;
                    }
                case Search.OR:
                    {
                        sqlTerm += " or ";
                        break;
                    }
                default:
                    {
                        throw new IllegalArgumentException("inOrderTraverse(): unknown operator");
                    }
            }
            sqlTerm += " ( " + inOrderTraverse(s.getCriteria().getRightChild()) + " ) ";
        }
        return sqlTerm;
    }
}
