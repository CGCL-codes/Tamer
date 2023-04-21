package squirrels.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import nuts.core.net.SendMail;
import nuts.ext.struts2.views.freemarker.FreemarkerUtils;
import nuts.ext.xwork2.util.ContextUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import squirrels.persistence.bean.Customer;
import com.opensymphony.xwork2.TextProvider;
import com.opensymphony.xwork2.util.ValueStack;
import freemarker.template.TemplateException;

/**
 * CustomerUtils.
 */
public abstract class CustomerUtils extends ActionUtils {

    /**
	 * log
	 */
    protected static Log log = LogFactory.getLog(CustomerUtils.class);

    /**
	 * get Customer from session
	 * @return Customer
	 */
    public static Customer getLoginCustomer() {
        Customer o = (Customer) getSession().get(CUSTOMER);
        return o;
    }

    /**
	 * set Customer to session
	 * @param customer customer
	 */
    public static void setLoginCustomer(Customer customer) {
        getSession().put(CUSTOMER, customer);
    }

    /**
	 * remove Customer from session
	 */
    public static void removeLoginCustomer() {
        getSession().remove(CUSTOMER);
    }

    /**
	 * create a illegal customer id exception
	 * @param id customer id
	 * @return exception
	 */
    public static Exception IllegalCustomerIdException(Integer id) {
        return new IllegalArgumentException("Incorrect Customer ID [" + id + "].");
    }

    private static final SimpleDateFormat COC_DF = new SimpleDateFormat("yyyyMMdd-HHmmss-SSS");

    /**
	 * generate customer order unique code
	 * @return code
	 */
    public static String genCustomerOrderCode() {
        Date d = Calendar.getInstance().getTime();
        return COC_DF.format(d) + "-" + d.getTime();
    }

    /**
	 * send email
	 * @param customer customer
	 * @param subjectId subject template id
	 * @param contentId msg content template id
	 * @param context context object
	 * @throws IOException io error 
	 * @throws TemplateException  template error
	 * @throws EmailException  email error
	 */
    public static void sendTemplateMail(Customer customer, String subjectId, String contentId, Object context) throws TemplateException, IOException, EmailException {
        ValueStack vs = ContextUtils.getValueStack();
        if (context != null) {
            vs.push(context);
        }
        String subject;
        String content;
        try {
            subject = FreemarkerUtils.processTemplate(subjectId);
            content = FreemarkerUtils.processTemplate(contentId);
        } finally {
            if (context != null) {
                vs.pop();
            }
        }
        sendMail(customer, subject, content);
    }

    /**
	 * send email
	 * @param customer customer
	 * @param subjectId subject template id
	 * @param contentId msg content template id
	 * @throws IOException io error 
	 * @throws TemplateException  template error
	 * @throws EmailException  email error
	 */
    public static void sendTemplateMail(Customer customer, String subjectId, String contentId) throws TemplateException, IOException, EmailException {
        String subject = FreemarkerUtils.processTemplate(subjectId);
        String content = FreemarkerUtils.processTemplate(contentId);
        sendMail(customer, subject, content);
    }

    /**
	 * send email
	 * @param customer customer
	 * @param subject subject
	 * @param msg msg
	 * @throws EmailException if an email error occurs
	 */
    public static void sendMail(Customer customer, String subject, String msg) throws EmailException {
        if (log.isDebugEnabled()) {
            log.debug("\n" + "============SEND EMAIL================================\n" + "TO     : " + customer.getName() + "<" + customer.getEmail() + ">\n" + "SUBJECT: " + subject + "\n" + "MESSAGE: " + msg + "\n" + "=======================================================");
        }
        TextProvider tp = ContextUtils.getTextProvider();
        SimpleEmail email = new SimpleEmail();
        String charset = tp.getText(MAIL_CHARSET, "");
        if (StringUtils.isNotEmpty(charset)) {
            email.setCharset(charset);
        }
        email.setFrom(tp.getText(MAIL_FROM));
        email.setSubject(subject);
        email.addTo(customer.getEmail(), customer.getName());
        email.setMsg(msg);
        String host = tp.getText(MAIL_HOST, "");
        if (StringUtils.isNotEmpty(host)) {
            email.setHostName(host);
            String username = tp.getText(MAIL_HOST_USERNAME, "");
            if (StringUtils.isNotEmpty(username)) {
                email.setAuthentication(username, tp.getText(MAIL_HOST_PASSWORD, ""));
            }
            String bounce = tp.getText(MAIL_BOUNCE, "");
            if (StringUtils.isNotEmpty(bounce)) {
                email.setBounceAddress(bounce);
            }
            email.send();
        } else {
            SendMail.send(email);
        }
    }

    /**
	 * ingore email exception
	 * @return true if mail-exception is set to warn/ignore
	 */
    public static boolean ignoreEmailException() {
        TextProvider tp = ContextUtils.getTextProvider();
        String iee = tp.getText(MAIL_EXCEPTION, "");
        if ("warn".equals(iee) || "ignore".equals(iee)) {
            return true;
        }
        return false;
    }
}
