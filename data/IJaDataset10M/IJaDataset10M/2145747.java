package org.schwering.irc.manager;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import org.schwering.irc.lib.IRCConstants;

/**
 * Provides some CTCP utilities.
 * <p>
 * A quotation from the CTCP protocol specification 
 * (http://www.irchelp.org/irchelp/rfc/ctcpspec.html):
 * <blockquote>
 * There are three levels of messages. The highest level (H) is the text
 * on the user-to-client level. The middle layer (M) is on the level
 * where CTCP quoting has been applied to the H-level message. The lowest
 * level (L) is on the client-to-server level, where low level quoting
 * has been applied to the M-level message.
 * <pre>
 * 	L = lowQuote(M)
 * 	M = ctcpDequote(L)
 * 
 * 	M = ctcpQuote(H)
 * 	H = ctcpDequote(ctcpExtract(M))
 * </pre>
 * </blockquote>
 * Hence, a message <code>m</code> that is to be sent to another user is 
 * sent as <code>lowQuote(ctcpQuote(m'))</code> where <code>m'</code> is a
 * CTCP-token of <code>m</code>.
 * <p>
 * A received message <code>n</code> is displayed as 
 * <code>m = ctcpDequote(lowDequote(n'))</code> where <code>n'</code> is a 
 * CTCP-token of <code>n</code>.
 * <p>
 * I'm sorry about the bad notation above. The point is: Be careful when you
 * have to apply <code>ctcpDequote / ctcpQuote</code>. Before applyingi them,
 * we need to know which tokens are CTCP-tokens and which are normal ones.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 */
public class CtcpUtil {

    private static final char MQUOTE = 0020;

    private static final char XDELIM = IRCConstants.CTCP_DELIMITER;

    private static final char XQUOTE = 0134;

    public static String lowQuote(String msg) {
        StringBuffer sb = new StringBuffer(msg.length());
        for (int i = 0; i < msg.length(); i++) {
            switch(msg.charAt(i)) {
                case 0:
                    sb.append(MQUOTE);
                    sb.append('0');
                    break;
                case '\r':
                    sb.append(MQUOTE);
                    sb.append('r');
                    break;
                case '\n':
                    sb.append(MQUOTE);
                    sb.append('n');
                    break;
                case MQUOTE:
                    sb.append(MQUOTE);
                    sb.append(MQUOTE);
                    break;
                default:
                    sb.append(msg.charAt(i));
            }
        }
        return sb.toString();
    }

    public static String lowDequote(String msg) {
        StringBuffer sb = new StringBuffer(msg.length());
        for (int i = 0; i < msg.length(); i++) {
            if (msg.charAt(i) == MQUOTE && i + 1 < msg.length()) {
                switch(msg.charAt(++i)) {
                    case '0':
                        sb.append(0);
                        break;
                    case 'r':
                        sb.append('\r');
                        break;
                    case 'n':
                        sb.append('\n');
                        break;
                    case MQUOTE:
                        sb.append(MQUOTE);
                        break;
                    default:
                        sb.append(msg.charAt(i));
                }
            } else {
                sb.append(msg.charAt(i));
            }
        }
        return sb.toString();
    }

    public static String ctcpQuote(String msg) {
        StringBuffer sb = new StringBuffer(msg.length());
        for (int i = 0; i < msg.length(); i++) {
            switch(msg.charAt(i)) {
                case XDELIM:
                    sb.append(XQUOTE);
                    sb.append('a');
                    break;
                case XQUOTE:
                    sb.append(XQUOTE);
                    sb.append(XQUOTE);
                    break;
                default:
                    sb.append(msg.charAt(i));
            }
        }
        return sb.toString();
    }

    public static String ctcpDequote(String msg) {
        StringBuffer sb = new StringBuffer(msg.length());
        for (int i = 0; i < msg.length(); i++) {
            if (msg.charAt(i) == XQUOTE && i + 1 < msg.length()) {
                switch(msg.charAt(++i)) {
                    case 'a':
                        sb.append(XDELIM);
                        break;
                    case XQUOTE:
                        sb.append(XQUOTE);
                        break;
                    default:
                        sb.append(msg.charAt(i));
                }
            } else {
                sb.append(msg.charAt(i));
            }
        }
        return sb.toString();
    }

    /**
	 * Returns a list of strings in which each 2*i-th string is a plain 
	 * message and each 2*i+1-th string is a CTCP message. The delimiting
	 * <code>CTCP_DELIMITER</code>s are stripped. 
	 */
    public static List ctcpTokenize(String msg) {
        List list = new LinkedList();
        int from = 0;
        for (int i = 0; i < msg.length(); i++) {
            if (msg.charAt(i) == XDELIM) {
                if (from != i) {
                    String str = msg.substring(from, i);
                    str = ctcpDequote(lowDequote(str));
                    list.add(str);
                } else {
                    list.add(null);
                }
                from = i + 1;
            }
        }
        if (from != msg.length() - 1) {
            String str = msg.substring(from);
            str = ctcpDequote(lowDequote(str));
            list.add(str);
        }
        return list;
    }

    public static InetAddress convertLongToInetAddress(long address) throws UnknownHostException {
        byte[] addr = new byte[4];
        addr[0] = (byte) ((address >>> 24) & 0xFF);
        addr[1] = (byte) ((address >>> 16) & 0xFF);
        addr[2] = (byte) ((address >>> 8) & 0xFF);
        addr[3] = (byte) (address & 0xFF);
        return InetAddress.getByAddress(addr);
    }

    public static long convertInetAddressToLong(InetAddress addr) {
        return addr.hashCode();
    }
}
