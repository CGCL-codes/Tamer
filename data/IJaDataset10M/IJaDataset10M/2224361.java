package tests;

import genetic.*;
import java.util.ArrayList;
import java.util.Enumeration;
import junit.framework.TestCase;

public class TestGeneratorTree extends TestCase {

    public static int STRUCTURAL = 0x1;

    public static FitnessTrap getFitnessTrap() {
        return new FitnessTrap(getFitnessTrapSettings());
    }

    public static FitnessTrapSettings getFitnessTrapSettings() {
        return new FitnessTrapSettings(0.2, 0.8, new SubtreeCalculatorSettings(), getPerfectTree());
    }

    public static void testInitialTree(PopulationInfo popInfo, GeneticNode tree) {
        testTree(popInfo.getInitialMaxHeigth(), tree);
    }

    public static void testTree(PopulationInfo popInfo, GeneticNode tree) {
        testTree(popInfo.getMaxHeigth(), tree);
    }

    public static ProblemInstance getProblemInstance() {
        ProblemInstance pi = new ProblemInstance("testInstance", getGeneticSettings(), getPerfectPopInfo());
        return pi;
    }

    public static void testTree(int heigth, GeneticNode tree) {
        GeneticNode node;
        assertTrue(tree.getDepth() <= heigth);
        Enumeration en = tree.children();
        while (en.hasMoreElements()) {
            node = (GeneticNode) en.nextElement();
            assertTrue(node.getChildCount() == ((GeneticNodeObj) node.getUserObject()).getAriety());
        }
    }

    public static PopulationInfo getPerfectPopInfo() {
        return getPerfectPopInfo(0);
    }

    public static PopulationInfo getPerfectPopInfo(int ext) {
        ArrayList<FunctionNodeObj> funcs;
        ArrayList<TerminalNodeObj> terminals;
        PopulationInfo popInfo;
        FunctionNodeObj func;
        funcs = new ArrayList<FunctionNodeObj>();
        terminals = new ArrayList<TerminalNodeObj>();
        String names = "AB";
        func = new FunctionNodeObj("A", 3);
        funcs.add(func);
        func = new FunctionNodeObj("B", 2);
        funcs.add(func);
        terminals.add(new TerminalNodeObj("C"));
        if (ext != 0) {
            func = new FunctionNodeObj("X", 3);
            funcs.add(func);
            func = new FunctionNodeObj("Y", 2);
            funcs.add(func);
            terminals.add(new TerminalNodeObj("Z"));
        }
        popInfo = new PopulationInfo(10, 10, funcs, terminals);
        popInfo.setInitialMaxHeigth(5);
        return popInfo;
    }

    public static Population getPerfectPop() {
        PopulationInfo popInfo = getPerfectPopInfo();
        Population pop = new Population(popInfo);
        for (int i = 0; i < popInfo.getMaxSize(); i++) {
            pop.addInd(getPerfectTree());
        }
        return pop;
    }

    public static Population getPerfectDiffPop() {
        PopulationInfo popInfo = getPerfectPopInfo();
        Population pop = new Population(popInfo);
        for (int i = popInfo.getMaxSize(); i > 0; i--) {
            pop.addInd(getPerfectDiffTree());
        }
        return pop;
    }

    public static Population getNormalPop() {
        PopulationInfo popInfo = getPerfectPopInfo();
        Population pop = new Population(popInfo);
        pop.addInd(getPerfectTree());
        pop.addInd(getCCCATree());
        pop.addInd(getPerfectDiffLeafTree());
        pop.addInd(getCTree());
        pop.addInd(getPerfectDiffTree());
        pop.addInd(getPerfectTree());
        pop.addInd(getCCCATree());
        pop.addInd(getCCBTree());
        pop.addInd(getCTree());
        pop.addInd(getPerfectDiffTree());
        return pop;
    }

    public static GeneticNode getPerfectDiffTree() {
        GeneticNode ptree = getPerfectTree(0);
        ptree.remove(0);
        ptree.insert(getCTree(), 0);
        return ptree;
    }

    public static GeneticNode getPerfectDiffLeafTree() {
        GeneticNode ptree = getPerfectTree(0);
        GeneticNode child = (GeneticNode) ptree.getChildAt(1);
        child.remove(0);
        GeneticNode newCh = getCCBTree();
        child.insert(newCh, 0);
        System.out.println(ptree.getTreeKey());
        return ptree;
    }

    public static GeneticNode getCCBTree() {
        GeneticNode nodeC;
        GeneticNode nodeB;
        GeneticNodeObj obj;
        nodeB = new GeneticNode();
        obj = new FunctionNodeObj("B", 2);
        nodeB.setUserObject(obj);
        for (int b = 0; b < 2; b++) {
            nodeC = new GeneticNode();
            obj = new TerminalNodeObj("C");
            nodeC.setUserObject(obj);
            nodeB.add(nodeC);
        }
        return nodeB;
    }

    public static GeneticNode getCTree() {
        GeneticNode nodeC;
        GeneticNodeObj obj;
        nodeC = new GeneticNode();
        obj = new TerminalNodeObj("C");
        nodeC.setUserObject(obj);
        return nodeC;
    }

    public static GeneticNode getCCCACCCABTree() {
        GeneticNode tree;
        GeneticNode nodeB;
        GeneticNode nodeC;
        GeneticNodeObj obj;
        nodeB = new GeneticNode();
        obj = new FunctionNodeObj("B", 2);
        nodeB.setUserObject(obj);
        nodeB.add(getCCCATree());
        nodeB.add(getCCCATree());
        tree = nodeB;
        return tree;
    }

    public static GeneticNode getCCCATree() {
        GeneticNode tree;
        GeneticNode nodeA;
        GeneticNode nodeB1;
        GeneticNode nodeC;
        GeneticNodeObj obj;
        nodeA = new GeneticNode();
        obj = new FunctionNodeObj("A", 3);
        nodeA.setUserObject(obj);
        tree = nodeA;
        for (int i = 0; i < 3; i++) {
            nodeC = new GeneticNode();
            obj = new TerminalNodeObj("C");
            nodeC.setUserObject(obj);
            nodeA.add(nodeC);
        }
        return tree;
    }

    public static GeneticNode getPerfectTree() {
        return getPerfectTree(0);
    }

    public static GeneticNode getPerfectTree(int ext) {
        GeneticNode tree;
        GeneticNode nodeA;
        GeneticNode nodeB1;
        GeneticNode nodeC;
        GeneticNodeObj obj;
        String names[];
        if (ext != 0) {
            names = new String[3];
            names[0] = "X";
            names[1] = "Y";
            names[2] = "Z";
        } else {
            names = new String[3];
            names[0] = "A";
            names[1] = "B";
            names[2] = "C";
        }
        nodeA = new GeneticNode();
        obj = new FunctionNodeObj(names[0], 3);
        nodeA.setUserObject(obj);
        tree = nodeA;
        for (int a = 0; a < 3; a++) {
            nodeB1 = new GeneticNode();
            obj = new FunctionNodeObj(names[1], 2);
            nodeB1.setUserObject(obj);
            nodeA.add(nodeB1);
            for (int b = 0; b < 2; b++) {
                nodeC = new GeneticNode();
                obj = new TerminalNodeObj(names[2]);
                nodeC.setUserObject(obj);
                nodeB1.add(nodeC);
            }
        }
        return tree;
    }

    public static GeneticSettings getGeneticSettings() {
        GeneticSettings gs;
        gs = new GeneticSettings(0.9, 100, getInitializerSettings(), getSelectorSettings(), getFitnessTrapSettings(), getBagSub(), true, true);
        return gs;
    }

    public static GeneticSettings getGeneticSettingsFull() {
        GeneticSettings gs;
        gs = new GeneticSettings(0.9, 100, getInitializerSettings(), getSelectorSettings(), getFitnessTrapSettings(), getBagFull(), true, true);
        return gs;
    }

    private static DistanceCalculatorsBagSettings getBagFull() {
        DistanceCalculatorsBagSettings bag = new DistanceCalculatorsBagSettings();
        bag.add(new FullSubtreeCalculatorSettings());
        return bag;
    }

    private static int getInitializerSettings() {
        return Initializer.RHH;
    }

    private static SelectorSettings getSelectorSettings() {
        return new TournamentSelectorSettings(5);
    }

    public static GeneticSettings getGeneticSettings(int structural2) {
        GeneticSettings gs = getGeneticSettings();
        gs.setCalculatorBagSettings(getBagStruct());
        return gs;
    }

    private static DistanceCalculatorsBagSettings getBagStruct() {
        DistanceCalculatorsBagSettings bag = new DistanceCalculatorsBagSettings();
        bag.add(new StructuralDistanceSettings());
        return bag;
    }

    private static DistanceCalculatorsBagSettings getBagSub() {
        DistanceCalculatorsBagSettings bag = new DistanceCalculatorsBagSettings();
        bag.add(new SubtreeCalculatorSettings());
        return bag;
    }
}
