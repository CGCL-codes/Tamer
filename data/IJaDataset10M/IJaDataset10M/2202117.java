package com.wozgonon.eventstore;

import java.util.EventListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import com.wozgonon.Logger;
import com.wozgonon.docustrate.DesignPattern;
import com.wozgonon.eventstore.BaseData.PlugInMemento;
import com.wozgonon.math.IHasInvariant;
import com.wozgonon.time.SamplingPeriodClock;
import com.wozgonon.time.TimeSeriesType;
import com.wozgonon.usecase.IActorUseCase;
import com.wozgonon.usecase.IUseCase;
import com.wozgonon.usecase.IUseCaseRun;
import com.wozgonon.usecase.Interaction;
import com.wozgonon.usecase.UseCaseEventsLogger;

@DesignPattern(url = "http://wiki.hsr.ch/PeterSommerlad/files/manager-.pdf", usage = "Responsible for the life time of all use case monitoring objects.")
public class UseCaseManager implements IHasInvariant {

    private static final UseCaseManager s_this = new UseCaseManager();

    BaseData baseData = new BaseData();

    private final PluginExecutor executor = new PluginExecutor(this);

    private final UseCaseExtracts extracts = new UseCaseExtracts(this);

    private final SaveLoad saveLoad = new SaveLoad(this);

    final SamplingPeriodClock clock;

    private final Set<ChangeListener> changeListeners = new HashSet<ChangeListener>();

    final HashMap<ActorUseCase, Counter> counters = new HashMap<ActorUseCase, Counter>();

    public UseCaseManager() {
        this(new SamplingPeriodClock());
    }

    public UseCaseManager(SamplingPeriodClock clock) {
        assert clock != null;
        this.clock = clock;
        assertClassInvariant();
    }

    public void assertClassInvariant() {
        this.baseData.assertClassInvariant();
        assert this.baseData.numberOfCounters() == counters.size() : this.baseData.numberOfCounters() + " : " + counters.size();
        for (Counter counter : counters.values()) {
            assert counter != null;
            assert baseData.rows[counter.row] == counter;
        }
    }

    public void sniffTest() {
        if (baseData.numberOfCounters <= 3 || baseData.numberOfCounters % 27 == 0) {
            assertClassInvariant();
        }
    }

    private void recreateTransients() {
        for (PlugInMemento memento : this.baseData.getPlugIns()) {
            try {
                memento.createTransientEventSource(this.executor);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        counters.clear();
        baseData.rows = new IActorUseCase[baseData.rowsMementos.length];
        for (short xx = 0; xx < baseData.numberOfCounters; ++xx) {
            final ActorUseCase counter = baseData.rowsMementos[xx];
            final IUseCase usecase = this.getUsecase(counter.getInteraction(), counter.getName());
            final Counter result = new Counter(this, usecase, counter.getUser(), xx);
            assert result.row == xx;
            baseData.rows[xx] = result;
            this.counters.put(counter, result);
        }
        assertClassInvariant();
    }

    public void setBaseData(BaseData baseData) {
        synchronized (this) {
            this.baseData = baseData;
            recreateTransients();
        }
        fireChangeEvent(ChangeEvent.BASE_DATA_CHANGED);
    }

    public static synchronized UseCaseManager current() {
        return s_this;
    }

    public SaveLoad getSaveLoad() {
        return saveLoad;
    }

    public synchronized BaseData getBaseData() {
        return baseData;
    }

    public synchronized PluginExecutor getExecutor() {
        return executor;
    }

    public UseCaseExtracts getExtracts() {
        return extracts;
    }

    public SamplingPeriodClock getClock() {
        return this.clock;
    }

    public short currentPeriod() {
        final short last = this.clock.currentPeriod(this.baseData.timestamp);
        final short next = this.clock.currentPeriod();
        if (last > next) {
            extracts.copy(this.baseData.getLog(TimeSeriesType.WEEK), this.baseData.getToday(), last, next);
        }
        this.baseData.timestamp = this.clock.currentTimeMillis();
        return next;
    }

    void fireChangeEvent(ChangeEvent event) {
        for (ChangeListener listener : this.changeListeners) {
            listener.usecaseManagerChanged(event);
        }
    }

    public static enum ChangeEvent {

        BASE_DATA_CHANGED, PLUGIN_ADDED_OR_REMOVED
    }

    public static interface ChangeListener extends EventListener {

        void usecaseManagerChanged(ChangeEvent event);
    }

    public void addChangeListener(final ChangeListener listener) {
        this.changeListeners.add(listener);
    }

    public void removeChangeListener(final ChangeListener listener) {
        this.changeListeners.remove(listener);
    }

    IUseCaseRun get(IUseCase usecase, String actor) {
        final ActorUseCase key = new ActorUseCase(usecase.getInteraction(), usecase.getName(), actor);
        synchronized (this) {
            Counter result = counters.get(key);
            if (result == null) {
                if (baseData.numberOfCounters >= baseData.rows.length) {
                    Logger.log.warning("Run out of counters [" + baseData.numberOfCounters + "]");
                    return defaultCounter(usecase);
                }
                result = new Counter(this, usecase, actor, baseData.numberOfCounters);
                baseData.rows[baseData.numberOfCounters] = result;
                baseData.rowsMementos[baseData.numberOfCounters] = key;
                counters.put(key, result);
                assert counters.get(key) == result;
                ++baseData.numberOfCounters;
                assert counters.size() == baseData.numberOfCounters : counters.size() + " : " + baseData.numberOfCounters;
            }
            sniffTest();
            assert result != null;
            return result;
        }
    }

    public static IUseCase usecase(final Interaction interaction, final String name) {
        return current().getUsecase(interaction, name);
    }

    /**
	 * Find or create a use case.
	 */
    public IUseCase getUsecase(final Interaction interaction, final String name) {
        return new IUseCase() {

            public Interaction getInteraction() {
                return interaction;
            }

            public String getName() {
                return name.intern();
            }

            public IUseCaseRun newRun(String actor) {
                IUseCaseRun aa = UseCaseManager.this.get(this, actor.intern());
                return aa;
            }
        };
    }

    /**
	 * @param usecase the use case for logging
	 * @param user    the user (actor) who invoked the use case
	 * @param level   the logging level
	 * @return
	 */
    public static synchronized IUseCase usecaseWithEventLogging(final Interaction interaction, final String name, final Level level) {
        final IUseCase usecase = usecase(interaction, name);
        return new IUseCase() {

            @Override
            public Interaction getInteraction() {
                return usecase.getInteraction();
            }

            @Override
            public String getName() {
                return usecase.getName();
            }

            @Override
            public IUseCaseRun newRun(String actor) {
                final IUseCaseRun next = usecase.newRun(actor);
                final IUseCaseRun logger = new UseCaseEventsLogger.UseCaseEventLogger(next, level);
                return logger;
            }
        };
    }

    private static final IUseCaseRun defaultCounter(final IUseCase usecase) {
        Logger.log.warning("Using default logger");
        return new IUseCaseRun() {

            @Override
            public void addEvent(Object event) {
            }

            @Override
            public void addSample(Sample sample) {
            }

            @Override
            public void begin() {
            }

            @Override
            public void end(int size) {
            }

            @Override
            public void exception(Exception exception) {
            }

            @Override
            public IUseCase getUseCase() {
                return usecase;
            }
        };
    }
}
