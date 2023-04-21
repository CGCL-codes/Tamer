package jolie.runtime;

import java.util.Collection;
import java.util.HashSet;
import jolie.ExecutionThread;
import jolie.SessionThread;
import jolie.process.Process;
import jolie.process.SpawnProcess;

public class SpawnExecution {

    private class SpawnedThread extends SessionThread {

        private final int index;

        public SpawnedThread(ExecutionThread parentThread, Process process, int index) {
            super(process, parentThread);
            this.index = index;
        }

        @Override
        public void run() {
            parentSpawnProcess.indexPath().getValue().setValue(index);
            try {
                process().run();
            } catch (FaultException f) {
            } catch (ExitingException e) {
            }
            if (parentSpawnProcess.inPath() != null) {
                parentSpawnProcess.inPath().getValueVector(ethread.state().root()).get(index).deepCopy(parentSpawnProcess.inPath().getValueVector().first());
            }
            terminationNotify(this);
        }
    }

    private final Collection<SpawnedThread> threads = new HashSet<SpawnedThread>();

    private final SpawnProcess parentSpawnProcess;

    private final ExecutionThread ethread = ExecutionThread.currentThread();

    public SpawnExecution(SpawnProcess parent) {
        this.parentSpawnProcess = parent;
    }

    public void run() throws FaultException {
        if (parentSpawnProcess.inPath() != null) {
            parentSpawnProcess.inPath().undef();
        }
        int upperBound = parentSpawnProcess.upperBound().evaluate().intValue();
        SpawnedThread thread;
        synchronized (this) {
            for (int i = 0; i < upperBound; i++) {
                thread = new SpawnedThread(ethread, parentSpawnProcess.body(), i);
                threads.add(thread);
            }
            for (SpawnedThread t : threads) {
                t.start();
            }
            while (!threads.isEmpty()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                }
            }
        }
    }

    private void terminationNotify(SpawnedThread thread) {
        synchronized (this) {
            threads.remove(thread);
            if (threads.isEmpty()) {
                notify();
            }
        }
    }
}
