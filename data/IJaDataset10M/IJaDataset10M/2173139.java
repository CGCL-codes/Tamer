package au.edu.uq.itee.maenad.pronto.importer;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Importer {

    private DescriptorSource source;

    private URL targetUrl;

    private long heartbeat;

    private String username;

    private String password;

    public Importer(DescriptorSource source, URL targetUrl, String username, String password, long heartbeat) {
        this.source = source;
        this.targetUrl = targetUrl;
        this.username = username;
        this.password = password;
        this.heartbeat = heartbeat;
    }

    public List<Result> run() throws IOException, InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newCachedThreadPool();
        List<Future<Result>> futures = new ArrayList<Future<Result>>();
        for (Descriptor descriptor : source.getDescriptors()) {
            SubmissionTask task = new SubmissionTask(targetUrl, username, password, descriptor);
            futures.add(executor.submit(task));
            Thread.sleep(heartbeat);
        }
        List<Result> results = new ArrayList<Result>();
        for (Future<Result> future : futures) {
            try {
                results.add(future.get(10, TimeUnit.SECONDS));
            } catch (TimeoutException ex) {
                results.add(new Result(Result.Type.DOWNLOAD_ERROR, "Timeout (probably on the download end): " + ex.getMessage(), -1, null));
                Logger.getLogger(getClass().getName()).log(Level.WARNING, "We got a timeout", ex);
            } catch (ExecutionException ex) {
                results.add(new Result(Result.Type.DOWNLOAD_ERROR, "Exception caught: " + ex.getMessage(), -1, null));
                Logger.getLogger(getClass().getName()).log(Level.WARNING, "We got an exception", ex);
            }
        }
        executor.shutdown();
        return results;
    }
}
