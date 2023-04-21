package wmh.satsolver;

import wmh.satsolver.input.BooleanFormulaReader;
import wmh.satsolver.input.CnfFileLoadingException;
import wmh.satsolver.sc.ElapsedTimeStopCondition;
import wmh.satsolver.sc.NumIterationsStopCondition;
import wmh.satsolver.solvers.WalkSatSolver;
import wmh.satsolver.solvers.DLMA1;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.List;
import java.util.ArrayList;
import org.apache.log4j.Logger;

public class SatSolver {

    private static Logger logger = Logger.getLogger(SatSolver.class);

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: satsolver problem_file cfg_file");
            System.exit(-1);
        }
        String problemFileName = args[0];
        String cfgFileName = args[1];
        BooleanFormula formulaToSolve = null;
        try {
            formulaToSolve = BooleanFormulaReader.readDimacs(problemFileName);
        } catch (CnfFileLoadingException e) {
            System.out.println("Cannot load problem file");
            System.out.println(e);
            System.exit(-1);
        }
        SolverOptions options = null;
        try {
            options = loadFromFile(cfgFileName);
        } catch (IOException e) {
            System.out.println("Cannot load configuration file");
            System.out.println(e);
            System.exit(-1);
        }
        runSolver(formulaToSolve, options);
    }

    private static void runSolver(BooleanFormula formulaToSolve, SolverOptions options) {
        logger.info("Running solver, num clauses = " + formulaToSolve.getNumClauses() + ", num vars = " + formulaToSolve.getNumVarsPerClause());
        List<StopCondition> stopConditions = new ArrayList<StopCondition>();
        if (options.scMaxIters > 0) {
            logger.debug("Max iterations: " + options.scMaxIters);
            stopConditions.add(new NumIterationsStopCondition(options.scMaxIters));
        }
        if (options.scTimeLimit > 0) {
            long timeLimitPerIteration = (long) Math.ceil((double) options.scTimeLimit / options.numRestarts);
            logger.debug("Time limit: " + options.scTimeLimit + "ms, per iteration: " + timeLimitPerIteration + " ms");
            stopConditions.add(new ElapsedTimeStopCondition(timeLimitPerIteration));
        }
        Assignment bestAsFoundByWS;
        Assignment bestAsFoundByDLM = null;
        int numSatForBestWS = 0;
        int numSatForBestDLM = 0;
        List<Assignment> initialAssignmentsWS = new ArrayList<Assignment>(options.numRestarts);
        List<Assignment> initAssignmentsDLM = new ArrayList<Assignment>(options.numRestarts);
        for (int i = 0; i < options.numRestarts; i++) {
            Assignment as = AssignmentFactory.getRandomAssignment(formulaToSolve.getNumVarsPerClause());
            initialAssignmentsWS.add(as);
            initAssignmentsDLM.add(as.duplicate());
        }
        long currentTime = System.nanoTime();
        logger.info("Starting WalkSAT iterations");
        for (int i = 0; i < options.numRestarts; i++) {
            logger.debug("BIG Iteration " + (i + 1) + "/" + options.numRestarts);
            Assignment initialAssignmentWS = initialAssignmentsWS.get(i);
            logger.debug("Starting WalkSAT, rmProb = " + options.rmProb + ", rndBest = " + options.rndBest);
            AbstractSolver walkSAT = new WalkSatSolver(formulaToSolve, initialAssignmentWS, options.rmProb, options.rndBest);
            for (StopCondition stopCondition : stopConditions) {
                walkSAT.addStopCondition(stopCondition);
            }
            Assignment asFoundByWS = walkSAT.solve();
            if (logger.isDebugEnabled()) {
                TaskStats stats = walkSAT.taskStats;
                logger.debug("WalkSAT ended after " + stats.getNumIterations() + " iterations, " + ((double) stats.getElapsedTime() / 1000000) + " ms, satisfied clauses = " + stats.getBestNumSatisfiedClauses());
            }
            int numSatByWS = formulaToSolve.getNumSatisfiedClauses(asFoundByWS);
            if (numSatByWS > numSatForBestWS) {
                bestAsFoundByWS = asFoundByWS;
                numSatForBestWS = numSatByWS;
            }
            if (numSatForBestWS == formulaToSolve.getNumClauses()) {
                logger.info("WalkSAT found a solution, no restarts needed");
                break;
            }
        }
        long wsTime = System.nanoTime() - currentTime;
        currentTime = System.nanoTime();
        logger.debug("Starting DLM iterations");
        for (int i = 0; i < options.numRestarts; i++) {
            logger.debug("BIG Iteration " + (i + 1) + "/" + options.numRestarts);
            Assignment iniAssignmentDLM = initAssignmentsDLM.get(i);
            AbstractSolver dlm = new DLMA1(formulaToSolve, iniAssignmentDLM, options.dlmGamma);
            for (StopCondition stopCondition : stopConditions) {
                dlm.addStopCondition(stopCondition);
            }
            logger.debug("Starting DLM, gamma = " + options.dlmGamma);
            Assignment asFoundByDLM = dlm.solve();
            if (logger.isDebugEnabled()) {
                TaskStats stats = dlm.taskStats;
                logger.debug("DLM ended after " + stats.getNumIterations() + " iterations, " + ((double) stats.getElapsedTime() / 1000000) + " ms, satisfied clauses = " + stats.getBestNumSatisfiedClauses());
            }
            int numSatByDLM = formulaToSolve.getNumSatisfiedClauses(asFoundByDLM);
            if (numSatByDLM > numSatForBestDLM) {
                bestAsFoundByDLM = asFoundByDLM;
                numSatForBestDLM = numSatByDLM;
            }
            if (numSatForBestDLM == formulaToSolve.getNumClauses()) {
                logger.info("DLM found a solution, no restarts needed");
                break;
            }
        }
        long dlmTime = System.nanoTime() - currentTime;
        System.out.println("Number of clauses in formula: " + formulaToSolve.getNumClauses());
        System.out.println("Number of satisfied clauses by WalkSAT =  " + numSatForBestWS);
        System.out.println("Time of execution for WalkSAT: " + ((double) wsTime / 1000000000) + " seconds (" + ((double) wsTime / 1000000) + " ms)");
        System.out.println("Number of satisfied clauses by DLM =  " + numSatForBestDLM);
        System.out.println("Time of execution for DLM: " + ((double) dlmTime / 1000000000) + " seconds (" + ((double) dlmTime / 1000000) + " ms)");
    }

    private static SolverOptions loadFromFile(String fileName) throws IOException {
        SolverOptions options = new SatSolver.SolverOptions();
        FileInputStream fis = new FileInputStream(fileName);
        try {
            Properties properties = new Properties();
            properties.load(fis);
            options.numRestarts = Integer.parseInt(properties.getProperty("numrestarts"));
            options.scMaxIters = Integer.parseInt(properties.getProperty("sc.maxiters"));
            options.scTimeLimit = Long.parseLong(properties.getProperty("sc.timelimit"));
            options.dlmGamma = Integer.parseInt(properties.getProperty("dlm.gamma"));
            options.rmProb = Float.parseFloat(properties.getProperty("walksat.rmprob"));
            options.rndBest = Boolean.parseBoolean(properties.getProperty("walksat.rndbest"));
        } finally {
            fis.close();
        }
        return options;
    }

    private static class SolverOptions {

        public int numRestarts;

        public int scMaxIters;

        public long scTimeLimit;

        public int dlmGamma;

        public float rmProb;

        public boolean rndBest;
    }
}
