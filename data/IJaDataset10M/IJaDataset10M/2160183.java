package jeco.lib.problems.floorplan;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import jeco.kernel.algorithm.moga.NSGAII;
import jeco.kernel.operator.crossover.CycleCrossover;
import jeco.kernel.operator.selector.BinaryTournamentNSGAII;
import jeco.kernel.problem.Problem;
import jeco.kernel.problem.Solution;
import jeco.kernel.problem.Solutions;
import jeco.kernel.util.JecoLogger;

public class FloorplanGeneticAsocWire extends Problem<ComponentVariable> {

    private static final Logger logger = Logger.getLogger(FloorplanGeneticAsocWire.class.getName());

    protected FloorplanConfiguration cfg;

    protected boolean[][][] freeCells;

    protected int MaxWireLength = Integer.MAX_VALUE;

    public FloorplanGeneticAsocWire(String name, FloorplanConfiguration cfg) {
        super(name, cfg.components.size(), 2);
        this.cfg = cfg;
        freeCells = new boolean[cfg.maxLengthInCells][cfg.maxWidthInCells][cfg.numLayers];
        MaxWireLength = cfg.maxLengthInCells * cfg.maxWidthInCells * cfg.numLayers;
    }

    public FloorplanGeneticAsocWire clone() {
        FloorplanGeneticAsocWire clone = new FloorplanGeneticAsocWire(super.name, cfg.clone());
        return clone;
    }

    @Override
    public void newRandomSetOfSolutions(Solutions<ComponentVariable> solutions) {
        LinkedList<Component> componentsAsList = new LinkedList<Component>();
        for (Component c : cfg.components.values()) {
            componentsAsList.add(c);
        }
        Solution<ComponentVariable> solution = null;
        for (int i = 0; i < solutions.size(); ++i) {
            solution = solutions.get(i);
            int j = 0;
            for (Component component : componentsAsList) {
                solution.getVariables().set(j, new ComponentVariable(component.clone()));
                j++;
            }
            Collections.shuffle(componentsAsList);
        }
    }

    public void evaluate(Solution<ComponentVariable> solution) {
        if (solution.isEvaluated()) {
            return;
        }
        int x = 0, y = 0, z = 0;
        for (x = 0; x < cfg.maxLengthInCells; ++x) {
            for (y = 0; y < cfg.maxWidthInCells; ++y) {
                for (z = 0; z < cfg.numLayers; ++z) {
                    freeCells[x][y][z] = true;
                }
            }
        }
        double unfeasible = 0;
        ArrayList<ComponentVariable> variables = solution.getVariables();
        for (int i = 0; i < variables.size(); ++i) {
            unfeasible += place(solution, i);
        }
        solution.getObjectives().set(0, unfeasible);
        solution.getObjectives().set(1, computeWire(solution));
    }

    public double fitnessWire(Solution<ComponentVariable> solution, int idx) {
        double fitness = 0.0;
        int idFrom, idTo;
        Component cI = solution.getVariables().get(idx).getValue();
        idFrom = cI.id;
        Component cJ = null;
        for (int j = 0; j < idx; ++j) {
            cJ = solution.getVariables().get(j).getValue();
            idTo = cJ.id;
            if ((cfg.couplings.containsKey(idFrom) && cfg.couplings.get(idFrom).contains(idTo)) || (cfg.couplings.containsKey(idTo) && cfg.couplings.get(idTo).contains(idFrom))) {
                fitness += Math.abs(cI.x + cI.l / 2 - cJ.x - cJ.l / 2) + Math.abs(cI.y + cI.w / 2 - cJ.y - cJ.w / 2) + Math.abs(cI.z - cJ.z);
            }
        }
        return fitness;
    }

    public double computeWire(Solution<ComponentVariable> solution) {
        boolean feasibleI = true, feasibleJ = true;
        double fitness = 0.0, dist = 0.0;
        int idFrom, idTo;
        ArrayList<ComponentVariable> variables = solution.getVariables();
        for (int i = 0; i < variables.size() - 1; ++i) {
            feasibleI = true;
            Component cI = variables.get(i).getValue();
            if (cI.x < 0 || cI.y < 0 || cI.z < 0) {
                feasibleI = false;
            }
            idFrom = cI.id;
            for (int j = i + 1; j < variables.size(); ++j) {
                feasibleJ = true;
                Component cJ = variables.get(j).getValue();
                if (cJ.x < 0 || cJ.y < 0 || cJ.z < 0) {
                    feasibleJ = false;
                }
                idTo = cJ.id;
                if ((cfg.couplings.containsKey(idFrom) && cfg.couplings.get(idFrom).contains(idTo)) || (cfg.couplings.containsKey(idTo) && cfg.couplings.get(idTo).contains(idFrom))) {
                    if (feasibleI && feasibleJ) {
                        fitness += Math.abs(cI.x + cI.l / 2 - cJ.x - cJ.l / 2) + Math.abs(cI.y + cI.w / 2 - cJ.y - cJ.w / 2) + Math.abs(cI.z - cJ.z);
                    } else {
                        fitness += MaxWireLength;
                    }
                }
            }
        }
        return fitness;
    }

    public double place(Solution<ComponentVariable> solution, int idx) {
        Component component = solution.getVariables().get(idx).getValue();
        int x = 0, y = 0, z = 0, bestX = -1, bestY = -1, bestZ = -1;
        double currentObj = Double.POSITIVE_INFINITY, bestObj = Double.POSITIVE_INFINITY;
        for (z = component.zMin; z <= component.zMax; ++z) {
            for (x = component.xMin; x <= component.xMax; ++x) {
                for (y = component.yMin; y <= component.yMax; ++y) {
                    component.x = x;
                    component.y = y;
                    component.z = z;
                    if (freeCells[x][y][z] && feasible(solution, idx)) {
                        currentObj = fitnessWire(solution, idx);
                        if (currentObj < bestObj) {
                            bestObj = currentObj;
                            bestX = x;
                            bestY = y;
                            bestZ = z;
                        }
                    }
                }
            }
        }
        component.x = bestX;
        component.y = bestY;
        component.z = bestZ;
        if (bestX < 0 || bestY < 0 || bestZ < 0) {
            return 1.0;
        }
        for (x = component.x; x < component.x + component.l; ++x) {
            for (y = component.y; y < component.y + component.w; ++y) {
                freeCells[x][y][bestZ] = false;
            }
        }
        return 0.0;
    }

    public boolean feasible(Solution<ComponentVariable> solution, int idx) {
        Component cI = solution.getVariables().get(idx).getValue();
        if (cI.x + cI.l > cfg.maxLengthInCells) {
            return false;
        }
        if (cI.y + cI.w > cfg.maxWidthInCells) {
            return false;
        }
        if (cI.z + cI.h > cfg.numLayers) {
            return false;
        }
        Component cJ = null;
        double dx = 0, dy = 0;
        for (int j = 0; j < idx; ++j) {
            cJ = solution.getVariables().get(j).getValue();
            if (cJ.z != cI.z) {
                continue;
            }
            dx = Math.abs(cJ.x + cJ.l / 2.0 - cI.x - cI.l / 2.0);
            dy = Math.abs(cJ.y + cJ.w / 2.0 - cI.y - cI.w / 2.0);
            if (dx < (cJ.l / 2.0 + cI.l / 2.0) && dy < (cJ.w / 2.0 + cI.w / 2.0)) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java -jar FloorplanGeneticAsocWire.jar XmlFilePath TimeInSeconds");
            System.out.println("Where:");
            System.out.println("XmlFilePath: Scenario path file (Absolute or relative)");
            System.out.println("TimeInSeconds: Execution time in seconds");
            return;
        }
        JecoLogger.setup(Level.INFO);
        String xmlFilePath = args[0];
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(new File(xmlFilePath.replaceAll(".xml", "Wire.txt"))));
        } catch (IOException ex) {
            Logger.getLogger(FloorplanGeneticAsocWire.class.getName()).log(Level.SEVERE, null, ex);
        }
        Long timeInMiliSeconds = 1000 * Long.valueOf(args[1]);
        Integer numIndi = 100;
        Integer numGene = 500000;
        FloorplanConfiguration cfg = new FloorplanConfiguration(xmlFilePath);
        FloorplanGeneticAsocWire problem = new FloorplanGeneticAsocWire("TempAware", cfg);
        NSGAII<ComponentVariable> algorithm = new NSGAII<ComponentVariable>("TempAware", problem, numIndi, numGene, new ComponentVariable.ComponentMutation(1.0 / problem.getNumberOfVariables()), new CycleCrossover<ComponentVariable>(), new BinaryTournamentNSGAII<ComponentVariable>());
        logger.info("Initializing ...");
        algorithm.initialize();
        logger.info("Running ...");
        long startTime = System.currentTimeMillis();
        long endTime = startTime;
        Solutions<ComponentVariable> solutions = null;
        Solution<ComponentVariable> bestSolution = null;
        double bestObj = Double.POSITIVE_INFINITY;
        StringBuilder line = new StringBuilder();
        while (endTime - startTime < timeInMiliSeconds) {
            algorithm.step();
            endTime = System.currentTimeMillis();
            bestSolution = null;
            bestObj = Double.POSITIVE_INFINITY;
            solutions = algorithm.getPopulation();
            for (Solution<ComponentVariable> solution : solutions) {
                if (solution.getObjectives().get(0) <= 0.0 && solution.getObjectives().get(1) < bestObj) {
                    bestObj = solution.getObjectives().get(1);
                    bestSolution = solution;
                }
            }
            if (bestSolution != null) {
                try {
                    line = new StringBuilder();
                    line.append((endTime - startTime) / 1000.0).append(" ").append(bestObj);
                    logger.info(line.toString());
                    line.append("\n");
                    writer.write(line.toString());
                    writer.flush();
                } catch (IOException ex) {
                    Logger.getLogger(FloorplanGeneticAsocWire.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        logger.info("Done.");
        try {
            writer.close();
            solutions = algorithm.getCurrentSolution();
            problem.save(solutions, xmlFilePath);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    public void save(Solutions<ComponentVariable> solutions, String xmlFilePath) throws IOException {
        for (Solution<ComponentVariable> solution : solutions) {
            ArrayList<ComponentVariable> variables = solution.getVariables();
            for (int i = 0; i < variables.size(); ++i) {
                Component c = variables.get(i).getValue();
                if (c.id < 0) {
                    continue;
                }
                Component cCfg = cfg.components.get(c.id);
                cCfg.x = c.x;
                cCfg.xMin = c.x;
                cCfg.xMax = c.x;
                cCfg.y = c.y;
                cCfg.yMin = c.y;
                cCfg.yMax = c.y;
                cCfg.z = c.z;
                cCfg.zMin = c.z;
                cCfg.zMax = c.z;
                cCfg.l = c.l;
                cCfg.w = c.w;
                cCfg.h = c.h;
            }
            String newXmlFilePathSuffix = "_" + this.getClass().getSimpleName() + "_" + solution.getObjectives().get(0) + "_" + solution.getObjectives().get(1) + ".xml";
            cfg.xmlFilePath = xmlFilePath.replaceAll(".xml", newXmlFilePathSuffix);
            cfg.save();
        }
    }

    @Override
    public ComponentVariable newVariable() {
        return new ComponentVariable(new Component(0, "0"));
    }
}
