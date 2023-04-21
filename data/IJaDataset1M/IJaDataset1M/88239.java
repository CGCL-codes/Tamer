package com.sonic.jms.interceptors;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.jms.Message;
import javax.jms.TextMessage;
import com.sonic.jms.exceptions.MQuotesListenerException;
import com.sonic.log.ejb.impl.MQLogger;

/**
 * MDB <tt>onMessage()</tt> method interceptor.<br>
 * Logs incoming messages from ESB.
 * 
 * @author <b>Adam Dec</b> for <b><I>ComArch S.A.</i></b><br> Copyright &#169; 2008
 * @since 25-09-2008
 * @category MDB INTERCEPTOR
 * @version 1.0
 */
public class MQuotesProcessorListener {

    static MQLogger logger = new MQLogger(MQuotesProcessorListener.class, "CII-MQUOTES-LISTENER");

    private boolean logSending = true;

    @AroundInvoke
    public Object logIncomingMessages(InvocationContext context) throws Exception {
        final String methodName = "logIncomingMessages()";
        Object target = context.getTarget();
        final String targetMethodName = context.getMethod().getName();
        final Message message = (Message) context.getParameters()[0];
        if (logSending) logger.debug(target.getClass().getName(), "--- " + methodName + " Logging '" + targetMethodName + "' function argument:");
        if (message == null) {
            logger.debug(target.getClass().getName(), " --- " + methodName + " No incoming message or incoming message is not in text format.");
            throw new MQuotesListenerException(target.getClass().getName() + " --- " + methodName + " No incoming message or incoming message is not in text format.");
        }
        if (logSending) logger.debug(target.getClass().getName() + " --- " + methodName, " Incoming message: " + (((TextMessage) message).getText()));
        return context.proceed();
    }
}
