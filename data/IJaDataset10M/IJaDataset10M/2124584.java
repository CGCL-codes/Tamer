package org.punit.runner;

import org.punit.message.*;
import org.punit.method.builder.*;
import org.punit.method.runner.*;

public class ConcurrentRunner extends AbstractRunner {

    private static final long serialVersionUID = -7193902024861434576L;

    private static final int DEFAULT_CONCURRENT_COUNT = 10;

    public ConcurrentRunner() {
        this(ConcurrentRunner.DEFAULT_CONCURRENT_COUNT);
    }

    public ConcurrentRunner(int concurrentCount) {
        super(new MethodBuilderImpl(), new ConcurrentMethodRunner(concurrentCount));
    }

    public String punitName() {
        return Messages.getString("runner.03");
    }
}
