package regexp;

/** A Regexp subclass that handles group references of the form \1 \2
    \3 constructs. */
class GroupReference extends Regexp {

    int n;

    GroupReference(Regexp prev, int n) {
        super(prev);
        this.n = n;
    }

    public String toStringThis() {
        return "\\" + new Character((char) ('0' + n));
    }

    Regexp advance(State state) {
        String group;
        try {
            group = state.getGroupString(n);
        } catch (NoSuchMatchException e) {
            return null;
        }
        int cnt = group.length();
        if (state.charsLeft() < cnt) return null;
        int offset = state.offset;
        int i = 0;
        while (--cnt >= 0) if (group.charAt(i++) != state.getchar(offset++)) return null;
        state.offset = offset;
        return next;
    }

    void backup(State s) {
        try {
            int len = s.getGroupLength(n);
            s.offset -= len;
        } catch (NoSuchMatchException e) {
        }
    }
}
