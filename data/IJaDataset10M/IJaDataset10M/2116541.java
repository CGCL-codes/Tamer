package com.cie2.mreunion.services;

import com.cie2.mreunion.services.IMailService;
import com.cie2.mreunion.services.ServiceManager;

public class MailServiceTest {

    private IMailService mailService;

    public static void main(String[] args) {
        MailServiceTest testMail = new MailServiceTest();
        testMail.init();
        String from = "admin@mreunion.org";
        String to = "topik.hidayat@gmail.com";
        String subject = "Registration Code";
        String message = "Click the following URL <http://localhost:8080/mreunion/regApprove.jsp?regCode=AB234BGSDF/>";
        String smptServer = "192.168.1.198";
        testMail.send(from, to, subject, message, smptServer);
    }

    public void init() {
        ServiceManager manager = ServiceManager.getInstance();
        mailService = (IMailService) manager.getService("MailService");
    }

    public void send(String from, String to, String subject, String message, String smptServer) {
        mailService.send(from, to, subject, message);
    }
}
