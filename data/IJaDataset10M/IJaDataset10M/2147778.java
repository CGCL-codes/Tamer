package lang.SQLQueryParserRoutine;

import lang.SQLQueryToken;

/**
 * User: [J0k3r]
* Date: 31.05.2008
* Time: 21:42:27
*/
public class SQLQueryExpressionElement {

    private Integer ID;

    private Object lOper;

    private Object rOper;

    private SQLQueryToken operation;

    private SQLQueryExpressionElement parent = null;

    private int state = 0;

    public SQLQueryExpressionElement(Integer ID, Object lOper, SQLQueryToken operation, Object rOper, SQLQueryExpressionElement parent) {
        this.ID = ID;
        this.lOper = lOper;
        this.operation = operation;
        this.rOper = rOper;
        this.parent = parent;
        if (this.lOper != null) this.state = 1;
        if (this.operation != null) this.state = 2;
        if (this.rOper != null) this.state = 3;
    }

    public Object getlOper() {
        return lOper;
    }

    public SQLQueryToken getOperation() {
        return operation;
    }

    public Object getrOper() {
        return rOper;
    }

    public void setlOper(Object lOper) {
        this.lOper = lOper;
        this.state = 1;
    }

    public void setOperation(SQLQueryToken operation) {
        this.operation = operation;
        this.state = 2;
    }

    public void setrOper(Object rOper) {
        this.rOper = rOper;
        this.state = 3;
    }

    public int getState() {
        return state;
    }

    public SQLQueryExpressionElement getParent() {
        return parent;
    }

    public String toString() {
        return "[" + this.getlOper() + "] " + this.getOperation() + " [" + this.getrOper() + "]";
    }

    public Integer getID() {
        return ID;
    }

    public void setParent(SQLQueryExpressionElement parent) {
        this.parent = parent;
    }
}
