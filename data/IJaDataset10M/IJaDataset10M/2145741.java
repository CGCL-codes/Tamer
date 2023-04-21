package com.controltier.ctl.tasks.session;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.types.RegularExpression;
import org.apache.tools.ant.util.regexp.Regexp;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ant.task name="session-keylist"
 */
public class SessionKeyList extends SessionGetInstance {

    protected String pattern;

    public void setPattern(final String regex) {
        pattern = regex;
    }

    protected String delimiter = ",";

    public void setDelimiter(final String delimiter) {
        this.delimiter = delimiter;
    }

    /**
     * reference to select property. defaults to <code>\0</code> back reference
     */
    protected String select = "\\0";

    public void setSelect(final String select) {
        this.select = select;
    }

    private Sort sort;

    /**
     * sort order
     *
     * @param sort order to sort by
     */
    public void setSort(final Sort sort) {
        this.sort = sort;
    }

    public void execute() {
        final Session session = getSessionInstance();
        final String keylist;
        if (null == pattern) {
            keylist = session.getKeyList(delimiter);
        } else {
            final Collection c = matchKeys(Pattern.compile(pattern), session.toMap());
            keylist = format(Sort.applyOptionalSort(sort, c), delimiter);
        }
        getProject().log("SessionKeyList.execute() : keylist: " + keylist, Project.MSG_DEBUG);
        storeResult(keylist);
    }

    private Collection matchKeys(final Pattern pattern, final Map map) {
        final Collection c = new ArrayList();
        for (Iterator keys = map.keySet().iterator(); keys.hasNext(); ) {
            final String key = (String) keys.next();
            final Matcher m = pattern.matcher(key);
            if (m.matches()) {
                if (null == select) {
                    c.add(key);
                } else {
                    String output = select;
                    getProject().log("processing select. groupCount: " + m.groupCount(), Project.MSG_DEBUG);
                    for (int i = 0; i <= m.groupCount(); i++) {
                        final String s = m.group(i);
                        getProject().log("i: " + i + ", s: " + s, Project.MSG_DEBUG);
                        final RegularExpression result = new RegularExpression();
                        result.setPattern("\\\\" + i);
                        final Regexp sregex = result.getRegexp(getProject());
                        output = sregex.substitute(output, s, Regexp.MATCH_DEFAULT);
                        getProject().log(i + ": output: " + output, Project.MSG_DEBUG);
                    }
                    c.add(output);
                }
            }
        }
        return c;
    }

    public static class Sort extends EnumeratedAttribute {

        static String ASCENDING = "ascending";

        static String DESCENDING = "descending";

        public String[] getValues() {
            return new String[] { ASCENDING, DESCENDING };
        }

        Collection sortAscending(final List c) {
            if (null == c) throw new IllegalArgumentException("list paramater was null");
            Collections.sort(c);
            return c;
        }

        Collection sortDescending(final List c) {
            sortAscending(c);
            Collections.reverse(c);
            return c;
        }

        static boolean shouldSort(final Sort sort) {
            return null != sort && (Sort.ASCENDING.equals(sort.getValue()) || Sort.DESCENDING.equals(sort.getValue()));
        }

        static Collection applyOptionalSort(final Sort sort, final Collection c) {
            if (shouldSort(sort)) {
                if (Sort.ASCENDING.equals(sort.getValue())) {
                    return sort.sortAscending(new ArrayList(c));
                } else if (Sort.DESCENDING.equals(sort.getValue())) {
                    sort.sortDescending(new ArrayList(c));
                }
            }
            return c;
        }
    }
}
