package net.sf.serialex.beans;

public class Deep2Bean {

    private int d2Int;

    private Deep3Bean deep3;

    public int getD2Int() {
        return d2Int;
    }

    public void setD2Int(int d2Int) {
        this.d2Int = d2Int;
    }

    public Deep3Bean getDeep3() {
        return deep3;
    }

    public void setDeep3(Deep3Bean deep3) {
        this.deep3 = deep3;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Deep2Bean deep2Bean = (Deep2Bean) o;
        if (d2Int != deep2Bean.d2Int) return false;
        if (deep3 != null ? !deep3.equals(deep2Bean.deep3) : deep2Bean.deep3 != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = d2Int;
        result = 31 * result + (deep3 != null ? deep3.hashCode() : 0);
        return result;
    }
}
