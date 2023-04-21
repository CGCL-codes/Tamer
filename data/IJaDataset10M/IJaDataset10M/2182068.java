package edu.cmu.ece.agora.core;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.Executor;
import edu.cmu.ece.agora.futures.Future;
import edu.cmu.ece.agora.futures.FutureListener;

public class OutputStreamAdapter extends AbstractAsyncOutputStream {

    private final OutputStream os;

    private final Executor boss_exec;

    public OutputStreamAdapter(OutputStream os, Executor boss_exec, Executor worker_exec) {
        super(worker_exec);
        this.os = os;
        this.boss_exec = boss_exec;
        doWritePoll();
    }

    private void doWritePoll() {
        this.pollWrite().addListener(new FutureListener<byte[]>() {

            @Override
            public void onCancellation(Throwable cause) {
            }

            @Override
            public void onCompletion(final byte[] result) {
                boss_exec.execute(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            os.write(result);
                            os.flush();
                        } catch (IOException e) {
                        }
                        doWritePoll();
                    }
                });
            }
        });
    }

    @Override
    public Future<Void> close() {
        try {
            os.close();
        } catch (IOException e) {
        }
        return super.close();
    }
}
