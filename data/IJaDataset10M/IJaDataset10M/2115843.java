package org.gocha.types;

import java.util.HashMap;
import java.util.Map;
import org.gocha.collection.Iterators;
import org.gocha.collection.NodesExtracter;

/**
 * Карта выполняющая доступ к дочерним узлам дерева, на основании типа узлов.
 * @author gocha
 */
public class ClassNodesExtracterMap implements NodesExtracter {

    /**
     * Карта доступа к дочерним объектам класса
     */
    protected Map<Class, NodesExtracter> extractersMap = null;

    /**
     * Указывает карту доступа к классам
     * @return Карта доступа к дочерним объектам класса
     */
    public Map<Class, NodesExtracter> getExtractersMap() {
        if (extractersMap == null) {
            extractersMap = new HashMap<Class, NodesExtracter>();
        }
        return extractersMap;
    }

    /**
     * Указывает карту доступа к классам
     * @param extractersMap Карта доступа к дочерним объектам класса
     */
    public void setExtractersMap(Map<Class, NodesExtracter> extractersMap) {
        this.extractersMap = extractersMap;
    }

    /**
     * Объект достпука к дочерним элементам или null
     */
    protected NodesExtracter nextExtracter = null;

    /**
     * Указывает след. объект доступа, если значение null, либо нет подходящего
     * @return Объект достпука к дочерним элементам или null
     */
    public NodesExtracter getNextExtracter() {
        return nextExtracter;
    }

    /**
     * Указывает след. объект доступа, если значение null, либо нет подходящего
     * @param nextExtracter Объект достпука к дочерним элементам или null
     */
    public void setNextExtracter(NodesExtracter nextExtracter) {
        this.nextExtracter = nextExtracter;
    }

    /**
     * Значение по умолчанию, используется если нет подходящего значение в карте и не указан след. объект доступа
     * @see #getExtractersMap()
     * @see #getNextExtracter()
     */
    protected Iterable defaultIterable = emptyIterable();

    /**
     * Указывает значение по умолчанию
     * @return Значение по умолчанию
     */
    public Iterable getDefaultIterable() {
        return defaultIterable;
    }

    /**
     * Указывает значение по умолчанию
     * @param defaultIterable Значение по умолчанию
     */
    public void setDefaultIterable(Iterable defaultIterable) {
        this.defaultIterable = defaultIterable;
    }

    private static Iterable emptyIterable = Iterators.empty();

    /**
     * Указывает пустой набор объектов
     * @return Пустой набор объектов
     */
    public static Iterable emptyIterable() {
        return emptyIterable;
    }

    /**
     * Извлекает дочерние объекты, для класса переданного объекта, в соответствии с картой доступа
     * @param from Объект для которого в карте доступа осуществляется поиск
     * @return Дочерние объекты
     */
    @Override
    public Iterable extract(Object from) {
        if (from == null) {
            NodesExtracter next = getNextExtracter();
            if (next != null) return nextExtracter.extract(from);
            return getDefaultIterable();
        }
        Class cls = from.getClass();
        Map<Class, NodesExtracter> map = getExtractersMap();
        if (map.containsKey(cls)) {
            NodesExtracter ext = map.get(cls);
            if (ext == null) {
                NodesExtracter next = getNextExtracter();
                if (next != null) return nextExtracter.extract(from);
                return getDefaultIterable();
            }
            return ext.extract(from);
        }
        NodesExtracter next = getNextExtracter();
        if (next != null) return nextExtracter.extract(from);
        return getDefaultIterable();
    }
}
