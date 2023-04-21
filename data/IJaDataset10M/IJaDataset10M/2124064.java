package org.vardb.motifs;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.vardb.util.CStringHelper;

public final class CAminoAcidRegexParser {

    private static final String AAS = "GALMFWKQESPVICYHRNDT";

    private static final String X = "X";

    private static final String AA_REGEX = "[" + AAS + "]";

    private static final String ANY_REGEX = "[" + AAS + X + "]";

    private static final String ALTERATION_REGEX = "\\(" + AA_REGEX + "(/" + AA_REGEX + ")+\\)";

    private static final String RANGE_REGEX = ANY_REGEX + "[0-9]+(-[0-9]+)?";

    private static final String REGEX = ALTERATION_REGEX + "|" + RANGE_REGEX + "|" + ANY_REGEX;

    private CAminoAcidRegexParser() {
    }

    public static String parse(String pattern) {
        System.out.println("pattern=" + pattern);
        List<String> tokens = tokenize(pattern);
        List<String> parts = new ArrayList<String>();
        for (String token : tokens) {
            System.out.println("token=" + token);
            if (token.length() == 1) parts.add(expand(token)); else if (token.indexOf('/') != -1) parts.add(parseAlteration(token)); else if (token.matches(RANGE_REGEX)) parts.add(parseRange(token));
        }
        return CStringHelper.join(parts, "");
    }

    private static String expand(String aa) {
        if (aa.equals(X)) return "[A-Z]"; else return aa;
    }

    private static String parseAlteration(String token) {
        token = token.substring(1);
        token = token.substring(0, token.length() - 1);
        StringBuilder buffer = new StringBuilder();
        buffer.append("[");
        for (String aa : CStringHelper.split(token, "/")) {
            buffer.append(aa);
        }
        buffer.append("]");
        return buffer.toString();
    }

    private static String parseRange(String token) {
        String aa = token.substring(0, 1);
        token = token.substring(1);
        int index = token.indexOf('-');
        if (index != -1) {
            int min = Integer.parseInt(token.substring(0, index));
            int max = Integer.parseInt(token.substring(index + 1));
            return expand(aa) + "{" + min + "," + max + "}";
        } else {
            int num = Integer.parseInt(token);
            return expand(aa) + "{" + num + "}";
        }
    }

    private static List<String> tokenize(String pattern) {
        List<String> tokens = new ArrayList<String>();
        Pattern pat = Pattern.compile(REGEX);
        Matcher matcher = pat.matcher(pattern);
        while (matcher.find()) {
            tokens.add(matcher.group());
        }
        return tokens;
    }
}
