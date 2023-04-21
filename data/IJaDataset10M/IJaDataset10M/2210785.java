package ch.idsia.benchmark.tasks;

import ch.idsia.agents.Agent;
import ch.idsia.tools.MarioAIOptions;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey at idsia dot ch
 * Date: Apr 4, 2010 Time: 11:33:15 AM
 * Package: ch.idsia.maibe.tasks
 */
public class MushroomTask extends BasicTask implements Task {

    private MarioCustomSystemOfValues sov = new MarioCustomSystemOfValues();

    public MushroomTask(MarioAIOptions marioAIOptions) {
        super(marioAIOptions);
        this.options = marioAIOptions;
    }

    public int evaluate(Agent controller) {
        float fitness = 0;
        controller.reset();
        options.setAgent(controller);
        this.setOptionsAndReset(options);
        this.runSingleEpisode(1);
        fitness += this.getEnvironment().getEvaluationInfo().computeWeightedFitness(sov);
        return (int) fitness;
    }
}
