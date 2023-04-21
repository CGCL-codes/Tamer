package gnu.regexp;

class RETokenChar extends REToken {

    private char[] ch;

    private boolean insens;

    RETokenChar(int f_subIndex, char c, boolean ins) {
        super(f_subIndex);
        ch = new char[1];
        ch[0] = (insens = ins) ? Character.toLowerCase(c) : c;
    }

    int getMinimumLength() {
        return ch.length;
    }

    int[] match(CharIndexed input, int index, int eflags, REMatch mymatch) {
        int z = ch.length;
        char c;
        for (int i = 0; i < z; i++) {
            c = input.charAt(index + i);
            if (((insens) ? Character.toLowerCase(c) : c) != ch[i]) return null;
        }
        return next(input, index + z, eflags, mymatch);
    }

    boolean chain(REToken next) {
        if (next instanceof RETokenChar) {
            RETokenChar cnext = (RETokenChar) next;
            int newsize = ch.length + cnext.ch.length;
            char[] chTemp = new char[newsize];
            System.arraycopy(ch, 0, chTemp, 0, ch.length);
            System.arraycopy(cnext.ch, 0, chTemp, ch.length, cnext.ch.length);
            ch = chTemp;
            return false;
        } else return super.chain(next);
    }

    void dump(StringBuffer os) {
        os.append(ch);
    }
}
