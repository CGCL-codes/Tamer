package com.hs.mail.imap.processor;

/**
 * 
 * @author Won Chul Doh
 * @since Feb 1, 2010
 *
 */
public class SelectProcessor extends AbstractSelectProcessor {

    @Override
    protected boolean isReadOnly() {
        return false;
    }
}
