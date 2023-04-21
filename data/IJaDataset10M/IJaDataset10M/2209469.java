package ytex.svmlight;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ytex.kernel.model.ClassifierEvaluation;
import ytex.kernel.model.ClassifierInstanceEvaluation;
import ytex.kernel.model.SVMClassifierEvaluation;
import ytex.libsvm.LibSVMParser;

/**
 * same as libsvm with following changes:
 * <ul>
 * <li>parse output.txt - contains vcdim and number support vectors <
 * </ul>
 */
public class SVMLightParser extends LibSVMParser {

    static final Pattern psv = Pattern.compile("Number of SV:\\s(\\d+)\\s.*");

    static final Pattern pvc = Pattern.compile("Estimated VCdim of classifier: VCdim<=([\\d\\.]+)");

    /**
	 * Parse svm-classify input (instance file) and predictions (prediction
	 * file). instance file has target class id and attributes for each
	 * instance. predict file has value less than or greater than 0 for each
	 * instance, corresponding to class ids -1 and +1.
	 * 
	 * @param predictionFile
	 * @param instanceFile
	 * @return
	 * @throws IOException
	 */
    @Override
    protected void parsePredictions(String predictionFile, String instanceFile, Properties props, String instanceIdFile, SVMClassifierEvaluation eval) throws IOException {
        List<Long> instanceIds = null;
        if (instanceIdFile != null) instanceIds = parseInstanceIds(instanceIdFile);
        boolean bStoreUnlabeled = YES.equals(props.getProperty(ParseOption.STORE_UNLABELED.getOptionKey(), ParseOption.STORE_UNLABELED.getDefaultValue()));
        BufferedReader instanceReader = null;
        BufferedReader predictionReader = null;
        try {
            instanceReader = new BufferedReader(new FileReader(instanceFile));
            predictionReader = new BufferedReader(new FileReader(predictionFile));
            String instanceLine = null;
            String predictionLine = null;
            int nLine = 0;
            while (((instanceLine = instanceReader.readLine()) != null) && ((predictionLine = predictionReader.readLine()) != null)) {
                long instanceId = instanceIds.size() > nLine ? instanceIds.get(nLine) : nLine;
                nLine++;
                int classIdTarget = Integer.parseInt(extractFirstToken(instanceLine, wsPattern));
                if (bStoreUnlabeled || classIdTarget != 0) {
                    ClassifierInstanceEvaluation result = new ClassifierInstanceEvaluation();
                    result.setTargetClassId(classIdTarget == 0 ? null : classIdTarget);
                    int classIdPredicted = 0;
                    try {
                        double dPredict = Double.parseDouble(predictionLine);
                        if (dPredict > 0) classIdPredicted = 1; else classIdPredicted = -1;
                    } catch (NumberFormatException nfe) {
                        System.err.println("error parsing:" + predictionLine);
                        nfe.printStackTrace(System.err);
                    }
                    result.setPredictedClassId(classIdPredicted);
                    result.setInstanceId(instanceId);
                    result.setClassifierEvaluation(eval);
                    eval.getClassifierInstanceEvaluations().put(instanceId, result);
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

    /**
	 * <pre>
	 * Number of SV: 133 (including 0 at upper bound)
	 * L1 loss: loss=0.00000
	 * Norm of weight vector: |w|=2.09380
	 * Norm of longest example vector: |x|=16.91153
	 * Estimated VCdim of classifier: VCdim<=684.90185
	 * </pre>
	 * 
	 * @param eval
	 * @param trainOutputFile
	 * @throws IOException
	 */
    private void parseTrainOutput(SVMClassifierEvaluation eval, String trainOutputFile) throws IOException {
        if (trainOutputFile == null) return;
        BufferedReader r = null;
        try {
            r = new BufferedReader(new FileReader(trainOutputFile));
            String line = null;
            while ((line = r.readLine()) != null) {
                Matcher m = psv.matcher(line);
                if (m.matches()) eval.setSupportVectors(Integer.parseInt(m.group(1)));
                m = pvc.matcher(line);
                if (m.matches()) eval.setVcdim(Double.parseDouble(m.group(1)));
            }
        } catch (FileNotFoundException fnfe) {
        } finally {
            if (r != null) r.close();
        }
    }

    /**
	 * parse output.txt - contains vcdim and number support vectors
	 */
    @Override
    protected void parseResults(File dataDir, File outputDir, String model, String predict, SVMClassifierEvaluation eval, Properties props) throws IOException {
        super.parseResults(dataDir, outputDir, model, predict, eval, props);
        eval.setAlgorithm("svmlight");
        parseTrainOutput(eval, outputDir + File.separator + "output.txt");
    }

    /**
	 * store semi supervised results.
	 */
    @Override
    protected void storeResults(File dataDir, Properties props, SVMClassifierEvaluation eval) {
        this.storeSemiSupervised(props, eval, null);
    }
}
