package codebush.service.support.mail;

import java.util.Map;
import javax.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 
 * @author Fution Bai
 * @since 1.0
 */
public class CodebushMailSender {

    private JavaMailSender mailSender;

    private Configuration configuration;

    private String recommendationOfTopicMailBodyContentTemplateViewName;

    private String passwordResetMailBodyContentTemplateViewName;

    /**
	 * peocess the templateName freemarker template with this model
	 * 
	 * @param from
	 * @param to
	 * @param subject
	 * @param templateName
	 * @param model
	 */
    public void send(final String from, final String[] to, final String subject, final String templateName, final Map model) {
        this.getMailSender().send(new MimeMessagePreparator() {

            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper mmh = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                mmh.setTo(to);
                mmh.setFrom(from);
                mmh.setSubject(subject);
                Template template = configuration.getTemplate(templateName);
                String text = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
                mmh.setText(text, true);
            }
        });
    }

    public JavaMailSender getMailSender() {
        return mailSender;
    }

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void setFreemarkerConfigurer(FreeMarkerConfigurer freemarkerConfigurer) {
        this.configuration = freemarkerConfigurer.getConfiguration();
    }

    public String getRecommendationOfTopicMailBodyContentTemplateViewName() {
        return recommendationOfTopicMailBodyContentTemplateViewName;
    }

    public void setRecommendationOfTopicMailBodyContentTemplateViewName(String recommendationOfTopicMailBodyContentTemplateViewName) {
        this.recommendationOfTopicMailBodyContentTemplateViewName = recommendationOfTopicMailBodyContentTemplateViewName;
    }

    public String getPasswordResetMailBodyContentTemplateViewName() {
        return passwordResetMailBodyContentTemplateViewName;
    }

    public void setPasswordResetMailBodyContentTemplateViewName(String passwordResetMailBodyContentTemplateViewName) {
        this.passwordResetMailBodyContentTemplateViewName = passwordResetMailBodyContentTemplateViewName;
    }
}
