package playground.johannes.coopsim.mental.planmod.concurrent;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.matsim.api.core.v01.population.Plan;
import playground.johannes.coopsim.mental.planmod.Choice2ModAdaptorFactory;
import playground.johannes.coopsim.mental.planmod.PlanModEngine;
import playground.johannes.sna.util.MultiThreading;

/**
 * @author illenberger
 * 
 */
public class ConcurrentPlanModEngine implements PlanModEngine {

    private final PlanModExecutor executor;

    public ConcurrentPlanModEngine(Choice2ModAdaptorFactory factory) {
        int threads = MultiThreading.getNumAllowedThreads();
        executor = new PlanModExecutor(threads, threads, Integer.MAX_VALUE, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new PlanModThreadFactory(factory));
    }

    @Override
    public void run(List<Plan> plans, Map<String, Object> choices) {
        PlanModFutureTask[] tasks = new PlanModFutureTask[plans.size()];
        for (int i = 0; i < plans.size(); i++) {
            PlanModRunnable r = new PlanModRunnable(choices, plans.get(i));
            PlanModFutureTask t = new PlanModFutureTask(r);
            executor.execute(t);
            tasks[i] = t;
        }
        for (int i = 0; i < tasks.length; i++) {
            try {
                tasks[i].get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public void finalize() {
        try {
            super.finalize();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        executor.shutdown();
    }
}
