package net.sf.buildbox.worker.impl;

import net.sf.buildbox.reactor.api.JobController;
import net.sf.buildbox.reactor.model.AreaType;
import net.sf.buildbox.reactor.model.JobDependency;
import net.sf.buildbox.reactor.model.JobId;
import net.sf.buildbox.reactor.model.WorkerJob;
import net.sf.buildbox.util.BbxSystemUtils;
import net.sf.buildbox.util.SlowTask;
import net.sf.buildbox.worker.api.ExecutionContext;
import net.sf.buildbox.worker.api.Transfer;
import net.sf.buildbox.worker.api.TransferProgress;
import net.sf.buildbox.worker.util.WorkerUtils;
import org.codehaus.plexus.util.FileUtils;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileUploader {

    private static final Logger LOGGER = Logger.getLogger(FileUploader.class.getName());

    private final Transfer transfer;

    private final ExecutionContext ec;

    private JobId jobId;

    private long executionId;

    private int totalDirs = 0;

    private int totalFiles = 0;

    private int totalBytes = 0;

    private final JobController controller;

    public FileUploader(JobController controller, Transfer transfer, ExecutionContext ec) {
        this.controller = controller;
        this.transfer = transfer;
        this.ec = ec;
    }

    public void setWorkerJob(WorkerJob workerJob) {
        this.jobId = workerJob.getJobId();
        this.executionId = workerJob.getExecutionId();
    }

    private void setStatusProperty(String key, String value) {
        try {
            controller.reportJobProgress(executionId, key, value);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, String.format("%d:: failed to set %s to %s", executionId, key, value), e);
        }
    }

    private void console(String format, Object... args) {
        LOGGER.fine(String.format(format, args));
    }

    public final void uploadAll(Set<File> retrievedFiles) throws Exception {
        final long startTime = System.currentTimeMillis();
        console("Uploader: cleaning %d retrieved files", retrievedFiles.size());
        for (File path : retrievedFiles) {
            BbxSystemUtils.deletePathAndItsEmptyParents(path);
        }
        for (final AreaType area : new AreaType[] { AreaType.SETTINGS, AreaType.REPOSITORY, AreaType.CODE, AreaType.LOGS }) {
            upload(area);
        }
        setStatusProperty("upload.status", String.format("uploaded %d dirs, %d files, %s in %s", totalDirs, totalFiles, BbxSystemUtils.byteCountToDisplaySize(totalBytes), WorkerUtils.durationMillisToDisplayTime(System.currentTimeMillis() - startTime)));
        final File workdir = ec.getSandbox().getParentFile().getParentFile();
        setStatusProperty("catalog.uri", ec.getSandbox().getAbsolutePath().substring(workdir.getAbsolutePath().length() + 1));
    }

    protected boolean skipAreaUpload(AreaType area) {
        return area == AreaType.CODE && !jobId.getTargetName().equals("fetch");
    }

    final Set<AreaType> alreadyUploaded = new HashSet<AreaType>();

    private void upload(final AreaType area) throws IOException, InterruptedException {
        if (alreadyUploaded.contains(area)) {
            console("skipping %s - already uploaded", area);
            return;
        }
        final File srcRootDir = ec.resourceBaseDir(area);
        if (!srcRootDir.exists()) {
            console("skipping %s - directory does not exist: %s", area, srcRootDir);
            return;
        }
        if (skipAreaUpload(area)) {
            console("skipping %s - ignored area", area);
            return;
        }
        final JobDependency jobDependency = JobDependency.create(jobId, area);
        jobDependency.setExecutionId(executionId);
        setStatusProperty("upload.status", "uploading " + jobDependency.getPath());
        boolean success = false;
        int retries = 4;
        while (retries > 0 && !success) {
            retries--;
            try {
                final TransferProgress progress = new TransferProgress();
                final Callable<Boolean> slowCallable = new Callable<Boolean>() {

                    public Boolean call() throws Exception {
                        transfer.store(srcRootDir, jobDependency, progress);
                        return true;
                    }
                };
                final SlowTask<Boolean> wrapper = new SlowTask<Boolean>("uploading " + area, 45000, 30000, slowCallable) {

                    @Override
                    protected void onStarting() {
                        LOGGER.info("uploading " + area);
                    }

                    @Override
                    public void stillWorking() {
                        setStatusProperty("upload.status", " ... still uploading " + area + " - done:" + FileUtils.byteCountToDisplaySize((int) progress.getTransferredBytes()));
                    }
                };
                wrapper.call();
                totalDirs += progress.getTransferredDirs();
                totalFiles += progress.getTransferredFiles();
                totalBytes += progress.getTransferredBytes();
                success = true;
                alreadyUploaded.add(area);
            } catch (IOException e) {
                e.printStackTrace();
                if (retries == 0) {
                    throw e;
                }
                final int millis = 60 * 1000;
                System.err.println(millis + " pause, the retrying...");
                Thread.sleep(millis);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
    }

    /**
     * This is temporary thing; it should be reported DURING job execution, to allow better UI feedback and faster dependency unlocking
     *
     * @param requiredOutputPaths -
     * @param executionSucceeded  -
     */
    public void reportOutputArtifacts(Set<String> requiredOutputPaths, boolean executionSucceeded) {
        final File sandbox = ec.getSandbox();
        for (String path : requiredOutputPaths) {
            final File file = new File(sandbox, path);
            final String relativePath = file.getAbsolutePath().substring(sandbox.getAbsolutePath().length() + 1);
            final String artifactPath;
            final Long size;
            if (path.endsWith("/")) {
                if (!executionSucceeded) {
                    continue;
                }
                final String[] list = file.list();
                artifactPath = relativePath + "/";
                size = list == null || !file.isDirectory() ? null : (long) list.length;
            } else {
                artifactPath = relativePath;
                size = file.isFile() ? file.length() : null;
            }
            LOGGER.warning(String.format("REPORT OUTPUT ARTIFACT %s = %d [length=%d for %s]", path, size, file.length(), file));
            controller.reportJobArtifact(executionId, artifactPath, size);
        }
    }
}
