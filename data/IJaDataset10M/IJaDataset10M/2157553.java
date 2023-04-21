package org.gocha.collection.graph;

import java.util.List;

/**
 * Описывает путь в графе
 * @author gocha
 */
public interface Path<N, E> extends List<Edge<N, E>> {

    /**
     Описывает напарвления движения
     */
    public static enum Direction {

        /**
         Из вершины A в вершину B
         */
        AB, /**
         Из вершины B в вершину A
         */
        BA
    }

    /**
     Описывает направление движения
     @return направление
     */
    Direction getDirection();

    /**
     Возвращает последенее ребро в пути (index=size-1)
     @return Последнее ребро или null (если элементы пути отсуствуют)
     */
    Edge<N, E> getLastEdge();

    /**
     Возвращает последнюю вершину в пути, согласно направлению (AB -&gt; B / BA -&gt; A)
     @return Последняя вершина в пути или null (если элементы пути отсуствуют)
     */
    N getLastNode();

    /**
     Проверяет содержит ли путь ребро из вершины A в B
     @param a вершина A
     @param b вершина B
     @return true - есть такое ребро
     */
    boolean contains(N a, N b);
}
