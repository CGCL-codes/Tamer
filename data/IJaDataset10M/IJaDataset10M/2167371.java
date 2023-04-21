package xmage.turbine.util;

public class Tri {

    public int idx = 0;

    public int v1 = 0;

    public int v2 = 0;

    public int v3 = 0;

    public Tri() {
    }

    public Tri(Tri tri) {
        idx = tri.idx;
        v1 = tri.v1;
        v2 = tri.v2;
        v3 = tri.v3;
    }

    public Tri(int idx, int v1, int v2, int v3) {
        this.idx = idx;
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }

    public void set(int idx, int v1, int v2, int v3) {
        this.idx = idx;
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + idx;
        result = PRIME * result + v1;
        result = PRIME * result + v2;
        result = PRIME * result + v3;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Tri other = (Tri) obj;
        if (idx != other.idx) return false;
        if (v1 != other.v1) return false;
        if (v2 != other.v2) return false;
        if (v3 != other.v3) return false;
        return true;
    }

    @Override
    public String toString() {
        return "[" + idx + ": " + v1 + ", " + v2 + ", " + v3 + "]";
    }
}
