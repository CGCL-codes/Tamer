package nevo.pkgs.es;

import java.util.*;
import nevo.core.*;

public class EvolutionaryOptimizer implements Optimizer {

    protected Mutator mutator;

    protected Selector parentSelector;

    protected Selector populationSelector;

    protected Model model;

    protected ObjectiveFunction landscape;

    protected List<IRecord> population;

    protected int populationSize;

    protected int numOffspring;

    protected int numReproducingParents;

    public void setParentSelector(Selector ps) {
        parentSelector = ps;
    }

    public Selector getParentSelector() {
        return parentSelector;
    }

    public void setPopulationSelector(Selector ps) {
        populationSelector = ps;
    }

    public Selector getPopulationSelector() {
        return populationSelector;
    }

    public void setNumReproducingParents(int n) {
        numReproducingParents = n;
    }

    public int getNumReproducingParents() {
        return numReproducingParents;
    }

    public void setNumOffspring(int n) {
        numOffspring = n;
    }

    public int getNumOffspring() {
        return numOffspring;
    }

    public void setMutator(Mutator m) {
        mutator = m;
    }

    public Mutator getMutator() {
        return mutator;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model m) {
        model = m;
    }

    public ObjectiveFunction getObjectiveFunction() {
        return landscape;
    }

    public void setObjectiveFunction(ObjectiveFunction func) {
        landscape = func;
    }

    public List<IRecord> getCurrentEstimates() {
        return population;
    }

    public List<IRecord> getPopulation() {
        return population;
    }

    public EvolutionaryOptimizer() {
        population = new ArrayList<IRecord>();
        numOffspring = 1;
    }

    public void setInitialGuesses(List<Map<String, Object>> params) throws Exception {
        List<IRecord> parents = new ArrayList<IRecord>();
        for (Map<String, Object> m : params) {
            IRecord i = new IRecord();
            i.setInput(m);
            parents.add(i);
        }
        List<Map<String, Object>> outputs;
        if (model.isParallel()) {
            outputs = runModelsAndWait(parents);
        } else {
            outputs = new ArrayList<Map<String, Object>>();
            for (IRecord i : parents) {
                Map<String, Object> m = i.getInput();
                Map<String, Object> fout = model.run(m);
                outputs.add(fout);
            }
        }
        for (int k = 0; k < outputs.size(); k++) {
            Map<String, Object> out = outputs.get(k);
            double fitness = landscape.compute(out);
            IRecord p = parents.get(k);
            p.setValue(fitness);
            population.add(p);
        }
        populationSize = params.size();
    }

    public IRecord iterate(IRecord previrec) throws Exception {
        List<IRecord> parents = parentSelector.select(population, numReproducingParents);
        List<IRecord> offspring = new ArrayList<IRecord>();
        IRecord rec = new IRecord();
        rec.setValue(0.0);
        rec.setInput(null);
        rec.setOutput(null);
        for (int k = 0; k < parents.size(); k++) {
            IRecord p = parents.get(k);
            for (int j = 0; j < numOffspring; j++) {
                offspring.add(mutator.mutate(p));
            }
        }
        List<Map<String, Object>> outputs;
        if (model.isParallel()) {
            outputs = runModelsAndWait(offspring);
        } else {
            outputs = new ArrayList<Map<String, Object>>();
            for (int k = 0; k < offspring.size(); k++) {
                IRecord child = offspring.get(k);
                Map<String, Object> mout = model.run(child.getInput());
                outputs.add(mout);
            }
        }
        for (int k = 0; k < outputs.size(); k++) {
            Map<String, Object> out = outputs.get(k);
            double fitness = landscape.compute(out);
            IRecord child = offspring.get(k);
            child.setValue(fitness);
            population.add(child);
        }
        List<IRecord> newgeneration = populationSelector.select(population, populationSize);
        population = newgeneration;
        return rec;
    }

    protected List<Map<String, Object>> runModelsAndWait(List<IRecord> inputs) throws Exception {
        List<ModelThread> threads = new ArrayList<ModelThread>();
        for (int k = 0; k < inputs.size(); k++) {
            Map<String, Object> in = inputs.get(k).getInput();
            ModelThread mt = new ModelThread();
            mt.setModelData(model, in);
            threads.add(k, mt);
            Thread t = new Thread(mt);
            t.start();
        }
        int runningcnt = threads.size();
        while (runningcnt > 0) {
            runningcnt = 0;
            for (int k = 0; k < threads.size(); k++) {
                if (!(threads.get(k).isStarted()) || (threads.get(k).isRunning())) runningcnt++;
            }
            Thread.sleep(100);
        }
        List<Map<String, Object>> outputs = new ArrayList<Map<String, Object>>();
        for (int k = 0; k < inputs.size(); k++) {
            Map<String, Object> out = threads.get(k).getModelOutput();
            outputs.add(k, out);
        }
        return outputs;
    }
}
