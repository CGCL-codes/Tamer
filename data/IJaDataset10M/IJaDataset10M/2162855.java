package com.liusoft.dlog4j.test;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;

/**
 * �����ʼ�ֱͨ��
 * @author liudong
 */
public class Mailer {

    /**
	 * @param args
	 * @throws MessagingException 
	 * @throws TextParseException 
	 * @throws UnsupportedEncodingException 
	 */
    public static void main(String[] args) throws MessagingException, TextParseException, UnsupportedEncodingException {
        for (int ai = 0; ai < args.length; ai++) {
            String mailaddr = args[ai];
            Session ssn = initMailSession();
            MimeMessage mailMessage = new MimeMessage(ssn);
            mailMessage.setSubject("Hello MAIL");
            mailMessage.setSentDate(new Date());
            Multipart multipart = new MimeMultipart("related");
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("Welcome to JavaMail.");
            multipart.addBodyPart(messageBodyPart);
            mailMessage.setContent(multipart);
            mailMessage.setFrom(new InternetAddress("javayou@gmail.com", "Winter Lau"));
            String mail_postfix = mailaddr.substring(mailaddr.indexOf('@') + 1);
            Lookup lookup = new Lookup(mail_postfix, Type.MX);
            lookup.run();
            if (lookup.getResult() != Lookup.SUCCESSFUL) {
                System.out.println(" " + lookup.getErrorString());
                return;
            }
            Record[] answers = lookup.getAnswers();
            for (int i = 0; i < answers.length; i++) {
                Transport transport = null;
                ssn.getProperties().put("mail.smtp.host", answers[i].getAdditionalName().toString());
                InternetAddress smtp_host = new InternetAddress(answers[i].getAdditionalName().toString());
                try {
                    transport = ssn.getTransport(smtp_host);
                    transport.connect();
                    System.out.println("connect to " + smtp_host + " ok.");
                    InternetAddress mailToAddress = new InternetAddress(mailaddr);
                    transport.sendMessage(mailMessage, new InternetAddress[] { mailToAddress });
                    System.out.println("mail sent to " + mailaddr + " via " + smtp_host);
                    break;
                } catch (MessagingException me) {
                    me.printStackTrace();
                } finally {
                    if (transport != null) {
                        transport.close();
                        transport = null;
                    }
                }
            }
        }
    }

    private static Session initMailSession() {
        Properties props = new Properties();
        props.put("mail.debug", "false");
        props.put("mail.smtp.ehlo", "false");
        props.put("mail.smtp.timeout", smtpTimeout + "");
        props.put("mail.smtp.connectiontimeout", connectionTimeout + "");
        props.put("mail.smtp.sendpartial", String.valueOf(sendPartial));
        return Session.getInstance(props, null);
    }

    private static long smtpTimeout = 600000;

    private static int connectionTimeout = 60000;

    private static boolean sendPartial = false;
}
