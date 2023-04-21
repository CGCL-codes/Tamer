package com.oat.domains.examples;

import java.util.LinkedList;
import com.oat.AlgorithmExecutor;
import com.oat.RunProbe;
import com.oat.domains.tsp.TSPDomain;
import com.oat.domains.tsp.TSPProblem;
import com.oat.domains.tsp.algorithms.aco.AntSystem;
import com.oat.stopcondition.EvaluationsStopCondition;

/**
 * Type: ExampleASTSP<br/>
 * Date: 28/11/2006<br/>
 * <br/>
 * Description: Example implementation of an ACO algorithm for a TSP problem
 * <br/>
 * @author Jason Brownlee
 * 
 * <pre>
 * Change History
 * ----------------------------------------------------------------------------
 * 
 * </pre>
 */
public class ExampleASTSP {

    /**
     * Example implementation of the Ant System (AS) algorithm for
     * a TSP problem (berlin 52)
     * @param args
     */
    public static void main(String[] args) {
        TSPDomain domain = new TSPDomain();
        TSPProblem problem = new TSPProblem("tsp/berlin52.tsp", "tsp/berlin52.opt.tour");
        EvaluationsStopCondition stopCondition = new EvaluationsStopCondition(1000);
        AntSystem algorithm = new AntSystem();
        algorithm.setSeed(1);
        AlgorithmExecutor executor = new AlgorithmExecutor(problem, algorithm, stopCondition);
        LinkedList<RunProbe> probes = domain.loadDomainRunProbes();
        executor.addRunProbes(probes);
        try {
            executor.executeAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        System.out.println("Problem Details");
        System.out.println("Problem: " + problem.getName());
        System.out.println("Problem Details: " + problem.getDetails());
        System.out.println("Problem Configuration: " + problem.getConfigurationDetails());
        System.out.println("Algorithm Details");
        System.out.println("Algorithm: " + algorithm.getName());
        System.out.println("Algorithm Details: " + algorithm.getDetails());
        System.out.println("Algorithm Configuration: " + algorithm.getConfigurationDetails());
        System.out.println("Run Details");
        for (RunProbe probe : probes) {
            System.out.println(probe.getName() + ": " + probe.getProbeObservation());
        }
    }
}
