package parserMetrics.javaStructures;

public class ParDeMetodo {

    public Metodo m1;

    public Metodo m2;

    public ParDeMetodo(Metodo m1, Metodo m2) {
        this.m1 = m1;
        this.m2 = m2;
    }

    public boolean equals(Object o) {
        boolean ret = false;
        if (o instanceof ParDeMetodo) {
            ParDeMetodo p = (ParDeMetodo) o;
            if ((this.m1.equals(p.m1) && this.m2.equals(p.m2)) || (this.m1.equals(p.m2) && this.m2.equals(p.m1))) {
                ret = true;
            }
        }
        return ret;
    }

    public int hashCode() {
        return m1.hashCode() + m2.hashCode();
    }
}
