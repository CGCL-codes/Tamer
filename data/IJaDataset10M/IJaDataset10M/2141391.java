package org.gocha.data;

/**
 * Исключение связанное с работой с данными
 * @author gocha
 */
public class DataException extends Exception {

    /**
     * Конструктор
     * @param message описание ошибки
     */
    public DataException(String message) {
        super(message);
    }

    /**
     * Конструктор
     * @param inner Вложенная ошибка
     */
    public DataException(Throwable inner) {
        super(inner);
    }
}
