package cn.adamkts.mail;

import java.util.Map;
import javax.annotation.Resource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.util.Assert;

/**
 * 模板邮件支持,可以在邮件中直接使用$message之类的velocity标记 注意:to,cc均接收多个接收者,用","隔开,比如 111@111.com,222@222.com
 * 
 * @author Atomic
 */
@Service
public class VelocityMimeMessagePreparator implements MimeMessagePreparator {

    @Resource
    private VelocityEngine velocityEngine;

    @Resource
    private TemplateMailFactory templateMailFactory;

    private String to = "";

    private String cc = "";

    private Map<String, Object> dataContext;

    private String mailId;

    public void prepare(MimeMessage message) throws Exception {
        MailTemplateDefinition definition = templateMailFactory.create(mailId);
        Assert.notNull(definition);
        message.addFrom(InternetAddress.parse(definition.getFrom()));
        message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
        message.setSubject(definition.getSubject());
        MimeMultipart multipart = new MimeMultipart("alternative");
        message.setContent(multipart);
        if (definition.isPlainTextMail()) {
            BodyPart plainText = new MimeBodyPart();
            plainText.setText(VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, definition.getPlainTextTemplate(), "GBK", dataContext));
            multipart.addBodyPart(plainText);
        }
        if (definition.isHtmlMail()) {
            BodyPart html = new MimeBodyPart();
            html.setContent(VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, definition.getHtmlTemplate(), "UTF-8", dataContext), "text/html;charset=UTF-8");
            multipart.addBodyPart(html);
        }
    }

    public String getTo() {
        return to;
    }

    public void setMailId(String mailId) {
        this.mailId = mailId;
    }

    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public void setDataContext(Map<String, Object> dataContext) {
        this.dataContext = dataContext;
    }

    public void setTemplateMailFactory(TemplateMailFactory templateMailFactory) {
        this.templateMailFactory = templateMailFactory;
    }
}
