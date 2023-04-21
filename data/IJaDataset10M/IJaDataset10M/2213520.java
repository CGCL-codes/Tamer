package net.sourceforge.freejava.string;

import java.util.AbstractList;
import java.util.List;
import java.util.regex.Matcher;

public class IndexVarSubst {

    public static String subst(String format, List<String> list) {
        return subst(format, list, null);
    }

    public static String subst(String format, List<String> list, String missing) {
        return subst(format, 0, list, missing);
    }

    public static String subst(String format, int indexBase, List<String> list, String missing) {
        int len = format.length();
        StringBuffer buffer = new StringBuffer(len);
        for (int i = 0; i < len; i++) {
            char c = format.charAt(i);
            if (i == len - 1) {
                buffer.append(c);
                break;
            }
            if (c == '\\') {
                buffer.append(c);
                buffer.append(format.charAt(++i));
                continue;
            }
            if (c == '$') {
                char look = format.charAt(++i);
                int index;
                if (look == '{') {
                    int start = i - 1;
                    int num = 0;
                    int end = i;
                    boolean err = false;
                    while (++end < len) {
                        char d = format.charAt(end);
                        if (d >= '0' && d <= '9') {
                            num = num * 10 + d - '0';
                            continue;
                        }
                        err = d != '}';
                        break;
                    }
                    if (err) {
                        buffer.append(format.substring(start, end));
                        i = end - 1;
                        continue;
                    }
                    index = num;
                    i = end;
                } else if (look >= '0' && look <= '9') {
                    index = look - '0';
                } else {
                    buffer.append(c);
                    buffer.append(look);
                    continue;
                }
                index -= indexBase;
                String s = (missing != null && index >= list.size()) ? missing : list.get(index);
                buffer.append(s);
                continue;
            }
            buffer.append(c);
        }
        return buffer.toString();
    }

    static class MatcherGroupList extends AbstractList<String> {

        private final Matcher matcher;

        public MatcherGroupList(Matcher matcher) {
            assert matcher != null;
            this.matcher = matcher;
        }

        @Override
        public String get(int index) {
            return matcher.group(index);
        }

        @Override
        public int size() {
            return matcher.groupCount();
        }
    }

    public static String subst(String format, Matcher matcher) {
        return subst(format, new MatcherGroupList(matcher));
    }

    public static String subst(String format, Matcher matcher, String missing) {
        return subst(format, new MatcherGroupList(matcher), missing);
    }
}
