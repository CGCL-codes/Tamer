package com.endigi.frame.base.domain.test;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

public class SendEmailTest {

    public static void main(String[] args) throws EmailException {
        HtmlEmail email = new HtmlEmail();
        email.setHostName("smtp.qq.com");
        email.setSmtpPort(25);
        email.setAuthentication("370074995@qq.com", "000000000000");
        email.setFrom("370074995@qq.com");
        email.addTo("coolcooldool@gmail.com");
        email.setSubject("test");
        email.setHtmlMsg("testttttttttt");
        String result = email.send();
        System.out.println(result);
    }
}
