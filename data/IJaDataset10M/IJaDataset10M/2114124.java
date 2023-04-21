package java.lang;

import org.apache.harmony.kernel.vm.LangAccess;

final class LangAccessImpl extends LangAccess {

    static final LangAccessImpl THE_ONE = new LangAccessImpl();

    /** 
     * This class is not publicly instantiable. Use {@link #THE_ONE}.
     */
    private LangAccessImpl() {
    }

    /** {@inheritDoc} */
    public <T> T[] getEnumValuesInOrder(Class<T> clazz) {
        ClassCache<T> cache = clazz.getClassCache();
        return cache.getEnumValuesInOrder();
    }

    /** {@inheritDoc} */
    public void unpark(Thread thread) {
        thread.unpark();
    }

    /** {@inheritDoc} */
    public void parkFor(long nanos) {
        Thread.currentThread().parkFor(nanos);
    }

    /** {@inheritDoc} */
    public void parkUntil(long time) {
        Thread.currentThread().parkUntil(time);
    }
}
