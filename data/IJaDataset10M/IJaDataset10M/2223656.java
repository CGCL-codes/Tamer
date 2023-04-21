package net.sourceforge.jfunctions.text;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import net.sourceforge.jfunctions.functions.Aggregate;
import net.sourceforge.jfunctions.functions.FunctionToolkit;
import net.sourceforge.jfunctions.structures.StructureToolkit;

public class StringToolkit {

    public static String escape(String replacement) {
        return replacement.replaceAll("([\\.\\$\\^\\[\\]\\{\\}\\(\\)\\\\\\*\\+])", "\\\\$1");
    }

    private static final int TAG_BEGIN = 0;

    private static final int TAG_END = 1;

    private static final char[] TAG_BOUNDS = new char[] { '<', '>' };

    private static final Set<String> NEW_LINE_END_TAGS = Collections.unmodifiableSet(StructureToolkit.set("/title", "/h1", "/h2", "/h3", "/h4", "/h5", "/div", "/p", "/tr", "br/", "br"));

    private static final Set<String> NEW_TAB_END_TAGS = Collections.unmodifiableSet(StructureToolkit.set("/td", "/label"));

    public static String stripHTML(String message) {
        return stripHTML(message, true);
    }

    public static String stripHTML(String message, boolean fullPage) {
        StringBuilder builder = new StringBuilder();
        int token = TAG_BEGIN;
        int pos = 0;
        boolean stripTagContent = false;
        boolean allowAppend = fullPage || (message.indexOf("<body") < 0);
        while (pos < message.length()) {
            int next = message.indexOf(TAG_BOUNDS[token], pos);
            if (next == -1) next = message.length();
            String subst = message.substring(pos, next);
            switch(token) {
                case TAG_BEGIN:
                    {
                        if (!stripTagContent && allowAppend) {
                            for (int i = 0; i < subst.length(); i++) {
                                char c = subst.charAt(i);
                                append(builder, c == '\n' ? ' ' : c);
                            }
                        }
                        token = TAG_END;
                        break;
                    }
                case TAG_END:
                    {
                        subst = subst.trim().toLowerCase();
                        if (!stripTagContent && (subst.startsWith("script") || subst.startsWith("style") || subst.startsWith("option"))) {
                            stripTagContent = true;
                        } else if (stripTagContent && (subst.startsWith("/script") || subst.startsWith("/style") || subst.startsWith("option"))) {
                            stripTagContent = false;
                        } else if (allowAppend) {
                            if (NEW_LINE_END_TAGS.contains(subst)) {
                                append(builder, '\n');
                            } else if (NEW_TAB_END_TAGS.contains(subst)) {
                                append(builder, '\t');
                            }
                        } else if (subst.startsWith("body")) allowAppend = true;
                        token = TAG_BEGIN;
                    }
            }
            pos = next + 1;
        }
        return builder.toString();
    }

    private static void append(StringBuilder builder, char c) {
        int length = builder.length();
        if (length == 0) {
            if (!Character.isWhitespace(c)) {
                builder.append(c);
            }
            return;
        }
        char prev = builder.charAt(length - 1);
        if (c == '\n') {
            if (length == 1) {
                builder.append(c);
                return;
            }
            if (prev != '\n') {
                builder.append(c);
            }
            return;
        }
        if (!Character.isWhitespace(c) || !Character.isWhitespace(prev)) {
            builder.append(c);
        }
    }

    public static String csv(float[] items) {
        return concat(StructureToolkit.list(items), ",");
    }

    public static String csv(double[] items) {
        return concat(StructureToolkit.list(items), ",");
    }

    public static String csv(byte[] items) {
        return concat(StructureToolkit.list(items), ",");
    }

    public static String csv(short[] items) {
        return concat(StructureToolkit.list(items), ",");
    }

    public static String csv(int[] items) {
        return concat(StructureToolkit.list(items), ",");
    }

    public static String csv(long... items) {
        return concat(StructureToolkit.list(items), ",");
    }

    public static String csv(String... items) {
        return concat(new StringBuilder(), Arrays.asList(items), ",", true).toString();
    }

    public static String concat(Iterable<? extends Object> items) {
        return concat(new StringBuilder(), items, null, false).toString();
    }

    public static String concat(Iterable<? extends Object> items, String delimeter) {
        return concat(new StringBuilder(), items, delimeter, false).toString();
    }

    public static String concat(Iterable<? extends Object> items, String delimeter, boolean escape) {
        return concat(new StringBuilder(), items, delimeter, escape).toString();
    }

    public static StringBuilder concat(StringBuilder builder, Iterable<? extends Object> items, String delimeter, boolean escape) {
        boolean appended = false;
        boolean hasDelimeter = (delimeter != null) && (delimeter.length() > 0);
        for (Object item : items) {
            if (appended && hasDelimeter) builder.append(delimeter);
            String s = String.valueOf(item);
            builder.append(escape ? escape(s) : s);
            appended = true;
        }
        return builder;
    }

    public static <T> Aggregate<String, String> concat(String beforeFirst, String afterFirst, String before, String between, String after, String afterLast) {
        return new ConcatAggregate(beforeFirst, afterFirst, before, between, after, afterLast);
    }

    public static String concat(Iterable<String> iterable, String beforeFirst, String afterFirst, String before, String between, String after, String afterLast) {
        return concat(beforeFirst, afterFirst, before, between, after, afterLast).apply(iterable);
    }

    public static ContainsFilter contains(String contained) {
        return new ContainsFilter(contained);
    }

    public static boolean contains(String contained, String container) {
        return new ContainsFilter(contained).accept(container);
    }

    public static RegexFilter matches(String regex) {
        return new RegexFilter(regex);
    }

    public static boolean matches(String regex, String probe) {
        return new RegexFilter(regex).accept(probe);
    }

    public static <T> Iterable<String> format(Iterable<? extends T> iterable, Formatter<T> formatter) {
        return FunctionToolkit.transform(iterable, formatter);
    }

    public static boolean isEmpty(String value) {
        return (value == null) || value.isEmpty();
    }

    public static String javaName(String prefix, String name) {
        if (name.length() == 0) return prefix;
        StringBuilder builder = new StringBuilder(prefix).append(name);
        builder.setCharAt(0, Character.toLowerCase(builder.charAt(0)));
        if (prefix.length() > 0) {
            builder.setCharAt(prefix.length(), Character.toUpperCase(name.charAt(0)));
        }
        return builder.toString();
    }
}
