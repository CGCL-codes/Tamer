package org.phramer.mert.strategy.implementations;

import info.olteanu.utils.*;
import java.io.*;
import org.phramer.mert.evaluation.*;
import org.phramer.mert.intersection.*;
import org.phramer.mert.intersection.initialize.*;
import org.phramer.mert.item.*;
import org.phramer.mert.strategy.*;
import org.phramer.*;

public class SearchLambdaStrategyOrthogonal implements SearchLambdaStrategy {

    public String __DBG_prefix = "(1";

    public PrintStream d = System.err;

    public int DEBUG_LEVEL = 1;

    private final int steps;

    private final double[][] intervalsRandom;

    private final double minDeltaScore;

    public SearchLambdaStrategyOrthogonal(int steps, double[][] intervalsRandom, double[][] intervalsLambda, double minDeltaScore) {
        this.steps = steps;
        this.intervalsRandom = intervalsRandom;
        this.minDeltaScore = minDeltaScore;
    }

    private int bestLambdaIndex = -1;

    public double[] getNewLambdas(final int iteration, final Evaluator e, final ReferenceWithHypotheses[] f, final double[] initialLambda, final double[] bestPrevLambda, int maxLambdaAlter) throws PhramerException {
        if (__DBG_prefix.startsWith("(")) __DBG_prefix = "(" + iteration;
        if (maxLambdaAlter > initialLambda.length) maxLambdaAlter = initialLambda.length;
        double[] bestLambdas = null;
        double bestScore = Double.NEGATIVE_INFINITY;
        int bestLambdaIndex = -1;
        for (int i = 0; i < steps; i++) {
            double[] lambda;
            if (i == 0) lambda = initialLambda.clone(); else if (i == 1 && bestPrevLambda != null) lambda = bestPrevLambda.clone(); else {
                lambda = new double[initialLambda.length];
                for (int k = 0; k < maxLambdaAlter; k++) lambda[k] = intervalsRandom[k][0] + (intervalsRandom[k][1] - intervalsRandom[k][0]) * Math.random();
                for (int k = maxLambdaAlter; k < initialLambda.length; k++) lambda[k] = initialLambda[k];
            }
            double currentScore = executeStep(lambda, i + 1, e, f, initialLambda.length, maxLambdaAlter);
            if (currentScore > bestScore) {
                bestScore = currentScore;
                bestLambdas = lambda;
                bestLambdaIndex = i;
            }
        }
        if (DEBUG_LEVEL >= 1) {
            d.println(__DBG_prefix + ") End lambdas: ");
            __DBG_printLambda(bestLambdas, -1, d);
            d.println(__DBG_prefix + ") Score: " + bestScore);
        }
        this.bestLambdaIndex = bestLambdaIndex;
        return bestLambdas;
    }

    public double executeStep(double[] lambda, int iterNo, Evaluator e, ReferenceWithHypotheses[] f, int n, int maxLambdaAlter) throws PhramerException {
        Line[][] lines = InitializeLines.getLinesWithoutM(f, lambda);
        double currentScore = ItemTools.evaluate(lines, e);
        if (DEBUG_LEVEL >= 1) {
            d.print(__DBG_prefix + "." + iterNo + ") Start Lambda=");
            __DBG_printLambda(lambda, -1, d);
            d.println(__DBG_prefix + "." + iterNo + ") Score: " + ItemTools.evaluate(f, lambda, e));
        }
        int iter = 1;
        while (true) {
            lines = InitializeLines.getLinesWithoutM(f, lambda);
            int chosenIndex = -1;
            LambdaScorePair localBest = null;
            for (int j = 0; j < maxLambdaAlter; j++) {
                assert __DBG_initializeDebugDataPointSearch(f, lambda, e, j);
                InitializeLines.setMOrthogonalDirection(lines, j);
                LambdaScorePair q = OptimumLambdaCalculatorTool.getBestLambda(lines, e);
                if (DEBUG_LEVEL >= 1) d.println(__DBG_prefix + "." + iterNo + "." + iter + "." + j + ") Score by altering lambda on axis (delta=" + StringTools.formatDouble(q.lambda, "0.######") + "): " + q.score);
                if (localBest == null || q.score > localBest.score) {
                    localBest = q;
                    chosenIndex = j;
                }
            }
            if (localBest.score > currentScore + minDeltaScore) {
                lambda[chosenIndex] += localBest.lambda;
                currentScore = localBest.score;
                assert ItemTools.evaluate(f, lambda, e) == currentScore : "different scores: real=" + ItemTools.evaluate(f, lambda, e) + "/reported=" + currentScore + " " + __DBG_printIdx(f, lambda);
                if (DEBUG_LEVEL >= 1) {
                    d.print(__DBG_prefix + "." + iterNo + "." + iter + ") Updated score (idx=" + chosenIndex + ") BLEU=" + currentScore + " Lambda=");
                    __DBG_printLambda(lambda, chosenIndex, d);
                    if (DEBUG_LEVEL >= 2) __DBG_printBestChoice(f, lambda);
                }
                iter++;
            } else {
                if (DEBUG_LEVEL >= 1) d.println(__DBG_prefix + "." + iterNo + "." + iter + ") Failed to find better (" + localBest.score + ")");
                break;
            }
        }
        return currentScore;
    }

    private boolean __DBG_initializeDebugDataPointSearch(ReferenceWithHypotheses[] f, double[] lambda, Evaluator e, int j) {
        OptimumLambdaCalculatorTool.__DEBUG_f = f;
        OptimumLambdaCalculatorTool.__DEBUG_lbd = lambda.clone();
        OptimumLambdaCalculatorTool.__DEBUG_idxLbd = j;
        OptimumLambdaCalculatorTool.DEBUG_ASSERT = true;
        return true;
    }

    public String __DBG_printIdx(ReferenceWithHypotheses[] f, double[] lambda) throws PhramerException {
        d.println("Best choice:");
        Line[][] l = InitializeLines.getLinesWithoutM(f, lambda);
        int[] best = ItemTools.getBestIdx(l);
        for (int i = 0; i < best.length; i++) {
            if (i != 0) d.print(' ');
            d.print("p[" + i + "]=" + best[i]);
        }
        d.println();
        return "";
    }

    private void __DBG_printBestChoice(ReferenceWithHypotheses[] f, double[] lambda) throws PhramerException {
        Line[][] l = InitializeLines.getLinesWithoutM(f, lambda);
        int[] best = ItemTools.getBestIdx(l);
        d.print("Index:");
        for (int i = 0; i < best.length; i++) d.print(" " + best[i]);
        d.println();
    }

    public static void __DBG_printLambda(double[] lambda, int chgIdx, PrintStream log) {
        log.print("(");
        for (int j = 0; j < lambda.length; j++) log.print((j > 0 ? "," : "") + (j == chgIdx ? "*" : "") + lambda[j] + (j == chgIdx ? "*" : ""));
        log.println(")");
    }

    public Object getLastSearchInfo() {
        return null;
    }
}
