package magoffin.matt.ieat.aop;

import java.util.Locale;
import java.util.Map;
import magoffin.matt.ieat.EatProcessingException;
import magoffin.matt.ieat.util.MailMergeSupport;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;

/**
 * Base class for mail merge interceptors.
 * 
 * <p>This interceptor serves as a base for email interceptors using 
 * templated email content (mail merge). The {@link #invoke(MethodInvocation)}
 * method performs the following steps:</p>
 * 
 * <ol>
 *   <li>Calls {@link #getInitialModel(MethodInvocation)}. This method must 
 *   return a non-null Map to use for the model data for the mail merge.</li>
 * 
 *   <li>Calls {@link MethodInvocation#proceed()}.</li>
 * 
 *   <li>Calls {@link #postProcessModel(MethodInvocation, Map, Object)}, passing 
 *   the model Map previously returned and the result of the method invocation.</li>
 * 
 *   <li>Calls {@link #getLocale(MethodInvocation, Map, Object)}.</li>
 * 
 *   <li>Calls {@link magoffin.matt.ieat.util.MailMergeSupport#sendMerge(Locale, ClassLoader, Map, SimpleMailMessage)}
 *   to perform the mail merge and send the email.</li>
 * </ol>
 * 
 * <p>The configurable properties of this class are:</p>
 * 
 * <dl>
 *   <dt>mailMergeSupport</dt>
 *   <dd>The {@link magoffin.matt.ieat.util.MailMergeSupport} instance to use for 
 *   sending the mail merge.</dd>
 * </dl>
 * 
 * @author Matt Magoffin (spamsqr@msqr.us)
 * @version $Revision: 23 $ $Date: 2009-05-03 18:59:25 -0400 (Sun, 03 May 2009) $
 */
public abstract class AbstractMailTemplateInterceptor implements MethodInterceptor {

    /** A class-level logger. */
    protected final Logger logger = Logger.getLogger(getClass());

    private MailMergeSupport mailMergeSupport = null;

    public final Object invoke(MethodInvocation invocation) throws Throwable {
        if (mailMergeSupport == null) {
            throw new RuntimeException("Mail templateName not configured in " + getClass());
        }
        Map<String, Object> model = getInitialModel(invocation);
        Object result = invocation.proceed();
        SimpleMailMessage msg = postProcessModel(invocation, model, result);
        Locale locale = getLocale(invocation, model, result);
        try {
            mailMergeSupport.sendMerge(locale, getClass().getClassLoader(), model, msg);
        } catch (MailException ex) {
            logger.warn("Unable to send email: " + ex.toString());
            throw new EatProcessingException(result, "Unable to send email", ex);
        }
        return result;
    }

    /**
	 * Get the locale for the email message.
	 * 
	 * <p>This implementation simply returns {@link Locale#getDefault()}. Extending 
	 * implementations can use the invocation and invocation result to provide 
	 * a custom Locale as deisred.</p>
	 * 
	 * @param invocation the current method invocation
	 * @param model the model
	 * @param result the resut of the current modthod invocation
	 * @return a Locale to use for the mail merge
	 */
    protected Locale getLocale(MethodInvocation invocation, Map<String, Object> model, Object result) {
        return Locale.getDefault();
    }

    /**
	 * Get an initial Map object to use for the model data for the mail merge.
	 * @param invocation the current method invocation
	 * @return Map
	 */
    protected abstract Map<String, Object> getInitialModel(MethodInvocation invocation);

    /**
	 * Get a SimpleMailMessage object based on the result of the method invocation.
	 * 
	 * <p>You can use the MailMergeSupport instance's 
	 * {@link MailMergeSupport#getMessageTemplate()} method to obtain a mail message
	 * template, and use the 
	 * {@link SimpleMailMessage#SimpleMailMessage(org.springframework.mail.SimpleMailMessage)}
	 * copy constructor to create the SimpleMailMessage to return here. Most likely the 
	 * only property you'll need to set on the result if the <code>to</code> property.</p>
	 * 
	 * @param invocation the current method invocation
	 * @param model the model Map
	 * @param result the method invocation result object
	 * @return a SimpleMailMessage object with appropriate information set, 
	 * i.e. the <code>to</code> property
	 */
    protected abstract SimpleMailMessage postProcessModel(MethodInvocation invocation, Map<String, Object> model, Object result);

    /**
	 * @return the mailMergeSupport
	 */
    public MailMergeSupport getMailMergeSupport() {
        return mailMergeSupport;
    }

    /**
	 * @param mailMergeSupport the mailMergeSupport to set
	 */
    public void setMailMergeSupport(MailMergeSupport mailMergeSupport) {
        this.mailMergeSupport = mailMergeSupport;
    }
}
