package org.immernurrosinen.unimulch;

import edu.rit.pj.*;

public class ProcessTags {

    public static void main(String[] args) throws Exception {
        long time = -System.currentTimeMillis();
        final int iters = Integer.parseInt(args[0]);
        new ParallelTeam().execute(new ParallelRegion() {

            public void run() throws Exception {
                execute(1, 4, new IntegerForLoop() {

                    public void run(int first, int last) {
                        for (int i = first; i <= last; ++i) {
                            int k = 0;
                            for (int j = 0; j < iters; ++j) k = k + 1;
                        }
                    }
                });
            }
        });
        time += System.currentTimeMillis();
        System.out.println(time + " msec");
    }
}
