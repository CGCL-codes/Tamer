package de.uni_leipzig.lots.webfrontend.mail;

import de.uni_leipzig.lots.common.objects.Role;
import de.uni_leipzig.lots.common.objects.User;
import de.uni_leipzig.lots.server.persist.UserRepository;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModelException;
import freemarker.ext.beans.BeansWrapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.SimpleMailMessage;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;

/**
 * Factory for mail messages which the admin receives as confirmation of a userregistration.
 *
 * @author Alexander Kiel
 * @version $Id: RegistrationAdminMailMessageFactory.java,v 1.9 2007/10/23 06:30:30 mai99bxd Exp $
 */
public class RegistrationAdminMailMessageFactory extends BaseMailMessageFactory {

    private UserRepository userRepository;

    public RegistrationAdminMailMessageFactory() {
    }

    @Required
    public void setUserRepository(@NotNull UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public SimpleMailMessage[] getMailMessages(@NotNull User registeredUser, @NotNull String identifier) {
        assert userRepository != null : "userRepository != null";
        Collection<User> admins = userRepository.getAllWithRole(Role.admin);
        SimpleMailMessage[] messages = new SimpleMailMessage[admins.size()];
        Date now = new Date();
        int i = 0;
        for (User admin : admins) {
            SimpleMailMessage message = new SimpleMailMessage(template);
            message.setTo(admin.getTutorDisplayName() + "<" + admin.getEmail() + ">");
            message.setSentDate(now);
            Map<String, Object> model = new HashMap<String, Object>();
            try {
                model.put("Salutation", BeansWrapper.getDefaultInstance().getEnumModels().get("de.uni_leipzig.lots.common.objects.Salutation"));
            } catch (TemplateModelException e) {
                throw new MailPreparationException("TemplateModelException while composing a " + "RegistrationAdminMailMessage with model: " + model, e);
            }
            model.put("admin", admin);
            model.put("registeredUser", registeredUser);
            model.put("identifier", identifier);
            try {
                Template template = freeMarkerConfig.getTemplate("RegistrationAdminMailSubject.ftl", admin.getDefaultLocale());
                StringWriter writer = new StringWriter();
                template.process(model, writer);
                message.setSubject(writer.toString());
                template = freeMarkerConfig.getTemplate("RegistrationAdminMailMessage.ftl", admin.getDefaultLocale());
                writer = new StringWriter();
                template.process(model, writer);
                message.setText(writer.toString());
            } catch (IOException e) {
                throw new MailPreparationException("IOException while composing a " + "RegistrationAdminMailMessage with model: " + model, e);
            } catch (TemplateException e) {
                throw new MailPreparationException("TemplateException while composing a " + "RegistrationAdminMailMessage with model: " + model, e);
            }
            messages[i++] = message;
        }
        return messages;
    }
}
