package org.node.perf.test.andy;

import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;
import org.node.perf.test.Test1BaselineHello.Test2Times;
import org.node.perf.test.util.SimpleDoTimes;
import org.node.perf.test.util.SimpleTime;
import org.node.perf.test.util.Times;
import org.node.perf.util.SystemUtils;

public final class mandelbrot1 {

    public static final class Test1Times implements Times {

        final int size;

        final boolean printFormatPbm = true;

        public Test1Times(int s) {
            this.size = s;
        }

        public final void execute() {
            int width_bytes = size / 8 + 1;
            byte[][] output_data = new byte[size][width_bytes];
            int[] bytes_per_line = new int[size];
            Compute(size, output_data, bytes_per_line);
            if (printFormatPbm) {
                BufferedOutputStream ostream = new BufferedOutputStream(System.out);
                for (int i = 0; i < size; i++) try {
                    ostream.write(output_data[i], 0, bytes_per_line[i]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    ostream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < bytes_per_line[i]; j++) {
                        System.out.format(" %02x", output_data[i][j]);
                    }
                    System.out.format("\n");
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        int size = 200;
        boolean printFormatPbm = true;
        if (args.length >= 1) size = Integer.parseInt(args[0]);
        if (args.length >= 2) printFormatPbm = false;
        System.out.format("P4\n%d %d\n", size, size);
        new SimpleTime(new SimpleDoTimes(new Test1Times(size), SystemUtils.once)).execute();
    }

    private static final void Compute(final int N, final byte[][] output, final int[] bytes_per_line) {
        final double inverse_N = 2.0 / N;
        final AtomicInteger current_line = new AtomicInteger(0);
        final Thread[] pool = new Thread[Runtime.getRuntime().availableProcessors()];
        for (int i = 0; i < pool.length; i++) {
            pool[i] = new Thread() {

                public void run() {
                    int y;
                    while ((y = current_line.getAndIncrement()) < N) {
                        byte[] pdata = output[y];
                        int bit_num = 0;
                        int byte_count = 0;
                        int byte_accumulate = 0;
                        double Civ = (double) y * inverse_N - 1.0;
                        for (int x = 0; x < N; x++) {
                            double Crv = (double) x * inverse_N - 1.5;
                            double Zrv = Crv;
                            double Ziv = Civ;
                            double Trv = Crv * Crv;
                            double Tiv = Civ * Civ;
                            int i = 49;
                            do {
                                Ziv = (Zrv * Ziv) + (Zrv * Ziv) + Civ;
                                Zrv = Trv - Tiv + Crv;
                                Trv = Zrv * Zrv;
                                Tiv = Ziv * Ziv;
                            } while (((Trv + Tiv) <= 4.0) && (--i > 0));
                            byte_accumulate <<= 1;
                            if (i == 0) byte_accumulate++;
                            if (++bit_num == 8) {
                                pdata[byte_count++] = (byte) byte_accumulate;
                                bit_num = byte_accumulate = 0;
                            }
                        }
                        if (bit_num != 0) {
                            byte_accumulate <<= (8 - (N & 7));
                            pdata[byte_count++] = (byte) byte_accumulate;
                        }
                        bytes_per_line[y] = byte_count;
                    }
                }
            };
            pool[i].start();
        }
        for (Thread t : pool) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
