package uima.taes.interestingness;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.jcas.JCas;
import uima.taes.interestingness.graphHelpers.ClusterEntityGraphHelper;
import uima.types.Cluster;
import uima.types.NamedEntity;
import uima.types.NewsItem;

public abstract class CEGraphInterestingness extends GraphInterestingness {

    @Override
    protected void initializeDataStructures() {
        helper = new ClusterEntityGraphHelper();
    }

    protected HashMap<?, Double> getCandidates(NewsItem newsItem, JCas jcas) {
        HashMap<Object, Double> candidateCounts = new HashMap<Object, Double>();
        FSIndex entityIndex = jcas.getJFSIndexRepository().getAnnotationIndex(NamedEntity.type);
        for (Iterator<NamedEntity> itr = entityIndex.iterator(); itr.hasNext(); ) {
            NamedEntity entity = itr.next();
            String entityString = entity.getEntity();
            double weight = 1;
            if (entity.getLocation().equals("title")) weight = 2;
            if (candidateCounts.containsKey(entityString)) {
                candidateCounts.put(entityString, candidateCounts.get(entityString) + weight);
            } else candidateCounts.put(entityString, weight);
        }
        HashSet<String> remove = new HashSet<String>();
        for (Object entity : candidateCounts.keySet()) {
            if (candidateCounts.get(entity) <= 1) remove.add((String) entity);
        }
        for (String entity : remove) {
            candidateCounts.remove(entity);
        }
        FSIndex topicIndex = jcas.getJFSIndexRepository().getAnnotationIndex(Cluster.type);
        for (Iterator<Cluster> itr = topicIndex.iterator(); itr.hasNext(); ) {
            Cluster topic = itr.next();
            int topicID = topic.getClusterID();
            double similarity = topic.getSimilarity();
            candidateCounts.put(topicID, similarity);
        }
        return candidateCounts;
    }

    protected void updateEdges(HashMap<?, Double> candidates) {
        HashSet<String> seen = new HashSet<String>();
        for (Object obj1 : candidates.keySet()) {
            if (obj1 instanceof String) continue;
            int topicID = (Integer) obj1;
            HashSet<Object> set = new HashSet<Object>();
            for (Object obj2 : candidates.keySet()) {
                if (obj1 instanceof Integer) continue;
                String entityString = (String) obj2;
                if (!seen.contains(entityString + "/" + topicID)) {
                    helper.addEdge(topicID, entityString);
                    set.add(entityString);
                }
            }
            helper.markEdges(topicID, set);
        }
    }
}
