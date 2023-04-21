package net.sourceforge.velai.examples;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import net.sourceforge.velai.ExecutionContext;
import net.sourceforge.velai.ProgressState;
import net.sourceforge.velai.annotations.Context;
import net.sourceforge.velai.annotations.Daemon;
import net.sourceforge.velai.annotations.OnCancel;
import net.sourceforge.velai.annotations.Progress;

@Daemon
public class SimpleTaskExample implements Callable<String> {

    @Context
    private ExecutionContext execution;

    private boolean talkative = false;

    private String result;

    public SimpleTaskExample() {
        this(false);
    }

    public SimpleTaskExample(String result) {
        this(result, false);
    }

    public SimpleTaskExample(boolean talkative) {
        this("the result", talkative);
    }

    public SimpleTaskExample(String result, boolean talkative) {
        this.result = result;
        this.talkative = talkative;
    }

    @Override
    public String call() throws Exception, InterruptedException {
        while (true) {
            if (talkative) System.out.println("[" + Thread.currentThread().getName() + "]: started");
            if (execution.isCancellationRequested(true)) {
                if (talkative) System.out.println("[" + Thread.currentThread().getName() + "]: cancelled");
                throw new CancellationException();
            }
            Thread.sleep(100);
            break;
        }
        if (talkative) System.out.println("[" + Thread.currentThread().getName() + "]: success");
        return result;
    }

    @OnCancel
    public void cancelled() {
    }

    @Progress
    public ProgressState getProgress() {
        return null;
    }
}
