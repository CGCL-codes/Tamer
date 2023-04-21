package org.tolven.trim.ex;

import org.tolven.trim.PQ;

public class PQEx extends PQ {

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof PQ)) return false;
        PQ pq = (PQ) obj;
        if (!pq.getValue().equals(getValue())) return false;
        if (!pq.getUnit().equals(getUnit())) return false;
        return true;
    }

    @Override
    public int hashCode() {
        if (getValue() != null) return getValue().hashCode();
        return 0;
    }

    @Override
    public String toString() {
        if (this.getOriginalText() != null) return this.getOriginalText();
        StringBuffer sb = new StringBuffer(100);
        sb.append(getValue());
        sb.append(' ');
        sb.append(getUnit());
        return sb.toString();
    }

    @Override
    public String getOriginalText() {
        if (super.getOriginalText() == null) {
            if (getValue() == null) {
                return "";
            }
            return Double.toString(getValue());
        } else {
            return super.getOriginalText();
        }
    }
}
