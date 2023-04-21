package com.x5.template.filters;

import com.x5.template.Chunk;
import com.x5.template.TextFilter;

public class SelectedFilter extends BasicFilter implements ChunkFilter {

    public String transformText(Chunk chunk, String text, String[] args) {
        return text == null ? null : selected(chunk, text, args);
    }

    public String getFilterName() {
        return "selected";
    }

    public String[] getFilterAliases() {
        return new String[] { "select", "sel" };
    }

    private static final String SELECTED_TOKEN = " selected=\"selected\" ";

    private static final String CHECKED_TOKEN = " checked=\"checked\" ";

    private static String selected(Chunk context, String text, String[] args) {
        return selected(context, text, args, SELECTED_TOKEN);
    }

    protected static String checked(Chunk context, String text, String[] args) {
        return selected(context, text, args, CHECKED_TOKEN);
    }

    private static String selected(Chunk context, String text, String[] args, String token) {
        if (args == null) return token;
        String testValue = args[0];
        if (args.length > 1) testValue = args[1];
        if (args.length > 2) token = args[2];
        if (testValue.charAt(0) == '~') {
            String xlation = testValue + "|onmatch(/^" + RegexFilter.escapeRegex(text) + "$/," + token + ")";
            return TextFilter.magicBraces(context, xlation);
        }
        if (text.equals(testValue)) {
            return token;
        } else {
            return "";
        }
    }
}
