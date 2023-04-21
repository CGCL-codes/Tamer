package quotes;

public class NoVAL {

    private static int hc = 0;

    public NoVAL() {
        if (hc == 0) hc = super.hashCode();
    }

    public int hashCode() {
        return hc;
    }

    public boolean equals(Object obj) {
        return obj instanceof NoVAL;
    }

    public String toString() {
        return "<NoVAL>";
    }
}

;
