package org.jactr.core.runtime.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.commonreality.executor.GeneralThreadFactory;
import org.jactr.core.concurrent.ExecutorServices;
import org.jactr.core.model.IModel;
import org.jactr.core.runtime.ACTRRuntime;
import org.jactr.core.runtime.DefaultModelRunner;
import org.jactr.core.runtime.controller.impl.ModelListener;
import org.jactr.core.runtime.controller.impl.RuntimeListener;
import org.jactr.core.runtime.controller.impl.RuntimeState;
import org.jactr.core.runtime.event.ACTRRuntimeEvent;
import org.jactr.core.runtime.profile.ProfilingModelRunner;

public class DefaultController implements IController {

    /**
   * Logger definition
   */
    private static final transient Log LOGGER = LogFactory.getLog(DefaultController.class);

    private final RuntimeState _state;

    private final RuntimeListener _runtimeListener;

    private final ModelListener _modelListener;

    protected final Lock _lock = new ReentrantLock();

    private final Set<Thread> _activeThreads = new HashSet<Thread>();

    private final Set<ExecutorService> _executors = new HashSet<ExecutorService>();

    public DefaultController() {
        _state = new RuntimeState();
        _runtimeListener = new RuntimeListener(_state) {

            @Override
            public void modelAdded(ACTRRuntimeEvent event) {
                super.modelAdded(event);
                if (isRunning()) {
                    if (LOGGER.isDebugEnabled()) LOGGER.debug(String.format("adding %s to a running runtime", event.getModel()));
                    startModel(event.getModel());
                }
            }

            @Override
            public void modelRemoved(ACTRRuntimeEvent event) {
                super.modelRemoved(event);
            }
        };
        _modelListener = new ModelListener(_state);
    }

    public void attach() {
        ACTRRuntime.getRuntime().addListener(_runtimeListener, ExecutorServices.INLINE_EXECUTOR);
    }

    public void detach() {
        ACTRRuntime.getRuntime().removeListener(_runtimeListener);
    }

    public Collection<IModel> getRunningModels() {
        ArrayList<IModel> models = new ArrayList<IModel>();
        _state.getRunning(models);
        return models;
    }

    public Collection<IModel> getSuspendedModels() {
        ArrayList<IModel> models = new ArrayList<IModel>();
        _state.getSuspended(models);
        return models;
    }

    public Collection<IModel> getTerminatedModels() {
        ArrayList<IModel> models = new ArrayList<IModel>();
        _state.getTerminated(models);
        return models;
    }

    public boolean isRunning() {
        return _state.isRunning();
    }

    public boolean isSuspended() {
        return _state.isSuspended();
    }

    public Future<Boolean> start() {
        return start(false);
    }

    public Future<Boolean> start(boolean suspendImmediately) {
        if (_state.isRunning()) return _runtimeListener.getStartFuture();
        ArrayList<IModel> models = new ArrayList<IModel>(ACTRRuntime.getRuntime().getModels());
        if (models.size() == 0 && LOGGER.isWarnEnabled()) LOGGER.warn("No models to execute");
        try {
            _lock.lock();
            _state.clear();
            _modelListener.setShouldSuspend(suspendImmediately);
        } finally {
            _lock.unlock();
        }
        _state.starting();
        for (IModel model : models) startModel(model);
        _state.started();
        return _runtimeListener.getStartFuture();
    }

    protected void startModel(final IModel model) {
        final ExecutorService service = createExecutorService(model);
        final Runnable runner = createModelRunnable(model, service);
        Runnable actual = new Runnable() {

            public void run() {
                try {
                    _lock.lock();
                    _activeThreads.add(Thread.currentThread());
                } finally {
                    _lock.unlock();
                }
                _state.starting(model);
                try {
                    model.addListener(_modelListener, ExecutorServices.INLINE_EXECUTOR);
                    runner.run();
                } finally {
                    try {
                        _lock.lock();
                        _activeThreads.remove(Thread.currentThread());
                    } finally {
                        _lock.unlock();
                    }
                    model.removeListener(_modelListener);
                    _state.stopped(model);
                    destroyModelRunnable(model, runner);
                    destroyExecutorService(model, service);
                }
            }
        };
        service.execute(actual);
    }

    public Future<Boolean> complete() {
        return _runtimeListener.getStopFuture();
    }

    public Future<Boolean> stop() {
        if (!_state.isRunning()) return _runtimeListener.getStopFuture();
        try {
            _lock.lock();
            for (ExecutorService service : _executors) service.shutdown();
        } finally {
            _lock.unlock();
        }
        _modelListener.setShouldSuspend(false);
        return _runtimeListener.getStopFuture();
    }

    /**
   * provides access to suspension mechanism. this is here so that extenders can
   * use the suspend mechanism in the debug controller
   */
    protected void suspendLocally(IModel model) {
        _modelListener.suspendModel(model);
    }

    public Future<Boolean> suspend() {
        if (_state.isRunning() && !_state.isSuspended()) _modelListener.setShouldSuspend(true);
        return _runtimeListener.getSuspendFuture();
    }

    public Future<Boolean> resume() {
        if (_state.isRunning() && _state.isSuspended()) _modelListener.setShouldSuspend(false);
        return _runtimeListener.getResumeFuture();
    }

    public Future<Boolean> terminate() {
        stop();
        try {
            _lock.lock();
            for (ExecutorService service : _executors) service.shutdownNow();
            for (Thread thread : _activeThreads) thread.interrupt();
        } finally {
            _activeThreads.clear();
            _executors.clear();
            _lock.unlock();
        }
        return _runtimeListener.getStopFuture();
    }

    protected Runnable createModelRunnable(IModel model, ExecutorService service) {
        if (Boolean.getBoolean("jactr.profiling")) return new ProfilingModelRunner(service, model, model.getCycleProcessor());
        return new DefaultModelRunner(service, model, model.getCycleProcessor());
    }

    protected void destroyModelRunnable(IModel model, Runnable runnable) {
        if (runnable instanceof ProfilingModelRunner) {
            ProfilingModelRunner prm = (ProfilingModelRunner) runnable;
            StringBuilder sb = new StringBuilder("Profile Stats for ");
            sb.append(model.getName()).append("\n");
            sb.append(" Total actual processing cycles \t").append(prm.getTotalCycles()).append("\n");
            sb.append(" Simulated processing cycles \t\t").append(model.getCycle()).append("\n");
            sb.append(" Total actual time \t\t\t").append(prm.getTotalCycleTime()).append("s\n");
            sb.append(" Simulate time \t\t\t\t").append(prm.getSimulatedTime()).append("s\n");
            sb.append(" Average sleep time (wait for clock) \t").append(prm.getActualWaitTime() / prm.getTotalCycles() * 1000d).append("ms\n");
            sb.append(" Average time processing events \t").append(prm.getActualEventTime() / prm.getTotalCycles() * 1000d).append("ms\n");
            sb.append(" Average production cycle time \t\t").append(prm.getActualCycleTime() / prm.getTotalCycles() * 1000d).append("ms\n");
            sb.append(" Average production time + waits \t").append(prm.getTotalCycleTime() / prm.getTotalCycles() * 1000d).append("ms\n");
            sb.append(" Realtime factor \t\t\t").append(prm.getRealTimeFactor()).append(" X \n");
            System.out.println(sb.toString());
            System.out.flush();
        }
    }

    private ExecutorService createExecutorService(IModel model) {
        ExecutorService service = Executors.newSingleThreadExecutor(new GeneralThreadFactory(model.getName()));
        _executors.add(service);
        ExecutorServices.addExecutor(model.getName(), service);
        return service;
    }

    /**
   * do not wait or trigger an immediate shutdown of the executor as it make
   * sure you call super if overriding
   * 
   * @param model
   * @param service
   */
    private void destroyExecutorService(IModel model, ExecutorService service) {
        if (!service.isShutdown()) service.shutdown();
        if (service instanceof ThreadPoolExecutor) {
            ThreadFactory factory = ((ThreadPoolExecutor) service).getThreadFactory();
            if (factory instanceof GeneralThreadFactory) ((GeneralThreadFactory) factory).dispose();
        }
        _executors.remove(service);
        ExecutorServices.removeExecutor(model.getName());
    }

    public Future<Boolean> waitForCompletion() {
        return _runtimeListener.getStopFuture();
    }

    public Future<Boolean> waitForResumption() {
        return _runtimeListener.getResumeFuture();
    }

    public Future<Boolean> waitForStart() {
        return _runtimeListener.getStartFuture();
    }

    public Future<Boolean> waitForSuspension() {
        return _runtimeListener.getSuspendFuture();
    }
}
