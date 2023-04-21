package org.pixory.pxnet;

import javax.mail.MessagingException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PXMessagingExceptionUtility extends Object {

    private static final Log LOG = LogFactory.getLog(PXMessagingExceptionUtility.class);

    private static final String NESTED_EXCEPTION_PATTERN = "nested exception is:[\\s,\\w,\\.]+:";

    private PXMessagingExceptionUtility() {
    }

    /**
	 * strips out all the references to java classes, uncoverering just the core
	 * messages in the MessagingException stack. The goal is to produce something
	 * that is more presentable to an end user
	 */
    public static String getUserMessage(MessagingException exception) {
        String getUserMessage = null;
        if (exception != null) {
            String aMessage = exception.getMessage();
            getUserMessage = aMessage.replaceAll(NESTED_EXCEPTION_PATTERN, "");
        }
        return getUserMessage;
    }
}
