package org.dbe.composer.wfengine.bpel.webserver.web;

import org.apache.axis.MessageContext;
import org.apache.axis.handlers.BasicHandler;
import org.apache.log4j.Logger;

/**
 * This class restores the original classloader which was replaced
 * with the services context classloader by
 * <code>org.activebpel.rt.tomcat.AeSetClassLoader</code>.
 */
public class SdlRestoreClassLoaderHandler extends BasicHandler {

    private static final Logger logger = Logger.getLogger(SdlRestoreClassLoaderHandler.class.getName());

    /**
     * Restore original classloader.
     * @see org.apache.axis.Handler#invoke(org.apache.axis.MessageContext)
     */
    public void invoke(MessageContext aMessageContext) {
        logger.info("invoke(MessageContext)");
        restoreClassLoader(aMessageContext);
    }

    /**
     * Restore original classloader.
     * @see org.apache.axis.Handler#onFault(org.apache.axis.MessageContext)
     */
    public void onFault(MessageContext aMessageContext) {
        restoreClassLoader(aMessageContext);
    }

    /**
     * Puts the original classloader context back in place from the one
     * saved in the passed message context.
     * @param aMessageContext the message context to restore the classloader for.
     */
    protected void restoreClassLoader(MessageContext aMessageContext) {
        ClassLoader loader = (ClassLoader) aMessageContext.getProperty(SdlSetClassLoaderHandler.SAVED_CLASSLOADER_PROPERTY);
        if (loader != null) {
            aMessageContext.setProperty(SdlSetClassLoaderHandler.SAVED_CLASSLOADER_PROPERTY, null);
            Thread.currentThread().setContextClassLoader(loader);
        }
    }
}
