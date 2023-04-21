package ru.adv.util;

/**
 * Указывает на невозможность преобразования строки в boolean
 * @see ru.adv.util.StringParser
 */
public class BadBooleanException extends ErrorCodeException {

    /**
	 * Создает объект исключения с описанием проблемы
	 * @param msg описание проблемы
	 */
    public BadBooleanException(String msg, String value) {
        super(INVALID_BOOLEAN_VALUE, msg);
        setAttr("value", value);
    }
}
