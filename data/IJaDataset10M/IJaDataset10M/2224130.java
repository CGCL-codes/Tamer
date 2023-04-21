package com.ohua.engine;

import java.util.concurrent.atomic.AtomicInteger;
import com.ohua.engine.flowgraph.elements.AbstractUniqueID;

public class ProcessID extends AbstractUniqueID {

    protected ProcessID(int id) {
        super(id);
    }

    public static class ProcessIDGenerator {

        private static AtomicInteger _processIDCounter = new AtomicInteger(0);

        public static ProcessID generateNewProcessID() {
            return new ProcessID(_processIDCounter.getAndIncrement());
        }

        public static void resetIDCounter() {
            _processIDCounter = new AtomicInteger(0);
        }
    }
}
