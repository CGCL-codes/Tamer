package com.griddynamics.convergence.fabric.remote;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import com.griddynamics.convergence.demo.dar.service.GridScheduler;
import com.griddynamics.convergence.demo.utils.cluster.GridHostMap;
import com.griddynamics.convergence.demo.utils.exec.ExecCommand;
import com.griddynamics.convergence.demo.utils.exec.GridProcessScheduler;
import com.griddynamics.convergence.demo.utils.exec.ProcessExecutor;

public class ProcessGridSchedulerAdaptor implements GridScheduler {

    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();

    private static final AtomicInteger TASK_COUNTER = new AtomicInteger();

    private GridProcessScheduler scheduler;

    private ExecCommand javaCmd;

    private GridHostMap hostMap;

    public ProcessGridSchedulerAdaptor(GridProcessScheduler scheduler, ExecCommand javaCmd) {
        this.scheduler = scheduler;
        this.javaCmd = javaCmd;
    }

    public void setHostMap(GridHostMap hostMap) {
        this.hostMap = hostMap;
    }

    public <T> Future<T> submit(final String host, final Callable<T> task) {
        final int taskId = TASK_COUNTER.getAndIncrement();
        try {
            Callable<T> wrapper = new Callable<T>() {

                public T call() throws Exception {
                    String targetHost = host;
                    if (hostMap != null && host != null) {
                        targetHost = hostMap.normalizeHost(targetHost);
                    }
                    ExecutorService service = null;
                    try {
                        ProcessExecutor pexec = scheduler.getAllignedExecutor(targetHost);
                        service = RemoteJvmHelper.createRemoteExecutor(pexec, "[" + targetHost + ":task#" + taskId + "] ", javaCmd);
                        T result = service.submit(task).get();
                        return result;
                    } finally {
                        try {
                            if (service != null) {
                                service.shutdown();
                            }
                        } catch (Exception e) {
                        }
                    }
                }
            };
            return EXECUTOR.submit(wrapper);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
