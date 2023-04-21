package edu.smu.cse8377.algs;

import java.util.ArrayList;
import edu.smu.cse8377.inf.config.Config;
import edu.smu.cse8377.inf.log.LogIt;

/**
 * Encapsulate Reed-Solomon Erasure Coding
 * 
 * @author acrahan
 * 
 */
public class ReedSoloman extends AbsNullableAlgorithm {

    /**
     * ClassName for Logging
     */
    private static final String CLASSNAME = ReedSoloman.class.getName();

    /**
     * Symbolic name for System Config
     */
    private static final String CONFIG_NMKEY = LogIt.CONFIG_NAMEKEY;

    /**
     * Key for Number of Data Bytes to encode configuration setting
     */
    private static final String CONFIG_NUMDATAKEY = "RSNumData";

    /**
     * Key for Number of Check Bytes to encode configuration setting
     */
    private static final String CONFIG_NUMCHECKKEY = "RSNumCheck";

    /**
     * Num Data Bytes to Encode
     */
    private int numData;

    /**
     * Num Check Bytes to Encode
     */
    private int numCheck;

    /**
     * Constructor for ReedSoloman
     * 
     * @param bytes Byte[]
     * @throws Exception if a problem occurs
     */
    public ReedSoloman(Object o, Byte[] bytes) throws Exception {
        super(o, bytes);
        loadConfigValues();
    }

    /**
     * Method to load values from config
     *
     * @throws Exception if a problem occurs
     */
    private void loadConfigValues() throws Exception {
        final String mN = CLASSNAME + ".loadConfigValues() -> ";
        LogIt.getInstance().logNormal(mN + "Loading configuration values...");
        numData = Config.getInstance().getConfig(CONFIG_NMKEY).getValueAsInt(CONFIG_NUMDATAKEY);
        numCheck = Config.getInstance().getConfig(CONFIG_NMKEY).getValueAsInt(CONFIG_NUMCHECKKEY);
        LogIt.getInstance().logNormal(mN + "Values loaded OK.");
    }

    /**
     * MANDATORY OVERRIDE:
     * Entry-point to "encoding" logic
     * 
     * @throws Exception if a problem occurs
     * @see edu.smu.cse8377.algs.AbsECDAlgorithm#prepareDataForTransmission()
     */
    @Override
    protected void prepareDataForTransmission() throws Exception {
        LogIt.logNormalCleanup("data length is " + data.length);
        LogIt.logNormalCleanup("trueData length is " + trueData.length);
        for (int tdl = 0; tdl < trueData.length; tdl++) {
            LogIt.logNormalCleanup("[" + tdl + "]=" + trueData[tdl]);
        }
        double[][] rsmatrix = new double[numData + numCheck][numData];
        ArrayList<Byte> finalBytes = new ArrayList<Byte>();
        for (int y = 0; y < numData + numCheck; y++) {
            for (int x = 0; x < numData; x++) {
                rsmatrix[y][x] = Math.pow(y, x);
            }
        }
        LogIt.logNormalCleanup("Vandermonde Matrix - " + (numCheck + numData) + "x" + numData);
        matrixOut(rsmatrix, numData + numCheck, numData);
        for (int iter = 0; iter < numData; iter++) {
            if (rsmatrix[iter][iter] != 1) {
                double operation = rsmatrix[iter][iter];
                for (int y = 0; y < numData + numCheck; y++) {
                    rsmatrix[y][iter] = rsmatrix[y][iter] / operation;
                }
            }
            for (int x = 0; x < numData; x++) {
                if (x != iter) {
                    for (int y = 0; y < numData + numCheck; y++) {
                        rsmatrix[y][x] = rsmatrix[y][x] - rsmatrix[iter][x] * rsmatrix[y][iter];
                        ;
                    }
                }
            }
            LogIt.logNormalCleanup("Reducing the Matrix - " + (numCheck + numData) + "x" + numCheck);
            matrixOut(rsmatrix, numData + numCheck, numData);
        }
        LogIt.logNormalCleanup("Reed Solomon Matrix");
        matrixOut(rsmatrix, numData + numCheck, numData);
        Double[] DoubleDs = new Double[numData + numCheck];
        LogIt.logNormalCleanup("Calculated Bytes to Send");
        for (int y = 0; y < numData + numCheck; y++) {
            double rowtotal = 0;
            for (int x = 0; x < numData; x++) {
                rowtotal += rsmatrix[y][x] * trueData[x];
            }
            finalBytes.add((byte) rowtotal);
            DoubleDs[y] = rowtotal;
            LogIt.logNormalCleanup("" + rowtotal);
        }
        AlgDataPersistence.setRSData(DoubleDs);
        int size = finalBytes.size();
        trueData = new Byte[size];
        for (int i = 0; i < size; i++) {
            trueData[i] = finalBytes.get(i);
        }
        LogIt.logNormalCleanup("Bytes Sent");
        for (int i = 0; i < trueData.length; i++) {
            LogIt.logNormalCleanup("" + trueData[i]);
        }
    }

    /**
     * MANDATORY OVERRIDE:
     * Entry-point into "receive" logic
     * 
     * @throws Exception
     * @see edu.smu.cse8377.algs.AbsECDAlgorithm#prepareDataForReception()
     */
    @Override
    protected void prepareDataForReception() throws Exception {
        StringBuilder arrDebug = new StringBuilder();
        arrDebug.append("\n***TRUEDATA ARRAY BEFORE EXECUTION***");
        for (int i = 0; i < trueData.length; i++) {
            arrDebug.append("\n[" + i + "]: [" + trueData[i] + "]");
        }
        arrDebug.append("\n***END ARRAY***");
        LogIt.logNormalCleanup(arrDebug.toString());
        double[][] rsmatrix = new double[numData + numCheck][numData];
        double[][] somatrix = new double[numData][numData + 1];
        Double[] DoubleDs = new Double[numData + numCheck];
        DoubleDs = AlgDataPersistence.getRSData();
        for (int y = 0; y < numData + numCheck; y++) {
            for (int x = 0; x < numData; x++) {
                rsmatrix[y][x] = Math.pow(y, x);
            }
        }
        LogIt.logNormalCleanup("Reed Solomon Starter Matrix");
        matrixOut(rsmatrix, numData + numCheck, numData);
        for (int iter = 0; iter < numData; iter++) {
            if (rsmatrix[iter][iter] != 1) {
                double operation = rsmatrix[iter][iter];
                for (int y = 0; y < numData + numCheck; y++) {
                    rsmatrix[y][iter] = rsmatrix[y][iter] / operation;
                }
            }
            for (int x = 0; x < numData; x++) {
                if (x != iter) {
                    for (int y = 0; y < numData + numCheck; y++) {
                        rsmatrix[y][x] = rsmatrix[y][x] - rsmatrix[iter][x] * rsmatrix[y][iter];
                        ;
                    }
                }
            }
        }
        LogIt.logNormalCleanup("Reed Solomon Identity Matrix");
        matrixOut(rsmatrix, numData + numCheck, numData);
        byte packetsUsed = 0;
        byte goodBytesUsed = 0;
        byte checkBytesUsed = (byte) numData;
        for (int iter = 0; iter < numData; iter++) {
            if (packetsUsed != numData) {
                if (trueData[iter] == null) {
                    for (int y = checkBytesUsed; y < numData + numCheck; y++) {
                        if (trueData[y] != null) {
                            for (int x = 0; x < numData; x++) {
                                somatrix[iter][x] = rsmatrix[y][x];
                            }
                            somatrix[iter][numData] = DoubleDs[y];
                            packetsUsed++;
                            checkBytesUsed++;
                            break;
                        }
                        checkBytesUsed++;
                    }
                } else {
                    for (int y = goodBytesUsed; y < numData; y++) {
                        if (trueData[y] != null) {
                            for (int x = 0; x < numData; x++) {
                                somatrix[iter][x] = rsmatrix[y][x];
                            }
                            somatrix[iter][numData] = trueData[y];
                            packetsUsed++;
                            goodBytesUsed++;
                            break;
                        }
                        goodBytesUsed++;
                    }
                }
            }
        }
        if (packetsUsed == numData) {
            LogIt.logNormalCleanup("Solvable Square Matrix");
            matrixOut(somatrix, numData, numData + 1);
            for (int iter = 0; iter < numData; iter++) {
                if (somatrix[iter][iter] != 1) {
                    double operation = somatrix[iter][iter];
                    for (int x = 0; x < numData + 1; x++) {
                        somatrix[iter][x] = somatrix[iter][x] / operation;
                    }
                }
                for (int y = iter + 1; y < numData; y++) {
                    if (y != iter) {
                        double operation = somatrix[y][iter];
                        for (int x = 0; x < numData + 1; x++) {
                            double mult = operation * somatrix[iter][x];
                            somatrix[y][x] = somatrix[y][x] - mult;
                        }
                    }
                }
            }
            LogIt.logNormalCleanup("Row Echelon Solvable Square Matrix");
            matrixOut(somatrix, numData, numData + 1);
            double total = 0;
            for (int iter = numData - 1; iter > -1; iter--) {
                for (int y = iter + 1; y < numData; y++) {
                    total += somatrix[y][y] * somatrix[iter][y];
                }
                total = somatrix[iter][numData] - total;
                somatrix[iter][iter] = total;
                total = 0;
            }
            LogIt.logNormalCleanup("Solved Matrix, diagonals are answers.");
            matrixOut(somatrix, numData, numData);
            trueData = new Byte[numData];
            for (int i = 0; i < numData; i++) {
                trueData[i] = (byte) somatrix[i][i];
                if (trueData[i] < somatrix[i][i] - .5) {
                    trueData[i]++;
                }
                LogIt.logNormalCleanup("Reed Solomon rounding " + somatrix[i][i] + " to " + trueData[i]);
                LogIt.logNormalCleanup("Ending Solomon Data[" + i + "]: " + trueData[i]);
            }
            arrDebug = new StringBuilder();
            arrDebug.append("\n***TRUEDATA ARRAY AFTER EXECUTION***");
            for (int i = 0; i < trueData.length; i++) {
                arrDebug.append("\n[" + i + "]: " + trueData[i]);
            }
            arrDebug.append("\n***END ARRAY***");
            LogIt.logNormalCleanup(arrDebug.toString());
            for (int i = 0; i < trueData.length; i++) {
                if (trueData[i] == null) {
                    throw new Exception("INTERNAL_ERROR: Null data byte found at index " + i + " - should not have any nulls left after Reed-Solomon executes!");
                }
            }
            LogIt.logNormalCleanup("Reed-Solomon yielded NO null bytes - all is well.");
        } else {
            LogIt.logErrorCleanup("ReedSolomon Code lost too many packets to recover data.");
            trueData = new Byte[0];
        }
    }

    /**
     * Convenience method to print a matrix to the logs
     *
     * @param somatrix double[][] matrix
     * @param r int
     * @param c int
     * @throws Exception if a problem occurs
     */
    private void matrixOut(double[][] somatrix, int r, int c) throws Exception {
        String log = "";
        for (int y = 0; y < r; y++) {
            log += "[";
            for (int x = 0; x < c; x++) {
                log += " " + somatrix[y][x];
            }
            log += "]";
            LogIt.logNormalCleanup(log);
            log = "";
        }
    }
}
