package au.com.cahaya.hubung.contact.list;

import java.io.File;
import java.io.IOException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import au.com.cahaya.asas.util.cli.FromEmailOption;
import au.com.cahaya.asas.util.cli.HelpOption;
import au.com.cahaya.asas.util.cli.SubjectOption;
import au.com.cahaya.hubung.contact.model.adt.EmailMessageContent;
import au.com.cahaya.hubung.contact.util.cli.MessageFileOption;
import au.com.cahaya.hubung.contact.util.cli.RecipientFileOption;

/**
 * 
 *
 * @author Mathew Pole
 * @since December 2008
 * @version ${Revision}
 */
public class SendMessageExcel extends SendMessageAbstract {

    /** The private logger for this class */
    private Logger myLog = LoggerFactory.getLogger(SendMessageExcel.class);

    /**
   * @param from
   * @param message
   * @param recipient
   * @return
   */
    public SendMessageExcel(InternetAddress from) {
        super(from);
    }

    /**
   * 
   */
    public boolean send(String subject, File recipient, File message) {
        HssfRecipientIterator i = null;
        try {
            i = new HssfRecipientIterator(recipient);
        } catch (IOException exc) {
            myLog.error("send", exc);
            return false;
        }
        EmailMessageContent c = null;
        try {
            c = new EmailMessageContent(subject, message);
        } catch (IOException exc) {
            myLog.error("send", exc);
            return false;
        }
        return send(i, c, c.getPath());
    }

    /**
   * @param args
   */
    public static void main(String[] args) {
        CommandLineParser parser = new GnuParser();
        Options options = new Options();
        options.addOption(new HelpOption());
        FromEmailOption fromOption = new FromEmailOption();
        options.addOption(fromOption);
        options.addOption(new SubjectOption());
        options.addOption(new MessageFileOption());
        options.addOption(new RecipientFileOption());
        try {
            CommandLine line = parser.parse(options, args);
            if (line.hasOption(HelpOption.cValue)) {
                HelpFormatter hf = new HelpFormatter();
                hf.printHelp(SendMessageExcel.class.toString(), options);
            } else {
                File message = new File(line.getOptionValue(MessageFileOption.cValue));
                if (!message.canRead()) {
                    System.out.println("Cannot read " + message);
                }
                File recipient = new File(line.getOptionValue(RecipientFileOption.cValue));
                InternetAddress from = fromOption.parse(line);
                String subject = line.getOptionValue(SubjectOption.cValue);
                SendMessageExcel sme = new SendMessageExcel(from);
                sme.send(subject, recipient, message);
                System.exit(0);
            }
        } catch (ParseException exc) {
            System.out.println("Unexpected exception: " + exc.getMessage());
            HelpFormatter hf = new HelpFormatter();
            hf.printHelp(SendMessageExcel.class.toString(), options);
            System.exit(1);
        } catch (AddressException exc) {
            System.out.println("Error parsing fromEmail option: " + exc.getMessage());
            System.exit(1);
        }
    }
}
