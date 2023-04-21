package org.telluriumsource.component.connector;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 
 * @author Jian Fang (John.Jian.Fang@gmail.com)
 *
 * Date: Sep 9, 2010
 * 
 */
class DefaultRemoteCommand implements RemoteCommand {

    private static final int NUMARGSINCLUDINGBOUNDARIES = 4;

    private static final int FIRSTINDEX = 1;

    private static final int SECONDINDEX = 2;

    private static final int THIRDINDEX = 3;

    private final String command;

    private final String[] args;

    public DefaultRemoteCommand(String command, String[] args) {
        this.command = command;
        this.args = args;
        if ("selectWindow".equals(command) && args[0] == null) {
            args[0] = "null";
        }
    }

    public String getCommandURLString() {
        StringBuffer sb = new StringBuffer("cmd=");
        sb.append(urlEncode(command));
        if (args == null) return sb.toString();
        for (int i = 0; i < args.length; i++) {
            sb.append('&');
            sb.append(Integer.toString(i + 1));
            sb.append('=');
            sb.append(urlEncode(args[i]));
        }
        return sb.toString();
    }

    public String toString() {
        return getCommandURLString();
    }

    /** Factory method to create a RemoteCommand from a wiki-style input string */
    public static RemoteCommand parse(String inputLine) {
        if (null == inputLine) throw new NullPointerException("inputLine can't be null");
        String[] values = inputLine.split("\\|");
        if (values.length != NUMARGSINCLUDINGBOUNDARIES) {
            throw new IllegalStateException("Cannot parse invalid line: " + inputLine + values.length);
        }
        return new DefaultRemoteCommand(values[FIRSTINDEX], new String[] { values[SECONDINDEX], values[THIRDINDEX] });
    }

    /** Encodes the text as an URL using UTF-8.
   * 
   * @param text the text too encode
   * @return the encoded URI string
   * @see URLEncoder#encode(java.lang.String, java.lang.String)
   */
    public static String urlEncode(String text) {
        try {
            return URLEncoder.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
