package com.cs.util.db.query;

import com.cs.util.binary.Base16;
import com.cs.util.collection.MultiValuedMap;
import java.io.IOException;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMultipart;

/**
 * MultipartFormData is a parser for multipart/form-data media types.
 * @author dimitris@jmike.gr
 */
public class MultipartFormData extends MultiValuedMap<String, String> {

    public MultipartFormData(MimeMultipart form) {
        super();
        if (form != null) {
            try {
                for (int i = 0; i < form.getCount(); i++) {
                    final BodyPart bodyPart = form.getBodyPart(i);
                    super.add(getBodyPartName(bodyPart), getBodyPartValue(bodyPart));
                }
            } catch (MessagingException ex) {
            } catch (IOException ex) {
            }
        }
    }

    /**
     * Returns the name of the designated BodyPart.
     * @param bodyPart
     * @return
     * @throws javax.mail.MessagingException
     */
    private static String getBodyPartName(BodyPart bodyPart) throws MessagingException {
        if (bodyPart.getDisposition().equals("form-data")) {
            final String[] h = bodyPart.getHeader("Content-Disposition");
            if (h != null && h.length > 0) {
                final int a = h[0].indexOf("\"") + 1;
                final int b = h[0].indexOf("\"", a);
                return h[0].substring(a, b);
            }
        }
        return "";
    }

    /**
     * Returns the value of the designated BodyPart.
     * @param bodyPart
     * @return
     * @throws javax.mail.MessagingException
     * @throws java.io.IOException
     */
    private static String getBodyPartValue(BodyPart bodyPart) throws MessagingException, IOException {
        if (bodyPart.getContentType().equals("text/plain")) {
            return (String) bodyPart.getContent();
        } else {
            return Base16.toHexString(bodyPart.getInputStream().toString().getBytes());
        }
    }
}
