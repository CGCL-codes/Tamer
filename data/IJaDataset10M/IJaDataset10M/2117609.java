package net.sf.l2j.gameserver.lib;

import java.util.Vector;

/**
 * Contains some utilities like numeric error and reply numbers.
 * <p>
 * The most description of the numeric errors and numeric replies are copied from RFC1459.
 *
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @author Normton &lt;normton@latinmail.com&gt;
 * @version 2.03
 * @see IRCConnection_alt
 * @see IRCEventListener#onError(int, String)
 * @see IRCEventListener#onReply(int, String, String)
 */
public class IRCUtil implements IRCConstants {

    /**
	 * This is part of the mIRC code and shows that a color-code starts / ends. Here it is as the ASCII decimal int 3.
	 *
	 * @deprecated Moved to <code>IRCConstants</code>.
	 */
    @Deprecated
    public static char colorIndicator = 3;

    /**
	 * This is part of the mIRC code and shows that bold starts / ends. Here it is as the ASCII decimal int 32.
	 *
	 * @deprecated Moved to <code>IRCConstants</code>.
	 */
    @Deprecated
    public static char boldIndicator = 31;

    /**
	 * This is part of the mIRC code and shows that bold starts / ends. Here it is as the ASCII decimal int 2.
	 *
	 * @deprecated Moved to <code>IRCConstants</code>.
	 */
    @Deprecated
    public static char underlinedIndicator = 2;

    /**
	 * This is part of the mIRC code and shows that bold, underline and colors end. Here it is as the ASCII decimal int 15.
	 *
	 * @deprecated Moved to <code>IRCConstants</code>.
	 */
    @Deprecated
    public static char colorEndIndicator = 15;

    /**
	 * This is part of the mIRC code and indicates that the client's colors are reversed (background -&gt; foreground and foreground -&gt; background). Here it is as the ASCII decimal int 1.
	 *
	 * @deprecated Moved to <code>IRCConstants</code>.
	 */
    @Deprecated
    public static char colorReverseIndicator = 22;

    /**
	 * This is part of the mIRC code and shows that a PRIVMSG is an ACTION (<code>/me</code>). Here it is as the ASCII decimal int 22.
	 *
	 * @deprecated Moved to <code>IRCConstants</code>.
	 */
    @Deprecated
    public static char actionIndicator = 1;

    /**
	 * This is an empty constructor, it does nothing. Nobody may instantiate this class.
	 */
    private IRCUtil() {
    }

    /**
	 * According to RFC2812 the channel's name may and must start with one of the following characters.
	 * <ul>
	 * <li>! == 33 (ASCII)</li>
	 * <li># == 35</li>
	 * <li>&amp; == 38</li>
	 * <li>+ == 43</li>
	 * </ul>
	 * .
	 *
	 * @param str
	 *            The name to check if it's a channel.
	 * @return <code>true</code> if the argument starts with one of the characters mentioned above.
	 */
    public static boolean isChan(String str) {
        int c;
        return str.length() >= 2 && ((c = str.charAt(0)) == 35 || c == 38 || c == 33 || c == 43);
    }

    /**
	 * Parses a <code>String</code> to an <code>int</code> via <code>Integer.parseInt</code> but avoids the <code>NumberFormatException</code>.
	 *
	 * @param str
	 *            The <code>String</code> to parse.
	 * @return The parsed new <code>int</code>. <code>-1</code> if <code>NumberFormatException</code> was thrown.
	 */
    public static int parseInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException exc) {
            return -1;
        }
    }

    /**
	 * Erases the mIRC colorcodes from a String. The documentation of the evil color codes is available on <a href="http://www.mirc.co.uk/help/color.txt" target="_blank">http://www.mirc.co.uk/help/color.txt</a>. This method links to the <code>parseColors(StringBuffer)</code> method.
	 *
	 * @param str
	 *            The line which should be parsed.
	 * @return A line cleaned from any mIRC colorcodes.
	 * @see #parseColors(StringBuffer)
	 */
    public static String parseColors(String str) {
        return parseColors(new StringBuffer(str)).toString();
    }

    /**
	 * Erases the mIRC colorcodes from a String. The documentation of the evil color codes is available on <a href="http://www.mirc.co.uk/help/color.txt" target="_blank">http://www.mirc.co.uk/help/color.txt</a>.
	 *
	 * @param buf
	 *            The line which should be parsed.
	 * @return A line as <code>StringBuffer</code> object which is cleaned from any mIRC colorcodes.
	 * @see #parseColors(String)
	 */
    public static StringBuffer parseColors(StringBuffer buf) {
        int len = buf.length();
        for (int i = 0, j = 0, c; i < len; i++, j = i) {
            c = buf.charAt(i);
            try {
                if (c == COLOR_INDICATOR) {
                    c = buf.charAt(++j);
                    if ('0' <= c && c <= '9') {
                        c = buf.charAt(++j);
                        if ('0' <= c && c <= '9') c = buf.charAt(++j);
                    }
                    if (c == ',') c = buf.charAt(++j);
                    if ('0' <= c && c <= '9') {
                        c = buf.charAt(++j);
                        if ('0' <= c && c <= '9') c = buf.charAt(++j);
                    }
                } else if (c == ACTION_INDICATOR || c == BOLD_INDICATOR || c == UNDERLINE_INDICATOR || c == COLOR_END_INDICATOR || c == COLOR_REVERSE_INDICATOR) j++;
            } catch (StringIndexOutOfBoundsException exc) {
            }
            if (j > i) {
                buf = buf.delete(i, j);
                len -= j - i;
                i -= j - i;
            }
        }
        return buf;
    }

    /**
	 * Splits a string into substrings.
	 *
	 * @param str
	 *            The string which is to split.
	 * @param delim
	 *            The delimiter character, for example a space <code>' '</code>.
	 * @param trailing
	 *            The ending which is added as a substring though it wasn't in the <code>str</code>. This parameter is just for the <code>IRCParser</code> class which uses this method to split the <code>middle</code> part into the parameters. But as last parameter always the <code>trailing</code> is added. This is done here because it's the fastest way to do it here. <br />
	 *            If the <code>end</code> is <code>null</code> or <code>""</code>, nothing is appended.
	 * @return An array with all substrings.
	 * @see #split(String, int)
	 */
    public static String[] split(String str, int delim, String trailing) {
        Vector<String> items = new Vector<String>(15);
        int last = 0;
        int index = 0;
        int len = str.length();
        while (index < len) {
            if (str.charAt(index) == delim) {
                items.add(str.substring(last, index));
                last = index + 1;
            }
            index++;
        }
        if (last != len) items.add(str.substring(last));
        if (trailing != null && trailing.length() != 0) items.add(trailing);
        String[] result = new String[items.size()];
        items.copyInto(result);
        return result;
    }

    /**
	 * Splits a string into substrings. This method is totally equal to <code>split(str, delim, null)</code>.
	 *
	 * @param str
	 *            The string which is to split.
	 * @param delim
	 *            The delimiter character, for example a space <code>' '</code>.
	 * @return An array with all substrings.
	 * @see #split(String, int, String)
	 */
    public static String[] split(String str, int delim) {
        return split(str, delim, null);
    }
}
