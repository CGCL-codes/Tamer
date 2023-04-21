package gate.cloud.batch;

import gate.Gate;
import gate.cloud.batch.BatchJobData.JobState;
import gate.cloud.util.Tools;
import gate.cloud.util.XMLBatchParser;
import gate.creole.ResourceInstantiationException;
import gate.util.GateException;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.management.JMException;
import javax.management.ObjectName;
import javax.management.StandardMBean;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.apache.log4j.Logger;

/**
 * This class is a Batch Runner, i.e. it manages the execution of batch jobs,
 * each of them being specified by a {@link Batch} object. This class is a
 * singleton, so you can only obtain an instance via the getInstance()method.
 */
public class BatchRunner {

    private static final Logger log = Logger.getLogger(BatchRunner.class);

    private static final long LOOP_WAIT = 10 * 1000;

    /**
   * This class manages the execution of a batch job. It also exposes a
   * {@link BatchJobData} interface that provides information about the
   * execution progress.
   */
    private class BatchHandler implements BatchJobData {

        /**
     * The document processor that runs the actual jobs.
     */
        private DocumentProcessor processor;

        /**
     * The batch being run.
     */
        private Batch batch;

        private int totalDocs;

        private int successDocs;

        private int errorDocs;

        private JobState state;

        private String id;

        /**
     * The moment when the execution of this batch started.
     */
        private long startTime;

        /**
     * The sum of {@link ProcessResult#getOriginalFileSize()} values for all 
     * processed documents.
     */
        private long totalBytes;

        /**
     * The sum of {@link ProcessResult#getDocumentLength()} values for all 
     * processed documents.
     */
        private long totalChars;

        /**
     * The results queue for this batch.
     */
        private BlockingQueue<ProcessResult> resultQueue;

        /**
     * The report file for this batch.
     */
        private File reportFile;

        /**
     * Writer to write the report file.
     */
        private XMLStreamWriter reportWriter;

        /**
     * Thread that pushes jobs into the DocumentProcessor for this batch.
     */
        private Thread jobPusher;

        private BatchHandler(final Batch batch) throws GateException, IOException {
            successDocs = 0;
            errorDocs = 0;
            totalBytes = 0;
            totalChars = 0;
            this.batch = batch;
            id = batch.getBatchId();
        }

        public void start() throws IOException, XMLStreamException, ResourceInstantiationException {
            reportWriter = batch.getReportWriter();
            totalDocs = batch.getUnprocessedDocumentIDs().length;
            startTime = System.currentTimeMillis();
            setState(JobState.RUNNING);
            resultQueue = new LinkedBlockingQueue<ProcessResult>();
            if (totalDocs > 0) {
                processor = new PooledDocumentProcessor(executor.getCorePoolSize());
                processor.setController(batch.getGateApplication());
                processor.setExecutor(executor);
                processor.setInputHandler(batch.getInputHandler());
                processor.setOutputHandlers(batch.getOutputs());
                processor.setResultQueue(resultQueue);
                processor.init();
                jobPusher = new Thread(new Runnable() {

                    public void run() {
                        for (String id : batch.getUnprocessedDocumentIDs()) {
                            processor.processDocument(id);
                            if (Thread.interrupted()) {
                                return;
                            }
                        }
                    }
                }, "Batch \"" + getBatchId() + "\"-job-pusher");
                jobPusher.start();
            }
        }

        public int getErrorDocumentCount() {
            return errorDocs;
        }

        public int getProcessedDocumentCount() {
            return errorDocs + successDocs;
        }

        public int getRemainingDocumentCount() {
            return totalDocs - errorDocs - successDocs;
        }

        public int getSuccessDocumentCount() {
            return successDocs;
        }

        public int getTotalDocumentCount() {
            return totalDocs;
        }

        public long getTotalDocumentLength() {
            return totalChars;
        }

        public long getTotalFileSize() {
            return totalBytes;
        }

        public JobState getState() {
            return state;
        }

        private void setState(JobState state) {
            this.state = state;
        }

        private void setErrorDocumentCount(int newCount) {
            errorDocs = newCount;
        }

        private void setSuccessDocumentCount(int newCount) {
            successDocs = newCount;
        }

        public long getStartTime() {
            return startTime;
        }

        public String getBatchId() {
            return id;
        }
    }

    /**
   * A SynchronousQueue in which offer delegates to put. ThreadPoolExecutor uses
   * offer to run a new task. Using put instead means that when all the threads
   * in the pool are occupied, execute will wait for one of them to become free,
   * rather than failing to submit the task.
   */
    public static class AlwaysBlockingSynchronousQueue extends SynchronousQueue<Runnable> {

        /**
     * Yes, I know this technically breaks the contract of BlockingQueue, but it
     * works for this case.
     */
        public boolean offer(Runnable task) {
            try {
                put(task);
            } catch (InterruptedException e) {
                return false;
            }
            return true;
        }
    }

    /**
   * A thread that runs continuously while the batch runner is active. Its role
   * is to monitor the running jobs, collect process results, save the report
   * files for each running batch, and shutdown the batch runner and/or Java
   * process when all the batches have completed (if requested via the
   * {@link BatchRunner#shutdownWhenFinished(boolean)} and
   * {@link BatchRunner#exitWhenFinished(boolean)} methods).
   */
    private class JobMonitor implements Runnable {

        public void run() {
            boolean finished = false;
            while (!finished) {
                long startTime = System.currentTimeMillis();
                try {
                    boolean jobsStillRunning = false;
                    Iterator<String> jobsIter = runningJobs.keySet().iterator();
                    while (jobsIter.hasNext()) {
                        String jobId = jobsIter.next();
                        BatchHandler job = runningJobs.get(jobId);
                        if (job.getState() == JobState.RUNNING) {
                            List<ProcessResult> results = new ArrayList<ProcessResult>();
                            int resultsCount = job.resultQueue.drainTo(results);
                            try {
                                for (ProcessResult result : results) {
                                    long fileSize = result.getOriginalFileSize();
                                    long docLength = result.getDocumentLength();
                                    if (fileSize > 0) job.totalBytes += fileSize;
                                    if (docLength > 0) job.totalChars += docLength;
                                    job.reportWriter.writeCharacters("\n");
                                    Tools.writeResultToXml(result, job.reportWriter);
                                    switch(result.getReturnCode()) {
                                        case SUCCESS:
                                            job.successDocs++;
                                            break;
                                        case FAIL:
                                            job.errorDocs++;
                                            break;
                                    }
                                }
                                job.reportWriter.flush();
                                if (job.getRemainingDocumentCount() == 0) {
                                    job.setState(JobState.FINISHED);
                                    job.reportWriter.writeCharacters("\n");
                                    job.reportWriter.writeEndElement();
                                    Tools.writeBatchResultToXml(job, job.reportWriter);
                                    job.reportWriter.close();
                                    if (job.processor != null) job.processor.dispose();
                                } else {
                                    jobsStillRunning = true;
                                }
                            } catch (XMLStreamException e) {
                                log.error("Can't write to report file for batch " + jobId + ", shutting down batch", e);
                                job.jobPusher.interrupt();
                                job.setState(JobState.ERROR);
                            }
                        }
                    }
                    if (!jobsStillRunning) {
                        if (shutdownWhenFinished) {
                            shutdown();
                            finished = true;
                        }
                        if (exitWhenFinished) {
                            System.exit(0);
                        }
                    }
                    long remainingSleepTime = LOOP_WAIT - (System.currentTimeMillis() - startTime);
                    if (!finished && remainingSleepTime > 0) Thread.sleep(remainingSleepTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    finished = true;
                }
            }
        }
    }

    /**
   * Creates a new BatchRunner, with a given number of threads.
   * 
   * @param numThreads
   */
    public BatchRunner(int numThreads) {
        runningJobs = Collections.synchronizedMap(new HashMap<String, BatchHandler>());
        executor = new ThreadPoolExecutor(numThreads, numThreads, 0L, TimeUnit.MILLISECONDS, new AlwaysBlockingSynchronousQueue());
    }

    public void shutdownWhenFinished(boolean flag) {
        synchronized (this) {
            this.shutdownWhenFinished = flag;
        }
    }

    public void exitWhenFinished(boolean flag) {
        synchronized (this) {
            this.exitWhenFinished = flag;
        }
    }

    /**
   * Stops this batch runner in an orderly fashion.
   */
    public void shutdown() {
        executor.shutdown();
        while (!executor.isTerminated()) {
            try {
                executor.awaitTermination(60L, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    /**
   * Stores data about the currently running batch jobs.
   */
    private Map<String, BatchHandler> runningJobs;

    /**
   * Executor used to run the tasks.
   */
    private ThreadPoolExecutor executor;

    /**
   * Thread to monitor jobs.
   */
    private Thread monitorThread;

    /**
   * A flag used to signal that the batch runner should shutdown when all
   * currently running batches have completed.
   */
    private boolean shutdownWhenFinished = true;

    /**
   * A flag used to signal that the batch runner should exit the Java process
   * when all currently running batches have completed.
   */
    private boolean exitWhenFinished = true;

    /**
   * Starts executing the batch task specified by the provided parameter.
   * 
   * @param batch
   *          a {@link Batch} object describing a batch job.
   * @throws IllegalArgumentException
   *           if there are problems with the provided batch specification (e.g.
   *           the new batch has the same ID as a previous batch).
   * @throws IOException
   * @throws GateException
   * @throws XMLStreamException
   */
    public void runBatch(Batch batch) throws IllegalArgumentException, GateException, IOException, XMLStreamException {
        synchronized (this) {
            String batchId = batch.getBatchId();
            if (runningJobs.containsKey(batchId)) {
                throw new IllegalArgumentException("A batch with the same ID (" + batchId + ") is already in process!");
            }
            BatchHandler jobHandler = new BatchHandler(batch);
            try {
                StandardMBean batchMBean = new StandardMBean(jobHandler, BatchJobData.class);
                Hashtable<String, String> props = new Hashtable<String, String>();
                props.put("type", "Batch");
                props.put("id", ObjectName.quote(batch.getBatchId()));
                ObjectName name = ObjectName.getInstance("net.gatecloud", props);
                ManagementFactory.getPlatformMBeanServer().registerMBean(batchMBean, name);
            } catch (JMException e) {
                log.warn("Could not register batch with platform MBean server", e);
            }
            runningJobs.put(batchId, jobHandler);
            jobHandler.start();
            if (monitorThread == null) {
                monitorThread = new Thread(new JobMonitor(), this.getClass().getCanonicalName() + "-Job-monitor");
                monitorThread.start();
            }
        }
    }

    /**
   * Checks if a batch has completed execution.
   * 
   * @param batchId
   * @return <tt>true</tt> iff the batch with the given ID has completed
   *         execution.
   */
    public boolean isFinished(String batchId) {
        return getBatchData(batchId).getState() == JobState.FINISHED;
    }

    /**
   * @param batchId
   * @return
   */
    public BatchJobData getBatchData(String batchId) {
        return null;
    }

    /**
   * Returns a set containing the IDs for all the currently running jobs.
   * 
   * @return a {@link Set} of {@link String}s.
   */
    public Set<String> getBatchJobIDs() {
        return new HashSet<String>(runningJobs.keySet());
    }

    /**
   * Main entry point.  Expects two parameters, a number of threads and a batch
   * file location, and runs the batch in a thread pool of the specified size,
   * exiting when the batch is complete.
   */
    public static void main(String[] args) {
        File gcpHome = new File(System.getProperty("gcp.home", "."));
        if (args.length != 2) {
            System.err.println("Command line arguments should be the number of " + "threads and a batch file!");
            System.exit(1);
        }
        File batchFile = new File(args[1]);
        if (!batchFile.exists()) {
            log.error("The provided file (" + batchFile + ") does not exist!");
            System.exit(1);
        }
        if (!batchFile.isFile()) {
            log.error("The provided file (" + batchFile + ") is not a file!");
            System.exit(1);
        }
        final Thread.UncaughtExceptionHandler defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

            public void uncaughtException(Thread t, Throwable e) {
                if (e instanceof Error) {
                    e.printStackTrace();
                    if (!(e instanceof ThreadDeath)) {
                        System.exit(2);
                    }
                } else if (defaultExceptionHandler != null) {
                    defaultExceptionHandler.uncaughtException(t, e);
                }
            }
        });
        try {
            int numThreads = Integer.parseInt(args[0]);
            File gateHome = new File(gcpHome, "gate-home");
            Gate.setGateHome(gateHome);
            Gate.setUserConfigFile(new File(gateHome, "user-gate.xml"));
            Gate.init();
            BatchRunner instance = new BatchRunner(numThreads);
            Batch aBatch = XMLBatchParser.fromXml(batchFile);
            log.info("Launching batch:\n" + aBatch);
            instance.runBatch(aBatch);
            instance.shutdownWhenFinished(true);
            instance.exitWhenFinished(true);
        } catch (Exception e) {
            log.error("Error starting up batch " + batchFile, e);
            System.exit(1);
        }
    }
}
