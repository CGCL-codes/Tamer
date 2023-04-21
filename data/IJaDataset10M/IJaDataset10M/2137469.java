package engine.executives;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import engine.ArchitectureWithEntity;
import engine.BehavioralProcess;
import engine.SignalPrimitive;
import engine.errors.CompilationError;

public abstract class AbstractExecutor implements Executor {

    protected static final Executor c = ExecutorAccessor.getExecutor();

    private Collection<SignalPrimitive> signalPrimitives;

    private List<BehavioralProcess> processes;

    private List<ArchitectureWithEntity> architectures;

    private SimulationManager manager;

    private boolean unstable;

    protected AbstractExecutor() {
        signalPrimitives = new ArrayList<SignalPrimitive>();
        architectures = new ArrayList<ArchitectureWithEntity>();
        processes = new ArrayList<BehavioralProcess>();
        manager = new SimulationManager();
    }

    @Override
    public void fireTiming() {
        for (SignalPrimitive s : signalPrimitives) {
            s.resetLastController();
        }
        for (BehavioralProcess p : processes) {
            p.newTimingPeriod();
        }
        boolean fired = true;
        while (fired) {
            unstable = true;
            int tries = 0;
            while (unstable) {
                ++tries;
                if (tries == getManager().getMaxTries()) {
                    throw new CompilationError("Circuit unstable (at lease one signal dependence loop).");
                }
                unstable = false;
                for (ArchitectureWithEntity architecture : architectures) {
                    architecture.apply();
                }
                for (SignalPrimitive s : signalPrimitives) {
                    s.resetExecutorController();
                }
            }
            fired = fireAnyProcess();
        }
    }

    protected void setManager(SimulationManager manager) {
        this.manager = manager;
    }

    protected SimulationManager getManager() {
        return manager;
    }

    private boolean fireAnyProcess() {
        for (BehavioralProcess p : processes) {
            if (p.isTriggered()) {
                if (!p.wasTimingFired()) {
                    p.fireTiming();
                    return true;
                } else {
                    throw new CompilationError("Trying to trigger a process twice in one timing period. (Probably unstable clock signal)");
                }
            }
        }
        return false;
    }

    @Override
    public void justChanged(SignalPrimitive signal) {
        unstable = true;
    }

    @Override
    public boolean isProcess() {
        return false;
    }

    public void registerArchitecture(ArchitectureWithEntity architecture) {
        architectures.add(architecture);
    }

    public void registerProcess(BehavioralProcess process) {
        processes.add(process);
    }

    public void registerSignalPrimitive(SignalPrimitive signalPrimitive) {
        signalPrimitives.add(signalPrimitive);
    }

    public void runForever() {
        setUpTopLevel();
        while (true) {
            nextFullCycle();
        }
    }

    public abstract void setUpTopLevel();

    public void nextFullCycle() {
        getManager().performPreEIO();
        try {
            fireExternalIO();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        getManager().performPostEIO();
        getManager().performPreTiming();
        fireTiming();
        getManager().performPostTiming();
        getManager().performPreCallbacks();
        fireChangeCallbacks();
        getManager().performPostCallbacks();
    }

    public void fireChangeCallbacks() {
        for (SignalPrimitive sp : signalPrimitives) {
            sp.changeCallback();
        }
    }

    public abstract void fireExternalIO() throws IOException;
}
