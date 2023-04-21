package org.gocha.collection;

/**
 * Визитер, простая реализация
 * @author gocha
 */
public class BasicVisitor<T> implements Visitor<T> {

    @Override
    public void enter(T obj) {
    }

    @Override
    public void exit(T obj) {
    }

    /**
     * Обходит дерево
     * @param <T> Тип узла дерева
     * @param visitor Визитер
     * @param start Начальный узел
     * @param extracter Дочерние узлы
     */
    public static <T> void visit(Visitor<T> visitor, T start, NodesExtracter<T, T> extracter) {
        if (visitor == null) {
            throw new IllegalArgumentException("visitor == null");
        }
        if (extracter == null) {
            throw new IllegalArgumentException("extracter == null");
        }
        visitor.enter(start);
        Iterable<T> children = extracter.extract(start);
        if (children != null) {
            for (T n : children) {
                visit(visitor, n, extracter);
            }
        }
        visitor.exit(start);
    }
}
