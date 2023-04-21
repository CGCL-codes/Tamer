package net.codigofuerte.classify.bayesian;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Represent the model used for a Naïve Bayes classifier.<br/>
 * The Bayesian model consists of two "submodels" inside:<br/>
 * <ul>
 *   <li>
 *      <code>fcModel</code>. Represents the frequencies of a feature in a given category.
 *      <pre>
 *      {@code
 *          {
 *              'feature1':
 *                      {'category1': 0, 'category2': 6}, // "feature1" appears "0" times for "category1" and "6" times for "category2"
 *              'feature2':
 *                      {'category1': 3, 'category2': 3}
 *          }
 *      }
 *      </pre>
 *   </li>
 *   <li>
 *      <code>ccModel</code>. Represents the number of documents used for training in a given category.
 *      <pre>
 *      {@code
 *          {
 *              'category1':16,     // "category1" used "16" documents for training...
 *              'category2':25      // ...and "25" for "category2"
 *          }
 *      }
 *      </pre>
 *  </li>
 * </ul>
 * <br/>
 * The class provides utility methods to inspect and retrieve the contents of the model.<br/>
 * The model contains also a collection of "category thresholds". This is used to fine tune the classifier.<br/>
 * Each category has a threshold to help the classifier in the decission of assiging a category (among al categories)
 * to a document being classified.
 * 
 * @author Sergio Cruz Moral (scmoral@codigofuerte.net)
 */
public class NaiveBayesModel {

    private Map<String, Map<String, Float>> fcModel = null;

    private Map<String, Long> ccModel = null;

    private Map<String, Float> catThresholds;

    /** 
     * Default constructor<br/>
     * Initializes an empty model.
     */
    public NaiveBayesModel() {
        fcModel = new LinkedHashMap<String, Map<String, Float>>();
        ccModel = new LinkedHashMap<String, Long>();
        catThresholds = new LinkedHashMap<String, Float>();
    }

    /**
     * Initialize a model object with the values passed by parameter.
     * @param fcModelVal Frequency/Feature
     * @param ccModelVal
     */
    public NaiveBayesModel(Map<String, Map<String, Float>> fcModelVal, Map<String, Long> ccModelVal, Map<String, Float> catThrVal) {
        fcModel = fcModelVal;
        ccModel = ccModelVal;
        catThresholds = catThrVal;
    }

    public Map<String, Long> getCcModel() {
        return ccModel;
    }

    public void setCcModel(Map<String, Long> ccModelVal) {
        ccModel = ccModelVal;
    }

    public Map<String, Map<String, Float>> getFcModel() {
        return fcModel;
    }

    public void setFcModel(Map<String, Map<String, Float>> fcModelVal) {
        fcModel = fcModelVal;
    }

    /**
     * Increment the count of a feature/category frequency.
     *
     * @param feature Word/Feature whose category frequency is goin to be increased.
     * @param category Category for the given word/feature to be increased.
     */
    public void increaseFeatureCategoryCount(String feature, String category) {
        if (fcModel != null) {
            Map<String, Float> catFreqsForFeature = fcModel.get(feature);
            if (catFreqsForFeature == null) {
                catFreqsForFeature = new HashMap<String, Float>();
                catFreqsForFeature.put(category, 1f);
                fcModel.put(feature, catFreqsForFeature);
            } else {
                Float a = catFreqsForFeature.get(category);
                if (a == null) catFreqsForFeature.put(category, 1f); else catFreqsForFeature.put(category, catFreqsForFeature.get(category) + 1f);
            }
        }
    }

    /**
     * Increase the counter for the number of documents in a given category.
     *
     * @param category Category whose number of documents is going to be incremented.
     */
    public void increaseCategoryCount(String category) {
        if (ccModel != null) {
            Long aValue = ccModel.get(category);
            if (aValue == null) ccModel.put(category, 1L); else ccModel.put(category, aValue + 1L);
        }
    }

    /**
     * Returns the frequency of a given feature in a given category.
     *
     * @param feature Feature Name
     * @param category Category Name.
     * @return Frequency of the given feature in the given category.
     */
    public Float getFeatureCategoryFreq(String feature, String category) {
        Float retValue = 0f;
        if (fcModel != null) {
            Map<String, Float> catFreq = fcModel.get(feature);
            if (catFreq != null) {
                Float aux = catFreq.get(category);
                if (aux != null) retValue = aux;
            }
        }
        return retValue;
    }

    /**
     * Return all categories used by the classifier.
     * @return
     */
    public Set<String> getAllCategories() {
        Set<String> retValue = null;
        if (ccModel != null) retValue = ccModel.keySet();
        return retValue;
    }

    /**
     * Returns the number of items/documents for a given category.
     *
     * @param category
     * @return
     */
    public Long getItemsInCategory(String category) {
        Long retValue = 0L;
        if (ccModel != null) {
            if (ccModel.containsKey(category)) {
                Long result = ccModel.get(category);
                retValue = (result != null) ? result : 0L;
            }
        }
        return retValue;
    }

    /**
     * Return the total number of items/documents in the model.
     *
     * @return The total number of items/documents that the cmodel has for every category.
     */
    public Long getTotalItems() {
        Long retValue = 0L;
        if (ccModel != null) {
            Iterator<Long> itValues = ccModel.values().iterator();
            while (itValues.hasNext()) retValue += itValues.next();
        }
        return retValue;
    }

    /**
     * Method that dumps interesting information for the model currently that the classifier has loaded.
     * @return Model report.
     */
    public String modelReport() {
        String retValue = null;
        if (fcModel != null && ccModel != null) {
            Map<String, Long> cc = ccModel;
            Map<String, Map<String, Float>> fc = fcModel;
            StringBuilder report = new StringBuilder();
            report.append(System.getProperty("line.separator"));
            report.append("CATEGORIES PRESENT IN MODEL AND THEIR ITEM FREQs");
            report.append(System.getProperty("line.separator"));
            report.append("================================================");
            report.append(System.getProperty("line.separator"));
            Set<String> cKeys = cc.keySet();
            Iterator<String> itCKeys = cKeys.iterator();
            while (itCKeys.hasNext()) {
                String category = itCKeys.next();
                report.append(category);
                report.append(":");
                report.append(cc.get(category).toString());
                report.append(System.getProperty("line.separator"));
            }
            report.append(System.getProperty("line.separator"));
            report.append("FEATURES AND THEIR FREQs IN EACH CATEGORY");
            report.append(System.getProperty("line.separator"));
            report.append("==========================================");
            report.append(System.getProperty("line.separator"));
            Set<String> featuresSet = fc.keySet();
            Iterator<String> itFeatures = featuresSet.iterator();
            while (itFeatures.hasNext()) {
                String feature = itFeatures.next();
                report.append(feature);
                report.append(System.getProperty("line.separator"));
                Map<String, Float> freqs = fc.get(feature);
                Set<String> cats = freqs.keySet();
                Iterator<String> itCats = cats.iterator();
                while (itCats.hasNext()) {
                    String category = itCats.next();
                    report.append("\t");
                    report.append(category);
                    report.append(":");
                    report.append(freqs.get(category));
                    report.append(System.getProperty("line.separator"));
                }
            }
            retValue = report.toString();
        }
        return retValue;
    }

    public Map<String, Float> getCatThresholds() {
        return catThresholds;
    }

    public void setCatThresholds(Map<String, Float> catThrVal) {
        catThresholds = catThrVal;
    }

    /**
     * Adds a threshold to the given category.
     *
     * @param category
     * @param threshold
     */
    public void addCategoryThreshold(String category, Float threshold) {
        if (catThresholds == null) catThresholds = new LinkedHashMap<String, Float>();
        catThresholds.put(category, threshold);
    }
}
