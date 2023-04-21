package nuts.core.sql.criterion;

import nuts.core.lang.StringUtils;

/**
 * ParenExpression
 */
public class ParenExpression extends AbstractExpression {

    /**
	 * serialVersionUID
	 */
    private static final long serialVersionUID = 8614060804849294832L;

    protected String operator;

    /**
	 * Constructor
	 * @param operator	operator
	 */
    public ParenExpression(String operator) {
        setOperator(operator);
    }

    /**
	 * checkOperator
	 * @param operator operator
	 */
    protected void checkOperator(String operator) {
        if (StringUtils.isBlank(operator)) {
            throw new IllegalArgumentException("operator cannot be blank string");
        }
        if (!operator.equalsIgnoreCase(OPEN_PAREN) && !operator.equalsIgnoreCase(CLOSE_PAREN)) {
            throw new IllegalArgumentException("operator '" + operator + "' must be " + OPEN_PAREN + " | " + CLOSE_PAREN);
        }
    }

    /**
	 * @return the operator
	 */
    public String getOperator() {
        return operator;
    }

    /**
	 * @param operator the operator to set
	 */
    public void setOperator(String operator) {
        checkOperator(operator);
        this.operator = operator;
    }

    /**
	 * @see java.lang.Object#hashCode()
	 */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((operator == null) ? 0 : operator.hashCode());
        return result;
    }

    /**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        ParenExpression other = (ParenExpression) obj;
        if (operator == null) {
            if (other.operator != null) return false;
        } else if (!operator.equals(other.operator)) return false;
        return true;
    }

    /**
     * @return  a string representation of the object.
	 */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("operator: ");
        sb.append(operator);
        return sb.toString();
    }
}
