package osmosis.test;

import org.openstreetmap.osmosis.core.pipeline.common.TaskConfiguration;
import org.openstreetmap.osmosis.core.pipeline.common.TaskManager;
import org.openstreetmap.osmosis.core.pipeline.common.TaskManagerFactory;
import org.openstreetmap.osmosis.core.pipeline.v0_6.SinkManager;

class StatsTaskFactory extends TaskManagerFactory {

    @Override
    protected TaskManager createTaskManagerImpl(TaskConfiguration config) {
        return new SinkManager(config.getId(), new StatsTask(), config.getPipeArgs());
    }
}
