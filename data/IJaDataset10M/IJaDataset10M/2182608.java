package org.amlfilter.test.engine.vectorSpace;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import org.amlfilter.search.comparisonCriteria.VsComparisonCriteriaHandler;
import org.amlfilter.search.comparisonCriteria.VsCriteria_Cosine;
import org.amlfilter.search.comparisonCriteria.VsCriteria_Distance;
import org.amlfilter.search.comparisonCriteria.VsCriteria_Distance_Normalized;
import org.amlfilter.search.comparisonCriteria.VsCriteria_EditDistSimilarity;
import org.amlfilter.search.comparisonCriteria.VsCriteria_PairSimilarity;
import org.amlfilter.search.dataFiles.VectorLoader_hierarchy;
import org.amlfilter.search.utils.Sampling;
import org.amlfilter.search.utils.VectorSpaceMetrics;
import org.amlfilter.search.utils.VectorUtils;
import org.amlfilter.search.vectorSpace.Hierarchy_utils;
import org.amlfilter.search.vectorSpace.TreeResult;
import org.amlfilter.search.vectorSpace.VectorData4Tree;
import org.amlfilter.search.vectorSpace.VectorDefinition;
import org.amlfilter.search.vectorSpace.VectorSpace;
import org.amlfilter.test.engine.utils.NameErrorGenerator;
import org.amlfilter.test.engine.utils.OcrErrorMap;
import org.amlfilter.test.engine.utils.ProximityKeyboardMap;
import org.amlfilter.util.ObjectUtils;

public class vsTrainTest {

    private static String baseDir = "/raid/opt/data/vs/";

    private static Hierarchy_utils hu = new Hierarchy_utils();

    /**
	 * @param args
	 */
    public static void main(String[] args) throws Exception {
        if (null == args) {
            System.out.println("** No filename provided. Exiting.");
            System.exit(1);
        }
        int numElementsToLoad = 500000;
        int minSizeOfVsForTrainingIt = 50;
        String fileName = "random.names.25M.txt";
        String fieldSeparator = ",";
        int fieldToLoadPosition = 0;
        int numFirstLayerVectors = 500;
        boolean relocateCoordinates_relativeToParents = true;
        boolean trainDeeperLevels = true;
        boolean createSeedingVectorsPerLayer = false;
        boolean refineRefVectors = true;
        int maxSizeOfSampledVsForRefineSampling = 500;
        int numPassesForRefining = 10;
        boolean averageParentCoordinatesUsingChildren = false;
        String noteToHeaderOfFile = "(this note is not always accurate) training mantaining the orphans outside.";
        boolean runThresholdAdjustmentTest = false;
        boolean enable100x100SearchTest = true;
        float thresholdFor100x100test = 0.0001f;
        int numberOfVectorsFor100x100Test = 20000;
        boolean enableThresholdTest = false;
        boolean runEuclideanTest = true;
        float thresholdForSearching = 12f;
        int numNamesForEuclideanTest = 5;
        boolean usePriorVsFile = false;
        String priorVsFileName = "all";
        long previousCheckPoint = System.currentTimeMillis();
        VsCriteria_Distance comparator_distance = new VsCriteria_Distance();
        VsCriteria_Distance_Normalized comparator_distNorm = new VsCriteria_Distance_Normalized();
        VsCriteria_PairSimilarity comparator_pairSim = new VsCriteria_PairSimilarity();
        VsCriteria_EditDistSimilarity comparator_editDistSim = new VsCriteria_EditDistSimilarity();
        VsCriteria_Cosine comparator_cosine = new VsCriteria_Cosine();
        VsComparisonCriteriaHandler comparator_forTraining = comparator_pairSim;
        try {
            String outputFileName = System.currentTimeMillis() + "_" + "treeTRAIN_" + comparator_forTraining.getCriteriaName() + "_" + "AP-" + averageParentCoordinatesUsingChildren + "_" + "RC-" + relocateCoordinates_relativeToParents + "_" + "NSV-" + numFirstLayerVectors + "_" + "minSiz4Train-" + minSizeOfVsForTrainingIt + "_" + numElementsToLoad + "-vecs";
            outputFileName = outputFileName.replaceAll(" ", "_");
            String logName = outputFileName + ".log";
            FileOutputStream f = new FileOutputStream(baseDir + logName);
            Hierarchy_utils.log = new BufferedWriter(new OutputStreamWriter(f, (new VectorSpace()).getVectorManager().getLocale().getDisplayName()));
            Hierarchy_utils.logLine(Hierarchy_utils.log, "### NOTE: " + noteToHeaderOfFile);
            VectorSpace rawVs = new VectorSpace();
            rawVs.setVectorDefinition(VectorDefinition.makeRawVecDefinition());
            rawVs.setComparator(comparator_forTraining);
            VectorLoader_hierarchy.loadStringFileInVS_tiny(baseDir + fileName, rawVs, fieldToLoadPosition, fieldSeparator, true, 100000, numElementsToLoad);
            Hierarchy_utils.logLine(Hierarchy_utils.log, "# Number of elements in vs: " + rawVs.size());
            long checkpoint = System.currentTimeMillis();
            Hierarchy_utils.logLine(Hierarchy_utils.log, "###\n###\n### Check point timer - Loaded world of data: " + (checkpoint - previousCheckPoint) + " ms");
            previousCheckPoint = checkpoint;
            VectorData4Tree target_raw = rawVs.createVector("Mugabe Robert Gabriel");
            test_std_search(target_raw, rawVs, 20, 0.2f);
            VectorSpace orderedVs = new VectorSpace();
            orderedVs.setVectorDefinition(VectorDefinition.makeRawVecDefinition());
            orderedVs.setComparator(comparator_forTraining);
            hu.show_refVectors(orderedVs);
            VectorSpace testingRawVs = rawVs.clone();
            Hierarchy_utils.logLine(Hierarchy_utils.log, "## Training... *");
            if (!usePriorVsFile) {
                orderedVs = hu.train_(orderedVs, rawVs, averageParentCoordinatesUsingChildren, relocateCoordinates_relativeToParents, trainDeeperLevels, minSizeOfVsForTrainingIt, numFirstLayerVectors, maxSizeOfSampledVsForRefineSampling, numPassesForRefining, refineRefVectors, createSeedingVectorsPerLayer);
                checkpoint = System.currentTimeMillis();
                double trainingTime = ((double) (checkpoint - previousCheckPoint) / 60000d);
                Hierarchy_utils.logLine(Hierarchy_utils.log, "##### Training time: " + trainingTime + " min");
                previousCheckPoint = checkpoint;
                System.out.println("###### DONE TRAINING !");
                System.out.println("");
                Hierarchy_utils.logLine(Hierarchy_utils.log, "\t# Storing the vs in a file");
                System.out.println("# Storing the vs in a file");
                ObjectUtils.persistObjectToFile(orderedVs, baseDir + outputFileName + ".vs");
            }
            orderedVs = null;
            Hierarchy_utils.logLine(Hierarchy_utils.log, "\t# Reading the file");
            System.out.println("# Reading the file");
            VectorSpace readVs = null;
            if (usePriorVsFile) {
                readVs = (VectorSpace) ObjectUtils.readObjectFromFile(baseDir + priorVsFileName + ".vs");
            } else {
                readVs = (VectorSpace) ObjectUtils.readObjectFromFile(baseDir + outputFileName + ".vs");
            }
            readVs.setOriginalComparatorWhenTraining(comparator_forTraining);
            if (!usePriorVsFile) {
                Hierarchy_utils.logLine(Hierarchy_utils.log, "\t##### TREE ######");
                hu.show_refVectors_tree(readVs, 0);
            }
            if (runEuclideanTest) {
                writeExcelHeader();
                testingRawVs.markAllVectorsInList();
                VectorSpace sampleNames = Sampling.buildClonedRandomSample(testingRawVs, numNamesForEuclideanTest, false, false);
                VectorSpaceMetrics rawVsMetrics = new VectorSpaceMetrics(rawVs);
                for (int i = 0; i < sampleNames.size(); i++) {
                    Hierarchy_utils.logLine(Hierarchy_utils.log, "###########################################################");
                    euclideanTest(sampleNames.get(i).getString(), rawVs, testingRawVs, readVs, comparator_forTraining, false, thresholdForSearching, rawVsMetrics);
                }
                euclideanTest("JOHN SMITH", rawVs, testingRawVs, readVs, comparator_forTraining, false, thresholdForSearching, rawVsMetrics);
            }
            if (enable100x100SearchTest) {
                System.out.println("# About to perform 100x100 test ...");
                test_tree_search_batch(rawVs, readVs, 2, thresholdFor100x100test, true, true, true, numberOfVectorsFor100x100Test);
                System.out.println("# Done with 100x100 test.");
            }
            Hierarchy_utils.logLine(Hierarchy_utils.log, "\t##### ORPHANS (" + readVs.getOrphanList().size() + ")######");
            hu.show_vdList(readVs.getOrphanList());
            if (enableThresholdTest) {
                float dist2Analyze = 0;
                rawVs.setComparator(readVs.getComparator());
                VectorSpaceMetrics rawVsMetrics = new VectorSpaceMetrics(rawVs);
                dist2Analyze = rawVsMetrics.getAverageSimilarity();
                dist2Analyze = (float) readVs.getComparator().getHalfWayToMaximumSimilarity(dist2Analyze);
                float th = computeThresholdForDistance(rawVs, readVs, dist2Analyze, comparator_forTraining);
                th = computeThresholdForDistance(rawVs, readVs, 30f, comparator_forTraining);
            }
            System.out.println("# Counting node...");
            long parentNodes = countParentNodes(readVs);
            System.out.println("# number of parent nodes = " + parentNodes);
        } catch (Exception e) {
            e.printStackTrace();
            Hierarchy_utils.logLine(Hierarchy_utils.log, e.getStackTrace().toString());
        } finally {
            if (null != Hierarchy_utils.log) {
                Hierarchy_utils.log.close();
            }
            System.out.println("# Done executing.");
        }
    }

    private static long countParentNodes(VectorSpace pVs) {
        long retVal = 0l;
        VectorSpace childVs = null;
        for (int i = 0; i < pVs.size(); i++) {
            childVs = pVs.get(i).getVectorSpace();
            if (null != childVs) {
                retVal++;
                retVal += countParentNodes(childVs);
            }
        }
        return retVal;
    }

    private static void thresholdAdjustmentTest(VectorData4Tree pTarget, VectorSpace pOrderedVs, VectorSpace pRawVs, Hierarchy_utils pHu, double pSearchDistance) throws Exception {
        VsCriteria_Distance comparator_distance = new VsCriteria_Distance();
        pRawVs.setComparator(comparator_distance);
        test_std_search(pTarget, pRawVs, 20, 100f);
        List<TreeResult> res = pOrderedVs.recursiveTreeSearch(pTarget, 50, pSearchDistance, 0, false);
        pHu.show_results_with_original_similarities(Hierarchy_utils.log, res, pOrderedVs, pTarget);
    }

    private static float computeThresholdForDistance(VectorSpace pRawVs, VectorSpace pTrainedVs, float pDistanceToUse, VsComparisonCriteriaHandler pComp) throws Exception {
        float retval = -1f;
        Hierarchy_utils.logLine(Hierarchy_utils.log, "## computeThresholdForDistance: " + pDistanceToUse);
        System.out.println("## computeThresholdForDistance: " + pDistanceToUse);
        for (int i = 0; i < 10; i++) {
            VectorData4Tree testVector = pRawVs.get(i);
            List<TreeResult> trl = pTrainedVs.recursiveTreeSearch(testVector, pRawVs.size(), pDistanceToUse, 0, false);
            double maxForThisResult = -1d;
            double minForThisResult = 1000d;
            for (int j = 0; j < trl.size(); j++) {
                double algSim = pComp.computeSimilarity(testVector.getByteCoordinates(), trl.get(j).getFoundVectorData().getByteCoordinates());
                if (maxForThisResult < algSim) {
                    maxForThisResult = algSim;
                }
                if (minForThisResult > algSim) {
                    minForThisResult = algSim;
                }
                Hierarchy_utils.logLine(Hierarchy_utils.log, i + " - [" + j + "] SIM: " + algSim + " ... " + testVector.getString() + " vs " + trl.get(j).getFoundVectorData().getString());
                System.out.println("\t" + i + " - [" + j + "] SIM: " + algSim + " ... " + testVector.getString() + " vs " + trl.get(j).getFoundVectorData().getString());
            }
            pRawVs.setComparator(pComp);
            List<TreeResult> algResults = pRawVs.obtainSimilarResults(testVector, pRawVs.size(), minForThisResult, false);
            System.out.println(i + " - MAX: " + maxForThisResult + "\t\tMIN: " + minForThisResult + "\t\tNUM RESULTS: " + trl.size() + "\t\t\tResults bigger than min using ALGs: " + algResults.size());
        }
        return retval;
    }

    private static void test_std_search(VectorData4Tree pTarget, VectorSpace pVs, int pMaxNumResults, float pMinSimilarityAllowed) throws Exception {
        Hierarchy_utils.logLine(Hierarchy_utils.log, "## Test search (" + pTarget.getString() + ") (sim=" + pMinSimilarityAllowed + ") ... Comparator: " + pVs.getComparator().getCriteriaName());
        long startTime = System.currentTimeMillis();
        List<TreeResult> results = pVs.obtainSimilarResults(pTarget, pMaxNumResults, pMinSimilarityAllowed, false);
        Hierarchy_utils.logLine(Hierarchy_utils.log, "\t# Search time (ms)= " + (System.currentTimeMillis() - startTime));
        hu.show_results(Hierarchy_utils.log, results);
    }

    private static VectorData4Tree test_tree_search_batch(VectorSpace pRawVs, VectorSpace pTrainedVs, int pMaxNumResults, float pMinSimilarityAllowed, boolean pShowResults, boolean pIgnoreOrphans, boolean pComputeThreshold, int pNumberOfVectorsFor100x100Test) throws Exception {
        Hierarchy_utils.logLine(Hierarchy_utils.log, "## ! TREE BATCH test searching (" + pRawVs.size() + " elements) (sim=" + pMinSimilarityAllowed + ") ... Comparator: " + pTrainedVs.getComparator().getCriteriaName());
        int foundResults = 0;
        int numOfOrphansNotFound = 0;
        VectorData4Tree vectorToSearch = null;
        VectorData4Tree vectorToDebugSearchOn = null;
        List<TreeResult> results = null;
        long startTime = 0;
        long endTime = 0;
        long acumTime = 0;
        float avgTime = 0f;
        int numberOfResultsRetrievedInTotal = 0;
        int maxNumSearches = pRawVs.size();
        if (pNumberOfVectorsFor100x100Test > 0) {
            maxNumSearches = pNumberOfVectorsFor100x100Test;
        }
        for (int i = 0; i < maxNumSearches; i++) {
            vectorToSearch = pRawVs.get(i);
            vectorToSearch = pTrainedVs.createVector(pRawVs.get(i).getString(), pTrainedVs.getOriginalComparatorWhenTraining());
            boolean found = false;
            boolean wasItOrphan = false;
            int numberOfResultsRetrievedInSingleSearch = 0;
            int numberResultsInThisBatch = 0;
            startTime = System.currentTimeMillis();
            results = pTrainedVs.recursiveTreeSearch(vectorToSearch, pMaxNumResults, pMinSimilarityAllowed, 0, false);
            endTime = System.currentTimeMillis();
            numberResultsInThisBatch += results.size();
            if (i % 10000 == 0) {
                avgTime = Math.round((float) acumTime / (float) i * 100f) / 100f;
                System.out.println("...search progress: " + i + " / " + pRawVs.size() + "\t\t avg time= " + (avgTime) + " ms\tTotalNumberResults= " + numberOfResultsRetrievedInTotal + "\tNumberResultsInThisBatch= " + numberResultsInThisBatch);
                numberResultsInThisBatch = 0;
            }
            acumTime += (endTime - startTime);
            for (int j = 0; j < results.size(); j++) {
                if (results.get(j).getFoundVectorData().getString().equals(vectorToSearch.getString())) {
                    foundResults++;
                    found = true;
                    break;
                } else if (pShowResults) {
                }
            }
            numberOfResultsRetrievedInSingleSearch = results.size();
            numberResultsInThisBatch += numberOfResultsRetrievedInSingleSearch;
            numberOfResultsRetrievedInTotal += numberOfResultsRetrievedInSingleSearch;
            if (!found) {
                for (int j = 0; j < pTrainedVs.getOrphanList().size(); j++) {
                    if (pTrainedVs.getOrphanList().get(j).getString().equals(vectorToSearch.getString())) {
                        numOfOrphansNotFound++;
                        wasItOrphan = true;
                        break;
                    }
                }
                if (wasItOrphan) {
                } else {
                    Hierarchy_utils.logLine(Hierarchy_utils.log, "\t* ERROR: " + vectorToSearch.getString() + " was not found");
                    System.out.println("\t* ERROR: " + vectorToSearch.getString() + " was not found");
                }
            }
        }
        avgTime = Math.round((float) acumTime / (float) pRawVs.size() * 100f) / 100f;
        Hierarchy_utils.logLine(Hierarchy_utils.log, "\t# Total Search time (ms)= " + (acumTime) + "(" + avgTime + " ms/search)" + "\tFound: " + foundResults + " / " + pRawVs.size() + " (" + numOfOrphansNotFound + " not found orphans)" + " ms\tTotalNumberResults= " + numberOfResultsRetrievedInTotal);
        System.out.println("\t# Total Search time (ms)= " + (acumTime) + "(" + avgTime + " ms/search)" + "\tFound: " + foundResults + " / " + pRawVs.size() + " (" + numOfOrphansNotFound + " not found orphans)" + " ms\tTotalNumberResults= " + numberOfResultsRetrievedInTotal);
        return vectorToDebugSearchOn;
    }

    private static void euclideanTest(String pPivot_vector_string, VectorSpace rawVs, VectorSpace testingRawVs, VectorSpace orderedVs, VsComparisonCriteriaHandler comparator_forTraining, boolean pSimilarityBrowsing, float pThresholdForSearching, VectorSpaceMetrics pRawVsMetrics) throws Exception {
        int totalTestCasesCount = 0;
        int totalCases_PassingDistanceTest = 0;
        VectorSpaceMetrics rawVsMetrics = null;
        if (null == pRawVsMetrics) {
            rawVsMetrics = new VectorSpaceMetrics(rawVs);
        } else {
            rawVsMetrics = pRawVsMetrics;
        }
        FileOutputStream fos_elt = new FileOutputStream(baseDir + "euclid_last_test.log", true);
        BufferedWriter bw_elt = new BufferedWriter(new OutputStreamWriter(fos_elt, testingRawVs.getVectorManager().getLocale().getDisplayName()));
        FileOutputStream fos_etcsv = new FileOutputStream(baseDir + "euclid_testing.csv", true);
        BufferedWriter bw_etcsv = new BufferedWriter(new OutputStreamWriter(fos_etcsv, testingRawVs.getVectorManager().getLocale().getDisplayName()));
        Hierarchy_utils.logLine(bw_elt, "--------------------------------------------------------------------------------------------------------------------------------------");
        Hierarchy_utils.logLine(bw_elt, "Euclidean test");
        Hierarchy_utils.logLine(bw_elt, "--------------------------------------------------------------------------------------------------------------------------------------");
        ProximityKeyboardMap pkm = new ProximityKeyboardMap();
        OcrErrorMap oem = new OcrErrorMap();
        List<String> nameBoundaries = new ArrayList<String>();
        String[] ErrorSeries = NameErrorGenerator.char_swaping(pPivot_vector_string);
        nameBoundaries = mergeNameArrays(nameBoundaries, ErrorSeries);
        ErrorSeries = NameErrorGenerator.miss_typing(pPivot_vector_string, false, pkm);
        nameBoundaries = mergeNameArrays(nameBoundaries, ErrorSeries);
        ErrorSeries = NameErrorGenerator.ocr_Errors_1_to_1_2_chars(pPivot_vector_string, false, oem);
        nameBoundaries = mergeNameArrays(nameBoundaries, ErrorSeries);
        ErrorSeries = NameErrorGenerator.ocr_Errors_2_to_1_char(pPivot_vector_string, false, oem);
        nameBoundaries = mergeNameArrays(nameBoundaries, ErrorSeries);
        ErrorSeries = NameErrorGenerator.deletionOfAChar(pPivot_vector_string);
        nameBoundaries = mergeNameArrays(nameBoundaries, ErrorSeries);
        ErrorSeries = NameErrorGenerator.doubles_char(pPivot_vector_string);
        nameBoundaries = mergeNameArrays(nameBoundaries, ErrorSeries);
        String boundary_vector_string = null;
        for (int i = 0; i < nameBoundaries.size(); i++) {
            boundary_vector_string = nameBoundaries.get(i);
            VectorData4Tree pivot_name_sim = testingRawVs.createVector(pPivot_vector_string);
            VectorData4Tree boundary_name_sim = testingRawVs.createVector(boundary_vector_string);
            double sim = testingRawVs.obtainSimilarity(pivot_name_sim, boundary_name_sim);
            List<TreeResult> sim_results = null;
            if (pSimilarityBrowsing) {
                sim_results = testingRawVs.obtainSimilarResults(pivot_name_sim, 10000, sim, false);
            }
            List<TreeResult> dist_results = null;
            double mathSimilarity = 0d;
            double distance = 0d;
            double cosine = 0d;
            VectorData4Tree pivot_vector = orderedVs.createVector(pPivot_vector_string, comparator_forTraining);
            VectorData4Tree boundary_vector = orderedVs.createVector(boundary_vector_string, comparator_forTraining);
            mathSimilarity = orderedVs.obtainSimilarity(pivot_vector, boundary_vector);
            cosine = VectorUtils.computeCosineOfVectors(pivot_vector.getByteCoordinates(), boundary_vector.getByteCoordinates());
            distance = VectorUtils.computeDistanceOfVectors(pivot_vector.getByteCoordinates(), boundary_vector.getByteCoordinates());
            orderedVs.getVectorManager().setCounter(0);
            long startTime = System.currentTimeMillis();
            dist_results = orderedVs.recursiveTreeSearch(boundary_vector, 10000, pThresholdForSearching, orderedVs.getComparator().getMaxSimilarityValue(), false);
            long timeToSearch = System.currentTimeMillis() - startTime;
            boolean pivotFound = false;
            int numResults = dist_results.size();
            String pivot_name_string = pivot_vector.getString();
            Hierarchy_utils.logLine(bw_elt, "### searching for: " + boundary_vector_string);
            for (int resReviewPos = 0; resReviewPos < numResults; resReviewPos++) {
                String result_name_String = dist_results.get(resReviewPos).getFoundVectorData().getString();
                double tempCosine = VectorUtils.computeCosineOfVectors(boundary_vector.getByteCoordinates(), dist_results.get(resReviewPos).getFoundVectorData().getByteCoordinates());
                Hierarchy_utils.logLine(bw_elt, "\t- " + result_name_String + "\t... cos= " + tempCosine);
                if (result_name_String.equals(pivot_name_string)) {
                    pivotFound = true;
                    break;
                }
            }
            int comparisonsCounter = orderedVs.getVectorManager().getCounter();
            Hierarchy_utils.logLine(bw_elt, "comparisonsCounter = " + comparisonsCounter);
            Hierarchy_utils.logLine(bw_elt, "rawVsMetrics.getAverageSimilarity() = " + rawVsMetrics.getAverageSimilarity());
            Hierarchy_utils.logLine(bw_elt, "original comparator = " + testingRawVs.getComparator().getCriteriaName());
            Hierarchy_utils.logLine(bw_elt, "comparator 4 trainning = " + orderedVs.getComparator().getCriteriaName());
            Hierarchy_utils.logLine(bw_elt, "pivot_name_string = " + pPivot_vector_string);
            Hierarchy_utils.logLine(bw_elt, "boundary_name_string = " + boundary_vector_string);
            Hierarchy_utils.logLine(bw_elt, "MathComparison = " + mathSimilarity);
            Hierarchy_utils.logLine(bw_elt, "DISTANCE = " + distance);
            Hierarchy_utils.logLine(bw_elt, "COSINE = " + cosine);
            Hierarchy_utils.logLine(bw_elt, "SIM  = " + sim);
            if (pSimilarityBrowsing) {
                hu.show_results(bw_elt, sim_results);
                if (sim_results.size() > 0) {
                    Hierarchy_utils.logLine(bw_elt, "\t(showing the distances of these results to the pivot name)");
                }
                double temp_dist = 0;
                for (int j = 0; j < sim_results.size(); j++) {
                    VectorData4Tree found_name = orderedVs.createVector(sim_results.get(j).getFoundVectorData().getString(), comparator_forTraining);
                    temp_dist = orderedVs.obtainSimilarity(pivot_vector, found_name);
                    Hierarchy_utils.logLine(bw_elt, "\t- found name: " + sim_results.get(j).getFoundVectorData().getString() + " dist : " + temp_dist);
                }
                Hierarchy_utils.logLine(bw_elt, "**** DIST results ****");
                hu.show_results(bw_elt, dist_results);
            }
            StringBuilder sbu = new StringBuilder();
            double fractionOfSpace = mathSimilarity / (double) rawVsMetrics.getMaxDistance();
            String boundaryVectorDimensions = "";
            for (int dimPos = 0; dimPos < boundary_vector.getByteCoordinates().length; dimPos++) {
                boundaryVectorDimensions = boundaryVectorDimensions.concat(boundary_vector.getByteCoordinates()[dimPos] + ",");
            }
            String pivotVectorDimensions = "";
            for (int dimPos = 0; dimPos < pivot_vector.getByteCoordinates().length; dimPos++) {
                pivotVectorDimensions = pivotVectorDimensions.concat(pivot_vector.getByteCoordinates()[dimPos] + ",");
            }
            Formatter formatter = new Formatter(sbu, Locale.FRANCE);
            formatter.format("%5d\t%9d\t%6d\t%4.4f\t%s\t%s\t%1.4f\t%5.3f\t%5.3f\t%5.3f\t%1.3f\t%6d\t%5d\t%5d\t%b\t%s\t%s\n", rawVsMetrics.getNumDimensions(), rawVs.size(), rawVsMetrics.getMaxDistance(), rawVsMetrics.getAverageSimilarity(), pPivot_vector_string, boundary_vector_string, sim, mathSimilarity, distance, cosine, fractionOfSpace, dist_results.size(), comparisonsCounter, timeToSearch, pivotFound, boundaryVectorDimensions, pivotVectorDimensions);
            bw_etcsv.write(sbu.toString());
            totalTestCasesCount++;
            if (comparator_forTraining.isFirstSimilarityBiggerOrEqual(mathSimilarity, pThresholdForSearching)) {
                totalCases_PassingDistanceTest++;
            }
        }
        bw_elt.write("\n----------------------------------------------------------------------------------------------");
        bw_elt.write("\n\n totalTestCasesCount: " + totalTestCasesCount + "\ttotalCases_PassingDistanceTest= " + totalCases_PassingDistanceTest);
        bw_elt.close();
        bw_etcsv.close();
    }

    private static List<String> mergeNameArrays(List<String> pMainNameContainer, String[] pNamesToMerge) {
        for (int i = 0; i < pNamesToMerge.length; i++) {
            if (null != pNamesToMerge[i]) {
                pMainNameContainer.add(pNamesToMerge[i]);
            }
        }
        return pMainNameContainer;
    }

    private static void writeExcelHeader() throws IOException {
        FileOutputStream fos_etcsv = new FileOutputStream(baseDir + "euclid_testing.csv", true);
        BufferedWriter bw_etcsv = new BufferedWriter(new OutputStreamWriter(fos_etcsv, "UTF-8"));
        String HEADER_STR = "nDim \trawVsSize \tmetrics-maxDist \tmetrixAvgSim \torigName " + "\tmodName \tSim \tmathSim \tDist \tCos \tfractionOfSpace \tnumResults " + "\tnumComparisons \ttime \tfound? \tdimForModifiedName \tdimForOrigName \n";
        bw_etcsv.write(HEADER_STR);
        bw_etcsv.close();
        bw_etcsv.close();
        fos_etcsv.close();
    }
}
