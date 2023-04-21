package net.sf.jfpl.algorithm;

import java.util.Iterator;
import net.sf.jfpl.core.Fun1;
import net.sf.jfpl.core.Fun2;

/**
 * 
 * @author <a href="mailto:sunguilin@users.sourceforge.net">Guile</a>
 */
public class ForEach<T> extends Fun2<Iterator<T>, Fun1<? super T, ?>, Integer> {

    @Override
    public Integer call(final Iterator<T> itor, final Fun1<? super T, ?> functor) {
        return forEach(itor, functor);
    }

    public static <K> ForEach<K> forEach(Class<K> clazz) {
        return new ForEach<K>();
    }

    public static <K> int forEach(final Iterator<K> itor, final Fun1<? super K, ?> functor) {
        int counter = 0;
        while (itor.hasNext()) {
            functor.call(itor.next());
            counter++;
        }
        return counter;
    }

    public static <K> int forEach(final Iterable<K> iterable, final Fun1<? super K, ?> functor) {
        return forEach(iterable.iterator(), functor);
    }
}
