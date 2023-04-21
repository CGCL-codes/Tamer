package org.gocha.data.exception;

import org.gocha.data.DataException;

/**
 * Исключение преобразования данных
 * @author gocha
 */
public class InvalidDataCastException extends DataException {

    private static String message = "Не возможно преобразовать данные";

    /**
     * Конструктор
     */
    public InvalidDataCastException() {
        super(message);
    }

    /**
     * Конструктор
     * @param cFrom Из какого типа
     * @param cTo К какому типу
     */
    public InvalidDataCastException(Class cFrom, Class cTo) {
        super(makeMessage(cFrom, cTo));
    }

    /**
     * Конструктор
     * @param message сообщение
     */
    public InvalidDataCastException(String message) {
        super(message);
    }

    private static String makeMessage(Class cFrom, Class cTo) {
        if (cFrom == null || cTo == null) return message;
        return message.replace("%from%", cFrom.getCanonicalName()).replace("%to%", cTo.getCanonicalName());
    }
}
