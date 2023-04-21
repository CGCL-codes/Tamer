package jaga.testing.deploy;

import jaga.control.*;
import jaga.deploy.*;
import jaga.evolve.*;
import jaga.experiment.*;
import jaga.*;
import jaga.pj.circuits.*;
import jaga.pj.circuits.experiment.*;
import jaga.pj.circuits.fpgaft.*;
import java.io.*;

/**
 *
 * @author  mmg20
 */
public class TestFPGALUT {

    FPGALUTAbsoluteMapping mapping;

    Experiment experiment;

    static SimulatorFaultyCircuit circuit;

    static int inputSampleSeparation;

    static int startAt = 0;

    static int eSize = 6;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TestFPGALUT tfl = new TestFPGALUT();
        tfl.testClockRead();
    }

    /** Creates a new instance of TestFPGALUT */
    public TestFPGALUT() {
    }

    void testClockRead() {
        SimulatorFaultyCircuit circuit;
        double ts = 0;
        BooleanFunction xor = new XOrFunction();
        Experiment xorExp = new ArbitraryFunctionExperiment(xor, ts);
        experiment = new AddClockExperiment(xorExp);
        int nrIns = experiment.getNumOfInputs();
        int nrOuts = experiment.getNumOfOutputs();
        int LUTIns = 2;
        int BPV = 2;
        mapping = new FPGALUTAbsoluteMapping(nrIns, nrOuts, BPV, LUTIns, new ConstantDelayModel(0));
        String Q0 = "000";
        String LUT0 = "0011";
        String LUTIns0 = "011001";
        String LIns0 = "001";
        Genotype genotype = new Genotype(Q0 + LUT0 + LUTIns0 + LIns0);
        circuit = new SimulatorFaultyCircuit(mapping);
        circuit.reconfigure(genotype);
        SampleData[] ins = experiment.generateInput(2);
        SampleData[] outs = circuit.run(ins);
        System.out.println(jaga.ESLib.sampleDatasToString(ins, outs));
    }

    static double testSeqBIST(int printWhich) throws IOException {
        boolean sequentialCircuit = true;
        final int EDL4 = 4;
        int exp = EDL4;
        boolean delayNoise = true;
        boolean inputsNoise = true;
        boolean stateNoise = true;
        int BITS_PER_VARIABLE = -1;
        switch(exp) {
            case EDL4:
                BITS_PER_VARIABLE = 2;
                break;
        }
        final int LUT_INPUTS = 2;
        final int SIMULATOR_GATE_DELAY = 1;
        final double T_SETUP = 0.45;
        final int TEST_LENGTH = 50;
        int DUMP_POP_EVERY = 60;
        Experiment experiment = null;
        switch(exp) {
            case EDL4:
                experiment = new EdgeDLExperiment(T_SETUP);
                break;
        }
        ElementDelayModel delayModel;
        if (delayNoise) {
            delayModel = new GaussianDelayModel(0.5, 0.3);
        } else {
            delayModel = new ConstantDelayModel(SIMULATOR_GATE_DELAY);
        }
        CircuitMapping circuitMapping;
        circuitMapping = new FPGALUTAbsoluteMapping(experiment.getNumOfInputs(), experiment.getNumOfOutputs() + 1, BITS_PER_VARIABLE, LUT_INPUTS, delayModel);
        int DQTol = 20;
        int eSize = 15;
        circuit = new SimulatorFaultyCircuitOpt(circuitMapping, DQTol, eSize);
        SimulatorDeployment deployment = new SimulatorDeployment(circuit);
        inputSampleSeparation = 5;
        Genotype ind = new Genotype("10000001101001010110000000000011");
        SimulatorLogicElement[][] ioe = circuitMapping.map(ind);
        SingleFaultModel faultModel = new SingleFullFaultModel(4);
        if (!inputsNoise && experiment instanceof ConfigurableRandomInputExperiment) {
            ((ConfigurableRandomInputExperiment) experiment).set(new Long(1));
        }
        deployment.program(ind);
        SampleData[] input = experiment.generateInput(inputSampleSeparation);
        SampleData[] invIn = new SampleData[2];
        invIn[0] = input[1];
        invIn[1] = input[0];
        if (!stateNoise) {
            circuit.reset();
        } else {
            circuit.randomReset();
        }
        SampleData[] noFQWithE = deployment.run(invIn);
        int nrOuts = noFQWithE.length - 1;
        SampleData[] noFQNoE = ESLib.getLines(noFQWithE, 0, nrOuts);
        double f_e = experiment.getFitness(input, noFQNoE);
        double f_b = 0;
        boolean mainTaskOK = true;
        if (mainTaskOK && !getE(noFQWithE)) {
            boolean[] used = getUsed(nrOuts);
            int nrFaults = 1;
            int diagFaults = 1;
            SampleData[] outputWithE;
            int[] rv2 = { 0 };
            faultModel.reset();
            while (faultModel.hasMoreElements()) {
                java.awt.Point fPosVal = (java.awt.Point) faultModel.nextElement();
                if (printWhich > 2) {
                    System.out.println(" From Fault Model " + fPosVal);
                }
                if (used[fPosVal.x]) {
                    circuit.setFault(fPosVal.x, fPosVal.y);
                    if (printWhich > 2) {
                        System.out.println(" Doing " + fPosVal);
                    }
                    if (!stateNoise) {
                        circuit.reset();
                    } else {
                        circuit.randomReset();
                    }
                    outputWithE = ((SimulatorFaultyCircuitOpt) circuit).run(invIn, noFQWithE, rv2);
                    if (printWhich > 2) {
                        System.out.println("" + es.ESLib.sampleDatasToString(input, outputWithE));
                    }
                    switch(rv2[0]) {
                        case SimulatorFaultyCircuitOpt.LINE_HIGH:
                            {
                            }
                            ;
                        case SimulatorFaultyCircuitOpt.NORMAL:
                            {
                                diagFaults++;
                            }
                        case SimulatorFaultyCircuitOpt.OUTPUT_DIFFERENT:
                            {
                            }
                            ;
                    }
                    nrFaults++;
                    circuit.setFault(fPosVal.x, FTLib.NOFAULT);
                }
            }
            f_b = 1d / ((nrFaults - diagFaults) / 25d + 1d);
        }
        ind.setFitness(f_e);
        ind.setProperty(0, new Double(f_b));
        ind.setProperty(1, new Double(f_b));
        int nrUsed = CircuitsLib.addConnectedGates(circuit).size();
        System.out.println("f_e=" + f_e + ", f_b=" + f_b + ", used=" + nrUsed);
        return f_b;
    }

    protected static boolean[] getUsed(int outs) {
        SimulatorLogicElement[] els = circuit.getElements();
        boolean[] rv = new boolean[els.length];
        for (int ol = 0; ol < outs; ol++) {
            addConnectedGates(rv, els[ol], els);
        }
        return rv;
    }

    protected static void addConnectedGates(boolean[] added, SimulatorLogicElement el, SimulatorLogicElement[] els) {
        int elinels = jaga.ESLib.indexOf(el, els);
        if (elinels >= 0 && !added[elinels]) {
            added[elinels] = true;
            SimulatorLogicElement[] ins = el.getInputs();
            if (ins != null) {
                for (int il = 0; il < ins.length; il++) {
                    addConnectedGates(added, ins[il], els);
                }
            }
        }
    }

    protected static boolean getE(SampleData[] output) {
        SampleData E = output[output.length - 1];
        int outLen = E.length();
        int inputCycles = outLen / inputSampleSeparation;
        for (int idl = 1; idl < inputCycles; idl++) {
            int conc = 0;
            for (int odl = startAt; odl < inputSampleSeparation; odl++) {
                if (E.get(idl * inputSampleSeparation + odl)) {
                    conc++;
                    if (conc == eSize) {
                        return true;
                    }
                } else {
                    conc = 0;
                }
            }
        }
        return false;
    }
}
