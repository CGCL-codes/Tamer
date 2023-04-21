package net.sf.japi.io.args;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import org.jetbrains.annotations.NotNull;

/** Compares methods by their {@link Option}s.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @since 0.2
 */
public class MethodOptionComparator implements Comparator<Method>, Serializable {

    /** Serial version. */
    private static final long serialVersionUID = 1L;

    /** Global instance. */
    @NotNull
    public static final Comparator<Method> INSTANCE = new MethodOptionComparator();

    /** {@inheritDoc} */
    public int compare(@NotNull final Method o1, @NotNull final Method o2) {
        final Option option1 = o1.getAnnotation(Option.class);
        final Option option2 = o2.getAnnotation(Option.class);
        final String[] names1 = option1.value();
        final String[] names2 = option2.value();
        Arrays.sort(names1);
        Arrays.sort(names2);
        int result = String.CASE_INSENSITIVE_ORDER.compare(names1[0], names2[0]);
        if (result == 0) {
            result = names1[0].compareTo(names2[0]);
        }
        return result;
    }
}
