package osmosis.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openstreetmap.osmosis.core.TaskRegistrar;
import org.openstreetmap.osmosis.core.container.v0_6.EntityContainer;
import org.openstreetmap.osmosis.core.pipeline.common.Pipeline;
import org.openstreetmap.osmosis.core.pipeline.common.TaskConfiguration;
import osmosis.FileFormat;
import osmosis.SourceIterator;

class Test {

    public static void main(String args[]) {
        String infile = "/home/z/dev/osmosis/osmosis/trunk/data/berlin.osm.pbf";
        infile = "/tmp/osm/berlin.extract/berlin.osm.pbf";
        if (args.length > 0) {
            infile = args[0];
        }
        System.out.println("osmosis");
        SourceIterator sourceIterator = new SourceIterator(FileFormat.PBF, infile);
        for (EntityContainer ec : sourceIterator) {
            System.out.println(ec);
        }
        TaskRegistrar taskRegistrar = new TaskRegistrar();
        taskRegistrar.initialize(new ArrayList<String>(0));
        taskRegistrar.getFactoryRegister().register("foo", new MyTaskFactory());
        taskRegistrar.getFactoryRegister().register("bar", new StatsTaskFactory());
        Pipeline pipeline = new Pipeline(taskRegistrar.getFactoryRegister());
        List<TaskConfiguration> tasks = new ArrayList<TaskConfiguration>();
        Map<String, String> pipeArgs = new HashMap<String, String>();
        Map<String, String> configArgs = new HashMap<String, String>();
        TaskConfiguration taskRead = new TaskConfiguration("reader", "read-pbf", pipeArgs, configArgs, infile);
        tasks.add(taskRead);
        tasks.add(new TaskConfiguration("bar", "bar", pipeArgs, configArgs, null));
        pipeline.prepare(tasks);
        pipeline.execute();
        pipeline.waitForCompletion();
    }
}
