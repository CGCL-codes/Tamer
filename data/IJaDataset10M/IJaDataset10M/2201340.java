package sunlabs.brazil.sunlabs;

import java.io.IOException;
import java.net.InetAddress;
import sunlabs.brazil.server.Handler;
import sunlabs.brazil.server.Request;
import sunlabs.brazil.server.Server;
import sunlabs.brazil.util.regexp.Regexp;

/**
 * Handler to enable proper interaction with a protocol conversion 
 * gateway, by rewriting "redirect" directives properly.
 * For example, this handler may be used with
 * stunnel (see 
 * <a href=http://www.stunnel.org)>stunnel.org</a>), configured as an SSL gateway.
 * enabling Brazil with an external ssl protocol stack.
 * For example, the stunnel configuration
 * <pre>
 *  [https]
 *  accept  = 443
 *  connect = 8080
 * </pre>
 * Will allow "https" connections on the standard port ssl (443) to access a Brazil
 * server on port 8080.
 *
 * When using Brazil in this configuration without this handler, since
 * Brazil talks to the gateway via "http", it will issue redirects to "http",
 * which is the wrong protocol.
 * This template looks at the origin ip address, and if
 * it matches, changes the server protocol for this request, resulting in
 * the client redirecting back through the gateway properly.
 * <p>
 * Properties:
 * <dl class=props>
 * <dt>ssl	<dd>The regexp to match client ip addresses that are coming
 *		from ssl gateways (such as stunnel).
 * <dt>protocol <dd>The protocol to replace "http" with when redirection
 *              via a gateway (defaults to "https").
 * </dl>
 */
public class StunnelHandler implements Handler {

    String prefix;

    Regexp ssl = null;

    String protocol;

    public boolean init(Server server, String prefix) {
        String str = server.props.getProperty(prefix + "ssl");
        protocol = server.props.getProperty(prefix + "protocol", "https");
        this.prefix = prefix;
        if (str != null) {
            try {
                ssl = new Regexp(str);
            } catch (Exception e) {
                server.log(Server.LOG_WARNING, prefix, "Invalid regular expression for \"ssl\" gateway");
                return false;
            }
        }
        return true;
    }

    /**
     * If we are coming from the machine which is designated as our ssl 
     * gateway, then we need to change the protocol to "https" and
     * remap the default port.
     */
    public boolean respond(Request request) throws IOException {
        InetAddress inet = request.getSocket().getInetAddress();
        System.out.println("GOT " + inet.getHostAddress());
        if (ssl.match(inet.getHostAddress()) != null) {
            String host = request.headers.get("Host");
            if (host != null && host.indexOf(":") < 0) {
                request.headers.put("Host", host + ":443");
            }
            request.serverProtocol = protocol;
            request.log(Server.LOG_LOG, prefix, "mapping to ssl: " + inet.toString());
        }
        return false;
    }
}
