package POPStats;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.columba.ristretto.auth.AuthenticationException;
import org.columba.ristretto.auth.AuthenticationFactory;
import org.columba.ristretto.auth.NoSuchAuthenticationException;
import org.columba.ristretto.io.Source;
import org.columba.ristretto.io.TempSourceFactory;
import org.columba.ristretto.log.RistrettoLogger;
import org.columba.ristretto.parser.ParserException;
import org.columba.ristretto.pop3.POP3Exception;
import org.columba.ristretto.pop3.POP3Protocol;
import org.columba.ristretto.pop3.ScanListEntry;
import MessageDecomposer.MessageDecomposer;

/**
 * POPStats class - A command line utility to read the status of a POP3 Server.
 * Can also be useful in debugging why a POP server might appear not to be
 * working properly.
 * @author timo
 */
public class POPStats {

    private static final String helpMessage = "Usage : POPStats pop3-server username password <outdir>\n\n" + "Example: POPStats pop3.mail.com myname mypassword\n\n";

    /**
	 * main method
	 * 
	 * @param args
	 */
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println(helpMessage);
            return;
        }
        String server = args[0];
        String username = args[1];
        String password = args[2];
        File outDir;
        if (args.length > 3) {
            outDir = new File(args[3]);
        } else {
            outDir = new File(".");
        }
        if (!outDir.exists() || !outDir.isDirectory()) {
            if (!outDir.mkdir()) {
                System.out.println("Could not create directory" + outDir.toString());
                return;
            }
        }
        boolean sslSupported = false;
        POP3Protocol protocol = new POP3Protocol(server, POP3Protocol.DEFAULT_PORT);
        try {
            System.out.println("Connecting to " + server + " ...");
            protocol.openPort();
            if (protocol.isApopSupported()) {
                System.out.println("APOP authentication supported");
            }
            String saslCapabilities = null;
            try {
                String[] capabilities = protocol.capa();
                for (int i = 0; i < capabilities.length && saslCapabilities == null; i++) {
                    if (capabilities[i].toLowerCase().startsWith("sasl")) {
                        saslCapabilities = capabilities[i];
                        System.out.println("SASL authentication method(s) supported: " + saslCapabilities);
                    }
                    if (capabilities[i].toLowerCase().startsWith("stls")) {
                        System.out.println("SSL supported");
                        sslSupported = true;
                    }
                }
            } catch (POP3Exception e1) {
                System.out.println("CAPA command not supported");
            }
            if (false) {
                System.out.println("Establishing secure SSL connection...");
                protocol.startTLS();
            }
            boolean authenticated = false;
            try {
                if (protocol.isApopSupported()) {
                    System.out.println("Authenticating with APOP...");
                    protocol.apop(username, password.toCharArray());
                } else if (saslCapabilities != null) {
                    try {
                        String authMethod = AuthenticationFactory.getInstance().getSecurestMethod(saslCapabilities);
                        System.out.println("Authenticating with SASL " + authMethod + "...");
                        protocol.auth(authMethod, username, password.toCharArray());
                    } catch (NoSuchAuthenticationException e) {
                        System.err.println("No Authentication Method available: " + e.getLocalizedMessage());
                        protocol.quit();
                        return;
                    }
                }
            } catch (AuthenticationException e) {
                System.err.println("Authentication error: " + e.getLocalizedMessage());
                System.err.println("Fallback to USER/PASS method");
            } catch (POP3Exception e1) {
                System.err.println("Authentication error: " + e1.getMessage());
                System.err.println("Fallback to USER/PASS method");
            }
            if (protocol.getState() != POP3Protocol.TRANSACTION) {
                protocol.userPass(username, password.toCharArray());
            }
            System.out.println("Getting mailbox statisics...");
            ScanListEntry[] list = protocol.list();
            System.out.println(list.length + " messages on server");
            int maxSize = -1;
            int sumSize = 0;
            for (int i = 0; i < list.length; i++) {
                sumSize += list[i].getSize();
                maxSize = Math.max(maxSize, list[i].getSize());
            }
            System.out.println("Total mailbox size: " + (sumSize / 1024) + " kB");
            System.out.println("Mean message size: " + (sumSize / (1024 * list.length)) + " kB");
            System.out.println("Largest message size: " + (maxSize / 1024) + " kB");
            int messageIndex = list.length - 1;
            System.out.println("Downloading message " + messageIndex + "...");
            InputStream messageStream = protocol.retr(list[messageIndex].getIndex(), list[messageIndex].getIndex());
            messageStream = new ShellProgressMonitorInputStream(messageStream);
            Source messageSource = TempSourceFactory.createTempSource(messageStream);
            messageStream.close();
            new MessageDecomposer().decomposeMessage(messageSource, outDir);
            protocol.quit();
        } catch (IOException e) {
            System.err.println("IO Error: " + e.getLocalizedMessage());
        } catch (POP3Exception e) {
            System.err.println("POP3 Error: " + e.getMessage());
            try {
                protocol.quit();
            } catch (IOException e1) {
            } catch (POP3Exception e1) {
            }
        } catch (ParserException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
