package au.com.cahaya.hubung.contact.mail;

import javax.mail.Folder;
import javax.mail.MessagingException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import au.com.cahaya.asas.net.mail.MailClient;
import au.com.cahaya.asas.net.mail.MailProtocolType;
import au.com.cahaya.asas.util.StringUtil;
import au.com.cahaya.asas.util.cli.HelpOption;
import au.com.cahaya.asas.util.cli.PasswordOption;
import au.com.cahaya.asas.util.cli.ServerOption;
import au.com.cahaya.asas.util.cli.UserOption;

/**
 * 
 *
 * @author Mathew Pole
 * @since January 2009
 * @version ${Revision}
 */
public class TraverseInboxFolders {

    /** The private logger for this class */
    private Logger myLog = LoggerFactory.getLogger(TraverseInboxFolders.class);

    /** The mail client for connecting to the server */
    MailClient myMailClient = null;

    /**
   * Constructor 
   */
    public TraverseInboxFolders(String server, String user, String password) {
        myMailClient = new MailClient(MailProtocolType.eImap, server, user, password);
    }

    /**
   * 
   */
    public int displayFolders() {
        if (!myMailClient.isOpenSession()) {
            myMailClient.openSession(false);
            myMailClient.openStore();
        }
        try {
            myMailClient.openInboxFolder();
            int totalMessages = displayFolders(myMailClient.openDefaultFolder(), 0);
            System.out.println("total messages: " + totalMessages);
            return totalMessages;
        } catch (MessagingException exc) {
            myLog.error("displayFolders", exc);
            return -1;
        }
    }

    /**
   * @throws MessagingException 
   * 
   */
    private int displayFolders(Folder folder, int level) throws MessagingException {
        folder.setSubscribed(true);
        int totalMessages = 0;
        Folder[] folders = folder.list();
        for (int i = 0; i < folders.length; i++) {
            totalMessages += displayFolders(folders[i], level + 1);
        }
        if ((folder.getType() & Folder.HOLDS_MESSAGES) != 0) {
            StringBuffer sb = new StringBuffer(StringUtil.padStart("", ' ', level * 2));
            sb.append(folder.getName());
            sb.append(" (").append(folder.getMessageCount());
            if (totalMessages > 0) {
                sb.append(" sub: ").append(totalMessages);
            }
            sb.append(")");
            System.out.println(sb);
            return totalMessages + folder.getMessageCount();
        } else {
            return totalMessages;
        }
    }

    /**
   * @param args
   */
    public static void main(String[] args) {
        CommandLineParser parser = new GnuParser();
        Options options = new Options();
        options.addOption(new HelpOption());
        options.addOption(new ServerOption());
        options.addOption(new UserOption());
        options.addOption(new PasswordOption());
        try {
            CommandLine line = parser.parse(options, args);
            if (line.hasOption(HelpOption.cValue)) {
                HelpFormatter hf = new HelpFormatter();
                hf.printHelp(TraverseInboxFolders.class.toString(), options);
            } else {
                String server = line.getOptionValue(ServerOption.cValue);
                String user = line.getOptionValue(UserOption.cValue);
                String password = line.getOptionValue(PasswordOption.cValue);
                TraverseInboxFolders tif = new TraverseInboxFolders(server, user, password);
                tif.displayFolders();
                System.exit(0);
            }
        } catch (ParseException exc) {
            System.out.println("Unexpected exception: " + exc.getMessage());
            HelpFormatter hf = new HelpFormatter();
            hf.printHelp(TraverseInboxFolders.class.toString(), options);
            System.exit(1);
        }
    }
}
