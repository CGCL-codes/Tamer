package com.choicemaker.correlation.predicates;

import java.util.HashSet;
import java.util.Set;
import com.choicemaker.shared.util.CMReflectionUtils.ACCESSOR_PREFIX;

public class EditDistanceTestBean {

    public static int INVALID_FLAG = -1;

    public enum F {

        STRING("String", 0, 0), STRINGNULL("StringNull", INVALID_FLAG, INVALID_FLAG), STRINGBLANK("StringBlank", 1, 1), EXACTMATCHSTRING("ExactMatchString", 0, 0), STRINGFRED("StringFred", 2, 1), STRINGFREDDY("StringFreddy", 2, 2), MUSTAFA("Mustafa", 2, 2), MICHELLE("Michelle", 4, 4), CARLO("Carlo", 1, 1), JIMSMITH("JimSmith", 6, 6), JIMJOHNSMITH("JimJohnSmith", 5, 5), JOHNSMITH("JohnSmith", 4, 4), STRINGARRAY("StringArray", INVALID_FLAG, INVALID_FLAG), STRINGARRAYNULL("StringArrayNull", INVALID_FLAG, INVALID_FLAG), STRINGARRAYFREDDY("StringArrayFreddy", INVALID_FLAG, INVALID_FLAG);

        public final String fieldName;

        public final int d1;

        public final int d2;

        private F(String stem, int d1, int d2) {
            this.fieldName = ACCESSOR_PREFIX.GET.prefix + stem;
            this.d1 = d1;
            this.d2 = d2;
        }
    }

    public static Set<String> getMethodNamesFromEnum() {
        Set<String> validNames = new HashSet<String>();
        for (F v : F.values()) {
            validNames.add(v.fieldName);
        }
        return validNames;
    }

    public final boolean heads;

    /** Always heads */
    public EditDistanceTestBean() {
        this(true);
    }

    /** Sets heads to specified value */
    public EditDistanceTestBean(boolean heads) {
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

    /** Distance 1: fred or frde */
    public String getStringFred() {
        return heads ? "fred" : "frde";
    }

    /** Distance 4: Freddy or Feddyr */
    public String getStringFreddy() {
        return heads ? "Freddy" : "Feddyr";
    }

    /** Distance 2: Mustafa, Mustapha */
    public String getMustafa() {
        return heads ? "Mustafa" : "Mustapha";
    }

    /** Distance 4: Michelle, Maxwell */
    public String getMichelle() {
        return heads ? "Michelle" : "Maxwell";
    }

    /** Distance: Carlo, Carlos */
    public String getCarlo() {
        return heads ? "Carlo" : "Carlos";
    }

    /** Distance 6: (jimsmith), (smithjim) */
    public String getJimSmith() {
        return heads ? "jimsmith" : "smithjim";
    }

    /** Distance 5: (Jim John Smith),(Jim Smith) */
    public String getJimJohnSmith() {
        return heads ? "Jim John Smith" : "Jim Smith";
    }

    /** Distance 4: (John Smith),(Rob John Smith) */
    public String getJohnSmith() {
        return heads ? "John Smith" : "Rob John Smith";
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
