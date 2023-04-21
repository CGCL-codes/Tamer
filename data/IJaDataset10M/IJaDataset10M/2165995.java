package repast.simphony.demo.watch;

import java.util.ArrayList;
import java.util.List;
import repast.simphony.engine.controller.ControllerActionConstants;
import repast.simphony.engine.controller.ScheduledMethodControllerAction;
import repast.simphony.engine.controller.WatcherControllerAction;
import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.engine.environment.ControllerRegistry;
import repast.simphony.engine.environment.RunEnvironmentBuilder;
import repast.simphony.engine.watcher.WatchAnnotationReader;
import repast.simphony.engine.watcher.WatcheeInstrumentor;
import repast.simphony.engine.watcher.WatcherTrigger;
import repast.simphony.scenario.ModelInitializer;
import repast.simphony.scenario.Scenario;
import repast.simphony.util.ClassPathFilter;

/**
 * An initializer that sets up watch and scheduled method annotation reading.
 * This may be used when agent classes are not defined in the model.score.
 * 
 * @author Eric Tatara
 *
 */
public class MyInitializer implements ModelInitializer {

    public void initialize(Scenario scen, RunEnvironmentBuilder builder) {
        System.out.print("Initializing...");
        WatcheeInstrumentor instrumentor = new WatcheeInstrumentor();
        WatcherTrigger.initInstance(instrumentor);
        String agentClassPath = "../repast.simphony.demos/bin";
        instrumentor.instrument(agentClassPath, new ClassPathFilter("repast.simphony.demo.watch.Agent"));
        ControllerRegistry registry = scen.getControllerRegistry();
        ControllerAction scheduleRoot = registry.findAction(scen.getContext().getId(), ControllerActionConstants.SCHEDULE_ROOT);
        List<Class<?>> myClasses = new ArrayList<Class<?>>();
        myClasses.add(Agent.class);
        WatchAnnotationReader watcherAnnotationReader = new WatchAnnotationReader();
        watcherAnnotationReader.processClasses(myClasses);
        ControllerAction watcherAction = new WatcherControllerAction(watcherAnnotationReader);
        registry.addAction(scen.getContext().getId(), scheduleRoot, watcherAction);
        ScheduledMethodControllerAction action = new ScheduledMethodControllerAction(myClasses);
        registry.addAction(scen.getContext().getId(), scheduleRoot, action);
        System.out.print("done.\n");
    }
}
