package com.divosa.eformulieren.web.core.signal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;
import nl.cpict.broker.eformshandler.EformsHandlerPort;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import com.divosa.eformulieren.web.core.exception.EFormulierenException;
import com.divosa.eformulieren.web.core.pdf.PDFBuilder;
import com.ibm.icu.util.StringTokenizer;

public class MailHandler extends BrokerHandler {

    private static Logger LOGGER = Logger.getLogger(MailHandler.class);

    private static String HANDLER = "eformsEmailHandler";

    /**
     * @see com.divosa.eformulieren.web.core.signal.FormSubmitHandler#handle(org.dom4j.Document)
     */
    public Object handle(Document inputDocument) throws EFormulierenException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("inputDocument: " + inputDocument.asXML());
        }
        String result = "success";
        String environment = MailProperties.getPropertyStore().getProperty(MailProperties.PROP_ENVIRONMENT);
        String useBroker = MailProperties.getPropertyStore().getProperty(environment + "." + MailProperties.USE_BROKER);
        if (useBroker != null && "true".equals(useBroker)) {
            result = mailWithBroker(inputDocument);
        } else {
            OutputStream out = new ByteArrayOutputStream();
            try {
                PDFBuilder.buildPdf(inputDocument, out);
                sendMailWithAttachment(out, getNameOfForm(inputDocument), getEmailAdresses(inputDocument), getEmailSubject(inputDocument));
            } catch (Exception e) {
                String errorMessage = e.getMessage();
                LOGGER.error(errorMessage);
                throw new EFormulierenException(errorMessage, e);
            } finally {
                try {
                    out.close();
                } catch (IOException e) {
                    LOGGER.error("Error during closing of outputstream");
                }
            }
        }
        return result;
    }

    private String mailWithBroker(Document inputDocument) throws EFormulierenException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("To be mailed..: " + inputDocument.asXML());
        }
        String answer = null;
        EformsHandlerPort client = (EformsHandlerPort) getBrokerClient(EformsHandlerPort.class, HANDLER);
        String environment = MailProperties.getPropertyStore().getProperty(MailProperties.PROP_ENVIRONMENT);
        String from = MailProperties.getPropertyStore().getProperty(environment + "." + MailProperties.PROP_FROM);
        String filename = getNameOfForm(inputDocument);
        String body = "Dit is een gegenereerde mail met een bijgevoegd pdf bestand voor formulier '" + filename + "'.";
        answer = client.handle(getEmailAdresses(inputDocument), from, getEmailSubject(inputDocument), body, inputDocument.asXML());
        if (LOGGER.isDebugEnabled()) {
            LOGGER.info("answer: " + answer);
        }
        return answer;
    }

    private void sendMailWithAttachment(OutputStream os, String filename, String to, String subject) throws Exception {
        String environment = MailProperties.getPropertyStore().getProperty(MailProperties.PROP_ENVIRONMENT);
        String host = MailProperties.getPropertyStore().getProperty(environment + "." + MailProperties.PROP_HOST);
        String from = MailProperties.getPropertyStore().getProperty(environment + "." + MailProperties.PROP_FROM);
        Properties props = System.getProperties();
        props.put("mail.smtp.host", host);
        Session session = Session.getInstance(props, null);
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from + "-" + filename + ".pdf"));
        StringTokenizer st = new StringTokenizer(to, ";");
        while (st.hasMoreTokens()) {
            String mail = st.nextToken();
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(mail));
        }
        message.setSubject(MimeUtility.decodeText(subject));
        Multipart multipart = new MimeMultipart();
        MimeBodyPart textMessageBodyPart = new MimeBodyPart();
        textMessageBodyPart.setText("Dit is een gegenereerde mail met een bijgevoegd pdf bestand voor formulier '" + filename + "'.");
        multipart.addBodyPart(textMessageBodyPart);
        MimeBodyPart attachmentMessageBodyPart = new MimeBodyPart();
        byte[] pdf = ((ByteArrayOutputStream) os).toByteArray();
        attachmentMessageBodyPart.setDataHandler(new DataHandler(new ByteArrayDataSource(pdf, "application/pdf")));
        attachmentMessageBodyPart.setFileName(filename + ".pdf");
        multipart.addBodyPart(attachmentMessageBodyPart);
        message.setContent(multipart);
        Transport.send(message);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Mail with pdf attachment sent..");
        }
    }

    private String getNameOfForm(Document inputDocument) {
        Element formcontent = (Element) inputDocument.getRootElement().selectObject("/formcontent/form-info");
        return formcontent.attributeValue("form-name");
    }

    private String getBsnOfForm(Document inputDocument) {
        String bsn = "[bsn onbekend]";
        try {
            Element formcontent = (Element) inputDocument.getRootElement().selectObject("/formcontent/widgetTree/widget//widget[@dkd='BSN']/value");
            bsn = formcontent.getStringValue();
        } catch (ClassCastException cce) {
        }
        return bsn;
    }

    private String getClientNameFromForm(Document inputDocument) {
        String name = "";
        Element formcontent = null;
        try {
            formcontent = (Element) inputDocument.getRootElement().selectObject("/formcontent/widgetTree/widget//widget[@dkd='firstForename']/value");
            if (formcontent != null) {
                name = formcontent.getStringValue() + " ";
            }
        } catch (ClassCastException cce) {
        }
        try {
            formcontent = (Element) inputDocument.getRootElement().selectObject("/formcontent/widgetTree/widget//widget[@dkd='otherForename']/value");
            if (formcontent != null) {
                name = name + formcontent.getStringValue() + " ";
            }
        } catch (ClassCastException cce) {
        }
        try {
            formcontent = (Element) inputDocument.getRootElement().selectObject("/formcontent/widgetTree/widget//widget[@dkd='surname']/value");
            if (formcontent != null) {
                name = name + formcontent.getStringValue();
            }
        } catch (ClassCastException cce) {
        }
        if ("".equals(name)) {
            name = "[naam onbekend]";
        }
        return name;
    }

    private String getEmailSubject(Document inputDocument) {
        String subject = getNameOfForm(inputDocument) + ".pdf";
        String bsn = getBsnOfForm(inputDocument);
        if (!"".equals(bsn)) {
            subject = subject + " voor BSN:" + bsn;
        }
        String name = getClientNameFromForm(inputDocument);
        if (!"".equals(name)) {
            subject = subject + " Client:" + getClientNameFromForm(inputDocument);
        }
        return subject;
    }

    private String getEmailAdresses(Document inputDocument) throws EFormulierenException {
        String email = null;
        Element brokerConnection = (Element) inputDocument.getRootElement().selectSingleNode("/formcontent/form-info/brokerConnection[last()]");
        if (brokerConnection != null) {
            if (brokerConnection.selectSingleNode("ownEmail") != null) {
                Element ownEmail = (Element) brokerConnection.selectObject("ownEmail");
                if (!"".equals(ownEmail.getText())) {
                    email = ownEmail.getText();
                }
            }
            if (brokerConnection.selectSingleNode("centralEmail") != null) {
                Element centralEmail = (Element) brokerConnection.selectObject("centralEmail");
                if (!"".equals(centralEmail.getText())) {
                    if (email != null) {
                        email = email + ";" + centralEmail.getText();
                    } else {
                        email = centralEmail.getText();
                    }
                }
            }
        } else {
            String message = "No brokerConnection found in document " + inputDocument.asXML();
            LOGGER.error(message);
            throw new EFormulierenException(message);
        }
        return email;
    }
}
