package com.gm.mail.smtp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.gm.core.lang.StringUtils;

/**
 * <p>邮件消息</p>
 */
public class MailMessage {

    private static final Log logger = LogFactory.getLog(MailMessage.class);

    private static final String SEPARATOR = ":";

    private static final String USUALLY_ERROR_SEPARATOR = "：";

    private String from;

    private List<String> to = new ArrayList<String>();

    private List<String> cc = new ArrayList<String>();

    private List<String> bcc = new ArrayList<String>();

    private Date date;

    private String subject;

    private String body;

    private List<Attachment> attachments = new ArrayList<Attachment>();

    private int size;

    private int priority = 3;

    private List<String> replyList = new ArrayList<String>();

    /**
	 * <p>获取发件人信息</p>
	 * @return the 发件人信息
	 */
    public String getForm() {
        return from;
    }

    /**
	 * <p>设置发件人信息</p>
	 * @param form 发件人信息
	 */
    public void setForm(String from) {
        this.from = from;
    }

    /**
	 * <p>获取收件人列表</p>
	 * @return 收件人列表
	 */
    public List<String> getTo() {
        return to;
    }

    /**
	 * <p>设置收件人列表</p>
	 * @param to 收件人列表
	 */
    public void setTo(List<String> to) {
        this.to = to;
    }

    /**
	 * <p>获取抄送人列表</p>
	 * @return 抄送人列表
	 */
    public List<String> getCc() {
        return cc;
    }

    /**
	 * <p>设置抄送人列表</p>
	 * @param cc 抄送人列表
	 */
    public void setCc(List<String> cc) {
        this.cc = cc;
    }

    /**
	 * <p>获取密送人列表</p>
	 * @return 密送人列表
	 */
    public List<String> getBcc() {
        return bcc;
    }

    /**
	 * <p>设置密送人列表</p>
	 * @param bcc 密送人列表
	 */
    public void setBcc(List<String> bcc) {
        this.bcc = bcc;
    }

    /**
	 * </p>获取日期</p>
	 * @return 日期
	 */
    public Date getDate() {
        return date;
    }

    /**
	 * <p>设置日期</p>
	 * @param date 日期
	 */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
	 * <p>获取邮件主题</p>
	 * @return 邮件主题
	 */
    public String getSubject() {
        return subject;
    }

    /**
	 * <p>设置邮件主题</p>
	 * @param subject 邮件主题t
	 */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
	 * <p>获取邮件正文</p>
	 * @return 邮件正文
	 */
    public String getBody() {
        return body;
    }

    /**
	 * <p>设置邮件正文</p>
	 * @param body 邮件正文
	 */
    public void setBody(String body) {
        this.body = body;
    }

    /**
	 * <p>获取附件列表</p>
	 * @return 附件列表
	 */
    public List<Attachment> getAttachments() {
        return attachments;
    }

    /**
	 * <p>Add attachment</p>
	 * @param att
	 */
    public void addAttachment(Attachment att) {
        this.attachments.add(att);
    }

    /**
	 * <p>设置附件列表</p>
	 * @param attachments 附件列表
	 */
    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    /**
	 * <p>获取邮件大小</p>
	 * @return the 邮件大小
	 */
    public int getSize() {
        return size;
    }

    /**
	 * <p>设置邮件大小</p>
	 * @param size 邮件大小
	 */
    public void setSize(int size) {
        this.size = size;
    }

    /**
	 * <p>获取邮件优先级</p>
	 * @return 邮件优先级
	 */
    public int getPriority() {
        return priority;
    }

    /**
	 * <p>设置邮件优先级</p>
	 * @param priority 邮件优先级
	 */
    public void setPriority(int priority) {
        this.priority = priority;
    }

    /**
	 * <p>获取邮件答复人列表</p>
	 * @return ArrayList
	 */
    public List<String> getReplyList() {
        return replyList;
    }

    /**
	 * <p>设置邮件优先级</p>
	 * @param list 答复人列表
	 */
    public void setReplyList(List<String> list) {
        this.replyList = list;
    }

    /**
	 * <p>自定义的MailMessage转换成JavaMail的Message</p>
	 * @return JavaMail的Message
	 * @throws MessagingException 
	 */
    protected Message toJavaMailMessage() throws Exception {
        Message message = new MimeMessage(MailSender.getNormalSMTPSession());
        message.setSubject(this.subject);
        message.setSentDate(new Date());
        message.setHeader("X-Priority", this.priority + "");
        message.setFrom(MailMessage.toAddress(this.from));
        message.setReplyTo(MailMessage.toAddressArray(this.replyList));
        for (String str : this.to) {
            message.setRecipient(RecipientType.TO, MailMessage.toAddress(str));
        }
        for (String str : this.cc) {
            message.setRecipient(RecipientType.CC, MailMessage.toAddress(str));
        }
        for (String str : this.bcc) {
            message.setRecipient(RecipientType.BCC, MailMessage.toAddress(str));
        }
        MimeMultipart mmp = null;
        if (this.attachments.size() == 0) {
            logger.trace("no attachmennts");
            mmp = new MimeMultipart("alternative");
        } else {
            logger.trace("has attachmennts");
            mmp = new MimeMultipart();
        }
        message.setContent(mmp);
        MimeBodyPart part = new MimeBodyPart();
        part.setText(this.body, "UTF-8");
        mmp.addBodyPart(part);
        if (this.attachments != null) {
            for (Attachment att : this.attachments) {
                DataSource source = null;
                try {
                    source = new FileDataSource(att.getFilePath());
                } catch (Exception e) {
                    logger.error(e);
                }
                if (source == null) {
                    logger.trace("cannot get file");
                    continue;
                }
                MimeBodyPart fileBodyPart = new MimeBodyPart();
                fileBodyPart.setDataHandler(new DataHandler(source));
                fileBodyPart.setFileName(MimeUtility.encodeText(att.getFileName(), "UTF-8", null));
                mmp.addBodyPart(fileBodyPart);
            }
        }
        return message;
    }

    /**
	 * <p>将自定义地址格式转换成javaMail中的Address</p>
	 * @param addressStr
	 * @return
	 * @throws Exception
	 */
    private static Address toAddress(String addressStr) throws Exception {
        if (StringUtils.isBlank(addressStr)) {
            return null;
        }
        addressStr = addressStr.replace(MailMessage.USUALLY_ERROR_SEPARATOR, MailMessage.SEPARATOR);
        String personal = StringUtils.substringBefore(addressStr, MailMessage.SEPARATOR);
        String address = StringUtils.substringAfter(addressStr, MailMessage.SEPARATOR);
        if (StringUtils.isBlank(address)) {
            address = addressStr;
        }
        Address javaMailAddress = new InternetAddress(address, personal);
        return javaMailAddress;
    }

    /**
	 * <p>将List中的地址列表转换成JavaMail Address数组</p>
	 * @param list	地址列表
	 * @return
	 * @throws Exception
	 */
    private static Address[] toAddressArray(List<String> list) throws Exception {
        List<Address> addressList = new ArrayList<Address>();
        if (list == null) {
            return null;
        }
        for (String str : list) {
            addressList.add(MailMessage.toAddress(str));
        }
        return addressList.toArray(new Address[] {});
    }
}
