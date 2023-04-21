package com.lts.ipc.test.semaphore;

import com.lts.ipc.test.ArgumentException;
import com.lts.ipc.test.TestClass;
import com.lts.ipc.test.TestMode;

public abstract class ClientServerTest extends TestClass {

    protected abstract void report();

    private TestMode myTestMode = TestMode.Server;

    protected void processArgumentNormal(String argument) throws ArgumentException {
        TestMode mode = TestMode.toValueIgnoreCase(argument);
        setTestMode(mode);
    }

    public TestMode getTestMode() {
        return myTestMode;
    }

    public void setTestMode(TestMode mode) {
        myTestMode = mode;
    }

    protected void checkReport() {
        if (System.currentTimeMillis() > getNextReport()) {
            noteStopTime();
            report();
            resetIterations();
            noteStartTime();
            updateNextReport();
        }
    }
}
