package edu.arizona.cs.learn.timeseries.experiment;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.math.stat.descriptive.SummaryStatistics;
import org.apache.log4j.Logger;
import edu.arizona.cs.learn.algorithm.alignment.SequenceAlignment;
import edu.arizona.cs.learn.algorithm.alignment.Params;
import edu.arizona.cs.learn.timeseries.model.Instance;
import edu.arizona.cs.learn.timeseries.model.SequenceType;
import edu.arizona.cs.learn.timeseries.model.signature.Signature;
import edu.arizona.cs.learn.util.Utils;

public class AverageDistance {

    private static Logger logger = Logger.getLogger(AverageDistance.class);

    public static void doTest(String name, SequenceType type) {
        Utils.LIMIT_RELATIONS = true;
        Utils.WINDOW = 5;
        Params params = new Params();
        params.setMin(10, 0);
        params.setBonus(1, 0);
        params.setPenalty(-1, 0);
        Set<String> seenSet = new HashSet<String>();
        List<Instance> instances = Instance.load(name, new File("data/input/" + name + ".lisp"), type);
        SummaryStatistics ss = new SummaryStatistics();
        while (ss.getN() < 200) {
            Collections.shuffle(instances);
            StringBuffer buf = new StringBuffer();
            for (Instance instance : instances) {
                buf.append(instance.id() + " ");
            }
            buf.deleteCharAt(buf.length() - 1);
            String orderName = buf.toString();
            if (seenSet.contains(orderName)) continue;
            seenSet.add(orderName);
            Signature signature = new Signature(name);
            signature.train(instances);
            SummaryStatistics distance = new SummaryStatistics();
            for (Instance instance : instances) {
                params.seq1 = signature.signature();
                params.seq2 = instance.sequence();
                distance.addValue(SequenceAlignment.distance(params));
            }
            logger.debug("\tSingle Ordering: " + distance.getMean() + " " + distance.getStandardDeviation());
            ss.addValue(distance.getMean());
        }
        logger.debug("Average Distance: " + ss.getMean() + " " + ss.getStandardDeviation() + " " + ss.getMin() + " " + ss.getMax());
    }

    public static void main(String[] args) {
        String name = "ww-jump-on";
        doTest(name, SequenceType.allen);
    }
}
