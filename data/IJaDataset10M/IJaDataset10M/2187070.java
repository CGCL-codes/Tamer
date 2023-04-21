package com.mainatom.utils;

import java.text.*;
import java.util.*;

/**
 * Локализированные сообщения
 * <p/>
 * ЗАМОРОЖЕНО ДО ПОРЫ ДО ВРЕМЕНИ
 */
@Deprecated
public class LangMsg {

    private static LangMsg _inst;

    /**
     * Локаль по умолчанию
     */
    private Locale _defaultLocale = Locale.getDefault();

    /**
     * Получить сообщение для класса
     *
     * @param locale      локаль
     * @param forObject   Объект, для которого сгенерировано сообщение. Может быть классом
     * @param messageCode код сообщения
     * @param params      параметры сообщения
     * @return текст в указанной локали
     */
    public static String g(Locale locale, Object forObject, String messageCode, Object... params) {
        return getInst().get(locale, forObject, messageCode, params);
    }

    /**
     * Получить сообщение для класса в текущей локали
     *
     * @param forObject   Объект, для которого сгенерировано сообщение. Может быть классом
     * @param messageCode код сообщения
     * @param params      параметры сообщения
     * @return текст в указанной локали
     */
    public static String g(Object forObject, String messageCode, Object... params) {
        return getInst().get(null, forObject, messageCode, params);
    }

    public static LangMsg getInst() {
        if (_inst == null) {
            _inst = new LangMsg();
        }
        return _inst;
    }

    /**
     * Получить сообщение для класса
     *
     * @param locale      локаль
     * @param forObject   Объект, для которого сгенерировано сообщение. Может быть классом
     * @param messageCode код сообщения
     * @param params      параметры сообщения
     * @return текст в указанной локали
     */
    public String get(Locale locale, Object forObject, String messageCode, Object... params) {
        if (locale == null) {
            locale = _defaultLocale;
        }
        if (forObject == null) {
            forObject = Object.class;
        }
        Class cls;
        if (forObject instanceof String) {
            try {
                cls = Class.forName((String) forObject);
            } catch (ClassNotFoundException e) {
                cls = Object.class;
            }
        } else if (forObject instanceof Class) {
            cls = (Class) forObject;
        } else {
            cls = forObject.getClass();
        }
        String msg = findMessage(messageCode, cls);
        if (msg == null) {
            msg = "NOT_LOCALIZED: " + messageCode;
        }
        return MessageFormat.format(msg, params);
    }

    protected String findMessage(String messageCode, Class cls) {
        return null;
    }
}
