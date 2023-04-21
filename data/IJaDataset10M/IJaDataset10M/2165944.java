package jaga.testing;

import jaga.control.*;
import jaga.deploy.*;
import jaga.evolve.*;
import jaga.experiment.*;
import jaga.*;
import jaga.pj.circuits.*;
import jaga.pj.circuits.experiment.*;
import jaga.pj.circuits.fpgaft.*;
import jaga.pj.circuits.control.*;
import jaga.external.*;
import jaga.pj.gral.*;
import debug.DebugLib;
import java.util.Vector;
import java.io.*;
import java.util.Hashtable;
import java.awt.Point;

/**
 *
 * @author  mmg20
 */
public class InPlaceTestTSCInd {

    InteractionModel im;

    SimulatorFaultyCircuit circuit;

    Genotype[] inds = new Genotype[1];

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            InPlaceTestTSCInd t = new InPlaceTestTSCInd();
            t.wim();
            t.runAndPrint();
            System.out.println("Circ " + CircuitsLib.circuitToString(t.circuit.getInOutEls()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void runAndPrint() {
        double[] fit = im.evaluate(inds);
        System.out.println("" + fit[0] + " for ind " + inds[0]);
    }

    void rd53() throws IOException {
        createTSCCombIM("/home/mmg20/eh/benchmarks/rd53.blif", "/home/mmg20/eh/benchmarks/rd53.sout", 2, true, false);
        inds[0] = new Genotype("00420gISRPUiDNHH_G`fqQlHo2eMZr621[UDlfF0S2o6j`~lVqRnZhoCLlQ5V_K60vDtosRvvYGQ5qr[~5qPeNWLMFTMu^tRiHCbQ__1vhLlkm]CsPPR2Nkpnrp^Y_Ac5LR7n9VOmDpc4aIKnvstosRFf7nZhfns_70KElgQPS6unP]veSveQQSK0^OHCi4aL]OkidO16[And9WSv1QAH6LlHfTRPLV]3p~uZ9Rt_4jeKGU6^Uh^nZiJLV7tvA38cHCiUssHi7[I1eQa_DNP_R]EHo^`vXkVtSP`3]93LlRq80P9vpYNkpJEnNaJLvWhO[0HOi~jS491_PVPlYPvPLQiXa5qr[T2DXvVhgUE[CmhfrUR[g4Cd]e0tbslA6_H`~hr5BPLO^j15SZZVgEZ_EDlR_5cDh^b~fclT2i77d`F3vT4dXdBW[7il_v]`ptoLM^OhHvVhjpnR]Ya3Fh2nvTM0nYktv0q7TZmjtPBJm7Y~nbeHffpa0hXvpD^o0Ikt]OhDaAUUkL80YlaVDUg0quvoT_`VsD_^ap~UsU2JNvfuMKYOMk^LWEPEq7ZhMLlH5OTQqHW6sl7o0Rs8N6Z6VrvPTD[p4XlOmRt5]pFgMrNO3X9vXkOns~jAprdcGg5PuX^8_oN[LpRNNj`_PE[OHO77`LYnr7v5HEePa~JVsLP~2cQgL^emnQF0V[d7QAN[h1_Ob5h~7aJL_CUS1UhjYm78d^uvhjfeQJ1mMr`vq6fjsSDi9VJ~EISPNVavuo]Za74m15X`YfmXiBN7~po93DO567d8Srr]J06dpGVcd7tv1js5truOUOHXcD]fIQU3[tN1fns~W^0", 5060, 6);
    }

    void wim() throws IOException {
        createTSCCombIM("/home/mmg20/eh/benchmarks/wim.blif", "/home/mmg20/eh/benchmarks/wim.sout", 2, true, false);
        inds[0] = new Genotype("00GGCO531P3ZD3~QZ[gX1fXTVjWJ3OeNFGJWjd~0R~ok4vjIVpVVjg`OqXRF2q2haGf_Z2[vVX2790fW~cKnVpPNcYSpCGTXFHTv`0FlOHq~nSW`Gh^dPHsQ`9bZ1SPBMsDOHrupJ~]rIVsto2~cg^e3OeG^YrFjbX05~fH6Js1gA86I68c1gCV2X1fY0sa_6KX`XBgrGi3WfpRCCHZ11f[e2L~WQ0lHGTbHCRvf`6pBmkp3LNkob`F`OP920K_]onVjXg3IgHEvXjJKto1GTcBuJbDJVo1OObZq4r][TvK2Z3X019vsvQCto0XS`]oScMPMv[s0a7G00q2FRA~chV~pCGS2Z0q_`0FgXSaGfevHW^WCvhRHVZgS]WWUik_", 2295, 6);
    }

    void c17() throws IOException {
        createTSCCombIM("/home/mmg20/eh/benchmarks/C17.blif", "/home/mmg20/eh/benchmarks/C17.sout", 2, true, false);
        inds[0] = new Genotype("041e_Nap[1evneTri]3Fb^i^UeFeTjoCu1Y`pCEmbfAuouhZbgrvUmJdiPoq~Bf4jo0", 398, 6);
    }

    void cm138a() throws IOException {
        createTSCCombIM("/home/mmg20/eh/benchmarks/cm138a.blif", "/home/mmg20/eh/benchmarks/cm138a.sout", 2, true, false);
        inds[0] = new Genotype("012345671pScq33^DESotik7CGSl1jk61pcbhuu^vunvqvv^t9rvrVIM2fNi`XHrQsvfPuufoGPEpSZnrqJ[Yic]bfSWiQi`0nd8Pue~M~Ve2dA]1pVb`ufm2WVXtNLMJeSkr11^YkSd0m75VL]~X_[nYkOAtWl63KORC", 988, 6);
    }

    void b1() throws IOException {
        createTSCCombIM("/home/mmg20/eh/benchmarks/b1.blif", "/home/mmg20/eh/benchmarks/b1.sout", 2, true, false);
        inds[0] = new Genotype("01232kFntfv_91aS2Gd0NMRsC1_ciZ^rlA_66gh]DFO]Bln5G1_ckQjdlv^_jZ[9~76kJDHrD_ImdQHkFQL0NNc8laKMvu[tA2aY0^gH`v^]6g44~YI]vun^fQL2`MZT25YiUeO[_fpsrTQ4dX`sMXBuVEnP2PgBtXc6GM~^S", 1012, 6);
    }

    void decod() throws IOException {
        createTSCACombIM("/home/mmg20/eh/benchmarks/decod.blif", "/home/mmg20/eh/benchmarks/decod.sout", 2, true, false);
        inds[0] = new Genotype("00GGC8531h0[KB63HgtM44]M4]N4]K4]L4cO4eO4_O4aO4cP4eP4_P4aP4cQ4eQ4_Q4aQPM47vrJvr7sHJrsZrs7rsBrsJsH7sIJsIQ3r~GaJuh[677Pd4_SiC]Brsh7J4aQ611~uf[mT[674diQ3rOAl8465_f5sF[DVJsI4aQiHjNrsRVn[oCFlh]`YP2F4sAFkS^KQ5qS4f3~0~~3_iCf~D]i`u4OMi`f5dSO3U5^dJrs5C3IsH~tY4di7gScWIDSUi`V7si[IB6vlDSQ4OM4aP5FC4TuGQ~[cQ7kQDSQ[IBO4F^LW[IBDbOCq44cO4aOiJV[0AJvQ[S8g8EgRaN^0[Gf7Rsh3DDSUcSviWq2^T4g^7PdcSv]Zf4iQ4_QFlAO4S", 2340, 6);
    }

    void m1() throws IOException {
        createTSCCombIM("/home/mmg20/eh/benchmarks/m1.blif", "/home/mmg20/eh/benchmarks/m1.sout", 2, false, false);
    }

    void cm82a() throws IOException {
        createTSCCombIM("/home/mmg20/eh/benchmarks/cm82a.blif", "/home/mmg20/eh/benchmarks/cm82a.sout", 2, true, false);
        inds[0] = new Genotype("01234QVuIKMh~[o9ae9IfEVkqXIhEPnLWh`2oZu~aCP1s]QuNkmrsc3s_K9Hm0q6mSMEm7nt9XR0uD[H~0[u0PRgcJ^2oPJUCH`VCJpTf22LGRrmTEY98B4p9XM8mOVOq12h0A51ev6aoB4ImIovtRfh0b]OQKOLZdW", 974, 6);
    }

    void cm42a() throws IOException {
        createTSCCombIM("/home/mmg20/eh/benchmarks/cm42a.blif", "/home/mmg20/eh/benchmarks/cm42a.sout", 2, true, false);
        inds[0] = new Genotype("0123456789jhcoejirGBd4ejZBFCd0ikJBvDrskkkotsBngKf7vunvqvvevuZrlcsM0tXbkKf9CtQQmIHXasZrmHEQNZ]YUIHbGC[r6JcTqL[q26fRHBQ]tbY6S3Zrmj_`_^d4eIH]s1RViGXMSO]g2JT]s1[r6FfOut[Ta~j6nD", 1032, 6);
    }

    public void createTSCCombIM(String blifFileName, String sisOutputFileName, int LUTInputs, boolean spaceForVoter, boolean locking) throws IOException {
        final int INPUT_SAMPLE_SEP = 1;
        final int SAMPLE_WINDOW_START = 0;
        final int DUMP_POP_EVERY = 500;
        final int E_LINES = 2;
        final int NO_EVALS = 1;
        final int E_SIZE = INPUT_SAMPLE_SEP - SAMPLE_WINDOW_START;
        final int E_START_AT = SAMPLE_WINDOW_START;
        FitnessFunction corrFF = new CorrelationFitnessFunction();
        FitnessFunction tSetupFF = new SampleWindowFitnessFunction(corrFF, SAMPLE_WINDOW_START);
        TestPatternGenerator tpg = new CompleteShuffledTPG();
        CombinationalBLIFExperiment experiment = new CombinationalBLIFExperiment(blifFileName, tSetupFF, tpg);
        final boolean FPGA = false;
        SisOutputReader sor = new SisOutputReader(new File(sisOutputFileName), E_LINES, LUTInputs, FPGA, spaceForVoter);
        FullOrderGenotype seed = new FullOrderGenotype(sor.getVassilevGenotype());
        int bitsPerVar = sor.getBitsPerVar();
        int usedEls = sor.getTotalEls();
        int nrAddUnits = (1 << bitsPerVar) - experiment.getNumOfInputs();
        ElementDelayModel delayModel = new ConstantDelayModel(0);
        CircuitMapping inMapping = new LUTAbsoluteMapping(experiment.getNumOfInputs(), experiment.getNumOfOutputs() + E_LINES, bitsPerVar, LUTInputs, delayModel);
        CircuitMapping circuitMapping = new VassilevMapping(inMapping, experiment.getNumOfOutputs() + E_LINES, bitsPerVar);
        circuitMapping = new FaultyFeedForwardOptimizedMapping(circuitMapping);
        circuit = new SimulatorFaultyCircuitAsynchronous(circuitMapping);
        SimulatorDeployment deployment = new SimulatorDeployment(circuit);
        int faultTypes = 2;
        SingleFaultModel faultModel = new SingleUsedFaultModel(faultTypes, circuit);
        final int POP_SIZE = 32;
        final int NUM_OF_ELITES = 2;
        int lutSize = 1 << LUTInputs;
        int blockSize = lutSize + LUTInputs * bitsPerVar;
        int qDefSize = (experiment.getNumOfOutputs() + E_LINES) * bitsPerVar;
        int genotypeLength = qDefSize + nrAddUnits * blockSize;
        Genotype[] seeds = new Genotype[POP_SIZE];
        for (int pl = 0; pl < POP_SIZE; pl++) {
            seeds[pl] = (FullOrderGenotype) seed.clone();
            for (int bl = qDefSize + usedEls * blockSize; bl < seeds[pl].length(); bl++) if (Math.random() < 0.5) seeds[pl].set(bl);
        }
        int fixedAlignments = locking ? usedEls : 0;
        int minMutationRate = genotypeLength / 1000 + 1;
        int maxMutationRate = genotypeLength / 50 + 1;
        int SAGAFitnessIndex = 1;
        int howManyBunches = 1;
        GeneticOperator m = new SAGAMutator(minMutationRate, maxMutationRate, SAGAFitnessIndex, fixedAlignments * blockSize, -1);
        GeneticOperator spxo = new SinglePointXOver();
        GeneticOperator bmin0 = new BunchMutator(bitsPerVar, howManyBunches, blockSize, lutSize, fixedAlignments, qDefSize);
        GeneticOperator bmin1 = new BunchMutator(bitsPerVar, howManyBunches, blockSize, lutSize + bitsPerVar, fixedAlignments, qDefSize);
        GeneticOperator bmin2 = new BunchMutator(bitsPerVar, howManyBunches, blockSize, lutSize + bitsPerVar * 2, fixedAlignments, qDefSize);
        GeneticOperator bmin3 = new BunchMutator(bitsPerVar, howManyBunches, blockSize, lutSize + bitsPerVar * 3, fixedAlignments, qDefSize);
        GeneticOperator bc = new BlockCopy(blockSize, blockSize, 0, fixedAlignments, qDefSize);
        GeneticOperator[] geneticOps = { m, spxo, bmin0, bmin1, bmin2, bmin3, bc };
        double[] opsProbs = { 0.2, 0.3, 0.1, 0.1, 0.1, 0.1, 0.1 };
        if (LUTInputs == 2) {
            geneticOps[4] = bmin0;
            geneticOps[5] = bmin1;
        }
        Selector selector = new RankSelector();
        Evolver evolver = new StandardEvolver(POP_SIZE, genotypeLength, geneticOps, opsProbs, selector, NUM_OF_ELITES, seeds);
        PopulationLogReader.fullOrderGenotypes = true;
        int[] numProps = { 2 };
        double maxSize = nrAddUnits;
        CircuitPainterObject painter = new CircuitPainterObject(new CircuitPainter(), new LUTAbsoluteMapping(experiment.getNumOfInputs(), experiment.getNumOfOutputs() + E_LINES, bitsPerVar, LUTInputs, new ConstantDelayModel(0)));
        TSCCombinationalIM inIm = new TSCCombinationalIM(evolver, deployment, circuit, experiment, faultModel, E_SIZE, E_START_AT, INPUT_SAMPLE_SEP, painter);
        InteractionModel noisyIM = new NoisyPIM(inIm, deployment, experiment, numProps, NO_EVALS);
        im = new CircuitParsimonyPIM(noisyIM, circuit, maxSize);
    }

    protected void createTSCACombIM(String blifFileName, String sisOutputFileName, int LUTInputs, boolean spaceForVoter, boolean locking) throws IOException {
        final int INPUT_SAMPLE_SEP = 1;
        final int SAMPLE_WINDOW_START = 0;
        final int DUMP_POP_EVERY = 500;
        final int E_LINES = 2;
        final int NO_EVALS = 1;
        final int E_SIZE = INPUT_SAMPLE_SEP - SAMPLE_WINDOW_START;
        final int E_START_AT = SAMPLE_WINDOW_START;
        FitnessFunction corrFF = new CorrelationFitnessFunction();
        FitnessFunction tSetupFF = new SampleWindowFitnessFunction(corrFF, SAMPLE_WINDOW_START);
        TestPatternGenerator tpg = new CompleteShuffledTPG();
        CombinationalBLIFExperiment experiment = new CombinationalBLIFExperiment(blifFileName, tSetupFF, tpg);
        final boolean FPGA = false;
        SisOutputReader sor = new SisOutputReader(new File(sisOutputFileName), E_LINES, LUTInputs, FPGA, spaceForVoter);
        FullOrderGenotype seed = new FullOrderGenotype(sor.getVassilevGenotype());
        int bitsPerVar = sor.getBitsPerVar();
        int usedEls = sor.getTotalEls();
        int nrAddUnits = (1 << bitsPerVar) - experiment.getNumOfInputs();
        int lutSize = 1 << LUTInputs;
        int blockSize = lutSize + LUTInputs * bitsPerVar;
        int qDefSize = (experiment.getNumOfOutputs() + E_LINES) * bitsPerVar;
        int genotypeLength = qDefSize + nrAddUnits * blockSize;
        ElementDelayModel delayModel = new ConstantDelayModel(0);
        CircuitMapping inMapping = new LUTAbsoluteMapping(experiment.getNumOfInputs(), experiment.getNumOfOutputs() + E_LINES, bitsPerVar, LUTInputs, delayModel);
        CircuitMapping circuitMapping = new VassilevMapping(inMapping, experiment.getNumOfOutputs() + E_LINES, bitsPerVar);
        circuitMapping = new FaultyFeedForwardOptimizedMapping(circuitMapping);
        circuit = new SimulatorFaultyCircuitAsynchronous(circuitMapping);
        SimulatorDeployment deployment = new SimulatorDeployment(circuit);
        SingleFaultModel faultModel = new SingleUsedFaultModel(circuit, experiment.getNumOfOutputs());
        final int POP_SIZE = 32;
        final int NUM_OF_ELITES = 2;
        Genotype[] seeds = new Genotype[POP_SIZE];
        for (int pl = 0; pl < POP_SIZE; pl++) {
            seeds[pl] = (FullOrderGenotype) seed.clone();
            for (int bl = qDefSize + usedEls * blockSize; bl < seeds[pl].length(); bl++) if (Math.random() < 0.5) seeds[pl].set(bl);
        }
        int fixedAlignments = locking ? usedEls : 0;
        int minMutationRate = genotypeLength / 500 + 1;
        int maxMutationRate = genotypeLength / 25 + 1;
        int SAGAFitnessIndex = 1;
        int howManyBunches = 1;
        GeneticOperator m = new SAGAMutator(minMutationRate, maxMutationRate, SAGAFitnessIndex, fixedAlignments * blockSize, -1);
        GeneticOperator spxo = new SinglePointXOver();
        GeneticOperator bmin0 = new BunchMutator(bitsPerVar, howManyBunches, blockSize, lutSize, fixedAlignments, qDefSize);
        GeneticOperator bmin1 = new BunchMutator(bitsPerVar, howManyBunches, blockSize, lutSize + bitsPerVar, fixedAlignments, qDefSize);
        GeneticOperator bmin2 = new BunchMutator(bitsPerVar, howManyBunches, blockSize, lutSize + bitsPerVar * 2, fixedAlignments, qDefSize);
        GeneticOperator bmin3 = new BunchMutator(bitsPerVar, howManyBunches, blockSize, lutSize + bitsPerVar * 3, fixedAlignments, qDefSize);
        GeneticOperator bc = new BlockCopy(blockSize, blockSize, 0, fixedAlignments, qDefSize);
        GeneticOperator[] geneticOps = { m, spxo, bmin0, bmin1, bmin2, bmin3, bc };
        double[] opsProbs = { 0.2, 0.3, 0.1, 0.1, 0.1, 0.1, 0.1 };
        if (LUTInputs == 2) {
            geneticOps[4] = bmin0;
            geneticOps[5] = bmin1;
        }
        Selector selector = new RankSelector();
        Evolver evolver = new StandardEvolver(POP_SIZE, genotypeLength, geneticOps, opsProbs, selector, NUM_OF_ELITES, seeds);
        PopulationLogReader.fullOrderGenotypes = true;
        int[] numProps = { 2 };
        double maxSize = nrAddUnits;
        CircuitPainterObject painter = new CircuitPainterObject(new CircuitPainter(), new LUTAbsoluteMapping(experiment.getNumOfInputs(), experiment.getNumOfOutputs() + E_LINES, bitsPerVar, LUTInputs, new ConstantDelayModel(0)));
        TSCCombinationalAlgebraicIM inIm = new TSCCombinationalAlgebraicIM(evolver, deployment, circuit, experiment, faultModel, E_SIZE, E_START_AT, INPUT_SAMPLE_SEP, painter);
        InteractionModel noisyIM = new NoisyPIM(inIm, deployment, experiment, numProps, NO_EVALS);
        im = new CircuitParsimonyPIM(noisyIM, circuit, maxSize);
    }
}
