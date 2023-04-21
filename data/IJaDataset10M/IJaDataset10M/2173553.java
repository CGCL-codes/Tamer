package edu.cmu.ece.agora.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executor;

public class InputStreamAdapter extends AbstractAsyncInputStream {

    private static final int DEFAULT_BUFSZ = 64;

    private final InputStream is;

    private final int bufsz;

    private final Executor boss_exec;

    public InputStreamAdapter(InputStream is, Executor boss_exec, Executor worker_exec) {
        this(is, boss_exec, worker_exec, DEFAULT_BUFSZ);
    }

    public InputStreamAdapter(InputStream is, Executor boss_exec, Executor worker_exec, int bufsz) {
        super(worker_exec);
        this.is = is;
        this.bufsz = bufsz;
        this.boss_exec = boss_exec;
        runBoss();
    }

    private void runBoss() {
        boss_exec.execute(new Runnable() {

            @Override
            public void run() {
                byte[] buf = new byte[bufsz];
                while (true) {
                    int read;
                    try {
                        read = is.read(buf);
                    } catch (IOException e) {
                        break;
                    }
                    if (read == -1) break;
                    readCompleted(buf, 0, read);
                }
                try {
                    is.close();
                } catch (IOException e) {
                }
                readClosed();
            }
        });
    }
}
