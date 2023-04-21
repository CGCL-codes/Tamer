package jvc.util.net.mail;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class SendMail {

    private MimeMessage mimeMsg;

    private Session session;

    private Properties props;

    private String username = "";

    private String password = "";

    private Multipart mp;

    /**
  * 
  */
    public SendMail() {
        createMimeMessage();
    }

    public SendMail(String smtp) {
        setSmtpHost(smtp);
        createMimeMessage();
    }

    /**
  * @param hostName String
  */
    public void setSmtpHost(String hostName) {
        System.out.println("����ϵͳ���ԣ�mail.smtp.host = " + hostName);
        if (props == null) props = System.getProperties();
        props.put("mail.smtp.host", hostName);
        props.put("mail.debug", "true");
    }

    /**
  * @return boolean
  */
    public boolean createMimeMessage() {
        try {
            System.out.println("׼����ȡ�ʼ��Ự����");
            session = Session.getDefaultInstance(props, null);
        } catch (Exception e) {
            System.err.println("��ȡ�ʼ��Ự����ʱ�������" + e);
            return false;
        }
        System.out.println("׼������MIME�ʼ�����");
        try {
            mimeMsg = new MimeMessage(session);
            mp = new MimeMultipart();
            return true;
        } catch (Exception e) {
            System.err.println("����MIME�ʼ�����ʧ�ܣ�" + e);
            return false;
        }
    }

    /**
  * @param need boolean
  */
    public void setNeedAuth(boolean need) {
        System.out.println("����smtp�����֤��mail.smtp.auth = " + need);
        if (props == null) props = System.getProperties();
        if (need) {
            props.put("mail.smtp.auth", "true");
        } else {
            props.put("mail.smtp.auth", "false");
        }
    }

    /**
  * @param name String
  * @param pass String
  */
    public void setNamePass(String name, String pass) {
        username = name;
        password = pass;
    }

    /**
  * @param mailSubject String
  * @return boolean
  */
    public boolean setSubject(String mailSubject) {
        System.out.println("�����ʼ����⣡");
        try {
            mimeMsg.setSubject(mailSubject);
            return true;
        } catch (Exception e) {
            System.err.println("�����ʼ����ⷢ�����");
            return false;
        }
    }

    public boolean setBodyHtml(String mailBody) {
        try {
            BodyPart bp = new MimeBodyPart();
            bp.setContent("<meta http-equiv=Content-Type content=text/html; charset=gb2312>" + mailBody, "text/html;charset=GB2312");
            mp.addBodyPart(bp);
            return true;
        } catch (Exception e) {
            System.err.println("�����ʼ�����ʱ�������" + e);
            return false;
        }
    }

    /**
  * @param mailBody String
  */
    public boolean setBody(String mailBody) {
        try {
            BodyPart bp = new MimeBodyPart();
            bp.setText(mailBody);
            mp.addBodyPart(bp);
            return true;
        } catch (Exception e) {
            System.err.println("�����ʼ�����ʱ�������" + e);
            return false;
        }
    }

    /**
  * @param name String
  * @param pass String
  */
    public boolean addFileAffix(String filename) {
        System.out.println("�����ʼ�������" + filename);
        try {
            BodyPart bp = new MimeBodyPart();
            FileDataSource fileds = new FileDataSource(filename);
            bp.setDataHandler(new DataHandler(fileds));
            bp.setFileName(fileds.getName());
            mp.addBodyPart(bp);
            return true;
        } catch (Exception e) {
            System.err.println("�����ʼ�������" + filename + "�������" + e);
            return false;
        }
    }

    /**
  * @param name String
  * @param pass String
  */
    public boolean setFrom(String from) {
        System.out.println("���÷����ˣ�");
        try {
            mimeMsg.setFrom(new InternetAddress(from));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
  * @param name String
  * @param pass String
  */
    public boolean setTo(String to) {
        if (to == null) return false;
        try {
            mimeMsg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
  * @param name String
  * @param pass String
  */
    public boolean setCopyTo(String copyto) {
        if (copyto == null) return false;
        try {
            mimeMsg.setRecipients(Message.RecipientType.CC, (Address[]) InternetAddress.parse(copyto));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public MimeMessage getMessage() {
        return mimeMsg;
    }

    /**
  * @param name String
  * @param pass String
  */
    public boolean sendout() {
        try {
            mimeMsg.setContent(mp);
            mimeMsg.saveChanges();
            System.out.println("���ڷ����ʼ�....");
            Session mailSession = Session.getInstance(props, null);
            Transport transport = mailSession.getTransport("smtp");
            System.out.println(transport.getURLName());
            System.out.println(transport.getClass().getCanonicalName());
            transport.connect((String) props.get("mail.smtp.host"), username, password);
            transport.sendMessage(mimeMsg, mimeMsg.getRecipients(Message.RecipientType.TO));
            System.out.println("�����ʼ��ɹ���");
            transport.close();
            return true;
        } catch (Exception e) {
            System.err.println("�ʼ�����ʧ�ܣ�" + e);
            return false;
        }
    }

    /**
   *  Just do it as this
   */
    public static void main(String[] args) {
        for (int i = 0; i < 1; i++) {
            String mailbody = "test,test";
            SendMail themail = new SendMail("localhost");
            themail.setNeedAuth(true);
            if (themail.setSubject("test") == false) return;
            if (themail.setBody(mailbody) == false) return;
            if (themail.setTo("rufujian@163.com") == false) return;
            if (themail.setFrom("rufujian@163.com") == false) return;
            if (themail.addFileAffix("c:\\boot.ini") == false) return;
            themail.setNamePass("", "");
            System.out.println("�ɹ�" + i);
            if (themail.sendout() == false) System.out.println("ʧ��" + i);
        }
    }
}
