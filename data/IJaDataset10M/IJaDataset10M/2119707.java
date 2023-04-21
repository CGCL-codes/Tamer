package de.fuberlin.wiwiss.r2r.discovery;

import de.fuberlin.wiwiss.r2r.MetadataRepository;
import de.fuberlin.wiwiss.r2r.R2R;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.util.List;

public class MappingDiscovery {

    private DatasetChecker datasetCheck;

    private MetadataRepository repository;

    private MetaDataCatcher metaDataCatcher;

    public MappingDiscovery(DatasetChecker datasetCheck, MetadataRepository repository) {
        super();
        this.datasetCheck = datasetCheck;
        this.repository = repository;
        metaDataCatcher = new MetaDataCatcher(repository);
    }

    public DependencyGraph buildDependencyGraph(String targetVocabularyTerm, String sourceDataset, String targetDataset, int maxDepth) {
        Set<VocabularyNode> sourceNodes = new HashSet<VocabularyNode>();
        LinkedList<VocabularyNode> openNodes = new LinkedList<VocabularyNode>();
        Map<String, VocabularyNode> nodes = new HashMap<String, VocabularyNode>();
        VocabularyNode root = initRootNode(targetVocabularyTerm);
        nodes.put(targetVocabularyTerm, root);
        openNodes.add(root);
        while (!openNodes.isEmpty()) {
            VocabularyNode node = openNodes.poll();
            int depth = node.getDepth() + 1;
            if (depth > maxDepth) break;
            expand(targetVocabularyTerm, node, sourceNodes, nodes, openNodes, depth);
        }
        return new DependencyGraph(root, targetVocabularyTerm, sourceDataset, targetDataset, nodes, sourceNodes, metaDataCatcher);
    }

    public MappingChain getMappingChain(String targetVocabularyElement, String sourceDataset, String targetDataset, int maxDepth) {
        DependencyGraph dGraph = buildDependencyGraph(targetVocabularyElement, sourceDataset, targetDataset, maxDepth);
        dGraph.assertSatisfiability();
        dGraph.removeUnsatisfiableNodesAndMappings();
        dGraph.removeIrrelevantNodes();
        dGraph.createMappingClustersAndDependencies();
        return dGraph.buildBestMappingComposition(maxDepth, datasetCheck);
    }

    /**
	 * Get all the mapping chains for the given target vocabulary definition
	 * @param targetVocabDefinition The (discovery) target vocabulary definition string
	 * @param sourceDataset The source dataset URI or null if unknown
	 * @param maxDepth the maximum depth of the search graph from the target term node
	 * @return a collection of mapping chains that can be executed against the/a source dataset
	 */
    public Collection<MappingChain> getMappingChains(String targetVocabDefinition, String sourceDataset, int maxDepth) {
        Collection<DiscoveryTargetVocabulary> vocabDefs = DiscoveryTargetVocabulary.parse(targetVocabDefinition);
        List<MappingChain> mappingChains = new ArrayList<MappingChain>();
        for (DiscoveryTargetVocabulary dtv : vocabDefs) for (Map.Entry<String, String> termDataset : dtv.getTermDatasetPairs().entrySet()) mappingChains.add(getMappingChain(termDataset.getKey(), sourceDataset, termDataset.getValue(), maxDepth));
        return mappingChains;
    }

    private void expand(String vocabularyElement, VocabularyNode node, Set<VocabularyNode> sourceNodes, Map<String, VocabularyNode> nodes, LinkedList<VocabularyNode> openNodes, int depth) {
        for (Map.Entry<String, MappingMetaData> mappingData : node.getMappings().entrySet()) {
            Set<String> dependencies = mappingData.getValue().getValuesForProperty(R2R.dependsOn);
            for (String dependency : dependencies) {
                if (nodes.get(dependency) == null) {
                    VocabularyNode nextNode = null;
                    if (datasetCheck.containsTargetElement(dependency)) {
                        nextNode = createSourceNode(dependency);
                        sourceNodes.add(nextNode);
                    } else {
                        nextNode = createInnerNode(vocabularyElement, dependency);
                        openNodes.add(nextNode);
                    }
                    nextNode.setDepth(depth);
                    nodes.put(dependency, nextNode);
                }
                nodes.get(dependency).addConsument(node);
            }
        }
    }

    private VocabularyNode createInnerNode(String vocabularyElement, String dependency) {
        Set<String> depMappings = repository.getMappingsOfTargetElement(dependency);
        Map<String, MappingMetaData> depMappingsMetadata = new HashMap<String, MappingMetaData>();
        for (String mapping : depMappings) {
            MappingMetaData mmd = metaDataCatcher.getMetaDataForMapping(mapping);
            Set<String> dependencies = mmd.getValuesForProperty(R2R.dependsOn);
            if (dependencies != null && (!dependencies.contains(vocabularyElement))) depMappingsMetadata.put(mapping, mmd);
        }
        return new VocabularyNode(dependency, depMappingsMetadata);
    }

    private VocabularyNode createSourceNode(String dependency) {
        VocabularyNode nextNode = new VocabularyNode(dependency, null);
        nextNode.setSatisfiable(true);
        nextNode.setMinDistanceToSourceDataset(0);
        return nextNode;
    }

    private VocabularyNode initRootNode(String vocabularyElement) {
        Set<String> rootMappings = repository.getMappingsOfTargetElement(vocabularyElement);
        Map<String, MappingMetaData> rootMappingsMetadata = new HashMap<String, MappingMetaData>();
        for (String mapping : rootMappings) {
            MappingMetaData mmd = metaDataCatcher.getMetaDataForMapping(mapping);
            if (mmd.getValuesForProperty(R2R.dependsOn) != null) rootMappingsMetadata.put(mapping, mmd);
        }
        VocabularyNode root = new VocabularyNode(vocabularyElement, rootMappingsMetadata);
        root.setDepth(0);
        return root;
    }
}
