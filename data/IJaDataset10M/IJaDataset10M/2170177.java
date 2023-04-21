package cz.uhk.prg.test;

import java.util.List;
import cz.uhk.prg.main.GeneratedLine;
import cz.uhk.prg.main.GeneratorExperiment;
import cz.uhk.prg.main.GeneratorTest;
import umontreal.iro.lecuyer.probdist.NormalDist;

public class ExtermalTest extends DefaultTest {

    private double alpha = 0.05;

    public ExtermalTest(GeneratorExperiment generatorExperiment, double alpha) {
        super(generatorExperiment);
        this.alpha = alpha;
    }

    @Override
    public GeneratorTest runTest() {
        for (GeneratedLine generatedLine : generatorExperiment.getGeneratedLines()) {
            List<Double> list = generatedLine.getNumbers();
            NormalDist nd = new NormalDist();
            double testCriterium = computeTestCriterium(list);
            double criticalValue = nd.inverseF(1 - alpha / 2);
            return onvertResultToGeneratorTest(testCriterium <= criticalValue);
        }
        return null;
    }

    public double computeTestCriterium(List<Double> list) {
        double P = computeExtermalPoints(list);
        double n = list.size();
        return Math.abs((P - (2 * (n - 2)) / 3) * Math.sqrt(90 / (16 * n - 29)));
    }

    public int computeExtermalPoints(List<Double> list) {
        int count = 0;
        for (int i = 1; i < list.size() - 1; i++) {
            if ((list.get(i) > list.get(i - 1) && list.get(i) > list.get(i + 1)) || (list.get(i) < list.get(i - 1) && list.get(i) < list.get(i + 1))) {
                count++;
            }
        }
        return count;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    private GeneratorTest onvertResultToGeneratorTest(boolean b) {
        GeneratorTest generatorTest = new GeneratorTest();
        StringBuilder sb = new StringBuilder();
        sb.append("Extermal points test\n");
        sb.append("Test parameters:\n");
        sb.append("Alpha: ").append(getAlpha()).append("\n");
        if (b) {
            sb.append("Null hypothesis of extermal points test is ACCEPTED on ").append(getAlpha()).append(" significance level");
        } else {
            sb.append("Null hypothesis of extermal points test is REJECTED on ").append(getAlpha()).append(" significance level");
        }
        generatorTest.setIsTestPassed(b);
        generatorTest.setGeneratorTestResult(sb.toString());
        return generatorTest;
    }
}
