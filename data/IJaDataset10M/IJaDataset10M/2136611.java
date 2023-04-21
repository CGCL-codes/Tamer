package repast.simphony.data.engine;

import repast.simphony.context.Context;
import repast.simphony.data.logging.LoggingFramework;
import repast.simphony.data.logging.LoggingInitializable;
import repast.simphony.data.logging.gather.*;
import repast.simphony.engine.controller.NullAbstractControllerAction;
import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.parameter.Parameters;
import simphony.util.messages.MessageCenter;

/**
 * An action that registers a {@link repast.simphony.data.logging.gather.DataGatherer} with the
 * logging framework and the run state's
 * {@link repast.simphony.data.logging.gather.LoggingRegistry}.
 * 
 * @author Jerry Vos
 */
public class DefaultDataGathererControllerAction<T> extends NullAbstractControllerAction<T> {

    @SuppressWarnings("unused")
    private static final MessageCenter LOG = MessageCenter.getMessageCenter(DefaultDataGathererControllerAction.class);

    protected DataGatherer<T> dataGatherer;

    protected ScheduleParameters gathererScheduleParameters;

    public DefaultDataGathererControllerAction() {
        super();
    }

    /**
	 * Loads the objects source into the data gatherer, initializes all the
	 * {@link DataMapping}s, and then registers the data gatherer.
	 * 
	 * @see #register(RunState, DataGatherer, ScheduleParameters)
	 * @see #initialize(DataMapping, RunState)
	 * 
	 * @param runState
	 *            the run state of the
	 * @param context
	 */
    @Override
    public void runInitialize(RunState runState, Context<? extends T> context, Parameters params) {
        getDataGatherer().setObjectsSource(getObjectsSource(context));
        for (DataMapping mapping : getDataGatherer().getDataMappings()) {
            initialize(mapping, runState);
        }
        for (AggregateDataMapping mapping : getDataGatherer().getPrimaryAggregateDataMappings()) {
            initialize(mapping, runState);
        }
        for (AggregateDataMapping mapping : getDataGatherer().getAlternatedAggregateMappings()) {
            initialize(mapping, runState);
        }
        LoggingFramework.register(runState, getDataGatherer(), getScheduleParameters());
    }

    /**
	 * Sets the schedule parameters for executing the data gatherer.
	 * 
	 * @param scheduleParameters
	 *            the schedule parameters for executing the data gatherer
	 */
    public void setScheduleParameters(ScheduleParameters scheduleParameters) {
        this.gathererScheduleParameters = scheduleParameters;
    }

    /**
	 * Retrieves the schedule parameters for executing the data gatherer.
	 * 
	 * @return the schedule parameters for executing the data gatherer
	 */
    public ScheduleParameters getScheduleParameters() {
        return gathererScheduleParameters;
    }

    /**
	 * Just returns the data gatherer's objects source. This is meant to be
	 * overridden.
	 * 
	 * @see DataGatherer#getObjectsSource()
	 * 
	 * @param context
	 *            the context which may be used to generate a more complicated
	 *            objects source
	 * @return the gatherer's objects source
	 */
    public DataObjectSource<T> getObjectsSource(Context<? extends T> context) {
        return dataGatherer.getObjectsSource();
    }

    /**
	 * Retrieves the DataGatherer that will be registered.
	 */
    public DataGatherer<T> getDataGatherer() {
        return dataGatherer;
    }

    /**
	 * Sets the DataGatherer that will be registered.
	 * 
	 * @param dataGatherer
	 *            the DataGather that will be registered
	 */
    public void setDataGatherer(DataGatherer<T> dataGatherer) {
        this.dataGatherer = dataGatherer;
    }

    /**
	 * Initializes the given DataMapping. If the DataMapping is a
	 * TimeDataMapping this set its schedule as the model schedule in the
	 * RunState's ScheduleRegistry. If the DataMapping is LoggingInitializable
	 * it will be initialized with the RunState's RunInfo.
	 * 
	 * @param mapping
	 * @param runState
	 */
    @SuppressWarnings("unchecked")
    public void initialize(DataMapping<?, ?> mapping, RunState runState) {
        if (mapping instanceof TimeDataMapping) {
            ((TimeDataMapping<?>) mapping).setSchedule(runState.getScheduleRegistry().getModelSchedule());
        }
        if (mapping instanceof LoggingInitializable) {
            ((LoggingInitializable) mapping).initialize(runState.getRunInfo());
        }
    }

    /**
	 * Initializes the given AggregateDataMapping. If the AggregateDataMapping is a
	 * TimeDataMapping this set its schedule as the model schedule in the
	 * RunState's ScheduleRegistry. If the DataMapping is LoggingInitializable
	 * it will be initialized with the RunState's RunInfo.
	 * 
	 * @param mapping
	 * @param runState
	 */
    @SuppressWarnings("unchecked")
    public void initialize(AggregateDataMapping<?, ?> mapping, RunState runState) {
        if (mapping instanceof TimeDataMapping) {
            ((TimeDataMapping<?>) mapping).setSchedule(runState.getScheduleRegistry().getModelSchedule());
        }
        if (mapping instanceof LoggingInitializable) {
            ((LoggingInitializable) mapping).initialize(runState.getRunInfo());
        }
    }

    /**
	 * Calls unregister with the given RunState and the DataGatherer returned by
	 * {@link #getDataGatherer()}.
	 * 
	 * @see #unregister(RunState, DataGatherer)
	 * 
	 * @param runState
	 *            the RunState passed to unregister
	 * @param context
	 *            ignored
	 */
    @Override
    public void runCleanup(RunState runState, Context context) {
        LoggingFramework.unregister(runState, getDataGatherer());
    }
}
