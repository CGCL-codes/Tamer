package net.sf.katta.integrationTest;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import net.sf.katta.integrationTest.support.AbstractIntegrationTest;
import net.sf.katta.lib.lucene.LuceneClient;
import net.sf.katta.node.Node;
import net.sf.katta.operation.node.ShardUndeployOperation;
import net.sf.katta.protocol.InteractionProtocol;
import net.sf.katta.testutil.TestUtil;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class NodeIntegrationTest extends AbstractIntegrationTest {

    public NodeIntegrationTest() {
        super(2);
    }

    @Test
    public void testDeployShardAfterRestart() throws Exception {
        deployTestIndices(1, getNodeCount());
        final InteractionProtocol protocol = _miniCluster.getProtocol();
        assertEquals(1, protocol.getIndices().size());
        Collection<String> deployedShards = protocol.getNodeShards(_miniCluster.getNode(0).getName());
        assertFalse(deployedShards.isEmpty());
        Node node = _miniCluster.restartNode(0);
        assertEquals(deployedShards, protocol.getNodeShards(node.getName()));
    }

    @Test
    public void testUndeployShard() throws Exception {
        deployTestIndices(1, getNodeCount());
        final InteractionProtocol protocol = _miniCluster.getProtocol();
        assertEquals(1, protocol.getIndices().size());
        Node node = _miniCluster.getNode(0);
        TestUtil.waitUntilNodeServesShards(protocol, node.getName(), SHARD_COUNT);
        File shardsFolder = node.getContext().getShardManager().getShardsFolder();
        assertEquals(SHARD_COUNT, shardsFolder.list().length);
        ShardUndeployOperation undeployOperation = new ShardUndeployOperation(Arrays.asList(protocol.getNodeShards(node.getName()).iterator().next()));
        protocol.addNodeOperation(node.getName(), undeployOperation);
        TestUtil.waitUntilNodeServesShards(protocol, node.getName(), 3);
        assertEquals(3, shardsFolder.list().length);
    }

    @Test
    public void testContentServer() throws Exception {
        deployTestIndices(1, getNodeCount());
        final InteractionProtocol protocol = _miniCluster.getProtocol();
        assertEquals(1, protocol.getIndices().size());
        LuceneClient luceneClient = new LuceneClient(_miniCluster.getZkConfiguration());
        final Query query = new QueryParser(Version.LUCENE_30, "", new KeywordAnalyzer()).parse("content: the");
        luceneClient.count(query, new String[] { INDEX_NAME });
        luceneClient.close();
    }
}
