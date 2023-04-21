package edu.yale.cs.hadoopdb.catalog.xml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class ConfigurationMapping {

    private static ConfigurationMapping singleton;

    private Configuration dbConfig;

    private Map<String, Map<String, List<Node>>> relationPartitionNodeMap;

    private Map<Node, Map<String, Map<String, Partition>>> nodeRelationPartitionMap;

    public static ConfigurationMapping getInstance() {
        return singleton;
    }

    public static ConfigurationMapping getInstance(InputStream configuration_file) throws JAXBException {
        if (singleton == null) {
            singleton = new ConfigurationMapping(configuration_file);
        }
        return singleton;
    }

    @SuppressWarnings("unchecked")
    private ConfigurationMapping(InputStream configuration_file) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance("edu.yale.cs.hadoopdb.catalog.xml");
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        dbConfig = (Configuration) ((JAXBElement) unmarshaller.unmarshal(configuration_file)).getValue();
    }

    private void updateRelationMappings() {
        relationPartitionNodeMap = new HashMap<String, Map<String, List<Node>>>();
        for (Node node : dbConfig.getNodes()) {
            for (Relation relation : node.getRelations()) {
                Map<String, List<Node>> partitionNodeMap;
                if (relationPartitionNodeMap.containsKey(relation.getId().toLowerCase().trim())) {
                    partitionNodeMap = relationPartitionNodeMap.get(relation.getId().toLowerCase().trim());
                } else {
                    partitionNodeMap = new HashMap<String, List<Node>>();
                }
                for (Partition partition : relation.getPartitions()) {
                    if (partitionNodeMap.containsKey(partition.getId().toLowerCase().trim())) {
                        partitionNodeMap.get(partition.getId().toLowerCase().trim()).add(node);
                    } else {
                        List<Node> n = new ArrayList<Node>();
                        n.add(node);
                        partitionNodeMap.put(partition.getId().trim().toLowerCase(), n);
                    }
                }
                relationPartitionNodeMap.put(relation.getId().trim().toLowerCase(), partitionNodeMap);
            }
        }
    }

    public List<Node> getNodesForRelation(String relation_id) {
        if (relationPartitionNodeMap == null) {
            updateRelationMappings();
        }
        if (relationPartitionNodeMap.containsKey(relation_id.toLowerCase().trim())) {
            List<Node> relationNodes = new ArrayList<Node>();
            for (List<Node> n : relationPartitionNodeMap.get(relation_id.toLowerCase().trim()).values()) {
                relationNodes.addAll(n);
            }
            return relationNodes;
        } else return null;
    }

    public Map<String, List<Node>> getPartitionsForRelation(String relation_id) {
        if (relationPartitionNodeMap == null) {
            updateRelationMappings();
        }
        if (relationPartitionNodeMap.containsKey(relation_id.toLowerCase().trim())) {
            return relationPartitionNodeMap.get(relation_id.toLowerCase().trim());
        } else return null;
    }

    public List<Node> getNodesForRelationAndPartition(String relation_id, String partition_id) {
        if (relationPartitionNodeMap == null) updateRelationMappings();
        if (relationPartitionNodeMap.containsKey(relation_id.toLowerCase().trim())) {
            if (relationPartitionNodeMap.get(relation_id.toLowerCase().trim()).containsKey(partition_id.toLowerCase().trim())) return relationPartitionNodeMap.get(relation_id.toLowerCase().trim()).get(partition_id.toLowerCase().trim());
        }
        return null;
    }

    private void updateNodeMappings() {
        nodeRelationPartitionMap = new HashMap<Node, Map<String, Map<String, Partition>>>();
        for (Node node : dbConfig.getNodes()) {
            Map<String, Map<String, Partition>> relationPartitionMap = new HashMap<String, Map<String, Partition>>();
            for (Relation relation : node.getRelations()) {
                Map<String, Partition> partitionMap;
                if (relationPartitionMap.containsKey(relation.getId().toLowerCase().trim())) partitionMap = relationPartitionMap.get(relation.getId().toLowerCase().trim()); else partitionMap = new HashMap<String, Partition>();
                for (Partition partition : relation.getPartitions()) {
                    partitionMap.put(partition.getId().toLowerCase().trim(), partition);
                }
                relationPartitionMap.put(relation.getId().toLowerCase().trim(), partitionMap);
            }
            nodeRelationPartitionMap.put(node, relationPartitionMap);
        }
    }

    public Partition getPartitionForNodeRelation(Node n, String relation_id, String partition_id) {
        if (nodeRelationPartitionMap == null) updateNodeMappings();
        if (nodeRelationPartitionMap.containsKey(n)) {
            if (nodeRelationPartitionMap.get(n).containsKey(relation_id.toLowerCase().trim())) {
                Map<String, Partition> partitionMap = nodeRelationPartitionMap.get(n).get(relation_id.toLowerCase().trim());
                if (partitionMap.containsKey(partition_id.toLowerCase().trim())) {
                    return partitionMap.get(partition_id.toLowerCase().trim());
                }
            }
        }
        return null;
    }

    public void printNodeMap() {
        for (Node n : nodeRelationPartitionMap.keySet()) {
            System.out.println(n.getLocation() + ":");
            for (String rel : nodeRelationPartitionMap.get(n).keySet()) {
                System.out.println("\t" + rel + ":");
                for (String part_id : nodeRelationPartitionMap.get(n).get(rel).keySet()) System.out.println("\t\t" + part_id);
                for (Partition part : nodeRelationPartitionMap.get(n).get(rel).values()) System.out.println("\t\t" + part.getUrl());
            }
        }
    }
}
