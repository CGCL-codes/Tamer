package org.jbench.bytemark;

final class NeuralNetTest extends BmarkTest {

    private int IN_SIZE = 35;

    private int MID_SIZE = 8;

    private int OUT_SIZE = 8;

    private int MAXPATS = 5;

    private double BETA = 0.09;

    private double ALPHA = 0.09;

    private double STOP = 0.1;

    private double[][] mid_wts = new double[MID_SIZE][IN_SIZE];

    private double[][] out_wts = new double[OUT_SIZE][MID_SIZE];

    private double[] mid_out = new double[MID_SIZE];

    private double[] out_out = new double[OUT_SIZE];

    private double[] mid_error = new double[MID_SIZE];

    private double[] out_error = new double[OUT_SIZE];

    private double[][] mid_wt_change = new double[MID_SIZE][IN_SIZE];

    private double[][] out_wt_change = new double[OUT_SIZE][MID_SIZE];

    private double[] tot_out_error = new double[MAXPATS];

    private double[][] mid_wt_cum_change = new double[MID_SIZE][IN_SIZE];

    private double[][] out_wt_cum_change = new double[OUT_SIZE][MID_SIZE];

    private double[] avg_out_error = new double[MAXPATS];

    private double worst_error;

    private double average_error;

    private int iteration_count;

    private int numpats;

    private int numpasses;

    private boolean learned;

    private double[][] in_pats = { { 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1 }, { 1, 1, 1, 1, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 0 }, { 0, 1, 1, 1, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 1, 1, 0 }, { 1, 1, 1, 1, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 0 }, { 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1 }, { 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0 }, { 0, 1, 1, 1, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 1, 1, 0, 0, 0, 1, 0, 1, 1, 1, 0 }, { 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1 }, { 0, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0 }, { 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 1, 1, 1, 0 }, { 1, 0, 0, 0, 1, 1, 0, 0, 1, 0, 1, 0, 1, 0, 0, 1, 1, 0, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 1 }, { 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1 }, { 1, 0, 0, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1 }, { 1, 0, 0, 0, 1, 1, 1, 0, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 0, 1, 1, 1, 0, 0, 0, 1 }, { 0, 1, 1, 1, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 1, 1, 1, 0 }, { 1, 1, 1, 1, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0 }, { 0, 1, 1, 1, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 1, 0, 1, 1, 0, 0, 1, 1, 0, 1, 1, 1, 1 }, { 1, 1, 1, 1, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 1 }, { 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0 }, { 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0 }, { 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 1, 1, 1, 0 }, { 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0 }, { 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0 }, { 1, 0, 0, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1 }, { 1, 0, 0, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0 }, { 1, 1, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 1, 1 } };

    private double[][] out_pats = { { 0, 1, 0, 0, 0, 0, 0, 1 }, { 0, 1, 0, 0, 0, 0, 1, 0 }, { 0, 1, 0, 0, 0, 0, 1, 1 }, { 0, 1, 0, 0, 0, 1, 0, 0 }, { 0, 1, 0, 0, 0, 1, 0, 1 }, { 0, 1, 0, 0, 0, 1, 1, 0 }, { 0, 1, 0, 0, 0, 1, 1, 1 }, { 0, 1, 0, 0, 1, 0, 0, 0 }, { 0, 1, 0, 0, 1, 0, 0, 1 }, { 0, 1, 0, 0, 1, 0, 1, 0 }, { 0, 1, 0, 0, 1, 0, 1, 1 }, { 0, 1, 0, 0, 1, 1, 0, 0 }, { 0, 1, 0, 0, 1, 1, 0, 1 }, { 0, 1, 0, 0, 1, 1, 1, 0 }, { 0, 1, 0, 0, 1, 1, 1, 1 }, { 0, 1, 0, 1, 0, 0, 0, 0 }, { 0, 1, 0, 1, 0, 0, 0, 1 }, { 0, 1, 0, 1, 0, 0, 1, 0 }, { 0, 1, 0, 1, 0, 0, 1, 1 }, { 0, 1, 0, 1, 0, 1, 0, 0 }, { 0, 1, 0, 1, 0, 1, 0, 1 }, { 0, 1, 0, 1, 0, 1, 1, 0 }, { 0, 1, 0, 1, 0, 1, 1, 1 }, { 0, 1, 0, 1, 1, 0, 0, 0 }, { 0, 1, 0, 1, 1, 0, 0, 1 }, { 0, 1, 0, 1, 1, 0, 1, 0 } };

    /**
* constructor
*
* This routine sets the adjustment flag to false (indicating
* that the initialize method hasn't yet adjusted the number of
* bitfield operations to the clock accuracy, and initializes
* the test name (for error context reasons).
* It also loads up important instance variables with their
* "starting" values. Variables in this method are all instance
* variables declared in the parent class.
*/
    NeuralNetTest() {
        adjust_flag = BMglobals.nnetadjust;
        testname = "FPU: Neural Net";
        base_iters_per_sec = BMglobals.nnettestbase;
        loops_per_iter = BMglobals.nnetloops;
        numpats = MAXPATS;
        for (int patt = 0; patt < numpats; patt++) {
            for (int i = 0; i < IN_SIZE; i++) {
                if (in_pats[patt][i] >= 0.9) in_pats[patt][i] = 0.9;
                if (in_pats[patt][i] <= 0.1) in_pats[patt][i] = 0.1;
            }
        }
    }

    protected void initialize() {
        long duration;
        while (true) {
            duration = DoIteration();
            if (duration > BMglobals.minTicks / 10) break;
            loops_per_iter += 1;
        }
        int factor = (int) (BMglobals.minTicks / duration);
        loops_per_iter += (factor * loops_per_iter);
        adjust_flag = true;
    }

    /**
* dotest
*
*
* Perform the actual benchmark test.
* The steps involved are:
*  1. See if the test has already been "adjusted".
*     If it hasn't do an adjustment phase (initialize()).
*  2. If the test has been adjusted, go ahead and
*      run it.
*/
    void dotest() {
        long duration;
        buildTestData();
        if (adjust_flag == false) initialize();
        duration = DoIteration();
        iters_per_sec = (double) loops_per_iter / ((double) duration / (double) 1000);
        if (debug_on == true) {
            System.out.println("--- Neural Net debug data ---");
            System.out.println("Number loops per iteration: " + loops_per_iter);
            System.out.println("Elapsed time: " + duration);
            System.out.println("Neural net cycles per sec: " + iters_per_sec);
        }
        cleanup();
    }

    private long DoIteration() {
        long testTime;
        testTime = System.currentTimeMillis();
        for (int i = 0; i < loops_per_iter; i++) {
            randomize_wts();
            zero_changes();
            iteration_count = 1;
            learned = false;
            numpasses = 0;
            while (!learned) {
                for (int patt = 0; patt < numpats; patt++) {
                    worst_error = 0.0;
                    move_wt_changes();
                    do_forward_pass(patt);
                    do_back_pass(patt);
                    iteration_count++;
                }
                numpasses++;
                learned = check_out_error();
            }
        }
        testTime = System.currentTimeMillis() - testTime;
        return (testTime);
    }

    private void buildTestData() {
    }

    private void do_forward_pass(int patt) {
        do_mid_forward(patt);
        do_out_forward();
    }

    private void do_mid_forward(int patt) {
        double sum;
        for (int neurode = 0; neurode < MID_SIZE; neurode++) {
            sum = 0.0;
            for (int i = 0; i < IN_SIZE; i++) {
                sum += mid_wts[neurode][i] * in_pats[patt][i];
            }
            sum = 1.0 / (1.0 + Math.exp(-sum));
            mid_out[neurode] = sum;
        }
    }

    private void do_out_forward() {
        double sum;
        for (int neurode = 0; neurode < OUT_SIZE; neurode++) {
            sum = 0.0;
            for (int i = 0; i < MID_SIZE; i++) {
                sum += out_wts[neurode][i] * mid_out[i];
            }
            sum = 1.0 / (1.0 + Math.exp(-sum));
            out_out[neurode] = sum;
        }
    }

    private void do_back_pass(int patt) {
        do_out_error(patt);
        do_mid_error();
        adjust_out_wts();
        adjust_mid_wts(patt);
    }

    private void do_out_error(int patt) {
        double error, tot_error, sum;
        tot_error = 0.0;
        sum = 0.0;
        for (int neurode = 0; neurode < OUT_SIZE; neurode++) {
            out_error[neurode] = out_pats[patt][neurode] - out_out[neurode];
            error = out_error[neurode];
            if (error < 0.0) {
                sum += -error;
                if (-error > tot_error) tot_error = -error;
            } else {
                sum += error;
                if (error > tot_error) tot_error = error;
            }
        }
        avg_out_error[patt] = sum / OUT_SIZE;
        tot_out_error[patt] = tot_error;
    }

    private void do_mid_error() {
        double sum;
        for (int neurode = 0; neurode < MID_SIZE; neurode++) {
            sum = 0.0;
            for (int i = 0; i < OUT_SIZE; i++) sum += out_wts[i][neurode] * out_error[i];
            mid_error[neurode] = mid_out[neurode] * (1 - mid_out[neurode]) * sum;
        }
    }

    private void adjust_out_wts() {
        double learn, delta, alph;
        learn = BETA;
        alph = ALPHA;
        for (int neurode = 0; neurode < OUT_SIZE; neurode++) {
            for (int weight = 0; weight < MID_SIZE; weight++) {
                delta = learn * out_error[neurode] * mid_out[weight];
                delta += alph * out_wt_change[neurode][weight];
                out_wts[neurode][weight] += delta;
                out_wt_cum_change[neurode][weight] += delta;
            }
        }
    }

    private void adjust_mid_wts(int patt) {
        double learn, alph, delta;
        learn = BETA;
        alph = ALPHA;
        for (int neurode = 0; neurode < MID_SIZE; neurode++) {
            for (int weight = 0; weight < IN_SIZE; weight++) {
                delta = learn * mid_error[neurode] * in_pats[patt][weight];
                delta += alph * mid_wt_change[neurode][weight];
                mid_wts[neurode][weight] += delta;
                mid_wt_cum_change[neurode][weight] += delta;
            }
        }
    }

    private void move_wt_changes() {
        for (int i = 0; i < MID_SIZE; i++) {
            for (int j = 0; j < IN_SIZE; j++) {
                mid_wt_change[i][j] = mid_wt_cum_change[i][j];
                mid_wt_cum_change[i][j] = 0.0;
            }
        }
        for (int i = 0; i < OUT_SIZE; i++) {
            for (int j = 0; j < MID_SIZE; j++) {
                out_wt_change[i][j] = out_wt_cum_change[i][j];
                out_wt_cum_change[i][j] = 0.0;
            }
        }
    }

    private boolean check_out_error() {
        boolean result = true;
        boolean error = false;
        worst_pass_error();
        if (worst_error >= STOP) result = false;
        for (int i = 0; i < numpats; i++) {
            if (tot_out_error[i] >= 16.0) {
                error = true;
                System.out.println("Learning error: total error >= 16.");
            }
        }
        return (result);
    }

    private void worst_pass_error() {
        double error, sum;
        error = 0.0;
        sum = 0.0;
        for (int i = 0; i < numpats; i++) {
            if (tot_out_error[i] > error) error = tot_out_error[i];
            sum += avg_out_error[i];
        }
        worst_error = error;
        average_error = sum / numpats;
    }

    private void zero_changes() {
        for (int i = 0; i < MID_SIZE; i++) {
            for (int j = 0; j < IN_SIZE; j++) {
                mid_wt_change[i][j] = 0.0;
                mid_wt_cum_change[i][j] = 0.0;
            }
        }
        for (int i = 0; i < OUT_SIZE; i++) {
            for (int j = 0; j < MID_SIZE; j++) {
                out_wt_change[i][j] = 0.0;
                out_wt_cum_change[i][j] = 0.0;
            }
        }
    }

    private void randomize_wts() {
        double value;
        RandNum rndnum = new RandNum();
        for (int neurode = 0; neurode < MID_SIZE; neurode++) {
            for (int i = 0; i < IN_SIZE; i++) {
                value = (double) rndnum.abs_nextwc(100000);
                value = value / 100000.0 - 0.5;
                mid_wts[neurode][i] = value / 2.0;
            }
        }
        for (int neurode = 0; neurode < OUT_SIZE; neurode++) {
            for (int i = 0; i < MID_SIZE; i++) {
                value = (double) rndnum.abs_nextwc(100000);
                value = value / 100000.0 - 0.5;
                out_wts[neurode][i] = value / 2.0;
            }
        }
    }

    private void freeTestData() {
        System.gc();
    }

    protected void cleanup() {
        freeTestData();
    }
}
