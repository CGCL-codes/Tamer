package net.sf.callmesh.util;

import java.util.ArrayList;
import java.util.List;

public final class Filters {

    private Filters() {
    }

    @SuppressWarnings("unchecked")
    public static <VALUE> Filter[] array(Iterable<Filter<VALUE>> filters) {
        List<Filter<VALUE>> tmp = new ArrayList<Filter<VALUE>>();
        for (Filter<VALUE> filter : filters) tmp.add(filter);
        return tmp.toArray(new Filter[0]);
    }

    /** accepts every value */
    public static <VALUE> Filter<VALUE> always() {
        return new Filter<VALUE>() {

            public boolean accept(VALUE element) {
                return true;
            }
        };
    }

    /** accepts no value */
    public static <VALUE> Filter<VALUE> never() {
        return new Filter<VALUE>() {

            public boolean accept(VALUE element) {
                return false;
            }
        };
    }

    /** accepts if the sub does not accept */
    public static <VALUE> Filter<VALUE> not(final Filter<VALUE> sub) {
        return new Filter<VALUE>() {

            public boolean accept(VALUE element) {
                return !sub.accept(element);
            }
        };
    }

    /** accepts if all subs accept */
    public static <VALUE> Filter<VALUE> and(final Filter<VALUE>... subs) {
        return new Filter<VALUE>() {

            public boolean accept(VALUE element) {
                for (Filter<VALUE> sub : subs) {
                    if (!sub.accept(element)) return false;
                }
                return true;
            }
        };
    }

    /** accepts if at least one sub accepts */
    public static <VALUE> Filter<VALUE> or(final Filter<VALUE>... subs) {
        return new Filter<VALUE>() {

            public boolean accept(VALUE element) {
                for (Filter<VALUE> sub : subs) {
                    if (sub.accept(element)) return true;
                }
                return false;
            }
        };
    }
}
