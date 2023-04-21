package com.google.ortools.knapsacksolver.samples;

import com.google.ortools.knapsacksolver.KnapsackSolver;

/**
 * Sample showing how to model using the knapsack solver.
 *
 */
public class Knapsack {

    static {
        System.loadLibrary("jniknapsacksolver");
    }

    private static void solve() {
        KnapsackSolver solver = new KnapsackSolver(KnapsackSolver.KNAPSACK_MULTIDIMENSION_BRANCH_AND_BOUND_SOLVER, "test");
        final long[] profits = { 360, 83, 59, 130, 431, 67, 230, 52, 93, 125, 670, 892, 600, 38, 48, 147, 78, 256, 63, 17, 120, 164, 432, 35, 92, 110, 22, 42, 50, 323, 514, 28, 87, 73, 78, 15, 26, 78, 210, 36, 85, 189, 274, 43, 33, 10, 19, 389, 276, 312 };
        final long[][] weights = { { 7, 0, 30, 22, 80, 94, 11, 81, 70, 64, 59, 18, 0, 36, 3, 8, 15, 42, 9, 0, 42, 47, 52, 32, 26, 48, 55, 6, 29, 84, 2, 4, 18, 56, 7, 29, 93, 44, 71, 3, 86, 66, 31, 65, 0, 79, 20, 65, 52, 13 } };
        final long[] capacities = { 850 };
        final long optimalProfit = 7534;
        solver.init(profits, weights, capacities);
        final long computedProfit = solver.solve();
        System.out.println("Optimal_Profit = " + computedProfit + "/" + optimalProfit);
    }

    public static void main(String[] args) throws Exception {
        Knapsack.solve();
    }
}
