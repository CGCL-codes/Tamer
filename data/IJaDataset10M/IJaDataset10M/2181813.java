package org.gocha.collection;

/**
 * Предикат
 * @author GoCha
 */
public interface Predicate<T> {

    /**
     * Проверка значение
     * @param value Значение
     * @return true - прошло проверку, false - не прошло проверкуs
     */
    boolean validate(T value);
}
