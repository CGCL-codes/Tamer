package org.vardb.analysis;

import java.util.*;
import com.google.common.collect.*;
import org.vardb.sequences.dao.*;

public class CFeatureTable {

    protected Multimap<Integer, CFeature> m_features = LinkedHashMultimap.create();

    public Multimap<Integer, CFeature> getFeatures() {
        return m_features;
    }

    public Collection<CFeature> getFeatures(int sequence_id) {
        Collection<CFeature> features = m_features.get(sequence_id);
        if (features == null) return new ArrayList<CFeature>();
        return features;
    }

    public void put(int sequence_id, CFeature feature) {
        m_features.put(sequence_id, feature);
    }
}
