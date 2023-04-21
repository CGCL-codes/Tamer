package gov.nasa.jpf.jvm;

import gov.nasa.jpf.util.IntVector;
import gov.nasa.jpf.util.ObjVector;

public abstract class CachingSerializerDeserializer extends AbstractSerializerDeserializer implements IncrementalChangeTracker {

    protected final IntVector daCache = new IntVector();

    protected final IntVector saCache = new IntVector();

    static final class TCacheEntry {

        final IntVector cache = new IntVector();

        ThreadInfo ti;
    }

    protected final ObjVector<TCacheEntry> threadCaches = new ObjVector<TCacheEntry>();

    protected TCacheEntry ensureAndGetThreadEntry(int i) {
        TCacheEntry entry = threadCaches.get(i);
        if (entry == null) {
            entry = new TCacheEntry();
            threadCaches.set(i, entry);
        }
        return entry;
    }

    protected int[] computeStoringData() {
        updateThreadListCache(ks.tl);
        updateStaticAreaCache(ks.sa);
        updateDynamicAreaCache(ks.da);
        int totalLen = 1;
        int nThreads = threadCaches.size();
        for (int i = 0; i < nThreads; i++) {
            totalLen += threadCaches.get(i).cache.size();
        }
        totalLen += saCache.size() + daCache.size();
        int[] data = new int[totalLen];
        data[0] = nThreads;
        int pos = 1;
        for (int i = 0; i < nThreads; i++) {
            pos = threadCaches.get(i).cache.dumpTo(data, pos);
        }
        pos = saCache.dumpTo(data, pos);
        pos = daCache.dumpTo(data, pos);
        assert pos == totalLen;
        return data;
    }

    protected void updateThreadListCache(ThreadList tl) {
        int length = tl.length();
        for (int i = 0; i < length; i++) {
            ThreadInfo ti = tl.get(i);
            TCacheEntry cache = ensureAndGetThreadEntry(i);
            updateThreadCache(ti, cache);
        }
        threadCaches.setSize(length);
    }

    protected abstract void updateThreadCache(ThreadInfo ti, TCacheEntry entry);

    protected abstract void updateDynamicAreaCache(DynamicArea a);

    protected abstract void updateStaticAreaCache(StaticArea a);

    protected void doRestore(int[] data) {
        ArrayOffset storing = new ArrayOffset(data);
        doRestore(ks.tl, storing);
        doRestore(ks.sa, storing);
        doRestore(ks.da, storing);
    }

    protected void doRestore(ThreadList tl, ArrayOffset storing) {
        int newLength = storing.get();
        ThreadInfo[] threads = new ThreadInfo[newLength];
        for (int i = 0; i < newLength; i++) {
            threads[i] = restoreThreadInfo(storing, ensureAndGetThreadEntry(i));
        }
        tl.setAll(threads);
    }

    protected abstract ThreadInfo restoreThreadInfo(final ArrayOffset storing, TCacheEntry entry);

    protected abstract void doRestore(DynamicArea a, ArrayOffset storing);

    protected abstract void doRestore(StaticArea a, ArrayOffset storing);
}
