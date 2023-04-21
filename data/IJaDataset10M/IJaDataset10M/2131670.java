package org.javatech.benchmark.java.collections;

import java.util.Collection;

public abstract class AbstractAddingToCollectionBenchmark extends AbstractStringDataBenchmark {

    @Override
    public void runInternalTrial() throws Exception {
        Collection<String> c = getCollectionImpl();
        for (int i = 0; i < getWorkload(); i++) {
            c.add("" + i);
        }
    }

    public abstract Collection<String> getCollectionImpl();
}
