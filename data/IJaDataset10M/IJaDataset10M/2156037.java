package net.sourceforge.quexec.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import net.sourceforge.quexec.main.OSCommandExecutor;
import net.sourceforge.quexec.proc.AsyncProcessHandle;
import net.sourceforge.quexec.proc.LocalProcessController;
import net.sourceforge.quexec.proc.ProcessExecutor;
import net.sourceforge.quexec.proc.ProcessResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ProcessExecutorServiceBean implements ProcessExecutorService, ApplicationContextAware {

    private static final Log log = LogFactory.getLog(ProcessExecutorServiceBean.class);

    private ApplicationContext ctx;

    private long defaultTimeout = -1;

    private long launcherTimeout = -1;

    private JmsQueueGenerator queueGenerator = new CountingJmsQueueGenerator("dynamicQueues/quexec");

    public long getDefaultTimeout() {
        return defaultTimeout;
    }

    public void setDefaultTimeout(long defaulTimeout) {
        this.defaultTimeout = defaulTimeout;
    }

    public long getLauncherTimeout() {
        return launcherTimeout;
    }

    public void setLauncherTimeout(long launcherTimeout) {
        this.launcherTimeout = launcherTimeout;
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        this.ctx = ctx;
    }

    @PostConstruct
    public void init() {
        log.info("process executor service initialized: " + "launcherTimeout=" + launcherTimeout + ", defaultTimeout=" + defaultTimeout);
    }

    @PreDestroy
    public void destroy() {
        log.info("process executor service terminated");
    }

    @Override
    public ProcessResult runSync(List<String> cmd) throws NativeExecutionException {
        return runSyncImpl(cmd, false, this.defaultTimeout);
    }

    @Override
    public ProcessResult runSync(List<String> cmd, long timeout) throws NativeExecutionException {
        return runSyncImpl(cmd, false, timeout);
    }

    @Override
    public ProcessResult runSyncJava(List<String> cmd) throws NativeExecutionException {
        return runSyncImpl(cmd, true, this.defaultTimeout);
    }

    @Override
    public ProcessResult runSyncJava(List<String> cmd, long timeout) throws NativeExecutionException {
        return runSyncImpl(cmd, true, timeout);
    }

    @Override
    public AsyncProcessHandle runAsync(List<String> cmd) throws NativeExecutionException {
        return runAsyncImpl(cmd, false, this.launcherTimeout, this.defaultTimeout);
    }

    @Override
    public AsyncProcessHandle runAsync(List<String> cmd, long timeout) throws NativeExecutionException {
        return runAsyncImpl(cmd, false, this.launcherTimeout, timeout);
    }

    @Override
    public AsyncProcessHandle runAsyncJava(List<String> cmd) throws NativeExecutionException {
        return runAsyncImpl(cmd, true, this.launcherTimeout, this.defaultTimeout);
    }

    @Override
    public AsyncProcessHandle runAsyncJava(List<String> cmd, long timeout) throws NativeExecutionException {
        return runAsyncImpl(cmd, true, this.launcherTimeout, timeout);
    }

    private ProcessResult runSyncImpl(List<String> cmd, boolean isJavaRun, long timeout) throws NativeExecutionException {
        log.debug("run synchronously: isJavaRun=" + isJavaRun + ", timeout=" + timeout + ", cmd=" + cmd);
        LocalProcessController procCtrl = (LocalProcessController) ctx.getBean("quexec:localSyncController");
        ProcessExecutor pexec = procCtrl.getProcessExecutor();
        try {
            if (isJavaRun) {
                pexec.execJava(cmd);
            } else {
                pexec.exec(cmd);
            }
        } catch (IOException e) {
            throw new NativeExecutionException("IOException while starting native process: ", e);
        }
        int returnValue;
        try {
            returnValue = pexec.waitFor(timeout);
        } catch (TimeoutException e) {
            throw new NativeExecutionException("timeout occurred: ", e);
        } catch (InterruptedException e) {
            throw new NativeExecutionException("unexpected interrupt occurred: " + e.getMessage());
        }
        String procOut = procCtrl.collectProcessOutput();
        return new ProcessResult(returnValue, procOut);
    }

    private AsyncProcessHandle runAsyncImpl(List<String> cmd, boolean isJavaRun, long launcherTimeout, long timeout) throws NativeExecutionException {
        log.info("run asynchronous subprocess: isJavaRun=" + isJavaRun + ", launcherTimeout=" + launcherTimeout + ", timeout=" + defaultTimeout + ", cmd=" + cmd);
        List<String> executorCmd = new ArrayList<String>(cmd.size() + 2);
        executorCmd.add("-Dquexec.tag=Executor");
        executorCmd.add(OSCommandExecutor.class.getName());
        executorCmd.add("-r");
        if (isJavaRun) {
            executorCmd.add("-j");
        }
        if (timeout > 0) {
            executorCmd.add("-t");
            executorCmd.add(String.valueOf(timeout));
        }
        String procInQueueName = queueGenerator.generateJmsQueue();
        String procOutQueueName = queueGenerator.generateJmsQueue();
        executorCmd.add("-i");
        executorCmd.add(procInQueueName);
        executorCmd.add("-o");
        executorCmd.add(procOutQueueName);
        executorCmd.add("--");
        executorCmd.addAll(cmd);
        LocalProcessController procCtrl = (LocalProcessController) ctx.getBean("quexec:localSyncController");
        ProcessExecutor pexec = procCtrl.getProcessExecutor();
        try {
            pexec.execJava(executorCmd);
        } catch (IOException e) {
            throw new NativeExecutionException("IOException while starting native process", e);
        }
        log.info("command started: procInputQueue='" + procInQueueName + "', procOutputQueue='" + procOutQueueName + "'");
        return new AsyncProcessHandle(procInQueueName, procOutQueueName);
    }
}
