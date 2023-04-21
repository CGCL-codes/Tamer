package mips.model.test;

import model.modeling.content;
import model.modeling.message;
import java.awt.Dimension;
import java.awt.Point;
import java.math.BigInteger;
import junit.framework.Assert;
import mips.model.ALU;
import mips.model.ExperimentalFrame;
import mips.model.test.util.MyTunableCoordinator;
import mips.model.test.util.TestUnit;
import mips.util.Signal;
import view.modeling.ViewableAtomic;
import view.modeling.ViewableComponent;
import view.modeling.ViewableDigraph;

public class ALU_Test extends ViewableDigraph implements TestUnit {

    private boolean inErrorState;

    public boolean isInErrorState() {
        return inErrorState;
    }

    public ALU_Test() {
        super("ALU_Test");
        ExperimentalFrame.setAlwaysUpdateOutput(true);
        ViewableAtomic alu = new ALU("ALU");
        int alu_prop = ExperimentalFrame.getIntProperty("ALU");
        ViewableAtomic tester = new Tester(alu_prop + 50);
        add(alu);
        add(tester);
        initialize();
        addCoupling(alu, "Result", tester, "Result");
        addCoupling(alu, "Zero", tester, "Zero");
        addCoupling(alu, "Overflow", tester, "Overflow");
        addCoupling(tester, "a", alu, "a");
        addCoupling(tester, "b", alu, "b");
        addCoupling(tester, "ALUOp", alu, "ALUOp");
    }

    public class Tester extends ViewableAtomic {

        private double clock;

        private int interval;

        private int[] as = { 0, -1, 1, -2147483648, -2147483648, 2147483647 };

        private int[] bs = { -2147483648, 1, 0, 1, -1, 2 };

        private String[] aluOPs = { "0000", "0001", "0010", "0110", "0111", "1100" };

        private int[][] results = { { 0, 1, 0, 0, -2147483648, 2 }, { -2147483648, -1, 1, -2147483647, -1, 2147483647 }, { -2147483648, 0, 1, -2147483647, 2147483647, -2147483647 }, { -2147483648, -2, 1, 2147483647, -2147483647, 2147483645 }, { 0, 1, 0, 1, 1, 0 }, { 2147483647, 0, -2, 2147483646, 0, -2147483648 } };

        private int[][] zeros = { { 1, 0, 1, 1, 0, 0 }, { 0, 0, 0, 0, 0, 0 }, { 0, 1, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0 }, { 0, 1, 0, 0, 1, 0 } };

        private int[][] overflows = { { 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 1, 1 }, { 1, 0, 0, 1, 0, 0 }, { 1, 0, 0, 1, 0, 0 }, { 0, 0, 0, 0, 0, 0 } };

        int index1, index2;

        public Tester(int interval) {
            super("Tester");
            this.interval = interval;
            addInport("Result");
            addInport("Zero");
            addInport("Overflow");
            addOutport("a");
            addOutport("b");
            addOutport("ALUOp");
        }

        public void initialize() {
            super.initialize();
            clock = 0;
            index1 = 0;
            index2 = 0;
            inErrorState = false;
            holdIn("active", 0);
        }

        public void deltext(double e, message x) {
            Continue(e);
            clock = clock + e;
            if (phaseIs("Error")) {
                return;
            }
            Signal val;
            String s;
            int result;
            boolean gotInput = false;
            for (int i = 0; i < x.size(); i++) {
                if (messageOnPort(x, "Result", i)) {
                    val = (Signal) x.getValOnPort("Result", i);
                    s = Signal.decryptSignal(val.getValue());
                    result = Signal.binStrToInt(s);
                    if (result != results[index1][index2]) {
                        System.out.printf("Error: the %d result of %s operation is incorrect. %n", index2, aluOPs[index1]);
                        System.out.printf("Expected: %d, actual value %d. %n%n", results[index1][index2], result);
                        inErrorState = true;
                        holdIn("Error", INFINITY);
                        return;
                    }
                    gotInput = true;
                } else if (messageOnPort(x, "Zero", i)) {
                    val = (Signal) x.getValOnPort("Zero", i);
                    s = Signal.decryptSignal(val.getName());
                    result = Signal.binStrToInt(s);
                    if (result != zeros[index1][index2]) {
                        System.out.printf("Error: the %d result (zero) of %s operation is incorrect. %n", index2, aluOPs[index1]);
                        System.out.printf("Expected: %d, actual value %d. %n%n", zeros[index1][index2], result);
                        inErrorState = true;
                        holdIn("Error", INFINITY);
                        return;
                    }
                    gotInput = true;
                } else if (messageOnPort(x, "Overflow", i)) {
                    val = (Signal) x.getValOnPort("Overflow", i);
                    s = Signal.decryptSignal(val.getName());
                    result = Signal.binStrToInt(s);
                    if (result != overflows[index1][index2]) {
                        System.out.printf("Error: the %d result(overflow) of %s operation is incorrect. %n", index2, aluOPs[index1]);
                        System.out.printf("Expected: %d, actual value %d. %n%n", overflows[index1][index2], result);
                        inErrorState = true;
                        holdIn("Error", INFINITY);
                        return;
                    }
                    gotInput = true;
                }
            }
            if (gotInput) {
                index2++;
                if (index2 >= results[1].length) {
                    index1++;
                    index2 = 0;
                }
            }
        }

        public void deltint() {
            clock = clock + sigma;
            if (phaseIs("active")) {
                if (index1 < aluOPs.length) {
                    holdIn("active", interval);
                } else {
                    holdIn("passive", INFINITY);
                }
            }
        }

        public message out() {
            message m = new message();
            content con;
            String newVal;
            if (phaseIs("active") & (index2 < as.length) & (index1 < aluOPs.length)) {
                newVal = Signal.numToBinStr(BigInteger.valueOf(as[index2]), 32);
                con = makeContent("a", new Signal(Signal.toBinSignal(newVal)));
                m.add(con);
                newVal = Signal.numToBinStr(BigInteger.valueOf(bs[index2]), 32);
                con = makeContent("b", new Signal(Signal.toBinSignal(newVal)));
                m.add(con);
                newVal = aluOPs[index1];
                con = makeContent("ALUOp", new Signal(Signal.toBinSignal(newVal)));
                m.add(con);
            }
            return m;
        }
    }

    public static void main(String[] args) {
        ALU_Test testUnit = new ALU_Test();
        MyTunableCoordinator c = new MyTunableCoordinator(testUnit);
        c.setTimeScale(.0001);
        c.initialize();
        c.simulate(100000);
        Assert.assertFalse(testUnit.isInErrorState());
    }

    /**
     * Automatically generated by the SimView program.
     * Do not edit this manually, as such changes will get overwritten.
     */
    public void layoutForSimView() {
        preferredSize = new Dimension(416, 293);
        ((ViewableComponent) withName("Tester")).setPreferredLocation(new Point(65, 151));
        ((ViewableComponent) withName("ALU")).setPreferredLocation(new Point(65, 50));
    }
}
