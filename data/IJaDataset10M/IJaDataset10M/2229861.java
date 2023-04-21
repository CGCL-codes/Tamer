package edu.vub.util.regexp;

final class RETokenNamedProperty extends REToken {

    String name;

    boolean insens;

    boolean negate;

    Handler handler;

    static final byte[] LETTER = new byte[] { Character.LOWERCASE_LETTER, Character.UPPERCASE_LETTER, Character.TITLECASE_LETTER, Character.MODIFIER_LETTER, Character.OTHER_LETTER };

    static final byte[] MARK = new byte[] { Character.NON_SPACING_MARK, Character.COMBINING_SPACING_MARK, Character.ENCLOSING_MARK };

    static final byte[] SEPARATOR = new byte[] { Character.SPACE_SEPARATOR, Character.LINE_SEPARATOR, Character.PARAGRAPH_SEPARATOR };

    static final byte[] SYMBOL = new byte[] { Character.MATH_SYMBOL, Character.CURRENCY_SYMBOL, Character.MODIFIER_SYMBOL, Character.OTHER_SYMBOL };

    static final byte[] NUMBER = new byte[] { Character.DECIMAL_DIGIT_NUMBER, Character.LETTER_NUMBER, Character.OTHER_NUMBER };

    static final byte[] PUNCTUATION = new byte[] { Character.DASH_PUNCTUATION, Character.START_PUNCTUATION, Character.END_PUNCTUATION, Character.CONNECTOR_PUNCTUATION, Character.OTHER_PUNCTUATION };

    static final byte[] OTHER = new byte[] { Character.CONTROL, Character.FORMAT, Character.PRIVATE_USE, Character.SURROGATE, Character.UNASSIGNED };

    RETokenNamedProperty(int subIndex, String name, boolean insens, boolean negate) throws REException {
        super(subIndex);
        this.name = name;
        this.insens = insens;
        this.negate = negate;
        handler = getHandler(name);
    }

    int getMinimumLength() {
        return 1;
    }

    int getMaximumLength() {
        return 1;
    }

    boolean match(CharIndexed input, REMatch mymatch) {
        char ch = input.charAt(mymatch.index);
        if (ch == CharIndexed.OUT_OF_BOUNDS) return false;
        boolean retval = handler.includes(ch);
        if (insens) {
            retval = retval || handler.includes(Character.toUpperCase(ch)) || handler.includes(Character.toLowerCase(ch));
        }
        if (negate) retval = !retval;
        if (retval) {
            ++mymatch.index;
            return next(input, mymatch);
        } else return false;
    }

    void dump(StringBuffer os) {
        os.append("\\").append(negate ? "P" : "p").append("{" + name + "}");
    }

    private abstract static class Handler {

        abstract boolean includes(char c);
    }

    private Handler getHandler(String name) throws REException {
        if (name.equals("Lower") || name.equals("Upper") || name.equals("Alpha") || name.equals("Digit") || name.equals("Alnum") || name.equals("Punct") || name.equals("Graph") || name.equals("Print") || name.equals("Blank") || name.equals("Cntrl") || name.equals("XDigit") || name.equals("Space")) {
            return new POSIXHandler(name);
        }
        if (name.startsWith("Is")) {
            name = name.substring(2);
        }
        if (name.equals("L")) return new UnicodeCategoriesHandler(LETTER);
        if (name.equals("M")) return new UnicodeCategoriesHandler(MARK);
        if (name.equals("Z")) return new UnicodeCategoriesHandler(SEPARATOR);
        if (name.equals("S")) return new UnicodeCategoriesHandler(SYMBOL);
        if (name.equals("N")) return new UnicodeCategoriesHandler(NUMBER);
        if (name.equals("P")) return new UnicodeCategoriesHandler(PUNCTUATION);
        if (name.equals("C")) return new UnicodeCategoriesHandler(OTHER);
        if (name.equals("Mc")) return new UnicodeCategoryHandler(Character.COMBINING_SPACING_MARK);
        if (name.equals("Pc")) return new UnicodeCategoryHandler(Character.CONNECTOR_PUNCTUATION);
        if (name.equals("Cc")) return new UnicodeCategoryHandler(Character.CONTROL);
        if (name.equals("Sc")) return new UnicodeCategoryHandler(Character.CURRENCY_SYMBOL);
        if (name.equals("Pd")) return new UnicodeCategoryHandler(Character.DASH_PUNCTUATION);
        if (name.equals("Nd")) return new UnicodeCategoryHandler(Character.DECIMAL_DIGIT_NUMBER);
        if (name.equals("Me")) return new UnicodeCategoryHandler(Character.ENCLOSING_MARK);
        if (name.equals("Pe")) return new UnicodeCategoryHandler(Character.END_PUNCTUATION);
        if (name.equals("Cf")) return new UnicodeCategoryHandler(Character.FORMAT);
        if (name.equals("Nl")) return new UnicodeCategoryHandler(Character.LETTER_NUMBER);
        if (name.equals("Zl")) return new UnicodeCategoryHandler(Character.LINE_SEPARATOR);
        if (name.equals("Ll")) return new UnicodeCategoryHandler(Character.LOWERCASE_LETTER);
        if (name.equals("Sm")) return new UnicodeCategoryHandler(Character.MATH_SYMBOL);
        if (name.equals("Lm")) return new UnicodeCategoryHandler(Character.MODIFIER_LETTER);
        if (name.equals("Sk")) return new UnicodeCategoryHandler(Character.MODIFIER_SYMBOL);
        if (name.equals("Mn")) return new UnicodeCategoryHandler(Character.NON_SPACING_MARK);
        if (name.equals("Lo")) return new UnicodeCategoryHandler(Character.OTHER_LETTER);
        if (name.equals("No")) return new UnicodeCategoryHandler(Character.OTHER_NUMBER);
        if (name.equals("Po")) return new UnicodeCategoryHandler(Character.OTHER_PUNCTUATION);
        if (name.equals("So")) return new UnicodeCategoryHandler(Character.OTHER_SYMBOL);
        if (name.equals("Zp")) return new UnicodeCategoryHandler(Character.PARAGRAPH_SEPARATOR);
        if (name.equals("Co")) return new UnicodeCategoryHandler(Character.PRIVATE_USE);
        if (name.equals("Zs")) return new UnicodeCategoryHandler(Character.SPACE_SEPARATOR);
        if (name.equals("Ps")) return new UnicodeCategoryHandler(Character.START_PUNCTUATION);
        if (name.equals("Cs")) return new UnicodeCategoryHandler(Character.SURROGATE);
        if (name.equals("Lt")) return new UnicodeCategoryHandler(Character.TITLECASE_LETTER);
        if (name.equals("Cn")) return new UnicodeCategoryHandler(Character.UNASSIGNED);
        if (name.equals("Lu")) return new UnicodeCategoryHandler(Character.UPPERCASE_LETTER);
        throw new REException("unsupported name " + name, REException.REG_ESCAPE, 0);
    }

    private static class POSIXHandler extends Handler {

        private RETokenPOSIX retoken;

        private REMatch mymatch = new REMatch(0, 0, 0);

        private char[] chars = new char[1];

        private CharIndexedCharArray ca = new CharIndexedCharArray(chars, 0);

        POSIXHandler(String name) {
            int posixId = RETokenPOSIX.intValue(name.toLowerCase());
            if (posixId != -1) retoken = new RETokenPOSIX(0, posixId, false, false); else throw new RuntimeException("Unknown posix ID: " + name);
        }

        boolean includes(char c) {
            chars[0] = c;
            mymatch.index = 0;
            return retoken.match(ca, mymatch);
        }
    }

    private static class UnicodeCategoryHandler extends Handler {

        UnicodeCategoryHandler(byte category) {
            this.category = (int) category;
        }

        private int category;

        boolean includes(char c) {
            return Character.getType(c) == category;
        }
    }

    private static class UnicodeCategoriesHandler extends Handler {

        UnicodeCategoriesHandler(byte[] categories) {
            this.categories = categories;
        }

        private byte[] categories;

        boolean includes(char c) {
            int category = Character.getType(c);
            for (int i = 0; i < categories.length; i++) if (category == categories[i]) return true;
            return false;
        }
    }
}
