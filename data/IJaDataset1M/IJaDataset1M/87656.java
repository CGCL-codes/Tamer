package org.az.paccman.services;

import java.util.HashMap;
import java.util.Map;
import javax.mail.internet.MimeMessage;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.az.paccman.ConfigurationConst;
import org.az.tb.common.vo.client.ResourceIDConst;
import org.az.tb.services.client.BeanNamesConst;
import org.az.tb.services.client.SnippetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.VelocityEngineUtils;

@Component(BeanNamesConst.MAIL_SERVICE_BEAN_NAME)
public class MailServiceImpl implements MailService {

    private static final String DEFAULT_ENCODING = "UTF-8";

    private static final Logger logger = Logger.getLogger(MailServiceImpl.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ConfigService config;

    @Autowired
    private VelocityEngine velocityEngine;

    @Autowired
    private SnippetService resources;

    private static Map<String, String> commonResources;

    public void initCommonResources(String subject) {
        commonResources = new HashMap<String, String>();
        commonResources.put("siteName", resources.get(ResourceIDConst.SITE_NAME));
        commonResources.put("siteUrl", config.getValue(ConfigurationConst.SYSTEM_URL));
        commonResources.put("subject", subject);
    }

    public void send(final String template, final String senderEmail, final String receiverEmail, final String subject, final Map<String, Object> model) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {

            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage, DEFAULT_ENCODING);
                if (commonResources == null) {
                    initCommonResources(subject);
                }
                model.put("common", commonResources);
                String personal = (String) model.get("fromPersonal");
                String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, template, DEFAULT_ENCODING, model);
                logger.debug("message text is:\n" + text);
                message.setTo(receiverEmail);
                message.setSubject(subject);
                if (personal != null) {
                    message.setFrom(senderEmail == null ? config.getValue(ConfigurationConst.EMAIL_DEFAULT_SENDER_ADDRESS) : senderEmail, personal);
                } else {
                    message.setFrom(senderEmail == null ? config.getValue(ConfigurationConst.EMAIL_DEFAULT_SENDER_ADDRESS) : senderEmail);
                }
                message.setText(text, true);
                logger.info("sending message with template " + template + " to " + receiverEmail + " from " + senderEmail);
            }
        };
        mailSender.send(preparator);
    }
}
