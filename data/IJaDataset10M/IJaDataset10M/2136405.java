package hermesserver.smtpHandler;

import java.util.Date;
import java.text.SimpleDateFormat;

/**
 *
 * @author  Simon Thompson MEng MIET
 */
public class hermesReplyToSender {

    String sendToEmail;

    String[] sendTo;

    String mailTransportProtocol = "smtp";

    String mailHost;

    String mailUser;

    String mailPort;

    String mailPassword;

    String messageOnInternet;

    String subject = "Hermes Yacht Communication Tool";

    String content;

    boolean verbose;

    hermesOutgoingMailHandler homh;

    /**
     *
     * @param sendToEmail
     * @param mailHost
     * @param mailUser
     * @param mailPort
     * @param mailPassword
     * @param messageOnInternet
     * @param verbose
     */
    public hermesReplyToSender(String sendToEmail, String mailHost, String mailUser, String mailPort, String mailPassword, String messageOnInternet, boolean verbose) {
        this.sendToEmail = sendToEmail;
        this.mailHost = mailHost;
        this.mailUser = mailUser;
        this.mailPassword = mailPassword;
        this.messageOnInternet = messageOnInternet;
        this.verbose = verbose;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        java.util.Date date = new java.util.Date();
        subject = subject + " " + dateFormat.format(date);
        content = "Your message has been dealt with by Hermes Yacht Communication Tool!\n";
        content = content + "The following was put on the internet for your friends to enjoy:\n\n";
        content = content + messageOnInternet;
        sendTo = new String[1];
        sendTo[0] = sendToEmail;
        System.out.println(sendTo[0]);
        homh = new hermesOutgoingMailHandler(mailHost, mailPort, mailUser, mailPassword, content, subject, mailUser, sendTo, verbose);
    }

    /**
     * 
     */
    public void sendMessage() {
        try {
            homh.sendSSLEmail();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
