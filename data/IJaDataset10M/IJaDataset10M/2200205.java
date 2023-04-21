package edu.ucdavis.genomics.metabolomics.binbase.integration.standalone;

import edu.ucdavis.genomics.metabolomics.binbase.cluster.job.UpdateBinBaseJob;
import edu.ucdavis.genomics.metabolomics.binbase.cluster.node.CalculationNode;
import edu.ucdavis.genomics.metabolomics.binbase.cluster.util.Scheduler;
import edu.ucdavis.genomics.metabolomics.binbase.integration.cluster.BinBaseUpdateDatabaseCalculationTest;

public class BinBaseLocalNodeUpdateDatabaseCalculationTest extends BinBaseUpdateDatabaseCalculationTest {

    @Override
    protected void calculate() throws Exception {
        getLogger().info("creating node for calculation");
        CalculationNode node = new CalculationNode();
        node.setName(this.getClass().getName());
        node.setSingleRun(false);
        node.run();
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Override
    protected void scheduleUpdate(UpdateBinBaseJob job) throws Exception {
        Scheduler.schedule(job);
    }
}
