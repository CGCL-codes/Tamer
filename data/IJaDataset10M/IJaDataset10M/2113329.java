package net.sf.adatagenerator.pred;

import com.choicemaker.shared.util.CMReflectionUtils.ACCESSOR_PREFIX;

public class TrimmedStringsTestBean {

    public enum F {

        STRING("String", true), STRINGNULL("StringNull", false), STRINGBLANK("StringBlank", true), EXACTMATCHSTRING("ExactMatchString", true), STRINGFRED("StringFred", true), STRINGFREDDY("StringFreddy", true), STRINGFREDRICK("StringFredrick", true), STRINGARRAY("StringArray", false), STRINGARRAYNULL("StringArrayNull", false), STRINGARRAYFREDDY("StringArrayFreddy", false);

        public final String fieldName;

        public final boolean expected;

        private F(String stem, boolean b) {
            this.fieldName = ACCESSOR_PREFIX.GET.prefix + stem;
            this.expected = b;
        }
    }

    public final boolean heads;

    /** Always heads */
    public TrimmedStringsTestBean() {
        this(true);
    }

    /** Sets heads to specified value */
    public TrimmedStringsTestBean(boolean heads) {
        this.heads = heads;
    }

    /** Distance 0: Empty */
    public String getString() {
        return new String();
    }

    /** Distance: INVALID */
    public String getStringNull() {
        return null;
    }

    /** Distance 1: One or two blanks */
    public String getStringBlank() {
        return heads ? " " : "  ";
    }

    /** Distance 0 */
    public String getExactMatchString() {
        return "exact match";
    }

    public String getStringFred() {
        return heads ? "frEd" : " FRED ";
    }

    public String getStringFreddy() {
        return heads ? " freddy" : "FrEdDy ";
    }

    public String getStringFredrick() {
        return heads ? "fredrick " : " FREDRICK ";
    }

    public String[] getStringArray() {
        return new String[0];
    }

    public String[] getStringArrayNull() {
        return null;
    }

    public String[] getStringArrayFreddy() {
        return new String[] { "Fred", "Fredrick", "Freddy" };
    }
}
