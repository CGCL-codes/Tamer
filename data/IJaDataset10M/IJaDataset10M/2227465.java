package org.beepcore.beep.example;

import java.io.InputStream;
import java.io.IOException;
import org.beepcore.beep.core.BEEPError;
import org.beepcore.beep.core.BEEPException;
import org.beepcore.beep.core.BEEPInterruptedException;
import org.beepcore.beep.core.Channel;
import org.beepcore.beep.core.Session;
import org.beepcore.beep.core.StringOutputDataStream;
import org.beepcore.beep.lib.Reply;
import org.beepcore.beep.profile.echo.EchoProfile;
import org.beepcore.beep.profile.tls.TLSProfile;
import org.beepcore.beep.transport.tcp.TCPSessionCreator;
import org.beepcore.beep.transport.tcp.TCPSession;

/**
 * Sample client application analogous to ping. The application uses the
 * echo profile (http://xml.resource.org/profiles/NULL/ECHO) to get ping
 * like behavior. TLS is also supported for servers that only advertise
 * the echo profile once TLS is negotiated.
 *
 * @author Eric Dixon
 * @author Huston Franklin
 * @author Jay Kint
 * @author Scott Pead
 * @version $Revision: 1.12 $, $Date: 2003/06/03 02:52:14 $
 */
public class Bing {

    public static void main(String[] argv) {
        if (parseArgs(argv) == false) {
            System.out.println(usage);
            return;
        }
        Session session;
        try {
            session = TCPSessionCreator.initiate(host, port);
        } catch (BEEPException e) {
            System.err.println("bing: Error connecting to " + host + ":" + port + "\n\t" + e.getMessage());
            return;
        }
        try {
            if (privacy != PRIVACY_NONE) {
                try {
                    session = TLSProfile.getDefaultInstance().startTLS((TCPSession) session);
                } catch (BEEPException e) {
                    System.err.println("bing: Error unable to start TLS.\n\t" + e.getMessage());
                    if (privacy == PRIVACY_REQUIRED) return;
                }
            }
            Channel channel;
            try {
                channel = session.startChannel(EchoProfile.ECHO_URI);
            } catch (BEEPError e) {
                if (e.getCode() == 550) {
                    System.err.println("bing: Error host does not support " + "echo profile");
                } else {
                    System.err.println("bing: Error starting channel (" + e.getCode() + ": " + e.getMessage() + ")");
                }
                return;
            } catch (BEEPException e) {
                System.err.println("bing: Error starting channel (" + e.getMessage() + ")");
                return;
            }
            String request = createRequest(size);
            for (int i = 0; i < count; ++i) {
                long time;
                int replyLength = 0;
                Reply reply = new Reply();
                time = System.currentTimeMillis();
                try {
                    channel.sendMSG(new StringOutputDataStream(request), reply);
                } catch (BEEPException e) {
                    System.err.println("bing: Error sending request (" + e.getMessage() + ")");
                    return;
                }
                try {
                    InputStream is = reply.getNextReply().getDataStream().getInputStream();
                    while (is.read() != -1) {
                        ++replyLength;
                    }
                } catch (BEEPInterruptedException e) {
                    System.err.println("bing: Error receiving reply (" + e.getMessage() + ")");
                    return;
                } catch (IOException e) {
                    System.err.println("bing: Error receiving reply (" + e.getMessage() + ")");
                    return;
                }
                System.out.println("Reply from " + host + ": bytes=" + replyLength + " time=" + (System.currentTimeMillis() - time) + "ms");
            }
            try {
                channel.close();
            } catch (BEEPException e) {
                System.err.println("bing: Error closing channel (" + e.getMessage() + ")");
                return;
            }
        } finally {
            try {
                session.close();
            } catch (BEEPException e) {
                System.err.print("bing: Error closing session (" + e.getMessage() + ")");
                return;
            }
        }
    }

    private static String createRequest(int size) {
        char[] c = new char[size];
        c[0] = 'a';
        for (int i = 1; i < c.length; ++i) {
            c[i] = (char) (c[i - 1] + 1);
            if (c[i] > 'z') {
                c[i] = 'a';
            }
        }
        return new String(c);
    }

    private static boolean parseArgs(String[] argv) {
        if (argv.length < 1) {
            return false;
        }
        int i = 0;
        while (i < (argv.length - 1)) {
            if (argv[i].equalsIgnoreCase("-port")) {
                port = Integer.parseInt(argv[++i]);
            } else if (argv[i].equalsIgnoreCase("-count")) {
                count = Integer.parseInt(argv[++i]);
            } else if (argv[i].equalsIgnoreCase("-size")) {
                size = Integer.parseInt(argv[++i]);
            } else if (argv[i].equalsIgnoreCase("-privacy")) {
                ++i;
                if (argv[i].equalsIgnoreCase("none")) {
                    privacy = PRIVACY_NONE;
                } else if (argv[i].equalsIgnoreCase("preferred")) {
                    privacy = PRIVACY_PREFERRED;
                } else if (argv[i].equalsIgnoreCase("required")) {
                    privacy = PRIVACY_REQUIRED;
                } else {
                    return false;
                }
            }
            ++i;
        }
        if (i != argv.length - 1) return false;
        host = argv[argv.length - 1];
        return true;
    }

    private static final int PRIVACY_NONE = 0;

    private static final int PRIVACY_PREFERRED = 1;

    private static final int PRIVACY_REQUIRED = 2;

    private static int count = 4;

    private static String host;

    private static int port = 10288;

    private static int privacy = PRIVACY_NONE;

    private static int size = 1024;

    private static final String usage = "usage: bing [-port port] [-count count] [-size size]\n" + "            [-privacy required|preferred|none] host\n\n" + "options:\n" + "    -port port    Specifies the port number.\n" + "    -count count  Number of echo requests to send.\n" + "    -size size    Request size.\n" + "    -privacy      required = require TLS.\n" + "                  preferred = request TLS.\n" + "                  none = don't request TLS.\n";
}
