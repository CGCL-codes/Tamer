package classifier.examples;

import java.util.ArrayList;
import java.util.List;
import classifier.Rule;
import classifier.RulesGenerator;
import classifier.data.ClassifiedSample;
import classifier.examples.PhotoRuleCondition.ConditionRepairer;
import engine.Population;
import engine.utils.WevoRandom;

/**
 * Generator of random rules used in satellite photos classification.
 * 
 * @author Lukasz Krawiec (lmkrawiec@gmail.com)
 * @author Michal Anglart (anglart.michal@gmail.com)
 */
public class PhotoRulesGenerator implements RulesGenerator<List<Integer>, SatellitePhotoCategory> {

    /** Lower limit on single band range (treated inclusively). */
    public static final int LOWER_BOUND_ON_BAND = 0;

    /** Upper limit on single band range (treated exclusively). */
    public static final int UPPER_BOUND_ON_BAND = 256;

    /** Random numbers generator. */
    private WevoRandom random;

    /** Sigma value (std deviation) used in gaussian distribution.  */
    private double sigma;

    /** No of bands in single pixel of original data. */
    private int noOfPixelBands;

    /**
     * Standard constructor.
     * @param random Random numbers generator.
     * @param sigma Sigma value (std deviation) used in gaussian distribution.
     * @param noOfPixelBands Number of bands in single pixel.
     */
    public PhotoRulesGenerator(WevoRandom random, double sigma, int noOfPixelBands) {
        this.random = random;
        this.sigma = sigma;
        this.noOfPixelBands = noOfPixelBands;
    }

    /**
     * Generates random rule bounded with given category.
     * Package-visible for testing.
     * @param category Category in which rule should check for membership.
     * @return Random rule for given category.
     */
    Rule<List<Integer>, SatellitePhotoCategory> generateRandomRule(SatellitePhotoCategory category) {
        List<List<Integer>> condParams = new ArrayList<List<Integer>>();
        for (int i = 0; i < noOfPixelBands; i++) {
            List<Integer> range = new ArrayList<Integer>();
            range.add(random.nextInt(LOWER_BOUND_ON_BAND, UPPER_BOUND_ON_BAND));
            range.add(random.nextInt(LOWER_BOUND_ON_BAND, UPPER_BOUND_ON_BAND));
            condParams.add(range);
        }
        PhotoRuleCondition condition = new PhotoRuleCondition(condParams);
        ConditionRepairer.repairCondition(condition);
        return new Rule<List<Integer>, SatellitePhotoCategory>(condition, category);
    }

    /**
     * Generates rule using MinMax approach. Given category C we first
     * look into learning set and collect minimum and maximum value of
     * every band in pixels which are classified as C. Next we create
     * range for each band using min and max values with a little
     * modification made by adding a random gaussian-distributed value
     * to range bounds.
     * 
     * @param learningMultiset Set of classified samples used during generation.
     * @param category Category for which rule should be created.
     * @return Generated rule.
     */
    Rule<List<Integer>, SatellitePhotoCategory> generateRandomRuleWithMinMax(List<ClassifiedSample<List<Integer>, SatellitePhotoCategory>> learningMultiset, SatellitePhotoCategory category) {
        List<Integer> minValues = new ArrayList<Integer>();
        List<Integer> maxValues = new ArrayList<Integer>();
        for (int i = 0; i < noOfPixelBands; i++) {
            minValues.add(UPPER_BOUND_ON_BAND);
            maxValues.add(LOWER_BOUND_ON_BAND);
        }
        for (ClassifiedSample<List<Integer>, SatellitePhotoCategory> sample : learningMultiset) {
            if (sample.getCategory().equals(category)) {
                for (int i = 0; i < noOfPixelBands; i++) {
                    int bandValue = sample.getDataSample().get(i);
                    minValues.set(i, Math.min(bandValue, minValues.get(i)));
                    maxValues.set(i, Math.max(bandValue, maxValues.get(i)));
                }
            }
        }
        List<List<Integer>> ranges = new ArrayList<List<Integer>>();
        for (int i = 0; i < noOfPixelBands; i++) {
            int intervals = random.nextInt(1, PhotoRuleCondition.MAX_INTERVALS);
            List<Integer> range = new ArrayList<Integer>();
            for (int j = 0; j < intervals; j++) {
                int finalBandLowerValue = minValues.get(i);
                int finalBandUpperValue = maxValues.get(i);
                finalBandLowerValue += random.nextGaussian() * sigma;
                finalBandUpperValue += random.nextGaussian() * sigma;
                range.add(finalBandLowerValue);
                range.add(finalBandUpperValue);
            }
            ranges.add(range);
        }
        PhotoRuleCondition condition = new PhotoRuleCondition(ranges);
        ConditionRepairer.repairCondition(condition);
        return new Rule<List<Integer>, SatellitePhotoCategory>(condition, category);
    }

    /**
     * {@inheritDoc}
     */
    public Population<Rule<List<Integer>, SatellitePhotoCategory>> generateRulesPopulation(SatellitePhotoCategory category, int populationSize) {
        Population<Rule<List<Integer>, SatellitePhotoCategory>> population = new Population<Rule<List<Integer>, SatellitePhotoCategory>>();
        for (int i = 0; i < populationSize; i++) {
            population.addIndividual(generateRandomRule(category));
        }
        return population;
    }

    /**
     * {@inheritDoc}
     */
    public Population<Rule<List<Integer>, SatellitePhotoCategory>> generateRulesPopulationUsingLearningSet(SatellitePhotoCategory category, int populationSize, List<ClassifiedSample<List<Integer>, SatellitePhotoCategory>> learningSet) {
        Population<Rule<List<Integer>, SatellitePhotoCategory>> population = new Population<Rule<List<Integer>, SatellitePhotoCategory>>();
        for (int i = 0; i < populationSize; i++) {
            Rule<List<Integer>, SatellitePhotoCategory> rule = generateRandomRuleWithMinMax(learningSet, category);
            population.addIndividual(rule);
        }
        return population;
    }
}
