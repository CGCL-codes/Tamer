package edu.smu.cse8377.algs;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import edu.smu.cse8377.inf.log.LogIt;

/**
 * Base class for the family of Algorithms which will be used to perform
 * Error Correction (EC) and/or Error Detection (ED) upon a byte[]
 * which is being transmitted to/from an endpoint (A or C) via the NetSim
 * module (B).
 *
 * @author mdtrl
 *
 */
public abstract class AbsECDAlgorithm {

    /**
     * ClassName for logging
     */
    private static final String CLASSNAME = AbsECDAlgorithm.class.getName();

    /**
     * Data Bytes to prepare for send or receive
     */
    protected byte[] data;

    /**
     * Flag indicating whether bytes have been prepped for sending
     */
    protected boolean isPreparedForSend;

    /**
     * Flag indicating whether bytes have been prepped for receiving
     */
    protected boolean isPreparedForRecv;

    /**
     * Flag indicating whether config and log infrastructure should be used
     * (for test mode=true, these will NOT be used).
     */
    private boolean isTestMode;

    /**
     * Constructor for AbsECDAlgorithm
     *
     * @param bytes byte[] - Data to prep for send/recv
     * @throws Exception if a problem occurs
     */
    public AbsECDAlgorithm(byte[] bytes) throws Exception {
        if (bytes == null) {
            throw new NullPointerException("Cannot perform algorithms upon a NULL array!");
        }
        data = new byte[bytes.length];
        for (int i = 0; i < data.length; i++) {
            data[i] = bytes[i];
        }
        isPreparedForSend = false;
        isPreparedForRecv = false;
    }

    /**
     * Constructor for AbsECDAlgorithm
     *
     * @param bytes Byte[] - for nullable bytes
     * @throws Exception if the input is null or has null bytes in it
     */
    public AbsECDAlgorithm(Object o, Byte[] bytes) throws Exception {
        if (bytes == null) {
            throw new NullPointerException("Cannot perform algorithms upon a NULL array!");
        }
        data = ByteConverter.convertByteArray(bytes);
        isPreparedForSend = false;
        isPreparedForRecv = false;
    }

    /**
     * Hook for Unit Testing of Algorithms in order to properly
     * enable output to STDOUT instead of to a logfile, since logging
     * and config files are not available in that mode.
     *
     */
    public void enableTestMode() {
        isTestMode = true;
    }

    /**
     * File Parser method - Simple - For Test Mode
     *
     * @param path String - path to file to read in and convert from Bit String
     * @return byte[] the bytes
     * @throws Exception if a problem occurs
     */
    private static byte[] parseFile(String path) throws Exception {
        final String mN = CLASSNAME + ".parseFile() -> ";
        File f = new File(path);
        LogIt.outNormalAsLog(mN + "Opening file to parse...");
        Scanner s = new Scanner(f);
        ArrayList<String> lines = new ArrayList<String>();
        LogIt.outNormalAsLog(mN + "Parsing...");
        while (s.hasNextLine()) {
            String line = s.nextLine();
            lines.add(line);
        }
        s.close();
        byte[] result = ByteConverter.convertFromBitString(lines);
        LogIt.outNormalAsLog(mN + "Returning byte[]...");
        return result;
    }

    /**
     * For Test mode, need to output file
     *
     * @param file String - path to output file
     * @param data byte[] - data to be outputted
     * @throws Exception if a problem occurs
     */
    private static void writeOutputFile(String file, byte[] data) throws Exception {
        final String mN = CLASSNAME + ".writeOutputFile() -> ";
        File f = new File(file);
        String toOutput = ByteConverter.convertFromBytes(data);
        LogIt.outNormalAsLog(mN + "Attempting to open connection to output file...");
        PrintWriter pw = new PrintWriter(f);
        LogIt.outNormalAsLog(mN + "File open for writing.  Outputting data...");
        pw.print(toOutput);
        pw.flush();
        pw.close();
        LogIt.outNormalAsLog(mN + "Output was successful.");
    }

    /**
     * Test scaffolding to enable unit-style testing on a per-algorithm basis.
     *
     * @param args
     */
    public static void main(String[] args) {
        final String mN = CLASSNAME + ".main() -> ";
        if (args.length == 0) {
            byte[] sampledata = { 1, 1, 1, 0, 0 };
            try {
                writeOutputFile("/home/mdtrl/inputfile.dat", sampledata);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        if (args.length != 4) {
            String error = "Must specify command line arguments properly, as follows:\n";
            error += "<AlgorithmClassNameToRun, no package> <path to file to use as input bytes> " + "<path to file to use as output bytes> <'R' or 'S' for RECV or SEND>";
            throw new IllegalArgumentException(error);
        }
        String algClass = args[0];
        String inputFile = args[1];
        String outputFile = args[2];
        String mode = args[3];
        boolean recv = false;
        if (mode.equalsIgnoreCase("R")) {
            recv = true;
        }
        try {
            byte[] inputdata = parseFile(inputFile);
            LogIt.outNormalAsLog(mN + "Attempting to get Class object for '" + algClass + "'...");
            Class clazz = Class.forName("edu.smu.cse8377.algs." + algClass);
            LogIt.outNormalAsLog(mN + "Attempting to get Constructor with parameter of byte[]...");
            Constructor ctor = clazz.getConstructor(byte[].class);
            AbsECDAlgorithm algorithmObject = (AbsECDAlgorithm) ctor.newInstance(inputdata);
            algorithmObject.enableTestMode();
            LogIt.outNormalAsLog(mN + "Got object OK.");
            if (recv) {
                LogIt.outNormalAsLog(mN + "Invoking for RECV since mode is '" + mode + "'...");
                algorithmObject.prepareToRecv();
            } else {
                LogIt.outNormalAsLog(mN + "Invoking for SEND since mode is '" + mode + "'...");
                algorithmObject.prepareToSend();
            }
            byte[] finalBytes = algorithmObject.getData();
            LogIt.outNormalAsLog(mN + "Final byte[] obtained - attempting to write output file...");
            writeOutputFile(outputFile, finalBytes);
            LogIt.outNormalAsLog(mN + "EXECUTION COMPLETE!");
            System.exit(0);
        } catch (Throwable t) {
            LogIt.errErrorAsLog(mN + "An exception occurred during execution!");
            LogIt.errExAsLog(t);
            LogIt.errErrorAsLog(mN + "EXECUTION FAILED!");
            System.exit(1);
        }
    }

    /**
     * Getter for isPreparedForSend flag
     *
     * @return boolean
     */
    public synchronized boolean isPreparedForSend() {
        return isPreparedForSend;
    }

    /**
     * Getter for isPreparedForRecv flag
     *
     * @return
     */
    public synchronized boolean isPreparedForRecv() {
        return isPreparedForRecv;
    }

    /**
     * Prepares the data for transmission, according to the
     * implementation of the Algorithm child-class instance.
     *
     * @throws Exception if a problem occurs
     */
    public synchronized void prepareToSend() throws Exception {
        final String mN = CLASSNAME + ".prepareToSend() -> ";
        if (isPreparedForRecv) {
            throw new Exception("Data is already prepped for receive; Cannot prep for send also!");
        }
        if (isPreparedForSend) {
            String stmt = mN + "Data is already prepared for transmission - Nothing to do.";
            if (isTestMode) {
                LogIt.outNormalAsLog(stmt);
            } else {
                LogIt.getInstance().logNormal(stmt);
            }
            return;
        }
        String stmt1 = mN + "Preparing data for transmission...";
        String stmt2 = mN + "Data has been prepared for transmission successfully.";
        if (isTestMode) {
            LogIt.outNormalAsLog(stmt1);
            prepareDataForTransmission();
            LogIt.outNormalAsLog(stmt2);
        } else {
            LogIt.getInstance().logNormal(stmt1);
            prepareDataForTransmission();
            LogIt.getInstance().logNormal(stmt2);
        }
        isPreparedForSend = true;
    }

    /**
     * Prepares the data for reception, according to the
     * implementation of the Algorithm child-class instance.
     *
     * @throws Exception if a problem occurs
     */
    public synchronized void prepareToRecv() throws Exception {
        final String mN = CLASSNAME + ".prepareToRecv() -> ";
        if (isPreparedForSend) {
            throw new Exception("Data is already prepped for send; Cannot prep for receive also!");
        }
        if (isPreparedForRecv) {
            String stmt = mN + "Data is already prepared for reception - Nothing to do.";
            if (isTestMode) {
                LogIt.outNormalAsLog(stmt);
            } else {
                LogIt.getInstance().logNormal(stmt);
            }
            return;
        }
        String stmt1 = mN + "Preparing data for reception...";
        String stmt2 = mN + "Data has been prepared for reception successfully.";
        if (isTestMode) {
            LogIt.outNormalAsLog(stmt1);
            prepareDataForReception();
            LogIt.outNormalAsLog(stmt2);
        } else {
            LogIt.getInstance().logNormal(stmt1);
            prepareDataForReception();
            LogIt.getInstance().logNormal(stmt2);
        }
        isPreparedForSend = true;
    }

    /**
     * ABSTRACT METHOD: CHILD CLASSES MUST IMPLEMENT!
     * This method is the entry-point to the child-class implementation
     * of the algorithm(s) needed to prepare data for transmission.
     *
     * @throws Exception if a problem occurs
     */
    protected abstract void prepareDataForTransmission() throws Exception;

    /**
     * ABSTRACT METHOD: CHILD CLASSES MUST IMPLEMENT!
     * This method is the entry-point to the child-class implementation
     * of the algorithm(s) needed to prepare data for reception.
     *
     * @throws Exception if a problem occurs
     */
    protected abstract void prepareDataForReception() throws Exception;

    /**
     * Getter for the data bytes being prepared for send/recv
     *
     * @return byte[]
     */
    public synchronized byte[] getData() {
        return data;
    }
}
