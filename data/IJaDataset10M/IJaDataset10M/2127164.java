package com.google.ortools.constraintsolver.samples;

import java.io.*;
import java.util.*;
import java.text.*;
import com.google.ortools.constraintsolver.DecisionBuilder;
import com.google.ortools.constraintsolver.IntVar;
import com.google.ortools.constraintsolver.Solver;

public class ToNum {

    static {
        System.loadLibrary("jniconstraintsolver");
    }

    /**
   *
   *  toNum(solver, a, num, base)
   *
   *  channelling between the array a and the number num
   *
   */
    private static void toNum(Solver solver, IntVar[] a, IntVar num, int base) {
        int len = a.length;
        IntVar[] tmp = new IntVar[len];
        for (int i = 0; i < len; i++) {
            tmp[i] = solver.makeProd(a[i], (int) Math.pow(base, (len - i - 1))).var();
        }
        solver.addConstraint(solver.makeEquality(solver.makeSum(tmp).var(), num));
    }

    /**
   *
   * Implements toNum: channeling between a number and an array.
   * See http://www.hakank.org/google_or_tools/toNum.py
   *
   */
    private static void solve() {
        Solver solver = new Solver("ToNum");
        int n = 5;
        int base = 10;
        IntVar[] x = solver.makeIntVarArray(n, 0, base - 1, "x");
        IntVar num = solver.makeIntVar(0, (int) Math.pow(base, n) - 1, "num");
        solver.addConstraint(solver.makeAllDifferent(x));
        toNum(solver, x, num, base);
        DecisionBuilder db = solver.makePhase(x, solver.CHOOSE_FIRST_UNBOUND, solver.ASSIGN_MIN_VALUE);
        solver.newSearch(db);
        while (solver.nextSolution()) {
            System.out.print("num: " + num.value() + ": ");
            for (int i = 0; i < n; i++) {
                System.out.print(x[i].value() + " ");
            }
            System.out.println();
        }
        solver.endSearch();
        System.out.println();
        System.out.println("Solutions: " + solver.solutions());
        System.out.println("Failures: " + solver.failures());
        System.out.println("Branches: " + solver.branches());
        System.out.println("Wall time: " + solver.wallTime() + "ms");
    }

    public static void main(String[] args) throws Exception {
        ToNum.solve();
    }
}
