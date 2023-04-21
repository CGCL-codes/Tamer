package net.taylor.mail;

import static org.jboss.seam.ScopeType.APPLICATION;
import static org.jboss.seam.annotations.Install.BUILT_IN;
import java.util.Properties;
import javax.mail.Session;
import javax.naming.InitialContext;
import net.taylor.jndi.JndiUtil.NonSerializableFactory;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.log.Log;

/**
 * Binds a mail session to the jndi tree as java:/Mail.
 * 
 */
@Name("org.jboss.seam.mail.sessionFactory")
@Startup
@Scope(APPLICATION)
@BypassInterceptors
@Install(precedence = BUILT_IN, classDependencies = "org.jvnet.mock_javamail.Mailbox")
public class SessionFactory {

    @Logger
    private Log log;

    private Properties mailProps = new Properties();

    @Create
    public void create() throws Exception {
        mailProps.put("mail.smtp.host", "localhost");
        mailProps.put("mail.smtp.port", "24");
        mailProps.put("mail.transport.protocol", "smtp");
        mailProps.put("mail.debug", "true");
        Session session = Session.getDefaultInstance(mailProps);
        try {
            NonSerializableFactory.bind(new InitialContext(), "java:/Mail", session);
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
        }
    }
}
