package org.dml.level010;

import org.dml.tools.RunTime;
import org.dml.tools.TwoWayHashMap;

/**
 * Symbol in storage<br>
 * instances of Symbol gotten from storage, which are in common to all environments<br>
 * for example, two different environments may return the same Long but because they are in different environment they
 * are different Symbols<br>
 * but this class doesn't care about different environments - that will be done in Symbol class - this class is a
 * mapping between the stored and retrieved Symbol before it is split by environments<br>
 */
public class TheStoredSymbol {

    private static final TwoWayHashMap<TheStoredSymbol, Long> allStoredSymbolsFromAllEnvironments = new TwoWayHashMap<TheStoredSymbol, Long>();

    private final Long l;

    /**
	 * private constructor
	 */
    private TheStoredSymbol(Long l1) {
        l = l1;
        RunTime.assumedNotNull(l);
    }

    /**
	 * @param l1
	 * @return never null
	 */
    public static TheStoredSymbol getNew(Long l1) {
        RunTime.assumedNotNull(l1);
        TheStoredSymbol ret = null;
        ret = allStoredSymbolsFromAllEnvironments.getKey(l1);
        if (null == ret) {
            ret = new TheStoredSymbol(l1);
            RunTime.assumedFalse(allStoredSymbolsFromAllEnvironments.ensure(ret, l1));
        }
        RunTime.assumedNotNull(ret);
        return ret;
    }

    public Long getLong() {
        RunTime.assumedNotNull(l);
        return l;
    }

    @Override
    public boolean equals(Object obj) {
        if (null != obj) {
            if (super.equals(obj)) {
                return true;
            } else {
                if (obj.getClass().equals(this.getClass())) {
                    if (l.equals(((TheStoredSymbol) obj).l)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return l.hashCode();
    }

    /**
	 * for use in junit only!
	 */
    public static void junitClearCache() {
        allStoredSymbolsFromAllEnvironments.clear();
    }
}
