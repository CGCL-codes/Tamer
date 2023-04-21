package unbbayes.evaluation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import unbbayes.evaluation.exception.EvaluationException;
import unbbayes.prs.Node;
import unbbayes.prs.bn.TreeVariable;
import unbbayes.simulation.montecarlo.sampling.IMonteCarloSampling;
import unbbayes.simulation.montecarlo.sampling.MatrixMonteCarloSampling;

public class FastMCApproximateEvaluation extends AEvaluation {

    protected int sampleSize;

    protected IMonteCarloSampling mc;

    protected TreeVariable targetNode;

    protected byte[][] sampleMatrix;

    protected List<Node> positionNodeList;

    protected int[] positionTargetNodeList;

    protected int[] positionEvidenceNodeList;

    public float getError() {
        return (float) (1 / Math.sqrt(this.sampleSize));
    }

    public FastMCApproximateEvaluation(int sampleSize) {
        this.sampleSize = sampleSize;
    }

    protected void evaluate(List<String> targetNodeNameList, List<String> evidenceNodeNameList, boolean onlyGCM) throws EvaluationException {
        mc = new MatrixMonteCarloSampling();
        long init = System.currentTimeMillis();
        mc.start(net, sampleSize);
        long end = System.currentTimeMillis();
        System.out.println("Time elapsed for sampling: " + (float) (end - init) / 1000);
        init = System.currentTimeMillis();
        sampleMatrix = mc.getSampledStatesMatrix();
        end = System.currentTimeMillis();
        System.out.println("Time elapsed for matrix: " + (float) (end - init) / 1000);
        System.out.println();
        super.evaluate(targetNodeNameList, evidenceNodeNameList, onlyGCM);
    }

    protected float[][] computeCM(List<String> targetNodeNameList, List<String> evidenceNodeNameList) throws EvaluationException {
        init(targetNodeNameList, evidenceNodeNameList);
        long init = System.currentTimeMillis();
        targetNode = targetNodeList[0];
        if (targetNodeList.length != 1) {
            throw new EvaluationException("For now, just one target node is accepted!");
        }
        positionTargetNodeList = new int[targetNodeList.length];
        positionEvidenceNodeList = new int[evidenceNodeList.length];
        positionNodeList = mc.getSamplingNodeOrderQueue();
        for (int i = 0; i < positionTargetNodeList.length; i++) {
            positionTargetNodeList[i] = positionNodeList.indexOf((Node) targetNodeList[i]);
        }
        for (int i = 0; i < positionEvidenceNodeList.length; i++) {
            positionEvidenceNodeList[i] = positionNodeList.indexOf((Node) evidenceNodeList[i]);
        }
        int[] frequencyEvidenceGivenTargetList = new int[statesProduct];
        int[] frequencyEvidenceList = new int[targetStatesProduct];
        for (int i = 0; i < sampleMatrix.length; i++) {
            int row = 0;
            int currentStatesProduct = evidenceStatesProduct;
            for (int j = positionTargetNodeList.length - 1; j >= 0; j--) {
                byte state = sampleMatrix[i][positionTargetNodeList[j]];
                row += state * currentStatesProduct;
                currentStatesProduct *= positionNodeList.get(positionTargetNodeList[j]).getStatesSize();
            }
            frequencyEvidenceList[(int) (row / evidenceStatesProduct)]++;
            currentStatesProduct = evidenceStatesProduct;
            for (int j = 0; j < positionEvidenceNodeList.length; j++) {
                currentStatesProduct /= positionNodeList.get(positionEvidenceNodeList[j]).getStatesSize();
                byte state = sampleMatrix[i][positionEvidenceNodeList[j]];
                row += state * currentStatesProduct;
            }
            frequencyEvidenceGivenTargetList[row]++;
        }
        float[] postProbEvidenceGivenTarget = new float[statesProduct];
        for (int i = 0; i < postProbEvidenceGivenTarget.length; i++) {
            float n = (float) frequencyEvidenceList[(int) (i / evidenceStatesProduct)];
            if (n != 0) {
                postProbEvidenceGivenTarget[i] = (float) frequencyEvidenceGivenTargetList[i] / n;
            }
        }
        float[] postProbTargetGivenEvidence = new float[statesProduct];
        int row = 0;
        float prob = 0.0f;
        float[] normalizationList = new float[evidenceStatesProduct];
        try {
            net.compile();
        } catch (Exception e) {
            throw new EvaluationException(e.getMessage());
        }
        for (int i = 0; i < targetNode.getStatesSize(); i++) {
            for (int j = 0; j < evidenceStatesProduct; j++) {
                row = j + i * evidenceStatesProduct;
                prob = postProbEvidenceGivenTarget[row] * targetNode.getMarginalAt(i);
                postProbTargetGivenEvidence[row] = prob;
                normalizationList[j] += prob;
            }
        }
        float norm = 0;
        for (int i = 0; i < postProbTargetGivenEvidence.length; i++) {
            norm = normalizationList[i % evidenceStatesProduct];
            if (norm != 0) {
                postProbTargetGivenEvidence[i] /= norm;
            }
        }
        float[] postProbTargetGivenTarget = new float[(int) Math.pow(targetNode.getStatesSize(), 2)];
        int statesSize = targetNode.getStatesSize();
        row = 0;
        int index = 0;
        for (int i = 0; i < statesProduct; i++) {
            for (int j = 0; j < statesSize; j++) {
                row = ((int) (i / evidenceStatesProduct)) * statesSize + j;
                index = (i % evidenceStatesProduct) + j * evidenceStatesProduct;
                postProbTargetGivenTarget[row] += postProbTargetGivenEvidence[i] * postProbEvidenceGivenTarget[index];
            }
        }
        float[][] CM = new float[statesSize][statesSize];
        for (int i = 0; i < statesSize; i++) {
            for (int j = 0; j < statesSize; j++) {
                CM[i][j] = postProbTargetGivenTarget[i * statesSize + j];
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("Time elapsed for computing CM: " + (float) (end - init) / 1000);
        return CM;
    }

    public static void main(String[] args) throws Exception {
        boolean runSmallTest = false;
        boolean onlyGCM = true;
        List<String> targetNodeNameList = new ArrayList<String>();
        List<String> evidenceNodeNameList = new ArrayList<String>();
        String netFileName = "";
        int sampleSize = 0;
        if (runSmallTest) {
            targetNodeNameList = new ArrayList<String>();
            targetNodeNameList.add("Springler");
            evidenceNodeNameList = new ArrayList<String>();
            evidenceNodeNameList.add("Cloudy");
            evidenceNodeNameList.add("Rain");
            evidenceNodeNameList.add("Wet");
            netFileName = "src/test/resources/testCases/evaluation/WetGrass.xml";
            sampleSize = 100000;
        } else {
            targetNodeNameList = new ArrayList<String>();
            targetNodeNameList.add("TargetType");
            evidenceNodeNameList = new ArrayList<String>();
            evidenceNodeNameList.add("UHRR_Confusion");
            evidenceNodeNameList.add("ModulationFrequency");
            evidenceNodeNameList.add("CenterFrequency");
            evidenceNodeNameList.add("PRI");
            evidenceNodeNameList.add("PRF");
            netFileName = "src/test/resources/testCases/evaluation/AirID.xml";
            sampleSize = 500000;
        }
        FastMCApproximateEvaluation evaluationApproximate = new FastMCApproximateEvaluation(sampleSize);
        evaluationApproximate.evaluate(netFileName, targetNodeNameList, evidenceNodeNameList, onlyGCM);
        System.out.println("----TOTAL------");
        System.out.println("LCM:\n");
        show(evaluationApproximate.getEvidenceSetCM());
        System.out.println("\n");
        System.out.println("PCC: ");
        System.out.printf("%2.2f\n", evaluationApproximate.getEvidenceSetPCC() * 100);
        if (!onlyGCM) {
            System.out.println("\n\n\n");
            System.out.println("----MARGINAL------");
            System.out.println("\n\n");
            List<EvidenceEvaluation> list = evaluationApproximate.getBestMarginalImprovement();
            for (EvidenceEvaluation evidenceEvaluation : list) {
                System.out.println("-" + evidenceEvaluation.getName() + "-");
                System.out.println("\n\n");
                System.out.println("LCM:\n");
                show(evidenceEvaluation.getMarginalCM());
                System.out.println("\n");
                System.out.println("PCC: ");
                System.out.printf("%2.2f\n", evidenceEvaluation.getMarginalPCC() * 100);
                System.out.println("\n");
                System.out.println("Marginal Improvement: ");
                System.out.printf("%2.2f\n", evidenceEvaluation.getMarginalImprovement() * 100);
                System.out.println("\n\n");
            }
            System.out.println("\n");
            System.out.println("----INDIVIDUAL PCC------");
            System.out.println("\n\n");
            list = evaluationApproximate.getBestIndividualPCC();
            for (EvidenceEvaluation evidenceEvaluation : list) {
                System.out.println("-" + evidenceEvaluation.getName() + "-");
                System.out.println("\n\n");
                System.out.println("LCM:\n");
                show(evidenceEvaluation.getIndividualLCM());
                System.out.println("\n");
                System.out.println("PCC: ");
                System.out.printf("%2.2f\n", evidenceEvaluation.getIndividualPCC() * 100);
                System.out.println("\n\n");
                evidenceEvaluation.setCost((new Random()).nextFloat() * 1000);
            }
            System.out.println("\n");
            System.out.println("----INDIVIDUAL PCC------");
            System.out.println("\n\n");
            list = evaluationApproximate.getBestIndividualCostRate();
            for (EvidenceEvaluation evidenceEvaluation : list) {
                System.out.println("-" + evidenceEvaluation.getName() + "-");
                System.out.println("\n\n");
                System.out.println("PCC: ");
                System.out.printf("%2.2f\n", evidenceEvaluation.getIndividualPCC() * 100);
                System.out.println("\n");
                System.out.println("Cost: ");
                System.out.printf("%2.2f\n", evidenceEvaluation.getCost());
                System.out.println("\n");
                System.out.println("Cost Rate: ");
                System.out.printf("%2.2f\n", evidenceEvaluation.getMarginalCost() * 100);
                System.out.println("\n\n");
            }
        }
    }
}
