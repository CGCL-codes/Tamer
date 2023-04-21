package net.saim.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.saim.datastructures.MetricInfo;
import net.saim.datastructures.MetricInfoComposite;
import net.saim.datastructures.MetricInfoSimple;
import net.saim.datastructures.Tree;
import uk.ac.shef.wit.simmetrics.similaritymetrics.InterfaceStringMetric;
import uk.ac.shef.wit.simmetrics.similaritymetrics.QGramsDistance;
import com.hp.hpl.jena.query.ResultSet;
import de.konrad.commons.sparql.PrefixHelper;
import de.konrad.commons.sparql.SPARQLHelper;
import de.uni_leipzig.simba.io.KBInfo;

/**
 * 
 * @author Konrad Höffner
 *
 */
public class SimpleLearner implements Learner {

    Map<String, String> geoParams = new HashMap<String, String>();

    InterfaceStringMetric[] metrics = { new QGramsDistance() };

    final String[] properties1;

    final String[] properties2;

    final double THRESHOLD = 0.8;

    private KBInfo source;

    private KBInfo target;

    Map<String, Integer> propertyToColumn = new HashMap<String, Integer>();

    Map<String, Integer> propertyToRow = new HashMap<String, Integer>();

    public SimpleLearner(String[] properties1, String[] properties2) {
        super();
        this.properties1 = properties1;
        this.properties2 = properties2;
    }

    public List<String> getProperties(KBInfo kb, String url) {
        List<String> properties = new ArrayList<String>();
        String query = "SELECT distinct(?p) where {<" + url + "> ?p ?o. }";
        ResultSet rs = SPARQLHelper.query(kb.endpoint, null, query);
        while (rs.hasNext()) {
            String property = rs.next().getResource("p").getURI();
            if (property != null) properties.add(property);
        }
        return properties;
    }

    List<String> getObjects(KBInfo kb, String url, String property) {
        List<String> objects = new ArrayList<String>();
        try {
            String query = "SELECT distinct(?o) where {<" + url + "> <" + property + "> ?o. }";
            ResultSet rs = SPARQLHelper.query(kb.endpoint, null, query);
            while (rs.hasNext()) {
                String object = rs.next().get("o").toString();
                if (object != null) {
                    object = SPARQLHelper.lexicalForm(object);
                    objects.add(object);
                }
            }
        } catch (Exception e) {
            return objects;
        }
        return objects;
    }

    /**
	 * @param examples Adds to all negative or all positive correctness values a value that is chosen to guarantee that the sum of all correctness values is 0.
	 * If there are no negative correctness values (only positive examples) this does nothing.
	 */
    private static void normalizeExamples(Example[] examples) {
        double positiveSum = 0;
        double negativeSum = 0;
        int negativeNum = 0;
        for (Example example : examples) {
            if (example.correctness < 0) {
                negativeSum -= example.correctness;
                negativeNum++;
            } else {
                positiveSum += example.correctness;
            }
        }
        if (negativeNum > 0) {
            if (positiveSum > negativeSum) {
                for (int i = 0; i < examples.length; i++) {
                    if (examples[i].correctness < 0) examples[i].correctness *= positiveSum / negativeSum;
                }
            }
            if (negativeSum > positiveSum) {
                for (int i = 0; i < examples.length; i++) {
                    if (examples[i].correctness > 0) examples[i].correctness *= negativeSum / positiveSum;
                }
            }
        }
    }

    @Override
    public Tree<MetricInfo> learn(KBInfo source, KBInfo target, Example[] examples) {
        normalizeExamples(examples);
        Tree<MetricInfo> tree = new Tree<MetricInfo>();
        MetricInfo rootMetric = new MetricInfoComposite("average");
        tree.setRootElement(rootMetric);
        this.source = source;
        this.target = target;
        Map<String, Double> sourcePropertyOccurrences = new HashMap<String, Double>();
        Map<String, Double> targetPropertyOccurrences = new HashMap<String, Double>();
        for (Example example : examples) {
            for (String property : getProperties(source, example.url1)) {
                if (sourcePropertyOccurrences.containsKey(property)) {
                    sourcePropertyOccurrences.put(property, sourcePropertyOccurrences.get(property) + 1);
                } else {
                    sourcePropertyOccurrences.put(property, 1.0);
                }
            }
            for (String property : getProperties(target, example.url2)) {
                if (targetPropertyOccurrences.containsKey(property)) targetPropertyOccurrences.put(property, targetPropertyOccurrences.get(property) + 1); else {
                    targetPropertyOccurrences.put(property, 1.0);
                }
            }
        }
        List<String> sourceProperties = new ArrayList<String>();
        List<String> targetProperties = new ArrayList<String>();
        for (String property : sourcePropertyOccurrences.keySet()) {
            if (sourcePropertyOccurrences.get(property) > (examples.length * THRESHOLD)) {
                sourceProperties.add(property);
            }
        }
        for (String property : targetPropertyOccurrences.keySet()) {
            if (targetPropertyOccurrences.get(property) > (examples.length * THRESHOLD)) {
                targetProperties.add(property);
            }
        }
        for (InterfaceStringMetric metric : metrics) {
            double[][] correlations = correlations(metric, examples, sourceProperties, targetProperties);
            normalize(correlations);
            final double threshold = 0.9;
            for (int i = 0; i < correlations.length; i++) for (int j = 0; j < correlations[0].length; j++) {
                MetricInfoSimple metricInfo = new MetricInfoSimple(metric.getClass().toString(), null);
                metricInfo.sourceProperty = sourceProperties.get(i);
                metricInfo.targetProperty = targetProperties.get(j);
                if (correlations[i][j] > threshold) tree.getRootElement().addChild(metricInfo);
            }
        }
        return tree;
    }

    public void normalize(double[][] correlations) {
        double max = 0;
        for (int i = 0; i < correlations.length; i++) for (int j = 0; j < correlations[0].length; j++) {
            max = Math.max(max, correlations[i][j]);
        }
        for (int i = 0; i < correlations.length; i++) for (int j = 0; j < correlations[0].length; j++) {
            correlations[i][j] /= max;
        }
    }

    /**
	 * @param metric the metric which is evaluated for each pair source, target in each example.  
	 * @param examples
	 * @param sourceProperties restrict the properties to evaluate. if set to null, all properties are used.
	 * @param targetProperties restrict the properties to evaluate. if set to null, all properties are used.
	 * @return the resulting values of the metric are averaged in the correlation array, where the rows are for the source properties and columns for the target properties.
	 */
    public double[][] correlations(InterfaceStringMetric metric, Example[] examples, List<String> sourceProperties, List<String> targetProperties) {
        double[][] correlations = new double[sourceProperties.size()][targetProperties.size()];
        for (int i = 0; i < sourceProperties.size(); i++) propertyToColumn.put(sourceProperties.get(i), i);
        for (int i = 0; i < targetProperties.size(); i++) propertyToRow.put(targetProperties.get(i), i);
        for (Example example : examples) {
            Set<String> commonSourceProperties = new HashSet<String>(getProperties(source, example.url1));
            if (sourceProperties != null) commonSourceProperties.retainAll(sourceProperties);
            Set<String> commonTargetProperties = new HashSet<String>(getProperties(target, example.url2));
            if (targetProperties != null) commonTargetProperties.retainAll(targetProperties);
            for (String sourceProperty : commonSourceProperties) {
                for (String targetProperty : commonTargetProperties) {
                    List<String> sourceObjects = getObjects(source, example.url1, sourceProperty);
                    List<String> targetObjects = getObjects(target, example.url2, targetProperty);
                    List<Double> similarities = new ArrayList<Double>();
                    similarities.add(0.0);
                    for (String sourceObject : sourceObjects) {
                        sourceObject = SPARQLHelper.lexicalForm(PrefixHelper.getSuffix(sourceObject));
                        for (String targetObject : targetObjects) {
                            targetObject = SPARQLHelper.lexicalForm(PrefixHelper.getSuffix(targetObject));
                            double literalSimilarity = 0;
                            literalSimilarity = metric.getSimilarity(sourceObject, targetObject);
                            if (literalSimilarity > 0) {
                            }
                            similarities.add(literalSimilarity);
                        }
                    }
                    correlations[propertyToColumn.get(sourceProperty)][propertyToRow.get(targetProperty)] += Collections.max(similarities) * example.correctness / examples.length;
                }
            }
        }
        for (int i = 0; i < correlations.length; i++) for (int j = 0; j < correlations.length; j++) {
            if (correlations[i][j] > 0.5) {
                System.out.println("correlation of " + (correlations[i][j] > 0.5) + " for " + properties1[i] + " and " + properties2[j]);
            }
        }
        return correlations;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public String getName() {
        return "Simple Algorithm";
    }

    @Override
    public Example[] getExamples(Tree<MetricInfo> metric) {
        return null;
    }
}
