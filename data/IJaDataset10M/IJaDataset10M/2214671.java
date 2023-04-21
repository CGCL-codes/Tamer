package lang2.vm.jre;

import java.util.Comparator;
import org.gocha.collection.graph.Edge;
import org.gocha.collection.graph.Path;

/**
 * Взвешенный Caster
 * @author gocha
 */
public class WeightedCaster implements Caster, EdgeWeight {

    protected Caster delegateTarget = null;

    protected int weight = 0;

    /**
     * Конструктор
     * @param delegateTarget Кому делегируются обращения cast(..)
     * @param weight Вес
     */
    public WeightedCaster(Caster delegateTarget, int weight) {
        if (delegateTarget == null) {
            throw new IllegalArgumentException("delegateTarget==null");
        }
        this.delegateTarget = delegateTarget;
        this.weight = weight;
    }

    @Override
    public Object cast(Object from) {
        return delegateTarget.cast(from);
    }

    @Override
    public int getEdgeWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "WeightedCaster(" + weight + "):" + delegateTarget.toString();
    }

    /**
     * Возвращает вес пути
     * @param path Путь
     * @return Вес
     */
    public static int getWeightOf(Path<Class, Caster> path) {
        if (path == null) {
            throw new IllegalArgumentException("path==null");
        }
        int weight = 0;
        for (Edge<Class, Caster> e : path) {
            if (e == null) continue;
            Caster c = e.getEdge();
            if (c == null) continue;
            if (c instanceof EdgeWeight) {
                int w = ((EdgeWeight) c).getEdgeWeight();
                weight += w;
            }
        }
        return weight;
    }

    private static final Comparator<Path<Class, Caster>> comparator = new Comparator<Path<Class, Caster>>() {

        @Override
        public int compare(Path<Class, Caster> p1, Path<Class, Caster> p2) {
            int weight1 = getWeightOf(p1);
            int weight2 = getWeightOf(p2);
            return (weight1 == weight2 ? 0 : (weight1 > weight2 ? 1 : -1));
        }
    };

    /**
     * Сравнение веса путей
     * @return Сравнение
     */
    public static Comparator<Path<Class, Caster>> getWeightComparator() {
        return comparator;
    }
}
