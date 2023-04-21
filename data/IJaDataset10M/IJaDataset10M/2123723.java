package quotes;

public class SKIPPED {

    private static int hc = 0;

    public SKIPPED() {
        if (hc == 0) hc = super.hashCode();
    }

    public int hashCode() {
        return hc;
    }

    public boolean equals(Object obj) {
        return obj instanceof SKIPPED;
    }

    public String toString() {
        return "<SKIPPED>";
    }
}

;
