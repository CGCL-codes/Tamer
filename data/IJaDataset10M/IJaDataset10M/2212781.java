package edu.vub.util.regexp;

/**
 * @author Ito Kazumitsu
 */
final class RETokenIndependent extends REToken {

    REToken re;

    RETokenIndependent(REToken re) throws REException {
        super(0);
        this.re = re;
    }

    int getMinimumLength() {
        return re.getMinimumLength();
    }

    int getMaximumLength() {
        return re.getMaximumLength();
    }

    boolean match(CharIndexed input, REMatch mymatch) {
        if (re.match(input, mymatch)) {
            mymatch.next = null;
            return next(input, mymatch);
        }
        return false;
    }

    void dump(StringBuffer os) {
        os.append("(?>");
        re.dumpAll(os);
        os.append(')');
    }
}
