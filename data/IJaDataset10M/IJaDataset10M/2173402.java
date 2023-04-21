package org.linkedgeodata.osm.osmosis.plugins;

import java.awt.geom.Point2D;
import java.sql.SQLException;
import org.jboss.cache.util.DeltaBulkMap;
import org.linkedgeodata.dao.nodestore.INodePositionDao;
import org.linkedgeodata.scripts.LiveSync;
import org.linkedgeodata.util.IDiff;
import org.linkedgeodata.util.sparql.ISparulExecutor;
import org.linkedgeodata.util.sparql.cache.DeltaGraph;
import org.openstreetmap.osmosis.core.container.v0_6.ChangeContainer;
import org.openstreetmap.osmosis.core.domain.v0_6.Node;
import org.openstreetmap.osmosis.core.task.v0_6.ChangeSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.hp.hpl.jena.rdf.model.Model;

public class LiveDumpChangeSink implements ChangeSink {

    private static final Logger logger = LoggerFactory.getLogger(LiveDumpChangeSink.class);

    private IUpdateStrategy strategy;

    private int entityCount = 0;

    private int maxEntityCount = 1024;

    private DeltaBulkMap<Long, Point2D> nodePositionDao;

    private DeltaGraph deltaGraph;

    private long totalEntityCount = 0;

    public LiveDumpChangeSink(IUpdateStrategy strategy, DeltaGraph deltaGraph, DeltaBulkMap<Long, Point2D> nodePositionDao) {
        this.strategy = strategy;
        this.deltaGraph = deltaGraph;
        this.nodePositionDao = nodePositionDao;
    }

    public LiveDumpChangeSink(IUpdateStrategy strategy, DeltaGraph deltaGraph, DeltaBulkMap<Long, Point2D> nodePositionDao, int maxEntityCount) {
        this.strategy = strategy;
        this.deltaGraph = deltaGraph;
        this.nodePositionDao = nodePositionDao;
        this.maxEntityCount = maxEntityCount;
    }

    private void processBatch() {
        long start = System.nanoTime();
        strategy.complete();
        IDiff<Model> mainDiff = strategy.getMainGraphDiff();
        try {
            applyDiff(mainDiff);
            applyNodeDiff(strategy.getNodeDiff());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        strategy.release();
        totalEntityCount += entityCount;
        entityCount = 0;
        logger.info("" + ((System.nanoTime() - start) / 1000000000.0) + "Completed processing batch of " + entityCount + " entities (total: " + totalEntityCount + ")");
    }

    private void applyDiff(IDiff<Model> diff) throws Exception {
        deltaGraph.commit();
    }

    private void applyNodeDiff(TreeSetDiff<Node> diff) throws SQLException {
        nodePositionDao.commit();
    }

    @Override
    public void complete() {
        processBatch();
    }

    @Override
    public void release() {
    }

    @Override
    public void process(ChangeContainer ec) {
        strategy.process(ec);
        ++entityCount;
        if (entityCount >= maxEntityCount) {
            processBatch();
        }
    }
}
