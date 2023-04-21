package net.sf.katta.master;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.sf.katta.AbstractTest;
import org.junit.Test;

public class LowestShardCountDistributionPolicyTest extends AbstractTest {

    private LowestShardCountDistributionPolicy _distributionPolicy = new LowestShardCountDistributionPolicy();

    Map<String, List<String>> _currentShard2NodesMap = new HashMap<String, List<String>>();

    Map<String, List<String>> _currentNode2ShardsMap = new HashMap<String, List<String>>();

    @Test
    public void testInitialDistribution() throws Exception {
        int replicationLevel = 2;
        List<String> nodes = createNodes("node1", "node2", "node3");
        Set<String> shards = createShards("shard1", "shard2");
        Map<String, List<String>> node2ShardsMap = _distributionPolicy.createDistributionPlan(_currentShard2NodesMap, _currentNode2ShardsMap, nodes, replicationLevel);
        assertEquals(nodes.size(), node2ShardsMap.size());
        assertSufficientDistribution(replicationLevel, nodes, shards, node2ShardsMap);
    }

    @Test
    public void testInitialDistribution_TooLessNodes() throws Exception {
        List<String> nodes = createNodes("node1");
        Set<String> shards = createShards("shard1", "shard2");
        Map<String, List<String>> node2ShardsMap = _distributionPolicy.createDistributionPlan(_currentShard2NodesMap, _currentNode2ShardsMap, nodes, 3);
        assertEquals(nodes.size(), node2ShardsMap.size());
        assertEquals(shards.size(), node2ShardsMap.get("node1").size());
    }

    @Test
    public void testUnderReplicatedDistribution() throws Exception {
        int replicationLevel = 3;
        List<String> nodes = createNodes("node1", "node2", "node3");
        Set<String> shards = createShards("shard1", "shard2", "shard3");
        addMapping("shard1", "node1", "node2", "node3");
        addMapping("shard2", "node1");
        Map<String, List<String>> node2ShardsMap = _distributionPolicy.createDistributionPlan(_currentShard2NodesMap, _currentNode2ShardsMap, nodes, replicationLevel);
        assertEquals(nodes.size(), node2ShardsMap.size());
        assertSufficientDistribution(replicationLevel, nodes, shards, node2ShardsMap);
    }

    @Test
    public void testOverReplicatedDistribution() throws Exception {
        int replicationLevel = 2;
        List<String> nodes = createNodes("node1", "node2", "node3", "node4");
        Set<String> shards = createShards("shard1", "shard2");
        addMapping("shard1", "node1", "node2", "node3", "node4");
        addMapping("shard2", "node1", "node2");
        Map<String, List<String>> node2ShardsMap = _distributionPolicy.createDistributionPlan(_currentShard2NodesMap, _currentNode2ShardsMap, nodes, replicationLevel);
        assertEquals(nodes.size(), node2ShardsMap.size());
        assertSufficientDistribution(replicationLevel, nodes, shards, node2ShardsMap);
    }

    private void assertSufficientDistribution(int replicationLevel, List<String> nodes, Set<String> shards, Map<String, List<String>> node2ShardsMap) {
        int deployedShardCount = 0;
        for (String node : nodes) {
            deployedShardCount += node2ShardsMap.get(node).size();
            if (deployedShardCount == 0) {
                continue;
            }
            assertTrue(node2ShardsMap.get(node).size() >= 1);
            assertTrue(node2ShardsMap.get(node).size() <= replicationLevel);
        }
        assertEquals(shards.size() * replicationLevel, deployedShardCount);
    }

    private void addMapping(String shard, String... nodes) {
        List<String> shardNodes = _currentShard2NodesMap.get(shard);
        for (String node : nodes) {
            List<String> shards = _currentNode2ShardsMap.get(node);
            shards.add(shard);
            shardNodes.add(node);
        }
    }

    private List<String> createNodes(String... nodeNames) {
        List<String> nodes = new ArrayList<String>();
        for (String nodeName : nodeNames) {
            nodes.add(nodeName);
            _currentNode2ShardsMap.put(nodeName, new ArrayList<String>());
        }
        return nodes;
    }

    private Set<String> createShards(String... shardNames) {
        Set<String> shards = new HashSet<String>();
        for (String shardName : shardNames) {
            shards.add(shardName);
            _currentShard2NodesMap.put(shardName, new ArrayList<String>());
        }
        return shards;
    }
}
