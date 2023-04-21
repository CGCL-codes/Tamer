package uima.taes.interestingness;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import uima.taes.interestingness.graphHelpers.PhraseNetworkHelper;
import uima.types.PhrasePair;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.jcas.JFSIndexRepository;
import org.apache.uima.jcas.JCas;

public class TFIDFBodyTitlePhraseNENetworkBayesClassifier extends BodyTitlePhraseNENetworkBayesClassifier {

    protected HashMap relationshipDF = new HashMap();

    protected HashMap verticesDF = new HashMap();

    protected double numDocs = 0;

    protected void updateFrequencies(Set relationships, Set vertices) {
        for (Iterator itr = relationships.iterator(); itr.hasNext(); ) {
            String relationship = (String) itr.next();
            Integer count = null;
            if ((count = (Integer) relationshipDF.get(relationship)) != null) {
                count = new Integer(count.intValue() + 1);
            } else count = new Integer(1);
            relationshipDF.put(relationship, count);
        }
        for (Iterator itr = vertices.iterator(); itr.hasNext(); ) {
            String relationship = (String) itr.next();
            Integer count = null;
            if ((count = (Integer) relationshipDF.get(relationship)) != null) {
                count = new Integer(count.intValue() + 1);
            } else count = new Integer(1);
            relationshipDF.put(relationship, count);
        }
        numDocs++;
    }

    protected void updateFeatureVector(double[] featureVector, JCas jcas, String graphFilter, int offset) {
        JFSIndexRepository indexes = jcas.getJFSIndexRepository();
        FSIndex termIndex = indexes.getAnnotationIndex(PhrasePair.type);
        PhraseNetworkHelper helper = (PhraseNetworkHelper) graphs.get(graphFilter);
        HashMap relationships = new HashMap();
        HashMap vertices = new HashMap();
        double numRelationships = 0;
        for (Iterator itr1 = termIndex.iterator(); itr1.hasNext(); ) {
            PhrasePair ep = (PhrasePair) itr1.next();
            if (!ep.getSentenceType().equals(graphFilter)) continue;
            numRelationships++;
            String relationshipString = ep.getPhrase1() + "->" + ep.getPhrase2();
            Integer count = null;
            if ((count = (Integer) relationships.get(relationshipString)) != null) count = new Integer(count.intValue() + 1); else count = new Integer(1);
            relationships.put(relationshipString, count);
            if ((count = (Integer) vertices.get(ep.getPhrase1())) != null) count = new Integer(count.intValue() + 1); else count = new Integer(1);
            vertices.put(ep.getPhrase1(), count);
            if ((count = (Integer) vertices.get(ep.getPhrase2())) != null) count = new Integer(count.intValue() + 1); else count = new Integer(1);
            vertices.put(ep.getPhrase2(), count);
        }
        for (Iterator itr1 = termIndex.iterator(); itr1.hasNext(); ) {
            PhrasePair ep = (PhrasePair) itr1.next();
            for (int i = 0; i < order; i++) {
                featureVector[offset + i] += helper.getNumPathsOfLength(ep.getPhrase1(), ep.getPhrase2(), i + 1);
            }
            featureVector[offset + order + 0] += computeIDFRel(relationships, ep.getPhrase1(), ep.getPhrase2()) * helper.getBothNodeExistence(ep.getPhrase1(), ep.getPhrase2());
            featureVector[offset + order + 1] += computeIDFRel(relationships, ep.getPhrase1(), ep.getPhrase2()) * helper.getKatzScores(ep.getPhrase1(), ep.getPhrase2());
            featureVector[offset + order + 2] += computeIDFRel(relationships, ep.getPhrase1(), ep.getPhrase2()) * helper.getOnlyOneNodeExistence(ep.getPhrase1(), ep.getPhrase2());
            featureVector[offset + order + 3] += computeIDFRel(relationships, ep.getPhrase1(), ep.getPhrase2()) * helper.getDirectlyConnected(ep.getPhrase1(), ep.getPhrase2());
            featureVector[offset + order + 4] += computeIDFRel(relationships, ep.getPhrase1(), ep.getPhrase2()) * helper.getIndirectlyConnected(ep.getPhrase1(), ep.getPhrase2());
            featureVector[offset + order + 5] += computeIDFRel(relationships, ep.getPhrase1(), ep.getPhrase2()) * helper.getNoNodeExistence(ep.getPhrase1(), ep.getPhrase2());
        }
        for (int i = 0; i < order + 6; i++) {
            featureVector[offset + i] = featureVector[offset + i] / numRelationships;
        }
        featureVector[offset + order + 6] = helper.getNewEdges(relationships.keySet()) / ((double) relationships.size());
        featureVector[offset + order + 7] = helper.getNewVertices(vertices.keySet()) / ((double) vertices.size());
        featureVector[offset + order + 8] = helper.getOldEdges(relationships.keySet()) / ((double) relationships.size());
        featureVector[offset + order + 9] = helper.getOldVertices(vertices.keySet()) / ((double) vertices.size());
        featureVector[offset + order + 10] = relationships.size();
        featureVector[offset + order + 11] = vertices.size();
        offset += order + 12;
        for (int i = 0; i < FREQUENCY; i++) {
            featureVector[offset + i] = 0;
        }
        for (Iterator itr = relationships.keySet().iterator(); itr.hasNext(); ) {
            String relationshipString = (String) itr.next();
            Integer count = (Integer) relationships.get(relationshipString);
            if (count.intValue() > 10) continue;
            featureVector[offset + count.intValue() - 1]++;
        }
        offset += FREQUENCY;
        for (int i = 0; i < FREQUENCY; i++) {
            featureVector[offset + i] = 0;
        }
        for (Iterator itr = vertices.keySet().iterator(); itr.hasNext(); ) {
            String entity = (String) itr.next();
            Integer count = (Integer) vertices.get(entity);
            if (count.intValue() > 10) continue;
            featureVector[offset + count.intValue() - 1]++;
        }
        if (isInteresting(jcas)) {
            helper.updateRelationships(jcas, beta, graphFilter);
        } else helper.addUninterestingArticle(jcas);
        updateFrequencies(relationships.keySet(), vertices.keySet());
    }

    protected double computeIDFRel(HashMap relationships, String phrase1, String phrase2) {
        String relationshipString = phrase1 + "->" + phrase2;
        Integer count = (Integer) relationshipDF.get(relationshipString);
        return Math.log(numDocs / (count.doubleValue() + 1.0) + 1.0);
    }

    @Override
    protected void initializeFeatureSize() {
        graphFeatureSize = (order + 12) + FREQUENCY;
        featureSize = 4 * graphFeatureSize;
        bodyPhrase = new PhraseNetworkHelper(refreshRate, order, topK, true, maxCacheSize, maxGraphSize, minSupport, minConfidence);
        bodyNE = new PhraseNetworkHelper(refreshRate, order, topK, true, maxCacheSize, maxGraphSize, minSupport, minConfidence);
        titlePhrase = new PhraseNetworkHelper(refreshRate, order, topK, true);
        titleNE = new PhraseNetworkHelper(refreshRate, order, topK, true);
        graphs.put("bodyPhrase", bodyPhrase);
        graphs.put("bodyNE", bodyNE);
        graphs.put("titlePhrase", titlePhrase);
        graphs.put("titleNE", titleNE);
    }
}
