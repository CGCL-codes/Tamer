package com.ihafer.switchboard.constraints;

import java.net.Socket;
import java.util.Map;
import com.ihafer.switchboard.IConstraint;

/**
 * A constraint class that allows connections only if the remote IP address
 * matches the configured pattern, which may use wildcards.  The character ?
 * may be used as a wildcard to match any single numeric character.  The
 * character * may be used as a wildcard to match zero or more numeric
 * characters.
 * <br><br>
 * This constraint has the following parameters:
 * <table border="1">
 *   <tr valign="top">
 *     <td><b>Name</b></td>
 *     <td><b>Required</b></td>
 *     <td><b>Values</b></td>
 *   </tr>
 *   <tr valign="top">
 *     <td>pattern</td>
 *     <td>Yes</td>
 *     <td><i>IP address match pattern.<br>e.g.,</i> 192.168.0.1<br>
 * <i>e.g.,</i> 192.168.?.*</td>
 *   </tr>
 * </table>
 */
public class IpMatch implements IConstraint {

    /**
     * Public default constructor.
     */
    public IpMatch() {
    }

    /**
     * Allows connections only if the remote IP address matches the configured
     * pattern, which may use wildcards.
     * @param socket The socket of the attempted connection.
     * @param parameters Map of the constraint parameters specified in the
     * socket mapping XML configuration file.
     * @return True if the specified socket is allowed to connect; otherwise,
     * false.
     */
    public boolean isConnectionAllowed(Socket socket, Map parameters) {
        String pattern = (String) parameters.get("pattern");
        if (pattern == null) {
            throw new RuntimeException("Required parameter (pattern) missing.");
        }
        String remote = socket.getInetAddress().getHostAddress();
        return isIpPatternMatch(remote, pattern);
    }

    /**
     * Returns true if the specified IP address matches the specified pattern,
     * which may use wildcards; otherwise, false.
     * @param ip IP address to test.
     * @param pattern Pattern to match IP address against.
     * @return True if the specified IP address matches the specified pattern;
     * otherwise, false.
     */
    private boolean isIpPatternMatch(String ip, String pattern) {
        String[] parts = pattern.split("\\.", -1);
        if ((parts.length != 4) || (parts[0].equals("")) || (parts[1].equals("")) || (parts[2].equals("")) || (parts[3].equals("")) || (!pattern.equals(pattern.replaceAll("[^0-9?*.]", "")))) {
            throw new RuntimeException("Parameter (pattern) invalid.");
        }
        pattern = pattern.replaceAll("\\.", "\\\\.");
        pattern = pattern.replaceAll("\\?", "[0-9]");
        pattern = pattern.replaceAll("\\*", "[0-9]*");
        return ip.matches(pattern);
    }
}
