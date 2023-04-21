package SimpleSMTP;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.nio.charset.Charset;
import org.columba.ristretto.auth.AuthenticationException;
import org.columba.ristretto.auth.AuthenticationFactory;
import org.columba.ristretto.auth.NoSuchAuthenticationException;
import org.columba.ristretto.composer.MimeTreeRenderer;
import org.columba.ristretto.io.CharSequenceSource;
import org.columba.ristretto.io.FileSource;
import org.columba.ristretto.log.RistrettoLogger;
import org.columba.ristretto.message.Address;
import org.columba.ristretto.message.BasicHeader;
import org.columba.ristretto.message.Header;
import org.columba.ristretto.message.LocalMimePart;
import org.columba.ristretto.message.MimeHeader;
import org.columba.ristretto.message.MimeType;
import org.columba.ristretto.parser.AddressParser;
import org.columba.ristretto.parser.ParserException;
import org.columba.ristretto.smtp.SMTPException;
import org.columba.ristretto.smtp.SMTPProtocol;

/**
 * SimpleSMTP class - This is a simple command line smtp client. It shows the
 * usage of the Ristretto SMTP capabilities.
 * @author timo
 */
public class SimpleSMTP {

    private static final String helpMessage = "Usage : SimpleSMTP smtp-server from-address to-address subject [optional args]\n\n" + "Example: SimpleSMTP smtp.test.com sender@test.com example@test.com \"This is a test message\" --text \"Hello, World!\"\n\n" + "--body\t\tMessage String\n" + "--textfile\tMessage from a text file\n" + "--attachment\tFile Attachment\n" + "--auth\t\tusername password\n" + "--help\t\tShow this help screen\n";

    /**
	 * main method
	 * 
	 * @param args
	 */
    public static void main(String[] args) {
        if (args.length < 4 || args[0].equals("--help")) {
            System.out.println(helpMessage);
            return;
        }
        String smtpServer = args[0];
        Address fromAddress;
        try {
            fromAddress = AddressParser.parseAddress(args[1]);
        } catch (ParserException e) {
            System.err.println("Invalid from-address : " + e.getSource());
            return;
        }
        Address toAddress;
        try {
            toAddress = AddressParser.parseAddress(args[2]);
        } catch (ParserException e) {
            System.err.println("Invalid to-address : " + e.getSource());
            return;
        }
        String subject = args[3];
        String body = "Hello, World!";
        String textfile = null;
        String attachment = null;
        String user = null;
        String password = null;
        boolean verbose = false;
        for (int i = 4; i < args.length; ) {
            if (args[i].equals("--body")) {
                body = args[i + 1];
                i += 2;
            } else if (args[i].equals("--textfile")) {
                textfile = args[i + 1];
                i += 2;
            } else if (args[i].equals("--attachment")) {
                attachment = args[i + 1];
                i += 2;
            } else if (args[i].equals("--auth")) {
                user = args[i + 1];
                password = args[i + 2];
                i += 3;
            } else if (args[i].equals("--verbose")) {
                verbose = true;
                i++;
            }
        }
        if (verbose) {
            RistrettoLogger.setLogStream(System.out);
        }
        Header header = new Header();
        BasicHeader basicHeader = new BasicHeader(header);
        basicHeader.setFrom(fromAddress);
        basicHeader.setTo(new Address[] { toAddress });
        basicHeader.setSubject(subject, Charset.forName("ISO-8859-1"));
        basicHeader.set("X-Mailer", "SimpleSMTP example / Ristretto API");
        MimeHeader mimeHeader = new MimeHeader(header);
        mimeHeader.set("Mime-Version", "1.0");
        LocalMimePart root = new LocalMimePart(mimeHeader);
        LocalMimePart textPart;
        if (attachment == null) {
            textPart = root;
        } else {
            mimeHeader.setMimeType(new MimeType("multipart", "mixed"));
            textPart = new LocalMimePart(new MimeHeader());
            root.addChild(textPart);
        }
        MimeHeader textHeader = textPart.getHeader();
        textHeader.setMimeType(new MimeType("text", "plain"));
        if (body != null) {
            root.setBody(new CharSequenceSource(body));
        } else if (textfile != null) {
            try {
                root.setBody(new FileSource(new File(textfile)));
            } catch (IOException e1) {
                System.err.println(e1.getLocalizedMessage());
                return;
            }
        }
        if (attachment != null) {
            MimeHeader attachmentHeader = new MimeHeader("application", "octet-stream");
            attachmentHeader.setContentTransferEncoding("base64");
            attachmentHeader.putDispositionParameter("filename", attachment);
            LocalMimePart attachmentPart = new LocalMimePart(attachmentHeader);
            try {
                attachmentPart.setBody(new FileSource(new File(attachment)));
            } catch (IOException e1) {
                System.err.println(e1.getLocalizedMessage());
                return;
            }
            root.addChild(attachmentPart);
        }
        InputStream messageSource;
        try {
            messageSource = MimeTreeRenderer.getInstance().renderMimePart(root);
        } catch (Exception e2) {
            System.err.println(e2.getLocalizedMessage());
            return;
        }
        SMTPProtocol protocol = new SMTPProtocol(smtpServer);
        try {
            protocol.openPort();
            String capabilities[] = protocol.ehlo(InetAddress.getLocalHost());
            if (user != null) {
                String authCapability = null;
                for (int i = 0; i < capabilities.length; i++) {
                    if (capabilities[i].startsWith("AUTH")) {
                        authCapability = capabilities[i];
                        break;
                    }
                }
                if (authCapability != null) {
                    try {
                        protocol.auth(AuthenticationFactory.getInstance().getSecurestMethod(authCapability), user, password.toCharArray());
                    } catch (NoSuchAuthenticationException e3) {
                        System.err.println(e3.getLocalizedMessage());
                        return;
                    } catch (AuthenticationException e) {
                        System.err.println(e.getMessage());
                        return;
                    }
                } else {
                    System.err.println("Server does not support Authentication!");
                    return;
                }
            }
            protocol.mail(fromAddress);
            protocol.rcpt(toAddress);
            protocol.data(messageSource);
            protocol.quit();
        } catch (IOException e1) {
            System.err.println(e1.getLocalizedMessage());
            return;
        } catch (SMTPException e1) {
            System.err.println(e1.getMessage());
            return;
        }
        System.exit(0);
    }
}
