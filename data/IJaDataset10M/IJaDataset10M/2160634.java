package org.geonetwork.domain.filter110;

public class Filter {

    Operator operator;

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((operator == null) ? 0 : operator.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Filter)) {
            return false;
        }
        Filter other = (Filter) obj;
        if (operator == null) {
            if (other.operator != null) return false;
        } else if (!operator.equals(other.operator)) return false;
        return true;
    }
}
