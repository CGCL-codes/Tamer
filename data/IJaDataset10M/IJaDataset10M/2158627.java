package playground.mrieser.template.replanning;

import org.apache.log4j.Logger;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.api.core.v01.replanning.PlanStrategyModule;

public class TemplateStrategyModule implements PlanStrategyModule {

    private static final Logger log = Logger.getLogger(TemplateStrategyModule.class);

    private final TemplatePlanAlgorithm planAlgo;

    private int counter = 0;

    public TemplateStrategyModule() {
        this.planAlgo = new TemplatePlanAlgorithm();
    }

    public void prepareReplanning() {
        this.counter = 0;
    }

    public void handlePlan(final Plan plan) {
        this.counter++;
        this.planAlgo.run(plan);
    }

    public void finishReplanning() {
        log.info("number of handled plans: " + this.counter);
    }
}
