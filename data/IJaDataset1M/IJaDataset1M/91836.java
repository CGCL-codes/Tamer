package org.tigr.microarray.mev.cluster.algorithm.impl.ease;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Hashtable;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.tigr.microarray.mev.cluster.algorithm.AbstractAlgorithm;
import org.tigr.microarray.mev.cluster.algorithm.AlgorithmData;
import org.tigr.microarray.mev.cluster.algorithm.AlgorithmEvent;
import org.tigr.microarray.mev.cluster.algorithm.AlgorithmException;
import org.tigr.microarray.mev.cluster.algorithm.AlgorithmParameters;
import org.tigr.util.FloatMatrix;
import org.tigr.util.QSort;

/** Manages EASE analysis and raw result manipulation and return.
 * @author braisted
 */
public class EASE extends AbstractAlgorithm {

    private JEASEStatistics jstats;

    private Vector sampleVector;

    private Vector populationVector;

    private String[] annotationFileList;

    private String[][] result;

    private String[][] hitList;

    private String[] categoryNames;

    /** True if accession numbers are appended.
     */
    private boolean haveAccessionNumbers = false;

    private boolean reportEaseScore = false;

    private AlgorithmEvent event;

    private Vector headerNames;

    private DecimalFormat format;

    private FloatMatrix expData;

    private boolean stop = false;

    private boolean performClusterAnalysis;

    long start;

    /** Creates a new instance of ease (Default)
     */
    public EASE() {
    }

    public void abort() {
        stop = true;
    }

    /** Recieves parameters, executes algorithm and returns the result in the <CODE>AlgorithmData</CODE> object.
     * @param algorithmData Intput data and parameters
     * @throws AlgorithmException Reports errors or abort requests
     * @return Returns result in <CODE>AlgorithmData</CODE>
     */
    public AlgorithmData execute(AlgorithmData algorithmData) throws AlgorithmException {
        AlgorithmParameters params = algorithmData.getParams();
        performClusterAnalysis = params.getBoolean("perform-cluster-analysis", true);
        expData = algorithmData.getMatrix("expression");
        if (performClusterAnalysis) return performClusterAnnotationAnalysis(algorithmData); else {
            return performSlideAnnotationSurvey(algorithmData);
        }
    }

    /** Main method for cluster analysis.
     * @param algorithmData Input data and parameters.
     * @throws AlgorithmException
     * @return
     */
    private AlgorithmData performClusterAnnotationAnalysis(AlgorithmData algorithmData) throws AlgorithmException {
        AlgorithmParameters params = algorithmData.getParams();
        headerNames = new Vector();
        reportEaseScore = params.getBoolean("report-ease-score", false);
        intializeHeaderNames();
        format = new DecimalFormat("0.###E00");
        event = new AlgorithmEvent(this, AlgorithmEvent.MONITOR_VALUE, 0);
        event.setDescription("Start EASE Analyis\n");
        fireValueChanged(event);
        String converterFileName = params.getString("converter-file-name");
        int[] clusterIndices = algorithmData.getIntArray("sample-indices");
        String[] sampleList = algorithmData.getStringArray("sample-list");
        String[] populationList = algorithmData.getStringArray("population-list");
        annotationFileList = algorithmData.getStringArray("annotation-file-list");
        EaseElementList sampleElementList = new EaseElementList(clusterIndices, sampleList);
        EaseElementList populationElementList = new EaseElementList(populationList);
        if (stop) return null;
        try {
            if (converterFileName != null) {
                event.setDescription("Loading Cluster Annotation List\n");
                fireValueChanged(event);
                sampleElementList.loadValues(converterFileName);
                event.setDescription("Loading Population Annotation List\n");
                fireValueChanged(event);
                populationElementList.loadValues(converterFileName);
            } else {
                event.setDescription("Preparing Annotation Lists (no conversion file)\n");
                sampleElementList.setDefaultValues();
                populationElementList.setDefaultValues();
            }
        } catch (FileNotFoundException fnfe) {
            throw new AlgorithmException("Annotation Conversion File Not Found\n" + converterFileName + "\n" + fnfe.getMessage());
        } catch (IOException ioe) {
            throw new AlgorithmException("Error Reading File: " + converterFileName + "\n" + ioe.getMessage());
        }
        event.setDescription("Extracting Unique Cluster Annotation List\n");
        fireValueChanged(event);
        sampleVector = sampleElementList.getUniqueValueList();
        if (stop) return null;
        event.setDescription("Extracting Unique Population Annotation List\n");
        fireValueChanged(event);
        populationVector = populationElementList.getUniqueValueList();
        if (stop) return null;
        jstats = new JEASEStatistics(reportEaseScore);
        for (int i = 0; i < annotationFileList.length; i++) {
            jstats.AddAnnotationFileName(annotationFileList[i]);
        }
        event.setDescription("Loading Annotation Category Files\n");
        fireValueChanged(event);
        jstats.GetCategories(populationVector);
        if (stop) return null;
        event.setDescription("Finding Sample Category Hits\n");
        fireValueChanged(event);
        jstats.GetListHitsByCategory(sampleVector);
        if (stop) return null;
        event.setDescription("Finding Population Category Hits\n");
        fireValueChanged(event);
        jstats.GetPopulationHitsByCategory(populationVector);
        event.setDescription("Statistical Testing and Result Prep.\n");
        fireValueChanged(event);
        jstats.ConstructResults();
        result = jstats.getResults();
        if (result.length < 1) {
            return algorithmData;
        }
        hitList = jstats.getListHitMatrix();
        categoryNames = jstats.getCategoryNames();
        event.setDescription("Sorting Result on p-value\n");
        fireValueChanged(event);
        sortResults();
        if (stop) return null;
        if (algorithmData.getParams().getBoolean("p-value-corrections", false)) {
            event.setDescription("Applying p-value Multiplicity Corrections\n");
            fireValueChanged(event);
            pValueCorrections(algorithmData);
        }
        if (algorithmData.getParams().getBoolean("run-permutation-analysis", false)) {
            event.setDescription("Resampling Analysis\n");
            fireValueChanged(event);
            permutationAnalysis(algorithmData.getParams().getInt("permutation-count", 1));
        }
        if (stop) return null;
        event.setDescription("Appending Accessions\n");
        fireValueChanged(event);
        result = appendAccessions(result, annotationFileList);
        algorithmData.addParam("have-accession-numbers", String.valueOf(haveAccessionNumbers));
        String trimOption = params.getString("trim-option");
        float trimValue;
        if (!(trimOption.equals("NO_TRIM"))) {
            event.setDescription("Trim Result\n");
            fireValueChanged(event);
            trimValue = params.getFloat("trim-value");
            trimResult(trimOption, trimValue);
        }
        event.setDescription("Indexing Result\n");
        fireValueChanged(event);
        indexResult();
        algorithmData.addObjectMatrix("result-matrix", result);
        algorithmData.addObjectMatrix("hit-list-matrix", hitList);
        event.setDescription("Extracting Cluster Indices and Stats\n");
        fireValueChanged(event);
        int[][] clusters = getClusters(sampleElementList, hitList);
        algorithmData.addStringArray("category-names", categoryNames);
        algorithmData.addIntMatrix("cluster-matrix", clusters);
        algorithmData.addStringArray("header-names", getHeaderNames());
        FloatMatrix means = getMeans(expData, clusters);
        algorithmData.addMatrix("means", means);
        algorithmData.addMatrix("variances", getVariances(expData, means, clusters));
        if (stop) return null;
        return algorithmData;
    }

    /** Alternative analysis mode (slide survey)
     * @param algorithmData
     * @throws AlgorithmException
     * @return  */
    private AlgorithmData performSlideAnnotationSurvey(AlgorithmData algorithmData) throws AlgorithmException {
        AlgorithmParameters params = algorithmData.getParams();
        headerNames = new Vector();
        intializeHeaderNames();
        format = new DecimalFormat("0.###E00");
        AlgorithmEvent event = new AlgorithmEvent(this, AlgorithmEvent.MONITOR_VALUE, 0);
        event.setDescription("Start Survey\n");
        fireValueChanged(event);
        String converterFileName = params.getString("converter-file-name");
        String[] populationList = algorithmData.getStringArray("population-list");
        String[] annotationFileList = algorithmData.getStringArray("annotation-file-list");
        EaseElementList populationElementList = new EaseElementList(populationList);
        try {
            if (converterFileName != null) {
                event.setDescription("Loading Population Annotation List\n");
                fireValueChanged(event);
                populationElementList.loadValues(converterFileName);
            } else {
                event.setDescription("Preparing Annotation Lists (no conversion file)\n");
                populationElementList.setDefaultValues();
            }
        } catch (FileNotFoundException fnfe) {
            throw new AlgorithmException("Annotation Conversion File Not Found\n" + converterFileName + "\n" + fnfe.getMessage());
        } catch (IOException ioe) {
            throw new AlgorithmException("Error Reading File: " + converterFileName + "\n" + ioe.getMessage());
        }
        event.setDescription("Extracting Unique Population Annotation List\n");
        fireValueChanged(event);
        populationVector = populationElementList.getUniqueValueList();
        jstats = new JEASEStatistics();
        for (int i = 0; i < annotationFileList.length; i++) {
            jstats.AddAnnotationFileName(annotationFileList[i]);
        }
        event.setDescription("Reading Annotation Category Files into Memory\n");
        fireValueChanged(event);
        jstats.GetCategories();
        event.setDescription("Finding Population Category Hits\n");
        fireValueChanged(event);
        jstats.GetPopulationHitsByCategoryForSurvey(populationVector);
        event.setDescription("Result Prep.\n");
        fireValueChanged(event);
        jstats.ConstructSurveyResults();
        result = jstats.getSurveyResults();
        if (result.length < 1) return algorithmData;
        hitList = jstats.getListHitMatrix();
        categoryNames = jstats.getCategoryNames();
        event.setDescription("Sorting Result on hit count\n");
        fireValueChanged(event);
        sortSurveyResults();
        event.setDescription("Appending Accessions\n");
        fireValueChanged(event);
        result = appendAccessions(result, annotationFileList);
        algorithmData.addParam("have-accession-numbers", String.valueOf(haveAccessionNumbers));
        algorithmData.addStringArray("header-names", getHeaderNames());
        String trimOption = params.getString("trim-option");
        float trimValue;
        if (!(trimOption.equals("NO_TRIM"))) {
            event.setDescription("Trim Result\n");
            fireValueChanged(event);
            trimValue = params.getFloat("trim-value");
            trimResult(trimOption, trimValue);
        }
        event.setDescription("Indexing Result\n");
        fireValueChanged(event);
        indexResult();
        algorithmData.addObjectMatrix("result-matrix", result);
        algorithmData.addObjectMatrix("hit-list-matrix", hitList);
        event.setDescription("Extracting Cluster Indices and Stats\n");
        fireValueChanged(event);
        int[][] clusters = getClusters(populationElementList, hitList);
        algorithmData.addStringArray("category-names", categoryNames);
        algorithmData.addIntMatrix("cluster-matrix", clusters);
        FloatMatrix means = getMeans(expData, clusters);
        algorithmData.addMatrix("means", means);
        algorithmData.addMatrix("variances", getVariances(expData, means, clusters));
        return algorithmData;
    }

    /** Creates header names based on analysis mode.
     */
    private void intializeHeaderNames() {
        if (performClusterAnalysis) {
            headerNames.add("Index");
            headerNames.add("File");
            headerNames.add("Term");
            headerNames.add("List Hits");
            headerNames.add("List Size");
            headerNames.add("Pop. Hits");
            headerNames.add("Pop. Size");
            if (reportEaseScore) headerNames.add("EASE Score"); else headerNames.add("Fisher's Exact");
        } else {
            headerNames.add("Index");
            headerNames.add("File");
            headerNames.add("Term");
            headerNames.add("Pop. Hits");
            headerNames.add("Pop. Size");
        }
    }

    /** Returns cluster indices
     * @param clusterList list of cluster indices
     * @param hitList List of acc. in each category
     * @return
     */
    private int[][] getClusters(EaseElementList clusterList, String[][] hitList) {
        int[][] clusters = new int[hitList.length][];
        for (int i = 0; i < hitList.length; i++) {
            clusters[i] = clusterList.getIndices(hitList[i]);
        }
        return clusters;
    }

    /** Sorts analysis results on stat.
     */
    private void sortResults() {
        double[] stat = new double[result.length];
        int pValueIndex;
        if (reportEaseScore) pValueIndex = headerNames.indexOf("EASE Score"); else pValueIndex = headerNames.indexOf("Fisher's Exact");
        pValueIndex--;
        for (int i = 0; i < result.length; i++) {
            stat[i] = Double.parseDouble(result[i][pValueIndex]);
        }
        QSort qsorter = new QSort(stat);
        stat = qsorter.getSortedDouble();
        int[] orderedIndices = qsorter.getOrigIndx();
        String[] holder;
        String nameHolder;
        String[] newCatNames = new String[categoryNames.length];
        String[][] newHitList = new String[hitList.length][];
        String[][] newResult = new String[result.length][];
        for (int i = 0; i < orderedIndices.length; i++) {
            newCatNames[i] = categoryNames[orderedIndices[i]];
            newHitList[i] = hitList[orderedIndices[i]];
            newResult[i] = result[orderedIndices[i]];
        }
        for (int i = 0; i < newResult.length; i++) {
            newResult[i][pValueIndex] = format.format(Double.parseDouble(newResult[i][pValueIndex]));
            newResult[i][pValueIndex] = newResult[i][pValueIndex].replace(',', '.');
        }
        categoryNames = newCatNames;
        hitList = newHitList;
        result = newResult;
    }

    /** Sorts survey analysis results on population hits (high --> low)
     */
    private void sortSurveyResults() {
        double[] hitCounts = new double[result.length];
        int hitIndex = this.headerNames.indexOf("Pop. Hits");
        hitIndex--;
        for (int i = 0; i < result.length; i++) {
            hitCounts[i] = Double.parseDouble(result[i][hitIndex]);
        }
        QSort qsorter = new QSort(hitCounts);
        hitCounts = qsorter.getSortedDouble();
        int[] orderedIndices = qsorter.getOrigIndx();
        String[] holder;
        String nameHolder;
        String[] newCatNames = new String[categoryNames.length];
        String[][] newHitList = new String[hitList.length][];
        String[][] newResult = new String[result.length][];
        int index = 0;
        for (int i = orderedIndices.length - 1; i >= 0; i--) {
            newCatNames[index] = categoryNames[orderedIndices[i]];
            newHitList[index] = hitList[orderedIndices[i]];
            newResult[index] = result[orderedIndices[i]];
            index++;
        }
        categoryNames = newCatNames;
        hitList = newHitList;
        result = newResult;
    }

    /** Appends accessions
     * @param resultMatrix Result matrix input.
     * @param fileNames File names.
     * @return
     */
    private String[][] appendAccessions(String[][] resultMatrix, String[] fileNames) {
        if (resultMatrix == null || resultMatrix.length < 1) return resultMatrix;
        String[][] newResult = null;
        File file = null;
        haveAccessionNumbers = false;
        try {
            for (int i = 0; i < fileNames.length; i++) {
                file = getAccessionFile(fileNames[i]);
                if (file.isFile()) {
                    if (!haveAccessionNumbers) {
                        newResult = new String[resultMatrix.length][resultMatrix[0].length + 1];
                        initializeNewResult(newResult, resultMatrix);
                        headerNames.insertElementAt("Acc.", 2);
                    }
                    insertAccessions(file, newResult);
                    haveAccessionNumbers = true;
                    resultMatrix = newResult;
                }
            }
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(new JFrame(), "Error in collecting accessions following analysis" + " from file: " + file.getName() + "\n Results will not have accessions.  Please check file location" + " and format", "File Error", JOptionPane.WARNING_MESSAGE);
            return result;
        }
        if (haveAccessionNumbers) return newResult; else return resultMatrix;
    }

    /** Builds a result copy
     */
    private void initializeNewResult(String[][] newResult, String[][] oldResult) {
        for (int i = 0; i < newResult.length; i++) {
            for (int j = 0; j < oldResult[0].length; j++) {
                if (j < 1) newResult[i][j] = oldResult[i][j]; else newResult[i][j + 1] = oldResult[i][j];
            }
        }
        for (int i = 0; i < newResult.length; i++) newResult[i][1] = " ";
    }

    /** Inserts an index for each record in the result after sorting
     */
    private void indexResult() {
        if (result == null || result.length < 1) return;
        String[][] newResult = new String[result.length][result[0].length + 1];
        for (int i = 0; i < result.length; i++) {
            newResult[i][0] = String.valueOf(i + 1);
            for (int j = 1; j < newResult[0].length; j++) {
                newResult[i][j] = result[i][j - 1];
            }
        }
        result = newResult;
    }

    /** Insert accession numbers if they exist.
     * @param file file object
     * @param result Result data
     * @throws IOException
     */
    private void insertAccessions(File file, String[][] result) throws IOException {
        if (file == null) return;
        BufferedReader fr = new BufferedReader(new FileReader(file));
        String line;
        Hashtable accHash = new Hashtable();
        StringTokenizer stok;
        while ((line = fr.readLine()) != null) {
            stok = new StringTokenizer(line, "\t");
            accHash.put(stok.nextToken(), stok.nextToken());
        }
        String acc;
        for (int i = 0; i < result.length; i++) {
            acc = (String) accHash.get(result[i][2]);
            if (acc != null) result[i][1] = acc;
        }
    }

    /** Creates the <CODE>File</CODE> object containing the
     * accessions (or indices)
     * @param fileName File name String
     * @return
     */
    private File getAccessionFile(String fileName) {
        String sep = System.getProperty("file.separator");
        File file = new File(fileName);
        String accFileName = file.getName();
        file = file.getParentFile();
        file = new File(file.getPath() + sep + "URL data" + sep + "Tags" + sep + accFileName);
        return file;
    }

    /** Returns header names based on criteria of the analysis mode and
     * depending on if accessions are found.
     * @return  */
    private String[] getHeaderNames() {
        String[] headerNamesArray = new String[headerNames.size()];
        for (int i = 0; i < headerNamesArray.length; i++) {
            headerNamesArray[i] = (String) (headerNames.elementAt(i));
        }
        return headerNamesArray;
    }

    /**
     *  Calculates means for the clusters
     */
    private FloatMatrix getMeans(FloatMatrix data, int[][] clusters) {
        FloatMatrix means = new FloatMatrix(clusters.length, data.getColumnDimension());
        for (int i = 0; i < clusters.length; i++) {
            means.A[i] = getMeans(data, clusters[i]);
        }
        return means;
    }

    /**
     *  Returns a set of means for an element
     */
    private float[] getMeans(FloatMatrix data, int[] indices) {
        int nSamples = data.getColumnDimension();
        float[] means = new float[nSamples];
        float sum = 0;
        float n = 0;
        float value;
        for (int i = 0; i < nSamples; i++) {
            n = 0;
            sum = 0;
            for (int j = 0; j < indices.length; j++) {
                value = data.get(indices[j], i);
                if (!Float.isNaN(value)) {
                    sum += value;
                    n++;
                }
            }
            if (n > 0) means[i] = sum / n; else means[i] = Float.NaN;
        }
        return means;
    }

    /** Returns a matrix of standard deviations grouped by cluster and element
     * @param data Expression data
     * @param means calculated means
     * @param clusters cluster indices
     * @return
     */
    private FloatMatrix getVariances(FloatMatrix data, FloatMatrix means, int[][] clusters) {
        int nSamples = data.getColumnDimension();
        FloatMatrix variances = new FloatMatrix(clusters.length, nSamples);
        for (int i = 0; i < clusters.length; i++) {
            variances.A[i] = getVariances(data, means, clusters[i], i);
        }
        return variances;
    }

    /** Calculates the standard deviation for a set of genes.  One SD for each experiment point
     * in the expression vectors.
     * @param data Expression data
     * @param means previously calculated means
     * @param indices gene indices for cluster members
     * @param clusterIndex the index for the cluster to work upon
     * @return
     */
    private float[] getVariances(FloatMatrix data, FloatMatrix means, int[] indices, int clusterIndex) {
        int nSamples = data.getColumnDimension();
        float[] variances = new float[nSamples];
        float sse = 0;
        float mean;
        float value;
        int n = 0;
        for (int i = 0; i < nSamples; i++) {
            mean = means.get(clusterIndex, i);
            n = 0;
            sse = 0;
            for (int j = 0; j < indices.length; j++) {
                value = data.get(indices[j], i);
                if (!Float.isNaN(value)) {
                    sse += (float) Math.pow((value - mean), 2);
                    n++;
                }
            }
            if (n > 1) variances[i] = (float) Math.sqrt(sse / (n - 1)); else variances[i] = 0.0f;
        }
        return variances;
    }

    /** Appends a result onto the main result
     * @param resultVector data to append
     */
    private void appendResult(Vector resultVector) {
        int numCorr = resultVector.size();
        double[] currentArray;
        int rawPIndex = result[0].length;
        int resultColumns = rawPIndex + numCorr;
        String[][] newResult = new String[result.length][resultColumns];
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[0].length; j++) {
                newResult[i][j] = result[i][j];
            }
        }
        int resultCol;
        for (int col = 0; col < numCorr; col++) {
            currentArray = (double[]) resultVector.elementAt(col);
            resultCol = col + rawPIndex;
            for (int row = 0; row < newResult.length; row++) {
                newResult[row][resultCol] = format.format(currentArray[row]);
                newResult[row][resultCol] = newResult[row][resultCol].replace(',', '.');
            }
        }
        result = newResult;
    }

    /** Selects and makes calles to various multiplicity corrections.
     */
    private void pValueCorrections(AlgorithmData inputData) {
        int k = this.result.length;
        double[] pValues = new double[k];
        double[] correctedP = new double[k];
        int pIndex;
        if (reportEaseScore) pIndex = headerNames.indexOf("EASE Score"); else pIndex = headerNames.indexOf("Fisher's Exact");
        pIndex--;
        Vector pValueCorrectionVector = new Vector();
        for (int i = 0; i < k; i++) {
            pValues[i] = Double.parseDouble(result[i][pIndex]);
        }
        AlgorithmParameters params = inputData.getParams();
        if (params.getBoolean("bonferroni-correction", false)) {
            pValueCorrectionVector.add(bonferroniCorrection(pValues));
            headerNames.add("Bonf. Corr.");
        }
        if (params.getBoolean("bonferroni-step-down-correction", false)) {
            pValueCorrectionVector.add(stepDownBonferroniCorrection(pValues));
            headerNames.add("Bonf. S.D. Corr.");
        }
        if (params.getBoolean("sidak-correction", false)) {
            pValueCorrectionVector.add(sidakCorrection(pValues));
            headerNames.add("Sidak Corr.");
        }
        appendResult(pValueCorrectionVector);
    }

    /** Performs the standard Bonferroni correction.
     * @param pValues Raw values
     * @return Returns corrected values.
     */
    private double[] bonferroniCorrection(double[] pValues) {
        int k = pValues.length;
        double[] correctedP = new double[k];
        for (int i = 0; i < k; i++) {
            correctedP[i] = pValues[i] * (double) k;
            if (correctedP[i] > 1.0d) correctedP[i] = 1.0d;
        }
        return correctedP;
    }

    /** Performs the step down Bonferroni correction.
     * @param pValues input values
     * @return returns corrected values
     */
    private double[] stepDownBonferroniCorrection(double[] pValues) {
        int k = pValues.length;
        double[] correctedP = new double[k];
        int m = 0;
        correctedP[0] = pValues[0] * (double) k;
        for (int i = 1; i < k; i++) {
            if (pValues[i] > pValues[i - 1]) m = i;
            correctedP[i] = pValues[i] * (double) (k - m);
            if (correctedP[i] > 1.0d) correctedP[i] = 1.0d;
        }
        return correctedP;
    }

    /** Perform Sidak method.
     * @param pValues input
     * @return corrected output
     */
    private double[] sidakCorrection(double[] pValues) {
        int k = pValues.length;
        double[] correctedP = new double[k];
        for (int i = 0; i < k; i++) {
            correctedP[i] = 1.0d - Math.pow((1.0d - pValues[i]), (double) k);
            if (correctedP[i] > 1.0d) correctedP[i] = 1.0d;
        }
        return correctedP;
    }

    /** performs permutation analysis, bootstrapping selection of random samples from
     * the population.
     * @param p number of permutations
     */
    private void permutationAnalysis(int p) {
        AlgorithmEvent permEvent = new AlgorithmEvent(this, AlgorithmEvent.PROGRESS_VALUE, p);
        permEvent.setDescription("SET_UNITS");
        permEvent.setIntValue(p);
        fireValueChanged(permEvent);
        permEvent.setDescription("SET_VALUE");
        long start = System.currentTimeMillis();
        int k = result.length;
        int sampleSize = this.sampleVector.size();
        int populationSize = this.populationVector.size();
        int[] accumulator = new int[this.result.length];
        for (int i = 0; i < accumulator.length; i++) accumulator[0] = 0;
        int[] hitNumberAcc = new int[sampleSize];
        Random rand = new Random(System.currentTimeMillis());
        String[][] testResult;
        long sampleTime = 0;
        long startsamp;
        for (int i = 0; i < p; i++) {
            permEvent.setIntValue(i + 1);
            fireValueChanged(permEvent);
            sampleVector = getRandomSampleVector(sampleSize, rand);
            jstats.resetForNewList();
            jstats.GetListHitsByCategory(sampleVector);
            jstats.ConstructResults();
            testResult = jstats.getResults();
            accumulateBinHits(testResult, accumulator);
        }
        double[] prob = new double[k];
        for (int i = 0; i < k; i++) {
            prob[i] = (double) accumulator[i] / (double) p;
            if (prob[i] == 0d) prob[i] = 1d / (double) p;
        }
        permEvent.setDescription("DISPOSE");
        fireValueChanged(permEvent);
        Vector probVector = new Vector();
        probVector.add(prob);
        appendResult(probVector);
        headerNames.add("Prob. Anal.");
    }

    /** Returns the maximum number of population hits for any category.
     */
    private int getMaxPopHits(String[][] result) {
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < result.length; i++) {
            max = Math.max(max, Integer.parseInt(result[i][4]));
        }
        return max;
    }

    /** Returns a random sample vector of indices.
     */
    private Vector getRandomSampleVector(int sampleSize, Random rand) {
        Vector sampleVector = new Vector(sampleSize);
        Vector dummyPopVector = (Vector) populationVector.clone();
        int popSize = populationVector.size();
        int index = 0;
        for (int i = 0; i < sampleSize; i++) {
            index = (int) (dummyPopVector.size() * rand.nextFloat());
            sampleVector.add(dummyPopVector.remove(index));
        }
        return sampleVector;
    }

    /** accumulates list hits results from permutations
     * @param result
     * @param keys
     * @param accumulator  */
    private void accumulateHits(String[][] result, String[] keys, int[] accumulator) {
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < keys.length; j++) {
                if ((result[i][1]).equals(keys[j])) {
                    if (Integer.parseInt(result[i][2]) > Integer.parseInt(this.result[j][2])) accumulator[j]++;
                    break;
                }
            }
        }
    }

    /** Accumulates results from permutations.
     */
    private void accumulateBinHits(String[][] newResult, int[] accumulator) {
        double minP;
        for (int cat = 0; cat < annotationFileList.length; cat++) {
            minP = Double.POSITIVE_INFINITY;
            for (int i = 0; i < newResult.length; i++) {
                if (this.annotationFileList[cat].indexOf(newResult[i][0]) >= 0) minP = Math.min(minP, Double.parseDouble(newResult[i][6]));
            }
            for (int j = 0; j < result.length; j++) {
                if ((this.annotationFileList[cat].indexOf(result[j][0]) >= 0) && (minP < Double.parseDouble(result[j][6]))) {
                    accumulator[j]++;
                }
            }
        }
    }

    /** Orders the bootstrap probabilities based on raw probability order.
     */
    private double[] orderBootStrappedProb(double[] prob) {
        double[] orderedProb = new double[result.length];
        for (int i = 0; i < this.result.length; i++) {
            orderedProb[i] = prob[Integer.parseInt(this.result[i][2])];
        }
        return orderedProb;
    }

    /** Trims the results according to specified criteria.
     * @param trimOption Defines trim mode, "NO_TRIM", "N_TRIM", or "PERCENT_TRIM"
     * @param trimValue Trim parameter.
     */
    private void trimResult(String trimOption, float trimValue) {
        boolean[] flagged = new boolean[result.length];
        int hitIndex;
        if (this.performClusterAnalysis) {
            hitIndex = this.headerNames.indexOf("List Hits");
        } else {
            hitIndex = this.headerNames.indexOf("Pop. Hits");
        }
        hitIndex--;
        int keeperCount = this.result.length;
        if (trimOption.equals("N_TRIM")) {
            for (int i = 0; i < result.length; i++) {
                if (Integer.parseInt(result[i][hitIndex]) < trimValue) {
                    flagged[i] = true;
                    keeperCount--;
                }
            }
        } else {
            trimValue /= (float) 100;
            for (int i = 0; i < result.length; i++) {
                if (Double.parseDouble(result[i][hitIndex]) / Double.parseDouble(result[i][hitIndex + 1]) < trimValue) {
                    flagged[i] = true;
                    keeperCount--;
                }
            }
        }
        String[][] newResult = new String[keeperCount][];
        String[][] newHitList = new String[keeperCount][];
        String[] newCategoryNames = new String[keeperCount];
        int keeperIndex = 0;
        for (int i = 0; i < result.length; i++) {
            if (!flagged[i]) {
                newResult[keeperIndex] = result[i];
                newHitList[keeperIndex] = hitList[i];
                newCategoryNames[keeperIndex] = categoryNames[i];
                keeperIndex++;
            }
        }
        this.result = newResult;
        this.hitList = newHitList;
        this.categoryNames = newCategoryNames;
    }
}
