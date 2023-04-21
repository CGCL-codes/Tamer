package co.edu.unal.ungrid.transformation.dst;

import co.edu.unal.ungrid.transformation.dft.IoUtils;
import co.edu.unal.ungrid.util.ConcurrencyHelper;

/**
 * Benchmark of single precision DST's
 * 
 */
public class BenchmarkFloatDst {

    private BenchmarkFloatDst() {
    }

    public static void parseArguments(String[] args) {
        for (int i = 0; i < args.length; i++) {
            System.out.println("args[" + i + "]:" + args[i]);
        }
        if ((args == null) || (args.length != 10)) {
            System.out.println("Parameters: <number of threads> <THREADS_BEGIN_N_2D> <THREADS_BEGIN_N_3D> <number of iterations> <perform warm-up> <perform scaling> <number of sizes> <initial exponent for 1D transforms> <initial exponent for 2D transforms> <initial exponent for 3D transforms>");
            System.exit(-1);
        }
        nthread = Integer.parseInt(args[0]);
        ConcurrencyHelper.setThreadsMinSize2D(Integer.parseInt(args[1]));
        ConcurrencyHelper.setThreadsMinSize3D(Integer.parseInt(args[2]));
        niter = Integer.parseInt(args[3]);
        doWarmup = Boolean.parseBoolean(args[4]);
        doScaling = Boolean.parseBoolean(args[5]);
        nsize = Integer.parseInt(args[6]);
        initialExponent1D = Integer.parseInt(args[7]);
        initialExponent2D = Integer.parseInt(args[8]);
        initialExponent3D = Integer.parseInt(args[9]);
        ConcurrencyHelper.setNumberOfProcessors(nthread);
    }

    public static void benchmarkForward_1D(int init_exp) {
        int[] sizes = new int[nsize];
        double[] times = new double[nsize];
        float[] x;
        for (int i = 0; i < nsize; i++) {
            int exponent = init_exp + i;
            int N = (int) Math.pow(2, exponent);
            sizes[i] = N;
            System.out.println("Forward DST 1D of size 2^" + exponent);
            FloatDst1D dst = new FloatDst1D(N);
            x = new float[N];
            if (doWarmup) {
                IoUtils.fillMatrix_1D(N, x);
                dst.forward(x, false);
                IoUtils.fillMatrix_1D(N, x);
                dst.forward(x, false);
            }
            float av_time = 0;
            long elapsedTime = 0;
            for (int j = 0; j < niter; j++) {
                IoUtils.fillMatrix_1D(N, x);
                elapsedTime = System.nanoTime();
                dst.forward(x, false);
                elapsedTime = System.nanoTime() - elapsedTime;
                av_time = av_time + elapsedTime;
            }
            times[i] = (double) av_time / 1000000.0 / (double) niter;
            System.out.println("Average execution time: " + String.format("%.2f", av_time / 1000000.0 / (float) niter) + " msec");
            x = null;
            dst = null;
            System.gc();
        }
        IoUtils.writeFFTBenchmarkResultsToFile("benchmarkFloatForwardDST_1D.txt", nthread, niter, doWarmup, doScaling, sizes, times);
    }

    public static void benchmarkForward_2D_input_1D(int init_exp) {
        int[] sizes = new int[nsize];
        double[] times = new double[nsize];
        float[] x;
        for (int i = 0; i < nsize; i++) {
            int exponent = init_exp + i;
            int N = (int) Math.pow(2, exponent);
            sizes[i] = N;
            System.out.println("Forward DST 2D (input 1D) of size 2^" + exponent + " x 2^" + exponent);
            FloatDst2D dst2 = new FloatDst2D(N, N);
            x = new float[N * N];
            if (doWarmup) {
                IoUtils.fillMatrix_2D(N, N, x);
                dst2.forward(x, false);
                IoUtils.fillMatrix_2D(N, N, x);
                dst2.forward(x, false);
            }
            float av_time = 0;
            long elapsedTime = 0;
            for (int j = 0; j < niter; j++) {
                IoUtils.fillMatrix_2D(N, N, x);
                elapsedTime = System.nanoTime();
                dst2.forward(x, false);
                elapsedTime = System.nanoTime() - elapsedTime;
                av_time = av_time + elapsedTime;
            }
            times[i] = (double) av_time / 1000000.0 / (double) niter;
            System.out.println("Average execution time: " + String.format("%.2f", av_time / 1000000.0 / (float) niter) + " msec");
            x = null;
            dst2 = null;
            System.gc();
        }
        IoUtils.writeFFTBenchmarkResultsToFile("benchmarkFloatForwardDST_2D_input_1D.txt", nthread, niter, doWarmup, doScaling, sizes, times);
    }

    public static void benchmarkForward_2D_input_2D(int init_exp) {
        int[] sizes = new int[nsize];
        double[] times = new double[nsize];
        float[][] x;
        for (int i = 0; i < nsize; i++) {
            int exponent = init_exp + i;
            int N = (int) Math.pow(2, exponent);
            sizes[i] = N;
            System.out.println("Forward DST 2D (input 2D) of size 2^" + exponent + " x 2^" + exponent);
            FloatDst2D dst2 = new FloatDst2D(N, N);
            x = new float[N][N];
            if (doWarmup) {
                IoUtils.fillMatrix_2D(N, N, x);
                dst2.forward(x, false);
                IoUtils.fillMatrix_2D(N, N, x);
                dst2.forward(x, false);
            }
            float av_time = 0;
            long elapsedTime = 0;
            for (int j = 0; j < niter; j++) {
                IoUtils.fillMatrix_2D(N, N, x);
                elapsedTime = System.nanoTime();
                dst2.forward(x, false);
                elapsedTime = System.nanoTime() - elapsedTime;
                av_time = av_time + elapsedTime;
            }
            times[i] = (double) av_time / 1000000.0 / (double) niter;
            System.out.println("Average execution time: " + String.format("%.2f", av_time / 1000000.0 / (float) niter) + " msec");
            x = null;
            dst2 = null;
            System.gc();
        }
        IoUtils.writeFFTBenchmarkResultsToFile("benchmarkFloatForwardDST_2D_input_2D.txt", nthread, niter, doWarmup, doScaling, sizes, times);
    }

    public static void benchmarkForward_3D_input_1D(int init_exp) {
        int[] sizes = new int[nsize];
        double[] times = new double[nsize];
        float[] x;
        for (int i = 0; i < nsize; i++) {
            int exponent = init_exp + i;
            int N = (int) Math.pow(2, exponent);
            sizes[i] = N;
            System.out.println("Forward DST 3D (input 1D) of size 2^" + exponent + " x 2^" + exponent + " x 2^" + exponent);
            FloatDst3D dst3 = new FloatDst3D(N, N, N);
            x = new float[N * N * N];
            if (doWarmup) {
                IoUtils.fillMatrix_3D(N, N, N, x);
                dst3.forward(x, false);
                IoUtils.fillMatrix_3D(N, N, N, x);
                dst3.forward(x, false);
            }
            float av_time = 0;
            long elapsedTime = 0;
            for (int j = 0; j < niter; j++) {
                IoUtils.fillMatrix_3D(N, N, N, x);
                elapsedTime = System.nanoTime();
                dst3.forward(x, false);
                elapsedTime = System.nanoTime() - elapsedTime;
                av_time = av_time + elapsedTime;
            }
            times[i] = (double) av_time / 1000000.0 / (double) niter;
            System.out.println("Average execution time: " + String.format("%.2f", av_time / 1000000.0 / (float) niter) + " msec");
            x = null;
            dst3 = null;
            System.gc();
        }
        IoUtils.writeFFTBenchmarkResultsToFile("benchmarkFloatForwardDST_3D_input_1D.txt", nthread, niter, doWarmup, doScaling, sizes, times);
    }

    public static void benchmarkForward_3D_input_3D(int init_exp) {
        int[] sizes = new int[nsize];
        double[] times = new double[nsize];
        float[][][] x;
        for (int i = 0; i < nsize; i++) {
            int exponent = init_exp + i;
            int N = (int) Math.pow(2, exponent);
            sizes[i] = N;
            System.out.println("Forward DST 3D (input 3D) of size 2^" + exponent + " x 2^" + exponent + " x 2^" + exponent);
            FloatDst3D dst3 = new FloatDst3D(N, N, N);
            x = new float[N][N][N];
            if (doWarmup) {
                IoUtils.fillMatrix_3D(N, N, N, x);
                dst3.forward(x, false);
                IoUtils.fillMatrix_3D(N, N, N, x);
                dst3.forward(x, false);
            }
            float av_time = 0;
            long elapsedTime = 0;
            for (int j = 0; j < niter; j++) {
                IoUtils.fillMatrix_3D(N, N, N, x);
                elapsedTime = System.nanoTime();
                dst3.forward(x, false);
                elapsedTime = System.nanoTime() - elapsedTime;
                av_time = av_time + elapsedTime;
            }
            times[i] = (double) av_time / 1000000.0 / (double) niter;
            System.out.println("Average execution time: " + String.format("%.2f", av_time / 1000000.0 / (float) niter) + " msec");
            x = null;
            dst3 = null;
            System.gc();
        }
        IoUtils.writeFFTBenchmarkResultsToFile("benchmarkFloatForwardDST_3D_input_3D.txt", nthread, niter, doWarmup, doScaling, sizes, times);
    }

    public static void main(String[] args) {
        parseArguments(args);
        benchmarkForward_1D(initialExponent1D);
        benchmarkForward_2D_input_1D(initialExponent2D);
        benchmarkForward_2D_input_2D(initialExponent2D);
        benchmarkForward_3D_input_1D(initialExponent3D);
        benchmarkForward_3D_input_3D(initialExponent3D);
    }

    private static int nthread = 2;

    private static int nsize = 6;

    private static int niter = 200;

    private static int initialExponent1D = 17;

    private static int initialExponent2D = 7;

    private static int initialExponent3D = 2;

    private static boolean doWarmup = true;

    private static boolean doScaling = false;
}
