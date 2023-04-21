package net.sf.buildbox.worker.impl;

import com.caucho.hessian.client.HessianProxyFactory;
import net.sf.buildbox.reactor.api.JobController;
import net.sf.buildbox.reactor.model.*;
import net.sf.buildbox.worker.api.ExecutionContext;
import net.sf.buildbox.worker.api.Transfer;
import net.sf.buildbox.worker.api.WorkerFeedback;
import net.sf.buildbox.worker.util.WorkerUtils;
import org.apache.commons.io.FileSystemUtils;
import org.codehaus.plexus.util.FileUtils;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class Worker implements Callable<Integer> {

    private static final long bootTime = System.currentTimeMillis();

    private static final Logger LOGGER = Logger.getLogger(Worker.class.getName());

    private JobController controller;

    private Transfer transfer;

    private long requestTimeout = 300000;

    private final WorkerPluginFactory workerPluginFactory = new WorkerPluginFactory();

    private String jobControllerHessianUrl;

    private final WorkerSpec workerSpec;

    private long minFreeSpaceMb = 10 * 1024;

    public Worker(WorkerSpec workerSpec) {
        this.workerSpec = workerSpec;
    }

    public void setMinFreeSpaceMb(long minFreeSpaceMb) {
        this.minFreeSpaceMb = minFreeSpaceMb;
    }

    public void setTransfer(Transfer transfer) {
        this.transfer = transfer;
    }

    /**
     * How long to wait for a job at server side
     *
     * @param requestTimeout -
     */
    public void setRequestTimeout(long requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    private ToolFinder toolFinder;

    public void setToolFinder(ToolFinder toolFinder) {
        this.toolFinder = toolFinder;
    }

    private void connectController() throws MalformedURLException, ClassNotFoundException {
        if (controller == null) {
            jobControllerHessianUrl = toolFinder.getControllerUrl();
            LOGGER.info("Connecting " + jobControllerHessianUrl);
            final HessianProxyFactory factory = new HessianProxyFactory();
            factory.setConnectTimeout(20000);
            factory.setReadTimeout(requestTimeout * 2);
            controller = (JobController) factory.create(this.jobControllerHessianUrl);
            LOGGER.fine("connected.");
        }
    }

    private WorkerJobParams acceptOrRejectJob(WorkerJob workerJob) {
        try {
            workerPluginFactory.checkCommand(workerJob.getCommandImpl());
            return controller.jobAccepted(workerSpec.getName(), workerJob.getExecutionId());
        } catch (Exception e) {
            controller.reportWorkDone(workerSpec.getName(), workerJob.getExecutionId(), WorkerResultCode.REJECTED, e.getMessage());
        }
        return null;
    }

    private void workerReport(String message) {
        try {
            controller.reportJobProgress(null, "worker." + workerSpec.getName(), message);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "failed to send workerReport: " + message);
        }
    }

    private WorkerJob requestJob() throws IOException, ClassNotFoundException {
        connectController();
        final File workDir = toolFinder.getJobsDir();
        final long freeSpaceKb = FileSystemUtils.freeSpaceKb(workDir.getAbsolutePath());
        final long freeSpaceMb = freeSpaceKb / 1024;
        if (freeSpaceMb < minFreeSpaceMb) {
            final String dsi = WorkerUtils.diskspaceInfo(workDir);
            LOGGER.severe(String.format("Disk space exhausted on %s (%s)", workDir, dsi));
            workerReport("Removing garbage, diskspace is " + dsi);
            final int removedFiles = toolFinder.getFgc().garbageCollect(requestTimeout);
            if (removedFiles == 0) {
                workerReport("Seeking for more garbage, diskspace is " + dsi);
                try {
                    Thread.sleep(requestTimeout);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            return null;
        } else {
            LOGGER.info(workerSpec.getName() + ": requesting job from " + jobControllerHessianUrl);
            WorkerJob workToDo = null;
            try {
                workToDo = controller.getWorkToDo(workerSpec, requestTimeout);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Problem while trying to reserve a job", e);
                controller = null;
            }
            if (workToDo == null) {
                final int cnt = toolFinder.getFgc().garbageCollect(requestTimeout / 10);
                if (cnt > 0) {
                    LOGGER.finer("Removed " + cnt + " garbage entries");
                }
            }
            return workToDo;
        }
    }

    public Integer call() throws InterruptedException, IOException, ClassNotFoundException {
        final String javaClassPath = System.getProperty("java.class.path");
        LOGGER.finest("java.class.path = " + javaClassPath);
        final File jarFile = new File(javaClassPath).getAbsoluteFile();
        if (!jarFile.exists()) {
            LOGGER.severe(String.format("File does not exist: '%s'. Make sure that classpath has only one element. Exiting...", jarFile));
            return 1;
        }
        final long bootJarTimestamp = jarFile.lastModified();
        LOGGER.finest(String.format("boot timestamp = %tFT%<tT.%<tL", bootJarTimestamp));
        while (!Thread.interrupted()) {
            final long currentJarTimestamp = jarFile.lastModified();
            if (currentJarTimestamp != bootJarTimestamp) {
                LOGGER.warning(String.format("! TIMESTAMP CHANGED ON %s FROM %tFT%<tT.%<tL TO %tFT%<tT.%<tL", jarFile, bootJarTimestamp, currentJarTimestamp));
                LOGGER.warning("! Timestamp change is considered a request to restart the application after update.");
                LOGGER.warning("! Shutting down now, expecting soon restart by launcher.");
                return 0;
            }
            final WorkerJob workerJob = requestJob();
            final File markerFile = new File(toolFinder.getJobsDir(), "worker." + workerSpec.getName() + ".marker");
            if (markerFile.exists()) {
                final String abandonedSandbox = FileUtils.fileRead(markerFile);
                markerFile.delete();
                LOGGER.warning("Garbaging abandoned sandbox: " + abandonedSandbox);
                toolFinder.getFgc().garbage(new File(abandonedSandbox));
            }
            if (workerJob == null) {
                Thread.sleep(15000);
                continue;
            }
            LOGGER.info("received: " + workerJob);
            final WorkerJobParams workerJobParams = acceptOrRejectJob(workerJob);
            if (workerJobParams == null) continue;
            final String sandboxUri = ExecutionContext.sandboxUri(workerJob.getJobId(), workerJob.getExecutionId());
            final String projectCacheUri = ExecutionContext.projectCacheUri(workerJob.getJobId());
            final ExecutionContext ec = new ExecutionContext(toolFinder.getJobsDir(), sandboxUri, projectCacheUri);
            final FileUploader fileUploader = new FileUploader(controller, transfer, ec);
            fileUploader.setWorkerJob(workerJob);
            FileUtils.fileWrite(markerFile, ec.getSandbox().getAbsolutePath());
            final AcceptedJobRunner acceptedJobRunner;
            try {
                acceptedJobRunner = initJobRunner(workerJob, workerJobParams, ec);
                acceptedJobRunner.initialReports(new File(ec.getLogs(), "inputs"));
            } catch (Exception e) {
                controller.reportWorkDone(workerSpec.getName(), workerJob.getExecutionId(), WorkerResultCode.IOERROR, "Initialization error - " + e.getClass() + ": " + e.getMessage());
                continue;
            }
            LOGGER.info("executing  " + workerJob.getCommandImpl());
            final WorkerResultCode workerResultCode = acceptedJobRunner.call();
            switch(workerResultCode) {
                case INTERRUPTED:
                    break;
                case IOERROR:
                    break;
                default:
                    try {
                        fileUploader.uploadAll(acceptedJobRunner.getDownloadedFiles());
                        fileUploader.reportOutputArtifacts(workerJobParams.getRequiredOutputPaths(), workerResultCode == WorkerResultCode.OK);
                    } catch (Exception e) {
                        LOGGER.severe("upload failed - " + e.getClass().getName() + ": " + e.getMessage());
                        controller.reportWorkDone(workerSpec.getName(), workerJob.getExecutionId(), WorkerResultCode.IOERROR, "upload failed, job execution result is: " + workerResultCode + ": " + acceptedJobRunner.getFinalMessage());
                    }
            }
            controller.reportWorkDone(workerSpec.getName(), workerJob.getExecutionId(), workerResultCode, acceptedJobRunner.getFinalMessage());
            final File sandbox = acceptedJobRunner.getSandbox();
            LOGGER.warning("Garbaging sandbox after upload: " + sandbox);
            toolFinder.getFgc().garbage(sandbox);
            markerFile.delete();
        }
        LOGGER.warning("interrupted");
        return 1;
    }

    private AcceptedJobRunner initJobRunner(WorkerJob workerJob, WorkerJobParams workerJobParams, ExecutionContext ec) throws IOException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        final File sandbox = ec.getSandbox();
        if (sandbox.exists()) {
            LOGGER.warning(sandbox + " already exists - removing!");
            FileUtils.deleteDirectory(sandbox);
        }
        final WorkerFeedback controllerFeedback = new FeedbackToServer(controller, workerJob.getExecutionId(), sandbox);
        final String sandboxUri = sandbox.getAbsolutePath().substring(toolFinder.getJobsDir().getAbsolutePath().length() + 1);
        final String sandboxUrl = toolFinder.getMyPublicUrlBase() + "/" + sandboxUri;
        controllerFeedback.setStatusProperty(JobNode.STATUSPROPERTY_SANDBOX_URL, sandboxUrl);
        final String welcomeMsg = String.format("Buildbox Worker %s (built: %s)", toolFinder.getMyVersion(), toolFinder.getMyBuildTime());
        final FeedbackToFile workerFeedback = new FeedbackToFile(sandbox, controllerFeedback, welcomeMsg);
        workerFeedback.console(nodeInfo());
        workerFeedback.console("Running: since %tFT%<tT.%<tL", bootTime);
        workerFeedback.console("Command: %s", workerJob);
        workerFeedback.addReplacement(Pattern.compile(Pattern.quote(ec.getSandbox().getAbsolutePath())), "/BUILDBOX/sandbox");
        workerFeedback.addReplacement(Pattern.compile(Pattern.quote(ec.getProjectCache().getAbsolutePath())), "/BUILDBOX/cache");
        final AcceptedJobRunner acceptedJobRunner = new AcceptedJobRunner();
        acceptedJobRunner.setWorkerPluginFactory(workerPluginFactory);
        acceptedJobRunner.setWorkerFeedback(workerFeedback);
        final FileDownloader fileDownloader = new FileDownloader(workerFeedback, transfer, ec);
        acceptedJobRunner.setFileDownloader(fileDownloader);
        acceptedJobRunner.setupJob(workerJob, workerJobParams, ec, toolFinder.getLocalProperties());
        return acceptedJobRunner;
    }

    private String nodeInfo() {
        final Map<String, String> hello = new LinkedHashMap<String, String>();
        hello.put("user.name", System.getProperty("user.name"));
        hello.put("os.name", System.getProperty("os.name"));
        hello.put("os.arch", System.getProperty("os.arch"));
        return String.format("Node:  %s %s", workerSpec.getName(), hello);
    }
}
