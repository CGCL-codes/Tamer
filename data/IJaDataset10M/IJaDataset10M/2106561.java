package net.admin4j.util.notify;

import java.util.Properties;
import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import net.admin4j.deps.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Places a notification in the log.
 * @author D. Ashmore
 * @since 1.0
 */
public class LogNotifier implements Notifier {

    private static Logger logger = LoggerFactory.getLogger(LogNotifier.class);

    public void configure(ServletConfig config) throws ServletException {
    }

    public void configure(FilterConfig config) throws ServletException {
    }

    public void configure(String namePrefix, Properties config) {
    }

    public void notify(String subject, String message) {
        StringBuffer buffer = new StringBuffer(1024);
        if (subject != null) {
            buffer.append(subject);
            buffer.append(SystemUtils.LINE_SEPARATOR);
        }
        if (message != null) {
            buffer.append(message);
        }
        logger.error(message);
    }

    public boolean supportsHtml() {
        return false;
    }

    public boolean supportsSMS() {
        return false;
    }
}
