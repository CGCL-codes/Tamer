package edu.vub.util.regexp;

final class RETokenRange extends REToken {

    private char lo, hi;

    private boolean insens;

    RETokenRange(int subIndex, char lo, char hi, boolean ins) {
        super(subIndex);
        insens = ins;
        this.lo = lo;
        this.hi = hi;
    }

    int getMinimumLength() {
        return 1;
    }

    int getMaximumLength() {
        return 1;
    }

    boolean match(CharIndexed input, REMatch mymatch) {
        char c = input.charAt(mymatch.index);
        if (c == CharIndexed.OUT_OF_BOUNDS) return false;
        boolean matches = (c >= lo) && (c <= hi);
        if (!matches && insens) {
            char c1 = Character.toLowerCase(c);
            matches = (c1 >= lo) && (c1 <= hi);
            if (!matches) {
                c1 = Character.toUpperCase(c);
                matches = (c1 >= lo) && (c1 <= hi);
            }
        }
        if (matches) {
            ++mymatch.index;
            return next(input, mymatch);
        }
        return false;
    }

    void dump(StringBuffer os) {
        os.append(lo).append('-').append(hi);
    }
}
