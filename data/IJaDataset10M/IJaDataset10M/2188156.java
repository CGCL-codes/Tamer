package exec.console;

import java.beans.IntrospectionException;
import java.io.IOException;
import org.xml.sax.SAXException;
import exec.statistics.FullExecutionInfo;
import exec.statistics.SingleQueryStatistics;
import exec.statistics.StatKeeper;
import exec.visual.IExecutionContent;
import pathplanning.plan.PathPlannerController;
import pathplanning.stats.IStatistics;
import pathplanning.stats.PathPlanStatistics;
import planning.file.PlannerLoader;
import planning.file.props.ControllerProp;
import planning.plan.Executer;
import planning.plan.FinishReason;
import planning.plan.IController;
import simulation.file.SimulatorLoader;
import simulation.model.Simulator;
import simulation.shell.Shell;

public class RobotSimCmd {

    IExecutionContent executionContent;

    Executer executer;

    Simulator simulator;

    StateKeeper stateKeeper;

    PlannerLoader planLoader;

    public RobotSimCmd() {
    }

    public void initialize(String simFile, String execFile) throws Exception {
        SimulatorLoader simLoader = new SimulatorLoader();
        simulator = simLoader.loadSimulator(simFile);
        Shell shell = new Shell(simulator);
        planLoader = new PlannerLoader();
        executer = planLoader.loadExecuter(execFile, shell);
        executionContent = new ConsoleExecutionContent(executer, shell);
    }

    public PathPlanStatistics run(boolean first, boolean randomTargets, boolean randomInits, double enlargement, long timeUp) {
        IStatistics statistics = new SingleQueryStatistics(simulator, executer);
        if (first) {
            stateKeeper = new StateKeeper(simulator);
            stateKeeper.saveInitialConfigurations();
        } else {
            stateKeeper.revertToInitial();
        }
        executionContent.getShell().next();
        long initStart = System.currentTimeMillis();
        System.out.println("Started placing robots and targets, time : " + initStart);
        for (IController<?> controller : executer.getControllers()) {
            if (randomTargets || randomInits) {
                System.out.println("Random init " + randomInits + " - random targets " + randomTargets);
                if (!executer.interchangeRobotConfigurations(controller.getWorld(), executionContent.getShell(), enlargement, randomTargets)) {
                    System.err.println("Interchanged robots are in collision :(");
                    System.exit(1);
                }
            }
            executionContent.getShell().next();
        }
        initStart = System.currentTimeMillis() - initStart;
        System.out.println("Robots placed in " + initStart + " milliseconds");
        executionContent.getShell().next();
        statistics.initialize();
        statistics.controllersInitializing();
        boolean running = false;
        if (first) {
            running = executer.initialize();
        } else {
            running = executer.subInitialize();
        }
        statistics.controllersInitialized();
        boolean collided = false;
        boolean timeIsUp = false;
        long startTime = -System.currentTimeMillis();
        while (running) {
            executionContent.getShell().next();
            if (executer.checkCollisions()) {
                collided = true;
                running = false;
            } else {
                boolean allFinished = true;
                for (IController<?> controller : executer.getControllers()) {
                    if (controller.continues()) {
                        controller.next();
                        if (controller.isTakingStatistics()) {
                            allFinished = false;
                        }
                    }
                }
                statistics.step();
                if (allFinished) {
                    running = false;
                }
                long elapsed = System.currentTimeMillis() + startTime;
                if (elapsed > timeUp) {
                    timeIsUp = true;
                    running = false;
                }
            }
        }
        if (!collided && !timeIsUp) {
            FinishReason finishReason = FinishReason.SUCCESSFUL;
            for (IController<?> controller : executer.getControllers()) {
                if (controller.isTakingStatistics()) {
                    FinishReason reason = controller.finishReason();
                    if (reason != finishReason) {
                        if (reason.ordinal() > finishReason.ordinal()) finishReason = reason;
                    }
                }
            }
            statistics.finish(finishReason);
        } else {
            if (collided) statistics.finish(FinishReason.COLLIDED); else if (timeIsUp) statistics.finish(FinishReason.TIME_UP);
        }
        int totalNodeCount = 0;
        int totalEdgeCount = 0;
        for (IController<?> controller : executer.controllers) {
            if (controller instanceof PathPlannerController) {
                PathPlannerController ppC = (PathPlannerController) controller;
                if (ppC.isTakingStatistics()) {
                    totalNodeCount += ppC.calculateNodeCount();
                    totalEdgeCount += ppC.calculateEdgeCount();
                }
            }
        }
        statistics.getStatistics().setNodeCount(totalNodeCount);
        statistics.getStatistics().setEdgeCount(totalEdgeCount);
        return statistics.getStatistics();
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 10) {
            System.out.println("args[0] - simulator xml file\n" + "args[1] [file path]- executer xml file\n" + "args[2] [int]- time up amount (Without initialization)\n" + "args[3] [int]- number of runs\n" + "args[4] [bool] - for each plan generate new random targets\n" + "args[5] [bool] - for each plan generate new random inits\n" + "args[6] double - How much to enlarge robots when making collision checks, to place them, and their goals in the world\n" + "args[7] [bool] - for each plan make the precomputation again\n" + "args[8] [file path] - file to save execution properties\n" + "args[9] [file path] - file to write the statistics\n" + "\nAll rights reserved to Fuat Geleri mailto:fuatgeleri@gmail.com\n");
            return;
        }
        System.out.println("Simulation : " + args[0]);
        System.out.println("Executer : " + args[1]);
        System.out.println("Save execution properties to : " + args[8]);
        System.out.println("Save statistics to : " + args[9]);
        System.out.println("Number of runs  : " + args[3]);
        System.out.println("Enlargement : " + args[6]);
        System.out.println("Time up : " + args[2]);
        System.out.println("New inits : " + args[4]);
        System.out.println("New targets : " + args[5]);
        System.out.println("Precompute again : " + args[7]);
        int timeUp = Integer.parseInt(args[2]);
        int runCount = Integer.parseInt(args[3]);
        boolean newTargets = Boolean.parseBoolean(args[4]);
        boolean newInits = Boolean.parseBoolean(args[5]);
        double enlargement = Double.parseDouble(args[6]);
        boolean stratchInitialization = Boolean.parseBoolean(args[7]);
        RobotSimCmd cmd = new RobotSimCmd();
        cmd.initialize(args[0], args[1]);
        cmd.execute(runCount, timeUp, newTargets, newInits, enlargement, stratchInitialization, args[8], args[9]);
    }

    /**
	 * Run the algorithm many times and keep statistics, with also execution properties in files..
	 * @param runCount Count of the runs to be taken to construct the statistics..
	 * @param timeUp How much to give to the controllers to make their jobs in finding ways and taking those ways
	 * to reach to the target
	 * @param newTargets Whether or not change the target configurations of the robots at each run
	 * @param newInits Whether or not change the initial configurations of the robots at each run
	 * @param enlargement How much to enlarge objects when making collision checks, to place them, and their goals in the world
	 * @param stratchInitialization Whether to use total initialization of the controller for each run
	 * @param propsFile File to write properties of the execution, the statistics taken for
	 * @param statFile The statistics of the execution
	 * @throws IOException
	 * @throws SAXException
	 * @throws IntrospectionException
	 */
    private void execute(int runCount, int timeUp, boolean newTargets, boolean newInits, double enlargement, boolean stratchInitialization, String propsFile, String statFile) throws IOException, SAXException, IntrospectionException {
        StatKeeper stats = new StatKeeper();
        stats.initialize(statFile, propsFile);
        FullExecutionInfo properties = stats.properties;
        properties.setRunCount(runCount);
        properties.setTimeUp(timeUp);
        properties.setNewTargets(newTargets);
        properties.setNewInits(newInits);
        properties.setStratchInitialization(stratchInitialization);
        for (IController<?> controller : executer.getControllers()) {
            ControllerProp prop = planLoader.propConverter.generateControllerProp(controller);
            properties.addController(prop);
        }
        long elapsed = -System.currentTimeMillis();
        PathPlanStatistics next = run(true, newTargets, newInits, enlargement, timeUp);
        printStatistics(next);
        stats.next(next);
        for (int i = 1; i < runCount; i++) {
            next = run(stratchInitialization, newTargets, newInits, enlargement, timeUp);
            System.out.println("Run : " + i);
            printStatistics(next);
            stats.next(next);
        }
        elapsed += System.currentTimeMillis();
        System.out.println("Elapsed : " + elapsed);
        stats.finish();
    }

    protected void printStatistics(PathPlanStatistics stats) {
        System.out.println("Finish reason : " + FinishReason.values()[stats.getFinishReason()]);
        System.out.println("Elapsed time : " + stats.getTimeElapsed());
        System.out.println("PreComputation time : " + stats.getPreComputationTime());
        System.out.println("Tightness : " + stats.getTightness());
        System.out.println("Travelled : " + stats.getSumDistanceTravelled());
        System.out.println("Step count : " + stats.getStepCount());
        System.out.println("Initial distances : " + stats.getTotalInitialDistanceToTargets());
        System.out.println("Final distances : " + stats.getTotalRemainingDistanceToTargets());
        System.out.println("NRL : " + stats.getNormalizedRobotPathLength());
        System.out.println();
    }
}
