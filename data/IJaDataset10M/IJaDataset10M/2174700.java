package net.sf.ahtutils.mail.content;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import net.sf.ahtutils.xml.mail.Attachment;
import net.sf.ahtutils.xml.mail.Mail;
import org.apache.commons.lang.SystemUtils;

public class XmlMimeContentCreator extends AbstractMimeContentCreator {

    private MimeMessage message;

    public XmlMimeContentCreator(MimeMessage message) {
        this.message = message;
    }

    public void createContent(Mail mail) throws MessagingException {
        Multipart mpAlternative = new MimeMultipart("alternative");
        mpAlternative.addBodyPart(createTxt(mail));
        if (!mail.isSetAttachment()) {
            message.setContent(mpAlternative);
        } else {
            Multipart mixed = new MimeMultipart("mixed");
            MimeBodyPart wrap = new MimeBodyPart();
            wrap.setContent(mpAlternative);
            mixed.addBodyPart(wrap);
            for (Attachment attachment : mail.getAttachment()) {
                mixed.addBodyPart(createBinary(attachment));
            }
            message.setContent(mixed);
        }
    }

    private MimeBodyPart createTxt(Mail mail) throws MessagingException {
        MimeBodyPart txt = new MimeBodyPart();
        if (mail.isSetAttachment()) {
            txt.setContent(mail.getExample() + SystemUtils.LINE_SEPARATOR, "text/plain; charset=\"ISO-8859-1\"");
        } else {
            txt.setContent(mail.getExample(), "text/plain; charset=\"ISO-8859-1\"");
        }
        return txt;
    }
}
