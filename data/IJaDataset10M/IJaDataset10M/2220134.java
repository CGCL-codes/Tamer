package com.genia.toolbox.projects.csv.business.util.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import com.genia.toolbox.basics.i18n.EnumDescriptable;
import com.genia.toolbox.basics.i18n.I18nMessage;
import com.genia.toolbox.projects.csv.business.util.EnumerationUtils;
import com.genia.toolbox.spring.provider.message.manager.CustomResourceBundleMessageSource;

/**
 * the implementation class of the {@link EnumerationUtils} interface.
 */
public class EnumerationUtilsImpl implements EnumerationUtils {

    /**
   * reference to the {@link CustomResourceBundleMessageSource}.
   */
    private CustomResourceBundleMessageSource messageSource;

    /**
   * get the corresponding messages for all instances of the given enum for the
   * specified locale.
   * 
   * @param <T>
   *          the type of the enum
   * @param locale
   *          the {@link Locale} to consider
   * @param classz
   *          the {@link Class} of the enum
   * @return a {@link List} of string message in the concerned language
   */
    public <T extends Enum<T>> List<String> getAllMessageValues(Locale locale, Class<T> classz) {
        List<String> messages = new ArrayList<String>();
        for (T enumInstance : classz.getEnumConstants()) {
            if (enumInstance instanceof EnumDescriptable) {
                I18nMessage i18nMessage = ((EnumDescriptable) enumInstance).getDescription();
                messages.add(getMessageSource().getMessage(i18nMessage, locale));
            } else {
                messages.add(enumInstance.name());
            }
        }
        return messages;
    }

    /**
   * get the corresponding enum instance for the given locale and the string
   * message value in the concerned language.
   * 
   * @param <T>
   *          the type of the enum
   * @param locale
   *          the {@link Locale} to consider
   * @param classz
   *          the {@link Class} of the enum
   * @param messageValue
   *          the string message value
   * @return the corresponding enum instance or null if no such message value
   */
    public <T extends Enum<T>> T getEnumInstance(Locale locale, Class<T> classz, String messageValue) {
        if (EnumDescriptable.class.isAssignableFrom(classz)) {
            for (T enumInstance : classz.getEnumConstants()) {
                I18nMessage i18nMessage = ((EnumDescriptable) enumInstance).getDescription();
                if (messageValue.equals(getMessageSource().getMessage(i18nMessage, locale))) {
                    return enumInstance;
                }
            }
        }
        return Enum.valueOf(classz, messageValue);
    }

    /**
   * setter injection of the {@link CustomResourceBundleMessageSource}
   * reference.
   * 
   * @param messageSource
   *          the {@link CustomResourceBundleMessageSource} reference
   */
    public void setMessageSource(CustomResourceBundleMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
   * getter for the messageSource property.
   * 
   * @return the messageSource
   */
    public CustomResourceBundleMessageSource getMessageSource() {
        return messageSource;
    }
}
