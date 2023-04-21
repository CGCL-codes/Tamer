package org.riant.simplemms.mime;

import org.riant.simplemms.mm7.MarshallSubmitRequestJaxbImpl;
import org.riant.simplemms.SimpleMMSException;
import org.apache.log4j.Logger;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import org.riant.simplemms.mm7.SubmitRequestMessage;
import org.riant.simplemms.MediaPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.MessagingException;

/**
 *
 * @author jmills
 */
public class MarshallMMSMessageJavaMailImpl implements MarshallMMSMessage {

    private static final String MM7_START_PARAMTER = "mm7";

    private static final Logger logger = Logger.getLogger(MarshallMMSMessageJavaMailImpl.class);

    private static final MarshallMMSMessageJavaMailImpl INSTANCE = new MarshallMMSMessageJavaMailImpl();

    public static MarshallMMSMessageJavaMailImpl getInstance() {
        return INSTANCE;
    }

    /**
     * Private constructor to help enforce instance control. Use getInstance()
     */
    private MarshallMMSMessageJavaMailImpl() {
    }

    @Override
    public String marshallMMSMessage(List<MediaPart> parts, SubmitRequestMessage mm7message, OutputStream os) throws SimpleMMSException {
        MimeMultipart message = new MimeMultipart();
        try {
            message.addBodyPart(createMM7BodyPart(mm7message));
            for (MediaPart part : parts) {
                message.addBodyPart(convertMediaPartToBodyPart(part));
            }
            message.writeTo(os);
            return message.getContentType();
        } catch (MessagingException e) {
            System.err.print(e);
            logger.error("Error while marshalling MMS Message.", e);
            throw new SimpleMMSException("Error while marshalling MMS Message.");
        } catch (IOException e) {
            System.err.print(e);
            logger.error("Error while writing MMS Message to stream.", e);
            throw new SimpleMMSException("Error while writing MMS Message to stream.");
        }
    }

    private MimeBodyPart createMM7BodyPart(SubmitRequestMessage message) throws MessagingException {
        MimeBodyPart mm7 = new MimeBodyPart();
        MarshallSubmitRequestJaxbImpl submitRequestMarshaller = MarshallSubmitRequestJaxbImpl.getInstance();
        ByteArrayOutputStream submitReqAsByteArray = new ByteArrayOutputStream();
        submitRequestMarshaller.marshallMM7SubmitRequest(message, submitReqAsByteArray);
        mm7.setText(new String(submitReqAsByteArray.toByteArray()));
        mm7.setHeader("Content-Type", "text/xml");
        mm7.setContentID(MM7_START_PARAMTER);
        return mm7;
    }

    private MimeBodyPart convertMediaPartToBodyPart(MediaPart part) throws MessagingException {
        MimeBodyPart bodyPart = new MimeBodyPart();
        bodyPart.setContentID(part.getContentId());
        bodyPart.setHeader("Content-Type", part.getContentType());
        bodyPart.setHeader("Content-Transfer-Encoding", part.getContentTransferEncoding().getValue());
        bodyPart.setDataHandler(part.getContentPart());
        if (part.isDrm()) {
            MimeMultipart drmMultipart = new MimeMultipart();
            drmMultipart.addBodyPart(bodyPart);
            MimeBodyPart drmBodyPart = new MimeBodyPart();
            drmBodyPart.setContent(drmMultipart);
            return drmBodyPart;
        }
        return bodyPart;
    }
}
