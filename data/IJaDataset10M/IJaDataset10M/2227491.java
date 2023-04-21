package com.neoworks.util;

import java.util.List;
import java.util.LinkedList;
import java.util.StringTokenizer;

/**
 * StringDecorator decorates Strings, like this:
 *
 *  ************
 *  * A String *
 *  ************
 *
 * @author Nick Vincent (<a href="mailto:nick@neoworks.com">nick@neoworks.com</a>)
 */
public class StringDecorator {

    /** Creates a new instance of StringDecorator */
    public StringDecorator() {
    }

    public static String boxAroundString(String string, String title) {
        StringTokenizer st = new StringTokenizer(string, "\n");
        List l = new LinkedList();
        while (st.hasMoreTokens()) {
            l.add(st.nextToken());
        }
        return boxAroundStrings((String[]) l.toArray(new String[0]), title);
    }

    public static String boxAroundStrings(String[] strings, String title) {
        StringBuffer sb = new StringBuffer();
        int decorationLength = 8;
        int maxwidth = 0;
        for (int x = strings.length; --x >= 0; ) {
            if (strings[x].length() > maxwidth) {
                maxwidth = strings[x].length();
            }
        }
        if (maxwidth < (title.length() + decorationLength)) maxwidth = title.length() + decorationLength;
        StringBuffer topcap = new StringBuffer();
        topcap.append("+-=[").append(title).append("]=-");
        for (int x2 = (maxwidth - decorationLength - title.length() + 2); --x2 >= 0; ) {
            topcap.append('-');
        }
        topcap.append("+\n");
        StringBuffer bottomcap = new StringBuffer();
        bottomcap.append('+');
        for (int y = maxwidth; --y >= 0; ) {
            bottomcap.append('-');
        }
        ;
        bottomcap.append("+\n");
        sb.append(topcap.toString());
        for (int z = strings.length; --z >= 0; ) {
            sb.append('|').append(strings[z]);
            for (int z2 = maxwidth - strings[z].length(); --z2 >= 0; ) {
                sb.append(' ');
            }
            sb.append("|\n");
        }
        sb.append(bottomcap.toString());
        return sb.toString();
    }
}
