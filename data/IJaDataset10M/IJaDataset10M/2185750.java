package com.genia.toolbox.security.annotation.ws_interceptor;

import java.io.OutputStream;
import org.apache.cxf.io.CacheAndWriteOutputStream;
import org.apache.cxf.message.Message;
import org.apache.log4j.Logger;

/**
 *
 * Class called by the annotation CatchOutInterceptor
 * This class is used to print in a logger the message send by the annotation
 *
 */
public class ReadOutInterceptor {

    private static final Logger logger = Logger.getLogger(ReadOutInterceptor.class);

    public final Message message;

    /**
	 * Constructor needs the message send by the annotation
	 * @param msg
	 */
    public ReadOutInterceptor(Message msg) {
        this.message = msg;
    }

    /**
	 * Method used to read and print the outflow.
	 */
    public void logging() {
        InterceptorWrapper wrap = new InterceptorWrapper(message);
        final OutputStream os = (OutputStream) wrap.getContent(OutputStream.class);
        if (os == null) {
            logger.error("The OutputStream is empty.");
            return;
        }
        final CacheAndWriteOutputStream newOut = new CacheAndWriteOutputStream(os);
        message.setContent(OutputStream.class, newOut);
        newOut.registerCallback(new LoggingCallback(message, os));
    }
}
