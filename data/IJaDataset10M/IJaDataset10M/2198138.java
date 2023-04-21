package functionalProgramming;

import java.util.ArrayList;

public class FunctionalStuff {

    public static <T, V> T fold(ArrayList<V> ba, T t, Fold<T, V> fold) {
        for (V v : ba) {
            t = fold.foldWith(t, v);
        }
        return t;
    }

    public static <T> void forEach(ArrayList<T> ba, Apply<T> apply) {
        for (T t : ba) {
            apply.apply(t);
        }
    }

    public static <T> ArrayList<T> filter(ArrayList<T> ba, Filter<T> filter) {
        ArrayList<T> out = new ArrayList<T>();
        for (T t : ba) {
            if (filter.decide(t)) out.add(t);
        }
        return out;
    }
}
