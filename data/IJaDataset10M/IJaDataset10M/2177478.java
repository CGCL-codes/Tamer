package de.sonivis.tool.textmining.test;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.Assert;
import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import de.sonivis.tool.textmining.operations.list.EventListFilter;
import de.sonivis.tool.textmining.operations.list.EventListGrouper;
import de.sonivis.tool.textmining.operations.list.EventListSorter;
import de.sonivis.tool.textmining.operations.list.EventListSummarizer;
import de.sonivis.tool.textmining.operations.matrix.EMatrixOperation;
import de.sonivis.tool.textmining.operations.matrix.EMatrixOperationsGrouped;
import de.sonivis.tool.textmining.representation.list.ETermAttributes;
import de.sonivis.tool.textmining.representation.list.TermListFactory;
import de.sonivis.tool.textmining.representation.matrix.RDataMatrix;
import de.sonivis.tool.textmining.representation.matrix.RDistanceMatrix;
import de.sonivis.tool.textmining.representation.matrix.RDistanceMatrixClustering;

/**
 * This class includes some help functions for the text mining tests.
 * 
 * @author Nette
 * @version $Revision$, $Date$
 */
public class TextMiningOperations {

    /**
	 * Initialize a list.
	 */
    public static final void initTermList(final String termListName) {
        final List<Object[]> lsTermAttributes = new ArrayList<Object[]>();
        lsTermAttributes.add(new Object[] { "Test1", 2L, 0L, 2L, 1L, "page2", new Date(0), "actor2", "term1", 10, 0, 10, 10 });
        lsTermAttributes.add(new Object[] { "Test1", 2L, 0L, 5L, 1L, "page2", new Date(0), "actor5", "term1", 3, 5, -2, 8 });
        lsTermAttributes.add(new Object[] { "Test1", 2L, 0L, 6L, 1L, "page2", new Date(0), "actor6", "term1", 0, 1, -1, 1 });
        lsTermAttributes.add(new Object[] { "Test1", 7L, 0L, 2L, 3L, "page7", new Date(0), "actor2", "term3", 5, 4, 1, 9 });
        lsTermAttributes.add(new Object[] { "Test1", 4L, 0L, 3L, 4L, "page4", new Date(0), "actor3", "term4", 0, 8, -8, 8 });
        lsTermAttributes.add(new Object[] { "Test1", 6L, 0L, 5L, 4L, "page6", new Date(0), "actor5", "term4", 3, 2, 1, 5 });
        lsTermAttributes.add(new Object[] { "Test1", 7L, 0L, 5L, 4L, "page7", new Date(0), "actor5", "term4", 0, 3, -3, 3 });
        lsTermAttributes.add(new Object[] { "Test2", 4L, 0L, 2L, 6L, "page4", new Date(0), "actor2", "term6", 9, 0, 9, 9 });
        lsTermAttributes.add(new Object[] { "Test2", 6L, 0L, 2L, 6L, "page6", new Date(0), "actor2", "term6", 5, 7, -2, 12 });
        lsTermAttributes.add(new Object[] { "Test2", 5L, 0L, 4L, 6L, "page5", new Date(0), "actor4", "term6", 8, 5, 3, 13 });
        lsTermAttributes.add(new Object[] { "Test2", 6L, 0L, 5L, 6L, "page6", new Date(0), "actor5", "term6", 0, 1, -1, 1 });
        lsTermAttributes.add(new Object[] { "Test2", 7L, 0L, 6L, 6L, "page7", new Date(0), "actor6", "term6", 9, 0, 9, 9 });
        lsTermAttributes.add(new Object[] { "Test2", 5L, 0L, 7L, 6L, "page5", new Date(0), "actor7", "term6", 9, 10, -1, 19 });
        lsTermAttributes.add(new Object[] { "Test2", 3L, 0L, 2L, 7L, "page3", new Date(0), "actor2", "term7", 0, 10, -10, 10 });
        final EventList<Map<ETermAttributes, Object>> termList = new BasicEventList<Map<ETermAttributes, Object>>();
        for (final Object[] arrTermAttr : lsTermAttributes) {
            final Map<ETermAttributes, Object> termAttr = new HashMap<ETermAttributes, Object>();
            termAttr.put(ETermAttributes.TERM_TYPE, arrTermAttr[0]);
            termAttr.put(ETermAttributes.PAGE_ID, arrTermAttr[1]);
            termAttr.put(ETermAttributes.REV_ID, arrTermAttr[2]);
            termAttr.put(ETermAttributes.ACTOR_ID, arrTermAttr[3]);
            termAttr.put(ETermAttributes.TERM_ID, arrTermAttr[4]);
            termAttr.put(ETermAttributes.PAGE_NAME, arrTermAttr[5]);
            termAttr.put(ETermAttributes.REV_CREATED, arrTermAttr[6]);
            termAttr.put(ETermAttributes.ACTOR_NAME, arrTermAttr[7]);
            termAttr.put(ETermAttributes.TERM_NAME, arrTermAttr[8]);
            termAttr.put(ETermAttributes.ADDS, arrTermAttr[9]);
            termAttr.put(ETermAttributes.DELETES, arrTermAttr[10]);
            termAttr.put(ETermAttributes.SUM, arrTermAttr[11]);
            termAttr.put(ETermAttributes.ABSSUM, arrTermAttr[12]);
            termList.add(termAttr);
        }
        TermListFactory.loadCombinedTermList(termListName, new ArrayList<String>());
        TermListFactory.getCombinedTermList(termListName).addAll(termList);
        clearSettings(termListName);
    }

    public static void clearSettings(final String termListName) {
        TermListFactory.getCombinedFilteredTermList(termListName).setMatcherEditor(EventListFilter.defineFilter(new HashMap<ETermAttributes, String[]>()));
        TermListFactory.getCombinedSummarizedTermList(termListName).setSummarizer(new EventListSummarizer<ETermAttributes>(new ArrayList<ETermAttributes>()));
        TermListFactory.getCombinedGroupedTermList(termListName).setComparator(new EventListGrouper<ETermAttributes>(new ArrayList<ETermAttributes>()));
        TermListFactory.getCombinedSortedTermList(termListName).setComparator(new EventListSorter<ETermAttributes>(ETermAttributes.PAGE_NAME, EventListSorter.SORT_NONE));
    }

    public static boolean compareTermAttributes(final Map<ETermAttributes, Object> attr, final Object termType, final Object pageId, final Object revId, final Object actorId, final Object termId, final Object pageName, final Object revCreated, final Object actorName, final Object termName, final Integer adds, final Integer deletes) {
        Assert.assertEquals(termType, attr.get(ETermAttributes.TERM_TYPE));
        Assert.assertEquals(pageId, attr.get(ETermAttributes.PAGE_ID));
        Assert.assertEquals(revId, attr.get(ETermAttributes.REV_ID));
        Assert.assertEquals(actorId, attr.get(ETermAttributes.ACTOR_ID));
        Assert.assertEquals(termId, attr.get(ETermAttributes.TERM_ID));
        Assert.assertEquals(pageName, attr.get(ETermAttributes.PAGE_NAME));
        Assert.assertEquals(revCreated, attr.get(ETermAttributes.REV_CREATED));
        Assert.assertEquals(actorName, attr.get(ETermAttributes.ACTOR_NAME));
        Assert.assertEquals(termName, attr.get(ETermAttributes.TERM_NAME));
        Assert.assertEquals(adds, attr.get(ETermAttributes.ADDS));
        Assert.assertEquals(deletes, attr.get(ETermAttributes.DELETES));
        Assert.assertEquals(adds - deletes, attr.get(ETermAttributes.SUM));
        Assert.assertEquals(adds + deletes, attr.get(ETermAttributes.ABSSUM));
        return attr.get(ETermAttributes.TERM_TYPE).equals(termType) && attr.get(ETermAttributes.PAGE_ID).equals(pageId) && attr.get(ETermAttributes.REV_ID).equals(revId) && attr.get(ETermAttributes.ACTOR_ID).equals(actorId) && attr.get(ETermAttributes.TERM_ID).equals(termId) && attr.get(ETermAttributes.PAGE_NAME).equals(pageName) && attr.get(ETermAttributes.REV_CREATED).equals(revCreated) && attr.get(ETermAttributes.ACTOR_NAME).equals(actorName) && attr.get(ETermAttributes.TERM_NAME).equals(termName) && attr.get(ETermAttributes.ADDS).equals(adds) && attr.get(ETermAttributes.DELETES).equals(deletes) && attr.get(ETermAttributes.SUM).equals(adds - deletes) && attr.get(ETermAttributes.ABSSUM).equals(adds + deletes);
    }

    public static boolean compareTermAttributes(final Map<ETermAttributes, Object> attr1, final Map<ETermAttributes, Object> attr2) {
        boolean equal = false;
        if (attr1 == attr2) {
            equal = true;
        } else {
            compareTermAttributes(attr1, attr2.get(ETermAttributes.TERM_TYPE), attr2.get(ETermAttributes.PAGE_ID), attr2.get(ETermAttributes.REV_ID), attr2.get(ETermAttributes.ACTOR_ID), attr2.get(ETermAttributes.TERM_ID), attr2.get(ETermAttributes.PAGE_NAME), attr2.get(ETermAttributes.REV_CREATED), attr2.get(ETermAttributes.ACTOR_NAME), attr2.get(ETermAttributes.TERM_NAME), (Integer) attr2.get(ETermAttributes.ADDS), (Integer) attr2.get(ETermAttributes.DELETES));
        }
        return equal;
    }

    /**
	 * Initialize a data matrix.
	 */
    public static final void initDataMatrix(final String matrixName) {
        TextMiningOperations.initTermList(matrixName + "List");
        final RDataMatrix rDataMatrix = new RDataMatrix(matrixName);
        rDataMatrix.cleanUp();
        rDataMatrix.createMatrixFromList(TermListFactory.getCombinedFilteredTermList(matrixName + "List"), ETermAttributes.PAGE_NAME, ETermAttributes.TERM_NAME, ETermAttributes.SUM);
    }

    /**
	 * Initialize a distance matrix.
	 */
    public static final void initDistanceMatrix(final String matrixName) {
        TextMiningOperations.initDataMatrix(matrixName + "Data");
        final RDistanceMatrix rDistanceMatrix = new RDistanceMatrix(EMatrixOperation.DISTANCE_SIMPLEMATCHING, matrixName);
        rDistanceMatrix.createDistanceMatrix(matrixName + "Data");
    }

    /**
	 * Initialize a clustering results object.
	 */
    public static final void initDistanceMatrixClustering(final String dataMatrixName, final String clusterResultsName) {
        TextMiningOperations.initTermList(dataMatrixName + "List");
        final RDataMatrix rDataMatrix = new RDataMatrix(dataMatrixName);
        rDataMatrix.cleanUp();
        rDataMatrix.createMatrixFromList(TermListFactory.getCombinedFilteredTermList(dataMatrixName + "List"), ETermAttributes.PAGE_NAME, ETermAttributes.TERM_NAME, ETermAttributes.SUM);
        final RDistanceMatrix rDistanceMatrix = new RDistanceMatrix(EMatrixOperation.DISTANCE_SIMPLEMATCHING, dataMatrixName + "Dist");
        rDistanceMatrix.createDistanceMatrix(dataMatrixName);
        final RDistanceMatrixClustering rDistanceMatrixClustering = new RDistanceMatrixClustering(EMatrixOperation.CLUSTERING_AGNES, 3, 0, 0.0, clusterResultsName);
        rDistanceMatrixClustering.executeClustering(dataMatrixName + "Dist");
    }

    /***************************************************************************
	 * Help Functions - Clustering
	 **************************************************************************/
    public static final Map<String, Object> getDefaultClusteringConfiguration() {
        final Map<String, Object> actClusteringConfiguration = new HashMap<String, Object>();
        final Map<String, List<String>> actClusteringExportConfiguration = new HashMap<String, List<String>>();
        actClusteringConfiguration.put(EMatrixOperationsGrouped.CLUSTERING_OBJECTS.getOperationGroupName(), EMatrixOperationsGrouped.CLUSTERING_OBJECTS.getOperationGroupMethodDefault());
        actClusteringConfiguration.put(EMatrixOperationsGrouped.CLUSTERING_DATA.getOperationGroupName(), EMatrixOperationsGrouped.CLUSTERING_DATA.getOperationGroupMethodDefault());
        actClusteringConfiguration.put(EMatrixOperationsGrouped.WEIGHTING.getOperationGroupName(), EMatrixOperationsGrouped.WEIGHTING.getOperationGroupMethodDefault());
        actClusteringConfiguration.put(EMatrixOperationsGrouped.FEATURE_SELECTION.getOperationGroupName(), EMatrixOperationsGrouped.FEATURE_SELECTION.getOperationGroupMethodDefault());
        actClusteringConfiguration.put(EMatrixOperation.FEATSEL_THRESHOLD_NAME, String.valueOf(EMatrixOperation.FEATSEL_THRESHOLD_DEFAULT));
        actClusteringConfiguration.put(EMatrixOperationsGrouped.FEATURE_TRANSFORMATION.getOperationGroupName(), EMatrixOperationsGrouped.FEATURE_TRANSFORMATION.getOperationGroupMethodDefault());
        actClusteringConfiguration.put(EMatrixOperation.FEATTRANSF_DIMENSIONS_NAME, String.valueOf(EMatrixOperation.FEATTRANSF_DIMENSIONS_DEFAULT));
        actClusteringConfiguration.put(EMatrixOperationsGrouped.DISTANCE_MEASURES.getOperationGroupName(), EMatrixOperationsGrouped.DISTANCE_MEASURES.getOperationGroupMethodDefault());
        actClusteringConfiguration.put(EMatrixOperationsGrouped.CLUSTERING_METHODS.getOperationGroupName(), EMatrixOperationsGrouped.CLUSTERING_METHODS.getOperationGroupMethodDefault());
        actClusteringConfiguration.put(EMatrixOperation.CLUSTERING_NUMBERS_NAME, String.valueOf(EMatrixOperation.CLUSTERING_NUMBERS_DEFAULT));
        actClusteringConfiguration.put(EMatrixOperation.CLUSTERING_ITERATIONS_NAME, String.valueOf(EMatrixOperation.CLUSTERING_ITERATIONS_DEFAULT));
        actClusteringConfiguration.put(EMatrixOperation.CLUSTERING_MAXDISTANCE_NAME, String.valueOf(EMatrixOperation.CLUSTERING_MAXDISTANCE_DEFAULT));
        actClusteringExportConfiguration.put(EMatrixOperationsGrouped.INTERNAL_QUALITY_MEASURES.getOperationGroupName(), new ArrayList<String>());
        actClusteringExportConfiguration.put(EMatrixOperationsGrouped.EXTERNAL_QUALITY_MEASURES.getOperationGroupName(), new ArrayList<String>());
        actClusteringExportConfiguration.put(EMatrixOperation.EXTQUALMEAS_IDEALCLUSTERFILE_NAME, new ArrayList<String>());
        actClusteringExportConfiguration.get(EMatrixOperation.EXTQUALMEAS_IDEALCLUSTERFILE_NAME).add(EMatrixOperation.EXTQUALMEAS_IDEALCLUSTERFILE_DEFAULT);
        actClusteringExportConfiguration.put(EMatrixOperationsGrouped.FURTHER_QUALITY_MEASURES.getOperationGroupName(), new ArrayList<String>());
        actClusteringExportConfiguration.put(EMatrixOperation.CLUSTERING_RESULT_FILE_NAME, new ArrayList<String>());
        actClusteringExportConfiguration.get(EMatrixOperation.CLUSTERING_RESULT_FILE_NAME).add(EMatrixOperation.CLUSTERING_RESULT_FILE_DEFAULT);
        actClusteringConfiguration.putAll(actClusteringExportConfiguration);
        return actClusteringConfiguration;
    }

    /***************************************************************************
	 * Help Functions - General
	 **************************************************************************/
    public static final String createTmpFile(final String strIdealCluster, final String fileName) {
        String filePath = "";
        try {
            final File file = File.createTempFile(fileName, ".dat");
            final DataOutputStream outStream = new DataOutputStream(new FileOutputStream(file));
            outStream.write(strIdealCluster.getBytes());
            outStream.close();
            filePath = file.getAbsolutePath();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return filePath;
    }
}
