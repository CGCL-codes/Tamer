package org.openexi.fujitsu.util;

import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class MessageResolver {

    private static final String REPLACEMENT_TEXT_NULL = "<null/>";

    private final ResourceBundle m_bundle;

    private final PrintStream m_pStream;

    public MessageResolver(Class<?> cls) {
        this(cls.getName() + "XMsg", null);
    }

    public MessageResolver(Class<?> cls, Locale locale) {
        this(cls.getName() + "XMsg", locale, null);
    }

    public MessageResolver(Class<?> cls, PrintStream ps) {
        this(cls.getName() + "XMsg", null, ps);
    }

    public MessageResolver(Class<?> cls, Locale locale, PrintStream ps) {
        this(cls.getName() + "XMsg", locale, ps);
    }

    MessageResolver(String baseName, Locale locale) {
        this(baseName, locale, null);
    }

    MessageResolver(String baseName, Locale locale, PrintStream ps) {
        if (locale == null) locale = Locale.getDefault();
        m_pStream = ps;
        m_bundle = ResourceBundle.getBundle(baseName, locale);
    }

    public String getMessage(int code) {
        String msg = null;
        try {
            msg = getMessage(code, null);
        } catch (MissingResourceException e) {
            if (m_pStream != null) {
                m_pStream.println(e.getMessage());
                e.printStackTrace(m_pStream);
            }
        }
        return msg;
    }

    public String getMessage(int code, String[] texts) {
        texts = texts != null ? texts : new String[0];
        String templ = m_bundle.getString(String.valueOf(code));
        String msg = templ;
        if (templ != null && templ.length() > 0 && texts.length > 0) {
            for (int i = 0; i < texts.length; i++) if (texts[i] == null) texts[i] = REPLACEMENT_TEXT_NULL;
            msg = MessageFormat.format(templ, (Object[]) texts);
        }
        return msg != null ? msg : "";
    }

    /**
   * Returns resource bundle. (For unit tests)
   */
    protected ResourceBundle getBundle() {
        return m_bundle;
    }
}
