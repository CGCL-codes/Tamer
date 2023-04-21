package com.mapquest.spatialbase.lucene.search;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import com.mapquest.spatialbase.lucene.cache.SidCache;
import com.mapquest.spatialbase.lucene.cache.SidCacheVersion;
import com.mapquest.spatialbase.lucene.cache.SidData;
import com.mapquest.spatialbase.lucene.cache.SidDataCursor;
import com.mapquest.spatialbase.lucene.cache.SidPartition;

/**
 * Helper class for assembling a spatial query
 * @author tlaurenzomq
 *
 */
public class SpatialQueryBuilder {

    private SpatialConfig config = SpatialConfig.DEFAULT;

    private IndexReader reader;

    private Set<String> spatialIds = new HashSet<String>();

    private CandidateScorer candidateScorer = CandidateScorer.DEFAULT;

    private SidCache cache;

    public SpatialQueryBuilder(IndexReader reader) {
        this.reader = reader;
    }

    /**
	 * If a cache is set, it will be used.  If not, direct traversal of the
	 * Lucene index is used.
	 * @param cache
	 */
    public void setCache(SidCache cache) {
        this.cache = cache;
    }

    public void addSpatialIds(Collection<String> sids) {
        spatialIds.addAll(sids);
    }

    public void addSpatialIds(String... sids) {
        addSpatialIds(Arrays.asList(sids));
    }

    public void setCandidateScorer(CandidateScorer candidateScorer) {
        this.candidateScorer = candidateScorer;
    }

    public CandidateScorer getCandidateScorer() {
        return candidateScorer;
    }

    public void setConfig(SpatialConfig config) {
        this.config = config;
    }

    public SpatialConfig getConfig() {
        return config;
    }

    /**
	 * Create a SpatialQuery.  This *must* be close()'d when not in use to avoid
	 * resource leaks.
	 * @return new SpatialQuery
	 * @throws IOException 
	 */
    public SpatialQuery create(Query origQuery) throws IOException {
        SidCursor cursors[] = new SidCursor[spatialIds.size()];
        int index = 0;
        boolean success = false;
        try {
            for (String spatialId : spatialIds) {
                cursors[index++] = allocateSidCursor(spatialId);
            }
            success = true;
        } finally {
            if (!success) {
                for (SidCursor c : cursors) if (c != null) c.close();
            }
        }
        CompositeSidCursor sidCursor = new CompositeSidCursor(cursors);
        SpatialCandidateQuery candidateQuery = new SpatialCandidateQuery(sidCursor, spatialIds.toArray(new String[spatialIds.size()]));
        BooleanQuery boolQuery;
        boolQuery = new BooleanQuery();
        if (origQuery != null) {
            boolQuery.add(origQuery, BooleanClause.Occur.MUST);
        }
        candidateQuery.setBoost(0.0f);
        boolQuery.add(candidateQuery, BooleanClause.Occur.MUST);
        return new SpatialQuery(candidateScorer, sidCursor, boolQuery);
    }

    /**
	 * Allocate a Sid Cursor for a spatial id
	 * @param spatialId
	 * @return
	 * @throws IOException 
	 */
    protected SidCursor allocateSidCursor(String spatialId) throws IOException {
        if (cache != null) {
            SidCacheVersion scv = cache.open(reader);
            SidPartition partition = scv.openPartition(spatialId, reader);
            SidData sidData = partition.getSidData();
            return new SidDataCursor(sidData);
        } else {
            return new TermDocsSidCursor(config, reader, spatialId);
        }
    }
}
