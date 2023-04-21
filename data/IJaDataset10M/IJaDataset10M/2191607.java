package org.gocha.collection.map;

/**
 * Событие добавления данных в карту
 * @author gocha
 */
public class InsertedMapEvent<Key, Value> extends KeyValueMapEvent<Key, Value> {

    /**
     * Конструктор
     * @param source Источник
     * @param key Ключ
     * @param value Значение
     */
    public InsertedMapEvent(EventMap<Key, Value> source, Key key, Value value) {
        super(source, key, value);
    }
}
