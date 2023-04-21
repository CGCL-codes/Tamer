package org.epistasis.combinatoric.mdr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;

public class MDRExternalAPI {

    private String bestModelAttributes;

    private int cvc;

    private float balancedAccuracyOverall;

    private float balancedAccuracyCvPartitionTraining;

    private float balancedAccuracyCvPartitionTesting;

    private float balancedAccuracyBestModelTraining;

    private float balancedAccuracyBestModelTesting;

    private String mdrConsoleOutput;

    private Status status;

    public static void main(final String[] args) throws IOException {
        final MDRExternalAPI result = new MDRExternalAPI(3, 10, 0, "http://endeavour.dartmouth.edu/eva/MDR-SampleData.txt");
        System.out.println(result.toString());
        final MDRExternalAPI result2 = new MDRExternalAPI(1, 5, 0, "C:\\dev\\moorelab\\mdr\\data\\MDR-SampleData.txt");
        System.out.println(result2.toString());
        final MDRExternalAPI result3 = new MDRExternalAPI(1, 5, 0, "nonexistentfile");
        System.out.println(result3.toString());
    }

    /**
	 * @param level
	 * @param CVC
	 * @param randomSeed
	 * @param datasetIdentifier this can be a local filename or a URL
	 * @throws IOException
	 */
    public MDRExternalAPI(final int level, final int cv, final long randomSeed, final String dataFileName) throws IOException {
        final File tempSystemOut = File.createTempFile("MDRSimpleAnalysis_output", ".tmp");
        tempSystemOut.deleteOnExit();
        final PrintStream oldSystemOut = System.out;
        final PrintStream oldSystemErr = System.err;
        try {
            System.setOut(new PrintStream(tempSystemOut));
            System.setErr(new PrintStream(tempSystemOut));
            final String[] args = new String[] { "-cv=" + cv, "-min=" + level, "-max=" + level, "-seed=" + randomSeed, "-parallel", "-nolandscape", "-minimal_output=true", "-table_data=true", dataFileName };
            Main.main(args);
            final BufferedReader r = new BufferedReader(new FileReader(tempSystemOut));
            String line;
            final String lineSeparator = System.getProperty("line.separator");
            final StringBuilder sb = new StringBuilder("Results for mdr call with these parameters: " + Arrays.toString(args));
            sb.append(lineSeparator);
            boolean successfulCall = false;
            while ((line = r.readLine()) != null) {
                sb.append(line);
                sb.append(lineSeparator);
                if (line.startsWith(dataFileName)) {
                    final String[] results = line.split("\t");
                    bestModelAttributes = results[3];
                    cvc = Integer.parseInt(results[4]);
                    balancedAccuracyOverall = Float.parseFloat(results[5]);
                    balancedAccuracyCvPartitionTraining = Float.parseFloat(results[6]);
                    balancedAccuracyCvPartitionTesting = Float.parseFloat(results[7]);
                    balancedAccuracyBestModelTraining = Float.parseFloat(results[8]);
                    balancedAccuracyBestModelTesting = Float.parseFloat(results[9]);
                    successfulCall = true;
                    break;
                }
            }
            mdrConsoleOutput = sb.toString();
            if (successfulCall) {
                status = Status.SUCCESS;
            } else {
                status = Status.FAILURE;
            }
        } catch (final Exception ex) {
            status = Status.FAILURE;
            mdrConsoleOutput = ex.toString();
        } finally {
            System.setOut(oldSystemOut);
            System.setErr(oldSystemErr);
        }
    }

    public float getBalancedAccuracyBestModelTesting() {
        return balancedAccuracyBestModelTesting;
    }

    public float getBalancedAccuracyBestModelTraining() {
        return balancedAccuracyBestModelTraining;
    }

    public float getBalancedAccuracyCvPartitionTesting() {
        return balancedAccuracyCvPartitionTesting;
    }

    public float getBalancedAccuracyCvPartitionTraining() {
        return balancedAccuracyCvPartitionTraining;
    }

    public float getBalancedAccuracyOverall() {
        return balancedAccuracyOverall;
    }

    public String getBestModelAttributes() {
        return bestModelAttributes;
    }

    public int getCvc() {
        return cvc;
    }

    public String getMdrConsoleOutput() {
        return mdrConsoleOutput;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public String toString() {
        String result;
        if (status == Status.FAILURE) {
            result = status + "    " + getMdrConsoleOutput().replace(System.getProperty("line.separator"), " <CR> ");
        } else {
            result = "bestModel=" + bestModelAttributes + " cvc=" + cvc + " balancedAccuracyOverall=" + balancedAccuracyOverall + " balancedAccuracyCvPartitionTraining=" + balancedAccuracyCvPartitionTraining + " balancedAccuracyCvPartitionTesting=" + balancedAccuracyCvPartitionTesting + " balancedAccuracyBestModelTraining=" + balancedAccuracyBestModelTraining + " balancedAccuracyBestModelTesting=" + balancedAccuracyBestModelTesting;
        }
        return result;
    }

    enum Status {

        SUCCESS, FAILURE
    }
}
