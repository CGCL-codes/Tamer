package uk.org.ponder.genetic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import uk.org.ponder.arrayutil.ListUtil;
import uk.org.ponder.doubleutil.doubleVector;
import uk.org.ponder.event.EventFirer;
import uk.org.ponder.event.Listener;
import uk.org.ponder.streamutil.write.PrintOutputStream;
import uk.org.ponder.util.Logger;

/**
 * The class 
 * 
 * @author Bosmon
 */
public class Optimiser {

    private PrintOutputStream transcript;

    ArrayList operators = new ArrayList();

    doubleVector operatorprob = new doubleVector(10);

    ArrayList population = new ArrayList();

    Operator operatorAt(int i) {
        return (Operator) operators.get(i);
    }

    int operatorSize() {
        return operators.size();
    }

    public void addOperator(Operator o, double prob) {
        operators.add(o);
        operatorprob.addElement(prob);
    }

    Individual individualAt(int i) {
        return (Individual) population.get(i);
    }

    int currentPopSize() {
        return population.size();
    }

    int popsize;

    public void setPopSize(int popsize) {
        this.popsize = popsize;
    }

    int gens;

    public void setGenerations(int gens) {
        this.gens = gens;
    }

    ModelRunContext context;

    Wheel massivewheel;

    boolean request_cancel = false;

    EventFirer eventfirer = new EventFirer(new Class[] { RunStartedEvent.class, RunTerminatedEvent.class });

    public void addRunListener(Class eventclass, Listener l) {
        eventfirer.addListener(eventclass, l);
    }

    public void setTranscriptStream(PrintOutputStream transcript) {
        this.transcript = transcript;
    }

    public void init(ModelRunContext context) {
        this.context = context;
        request_cancel = false;
        indcount = 0;
        massivewheel = new Wheel(operatorprob.asArray());
        population.clear();
        fillPop();
    }

    private void fillPop() {
        for (int i = population.size(); i < popsize; ++i) {
            Individual newind = context.generateIndividual();
            context.chewIndividual(newind);
            transcript.println(newind);
            population.add(newind);
        }
    }

    IndividualComparator comparator = new IndividualComparator();

    Wheel popwheel;

    private void makePopWheel() {
        double[] fits = new double[popsize];
        double minfit = Double.MAX_VALUE;
        double maxfit = -Double.MAX_VALUE;
        for (int i = 0; i < popsize; ++i) {
            double fit = individualAt(i).selfitness;
            if (fit < minfit) minfit = fit;
            if (fit > maxfit) maxfit = fit;
        }
        double floor = maxfit - 2 * (maxfit - minfit);
        for (int i = 0; i < popsize; ++i) {
            fits[i] = individualAt(i).selfitness - floor;
        }
        popwheel = new Wheel(fits);
    }

    int getRandomInd(Random r) {
        return r.nextInt(popsize);
    }

    int indcount;

    void processNewInd(Individual newind) {
        context.chewIndividual(newind);
        newpop.add(newind);
        ++indcount;
    }

    boolean tournament = true;

    private void tournamentSelect() {
        Random random = context.getRandom();
        while (population.size() > popsize) {
            int ind1 = random.nextInt(population.size());
            int ind2 = random.nextInt(population.size());
            if (ind1 > ind2) {
                population.remove(ind1);
            } else if (ind2 > ind1) {
                population.remove(ind2);
            }
        }
    }

    ArrayList newpop;

    public void run() {
        eventfirer.fireEvent(new RunStartedEvent());
        try {
            context.postChewIndividuals(0, population);
            int popsize = currentPopSize();
            Random random = context.getRandom();
            indcount = 0;
            for (int gen = 1; gen < gens; ++gen) {
                makePopWheel();
                newpop = population;
                for (int i = 0; i < popsize; ++i) {
                    int opit = massivewheel.sample(random);
                    Operator op = operatorAt(opit);
                    int indsel = getRandomInd(random);
                    Individual ind = individualAt(indsel);
                    Logger.println("Creating ind " + indcount + ": ", Logger.DEBUG_SUBATOMIC);
                    try {
                        if (op instanceof BinaryOperator) {
                            int otheri = getRandomInd(random);
                            Individual otherind = individualAt(otheri);
                            Individual[] newinds = ((BinaryOperator) op).operate(ind, otherind);
                            processNewInd(newinds[0]);
                            processNewInd(newinds[1]);
                        } else {
                            Individual newind = ((UnaryOperator) op).operate(ind);
                            processNewInd(newind);
                        }
                    } finally {
                    }
                }
                population = newpop;
                context.postChewIndividuals(gen, population);
                Collections.sort(population, comparator);
                transcript.println("Generation " + gen + ":");
                for (int i = 0; i < 5; ++i) {
                    transcript.println(individualAt(i));
                }
                for (int i = 0; i < population.size() - 1; ++i) {
                    if (context.isDuplicate(individualAt(i), individualAt(i + 1))) {
                        population.remove(i + 1);
                    }
                }
                fillPop();
                if (tournament) {
                    tournamentSelect();
                } else {
                    ListUtil.setSize(population, popsize);
                }
                transcript.println(individualAt(popsize - 1));
                context.endGeneration(gen, population);
                if (request_cancel) break;
            }
        } finally {
            eventfirer.fireEvent(new RunTerminatedEvent());
        }
    }

    void printBot3() {
        for (int i = population.size() - 4; i < population.size(); ++i) {
            transcript.println(i + ": " + individualAt(i));
        }
    }

    /**
   * Requests that the run be terminated. The run() method will return at the
   * end of the next generation.
   */
    public void requestCancelRun() {
        request_cancel = true;
    }
}
