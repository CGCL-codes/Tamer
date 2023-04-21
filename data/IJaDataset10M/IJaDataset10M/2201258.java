package org.jiopi.ibean.show.mailsender;

import java.net.URL;
import org.jiopi.framework.CentralConsole;
import org.jiopi.framework.FrameworkInitializer;
import org.jiopi.framework.ControlPanel;
import org.jiopi.blueprint.mailsender.Mail;
import org.jiopi.blueprint.mailsender.MailSender;
import org.jiopi.blueprint.mailsender.SendMailException;
import org.jiopi.blueprint.mailsender.MailAttachment;

public class SimpleSendMail {

    public static void JIOPiAccess() {
        ControlPanel mailSender = CentralConsole.accessControlPanel("jiopi.module.mailsender", "0.1", "jiopi.MailSender?mymail2", ControlPanel.class);
        try {
            String status = mailSender.operate("sendMail", String.class, "����  <to@yourname.com>", "subject", "text content");
            System.out.println("send mail status : " + status);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void APIAccess() {
        MailSender mailSender = CentralConsole.accessControlPanel("jiopi.module.mailsender", "0.1", "jiopi.MailSender?mymail", MailSender.class);
        testSimpleSendMail(mailSender);
    }

    /**
	 * need jdk1.6
	 */
    public static void POJOAccess() {
        MailSender mailSender = new MyMailSender();
        testSimpleSendMail(mailSender);
    }

    private static void testSimpleSendMail(MailSender mailSender) {
        try {
            String status = mailSender.sendMail("����  <to@yourname.com>", "subject", "text content");
            System.out.println("send mail status : " + status);
        } catch (SendMailException e) {
            e.printStackTrace();
        }
    }

    public static void testPojoMail() throws Exception {
        Mail myMail = new MyMail();
        myMail.addTo("to@yourname.com", "����");
        myMail.setSubject("�����ʼ�");
        myMail.setTextMsg("��������");
        MailAttachment attachment = new MailAttachment(new URL("http://www.google.com.hk/intl/zh-CN/images/logo_cn.png"));
        myMail.attach(attachment);
        myMail.send();
    }

    /**
	 * @param args
	 * @throws SendMailException 
	 */
    public static void main(String[] args) throws Exception {
        FrameworkInitializer.initialize();
        System.out.println("send ok");
    }
}
