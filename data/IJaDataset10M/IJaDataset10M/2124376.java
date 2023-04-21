package org.gocha.collection;

/**
 * Пара значений
 * @author gocha
 */
public interface Pair<A, B> {

    /**
     * Возвращает первый элемент пары
     * @return первый элемент пары
     */
    A A();

    /**
     * Возвращает второй элемент пары
     * @return второй элемент пары
     */
    B B();
}
