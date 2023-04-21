package com.googlecode.yoohoo.message;

import java.util.Locale;

public interface IMessageManager {

    public static final String DEFAULT_I18N_PATH = "/META-INF/i18n";

    public static final String MESSAGE_MANAGER_BUNDLE_SYMBOLIC_NAME = "__yoohoo_m_m_b_s_n__";

    String getMessage(String key, Locale locale);

    String getMessage(String key, String[] args, Locale locale);

    String getMessage(String key, String[] args, String defaultMessage, Locale locale);
}
