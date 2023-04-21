package ytex.libsvm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ytex.kernel.BaseClassifierEvaluationParser;
import ytex.kernel.model.ClassifierEvaluation;
import ytex.kernel.model.ClassifierInstanceEvaluation;
import ytex.kernel.model.SVMClassifierEvaluation;

public class LibSVMParser extends BaseClassifierEvaluationParser {

    public static Pattern labelsPattern = Pattern.compile("labels\\s+(.*)");

    public static Pattern totalSVPattern = Pattern.compile("total_sv (\\d+)");

    public static Pattern pKernel = Pattern.compile("-t\\s+(\\d)");

    public static Pattern pGamma = Pattern.compile("-g\\s+([\\d\\.eE-]+)");

    public static Pattern pCost = Pattern.compile("-c\\s+([\\d\\.eE-]+)");

    public static Pattern pWeight = Pattern.compile("-w-{0,1}\\d\\s+[\\d\\.]+\\b");

    public static Pattern pDegree = Pattern.compile("-d\\s+(\\d+)");

    /**
	 * parse svm-train model file to get the number of support vectors. Needed
	 * for model selection
	 * 
	 * @param modelFile
	 * @return
	 * @throws IOException
	 */
    public Integer parseModel(String modelFile) throws IOException {
        BufferedReader r = null;
        try {
            r = new BufferedReader(new FileReader(modelFile));
            String line = null;
            while ((line = r.readLine()) != null) {
                Matcher m = totalSVPattern.matcher(line);
                if (m.find()) {
                    return new Integer(m.group(1));
                }
            }
        } finally {
            try {
                if (r != null) r.close();
            } catch (Exception e) {
                System.err.println("reading model file");
                e.printStackTrace(System.err);
            }
        }
        return null;
    }

    /**
	 * parse class ids from first line in prediction file. this correspond to
	 * probabilities
	 * 
	 * @param predictionReader
	 * @return
	 * @throws IOException
	 */
    protected List<Integer> parseClassIds(BufferedReader predictionReader) throws IOException {
        List<Integer> labels = null;
        String labelLine = predictionReader.readLine();
        Matcher labelMatcher = labelsPattern.matcher(labelLine);
        if (labelMatcher.find()) {
            String labelsA[] = wsPattern.split(labelMatcher.group(1));
            if (labelsA != null && labelsA.length > 0) {
                labels = new ArrayList<Integer>(labelsA.length);
                for (String label : labelsA) labels.add(Integer.parseInt(label));
            }
        }
        return labels;
    }

    protected SVMClassifierEvaluation initClassifierEval(String name, String experiment, String label, String options, String instanceIdFile) {
        SVMClassifierEvaluation eval = new SVMClassifierEvaluation();
        initClassifierEval(name, experiment, label, options, instanceIdFile, eval);
        return eval;
    }

    private void initClassifierEval(String name, String experiment, String label, String options, String instanceIdFile, ClassifierEvaluation eval) {
        initClassifierEvaluation(instanceIdFile, eval);
        eval.setName(name);
        eval.setExperiment(experiment);
        eval.setOptions(options);
    }

    /**
	 * parse predicted class ids, probabilities; correlate to target class ids
	 * and instance ids.
	 * 
	 * @param predictionFile
	 *            prediction (output)
	 * @param instanceFile
	 *            input data file; contains target class ids
	 * @param props
	 * @param instanceIdFile
	 *            instance ids corresponding to lines in input data file
	 * @param eval
	 * @throws IOException
	 */
    protected void parsePredictions(String predictionFile, String instanceFile, Properties props, String instanceIdFile, SVMClassifierEvaluation eval) throws IOException {
        boolean storeProbabilities = YES.equalsIgnoreCase(props.getProperty(ParseOption.STORE_PROBABILITIES.getOptionKey(), ParseOption.STORE_PROBABILITIES.getDefaultValue()));
        List<Long> instanceIds = null;
        if (instanceIdFile != null) instanceIds = parseInstanceIds(instanceIdFile);
        BufferedReader instanceReader = null;
        BufferedReader predictionReader = null;
        try {
            instanceReader = new BufferedReader(new FileReader(instanceFile));
            predictionReader = new BufferedReader(new FileReader(predictionFile));
            String instanceLine = null;
            String predictionLine = null;
            int nLine = 0;
            List<Integer> classIds = parseClassIds(predictionReader);
            while (((instanceLine = instanceReader.readLine()) != null) && ((predictionLine = predictionReader.readLine()) != null)) {
                long instanceId = instanceIds.size() > nLine ? instanceIds.get(nLine) : nLine;
                nLine++;
                ClassifierInstanceEvaluation instanceEval = new ClassifierInstanceEvaluation();
                String predictTokens[] = wsPattern.split(predictionLine);
                String classIdPredicted = predictTokens[0];
                String classIdTarget = extractFirstToken(instanceLine, wsPattern);
                instanceEval.setTargetClassId(Integer.parseInt(classIdTarget));
                instanceEval.setPredictedClassId(Integer.parseInt(classIdPredicted));
                instanceEval.setInstanceId(instanceId);
                instanceEval.setClassifierEvaluation(eval);
                eval.getClassifierInstanceEvaluations().put(instanceId, instanceEval);
                if (storeProbabilities && predictTokens.length > 1) {
                    for (int i = 1; i < predictTokens.length; i++) {
                        instanceEval.getClassifierInstanceProbabilities().put(classIds.get(i - 1), Double.parseDouble(predictTokens[i]));
                    }
                }
            }
        } finally {
            if (instanceReader != null) {
                try {
                    instanceReader.close();
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                }
            }
            if (predictionReader != null) {
                try {
                    predictionReader.close();
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                }
            }
        }
    }

    protected void parseOptions(SVMClassifierEvaluation eval, String options) {
        if (options != null) {
            eval.setKernel(parseIntOption(pKernel, options));
            if (eval.getKernel() == null) eval.setKernel(0);
            eval.setDegree(parseIntOption(pDegree, options));
            eval.setWeight(parseWeight(options));
            eval.setCost(parseDoubleOption(pCost, options));
            eval.setGamma(parseDoubleOption(pGamma, options));
        }
    }

    /**
	 * parse the weight options out of the libsvm command line. they are of the
	 * form -w0 1 -w2 1.5 ...
	 * 
	 * @param options
	 * @return null if no weight options, else weight options
	 */
    private String parseWeight(String options) {
        StringBuilder bWeight = new StringBuilder();
        Matcher m = pWeight.matcher(options);
        boolean bWeightParam = false;
        while (m.find()) {
            bWeightParam = true;
            bWeight.append(m.group()).append(" ");
        }
        if (bWeightParam) return bWeight.toString(); else return null;
    }

    /**
	 * parse directory. Expect following files:
	 * <ul>
	 * <li>model.txt - libsvm model file
	 * <li>options.properties - properties file with needed parameter settings
	 * (see ParseOption)
	 * <li>predict.txt - predictions on test set
	 * </ul>
	 */
    @Override
    public void parseDirectory(File dataDir, File outputDir) throws IOException {
        String model = outputDir.getPath() + File.separator + "model.txt";
        String predict = outputDir.getPath() + File.separator + "predict.txt";
        String optionsFile = outputDir.getPath() + File.separator + "options.properties";
        if (checkFileRead(model) && checkFileRead(predict) && checkFileRead(optionsFile)) {
            Properties props = this.loadProps(outputDir);
            SVMClassifierEvaluation eval = new SVMClassifierEvaluation();
            eval.setAlgorithm("libsvm");
            parseResults(dataDir, outputDir, model, predict, eval, props);
            storeResults(dataDir, props, eval);
        }
    }

    /**
	 * store the parsed classifier evaluation
	 * 
	 * @param props
	 * @param eval
	 * @throws IOException
	 */
    protected void storeResults(File dataDir, Properties props, SVMClassifierEvaluation eval) throws IOException {
        getClassifierEvaluationDao().saveClassifierEvaluation(eval, this.loadClassIdMap(dataDir, eval.getLabel()), YES.equalsIgnoreCase(props.getProperty(ParseOption.STORE_INSTANCE_EVAL.getOptionKey(), ParseOption.STORE_INSTANCE_EVAL.getDefaultValue())));
    }

    /**
	 * parse the results in the specified output dir. use reference data from
	 * dataDir.
	 * 
	 * @param dataDir
	 * @param outputDir
	 * @param model
	 * @param predict
	 * @param eval
	 * @param props
	 * @throws IOException
	 */
    protected void parseResults(File dataDir, File outputDir, String model, String predict, SVMClassifierEvaluation eval, Properties props) throws IOException {
        initClassifierEvaluationFromProperties(props, eval);
        eval.setSupportVectors(this.parseModel(model));
        parseOptions(eval, props.getProperty(ParseOption.EVAL_LINE.getOptionKey()));
        String fileBaseName = this.getFileBaseName(props);
        initClassifierEvaluation(fileBaseName, eval);
        String instanceIdFile = dataDir + File.separator + fileBaseName + "test_id.txt";
        String instanceFile = dataDir + File.separator + fileBaseName + "test_data.txt";
        this.parsePredictions(predict, instanceFile, props, instanceIdFile, eval);
    }
}
