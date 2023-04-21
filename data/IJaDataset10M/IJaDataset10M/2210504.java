package org.jgap.distr.grid;

import org.homedns.dade.jcgrid.*;
import org.homedns.dade.jcgrid.worker.*;
import org.jgap.*;
import org.jgap.distr.grid.*;

/**
 * A worker receives work units from a JGAPServer and sends back computed
 * solutions to the same JGAPServer.
 *
 * @author Klaus Meffert
 * @since 3.2 (since 3.01 this class contained something different that is now
 * in class org.jgap.distr.grid.JGAPWorkers)
 */
public class JGAPWorker implements Worker {

    /** String containing the CVS revision. Read out via reflection!*/
    private static final String CVS_REVISION = "$Revision: 1.8 $";

    /**
   * Executes the evolution and returns the result.
   *
   * @param work WorkRequest
   * @param workDir String
   * @return WorkResult
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.01
   */
    public WorkResult doWork(WorkRequest work, String workDir) throws Exception {
        JGAPRequest req = ((JGAPRequest) work);
        Configuration conf = req.getConfiguration();
        conf = conf.newInstance(conf.getId() + "_1", conf.getName() + "_1");
        req.setConfiguration(conf);
        Genotype gen = null;
        if (req.getGenotypeInitializer() != null) {
            Population initialPop = req.getPopulation();
            gen = req.getGenotypeInitializer().setupGenotype(req, initialPop);
            if (req.getWorkerEvolveStrategy() != null) {
                req.getWorkerEvolveStrategy().evolve(gen);
            }
        }
        WorkResult res = req.getWorkerReturnStrategy().assembleResult(req, gen);
        return res;
    }

    /**
   * Convenience method to start the worker.
   *
   * @param args command-line arguments, such as server address
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.01
   */
    public static void main(String[] args) throws Exception {
        new JGAPWorkers(args);
    }
}
