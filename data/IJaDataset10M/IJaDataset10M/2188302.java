package edu.emory.mathcs.restoretools.hrrt;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import edu.emory.mathcs.utils.ConcurrencyUtils;

/**
 * Benchmark of Parallel HRRT Deconvolution.
 * 
 * @author Piotr Wendykier (piotr.wendykier@gmail.com)
 * 
 */
public class Benchmark {

    private static final String path = "/home/pwendyk/HRRT/data/1081_1081/";

    private static final String pathToSegmentationInformation = path + "segmentation.csv";

    private static final String maxIters = "15";

    private static final String interpolation = "NEAREST_NEIGHBOR";

    private static final String pathToDeblurredImage = path + "deblurredData.tif";

    private static final String pathToBlurredImage = path + "blurredData.tif";

    private static final String pathToCalibrationMatrix = path + "calibrationMatrix.txt";

    private static final String pathToMotionInformation = path + "motions.csv";

    private static final String segmentationDataSeries = "AVG";

    private static final String quaternionsWeight = "50";

    private static final String minSegmentSize = "-1";

    private static final String samplingRate = "20";

    private static final String scanDuration = "28";

    private static final String initialTimeOffset = "0";

    private static final String output = "SAME_AS_SOURCE";

    private static final String stoppingTol = "0";

    private static final String threshold = "-1";

    private static final String logConvergence = "false";

    private static final String showIters = "false";

    private static final String innerSolver = "TIKHONOV";

    private static final String regMethod = "ADAPTWGCV";

    private static final String regParameter = "0";

    private static final String omega = "0";

    private static final String reorthogonalize = "false";

    private static final String beginReg = "2";

    private static final String flatTolerance = "0";

    private static final String nsubsets = "2";

    private static final int NITER = 5;

    private static final String format = "%.2f";

    public static void benchmarkDoubleCGLS(int threads) {
        ConcurrencyUtils.setNumberOfThreads(threads);
        System.out.println("Benchmarking DoubleCGLS using " + threads + " threads");
        double av_time_deblur = 0;
        long elapsedTime_deblur = 0;
        for (int i = 0; i < NITER; i++) {
            elapsedTime_deblur = System.nanoTime();
            ParallelHRRTDeconvolution.deconvolveCGLS(pathToDeblurredImage, pathToBlurredImage, pathToCalibrationMatrix, pathToMotionInformation, pathToSegmentationInformation, segmentationDataSeries, quaternionsWeight, minSegmentSize, samplingRate, scanDuration, initialTimeOffset, interpolation, output, "DOUBLE", stoppingTol, threshold, maxIters, logConvergence, new Integer(threads).toString(), showIters);
            elapsedTime_deblur = System.nanoTime() - elapsedTime_deblur;
            av_time_deblur = av_time_deblur + elapsedTime_deblur;
            System.gc();
        }
        System.out.println("Average execution time: " + String.format(format, av_time_deblur / 1000000000.0 / NITER) + " sec");
        writeResultsToFile("DoubleCGLS_" + threads + "_threads.txt", av_time_deblur / 1000000000.0 / NITER);
    }

    public static void benchmarkDoubleHyBR(int threads) {
        ConcurrencyUtils.setNumberOfThreads(threads);
        System.out.println("Benchmarking DoubleHyBR using " + threads + " threads");
        double av_time_deblur = 0;
        long elapsedTime_deblur = 0;
        for (int i = 0; i < NITER; i++) {
            elapsedTime_deblur = System.nanoTime();
            ParallelHRRTDeconvolution.deconvolveHyBR(pathToDeblurredImage, pathToBlurredImage, pathToCalibrationMatrix, pathToMotionInformation, pathToSegmentationInformation, segmentationDataSeries, quaternionsWeight, minSegmentSize, samplingRate, scanDuration, initialTimeOffset, interpolation, output, "DOUBLE", threshold, maxIters, logConvergence, new Integer(threads).toString(), showIters, innerSolver, regMethod, regParameter, omega, reorthogonalize, beginReg, flatTolerance);
            elapsedTime_deblur = System.nanoTime() - elapsedTime_deblur;
            av_time_deblur = av_time_deblur + elapsedTime_deblur;
            System.gc();
        }
        System.out.println("Average execution time: " + String.format(format, av_time_deblur / 1000000000.0 / NITER) + " sec");
        writeResultsToFile("DoubleHyBR_" + threads + "_threads.txt", av_time_deblur / 1000000000.0 / NITER);
    }

    public static void benchmarkDoubleMRNSD(int threads) {
        ConcurrencyUtils.setNumberOfThreads(threads);
        System.out.println("Benchmarking DoubleMRNSD using " + threads + " threads");
        double av_time_deblur = 0;
        long elapsedTime_deblur = 0;
        for (int i = 0; i < NITER; i++) {
            elapsedTime_deblur = System.nanoTime();
            ParallelHRRTDeconvolution.deconvolveMRNSD(pathToDeblurredImage, pathToBlurredImage, pathToCalibrationMatrix, pathToMotionInformation, pathToSegmentationInformation, segmentationDataSeries, quaternionsWeight, minSegmentSize, samplingRate, scanDuration, initialTimeOffset, interpolation, output, "DOUBLE", stoppingTol, threshold, maxIters, logConvergence, new Integer(threads).toString(), showIters);
            elapsedTime_deblur = System.nanoTime() - elapsedTime_deblur;
            av_time_deblur = av_time_deblur + elapsedTime_deblur;
            System.gc();
        }
        System.out.println("Average execution time: " + String.format(format, av_time_deblur / 1000000000.0 / NITER) + " sec");
        writeResultsToFile("DoubleMRNSD_" + threads + "_threads.txt", av_time_deblur / 1000000000.0 / NITER);
    }

    public static void benchmarkDoubleOSEM(int threads) {
        ConcurrencyUtils.setNumberOfThreads(threads);
        System.out.println("Benchmarking DoubleOSEM using " + threads + " threads");
        double av_time_deblur = 0;
        long elapsedTime_deblur = 0;
        for (int i = 0; i < NITER; i++) {
            elapsedTime_deblur = System.nanoTime();
            ParallelHRRTDeconvolution.deconvolveOSEM(pathToDeblurredImage, pathToBlurredImage, pathToCalibrationMatrix, pathToMotionInformation, pathToSegmentationInformation, segmentationDataSeries, quaternionsWeight, minSegmentSize, samplingRate, scanDuration, initialTimeOffset, interpolation, output, "DOUBLE", nsubsets, stoppingTol, threshold, maxIters, logConvergence, new Integer(threads).toString(), showIters);
            elapsedTime_deblur = System.nanoTime() - elapsedTime_deblur;
            av_time_deblur = av_time_deblur + elapsedTime_deblur;
            System.gc();
        }
        System.out.println("Average execution time: " + String.format(format, av_time_deblur / 1000000000.0 / NITER) + " sec");
        writeResultsToFile("DoubleOSEM_" + threads + "_threads.txt", av_time_deblur / 1000000000.0 / NITER);
    }

    public static void benchmarkFloatCGLS(int threads) {
        ConcurrencyUtils.setNumberOfThreads(threads);
        System.out.println("Benchmarking FloatCGLS using " + threads + " threads");
        double av_time_deblur = 0;
        long elapsedTime_deblur = 0;
        for (int i = 0; i < NITER; i++) {
            elapsedTime_deblur = System.nanoTime();
            ParallelHRRTDeconvolution.deconvolveCGLS(pathToDeblurredImage, pathToBlurredImage, pathToCalibrationMatrix, pathToMotionInformation, pathToSegmentationInformation, segmentationDataSeries, quaternionsWeight, minSegmentSize, samplingRate, scanDuration, initialTimeOffset, interpolation, output, "SINGLE", stoppingTol, threshold, maxIters, logConvergence, new Integer(threads).toString(), showIters);
            elapsedTime_deblur = System.nanoTime() - elapsedTime_deblur;
            av_time_deblur = av_time_deblur + elapsedTime_deblur;
            System.gc();
        }
        System.out.println("Average execution time: " + String.format(format, av_time_deblur / 1000000000.0 / NITER) + " sec");
        writeResultsToFile("FloatCGLS_" + threads + "_threads.txt", av_time_deblur / 1000000000.0 / NITER);
    }

    public static void benchmarkFloatHyBR(int threads) {
        ConcurrencyUtils.setNumberOfThreads(threads);
        System.out.println("Benchmarking FloatHyBR using " + threads + " threads");
        double av_time_deblur = 0;
        long elapsedTime_deblur = 0;
        for (int i = 0; i < NITER; i++) {
            elapsedTime_deblur = System.nanoTime();
            ParallelHRRTDeconvolution.deconvolveHyBR(pathToDeblurredImage, pathToBlurredImage, pathToCalibrationMatrix, pathToMotionInformation, pathToSegmentationInformation, segmentationDataSeries, quaternionsWeight, minSegmentSize, samplingRate, scanDuration, initialTimeOffset, interpolation, output, "SINGLE", threshold, maxIters, logConvergence, new Integer(threads).toString(), showIters, innerSolver, regMethod, regParameter, omega, reorthogonalize, beginReg, flatTolerance);
            elapsedTime_deblur = System.nanoTime() - elapsedTime_deblur;
            av_time_deblur = av_time_deblur + elapsedTime_deblur;
            System.gc();
        }
        System.out.println("Average execution time: " + String.format(format, av_time_deblur / 1000000000.0 / NITER) + " sec");
        writeResultsToFile("FloatHyBR_" + threads + "_threads.txt", av_time_deblur / 1000000000.0 / NITER);
    }

    public static void benchmarkFloatMRNSD(int threads) {
        ConcurrencyUtils.setNumberOfThreads(threads);
        System.out.println("Benchmarking FloatMRNSD using " + threads + " threads");
        double av_time_deblur = 0;
        long elapsedTime_deblur = 0;
        for (int i = 0; i < NITER; i++) {
            elapsedTime_deblur = System.nanoTime();
            ParallelHRRTDeconvolution.deconvolveMRNSD(pathToDeblurredImage, pathToBlurredImage, pathToCalibrationMatrix, pathToMotionInformation, pathToSegmentationInformation, segmentationDataSeries, quaternionsWeight, minSegmentSize, samplingRate, scanDuration, initialTimeOffset, interpolation, output, "SINGLE", stoppingTol, threshold, maxIters, logConvergence, new Integer(threads).toString(), showIters);
            elapsedTime_deblur = System.nanoTime() - elapsedTime_deblur;
            av_time_deblur = av_time_deblur + elapsedTime_deblur;
            System.gc();
        }
        System.out.println("Average execution time: " + String.format(format, av_time_deblur / 1000000000.0 / NITER) + " sec");
        writeResultsToFile("FloatMRNSD_" + threads + "_threads.txt", av_time_deblur / 1000000000.0 / NITER);
    }

    public static void benchmarkFloatOSEM(int threads) {
        ConcurrencyUtils.setNumberOfThreads(threads);
        System.out.println("Benchmarking FloatOSEM using " + threads + " threads");
        double av_time_deblur = 0;
        long elapsedTime_deblur = 0;
        for (int i = 0; i < NITER; i++) {
            elapsedTime_deblur = System.nanoTime();
            ParallelHRRTDeconvolution.deconvolveOSEM(pathToDeblurredImage, pathToBlurredImage, pathToCalibrationMatrix, pathToMotionInformation, pathToSegmentationInformation, segmentationDataSeries, quaternionsWeight, minSegmentSize, samplingRate, scanDuration, initialTimeOffset, interpolation, output, "SINGLE", nsubsets, stoppingTol, threshold, maxIters, logConvergence, new Integer(threads).toString(), showIters);
            elapsedTime_deblur = System.nanoTime() - elapsedTime_deblur;
            av_time_deblur = av_time_deblur + elapsedTime_deblur;
            System.gc();
        }
        System.out.println("Average execution time: " + String.format(format, av_time_deblur / 1000000000.0 / NITER) + " sec");
        writeResultsToFile("FloatOSEM_" + threads + "_threads.txt", av_time_deblur / 1000000000.0 / NITER);
    }

    public static void main(String[] args) {
        benchmarkFloatOSEM(1);
        benchmarkFloatOSEM(2);
        benchmarkFloatOSEM(4);
        benchmarkFloatOSEM(8);
        benchmarkFloatMRNSD(1);
        benchmarkFloatMRNSD(2);
        benchmarkFloatMRNSD(4);
        benchmarkFloatMRNSD(8);
        benchmarkFloatHyBR(1);
        benchmarkFloatHyBR(2);
        benchmarkFloatHyBR(4);
        benchmarkFloatHyBR(8);
    }

    public static void writeResultsToFile(String filename, double time_deblur) {
        String[] properties = { "os.name", "os.version", "os.arch", "java.vendor", "java.version" };
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(filename));
            out.write(new Date().toString());
            out.newLine();
            out.write("System properties:");
            out.newLine();
            out.write("\tos.name = " + System.getProperty(properties[0]));
            out.newLine();
            out.write("\tos.version = " + System.getProperty(properties[1]));
            out.newLine();
            out.write("\tos.arch = " + System.getProperty(properties[2]));
            out.newLine();
            out.write("\tjava.vendor = " + System.getProperty(properties[3]));
            out.newLine();
            out.write("\tjava.version = " + System.getProperty(properties[4]));
            out.newLine();
            out.write("\tavailable processors = " + Runtime.getRuntime().availableProcessors());
            out.newLine();
            out.write("average deblur time: ");
            out.write(String.format(format, time_deblur));
            out.write(" seconds");
            out.newLine();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
