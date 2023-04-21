package snucse.teamd;

class ArbiterKey implements Comparable<ArbiterKey> {

    private Body b1, b2;

    public ArbiterKey(Body b1_, Body b2_) {
        if (b1_.toString().compareTo(b2_.toString()) > 0) {
            b1 = b1_;
            b2 = b2_;
        } else {
            b1 = b2_;
            b2 = b1_;
        }
    }

    @Override
    public int hashCode() {
        return b1.toString().hashCode() + 31 * b2.toString().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null || !(o instanceof ArbiterKey)) return false;
        ArbiterKey cp = ArbiterKey.class.cast(o);
        return b1.equals(cp.b1) && b2.equals(cp.b2);
    }

    @Override
    public String toString() {
        return "(" + b1.toString() + ";" + b2.toString() + ")";
    }

    @Override
    public int compareTo(ArbiterKey cp) {
        if (cp == this) return 0;
        int b1cmp = b1.toString().compareTo(cp.b1.toString());
        if (b1cmp != 0) return b1cmp;
        return b2.toString().compareTo(cp.b2.toString());
    }
}
