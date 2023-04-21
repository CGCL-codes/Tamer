package edu.arizona.cs.learn.timeseries.classification;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.math.stat.descriptive.SummaryStatistics;
import edu.arizona.cs.learn.timeseries.model.Instance;
import edu.arizona.cs.learn.timeseries.model.symbols.ProportionSymbol;
import edu.arizona.cs.learn.timeseries.model.symbols.Symbol;
import edu.arizona.cs.learn.util.DataMap;
import edu.arizona.cs.learn.util.MathUtils;

/**
 * The ProportionClassifier will take sequences of ProportionSymbols
 * and construct a aggregate distribution for the propositions that
 * turn on during the instances.
 * 
 *   a  000011110000
 *   b  000001111111
 *   c  111000111111  
 *   
 * yields a sequence of ProportionSymbols like the following
 * 
 *   a  4/12 -- 0.33333
 *   b  7/12 -- 0.58333333
 *   c  9/12 -- 0.75
 *   
 * Ordering information is lost, but this classifier is primarily
 * to show that ordering information can actual improve performance.
 * @author kerrw
 *
 */
public class ProportionClassifier extends Classifier {

    private Map<String, Map<Integer, Double>> _map;

    public ProportionClassifier(ClassifyParams params) {
        super(params);
    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public Map<String, Long> train(int batchId, Map<String, List<Instance>> trainingSet) {
        _map = new HashMap<String, Map<Integer, Double>>();
        Map<String, Long> trainingTime = new HashMap<String, Long>();
        for (String key : trainingSet.keySet()) {
            long start = System.currentTimeMillis();
            Map<Integer, SummaryStatistics> propMap = new HashMap<Integer, SummaryStatistics>();
            List<Instance> instances = trainingSet.get(key);
            for (Instance instance : instances) {
                List<Symbol> sequence = instance.sequence();
                for (Symbol s : sequence) {
                    ProportionSymbol ps = (ProportionSymbol) s;
                    double proportion = (double) ps.timeOn() / (double) ps.duration();
                    SummaryStatistics ss = propMap.get(ps.propId());
                    if (ss == null) {
                        ss = new SummaryStatistics();
                        propMap.put(ps.propId(), ss);
                    }
                    ss.addValue(proportion);
                }
            }
            Map<Integer, Double> map = new HashMap<Integer, Double>();
            double sum = 0;
            for (Integer propId : propMap.keySet()) {
                SummaryStatistics ss = propMap.get(propId);
                double mean = ss.getMean();
                map.put(propId, mean);
                sum += mean;
            }
            Map<Integer, Double> copy = new HashMap<Integer, Double>();
            for (Integer propId : map.keySet()) {
                copy.put(propId, map.get(propId) / sum);
            }
            _map.put(key, copy);
            long end = System.currentTimeMillis();
            trainingTime.put(key, end - start);
        }
        return trainingTime;
    }

    @Override
    public String test(Instance testInstance) {
        Map<Integer, Double> tmp = new HashMap<Integer, Double>();
        double sum = 0;
        for (Symbol s : testInstance.sequence()) {
            ProportionSymbol ps = (ProportionSymbol) s;
            double p = (double) ps.timeOn() / (double) ps.duration();
            tmp.put(ps.propId(), p);
            sum += p;
        }
        Map<Integer, Double> qDist = new HashMap<Integer, Double>();
        for (Integer key : tmp.keySet()) {
            qDist.put(key, tmp.get(key) / sum);
        }
        String bestClass = "";
        double closest = Double.POSITIVE_INFINITY;
        for (String className : _map.keySet()) {
            Map<Integer, Double> pDist = _map.get(className);
            double d = MathUtils.klDivergence(pDist, qDist);
            if (d < closest) {
                bestClass = className;
                closest = d;
            }
        }
        return bestClass;
    }
}
