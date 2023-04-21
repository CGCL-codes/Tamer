package net.sf.webwarp.base.i18n.impl;

import java.util.List;
import java.util.Locale;
import net.sf.webwarp.base.i18n.Message;
import net.sf.webwarp.base.i18n.MessageProvider;
import org.springframework.context.MessageSource;

/**
 * Simple message provider implementation based on the Spring MessageSource class.
 * 
 * @see org.springframework.context.MessageSource
 * @author mos
 */
public class MessageProviderSpringImpl implements MessageProvider {

    /**
     * @see net.sf.webwarp.base.i18n.MessageProvider#getPriority()
     */
    public int getPriority() {
        return PRIORITY_NORMAL;
    }

    private MessageSource messageSource;

    public String getMessage(String code, String defaultMessage, Locale locale) {
        return messageSource.getMessage(code, null, defaultMessage, locale);
    }

    public String getMessage(String code, Locale locale) {
        return messageSource.getMessage(code, null, locale);
    }

    public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
        return messageSource.getMessage(code, args, defaultMessage, locale);
    }

    public String getMessage(String code, Object[] args, Locale locale) {
        return messageSource.getMessage(code, args, locale);
    }

    /**
     * Sets the Spring messageSource.
     * 
     * @param messageSource
     *            The Spring message source.
     */
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Throws a UnsupportedOperationException since the underlying message source does not support it. Use one of the
     * other message provider implementations.
     * 
     * @see net.sf.webwarp.base.i18n.MessageProvider
     * @see net.sf.webwarp.base.i18n.impl.MessageProviderRAMImpl
     * @see net.sf.webwarp.base.i18n.impl.MessageProviderDAOImpl
     * @see net.sf.webwarp.base.i18n.MultiMessageProvider
     * @see net.sf.webwarp.base.i18n.MessageProvider#getMessages()
     */
    public List<Message> getMessages() {
        throw new UnsupportedOperationException("getMessages is only supported by other message provider implementations.");
    }

    /**
     * Throws a UnsupportedOperationException since the underlying message source does not support it. Use one of the
     * other message provider implementations.
     * 
     * @param code
     *            The message code
     * @see net.sf.webwarp.base.i18n.MessageProvider
     * @see net.sf.webwarp.base.i18n.impl.MessageProviderRAMImpl
     * @see net.sf.webwarp.base.i18n.impl.MessageProviderDAOImpl
     * @see net.sf.webwarp.base.i18n.MultiMessageProvider
     * @see net.sf.webwarp.base.i18n.MessageProvider#getMessages()
     */
    public List<Message> getMessagesByCode(String code) {
        throw new UnsupportedOperationException("getMessages is only supported by other message provider implementations.");
    }

    /**
     * Throws a UnsupportedOperationException since the underlying message source does not support it. Use one of the
     * other message provider implementations.
     * 
     * @param locale
     *            The locale or null for the default locale
     * @see net.sf.webwarp.base.i18n.MessageProvider
     * @see net.sf.webwarp.base.i18n.impl.MessageProviderRAMImpl
     * @see net.sf.webwarp.base.i18n.impl.MessageProviderDAOImpl
     * @see net.sf.webwarp.base.i18n.MultiMessageProvider
     * @see net.sf.webwarp.base.i18n.MessageProvider#getMessages()
     */
    public List<Message> getMessages(Locale locale) {
        throw new UnsupportedOperationException("getMessages is only supported by other message provider implementations.");
    }

    public Message getMessageObject(String code, Locale locale) {
        throw new UnsupportedOperationException("getMessageObject is only supported by other message provider implementations.");
    }
}
