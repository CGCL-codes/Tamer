package org.anddev.andengine.extension.multiplayer.protocol.exception;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 16:58:37 - 20.03.2011
 */
public class WifiException extends Exception {

    private static final long serialVersionUID = -8647288255044498718L;

    /**
         * 
         */
    public WifiException() {
    }

    /**
         * 
         * @param pMessage
         */
    public WifiException(final String pMessage) {
        super(pMessage);
    }

    /**
         * 
         * @param pMessage
         * @param pThrowable
         */
    public WifiException(final String pMessage, final Throwable pThrowable) {
        super(pMessage, pThrowable);
    }
}
