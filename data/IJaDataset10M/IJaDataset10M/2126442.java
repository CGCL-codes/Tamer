package org.mooym.moody;

import jAudioFeatureExtractor.DataModel;
import jAudioFeatureExtractor.ACE.DataTypes.FeatureDefinition;
import jAudioFeatureExtractor.ACE.XMLParsers.XMLDocumentParser;
import jAudioFeatureExtractor.Aggregators.Aggregator;
import jAudioFeatureExtractor.DataTypes.RecordingInfo;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.mooym.incident.FeedbackManagerFactory;
import org.mooym.incident.FeedbackManager;
import org.mooym.mp3.AudioRecord;

/**
 * Wrapper class for the jAudio API.
 * 
 * @author roesslerj
 */
public class JAudioWrapper {

    private final DataModel jAudioFeatureExtractor;

    private int windowSize;

    private double windowOverlap;

    private double samplingRate;

    private boolean normalise;

    private boolean perWindowStats;

    private boolean overallStats;

    private RecordingInfo[] info;

    private int arff;

    private static final FeedbackManager feedback = FeedbackManagerFactory.getInstance(JAudioWrapper.class);

    public JAudioWrapper(String featureFile) {
        jAudioFeatureExtractor = new DataModel(featureFile, null);
    }

    public void loadSettings(String settingsFile) throws Exception {
        Object[] tmp = (Object[]) XMLDocumentParser.parseXMLDocument(settingsFile, "save_settings");
        windowSize = Integer.valueOf((String) tmp[0]);
        windowOverlap = Double.valueOf((String) tmp[1]);
        samplingRate = ((Double) tmp[2]).doubleValue();
        normalise = ((Boolean) tmp[3]).booleanValue();
        perWindowStats = ((Boolean) tmp[4]).booleanValue();
        overallStats = ((Boolean) tmp[5]).booleanValue();
        arff = tmp[6].equals("ACE") ? 0 : 1;
        configureFeatures(tmp);
        configureAggregators(tmp);
    }

    private void configureFeatures(Object[] tmp) {
        HashMap<String, Boolean> checked = (HashMap<String, Boolean>) tmp[7];
        HashMap<String, String[]> featureAttributes = (HashMap<String, String[]>) tmp[8];
        for (String name : featureAttributes.keySet()) {
            if (checked.containsKey(name)) {
                int featureIdx = getFeatureIndex(name);
                jAudioFeatureExtractor.defaults[featureIdx] = true;
                String[] fa = featureAttributes.get(name);
                for (int j = 0; j < fa.length; ++j) {
                    try {
                        jAudioFeatureExtractor.features[featureIdx].setElement(j, fa[j]);
                    } catch (Exception exc) {
                        feedback.registerBug("Exception setting attributes of feature %s.", name, exc);
                    }
                }
            }
        }
    }

    private int getFeatureIndex(String name) {
        for (int idx = 0; idx < jAudioFeatureExtractor.featureDefinitions.length; idx++) {
            FeatureDefinition featureDefinition = jAudioFeatureExtractor.featureDefinitions[idx];
            if (featureDefinition.name.equals(name)) {
                return idx;
            }
        }
        throw new IllegalArgumentException("Given feature " + name + " not contained in feature definitions!");
    }

    private void configureAggregators(Object[] tmp) throws Exception {
        LinkedList<String> aggNames = (LinkedList<String>) tmp[9];
        LinkedList<String[]> aggFeatures = (LinkedList<String[]>) tmp[10];
        LinkedList<String[]> aggParameters = (LinkedList<String[]>) tmp[11];
        LinkedList<Aggregator> dest = new LinkedList<Aggregator>();
        Iterator<String> namesIter = aggNames.iterator();
        Iterator<String[]> featuresIter = aggFeatures.iterator();
        Iterator<String[]> parametersIter = aggParameters.iterator();
        while (namesIter.hasNext()) {
            Aggregator agg = jAudioFeatureExtractor.aggregatorMap.get(namesIter.next());
            agg.setParameters(featuresIter.next(), parametersIter.next());
            dest.add(agg);
        }
        jAudioFeatureExtractor.aggregators = dest.toArray(new Aggregator[] {});
    }

    public void loadRecordings(List<AudioRecord> files) {
        info = new RecordingInfo[files.size()];
        int fileIdx = 0;
        for (AudioRecord file : files) {
            if (canceled) {
                return;
            }
            info[fileIdx++] = new RecordingInfo(file.getFilePath());
        }
    }

    public void extractFeatures(OutputStream destination) throws Exception {
        jAudioFeatureExtractor.featureValue = new DataOutputStream(destination);
        jAudioFeatureExtractor.extract(windowSize, windowOverlap, samplingRate, normalise, perWindowStats, overallStats, info, arff);
    }

    private boolean canceled = false;

    public void cancel() {
        canceled = true;
        if (jAudioFeatureExtractor != null) {
            jAudioFeatureExtractor.cancel_.setCancel(true);
        }
    }
}
