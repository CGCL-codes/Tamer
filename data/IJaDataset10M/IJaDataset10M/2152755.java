package org.portmap.server.core;

import org.portmap.server.core.threadpool.Job;
import org.portmap.server.framework.Logger;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

abstract class TheJob implements Job {

    protected final JobPair jobPair;

    protected volatile InputStream is;

    protected volatile OutputStream os;

    protected volatile OutputStream logs;

    private final byte[] buffer = new byte[4096];

    volatile boolean socketShutdown = false;

    public TheJob(JobPair jobPair) {
        Logger.trace(".TheJob()");
        this.jobPair = jobPair;
    }

    public void run() {
        Logger.trace("TheJob.run()");
        try {
            while (true) {
                int i = is.read();
                if (i < 0) return;
                os.write(i);
                if (logs != null) logs.write(i);
                while (is.available() > 0) {
                    int bytesRead = is.read(buffer, 0, Math.min(is.available(), buffer.length));
                    os.write(buffer, 0, bytesRead);
                    if (logs != null) logs.write(buffer, 0, bytesRead);
                }
                os.flush();
                if (logs != null) logs.flush();
            }
        } catch (IOException e) {
            if (!socketShutdown) Logger.debug("Socket operation failed", e);
        } finally {
            Logger.trace("TheJob.run() finished");
        }
    }

    protected void closeStreams() {
        Logger.trace("TheJob.closeStreams()");
        try {
            is.close();
        } catch (IOException e) {
        } catch (NullPointerException e) {
        }
        try {
            os.close();
        } catch (IOException e) {
        } catch (NullPointerException e) {
        }
        try {
            logs.close();
        } catch (IOException e) {
        } catch (NullPointerException e) {
        }
        boolean doStopAnotherJob = false;
        synchronized (jobPair.stopJobsMutex) {
            Logger.trace("TheJob.closeStreams(): stopJobsCounter = " + jobPair.stopJobsCounter + "->" + (jobPair.stopJobsCounter + 1));
            if (jobPair.stopJobsCounter == 0) doStopAnotherJob = true;
            jobPair.stopJobsCounter++;
            if (jobPair.stopJobsCounter == 2) {
                jobPair.stopJobsMutex.notify();
            }
        }
        if (doStopAnotherJob) jobPair.closeSockets();
    }
}
