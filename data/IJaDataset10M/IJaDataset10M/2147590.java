package de.mogwai.kias.example.tools;

import javax.mail.MessagingException;
import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mail.javamail.JavaMailSender;

public class Mail {

    public static void main(String args[]) throws AddressException, MessagingException {
        ApplicationContext context = new ClassPathXmlApplicationContext("de/mogwai/kias/example/spring-config.xml");
        JavaMailSender theSender = (JavaMailSender) context.getBean("mailSender");
        MimeMessage theMessage = theSender.createMimeMessage();
        theMessage.setFrom(new InternetAddress("no-reply@synergie-effekt-gmbh.de"));
        theMessage.setRecipients(RecipientType.TO, "Mirko.Sertic@web.de");
        theMessage.setSubject("Synekt Newsletter");
        MimeMultipart theMultipart = new MimeMultipart();
        theMultipart.setSubType("related");
        MimeBodyPart theBodyPart = new MimeBodyPart();
        theBodyPart.setContent("Text Text", "text/html");
        theMultipart.addBodyPart(theBodyPart);
        theMessage.setContent(theMultipart);
        theSender.send(theMessage);
        System.exit(0);
    }

    ;
}
