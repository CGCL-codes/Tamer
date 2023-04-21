package mips.model.test;

import model.modeling.message;
import java.awt.Dimension;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import mips.model.ExperimentalFrame;
import mips.model.experiment.MCPEF;
import mips.model.test.util.MyTunableCoordinator;
import mips.model.test.util.TestUnit;
import mips.util.Result;
import view.modeling.ViewableAtomic;
import view.modeling.ViewableComponent;
import view.modeling.ViewableDigraph;

public class MCPEF_Test extends ViewableDigraph implements TestUnit {

    private boolean inErrorState;

    private boolean gotInputs;

    private String propertyFilePath;

    public boolean isInErrorState() {
        return !gotInputs || inErrorState;
    }

    public MCPEF_Test() {
        this(ExperimentalFrame.inputPath + "mipsProcProperties_1.txt");
    }

    public MCPEF_Test(String propertyFilePath) {
        super("MCPEF_Test");
        this.propertyFilePath = propertyFilePath;
        ExperimentalFrame.setPropFilePath(propertyFilePath);
        ExperimentalFrame.setProperties(null);
        ViewableDigraph ut = new MCPEF();
        ViewableAtomic tester = new Tester();
        ut.setBlackBox(true);
        add(ut);
        add(tester);
        addCoupling(ut, "Out", tester, "In");
        initialize();
    }

    public class Tester extends ViewableAtomic {

        private double clock;

        private Map<String, Result> expectedResults;

        public Tester() {
            super("Tester");
            addInport("In");
            expectedResults = new HashMap<String, Result>();
            Result result;
            result = new Result("MIPSProcSimResult");
            result.setCpi(765 * 1.0 / 201);
            result.setCycleCount(765);
            result.setCycleTime(600);
            result.setInstrCount(201);
            expectedResults.put(ExperimentalFrame.inputPath + "mipsProcProperties_1.txt", result);
            result = new Result("MIPSProcSimResult");
            result.setCpi(1.0 * 385 / 101);
            result.setCycleCount(385);
            result.setCycleTime(600);
            result.setInstrCount(101);
            expectedResults.put(ExperimentalFrame.inputPath + "mipsProcProperties_2.txt", result);
            result = new Result("MIPSProcSimResult");
            result.setCpi(1.0 * 15 / 4);
            result.setCycleCount(15);
            result.setCycleTime(600);
            result.setInstrCount(4);
            expectedResults.put(ExperimentalFrame.inputPath + "mipsProcProperties_3.txt", result);
            result = new Result("MIPSProcSimResult");
            result.setCpi(1.0 * 1802 / 500);
            result.setCycleCount(1802);
            result.setCycleTime(600);
            result.setInstrCount(500);
            expectedResults.put(ExperimentalFrame.inputPath + "mipsProcProperties_jump.txt", result);
            result = new Result("MIPSProcSimResult");
            result.setCpi(1.0 * 29 / 7);
            result.setCycleCount(29);
            result.setCycleTime(600);
            result.setInstrCount(7);
            expectedResults.put(ExperimentalFrame.inputPath + "mipsProcProperties_rFormat.txt", result);
        }

        public void initialize() {
            super.initialize();
            clock = 0;
            inErrorState = false;
            gotInputs = false;
            holdIn("passive", INFINITY);
        }

        public void deltext(double e, message x) {
            Continue(e);
            clock = clock + e;
            if (phaseIs("Error")) {
                return;
            }
            Result result;
            Result expectedResult = expectedResults.get(propertyFilePath);
            for (int i = 0; i < x.size(); i++) {
                if (messageOnPort(x, "In", i)) {
                    result = (Result) x.getValOnPort("In", i);
                    System.out.println(result);
                    gotInputs = true;
                    if (!result.equals(expectedResult)) {
                        System.out.println("Error: wrong results");
                        System.out.println("Expected results: ");
                        System.out.println(expectedResult);
                        inErrorState = true;
                        holdIn("Error", INFINITY);
                        return;
                    } else {
                        System.out.println("The results are correct");
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        String[] propertyFiles = { ExperimentalFrame.inputPath + "mipsProcProperties_1.txt", ExperimentalFrame.inputPath + "mipsProcProperties_2.txt", ExperimentalFrame.inputPath + "mipsProcProperties_3.txt", ExperimentalFrame.inputPath + "mipsProcProperties_jump.txt", ExperimentalFrame.inputPath + "mipsProcProperties_rFormat.txt" };
        MyTunableCoordinator c;
        for (int i = 0; i < propertyFiles.length; i++) {
            c = new MyTunableCoordinator(new MCPEF_Test(propertyFiles[i]));
            c.setTimeScale(.0001);
            c.initialize();
            c.simulate(100000);
            try {
                c.join();
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Automatically generated by the SimView program.
     * Do not edit this manually, as such changes will get overwritten.
     */
    public void layoutForSimView() {
        preferredSize = new Dimension(416, 293);
        ((ViewableComponent) withName("Tester")).setPreferredLocation(new Point(177, 78));
        ((ViewableComponent) withName("MCPEF")).setPreferredLocation(new Point(50, 50));
    }
}
