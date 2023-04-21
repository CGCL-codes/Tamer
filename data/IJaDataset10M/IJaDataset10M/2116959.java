package net.sourceforge.aiexperiments.symbiotic.examples.tspsymbiot;

import java.util.*;
import net.sourceforge.aiexperiments.symbiotic.symbiot.*;
import net.sourceforge.aiexperiments.symbiotic.console.*;

/**
 * This algorithm identifies three behaviour patterns based on the stress.
 * The stress for:
 * 1: this symbiot is not the biggest => altruistic response
 * 2: this symbiot is the biggest => selfish response
 * 3: the next symbiot in the sequence is biggest => adjusted altruism
 *
 * This results that every cycle has symbiots engaging in different behaviour,
 * i.e are "different"
*/
public class GaAlgorithm extends Algorithm {

    /**
   * The neighbourhoods contain additional data about the paths, that are
   * maintained by this TreeMap
  */
    private TreeMap neighbourhoods;

    private Environment env;

    private Configuration config;

    /**
   * CONSTRUCTOR: Only used to create a parent algorithm
  */
    public GaAlgorithm(Configuration configuration, String name) {
        super(configuration, name);
        config = configuration;
    }

    /**
   * CONSTRUCTOR:
  */
    public GaAlgorithm(Configuration configuration, String name, Nature nature) {
        super(configuration, name, nature);
        config = configuration;
        neighbourhoods = env.getNeighbourhoods();
        super.setSelectIntegrator(false);
    }

    /**
  * This method creates an algorithm similar to this class. It should be made
  * for every algorithm
  */
    public Algorithm create(Nature nature) {
        return new GaAlgorithm(config, super.getName(), nature);
    }

    /**
   * This method performs the classic mutation of a path, according
   * to principles used in genetic algorithms
  */
    public float gaMutation(Integer locationi, Integer node, Integer nodei) {
        int mui = 0;
        if (node != nodei) {
            SymbiotLog log = (SymbiotLog) config.getLog();
            log.println("Mutation at mutation point " + locationi.intValue());
            Integer location = super.getLocation();
            Symbiot symbiot = super.getSymbiot(location);
            TspNeighbourhood neighbourhood = (TspNeighbourhood) neighbourhoods.get(node);
            PathManager pathManager = env.getPathManager();
            ArrayList path = pathManager.getPath();
            int currentDistance;
            synchronized (pathManager) {
                currentDistance = pathManager.calculateLength(path);
                if (currentDistance >= 0) {
                    log.println("" + pathManager.toString(path) + " = " + currentDistance);
                    TspStrategy tspOperator = new TspStrategy(config, env);
                    tspOperator.mutate(path, locationi.intValue());
                    int newDistance = pathManager.calculateLength(path);
                    if (newDistance >= 0) {
                        log.println("" + pathManager.toString(path) + " = " + newDistance);
                        log.println();
                        mui = currentDistance - newDistance;
                    }
                }
                if (mui > 0) {
                }
            }
        }
        Scaling scale = env.getScaling();
        return scale.intToFloat(mui);
    }

    /**
   * This method performs the classic crossover of a path, according
   * to principles used in genetic algorithms
  */
    public float gaCrossover(Integer locationi, Integer node, Integer nodei) {
        int mui = 0;
        if (node != nodei) {
            SymbiotLog log = (SymbiotLog) config.getLog();
            Integer location = super.getLocation();
            Symbiot symbiot = super.getSymbiot(location);
            TspNeighbourhood neighbourhood = (TspNeighbourhood) neighbourhoods.get(node);
            PathManager pathManager = env.getPathManager();
            ArrayList path = pathManager.getPath();
            TspNeighbourhood neighbourhoodi = (TspNeighbourhood) neighbourhoods.get(nodei);
            TspGraph graph = config.getGraph();
            ArrayList selection = graph.getNodes();
            int currentDistance;
            synchronized (pathManager) {
                ArrayList partner = pathManager.getPath();
                if (path != partner) {
                    currentDistance = pathManager.calculateLength(path);
                    if (currentDistance >= 0) {
                        int partnerDistance = pathManager.calculateLength(partner);
                        int recombinationPoint = locationi.intValue();
                        log.println("Crossover " + node.intValue() + " at point " + recombinationPoint);
                        log.println("" + pathManager.toString(path) + " = " + currentDistance);
                        TspStrategy tspOperator = new TspStrategy(config, env);
                        path = tspOperator.recombination(selection, path, partner, recombinationPoint);
                        int newDistance = 0;
                        newDistance = pathManager.calculateLength(path);
                        if (newDistance >= 0) {
                            log.println("" + pathManager.toString(path) + " = " + newDistance);
                            log.println();
                            mui = currentDistance - newDistance;
                        }
                    }
                    if (mui > 0) {
                    }
                }
            }
        }
        Scaling scale = env.getScaling();
        return scale.intToFloat(mui);
    }

    /**
   * This algorithm uses genetic algorithm types of operationsto improve
   * the path
  */
    public float symbiosis(Integer locationi, float mu) throws NoStreamException {
        float mui = 0;
        Integer location = super.getLocation();
        TspSymbiot symbiot = (TspSymbiot) super.getSymbiot(location);
        StressData stressData = symbiot.getStressData();
        float stress = stressData.get();
        TspNeighbourhood neighbourhood = (TspNeighbourhood) symbiot.origin();
        Integer node = neighbourhood.getNode();
        Symbiot symbioti = super.getSymbiot(locationi);
        stressData = symbioti.getStressData();
        float stressi = stressData.get();
        neighbourhood = (TspNeighbourhood) symbioti.origin();
        Integer nodei = neighbourhood.getNode();
        SymbioticOrganism organism = env.getOrganism();
        ArrayList stressList = organism.getStressList();
        Float maxStress;
        Float minStress;
        if (stressList.size() > 0) {
            minStress = (Float) stressList.get(0);
            maxStress = (Float) stressList.get(stressList.size() - 1);
            if (stress < maxStress.floatValue()) {
                mui = this.gaCrossover(locationi, node, nodei);
            } else {
                mui = this.gaMutation(locationi, node, nodei);
            }
        }
        config.updateCalculations();
        return mui;
    }
}
